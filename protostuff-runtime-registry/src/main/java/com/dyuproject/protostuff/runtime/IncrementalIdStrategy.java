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


package com.dyuproject.protostuff.runtime;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.MapSchema;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;

/**
 * The ids are generated (incremental) on the fly and you can optionally register classes 
 * by reserving the first x ids via {@link Registry}.
 * To minimize overhead, {@link ArrayList}s are used for the id mapping rather than 
 * {@link ConcurrentHashMap}.  This optimization has a disadvantage though.
 * Ids will not be unlimited.  You'll have to specificy a max id for the 
 * 4 types (pojo, enum, collection, map)
 *
 * @author David Yu
 * @created Mar 27, 2012
 */
public final class IncrementalIdStrategy extends NumericIdStrategy
{
    /**
     * To use {@link IncrementalIdStrategy} without registering anything,  
     * set the system property:
     * "-Dprotostuff.runtime.id_strategy_factory=com.dyuproject.protostuff.runtime.IncrementalIdStrategy$Factory"
     * 
     * Note that the pojos will be limited to 63 and the enums to 15.
     * 
     * It is best that you use the {@link Registry} to configure the strategy and 
     * set the max limits for each type. 
     */
    public static class Factory implements IdStrategy.Factory
    {
        public IdStrategy create()
        {
            return new IncrementalIdStrategy(
                    CollectionSchema.MessageFactories.values().length, 1, 
                    MapSchema.MessageFactories.values().length, 1, 
                    16, 1, // enums
                    64, 1); // pojos
        }

        public void postCreate()
        {
            
        }
    }
    
    /**
     * This Registry is only way to register your pojos/enums/collections/maps/delegates.
     */
    public static class Registry implements NumericIdStrategy.Registry
    {
        
        public final IncrementalIdStrategy strategy;
        
        public Registry(
                int collectionIdMax, int collectionIdStart,
                int mapIdMax, int mapIdStart,
                int enumIdMax, int enumIdStart, 
                int pojoIdMax, int pojoIdStart)
        {
            strategy = new IncrementalIdStrategy(
                    collectionIdMax, collectionIdStart, 
                    mapIdMax, mapIdStart, 
                    enumIdMax, enumIdStart, 
                    pojoIdMax, pojoIdStart);
        }
        
        /**
         * Collection ids start at 1.
         */
        public <T extends Collection<?>> Registry registerCollection(
                CollectionSchema.MessageFactory factory, int id)
        {
            if(id < 1)
                throw new IllegalArgumentException("collection ids start at 1.");
            
            if(id >= strategy.collectionIdStart)
                throw new IllegalArgumentException("collection ids must be lesser than " + strategy.collectionIdStart);
            else if(strategy.collections.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id + 
                        " (" + factory.typeClass() + ")");
            }
            
            RuntimeCollectionFactory rf = new RuntimeCollectionFactory();
            rf.id = id;
            rf.factory = factory;
            strategy.collections.set(id, rf);
            // just in case
            if(strategy.collectionMapping.put(factory.typeClass(), rf) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + factory.typeClass());
            
            return this;
        }
        
        /**
         * Map ids start at 1.
         */
        public <T extends Map<?,?>> Registry registerMap(
                MapSchema.MessageFactory factory, int id)
        {
            if(id < 1)
                throw new IllegalArgumentException("map ids start at 1.");
            
            if(id >= strategy.mapIdStart)
                throw new IllegalArgumentException("map ids must be lesser than " + strategy.mapIdStart);
            else if(strategy.maps.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id + 
                        " (" + factory.typeClass() + ")");
            }
            
            RuntimeMapFactory rf = new RuntimeMapFactory();
            rf.id = id;
            rf.factory = factory;
            strategy.maps.set(id, rf);
            // just in case
            if(strategy.mapMapping.put(factory.typeClass(), rf) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + factory.typeClass());
            
            return this;
        }
        
        /**
         * Enum ids start at 1.
         */
        public <T extends Enum<T>> Registry registerEnum(Class<T> clazz, int id)
        {
            if(id < 1)
                throw new IllegalArgumentException("enum ids start at 1.");
            
            if(id >= strategy.enumIdStart)
                throw new IllegalArgumentException("enum ids must be lesser than " + strategy.enumIdStart);
            else if(strategy.enums.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id + 
                        " (" + clazz.getName() + ")");
            }
            
            EnumIO<?> eio = EnumIO.newEnumIO(clazz);
            RuntimeEnumIO reio = new RuntimeEnumIO();
            reio.id = id;
            reio.eio = eio;
            strategy.enums.set(id, reio);
            
            // just in case
            if(strategy.enumMapping.put(clazz, reio) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + clazz);
            
            return this;
        }
        
        /**
         * Enum ids start at 1.
         */
        public Registry registerEnum(EnumIO<?> eio, int id)
        {
            if(id < 1)
                throw new IllegalArgumentException("enum ids start at 1.");
            
            if(id >= strategy.enumIdStart)
                throw new IllegalArgumentException("enum ids must be lesser than " + strategy.enumIdStart);
            else if(strategy.enums.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id + 
                        " (" + eio.enumClass.getName() + ")");
            }
            
            RuntimeEnumIO reio = new RuntimeEnumIO();
            reio.id = id;
            reio.eio = eio;
            strategy.enums.set(id, reio);
            
            // just in case
            if(strategy.enumMapping.put(eio.enumClass, reio) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + eio.enumClass);
            
            return this;
        }
        
        /**
         * Pojo ids start at 1.
         */
        public <T> Registry registerPojo(Class<T> clazz, int id)
        {
            if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
                throw new IllegalArgumentException("Not a concrete class: " + clazz);
            
            if(id < 1)
                throw new IllegalArgumentException("pojo ids start at 1.");
            
            if(id >= strategy.pojoIdStart)
                throw new IllegalArgumentException("pojo ids must be lesser than " + strategy.pojoIdStart);
            else if(strategy.pojos.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id + 
                        " (" + clazz.getName() + ")");
            }
            
            if(strategy.pojoMapping.containsKey(clazz))
                throw new IllegalArgumentException("Duplicate registration for: " + clazz);
            
            BaseHS<T> wrapper = new Lazy<T>(clazz, strategy);
            wrapper.id = id;
            strategy.pojos.set(id, wrapper);
            
            strategy.pojoMapping.put(clazz, wrapper);
            
            return this;
        }
        
        /**
         * Pojo ids start at 1.
         */
        public <T> Registry registerPojo(Schema<T> schema, Pipe.Schema<T> pipeSchema, 
                int id)
        {
            if(id >= strategy.pojoIdStart)
                throw new IllegalArgumentException("pojo ids must be lesser than " + strategy.pojoIdStart);
            else if(strategy.pojos.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id + 
                        " (" + schema.typeClass().getName() + ")");
            }
            
            if(strategy.pojoMapping.containsKey(schema.typeClass()))
                throw new IllegalArgumentException("Duplicate registration for: " + schema.typeClass());
            
            Registered<T> wrapper = new Registered<T>(id, schema, pipeSchema);
            strategy.pojos.set(id, wrapper);
            
            strategy.pojoMapping.put(schema.typeClass(), wrapper);
            
            return this;
        }
        
        /**
         * If you are sure that you are only using a single implementation of 
         * your interface/abstract class, then it makes sense to map it directly 
         * to its impl class to avoid writing the type.
         * 
         * Note that the type is always written when your field is 
         * {@link java.lang.Object}. 
         * 
         * Pojo ids start at 1.
         */
        public <T> Registry mapPojo(Class<? super T> baseClass, Class<T> implClass)
        {
            if(strategy.pojoMapping.containsKey(baseClass))
                throw new IllegalArgumentException("Duplicate registration for: " + baseClass);
            
            BaseHS<?> wrapper = strategy.pojoMapping.get(implClass);
            if(wrapper == null)
                throw new IllegalArgumentException("Must register the impl class first. " + implClass);
            
            strategy.pojoMapping.put(baseClass, wrapper);
            
            return this;
        }
        
        /**
         * Register a {@link Delegate} and assign an id.
         * 
         * Delegate ids start at 1.
         */
        public <T> Registry registerDelegate(Delegate<T> delegate, int id)
        {
            if(id < 1)
                throw new IllegalArgumentException("delegate ids start at 1.");
            
            if(id >= strategy.delegates.size())
                grow(strategy.delegates, id+1);
            else if(strategy.delegates.get(id) != null)
            {
                throw new IllegalArgumentException("Duplicate id registration: " + id + 
                        " (" + delegate.typeClass() + ")");
            }
            
            RegisteredDelegate<T> rd = new RegisteredDelegate<T>(id, delegate);
            strategy.delegates.set(id, rd);
            // just in case
            if(strategy.delegateMapping.put(delegate.typeClass(), rd) != null)
                throw new IllegalArgumentException("Duplicate registration for: " + delegate.typeClass());
            
            return this;
        }
    }
    
    final ConcurrentHashMap<Class<?>,RuntimeCollectionFactory> collectionMapping;
    
    final ArrayList<RuntimeCollectionFactory> collections;
    
    final ConcurrentHashMap<Class<?>,RuntimeMapFactory> mapMapping;
    
    final ArrayList<RuntimeMapFactory> maps;
    
    final ConcurrentHashMap<Class<?>,RuntimeEnumIO> enumMapping;
    
    final ArrayList<RuntimeEnumIO> enums;
    
    final ConcurrentHashMap<Class<?>, BaseHS<?>> pojoMapping;
    
    final ArrayList<BaseHS<?>> pojos;
    
    final IdentityHashMap<Class<?>, RegisteredDelegate<?>> delegateMapping;
    
    final ArrayList<RegisteredDelegate<?>> delegates;
    
    final AtomicInteger pojoId, enumId, collectionId, mapId;
    final int pojoIdStart, enumIdStart, collectionIdStart, mapIdStart;
    
    public IncrementalIdStrategy(
            int collectionIdMax, int collectionIdStart,
            int mapIdMax, int mapIdStart,
            int enumIdMax, int enumIdStart, 
            int pojoIdMax, int pojoIdStart)
            //int delegateIdMax, int delegateIdStart)
    {
        assert collectionIdMax > collectionIdStart;
        assert mapIdMax > mapIdStart;
        assert enumIdMax > enumIdStart;
        assert pojoIdMax > pojoIdStart;
        //assert delegateIdMax > delegateIdStart;
        
        this.collectionIdStart = collectionIdStart;
        collectionId = new AtomicInteger(collectionIdStart);
        collectionMapping = new ConcurrentHashMap<Class<?>, RuntimeCollectionFactory>(
                collectionIdMax);
        collections = newList(collectionIdMax + 1);
        
        this.mapIdStart = mapIdStart;
        mapId = new AtomicInteger(mapIdStart);
        mapMapping = new ConcurrentHashMap<Class<?>, RuntimeMapFactory>(mapIdMax);
        maps = newList(mapIdMax + 1);
        
        this.enumIdStart = enumIdStart;
        enumId = new AtomicInteger(enumIdStart);
        enumMapping = new ConcurrentHashMap<Class<?>, RuntimeEnumIO>(enumIdMax);
        enums = newList(enumIdMax + 1);
        
        this.pojoIdStart = pojoIdStart;
        pojoId = new AtomicInteger(pojoIdStart);
        pojoMapping = new ConcurrentHashMap<Class<?>, BaseHS<?>>(pojoIdMax);
        pojos = newList(pojoIdMax + 1);
        
        // delegates require explicit registration
        delegateMapping = new IdentityHashMap<Class<?>, RegisteredDelegate<?>>(
                10);//delegateIdMax);
        delegates = newList(11);//delegateIdMax + 1);
    }
    
    public boolean isRegistered(Class<?> typeClass)
    {
        return pojoMapping.get(typeClass) instanceof Registered;
    }
    
    @SuppressWarnings("unchecked")
    private <T> BaseHS<T> getBaseHS(Class<T> typeClass, boolean create)
    {
        BaseHS<T> hs = (BaseHS<T>)pojoMapping.get(typeClass);
        if(hs == null && create)
        {
            hs = new Lazy<T>(typeClass, this);
            final BaseHS<T> last = (BaseHS<T>)pojoMapping.putIfAbsent(
                    typeClass, hs);
            if(last != null)
                hs = last;
            else
            {
                int id = pojoId.getAndIncrement();
                pojos.set(id, hs);
                
                // commit
                hs.id = id;
            }
        }
        
        return hs;
    }
    
    public <T> HasSchema<T> getSchemaWrapper(Class<T> typeClass, boolean create)
    {
        return getBaseHS(typeClass, create);
    }
    
    private RuntimeEnumIO getRuntimeEnumIO(Class<?> enumClass)
    {
        RuntimeEnumIO reio = enumMapping.get(enumClass);
        if(reio == null)
        {
            reio = new RuntimeEnumIO();
            
            final RuntimeEnumIO existing = enumMapping.putIfAbsent(enumClass, reio);
            if(existing != null)
                reio = existing;
            else
            {
                reio.eio = EnumIO.newEnumIO(enumClass);
                int id = enumId.getAndIncrement();
                enums.set(id, reio);
                
                // commit
                reio.id = id;
            }
        }
        
        return reio;
    }
    
    protected EnumIO<? extends Enum<?>> getEnumIO(Class<?> enumClass)
    {
        return getRuntimeEnumIO(enumClass).eio;
    }
    
    private RuntimeCollectionFactory getRuntimeCollectionFactory(Class<?> clazz)
    {
        RuntimeCollectionFactory rfactory = collectionMapping.get(clazz);
        if(rfactory == null)
        {
            rfactory = new RuntimeCollectionFactory();
            RuntimeCollectionFactory f = collectionMapping.putIfAbsent(
                    clazz, rfactory);
            if(f != null)
                rfactory = f;
            else
            {
                if(clazz.getName().startsWith("java.util"))
                {
                    rfactory.factory = CollectionSchema.MessageFactories.valueOf(
                            clazz.getSimpleName());
                }
                else
                {
                    rfactory.instantiator = RuntimeEnv.newInstantiator(clazz);
                    rfactory.collectionClass = clazz;
                }
                
                int id = collectionId.getAndIncrement();
                collections.set(id, rfactory);
                
                // commit
                rfactory.id = id;
            }
        }
        
        return rfactory;
    }
    
    protected CollectionSchema.MessageFactory getCollectionFactory(Class<?> clazz)
    {
        return getRuntimeCollectionFactory(clazz);
    }
    
    private RuntimeMapFactory getRuntimeMapFactory(Class<?> clazz)
    {
        RuntimeMapFactory rfactory = mapMapping.get(clazz);
        if(rfactory == null)
        {
            rfactory = new RuntimeMapFactory();
            RuntimeMapFactory f = mapMapping.putIfAbsent(
                    clazz, rfactory);
            if(f != null)
                rfactory = f;
            else
            {
                if(clazz.getName().startsWith("java.util"))
                {
                    rfactory.factory = MapSchema.MessageFactories.valueOf(
                            clazz.getSimpleName());
                }
                else
                {
                    rfactory.instantiator = RuntimeEnv.newInstantiator(clazz);
                    rfactory.mapClass = clazz;
                }
                
                int id = mapId.getAndIncrement();
                maps.set(id, rfactory);
                
                // commit
                rfactory.id = id;
            }
        }
        
        return rfactory;
    }
    
    protected MapSchema.MessageFactory getMapFactory(Class<?> clazz)
    {
        return getRuntimeMapFactory(clazz);
    }
    
    protected void writeCollectionIdTo(Output output, int fieldNumber, Class<?> clazz)
            throws IOException
    {
        int id;
        RuntimeCollectionFactory factory = getRuntimeCollectionFactory(clazz);
        
        // wait till everything is completely set
        while(0 == (id = factory.id))
            LockSupport.parkNanos(1);
        
        output.writeUInt32(fieldNumber, id, false);
    }
    
    protected void transferCollectionId(Input input, Output output, int fieldNumber)
            throws IOException
    {
        output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }

    
    protected CollectionSchema.MessageFactory resolveCollectionFrom(Input input) 
            throws IOException
    {
        final int id = input.readUInt32();
        
        final RuntimeCollectionFactory factory = id < collections.size() ? 
                collections.get(id) : null;
        if(factory == null)
            throw new UnknownTypeException("Unknown collection id: " + id);
        
        return factory;
    }
    
    protected void writeMapIdTo(Output output, int fieldNumber, Class<?> clazz) 
            throws IOException
    {
        int id;
        RuntimeMapFactory factory = getRuntimeMapFactory(clazz);

        // wait till everything is completely set
        while(0 == (id = factory.id))
            LockSupport.parkNanos(1);
        
        output.writeUInt32(fieldNumber, id, false);
    }
    
    protected void transferMapId(Input input, Output output, int fieldNumber) 
            throws IOException
    {
        output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }
    
    protected MapSchema.MessageFactory resolveMapFrom(Input input)
            throws IOException
    {
        final int id = input.readUInt32();
        
        final RuntimeMapFactory factory = id < maps.size() ? maps.get(id) : null;
        if(factory == null)
            throw new UnknownTypeException("Unknown map id: " + id);
        
        return factory;
    }
    
    protected void writeEnumIdTo(Output output, int fieldNumber, Class<?> clazz) 
            throws IOException
    {
        int id;
        RuntimeEnumIO reio = getRuntimeEnumIO(clazz);

        // wait till everything is completely set
        while(0 == (id = reio.id))
            LockSupport.parkNanos(1);
        
        output.writeUInt32(fieldNumber, id , false);
    }
    
    protected void transferEnumId(Input input, Output output, int fieldNumber) 
            throws IOException
    {
        output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }
    
    protected EnumIO<?> resolveEnumFrom(Input input) throws IOException
    {
        final int id = input.readUInt32();
        
        final RuntimeEnumIO reio = id < enums.size() ? enums.get(id) : null;
        if(reio == null)
            throw new UnknownTypeException("Unknown enum id: " + id);
        
        return reio.eio;
    }
    
    public boolean isDelegateRegistered(Class<?> typeClass)
    {
        return delegateMapping.containsKey(typeClass);
    }

    @SuppressWarnings("unchecked")
    public <T> Delegate<T> getDelegate(Class<? super T> typeClass)
    {
        final RegisteredDelegate<T> rd = (RegisteredDelegate<T>)delegateMapping.get(
                typeClass);
        
        return rd == null ? null : rd.delegate;
    }

    @SuppressWarnings("unchecked")
    protected <T> Delegate<T> tryWriteDelegateIdTo(Output output, int fieldNumber, 
            Class<T> clazz) throws IOException
    {
        final RegisteredDelegate<T> rd = (RegisteredDelegate<T>)delegateMapping.get(
                clazz);
        
        if(rd == null)
            return null;
        
        output.writeUInt32(fieldNumber, rd.id, false);
        
        return rd.delegate;
    }

    @SuppressWarnings("unchecked")
    protected <T> Delegate<T> transferDelegateId(Input input, Output output, int fieldNumber)
            throws IOException
    {
        final int id = input.readUInt32();
        
        final RegisteredDelegate<T> rd = id < delegates.size() ? 
                (RegisteredDelegate<T>)delegates.get(id) : null;
        if(rd == null)
            throw new UnknownTypeException("delegate id: " + id + " (Outdated registry)");
        
        output.writeUInt32(fieldNumber, id, false);
        
        return rd.delegate;
    }

    @SuppressWarnings("unchecked")
    protected <T> Delegate<T> resolveDelegateFrom(Input input) throws IOException
    {
        final int id = input.readUInt32();
        
        final RegisteredDelegate<T> rd = id < delegates.size() ? 
                (RegisteredDelegate<T>)delegates.get(id) : null;
        if(rd == null)
            throw new UnknownTypeException("delegate id: " + id + " (Outdated registry)");
        
        return rd.delegate;
    }
    
    protected <T> Schema<T> writePojoIdTo(Output output, int fieldNumber, Class<T> clazz)
            throws IOException
    {
        int id;
        BaseHS<T> wrapper = getBaseHS(clazz, true);
        
        // wait till everything is completely set
        while(0 == (id = wrapper.id))
            LockSupport.parkNanos(1);
        
        output.writeUInt32(fieldNumber, id , false);
        
        return wrapper.getSchema();
    }
    
    @SuppressWarnings("unchecked")
    protected <T> HasSchema<T> transferPojoId(Input input, Output output, int fieldNumber)
            throws IOException
    {
        final int id = input.readUInt32();
        
        final BaseHS<T> wrapper = id < pojos.size() ? (BaseHS<T>)pojos.get(id) : null;
        if(wrapper == null)
            throw new UnknownTypeException("unknown pojo id: " + id);
        
        output.writeUInt32(fieldNumber, id, false);
        
        return wrapper;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> HasSchema<T> resolvePojoFrom(Input input, int fieldNumber) throws IOException
    {
        final int id = input.readUInt32();
        
        final BaseHS<T> wrapper = id < pojos.size() ? (BaseHS<T>)pojos.get(id) : null;
        if(wrapper == null)
            throw new UnknownTypeException("unknown pojo id: " + id);
        
        return wrapper;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> Schema<T> writeMessageIdTo(Output output, int fieldNumber, 
            Message<T> message) throws IOException
    {
        int id;
        BaseHS<T> wrapper = (BaseHS<T>)getSchemaWrapper(message.getClass(), true);
        
        // wait till everything is completely set
        while(0 == (id = wrapper.id))
            LockSupport.parkNanos(1);
        
        output.writeUInt32(fieldNumber, id , false);
        
        // TODO allow the wrapper to return an override schema?
        return message.cachedSchema();
    }
    
    protected Class<?> collectionClass(int id)
    {
        final RuntimeCollectionFactory factory = id < collections.size() ? 
                collections.get(id) : null;
        if(factory == null)
            throw new UnknownTypeException("Unknown collection id: " + id);
        
        return factory.typeClass();
    }
    
    protected Class<?> mapClass(int id)
    {
        final RuntimeMapFactory factory = id < maps.size() ? maps.get(id) : null;
        if(factory == null)
            throw new UnknownTypeException("Unknown map id: " + id);
        
        return factory.typeClass();
    }
    
    protected Class<?> enumClass(int id)
    {
        final RuntimeEnumIO reio = id < enums.size() ? enums.get(id) : null;
        if(reio == null)
            throw new UnknownTypeException("Unknown enum id: " + id);
        
        return reio.eio.enumClass;
    }
    
    protected Class<?> delegateClass(int id)
    {
        final RegisteredDelegate<?> rd = id < delegates.size() ? delegates.get(id) : null;
        return rd == null ? null : rd.delegate.typeClass();
    }
    
    protected Class<?> pojoClass(int id)
    {
        final BaseHS<?> wrapper = id < pojos.size() ? pojos.get(id) : null;
        if(wrapper == null)
            throw new UnknownTypeException("Unknown pojo id: " + id);
        
        return wrapper.getSchema().typeClass();
    }
    
    protected RegisteredDelegate<?> getRegisteredDelegate(Class<?> clazz)
    {
        return delegateMapping.get(clazz);
    }
    
    protected int getEnumId(Class<?> clazz)
    {
        final RuntimeEnumIO reio = getRuntimeEnumIO(clazz);

        // wait till everything is completely set
        int id;
        while(0 == (id = reio.id))
            LockSupport.parkNanos(1);
        
        return (id << 5) | CID_ENUM;
    }
    
    protected int getId(Class<?> clazz)
    {
        int id;
        if(Message.class.isAssignableFrom(clazz))
        {
            final BaseHS<?> wrapper = getBaseHS(clazz, true);
            
            // wait till everything is completely set
            while(0 == (id = wrapper.id))
                LockSupport.parkNanos(1);
            
            return (id << 5) | CID_POJO;
        }
        
        if(Map.class.isAssignableFrom(clazz))
        {
            if(EnumMap.class.isAssignableFrom(clazz))
                return CID_ENUM_MAP;
            
            final RuntimeMapFactory factory = getRuntimeMapFactory(clazz);
            
            // wait till everything is completely set
            while(0 == (id = factory.id))
                LockSupport.parkNanos(1);
            
            return (id << 5) | CID_MAP;
        }
        
        if(Collection.class.isAssignableFrom(clazz))
        {
            if(EnumSet.class.isAssignableFrom(clazz))
                return CID_ENUM_SET;
            
            final RuntimeCollectionFactory factory = getRuntimeCollectionFactory(clazz);
            
            // wait till everything is completely set
            while(0 == (id = factory.id))
                LockSupport.parkNanos(1);
            
            return (id << 5) | CID_COLLECTION;
        }
        
        final BaseHS<?> wrapper = getBaseHS(clazz, true);
        
        // wait till everything is completely set
        while(0 == (id = wrapper.id))
            LockSupport.parkNanos(1);
        
        return (id << 5) | CID_POJO;
    }
    
    static final class RuntimeCollectionFactory implements CollectionSchema.MessageFactory
    {
        volatile int id;
        CollectionSchema.MessageFactory factory;
        
        Class<?> collectionClass;
        RuntimeEnv.Instantiator<?> instantiator;
        
        @SuppressWarnings("unchecked")
        public <V> Collection<V> newMessage()
        {
            if(factory == null)
                return (Collection<V>)instantiator.newInstance();
            
            return factory.newMessage();
        }
        
        public Class<?> typeClass()
        {
            return factory == null ? collectionClass : factory.typeClass();
        }
    }
    
    static final class RuntimeMapFactory implements MapSchema.MessageFactory
    {
        volatile int id;
        MapSchema.MessageFactory factory;
        
        Class<?> mapClass;
        RuntimeEnv.Instantiator<?> instantiator;
        
        @SuppressWarnings("unchecked")
        public <K, V> Map<K, V> newMessage()
        {
            if(factory == null)
                return (Map<K, V>)instantiator.newInstance();
            
            return factory.newMessage();
        }
        
        public Class<?> typeClass()
        {
            return factory == null ? mapClass : factory.typeClass();
        }
    }
    
    static final class RuntimeEnumIO
    {
        volatile int id;
        EnumIO<?> eio;
    }
    
    static abstract class BaseHS<T> extends HasSchema<T>
    {
        volatile int id;
    }
    
    static final class Registered<T> extends BaseHS<T>
    {
        
        final Schema<T> schema;
        final Pipe.Schema<T> pipeSchema;
        
        Registered(int id, Schema<T> schema, Pipe.Schema<T> pipeSchema)
        {
            this.id = id;
            
            this.schema = schema;
            this.pipeSchema = pipeSchema;
        }

        public Schema<T> getSchema()
        {
            return schema;
        }
        
        public com.dyuproject.protostuff.Pipe.Schema<T> getPipeSchema()
        {
            return pipeSchema;
        }
    }
    
    static final class Lazy<T> extends BaseHS<T>
    {
        final IdStrategy strategy;
        final Class<T> typeClass;
        private volatile Schema<T> schema;
        private volatile Pipe.Schema<T> pipeSchema;
        
        Lazy(Class<T> typeClass, IdStrategy strategy)
        {
            this.typeClass = typeClass;
            this.strategy = strategy;
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
                            this.schema = schema = RuntimeSchema.createFrom(typeClass, strategy);
                        }
                    }
                }
            }

            return schema;
        }
        
        public Pipe.Schema<T> getPipeSchema()
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
}
