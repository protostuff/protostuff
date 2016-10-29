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

package io.protostuff.runtime;

import static io.protostuff.runtime.RuntimeEnv.USE_SUN_MISC_UNSAFE;
import io.protostuff.ByteString;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Morph;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.WireFormat.FieldType;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory to create runtime {@link Field fields} based on reflection.
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class RuntimeFieldFactory<V> implements Delegate<V>
{

    static final int ID_BOOL = 1, ID_BYTE = 2, ID_CHAR = 3, ID_SHORT = 4,
            ID_INT32 = 5, ID_INT64 = 6, ID_FLOAT = 7,
            ID_DOUBLE = 8,
            ID_STRING = 9,
            ID_BYTES = 10,
            ID_BYTE_ARRAY = 11,
            ID_BIGDECIMAL = 12,
            ID_BIGINTEGER = 13,
            ID_DATE = 14,
            ID_ARRAY = 15, // 1-15 is encoded as 1 byte on protobuf and
            // protostuff format
            ID_OBJECT = 16, ID_ARRAY_MAPPED = 17, ID_CLASS = 18,
            ID_CLASS_MAPPED = 19, ID_CLASS_ARRAY = 20,
            ID_CLASS_ARRAY_MAPPED = 21,

            ID_ENUM_SET = 22, ID_ENUM_MAP = 23, ID_ENUM = 24,
            ID_COLLECTION = 25, ID_MAP = 26,

            ID_POLYMORPHIC_COLLECTION = 28, ID_POLYMORPHIC_MAP = 29,
            ID_DELEGATE = 30,

            ID_ARRAY_DELEGATE = 32, ID_ARRAY_SCALAR = 33, ID_ARRAY_ENUM = 34,
            ID_ARRAY_POJO = 35,

            ID_THROWABLE = 52,

            // pojo fields limited to 126 if not explicitly using @Tag
            // annotations
            ID_POJO = 127;

    static final String STR_BOOL = "a", STR_BYTE = "b", STR_CHAR = "c",
            STR_SHORT = "d", STR_INT32 = "e", STR_INT64 = "f", STR_FLOAT = "g",
            STR_DOUBLE = "h", STR_STRING = "i", STR_BYTES = "j",
            STR_BYTE_ARRAY = "k", STR_BIGDECIMAL = "l", STR_BIGINTEGER = "m",
            STR_DATE = "n", STR_ARRAY = "o", STR_OBJECT = "p",
            STR_ARRAY_MAPPED = "q", STR_CLASS = "r", STR_CLASS_MAPPED = "s",
            STR_CLASS_ARRAY = "t", STR_CLASS_ARRAY_MAPPED = "u",

            STR_ENUM_SET = "v", STR_ENUM_MAP = "w", STR_ENUM = "x",
            STR_COLLECTION = "y", STR_MAP = "z",

            STR_POLYMORPHIC_COLLECTION = "B", STR_POLYMOPRHIC_MAP = "C",
            STR_DELEGATE = "D",

            STR_ARRAY_DELEGATE = "F", STR_ARRAY_SCALAR = "G",
            STR_ARRAY_ENUM = "H", STR_ARRAY_POJO = "I",

            STR_THROWABLE = "Z",

            // pojo fields limited to 126 if not explicitly using @Tag
            // annotations
            STR_POJO = "_";

    private static final HashMap<String, RuntimeFieldFactory<?>> __inlineValues = new HashMap<String, RuntimeFieldFactory<?>>();

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

    static final RuntimeFieldFactory<Collection<?>> COLLECTION = 
            new RuntimeFieldFactory<Collection<?>>(ID_COLLECTION)
    {
        @Override
        public <T> Field<T> create(int number, String name, java.lang.reflect.Field field,
                IdStrategy strategy)
        {
            final RuntimeFieldFactory<Collection<?>> factory = 
                    0 != (IdStrategy.COLLECTION_SCHEMA_ON_REPEATED_FIELDS & strategy.flags) ? 
                    RuntimeCollectionFieldFactory.getFactory() :
                    RuntimeRepeatedFieldFactory.getFactory();
                    
            return factory.create(number, name, field, strategy);
        }
        
        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Collection<?> value,
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

    static final RuntimeFieldFactory<Object> DELEGATE;
    
    // for repeated/collection fields.
    static final Accessor.Factory ACCESSOR_FACTORY;

    static
    {
        if (USE_SUN_MISC_UNSAFE)
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

            DELEGATE = RuntimeUnsafeFieldFactory.DELEGATE;
            ACCESSOR_FACTORY = UnsafeAccessor.FACTORY;
        }
        else
        {
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

            DELEGATE = RuntimeReflectionFieldFactory.DELEGATE;
            ACCESSOR_FACTORY = ReflectAccessor.FACTORY;
        }
        
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
     * <p>
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
        if (strategy.isDelegateRegistered(clazz))
            return DELEGATE;

        final RuntimeFieldFactory<?> inline = __inlineValues.get(clazz
                .getName());
        if (inline != null)
            return inline;

        if (Message.class.isAssignableFrom(clazz))
            return POJO;

        if (clazz.isEnum())
            return ENUM;

        // Of all the scalar (inline) fields, java.lang.Number is the only
        // abstract
        // super type, hence we can filter it here
        // Note that it has 10 built-in subtypes
        if (clazz.isArray() || Object.class == clazz || Number.class == clazz
                || Class.class == clazz || Enum.class == clazz
                || Throwable.class.isAssignableFrom(clazz))
        {
            return OBJECT;
        }
        
        if (strategy.isRegistered(clazz))
            return clazz.isInterface() ? POJO : POLYMORPHIC_POJO;
        
        if (Map.class.isAssignableFrom(clazz))
            return RuntimeMapFieldFactory.MAP;

        if (Collection.class.isAssignableFrom(clazz))
        {
            // repeated fields.
            return COLLECTION;
        }

        // Enums or boxed types of primitives do implement interfaces.
        // Although serializing polymorphic pojos declared as interfaces will be
        // a
        // little bit slower than before, this is more correct.
        //
        // In versions prior to 1.0.5, it would serialize a field declared as
        // java.lang.Serializable like a polymorphic pojo even though it
        // gets assigned enum/string/number types.
        //
        // If you have declared fields as serializable, it wont be compatible
        if (clazz.isInterface())
            return OBJECT;

        // checks delegated to POLYMORPHIC_POJO
        return POLYMORPHIC_POJO;
    }

    static boolean pojo(Class<?> clazz, Morph morph, IdStrategy strategy)
    {
        if (Modifier.isFinal(clazz.getModifiers()))
            return true;

        // check if user mapped an impl to this class
        if (Modifier.isAbstract(clazz.getModifiers()))
            return strategy.isRegistered(clazz);

        // the user can annotate fields with @Morph to have full control if
        // he knows a certain field will be set with a subtype.
        // To reverse the behavior (no subtype will be set), annotate with
        // @Morph(false)
        // This is an optimization that requires the user's full knowledge of
        // his dataset.
        if (morph != null)
            return !morph.value();

        return 0 == (IdStrategy.MORPH_NON_FINAL_POJOS & strategy.flags);
    }

    static Class<?> getGenericType(java.lang.reflect.Field f, int index)
    {
        try
        {
            Type type = ((ParameterizedType) f.getGenericType())
                    .getActualTypeArguments()[index];
            if (type instanceof GenericArrayType)
            {
                int dimensions = 1;
                Type componentType = ((GenericArrayType) type)
                        .getGenericComponentType();
                while (componentType instanceof GenericArrayType)
                {
                    dimensions++;
                    componentType = ((GenericArrayType) componentType)
                            .getGenericComponentType();
                }

                // TODO is there a more efficient way (reflection) to obtain an
                // array class?

                if (dimensions == 1)
                    return Array.newInstance((Class<?>) componentType, 0)
                            .getClass();

                final int[] arg = new int[dimensions];
                arg[0] = 0;
                return Array.newInstance((Class<?>) componentType, arg)
                        .getClass();
            }

            if (type instanceof ParameterizedType)
            {
                // TODO in the future, we can opt to do recursive type
                // inspection which can avoid including type metadata even with
                // very complex nested generic types (E.g a
                // List<List<List<String>>>).

                // special handling when generic type is either Class<?> or
                // Enum<?>
                Object rawType = ((ParameterizedType) type).getRawType();
                if (Class.class == rawType)
                    return Class.class;

                if (Enum.class == rawType)
                    return Enum.class;

                return null;
            }

            return (Class<?>) type;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    static <T> Delegate<T> getDelegateOrInline(Class<T> typeClass,
            IdStrategy strategy)
    {
        Delegate<T> d = strategy.getDelegate(typeClass);
        if (d == null)
            d = (RuntimeFieldFactory<T>) __inlineValues
                    .get(typeClass.getName());

        return d;
    }

    /**
     * Returns the factory for inline (scalar) values.
     */
    @SuppressWarnings("unchecked")
    public static <T> RuntimeFieldFactory<T> getInline(Class<T> typeClass)
    {
        return (RuntimeFieldFactory<T>) __inlineValues.get(typeClass.getName());
    }

    /**
     * Returns the factory for inline (scalar) values.
     */
    @SuppressWarnings("unchecked")
    static <T> RuntimeFieldFactory<T> getInline(String className)
    {
        return (RuntimeFieldFactory<T>) __inlineValues.get(className);
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
     * Creates a runtime {@link Field field} based on reflection.
     */
    public abstract <T> Field<T> create(int number, java.lang.String name,
            java.lang.reflect.Field field, IdStrategy strategy);

}
