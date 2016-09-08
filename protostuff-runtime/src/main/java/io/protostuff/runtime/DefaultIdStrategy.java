package io.protostuff.runtime;

import static io.protostuff.runtime.RuntimeFieldFactory.ID_BOOL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_CHAR;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import io.protostuff.CollectionSchema;
import io.protostuff.Input;
import io.protostuff.MapSchema;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The FQCN(fully qualified class name) will serve as the id (string). Does not need any registration in the user-code
 * (works out-of-the-box). The size of serialized representation may be not very efficient.
 * 
 * @author Leo Romanoff
 * @author David Yu
 */
public final class DefaultIdStrategy extends IdStrategy
{

    final ConcurrentHashMap<String, HasSchema<?>> pojoMapping = new ConcurrentHashMap<String, HasSchema<?>>();

    final ConcurrentHashMap<String, EnumIO<?>> enumMapping = new ConcurrentHashMap<String, EnumIO<?>>();

    final ConcurrentHashMap<String, CollectionSchema.MessageFactory> collectionMapping = new ConcurrentHashMap<String, CollectionSchema.MessageFactory>();

    final ConcurrentHashMap<String, MapSchema.MessageFactory> mapMapping = new ConcurrentHashMap<String, MapSchema.MessageFactory>();

    final ConcurrentHashMap<String, HasDelegate<?>> delegateMapping = new ConcurrentHashMap<String, HasDelegate<?>>();

    public DefaultIdStrategy()
    {
        super(DEFAULT_FLAGS, null, 0);
    }

    public DefaultIdStrategy(IdStrategy primaryGroup, int groupId)
    {
        super(DEFAULT_FLAGS, primaryGroup, groupId);
    }
    
    public DefaultIdStrategy(int flags, IdStrategy primaryGroup, int groupId)
    {
        super(flags, primaryGroup, groupId);
    }

    /**
     * Registers a pojo. Returns true if registration is successful or if the same exact schema was previously
     * registered. Used by {@link RuntimeSchema#register(Class, Schema)}.
     */
    public <T> boolean registerPojo(Class<T> typeClass, Schema<T> schema)
    {
        assert typeClass != null && schema != null;

        final HasSchema<?> last = pojoMapping.putIfAbsent(typeClass.getName(),
                new Registered<T>(schema, this));

        return last == null
                || (last instanceof Registered<?> && ((Registered<?>) last).schema == schema);
    }
    
    /**
     * Registers a pojo. Returns true if registration is successful or if the same exact schema was previously
     * registered.
     */
    public <T> boolean registerPojo(Class<T> typeClass)
    {
        assert typeClass != null;

        final HasSchema<?> last = pojoMapping.putIfAbsent(typeClass.getName(),
                new LazyRegister<T>(typeClass, this));
        
        return last == null || (last instanceof LazyRegister);
    }

    /**
     * Registers an enum. Returns true if registration is successful.
     */
    public <T extends Enum<T>> boolean registerEnum(Class<T> enumClass)
    {
        return null == enumMapping.putIfAbsent(enumClass.getName(),
                EnumIO.newEnumIO(enumClass, this));
    }

    /**
     * Registers a delegate. Returns true if registration is successful.
     */
    public <T> boolean registerDelegate(Delegate<T> delegate)
    {
        return null == delegateMapping.putIfAbsent(delegate.typeClass()
                .getName(), new HasDelegate<T>(delegate, this));
    }

    /**
     * Registers a collection. Returns true if registration is successful.
     */
    public boolean registerCollection(CollectionSchema.MessageFactory factory)
    {
        return null == collectionMapping.putIfAbsent(factory.typeClass()
                .getName(), factory);
    }

    /**
     * Registers a map. Returns true if registration is successful.
     */
    public boolean registerMap(MapSchema.MessageFactory factory)
    {
        return null == mapMapping.putIfAbsent(factory.typeClass().getName(),
                factory);
    }

    /**
     * Used by {@link RuntimeSchema#map(Class, Class)}.
     */
    public <T> boolean map(Class<? super T> baseClass, Class<T> typeClass)
    {
        assert baseClass != null && typeClass != null;

        if (typeClass.isInterface()
                || Modifier.isAbstract(typeClass.getModifiers()))
        {
            throw new IllegalArgumentException(typeClass
                    + " cannot be an interface/abstract class.");
        }

        final HasSchema<?> last = pojoMapping.putIfAbsent(baseClass.getName(),
                new Mapped<T>(baseClass, typeClass, this));

        return last == null
                || (last instanceof Mapped<?> && ((Mapped<?>) last).typeClass == typeClass);
    }

    @Override
    public boolean isDelegateRegistered(Class<?> typeClass)
    {
        return delegateMapping.containsKey(typeClass.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> HasDelegate<T> getDelegateWrapper(Class<? super T> typeClass)
    {
        return (HasDelegate<T>) delegateMapping.get(typeClass.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Delegate<T> getDelegate(Class<? super T> typeClass)
    {
        final HasDelegate<T> last = (HasDelegate<T>) delegateMapping
                .get(typeClass.getName());
        return last == null ? null : last.delegate;
    }

    @Override
    public boolean isRegistered(Class<?> typeClass)
    {
        final HasSchema<?> last = pojoMapping.get(typeClass.getName());
        return last != null && !(last instanceof Lazy<?>);
    }

    @SuppressWarnings("unchecked")
    private <T> HasSchema<T> getSchemaWrapper(String className, boolean load)
    {
        HasSchema<T> hs = (HasSchema<T>) pojoMapping.get(className);
        if (hs == null)
        {
            if (!load)
                return null;

            final Class<T> typeClass = RuntimeEnv.loadClass(className);

            hs = new Lazy<T>(typeClass, this);
            final HasSchema<T> last = (HasSchema<T>) pojoMapping.putIfAbsent(
                    typeClass.getName(), hs);
            if (last != null)
                hs = last;
        }

        return hs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass, boolean create)
    {
        HasSchema<T> hs = (HasSchema<T>) pojoMapping.get(typeClass.getName());
        if (hs == null && create)
        {
            hs = new Lazy<T>(typeClass, this);
            final HasSchema<T> last = (HasSchema<T>) pojoMapping.putIfAbsent(
                    typeClass.getName(), hs);
            if (last != null)
                hs = last;
        }

        return hs;
    }

    private EnumIO<? extends Enum<?>> getEnumIO(String className, boolean load)
    {
        EnumIO<?> eio = enumMapping.get(className);
        if (eio == null)
        {
            if (!load)
                return null;

            final Class<?> enumClass = RuntimeEnv.loadClass(className);

            eio = EnumIO.newEnumIO(enumClass, this);

            final EnumIO<?> existing = enumMapping.putIfAbsent(
                    enumClass.getName(), eio);
            if (existing != null)
                eio = existing;
        }

        return eio;
    }

    @Override
    protected EnumIO<? extends Enum<?>> getEnumIO(Class<?> enumClass)
    {
        EnumIO<?> eio = enumMapping.get(enumClass.getName());
        if (eio == null)
        {
            eio = EnumIO.newEnumIO(enumClass, this);

            final EnumIO<?> existing = enumMapping.putIfAbsent(
                    enumClass.getName(), eio);
            if (existing != null)
                eio = existing;
        }

        return eio;
    }

    @Override
    protected CollectionSchema.MessageFactory getCollectionFactory(
            Class<?> clazz)
    {
        final String className = clazz.getName();
        CollectionSchema.MessageFactory factory = collectionMapping
                .get(className);
        if (factory == null)
        {
            if (className.startsWith("java.util"))
            {
                factory = CollectionSchema.MessageFactories.valueOf(clazz
                        .getSimpleName());
            }
            else
            {
                factory = new RuntimeCollectionFactory(clazz);
                CollectionSchema.MessageFactory f = collectionMapping
                        .putIfAbsent(className, factory);
                if (f != null)
                    factory = f;
            }
        }

        return factory;
    }

    @Override
    protected MapSchema.MessageFactory getMapFactory(Class<?> clazz)
    {
        final String className = clazz.getName();
        MapSchema.MessageFactory factory = mapMapping.get(className);
        if (factory == null)
        {
            if (className.startsWith("java.util"))
            {
                factory = MapSchema.MessageFactories.valueOf(clazz
                        .getSimpleName());
            }
            else
            {
                factory = new RuntimeMapFactory(clazz);
                MapSchema.MessageFactory f = mapMapping.putIfAbsent(className,
                        factory);
                if (f != null)
                    factory = f;
            }
        }

        return factory;
    }

    @Override
    protected void writeCollectionIdTo(Output output, int fieldNumber,
            Class<?> clazz) throws IOException
    {
        final CollectionSchema.MessageFactory factory = collectionMapping
                .get(clazz);
        if (factory == null && clazz.getName().startsWith("java.util"))
        {
            // jdk collection
            // better not to register the jdk collection if using this strategy
            // as it saves space by not writing the full package
            output.writeString(fieldNumber, clazz.getSimpleName(), false);
        }
        else
        {
            output.writeString(fieldNumber, clazz.getName(), false);
        }
    }

    @Override
    protected void transferCollectionId(Input input, Output output,
            int fieldNumber) throws IOException
    {
        input.transferByteRangeTo(output, true, fieldNumber, false);
    }

    @Override
    protected CollectionSchema.MessageFactory resolveCollectionFrom(Input input)
            throws IOException
    {
        final String className = input.readString();
        CollectionSchema.MessageFactory factory = collectionMapping
                .get(className);
        if (factory == null)
        {
            if (className.indexOf('.') == -1)
            {
                factory = CollectionSchema.MessageFactories.valueOf(className);
            }
            else
            {
                factory = new RuntimeCollectionFactory(
                        RuntimeEnv.loadClass(className));
                CollectionSchema.MessageFactory f = collectionMapping
                        .putIfAbsent(className, factory);
                if (f != null)
                    factory = f;
            }
        }

        return factory;
    }

    @Override
    protected void writeMapIdTo(Output output, int fieldNumber, Class<?> clazz)
            throws IOException
    {
        final MapSchema.MessageFactory factory = mapMapping.get(clazz);
        if (factory == null && clazz.getName().startsWith("java.util"))
        {
            // jdk map
            // better not to register the jdk map if using this strategy
            // as it saves space by not writing the full package
            output.writeString(fieldNumber, clazz.getSimpleName(), false);
        }
        else
        {
            output.writeString(fieldNumber, clazz.getName(), false);
        }
    }

    @Override
    protected void transferMapId(Input input, Output output, int fieldNumber)
            throws IOException
    {
        input.transferByteRangeTo(output, true, fieldNumber, false);
    }

    @Override
    protected MapSchema.MessageFactory resolveMapFrom(Input input)
            throws IOException
    {
        final String className = input.readString();
        MapSchema.MessageFactory factory = mapMapping.get(className);
        if (factory == null)
        {
            if (className.indexOf('.') == -1)
            {
                factory = MapSchema.MessageFactories.valueOf(className);
            }
            else
            {
                factory = new RuntimeMapFactory(RuntimeEnv.loadClass(className));
                MapSchema.MessageFactory f = mapMapping.putIfAbsent(className,
                        factory);
                if (f != null)
                    factory = f;
            }
        }

        return factory;
    }

    @Override
    protected void writeEnumIdTo(Output output, int fieldNumber, Class<?> clazz)
            throws IOException
    {
        output.writeString(fieldNumber, clazz.getName(), false);
    }

    @Override
    protected void transferEnumId(Input input, Output output, int fieldNumber)
            throws IOException
    {
        input.transferByteRangeTo(output, true, fieldNumber, false);
    }

    @Override
    protected EnumIO<?> resolveEnumFrom(Input input) throws IOException
    {
        return getEnumIO(input.readString(), true);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasDelegate<T> tryWriteDelegateIdTo(Output output,
            int fieldNumber, Class<T> clazz) throws IOException
    {
        final HasDelegate<T> hd = (HasDelegate<T>) delegateMapping.get(clazz
                .getName());
        if (hd == null)
            return null;

        output.writeString(fieldNumber, clazz.getName(), false);

        return hd;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasDelegate<T> transferDelegateId(Input input, Output output,
            int fieldNumber) throws IOException
    {
        final String className = input.readString();

        final HasDelegate<T> hd = (HasDelegate<T>) delegateMapping
                .get(className);
        if (hd == null)
            throw new UnknownTypeException("delegate: " + className
                    + " (Outdated registry)");

        output.writeString(fieldNumber, className, false);

        return hd;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasDelegate<T> resolveDelegateFrom(Input input)
            throws IOException
    {
        final String className = input.readString();

        final HasDelegate<T> hd = (HasDelegate<T>) delegateMapping
                .get(className);
        if (hd == null)
            throw new UnknownTypeException("delegate: " + className
                    + " (Outdated registry)");

        return hd;
    }
    
    @Override
    protected <T> HasSchema<T> tryWritePojoIdTo(Output output, int fieldNumber,
            Class<T> clazz, boolean registered) throws IOException
    {
        HasSchema<T> hs = getSchemaWrapper(clazz, false);
        if (hs == null || (registered && hs instanceof Lazy<?>))
            return null;
        
        output.writeString(fieldNumber, clazz.getName(), false);
        
        return hs;
    }

    @Override
    protected <T> HasSchema<T> writePojoIdTo(Output output, int fieldNumber,
            Class<T> clazz) throws IOException
    {
        output.writeString(fieldNumber, clazz.getName(), false);

        // it is important to return the schema initialized (if it hasn't been).
        return getSchemaWrapper(clazz, true);
    }

    @Override
    protected <T> HasSchema<T> transferPojoId(Input input, Output output,
            int fieldNumber) throws IOException
    {
        final String className = input.readString();

        final HasSchema<T> wrapper = getSchemaWrapper(className,
                0 != (AUTO_LOAD_POLYMORPHIC_CLASSES & flags));
        if (wrapper == null)
        {
            throw new ProtostuffException("polymorphic pojo not registered: "
                    + className);
        }

        output.writeString(fieldNumber, className, false);

        return wrapper;
    }

    @Override
    protected <T> HasSchema<T> resolvePojoFrom(Input input, int fieldNumber)
            throws IOException
    {
        final String className = input.readString();

        final HasSchema<T> wrapper = getSchemaWrapper(className,
                0 != (AUTO_LOAD_POLYMORPHIC_CLASSES & flags));
        if (wrapper == null)
            throw new ProtostuffException("polymorphic pojo not registered: "
                    + className);

        return wrapper;
    }

    @Override
    protected <T> Schema<T> writeMessageIdTo(Output output, int fieldNumber,
            Message<T> message) throws IOException
    {
        output.writeString(fieldNumber, message.getClass().getName(), false);

        return message.cachedSchema();
    }

    @Override
    protected void writeArrayIdTo(Output output, Class<?> componentType)
            throws IOException
    {
        output.writeString(RuntimeFieldFactory.ID_ARRAY,
                componentType.getName(), false);
    }

    @Override
    protected void transferArrayId(Input input, Output output, int fieldNumber,
            boolean mapped) throws IOException
    {
        input.transferByteRangeTo(output, true, fieldNumber, false);
    }

    @Override
    protected Class<?> resolveArrayComponentTypeFrom(Input input, boolean mapped)
            throws IOException
    {
        return resolveClass(input.readString());
    }

    static Class<?> resolveClass(String className)
    {
        final RuntimeFieldFactory<Object> inline = RuntimeFieldFactory
                .getInline(className);

        if (inline == null)
            return RuntimeEnv.loadClass(className);

        if (className.indexOf('.') != -1)
            return inline.typeClass();

        switch (inline.id)
        {
            case ID_BOOL:
                return boolean.class;
            case ID_BYTE:
                return byte.class;
            case ID_CHAR:
                return char.class;
            case ID_SHORT:
                return short.class;
            case ID_INT32:
                return int.class;
            case ID_INT64:
                return long.class;
            case ID_FLOAT:
                return float.class;
            case ID_DOUBLE:
                return double.class;
            default:
                throw new RuntimeException("Should never happen.");
        }
    }

    @Override
    protected void writeClassIdTo(Output output, Class<?> componentType,
            boolean array) throws IOException
    {
        final int id = array ? RuntimeFieldFactory.ID_CLASS_ARRAY
                : RuntimeFieldFactory.ID_CLASS;

        output.writeString(id, componentType.getName(), false);
    }

    @Override
    protected void transferClassId(Input input, Output output, int fieldNumber,
            boolean mapped, boolean array) throws IOException
    {
        input.transferByteRangeTo(output, true, fieldNumber, false);
    }

    @Override
    protected Class<?> resolveClassFrom(Input input, boolean mapped,
            boolean array) throws IOException
    {
        return resolveClass(input.readString());
    }

    static final class RuntimeCollectionFactory implements
            CollectionSchema.MessageFactory
    {

        final Class<?> collectionClass;
        final RuntimeEnv.Instantiator<?> instantiator;

        public RuntimeCollectionFactory(Class<?> collectionClass)
        {
            this.collectionClass = collectionClass;
            instantiator = RuntimeEnv.newInstantiator(collectionClass);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <V> Collection<V> newMessage()
        {
            return (Collection<V>) instantiator.newInstance();
        }

        @Override
        public Class<?> typeClass()
        {
            return collectionClass;
        }
    }

    static final class RuntimeMapFactory implements MapSchema.MessageFactory
    {

        final Class<?> mapClass;
        final RuntimeEnv.Instantiator<?> instantiator;

        public RuntimeMapFactory(Class<?> mapClass)
        {
            this.mapClass = mapClass;
            instantiator = RuntimeEnv.newInstantiator(mapClass);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <K, V> Map<K, V> newMessage()
        {
            return (Map<K, V>) instantiator.newInstance();
        }

        @Override
        public Class<?> typeClass()
        {
            return mapClass;
        }

    }
    
    static final class Lazy<T> extends HasSchema<T>
    {
        final Class<T> typeClass;
        private volatile Schema<T> schema;
        private volatile Pipe.Schema<T> pipeSchema;

        Lazy(Class<T> typeClass, IdStrategy strategy)
        {
            super(strategy);
            this.typeClass = typeClass;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Schema<T> getSchema()
        {
            Schema<T> schema = this.schema;
            if (schema == null)
            {
                synchronized (this)
                {
                    if ((schema = this.schema) == null)
                    {
                        if (Message.class.isAssignableFrom(typeClass))
                        {
                            // use the message's schema.
                            Message<T> m = (Message<T>) createMessageInstance(typeClass);
                            this.schema = schema = m.cachedSchema();
                        }
                        else
                        {
                            // create new
                            this.schema = schema = strategy
                                    .newSchema(typeClass);
                        }
                    }
                }
            }

            return schema;
        }

        @Override
        public Pipe.Schema<T> getPipeSchema()
        {
            Pipe.Schema<T> pipeSchema = this.pipeSchema;
            if (pipeSchema == null)
            {
                synchronized (this)
                {
                    if ((pipeSchema = this.pipeSchema) == null)
                    {
                        this.pipeSchema = pipeSchema = RuntimeSchema
                                .resolvePipeSchema(getSchema(), typeClass, true);
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

        Mapped(Class<? super T> baseClass, Class<T> typeClass,
                IdStrategy strategy)
        {
            super(strategy);
            this.baseClass = baseClass;
            this.typeClass = typeClass;
        }

        @Override
        public Schema<T> getSchema()
        {
            HasSchema<T> wrapper = this.wrapper;
            if (wrapper == null)
            {
                synchronized (this)
                {
                    if ((wrapper = this.wrapper) == null)
                    {
                        this.wrapper = wrapper = strategy.getSchemaWrapper(
                                typeClass, true);
                    }
                }
            }

            return wrapper.getSchema();
        }

        @Override
        public Pipe.Schema<T> getPipeSchema()
        {
            HasSchema<T> wrapper = this.wrapper;
            if (wrapper == null)
            {
                synchronized (this)
                {
                    if ((wrapper = this.wrapper) == null)
                    {
                        this.wrapper = wrapper = strategy.getSchemaWrapper(
                                typeClass, true);
                    }
                }
            }

            return wrapper.getPipeSchema();
        }

    }
    
    static final class LazyRegister<T> extends HasSchema<T>
    {
        final Class<T> typeClass;
        private volatile Schema<T> schema;
        private volatile Pipe.Schema<T> pipeSchema;

        LazyRegister(Class<T> typeClass, IdStrategy strategy)
        {
            super(strategy);
            this.typeClass = typeClass;
        }

        @Override
        public Schema<T> getSchema()
        {
            Schema<T> schema = this.schema;
            if (schema == null)
            {
                synchronized (this)
                {
                    if ((schema = this.schema) == null)
                    {
                        // create new
                        this.schema = schema = strategy.newSchema(typeClass);
                    }
                }
            }
            
            return schema;
        }
        
        @Override
        public Pipe.Schema<T> getPipeSchema()
        {
            Pipe.Schema<T> pipeSchema = this.pipeSchema;
            if (pipeSchema == null)
            {
                synchronized (this)
                {
                    if ((pipeSchema = this.pipeSchema) == null)
                    {
                        this.pipeSchema = pipeSchema = RuntimeSchema
                                .resolvePipeSchema(getSchema(), typeClass, true);
                    }
                }
            }
            
            return pipeSchema;
        }
    }

    static final class Registered<T> extends HasSchema<T>
    {
        final Schema<T> schema;
        private volatile Pipe.Schema<T> pipeSchema;

        Registered(Schema<T> schema, IdStrategy strategy)
        {
            super(strategy);
            this.schema = schema;
        }

        @Override
        public Schema<T> getSchema()
        {
            return schema;
        }

        @Override
        public Pipe.Schema<T> getPipeSchema()
        {
            Pipe.Schema<T> pipeSchema = this.pipeSchema;
            if (pipeSchema == null)
            {
                synchronized (this)
                {
                    if ((pipeSchema = this.pipeSchema) == null)
                    {
                        this.pipeSchema = pipeSchema = RuntimeSchema
                                .resolvePipeSchema(schema, schema.typeClass(),
                                        true);
                    }
                }
            }
            return pipeSchema;
        }
    }
}
