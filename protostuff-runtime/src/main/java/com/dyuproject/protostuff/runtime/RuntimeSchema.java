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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;

/**
 * A schema that can be generated and cached at runtime for objects that have no schema.
 * This is particularly useful for pojos from 3rd party libraries. 
 *
 * @author David Yu
 * @created Nov 9, 2009
 */
public final class RuntimeSchema<T> extends MappedSchema<T>
{
    
    /**
     * By default, it is enabled.  For security purposes, you probably would want to 
     * register all known classes and disable this option.
     */
    public static final boolean AUTO_LOAD_POLYMORPHIC_CLASSES = 
        Boolean.parseBoolean(
                System.getProperty("protostuff.runtime.auto_load_polymorphic_classes", "true"));
    /**
     * Disabled by default.  For pojos that are not declared final, they 
     * could still be morphed to their respective subclasses (inheritance).
     * Enable this option if your parent classes aren't abstract classes.
     */
    public static final boolean MORPH_NON_FINAL_POJOS = 
        Boolean.getBoolean("protostuff.runtime.morph_non_final_pojos");
    
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
    public static final boolean COLLECTION_SCHEMA_ON_REPEATED_FIELDS = 
        Boolean.getBoolean("protostuff.runtime.collection_schema_on_repeated_fields");

    private static final ConcurrentHashMap<String, HasSchema<?>> __schemaWrappers = 
        new ConcurrentHashMap<String, HasSchema<?>>();
    
    private static final Set<String> NO_EXCLUSIONS = Collections.emptySet();
    
    static final Constructor<Object> OBJECT_CONSTRUCTOR;
    static
    {
        Constructor<Object> c = null;
        Class<?> reflectionFactoryClass = null;
        try
        {
            c = Object.class.getConstructor((Class[])null);
            reflectionFactoryClass = loadClass("sun.reflect.ReflectionFactory");
        }
        catch (Exception e)
        {
            // ignore
        }
        
        OBJECT_CONSTRUCTOR = c != null && reflectionFactoryClass != null ? c : null; 
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
    
    @SuppressWarnings("unchecked")
    static <T> Constructor<T> getConstructor(Class<T> clazz)
    {
        try
        {
            return clazz.getConstructor((Class[])null);
        }
        catch (SecurityException e)
        {
            if(OBJECT_CONSTRUCTOR == null)
                throw new RuntimeException(e);
            
            return sun.reflect.ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(clazz, OBJECT_CONSTRUCTOR);
        }
        catch (NoSuchMethodException e)
        {
            if(OBJECT_CONSTRUCTOR == null)
                throw new RuntimeException(e);
            
            return sun.reflect.ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(clazz, OBJECT_CONSTRUCTOR);
        }
    }
    
    /**
     * Maps the {@code baseClass} to a specific non-interface/non-abstract 
     * {@code typeClass} and registers it (this must be done on application startup).
     * 
     * With this approach, there is no overhead of writing the type metadata if a 
     * {@code baseClass} field is serialized.
     * 
     * Returns true if the baseClass does not exist.
     * 
     * @throws IllegalArgumentException if the {@code typeClass} is an interface or 
     * an abstract class.
     */
    public static <T> boolean map(Class<? super T> baseClass, Class<T> typeClass)
    {
        assert baseClass != null && typeClass != null;
        
        if(typeClass.isInterface() || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new IllegalArgumentException(typeClass + 
                    " cannot be an interface/abstract class.");
        }
        
        final HasSchema<?> last = __schemaWrappers.putIfAbsent(baseClass.getName(), 
                new Mapped<T>(baseClass, typeClass));
        
        return last == null || (last instanceof Mapped<?> && 
                ((Mapped<?>)last).typeClass == typeClass);
    }
    
    /**
     * Returns true if this there is no existing one or the same schema 
     * has already been registered (this must be done on application startup).
     */
    public static <T> boolean register(Class<T> typeClass, Schema<T> schema)
    {
        assert typeClass != null && schema != null;
        
        final HasSchema<?> last = __schemaWrappers.putIfAbsent(typeClass.getName(), 
                new Registered<T>(schema));
        
        return last == null || (last instanceof Registered<?> && 
                ((Registered<?>)last).schema == schema);
    }
    
    /**
     * Returns true if the {@code typeClass} has already been registered/mapped.
     */
    public static boolean isRegistered(Class<?> typeClass)
    {
        final HasSchema<?> last = __schemaWrappers.get(typeClass.getName());
        return last != null && !(last instanceof Lazy<?>);
    }
    
    /**
     * Gets the schema that was either registered or lazily initialized at runtime.
     */
    @SuppressWarnings("unchecked")
    public static <T> Schema<T> getSchema(Class<T> typeClass)
    {
        HasSchema<T> hs = (HasSchema<T>)__schemaWrappers.get(typeClass.getName());
        if(hs == null)
        {
            hs = new Lazy<T>(typeClass);
            final HasSchema<T> last = (HasSchema<T>)__schemaWrappers.putIfAbsent(
                    typeClass.getName(), hs);
            if(last != null)
                hs = last;
        }
        
        return hs.getSchema();
    }
    
    /**
     * Gets the schema that was either registered or lazily initialized at runtime (if 
     * the boolean arg {@code load} is true).
     */
    @SuppressWarnings("unchecked")
    public static <T> Schema<T> getSchema(String className, boolean load)
    {
        HasSchema<T> hs = (HasSchema<T>)__schemaWrappers.get(className);
        if(hs == null)
        {
            if(!load)
                return null;
            
            final Class<T> typeClass = loadClass(className);
            
            hs = new Lazy<T>(typeClass);
            final HasSchema<T> last = (HasSchema<T>)__schemaWrappers.putIfAbsent(
                    typeClass.getName(), hs);
            if(last != null)
                hs = last;
        }
        
        return hs.getSchema();
    }
    
    /**
     * Returns the schema wrapper.
     */
    @SuppressWarnings("unchecked")
    static <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass)
    {
        HasSchema<T> hs = (HasSchema<T>)__schemaWrappers.get(typeClass.getName());
        if(hs == null)
        {
            hs = new Lazy<T>(typeClass);
            final HasSchema<T> last = (HasSchema<T>)__schemaWrappers.putIfAbsent(
                    typeClass.getName(), hs);
            if(last != null)
                hs = last;
        }
        
        return hs;
    }
    
    /**
     * Gets the schema wrapper that was either registered or lazily initialized 
     * at runtime (if the boolean arg {@code load} is true).
     */
    @SuppressWarnings("unchecked")
    static <T> HasSchema<T> getSchemaWrapper(String className, boolean load)
    {
        HasSchema<T> hs = (HasSchema<T>)__schemaWrappers.get(className);
        if(hs == null)
        {
            if(!load)
                return null;
            
            final Class<T> typeClass = loadClass(className);
            
            hs = new Lazy<T>(typeClass);
            final HasSchema<T> last = (HasSchema<T>)__schemaWrappers.putIfAbsent(
                    typeClass.getName(), hs);
            if(last != null)
                hs = last;
        }
        
        return hs;
    }
    
    /**
     * Generates a schema from the given class.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass)
    {
        return createFrom(typeClass, NO_EXCLUSIONS);
    }
    
    /**
     * Generates a schema from the given class with the exclusion of certain fields.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, String[] exclusions)
    {
        HashSet<String> set = new HashSet<String>();
        for(String exclusion : exclusions)
            set.add(exclusion);
        
        return createFrom(typeClass, set);
    }
    
    /**
     * Generates a schema from the given class with the exclusion of certain fields.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, 
            Set<String> exclusions)
    {
        if(typeClass.isInterface() || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new RuntimeException("The root object can neither be an abstract " +
                        "class nor interface: \"" + typeClass.getName());
        }
        
        Map<String,java.lang.reflect.Field> fieldMap = findInstanceFields(typeClass);
        ArrayList<Field<T>> fields = new ArrayList<Field<T>>(fieldMap.size());
        int i = 0;
        for(java.lang.reflect.Field f : fieldMap.values())
        {
            if(!exclusions.contains(f.getName()))
            {
                if(f.getAnnotation(Deprecated.class) != null)
                {
                    // this field is deprecated and should be skipped.
                    // preserve its field number for backward-forward compat
                    i++;
                    continue;
                }
                
                RuntimeFieldFactory<?> rff = RuntimeFieldFactory.getFieldFactory(f.getType());
                if(rff!=null)
                {
                    Field<T> field = rff.create(i+1, f.getName(), f);
                    if(field!=null)
                    {
                        i++;
                        fields.add(field);
                    }
                }
            }
        }
        if(fields.isEmpty())
        {
            throw new RuntimeException("Not able to map any fields from " + 
                    typeClass + ".  All fields are either transient/static.  " +
                    "Two dimensional array fields are excluded.  " +
                    "Collection/Map fields whose generic type is another " +
            	    "Collection/Map or another generic type, are excluded.");
        }
        
        return new RuntimeSchema<T>(typeClass, fields, i, getConstructor(typeClass));
    }
    
    /**
     * Generates a schema from the given class with the declared fields (inclusive) based 
     * from the given Map.
     * The value of a the Map's entry will be the name used for the field (which enables aliasing).
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, 
            Map<String,String> declaredFields)
    {
        if(typeClass.isInterface() || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new RuntimeException("The root object can neither be an abstract " +
            		"class nor interface: \"" + typeClass.getName());
        }
        
        ArrayList<Field<T>> fields = new ArrayList<Field<T>>(declaredFields.size());
        int i = 0;
        for(Map.Entry<String, String> entry : declaredFields.entrySet())
        {
            java.lang.reflect.Field f;
            try
            {
                f = typeClass.getDeclaredField(entry.getKey());
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Exception on field: " + entry.getKey(), e);
            }
            
            int mod = f.getModifiers();
            if(!Modifier.isStatic(mod) && !Modifier.isTransient(mod))
            {
                RuntimeFieldFactory<?> rff = RuntimeFieldFactory.getFieldFactory(f.getType());
                if(rff!=null)
                {
                    Field<T> field = rff.create(i+1, entry.getValue(), f);
                    if(field!=null)
                    {
                        i++;
                        fields.add(field);
                    }
                }
            }
        }
        if(fields.isEmpty())
        {
            throw new RuntimeException("Not able to map any fields from " + 
                    typeClass + ".  All fields are either transient/static.  " +
                    "Two dimensional array fields are excluded.  " +
                    "Collection/Map fields whose generic type is another " +
                    "Collection/Map or another generic type, are excluded.");
        }
        return new RuntimeSchema<T>(typeClass, fields, i, getConstructor(typeClass));
    }

    static Map<String,java.lang.reflect.Field> findInstanceFields(Class<?> typeClass)
    {
        LinkedHashMap<String,java.lang.reflect.Field> fieldMap = 
            new LinkedHashMap<String,java.lang.reflect.Field>();
        fill(fieldMap, typeClass);
        return fieldMap;
    }
    
    static void fill(Map<String,java.lang.reflect.Field> fieldMap, Class<?> typeClass)
    {
        if(Object.class!=typeClass.getSuperclass())
            fill(fieldMap, typeClass.getSuperclass());
        
        for(java.lang.reflect.Field f : typeClass.getDeclaredFields())
        {
            int mod = f.getModifiers();
            if(!Modifier.isStatic(mod) && !Modifier.isTransient(mod))
                fieldMap.put(f.getName(), f);
        }
    }
    
    final Constructor<T> constructor;
    
    public RuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, 
            int lastFieldNumber, Constructor<T> constructor)
    {
        super(typeClass, fields, lastFieldNumber);
        this.constructor = constructor;
    }
    
    /**
     * Always returns true, everything is optional.
     */
    public boolean isInitialized(T message)
    {
        return true;
    }

    public T newMessage()
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
    
    /**
     * The schema wrapper.
     */
    public static abstract class HasSchema<T>
    {
        /**
         * Gets the schema.
         */
        public abstract Schema<T> getSchema();
        
        /**
         * Gets the pipe schema.
         */
        abstract Pipe.Schema<T> getPipeSchema();
    }
    
    static final class Registered<T> extends HasSchema<T>
    {
        final Schema<T> schema;
        private volatile Pipe.Schema<T> pipeSchema;
        
        Registered(Schema<T> schema)
        {
            this.schema = schema;
        }

        public Schema<T> getSchema()
        {
            return schema;
        }
        
        Pipe.Schema<T> getPipeSchema()
        {
            Pipe.Schema<T> pipeSchema = this.pipeSchema;
            if(pipeSchema == null)
            {
                synchronized(this)
                {
                    if((pipeSchema = this.pipeSchema) == null)
                    {
                        this.pipeSchema = pipeSchema = resolvePipeSchema(
                                schema, schema.typeClass(), true);
                    }
                }
            }
            return pipeSchema;
        }
    }
    
    static final class Lazy<T> extends HasSchema<T>
    {
        final Class<T> typeClass;
        private volatile Schema<T> schema;
        private volatile Pipe.Schema<T> pipeSchema;
        
        Lazy(Class<T> typeClass)
        {
            this.typeClass = typeClass;
        }

        @SuppressWarnings("unchecked")
        public Schema<T> getSchema()
        {
            Schema<T> schema = this.schema;
            if(schema==null)
            {
                synchronized(this)
                {
                    if((schema = this.schema) == null)
                    {
                        if(Message.class.isAssignableFrom(typeClass))
                        {
                            // use the message's schema.
                            try
                            {
                                final Message<T> m = (Message<T>)typeClass.newInstance();
                                this.schema = schema = m.cachedSchema();
                            }
                            catch (InstantiationException e)
                            {
                                throw new RuntimeException(e);
                            }
                            catch (IllegalAccessException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }
                        else
                        {
                            // create new
                            this.schema = schema = createFrom(typeClass);
                        }
                    }
                }
            }

            return schema;
        }
        
        Pipe.Schema<T> getPipeSchema()
        {
            Pipe.Schema<T> pipeSchema = this.pipeSchema;
            if(pipeSchema == null)
            {
                synchronized(this)
                {
                    if((pipeSchema = this.pipeSchema) == null)
                    {
                        this.pipeSchema = pipeSchema = RuntimeSchema.resolvePipeSchema(
                                getSchema(), typeClass, true);
                    }
                }
            }
            return pipeSchema;
        }
    }
    
    static final class Mapped<T> extends HasSchema<T>
    {
        
        final Class<? super T> baseClass;
        final Class<T> typeClass;
        private volatile HasSchema<T> wrapper;
        
        Mapped(Class<? super T> baseClass, Class<T> typeClass)
        {
            this.baseClass = baseClass;
            this.typeClass = typeClass;
        }

        public Schema<T> getSchema()
        {
            HasSchema<T> wrapper = this.wrapper;
            if(wrapper == null)
            {
                synchronized(this)
                {
                    if((wrapper = this.wrapper) == null)
                    {
                        this.wrapper = wrapper = getSchemaWrapper(typeClass);
                    }
                }
            }
            
            return wrapper.getSchema();
        }

        Pipe.Schema<T> getPipeSchema()
        {
            HasSchema<T> wrapper = this.wrapper;
            if(wrapper == null)
            {
                synchronized(this)
                {
                    if((wrapper = this.wrapper) == null)
                    {
                        this.wrapper = wrapper = getSchemaWrapper(typeClass);
                    }
                }
            }
            
            return wrapper.getPipeSchema();
        }
        
    }
    
    /**
     * Invoked only when applications are having pipe io operations.
     */
    @SuppressWarnings("unchecked")
    static <T> Pipe.Schema<T> resolvePipeSchema(Schema<T> schema, Class<? super T> clazz, 
            boolean throwIfNone)
    {
        if(Message.class.isAssignableFrom(clazz))
        {
            try
            {
                // use the pipe schema of code-generated messages if available.
                java.lang.reflect.Method m = clazz.getDeclaredMethod("getPipeSchema", 
                        new Class[]{});
                return (Pipe.Schema<T>)m.invoke(null, new Object[]{});
            }
            catch(Exception e)
            {
                // ignore
            }
        }
        
        if(MappedSchema.class.isAssignableFrom(schema.getClass()))
            return ((MappedSchema<T>)schema).pipeSchema;
        
        if(throwIfNone)
            throw new RuntimeException("No pipe schema for: " + clazz);
        
        return null;
    }
}
