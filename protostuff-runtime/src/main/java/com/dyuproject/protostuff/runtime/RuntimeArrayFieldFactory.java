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
import java.lang.reflect.Array;

import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * Static utility for creating runtime array fields.
 * 
 * The length of the array fields is not known in advance on deserialization, w/c 
 * will trigger creating a new array for every component. It is best to avoid arrays.
 *
 * @author David Yu
 * @created Jan 23, 2011
 */
final class RuntimeArrayFieldFactory
{
    
    private RuntimeArrayFieldFactory() {}
    
    private static <T> Field<T> createArrayInlineV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> componentType)
    {
        final RuntimeFieldFactory<Object> inline = getInline(componentType);
        return new Field<T>(inline.getFieldType(), number, name, true)
        {
            {
                f.setAccessible(true);
            }
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = inline.readFrom(input);
                try
                {
                    // the length is not known in advance ... better to avoid arrays.
                    final Object existing = f.get(message);
                    if(existing == null)
                    {
                        final Object newArray = Array.newInstance(componentType, 1);
                        Array.set(newArray, 0, value);
                        f.set(message, newArray);
                    }
                    else
                    {
                        final int len = Array.getLength(existing);
                        final Object newArray = Array.newInstance(componentType, len+1);
                        System.arraycopy(existing, 0, newArray, 0, len);
                        Array.set(newArray, len, value);
                        f.set(message, newArray);
                    }
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
            protected void writeTo(Output output, T message) throws IOException
            {
                final Object array;
                try
                {
                    array = f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(array != null)
                {
                    for(int i=0, len=Array.getLength(array); i<len; i++)
                        inline.writeTo(output, number, Array.get(array, i), true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                inline.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createArrayEnumV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> componentType)
    {
        final EnumIO<?> eio = EnumIO.create(componentType, null);
        return new Field<T>(FieldType.ENUM, number, name, true)
        {
            {
                f.setAccessible(true);
            }
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Enum<?> value = eio.readFrom(input);
                try
                {
                    // the length is not known in advance ... better to avoid arrays.
                    final Object existing = f.get(message);
                    if(existing == null)
                    {
                        final Object newArray = Array.newInstance(componentType, 1);
                        Array.set(newArray, 0, value);
                        f.set(message, newArray);
                    }
                    else
                    {
                        final int len = Array.getLength(existing);
                        final Object newArray = Array.newInstance(componentType, len+1);
                        System.arraycopy(existing, 0, newArray, 0, len);
                        Array.set(newArray, len, value);
                        f.set(message, newArray);
                    }
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
            protected void writeTo(Output output, T message) throws IOException
            {
                final Object array;
                try
                {
                    array = f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(array != null)
                {
                    for(int i=0, len=Array.getLength(array); i<len; i++)
                    {
                        eio.writeTo(output, number, repeated, 
                                (Enum<?>)Array.get(array, i));
                    }
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                eio.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createArrayPojoV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> componentType)
    {
        return new RuntimeMessageField<T, Object>(
                componentType, RuntimeSchema.getSchemaWrapper(componentType), 
                FieldType.MESSAGE, number, name, true)
        {
            
            {
                f.setAccessible(true);
            }
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(null, getSchema());
                try
                {
                    // the length is not known in advance ... better to avoid arrays.
                    final Object existing = f.get(message);
                    if(existing == null)
                    {
                        final Object newArray = Array.newInstance(componentType, 1);
                        Array.set(newArray, 0, value);
                        f.set(message, newArray);
                    }
                    else
                    {
                        final int len = Array.getLength(existing);
                        final Object newArray = Array.newInstance(componentType, len+1);
                        System.arraycopy(existing, 0, newArray, 0, len);
                        Array.set(newArray, len, value);
                        f.set(message, newArray);
                    }
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
            protected void writeTo(Output output, T message) throws IOException
            {
                final Object array;
                try
                {
                    array = f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(array != null)
                {
                    final Schema<Object> schema = getSchema();
                    for(int i=0, len=Array.getLength(array); i<len; i++)
                        output.writeObject(number, Array.get(array, i), schema, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, getPipeSchema(), repeated);
            }
        };
    }
    
    private static <T> Field<T> createArrayPolymorphicV(int number, String name, 
            final java.lang.reflect.Field f, final Class<Object> componentType)
    {
        return new PolymorphicRuntimeField<T>(
                componentType, FieldType.MESSAGE, number, name, true)
        {
            {
                f.setAccessible(true);
            }
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(message, schema);
                
                if(input instanceof GraphInput && 
                        ((GraphInput)input).isCurrentMessageReference())
                {
                    // a reference from polymorphic+cyclic graph deser
                    try
                    {
                        final Object existing = f.get(message);
                        if(existing == null)
                        {
                            final Object newArray = Array.newInstance(componentType, 1);
                            Array.set(newArray, 0, value);
                            f.set(message, newArray);
                        }
                        else
                        {
                            final int len = Array.getLength(existing);
                            final Object newArray = Array.newInstance(componentType, len+1);
                            System.arraycopy(existing, 0, newArray, 0, len);
                            Array.set(newArray, len, value);
                            f.set(message, newArray);
                        }
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
            protected void writeTo(Output output, T message) throws IOException
            {
                final Object array;
                try
                {
                    array = f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(array != null)
                {
                    for(int i=0, len=Array.getLength(array); i<len; i++)
                        output.writeObject(number, Array.get(array, i), schema, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
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
                    final Object existing = f.get(message);
                    if(existing == null)
                    {
                        final Object newArray = Array.newInstance(componentType, 1);
                        Array.set(newArray, 0, value);
                        f.set(message, newArray);
                    }
                    else
                    {
                        final int len = Array.getLength(existing);
                        final Object newArray = Array.newInstance(componentType, len+1);
                        System.arraycopy(existing, 0, newArray, 0, len);
                        Array.set(newArray, len, value);
                        f.set(message, newArray);
                    }
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
    
    static final RuntimeFieldFactory<Object> ARRAY = new RuntimeFieldFactory<Object>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            final Class<Object> componentType = (Class<Object>)f.getType().getComponentType();
            
            // no two dimensional arrays.
            if(componentType.isArray())
                return null;
            
            if(componentType.isEnum())
                return createArrayEnumV(number, name, f, componentType);
            
            if(getInline(componentType) != null)
                return createArrayInlineV(number, name, f, componentType);
            
            if(isInvalidChildType(componentType))
                return null;
            
            if(POJO == pojo(componentType) || RuntimeSchema.isRegistered(componentType))
                return createArrayPojoV(number, name, f, componentType);
            
            return createArrayPolymorphicV(number, name, f, componentType);
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, Object value, 
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
