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

import static com.dyuproject.protostuff.runtime.RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS;
import static com.dyuproject.protostuff.runtime.RuntimeEnv.MORPH_NON_FINAL_POJOS;
//import static com.dyuproject.protostuff.runtime.RuntimeEnv.USE_SUN_MISC_UNSAFE;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
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
        ID_BIGDECIMAL = 12, ID_BIGINTEGER = 13, ID_DATE = 14,
        ID_ARRAY = 15, // 1-15 is encoded as 1 byte on protobuf and protostuff format
        ID_OBJECT = 16, 
        ID_ARRAY_MAPPED = 17, 
        
        // room for more scalar types (18-21)
        
        ID_ENUM_SET = 22, 
        ID_ENUM_MAP = 23, 
        ID_ENUM = 24, 
        ID_COLLECTION = 25, 
        ID_MAP = 26, 
        // pojo fields limited to 126
        ID_POJO = 127;
    
    static final String STR_BOOL = "a", STR_BYTE = "b", STR_CHAR = "c", STR_SHORT = "d", 
        STR_INT32 = "e", STR_INT64 = "f", STR_FLOAT = "g", STR_DOUBLE = "h", 
        STR_STRING = "i", STR_BYTES = "j", STR_BYTE_ARRAY = "k", 
        STR_BIGDECIMAL = "l", STR_BIGINTEGER = "m", STR_DATE = "n", 
        STR_ARRAY = "o", 
        STR_OBJECT = "p", 
        STR_ARRAY_MAPPED = "q", 
        
        // room for more scalar types (18-21)
        
        STR_ENUM_SET = "v", 
        STR_ENUM_MAP = "w", 
        STR_ENUM = "x", 
        STR_COLLECTION = "y",
        STR_MAP = "z", 
        // pojo fields limited to 126
        STR_POJO = "_";
    
    
    private static final HashMap<String, RuntimeFieldFactory<?>> __inlineValues = 
        new HashMap<String, RuntimeFieldFactory<?>>();
    
    static final RuntimeFieldFactory<BigDecimal> BIGDECIMAL;
    static final RuntimeFieldFactory<BigInteger> BIGINTEGER;
    static final RuntimeFieldFactory<Boolean> BOOL;
    static final RuntimeFieldFactory<Byte> BYTE;
    static final RuntimeFieldFactory<ByteString> BYTES;
    static final RuntimeFieldFactory<byte[]> BYTE_ARRAY;
    static final RuntimeFieldFactory<Character> CHAR;
    static final RuntimeFieldFactory<Date> DATE;
    static final RuntimeFieldFactory<Double> DOUBLE;
    static final RuntimeFieldFactory<Float> FLOAT;
    static final RuntimeFieldFactory<Integer> INT32;
    static final RuntimeFieldFactory<Long> INT64;
    static final RuntimeFieldFactory<Short> SHORT;
    static final RuntimeFieldFactory<String> STRING;
    
    static final RuntimeFieldFactory<Integer> ENUM;
    static final RuntimeFieldFactory<Object> OBJECT;
    static final RuntimeFieldFactory<Object> POJO;
    static final RuntimeFieldFactory<Object> POLYMORPHIC_POJO;
    
    static final RuntimeFieldFactory<Collection<?>> COLLECTION;
    
    static
    {
        /*if(USE_SUN_MISC_UNSAFE)
        {
            BIGDECIMAL = RuntimeUnsafeFieldFactory.BIGDECIMAL;
            BIGINTEGER = RuntimeUnsafeFieldFactory.BIGINTEGER;
            BOOL = RuntimeUnsafeFieldFactory.BOOL;
            BYTE = RuntimeUnsafeFieldFactory.BYTE;
            BYTES = RuntimeUnsafeFieldFactory.BYTES;
            BYTE_ARRAY = RuntimeUnsafeFieldFactory.BYTE_ARRAY;
            CHAR = RuntimeUnsafeFieldFactory.CHAR;
            DATE = RuntimeUnsafeFieldFactory.DATE;
            DOUBLE = RuntimeUnsafeFieldFactory.DOUBLE;
            FLOAT = RuntimeUnsafeFieldFactory.FLOAT;
            INT32 = RuntimeUnsafeFieldFactory.INT32;
            INT64 = RuntimeUnsafeFieldFactory.INT64;
            SHORT = RuntimeUnsafeFieldFactory.SHORT;
            STRING = RuntimeUnsafeFieldFactory.STRING;
            
            ENUM = RuntimeUnsafeFieldFactory.ENUM;
            OBJECT = RuntimeUnsafeFieldFactory.OBJECT;
            POJO = RuntimeUnsafeFieldFactory.POJO;
            POLYMORPHIC_POJO = RuntimeUnsafeFieldFactory.POLYMORPHIC_POJO;
        }
        else
        {*/
            BIGDECIMAL = RuntimeReflectionFieldFactory.BIGDECIMAL;
            BIGINTEGER = RuntimeReflectionFieldFactory.BIGINTEGER;
            BOOL = RuntimeReflectionFieldFactory.BOOL;
            BYTE = RuntimeReflectionFieldFactory.BYTE;
            BYTES = RuntimeReflectionFieldFactory.BYTES;
            BYTE_ARRAY = RuntimeReflectionFieldFactory.BYTE_ARRAY;
            CHAR = RuntimeReflectionFieldFactory.CHAR;
            DATE = RuntimeReflectionFieldFactory.DATE;
            DOUBLE = RuntimeReflectionFieldFactory.DOUBLE;
            FLOAT = RuntimeReflectionFieldFactory.FLOAT;
            INT32 = RuntimeReflectionFieldFactory.INT32;
            INT64 = RuntimeReflectionFieldFactory.INT64;
            SHORT = RuntimeReflectionFieldFactory.SHORT;
            STRING = RuntimeReflectionFieldFactory.STRING;
            
            ENUM = RuntimeReflectionFieldFactory.ENUM;
            OBJECT = RuntimeReflectionFieldFactory.OBJECT;
            POJO = RuntimeReflectionFieldFactory.POJO;
            POLYMORPHIC_POJO = RuntimeReflectionFieldFactory.POLYMORPHIC_POJO;
        //}
        
        COLLECTION = COLLECTION_SCHEMA_ON_REPEATED_FIELDS ? 
                    RuntimeCollectionFieldFactory.getFactory() : 
                        RuntimeRepeatedFieldFactory.getFactory();
        
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
     * Gets the runtime field factory of the given {@code clazz}.
     * 
     * Method overload for backwards compatibility.
     */
    public static RuntimeFieldFactory<?> getFieldFactory(Class<?> clazz)
    {
        return getFieldFactory(clazz, RuntimeEnv.ID_STRATEGY);
    }

    /**
     * Gets the runtime field factory of the given {@code clazz}.
     */
    public static RuntimeFieldFactory<?> getFieldFactory(Class<?> clazz, 
            IdStrategy strategy)
    {
        if(Message.class.isAssignableFrom(clazz))
            return POJO;
        
        if(clazz.isEnum())
            return ENUM;
        
        final RuntimeFieldFactory<?> inline =  __inlineValues.get(clazz.getName());
        if(inline != null)
            return inline;
        
        // Of all the scalar (inline) fields, java.lang.Number is the only abstract
        // super type, hence we can filter it here
        // Note that it has 10 built-in subtypes
        if(clazz.isArray() || Object.class == clazz || Number.class == clazz)
            return OBJECT;
        
        if(Map.class.isAssignableFrom(clazz))
            return RuntimeMapFieldFactory.MAP;

        if(Collection.class.isAssignableFrom(clazz))
        {
            // repeated fields.
            return COLLECTION;
        }
        
        // Enums or boxed types of primitives do implement interfaces.
        // Although serializing polymorphic pojos declared as interfaces will be a 
        // little bit slower than before, this is more correct.
        //
        // In versions prior to 1.0.5, it would serialize a field declared as 
        // java.lang.Serializable like a polymorphic pojo even though it 
        // gets assigned enum/string/number types.
        //
        // If you have declared fields as serializable, it wont be compatible
        if(clazz.isInterface())
            return strategy.isRegistered(clazz) ? POJO : OBJECT;
        
        return POJO == pojo(clazz) || strategy.isRegistered(clazz) ? 
                POJO : POLYMORPHIC_POJO;
    }
    
    static RuntimeFieldFactory<?> pojo(Class<?> clazz)
    {
        return (Modifier.isAbstract(clazz.getModifiers()) 
            || (!Modifier.isFinal(clazz.getModifiers()) && 
                    MORPH_NON_FINAL_POJOS)) ? POLYMORPHIC_POJO : POJO;
    }
    
    static boolean isComplexComponentType(Class<?> clazz)
    {
        return clazz.isArray() || Object.class == clazz || Number.class == clazz;
    }

    static Class<?> getGenericType(java.lang.reflect.Field f, 
            int index, boolean checkByteArray)
    {
        try
        {
            Type type = ((ParameterizedType)f.getGenericType()).getActualTypeArguments()[index];
            // TODO find more efficient way to check byte[].class?
            return checkByteArray && "byte[]".equals(type.toString()) ? byte[].class : 
                (Class<?>)type;
        }
        catch(Exception e)
        {
            return null;
        }
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
            java.lang.reflect.Field field, IdStrategy strategy);
    
    protected abstract FieldType getFieldType();
    
    protected abstract V readFrom(Input input) throws IOException;
    
    protected abstract void writeTo(Output output, int number, V value, 
            boolean repeated) throws IOException;
    
    protected abstract void transfer(Pipe pipe, Input input, Output output, int number, 
            boolean repeated) throws IOException;
    
    protected abstract Class<?> typeClass();

}
