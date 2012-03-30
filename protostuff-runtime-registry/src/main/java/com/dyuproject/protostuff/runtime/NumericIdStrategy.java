//================================================================================
//Copyright (c) 2012, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================


package com.dyuproject.protostuff.runtime;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;

/**
 * Base class for numeric id strategies.
 *
 * @author David Yu
 * @created Mar 28, 2012
 */
public abstract class NumericIdStrategy extends IdStrategy
{
    
    // array ids will be limited to 5 bits (written as the value 
    // of the key: RuntimeFieldFactory.ID_ARRAY)
    
    // Note that ID_ARRAY is used to write the int ids because the value 
    // is 15 (ID_ARRAY_MAPPED is 17), which means it only takes 1 byte to 
    // write.
    // This optimization will benefit workloads that have primitive arrays 
    // (including their boxed types) and arrays that have concrete component 
    // types (e.g not an interface or abstract class)
    
    // primitive values are 0-7 (first 3 bits)
    // the 4th bit is the primitive flag
    
    // limited to 24 ids max
    protected static final int AID_BOOL = 0, AID_BYTE = 1, AID_CHAR = 2, AID_SHORT = 3, 
            AID_INT32 = 4, AID_INT64 = 5, AID_FLOAT = 6, AID_DOUBLE = 7, 
            AID_STRING = 16, AID_BYTES = 17, AID_BYTE_ARRAY = 18, 
            AID_BIGDECIMAL = 19, AID_BIGINTEGER = 20, 
            AID_DATE = 21, AID_OBJECT = 22, 
            AID_ENUM_SET = 23, AID_ENUM_MAP = 24, AID_ENUM = 25, 
            AID_COLLECTION = 26, AID_MAP = 27, 
            AID_POJO = 28;
    
    protected void writeArrayIdTo(Output output, Class<?> componentType) 
            throws IOException
    {
        // shouldn't happen
        assert !componentType.isArray();
        
        if(componentType.isPrimitive())
        {
            output.writeUInt32(RuntimeFieldFactory.ID_ARRAY, 
                    getIdPrimitive(componentType), false);
        }
        else if(!componentType.isInterface() && 
                !Modifier.isAbstract(componentType.getModifiers()))
        {
            output.writeUInt32(RuntimeFieldFactory.ID_ARRAY, 
                    getId(componentType), false);
        }
        else
        {
            // too many possible interfaces and abstract types that it would be costly 
            // to index it at runtime (Not all subclasses allow dynamic indexing)
            output.writeString(RuntimeFieldFactory.ID_ARRAY_MAPPED, 
                    componentType.getName(), false);
        }
    }
    
    protected void transferArrayId(Input input, Output output, int fieldNumber, 
            boolean mapped) throws IOException
    {
        if(mapped)
            input.transferByteRangeTo(output, true, fieldNumber, false);
        else
            output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }
    
    protected Class<?> resolveArrayComponentTypeFrom(Input input, boolean mapped) 
            throws IOException 
    {
        return mapped ? RuntimeEnv.loadClass(input.readString()) : 
            resolveClass(input.readUInt32());
    }
    
    private int getIdPrimitive(Class<?> clazz)
    {
        return RuntimeFieldFactory.getInline(clazz).id - 1;
    }
    
    private Class<?> resolveClass(int id)
    {
        final int type = id & 0x1F;
        if(type < 16)
        {
            final boolean primitive = type < 8;
            // first 3 bits
            switch(type & 0x07)
            {
                case AID_BOOL: return primitive ? boolean.class : Boolean.class;
                case AID_BYTE: return primitive ? byte.class : Byte.class;
                case AID_CHAR: return primitive ? char.class : Character.class;
                case AID_SHORT: return primitive ? short.class : Short.class;
                case AID_INT32: return primitive ? int.class : Integer.class;
                case AID_INT64: return primitive ? long.class : Long.class;
                case AID_FLOAT: return primitive ? float.class : Float.class;
                case AID_DOUBLE: return primitive ? double.class : Double.class;
                default: throw new RuntimeException("Should not happen.");
            }
        }
        
        switch(type)
        {
            case AID_STRING: return String.class;
            case AID_BYTES: return ByteString.class;
            case AID_BYTE_ARRAY: return byte[].class;
            case AID_BIGDECIMAL: return BigDecimal.class;
            case AID_BIGINTEGER: return BigInteger.class;
            case AID_DATE: return Date.class;
            case AID_OBJECT: return Object.class;
            case AID_ENUM_SET: return EnumSet.class;
            case AID_ENUM_MAP: return EnumMap.class;
            case AID_ENUM:
                return enumClass(id >>> 5);
                    
            case AID_COLLECTION: 
                return collectionClass(id >>> 5);
                        
            case AID_MAP: 
                return mapClass(id >>> 5);
                
            case AID_POJO:
                return pojoClass(id >>> 5);
        }
        
        throw new RuntimeException("Should not happen.");
    }

    protected abstract Class<?> enumClass(int id);
    
    protected abstract Class<?> collectionClass(int id);
    
    protected abstract Class<?> mapClass(int id);
    
    protected abstract Class<?> pojoClass(int id);
    
    protected abstract int getId(Class<?> clazz);
    
    
    protected static <T> ArrayList<T> newList(int size)
    {
        List<T> l = Collections.nCopies(size, null);
        return new ArrayList<T>(l);
    }

}
