//========================================================================
//Copyright 2007-2020 Mr14huashao Mr11huashao@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package io.protostuff.runtime;

import com.google.common.collect.Multimap;
import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Morph;
import io.protostuff.runtime.MultiMapSchema.MessageFactory;
import io.protostuff.runtime.MultiMapSchema.MultiMapWrapper;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 * Static utility for creating runtime {@link Multimap} fields.
 *
 * @author Mr14huashao
 * @created Otc 09, 2020
 */
final class RuntimeMultiMapFieldFactory
{

    private RuntimeMultiMapFieldFactory()
    {
    }

    static final Accessor.Factory AF = RuntimeFieldFactory.ACCESSOR_FACTORY;

    private static <T> Field<T> createMapObjectKObjectV(int number,
            String name, final java.lang.reflect.Field f,
            final MessageFactory messageFactory, final Schema<Object> keySchema,
            final Pipe.Schema<Object> keyPipeSchema,
            final Schema<Object> valueSchema,
            final Pipe.Schema<Object> valuePipeSchema, final IdStrategy strategy)
    {
        return new RuntimeMultiMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            final Accessor accessor = AF.create(f);

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                accessor.set(message, input.mergeObject(
                        (Multimap<Object, Object>)
                                accessor.get(message), schema));
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Multimap<Object, Object> existing = accessor.get(message);

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                                   MultiMapWrapper<Object, Object> wrapper)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper, keySchema);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);
                    return value;
                }

                return wrapper.setValue(null);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                                     int number, boolean repeated)
                    throws IOException
            {
                output.writeObject(number, pipe, keyPipeSchema, repeated);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                               boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, keySchema, repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                                    MultiMapWrapper<Object, Object> wrapper,
                                    Object key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, valueSchema);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                               boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, valueSchema, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                                     int number, boolean repeated)
                    throws IOException
            {
                output.writeObject(number, pipe, valuePipeSchema, repeated);
            }

            @Override
            protected Field<T> copy(IdStrategy strategy)
            {
                return createMapObjectKObjectV(number, name, f, messageFactory,
                        keySchema, keyPipeSchema, valueSchema, valuePipeSchema,
                        strategy);
            }
        };
    }

    static final RuntimeFieldFactory<Multimap<?, ?>> MAP = new RuntimeFieldFactory
            <Multimap<?, ?>>(RuntimeFieldFactory.ID_MULTIMAP)
    {

        @Override
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name,
                                   final java.lang.reflect.Field f,
                                   IdStrategy strategy)
        {
            final Class<?> clazz = f.getType();
            final Morph morph = f.getAnnotation(Morph.class);

            if (0 != (IdStrategy.POJO_SCHEMA_ON_MAP_FIELDS & strategy.flags) &&
                    (morph == null || morph.value()))
            {
                if (!clazz.getName().startsWith("java.util") &&
                        pojo(clazz, morph, strategy))
                {
                    return POJO.create(number, name, f, strategy);
                }

                return OBJECT.create(number, name, f, strategy);
            }

            final MessageFactory messageFactory;
            messageFactory = strategy.getMultiMapFactory(f.getType());
            final Class<Object> clazzK = (Class<Object>) getGenericType(f, 0);
            if (clazzK == null ||
                    2 != ((ParameterizedType)f.getGenericType()).
                            getActualTypeArguments().length
                    || ((Multimap.class.isAssignableFrom(clazzK)
                    || Collection.class.isAssignableFrom(clazzK))
                    && !strategy.isRegistered(clazzK)))
            {
                // the key is not a simple parameterized type.
                return createMapObjectKObjectV(number, name, f, messageFactory,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }

            final Class<Object> clazzV = (Class<Object>) getGenericType(f, 1);
            if (clazzV == null || ((Multimap.class.isAssignableFrom(clazzV) ||
                    Collection.class.isAssignableFrom(clazzV)) &&
                    !strategy.isRegistered(clazzV)))
            {
                // the value is not a simple parameterized type.

                final PolymorphicSchema psK = PolymorphicSchemaFactories
                        .getSchemaFromCollectionOrMapGenericType(clazzK,
                                strategy);

                if (psK != null)
                {
                    return createMapObjectKObjectV(number, name, f,
                            messageFactory, psK, psK.getPipeSchema(),
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                return createMapObjectKObjectV(number, name, f, messageFactory,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }


            final PolymorphicSchema psK = PolymorphicSchemaFactories
                    .getSchemaFromCollectionOrMapGenericType(clazzK, strategy);
            if (psK != null)
            {
                return createMapObjectKObjectV(number, name, f, messageFactory,
                        psK, psK.getPipeSchema(),
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }

            return createMapObjectKObjectV(number, name, f, messageFactory,
                    strategy.OBJECT_ELEMENT_SCHEMA,
                    strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema,
                    strategy.OBJECT_ELEMENT_SCHEMA,
                    strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                             boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Multimap<?, ?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Multimap<?, ?> value,
                            boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };

}
