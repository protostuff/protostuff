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

import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.getInline;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * Static utility for creating runtime {@link Set} fields.
 *
 * @author David Yu
 * @created Jan 23, 2011
 */
final class RuntimeSetFieldFactory
{
    
    private RuntimeSetFieldFactory() {}
    
    private static <T> Field<T> createSetInlineV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> genericType)
    {
        final RuntimeFieldFactory<Object> inline = getInline(genericType);
        return new Field<T>(inline.getFieldType(), number, name, true)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = inline.readFrom(input);
                try
                {
                    final Set<Object> existing = (Set<Object>)f.get(message);
                    if(existing == null)
                    {
                        final LinkedHashSet<Object> set = new LinkedHashSet<Object>();
                        set.add(value);
                        f.set(message, set);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Set<Object> set;
                try
                {
                    set = (Set<Object>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(set != null && !set.isEmpty())
                {
                    for(Object o : set)
                        inline.writeTo(output, number, o, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                inline.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createSetEnumV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> genericType)
    {
        final EnumIO<?> eio = EnumIO.create(genericType, null);
        return new Field<T>(FieldType.ENUM, number, name, true)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Enum<?> value = eio.readFrom(input);
                try
                {
                    final Set<Enum<?>> existing = (Set<Enum<?>>)f.get(message);
                    if(existing == null)
                    {
                        final LinkedHashSet<Enum<?>> set = new LinkedHashSet<Enum<?>>();
                        set.add(value);
                        f.set(message, set);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Set<Enum<?>> set;
                try
                {
                    set = (Set<Enum<?>>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(set != null && !set.isEmpty())
                {
                    for(Enum<?> en : set)
                        eio.writeTo(output, number, true, en);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                eio.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createSetPojoV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> genericType)
    {
        return new RuntimeMessageField<T,Object>(
                genericType, RuntimeSchema.getSchemaWrapper(genericType), 
                FieldType.MESSAGE, number, name, true)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(null, getSchema());
                try
                {
                    final Set<Object> existing = (Set<Object>)f.get(message);
                    if(existing == null)
                    {
                        final LinkedHashSet<Object> set = new LinkedHashSet<Object>();
                        set.add(value);
                        f.set(message, set);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Set<Object> set;
                try
                {
                    set = (Set<Object>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(set != null && !set.isEmpty())
                {
                    final Schema<Object> schema = getSchema();
                    for(Object o : set)
                        output.writeObject(number, o, schema, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, getPipeSchema(), repeated);
            }
        };
    }
    
    private static <T> Field<T> createSetPolymorphicV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> genericType)
    {
        return new PolymorphicRuntimeField<T>(
                genericType, FieldType.MESSAGE, number, name, true)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(message, schema);
                if(input instanceof GraphInput && 
                        ((GraphInput)input).isCurrentMessageReference())
                {
                    // a reference from polymorphic+cyclic graph deser
                    try
                    {
                        final Set<Object> existing = (Set<Object>)f.get(message);
                        if(existing == null)
                        {
                            final LinkedHashSet<Object> set = new LinkedHashSet<Object>();
                            set.add(value);
                            f.set(message, set);
                        }
                        else
                            existing.add(value);
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Set<Object> existing;
                try
                {
                    existing = (Set<Object>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null && !existing.isEmpty())
                {
                    for(Object o : existing)
                        output.writeObject(number, o, schema, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            @SuppressWarnings("unchecked")
            protected void doMergeFrom(Input input, Schema<Object> schema, 
                    Object message) throws IOException
            {
                final Object value = schema.newMessage();
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(value, message);
                }
                
                schema.mergeFrom(input, value);
                try
                {
                    final Set<Object> existing = (Set<Object>)f.get(message);
                    if(existing == null)
                    {
                        final LinkedHashSet<Object> set = new LinkedHashSet<Object>();
                        set.add(value);
                        f.set(message, set);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    /**
     * Mapped to {@link LinkedHashSet} if the field does not have an existing value.
     */
    static final RuntimeFieldFactory<Set<?>> SET = new RuntimeFieldFactory<Set<?>>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            final Class<Object> genericType;
            try
            {
                genericType = (Class<Object>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
            }
            catch(Exception e)
            {
                return null;
            }
            
            if(genericType.isEnum())
                return createSetEnumV(number, name, f, genericType);
            
            if(getInline(genericType) != null)
                return createSetInlineV(number, name, f, genericType);
            
            if(isInvalidChildType(genericType))
                return null;
            
            if(POJO == pojo(genericType) || RuntimeSchema.isRegistered(genericType))
                return createSetPojoV(number, name, f, genericType);
            
            return createSetPolymorphicV(number, name, f, genericType);
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected Set<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, Set<?> value, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };

}
