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

import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BIGDECIMAL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BIGINTEGER;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BOOL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTES;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTE_ARRAY;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_CHAR;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_DATE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_DELEGATE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_ENUM;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_OBJECT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_POJO;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_STRING;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Morph;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * Field factory via sun.misc.Unsafe.
 *
 * @author David Yu
 * @created Jul 7, 2011
 */
public final class RuntimeUnsafeFieldFactory
{
    
    private static final sun.misc.Unsafe us = initUnsafe();
    
    private static sun.misc.Unsafe initUnsafe()
    {
        try
        {
            java.lang.reflect.Field f = 
                sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            
            f.setAccessible(true);
            
            return (sun.misc.Unsafe)f.get(null);
        }
        catch(Exception e)
        {
            // ignore
            
            /* android 3.x
            try
            {
                java.lang.reflect.Field f = 
                        sun.misc.Unsafe.class.getDeclaredField("THE_ONE");
                    
                f.setAccessible(true);
                
                return (sun.misc.Unsafe)f.get(null);
            }
            catch(Exception e1)
            {
                // ignore
            }*/
        }
        
        return sun.misc.Unsafe.getUnsafe();
    }
    
    private RuntimeUnsafeFieldFactory() {}
    
    public static final RuntimeFieldFactory<Character> CHAR = new RuntimeFieldFactory<Character>(ID_CHAR)
    {
        
        
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name)
            {                 
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putChar(message, offset, (char)input.readUInt32());
                    else
                        us.putObject(message, offset, Character.valueOf((char)input.readUInt32()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeUInt32(number, us.getChar(message, offset), false);
                    else
                    {
                        Character value = (Character)us.getObject(message, offset);
                        if(value!=null)
                            output.writeUInt32(number, value.charValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
        public Character readFrom(Input input) throws IOException
        {
            return Character.valueOf((char)input.readUInt32());
        }
        public void writeTo(Output output, int number, Character value, boolean repeated) 
        throws IOException
        {
            output.writeUInt32(number, value.charValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }
        public Class<?> typeClass()
        {
            return Character.class;
        }
    };
    
    public static final RuntimeFieldFactory<Short> SHORT = new RuntimeFieldFactory<Short>(ID_SHORT)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putShort(message, offset, (short)input.readUInt32());
                    else
                        us.putObject(message, offset, Short.valueOf((short)input.readUInt32()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeUInt32(number, us.getShort(message, offset), false);
                    else
                    {
                        Short value = (Short)us.getObject(message, offset);
                        if(value!=null)
                            output.writeUInt32(number, value.shortValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
        public Short readFrom(Input input) throws IOException
        {
            return Short.valueOf((short)input.readUInt32());
        }
        public void writeTo(Output output, int number, Short value, boolean repeated) 
        throws IOException
        {
            output.writeUInt32(number, value.shortValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }
        public Class<?> typeClass()
        {
            return Short.class;
        }
    };
    
    public static final RuntimeFieldFactory<Byte> BYTE = new RuntimeFieldFactory<Byte>(ID_BYTE)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name)
            {                  
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putByte(message, offset, (byte)input.readUInt32());
                    else
                        us.putObject(message, offset, Byte.valueOf((byte)input.readUInt32()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeUInt32(number, us.getByte(message, offset), false);
                    else
                    {
                        Byte value = (Byte)us.getObject(message, offset);
                        if(value!=null)
                            output.writeUInt32(number, value.byteValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
        public Byte readFrom(Input input) throws IOException
        {
            return Byte.valueOf((byte)input.readUInt32());
        }
        public void writeTo(Output output, int number, Byte value, boolean repeated) 
        throws IOException
        {
            output.writeUInt32(number, value.byteValue(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }
        public Class<?> typeClass()
        {
            return Byte.class;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> INT32 = new RuntimeFieldFactory<Integer>(ID_INT32)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.INT32, number, name)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putInt(message, offset, input.readInt32());
                    else
                        us.putObject(message, offset, Integer.valueOf(input.readInt32()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeInt32(number, us.getInt(message, offset), false);
                    else
                    {
                        Integer value = (Integer)us.getObject(message, offset);
                        if(value!=null)
                            output.writeInt32(number, value.intValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeInt32(number, input.readInt32(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeInt32(number, input.readInt32(), repeated);
        }
        public Integer readFrom(Input input) throws IOException
        {
            return Integer.valueOf(input.readInt32());
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
        public Class<?> typeClass()
        {
            return Integer.class;
        }
    };
    
    public static final RuntimeFieldFactory<Long> INT64 = new RuntimeFieldFactory<Long>(ID_INT64)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.INT64, number, name)
            {                  
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putLong(message, offset, input.readInt64());
                    else
                        us.putObject(message, offset, Long.valueOf(input.readInt64()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeInt64(number, us.getLong(message, offset), false);
                    else
                    {
                        Long value = (Long)us.getObject(message, offset);
                        if(value!=null)
                            output.writeInt64(number, value.longValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeInt64(number, input.readInt64(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeInt64(number, input.readInt64(), repeated);
        }
        public Long readFrom(Input input) throws IOException
        {
            return Long.valueOf(input.readInt64());
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
        public Class<?> typeClass()
        {
            return Long.class;
        }
    };
    
    public static final RuntimeFieldFactory<Float> FLOAT = new RuntimeFieldFactory<Float>(ID_FLOAT)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.FLOAT, number, name)
            {                   
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putFloat(message, offset, input.readFloat());
                    else
                        us.putObject(message, offset, new Float(input.readFloat()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeFloat(number, us.getFloat(message, offset), false);
                    else
                    {
                        Float value = (Float)us.getObject(message, offset);
                        if(value!=null)
                            output.writeFloat(number, value.floatValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeFloat(number, input.readFloat(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeFloat(number, input.readFloat(), repeated);
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
        public Class<?> typeClass()
        {
            return Float.class;
        }
    };
    
    public static final RuntimeFieldFactory<Double> DOUBLE = new RuntimeFieldFactory<Double>(ID_DOUBLE)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.DOUBLE, number, name)
            {                   
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putDouble(message, offset, input.readDouble());
                    else
                        us.putObject(message, offset, new Double(input.readDouble()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeDouble(number, us.getDouble(message, offset), false);
                    else
                    {
                        Double value = (Double)us.getObject(message, offset);
                        if(value!=null)
                            output.writeDouble(number, value.doubleValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeDouble(number, input.readDouble(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeDouble(number, input.readDouble(), repeated);
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
        public Class<?> typeClass()
        {
            return Double.class;
        }
    };
    
    public static final RuntimeFieldFactory<Boolean> BOOL = new RuntimeFieldFactory<Boolean>(ID_BOOL)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BOOL, number, name)
            {              
                public void mergeFrom(Input input, T message) throws IOException
                {
                    if(primitive)
                        us.putBoolean(message, offset, input.readBool());
                    else
                        us.putObject(message, offset, input.readBool() ? Boolean.TRUE : Boolean.FALSE);
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    if(primitive)
                        output.writeBool(number, us.getBoolean(message, offset), false);
                    else
                    {
                        Boolean value = (Boolean)us.getObject(message, offset);
                        if(value!=null)
                            output.writeBool(number, value.booleanValue(), false);
                    }
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeBool(number, input.readBool(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeBool(number, input.readBool(), repeated);
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
        public Class<?> typeClass()
        {
            return Boolean.class;
        }
    };
    
    public static final RuntimeFieldFactory<String> STRING = new RuntimeFieldFactory<String>(ID_STRING)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.STRING, number, name)
            {                  
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, input.readString());
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    String value = (String)us.getObject(message, offset);
                    if(value!=null)
                        output.writeString(number, value, false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, true, number, repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, true, number, repeated);
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
        public Class<?> typeClass()
        {
            return String.class;
        }
    };
    
    public static final RuntimeFieldFactory<ByteString> BYTES = new RuntimeFieldFactory<ByteString>(ID_BYTES)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name)
            {                  
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, input.readBytes());
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    ByteString bs = (ByteString)us.getObject(message, offset);
                    if(bs!=null)
                        output.writeBytes(number, bs, false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
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
        public Class<?> typeClass()
        {
            return ByteString.class;
        }
    };
    
    public static final RuntimeFieldFactory<byte[]> BYTE_ARRAY = new RuntimeFieldFactory<byte[]>(ID_BYTE_ARRAY)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name)
            {                 
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, input.readByteArray());
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    byte[] array = (byte[])us.getObject(message, offset);
                    if(array!=null)
                        output.writeByteArray(number, array, false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
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
        public Class<?> typeClass()
        {
            return byte[].class;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> ENUM = new RuntimeFieldFactory<Integer>(ID_ENUM)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final EnumIO<? extends Enum<?>> eio = strategy.getEnumIO(f.getType());
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.ENUM, number, name)
            { 
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, eio.readFrom(input));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    final Enum<?> existing = (Enum<?>)us.getObject(message, offset);
                    if(existing != null)
                        EnumIO.writeTo(output, number, repeated, existing);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    EnumIO.transfer(pipe, input, output, number, repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public Integer readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public void writeTo(Output output, int number, Integer value, boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
        public Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    // NON-INLINE VALUES
    
    static final RuntimeFieldFactory<Object> POJO = new RuntimeFieldFactory<Object>(ID_POJO)
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            Class<Object> type = (Class<Object>)f.getType();
            final long offset = us.objectFieldOffset(f);
            return new RuntimeMessageField<T,Object>(
                    type, strategy.getSchemaWrapper(type, true), 
                    FieldType.MESSAGE, number, name, false)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, input.mergeObject(us.getObject(message, offset), getSchema()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    final Object existing = us.getObject(message, offset);
                    if(existing != null)
                        output.writeObject(number, existing, getSchema(), false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, getPipeSchema(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
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
        public Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Object> POLYMORPHIC_POJO = new RuntimeFieldFactory<Object>(0)
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            if(POJO == pojo(f.getType(), f.getAnnotation(Morph.class)))
                return POJO.create(number, name, f, strategy);
            
            final long offset = us.objectFieldOffset(f);
            return new RuntimeDerivativeField<T>(
                    (Class<Object>)f.getType(), 
                    FieldType.MESSAGE, number, name, false, strategy)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    final Object value = input.mergeObject(message, schema);
                    if(input instanceof GraphInput && 
                            ((GraphInput)input).isCurrentMessageReference())
                    {
                        // a reference from polymorphic+cyclic graph deser
                        us.putObject(message, offset, value);
                    }
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    final Object existing = us.getObject(message, offset);
                    if(existing != null)
                        output.writeObject(number, existing, schema, false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, schema.pipeSchema, false);
                }
                public void doMergeFrom(Input input, Schema<Object> schema, 
                        Object message) throws IOException
                {
                    final Object existing = us.getObject(message, offset);
                    
                    // merge if not null and is same type.
                    final Object value = existing == null || existing.getClass() != 
                        schema.typeClass() ? schema.newMessage() : existing;
                    
                    if(input instanceof GraphInput)
                    {
                        // update the actual reference.
                        ((GraphInput)input).updateLast(value, message);
                    }
                    
                    schema.mergeFrom(input, value);
                    
                    us.putObject(message, offset, value);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
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
        public Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Object> OBJECT = new RuntimeFieldFactory<Object>(ID_OBJECT)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new RuntimeObjectField<T>( 
                    FieldType.MESSAGE, number, name, false, 
                    PolymorphicSchemaFactories.getFactoryFromField(f.getType()), strategy)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    final Object value = input.mergeObject(message, schema);
                    if(input instanceof GraphInput && 
                            ((GraphInput)input).isCurrentMessageReference())
                    {
                        // a reference from polymorphic+cyclic graph deser
                        us.putObject(message, offset, value);
                    }
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    final Object existing = us.getObject(message, offset);
                    if(existing != null)
                        output.writeObject(number, existing, schema, false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, schema.getPipeSchema(), false);
                }
                public void setValue(Object value, Object message)
                {
                    us.putObject(message, offset, value);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
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
            return FieldType.MESSAGE;
        }
        public Class<?> typeClass()
        {
            return Object.class;
        }
    };
    
    public static final RuntimeFieldFactory<BigDecimal> BIGDECIMAL = new RuntimeFieldFactory<BigDecimal>(ID_BIGDECIMAL)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.STRING, number, name)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, new BigDecimal(input.readString()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    BigDecimal value = (BigDecimal)us.getObject(message, offset);
                    if(value!=null)
                        output.writeString(number, value.toString(), false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, true, number, repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, true, number, repeated);
        }
        public BigDecimal readFrom(Input input) throws IOException
        {
            return new BigDecimal(input.readString());
        }
        public void writeTo(Output output, int number, BigDecimal value, boolean repeated) 
        throws IOException
        {
            output.writeString(number, value.toString(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.STRING;
        }
        public Class<?> typeClass()
        {
            return BigDecimal.class;
        }
    };
    
    public static final RuntimeFieldFactory<BigInteger> BIGINTEGER = new RuntimeFieldFactory<BigInteger>(ID_BIGINTEGER)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, new BigInteger(input.readByteArray()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    BigInteger value = (BigInteger)us.getObject(message, offset);
                    if(value!=null)
                        output.writeByteArray(number, value.toByteArray(), false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }
        public BigInteger readFrom(Input input) throws IOException
        {
            return new BigInteger(input.readByteArray());
        }
        public void writeTo(Output output, int number, BigInteger value, boolean repeated) 
        throws IOException
        {
            output.writeByteArray(number, value.toByteArray(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.BYTES;
        }
        public Class<?> typeClass()
        {
            return BigInteger.class;
        }
    };
    
    public static final RuntimeFieldFactory<Date> DATE = new RuntimeFieldFactory<Date>(ID_DATE)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.FIXED64, number, name)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, new Date(input.readFixed64()));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    Date value = (Date)us.getObject(message, offset);
                    if(value!=null)
                        output.writeFixed64(number, value.getTime(), false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeFixed64(number, input.readFixed64(), repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeFixed64(number, input.readFixed64(), repeated);
        }
        public Date readFrom(Input input) throws IOException
        {
            return new Date(input.readFixed64());
        }
        public void writeTo(Output output, int number, Date value, boolean repeated) 
        throws IOException
        {
            output.writeFixed64(number, value.getTime(), repeated);
        }
        public FieldType getFieldType()
        {
            return FieldType.FIXED64;
        }
        public Class<?> typeClass()
        {
            return Date.class;
        }
    };

    public static final RuntimeFieldFactory<Object> DELEGATE = new RuntimeFieldFactory<Object>(ID_DELEGATE)
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, 
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final Delegate<Object> delegate = strategy.getDelegate(
                    (Class<Object>)f.getType());
            
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name)
            {
                public void mergeFrom(Input input, T message) throws IOException
                {
                    us.putObject(message, offset, delegate.readFrom(input));
                }
                public void writeTo(Output output, T message) throws IOException
                {
                    final Object value = (Object)us.getObject(message, offset);
                    if(value != null)
                        delegate.writeTo(output, number, value, false);
                }
                public void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    delegate.transfer(pipe, input, output, number, repeated);
                }
            };
        }
        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
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
        public Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
}
