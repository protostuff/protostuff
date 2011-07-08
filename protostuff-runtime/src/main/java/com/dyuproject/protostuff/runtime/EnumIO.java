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

package com.dyuproject.protostuff.runtime;

import static com.dyuproject.protostuff.runtime.RuntimeEnv.ENUMS_BY_NAME;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.MapSchema;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;

/**
 * Determines how enums are serialized/deserialized. 
 * Default is BY_NUMBER. 
 * To enable BY_NAME, set the property "protostuff.runtime.enums_by_name=true".
 *
 * @author David Yu
 * @created Oct 20, 2010
 */
public abstract class EnumIO<E extends Enum<E>>
{
    
    /**
     * A cache to prevent creating the same eio over and over.
     */
    private static final ConcurrentHashMap<String,EnumIO<?>> __eioCache = 
        new ConcurrentHashMap<String,EnumIO<?>>();
    
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
        catch(Exception e)
        {
            // ignore
        }
        
        __keyTypeFromEnumMap = success ? keyTypeFromMap : null;
        __elementTypeFromEnumSet = success ? valueTypeFromSet : null;
    }
    
    /**
     * Retrieves the enum key type from the EnumMap via reflection.
     * This is used by {@link ObjectSchema}.
     */
    static Class<?> getKeyTypeFromEnumMap(Object enumMap)
    {
        if(__keyTypeFromEnumMap == null)
        {
            throw new RuntimeException("Could not access (reflection) the private " +
                    "field *keyType* (enumClass) from: class java.util.EnumMap");
        }
        
        try
        {
            return (Class<?>)__keyTypeFromEnumMap.get(enumMap);
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
    
    /**
     * Retrieves the enum key type from the EnumMap via reflection.
     * This is used by {@link ObjectSchema}.
     */
    static Class<?> getElementTypeFromEnumSet(Object enumSet)
    {
        if(__elementTypeFromEnumSet == null)
        {
            throw new RuntimeException("Could not access (reflection) the private " +
                    "field *elementType* (enumClass) from: class java.util.EnumSet");
        }
        
        try
        {
            return (Class<?>)__elementTypeFromEnumSet.get(enumSet);
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
    
    
    @SuppressWarnings("unchecked")
    static EnumIO<? extends Enum<?>> get(Class<?> enumClass)
    {
        EnumIO<?> eio = __eioCache.get(enumClass.getName());
        if(eio == null)
        {
            eio = ENUMS_BY_NAME ? new ByName(enumClass) : new ByNumber(enumClass);
            
            final EnumIO<?> existing = __eioCache.putIfAbsent(enumClass.getName(), eio);
            if(existing != null)
                eio = existing;
        }
        
        return eio;
    }
    
    @SuppressWarnings("unchecked")
    static EnumIO<? extends Enum<?>> get(String className, boolean load)
    {
        EnumIO<?> eio = __eioCache.get(className);
        if(eio == null)
        {
            if(!load)
                return null;
            
            final Class<?> enumClass;
            try
            {
                enumClass = Thread.currentThread().getContextClassLoader().loadClass(
                        className);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            
            eio = ENUMS_BY_NAME ? new ByName(enumClass) : new ByNumber(enumClass);
            
            final EnumIO<?> existing = __eioCache.putIfAbsent(enumClass.getName(), eio);
            if(existing != null)
                eio = existing;
        }
        
        return eio;
    }
    
    /**
     * Writes the {@link Enum} to the output.
     */
    public static void writeTo(Output output, int number, boolean repeated, Enum<?> e) 
    throws IOException
    {
        if(ENUMS_BY_NAME)
            output.writeString(number, e.name(), repeated);
        else
            output.writeEnum(number, e.ordinal(), repeated);
    }
    
    /**
     * Transfers the {@link Enum} from the input to the output.
     */
    public static void transfer(Pipe pipe, Input input, Output output, int number, 
            boolean repeated) throws IOException
    {
        if(ENUMS_BY_NAME)
            input.transferByteRangeTo(output, true, number, repeated);
        else
            output.writeEnum(number, input.readEnum(), repeated);
    }
    
    private static <E extends Enum<E>> CollectionSchema.MessageFactory newEnumSetFactory(
            final EnumIO<E> eio)
    {
        return new CollectionSchema.MessageFactory()
        {
            @SuppressWarnings("unchecked")
            public <V> Collection<V> newMessage()
            {
                return (Collection<V>)eio.newEnumSet();
            }
        };
    }
    
    private static <E extends Enum<E>> MapSchema.MessageFactory newEnumMapFactory(
            final EnumIO<E> eio)
    {
        return new MapSchema.MessageFactory()
        {
            @SuppressWarnings("unchecked")
            public <K, V> Map<K, V> newMessage()
            {
                return (Map<K,V>)eio.newEnumMap();
            }
        };
    }
    
    /**
     * The enum class.
     */
    public final Class<E> enumClass;
    private volatile CollectionSchema.MessageFactory enumSetFactory;
    private volatile MapSchema.MessageFactory enumMapFactory;
    
    public EnumIO(Class<E> enumClass)
    {
        this.enumClass = enumClass;
    }
    
    /**
     * Returns the factory for an EnumSet (lazy).
     */
    public CollectionSchema.MessageFactory getEnumSetFactory()
    {
        CollectionSchema.MessageFactory enumSetFactory = this.enumSetFactory;
        if(enumSetFactory == null)
        {
            synchronized(this)
            {
                if((enumSetFactory = this.enumSetFactory) == null)
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
        if(enumMapFactory == null)
        {
            synchronized(this)
            {
                if((enumMapFactory = this.enumMapFactory) == null)
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
    public <V> EnumMap<E,V> newEnumMap()
    {
        return new EnumMap<E,V>(enumClass);
    }
    
    /**
     * Read the enum from the input.
     */
    public abstract E readFrom(Input input) throws IOException;
    
    /**
     * Reads the enum by its name.
     *
     */
    public static final class ByName<E extends Enum<E>> extends EnumIO<E>
    {
        public ByName(Class<E> enumClass)
        {
            super(enumClass);
        }
        
        public E readFrom(Input input) throws IOException
        {
            return Enum.valueOf(enumClass, input.readString());
        }
    }
    
    /**
     * Reads the enum by its number.
     *
     */
    public static final class ByNumber<E extends Enum<E>> extends EnumIO<E>
    {
        public ByNumber(Class<E> enumClass)
        {
            super(enumClass);
        } 
        
        public E readFrom(Input input) throws IOException
        {
            return enumClass.getEnumConstants()[input.readEnum()];
        }
    }

}
