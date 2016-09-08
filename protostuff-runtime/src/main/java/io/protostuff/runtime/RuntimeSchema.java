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

import static io.protostuff.runtime.RuntimeEnv.ID_STRATEGY;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.protostuff.Exclude;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.RuntimeEnv.DefaultInstantiator;
import io.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * A schema that can be generated and cached at runtime for objects that have no schema. This is particularly useful for
 * pojos from 3rd party libraries.
 * 
 * @author David Yu
 */
public final class RuntimeSchema<T> implements Schema<T>, FieldMap<T>
{

    public static final int MIN_TAG_VALUE = 1;
    public static final int MAX_TAG_VALUE = 536870911; // 2^29 - 1
    public static final String ERROR_TAG_VALUE = "Invalid tag number (value must be in range [1, 2^29-1])";

    private static final Set<String> NO_EXCLUSIONS = Collections.emptySet();

	public static final int MIN_TAG_FOR_HASH_FIELD_MAP = 100;

	private final Pipe.Schema<T> pipeSchema;
	private final FieldMap<T> fieldMap;
    private final Class<T> typeClass;

    /**
     * Maps the {@code baseClass} to a specific non-interface/non-abstract {@code typeClass} and registers it (this must
     * be done on application startup).
     * <p>
     * With this approach, there is no overhead of writing the type metadata if a {@code baseClass} field is serialized.
     * <p>
     * Returns true if the baseClass does not exist.
     * <p>
     * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is {@link DefaultIdStrategy}.
     * 
     * @throws IllegalArgumentException
     *             if the {@code typeClass} is an interface or an abstract class.
     */
    public static <T> boolean map(Class<? super T> baseClass, Class<T> typeClass)
    {
        if (ID_STRATEGY instanceof DefaultIdStrategy)
            return ((DefaultIdStrategy) ID_STRATEGY).map(baseClass, typeClass);

        throw new RuntimeException(
                "RuntimeSchema.map is only supported on DefaultIdStrategy");
    }

    /**
     * Returns true if this there is no existing one or the same schema has already been registered (this must be done
     * on application startup).
     * <p>
     * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is {@link DefaultIdStrategy}.
     */
    public static <T> boolean register(Class<T> typeClass, Schema<T> schema)
    {
        if (ID_STRATEGY instanceof DefaultIdStrategy)
            return ((DefaultIdStrategy) ID_STRATEGY).registerPojo(typeClass,
                    schema);

        throw new RuntimeException(
                "RuntimeSchema.register is only supported on DefaultIdStrategy");
    }
    
    /**
     * Returns true if this there is no existing one or the same schema has already been registered (this must be done
     * on application startup).
     * <p>
     * NOTE: This is only supported when {@link RuntimeEnv#ID_STRATEGY} is {@link DefaultIdStrategy}.
     */
    public static <T> boolean register(Class<T> typeClass)
    {
        if (ID_STRATEGY instanceof DefaultIdStrategy)
            return ((DefaultIdStrategy) ID_STRATEGY).registerPojo(typeClass);

        throw new RuntimeException(
                "RuntimeSchema.register is only supported on DefaultIdStrategy");
    }

    /**
     * Returns true if the {@code typeClass} was not lazily created.
     * <p>
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
     * <p>
     * Method overload for backwards compatibility.
     */
    public static <T> Schema<T> getSchema(Class<T> typeClass)
    {
        return getSchema(typeClass, ID_STRATEGY);
    }

    /**
     * Gets the schema that was either registered or lazily initialized at runtime.
     */
    public static <T> Schema<T> getSchema(Class<T> typeClass,
            IdStrategy strategy)
    {
        return strategy.getSchemaWrapper(typeClass, true).getSchema();
    }

    /**
     * Returns the schema wrapper.
     * <p>
     * Method overload for backwards compatibility.
     */
    static <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass)
    {
        return getSchemaWrapper(typeClass, ID_STRATEGY);
    }

    /**
     * Returns the schema wrapper.
     */
    static <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass,
            IdStrategy strategy)
    {
        return strategy.getSchemaWrapper(typeClass, true);
    }

    /**
     * Generates a schema from the given class.
     * <p>
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
        for (String exclusion : exclusions)
            set.add(exclusion);

        return createFrom(typeClass, set, strategy);
    }

    /**
     * Generates a schema from the given class with the exclusion of certain fields.
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass,
            Set<String> exclusions, IdStrategy strategy)
    {
        if (typeClass.isInterface()
                || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new RuntimeException(
                    "The root object can neither be an abstract "
                            + "class nor interface: \"" + typeClass.getName());
        }

        final Map<String, java.lang.reflect.Field> fieldMap = findInstanceFields(typeClass);
        final ArrayList<Field<T>> fields = new ArrayList<Field<T>>(
                fieldMap.size());
        int i = 0;
        boolean annotated = false;
        for (java.lang.reflect.Field f : fieldMap.values())
        {
            if (!exclusions.contains(f.getName()))
            {
                if (f.getAnnotation(Deprecated.class) != null)
                {
                    // this field should be ignored by ProtoStuff.
                    // preserve its field number for backward-forward compat
                    i++;
                    continue;
                }

                final Tag tag = f.getAnnotation(Tag.class);
                final int fieldMapping;
                final String name;
                if (tag == null)
                {
                    // Fields gets assigned mapping tags according to their
                    // definition order
                    if (annotated)
                    {
                        String className = typeClass.getCanonicalName();
                        String fieldName = f.getName();
                        String message = String.format("%s#%s is not annotated with @Tag", className, fieldName);
                        throw new RuntimeException(message);
                    }
                    fieldMapping = ++i;

                    name = f.getName();
                }
                else
                {
                    // Fields gets assigned mapping tags according to their
                    // annotation
                    if (!annotated && !fields.isEmpty())
                    {
                        throw new RuntimeException(
                                "When using annotation-based mapping, "
                                        + "all fields must be annotated with @"
                                        + Tag.class.getSimpleName());
                    }
                    annotated = true;
                    fieldMapping = tag.value();

                    if (fieldMapping < MIN_TAG_VALUE || fieldMapping > MAX_TAG_VALUE)
                    {
                        throw new IllegalArgumentException(ERROR_TAG_VALUE + ": " + fieldMapping + " on " + typeClass);
                    }

                    name = tag.alias().isEmpty() ? f.getName() : tag.alias();
                }

                final Field<T> field = RuntimeFieldFactory.getFieldFactory(
                        f.getType(), strategy).create(fieldMapping, name, f,
                        strategy);
                fields.add(field);
            }
        }

        return new RuntimeSchema<T>(typeClass, fields,
                RuntimeEnv.newInstantiator(typeClass));
    }

    /**
     * Generates a schema from the given class with the declared fields (inclusive) based from the given Map. The value
     * of a the Map's entry will be the name used for the field (which enables aliasing).
     */
    public static <T> RuntimeSchema<T> createFrom(Class<T> typeClass,
            Map<String, String> declaredFields, IdStrategy strategy)
    {
        if (typeClass.isInterface()
                || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new RuntimeException(
                    "The root object can neither be an abstract "
                            + "class nor interface: \"" + typeClass.getName());
        }

        final ArrayList<Field<T>> fields = new ArrayList<Field<T>>(
                declaredFields.size());
        int i = 0;
        for (Map.Entry<String, String> entry : declaredFields.entrySet())
        {
            final java.lang.reflect.Field f;
            try
            {
                f = typeClass.getDeclaredField(entry.getKey());
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Exception on field: "
                        + entry.getKey(), e);
            }

            final int mod = f.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && f.getAnnotation(Exclude.class) == null)
            {
                final Field<T> field = RuntimeFieldFactory.getFieldFactory(
                        f.getType(), strategy).create(++i, entry.getValue(), f,
                        strategy);
                fields.add(field);
            }
        }
        return new RuntimeSchema<T>(typeClass, fields, RuntimeEnv.newInstantiator(typeClass));
    }

    static Map<String, java.lang.reflect.Field> findInstanceFields(
            Class<?> typeClass)
    {
        LinkedHashMap<String, java.lang.reflect.Field> fieldMap = new LinkedHashMap<String, java.lang.reflect.Field>();
        fill(fieldMap, typeClass);
        return fieldMap;
    }

    static void fill(Map<String, java.lang.reflect.Field> fieldMap,
            Class<?> typeClass)
    {
        if (Object.class != typeClass.getSuperclass())
            fill(fieldMap, typeClass.getSuperclass());

        for (java.lang.reflect.Field f : typeClass.getDeclaredFields())
        {
            int mod = f.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isTransient(mod) && f.getAnnotation(Exclude.class) == null)
                fieldMap.put(f.getName(), f);
        }
    }

    public final Instantiator<T> instantiator;

    public RuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, Constructor<T> constructor)
    {
        this(typeClass, fields, new DefaultInstantiator<T>(
                constructor));
    }

    public RuntimeSchema(Class<T> typeClass, Collection<Field<T>> fields, Instantiator<T> instantiator)
    {
		this.fieldMap = createFieldMap(fields);
		this.pipeSchema = new RuntimePipeSchema<T>(this, fieldMap);
        this.instantiator = instantiator;
        this.typeClass = typeClass;
    }

	private FieldMap<T> createFieldMap(Collection<Field<T>> fields)
	{
		int lastFieldNumber = 0;
		for (Field<T> field : fields)
		{
			if (field.number > lastFieldNumber)
			{
				lastFieldNumber = field.number;
			}
		}
		if (preferHashFieldMap(fields, lastFieldNumber))
		{
			return new HashFieldMap<T>(fields);
		}
		// array field map should be more efficient
		return new ArrayFieldMap<T>(fields, lastFieldNumber);
	}

	private boolean preferHashFieldMap(Collection<Field<T>> fields, int lastFieldNumber)
	{
		return lastFieldNumber > MIN_TAG_FOR_HASH_FIELD_MAP && lastFieldNumber >= 2 * fields.size();
	}

	/**
	 * Returns the pipe schema linked to this.
	 */
	public Pipe.Schema<T> getPipeSchema()
	{
		return pipeSchema;
	}

	@Override
	public Field<T> getFieldByNumber(int n)
	{
		return fieldMap.getFieldByNumber(n);
	}

	@Override
	public Field<T> getFieldByName(String fieldName)
	{
		return fieldMap.getFieldByName(fieldName);
	}

	@Override
	public int getFieldCount()
	{
		return fieldMap.getFieldCount();
	}

	@Override
	public List<Field<T>> getFields()
	{
		return fieldMap.getFields();
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
    public String getFieldName(int number)
    {
        // only called on writes
        final Field<T> field = getFieldByNumber(number);
        return field == null ? null : field.name;
    }

    @Override
    public int getFieldNumber(String name)
    {
        final Field<T> field = getFieldByName(name);
        return field == null ? 0 : field.number;
    }

    @Override
    public final void mergeFrom(Input input, T message) throws IOException
    {
        for (int n = input.readFieldNumber(this); n != 0; n = input.readFieldNumber(this))
        {
            final Field<T> field = getFieldByNumber(n);
            if (field == null)
            {
                input.handleUnknownField(n, this);
            }
            else
            {
                field.mergeFrom(input, message);
            }
        }
    }

    @Override
    public final void writeTo(Output output, T message) throws IOException
    {
        for (Field<T> f : getFields())
            f.writeTo(output, message);
    }

    /**
     * Always returns true, everything is optional.
     */
    @Override
    public boolean isInitialized(T message)
    {
        return true;
    }

    @Override
    public T newMessage()
    {
        return instantiator.newInstance();
    }

    /**
     * Invoked only when applications are having pipe io operations.
     */
    @SuppressWarnings("unchecked")
    static <T> Pipe.Schema<T> resolvePipeSchema(Schema<T> schema,
            Class<? super T> clazz, boolean throwIfNone)
    {
        if (Message.class.isAssignableFrom(clazz))
        {
            try
            {
                // use the pipe schema of code-generated messages if available.
                java.lang.reflect.Method m = clazz.getDeclaredMethod("getPipeSchema");
                return (Pipe.Schema<T>) m.invoke(null);
            }
            catch (Exception e)
            {
                // ignore
            }
        }

        if (RuntimeSchema.class.isAssignableFrom(schema.getClass()))
            return ((RuntimeSchema<T>) schema).getPipeSchema();

        if (throwIfNone)
            throw new RuntimeException("No pipe schema for: " + clazz);

        return null;
    }
}
