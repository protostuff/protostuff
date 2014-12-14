//========================================================================
//Copyright 2012 David Yu
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

import static io.protostuff.runtime.RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGDECIMAL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGINTEGER;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BOOL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTES;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTE_ARRAY;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_CHAR;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DATE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_STRING;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import io.protostuff.ByteString;
import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.MapSchema.MapWrapper;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.runtime.PolymorphicSchema.Handler;

/**
 * Built-in array schemas.
 * 
 * @author David Yu
 * @created Dec 4, 2012
 */
public final class ArraySchemas
{
    private ArraySchemas()
    {
    }

    /*
     * public static final int CID_BOOL = 0, CID_BYTE = 1, CID_CHAR = 2, CID_SHORT = 3, CID_INT32 = 4, CID_INT64 = 5,
     * CID_FLOAT = 6, CID_DOUBLE = 7, CID_STRING = 16, CID_BYTES = 17, CID_BYTE_ARRAY = 18, CID_BIGDECIMAL = 19,
     * CID_BIGINTEGER = 20, CID_DATE = 21;
     */

    static final int ID_ARRAY_LEN = 1,
            ID_ARRAY_DATA = 2,
            ID_ARRAY_NULLCOUNT = 3;

    static final String STR_ARRAY_LEN = "a",
            STR_ARRAY_DATA = "b",
            STR_ARRAY_NULLCOUNT = "c";

    static boolean isPrimitive(int arrayId)
    {
        return arrayId < 8;
    }

    static int toArrayId(int id, boolean primitive)
    {
        if (primitive)
            return id - 1;

        // if id < 9, its the primitive's boxed type.
        return id < 9 ? ((id - 1) | 0x08) : (id + 7);
    }

    static int toInlineId(int arrayId)
    {
        if (arrayId < 8)
            return arrayId + 1;

        if (arrayId < 16)
            return 1 + (arrayId & 0x07);

        return arrayId - 7;
    }

    static Base getSchema(int id, boolean primitive)
    {
        switch (id)
        {
            case ID_BOOL:
                return primitive ? BoolArray.PRIMITIVE : BoolArray.ELEMENT_SCHEMA;
            case ID_CHAR:
                return primitive ? CharArray.PRIMITIVE : CharArray.ELEMENT_SCHEMA;
            case ID_SHORT:
                return primitive ? ShortArray.PRIMITIVE : ShortArray.ELEMENT_SCHEMA;
            case ID_INT32:
                return primitive ? Int32Array.PRIMITIVE : Int32Array.ELEMENT_SCHEMA;
            case ID_INT64:
                return primitive ? Int64Array.PRIMITIVE : Int64Array.ELEMENT_SCHEMA;
            case ID_FLOAT:
                return primitive ? FloatArray.PRIMITIVE : FloatArray.ELEMENT_SCHEMA;
            case ID_DOUBLE:
                return primitive ? DoubleArray.PRIMITIVE
                        : DoubleArray.ELEMENT_SCHEMA;
            case ID_STRING:
                return StringArray.ELEMENT_SCHEMA;
            case ID_BYTES:
                return ByteStringArray.ELEMENT_SCHEMA;
            case ID_BYTE_ARRAY:
                return ByteArrayArray.ELEMENT_SCHEMA;
            case ID_BIGDECIMAL:
                return BigDecimalArray.ELEMENT_SCHEMA;
            case ID_BIGINTEGER:
                return BigIntegerArray.ELEMENT_SCHEMA;
            case ID_DATE:
                return DateArray.ELEMENT_SCHEMA;
            default:
                throw new RuntimeException("Should not happen.");
        }
    }

    static Base getGenericElementSchema(int id)
    {
        switch (id)
        {
            case ID_BOOL:
                return BoolArray.ELEMENT_SCHEMA;
            case ID_CHAR:
                return CharArray.ELEMENT_SCHEMA;
            case ID_SHORT:
                return ShortArray.ELEMENT_SCHEMA;
            case ID_INT32:
                return Int32Array.ELEMENT_SCHEMA;
            case ID_INT64:
                return Int64Array.ELEMENT_SCHEMA;
            case ID_FLOAT:
                return FloatArray.ELEMENT_SCHEMA;
            case ID_DOUBLE:
                return DoubleArray.ELEMENT_SCHEMA;
            case ID_STRING:
                return StringArray.ELEMENT_SCHEMA;
            case ID_BYTES:
                return ByteStringArray.ELEMENT_SCHEMA;
            case ID_BYTE_ARRAY:
                return ByteArrayArray.ELEMENT_SCHEMA;
            case ID_BIGDECIMAL:
                return BigDecimalArray.ELEMENT_SCHEMA;
            case ID_BIGINTEGER:
                return BigIntegerArray.ELEMENT_SCHEMA;
            case ID_DATE:
                return DateArray.ELEMENT_SCHEMA;
            default:
                throw new RuntimeException("Should not happen.");
        }
    }

    static Base newSchema(int id, Class<?> compontentType, Class<?> typeClass,
            IdStrategy strategy, final Handler handler)
    {
        switch (id)
        {
            case ID_BOOL:
                return new BoolArray(handler, compontentType.isPrimitive());
            case ID_CHAR:
                return new CharArray(handler, compontentType.isPrimitive());
            case ID_SHORT:
                return new ShortArray(handler, compontentType.isPrimitive());
            case ID_INT32:
                return new Int32Array(handler, compontentType.isPrimitive());
            case ID_INT64:
                return new Int64Array(handler, compontentType.isPrimitive());
            case ID_FLOAT:
                return new FloatArray(handler, compontentType.isPrimitive());
            case ID_DOUBLE:
                return new DoubleArray(handler, compontentType.isPrimitive());
            case ID_STRING:
                return new StringArray(handler);
            case ID_BYTES:
                return new ByteStringArray(handler);
            case ID_BYTE_ARRAY:
                return new ByteArrayArray(handler);
            case ID_BIGDECIMAL:
                return new BigDecimalArray(handler);
            case ID_BIGINTEGER:
                return new BigIntegerArray(handler);
            case ID_DATE:
                return new DateArray(handler);
            default:
                throw new RuntimeException("Should not happen.");
        }
    }

    static String name(int number)
    {
        switch (number)
        {
            case ID_ARRAY_LEN:
                return STR_ARRAY_LEN;
            case ID_ARRAY_DATA:
                return STR_ARRAY_DATA;
            case ID_ARRAY_NULLCOUNT:
                return STR_ARRAY_NULLCOUNT;
            default:
                return null;
        }
    }

    static int number(String name)
    {
        if (name.length() != 1)
            return 0;

        switch (name.charAt(0))
        {
            case 'a':
                return ID_ARRAY_LEN;
            case 'b':
                return ID_ARRAY_DATA;
            case 'c':
                return ID_ARRAY_NULLCOUNT;
            default:
                return 0;
        }
    }

    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy,
            Delegate<?> delegate) throws IOException
    {
        if (ID_ARRAY_LEN != input.readFieldNumber(pipeSchema.wrappedSchema))
            throw new ProtostuffException("Corrupt input.");

        final int len = input.readUInt32();
        // write it back
        output.writeUInt32(ID_ARRAY_LEN, len, false);

        for (int i = 0, nullCount = 0; i < len;)
        {
            switch (input.readFieldNumber(pipeSchema.wrappedSchema))
            {
                case ID_ARRAY_DATA:
                    i++;
                    delegate.transfer(pipe, input, output, ID_ARRAY_DATA, true);
                    break;
                case ID_ARRAY_NULLCOUNT:
                    nullCount = input.readUInt32();
                    i += nullCount;
                    output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                    break;
                default:
                    throw new ProtostuffException("Corrupt input.");
            }
        }

        if (0 != input.readFieldNumber(pipeSchema.wrappedSchema))
            throw new ProtostuffException("Corrupt input.");
    }

    public static abstract class Base extends PolymorphicSchema
    {
        protected final Handler handler;

        public Base(final Handler handler)
        {
            super(null);

            this.handler = handler;
        }

        @Override
        public String getFieldName(int number)
        {
            return name(number);
        }

        @Override
        public int getFieldNumber(String name)
        {
            return number(name);
        }

        @Override
        public String messageFullName()
        {
            return Array.class.getName();
        }

        @Override
        public String messageName()
        {
            return Array.class.getSimpleName();
        }

        @Override
        protected void setValue(Object value, Object owner)
        {
            handler.setValue(value, owner);
        }

        @Override
        public void mergeFrom(Input input, Object owner) throws IOException
        {
            setValue(readFrom(input, owner), owner);
        }

        protected abstract Object readFrom(Input input, Object owner)
                throws IOException;
    }

    public static class BoolArray extends Base
    {
        public static final BoolArray PRIMITIVE = new BoolArray(null, true);

        public static final BoolArray ELEMENT_SCHEMA = new BoolArray(null,
                false)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.BOOL);
            }
        };

        final boolean primitive;

        BoolArray(Handler handler, boolean primitive)
        {
            super(handler);
            this.primitive = primitive;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            if (primitive)
            {
                boolean[] array = new boolean[len];
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(array, owner);
                }

                for (int i = 0; i < len; i++)
                {
                    if (ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");

                    array[i] = input.readBool();
                }

                if (0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");

                return array;
            }

            final Boolean[] array = new Boolean[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readBool();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            if (primitive)
            {
                boolean[] array = (boolean[]) value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);

                for (int i = 0, len = array.length; i < len; i++)
                    output.writeBool(ID_ARRAY_DATA, array[i], true);

                return;
            }

            final Boolean[] array = (Boolean[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Boolean v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeBool(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class CharArray extends Base
    {
        public static final CharArray PRIMITIVE = new CharArray(null, true);

        public static final CharArray ELEMENT_SCHEMA = new CharArray(null,
                false)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.CHAR);
            }
        };

        final boolean primitive;

        CharArray(Handler handler, boolean primitive)
        {
            super(handler);
            this.primitive = primitive;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            if (primitive)
            {
                char[] array = new char[len];
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(array, owner);
                }

                for (int i = 0; i < len; i++)
                {
                    if (ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");

                    array[i] = (char) input.readUInt32();
                }

                if (0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");

                return array;
            }

            final Character[] array = new Character[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = (char) input.readUInt32();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            if (primitive)
            {
                char[] array = (char[]) value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);

                for (int i = 0, len = array.length; i < len; i++)
                    output.writeUInt32(ID_ARRAY_DATA, array[i], true);

                return;
            }

            final Character[] array = (Character[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Character v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeUInt32(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class ShortArray extends Base
    {
        public static final ShortArray PRIMITIVE = new ShortArray(null, true);

        public static final ShortArray ELEMENT_SCHEMA = new ShortArray(null,
                false)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.SHORT);
            }
        };

        final boolean primitive;

        ShortArray(Handler handler, boolean primitive)
        {
            super(handler);
            this.primitive = primitive;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            if (primitive)
            {
                short[] array = new short[len];
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(array, owner);
                }

                for (int i = 0; i < len; i++)
                {
                    if (ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");

                    array[i] = (short) input.readUInt32();
                }

                if (0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");

                return array;
            }

            final Short[] array = new Short[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = (short) input.readUInt32();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            if (primitive)
            {
                short[] array = (short[]) value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);

                for (int i = 0, len = array.length; i < len; i++)
                    output.writeUInt32(ID_ARRAY_DATA, array[i], true);

                return;
            }

            final Short[] array = (Short[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Short v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeUInt32(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class Int32Array extends Base
    {
        public static final Int32Array PRIMITIVE = new Int32Array(null, true);

        public static final Int32Array ELEMENT_SCHEMA = new Int32Array(null,
                false)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.INT32);
            }
        };

        final boolean primitive;

        Int32Array(Handler handler, boolean primitive)
        {
            super(handler);
            this.primitive = primitive;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            if (primitive)
            {
                int[] array = new int[len];
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(array, owner);
                }

                for (int i = 0; i < len; i++)
                {
                    if (ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");

                    array[i] = input.readInt32();
                }

                if (0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");

                return array;
            }

            final Integer[] array = new Integer[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readInt32();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            if (primitive)
            {
                int[] array = (int[]) value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);

                for (int i = 0, len = array.length; i < len; i++)
                    output.writeInt32(ID_ARRAY_DATA, array[i], true);

                return;
            }

            final Integer[] array = (Integer[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Integer v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeInt32(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class Int64Array extends Base
    {
        public static final Int64Array PRIMITIVE = new Int64Array(null, true);

        public static final Int64Array ELEMENT_SCHEMA = new Int64Array(null,
                false)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.INT64);
            }
        };

        final boolean primitive;

        Int64Array(Handler handler, boolean primitive)
        {
            super(handler);
            this.primitive = primitive;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            if (primitive)
            {
                long[] array = new long[len];
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(array, owner);
                }

                for (int i = 0; i < len; i++)
                {
                    if (ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");

                    array[i] = input.readInt64();
                }

                if (0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");

                return array;
            }

            final Long[] array = new Long[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readInt64();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            if (primitive)
            {
                long[] array = (long[]) value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);

                for (int i = 0, len = array.length; i < len; i++)
                    output.writeInt64(ID_ARRAY_DATA, array[i], true);

                return;
            }

            final Long[] array = (Long[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Long v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeInt64(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class FloatArray extends Base
    {
        public static final FloatArray PRIMITIVE = new FloatArray(null, true);

        public static final FloatArray ELEMENT_SCHEMA = new FloatArray(null,
                false)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.FLOAT);
            }
        };

        final boolean primitive;

        FloatArray(Handler handler, boolean primitive)
        {
            super(handler);
            this.primitive = primitive;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            if (primitive)
            {
                float[] array = new float[len];
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(array, owner);
                }

                for (int i = 0; i < len; i++)
                {
                    if (ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");

                    array[i] = input.readFloat();
                }

                if (0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");

                return array;
            }

            final Float[] array = new Float[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readFloat();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            if (primitive)
            {
                float[] array = (float[]) value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);

                for (int i = 0, len = array.length; i < len; i++)
                    output.writeFloat(ID_ARRAY_DATA, array[i], true);

                return;
            }

            final Float[] array = (Float[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Float v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeFloat(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class DoubleArray extends Base
    {
        public static final DoubleArray PRIMITIVE = new DoubleArray(null, true);

        public static final DoubleArray ELEMENT_SCHEMA = new DoubleArray(null,
                false)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.DOUBLE);
            }
        };

        final boolean primitive;

        DoubleArray(Handler handler, boolean primitive)
        {
            super(handler);
            this.primitive = primitive;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            if (primitive)
            {
                double[] array = new double[len];
                if (input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(array, owner);
                }

                for (int i = 0; i < len; i++)
                {
                    if (ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");

                    array[i] = input.readDouble();
                }

                if (0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");

                return array;
            }

            final Double[] array = new Double[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readDouble();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            if (primitive)
            {
                double[] array = (double[]) value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);

                for (int i = 0, len = array.length; i < len; i++)
                    output.writeDouble(ID_ARRAY_DATA, array[i], true);

                return;
            }

            final Double[] array = (Double[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Double v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeDouble(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class StringArray extends Base
    {
        public static final StringArray ELEMENT_SCHEMA = new StringArray(null)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.STRING);
            }
        };

        StringArray(Handler handler)
        {
            super(handler);
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            String[] array = new String[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readString();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            String[] array = (String[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                String v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeString(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class ByteStringArray extends Base
    {
        public static final ByteStringArray ELEMENT_SCHEMA = new ByteStringArray(
                null)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.BYTES);
            }
        };

        ByteStringArray(Handler handler)
        {
            super(handler);
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            ByteString[] array = new ByteString[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readBytes();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            ByteString[] array = (ByteString[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                ByteString v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeBytes(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class ByteArrayArray extends Base
    {
        public static final ByteArrayArray ELEMENT_SCHEMA = new ByteArrayArray(
                null)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.BYTE_ARRAY);
            }
        };

        ByteArrayArray(Handler handler)
        {
            super(handler);
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            byte[][] array = new byte[len][];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = input.readByteArray();
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            byte[][] array = (byte[][]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                byte[] v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeByteArray(ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class BigDecimalArray extends Base
    {
        public static final BigDecimalArray ELEMENT_SCHEMA = new BigDecimalArray(
                null)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.BIGDECIMAL);
            }
        };

        BigDecimalArray(Handler handler)
        {
            super(handler);
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            BigDecimal[] array = new BigDecimal[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = new BigDecimal(input.readString());
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            BigDecimal[] array = (BigDecimal[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                BigDecimal v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeString(ID_ARRAY_DATA, v.toString(), true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class BigIntegerArray extends Base
    {
        public static final BigIntegerArray ELEMENT_SCHEMA = new BigIntegerArray(
                null)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.BIGINTEGER);
            }
        };

        BigIntegerArray(Handler handler)
        {
            super(handler);
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            BigInteger[] array = new BigInteger[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = new BigInteger(input.readByteArray());
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            BigInteger[] array = (BigInteger[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                BigInteger v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeByteArray(ID_ARRAY_DATA, v.toByteArray(), true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class DateArray extends Base
    {
        public static final DateArray ELEMENT_SCHEMA = new DateArray(null)
        {
            @Override
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if (MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>) owner).setValue(value);
                else
                    ((Collection<Object>) owner).add(value);
            }
        };

        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy,
                        RuntimeFieldFactory.DATE);
            }
        };

        DateArray(Handler handler)
        {
            super(handler);
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            Date[] array = new Date[len];
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        array[i++] = new Date(input.readFixed64());
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            Date[] array = (Date[]) value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);

            int nullCount = 0;
            for (int i = 0, len = array.length; i < len; i++)
            {
                Date v = array[i];
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeFixed64(ID_ARRAY_DATA, v.getTime(), true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class DelegateArray extends Base
    {
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                transferObject(this, pipe, input, output, strategy, delegate);
            }
        };

        final Delegate<Object> delegate;

        public DelegateArray(Handler handler, Delegate<Object> delegate)
        {
            super(handler);
            this.delegate = delegate;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            Object array = Array.newInstance(delegate.typeClass(), len);
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        Array.set(array, i++, delegate.readFrom(input));
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object array) throws IOException
        {
            final int len = Array.getLength(array);

            output.writeUInt32(ID_ARRAY_LEN, len, false);

            int nullCount = 0;
            for (int i = 0; i < len; i++)
            {
                Object v = Array.get(array, i);
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    delegate.writeTo(output, ID_ARRAY_DATA, v, true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class EnumArray extends Base
    {
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                if (ID_ARRAY_LEN != input
                        .readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");

                final int len = input.readUInt32();
                // write it back
                output.writeUInt32(ID_ARRAY_LEN, len, false);

                for (int i = 0, nullCount = 0; i < len;)
                {
                    switch (input.readFieldNumber(pipeSchema.wrappedSchema))
                    {
                        case ID_ARRAY_DATA:
                            i++;
                            EnumIO.transfer(pipe, input, output, ID_ARRAY_DATA, true);
                            break;
                        case ID_ARRAY_NULLCOUNT:
                            nullCount = input.readUInt32();
                            i += nullCount;
                            output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                            break;
                        default:
                            throw new ProtostuffException("Corrupt input.");
                    }
                }

                if (0 != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");
            }
        };

        final EnumIO<?> eio;

        public EnumArray(Handler handler, EnumIO<?> eio)
        {
            super(handler);
            this.eio = eio;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            Object array = Array.newInstance(eio.enumClass, len);
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        Array.set(array, i++, eio.readFrom(input));
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object array) throws IOException
        {
            final int len = Array.getLength(array);

            output.writeUInt32(ID_ARRAY_LEN, len, false);

            int nullCount = 0;
            for (int i = 0; i < len; i++)
            {
                Enum<?> v = (Enum<?>) Array.get(array, i);
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    eio.writeTo(output, ID_ARRAY_DATA, true, v);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }

    public static class PojoArray extends Base
    {
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
                this)
        {
            @Override
            protected void transfer(Pipe pipe, Input input, Output output)
                    throws IOException
            {
                if (ID_ARRAY_LEN != input
                        .readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");

                final int len = input.readUInt32();
                // write it back
                output.writeUInt32(ID_ARRAY_LEN, len, false);

                for (int i = 0, nullCount = 0; i < len;)
                {
                    switch (input.readFieldNumber(pipeSchema.wrappedSchema))
                    {
                        case ID_ARRAY_DATA:
                            i++;
                            output.writeObject(ID_ARRAY_DATA, pipe, hs.getPipeSchema(),
                                    true);
                            break;
                        case ID_ARRAY_NULLCOUNT:
                            nullCount = input.readUInt32();
                            i += nullCount;
                            output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                            break;
                        default:
                            throw new ProtostuffException("Corrupt input.");
                    }
                }

                if (0 != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");
            }
        };

        final HasSchema<Object> hs;

        public PojoArray(Handler handler, HasSchema<Object> hs)
        {
            super(handler);
            this.hs = hs;
        }

        @Override
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }

        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            final int len = input.readUInt32();

            Object array = Array.newInstance(hs.getSchema().typeClass(), len);
            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(array, owner);
            }

            for (int i = 0; i < len;)
            {
                switch (input.readFieldNumber(this))
                {
                    case ID_ARRAY_DATA:
                        Array.set(array, i++, input.mergeObject(null, hs.getSchema()));
                        break;
                    case ID_ARRAY_NULLCOUNT:
                        i += input.readUInt32();
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }

            if (0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");

            return array;
        }

        @Override
        public void writeTo(Output output, Object array) throws IOException
        {
            final int len = Array.getLength(array);

            output.writeUInt32(ID_ARRAY_LEN, len, false);

            int nullCount = 0;
            for (int i = 0; i < len; i++)
            {
                Object v = Array.get(array, i);
                if (v != null)
                {
                    if (nullCount != 0)
                    {
                        output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
                        nullCount = 0;
                    }

                    output.writeObject(ID_ARRAY_DATA, v, hs.getSchema(), true);
                }
                else if (ALLOW_NULL_ARRAY_ELEMENT)
                {
                    nullCount++;
                }
            }

            // if last element is null
            if (nullCount != 0)
                output.writeUInt32(ID_ARRAY_NULLCOUNT, nullCount, false);
        }
    }
}
