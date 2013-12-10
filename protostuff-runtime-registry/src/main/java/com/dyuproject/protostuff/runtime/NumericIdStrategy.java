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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.MapSchema;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;

/**
 * Base class for numeric id strategies.
 *
 * @author David Yu
 * @created Mar 28, 2012
 */
public abstract class NumericIdStrategy extends IdStrategy
{
    
    // class ids will be limited to 5 bits (written as the value 
    // of the key: RuntimeFieldFactory.ID_ARRAY)
    
    // Note that ID_ARRAY is used to write the int ids because the value 
    // is 15 (ID_ARRAY_MAPPED is 17), which means it only takes 1 byte to 
    // write.
    // This optimization will benefit workloads that have primitive arrays 
    // (including their boxed types) and arrays that have concrete component 
    // types (e.g not an interface or abstract class)
    
    // The same rules apply for ID_CLASS_ARRAY and ID_CLASS_ARRAY_MAPPED
    
    // primitive values are 0-7 (first 3 bits)
    // the 4th bit is the primitive flag
    
    // limited to 24 ids max
    protected static final int CID_BOOL = 0, CID_BYTE = 1, CID_CHAR = 2, CID_SHORT = 3, 
            CID_INT32 = 4, CID_INT64 = 5, CID_FLOAT = 6, CID_DOUBLE = 7, 
            CID_STRING = 16, CID_BYTES = 17, CID_BYTE_ARRAY = 18, 
            CID_BIGDECIMAL = 19, CID_BIGINTEGER = 20, 
            CID_DATE = 21, CID_OBJECT = 22, 
            CID_ENUM_SET = 23, CID_ENUM_MAP = 24, CID_ENUM = 25, 
            CID_COLLECTION = 26, CID_MAP = 27, 
            CID_POJO = 28, CID_CLASS = 29, CID_DELEGATE = 30;
    
    protected NumericIdStrategy(IdStrategy primaryGroup, int groupId)
    {
        super(primaryGroup, groupId);
    }
    
    protected void writeArrayIdTo(Output output, Class<?> componentType) 
            throws IOException
    {
        // shouldn't happen
        assert !componentType.isArray();
        
        final RegisteredDelegate<?> rd = getRegisteredDelegate(componentType);
        if(rd != null)
        {
            output.writeUInt32(RuntimeFieldFactory.ID_ARRAY, 
                    (rd.id << 5) | CID_DELEGATE, false);
            return;
        }
        
        final RuntimeFieldFactory<?> inline = 
                RuntimeFieldFactory.getInline(componentType);
        
        if(inline != null)
        {
            output.writeUInt32(RuntimeFieldFactory.ID_ARRAY, 
                    getPrimitiveOrScalarId(componentType, inline.id), false);
        }
        else if(componentType.isEnum())
        {
            output.writeUInt32(RuntimeFieldFactory.ID_ARRAY, 
                    getEnumId(componentType), false);
        }
        else if(Object.class == componentType)
        {
            output.writeUInt32(RuntimeFieldFactory.ID_ARRAY, 
                    CID_OBJECT, false);
        }
        else if(Class.class == componentType)
        {
            output.writeUInt32(RuntimeFieldFactory.ID_ARRAY, 
                    CID_CLASS, false);
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
    
    protected void writeClassIdTo(Output output, Class<?> componentType, boolean array) 
            throws IOException
    {
        // shouldn't happen
        assert !componentType.isArray();
        
        final int id = array ? 
                RuntimeFieldFactory.ID_CLASS_ARRAY : RuntimeFieldFactory.ID_CLASS;
        
        final RegisteredDelegate<?> rd = getRegisteredDelegate(componentType);
        if(rd != null)
        {
            output.writeUInt32(id, 
                    (rd.id << 5) | CID_DELEGATE, false);
            return;
        }
        
        final RuntimeFieldFactory<?> inline = 
                RuntimeFieldFactory.getInline(componentType);
        
        if(inline != null)
        {
            output.writeUInt32(id, 
                    getPrimitiveOrScalarId(componentType, inline.id), false);
        }
        else if(componentType.isEnum())
        {
            output.writeUInt32(id, 
                    getEnumId(componentType), false);
        }
        else if(Object.class == componentType)
        {
            output.writeUInt32(id, 
                    CID_OBJECT, false);
        }
        else if(Class.class == componentType)
        {
            output.writeUInt32(id, 
                    CID_CLASS, false);
        }
        else if(!componentType.isInterface() && 
                !Modifier.isAbstract(componentType.getModifiers()))
        {
            output.writeUInt32(id, 
                    getId(componentType), false);
        }
        else
        {
            // too many possible interfaces and abstract types that it would be costly 
            // to index it at runtime (Not all subclasses allow dynamic indexing)
            // mapped class ids are +1 from regular the regular ones
            output.writeString(id + 1, 
                    componentType.getName(), false);
        }
    }
    
    protected void transferClassId(Input input, Output output, int fieldNumber, 
            boolean mapped, boolean array) throws IOException
    {
        if(mapped)
            input.transferByteRangeTo(output, true, fieldNumber, false);
        else
            output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }
    
    protected Class<?> resolveClassFrom(Input input, boolean mapped, 
            boolean array) throws IOException 
    {
        return mapped ? RuntimeEnv.loadClass(input.readString()) : 
            resolveClass(input.readUInt32());
    }
    
    private static int getPrimitiveOrScalarId(Class<?> clazz, int id)
    {
        if(clazz.isPrimitive())
            return id - 1;
        
        // if id < 9, its the primitive's boxed type.
        return id < 9 ? ((id - 1) | 0x08) : (id + 7);
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
                case CID_BOOL: return primitive ? boolean.class : Boolean.class;
                case CID_BYTE: return primitive ? byte.class : Byte.class;
                case CID_CHAR: return primitive ? char.class : Character.class;
                case CID_SHORT: return primitive ? short.class : Short.class;
                case CID_INT32: return primitive ? int.class : Integer.class;
                case CID_INT64: return primitive ? long.class : Long.class;
                case CID_FLOAT: return primitive ? float.class : Float.class;
                case CID_DOUBLE: return primitive ? double.class : Double.class;
                default: throw new RuntimeException("Should not happen.");
            }
        }
        
        switch(type)
        {
            case CID_STRING: return String.class;
            case CID_BYTES: return ByteString.class;
            case CID_BYTE_ARRAY: return byte[].class;
            case CID_BIGDECIMAL: return BigDecimal.class;
            case CID_BIGINTEGER: return BigInteger.class;
            case CID_DATE: return Date.class;
            case CID_OBJECT: return Object.class;
            case CID_ENUM_SET: return EnumSet.class;
            case CID_ENUM_MAP: return EnumMap.class;
            case CID_ENUM:
                return enumClass(id >>> 5);
                    
            case CID_COLLECTION: 
                return collectionClass(id >>> 5);
                        
            case CID_MAP: 
                return mapClass(id >>> 5);
                
            case CID_POJO:
                return pojoClass(id >>> 5);
                
            case CID_CLASS:
                return Class.class;
                
            case CID_DELEGATE:
                return delegateClass(id >>> 5);
        }
        
        throw new RuntimeException("Should not happen.");
    }

    protected abstract RegisteredDelegate<?> getRegisteredDelegate(Class<?> clazz);
    
    protected abstract Class<?> enumClass(int id);
    
    protected abstract Class<?> delegateClass(int id);
    
    protected abstract Class<?> collectionClass(int id);
    
    protected abstract Class<?> mapClass(int id);
    
    protected abstract Class<?> pojoClass(int id);
    
    protected abstract int getEnumId(Class<?> clazz);
    
    protected abstract int getId(Class<?> clazz);
    
    protected static <T> ArrayList<T> newList(int size)
    {
        List<T> l = Collections.nCopies(size, null);
        return new ArrayList<T>(l);
    }
    
    protected static <T> void grow(ArrayList<T> list, int size)
    {
        int previousSize = list.size();
        
        list.ensureCapacity(size);
        List<T> l = Collections.nCopies(size - previousSize, null);
        list.addAll(l);
    }

    
    protected static final class RegisteredDelegate<T> extends HasDelegate<T>
    {
        public final int id;

        RegisteredDelegate(int id, Delegate<T> delegate)
        {
            super(delegate);
            
            this.id = id;
        }
    }
    
    /**
     * Register your pojos/enums/collections/maps/delegates here.
     */
    public interface Registry
    {
        /**
         * Collection ids start at 1.
         */
        public <T extends Collection<?>> Registry registerCollection(
                CollectionSchema.MessageFactory factory, int id);
        
        /**
         * Map ids start at 1.
         */
        public <T extends Map<?,?>> Registry registerMap(
                MapSchema.MessageFactory factory, int id);
        
        /**
         * Enum ids start at 1.
         */
        public <T extends Enum<T>> Registry registerEnum(Class<T> clazz, int id);
        
        /**
         * Enum ids start at 1.
         */
        public Registry registerEnum(EnumIO<?> eio, int id);
        
        /**
         * Pojo ids start at 1.
         */
        public <T> Registry registerPojo(Class<T> clazz, int id);
        
        /**
         * Pojo ids start at 1.
         */
        public <T> Registry registerPojo(Schema<T> schema, Pipe.Schema<T> pipeSchema, 
                int id);
        
        /**
         * If you are sure that you are only using a single implementation of 
         * your interface/abstract class, then it makes sense to map it directly 
         * to its impl class to avoid writing the type.
         * 
         * Note that the type is always written when your field is 
         * {@link java.lang.Object}. 
         * 
         * Pojo ids start at 1.
         */
        public <T> Registry mapPojo(Class<? super T> baseClass, Class<T> implClass);
        
        /**
         * Register a {@link Delegate} and assign an id.
         * 
         * Delegate ids start at 1.
         */
        public <T> Registry registerDelegate(Delegate<T> delegate, int id);
    }
    
}
