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

package com.dyuproject.protostuff.runtime;

import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BIGDECIMAL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BIGINTEGER;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BOOL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTES;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTE_ARRAY;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_CHAR;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_DATE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_STRING;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.ProtostuffException;
import com.dyuproject.protostuff.MapSchema.MapWrapper;
import com.dyuproject.protostuff.runtime.PolymorphicSchema.Handler;

/**
 * Built-in array schemas.
 * 
 * @author David Yu
 * @created Dec 4, 2012
 */
public final class ArraySchemas
{
    private ArraySchemas() {}
    
    /*public static final int CID_BOOL = 0, CID_BYTE = 1, CID_CHAR = 2, CID_SHORT = 3, 
            CID_INT32 = 4, CID_INT64 = 5, CID_FLOAT = 6, CID_DOUBLE = 7, 
            CID_STRING = 16, CID_BYTES = 17, CID_BYTE_ARRAY = 18, 
            CID_BIGDECIMAL = 19, CID_BIGINTEGER = 20, 
            CID_DATE = 21;*/
    
    static final int ID_ARRAY_LEN = 1, ID_ARRAY_DATA = 2;
    
    static final String STR_ARRAY_LEN = "a", STR_ARRAY_DATA = "b";
    
    static boolean isPrimitive(int arrayId)
    {
        return arrayId < 8;
    }
    
    static int toArrayId(int id, boolean primitive)
    {
        if(primitive)
            return id - 1;
        
        // if id < 9, its the primitive's boxed type.
        return id < 9 ? ((id - 1) | 0x08) : (id + 7);
    }
    
    static int toInlineId(int arrayId)
    {
        if(arrayId < 8)
            return arrayId + 1;
        
        if(arrayId < 16)
            return 1 + (arrayId & 0x07);
        
        return arrayId - 7;
    }
    
    static Base getSchema(int id, boolean primitive)
    {
        switch(id)
        {
            case ID_BOOL: return primitive ? BoolArray.PRIMITIVE : BoolArray.ELEMENT_SCHEMA;
            case ID_CHAR: return primitive ? CharArray.PRIMITIVE : CharArray.ELEMENT_SCHEMA;
            case ID_SHORT: return primitive ? ShortArray.PRIMITIVE : ShortArray.ELEMENT_SCHEMA;
            case ID_INT32: return primitive ? Int32Array.PRIMITIVE : Int32Array.ELEMENT_SCHEMA;
            case ID_INT64: return primitive ? Int64Array.PRIMITIVE : Int64Array.ELEMENT_SCHEMA;
            case ID_FLOAT: return primitive ? FloatArray.PRIMITIVE : FloatArray.ELEMENT_SCHEMA;
            case ID_DOUBLE: return primitive ? DoubleArray.PRIMITIVE : DoubleArray.ELEMENT_SCHEMA;
            case ID_STRING: return StringArray.ELEMENT_SCHEMA;
            case ID_BYTES: return ByteStringArray.ELEMENT_SCHEMA;
            case ID_BYTE_ARRAY: return ByteArrayArray.ELEMENT_SCHEMA;
            case ID_BIGDECIMAL: return BigDecimalArray.ELEMENT_SCHEMA;
            case ID_BIGINTEGER: return BigIntegerArray.ELEMENT_SCHEMA;
            case ID_DATE: return DateArray.ELEMENT_SCHEMA;
            default: throw new RuntimeException("Should not happen.");
        }
    }
    
    static Base getGenericElementSchema(int id)
    {
        switch(id)
        {
            case ID_BOOL: return BoolArray.ELEMENT_SCHEMA;
            case ID_CHAR: return CharArray.ELEMENT_SCHEMA;
            case ID_SHORT: return ShortArray.ELEMENT_SCHEMA;
            case ID_INT32: return Int32Array.ELEMENT_SCHEMA;
            case ID_INT64: return Int64Array.ELEMENT_SCHEMA;
            case ID_FLOAT: return FloatArray.ELEMENT_SCHEMA;
            case ID_DOUBLE: return DoubleArray.ELEMENT_SCHEMA;
            case ID_STRING: return StringArray.ELEMENT_SCHEMA;
            case ID_BYTES: return ByteStringArray.ELEMENT_SCHEMA;
            case ID_BYTE_ARRAY: return ByteArrayArray.ELEMENT_SCHEMA;
            case ID_BIGDECIMAL: return BigDecimalArray.ELEMENT_SCHEMA;
            case ID_BIGINTEGER: return BigIntegerArray.ELEMENT_SCHEMA;
            case ID_DATE: return DateArray.ELEMENT_SCHEMA;
            default: throw new RuntimeException("Should not happen.");
        }
    }
    
    static Base newSchema(int id, Class<?> compontentType, Class<?> typeClass, 
            IdStrategy strategy, final Handler handler)
    {
        switch(id)
        {
            case ID_BOOL: return new BoolArray(handler, compontentType.isPrimitive());
            case ID_CHAR: return new CharArray(handler, compontentType.isPrimitive());
            case ID_SHORT: return new ShortArray(handler, compontentType.isPrimitive());
            case ID_INT32: return new Int32Array(handler, compontentType.isPrimitive());
            case ID_INT64: return new Int64Array(handler, compontentType.isPrimitive());
            case ID_FLOAT: return new FloatArray(handler, compontentType.isPrimitive());
            case ID_DOUBLE: return new DoubleArray(handler, compontentType.isPrimitive());
            case ID_STRING: return new StringArray(handler);
            case ID_BYTES: return new ByteStringArray(handler);
            case ID_BYTE_ARRAY: return new ByteArrayArray(handler);
            case ID_BIGDECIMAL: return new BigDecimalArray(handler);
            case ID_BIGINTEGER: return new BigIntegerArray(handler);
            case ID_DATE: return new DateArray(handler);
            default: throw new RuntimeException("Should not happen.");
        }
    }
    
    static String name(int number)
    {
        switch(number)
        {
            case ID_ARRAY_LEN: return STR_ARRAY_LEN;
            case ID_ARRAY_DATA: return STR_ARRAY_DATA;
            default: return null;
        }
    }
    
    static int number(String name)
    {
        if(name.length() != 1)
            return 0;
        
        switch(name.charAt(0))
        {
            case 'a': return ID_ARRAY_LEN;
            case 'b': return ID_ARRAY_DATA;
            default: return 0;
        }
    }
    
    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe, 
            Input input, Output output, IdStrategy strategy, 
            Delegate<?> delegate) throws IOException
    {
        if(ID_ARRAY_LEN != input.readFieldNumber(pipeSchema.wrappedSchema))
            throw new ProtostuffException("Corrupt input.");
        
        final int len = input.readUInt32();
        // write it back
        output.writeUInt32(ID_ARRAY_LEN, len, false);
        
        for(int i = 0; i < len; i++)
        {
            if(ID_ARRAY_DATA != input.readFieldNumber(pipeSchema.wrappedSchema))
                throw new ProtostuffException("Corrupt input.");
            
            delegate.transfer(pipe, input, output, ID_ARRAY_DATA, true);
        }
        
        if(0 != input.readFieldNumber(pipeSchema.wrappedSchema))
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
        
        public String getFieldName(int number)
        {
            return name(number);
        }

        public int getFieldNumber(String name)
        {
            return number(name);
        }
        
        public String messageFullName()
        {
            return Array.class.getName();
        }

        public String messageName()
        {
            return Array.class.getSimpleName();
        }
        
        protected void setValue(Object value, Object owner)
        {
            handler.setValue(value, owner);
        }
        
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
        
        public static final BoolArray ELEMENT_SCHEMA = new BoolArray(null, false)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            if(primitive)
            {
                boolean[] array = new boolean[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readBool();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
            else
            {
                Boolean[] array = new Boolean[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readBool();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            if(primitive)
            {
                boolean[] array = (boolean[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeBool(ID_ARRAY_DATA, array[i], true);
            }
            else
            {
                Boolean[] array = (Boolean[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeBool(ID_ARRAY_DATA, array[i], true);
            }
        }
    }
    
    public static class CharArray extends Base
    {
        public static final CharArray PRIMITIVE = new CharArray(null, true);
        
        public static final CharArray ELEMENT_SCHEMA = new CharArray(null, false)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            if(primitive)
            {
                char[] array = new char[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = (char)input.readUInt32();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
            else
            {
                Character[] array = new Character[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = (char)input.readUInt32();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            if(primitive)
            {
                char[] array = (char[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeUInt32(ID_ARRAY_DATA, array[i], true);
            }
            else
            {
                Character[] array = (Character[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeUInt32(ID_ARRAY_DATA, array[i], true);
            }
        }
    }
    
    public static class ShortArray extends Base
    {
        public static final ShortArray PRIMITIVE = new ShortArray(null, true);
        
        public static final ShortArray ELEMENT_SCHEMA = new ShortArray(null, false)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            if(primitive)
            {
                short[] array = new short[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = (short)input.readUInt32();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
            else
            {
                Short[] array = new Short[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = (short)input.readUInt32();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            if(primitive)
            {
                short[] array = (short[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeUInt32(ID_ARRAY_DATA, array[i], true);
            }
            else
            {
                Short[] array = (Short[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeUInt32(ID_ARRAY_DATA, array[i], true);
            }
        }
    }
    
    public static class Int32Array extends Base
    {
        public static final Int32Array PRIMITIVE = new Int32Array(null, true);
        
        public static final Int32Array ELEMENT_SCHEMA = new Int32Array(null, false)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            if(primitive)
            {
                int[] array = new int[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readInt32();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
            else
            {
                Integer[] array = new Integer[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readInt32();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            if(primitive)
            {
                int[] array = (int[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeInt32(ID_ARRAY_DATA, array[i], true);
            }
            else
            {
                Integer[] array = (Integer[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeInt32(ID_ARRAY_DATA, array[i], true);
            }
        }
    }
    
    public static class Int64Array extends Base
    {
        public static final Int64Array PRIMITIVE = new Int64Array(null, true);
        
        public static final Int64Array ELEMENT_SCHEMA = new Int64Array(null, false)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            if(primitive)
            {
                long[] array = new long[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readInt64();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
            else
            {
                Long[] array = new Long[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readInt64();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            if(primitive)
            {
                long[] array = (long[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeInt64(ID_ARRAY_DATA, array[i], true);
            }
            else
            {
                Long[] array = (Long[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeInt64(ID_ARRAY_DATA, array[i], true);
            }
        }
    }
    
    public static class FloatArray extends Base
    {
        public static final FloatArray PRIMITIVE = new FloatArray(null, true);
        
        public static final FloatArray ELEMENT_SCHEMA = new FloatArray(null, false)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            if(primitive)
            {
                float[] array = new float[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readFloat();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
            else
            {
                Float[] array = new Float[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readFloat();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            if(primitive)
            {
                float[] array = (float[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeFloat(ID_ARRAY_DATA, array[i], true);
            }
            else
            {
                Float[] array = (Float[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeFloat(ID_ARRAY_DATA, array[i], true);
            }
        }
    }
    
    public static class DoubleArray extends Base
    {
        public static final DoubleArray PRIMITIVE = new DoubleArray(null, true);
        
        public static final DoubleArray ELEMENT_SCHEMA = new DoubleArray(null, false)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            if(primitive)
            {
                double[] array = new double[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readDouble();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
            else
            {
                Double[] array = new Double[len];
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(array, owner);
                }
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(this))
                        throw new ProtostuffException("Corrupt input.");
                    
                    array[i] = input.readDouble();
                }
                
                if(0 != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                return array;
            }
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            if(primitive)
            {
                double[] array = (double[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeDouble(ID_ARRAY_DATA, array[i], true);
            }
            else
            {
                Double[] array = (Double[])value;
                output.writeUInt32(ID_ARRAY_LEN, array.length, false);
                
                for(int i = 0, len = array.length; i < len; i++)
                    output.writeDouble(ID_ARRAY_DATA, array[i], true);
            }
        }
    }

    public static class StringArray extends Base
    {
        public static final StringArray ELEMENT_SCHEMA = new StringArray(null)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
            {
                transferObject(this, pipe, input, output, strategy, 
                        RuntimeFieldFactory.STRING);
            }
        };
        
        StringArray(Handler handler)
        {
            super(handler);
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            String[] array = new String[len];
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                array[i] = input.readString();
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            String[] array = (String[])value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);
            
            for(int i = 0, len = array.length; i < len; i++)
                output.writeString(ID_ARRAY_DATA, array[i], true);
        }
    }
    
    public static class ByteStringArray extends Base
    {
        public static final ByteStringArray ELEMENT_SCHEMA = new ByteStringArray(null)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
            {
                transferObject(this, pipe, input, output, strategy, 
                        RuntimeFieldFactory.BYTES);
            }
        };
        
        ByteStringArray(Handler handler)
        {
            super(handler);
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            ByteString[] array = new ByteString[len];
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                array[i] = input.readBytes();
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            ByteString[] array = (ByteString[])value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);
            
            for(int i = 0, len = array.length; i < len; i++)
                output.writeBytes(ID_ARRAY_DATA, array[i], true);
        }
    }
    
    public static class ByteArrayArray extends Base
    {
        public static final ByteArrayArray ELEMENT_SCHEMA = new ByteArrayArray(null)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
            {
                transferObject(this, pipe, input, output, strategy, 
                        RuntimeFieldFactory.BYTE_ARRAY);
            }
        };
        
        ByteArrayArray(Handler handler)
        {
            super(handler);
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            byte[][] array = new byte[len][];
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                array[i] = input.readByteArray();
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            byte[][] array = (byte[][])value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);
            
            for(int i = 0, len = array.length; i < len; i++)
                output.writeByteArray(ID_ARRAY_DATA, array[i], true);
        }
    }
    
    public static class BigDecimalArray extends Base
    {
        public static final BigDecimalArray ELEMENT_SCHEMA = new BigDecimalArray(null)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
            {
                transferObject(this, pipe, input, output, strategy, 
                        RuntimeFieldFactory.BIGDECIMAL);
            }
        };
        
        BigDecimalArray(Handler handler)
        {
            super(handler);
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            BigDecimal[] array = new BigDecimal[len];
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                array[i] = new BigDecimal(input.readString());
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            BigDecimal[] array = (BigDecimal[])value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);
            
            for(int i = 0, len = array.length; i < len; i++)
                output.writeString(ID_ARRAY_DATA, array[i].toString(), true);
        }
    }
    
    public static class BigIntegerArray extends Base
    {
        public static final BigIntegerArray ELEMENT_SCHEMA = new BigIntegerArray(null)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
            {
                transferObject(this, pipe, input, output, strategy, 
                        RuntimeFieldFactory.BIGINTEGER);
            }
        };
        
        BigIntegerArray(Handler handler)
        {
            super(handler);
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            BigInteger[] array = new BigInteger[len];
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                array[i] = new BigInteger(input.readByteArray());
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            BigInteger[] array = (BigInteger[])value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);
            
            for(int i = 0, len = array.length; i < len; i++)
                output.writeByteArray(ID_ARRAY_DATA, array[i].toByteArray(), true);
        }
    }
    
    public static class DateArray extends Base
    {
        public static final DateArray ELEMENT_SCHEMA = new DateArray(null)
        {
            @SuppressWarnings("unchecked")
            protected void setValue(Object value, Object owner)
            {
                if(MapWrapper.class == owner.getClass())
                    ((MapWrapper<Object, Object>)owner).setValue(value);
                else
                    ((Collection<Object>)owner).add(value);
            }
        };
        
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
            {
                transferObject(this, pipe, input, output, strategy, 
                        RuntimeFieldFactory.DATE);
            }
        };
        
        DateArray(Handler handler)
        {
            super(handler);
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            Date[] array = new Date[len];
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                array[i] = new Date(input.readFixed64());
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object value) throws IOException
        {
            Date[] array = (Date[])value;
            output.writeUInt32(ID_ARRAY_LEN, array.length, false);
            
            for(int i = 0, len = array.length; i < len; i++)
                output.writeFixed64(ID_ARRAY_DATA, array[i].getTime(), true);
        }
    }
    
    public static class DelegateArray extends Base
    {
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
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
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            Object array = Array.newInstance(delegate.typeClass(), len);
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                Array.set(array, i, delegate.readFrom(input));
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object array) throws IOException
        {
            final int len = Array.getLength(array);
            
            output.writeUInt32(ID_ARRAY_LEN, len, false);
            
            for(int i = 0; i < len; i++)
                delegate.writeTo(output, ID_ARRAY_DATA, Array.get(array, i), true);
        }
    }
    
    public static class EnumArray extends Base
    {
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) 
                    throws IOException
            {
                if(ID_ARRAY_LEN != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");
                
                final int len = input.readUInt32();
                // write it back
                output.writeUInt32(ID_ARRAY_LEN, len, false);
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(pipeSchema.wrappedSchema))
                        throw new ProtostuffException("Corrupt input.");
                    
                    EnumIO.transfer(pipe, input, output, ID_ARRAY_DATA, true);
                }
                
                if(0 != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");
            }
        };
        
        final EnumIO<?> eio;
        public EnumArray(Handler handler, EnumIO<?> eio)
        {
            super(handler);
            this.eio = eio;
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            Object array = Array.newInstance(eio.enumClass, len);
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                Array.set(array, i, eio.readFrom(input));
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object array) throws IOException
        {
            final int len = Array.getLength(array);
            
            output.writeUInt32(ID_ARRAY_LEN, len, false);
            
            for(int i = 0; i < len; i++)
                EnumIO.writeTo(output, ID_ARRAY_DATA, true, (Enum<?>)Array.get(array, i));
        }
    }
    
    public static class PojoArray extends Base
    {
        protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(this)
        {
            protected void transfer(Pipe pipe, Input input, Output output) 
                    throws IOException
            {
                if(ID_ARRAY_LEN != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");
                
                final int len = input.readUInt32();
                // write it back
                output.writeUInt32(ID_ARRAY_LEN, len, false);
                
                for(int i = 0; i < len; i++)
                {
                    if(ID_ARRAY_DATA != input.readFieldNumber(pipeSchema.wrappedSchema))
                        throw new ProtostuffException("Corrupt input.");
                    
                    output.writeObject(ID_ARRAY_DATA, pipe, hs.getPipeSchema(), true);
                }
                
                if(0 != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");
            }
        };
        
        final HasSchema<Object> hs;
        public PojoArray(Handler handler, HasSchema<Object> hs)
        {
            super(handler);
            this.hs = hs;
        }
        
        public Pipe.Schema<Object> getPipeSchema()
        {
            return pipeSchema;
        }
        
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if(ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            final int len = input.readUInt32();
            
            Object array = Array.newInstance(hs.getSchema().typeClass(), len);
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(array, owner);
            }
            
            for(int i = 0; i < len; i++)
            {
                if(ID_ARRAY_DATA != input.readFieldNumber(this))
                    throw new ProtostuffException("Corrupt input.");
                
                Array.set(array, i, input.mergeObject(null, hs.getSchema()));
            }
            
            if(0 != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            return array;
        }

        public void writeTo(Output output, Object array) throws IOException
        {
            final int len = Array.getLength(array);
            
            output.writeUInt32(ID_ARRAY_LEN, len, false);
            
            for(int i = 0; i < len; i++)
                output.writeObject(ID_ARRAY_DATA, Array.get(array, i), hs.getSchema(), true);
        }
    }
}
