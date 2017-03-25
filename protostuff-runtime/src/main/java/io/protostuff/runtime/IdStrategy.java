package io.protostuff.runtime;

import io.protostuff.CollectionSchema;
import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.MapSchema;
import io.protostuff.MapSchema.MapWrapper;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This base class handles all the IO for reading and writing polymorphic fields. When a field's type is
 * polymorphic/dynamic (e.g interface/abstract/object), the type (id) needs to be written (ahead) before its
 * value/content to be able to deserialize it correctly.
 * <p>
 * The underlying impl will determine how the type (id) should be written.
 * <p>
 * An {@link IdStrategy} is standalone if the {@link #primaryGroup} is not set.
 * 
 * @author Leo Romanoff
 * @author David Yu
 */
public abstract class IdStrategy
{
    
    public static final int 
            ENUMS_BY_NAME = 1,
            AUTO_LOAD_POLYMORPHIC_CLASSES = 1 << 1,
            ALLOW_NULL_ARRAY_ELEMENT = 1 << 2,
            MORPH_NON_FINAL_POJOS = 1 << 3,
            MORPH_COLLECTION_INTERFACES = 1 << 4,
            MORPH_MAP_INTERFACES = 1 << 5,
            COLLECTION_SCHEMA_ON_REPEATED_FIELDS = 1 << 6,
            POJO_SCHEMA_ON_COLLECTION_FIELDS = 1 << 7,
            POJO_SCHEMA_ON_MAP_FIELDS = 1 << 8,
            DEFAULT_FLAGS;
    
    static
    {
        int flags = 0;
        
        if (RuntimeEnv.ENUMS_BY_NAME)
            flags |= ENUMS_BY_NAME;
        
        if (RuntimeEnv.AUTO_LOAD_POLYMORPHIC_CLASSES)
            flags |= AUTO_LOAD_POLYMORPHIC_CLASSES;
        
        if (RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT)
            flags |= ALLOW_NULL_ARRAY_ELEMENT;
        
        if (RuntimeEnv.MORPH_NON_FINAL_POJOS)
            flags |= MORPH_NON_FINAL_POJOS;
        
        if (RuntimeEnv.MORPH_COLLECTION_INTERFACES)
            flags |= MORPH_COLLECTION_INTERFACES;
        
        if (RuntimeEnv.MORPH_MAP_INTERFACES)
            flags |= MORPH_MAP_INTERFACES;
        
        if (RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            flags |= COLLECTION_SCHEMA_ON_REPEATED_FIELDS;
        
        if (RuntimeEnv.POJO_SCHEMA_ON_COLLECTION_FIELDS)
            flags |= POJO_SCHEMA_ON_COLLECTION_FIELDS;
        
        if (RuntimeEnv.POJO_SCHEMA_ON_MAP_FIELDS)
            flags |= POJO_SCHEMA_ON_MAP_FIELDS;
        
        DEFAULT_FLAGS = flags;
    }
    
    public final int flags;
    public final IdStrategy primaryGroup;
    public final int groupId;
    
    protected IdStrategy(int flags, IdStrategy primaryGroup, int groupId)
    {
        if (primaryGroup != null)
        {
            if (groupId <= 0 || 0 != (groupId & (groupId - 1)))
            {
                throw new RuntimeException(
                        "The groupId must be a power of two (1,2,4,8,etc).");
            }
        }
        else if (groupId != 0)
        {
            throw new RuntimeException("An IdStrategy without a primaryGroup "
                    + "(standalone) must have a groupId of zero.");
        }

        this.flags = flags;
        this.primaryGroup = primaryGroup;
        this.groupId = groupId;
    }
    
    /**
     * Generates a schema from the given class. If this strategy is part of a group, the existing fields of that group's
     * schema will be re-used.
     */
    protected <T> Schema<T> newSchema(Class<T> typeClass)
    {
        if (primaryGroup == null)
            return RuntimeSchema.createFrom(typeClass, this);
        
        final Schema<T> s = primaryGroup.getSchemaWrapper(typeClass, true).getSchema();
        
        // only pojos created by runtime schema support groups
        if (!(s instanceof RuntimeSchema))
            return s;
        
        final RuntimeSchema<T> rs = (RuntimeSchema<T>) s;
        
        // check if we need to filter
        if (rs.getFieldCount() == 0)
            return rs;
        
        final ArrayList<Field<T>> fields = new ArrayList<Field<T>>(rs.getFieldCount());
        
        for (Field<T> f : rs.getFields())
        {
            final int groupFilter = f.groupFilter;
            if (groupFilter != 0)
            {
                final int set; // set for exclusion
                if (groupFilter > 0)
                {
                    // inclusion
                    set = ~groupFilter & 0x7FFFFFFF;
                }
                else
                {
                    // exclusion
                    set = -groupFilter;
                }

                if (0 != (groupId & set))
                {
                    // this field is excluded on the current group id
                    continue;
                }
            }
            
            fields.add(f);
        }
        
        // The behavior has been changed to always allow messages with zero fields
        // regardless if it has a primary group or not.
        /*if (fields.size() == 0)
        {
            throw new RuntimeException("All fields were excluded for "
                    + rs.messageFullName() + " on group " + groupId);
        }*/
        
        return fields.size() == rs.getFieldCount() ? rs : 
                new RuntimeSchema<T>(typeClass, fields, rs.instantiator);
    }

    /**
     * Thrown when a type is not known by the IdStrategy. The DefaultIdStrategy will never throw this exception though.
     */
    public static class UnknownTypeException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;

        public UnknownTypeException(String msg)
        {
            super(msg);
        }
    }

    /**
     * Responsible for instantiating custom {@link IdStrategy} impls.
     */
    public interface Factory
    {
        /**
         * Creates a new {@link IdStrategy} instance (impl).
         */
        public IdStrategy create();

        /**
         * Called after the method {@link #create()} has been called. This is used to prevent classloader issues.
         * RuntimeEnv's {@link RuntimeEnv#ID_STRATEGY} need to be set first.
         */
        public void postCreate();
    }

    /**
     * Returns true if there is a {@link Delegate} explicitly registered for the {@code typeClass}.
     */
    public abstract boolean isDelegateRegistered(Class<?> typeClass);

    /**
     * Returns the {@link Delegate delegate}.
     */
    public abstract <T> HasDelegate<T> getDelegateWrapper(
            Class<? super T> typeClass);

    /**
     * Returns the {@link Delegate delegate}.
     */
    public abstract <T> Delegate<T> getDelegate(Class<? super T> typeClass);

    /**
     * Returns true if the {@code typeClass} is explicitly registered.
     */
    public abstract boolean isRegistered(Class<?> typeClass);

    /**
     * Returns the {@link HasSchema schema wrapper}. The caller is responsible that the typeClass is a pojo (e.g not an
     * enum/array/etc).
     */
    public abstract <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass,
            boolean create);

    /**
     * Returns the {@link EnumIO}. The callers (internal field factories) are responsible that the class provided is an
     * enum class.
     */
    protected abstract EnumIO<? extends Enum<?>> getEnumIO(Class<?> enumClass);

    /**
     * Returns the {@link CollectionSchema.MessageFactory}. The callers (internal field factories) are responsible that
     * the class provided implements {@link Collection}.
     */
    protected abstract CollectionSchema.MessageFactory getCollectionFactory(
            Class<?> clazz);

    /**
     * Returns the {@link MapSchema.MessageFactory}. The callers (internal field factories}) are responsible that the
     * class provided implements {@link Map}.
     */
    protected abstract MapSchema.MessageFactory getMapFactory(Class<?> clazz);

    // collection

    protected abstract void writeCollectionIdTo(Output output, int fieldNumber,
            Class<?> clazz) throws IOException;

    protected abstract void transferCollectionId(Input input, Output output,
            int fieldNumber) throws IOException;

    protected abstract CollectionSchema.MessageFactory resolveCollectionFrom(
            Input input) throws IOException;

    // map

    protected abstract void writeMapIdTo(Output output, int fieldNumber,
            Class<?> clazz) throws IOException;

    protected abstract void transferMapId(Input input, Output output,
            int fieldNumber) throws IOException;

    protected abstract MapSchema.MessageFactory resolveMapFrom(Input input)
            throws IOException;

    // enum

    protected abstract void writeEnumIdTo(Output output, int fieldNumber,
            Class<?> clazz) throws IOException;

    protected abstract void transferEnumId(Input input, Output output,
            int fieldNumber) throws IOException;

    protected abstract EnumIO<?> resolveEnumFrom(Input input)
            throws IOException;

    // pojo
    
    protected abstract <T> HasSchema<T> tryWritePojoIdTo(Output output,
            int fieldNumber, Class<T> clazz, boolean registered) throws IOException;

    protected abstract <T> HasSchema<T> writePojoIdTo(Output output,
            int fieldNumber, Class<T> clazz) throws IOException;

    protected abstract <T> HasSchema<T> transferPojoId(Input input,
            Output output, int fieldNumber) throws IOException;

    protected abstract <T> HasSchema<T> resolvePojoFrom(Input input,
            int fieldNumber) throws IOException;

    protected abstract <T> Schema<T> writeMessageIdTo(Output output,
            int fieldNumber, Message<T> message) throws IOException;

    // delegate

    /**
     * If this method returns null, the clazz was not registered as a delegate.
     */
    protected abstract <T> HasDelegate<T> tryWriteDelegateIdTo(Output output,
            int fieldNumber, Class<T> clazz) throws IOException;

    protected abstract <T> HasDelegate<T> transferDelegateId(Input input,
            Output output, int fieldNumber) throws IOException;

    protected abstract <T> HasDelegate<T> resolveDelegateFrom(Input input)
            throws IOException;

    // array

    protected abstract void writeArrayIdTo(Output output, Class<?> componentType)
            throws IOException;

    protected abstract void transferArrayId(Input input, Output output,
            int fieldNumber, boolean mapped) throws IOException;

    protected abstract Class<?> resolveArrayComponentTypeFrom(Input input,
            boolean mapped) throws IOException;

    // class

    protected abstract void writeClassIdTo(Output output,
            Class<?> componentType, boolean array) throws IOException;

    protected abstract void transferClassId(Input input, Output output,
            int fieldNumber, boolean mapped, boolean array) throws IOException;

    protected abstract Class<?> resolveClassFrom(Input input, boolean mapped,
            boolean array) throws IOException;

    // polymorphic requirements

    final DerivativeSchema polymorphicPojoElementSchema = new DerivativeSchema(
            this)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void doMergeFrom(Input input, Schema<Object> derivedSchema,
                Object owner) throws IOException
        {
            final Object value = derivedSchema.newMessage();

            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);

            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(value, owner);
            }

            derivedSchema.mergeFrom(input, value);
        }
    };

    // object polymorphic schema requirements

    final ArraySchema arrayElementSchema = new ArraySchema(this)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };

    final NumberSchema numberElementSchema = new NumberSchema(this)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };

    final ClassSchema classElementSchema = new ClassSchema(this)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };

    final PolymorphicEnumSchema polymorphicEnumElementSchema = new PolymorphicEnumSchema(
            this)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };

    final PolymorphicThrowableSchema polymorphicThrowableElementSchema = new PolymorphicThrowableSchema(
            this)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };

    final ObjectSchema objectElementSchema = new ObjectSchema(this)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };

    // object dynamic schema requirements

    final Schema<Object> dynamicValueSchema = new Schema<Object>()
    {
        @Override
        public String getFieldName(int number)
        {
            return ObjectSchema.name(number);
        }

        @Override
        public int getFieldNumber(String name)
        {
            return ObjectSchema.number(name);
        }

        @Override
        public boolean isInitialized(Object owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Object.class.getName();
        }

        @Override
        public String messageName()
        {
            return Object.class.getSimpleName();
        }

        @Override
        public Object newMessage()
        {
            // cannot instantiate because the type is dynamic.
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void mergeFrom(Input input, Object owner) throws IOException
        {
            if (PMapWrapper.class == owner.getClass())
            {
                // called from ENTRY_SCHEMA
                ((PMapWrapper) owner).setValue(ObjectSchema.readObjectFrom(
                        input, this, owner, IdStrategy.this));
            }
            else
            {
                // called from COLLECTION_SCHEMA
                ((Collection<Object>) owner).add(ObjectSchema.readObjectFrom(
                        input, this, owner, IdStrategy.this));
            }
        }

        @Override
        public void writeTo(Output output, Object message) throws IOException
        {
            ObjectSchema.writeObjectTo(output, message, this, IdStrategy.this);
        }
    };

    final Pipe.Schema<Object> dynamicValuePipeSchema = new Pipe.Schema<Object>(
            dynamicValueSchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            ObjectSchema.transferObject(this, pipe, input, output,
                    IdStrategy.this);
        }
    };

    final Schema<Collection<Object>> collectionSchema = new Schema<Collection<Object>>()
    {
        @Override
        public String getFieldName(int number)
        {
            return number == 1 ? CollectionSchema.FIELD_NAME_VALUE : null;
        }

        @Override
        public int getFieldNumber(String name)
        {
            return name.length() == 1 && name.charAt(0) == 'v' ? 1 : 0;
        }

        @Override
        public boolean isInitialized(Collection<Object> owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Collection.class.getName();
        }

        @Override
        public String messageName()
        {
            return Collection.class.getSimpleName();
        }

        @Override
        public Collection<Object> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Collection<Object>> typeClass()
        {
            return Collection.class;
        }

        @Override
        public void mergeFrom(Input input, Collection<Object> message)
                throws IOException
        {
            for (int number = input.readFieldNumber(this);; number = input
                    .readFieldNumber(this))
            {
                switch (number)
                {
                    case 0:
                        return;
                    case 1:
                        final Object value = input.mergeObject(message,
                                dynamicValueSchema);
                        if (input instanceof GraphInput
                                && ((GraphInput) input).isCurrentMessageReference())
                        {
                            // a reference from polymorphic+cyclic graph deser
                            message.add(value);
                        }
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }
        }

        @Override
        public void writeTo(Output output, Collection<Object> message)
                throws IOException
        {
            for (Object value : message)
            {
                if (value != null)
                    output.writeObject(1, value, dynamicValueSchema, true);
            }
        }
    };

    final Pipe.Schema<Collection<Object>> collectionPipeSchema = new Pipe.Schema<Collection<Object>>(
            collectionSchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            for (int number = input.readFieldNumber(wrappedSchema);; number = input
                    .readFieldNumber(wrappedSchema))
            {
                switch (number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, dynamicValuePipeSchema,
                                true);
                        break;
                    default:
                        throw new ProtostuffException(
                                "The collection was incorrectly " + "serialized.");
                }
            }
        }
    };

    final Schema<Object> arraySchema = new Schema<Object>()
    {
        @Override
        public String getFieldName(int number)
        {
            return number == 1 ? CollectionSchema.FIELD_NAME_VALUE : null;
        }

        @Override
        public int getFieldNumber(String name)
        {
            return name.length() == 1 && name.charAt(0) == 'v' ? 1 : 0;
        }

        @Override
        public boolean isInitialized(Object owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Array.class.getName();
        }

        @Override
        public String messageName()
        {
            return Array.class.getSimpleName();
        }

        @Override
        public Object newMessage()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        @Override
        public void mergeFrom(Input input, Object message) throws IOException
        {
            // using COLLECTION_SCHEMA instead.
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, Object message) throws IOException
        {
            for (int i = 0, len = Array.getLength(message); i < len; i++)
            {
                final Object value = Array.get(message, i);
                if (value != null)
                {
                    output.writeObject(1, value, dynamicValueSchema, true);
                }
            }
        }
    };

    final Pipe.Schema<Object> arrayPipeSchema = new Pipe.Schema<Object>(
            arraySchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            for (int number = input.readFieldNumber(wrappedSchema);; number = input
                    .readFieldNumber(wrappedSchema))
            {
                switch (number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, dynamicValuePipeSchema,
                                true);
                        break;
                    default:
                        throw new ProtostuffException("The array was incorrectly "
                                + "serialized.");
                }
            }
        }
    };

    final Schema<Map<Object, Object>> mapSchema = new Schema<Map<Object, Object>>()
    {
        @Override
        public final String getFieldName(int number)
        {
            return number == 1 ? MapSchema.FIELD_NAME_ENTRY : null;
        }

        @Override
        public final int getFieldNumber(String name)
        {
            return name.length() == 1 && name.charAt(0) == 'e' ? 1 : 0;
        }

        @Override
        public boolean isInitialized(Map<Object, Object> owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Map.class.getName();
        }

        @Override
        public String messageName()
        {
            return Map.class.getSimpleName();
        }

        @Override
        public Map<Object, Object> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Map<Object, Object>> typeClass()
        {
            return Map.class;
        }

        @Override
        public void mergeFrom(Input input, Map<Object, Object> message)
                throws IOException
        {
            PMapWrapper entry = null;
            for (int number = input.readFieldNumber(this);; number = input
                    .readFieldNumber(this))
            {
                switch (number)
                {
                    case 0:
                        return;
                    case 1:
                        if (entry == null)
                        {
                            // lazy initialize
                            entry = new PMapWrapper(message);
                        }

                        if (entry != input.mergeObject(entry, entrySchema))
                        {
                            // an entry will always be unique
                            // it can never be a reference.
                            throw new IllegalStateException(
                                    "A Map.Entry will always be "
                                            + "unique, hence it cannot be a reference "
                                            + "obtained from "
                                            + input.getClass().getName());
                        }
                        break;
                    default:
                        throw new ProtostuffException(
                                "The map was incorrectly serialized.");
                }
            }
        }

        @Override
        public void writeTo(Output output, Map<Object, Object> message)
                throws IOException
        {
            for (Map.Entry<Object, Object> entry : message.entrySet())
            {
                output.writeObject(1, entry, entrySchema, true);
            }
        }
    };

    final Pipe.Schema<Map<Object, Object>> mapPipeSchema = new Pipe.Schema<Map<Object, Object>>(
            mapSchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            for (int number = input.readFieldNumber(wrappedSchema);; number = input
                    .readFieldNumber(wrappedSchema))
            {
                switch (number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, entryPipeSchema, true);
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly "
                                + "serialized.");
                }
            }
        }
    };

    final Schema<Entry<Object, Object>> entrySchema = new Schema<Entry<Object, Object>>()
    {
        @Override
        public final String getFieldName(int number)
        {
            switch (number)
            {
                case 1:
                    return MapSchema.FIELD_NAME_KEY;
                case 2:
                    return MapSchema.FIELD_NAME_VALUE;
                default:
                    return null;
            }
        }

        @Override
        public final int getFieldNumber(String name)
        {
            if (name.length() != 1)
                return 0;

            switch (name.charAt(0))
            {
                case 'k':
                    return 1;
                case 'v':
                    return 2;
                default:
                    return 0;
            }
        }

        @Override
        public boolean isInitialized(Entry<Object, Object> message)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Entry.class.getName();
        }

        @Override
        public String messageName()
        {
            return Entry.class.getSimpleName();
        }

        @Override
        public Entry<Object, Object> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Entry<Object, Object>> typeClass()
        {
            return Entry.class;
        }

        @Override
        public void mergeFrom(Input input, Entry<Object, Object> message)
                throws IOException
        {
            // Nobody else calls this except MAP_SCHEMA.mergeFrom
            final PMapWrapper entry = (PMapWrapper) message;

            Object key = null, value = null;
            for (int number = input.readFieldNumber(this);; number = input
                    .readFieldNumber(this))
            {
                switch (number)
                {
                    case 0:
                        entry.map.put(key, value);
                        return;
                    case 1:
                        if (key != null)
                        {
                            throw new ProtostuffException(
                                    "The map was incorrectly " + "serialized.");
                        }
                        key = input.mergeObject(entry, dynamicValueSchema);
                        if (entry != key)
                        {
                            // a reference.
                            assert key != null;
                        }
                        else
                        {
                            // entry held the key
                            key = entry.setValue(null);
                            assert key != null;
                        }
                        break;
                    case 2:
                        if (value != null)
                        {
                            throw new ProtostuffException(
                                    "The map was incorrectly " + "serialized.");
                        }
                        value = input.mergeObject(entry, dynamicValueSchema);
                        if (entry != value)
                        {
                            // a reference.
                            assert value != null;
                        }
                        else
                        {
                            // entry held the value
                            value = entry.setValue(null);
                            assert value != null;
                        }
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly "
                                + "serialized.");
                }
            }
        }

        @Override
        public void writeTo(Output output, Entry<Object, Object> entry)
                throws IOException
        {
            if (entry.getKey() != null)
                output.writeObject(1, entry.getKey(), dynamicValueSchema,
                        false);

            if (entry.getValue() != null)
                output.writeObject(2, entry.getValue(), dynamicValueSchema,
                        false);
        }
    };

    final Pipe.Schema<Entry<Object, Object>> entryPipeSchema = new Pipe.Schema<Entry<Object, Object>>(
            entrySchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            for (int number = input.readFieldNumber(wrappedSchema);; number = input
                    .readFieldNumber(wrappedSchema))
            {
                switch (number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, dynamicValuePipeSchema,
                                false);
                        break;
                    case 2:
                        output.writeObject(number, pipe, dynamicValuePipeSchema,
                                false);
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly "
                                + "serialized.");
                }
            }
        }
    };

    final Schema<Object> objectSchema = new Schema<Object>()
    {
        @Override
        public String getFieldName(int number)
        {
            return ObjectSchema.name(number);
        }

        @Override
        public int getFieldNumber(String name)
        {
            return ObjectSchema.number(name);
        }

        @Override
        public boolean isInitialized(Object owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Object.class.getName();
        }

        @Override
        public String messageName()
        {
            return Object.class.getSimpleName();
        }

        @Override
        public Object newMessage()
        {
            // cannot instantiate because the type is dynamic.
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        @Override
        public void mergeFrom(Input input, Object owner) throws IOException
        {
            ((Wrapper) owner).value = ObjectSchema.readObjectFrom(input, this,
                    owner, IdStrategy.this);
        }

        @Override
        public void writeTo(Output output, Object message) throws IOException
        {
            ObjectSchema.writeObjectTo(output, message, this, IdStrategy.this);
        }
    };

    final Pipe.Schema<Object> objectPipeSchema = new Pipe.Schema<Object>(
            objectSchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            ObjectSchema.transferObject(this, pipe, input, output,
                    IdStrategy.this);
        }
    };

    final Schema<Object> classSchema = new Schema<Object>()
    {
        @Override
        public String getFieldName(int number)
        {
            return ClassSchema.name(number);
        }

        @Override
        public int getFieldNumber(String name)
        {
            return ClassSchema.number(name);
        }

        @Override
        public boolean isInitialized(Object owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Class.class.getName();
        }

        @Override
        public String messageName()
        {
            return Class.class.getSimpleName();
        }

        @Override
        public Object newMessage()
        {
            // cannot instantiate because the type is dynamic.
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        @Override
        public void mergeFrom(Input input, Object owner) throws IOException
        {
            ((Wrapper) owner).value = ClassSchema.readObjectFrom(input, this,
                    owner, IdStrategy.this);
        }

        @Override
        public void writeTo(Output output, Object message) throws IOException
        {
            ClassSchema.writeObjectTo(output, message, this, IdStrategy.this);
        }
    };

    final Pipe.Schema<Object> classPipeSchema = new Pipe.Schema<Object>(
            classSchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            ClassSchema.transferObject(this, pipe, input, output,
                    IdStrategy.this);
        }
    };

    final Schema<Object> polymorphicCollectionSchema = new Schema<Object>()
    {
        @Override
        public String getFieldName(int number)
        {
            return PolymorphicCollectionSchema.name(number);
        }

        @Override
        public int getFieldNumber(String name)
        {
            return PolymorphicCollectionSchema.number(name);
        }

        @Override
        public boolean isInitialized(Object owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Collection.class.getName();
        }

        @Override
        public String messageName()
        {
            return Collection.class.getSimpleName();
        }

        @Override
        public Object newMessage()
        {
            // cannot instantiate because the type is dynamic.
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        @Override
        public void mergeFrom(Input input, Object owner) throws IOException
        {
            ((Wrapper) owner).value = PolymorphicCollectionSchema
                    .readObjectFrom(input, this, owner, IdStrategy.this);
        }

        @Override
        public void writeTo(Output output, Object message) throws IOException
        {
            PolymorphicCollectionSchema.writeObjectTo(output, message, this,
                    IdStrategy.this);
        }
    };

    final Pipe.Schema<Object> polymorphicCollectionPipeSchema = new Pipe.Schema<Object>(
            polymorphicCollectionSchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            PolymorphicCollectionSchema.transferObject(this, pipe, input,
                    output, IdStrategy.this);
        }
    };

    final Schema<Object> polymorphicMapSchema = new Schema<Object>()
    {
        @Override
        public String getFieldName(int number)
        {
            return PolymorphicMapSchema.name(number);
        }

        @Override
        public int getFieldNumber(String name)
        {
            return PolymorphicMapSchema.number(name);
        }

        @Override
        public boolean isInitialized(Object owner)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Map.class.getName();
        }

        @Override
        public String messageName()
        {
            return Map.class.getSimpleName();
        }

        @Override
        public Object newMessage()
        {
            // cannot instantiate because the type is dynamic.
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        @Override
        public void mergeFrom(Input input, Object owner) throws IOException
        {
            ((Wrapper) owner).value = PolymorphicMapSchema.readObjectFrom(
                    input, this, owner, IdStrategy.this);
        }

        @Override
        public void writeTo(Output output, Object message) throws IOException
        {
            PolymorphicMapSchema.writeObjectTo(output, message, this,
                    IdStrategy.this);
        }
    };

    final Pipe.Schema<Object> polymorphicMapPipeSchema = new Pipe.Schema<Object>(
            polymorphicMapSchema)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            PolymorphicMapSchema.transferObject(this, pipe, input, output,
                    IdStrategy.this);
        }
    };
    
    // array element schema
    
    final ArraySchemas.BoolArray ARRAY_BOOL_PRIMITIVE_SCHEMA = 
            new ArraySchemas.BoolArray(this, null, true);
    final ArraySchemas.BoolArray ARRAY_BOOL_BOXED_SCHEMA = 
            new ArraySchemas.BoolArray(this, null, false);
    final ArraySchemas.BoolArray ARRAY_BOOL_DERIVED_SCHEMA = 
            new ArraySchemas.BoolArray(this, null, false)
    {
        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ArraySchemas.ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            int len = input.readInt32();
            return len >= 0 ? readPrimitiveFrom(input, owner, len) : 
                    readBoxedFrom(input, owner, -len - 1);
        }

        @Override
        protected void writeLengthTo(Output output, int len, boolean primitive)
                throws IOException
        {
            if (primitive)
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, len, false);
            else
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, -(len + 1), false);
        }
        
        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            writeTo(output, value, value.getClass().getComponentType().isPrimitive());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.CharArray ARRAY_CHAR_PRIMITIVE_SCHEMA = 
            new ArraySchemas.CharArray(this, null, true);
    final ArraySchemas.CharArray ARRAY_CHAR_BOXED_SCHEMA = 
            new ArraySchemas.CharArray(this, null, false);
    final ArraySchemas.CharArray ARRAY_CHAR_DERIVED_SCHEMA = 
            new ArraySchemas.CharArray(this, null, false)
    {
        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ArraySchemas.ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            int len = input.readInt32();
            return len >= 0 ? readPrimitiveFrom(input, owner, len) : 
                    readBoxedFrom(input, owner, -len - 1);
        }

        @Override
        protected void writeLengthTo(Output output, int len, boolean primitive)
                throws IOException
        {
            if (primitive)
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, len, false);
            else
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, -(len + 1), false);
        }
        
        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            writeTo(output, value, value.getClass().getComponentType().isPrimitive());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.ShortArray ARRAY_SHORT_PRIMITIVE_SCHEMA = 
            new ArraySchemas.ShortArray(this, null, true);
    final ArraySchemas.ShortArray ARRAY_SHORT_BOXED_SCHEMA = 
            new ArraySchemas.ShortArray(this, null, false);
    final ArraySchemas.ShortArray ARRAY_SHORT_DERIVED_SCHEMA = 
            new ArraySchemas.ShortArray(this, null, false)
    {
        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ArraySchemas.ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            int len = input.readInt32();
            return len >= 0 ? readPrimitiveFrom(input, owner, len) : 
                    readBoxedFrom(input, owner, -len - 1);
        }

        @Override
        protected void writeLengthTo(Output output, int len, boolean primitive)
                throws IOException
        {
            if (primitive)
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, len, false);
            else
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, -(len + 1), false);
        }
        
        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            writeTo(output, value, value.getClass().getComponentType().isPrimitive());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.Int32Array ARRAY_INT32_PRIMITIVE_SCHEMA = 
            new ArraySchemas.Int32Array(this, null, true);
    final ArraySchemas.Int32Array ARRAY_INT32_BOXED_SCHEMA = 
            new ArraySchemas.Int32Array(this, null, false);
    final ArraySchemas.Int32Array ARRAY_INT32_DERIVED_SCHEMA = 
            new ArraySchemas.Int32Array(this, null, false)
    {
        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ArraySchemas.ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            int len = input.readInt32();
            return len >= 0 ? readPrimitiveFrom(input, owner, len) : 
                    readBoxedFrom(input, owner, -len - 1);
        }

        @Override
        protected void writeLengthTo(Output output, int len, boolean primitive)
                throws IOException
        {
            if (primitive)
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, len, false);
            else
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, -(len + 1), false);
        }
        
        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            writeTo(output, value, value.getClass().getComponentType().isPrimitive());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.Int64Array ARRAY_INT64_PRIMITIVE_SCHEMA = 
            new ArraySchemas.Int64Array(this, null, true);
    final ArraySchemas.Int64Array ARRAY_INT64_BOXED_SCHEMA = 
            new ArraySchemas.Int64Array(this, null, false);
    final ArraySchemas.Int64Array ARRAY_INT64_DERIVED_SCHEMA = 
            new ArraySchemas.Int64Array(this, null, false)
    {
        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ArraySchemas.ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            int len = input.readInt32();
            return len >= 0 ? readPrimitiveFrom(input, owner, len) : 
                    readBoxedFrom(input, owner, -len - 1);
        }

        @Override
        protected void writeLengthTo(Output output, int len, boolean primitive)
                throws IOException
        {
            if (primitive)
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, len, false);
            else
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, -(len + 1), false);
        }
        
        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            writeTo(output, value, value.getClass().getComponentType().isPrimitive());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.FloatArray ARRAY_FLOAT_PRIMITIVE_SCHEMA = 
            new ArraySchemas.FloatArray(this, null, true);
    final ArraySchemas.FloatArray ARRAY_FLOAT_BOXED_SCHEMA = 
            new ArraySchemas.FloatArray(this, null, false);
    final ArraySchemas.FloatArray ARRAY_FLOAT_DERIVED_SCHEMA = 
            new ArraySchemas.FloatArray(this, null, false)
    {
        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ArraySchemas.ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            int len = input.readInt32();
            return len >= 0 ? readPrimitiveFrom(input, owner, len) : 
                    readBoxedFrom(input, owner, -len - 1);
        }

        @Override
        protected void writeLengthTo(Output output, int len, boolean primitive)
                throws IOException
        {
            if (primitive)
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, len, false);
            else
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, -(len + 1), false);
        }
        
        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            writeTo(output, value, value.getClass().getComponentType().isPrimitive());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.DoubleArray ARRAY_DOUBLE_PRIMITIVE_SCHEMA = 
            new ArraySchemas.DoubleArray(this, null, true);
    final ArraySchemas.DoubleArray ARRAY_DOUBLE_BOXED_SCHEMA = 
            new ArraySchemas.DoubleArray(this, null, false);
    final ArraySchemas.DoubleArray ARRAY_DOUBLE_DERIVED_SCHEMA = 
            new ArraySchemas.DoubleArray(this, null, false)
    {
        @Override
        public Object readFrom(Input input, Object owner) throws IOException
        {
            if (ArraySchemas.ID_ARRAY_LEN != input.readFieldNumber(this))
                throw new ProtostuffException("Corrupt input.");
            
            int len = input.readInt32();
            return len >= 0 ? readPrimitiveFrom(input, owner, len) : 
                    readBoxedFrom(input, owner, -len - 1);
        }

        @Override
        protected void writeLengthTo(Output output, int len, boolean primitive)
                throws IOException
        {
            if (primitive)
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, len, false);
            else
                output.writeInt32(ArraySchemas.ID_ARRAY_LEN, -(len + 1), false);
        }
        
        @Override
        public void writeTo(Output output, Object value) throws IOException
        {
            writeTo(output, value, value.getClass().getComponentType().isPrimitive());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.StringArray ARRAY_STRING_SCHEMA = 
            new ArraySchemas.StringArray(this, null)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.ByteStringArray ARRAY_BYTESTRING_SCHEMA = 
            new ArraySchemas.ByteStringArray(this, null)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.ByteArrayArray ARRAY_BYTEARRAY_SCHEMA = 
            new ArraySchemas.ByteArrayArray(this, null)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.BigDecimalArray ARRAY_BIGDECIMAL_SCHEMA = 
            new ArraySchemas.BigDecimalArray(this, null)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.BigIntegerArray ARRAY_BIGINTEGER_SCHEMA = 
            new ArraySchemas.BigIntegerArray(this, null)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    final ArraySchemas.DateArray ARRAY_DATE_SCHEMA = 
            new ArraySchemas.DateArray(this, null)
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            if (MapWrapper.class == owner.getClass())
                ((MapWrapper<Object, Object>) owner).setValue(value);
            else
                ((Collection<Object>) owner).add(value);
        }
    };
    
    private static final class PMapWrapper implements Entry<Object, Object>
    {

        final Map<Object, Object> map;
        private Object value;

        PMapWrapper(Map<Object, Object> map)
        {
            this.map = map;
        }

        @Override
        public Object getKey()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getValue()
        {
            return value;
        }

        @Override
        public Object setValue(Object value)
        {
            final Object last = this.value;
            this.value = value;
            return last;
        }

    }

    static final class Wrapper
    {
        Object value;
    }
    
    protected static <T> T createMessageInstance(Class<T> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (IllegalAccessException e)
        {
            try
            {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            }
            catch (Exception e1)
            {
                throw new RuntimeException(e);
            }
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
    }

}
