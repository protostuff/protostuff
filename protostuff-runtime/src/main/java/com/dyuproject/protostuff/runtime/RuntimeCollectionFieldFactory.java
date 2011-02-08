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

package com.dyuproject.protostuff.runtime;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.CollectionSchema.MessageFactories;
import com.dyuproject.protostuff.CollectionSchema.MessageFactory;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;
import com.dyuproject.protostuff.runtime.RuntimeSchema.HasSchema;

/**
 * Static utility for creating runtime {@link Collection} fields.
 *
 * @author David Yu
 * @created Jan 26, 2011
 */
final class RuntimeCollectionFieldFactory
{
    
    private RuntimeCollectionFieldFactory() {}
    
    /**
     * For lazy initialization called by {@link RuntimeFieldFactory}.
     */
    static RuntimeFieldFactory<Collection<?>> getFactory()
    {
        return COLLECTION;
    }
    
    private static final DerivativeSchema POLYMORPHIC_COLLECTION_VALUE_SCHEMA = 
        new DerivativeSchema()
    {
        @SuppressWarnings("unchecked")
        protected void doMergeFrom(Input input, Schema<Object> derivedSchema, 
                Object owner) throws IOException
        {
            final Object value = derivedSchema.newMessage();
            
            // the owner will always be a Collection
            ((Collection<Object>)owner).add(value);
            
            derivedSchema.mergeFrom(input, value);
        }
    };
    
    private static final ObjectSchema OBJECT_COLLECTION_VALUE_SCHEMA = new ObjectSchema()
    {
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            // the owner will always be a Collection
            ((Collection<Object>)owner).add(value);
        }
    };
    
    private static <T> Field<T> createCollectionInlineV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final RuntimeFieldFactory<Object> inline)
    {
        return new RuntimeCollectionField<T,Object>(
                inline.getFieldType(), 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Collection<Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing;
                try
                {
                    existing = (Collection<Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected void addValueFrom(Input input, Collection<Object> collection) 
            throws IOException
            {
                collection.add(inline.readFrom(input));
            }
            protected void writeValueTo(Output output, int fieldNumber, Object value, 
                    boolean repeated) throws IOException
            {
                inline.writeTo(output, fieldNumber, value, repeated);
            }
            protected void transferValue(Pipe pipe, Input input, Output output, 
                    int number, boolean repeated) throws IOException
            {
                inline.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createCollectionEnumV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory,  
            Class<Object> genericType)
    {
        final EnumIO<?> eio = EnumIO.get(genericType);
        return new RuntimeCollectionField<T,Enum<?>>(
                FieldType.ENUM, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Collection<Enum<?>>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Enum<?>> existing;
                try
                {
                    existing = (Collection<Enum<?>>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected void addValueFrom(Input input, Collection<Enum<?>> collection) 
            throws IOException
            {
                collection.add(eio.readFrom(input));
            }
            protected void writeValueTo(Output output, int fieldNumber, Enum<?> value, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, value);
            }
            protected void transferValue(Pipe pipe, Input input, Output output, 
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createCollectionPojoV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            Class<Object> genericType)
    {
        final HasSchema<Object> schemaV = RuntimeSchema.getSchemaWrapper(genericType);
        return new RuntimeCollectionField<T,Object>(
                FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Collection<Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing;
                try
                {
                    existing = (Collection<Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected void addValueFrom(Input input, Collection<Object> collection) 
            throws IOException
            {
                collection.add(input.mergeObject(null, schemaV.getSchema()));
            }
            protected void writeValueTo(Output output, int fieldNumber, Object value, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, value, schemaV.getSchema(), repeated);
            }
            protected void transferValue(Pipe pipe, Input input, Output output, 
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(), repeated);
            }
        };
    }
    
    private static <T> Field<T> createCollectionPolymorphicV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory,  
            Class<Object> genericType)
    {
        return new RuntimeCollectionField<T,Object>(
                FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Collection<Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing;
                try
                {
                    existing = (Collection<Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected void addValueFrom(Input input, Collection<Object> collection) 
            throws IOException
            {
                final Object value = input.mergeObject(collection, 
                        POLYMORPHIC_COLLECTION_VALUE_SCHEMA);
                
                if(input instanceof GraphInput && 
                        ((GraphInput)input).isCurrentMessageReference())
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(value, collection);
                    
                    collection.add(value);
                }
            }
            protected void writeValueTo(Output output, int fieldNumber, Object value, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, value, 
                        POLYMORPHIC_COLLECTION_VALUE_SCHEMA, repeated);
            }
            protected void transferValue(Pipe pipe, Input input, Output output, 
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, 
                        POLYMORPHIC_COLLECTION_VALUE_SCHEMA.pipeSchema, repeated);
            }
        };
    }
    
    private static <T> Field<T> createCollectionObjectV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory)
    {
        return new RuntimeCollectionField<T,Object>(
                FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Collection<Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing;
                try
                {
                    existing = (Collection<Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected void addValueFrom(Input input, Collection<Object> collection) 
            throws IOException
            {
                final Object value = input.mergeObject(collection, 
                        OBJECT_COLLECTION_VALUE_SCHEMA);
                
                if(input instanceof GraphInput && 
                        ((GraphInput)input).isCurrentMessageReference())
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(value, collection);
                    
                    collection.add(value);
                }
            }
            protected void writeValueTo(Output output, int fieldNumber, Object value, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, value, 
                        OBJECT_COLLECTION_VALUE_SCHEMA, repeated);
            }
            protected void transferValue(Pipe pipe, Input input, Output output, 
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, 
                        OBJECT_COLLECTION_VALUE_SCHEMA.pipeSchema, repeated);
            }
        };
    }
    
    private static final RuntimeFieldFactory<Collection<?>> COLLECTION = new RuntimeFieldFactory<Collection<?>>(RuntimeFieldFactory.ID_COLLECTION)
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            final MessageFactory messageFactory = MessageFactories.getFactory(
                    (Class<? extends Collection<?>>)f.getType());
            
            if(messageFactory == null)
            {
                // Not a standard jdk Collection impl.
                return null;
            }
            
            final Class<Object> genericType;
            try
            {
                genericType = (Class<Object>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
            }
            catch(Exception e)
            {
                // the value is not a simple parameterized type.
                return createCollectionObjectV(number, name, f, messageFactory);
            }
            
            if(isComplexComponentType(genericType))
                return createCollectionObjectV(number, name, f, messageFactory);
            
            if(genericType.isEnum())
                return createCollectionEnumV(number, name, f, messageFactory, genericType);
            
            final RuntimeFieldFactory<Object> inline = getInline(genericType);
            if(inline != null)
                return createCollectionInlineV(number, name, f, messageFactory, inline);
            
            if(POJO == pojo(genericType) || RuntimeSchema.isRegistered(genericType))
                return createCollectionPojoV(number, name, f, messageFactory, genericType);
            
            return createCollectionPolymorphicV(number, name, f, messageFactory, genericType);
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected Collection<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, Collection<?> value, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
        protected Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };

}
