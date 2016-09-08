//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

import io.protostuff.CollectionSchema;
import io.protostuff.Input;
import io.protostuff.MapSchema;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Tag;
import io.protostuff.runtime.PolymorphicSchema.Handler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Determines how enums are serialized/deserialized. Default is BY_NUMBER. To enable BY_NAME, set the property
 * "protostuff.runtime.enums_by_name=true".
 *
 * @author David Yu
 * @created Oct 20, 2010
 */
public abstract class EnumIO<E extends Enum<E>> implements
        PolymorphicSchema.Factory
{

    // Used by ObjectSchema to ser/deser both EnumMap and EnumSet.
    private static final java.lang.reflect.Field __keyTypeFromEnumMap;
    private static final java.lang.reflect.Field __elementTypeFromEnumSet;

    static
    {
        boolean success = false;
        java.lang.reflect.Field keyTypeFromMap = null, valueTypeFromSet = null;
        try
        {
            keyTypeFromMap = EnumMap.class.getDeclaredField("keyType");
            keyTypeFromMap.setAccessible(true);
            valueTypeFromSet = EnumSet.class.getDeclaredField("elementType");
            valueTypeFromSet.setAccessible(true);
            success = true;
        }
        catch (Exception e)
        {
            // ignore
        }

        __keyTypeFromEnumMap = success ? keyTypeFromMap : null;
        __elementTypeFromEnumSet = success ? valueTypeFromSet : null;
    }

    /**
     * Retrieves the enum key type from the EnumMap via reflection. This is used by {@link ObjectSchema}.
     */
    static Class<?> getKeyTypeFromEnumMap(Object enumMap)
    {
        if (__keyTypeFromEnumMap == null)
        {
            throw new RuntimeException(
                    "Could not access (reflection) the private "
                            + "field *keyType* (enumClass) from: class java.util.EnumMap");
        }

        try
        {
            return (Class<?>) __keyTypeFromEnumMap.get(enumMap);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the enum key type from the EnumMap via reflection. This is used by {@link ObjectSchema}.
     */
    static Class<?> getElementTypeFromEnumSet(Object enumSet)
    {
        if (__elementTypeFromEnumSet == null)
        {
            throw new RuntimeException(
                    "Could not access (reflection) the private "
                            + "field *elementType* (enumClass) from: class java.util.EnumSet");
        }

        try
        {
            return (Class<?>) __elementTypeFromEnumSet.get(enumSet);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static EnumIO<? extends Enum<?>> newEnumIO(Class<?> enumClass, IdStrategy strategy)
    {
        return 0 == (IdStrategy.ENUMS_BY_NAME & strategy.flags) ? 
                new ByNumber(enumClass, strategy) : new ByName(enumClass, strategy);
    }

    /**
     * Writes the {@link Enum} to the output.
     */
    public void writeTo(Output output, int number, boolean repeated,
            Enum<?> e) throws IOException
    {
        if (0 == (IdStrategy.ENUMS_BY_NAME & strategy.flags))
            output.writeEnum(number, getTag(e), repeated);
        else
            output.writeString(number, getAlias(e), repeated);
    }

    /**
     * Transfers the {@link Enum} from the input to the output.
     */
    public static void transfer(Pipe pipe, Input input, Output output,
            int number, boolean repeated, IdStrategy strategy) throws IOException
    {
        if (0 == (IdStrategy.ENUMS_BY_NAME & strategy.flags))
            output.writeEnum(number, input.readEnum(), repeated);
        else
            input.transferByteRangeTo(output, true, number, repeated);
    }

    private static <E extends Enum<E>> CollectionSchema.MessageFactory newEnumSetFactory(
            final EnumIO<E> eio)
    {
        return new CollectionSchema.MessageFactory()
        {
            @Override
            @SuppressWarnings("unchecked")
            public <V> Collection<V> newMessage()
            {
                return (Collection<V>) eio.newEnumSet();
            }

            @Override
            public Class<?> typeClass()
            {
                return EnumSet.class;
            }
        };
    }

    private static <E extends Enum<E>> MapSchema.MessageFactory newEnumMapFactory(
            final EnumIO<E> eio)
    {
        return new MapSchema.MessageFactory()
        {
            @Override
            @SuppressWarnings("unchecked")
            public <K, V> Map<K, V> newMessage()
            {
                return (Map<K, V>) eio.newEnumMap();
            }

            @Override
            public Class<?> typeClass()
            {
                return EnumMap.class;
            }
        };
    }

    /**
     * The enum class.
     */
    public final Class<E> enumClass;
    public final IdStrategy strategy;
    public final ArraySchemas.Base genericElementSchema;
    
    private volatile CollectionSchema.MessageFactory enumSetFactory;
    private volatile MapSchema.MessageFactory enumMapFactory;
    
    private final String[] alias;
    private final int[] tag;

    private final Map<String, E> valueByAliasMap;
    private final Map<Integer, E> valueByTagMap;

    public EnumIO(Class<E> enumClass, IdStrategy strategy)
    {
        this.enumClass = enumClass;
        this.strategy = strategy;
        genericElementSchema = new ArraySchemas.EnumArray(strategy, null, this);
        
        Field[] fields = enumClass.getFields();
        int n = fields.length;
        alias = new String[n];
        tag = new int[n];
        valueByAliasMap = new HashMap<String, E>(n * 2);
        valueByTagMap = new HashMap<Integer, E>(n * 2);
        for (E instance : enumClass.getEnumConstants())
        {
            int ordinal = instance.ordinal();
            try
            {
                Field field = enumClass.getField(instance.name());
                if (field.isAnnotationPresent(Tag.class))
                {
                    Tag annotation = field.getAnnotation(Tag.class);
                    tag[ordinal] = annotation.value();
                    alias[ordinal] = annotation.alias();
                    valueByTagMap.put(annotation.value(), instance);
                    valueByAliasMap.put(annotation.alias(), instance);
                }
                else
                {
                    tag[ordinal] = ordinal;
                    alias[ordinal] = field.getName();
                    valueByTagMap.put(ordinal, instance);
                    valueByAliasMap.put(field.getName(), instance);
                }
            }
            catch (NoSuchFieldException e)
            {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public PolymorphicSchema newSchema(Class<?> typeClass, IdStrategy strategy,
            Handler handler)
    {
        return new ArraySchemas.EnumArray(strategy, handler, this);
    }

    public int getTag(Enum<?> element)
    {
        return tag[element.ordinal()];
    }

    public String getAlias(Enum<?> element)
    {
        return alias[element.ordinal()];
    }

    public E getByTag(int tag)
    {
        return valueByTagMap.get(tag);
    }

    public E getByAlias(String alias)
    {
        return valueByAliasMap.get(alias);
    }

    /**
     * Returns the factory for an EnumSet (lazy).
     */
    public CollectionSchema.MessageFactory getEnumSetFactory()
    {
        CollectionSchema.MessageFactory enumSetFactory = this.enumSetFactory;
        if (enumSetFactory == null)
        {
            synchronized (this)
            {
                if ((enumSetFactory = this.enumSetFactory) == null)
                    this.enumSetFactory = enumSetFactory = newEnumSetFactory(this);
            }
        }
        return enumSetFactory;
    }

    /**
     * Returns the factory for an EnumMap (lazy).
     */
    public MapSchema.MessageFactory getEnumMapFactory()
    {
        MapSchema.MessageFactory enumMapFactory = this.enumMapFactory;
        if (enumMapFactory == null)
        {
            synchronized (this)
            {
                if ((enumMapFactory = this.enumMapFactory) == null)
                    this.enumMapFactory = enumMapFactory = newEnumMapFactory(this);
            }
        }
        return enumMapFactory;
    }

    /**
     * Returns an empty {@link EnumSet}.
     */
    public EnumSet<E> newEnumSet()
    {
        return EnumSet.noneOf(enumClass);
    }

    /**
     * Returns an empty {@link EnumMap}.
     */
    public <V> EnumMap<E, V> newEnumMap()
    {
        return new EnumMap<E, V>(enumClass);
    }

    /**
     * Read the enum from the input.
     */
    public abstract E readFrom(Input input) throws IOException;

    /**
     * Reads the enum by its name.
     */
    public static final class ByName<E extends Enum<E>> extends EnumIO<E>
    {
        public ByName(Class<E> enumClass, IdStrategy strategy)
        {
            super(enumClass, strategy);
        }

        @Override
        public E readFrom(Input input) throws IOException
        {
            String alias = input.readString();
            return getByAlias(alias);
        }
    }

    /**
     * Reads the enum by its number.
     */
    public static final class ByNumber<E extends Enum<E>> extends EnumIO<E>
    {
        public ByNumber(Class<E> enumClass, IdStrategy strategy)
        {
            super(enumClass, strategy);
        }

        @Override
        public E readFrom(Input input) throws IOException
        {
            int tag = input.readEnum();
            return getByTag(tag);
        }
    }

}
