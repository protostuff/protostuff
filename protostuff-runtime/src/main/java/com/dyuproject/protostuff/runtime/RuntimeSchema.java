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

import static com.dyuproject.protostuff.runtime.RuntimeEnv.ID_STRATEGY;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.Tag;
import com.dyuproject.protostuff.runtime.RuntimeEnv.DefaultInstantiator;
import com.dyuproject.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * A schema that can be generated and cached at runtime for objects that have no schema.
 * This is particularly useful for pojos from 3rd party libraries. 
 *
 * @author David Yu
 * @created Nov 9, 2009
 */
public final class RuntimeSchema<T> extends MappedSchema<T>
{
    
    private static final Set<String> NO_EXCLUSIONS = Collections.emptySet();
    
    /**
     * Maps the {@code baseClass} to a specific non-interface/non-abstract 
     * {@code typeClass} and registers it (this must be done on application startup).
     * 
     * With this approach, there is no overhead of writing the type metadata if a 
     * {@code baseClass} field is serialized.
     * 
     * Returns true if the baseClass does not exist.
     * 
     * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is 
     * {@link DefaultIdStrategy}.
     * 
     * @throws IllegalArgumentException if the {@code typeClass} is an interface or 
     * an abstract class.
     */
    public static <T> boolean map(Class<? super T> baseClass, Class<T> typeClass)
    {
        if(ID_STRATEGY instanceof DefaultIdStrategy)
            return ((DefaultIdStrategy)ID_STRATEGY).map(baseClass, typeClass);
        
        throw new RuntimeException("RuntimeSchema.map is only supported on DefaultIdStrategy");
    }
    
    /**
     * Returns true if this there is no existing one or the same schema 
     * has already been registered (this must be done on application startup).
     * 
     * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is 
     * {@link DefaultIdStrategy}.
     */
    public static <T> boolean register(Class<T> typeClass, Schema<T> schema)
    {
        if(ID_STRATEGY instanceof DefaultIdStrategy)
            return ((DefaultIdStrategy)ID_STRATEGY).registerPojo(typeClass, schema);
        
        throw new RuntimeException("RuntimeSchema.register is only supported on DefaultIdStrategy");
    }
    
    /**
     * Returns true if the {@code typeClass} was not lazily created.
     * 
     * Method overload for backwards compatibility.
     */
    public static boolean isRegistered(Class<?> typeClass)
    {
        return isRegistered(typeClass, ID_STRATEGY);
    }
    
    /**
     * Returns true if the {@code typeClass} was not lazily created.
     */
    public static boolean isRegistered(Class<?> typeClass, IdStrategy strategy)
    {
        return strategy.isRegistered(typeClass);
    }
    
    /**
     * Gets the schema that was either registered or lazily initialized at runtime.
     * 
     * Method overload for backwards compatibility.
     */
    public static <T> Schema<T> getSchema(Class<T> typeClass)
    {
        return getSchema(typeClass, ID_STRATEGY);
    }
    
    /**
     * Gets the schema that was either registered or lazily initialized at runtime.
     */
    public static <T> Schema<T> getSchema(Class<T> typeClass, IdStrategy strategy)
    {
        return strategy.getSchemaWrapper(typeClass, true).getSchema();
    }
    
    /**
     * Returns the schema wrapper.
     * 
     * Method overload for backwards compatibility.
     */
    static <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass)
    {
        return getSchemaWrapper(typeClass, ID_STRATEGY);
    }
    
    /**
     * Returns the schema wrapper.
     */
    static <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass, IdStrategy strategy)
    {
        return strategy.getSchemaWrapper(typeClass, true);
    }
    
    /**
     * Generates a schema from the given class.
     * 
     * Method overload for backwards compatibility.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass)
    {
        return createFrom(typeClass, NO_EXCLUSIONS, ID_STRATEGY);
    }
    
    /**
     * Generates a schema from the given class.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, 
            IdStrategy strategy)
    {
        return createFrom(typeClass, NO_EXCLUSIONS, strategy);
    }
    
    /**
     * Generates a schema from the given class with the exclusion of certain fields.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, 
            String[] exclusions, IdStrategy strategy)
    {
        HashSet<String> set = new HashSet<String>();
        for(String exclusion : exclusions)
            set.add(exclusion);
        
        return createFrom(typeClass, set, strategy);
    }
    
    /**
     * Generates a schema from the given class with the exclusion of certain fields.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, 
            Set<String> exclusions, IdStrategy strategy)
    {
        if(typeClass.isInterface() || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new RuntimeException("The root object can neither be an abstract " +
                        "class nor interface: \"" + typeClass.getName());
        }
        
        final Map<String,java.lang.reflect.Field> fieldMap = findInstanceFields(typeClass);
        final ArrayList<Field<T>> fields = new ArrayList<Field<T>>(fieldMap.size());
        int i = 0;
        int maxFieldMapping = 0;
        boolean annotated = false;
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
                
                int fieldMapping;
                Tag tag = f.getAnnotation(Tag.class);
                if(tag == null)
                {
                    // Fields gets assigned mapping tags according to their definition order
                    if(annotated)
                    {
                        throw new RuntimeException("When using annotation-based mapping, " +
                                "all fields must be annotated with @" + Tag.class.getSimpleName());
                    }
                    fieldMapping = ++i;
                }
                else
                {
                    // Fields gets assigned mapping tags according to their annotation
                    if(!annotated && !fields.isEmpty())
                    {
                        throw new RuntimeException("When using annotation-based mapping, " +
                                "all fields must be annotated with @" + Tag.class.getSimpleName());
                    }
                    annotated = true;
                    fieldMapping = tag.value();
                    
                    if(fieldMapping < 1)
                    {
                        throw new RuntimeException("Invalid field number: " + 
                                fieldMapping + " on " + typeClass);
                    }
                }
                
                final Field<T> field = RuntimeFieldFactory.getFieldFactory(
                        f.getType(), strategy).create(fieldMapping, f.getName(), f, strategy);
                fields.add(field);
                
                maxFieldMapping = Math.max(maxFieldMapping, fieldMapping);
            }
        }
        if(fields.isEmpty())
        {
            throw new RuntimeException("Not able to map any fields from " + 
                    typeClass + ".  All fields are either transient/static.");
        }
        
        return new RuntimeSchema<T>(typeClass, fields, maxFieldMapping, 
                RuntimeEnv.newInstantiator(typeClass));
    }
    
    /**
     * Generates a schema from the given class with the declared fields (inclusive) based 
     * from the given Map.
     * The value of a the Map's entry will be the name used for the field (which enables aliasing).
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, 
            Map<String,String> declaredFields, IdStrategy strategy)
    {
        if(typeClass.isInterface() || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new RuntimeException("The root object can neither be an abstract " +
            		"class nor interface: \"" + typeClass.getName());
        }
        
        final ArrayList<Field<T>> fields = new ArrayList<Field<T>>(declaredFields.size());
        int i = 0;
        for(Map.Entry<String, String> entry : declaredFields.entrySet())
        {
            final java.lang.reflect.Field f;
            try
            {
                f = typeClass.getDeclaredField(entry.getKey());
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Exception on field: " + entry.getKey(), e);
            }
            
            final int mod = f.getModifiers();
            if(!Modifier.isStatic(mod) && !Modifier.isTransient(mod))
            {
                final Field<T> field = RuntimeFieldFactory.getFieldFactory(
                        f.getType(), strategy).create(++i, entry.getValue(), f, strategy);
                fields.add(field);
            }
        }
        if(fields.isEmpty())
        {
            throw new RuntimeException("Not able to map any fields from " + 
                    typeClass + ".  All fields are either transient/static.");
        }
        return new RuntimeSchema<T>(typeClass, fields, i, 
                RuntimeEnv.newInstantiator(typeClass));
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
    
    final Instantiator<T> instantiator;
    
    public RuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, 
            int lastFieldNumber, Constructor<T> constructor)
    {
        this(typeClass, fields, lastFieldNumber, 
                new DefaultInstantiator<T>(constructor));
    }
    
    public RuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, 
            int lastFieldNumber, Instantiator<T> instantiator)
    {
        super(typeClass, fields, lastFieldNumber);
        this.instantiator = instantiator;
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
        return instantiator.newInstance();
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
