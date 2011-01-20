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
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
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
    
    protected abstract FieldType getFieldType();
    
    protected abstract V readFrom(Input input) throws IOException;
    
    protected abstract void writeTo(Output output, int number, V value, 
            boolean repeated) throws IOException;
    
    protected abstract void transfer(Pipe pipe, Input input, Output output, int number, 
            boolean repeated) throws IOException;

    
    /**
     * Gets the runtime field factory of the given {@code clazz}.
     */
    public static RuntimeFieldFactory<?> getFieldFactory(Class<?> clazz)
    {
        if(Message.class.isAssignableFrom(clazz))
            return POJO;
        
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
        
        if(Collection.class == clazz)
            return COLLECTION;
        
        RuntimeFieldFactory<?> inline =  __inlineValues.get(clazz.getName());
        return inline == null ? pojo(clazz) : inline;
    }
    
    static RuntimeFieldFactory<?> pojo(Class<?> clazz)
    {
        return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()) ? 
                POLYMORPHIC_POJO : POJO;
    }
    
    public static final RuntimeFieldFactory<Character> CHAR = new RuntimeFieldFactory<Character>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
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
                            f.setChar(message, (char)input.readUInt32());
                        else
                            f.set(message, new Character((char)input.readUInt32()));
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
                            output.writeUInt32(this.number, f.getChar(message), false);
                        else
                        {
                            Character value = (Character)f.get(message);
                            if(value!=null)
                                output.writeUInt32(this.number, value.charValue(), false);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
        protected Character readFrom(Input input) throws IOException
        {
            return new Character((char)input.readUInt32());
        }
        protected void writeTo(Output output, int number, Character value, boolean repeated) 
        throws IOException
        {
            output.writeUInt32(number, value.charValue(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.UINT32;
        }
    };
    
    public static final RuntimeFieldFactory<Short> SHORT = new RuntimeFieldFactory<Short>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
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
                            f.setShort(message, (short)input.readUInt32());
                        else
                            f.set(message, new Short((short)input.readUInt32()));
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
                            output.writeUInt32(this.number, f.getShort(message), false);
                        else
                        {
                            Short value = (Short)f.get(message);
                            if(value!=null)
                                output.writeUInt32(this.number, value.shortValue(), false);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
        protected Short readFrom(Input input) throws IOException
        {
            return new Short((short)input.readUInt32());
        }
        protected void writeTo(Output output, int number, Short value, boolean repeated) 
        throws IOException
        {
            output.writeUInt32(number, value.shortValue(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.UINT32;
        }
    };
    
    public static final RuntimeFieldFactory<Byte> BYTE = new RuntimeFieldFactory<Byte>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
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
                            f.setByte(message, (byte)input.readUInt32());
                        else
                            f.set(message, new Byte((byte)input.readUInt32()));
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
                            output.writeUInt32(this.number, f.getByte(message), false);
                        else
                        {
                            Byte value = (Byte)f.get(message);
                            if(value!=null)
                                output.writeUInt32(this.number, value.byteValue(), false);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
        protected Byte readFrom(Input input) throws IOException
        {
            return new Byte((byte)input.readUInt32());
        }
        protected void writeTo(Output output, int number, Byte value, boolean repeated) 
        throws IOException
        {
            output.writeUInt32(number, value.byteValue(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.UINT32;
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeInt32(number, input.readInt32(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeInt32(number, input.readInt32(), repeated);
        }
        protected Integer readFrom(Input input) throws IOException
        {
            return new Integer(input.readInt32());
        }
        protected void writeTo(Output output, int number, Integer value, boolean repeated) 
        throws IOException
        {
            output.writeInt32(number, value.intValue(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.INT32;
        }
    };
    
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeInt64(number, input.readInt64(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeInt64(number, input.readInt64(), repeated);
        }
        protected Long readFrom(Input input) throws IOException
        {
            return new Long(input.readInt64());
        }
        protected void writeTo(Output output, int number, Long value, boolean repeated) 
        throws IOException
        {
            output.writeInt64(number, value.longValue(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.INT64;
        }
    };
    
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeFloat(number, input.readFloat(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeFloat(number, input.readFloat(), repeated);
        }
        protected Float readFrom(Input input) throws IOException
        {
            return new Float(input.readFloat());
        }
        protected void writeTo(Output output, int number, Float value, boolean repeated) 
        throws IOException
        {
            output.writeFloat(number, value.floatValue(), repeated);
        }
        protected FieldType getFieldType()
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeDouble(number, input.readDouble(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeDouble(number, input.readDouble(), repeated);
        }
        protected Double readFrom(Input input) throws IOException
        {
            return new Double(input.readDouble());
        }
        protected void writeTo(Output output, int number, Double value, boolean repeated) 
        throws IOException
        {
            output.writeDouble(number, value.doubleValue(), repeated);
        }
        protected FieldType getFieldType()
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeBool(number, input.readBool(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeBool(number, input.readBool(), repeated);
        }
        protected Boolean readFrom(Input input) throws IOException
        {
            return input.readBool() ? Boolean.TRUE : Boolean.FALSE;
        }
        protected void writeTo(Output output, int number, Boolean value, boolean repeated) 
        throws IOException
        {
            output.writeBool(number, value.booleanValue(), repeated);
        }
        protected FieldType getFieldType()
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, true, number, repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, true, number, repeated);
        }
        protected String readFrom(Input input) throws IOException
        {
            return input.readString();
        }
        protected void writeTo(Output output, int number, String value, boolean repeated) 
        throws IOException
        {
            output.writeString(number, value, repeated);
        }
        protected FieldType getFieldType()
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }
        protected ByteString readFrom(Input input) throws IOException
        {
            return input.readBytes();
        }
        protected void writeTo(Output output, int number, ByteString value, boolean repeated) 
        throws IOException
        {
            output.writeBytes(number, value, repeated);
        }
        protected FieldType getFieldType()
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }
        protected byte[] readFrom(Input input) throws IOException
        {
            return input.readByteArray();
        }
        protected void writeTo(Output output, int number, byte[] value, boolean repeated) 
        throws IOException
        {
            output.writeByteArray(number, value, repeated);
        }
        protected FieldType getFieldType()
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
                final EnumIO<?> eio = EnumIO.create(f.getType(), f);
                
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        eio.mergeFrom(input, message);
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
                        eio.writeTo(output, number, repeated, message);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    eio.transfer(pipe, input, output, number, repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected Integer readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, Integer value, boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    // NON-INLINE VALUES
    
    static final RuntimeFieldFactory<Object> POJO = new RuntimeFieldFactory<Object>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            Class<Object> type = (Class<Object>)f.getType();
            return new RuntimeMessageField<T,Object>(
                    type, RuntimeSchema.getSchemaWrapper(type), 
                    FieldType.MESSAGE, number, name, false)
            {
                {
                    f.setAccessible(true);
                }
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, input.mergeObject(f.get(message), getSchema()));
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
                    final Object existing;
                    try
                    {
                        existing = f.get(message);
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
                        output.writeObject(number, existing, getSchema(), false);
                }
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, getPipeSchema(), repeated);
                }
            };
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
        protected void writeTo(Output output, int number, Object value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Object> POLYMORPHIC_POJO = new RuntimeFieldFactory<Object>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            if(RuntimeSchema.isRegistered(f.getType()))
            {
                // the explicit mapping is configured. 
                return POJO.create(number, name, f);
            }
            
            return new PolymorphicRuntimeField<T>(
                    (Class<Object>)f.getType(), 
                    FieldType.MESSAGE, number, name)
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
                            f.set(message, value);
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
                    final Object existing;
                    try
                    {
                        existing = f.get(message);
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                    
                    if(existing != null)
                        output.writeObject(number, existing, schema, repeated);
                }
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, pipeSchema, false);
                }
                protected void doMergeFrom(Input input, Schema<Object> schema, 
                        Object message) throws IOException
                {
                    try
                    {
                        final Object existing = f.get(message);
                        
                        // merge if not null and is same type.
                        final Object value = existing == null || existing.getClass() != 
                            schema.typeClass() ? schema.newMessage() : existing;
                        
                        if(input instanceof GraphInput)
                        {
                            // update the actual reference.
                            ((GraphInput)input).updateLast(value, message);
                        }
                        
                        schema.mergeFrom(input, value);
                        f.set(message, value);
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
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, Object value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    /**
     * Mapped to {@link ArrayList}.
     */
    static final RuntimeFieldFactory<Collection<?>> COLLECTION = new RuntimeFieldFactory<Collection<?>>()
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
                return new Field<T>(FieldType.ENUM, number, name, true)
                {
                    final EnumIO<?> eio = EnumIO.create(genericType, null);
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = eio.readFrom(input);
                        try
                        {
                            Collection<Object> existing = (Collection<Object>)f.get(message);
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
                            Collection<Enum<?>> list = (Collection<Enum<?>>)f.get(message);
                            if(list!=null && !list.isEmpty())
                            {
                                for(Enum<?> en : list)
                                    eio.writeTo(output, number, true, en);
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        eio.transfer(pipe, input, output, number, repeated);
                    }
                };
            }
            
            final RuntimeFieldFactory<Object> inline = 
                (RuntimeFieldFactory<Object>)__inlineValues.get(genericType.getName());
            
            if(inline==null)
            {
                if(genericType.isArray() || Collection.class.isAssignableFrom(genericType))
                    return null;
                
                if(POLYMORPHIC_POJO == pojo(genericType) && 
                        !RuntimeSchema.isRegistered(genericType))
                {
                    // polymorphic
                    return new PolymorphicRuntimeField<T>(
                            (Class<Object>)f.getType(), 
                            FieldType.MESSAGE, number, name, true)
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
                                    Collection<Object> existing = (Collection<Object>)f.get(message);
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
                        }
                        protected void writeTo(Output output, T message) throws IOException
                        {
                            final Collection<Object> existing;
                            try
                            {
                                existing = (Collection<Object>)f.get(message);
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
                            output.writeObject(number, pipe, pipeSchema, repeated);
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
                                Collection<Object> existing = (Collection<Object>)f.get(message);
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
                    };
                }
                
                Class<Object> type = (Class<Object>)genericType;
                
                return new RuntimeMessageField<T,Object>(
                        type, RuntimeSchema.getSchemaWrapper(type), 
                        FieldType.MESSAGE, number, name, true)
                {
                    
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = input.mergeObject(null, getSchema());
                        try
                        {
                            Collection<Object> existing = (Collection<Object>)f.get(message);
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
                            Collection<Object> list = (Collection<Object>)f.get(message);
                            if(list!=null && !list.isEmpty())
                            {
                                final Schema<Object> schema = getSchema();
                                for(Object o : list)
                                    output.writeObject(this.number, o, schema, true);
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        output.writeObject(number, pipe, getPipeSchema(), repeated);
                    }
                };
            }
            
            return new Field<T>(inline.getFieldType(), number, name, true)
            {
                {
                    f.setAccessible(true);
                }
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    Object value = inline.readFrom(input);
                    try
                    {
                        Collection<Object> existing = (Collection<Object>)f.get(message);
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
                        Collection<Object> list = (Collection<Object>)f.get(message);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    inline.transfer(pipe, input, output, number, repeated);
                }
            };
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
                return new Field<T>(FieldType.ENUM, number, name, true)
                {
                    final EnumIO<?> eio = EnumIO.create(genericType, null);
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = eio.readFrom(input);
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
                                    eio.writeTo(output, number, repeated, en);
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        eio.transfer(pipe, input, output, number, repeated);
                    }
                };
            }
            
            final RuntimeFieldFactory<Object> inline = 
                (RuntimeFieldFactory<Object>)__inlineValues.get(genericType.getName());
            
            if(inline==null)
            {
                if(genericType.isArray() || Collection.class.isAssignableFrom(genericType))
                    return null;
                
                if(POLYMORPHIC_POJO == pojo(genericType) && 
                        !RuntimeSchema.isRegistered(genericType))
                {
                    // polymorphic
                    return new PolymorphicRuntimeField<T>(
                            (Class<Object>)f.getType(), 
                            FieldType.MESSAGE, number, name, true)
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
                        }
                        protected void writeTo(Output output, T message) throws IOException
                        {
                            final List<Object> existing;
                            try
                            {
                                existing = (List<Object>)f.get(message);
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
                            output.writeObject(number, pipe, pipeSchema, repeated);
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
                    };
                }
                
                Class<Object> type = (Class)genericType;
                return new RuntimeMessageField<T,Object>(
                        type, RuntimeSchema.getSchemaWrapper(type), 
                        FieldType.MESSAGE, number, name, true)
                {
                    
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = input.mergeObject(null, getSchema());
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
                                final Schema<Object> schema = getSchema();
                                for(Object o : list)
                                    output.writeObject(this.number, o, schema, true);
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        output.writeObject(number, pipe, getPipeSchema(), repeated);
                    }
                };
            }
            
            return new Field<T>(inline.getFieldType(), number, name, true)
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    inline.transfer(pipe, input, output, number, repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected List<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, List<?> value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
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
                return new Field<T>(FieldType.ENUM, number, name, true)
                {
                    final EnumIO<?> eio = EnumIO.create(genericType, null);
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = eio.readFrom(input);
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
                                    eio.writeTo(output, number, repeated, en);
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        eio.transfer(pipe, input, output, number, repeated);
                    }
                };
            }
            
            final RuntimeFieldFactory<Object> inline = 
                (RuntimeFieldFactory<Object>)__inlineValues.get(genericType.getName());
            
            if(inline==null)
            {
                if(genericType.isArray() || Collection.class.isAssignableFrom(genericType))
                    return null;
                
                if(POLYMORPHIC_POJO == pojo(genericType) && 
                        !RuntimeSchema.isRegistered(genericType))
                {
                    // polymorphic
                    return new PolymorphicRuntimeField<T>(
                            (Class<Object>)f.getType(), 
                            FieldType.MESSAGE, number, name, true)
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
                        }
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
                            output.writeObject(number, pipe, pipeSchema, repeated);
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
                    };
                }
                
                Class<Object> type = (Class<Object>)genericType;
                return new RuntimeMessageField<T, Object>(
                        type, RuntimeSchema.getSchemaWrapper(type), 
                        FieldType.MESSAGE, number, name, true)
                {
                    
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = input.mergeObject(null, getSchema());
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
                                final Schema<Object> schema = getSchema();
                                for(Object o : set)
                                    output.writeObject(this.number, o, schema, true);
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        output.writeObject(number, pipe, getPipeSchema(), repeated);
                    }
                };
            }
            
            return new Field<T>(inline.getFieldType(), number, name, true)
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    inline.transfer(pipe, input, output, number, repeated);
                }
            };
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
        protected void writeTo(Output output, int number, Set<?> value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
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
            if(byte[].class==componentType || Map.class.isAssignableFrom(componentType))
                return null;
            
            if(componentType.isEnum())
            {
                return new Field<T>(FieldType.ENUM, number, name, true)
                {
                    final EnumIO<?> eio = EnumIO.create(componentType, null);
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = eio.readFrom(input);
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
                                    eio.writeTo(output, number, repeated, (Enum<?>)Array.get(array, i));
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        eio.transfer(pipe, input, output, number, repeated);
                    }
                };
            }
            
            final RuntimeFieldFactory<Object> inline = 
                (RuntimeFieldFactory<Object>)__inlineValues.get(componentType.getName());
            
            if(inline==null)
            {
                if(componentType.isArray() || Collection.class.isAssignableFrom(componentType))
                    return null;
                
                if(POLYMORPHIC_POJO == pojo(componentType) && 
                        !RuntimeSchema.isRegistered(componentType))
                {
                    // polymorphic
                    return new PolymorphicRuntimeField<T>(
                            (Class<Object>)f.getType(), 
                            FieldType.MESSAGE, number, name, true)
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
                                int len = Array.getLength(array);
                                for(int i=0; i<len; i++)
                                {
                                    output.writeObject(number, Array.get(array, i), 
                                            schema, true);
                                }
                            }
                        }
                        protected void transfer(Pipe pipe, Input input, Output output, 
                                boolean repeated) throws IOException
                        {
                            output.writeObject(number, pipe, pipeSchema, repeated);
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
                    };
                }
                
                Class<Object> type = (Class<Object>)componentType;
                return new RuntimeMessageField<T, Object>(
                        type, RuntimeSchema.getSchemaWrapper(type), 
                        FieldType.MESSAGE, number, name, true)
                {
                    
                    {
                        f.setAccessible(true);
                    }
                    protected void mergeFrom(Input input, T message) throws IOException
                    {
                        Object value = input.mergeObject(null, getSchema());
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
                                final Schema<Object> schema = getSchema();
                                int len = Array.getLength(array);
                                for(int i=0; i<len; i++)
                                {
                                    output.writeObject(this.number, Array.get(array, i), 
                                            schema, true);
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
                    protected void transfer(Pipe pipe, Input input, Output output, 
                            boolean repeated) throws IOException
                    {
                        output.writeObject(number, pipe, getPipeSchema(), repeated);
                    }
                };
            }
            
            return new Field<T>(inline.getFieldType(), number, name, true)
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    inline.transfer(pipe, input, output, number, repeated);
                }
            };
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
        protected void writeTo(Output output, int number, Object value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    public static final RuntimeFieldFactory<BigDecimal> BIGDECIMAL = new RuntimeFieldFactory<BigDecimal>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.STRING, number, name)
            {               
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, new BigDecimal(input.readString()));
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
                        BigDecimal value = (BigDecimal)f.get(message);
                        if(value!=null)
                            output.writeString(this.number, value.toString(), false);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, true, number, repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, true, number, repeated);
        }
        protected BigDecimal readFrom(Input input) throws IOException
        {
            return new BigDecimal(input.readString());
        }
        protected void writeTo(Output output, int number, BigDecimal value, boolean repeated) 
        throws IOException
        {
            output.writeString(number, value.toString(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.STRING;
        }
    };
    
    public static final RuntimeFieldFactory<BigInteger> BIGINTEGER = new RuntimeFieldFactory<BigInteger>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.BYTES, number, name)
            {               
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, new BigInteger(input.readByteArray()));
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
                        BigInteger value = (BigInteger)f.get(message);
                        if(value!=null)
                            output.writeByteArray(this.number, value.toByteArray(), false);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }
        protected BigInteger readFrom(Input input) throws IOException
        {
            return new BigInteger(input.readByteArray());
        }
        protected void writeTo(Output output, int number, BigInteger value, boolean repeated) 
        throws IOException
        {
            output.writeByteArray(number, value.toByteArray(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.BYTES;
        }
    };
    
    public static final RuntimeFieldFactory<Date> DATE = new RuntimeFieldFactory<Date>()
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.FIXED64, number, name)
            {               
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, new Date(input.readFixed64()));
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
                        Date value = (Date)f.get(message);
                        if(value!=null)
                            output.writeFixed64(this.number, value.getTime(), false);
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
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeFixed64(number, input.readFixed64(), repeated);
                }
            };
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }
        protected Date readFrom(Input input) throws IOException
        {
            return new Date(input.readFixed64());
        }
        protected void writeTo(Output output, int number, Date value, boolean repeated) 
        throws IOException
        {
            output.writeFixed64(number, value.getTime(), repeated);
        }
        protected FieldType getFieldType()
        {
            return FieldType.FIXED64;
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
        __inlineValues.put(BigInteger.class.getName(), BIGINTEGER);
        __inlineValues.put(BigDecimal.class.getName(), BIGDECIMAL);
        __inlineValues.put(Date.class.getName(), DATE);
    }
    
    /**
     * Returns the factory for inline (scalar) values.
     */
    @SuppressWarnings("unchecked")
    public static <T> RuntimeFieldFactory<T> getInline(Class<T> typeClass)
    {
        return (RuntimeFieldFactory<T>)__inlineValues.get(typeClass.getName());
    }

}
