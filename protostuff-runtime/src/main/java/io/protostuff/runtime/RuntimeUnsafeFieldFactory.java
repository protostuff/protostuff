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

import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGDECIMAL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGINTEGER;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BOOL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTES;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTE_ARRAY;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_CHAR;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DATE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DELEGATE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_ENUM;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_OBJECT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_POJO;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_STRING;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import io.protostuff.ByteString;
import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Morph;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;

/**
 * Field factory via sun.misc.Unsafe.
 * 
 * @author David Yu
 * @created Jul 7, 2011
 */
public final class RuntimeUnsafeFieldFactory
{

    static final sun.misc.Unsafe us = initUnsafe();

    private static sun.misc.Unsafe initUnsafe()
    {
        try
        {
            java.lang.reflect.Field f = sun.misc.Unsafe.class
                    .getDeclaredField("theUnsafe");

            f.setAccessible(true);

            return (sun.misc.Unsafe) f.get(null);
        }
        catch (Exception e)
        {
            // ignore

            /*
             * android 3.x try { java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("THE_ONE");
             * 
             * f.setAccessible(true);
             * 
             * return (sun.misc.Unsafe)f.get(null); } catch(Exception e1) { // ignore }
             */
        }

        return sun.misc.Unsafe.getUnsafe();
    }

    private RuntimeUnsafeFieldFactory()
    {
    }

    public static final RuntimeFieldFactory<Character> CHAR = new RuntimeFieldFactory<Character>(
            ID_CHAR)
    {

        @Override
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putChar(message, offset, (char) input.readUInt32());
                    else
                        us.putObject(message, offset,
                                Character.valueOf((char) input.readUInt32()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeUInt32(number, us.getChar(message, offset),
                                false);
                    else
                    {
                        Character value = (Character) us.getObject(message,
                                offset);
                        if (value != null)
                            output.writeUInt32(number, value.charValue(), false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }

        @Override
        public Character readFrom(Input input) throws IOException
        {
            return Character.valueOf((char) input.readUInt32());
        }

        @Override
        public void writeTo(Output output, int number, Character value,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, value.charValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }

        @Override
        public Class<?> typeClass()
        {
            return Character.class;
        }
    };

    public static final RuntimeFieldFactory<Short> SHORT = new RuntimeFieldFactory<Short>(
            ID_SHORT)
    {
        @Override
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putShort(message, offset, (short) input.readUInt32());
                    else
                        us.putObject(message, offset,
                                Short.valueOf((short) input.readUInt32()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeUInt32(number,
                                us.getShort(message, offset), false);
                    else
                    {
                        Short value = (Short) us.getObject(message, offset);
                        if (value != null)
                            output.writeUInt32(number, value.shortValue(),
                                    false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }

        @Override
        public Short readFrom(Input input) throws IOException
        {
            return Short.valueOf((short) input.readUInt32());
        }

        @Override
        public void writeTo(Output output, int number, Short value,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, value.shortValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }

        @Override
        public Class<?> typeClass()
        {
            return Short.class;
        }
    };

    public static final RuntimeFieldFactory<Byte> BYTE = new RuntimeFieldFactory<Byte>(
            ID_BYTE)
    {
        @Override
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putByte(message, offset, (byte) input.readUInt32());
                    else
                        us.putObject(message, offset,
                                Byte.valueOf((byte) input.readUInt32()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeUInt32(number, us.getByte(message, offset),
                                false);
                    else
                    {
                        Byte value = (Byte) us.getObject(message, offset);
                        if (value != null)
                            output.writeUInt32(number, value.byteValue(), false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }

        @Override
        public Byte readFrom(Input input) throws IOException
        {
            return Byte.valueOf((byte) input.readUInt32());
        }

        @Override
        public void writeTo(Output output, int number, Byte value,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, value.byteValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }

        @Override
        public Class<?> typeClass()
        {
            return Byte.class;
        }
    };

    public static final RuntimeFieldFactory<Integer> INT32 = new RuntimeFieldFactory<Integer>(
            ID_INT32)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.INT32, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putInt(message, offset, input.readInt32());
                    else
                        us.putObject(message, offset,
                                Integer.valueOf(input.readInt32()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeInt32(number, us.getInt(message, offset),
                                false);
                    else
                    {
                        Integer value = (Integer) us.getObject(message, offset);
                        if (value != null)
                            output.writeInt32(number, value.intValue(), false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeInt32(number, input.readInt32(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeInt32(number, input.readInt32(), repeated);
        }

        @Override
        public Integer readFrom(Input input) throws IOException
        {
            return Integer.valueOf(input.readInt32());
        }

        @Override
        public void writeTo(Output output, int number, Integer value,
                boolean repeated) throws IOException
        {
            output.writeInt32(number, value.intValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.INT32;
        }

        @Override
        public Class<?> typeClass()
        {
            return Integer.class;
        }
    };

    public static final RuntimeFieldFactory<Long> INT64 = new RuntimeFieldFactory<Long>(
            ID_INT64)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.INT64, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putLong(message, offset, input.readInt64());
                    else
                        us.putObject(message, offset,
                                Long.valueOf(input.readInt64()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeInt64(number, us.getLong(message, offset),
                                false);
                    else
                    {
                        Long value = (Long) us.getObject(message, offset);
                        if (value != null)
                            output.writeInt64(number, value.longValue(), false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeInt64(number, input.readInt64(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeInt64(number, input.readInt64(), repeated);
        }

        @Override
        public Long readFrom(Input input) throws IOException
        {
            return Long.valueOf(input.readInt64());
        }

        @Override
        public void writeTo(Output output, int number, Long value,
                boolean repeated) throws IOException
        {
            output.writeInt64(number, value.longValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.INT64;
        }

        @Override
        public Class<?> typeClass()
        {
            return Long.class;
        }
    };

    public static final RuntimeFieldFactory<Float> FLOAT = new RuntimeFieldFactory<Float>(
            ID_FLOAT)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.FLOAT, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putFloat(message, offset, input.readFloat());
                    else
                        us.putObject(message, offset,
                                new Float(input.readFloat()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeFloat(number, us.getFloat(message, offset),
                                false);
                    else
                    {
                        Float value = (Float) us.getObject(message, offset);
                        if (value != null)
                            output.writeFloat(number, value.floatValue(), false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeFloat(number, input.readFloat(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeFloat(number, input.readFloat(), repeated);
        }

        @Override
        public Float readFrom(Input input) throws IOException
        {
            return new Float(input.readFloat());
        }

        @Override
        public void writeTo(Output output, int number, Float value,
                boolean repeated) throws IOException
        {
            output.writeFloat(number, value.floatValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.FLOAT;
        }

        @Override
        public Class<?> typeClass()
        {
            return Float.class;
        }
    };

    public static final RuntimeFieldFactory<Double> DOUBLE = new RuntimeFieldFactory<Double>(
            ID_DOUBLE)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.DOUBLE, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putDouble(message, offset, input.readDouble());
                    else
                        us.putObject(message, offset,
                                new Double(input.readDouble()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeDouble(number,
                                us.getDouble(message, offset), false);
                    else
                    {
                        Double value = (Double) us.getObject(message, offset);
                        if (value != null)
                            output.writeDouble(number, value.doubleValue(),
                                    false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeDouble(number, input.readDouble(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeDouble(number, input.readDouble(), repeated);
        }

        @Override
        public Double readFrom(Input input) throws IOException
        {
            return new Double(input.readDouble());
        }

        @Override
        public void writeTo(Output output, int number, Double value,
                boolean repeated) throws IOException
        {
            output.writeDouble(number, value.doubleValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.DOUBLE;
        }

        @Override
        public Class<?> typeClass()
        {
            return Double.class;
        }
    };

    public static final RuntimeFieldFactory<Boolean> BOOL = new RuntimeFieldFactory<Boolean>(
            ID_BOOL)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BOOL, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    if (primitive)
                        us.putBoolean(message, offset, input.readBool());
                    else
                        us.putObject(message, offset,
                                input.readBool() ? Boolean.TRUE : Boolean.FALSE);
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    if (primitive)
                        output.writeBool(number,
                                us.getBoolean(message, offset), false);
                    else
                    {
                        Boolean value = (Boolean) us.getObject(message, offset);
                        if (value != null)
                            output.writeBool(number, value.booleanValue(),
                                    false);
                    }
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeBool(number, input.readBool(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeBool(number, input.readBool(), repeated);
        }

        @Override
        public Boolean readFrom(Input input) throws IOException
        {
            return input.readBool() ? Boolean.TRUE : Boolean.FALSE;
        }

        @Override
        public void writeTo(Output output, int number, Boolean value,
                boolean repeated) throws IOException
        {
            output.writeBool(number, value.booleanValue(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.BOOL;
        }

        @Override
        public Class<?> typeClass()
        {
            return Boolean.class;
        }
    };

    public static final RuntimeFieldFactory<String> STRING = new RuntimeFieldFactory<String>(
            ID_STRING)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.STRING, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset, input.readString());
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    CharSequence value = (CharSequence) us.getObject(message, offset);
                    if (value != null)
                        output.writeString(number, value, false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, true, number, repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, true, number, repeated);
        }

        @Override
        public String readFrom(Input input) throws IOException
        {
            return input.readString();
        }

        @Override
        public void writeTo(Output output, int number, String value,
                boolean repeated) throws IOException
        {
            output.writeString(number, (CharSequence) value, repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.STRING;
        }

        @Override
        public Class<?> typeClass()
        {
            return String.class;
        }
    };

    public static final RuntimeFieldFactory<ByteString> BYTES = new RuntimeFieldFactory<ByteString>(
            ID_BYTES)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset, input.readBytes());
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    ByteString bs = (ByteString) us.getObject(message, offset);
                    if (bs != null)
                        output.writeBytes(number, bs, false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }

        @Override
        public ByteString readFrom(Input input) throws IOException
        {
            return input.readBytes();
        }

        @Override
        public void writeTo(Output output, int number, ByteString value,
                boolean repeated) throws IOException
        {
            output.writeBytes(number, value, repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.BYTES;
        }

        @Override
        public Class<?> typeClass()
        {
            return ByteString.class;
        }
    };

    public static final RuntimeFieldFactory<byte[]> BYTE_ARRAY = new RuntimeFieldFactory<byte[]>(
            ID_BYTE_ARRAY)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset, input.readByteArray());
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    byte[] array = (byte[]) us.getObject(message, offset);
                    if (array != null)
                        output.writeByteArray(number, array, false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }

        @Override
        public byte[] readFrom(Input input) throws IOException
        {
            return input.readByteArray();
        }

        @Override
        public void writeTo(Output output, int number, byte[] value,
                boolean repeated) throws IOException
        {
            output.writeByteArray(number, value, repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.BYTES;
        }

        @Override
        public Class<?> typeClass()
        {
            return byte[].class;
        }
    };

    public static final RuntimeFieldFactory<Integer> ENUM = new RuntimeFieldFactory<Integer>(
            ID_ENUM)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, 
                final IdStrategy strategy)
        {
            final EnumIO<? extends Enum<?>> eio = strategy.getEnumIO(f
                    .getType());
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.ENUM, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset, eio.readFrom(input));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    final Enum<?> existing = (Enum<?>) us.getObject(message,
                            offset);
                    if (existing != null)
                        eio.writeTo(output, number, repeated, existing);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    EnumIO.transfer(pipe, input, output, number, repeated, strategy);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Integer value,
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

    // NON-INLINE VALUES

    static final RuntimeFieldFactory<Object> POJO = new RuntimeFieldFactory<Object>(
            ID_POJO)
    {
        @Override
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            Class<Object> type = (Class<Object>) f.getType();
            final long offset = us.objectFieldOffset(f);
            return new RuntimeMessageField<T, Object>(type,
                    strategy.getSchemaWrapper(type, true), FieldType.MESSAGE,
                    number, name, false, f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset, input.mergeObject(
                            us.getObject(message, offset), getSchema()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    final Object existing = us.getObject(message, offset);
                    if (existing != null)
                        output.writeObject(number, existing, getSchema(), false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, getPipeSchema(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Object value,
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

    static final RuntimeFieldFactory<Object> POLYMORPHIC_POJO = new RuntimeFieldFactory<Object>(
            0)
    {
        @Override
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            if (pojo(f.getType(), f.getAnnotation(Morph.class), strategy))
                return POJO.create(number, name, f, strategy);

            final long offset = us.objectFieldOffset(f);
            return new RuntimeDerivativeField<T>((Class<Object>) f.getType(),
                    FieldType.MESSAGE, number, name, false,
                    f.getAnnotation(Tag.class), strategy)
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    final Object value = input.mergeObject(message, schema);
                    if (input instanceof GraphInput
                            && ((GraphInput) input).isCurrentMessageReference())
                    {
                        // a reference from polymorphic+cyclic graph deser
                        us.putObject(message, offset, value);
                    }
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    final Object existing = us.getObject(message, offset);
                    if (existing != null)
                        output.writeObject(number, existing, schema, false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, schema.pipeSchema, false);
                }

                @Override
                public void doMergeFrom(Input input, Schema<Object> schema,
                        Object message) throws IOException
                {
                    final Object existing = us.getObject(message, offset);

                    // merge if not null and is same type.
                    final Object value = existing == null
                            || existing.getClass() != schema.typeClass() ? schema
                            .newMessage() : existing;

                    if (input instanceof GraphInput)
                    {
                        // update the actual reference.
                        ((GraphInput) input).updateLast(value, message);
                    }

                    schema.mergeFrom(input, value);

                    us.putObject(message, offset, value);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Object value,
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

    static final RuntimeFieldFactory<Object> OBJECT = new RuntimeFieldFactory<Object>(
            ID_OBJECT)
    {
        @Override
        public <T> Field<T> create(int number, java.lang.String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new RuntimeObjectField<T>(
                    f.getType(),
                    FieldType.MESSAGE,
                    number,
                    name,
                    false,
                    f.getAnnotation(Tag.class),
                    PolymorphicSchemaFactories.getFactoryFromField(f, strategy),
                    strategy)
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    final Object value = input.mergeObject(message, schema);
                    if (input instanceof GraphInput
                            && ((GraphInput) input).isCurrentMessageReference())
                    {
                        // a reference from polymorphic+cyclic graph deser
                        us.putObject(message, offset, value);
                    }
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    final Object existing = us.getObject(message, offset);
                    if (existing != null)
                        output.writeObject(number, existing, schema, false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeObject(number, pipe, schema.getPipeSchema(),
                            false);
                }

                @Override
                public void setValue(Object value, Object message)
                {
                    us.putObject(message, offset, value);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Object value,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.MESSAGE;
        }

        @Override
        public Class<?> typeClass()
        {
            return Object.class;
        }
    };

    public static final RuntimeFieldFactory<BigDecimal> BIGDECIMAL = new RuntimeFieldFactory<BigDecimal>(
            ID_BIGDECIMAL)
    {
        @Override
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.STRING, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset,
                            new BigDecimal(input.readString()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    BigDecimal value = (BigDecimal) us.getObject(message,
                            offset);
                    if (value != null)
                        output.writeString(number, value.toString(), false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, true, number, repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, true, number, repeated);
        }

        @Override
        public BigDecimal readFrom(Input input) throws IOException
        {
            return new BigDecimal(input.readString());
        }

        @Override
        public void writeTo(Output output, int number, BigDecimal value,
                boolean repeated) throws IOException
        {
            output.writeString(number, value.toString(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.STRING;
        }

        @Override
        public Class<?> typeClass()
        {
            return BigDecimal.class;
        }
    };

    public static final RuntimeFieldFactory<BigInteger> BIGINTEGER = new RuntimeFieldFactory<BigInteger>(
            ID_BIGINTEGER)
    {
        @Override
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset,
                            new BigInteger(input.readByteArray()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    BigInteger value = (BigInteger) us.getObject(message,
                            offset);
                    if (value != null)
                        output.writeByteArray(number, value.toByteArray(),
                                false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    input.transferByteRangeTo(output, false, number, repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, false, number, repeated);
        }

        @Override
        public BigInteger readFrom(Input input) throws IOException
        {
            return new BigInteger(input.readByteArray());
        }

        @Override
        public void writeTo(Output output, int number, BigInteger value,
                boolean repeated) throws IOException
        {
            output.writeByteArray(number, value.toByteArray(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.BYTES;
        }

        @Override
        public Class<?> typeClass()
        {
            return BigInteger.class;
        }
    };

    public static final RuntimeFieldFactory<Date> DATE = new RuntimeFieldFactory<Date>(
            ID_DATE)
    {
        @Override
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.FIXED64, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset, new Date(input.readFixed64()));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    Date value = (Date) us.getObject(message, offset);
                    if (value != null)
                        output.writeFixed64(number, value.getTime(), false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    output.writeFixed64(number, input.readFixed64(), repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeFixed64(number, input.readFixed64(), repeated);
        }

        @Override
        public Date readFrom(Input input) throws IOException
        {
            return new Date(input.readFixed64());
        }

        @Override
        public void writeTo(Output output, int number, Date value,
                boolean repeated) throws IOException
        {
            output.writeFixed64(number, value.getTime(), repeated);
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.FIXED64;
        }

        @Override
        public Class<?> typeClass()
        {
            return Date.class;
        }
    };

    public static final RuntimeFieldFactory<Object> DELEGATE = new RuntimeFieldFactory<Object>(
            ID_DELEGATE)
    {
        @Override
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final Delegate<Object> delegate = strategy
                    .getDelegate((Class<Object>) f.getType());

            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BYTES, number, name,
                    f.getAnnotation(Tag.class))
            {
                @Override
                public void mergeFrom(Input input, T message)
                        throws IOException
                {
                    us.putObject(message, offset, delegate.readFrom(input));
                }

                @Override
                public void writeTo(Output output, T message)
                        throws IOException
                {
                    final Object value = us.getObject(message, offset);
                    if (value != null)
                        delegate.writeTo(output, number, value, false);
                }

                @Override
                public void transfer(Pipe pipe, Input input, Output output,
                        boolean repeated) throws IOException
                {
                    delegate.transfer(pipe, input, output, number, repeated);
                }
            };
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Object value,
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
