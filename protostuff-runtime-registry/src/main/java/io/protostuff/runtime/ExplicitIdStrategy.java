//================================================================================
//Copyright (c) 2012, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================

package io.protostuff.runtime;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Map;

import io.protostuff.CollectionSchema;
import io.protostuff.Input;
import io.protostuff.MapSchema;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import com.google.common.collect.Multimap;

/**
 * Requires every messsage/pojo/enum/collection/map to be registered with unique ids.
 * 
 * @author David Yu
 * @created Mar 25, 2012
 */
public final class ExplicitIdStrategy extends NumericIdStrategy
{

    /**
     * This Registry is only way to register your pojos/enums/collections/maps/delegates.
     */
    public static class Registry implements NumericIdStrategy.Registry
    {

        public final ExplicitIdStrategy strategy;

        public Registry()
        {
            this(DEFAULT_FLAGS, null, 0, 10, 10, 10, 10, 10, 10);
        }

        public Registry(
                int initialCollectionSize,
                int initialMapSize,
                int initialMultiMapSize,
                int initialEnumSize,
                int initialPojoSize,
                int initialDelegateSize)
        {
            this(DEFAULT_FLAGS, null, 0,
                    initialCollectionSize,
                    initialMapSize,
                    initialMultiMapSize,
                    initialEnumSize,
                    initialPojoSize,
                    initialDelegateSize);
        }

        public Registry(
                int flags, 
                IdStrategy primaryGroup, int groupId,
                int initialCollectionSize,
                int initialMapSize,
                int initialMultiMapSize,
                int initialEnumSize,
                int initialPojoSize,
                int initialDelegateSize)
        {
            IdentityHashMap<Class<?>, RegisteredCollectionFactory> collectionMapping =
                    newMap(initialCollectionSize);

            ArrayList<RegisteredCollectionFactory> collections =
                    newList(initialCollectionSize + 1);

            IdentityHashMap<Class<?>, RegisteredMapFactory> mapMapping =
                    new IdentityHashMap<Class<?>, RegisteredMapFactory>(
                            initialMapSize);

            IdentityHashMap<Class<?>, RegisteredMultiMapFactory> multimapMapping =
                    new IdentityHashMap<Class<?>, RegisteredMultiMapFactory>(initialMultiMapSize);

            ArrayList<RegisteredMultiMapFactory> multiMaps =
                    newList(initialMultiMapSize);

            ArrayList<RegisteredMapFactory> maps = newList(initialMapSize);

            IdentityHashMap<Class<?>, RegisteredEnumIO> enumMapping =
                    newMap(initialEnumSize);

            ArrayList<RegisteredEnumIO> enums =
                    newList(initialEnumSize + 1);

            IdentityHashMap<Class<?>, BaseHS<?>> pojoMapping =
                    newMap(initialPojoSize);

            ArrayList<BaseHS<?>> pojos = newList(initialPojoSize + 1);

            IdentityHashMap<Class<?>, RegisteredDelegate<?>> delegateMapping =
                    newMap(initialDelegateSize);

            ArrayList<RegisteredDelegate<?>> delegates = newList(initialDelegateSize + 1);

            strategy = new ExplicitIdStrategy(
                    flags, 
                    primaryGroup, groupId,
                    collectionMapping, collections,
                    mapMapping, maps,
                    multimapMapping, multiMaps,
                    enumMapping, enums,
                    pojoMapping, pojos,
                    delegateMapping, delegates);
        }

        /**
         * Collection ids start at 1.
         */
        @Override
        public <T extends Collection<?>> Registry registerCollection(
                CollectionSchema.MessageFactory factory, int id)
        {
            if (id < 1)
                throw new IllegalArgumentException("collection ids start at 1.");

            if (id >= strategy.collections.size())
                grow(strategy.collections, id + 1);
            else if (strategy.collections.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id +
                        " (" + factory.typeClass() + ")");
            }

            RegisteredCollectionFactory rf = new RegisteredCollectionFactory(id, factory);
            strategy.collections.set(id, rf);
            // just in case
            if (strategy.collectionMapping.put(factory.typeClass(), rf) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + factory.typeClass());

            return this;
        }

        /**
         * Map ids start at 1.
         */
        @Override
        public <T extends Map<?, ?>> Registry registerMap(
                MapSchema.MessageFactory factory, int id)
        {
            if (id < 1)
                throw new IllegalArgumentException("map ids start at 1.");

            if (id >= strategy.maps.size())
                grow(strategy.maps, id + 1);
            else if (strategy.maps.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id +
                        " (" + factory.typeClass() + ")");
            }

            RegisteredMapFactory rf = new RegisteredMapFactory(id, factory);
            strategy.maps.set(id, rf);
            // just in case
            if (strategy.mapMapping.put(factory.typeClass(), rf) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + factory.typeClass());

            return this;
        }

        /**
         * Enum ids start at 1.
         */
        @Override
        public <T extends Enum<T>> Registry registerEnum(Class<T> clazz, int id)
        {
            if (id < 1)
                throw new IllegalArgumentException("enum ids start at 1.");

            if (id >= strategy.enums.size())
                grow(strategy.enums, id + 1);
            else if (strategy.enums.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id +
                        " (" + clazz.getName() + ")");
            }

            EnumIO<?> eio = EnumIO.newEnumIO(clazz, strategy);
            RegisteredEnumIO reio = new RegisteredEnumIO(id, eio);
            strategy.enums.set(id, reio);

            // just in case
            if (strategy.enumMapping.put(clazz, reio) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + clazz);

            return this;
        }

        /**
         * Enum ids start at 1.
         */
        @Override
        public Registry registerEnum(EnumIO<?> eio, int id)
        {
            if (id < 1)
                throw new IllegalArgumentException("enum ids start at 1.");

            if (id >= strategy.enums.size())
                grow(strategy.enums, id + 1);
            else if (strategy.enums.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id +
                        " (" + eio.enumClass.getName() + ")");
            }

            RegisteredEnumIO reio = new RegisteredEnumIO(id, eio);
            strategy.enums.set(id, reio);

            // just in case
            if (strategy.enumMapping.put(eio.enumClass, reio) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + eio.enumClass);

            return this;
        }

        /**
         * Pojo ids start at 1.
         */
        @Override
        public <T> Registry registerPojo(Class<T> clazz, int id)
        {
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
                throw new IllegalArgumentException("Not a concrete class: " + clazz);

            if (id < 1)
                throw new IllegalArgumentException("pojo ids start at 1.");

            if (id >= strategy.pojos.size())
                grow(strategy.pojos, id + 1);
            else if (strategy.pojos.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id +
                        " (" + clazz.getName() + ")");
            }

            if (strategy.pojoMapping.containsKey(clazz))
                throw new IllegalArgumentException("Duplicate registration for: " + clazz);

            BaseHS<T> wrapper = new Lazy<T>(id, clazz, strategy);
            strategy.pojos.set(id, wrapper);

            strategy.pojoMapping.put(clazz, wrapper);

            return this;
        }

        /**
         * Pojo ids start at 1.
         */
        @Override
        public <T> Registry registerPojo(Schema<T> schema, Pipe.Schema<T> pipeSchema,
                int id)
        {
            if (id >= strategy.pojos.size())
                grow(strategy.pojos, id + 1);
            else if (strategy.pojos.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id +
                        " (" + schema.typeClass().getName() + ")");
            }

            if (strategy.pojoMapping.containsKey(schema.typeClass()))
                throw new IllegalArgumentException("Duplicate registration for: " + schema.typeClass());

            Registered<T> wrapper = new Registered<T>(id, schema, pipeSchema, strategy);
            strategy.pojos.set(id, wrapper);

            strategy.pojoMapping.put(schema.typeClass(), wrapper);

            return this;
        }

        /**
         * If you are sure that you are only using a single implementation of your interface/abstract class, then it
         * makes sense to map it directly to its impl class to avoid writing the type.
         * <p>
         * Note that the type is always written when your field is {@link java.lang.Object}.
         * <p>
         * Pojo ids start at 1.
         */
        @Override
        public <T> Registry mapPojo(Class<? super T> baseClass, Class<T> implClass)
        {
            if (strategy.pojoMapping.containsKey(baseClass))
                throw new IllegalArgumentException("Duplicate registration for: " + baseClass);

            BaseHS<?> wrapper = strategy.pojoMapping.get(implClass);
            if (wrapper == null)
                throw new IllegalArgumentException("Must register the impl class first. " + implClass);

            strategy.pojoMapping.put(baseClass, wrapper);

            return this;
        }

        /**
         * Register a {@link Delegate} and assign an id.
         * <p>
         * Delegate ids start at 1.
         */
        @Override
        public <T> Registry registerDelegate(Delegate<T> delegate, int id)
        {
            if (id < 1)
                throw new IllegalArgumentException("delegate ids start at 1.");

            if (id >= strategy.delegates.size())
                grow(strategy.delegates, id + 1);
            else if (strategy.delegates.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id +
                        " (" + delegate.typeClass() + ")");
            }

            RegisteredDelegate<T> rd = new RegisteredDelegate<T>(id, delegate, strategy);
            strategy.delegates.set(id, rd);
            // just in case
            if (strategy.delegateMapping.put(delegate.typeClass(), rd) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + delegate.typeClass());

            return this;
        }
    }

    final IdentityHashMap<Class<?>, RegisteredCollectionFactory> collectionMapping;

    final ArrayList<RegisteredCollectionFactory> collections;

    final IdentityHashMap<Class<?>, RegisteredMapFactory> mapMapping;

    final ArrayList<RegisteredMapFactory> maps;

    final IdentityHashMap<Class<?>, RegisteredEnumIO> enumMapping;

    final ArrayList<RegisteredEnumIO> enums;

    final IdentityHashMap<Class<?>, BaseHS<?>> pojoMapping;

    final ArrayList<BaseHS<?>> pojos;

    final IdentityHashMap<Class<?>, RegisteredMultiMapFactory> multimapMapping;

    final ArrayList<RegisteredMultiMapFactory> multiMaps;

    final IdentityHashMap<Class<?>, RegisteredDelegate<?>> delegateMapping;

    final ArrayList<RegisteredDelegate<?>> delegates;

    public ExplicitIdStrategy(
            IdentityHashMap<Class<?>, RegisteredCollectionFactory> collectionMapping,
            ArrayList<RegisteredCollectionFactory> collections,
            IdentityHashMap<Class<?>, RegisteredMapFactory> mapMapping,
            ArrayList<RegisteredMapFactory> maps,
            IdentityHashMap<Class<?>, RegisteredEnumIO> enumMapping,
            IdentityHashMap<Class<?>, RegisteredMultiMapFactory> multimapMapping,
            ArrayList<RegisteredMultiMapFactory> multiMaps,
            ArrayList<RegisteredEnumIO> enums,
            IdentityHashMap<Class<?>, BaseHS<?>> pojoMapping,
            ArrayList<BaseHS<?>> pojos,
            IdentityHashMap<Class<?>, RegisteredDelegate<?>> delegateMapping,
            ArrayList<RegisteredDelegate<?>> delegates)
    {
        this(DEFAULT_FLAGS, null, 0,
                collectionMapping,
                collections,
                mapMapping,
                maps,
                multimapMapping,
                multiMaps,
                enumMapping,
                enums,
                pojoMapping,
                pojos,
                delegateMapping,
                delegates);
    }

    public ExplicitIdStrategy(final int flags,
            IdStrategy primaryGroup, int groupId,
            IdentityHashMap<Class<?>, RegisteredCollectionFactory> collectionMapping,
            ArrayList<RegisteredCollectionFactory> collections,
            IdentityHashMap<Class<?>, RegisteredMapFactory> mapMapping,
            ArrayList<RegisteredMapFactory> maps,
            IdentityHashMap<Class<?>, RegisteredMultiMapFactory> multimapMapping,
            ArrayList<RegisteredMultiMapFactory> multiMaps,
            IdentityHashMap<Class<?>, RegisteredEnumIO> enumMapping,
            ArrayList<RegisteredEnumIO> enums,
            IdentityHashMap<Class<?>, BaseHS<?>> pojoMapping,
            ArrayList<BaseHS<?>> pojos,
            IdentityHashMap<Class<?>, RegisteredDelegate<?>> delegateMapping,
            ArrayList<RegisteredDelegate<?>> delegates)
    {
        super(flags, primaryGroup, groupId);

        this.collectionMapping = collectionMapping;
        this.collections = collections;
        this.mapMapping = mapMapping;
        this.maps = maps;
        this.multimapMapping = multimapMapping;
        this.multiMaps = multiMaps;
        this.enumMapping = enumMapping;
        this.enums = enums;
        this.pojoMapping = pojoMapping;
        this.pojos = pojos;
        this.delegateMapping = delegateMapping;
        this.delegates = delegates;
    }

    @Override
    public boolean isRegistered(Class<?> typeClass)
    {
        return pojoMapping.containsKey(typeClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass, boolean create)
    {
        final BaseHS<T> wrapper = (BaseHS<T>) pojoMapping.get(typeClass);
        if (wrapper == null && create)
            throw new UnknownTypeException("pojo: " + typeClass);

        return wrapper;
    }

    @Override
    protected EnumIO<? extends Enum<?>> getEnumIO(Class<?> enumClass)
    {
        final RegisteredEnumIO reio = enumMapping.get(enumClass);
        if (reio == null)
            throw new UnknownTypeException("enum: " + enumClass);

        return reio.eio;
    }

    @Override
    protected CollectionSchema.MessageFactory getCollectionFactory(Class<?> clazz)
    {
        final RegisteredCollectionFactory rf = collectionMapping.get(clazz);
        if (rf == null)
        {
            if (clazz.getName().startsWith("java.util"))
                return CollectionSchema.MessageFactories.valueOf(clazz.getSimpleName());

            throw new UnknownTypeException("collection: " + clazz);
        }

        return rf;
    }

    @Override
    protected MapSchema.MessageFactory getMapFactory(Class<?> clazz)
    {
        final RegisteredMapFactory rf = mapMapping.get(clazz);
        if (rf == null)
        {
            if (clazz.getName().startsWith("java.util"))
                return MapSchema.MessageFactories.valueOf(clazz.getSimpleName());

            throw new UnknownTypeException("map: " + clazz);
        }

        return rf;
    }

    @Override
    protected MultiMapSchema.MessageFactory getMultiMapFactory(Class<?> clazz)
    {
        final RegisteredMultiMapFactory rf = multimapMapping.get(clazz);
        if (rf == null)
        {
            if (clazz.getName().startsWith("com.google.common.collect"))
                return MultiMapSchema.MessageFactories.valueOf(clazz.getSimpleName());

            throw new UnknownTypeException("multimap: " + clazz);
        }

        return rf;
    }

    @Override
    protected void writeCollectionIdTo(Output output, int fieldNumber, Class<?> clazz)
            throws IOException
    {
        final RegisteredCollectionFactory factory = collectionMapping.get(clazz);
        if (factory == null)
            throw new UnknownTypeException("collection: " + clazz);

        output.writeUInt32(fieldNumber, factory.id, false);
    }

    @Override
    protected void transferCollectionId(Input input, Output output,
            int fieldNumber) throws IOException
    {
        output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }

    @Override
    protected CollectionSchema.MessageFactory resolveCollectionFrom(Input input)
            throws IOException
    {
        final int id = input.readUInt32();

        final CollectionSchema.MessageFactory factory = id < collections.size() ?
                collections.get(id) : null;
        if (factory == null)
            throw new UnknownTypeException("collection id: " + id + " (Outdated registry)");

        return factory;
    }

    @Override
    protected void writeMapIdTo(Output output, int fieldNumber, Class<?> clazz)
            throws IOException
    {
        final RegisteredMapFactory factory = mapMapping.get(clazz);
        if (factory == null)
            throw new UnknownTypeException("map: " + clazz);

        output.writeUInt32(fieldNumber, factory.id, false);
    }

    @Override
    protected void transferMapId(Input input, Output output,
            int fieldNumber) throws IOException
    {
        output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }

    @Override
    protected MapSchema.MessageFactory resolveMapFrom(Input input)
            throws IOException
    {
        final int id = input.readUInt32();

        final MapSchema.MessageFactory factory = id < maps.size() ? maps.get(id) : null;
        if (factory == null)
            throw new UnknownTypeException("map id: " + id + " (Outdated registry)");

        return factory;
    }

    @Override
    protected void writeEnumIdTo(Output output, int fieldNumber,
            Class<?> clazz) throws IOException
    {
        final RegisteredEnumIO reio = enumMapping.get(clazz);
        if (reio == null)
            throw new UnknownTypeException("enum: " + clazz);

        output.writeUInt32(fieldNumber, reio.id, false);
    }

    @Override
    protected void transferEnumId(Input input, Output output,
            int fieldNumber) throws IOException
    {
        output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }

    @Override
    protected EnumIO<?> resolveEnumFrom(Input input) throws IOException
    {
        final int id = input.readUInt32();

        final RegisteredEnumIO reio = id < enums.size() ? enums.get(id) : null;
        if (reio == null)
            throw new UnknownTypeException("enum id: " + id + " (Outdated registry)");

        return reio.eio;
    }

    @Override
    public boolean isDelegateRegistered(Class<?> typeClass)
    {
        return delegateMapping.containsKey(typeClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Delegate<T> getDelegate(Class<? super T> typeClass)
    {
        final RegisteredDelegate<T> rd = (RegisteredDelegate<T>) delegateMapping.get(
                typeClass);

        return rd == null ? null : rd.delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> HasDelegate<T> getDelegateWrapper(Class<? super T> typeClass)
    {
        return (HasDelegate<T>) delegateMapping.get(typeClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasDelegate<T> tryWriteDelegateIdTo(Output output, int fieldNumber,
            Class<T> clazz) throws IOException
    {
        final RegisteredDelegate<T> rd = (RegisteredDelegate<T>) delegateMapping.get(
                clazz);

        if (rd == null)
            return null;

        output.writeUInt32(fieldNumber, rd.id, false);

        return rd;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasDelegate<T> transferDelegateId(Input input, Output output, int fieldNumber)
            throws IOException
    {
        final int id = input.readUInt32();

        final RegisteredDelegate<T> rd = id < delegates.size() ?
                (RegisteredDelegate<T>) delegates.get(id) : null;
        if (rd == null)
            throw new UnknownTypeException("delegate id: " + id + " (Outdated registry)");

        output.writeUInt32(fieldNumber, id, false);

        return rd;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasDelegate<T> resolveDelegateFrom(Input input) throws IOException
    {
        final int id = input.readUInt32();

        final RegisteredDelegate<T> rd = id < delegates.size() ?
                (RegisteredDelegate<T>) delegates.get(id) : null;
        if (rd == null)
            throw new UnknownTypeException("delegate id: " + id + " (Outdated registry)");

        return rd;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasSchema<T> tryWritePojoIdTo(Output output, int fieldNumber, 
            Class<T> clazz, boolean registered) throws IOException
    {
        final BaseHS<T> wrapper = (BaseHS<T>) pojoMapping.get(clazz);
        if (wrapper != null)
            output.writeUInt32(fieldNumber, wrapper.id, false);
        
        return wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasSchema<T> writePojoIdTo(Output output, int fieldNumber, Class<T> clazz)
            throws IOException
    {
        final BaseHS<T> wrapper = (BaseHS<T>) pojoMapping.get(clazz);
        if (wrapper == null)
            throw new UnknownTypeException("pojo: " + clazz);

        output.writeUInt32(fieldNumber, wrapper.id, false);

        return wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasSchema<T> transferPojoId(Input input, Output output,
            int fieldNumber) throws IOException
    {
        final int id = input.readUInt32();

        final BaseHS<T> wrapper = (BaseHS<T>) pojos.get(id);
        if (wrapper == null)
            throw new UnknownTypeException("pojo id: " + id + " (Outdated registry)");

        output.writeUInt32(fieldNumber, id, false);

        return wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> HasSchema<T> resolvePojoFrom(Input input, int fieldNumber)
            throws IOException
    {
        final int id = input.readUInt32();

        final BaseHS<T> wrapper = id < pojos.size() ? (BaseHS<T>) pojos.get(id) : null;
        if (wrapper == null)
            throw new UnknownTypeException("pojo id: " + id + " (Outdated registry)");

        return wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> Schema<T> writeMessageIdTo(Output output, int fieldNumber,
            Message<T> message) throws IOException
    {
        final BaseHS<T> wrapper = (BaseHS<T>) pojoMapping.get(message.getClass());

        if (wrapper == null)
            throw new UnknownTypeException("pojo: " + message.getClass());

        output.writeUInt32(fieldNumber, wrapper.id, false);

        // TODO allow the wrapper to return an override schema?
        return message.cachedSchema();
    }

    @Override
    protected Class<?> collectionClass(int id)
    {
        final RegisteredCollectionFactory factory = id < collections.size() ?
                collections.get(id) : null;
        if (factory == null)
        {
            throw new UnknownTypeException("collection id: " + id +
                    " (Outdated registry)");
        }

        return factory.typeClass();
    }

    @Override
    protected Class<?> mapClass(int id)
    {
        final RegisteredMapFactory factory = id < maps.size() ?
                maps.get(id) : null;
        if (factory == null)
        {
            throw new UnknownTypeException("map id: " + id +
                    " (Outdated registry)");
        }

        return factory.typeClass();
    }

    @Override
    protected Class<?> enumClass(int id)
    {
        final RegisteredEnumIO reio = id < enums.size() ? enums.get(id) : null;
        if (reio == null)
        {
            throw new UnknownTypeException("enum id: " + id +
                    " (Outdated registry)");
        }

        return reio.eio.enumClass;
    }

    @Override
    protected Class<?> delegateClass(int id)
    {
        final RegisteredDelegate<?> rd = id < delegates.size() ? delegates.get(id) : null;
        return rd == null ? null : rd.delegate.typeClass();
    }

    @Override
    protected Class<?> pojoClass(int id)
    {
        final BaseHS<?> wrapper = id < pojos.size() ? pojos.get(id) : null;
        if (wrapper == null)
        {
            throw new UnknownTypeException("pojo id: " + id +
                    " (Outdated registry)");
        }

        return wrapper.getSchema().typeClass();
    }

    @Override
    protected RegisteredDelegate<?> getRegisteredDelegate(Class<?> clazz)
    {
        return delegateMapping.get(clazz);
    }

    @Override
    protected int getEnumId(Class<?> clazz)
    {
        final RegisteredEnumIO reio = enumMapping.get(clazz);
        if (reio == null)
            throw new UnknownTypeException("enum: " + clazz);

        return (reio.id << 5) | CID_ENUM;
    }

    @Override
    protected int getId(Class<?> clazz)
    {
        if (Message.class.isAssignableFrom(clazz))
        {
            final BaseHS<?> wrapper = pojoMapping.get(clazz);
            if (wrapper == null)
                throw new UnknownTypeException("pojo: " + clazz);

            return (wrapper.id << 5) | CID_POJO;
        }

        if (Map.class.isAssignableFrom(clazz))
            return EnumMap.class.isAssignableFrom(clazz) ? CID_ENUM_MAP : mapId(clazz);

        if (Collection.class.isAssignableFrom(clazz))
            return EnumSet.class.isAssignableFrom(clazz) ? CID_ENUM_SET : collectionId(clazz);

        final BaseHS<?> wrapper = pojoMapping.get(clazz);
        if (wrapper == null)
            throw new UnknownTypeException("pojo: " + clazz);

        return (wrapper.id << 5) | CID_POJO;
    }

    private int collectionId(Class<?> clazz)
    {
        final RegisteredCollectionFactory factory = collectionMapping.get(clazz);
        if (factory == null)
            throw new UnknownTypeException("collection: " + clazz);

        return (factory.id << 5) | CID_COLLECTION;
    }

    private int mapId(Class<?> clazz)
    {
        final RegisteredMapFactory factory = mapMapping.get(clazz);
        if (factory == null)
            throw new UnknownTypeException("map: " + clazz);

        return (factory.id << 5) | CID_MAP;
    }

    static <K, V> IdentityHashMap<K, V> newMap(int size)
    {
        return new IdentityHashMap<K,V>(size);
    }

    static final class RegisteredCollectionFactory implements CollectionSchema.MessageFactory
    {

        final int id;
        final CollectionSchema.MessageFactory factory;

        public RegisteredCollectionFactory(int id, CollectionSchema.MessageFactory factory)
        {
            this.id = id;
            this.factory = factory;
        }

        @Override
        public <V> Collection<V> newMessage()
        {
            return factory.newMessage();
        }

        @Override
        public Class<?> typeClass()
        {
            return factory.typeClass();
        }

    }

    static final class RegisteredMapFactory implements MapSchema.MessageFactory
    {

        final int id;
        final MapSchema.MessageFactory factory;

        public RegisteredMapFactory(int id, MapSchema.MessageFactory factory)
        {
            this.id = id;
            this.factory = factory;
        }

        @Override
        public <K, V> Map<K, V> newMessage()
        {
            return factory.newMessage();
        }

        @Override
        public Class<?> typeClass()
        {
            return factory.typeClass();
        }

    }

    static final class RegisteredEnumIO
    {
        final int id;
        final EnumIO<?> eio;

        public RegisteredEnumIO(int id, EnumIO<?> eio)
        {
            this.id = id;
            this.eio = eio;
        }

    }

    static abstract class BaseHS<T> extends HasSchema<T>
    {
        final int id;

        BaseHS(int id, IdStrategy strategy)
        {
            super(strategy);
            this.id = id;
        }
    }

    static final class RegisteredMultiMapFactory
            implements MultiMapSchema.MessageFactory {

        final int id;
        final MultiMapSchema.MessageFactory factory;

        RegisteredMultiMapFactory(int id, MultiMapSchema.MessageFactory factory)
        {
            this.id = id;
            this.factory = factory;
        }

        @Override
        public <K, V> Multimap<K, V> newMessage() {
            return factory.newMessage();
        }

        @Override
        public Class<?> typeClass() {
            return factory.typeClass();
        }
    }

    static final class Registered<T> extends BaseHS<T>
    {

        final Schema<T> schema;
        final Pipe.Schema<T> pipeSchema;

        Registered(int id, Schema<T> schema, Pipe.Schema<T> pipeSchema, 
                IdStrategy strategy)
        {
            super(id, strategy);

            this.schema = schema;
            this.pipeSchema = pipeSchema;
        }

        @Override
        public Schema<T> getSchema()
        {
            return schema;
        }

        @Override
        public io.protostuff.Pipe.Schema<T> getPipeSchema()
        {
            return pipeSchema;
        }
    }

    static final class Lazy<T> extends BaseHS<T>
    {
        final Class<T> typeClass;
        private volatile Schema<T> schema;
        private volatile Pipe.Schema<T> pipeSchema;

        Lazy(int id, Class<T> typeClass, IdStrategy strategy)
        {
            super(id, strategy);
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
                            final Message<T> m = (Message<T>) createMessageInstance(typeClass);
                            this.schema = schema = m.cachedSchema();
                        }
                        else
                        {
                            // create new
                            this.schema = schema = strategy.newSchema(typeClass);
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
                        this.pipeSchema = pipeSchema = RuntimeSchema.resolvePipeSchema(
                                getSchema(), typeClass, true);
                    }
                }
            }
            return pipeSchema;
        }
    }
}
