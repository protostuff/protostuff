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

    private static <T> Field<T> createCollectionInlineV(int number,
            String name, final java.lang.reflect.Field f,
            final MessageFactory messageFactory, final Delegate<Object> inline)
    {
        return new Field<T>(inline.getFieldType(), number, name, true,
                f.getAnnotation(Tag.class))
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = inline.readFrom(input);
                try
                {
                    final Collection<Object> existing = (Collection<Object>) f
                            .get(message);
                    if (existing == null)
                    {
                        final Collection<Object> collection = messageFactory
                                .newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> collection;
                try
                {
                    collection = (Collection<Object>) f.get(message);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }

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
            final java.lang.reflect.Field f,
            final MessageFactory messageFactory,
            final Class<Object> genericType, IdStrategy strategy)
    {
        final EnumIO<?> eio = strategy.getEnumIO(genericType);
        return new Field<T>(FieldType.ENUM, number, name, true,
                f.getAnnotation(Tag.class))
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Enum<?> value = eio.readFrom(input);
                try
                {
                    final Collection<Enum<?>> existing = (Collection<Enum<?>>) f
                            .get(message);
                    if (existing == null)
                    {
                        final Collection<Enum<?>> collection = messageFactory
                                .newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Enum<?>> collection;
                try
                {
                    collection = (Collection<Enum<?>>) f.get(message);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }

                if (collection != null && !collection.isEmpty())
                {
                    for (Enum<?> en : collection)
                        eio.writeTo(output, number, true, en);
                }
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
        };
    }

    private static <T> Field<T> createCollectionPojoV(int number, String name,
            final java.lang.reflect.Field f,
            final MessageFactory messageFactory,
            final Class<Object> genericType, IdStrategy strategy)
    {
        return new RuntimeMessageField<T, Object>(genericType,
                strategy.getSchemaWrapper(genericType, true),
                FieldType.MESSAGE, number, name, true,
                f.getAnnotation(Tag.class))
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(null, getSchema());
                try
                {
                    final Collection<Object> existing = (Collection<Object>) f
                            .get(message);
                    if (existing == null)
                    {
                        final Collection<Object> collection = messageFactory
                                .newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> collection;
                try
                {
                    collection = (Collection<Object>) f.get(message);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }

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
            String name, final java.lang.reflect.Field f,
            final MessageFactory messageFactory,
            final Class<Object> genericType, IdStrategy strategy)
    {
        return new RuntimeDerivativeField<T>(genericType, FieldType.MESSAGE,
                number, name, true, f.getAnnotation(Tag.class), strategy)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(message, schema);
                if (input instanceof GraphInput
                        && ((GraphInput) input).isCurrentMessageReference())
                {
                    // a reference from polymorphic+cyclic graph deser
                    try
                    {
                        final Collection<Object> existing = (Collection<Object>) f
                                .get(message);
                        if (existing == null)
                        {
                            final Collection<Object> collection = messageFactory
                                    .newMessage();
                            collection.add(value);
                            f.set(message, collection);
                        }
                        else
                            existing.add(value);
                    }
                    catch (IllegalArgumentException | IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing;
                try
                {
                    existing = (Collection<Object>) f.get(message);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }

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
            @SuppressWarnings("unchecked")
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
                try
                {
                    final Collection<Object> existing = (Collection<Object>) f
                            .get(message);
                    if (existing == null)
                    {
                        final Collection<Object> collection = messageFactory
                                .newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static <T> Field<T> createCollectionObjectV(int number,
            String name, final java.lang.reflect.Field f,
            final MessageFactory messageFactory, Class<Object> genericType,
            PolymorphicSchema.Factory factory, IdStrategy strategy)
    {
        return new RuntimeObjectField<T>(genericType, FieldType.MESSAGE,
                number, name, true, f.getAnnotation(Tag.class), factory,
                strategy)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(message, schema);
                if (input instanceof GraphInput
                        && ((GraphInput) input).isCurrentMessageReference())
                {
                    // a reference from polymorphic+cyclic graph deser
                    try
                    {
                        final Collection<Object> existing = (Collection<Object>) f
                                .get(message);
                        if (existing == null)
                        {
                            final Collection<Object> collection = messageFactory
                                    .newMessage();
                            collection.add(value);
                            f.set(message, collection);
                        }
                        else
                            existing.add(value);
                    }
                    catch (IllegalArgumentException | IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing;
                try
                {
                    existing = (Collection<Object>) f.get(message);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }

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
            @SuppressWarnings("unchecked")
            public void setValue(Object value, Object message)
            {
                try
                {
                    final Collection<Object> existing = (Collection<Object>) f
                            .get(message);
                    if (existing == null)
                    {
                        final Collection<Object> collection = messageFactory
                                .newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static final RuntimeFieldFactory<Collection<?>> REPEATED = new RuntimeFieldFactory<Collection<?>>(
            RuntimeFieldFactory.ID_COLLECTION)
    {
        @Override
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            if (null != f.getAnnotation(Morph.class))
            {
                // can be used to override the configured system property:
                // RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS

                // In this context, Morph annotation will force using a
                // collection
                // schema only for this particular field.
                return RuntimeCollectionFieldFactory.getFactory().create(
                        number, name, f, strategy);
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

                return createCollectionEnumV(number, name, f, strategy
                        .getEnumIO(enumType).getEnumSetFactory(), enumType,
                        strategy);
            }

            final MessageFactory messageFactory = strategy
                    .getCollectionFactory(f.getType());

            final Class<Object> genericType = (Class<Object>) getGenericType(f,
                    0);
            if (genericType == null)
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

            if (pojo(genericType, f.getAnnotation(Morph.class), strategy))
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
