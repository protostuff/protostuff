//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * A factory to create runtime {@link MappedSchema.Field fields} based on reflection.
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class RuntimeFieldFactory<V>
{
    
    /**
     * Creates a runtime {@link MappedSchema.Field field} based on reflection.
     */
    public abstract <T> Field<T> create(int number, java.lang.String name, 
            java.lang.reflect.Field field);
    
    public abstract V readFrom(Input input) throws IOException;
    
    public abstract void writeTo(Output output, int number, V value, boolean repeated) 
    throws IOException;
    
    public abstract FieldType getFieldType();
    
    /**
     * Gets the runtime field factory of the given {@code clazz}.
     */
    public static RuntimeFieldFactory<?> getFieldFactory(Class<?> clazz)
    {
        if(Message.class.isAssignableFrom(clazz))
            return MESSAGE;
        
        // maps are excluded
        if(Map.class.isAssignableFrom(clazz))
            return null;
        
        if(clazz.isEnum())
            return ENUM;
        
        if(byte[].class == clazz)
            return BYTE_ARRAY;
        
        if(clazz.isArray())
            return ARRAY;

        if(List.class.isAssignableFrom(clazz))
            return LIST;
        
        if(Set.class.isAssignableFrom(clazz))
            return SET;
        
        RuntimeFieldFactory<?> inline =  __inlineValues.get(clazz.getName());
        return inline==null ? POJO : inline;
    }
    
    public static final RuntimeFieldFactory<Character> CHAR = new RuntimeFieldFactory<Character>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.INT32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setChar(message, (char)input.readInt32());
                        else
                            f.set(message, new Character((char)input.readInt32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeInt32(this.number, f.getChar(message), false);
                        else
                        {
                            Character value = (Character)f.get(message);
                            if(value!=null)
                                output.writeInt32(this.number, value.charValue(), false);
                        }
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
            };
        }
        public Character readFrom(Input input) throws IOException
        {
            return new Character((char)input.readInt32());
        }
        public void writeTo(Output output, int number, Character value, boolean repeated) 
        throws IOException
        {
            output.writeInt32(number, value.charValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.INT32;
        }
    };
    
    public static final RuntimeFieldFactory<Short> SHORT = new RuntimeFieldFactory<Short>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.INT32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setShort(message, (short)input.readInt32());
                        else
                            f.set(message, new Short((short)input.readInt32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeInt32(this.number, f.getShort(message), false);
                        else
                        {
                            Short value = (Short)f.get(message);
                            if(value!=null)
                                output.writeInt32(this.number, value.shortValue(), false);
                        }
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
            };
        }
        public Short readFrom(Input input) throws IOException
        {
            return new Short((short)input.readInt32());
        }
        public void writeTo(Output output, int number, Short value, boolean repeated) 
        throws IOException
        {
            output.writeInt32(number, value.shortValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.INT32;
        }
    };
    
    public static final RuntimeFieldFactory<Byte> BYTE = new RuntimeFieldFactory<Byte>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.INT32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setByte(message, (byte)input.readInt32());
                        else
                            f.set(message, new Byte((byte)input.readInt32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeInt32(this.number, f.getByte(message), false);
                        else
                        {
                            Byte value = (Byte)f.get(message);
                            if(value!=null)
                                output.writeInt32(this.number, value.byteValue(), false);
                        }
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
            };
        }
        public Byte readFrom(Input input) throws IOException
        {
            return new Byte((byte)input.readInt32());
        }
        public void writeTo(Output output, int number, Byte value, boolean repeated) throws IOException
        {
            output.writeInt32(number, value.byteValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.INT32;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> INT32 = new RuntimeFieldFactory<Integer>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.INT32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setInt(message, input.readInt32());
                        else
                            f.set(message, new Integer(input.readInt32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeInt32(this.number, f.getInt(message), false);
                        else
                        {
                            Integer value = (Integer)f.get(message);
                            if(value!=null)
                                output.writeInt32(this.number, value.intValue(), false);
                        }
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
            };
        }
        public Integer readFrom(Input input) throws IOException
        {
            return new Integer(input.readInt32());
        }
        public void writeTo(Output output, int number, Integer value, boolean repeated) 
        throws IOException
        {
            output.writeInt32(number, value.intValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.INT32;
        }
    };
    
    /*public static final RuntimeFieldFactory<Integer> UINT32 = new RuntimeFieldFactory<Integer>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.UINT32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setInt(message, input.readUInt32());
                        else
                            f.setInt(message, new Integer(input.readUInt32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeUInt32(this.number, f.getInt(message));
                        else
                        {
                            Integer value = (Integer)f.get(message);
                            if(value!=null)
                                output.writeUInt32(this.number, value.intValue());
                        }
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
            };
        }
        public Integer readFrom(Input input) throws IOException
        {
            return new Integer(input.readUInt32());
        }
        public void writeTo(Output output, int number, Integer value) throws IOException
        {
            output.writeUInt32(number, value.intValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> SINT32 = new RuntimeFieldFactory<Integer>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.SINT32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setInt(message, input.readSInt32());
                        else
                            f.setInt(message, new Integer(input.readSInt32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeSInt32(this.number, f.getInt(message));
                        else
                        {
                            Integer value = (Integer)f.get(message);
                            if(value!=null)
                                output.writeSInt32(this.number, value.intValue());
                        }
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
            };
        }
        public Integer readFrom(Input input) throws IOException
        {
            return new Integer(input.readSInt32());
        }
        public void writeTo(Output output, int number, Integer value) throws IOException
        {
            output.writeSInt32(number, value.intValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.SINT32;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> FIXED32 = new RuntimeFieldFactory<Integer>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.FIXED32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setInt(message, input.readFixed32());
                        else
                            f.set(message, new Integer(input.readFixed32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeFixed32(this.number, f.getInt(message));
                        else
                        {
                            Integer value = (Integer)f.get(message);
                            if(value!=null)
                                output.writeFixed32(this.number, value.intValue());
                        }
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
            };
        }
        public Integer readFrom(Input input) throws IOException
        {
            return new Integer(input.readFixed32());
        }
        public void writeTo(Output output, int number, Integer value) throws IOException
        {
            output.writeFixed32(number, value.intValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.FIXED32;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> SFIXED32 = new RuntimeFieldFactory<Integer>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.SFIXED32, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setInt(message, input.readSFixed32());
                        else
                            f.set(message, new Integer(input.readSFixed32()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeSFixed32(this.number, f.getInt(message));
                        else
                        {
                            Integer value = (Integer)f.get(message);
                            if(value!=null)
                                output.writeSFixed32(this.number, value.intValue());
                        }
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
            };
        }
        public Integer readFrom(Input input) throws IOException
        {
            return new Integer(input.readSFixed32());
        }
        public void writeTo(Output output, int number, Integer value) throws IOException
        {
            output.writeSFixed32(number, value.intValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.SFIXED32;
        }
    };*/
    
    public static final RuntimeFieldFactory<Long> INT64 = new RuntimeFieldFactory<Long>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.INT64, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setLong(message, input.readInt64());
                        else
                            f.set(message, new Long(input.readInt64()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeInt64(this.number, f.getLong(message), false);
                        else
                        {
                            Long value = (Long)f.get(message);
                            if(value!=null)
                                output.writeInt64(this.number, value.longValue(), false);
                        }
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
            };
        }
        public Long readFrom(Input input) throws IOException
        {
            return new Long(input.readInt64());
        }
        public void writeTo(Output output, int number, Long value, boolean repeated) 
        throws IOException
        {
            output.writeInt64(number, value.longValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.INT64;
        }
    };
    
    /*public static final RuntimeFieldFactory<Long> UINT64 = new RuntimeFieldFactory<Long>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.UINT64, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setLong(message, input.readUInt64());
                        else
                            f.set(message, new Long(input.readInt64()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeUInt64(this.number, f.getLong(message));
                        else
                        {
                            Long value = (Long)f.get(message);
                            if(value!=null)
                                output.writeUInt64(this.number, value.longValue());
                        }
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
            };
        }
        public Long readFrom(Input input) throws IOException
        {
            return new Long(input.readUInt64());
        }
        public void writeTo(Output output, int number, Long value) throws IOException
        {
            output.writeUInt64(number, value.longValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.UINT64;
        }
    };
    
    public static final RuntimeFieldFactory<Long> SINT64 = new RuntimeFieldFactory<Long>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.SINT64, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setLong(message, input.readSInt64());
                        else
                            f.setLong(message, new Long(input.readSInt64()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeSInt64(this.number, f.getLong(message));
                        else
                        {
                            Long value = (Long)f.get(message);
                            if(value!=null)
                                output.writeSInt64(this.number, value.longValue());
                        }
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
            };
        }
        public Long readFrom(Input input) throws IOException
        {
            return new Long(input.readSInt64());
        }
        public void writeTo(Output output, int number, Long value) throws IOException
        {
            output.writeSInt64(number, value.longValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.SINT64;
        }
    };
    
    public static final RuntimeFieldFactory<Long> FIXED64 = new RuntimeFieldFactory<Long>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.FIXED64, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setLong(message, input.readFixed64());
                        else
                            f.set(message, new Long(input.readFixed64()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeFixed64(this.number, f.getLong(message));
                        else
                        {
                            Long value = (Long)f.get(message);
                            if(value!=null)
                                output.writeFixed64(this.number, value.longValue());
                        }
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
            };
        }
        public Long readFrom(Input input) throws IOException
        {
            return new Long(input.readFixed64());
        }
        public void writeTo(Output output, int number, Long value) throws IOException
        {
            output.writeFixed64(number, value.longValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.FIXED64;
        }
    };
    
    public static final RuntimeFieldFactory<Long> SFIXED64 = new RuntimeFieldFactory<Long>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.SFIXED64, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setLong(message, input.readSFixed64());
                        else
                            f.set(message, new Long(input.readSFixed64()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeSFixed64(this.number, f.getLong(message));
                        else
                        {
                            Long value = (Long)f.get(message);
                            if(value!=null)
                                output.writeSFixed64(this.number, value.longValue());
                        }
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
            };
        }
        public Long readFrom(Input input) throws IOException
        {
            return new Long(input.readSFixed64());
        }
        public void writeTo(Output output, int number, Long value) throws IOException
        {
            output.writeSFixed64(number, value.longValue());
        }
        public FieldType getFieldType()
        {
            return FieldType.SFIXED64;
        }
    };*/
    
    public static final RuntimeFieldFactory<Float> FLOAT = new RuntimeFieldFactory<Float>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.FLOAT, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setFloat(message, input.readFloat());
                        else
                            f.set(message, new Float(input.readFloat()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeFloat(this.number, f.getFloat(message), false);
                        else
                        {
                            Float value = (Float)f.get(message);
                            if(value!=null)
                                output.writeFloat(this.number, value.floatValue(), false);
                        }
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
            };
        }
        public Float readFrom(Input input) throws IOException
        {
            return new Float(input.readFloat());
        }
        public void writeTo(Output output, int number, Float value, boolean repeated) 
        throws IOException
        {
            output.writeFloat(number, value.floatValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.FLOAT;
        }
    };
    
    public static final RuntimeFieldFactory<Double> DOUBLE = new RuntimeFieldFactory<Double>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.DOUBLE, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            f.setDouble(message, input.readDouble());
                        else
                            f.set(message, new Double(input.readDouble()));
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeDouble(this.number, f.getDouble(message), false);
                        else
                        {
                            Double value = (Double)f.get(message);
                            if(value!=null)
                                output.writeDouble(this.number, value.doubleValue(), false);
                        }
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
            };
        }
        public Double readFrom(Input input) throws IOException
        {
            return new Double(input.readDouble());
        }
        public void writeTo(Output output, int number, Double value, boolean repeated) 
        throws IOException
        {
            output.writeDouble(number, value.doubleValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.DOUBLE;
        }
    };
    
    public static final RuntimeFieldFactory<Boolean> BOOL = new RuntimeFieldFactory<Boolean>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.BOOL, number, name)
            {
                final boolean primitive = f.getType().isPrimitive();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        boolean value = input.readBool();
                        if(primitive)
                            f.setBoolean(message, value);
                        else
                            f.set(message, value ? Boolean.TRUE : Boolean.FALSE);
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        if(primitive)
                            output.writeBool(this.number, f.getBoolean(message), false);
                        else
                        {
                            Boolean value = (Boolean)f.get(message);
                            if(value!=null)
                                output.writeBool(this.number, value.booleanValue(), false);
                        }
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
            };
        }
        public Boolean readFrom(Input input) throws IOException
        {
            return input.readBool() ? Boolean.TRUE : Boolean.FALSE;
        }
        public void writeTo(Output output, int number, Boolean value, boolean repeated) 
        throws IOException
        {
            output.writeBool(number, value.booleanValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.BOOL;
        }
    };
    
    public static final RuntimeFieldFactory<String> STRING = new RuntimeFieldFactory<String>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.STRING, number, name)
            {
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, input.readString());
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        String value = (String)f.get(message);
                        if(value!=null)
                            output.writeString(this.number, value, false);
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
            };
        }
        public String readFrom(Input input) throws IOException
        {
            return input.readString();
        }
        public void writeTo(Output output, int number, String value, boolean repeated) 
        throws IOException
        {
            output.writeString(number, value, repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.STRING;
        }
    };
    
    public static final RuntimeFieldFactory<ByteString> BYTES = new RuntimeFieldFactory<ByteString>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.BYTES, number, name)
            {
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, input.readBytes());
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        ByteString bs = (ByteString)f.get(message);
                        if(bs!=null)
                            output.writeBytes(this.number, bs, false);
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
            };
        }
        public ByteString readFrom(Input input) throws IOException
        {
            return input.readBytes();
        }
        public void writeTo(Output output, int number, ByteString value, boolean repeated) 
        throws IOException
        {
            output.writeBytes(number, value, repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.BYTES;
        }
    };
    
    public static final RuntimeFieldFactory<byte[]> BYTE_ARRAY = new RuntimeFieldFactory<byte[]>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.BYTES, number, name)
            {
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, input.readByteArray());
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        byte[] array = (byte[])f.get(message);
                        if(array!=null)
                            output.writeByteArray(this.number, array, false);
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
            };
        }
        public byte[] readFrom(Input input) throws IOException
        {
            return input.readByteArray();
        }
        public void writeTo(Output output, int number, byte[] value, boolean repeated) 
        throws IOException
        {
            output.writeByteArray(number, value, repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.BYTES;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> ENUM = new RuntimeFieldFactory<Integer>()
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.ENUM, number, name)
            {
                final Object[] constants = f.getType().getEnumConstants();
                {
                    f.setAccessible(true);
                }                    
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, constants[input.readEnum()]);
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        Enum<?> en = (Enum<?>)f.get(message);
                        if(en!=null)
                            output.writeEnum(this.number, en.ordinal(), false);
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
            };
        }
        public Integer readFrom(Input input) throws IOException
        {
            return new Integer(input.readInt32());
        }
        public void writeTo(Output output, int number, Integer value, boolean repeated) throws IOException
        {
            output.writeInt32(number, value.intValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.INT32;
        }
    };
    
    // NON-INLINE VALUES
    
    static final RuntimeFieldFactory<Message<?>> MESSAGE = new RuntimeFieldFactory<Message<?>>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            Class<Object> typeClass = (Class<Object>)f.getType();
            if(!RuntimeSchema.isRegistered(typeClass))
            {
                synchronized(MESSAGE)
                {
                    if(!RuntimeSchema.isRegistered(typeClass))
                    {
                        try
                        {
                            Message<Object> m = (Message<Object>)typeClass.newInstance();
                            RuntimeSchema.register(typeClass, m.cachedSchema());
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            
            return new Field<T>(FieldType.MESSAGE, number, name)
            {
                final Schema<Message<?>> schema = (Schema<Message<?>>)RuntimeSchema.getSchema(
                        f.getType());
                {
                    f.setAccessible(true);
                }
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        Message<?> existing = (Message<?>)f.get(message);
                        if(existing==null)
                            f.set(message, input.mergeObject(schema.newMessage(), schema));
                        else
                            input.mergeObject(existing, schema);
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        Message<?> value = (Message<?>)f.get(message);
                        if(value!=null)
                            output.writeObject(this.number, value, schema, false);
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
            };
        }
        public Message<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public void writeTo(Output output, int number, Message<?> value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Object> POJO = new RuntimeFieldFactory<Object>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {            
            return new Field<T>(FieldType.MESSAGE, number, name)
            {
                final Schema<Object> schema = (Schema<Object>)RuntimeSchema.getSchema(
                        f.getType());
                {
                    f.setAccessible(true);
                }
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        Object existing = f.get(message);
                        if(existing==null)
                            f.set(message, input.mergeObject(schema.newMessage(), schema));
                        else
                            input.mergeObject(existing, schema);
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        Object value = f.get(message);
                        if(value!=null)
                            output.writeObject(this.number, value, schema, false);
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
            };
        }
        public Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public void writeTo(Output output, int number, Object value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<List<?>> LIST = new RuntimeFieldFactory<List<?>>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            Class<?> clazz;
            try
            {
                clazz = 
                    (Class<?>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
            }
            catch(Exception e)
            {
                return null;
            }
            final Class<?> genericType = clazz;
            
            if(genericType.isEnum())
            {
                return new Field<T>(FieldType.ENUM, number, name)
                {
                    final Object[] constants = genericType.getEnumConstants();
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = constants[input.readEnum()];
                        try
                        {
                            List<Object> existing = (List<Object>)f.get(message);
                            if(existing==null)
                            {
                                ArrayList<Object> list = new ArrayList<Object>();
                                list.add(value);
                                f.set(message, list);
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
                    protected void writeTo(Output output, T message) throws IOException
                    {
                        try
                        {
                            List<Enum<?>> list = (List<Enum<?>>)f.get(message);
                            if(list!=null && !list.isEmpty())
                            {
                                for(Enum<?> en : list)
                                    output.writeInt32(this.number, en.ordinal(), true);
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
            
            final RuntimeFieldFactory<Object> inline = 
                (RuntimeFieldFactory<Object>)__inlineValues.get(genericType.getName());
            
            if(inline==null)
            {
                if(genericType.isArray() || Collection.class.isAssignableFrom(genericType))
                    return null;
                
                return new Field<T>(FieldType.MESSAGE, number, name)
                {
                    final Schema<Object> targetSchema = 
                        (Schema<Object>)RuntimeSchema.getSchema(genericType);
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = input.mergeObject(targetSchema.newMessage(), targetSchema);
                        try
                        {
                            List<Object> existing = (List<Object>)f.get(message);
                            if(existing==null)
                            {
                                ArrayList<Object> list = new ArrayList<Object>();
                                list.add(value);
                                f.set(message, list);
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
                    protected void writeTo(Output output, T message) throws IOException
                    {
                        try
                        {
                            List<Object> list = (List<Object>)f.get(message);
                            if(list!=null && !list.isEmpty())
                            {
                                for(Object o : list)
                                    output.writeObject(this.number, o, targetSchema, true);
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
            
            return new Field<T>(inline.getFieldType(), number, name)
            {
                {
                    f.setAccessible(true);
                }
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    Object value = inline.readFrom(input);
                    try
                    {
                        List<Object> existing = (List<Object>)f.get(message);
                        if(existing==null)
                        {
                            ArrayList<Object> list = new ArrayList<Object>();
                            list.add(value);
                            f.set(message, list);
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        List<Object> list = (List<Object>)f.get(message);
                        if(list!=null && !list.isEmpty())
                        {
                            for(Object o : list)
                                inline.writeTo(output, this.number, o, true);
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
        public List<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        public void writeTo(Output output, int number, List<?> value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Set<?>> SET = new RuntimeFieldFactory<Set<?>>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            Class<?> clazz;
            try
            {
                clazz = 
                    (Class<?>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
            }
            catch(Exception e)
            {
                return null;
            }
            final Class<?> genericType = clazz;
            
            if(genericType.isEnum())
            {
                return new Field<T>(FieldType.ENUM, number, name)
                {
                    final Object[] constants = genericType.getEnumConstants();
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = constants[input.readEnum()];
                        try
                        {
                            Set<Object> existing = (Set<Object>)f.get(message);
                            if(existing==null)
                            {
                                LinkedHashSet<Object> set = new LinkedHashSet<Object>();
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
                    protected void writeTo(Output output, T message) throws IOException
                    {
                        try
                        {
                            Set<Enum<?>> list = (Set<Enum<?>>)f.get(message);
                            if(list!=null && !list.isEmpty())
                            {
                                for(Enum<?> en : list)
                                    output.writeInt32(this.number, en.ordinal(), true);
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
            
            final RuntimeFieldFactory<Object> inline = 
                (RuntimeFieldFactory<Object>)__inlineValues.get(genericType.getName());
            
            if(inline==null)
            {
                if(genericType.isArray() || Collection.class.isAssignableFrom(genericType))
                    return null;
                
                return new Field<T>(FieldType.MESSAGE, number, name)
                {
                    final Schema<Object> targetSchema = 
                        (Schema<Object>)RuntimeSchema.getSchema(genericType);
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = input.mergeObject(targetSchema.newMessage(), targetSchema);
                        try
                        {
                            Set<Object> existing = (Set<Object>)f.get(message);
                            if(existing==null)
                            {
                                LinkedHashSet<Object> set = new LinkedHashSet<Object>();
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
                    protected void writeTo(Output output, T message) throws IOException
                    {
                        try
                        {
                            Set<Object> set = (Set<Object>)f.get(message);
                            if(set!=null && !set.isEmpty())
                            {
                                for(Object o : set)
                                    output.writeObject(this.number, o, targetSchema, true);
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
            
            return new Field<T>(inline.getFieldType(), number, name)
            {
                {
                    f.setAccessible(true);
                }                
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    Object value = inline.readFrom(input);
                    try
                    {
                        Set<Object> existing = (Set<Object>)f.get(message);
                        if(existing==null)
                        {
                            LinkedHashSet<Object> set = new LinkedHashSet<Object>();
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
                protected void writeTo(Output output, T message) throws IOException
                {
                    try
                    {
                        Set<Object> set = (Set<Object>)f.get(message);
                        if(set!=null && !set.isEmpty())
                        {
                            for(Object o : set)
                                inline.writeTo(output, this.number, o, true);
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
        public Set<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        public void writeTo(Output output, int number, Set<?> value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Object> ARRAY = new RuntimeFieldFactory<Object>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            final Class<?> componentType = f.getType().getComponentType();
            
            // no two dimensional arrays
            if(byte[].class==componentType)
                return null;
            
            if(componentType.isEnum())
            {
                return new Field<T>(FieldType.ENUM, number, name)
                {
                    final Object[] constants = componentType.getEnumConstants();
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = constants[input.readEnum()];
                        try
                        {
                            Object existing = f.get(message);
                            if(existing==null)
                            {
                                Object newArray = Array.newInstance(componentType, 1);
                                Array.set(newArray, 0, value);
                                f.set(message, newArray);
                            }
                            else
                            {
                                int len = Array.getLength(existing);
                                Object newArray = Array.newInstance(componentType, len+1);
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
                        try
                        {
                            Object array = f.get(message);
                            if(array!=null)
                            {
                                int len = Array.getLength(array);
                                for(int i=0; i<len; i++)
                                {
                                    output.writeInt32(this.number, 
                                            ((Enum<?>)Array.get(array, i)).ordinal(), true);
                                }
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
            
            final RuntimeFieldFactory<Object> inline = 
                (RuntimeFieldFactory<Object>)__inlineValues.get(componentType.getName());
            
            if(inline==null)
            {
                if(componentType.isArray() || Collection.class.isAssignableFrom(componentType))
                    return null;
                
                return new Field<T>(FieldType.MESSAGE, number, name)
                {
                    final Schema<Object> targetSchema = 
                        (Schema<Object>)RuntimeSchema.getSchema(componentType);
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = input.mergeObject(targetSchema.newMessage(), targetSchema);
                        try
                        {
                            Object existing = f.get(message);
                            if(existing==null)
                            {
                                Object newArray = Array.newInstance(componentType, 1);
                                Array.set(newArray, 0, value);
                                f.set(message, newArray);
                            }
                            else
                            {
                                int len = Array.getLength(existing);
                                Object newArray = Array.newInstance(componentType, len+1);
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
                        try
                        {
                            Object array = f.get(message);
                            if(array!=null)
                            {
                                int len = Array.getLength(array);
                                for(int i=0; i<len; i++)
                                {
                                    output.writeObject(this.number, Array.get(array, i), 
                                            targetSchema, true);
                                }
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
            
            return new Field<T>(inline.getFieldType(), number, name)
            {
                {
                    f.setAccessible(true);
                }
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    Object value = inline.readFrom(input);
                    try
                    {
                        Object existing = f.get(message);
                        if(existing==null)
                        {
                            Object newArray = Array.newInstance(componentType, 1);
                            Array.set(newArray, 0, value);
                            f.set(message, newArray);
                        }
                        else
                        {
                            int len = Array.getLength(existing);
                            Object newArray = Array.newInstance(componentType, len+1);
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
                    try
                    {
                        Object array = f.get(message);
                        if(array!=null)
                        {
                            int len = Array.getLength(array);
                            for(int i=0; i<len; i++)
                                inline.writeTo(output, this.number, Array.get(array, i), true);
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
        public Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public void writeTo(Output output, int number, Object value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    
    private static final HashMap<String, RuntimeFieldFactory<?>> __inlineValues = 
        new HashMap<String, RuntimeFieldFactory<?>>();
    
    static
    {
        __inlineValues.put(Integer.TYPE.getName(), INT32);
        __inlineValues.put(Integer.class.getName(), INT32);
        __inlineValues.put(Long.TYPE.getName(), INT64);
        __inlineValues.put(Long.class.getName(), INT64);
        __inlineValues.put(Float.TYPE.getName(), FLOAT);
        __inlineValues.put(Float.class.getName(), FLOAT);
        __inlineValues.put(Double.TYPE.getName(), DOUBLE);
        __inlineValues.put(Double.class.getName(), DOUBLE);
        __inlineValues.put(Boolean.TYPE.getName(), BOOL);
        __inlineValues.put(Boolean.class.getName(), BOOL);
        __inlineValues.put(Character.TYPE.getName(), CHAR);
        __inlineValues.put(Character.class.getName(), CHAR);
        __inlineValues.put(Short.TYPE.getName(), SHORT);
        __inlineValues.put(Short.class.getName(), SHORT);
        __inlineValues.put(Byte.TYPE.getName(), BYTE);
        __inlineValues.put(Byte.class.getName(), BYTE);
        __inlineValues.put(String.class.getName(), STRING);
        __inlineValues.put(ByteString.class.getName(), BYTES);
        __inlineValues.put(byte[].class.getName(), BYTE_ARRAY);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> RuntimeFieldFactory<T> getInline(Class<T> typeClass)
    {
        return (RuntimeFieldFactory<T>)__inlineValues.get(typeClass);
    }

}
