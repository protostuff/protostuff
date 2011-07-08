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

/**
 * 
 */
package com.dyuproject.protostuff.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
     * Returns true if serializing enums by name is activated.
     * Disabled by default.
     */
    public static final boolean ENUMS_BY_NAME;
    
    /**
     * Enabled by default.  For security purposes, you probably would want to 
     * register all known classes and disable this option.
     */
    public static final boolean AUTO_LOAD_POLYMORPHIC_CLASSES;
    
    /**
     * Disabled by default.  For pojos that are not declared final, they 
     * could still be morphed to their respective subclasses (inheritance).
     * Enable this option if your parent classes aren't abstract classes.
     */
    public static final boolean MORPH_NON_FINAL_POJOS;
    
    /**
     * On repeated fields, the List/Collection itself is not serialized (only its values).
     * If you enable this option, the repeated field will be serialized as a 
     * standalone message with a collection schema.  Even if the List/Collection is empty, 
     * an empty collection message is still written.
     * 
     * This is particularly useful if you rely on {@link Object#equals(Object)} on your 
     * pojos.
     * 
     * Disabled by default for protobuf compatibility.
     */
    public static final boolean COLLECTION_SCHEMA_ON_REPEATED_FIELDS;
    
    /**
     * If true, sun.misc.Unsafe is used to access the fields of the objects instead of 
     * plain java reflections.  Enabled by default if running on a sun jre.
     */
    public static final boolean USE_SUN_MISC_UNSAFE;
    
    
    static final Method newInstanceFromObjectInputStream;
    
    static final Constructor<Object> OBJECT_CONSTRUCTOR;
    
    static
    {
        Constructor<Object> c = null;
        Class<?> reflectionFactoryClass = null;
        try
        {
            c = Object.class.getConstructor((Class[])null);
            reflectionFactoryClass = Thread.currentThread().getContextClassLoader()
                    .loadClass("sun.reflect.ReflectionFactory");
        }
        catch (Exception e)
        {
            // ignore
        }
        
        OBJECT_CONSTRUCTOR = c != null && reflectionFactoryClass != null ? c : null;
        
        newInstanceFromObjectInputStream = OBJECT_CONSTRUCTOR == null ? 
                getMethodNewInstanceFromObjectInputStream() : null;
                
        if(newInstanceFromObjectInputStream != null)
            newInstanceFromObjectInputStream.setAccessible(true);
        
        Properties props = OBJECT_CONSTRUCTOR == null ? new Properties() : 
            System.getProperties();
        
        ENUMS_BY_NAME = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.enums_by_name", "false"));
        
        AUTO_LOAD_POLYMORPHIC_CLASSES = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.auto_load_polymorphic_classes", "true"));
        
        MORPH_NON_FINAL_POJOS = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.morph_non_final_pojos", "false"));
        
        COLLECTION_SCHEMA_ON_REPEATED_FIELDS = Boolean.parseBoolean(props.getProperty(
                "protostuff.runtime.collection_schema_on_repeated_fields", "false"));
        
        // must be on a sun jre
        USE_SUN_MISC_UNSAFE = OBJECT_CONSTRUCTOR != null && Boolean.parseBoolean(
                props.getProperty("protostuff.runtime.use_sun_misc_unsafe", "true"));
    }
    
    private static Method getMethodNewInstanceFromObjectInputStream()
    {
        try
        {
            return java.io.ObjectInputStream.class.getDeclaredMethod(
                    "newInstance", Class.class, Class.class);
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    static <T> Class<T> loadClass(String className)
    {
        try
        {
            return (Class<T>)Thread.currentThread().getContextClassLoader().loadClass(
                    className);
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
        if(constructor == null)
        {
            // non-sun jre
            if(newInstanceFromObjectInputStream == null)
                throw new RuntimeException("Could not resolve constructor for " + clazz);
            
            return new Android2Instantiator<T>(clazz);
        }
        
        return new DefaultInstantiator<T>(constructor);
    }
    
    private static <T> Constructor<T> getConstructor(Class<T> clazz)
    {
        try
        {
            return clazz.getConstructor((Class[])null);
        }
        catch (SecurityException e)
        {
            return OBJECT_CONSTRUCTOR == null ? null : 
                OnDemandSunReflectionFactory.getConstructor(clazz, OBJECT_CONSTRUCTOR);
        }
        catch (NoSuchMethodException e)
        {
            return OBJECT_CONSTRUCTOR == null ? null : 
                OnDemandSunReflectionFactory.getConstructor(clazz, OBJECT_CONSTRUCTOR);
        }
    }
    
    
    private RuntimeEnv() {}
    
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
        }

        public T newInstance()
        {
            try
            {
                return constructor.newInstance((Object[])null);
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
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

        @SuppressWarnings("unchecked")
        public T newInstance()
        {
            try
            {
                return (T)newInstanceFromObjectInputStream.invoke(null, 
                        clazz, Object.class);
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}
