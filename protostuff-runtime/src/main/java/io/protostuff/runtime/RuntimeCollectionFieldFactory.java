//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

import io.protostuff.CollectionSchema.MessageFactory;
import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Morph;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;

/**
 * Static utility for creating runtime {@link Collection} fields.
 * 
 * @author David Yu
 * @created Jan 26, 2011
 */
final class RuntimeCollectionFieldFactory
{

    private RuntimeCollectionFieldFactory()
    {
    }

    /**
     * For lazy initialization called by {@link RuntimeFieldFactory}.
     */
    static RuntimeFieldFactory<Collection<?>> getFactory()
    {
        return COLLECTION;
    }
    
    static final Accessor.Factory AF = RuntimeFieldFactory.ACCESSOR_FACTORY;

    /*
     * private static final DerivativeSchema POLYMORPHIC_COLLECTION_VALUE_SCHEMA = new DerivativeSchema() {
     * 
     * @SuppressWarnings("unchecked") protected void doMergeFrom(Input input, Schema<Object> derivedSchema, Object
     * owner) throws IOException { final Object value = derivedSchema.newMessage();
     * 
     * // the owner will always be a Collection ((Collection<Object>)owner).add(value);
     * 
     * if(input instanceof GraphInput) { // update the actual reference. ((GraphInput)input).updateLast(value, owner); }
     * 
     * derivedSchema.mergeFrom(input, value); } };
     */

    /*
     * private static final ObjectSchema OBJECT_COLLECTION_VALUE_SCHEMA = new ObjectSchema() {
     * 
     * @SuppressWarnings("unchecked") protected void setValue(Object value, Object owner) { // the owner will always be
     * a Collection ((Collection<Object>)owner).add(value); } };
     */

    private static <T> Field<T> createCollectionInlineV(int number,
            String name, java.lang.reflect.Field f,
            MessageFactory messageFactory, final Delegate<Object> inline)
    {
        final Accessor accessor = AF.create(f);
        return new RuntimeCollectionField<T, Object>(inline.getFieldType(),
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                accessor.set(message, input.mergeObject(
                        accessor.<Collection<Object>>get(message), schema));
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing = accessor.get(message);
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
            protected void addValueFrom(Input input,
                    Collection<Object> collection) throws IOException
            {
                collection.add(inline.readFrom(input));
            }

            @Override
            protected void writeValueTo(Output output, int fieldNumber,
                    Object value, boolean repeated) throws IOException
            {
                inline.writeTo(output, fieldNumber, value, repeated);
            }

            @Override
            protected void transferValue(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inline.transfer(pipe, input, output, number, repeated);
            }
        };
    }

    private static <T> Field<T> createCollectionEnumV(int number, String name,
            java.lang.reflect.Field f, MessageFactory messageFactory,
            Class<Object> genericType, final IdStrategy strategy)
    {
        final EnumIO<?> eio = strategy.getEnumIO(genericType);
        final Accessor accessor = AF.create(f);
        return new RuntimeCollectionField<T, Enum<?>>(FieldType.ENUM, number,
                name, f.getAnnotation(Tag.class), messageFactory)
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                accessor.set(message, input.mergeObject(
                        accessor.<Collection<Enum<?>>>get(message), schema));
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Enum<?>> existing = accessor.get(message);
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
            protected void addValueFrom(Input input,
                    Collection<Enum<?>> collection) throws IOException
            {
                collection.add(eio.readFrom(input));
            }

            @Override
            protected void writeValueTo(Output output, int fieldNumber,
                    Enum<?> value, boolean repeated) throws IOException
            {
                eio.writeTo(output, fieldNumber, repeated, value);
            }

            @Override
            protected void transferValue(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }
        };
    }

    private static <T> Field<T> createCollectionPojoV(int number, String name,
            java.lang.reflect.Field f, MessageFactory messageFactory,
            Class<Object> genericType, IdStrategy strategy)
    {
        final HasSchema<Object> schemaV = strategy.getSchemaWrapper(
                genericType, true);
        final Accessor accessor = AF.create(f);
        return new RuntimeCollectionField<T, Object>(FieldType.MESSAGE, number,
                name, f.getAnnotation(Tag.class), messageFactory)
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                accessor.set(message, input.mergeObject(
                        accessor.<Collection<Object>>get(message), schema));
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing = accessor.get(message);
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
            protected void addValueFrom(Input input,
                    Collection<Object> collection) throws IOException
            {
                collection.add(input.mergeObject(null, schemaV.getSchema()));
            }

            @Override
            protected void writeValueTo(Output output, int fieldNumber,
                    Object value, boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, value, schemaV.getSchema(),
                        repeated);
            }

            @Override
            protected void transferValue(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(),
                        repeated);
            }
        };
    }

    private static <T> Field<T> createCollectionPolymorphicV(int number,
            String name, java.lang.reflect.Field f,
            MessageFactory messageFactory, Class<Object> genericType,
            final IdStrategy strategy)
    {
        final Accessor accessor = AF.create(f);
        return new RuntimeCollectionField<T, Object>(FieldType.MESSAGE, number,
                name, f.getAnnotation(Tag.class), messageFactory)
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                accessor.set(message, input.mergeObject(
                        accessor.<Collection<Object>>get(message), schema));
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing = accessor.get(message);
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
            protected void addValueFrom(Input input,
                    Collection<Object> collection) throws IOException
            {
                final Object value = input.mergeObject(collection,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA);

                if (input instanceof GraphInput
                        && ((GraphInput) input).isCurrentMessageReference())
                {
                    collection.add(value);
                }
            }

            @Override
            protected void writeValueTo(Output output, int fieldNumber,
                    Object value, boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, value,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA, repeated);
            }

            @Override
            protected void transferValue(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA.pipeSchema,
                        repeated);
            }
        };
    }

    private static <T> Field<T> createCollectionObjectV(int number,
            String name, java.lang.reflect.Field f,
            MessageFactory messageFactory, final Schema<Object> valueSchema,
            final Pipe.Schema<Object> valuePipeSchema, final IdStrategy strategy)
    {
        final Accessor accessor = AF.create(f);
        return new RuntimeCollectionField<T, Object>(FieldType.MESSAGE, number,
                name, f.getAnnotation(Tag.class), messageFactory)
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                accessor.set(message, input.mergeObject(
                        accessor.<Collection<Object>>get(message), schema));
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing = accessor.get(message);
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
            protected void addValueFrom(Input input,
                    Collection<Object> collection) throws IOException
            {
                final Object value = input.mergeObject(collection, valueSchema);

                if (input instanceof GraphInput
                        && ((GraphInput) input).isCurrentMessageReference())
                {
                    collection.add(value);
                }
            }

            @Override
            protected void writeValueTo(Output output, int fieldNumber,
                    Object value, boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, value, valueSchema, repeated);
            }

            @Override
            protected void transferValue(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, valuePipeSchema, repeated);
            }
        };
    }

    private static final RuntimeFieldFactory<Collection<?>> COLLECTION = new RuntimeFieldFactory<Collection<?>>(
            RuntimeFieldFactory.ID_COLLECTION)
    {
        @Override
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name,
                java.lang.reflect.Field f, IdStrategy strategy)
        {
            final Class<?> clazz = f.getType();
            final Morph morph = f.getAnnotation(Morph.class);
            
            if (0 != (IdStrategy.POJO_SCHEMA_ON_COLLECTION_FIELDS & strategy.flags) && 
                    (morph == null || morph.value()))
            {
                if (!clazz.getName().startsWith("java.util") && 
                        pojo(clazz, morph, strategy))
                {
                    return POJO.create(number, name, f, strategy);
                }
                
                return OBJECT.create(number, name, f, strategy);
            }
            
            if (Modifier.isAbstract(clazz.getModifiers()))
            {
                if (!clazz.isInterface())
                {
                    // abstract class
                    return OBJECT.create(number, name, f, strategy);
                }
                
                if (morph == null)
                {
                    if (0 != (IdStrategy.MORPH_COLLECTION_INTERFACES & strategy.flags))
                        return OBJECT.create(number, name, f, strategy);
                }
                else if (morph.value())
                    return OBJECT.create(number, name, f, strategy);
            }

            if (EnumSet.class.isAssignableFrom(f.getType()))
            {
                final Class<Object> enumType = (Class<Object>) getGenericType(
                        f, 0);
                if (enumType == null)
                {
                    // still handle the serialization of EnumSets even without
                    // generics
                    return RuntimeFieldFactory.OBJECT.create(number, name, f,
                            strategy);
                }

                // TODO optimize
                return createCollectionEnumV(number, name, f, strategy
                        .getEnumIO(enumType).getEnumSetFactory(), enumType,
                        strategy);
            }

            final MessageFactory messageFactory = strategy
                    .getCollectionFactory(f.getType());

            final Class<Object> genericType = (Class<Object>) getGenericType(f,
                    0);
            if (genericType == null || ((Map.class.isAssignableFrom(genericType) || 
                    Collection.class.isAssignableFrom(genericType)) && 
                    !strategy.isRegistered(genericType)))
            {
                // the value is not a simple parameterized type.
                return createCollectionObjectV(number, name, f, messageFactory,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }

            final Delegate<Object> inline = getDelegateOrInline(genericType,
                    strategy);
            if (inline != null)
                return createCollectionInlineV(number, name, f, messageFactory,
                        inline);

            if (Message.class.isAssignableFrom(genericType))
                return createCollectionPojoV(number, name, f, messageFactory,
                        genericType, strategy);

            if (genericType.isEnum())
                return createCollectionEnumV(number, name, f, messageFactory,
                        genericType, strategy);

            final PolymorphicSchema ps = PolymorphicSchemaFactories
                    .getSchemaFromCollectionOrMapGenericType(genericType,
                            strategy);
            if (ps != null)
            {
                return createCollectionObjectV(number, name, f, messageFactory,
                        ps, ps.getPipeSchema(), strategy);
            }

            if (pojo(genericType, morph, strategy))
                return createCollectionPojoV(number, name, f, messageFactory,
                        genericType, strategy);

            if (genericType.isInterface())
            {
                return createCollectionObjectV(number, name, f, messageFactory,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }

            return createCollectionPolymorphicV(number, name, f,
                    messageFactory, genericType, strategy);
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Collection<?> value,
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
