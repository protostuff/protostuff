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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * The runtime environment.
 * 
 * @author David Yu
 * @created Jul 8, 2011
 */
public final class RuntimeEnv
{
    /**
     * Returns true if serializing enums by name is activated. Disabled by default.
     */
    public static final boolean ENUMS_BY_NAME;

    /**
     * Enabled by default. For security purposes, you probably would want to register all known classes and disable this
     * option.
     */
    public static final boolean AUTO_LOAD_POLYMORPHIC_CLASSES;

    /**
     * Disabled by default. Writes a sentinel value (uint32) in place of null values. Works only on the binary formats
     * (protostuff/graph/protobuf).
     */
    public static final boolean ALLOW_NULL_ARRAY_ELEMENT;

    /**
     * Disabled by default. For pojos that are not declared final, they could still be morphed to their respective
     * subclasses (inheritance). Enable this option if your parent classes aren't abstract classes.
     */
    public static final boolean MORPH_NON_FINAL_POJOS;

    /**
     * Disabled by default. If true, type metadata will be included on serialization for fields that are collection
     * interfaces. Enabling this is useful if you want to retain the actual collection impl used.
     * <p>
     * If disabled, type metadata will not be included and instead, will be mapped to a default impl.
     * <p>
     * 
     * <pre>
     * Collection = ArrayList
     * List = ArrayList
     * Set = HashSet
     * SortedSet = TreeSet
     * NavigableSet = TreeSet
     * Queue = LinkedList
     * BlockingQueue = LinkedBlockingQueue
     * Deque = LinkedList
     * BlockingDequeue = LinkedBlockingDeque
     * </pre>
     * <p>
     * You can optionally enable only for a particular field by annotating it with {@link io.protostuff.Morph}.
     */
    public static final boolean MORPH_COLLECTION_INTERFACES;

    /**
     * Disabled by default. If true, type metadata will be included on serialization for fields that are map interfaces.
     * Enabling this is useful if you want to retain the actual map impl used.
     * <p>
     * If disabled, type metadata will not be included and instead, will be mapped to a default impl.
     * <p>
     * 
     * <pre>
     * Map = HashMap
     * SortedMap = TreeMap
     * NavigableMap = TreeMap
     * ConcurrentMap = ConcurrentHashMap
     * ConcurrentNavigableMap = ConcurrentSkipListMap
     * </pre>
     * <p>
     * You can optionally enable only for a particular field by annotating it with {@link io.protostuff.Morph}.
     */
    public static final boolean MORPH_MAP_INTERFACES;

    /**
     * On repeated fields, the List/Collection itself is not serialized (only its values). If you enable this option,
     * the repeated field will be serialized as a standalone message with a collection schema. Even if the
     * List/Collection is empty, an empty collection message is still written.
     * <p>
     * This is particularly useful if you rely on {@link Object#equals(Object)} on your pojos.
     * <p>
     * Disabled by default for protobuf compatibility.
     */
    public static final boolean COLLECTION_SCHEMA_ON_REPEATED_FIELDS;
    
    /**
     * Disabled by default.  If enabled, a list's internal state/fields 
     * will be serialized instead of just its elements.
     */
    public static final boolean POJO_SCHEMA_ON_COLLECTION_FIELDS;
    
    /**
     * Disabled by default.  If enabled, a map's internal state/fields 
     * will be serialized instead of just its elements.
     */
    public static final boolean POJO_SCHEMA_ON_MAP_FIELDS;

    /**
     * If true, sun.misc.Unsafe is used to access the fields of the objects instead of plain java reflections. Enabled
     * by default if running on a sun jre.
     */
    public static final boolean USE_SUN_MISC_UNSAFE;

    /**
     * If true, the constructor will always be obtained from {@code ReflectionFactory.newConstructorFromSerialization}.
     * <p>
     * Disabled by default, which means that if the pojo has a no-args constructor, that will be used instead.
     * <p>
     * Enable this if you intend to avoid deserializing objects whose no-args constructor initializes (unwanted)
     * internal state. This applies to complex/framework objects.
     * <p>
     * If you intend to fill default field values using your default constructor, leave this disabled. This normally
     * applies to java beans/data objects.
     */
    public static final boolean ALWAYS_USE_SUN_REFLECTION_FACTORY;

    static final Method newInstanceFromObjectInputStream;

    static final Constructor<Object> OBJECT_CONSTRUCTOR;

    public static final IdStrategy ID_STRATEGY;

    static
    {
        Constructor<Object> c = null;
        Class<?> reflectionFactoryClass = null;
        try
        {
            c = Object.class.getConstructor((Class[]) null);
            reflectionFactoryClass = Thread.currentThread()
                    .getContextClassLoader()
                    .loadClass("sun.reflect.ReflectionFactory");
        }
        catch (Exception e)
        {
            // ignore
        }

        OBJECT_CONSTRUCTOR = c != null && reflectionFactoryClass != null ? c
                : null;

        newInstanceFromObjectInputStream = OBJECT_CONSTRUCTOR == null ? getMethodNewInstanceFromObjectInputStream()
                : null;

        if (newInstanceFromObjectInputStream != null)
            newInstanceFromObjectInputStream.setAccessible(true);

        Properties props = OBJECT_CONSTRUCTOR == null ? new Properties()
                : System.getProperties();

        ENUMS_BY_NAME = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.enums_by_name", "false"));

        AUTO_LOAD_POLYMORPHIC_CLASSES = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.auto_load_polymorphic_classes", "true"));

        ALLOW_NULL_ARRAY_ELEMENT = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.allow_null_array_element", "false"));

        MORPH_NON_FINAL_POJOS = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.morph_non_final_pojos", "false"));

        MORPH_COLLECTION_INTERFACES = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.morph_collection_interfaces", "false"));

        MORPH_MAP_INTERFACES = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.morph_map_interfaces", "false"));

        COLLECTION_SCHEMA_ON_REPEATED_FIELDS = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.collection_schema_on_repeated_fields",
                "false"));
        
        POJO_SCHEMA_ON_COLLECTION_FIELDS = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.pojo_schema_on_collection_fields",
                "false"));
        
        POJO_SCHEMA_ON_MAP_FIELDS = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.pojo_schema_on_map_fields",
                "false"));

        // must be on a sun jre
        USE_SUN_MISC_UNSAFE = OBJECT_CONSTRUCTOR != null
                && Boolean.parseBoolean(props.getProperty(
                        "protostuff.runtime.use_sun_misc_unsafe", "true"));

        ALWAYS_USE_SUN_REFLECTION_FACTORY = OBJECT_CONSTRUCTOR != null
                && Boolean.parseBoolean(props.getProperty(
                        "protostuff.runtime.always_use_sun_reflection_factory",
                        "false"));

        String factoryProp = props
                .getProperty("protostuff.runtime.id_strategy_factory");
        if (factoryProp == null)
            ID_STRATEGY = new DefaultIdStrategy();
        else
        {
            final IdStrategy.Factory factory;
            try
            {
                factory = ((IdStrategy.Factory) loadClass(factoryProp)
                        .newInstance());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            ID_STRATEGY = factory.create();
            factory.postCreate();
        }
    }

    private static Method getMethodNewInstanceFromObjectInputStream()
    {
        try
        {
            return java.io.ObjectInputStream.class.getDeclaredMethod(
                    "newInstance", Class.class, Class.class);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    static <T> Class<T> loadClass(String className)
    {
        try
        {
            return (Class<T>) Thread.currentThread().getContextClassLoader()
                    .loadClass(className);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns an instatiator for the specified {@code clazz}.
     */
    public static <T> Instantiator<T> newInstantiator(Class<T> clazz)
    {
        final Constructor<T> constructor = getConstructor(clazz);
        if (constructor == null)
        {
            // non-sun jre
            if (newInstanceFromObjectInputStream == null)
                throw new RuntimeException("Could not resolve constructor for "
                        + clazz);

            return new Android2Instantiator<T>(clazz);
        }

        return new DefaultInstantiator<T>(constructor);
    }

    private static <T> Constructor<T> getConstructor(Class<T> clazz)
    {
        if (ALWAYS_USE_SUN_REFLECTION_FACTORY)
            return OnDemandSunReflectionFactory.getConstructor(clazz,
                    OBJECT_CONSTRUCTOR);

        try
        {
            return clazz.getDeclaredConstructor((Class[]) null);
        }
        catch (SecurityException e)
        {
            return OBJECT_CONSTRUCTOR == null ? null
                    : OnDemandSunReflectionFactory.getConstructor(clazz,
                            OBJECT_CONSTRUCTOR);
        }
        catch (NoSuchMethodException e)
        {
            return OBJECT_CONSTRUCTOR == null ? null
                    : OnDemandSunReflectionFactory.getConstructor(clazz,
                            OBJECT_CONSTRUCTOR);
        }
    }

    private RuntimeEnv()
    {
    }

    public static abstract class Instantiator<T>
    {
        /**
         * Creates a new instance of an object.
         */
        public abstract T newInstance();
    }

    static final class DefaultInstantiator<T> extends Instantiator<T>
    {

        final Constructor<T> constructor;

        DefaultInstantiator(Constructor<T> constructor)
        {
            this.constructor = constructor;
            constructor.setAccessible(true);
        }

        @Override
        public T newInstance()
        {
            try
            {
                return constructor.newInstance((Object[]) null);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    static final class Android2Instantiator<T> extends Instantiator<T>
    {

        final Class<T> clazz;

        Android2Instantiator(Class<T> clazz)
        {
            this.clazz = clazz;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T newInstance()
        {
            try
            {
                return (T) newInstanceFromObjectInputStream.invoke(null, clazz,
                        Object.class);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}
