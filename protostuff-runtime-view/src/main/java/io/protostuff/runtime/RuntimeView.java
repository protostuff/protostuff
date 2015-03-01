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

package io.protostuff.runtime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.runtime.MappedSchema.Field;
import io.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * A view schema can choose which fields to include during ser/deser.
 * 
 * @author David Yu
 * @created Nov 9, 2012
 */
public final class RuntimeView
{

    private RuntimeView()
    {
    }

    /**
     * Returns a new view schema based from an existing one.
     */
    public static <T> Schema<T> createFrom(RuntimeSchema<T> rs,
            Factory vf, Predicate.Factory pf,
            String... args)
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
            String... args)
    {
        return vf.create(ms.typeClass, ms.fields, ms.fieldsByNumber,
                ms.fieldsByName, instantiator, pf, args);
    }

    public interface Factory
    {
        /**
         * Creates a view schema based from the given metadata.
         * 
         * @param pf
         *            is optional, depending on the view factory used.
         * @param args
         *            is optional, depending on the view factory used.
         */
        public <T> Schema<T> create(Class<T> typeClass,
                Field<T>[] fields,
                Field<T>[] fieldsByNumber,
                Map<String, Field<T>> fieldsByName,
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

        @Override
        public Class<T> typeClass()
        {
            return typeClass;
        }

        @Override
        public String messageName()
        {
            return typeClass.getSimpleName();
        }

        @Override
        public String messageFullName()
        {
            return typeClass.getName();
        }

        @Override
        public boolean isInitialized(T message)
        {
            // same as runtime schema
            return true;
        }

        @Override
        public T newMessage()
        {
            return instantiator.newInstance();
        }
    }

    public static abstract class PostFilteredSchema<T> extends BaseSchema<T>
    {
        public final Field<T>[] fields;

        protected PostFilteredSchema(Class<T> typeClass, Instantiator<T> instantiator,
                Field<T>[] fields)
        {
            super(typeClass, instantiator);

            this.fields = fields;
        }
    }

    /**
     * Built-in view schema factories.
     * <p>
     * 
     * <pre>
     * All factory args are required except:
     * 
     * {@link Predicate.Factory} pf
     * {@link String}[] args
     * </pre>
     * <p>
     * For application/behavior specific filters, create your own view factory or predicate factory, and then design an
     * ahead-of-time filter (which is usually done at application startup).
     */
    public enum Factories implements Factory
    {
        /**
         * Filters the fields to include based on a {@link Predicate}.
         * <p>
         * The {@link Predicate.Factory} arg is required.
         */
        PREDICATE
        {
            @Override
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    final Field<T>[] fields,
                    final Field<T>[] fieldsByNumber,
                    final Map<String, Field<T>> fieldsByName,
                    Instantiator<T> instantiator,
                    Predicate.Factory pf,
                    String[] args)
            {
                if (pf == null)
                    throw new IllegalArgumentException("Predicate.Factory arg must not be null.");

                final Predicate predicate = pf.create(args);

                return new BaseSchema<T>(typeClass, instantiator)
                {
                    @Override
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field != null && predicate.apply(field) ? field.number : 0;
                    }

                    @Override
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0;
                        number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ?
                                    fieldsByNumber[number] : null;

                            if (field == null || !predicate.apply(field, message))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }

                    @Override
                    public String getFieldName(int number)
                    {
                        // only called during writes
                        // the predicate already applied on writeTo (the method below)
                        final Field<T> field = number < fieldsByNumber.length ?
                                fieldsByNumber[number] : null;

                        return field == null ? null : field.name;
                    }

                    @Override
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for (Field<T> f : fields)
                        {
                            if (predicate.apply(f, message))
                                f.writeTo(output, message);
                        }
                    }
                };
            }
        },

        /**
         * Exclude the fields for merging and writing.
         * <p>
         * The args param is required (the field names to exclude) if {@link Predicate.Factory} is not provided.
         */
        EXCLUDE
        {
            @Override
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    Field<T>[] flds,
                    final Field<T>[] fieldsByNumber,
                    Map<String, Field<T>> byName,
                    Instantiator<T> instantiator,
                    Predicate.Factory factory,
                    String[] args)
            {
                final HashMap<String, Field<T>> fieldsByName = factory == null ?
                        copyAndExclude(typeClass, byName, args) :
                        copyAndExclude(typeClass, byName, factory.create(args));

                @SuppressWarnings("unchecked")
                Field<T>[] fields = (Field<T>[]) new Field<?>[fieldsByName.size()];
                for (int i = 1, j = 0; i < fieldsByNumber.length; i++)
                {
                    Field<T> field = fieldsByNumber[i];
                    if (field != null && fieldsByName.containsKey(field.name))
                        fields[j++] = field;
                }

                return new PostFilteredSchema<T>(typeClass, instantiator, fields)
                {
                    @Override
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field == null ? 0 : field.number;
                    }

                    @Override
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0;
                        number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ?
                                    fieldsByNumber[number] : null;

                            if (field == null || !fieldsByName.containsKey(field.name))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }

                    @Override
                    public String getFieldName(int number)
                    {
                        // only called during writes
                        final Field<T> field = number < fieldsByNumber.length ?
                                fieldsByNumber[number] : null;

                        return field == null ? null : field.name;
                    }

                    @Override
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for (Field<T> f : fields)
                            f.writeTo(output, message);
                    }
                };
            }
        },

        /**
         * Optimized only for merging. If you need to do writes, use {@link #EXCLUDE} instead. The extra map lookups
         * during writes makes this slower than {@link #EXCLUDE}.
         * <p>
         * The args param is required (the field names to exclude).
         */
        EXCLUDE_OPTIMIZED_FOR_MERGE_ONLY
        {
            @Override
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    final Field<T>[] fields,
                    final Field<T>[] fieldsByNumber,
                    Map<String, Field<T>> byName,
                    Instantiator<T> instantiator,
                    Predicate.Factory pf,
                    String[] args)
            {
                final HashMap<String, Field<T>> fieldsByName = copyAndExclude(typeClass,
                        byName, args);

                return new BaseSchema<T>(typeClass, instantiator)
                {
                    @Override
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field == null ? 0 : field.number;
                    }

                    @Override
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0;
                        number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ?
                                    fieldsByNumber[number] : null;

                            if (field == null || !fieldsByName.containsKey(field.name))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }

                    @Override
                    public String getFieldName(int number)
                    {
                        // only called during writes
                        // already filtered on writeTo (the method below)
                        final Field<T> field = number < fieldsByNumber.length ?
                                fieldsByNumber[number] : null;

                        return field == null ? null : field.name;
                    }

                    @Override
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for (Field<T> f : fields)
                        {
                            // this extra lookup makes this slower
                            // hence use EXCLUDE instead
                            if (fieldsByName.containsKey(f.name))
                                f.writeTo(output, message);
                        }
                    }
                };
            }
        },

        /**
         * Include the fields for merging and writing.
         * <p>
         * The args param is required (the field names to include).
         */
        INCLUDE
        {
            @Override
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    Field<T>[] flds,
                    final Field<T>[] fieldsByNumber,
                    Map<String, Field<T>> byName,
                    Instantiator<T> instantiator,
                    Predicate.Factory factory,
                    String[] args)
            {
                final HashMap<String, Field<T>> fieldsByName =
                        new HashMap<>();

                int maxFieldNumber = includeAndAddTo(fieldsByName, typeClass, byName, args);

                @SuppressWarnings("unchecked")
                Field<T>[] fields = (Field<T>[]) new Field<?>[fieldsByName.size()];
                for (int i = 1, j = 0; i <= maxFieldNumber; i++)
                {
                    Field<T> field = fieldsByNumber[i];
                    if (field != null && fieldsByName.containsKey(field.name))
                        fields[j++] = field;
                }

                return new PostFilteredSchema<T>(typeClass, instantiator, fields)
                {
                    @Override
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field == null ? 0 : field.number;
                    }

                    @Override
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0;
                        number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ?
                                    fieldsByNumber[number] : null;

                            if (field == null || !fieldsByName.containsKey(field.name))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }

                    @Override
                    public String getFieldName(int number)
                    {
                        // only called during writes
                        final Field<T> field = number < fieldsByNumber.length ?
                                fieldsByNumber[number] : null;

                        return field == null ? null : field.name;
                    }

                    @Override
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for (Field<T> f : fields)
                            f.writeTo(output, message);
                    }
                };
            }
        },

        /**
         * Optimized only for merging. If you need to do writes, use {@link #INCLUDE} instead. The extra map lookups
         * during writes makes this slower than {@link #INCLUDE}.
         * <p>
         * The args param is required (the field names to include).
         */
        INCLUDE_OPTIMIZED_FOR_MERGE_ONLY
        {
            @Override
            public <T> Schema<T> create(
                    Class<T> typeClass,
                    final Field<T>[] fields,
                    final Field<T>[] fieldsByNumber,
                    Map<String, Field<T>> byName,
                    Instantiator<T> instantiator,
                    Predicate.Factory pf,
                    String[] args)
            {
                final HashMap<String, Field<T>> fieldsByName =
                        new HashMap<>();

                includeAndAddTo(fieldsByName, typeClass, byName, args);

                return new BaseSchema<T>(typeClass, instantiator)
                {
                    @Override
                    public int getFieldNumber(String name)
                    {
                        final Field<T> field = fieldsByName.get(name);
                        return field == null ? 0 : field.number;
                    }

                    @Override
                    public void mergeFrom(Input input, T message) throws IOException
                    {
                        for (int number = input.readFieldNumber(this); number != 0;
                        number = input.readFieldNumber(this))
                        {
                            final Field<T> field = number < fieldsByNumber.length ?
                                    fieldsByNumber[number] : null;

                            if (field == null || !fieldsByName.containsKey(field.name))
                                input.handleUnknownField(number, this);
                            else
                                field.mergeFrom(input, message);
                        }
                    }

                    @Override
                    public String getFieldName(int number)
                    {
                        // only called during writes
                        // already filtered on writeTo (the method below)
                        final Field<T> field = number < fieldsByNumber.length ?
                                fieldsByNumber[number] : null;

                        return field == null ? null : field.name;
                    }

                    @Override
                    public void writeTo(Output output, T message) throws IOException
                    {
                        for (Field<T> f : fields)
                        {
                            // this extra lookup makes this slower
                            // hence use INCLUDE instead
                            if (fieldsByName.containsKey(f.name))
                                f.writeTo(output, message);
                        }
                    }
                };
            }
        };
    }

    static <T> HashMap<String, Field<T>> copyAndExclude(Class<T> typeClass,
            Map<String, Field<T>> byName, final Predicate predicate)
    {
        // copy
        final HashMap<String, Field<T>> map = new HashMap<>(byName);

        for (Field<T> f : byName.values())
        {
            if (predicate.apply(f))
                map.remove(f.name);
        }

        if (map.size() == 0)
        {
            throw new IllegalArgumentException("No fields are left.  " +
                    "Everything was excluded.");
        }

        return map;
    }

    static <T> HashMap<String, Field<T>> copyAndExclude(Class<T> typeClass,
            Map<String, Field<T>> byName, final String[] args)
    {
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("You must provide at least 1 field to exclude.");

        // copy
        final HashMap<String, Field<T>> map = new HashMap<>(byName);

        for (String name : args)
        {
            // remove
            if (null == map.remove(name))
            {
                throw new IllegalArgumentException(name +
                        " field is either a duplicate or not a field of " + typeClass);
            }
        }

        if (map.size() == 0)
        {
            throw new IllegalArgumentException("No fields are left.  " +
                    "Everything was excluded.");
        }

        return map;
    }

    static <T> int includeAndAddTo(Map<String, Field<T>> map,
            Class<T> typeClass, Map<String, Field<T>> byName, final String[] args)
    {
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("You must provide at least 1 field to include.");

        int maxFieldNumber = 0;
        for (String name : args)
        {
            final Field<T> field = byName.get(name);
            if (field == null)
            {
                throw new IllegalArgumentException(name +
                        " is not a field of " + typeClass);
            }

            map.put(field.name, field);
            maxFieldNumber = Math.max(field.number, maxFieldNumber);
        }

        return maxFieldNumber;
    }
}
