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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final ConcurrentHashMap<String, HasSchema<?>> __schemaWrappers = 
        new ConcurrentHashMap<String, HasSchema<?>>();
    
    private static final List<String> NO_EXCLUSIONS = Collections.emptyList();
    
    /**
     * Registers the schema which overrides any existing schema mapped with the 
     * given class.
     */
    public static <T> void register(Class<? super T> typeClass, Schema<T> schema)
    {
        __schemaWrappers.put(typeClass.getName(), new Registered<T>(schema));
    }
    
    /**
     * Registers the schema which overrides any existing schema mapped with the 
     * given class.
     */
    public static <T> boolean isRegistered(Class<T> typeClass)
    {
        return __schemaWrappers.containsKey(typeClass.getName());
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
            HasSchema<T> last = (HasSchema<T>)__schemaWrappers.putIfAbsent(typeClass.getName(), hs);
            if(last != null)
                hs = last;
        }
        
        return hs.getSchema();
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
        return createFrom(typeClass, Arrays.asList(exclusions));
    }
    
    /**
     * Generates a schema from the given class with the exclusion of certain fields.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, List<String> exclusions)
    {
        if(typeClass.isInterface())
        {
            throw new RuntimeException("The interface \"" + typeClass.getName() + 
                    "\" must be pre-mapped to its respective impl.  E.g. " + 
                    "RuntimeSchema.register(ITask.class, RuntimeSchema.getSchema(TaskImpl.class));");
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
                    // reserver its field number for backward-forward compat
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
            throw new RuntimeException("All fields are either transient/static.  " +
            		"Note that Map fields are excluded.  " +
            		"Two dimensional array fields are excluded.  " +
            		"Collection fields whose generic type is a collection " +
            		"or another generic type, are excluded.");
        }
        return new RuntimeSchema<T>(typeClass, fields, i);
    }
    
    /**
     * Generates a schema from the given class with the declared fields (inclusive) based 
     * from the given Map.
     * The value of a the Map's entry will be the name used for the field (which enables aliasing).
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass, 
            Map<String,String> declaredFields)
    {
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
            throw new RuntimeException("All fields are either transient/static.  " +
                        "Note that Map fields are excluded.  " +
                        "Two dimensional array fields are excluded.  " +
                        "Collection fields whose generic type is a collection " +
                        "or another generic type, are excluded.");
        }
        return new RuntimeSchema<T>(typeClass, fields, i);
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
    
    public RuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, 
            int lastFieldNumber)
    {
        super(typeClass, fields, lastFieldNumber);
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
            return typeClass.newInstance();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    interface HasSchema<T>
    {
        /**
         * Gets the schema.
         */
        public Schema<T> getSchema();
    }
    
    static final class Registered<T> implements HasSchema<T>
    {
        final Schema<T> schema;
        
        public Registered(Schema<T> schema)
        {
            this.schema = schema;
        }

        public Schema<T> getSchema()
        {
            return schema;
        }
    }
    
    static final class Lazy<T> implements HasSchema<T>
    {
        final Class<T> typeClass;
        Schema<T> schema;
        
        public Lazy(Class<T> typeClass)
        {
            this.typeClass = typeClass;
        }

        public Schema<T> getSchema()
        {
            Schema<T> schema = this.schema;
            if(schema==null)
            {
                synchronized(this)
                {
                    if((schema = this.schema) == null)
                        this.schema = schema = createFrom(typeClass);
                }
            }

            return schema;
        }
    }

}
