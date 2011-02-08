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
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    
    static final int ID_BOOL = 1, ID_BYTE = 2, ID_CHAR = 3, ID_SHORT = 4, 
        ID_INT32 = 5, ID_INT64 = 6, ID_FLOAT = 7, ID_DOUBLE = 8, 
        ID_STRING = 9, ID_BYTES = 10, ID_BYTE_ARRAY = 11, 
        ID_BIGDECIMAL = 12, ID_BIGINTEGER = 13, ID_DATE = 14, ID_OBJECT = 15, 
        
        // room for more scalar types (16-22)
        
        ID_ENUM = 23, 
        ID_COLLECTION = 24, ID_ARRAY = 25, 
        ID_MAP = 26, 
        // pojo fields limited to 126
        ID_POJO = 127;
    
    static final String STR_BOOL = "a", STR_BYTE = "b", STR_CHAR = "c", STR_SHORT = "d", 
        STR_INT32 = "e", STR_INT64 = "f", STR_FLOAT = "g", STR_DOUBLE = "h", 
        STR_STRING = "i", STR_BYTES = "j", STR_BYTE_ARRAY = "k", 
        STR_BIGDECIMAL = "l", STR_BIGINTEGER = "m", STR_DATE = "n", STR_OBJECT = "o", 
        
        // room for more scalar types (16-22)
        
        STR_ENUM = "w", 
        STR_COLLECTION = "x", STR_ARRAY = "y", 
        STR_MAP = "z", 
        // pojo fields limited to 126
        STR_POJO = "_";
    
    
    
    /**
     * Used by {@link ObjectSchema} to serialize dynamic (polymorphic) fields.
     */
    final int id;
    
    public RuntimeFieldFactory(int id)
    {
        this.id = id;
    }
    
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
    
    protected abstract Class<?> typeClass();

    
    /**
     * Gets the runtime field factory of the given {@code clazz}.
     */
    public static RuntimeFieldFactory<?> getFieldFactory(Class<?> clazz)
    {
        if(clazz.isArray() || Object.class == clazz)
            return OBJECT;
        
        if(Message.class.isAssignableFrom(clazz))
            return POJO;
        
        if(clazz.isEnum())
            return ENUM;
        
        final RuntimeFieldFactory<?> inline =  __inlineValues.get(clazz.getName());
        if(inline != null)
            return inline;
        
        if(Map.class.isAssignableFrom(clazz))
            return RuntimeMapFieldFactory.MAP;

        if(Collection.class.isAssignableFrom(clazz))
        {
            // repeated fields.
            return COLLECTION;
        }
        
        return pojo(clazz);
    }
    
    static RuntimeFieldFactory<?> pojo(Class<?> clazz)
    {
        return (clazz.isInterface() 
            || Modifier.isAbstract(clazz.getModifiers()) 
            || (!Modifier.isFinal(clazz.getModifiers()) && 
                    RuntimeSchema.MORPH_NON_FINAL_POJOS)) ? POLYMORPHIC_POJO : POJO;
    }
    
    static boolean isComplexComponentType(Class<?> clazz)
    {
        return clazz.isArray() || Object.class == clazz;
    }
    
    public static final RuntimeFieldFactory<Character> CHAR = new RuntimeFieldFactory<Character>(ID_CHAR)
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
                            output.writeUInt32(number, f.getChar(message), false);
                        else
                        {
                            Character value = (Character)f.get(message);
                            if(value!=null)
                                output.writeUInt32(number, value.charValue(), false);
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
        protected Class<?> typeClass()
        {
            return Character.class;
        }
    };
    
    public static final RuntimeFieldFactory<Short> SHORT = new RuntimeFieldFactory<Short>(ID_SHORT)
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
                            output.writeUInt32(number, f.getShort(message), false);
                        else
                        {
                            Short value = (Short)f.get(message);
                            if(value!=null)
                                output.writeUInt32(number, value.shortValue(), false);
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
        protected Class<?> typeClass()
        {
            return Short.class;
        }
    };
    
    public static final RuntimeFieldFactory<Byte> BYTE = new RuntimeFieldFactory<Byte>(ID_BYTE)
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
                            output.writeUInt32(number, f.getByte(message), false);
                        else
                        {
                            Byte value = (Byte)f.get(message);
                            if(value!=null)
                                output.writeUInt32(number, value.byteValue(), false);
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
        protected Class<?> typeClass()
        {
            return Byte.class;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> INT32 = new RuntimeFieldFactory<Integer>(ID_INT32)
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
                            output.writeInt32(number, f.getInt(message), false);
                        else
                        {
                            Integer value = (Integer)f.get(message);
                            if(value!=null)
                                output.writeInt32(number, value.intValue(), false);
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
        protected Class<?> typeClass()
        {
            return Integer.class;
        }
    };
    
    public static final RuntimeFieldFactory<Long> INT64 = new RuntimeFieldFactory<Long>(ID_INT64)
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
                            output.writeInt64(number, f.getLong(message), false);
                        else
                        {
                            Long value = (Long)f.get(message);
                            if(value!=null)
                                output.writeInt64(number, value.longValue(), false);
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
        protected Class<?> typeClass()
        {
            return Long.class;
        }
    };
    
    public static final RuntimeFieldFactory<Float> FLOAT = new RuntimeFieldFactory<Float>(ID_FLOAT)
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
                            output.writeFloat(number, f.getFloat(message), false);
                        else
                        {
                            Float value = (Float)f.get(message);
                            if(value!=null)
                                output.writeFloat(number, value.floatValue(), false);
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
        protected Class<?> typeClass()
        {
            return Float.class;
        }
    };
    
    public static final RuntimeFieldFactory<Double> DOUBLE = new RuntimeFieldFactory<Double>(ID_DOUBLE)
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
                            output.writeDouble(number, f.getDouble(message), false);
                        else
                        {
                            Double value = (Double)f.get(message);
                            if(value!=null)
                                output.writeDouble(number, value.doubleValue(), false);
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
        protected Class<?> typeClass()
        {
            return Double.class;
        }
    };
    
    public static final RuntimeFieldFactory<Boolean> BOOL = new RuntimeFieldFactory<Boolean>(ID_BOOL)
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
                        if(primitive)
                            f.setBoolean(message, input.readBool());
                        else
                            f.set(message, input.readBool() ? Boolean.TRUE : Boolean.FALSE);
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
                            output.writeBool(number, f.getBoolean(message), false);
                        else
                        {
                            Boolean value = (Boolean)f.get(message);
                            if(value!=null)
                                output.writeBool(number, value.booleanValue(), false);
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
        protected Class<?> typeClass()
        {
            return Boolean.class;
        }
    };
    
    public static final RuntimeFieldFactory<String> STRING = new RuntimeFieldFactory<String>(ID_STRING)
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
                            output.writeString(number, value, false);
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
        protected Class<?> typeClass()
        {
            return String.class;
        }
    };
    
    public static final RuntimeFieldFactory<ByteString> BYTES = new RuntimeFieldFactory<ByteString>(ID_BYTES)
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
                            output.writeBytes(number, bs, false);
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
        protected Class<?> typeClass()
        {
            return ByteString.class;
        }
    };
    
    public static final RuntimeFieldFactory<byte[]> BYTE_ARRAY = new RuntimeFieldFactory<byte[]>(ID_BYTE_ARRAY)
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
                            output.writeByteArray(number, array, false);
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
        protected Class<?> typeClass()
        {
            return byte[].class;
        }
    };
    
    public static final RuntimeFieldFactory<Integer> ENUM = new RuntimeFieldFactory<Integer>(ID_ENUM)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            final EnumIO<? extends Enum<?>> eio = EnumIO.get(f.getType());
            return new Field<T>(FieldType.ENUM, number, name)
            {
                {
                    f.setAccessible(true);
                } 
                protected void mergeFrom(Input input, T message) throws IOException
                {
                    try
                    {
                        f.set(message, eio.readFrom(input));
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
                    final Enum<?> existing;
                    try
                    {
                        existing = (Enum<?>)f.get(message);
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
                        EnumIO.writeTo(output, number, repeated, existing);
                }
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    EnumIO.transfer(pipe, input, output, number, repeated);
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
        protected Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    // NON-INLINE VALUES
    
    static final RuntimeFieldFactory<Object> POJO = new RuntimeFieldFactory<Object>(ID_POJO)
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
        protected Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Object> POLYMORPHIC_POJO = new RuntimeFieldFactory<Object>(0)
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
                    FieldType.MESSAGE, number, name, false)
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
                        output.writeObject(number, existing, schema, false);
                }
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, schema.pipeSchema, false);
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
        protected Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
    static final RuntimeFieldFactory<Object> OBJECT = new RuntimeFieldFactory<Object>(ID_OBJECT)
    {
        public <T> Field<T> create(int number, java.lang.String name, 
                final java.lang.reflect.Field f)
        {
            return new RuntimeObjectField<T>( 
                    FieldType.MESSAGE, number, name, false)
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
                        output.writeObject(number, existing, schema, false);
                }
                protected void transfer(Pipe pipe, Input input, Output output, 
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, schema.pipeSchema, false);
                }
                protected void setValue(Object value, Object message)
                {
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
            return FieldType.MESSAGE;
        }
        protected Class<?> typeClass()
        {
            return Object.class;
        }
    };
    
    public static final RuntimeFieldFactory<BigDecimal> BIGDECIMAL = new RuntimeFieldFactory<BigDecimal>(ID_BIGDECIMAL)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
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
                            output.writeString(number, value.toString(), false);
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
        protected Class<?> typeClass()
        {
            return BigDecimal.class;
        }
    };
    
    public static final RuntimeFieldFactory<BigInteger> BIGINTEGER = new RuntimeFieldFactory<BigInteger>(ID_BIGINTEGER)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
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
                            output.writeByteArray(number, value.toByteArray(), false);
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
        protected Class<?> typeClass()
        {
            return BigInteger.class;
        }
    };
    
    public static final RuntimeFieldFactory<Date> DATE = new RuntimeFieldFactory<Date>(ID_DATE)
    {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            return new Field<T>(FieldType.FIXED64, number, name)
            {
                {
                    f.setAccessible(true);
                }
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
                            output.writeFixed64(number, value.getTime(), false);
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
            output.writeFixed64(number, input.readFixed64(), repeated);
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
        protected Class<?> typeClass()
        {
            return Date.class;
        }
    };
    
    static final RuntimeFieldFactory<Collection<?>> COLLECTION = 
        RuntimeSchema.COLLECTION_SCHEMA_ON_REPEATED_FIELDS ? 
                RuntimeCollectionFieldFactory.getFactory() : 
                    RuntimeRepeatedFieldFactory.getFactory();
    
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
    
    /**
     * Returns the factory for inline (scalar) values.
     */
    @SuppressWarnings("unchecked")
    static <T> RuntimeFieldFactory<T> getInline(String className)
    {
        return (RuntimeFieldFactory<T>)__inlineValues.get(className);
    }

}
