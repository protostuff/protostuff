//========================================================================
//Copyright 2012 David Yu
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;
import com.dyuproject.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * A view schema can choose which fields to include during ser/deser.
 * 
 * @author David Yu
 * @created Nov 9, 2012
 */
public final class RuntimeView
{
    
    private RuntimeView() {}
    
    /**
     * Returns a new view schema based from an existing one.
     */
    public static <T> Schema<T> createFrom(RuntimeSchema<T> rs, 
            Factory vf, Predicate.Factory pf, 
            String ... args)
    {
        return createFrom(rs, rs.instantiator, vf, pf, args);
    }
    
    /**
     * Returns a new view schema based from an existing one.
     */
    public static <T> Schema<T> createFrom(MappedSchema<T> ms, 
            Instantiator<T> instantiator, 
            Factory vf, 
            Predicate.Factory pf, 
            String ... args)
    {
        return vf.create(ms.typeClass, ms.fields, ms.fieldsByNumber, 
                ms.fieldsByName, instantiator, pf, args);
    }
    
    public interface Factory
    {
        /**
         * Creates a view schema based from the given metadata.
         * 
         * @param pf is optional, depending on the view factory used.
         * @param args is optional, depending on the view factory used.
         */
        public <T> Schema<T> create(Class<T> typeClass, 
                Field<T>[] fields, 
                Field<T>[] fieldsByNumber, 
                Map<String,Field<T>> fieldsByName, 
                Instantiator<T> instantiator, 
                Predicate.Factory pf, 
                String[] args);
    }
    
    /**
     * The base schema used by the built-in factories.
     */
    public static abstract class BaseSchema<T> implements Schema<T>
    {
        public final Class<T> typeClass;
        public final Instantiator<T> instantiator;
        
        protected BaseSchema(Class<T> typeClass, Instantiator<T> instantiator)
        {
            this.typeClass = typeClass;
            this.instantiator = instantiator;
        }
        
        public Class<T> typeClass()
        {
            return typeClass;
        }
        
        public String messageName()
        {
            return typeClass.getSimpleName();
        }
        
        public String messageFullName()
        {
            return typeClass.getName();
        }
        
        public boolean isInitialized(T message)
        {
            // same as runtime schema
            return true;
        }

        public T newMessage()
        {
            return instantiator.newInstance();
        }
    }
    
    /**
     * Built-in view schema factories.
     * 
     * <pre>
     * All factory args are required except:
     * 
     * {@link Predicate.Factory} pf
     * {@link String}[] args
     * </pre>
     * 
     * For application/behavior specific filters, create your own 
     * view factory or predicate factory, and then design an 
     * ahead-of-time filter (which is usually done at application startup).
     * </pre>
     */
    public enum FACTORY implements Factory
    {
        /**
         * Filters the fields to include based on a {@link Predicate}.
         * 
         * The {@link Predicate.Factory} arg is required.
         */
        PREDICATE
        {
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    final Field<T>[] fields,
                    final Field<T>[] fieldsByNumber,
                    final Map<String, Field<T>> fieldsByName,
                    Instantiator<T> instantiator, 
                    Predicate.Factory pf, 
                    String[] args)
            {
                if(pf == null)
                    throw new IllegalArgumentException("pf must not be null.");
                
                final Predicate predicate = pf.create(args);
                
                return new BaseSchema<T>(typeClass, instantiator)
                {
                    
                    public String getFieldName(int number)
                    {
                        final Field<T> field = number < fieldsByNumber.length ? 
                                fieldsByNumber[number] : null;
                                
                        return field != null && predicate.apply(field) ? field.name : null;
                    }
                    
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field != null && predicate.apply(field) ? field.number : 0;
                    }
                    
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0; 
                                number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ? 
                                    fieldsByNumber[number] : null;

                            if(field == null || !predicate.apply(field, message))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }
                    
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for(Field<T> f : fields)
                        {
                            if(predicate.apply(f, message))
                                f.writeTo(output, message);
                        }
                    }
                };
            }
        },
        
        /**
         * Copies the map.  
         * The extra map lookups during write makes this slower than the others.
         * 
         * The args param is required (the field names to exclude).
         */
        EXCLUDE_VIA_COPY_MAP
        {
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    final Field<T>[] fields,
                    final Field<T>[] fieldsByNumber,
                    Map<String, Field<T>> byName,
                    Instantiator<T> instantiator, 
                    Predicate.Factory pf, 
                    String[] args)
            {
                final HashMap<String,Field<T>> fieldsByName = copyAndExclude(byName, args);
                
                return new BaseSchema<T>(typeClass, instantiator)
                {
                    
                    public String getFieldName(int number)
                    {
                        final Field<T> field = number < fieldsByNumber.length ? 
                                fieldsByNumber[number] : null;
                                
                        return field != null && fieldsByName.containsKey(field.name) ? 
                                field.name : null;
                    }
                    
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field == null ? 0 : field.number;
                    }
                    
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0; 
                                number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ? 
                                    fieldsByNumber[number] : null;

                            if(field == null || !fieldsByName.containsKey(field.name))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }
                    
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for(Field<T> f : fields)
                        {
                            // this extra lookup is expensive
                            if(fieldsByName.containsKey(f.name))
                                f.writeTo(output, message);
                        }
                    }
                };
            }
        },
        
        /**
         * Copies both the map and the array.
         * 
         * Performs better than {@link #EXCLUDE_VIA_COPY_MAP} for writes.
         * 
         * The args param is required (the field names to exclude).
         */
        EXCLUDE_VIA_COPY_BOTH
        {
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    Field<T>[] flds,
                    final Field<T>[] fieldsByNumber,
                    Map<String, Field<T>> byName,
                    Instantiator<T> instantiator, 
                    Predicate.Factory factory, 
                    String[] args)
            {
                final HashMap<String,Field<T>> fieldsByName = copyAndExclude(byName, args);
                
                @SuppressWarnings("unchecked")
                final Field<T>[] fields = (Field<T>[])new Field<?>[fieldsByName.size()];
                for(int i = 1, j = 0; i < fieldsByNumber.length; i++)
                {
                    Field<T> field = fieldsByNumber[i];
                    if(field != null && fieldsByName.containsKey(field.name))
                        fields[j++] = field;
                }
                
                return new BaseSchema<T>(typeClass, instantiator)
                {
                    
                    public String getFieldName(int number)
                    {
                        final Field<T> field = number < fieldsByNumber.length ? 
                                fieldsByNumber[number] : null;
                                
                        return field != null && fieldsByName.containsKey(field.name) ? 
                                field.name : null;
                    }
                    
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field == null ? 0 : field.number;
                    }
                    
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0; 
                                number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ? 
                                    fieldsByNumber[number] : null;

                            if(field == null || !fieldsByName.containsKey(field.name))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }
                    
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for(Field<T> f : fields)
                            f.writeTo(output, message);
                    }
                };
            }
        }
        ;
    }
    
    static <T> HashMap<String,Field<T>> copyAndExclude(
            Map<String, Field<T>> byName, final String[] args)
    {
        if(args == null || args.length == 0)
            throw new IllegalArgumentException("args must not be empty.");
        
        // copy
        final HashMap<String,Field<T>> fieldsByName = 
                new HashMap<String,Field<T>>(byName);
        
        for(String name : args)
        {
            // remove
            if(null == fieldsByName.remove(name))
            {
                throw new IllegalArgumentException(name + 
                        " field is unknown or is a duplicate.");
            }
        }
        
        if(fieldsByName.size() == 0)
        {
            throw new IllegalArgumentException("No fields are left.  " +
                    "Everything was excluded.");
        }
        
        return fieldsByName;
    }

}
