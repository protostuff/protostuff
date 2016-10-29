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
 * Static utility for creating runtime repeated (list/collection) fields.
 * 
 * @author David Yu
 * @created Jan 23, 2011
 */
final class RuntimeRepeatedFieldFactory
{

    private RuntimeRepeatedFieldFactory()
    {
    }

    /**
     * For lazy initialization called by {@link RuntimeFieldFactory}.
     */
    static RuntimeFieldFactory<Collection<?>> getFactory()
    {
        return REPEATED;
    }
    
    static final Accessor.Factory AF = RuntimeFieldFactory.ACCESSOR_FACTORY;

    private static <T> Field<T> createCollectionInlineV(int number,
            String name, java.lang.reflect.Field f,
            final MessageFactory messageFactory, final Delegate<Object> inline)
    {
        final Accessor accessor = AF.create(f);
        return new Field<T>(inline.getFieldType(), number, name, true,
                f.getAnnotation(Tag.class))
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = inline.readFrom(input);
                Collection<Object> existing = accessor.get(message);
                if (existing == null)
                    accessor.set(message, existing = messageFactory.newMessage());
                
                existing.add(value);
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> collection = accessor.get(message);
                if (collection != null && !collection.isEmpty())
                {
                    for (Object o : collection)
                    {
                        if (o != null)
                            inline.writeTo(output, number, o, true);
                    }
                }
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                inline.transfer(pipe, input, output, number, repeated);
            }
        };
    }

    private static <T> Field<T> createCollectionEnumV(int number, String name,
            java.lang.reflect.Field f,
            final MessageFactory messageFactory,
            final Class<Object> genericType, 
            final IdStrategy strategy)
    {
        final EnumIO<?> eio = strategy.getEnumIO(genericType);
        final Accessor accessor = AF.create(f);
        return new Field<T>(FieldType.ENUM, number, name, true,
                f.getAnnotation(Tag.class))
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Enum<?> value = eio.readFrom(input);
                Collection<Enum<?>> existing = accessor.get(message);
                if (existing == null)
                    accessor.set(message, existing = messageFactory.newMessage());
                
                existing.add(value);
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Enum<?>> collection = accessor.get(message);
                if (collection != null && !collection.isEmpty())
                {
                    for (Enum<?> en : collection)
                    {
                        if (en != null)
                            eio.writeTo(output, number, true, en);
                    }
                }
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }
        };
    }

    private static <T> Field<T> createCollectionPojoV(int number, String name,
            java.lang.reflect.Field f,
            final MessageFactory messageFactory,
            final Class<Object> genericType, IdStrategy strategy)
    {
        final Accessor accessor = AF.create(f);
        return new RuntimeMessageField<T, Object>(genericType,
                strategy.getSchemaWrapper(genericType, true),
                FieldType.MESSAGE, number, name, true,
                f.getAnnotation(Tag.class))
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(null, getSchema());
                Collection<Object> existing = accessor.get(message);
                if (existing == null)
                    accessor.set(message, existing = messageFactory.newMessage());
                
                existing.add(value);
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> collection = accessor.get(message);
                if (collection != null && !collection.isEmpty())
                {
                    final Schema<Object> schema = getSchema();
                    for (Object o : collection)
                    {
                        if (o != null)
                            output.writeObject(number, o, schema, true);
                    }
                }
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, getPipeSchema(), repeated);
            }
        };
    }

    private static <T> Field<T> createCollectionPolymorphicV(int number,
            String name, java.lang.reflect.Field f,
            final MessageFactory messageFactory,
            final Class<Object> genericType, IdStrategy strategy)
    {
        final Accessor accessor = AF.create(f);
        return new RuntimeDerivativeField<T>(genericType, FieldType.MESSAGE,
                number, name, true, f.getAnnotation(Tag.class), strategy)
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(message, schema);
                if (input instanceof GraphInput
                        && ((GraphInput) input).isCurrentMessageReference())
                {
                    // a reference from polymorphic+cyclic graph deser
                    Collection<Object> existing = accessor.get(message);
                    if (existing == null)
                        accessor.set(message, existing = messageFactory.newMessage());
                    
                    existing.add(value);
                }
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing = accessor.get(message);
                if (existing != null && !existing.isEmpty())
                {
                    for (Object o : existing)
                    {
                        if (o != null)
                            output.writeObject(number, o, schema, true);
                    }
                }
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected void doMergeFrom(Input input, Schema<Object> schema,
                    Object message) throws IOException
            {
                final Object value = schema.newMessage();
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(value, message);
                }

                schema.mergeFrom(input, value);
                Collection<Object> existing = accessor.get(message);
                if (existing == null)
                    accessor.set(message, existing = messageFactory.newMessage());
                
                existing.add(value);
            }
        };
    }

    private static <T> Field<T> createCollectionObjectV(int number,
            String name, java.lang.reflect.Field f,
            final MessageFactory messageFactory, Class<Object> genericType,
            PolymorphicSchema.Factory factory, IdStrategy strategy)
    {
        final Accessor accessor = AF.create(f);
        return new RuntimeObjectField<T>(genericType, FieldType.MESSAGE,
                number, name, true, f.getAnnotation(Tag.class), factory,
                strategy)
        {
            @Override
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(message, schema);
                if (input instanceof GraphInput
                        && ((GraphInput) input).isCurrentMessageReference())
                {
                    // a reference from polymorphic+cyclic graph deser
                    Collection<Object> existing = accessor.get(message);
                    if (existing == null)
                        accessor.set(message, existing = messageFactory.newMessage());
                    
                    existing.add(value);
                }
            }

            @Override
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing = accessor.get(message);
                if (existing != null && !existing.isEmpty())
                {
                    for (Object o : existing)
                    {
                        if (o != null)
                            output.writeObject(number, o, schema, true);
                    }
                }
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.getPipeSchema(),
                        repeated);
            }

            @Override
            public void setValue(Object value, Object message)
            {
                Collection<Object> existing = accessor.get(message);
                if (existing == null)
                    accessor.set(message, existing = messageFactory.newMessage());
                
                existing.add(value);
            }
        };
    }

    private static final RuntimeFieldFactory<Collection<?>> REPEATED = new RuntimeFieldFactory<Collection<?>>(
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
            
            if (morph != null)
            {
                // can be used to override the configured system property:
                // RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS

                // In this context, Morph annotation will force using a
                // collection
                // schema only for this particular field.
                return RuntimeCollectionFieldFactory.getFactory().create(
                        number, name, f, strategy);
            }

            if (EnumSet.class.isAssignableFrom(clazz))
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

                return createCollectionEnumV(number, name, f, strategy
                        .getEnumIO(enumType).getEnumSetFactory(), enumType,
                        strategy);
            }

            final MessageFactory messageFactory = strategy
                    .getCollectionFactory(clazz);

            final Class<Object> genericType = (Class<Object>) getGenericType(f,
                    0);
            if (genericType == null || ((Map.class.isAssignableFrom(genericType) || 
                    Collection.class.isAssignableFrom(genericType)) && 
                    !strategy.isRegistered(genericType)))
            {
                // the value is not a simple parameterized type.
                return createCollectionObjectV(number, name, f, messageFactory,
                        genericType, PolymorphicSchemaFactories.OBJECT,
                        strategy);
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

            final PolymorphicSchema.Factory factory = PolymorphicSchemaFactories
                    .getFactoryFromRepeatedValueGenericType(genericType);
            if (factory != null)
            {
                return createCollectionObjectV(number, name, f, messageFactory,
                        genericType, factory, strategy);
            }

            if (pojo(genericType, morph, strategy))
                return createCollectionPojoV(number, name, f, messageFactory,
                        genericType, strategy);

            if (genericType.isInterface())
            {
                return createCollectionObjectV(number, name, f, messageFactory,
                        genericType, PolymorphicSchemaFactories.OBJECT,
                        strategy);
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
