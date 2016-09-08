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

import static io.protostuff.runtime.RuntimeFieldFactory.ID_COLLECTION;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_ENUM;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_ENUM_SET;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_COLLECTION;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_ENUM;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_ENUM_SET;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;
import io.protostuff.StatefulOutput;
import io.protostuff.runtime.IdStrategy.Wrapper;
import io.protostuff.runtime.RuntimeEnv.Instantiator;

/**
 * Used when the type is an interface (Collection/List/Set/SortedSet).
 * 
 * @author David Yu
 * @created Apr 24, 2012
 */
public abstract class PolymorphicCollectionSchema extends PolymorphicSchema
{

    static final int ID_EMPTY_SET = 1, ID_EMPTY_LIST = 2,

            ID_SINGLETON_SET = 3, ID_SINGLETON_LIST = 4,

            ID_SET_FROM_MAP = 5, ID_COPIES_LIST = 6,

            ID_UNMODIFIABLE_COLLECTION = 7, ID_UNMODIFIABLE_SET = 8,
            ID_UNMODIFIABLE_SORTED_SET = 9, ID_UNMODIFIABLE_LIST = 10,
            ID_UNMODIFIABLE_RANDOM_ACCESS_LIST = 11,

            ID_SYNCHRONIZED_COLLECTION = 12, ID_SYNCHRONIZED_SET = 13,
            ID_SYNCHRONIZED_SORTED_SET = 14, ID_SYNCHRONIZED_LIST = 15,
            ID_SYNCHRONIZED_RANDOM_ACCESS_LIST = 16,

            ID_CHECKED_COLLECTION = 17, ID_CHECKED_SET = 18,
            ID_CHECKED_SORTED_SET = 19, ID_CHECKED_LIST = 20,
            ID_CHECKED_RANDOM_ACCESS_LIST = 21;

    static final String STR_EMPTY_SET = "a", STR_EMPTY_LIST = "b",

            STR_SINGLETON_SET = "c", STR_SINGLETON_LIST = "d",

            STR_SET_FROM_MAP = "e", STR_COPIES_LIST = "f",

            STR_UNMODIFIABLE_COLLECTION = "g", STR_UNMODIFIABLE_SET = "h",
            STR_UNMODIFIABLE_SORTED_SET = "i", STR_UNMODIFIABLE_LIST = "j",
            STR_UNMODIFIABLE_RANDOM_ACCESS_LIST = "k",

            STR_SYNCHRONIZED_COLLECTION = "l", STR_SYNCHRONIZED_SET = "m",
            STR_SYNCHRONIZED_SORTED_SET = "n", STR_SYNCHRONIZED_LIST = "o",
            STR_SYNCHRONIZED_RANDOM_ACCESS_LIST = "p",

            STR_CHECKED_COLLECTION = "q", STR_CHECKED_SET = "r",
            STR_CHECKED_SORTED_SET = "s", STR_CHECKED_LIST = "t",
            STR_CHECKED_RANDOM_ACCESS_LIST = "u";

    static final IdentityHashMap<Class<?>, Integer> __nonPublicCollections = new IdentityHashMap<Class<?>, Integer>();

    static final Field fSingletonSet_element, fSingletonList_element,

            fUnmodifiableCollection_c, fUnmodifiableSortedSet_ss,
            fUnmodifiableList_list,

            fSynchronizedCollection_c, fSynchronizedSortedSet_ss,
            fSynchronizedList_list, fSynchronizedCollection_mutex,

            fCheckedCollection_c, fCheckedSortedSet_ss, fCheckedList_list,
            fCheckedCollection_type,

            fSetFromMap_m, fSetFromMap_s,

            fCopiesList_n, fCopiesList_element;

    static final Instantiator<?> iSingletonSet, iSingletonList,

            iUnmodifiableCollection, iUnmodifiableSet, iUnmodifiableSortedSet,
            iUnmodifiableList, iUnmodifiableRandomAccessList,

            iSynchronizedCollection, iSynchronizedSet, iSynchronizedSortedSet,
            iSynchronizedList, iSynchronizedRandomAccessList,

            iCheckedCollection, iCheckedSet, iCheckedSortedSet, iCheckedList,
            iCheckedRandomAccessList,

            iSetFromMap,

            iCopiesList;

    static
    {
        map("java.util.Collections$EmptySet", ID_EMPTY_SET);
        map("java.util.Collections$EmptyList", ID_EMPTY_LIST);

        Class<?> cSingletonSet = map("java.util.Collections$SingletonSet",
                ID_SINGLETON_SET);

        Class<?> cSingletonList = map("java.util.Collections$SingletonList",
                ID_SINGLETON_LIST);

        Class<?> cSetFromMap = map("java.util.Collections$SetFromMap",
                ID_SET_FROM_MAP);

        Class<?> cCopiesList = map("java.util.Collections$CopiesList",
                ID_COPIES_LIST);

        Class<?> cUnmodifiableCollection = map(
                "java.util.Collections$UnmodifiableCollection",
                ID_UNMODIFIABLE_COLLECTION);
        Class<?> cUnmodifiableSet = map(
                "java.util.Collections$UnmodifiableSet", ID_UNMODIFIABLE_SET);
        Class<?> cUnmodifiableSortedSet = map(
                "java.util.Collections$UnmodifiableSortedSet",
                ID_UNMODIFIABLE_SORTED_SET);
        Class<?> cUnmodifiableList = map(
                "java.util.Collections$UnmodifiableList", ID_UNMODIFIABLE_LIST);
        Class<?> cUnmodifiableRandomAccessList = map(
                "java.util.Collections$UnmodifiableRandomAccessList",
                ID_UNMODIFIABLE_RANDOM_ACCESS_LIST);

        Class<?> cSynchronizedCollection = map(
                "java.util.Collections$SynchronizedCollection",
                ID_SYNCHRONIZED_COLLECTION);
        Class<?> cSynchronizedSet = map(
                "java.util.Collections$SynchronizedSet", ID_SYNCHRONIZED_SET);
        Class<?> cSynchronizedSortedSet = map(
                "java.util.Collections$SynchronizedSortedSet",
                ID_SYNCHRONIZED_SORTED_SET);
        Class<?> cSynchronizedList = map(
                "java.util.Collections$SynchronizedList", ID_SYNCHRONIZED_LIST);
        Class<?> cSynchronizedRandomAccessList = map(
                "java.util.Collections$SynchronizedRandomAccessList",
                ID_SYNCHRONIZED_RANDOM_ACCESS_LIST);

        Class<?> cCheckedCollection = map(
                "java.util.Collections$CheckedCollection",
                ID_CHECKED_COLLECTION);
        Class<?> cCheckedSet = map("java.util.Collections$CheckedSet",
                ID_CHECKED_SET);
        Class<?> cCheckedSortedSet = map(
                "java.util.Collections$CheckedSortedSet", ID_CHECKED_SORTED_SET);
        Class<?> cCheckedList = map("java.util.Collections$CheckedList",
                ID_CHECKED_LIST);
        Class<?> cCheckedRandomAccessList = map(
                "java.util.Collections$CheckedRandomAccessList",
                ID_CHECKED_RANDOM_ACCESS_LIST);

        try
        {
            fSingletonSet_element = cSingletonSet.getDeclaredField("element");

            fSingletonList_element = cSingletonList.getDeclaredField("element");

            fSetFromMap_m = cSetFromMap.getDeclaredField("m");
            fSetFromMap_s = cSetFromMap.getDeclaredField("s");

            fCopiesList_n = cCopiesList.getDeclaredField("n");
            fCopiesList_element = cCopiesList.getDeclaredField("element");

            fUnmodifiableCollection_c = cUnmodifiableCollection
                    .getDeclaredField("c");
            fUnmodifiableSortedSet_ss = cUnmodifiableSortedSet
                    .getDeclaredField("ss");
            fUnmodifiableList_list = cUnmodifiableList.getDeclaredField("list");

            fSynchronizedCollection_c = cSynchronizedCollection
                    .getDeclaredField("c");
            fSynchronizedCollection_mutex = cSynchronizedCollection
                    .getDeclaredField("mutex");
            fSynchronizedSortedSet_ss = cSynchronizedSortedSet
                    .getDeclaredField("ss");
            fSynchronizedList_list = cSynchronizedList.getDeclaredField("list");

            fCheckedCollection_c = cCheckedCollection.getDeclaredField("c");
            fCheckedCollection_type = cCheckedCollection
                    .getDeclaredField("type");
            fCheckedSortedSet_ss = cCheckedSortedSet.getDeclaredField("ss");
            fCheckedList_list = cCheckedList.getDeclaredField("list");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // field accessors

        fSingletonSet_element.setAccessible(true);

        fSingletonList_element.setAccessible(true);

        fSetFromMap_m.setAccessible(true);
        fSetFromMap_s.setAccessible(true);

        fCopiesList_n.setAccessible(true);
        fCopiesList_element.setAccessible(true);

        fUnmodifiableCollection_c.setAccessible(true);
        fUnmodifiableSortedSet_ss.setAccessible(true);
        fUnmodifiableList_list.setAccessible(true);

        fSynchronizedCollection_c.setAccessible(true);
        fSynchronizedCollection_mutex.setAccessible(true);
        fSynchronizedSortedSet_ss.setAccessible(true);
        fSynchronizedList_list.setAccessible(true);

        fCheckedCollection_c.setAccessible(true);
        fCheckedCollection_type.setAccessible(true);
        fCheckedSortedSet_ss.setAccessible(true);
        fCheckedList_list.setAccessible(true);

        // instantiators

        iSingletonSet = RuntimeEnv.newInstantiator(cSingletonSet);

        iSingletonList = RuntimeEnv.newInstantiator(cSingletonList);

        iSetFromMap = RuntimeEnv.newInstantiator(cSetFromMap);

        iCopiesList = RuntimeEnv.newInstantiator(cCopiesList);

        iUnmodifiableCollection = RuntimeEnv
                .newInstantiator(cUnmodifiableCollection);
        iUnmodifiableSet = RuntimeEnv.newInstantiator(cUnmodifiableSet);
        iUnmodifiableSortedSet = RuntimeEnv
                .newInstantiator(cUnmodifiableSortedSet);
        iUnmodifiableList = RuntimeEnv.newInstantiator(cUnmodifiableList);
        iUnmodifiableRandomAccessList = RuntimeEnv
                .newInstantiator(cUnmodifiableRandomAccessList);

        iSynchronizedCollection = RuntimeEnv
                .newInstantiator(cSynchronizedCollection);
        iSynchronizedSet = RuntimeEnv.newInstantiator(cSynchronizedSet);
        iSynchronizedSortedSet = RuntimeEnv
                .newInstantiator(cSynchronizedSortedSet);
        iSynchronizedList = RuntimeEnv.newInstantiator(cSynchronizedList);
        iSynchronizedRandomAccessList = RuntimeEnv
                .newInstantiator(cSynchronizedRandomAccessList);

        iCheckedCollection = RuntimeEnv.newInstantiator(cCheckedCollection);
        iCheckedSet = RuntimeEnv.newInstantiator(cCheckedSet);
        iCheckedSortedSet = RuntimeEnv.newInstantiator(cCheckedSortedSet);
        iCheckedList = RuntimeEnv.newInstantiator(cCheckedList);
        iCheckedRandomAccessList = RuntimeEnv
                .newInstantiator(cCheckedRandomAccessList);
    }

    private static Class<?> map(String className, int id)
    {
        Class<?> clazz = RuntimeEnv.loadClass(className);
        __nonPublicCollections.put(clazz, id);
        return clazz;
    }

    static String name(int number)
    {
        switch (number)
        {
            case ID_EMPTY_SET:
                return STR_EMPTY_SET;
            case ID_EMPTY_LIST:
                return STR_EMPTY_LIST;
            case ID_SINGLETON_SET:
                return STR_SINGLETON_SET;
            case ID_SINGLETON_LIST:
                return STR_SINGLETON_LIST;
            case ID_SET_FROM_MAP:
                return STR_SET_FROM_MAP;
            case ID_COPIES_LIST:
                return STR_COPIES_LIST;
            case ID_UNMODIFIABLE_COLLECTION:
                return STR_UNMODIFIABLE_COLLECTION;
            case ID_UNMODIFIABLE_SET:
                return STR_UNMODIFIABLE_SET;
            case ID_UNMODIFIABLE_SORTED_SET:
                return STR_UNMODIFIABLE_SORTED_SET;
            case ID_UNMODIFIABLE_LIST:
                return STR_UNMODIFIABLE_LIST;
            case ID_UNMODIFIABLE_RANDOM_ACCESS_LIST:
                return STR_UNMODIFIABLE_RANDOM_ACCESS_LIST;
            case ID_SYNCHRONIZED_COLLECTION:
                return STR_SYNCHRONIZED_COLLECTION;
            case ID_SYNCHRONIZED_SET:
                return STR_SYNCHRONIZED_SET;
            case ID_SYNCHRONIZED_SORTED_SET:
                return STR_SYNCHRONIZED_SORTED_SET;
            case ID_SYNCHRONIZED_LIST:
                return STR_SYNCHRONIZED_LIST;
            case ID_SYNCHRONIZED_RANDOM_ACCESS_LIST:
                return STR_SYNCHRONIZED_RANDOM_ACCESS_LIST;
            case ID_CHECKED_COLLECTION:
                return STR_CHECKED_COLLECTION;
            case ID_CHECKED_SET:
                return STR_CHECKED_SET;
            case ID_CHECKED_SORTED_SET:
                return STR_CHECKED_SORTED_SET;
            case ID_CHECKED_LIST:
                return STR_CHECKED_LIST;
            case ID_CHECKED_RANDOM_ACCESS_LIST:
                return STR_CHECKED_RANDOM_ACCESS_LIST;
            case ID_ENUM_SET:
                return STR_ENUM_SET;
            case ID_ENUM:
                return STR_ENUM;
            case ID_COLLECTION:
                return STR_COLLECTION;
            default:
                return null;
        }
    }
    
    static int number(String name)
    {
        return name.length() != 1 ? 0 : number(name.charAt(0));
    }

    static int number(char c)
    {
        switch (c)
        {
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
            case 'i':
                return 9;
            case 'j':
                return 10;
            case 'k':
                return 11;
            case 'l':
                return 12;
            case 'm':
                return 13;
            case 'n':
                return 14;
            case 'o':
                return 15;
            case 'p':
                return 16;
            case 'q':
                return 17;
            case 'r':
                return 18;
            case 's':
                return 19;
            case 't':
                return 20;
            case 'u':
                return 21;
            case 'v':
                return ID_ENUM_SET;
            case 'x':
                return ID_ENUM;
            case 'y':
                return ID_COLLECTION;
            default:
                return 0;
        }
    }

    protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
            this)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            transferObject(this, pipe, input, output, strategy);
        }
    };

    public PolymorphicCollectionSchema(IdStrategy strategy)
    {
        super(strategy);
    }

    @Override
    public Pipe.Schema<Object> getPipeSchema()
    {
        return pipeSchema;
    }

    @Override
    public String getFieldName(int number)
    {
        return name(number);
    }

    @Override
    public int getFieldNumber(String name)
    {
        return number(name);
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
    public void mergeFrom(Input input, Object owner) throws IOException
    {
        setValue(readObjectFrom(input, this, owner, strategy), owner);
    }

    @Override
    public void writeTo(Output output, Object value) throws IOException
    {
        writeObjectTo(output, value, this, strategy);
    }

    static int idFrom(Class<?> clazz)
    {
        final Integer id = __nonPublicCollections.get(clazz);
        if (id == null)
            throw new RuntimeException("Unknown collection: " + clazz);

        return id.intValue();
    }

    static Object instanceFrom(final int id)
    {
        switch (id)
        {
            case ID_EMPTY_SET:
                return Collections.EMPTY_SET;
            case ID_EMPTY_LIST:
                return Collections.EMPTY_LIST;

            case ID_SINGLETON_SET:
                return iSingletonSet.newInstance();
            case ID_SINGLETON_LIST:
                return iSingletonList.newInstance();

            case ID_SET_FROM_MAP:
                return iSetFromMap.newInstance();
            case ID_COPIES_LIST:
                return iCopiesList.newInstance();

            case ID_UNMODIFIABLE_COLLECTION:
                return iUnmodifiableCollection.newInstance();
            case ID_UNMODIFIABLE_SET:
                return iUnmodifiableSet.newInstance();
            case ID_UNMODIFIABLE_SORTED_SET:
                return iUnmodifiableSortedSet.newInstance();
            case ID_UNMODIFIABLE_LIST:
                return iUnmodifiableList.newInstance();
            case ID_UNMODIFIABLE_RANDOM_ACCESS_LIST:
                return iUnmodifiableRandomAccessList.newInstance();

            case ID_SYNCHRONIZED_COLLECTION:
                return iSynchronizedCollection.newInstance();
            case ID_SYNCHRONIZED_SET:
                return iSynchronizedSet.newInstance();
            case ID_SYNCHRONIZED_SORTED_SET:
                return iSynchronizedSortedSet.newInstance();
            case ID_SYNCHRONIZED_LIST:
                return iSynchronizedList.newInstance();
            case ID_SYNCHRONIZED_RANDOM_ACCESS_LIST:
                return iSynchronizedRandomAccessList.newInstance();

            case ID_CHECKED_COLLECTION:
                return iCheckedCollection.newInstance();
            case ID_CHECKED_SET:
                return iCheckedSet.newInstance();
            case ID_CHECKED_SORTED_SET:
                return iCheckedSortedSet.newInstance();
            case ID_CHECKED_LIST:
                return iCheckedList.newInstance();
            case ID_CHECKED_RANDOM_ACCESS_LIST:
                return iCheckedRandomAccessList.newInstance();

            default:
                throw new RuntimeException("Unknown id: " + id);
        }
    }

    @SuppressWarnings("unchecked")
    static void writeObjectTo(Output output, Object value,
            Schema<?> currentSchema, IdStrategy strategy) throws IOException
    {
        if (Collections.class == value.getClass().getDeclaringClass())
        {
            writeNonPublicCollectionTo(output, value, currentSchema, strategy);
            return;
        }

        if (EnumSet.class.isAssignableFrom(value.getClass()))
        {
            strategy.writeEnumIdTo(output, ID_ENUM_SET,
                    EnumIO.getElementTypeFromEnumSet(value));

            // TODO optimize
        }
        else
        {
            strategy.writeCollectionIdTo(output, ID_COLLECTION,
                    value.getClass());
        }

        if (output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput) output).updateLast(strategy.COLLECTION_SCHEMA,
                    currentSchema);
        }

        strategy.COLLECTION_SCHEMA.writeTo(output, (Collection<Object>) value);
    }

    static void writeNonPublicCollectionTo(Output output, Object value,
            Schema<?> currentSchema, IdStrategy strategy) throws IOException
    {
        final Integer num = __nonPublicCollections.get(value.getClass());
        if (num == null)
            throw new RuntimeException("Unknown collection: "
                    + value.getClass());
        final int id = num.intValue();
        switch (id)
        {
            case ID_EMPTY_SET:
                output.writeUInt32(id, 0, false);
                break;

            case ID_EMPTY_LIST:
                output.writeUInt32(id, 0, false);
                break;

            case ID_SINGLETON_SET:
            {
                output.writeUInt32(id, 0, false);

                final Object element;
                try
                {
                    element = fSingletonSet_element.get(value);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (element != null)
                    output.writeObject(1, element, strategy.OBJECT_SCHEMA, false);

                break;
            }

            case ID_SINGLETON_LIST:
            {
                output.writeUInt32(id, 0, false);

                // faster path (reflection not needed to get the single element).
                final Object element = ((List<?>) value).get(0);

                if (element != null)
                    output.writeObject(1, element, strategy.OBJECT_SCHEMA, false);

                break;
            }

            case ID_SET_FROM_MAP:
            {
                final Object m;
                try
                {
                    m = fSetFromMap_m.get(value);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                output.writeObject(id, m, strategy.POLYMORPHIC_MAP_SCHEMA, false);

                break;
            }

            case ID_COPIES_LIST:
            {
                output.writeUInt32(id, 0, false);

                final int n = ((List<?>) value).size();
                final Object element;
                try
                {
                    element = fCopiesList_element.get(value);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                output.writeUInt32(1, n, false);

                if (element != null)
                    output.writeObject(2, element, strategy.OBJECT_SCHEMA, false);

                break;
            }
            case ID_UNMODIFIABLE_COLLECTION:
                writeUnmodifiableCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_UNMODIFIABLE_SET:
                writeUnmodifiableCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_UNMODIFIABLE_SORTED_SET:
                writeUnmodifiableCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_UNMODIFIABLE_LIST:
                writeUnmodifiableCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_UNMODIFIABLE_RANDOM_ACCESS_LIST:
                writeUnmodifiableCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;

            case ID_SYNCHRONIZED_COLLECTION:
                writeSynchronizedCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_SYNCHRONIZED_SET:
                writeSynchronizedCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_SYNCHRONIZED_SORTED_SET:
                writeSynchronizedCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_SYNCHRONIZED_LIST:
                writeSynchronizedCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;
            case ID_SYNCHRONIZED_RANDOM_ACCESS_LIST:
                writeSynchronizedCollectionTo(output, value, currentSchema,
                        strategy, id);
                break;

            case ID_CHECKED_COLLECTION:
                writeCheckedCollectionTo(output, value, currentSchema, strategy, id);
                break;
            case ID_CHECKED_SET:
                writeCheckedCollectionTo(output, value, currentSchema, strategy, id);
                break;
            case ID_CHECKED_SORTED_SET:
                writeCheckedCollectionTo(output, value, currentSchema, strategy, id);
                break;
            case ID_CHECKED_LIST:
                writeCheckedCollectionTo(output, value, currentSchema, strategy, id);
                break;
            case ID_CHECKED_RANDOM_ACCESS_LIST:
                writeCheckedCollectionTo(output, value, currentSchema, strategy, id);
                break;

            default:
                throw new RuntimeException("Should not happen.");
        }
    }

    private static void writeUnmodifiableCollectionTo(Output output,
            Object value, Schema<?> currentSchema, IdStrategy strategy, int id)
            throws IOException
    {
        final Object c;
        try
        {
            c = fUnmodifiableCollection_c.get(value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        output.writeObject(id, c, strategy.POLYMORPHIC_COLLECTION_SCHEMA, false);
    }

    private static void writeSynchronizedCollectionTo(Output output,
            Object value, Schema<?> currentSchema, IdStrategy strategy, int id)
            throws IOException
    {
        final Object c, mutex;
        try
        {
            c = fSynchronizedCollection_c.get(value);
            mutex = fSynchronizedCollection_mutex.get(value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        if (mutex != value)
        {
            // TODO for future release, introduce an interface(GraphOutput) so
            // we
            // can check whether the output can retain references.
            throw new RuntimeException(
                    "This exception is thrown to fail fast. "
                            + "Synchronized collections with a different mutex would only "
                            + "work if graph format is used, since the reference is retained.");
        }

        output.writeObject(id, c, strategy.POLYMORPHIC_COLLECTION_SCHEMA, false);
    }

    private static void writeCheckedCollectionTo(Output output, Object value,
            Schema<?> currentSchema, IdStrategy strategy, int id)
            throws IOException
    {
        final Object c, type;
        try
        {
            c = fCheckedCollection_c.get(value);
            type = fCheckedCollection_type.get(value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        output.writeObject(id, c, strategy.POLYMORPHIC_COLLECTION_SCHEMA, false);
        output.writeObject(1, type, strategy.CLASS_SCHEMA, false);
    }
    
    static Object readObjectFrom(Input input, Schema<?> schema, Object owner,
            IdStrategy strategy) throws IOException
    {
        return readObjectFrom(input, schema, owner, strategy, 
                input.readFieldNumber(schema));
    }

    @SuppressWarnings("unchecked")
    static Object readObjectFrom(Input input, Schema<?> schema, Object owner,
            IdStrategy strategy, final int number) throws IOException
    {
        final boolean graph = input instanceof GraphInput;
        Object ret = null;
        switch (number)
        {
            case ID_EMPTY_SET:
                if (0 != input.readUInt32())
                    throw new ProtostuffException("Corrupt input.");

                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(Collections.EMPTY_SET, owner);
                }

                ret = Collections.EMPTY_SET;
                break;

            case ID_EMPTY_LIST:
                if (0 != input.readUInt32())
                    throw new ProtostuffException("Corrupt input.");

                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(Collections.EMPTY_LIST, owner);
                }

                ret = Collections.EMPTY_LIST;
                break;

            case ID_SINGLETON_SET:
            {
                if (0 != input.readUInt32())
                    throw new ProtostuffException("Corrupt input.");

                final Object collection = iSingletonSet.newInstance();
                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(collection, owner);
                }

                final int next = input.readFieldNumber(schema);
                if (next == 0)
                {
                    // null element
                    return collection;
                }

                if (next != 1)
                    throw new ProtostuffException("Corrupt input");

                final Wrapper wrapper = new Wrapper();
                Object element = input.mergeObject(wrapper, strategy.OBJECT_SCHEMA);
                if (!graph || !((GraphInput) input).isCurrentMessageReference())
                    element = wrapper.value;

                try
                {
                    fSingletonSet_element.set(collection, element);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                ret = collection;
                break;
            }

            case ID_SINGLETON_LIST:
            {
                if (0 != input.readUInt32())
                    throw new ProtostuffException("Corrupt input.");

                final Object collection = iSingletonList.newInstance();
                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(collection, owner);
                }

                final int next = input.readFieldNumber(schema);
                if (next == 0)
                {
                    // null element
                    return collection;
                }

                if (next != 1)
                    throw new ProtostuffException("Corrupt input.");

                final Wrapper wrapper = new Wrapper();
                Object element = input.mergeObject(wrapper, strategy.OBJECT_SCHEMA);
                if (!graph || !((GraphInput) input).isCurrentMessageReference())
                    element = wrapper.value;

                try
                {
                    fSingletonList_element.set(collection, element);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                ret = collection;
                break;
            }

            case ID_SET_FROM_MAP:
            {
                final Object collection = iSetFromMap.newInstance();
                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(collection, owner);
                }

                final Wrapper wrapper = new Wrapper();
                Object m = input.mergeObject(wrapper,
                        strategy.POLYMORPHIC_MAP_SCHEMA);
                if (!graph || !((GraphInput) input).isCurrentMessageReference())
                    m = wrapper.value;

                try
                {
                    fSetFromMap_m.set(collection, m);
                    fSetFromMap_s.set(collection, ((Map<?, ?>) m).keySet());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                ret = collection;
                break;
            }

            case ID_COPIES_LIST:
            {
                if (0 != input.readUInt32())
                    throw new ProtostuffException("Corrupt input.");

                final Object collection = iCopiesList.newInstance();
                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(collection, owner);
                }

                if (1 != input.readFieldNumber(schema))
                    throw new ProtostuffException("Corrupt input.");

                final int n = input.readUInt32(), next = input
                        .readFieldNumber(schema);

                if (next == 0)
                {
                    // null element
                    try
                    {
                        fCopiesList_n.setInt(collection, n);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }

                    return collection;
                }

                if (next != 2)
                    throw new ProtostuffException("Corrupt input.");

                final Wrapper wrapper = new Wrapper();
                Object element = input.mergeObject(wrapper, strategy.OBJECT_SCHEMA);
                if (!graph || !((GraphInput) input).isCurrentMessageReference())
                    element = wrapper.value;

                try
                {
                    fCopiesList_n.setInt(collection, n);
                    fCopiesList_element.set(collection, element);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                ret = collection;
                break;
            }

            case ID_UNMODIFIABLE_COLLECTION:
                ret = readUnmodifiableCollectionFrom(input, schema, owner,
                        strategy, graph, iUnmodifiableCollection.newInstance(),
                        false, false);
                break;
            case ID_UNMODIFIABLE_SET:
                ret = readUnmodifiableCollectionFrom(input, schema, owner,
                        strategy, graph, iUnmodifiableSet.newInstance(), false,
                        false);
                break;
            case ID_UNMODIFIABLE_SORTED_SET:
                ret = readUnmodifiableCollectionFrom(input, schema, owner,
                        strategy, graph, iUnmodifiableSortedSet.newInstance(),
                        true, false);
                break;
            case ID_UNMODIFIABLE_LIST:
                ret = readUnmodifiableCollectionFrom(input, schema, owner,
                        strategy, graph, iUnmodifiableList.newInstance(), false,
                        true);
                break;
            case ID_UNMODIFIABLE_RANDOM_ACCESS_LIST:
                ret = readUnmodifiableCollectionFrom(input, schema, owner,
                        strategy, graph,
                        iUnmodifiableRandomAccessList.newInstance(), false, true);
                break;

            case ID_SYNCHRONIZED_COLLECTION:
                ret = readSynchronizedCollectionFrom(input, schema, owner,
                        strategy, graph, iSynchronizedCollection.newInstance(),
                        false, false);
                break;
            case ID_SYNCHRONIZED_SET:
                ret = readSynchronizedCollectionFrom(input, schema, owner,
                        strategy, graph, iSynchronizedSet.newInstance(), false,
                        false);
                break;
            case ID_SYNCHRONIZED_SORTED_SET:
                ret = readSynchronizedCollectionFrom(input, schema, owner,
                        strategy, graph, iSynchronizedSortedSet.newInstance(),
                        true, false);
                break;
            case ID_SYNCHRONIZED_LIST:
                ret = readSynchronizedCollectionFrom(input, schema, owner,
                        strategy, graph, iSynchronizedList.newInstance(), false,
                        true);
                break;
            case ID_SYNCHRONIZED_RANDOM_ACCESS_LIST:
                ret = readSynchronizedCollectionFrom(input, schema, owner,
                        strategy, graph,
                        iSynchronizedRandomAccessList.newInstance(), false, true);
                break;

            case ID_CHECKED_COLLECTION:
                ret = readCheckedCollectionFrom(input, schema, owner, strategy,
                        graph, iCheckedCollection.newInstance(), false, false);
                break;
            case ID_CHECKED_SET:
                ret = readCheckedCollectionFrom(input, schema, owner, strategy,
                        graph, iCheckedSet.newInstance(), false, false);
                break;
            case ID_CHECKED_SORTED_SET:
                ret = readCheckedCollectionFrom(input, schema, owner, strategy,
                        graph, iCheckedSortedSet.newInstance(), true, false);
                break;
            case ID_CHECKED_LIST:
                ret = readCheckedCollectionFrom(input, schema, owner, strategy,
                        graph, iCheckedList.newInstance(), false, true);
                break;
            case ID_CHECKED_RANDOM_ACCESS_LIST:
                ret = readCheckedCollectionFrom(input, schema, owner, strategy,
                        graph, iCheckedRandomAccessList.newInstance(), false, true);
                break;

            case ID_ENUM_SET:
            {
                final Collection<?> es = strategy.resolveEnumFrom(input)
                        .newEnumSet();

                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(es, owner);
                }

                // TODO enum schema
                strategy.COLLECTION_SCHEMA
                        .mergeFrom(input, (Collection<Object>) es);
                return es;
            }

            case ID_COLLECTION:
            {
                final Collection<Object> collection = strategy
                        .resolveCollectionFrom(input).newMessage();

                if (graph)
                {
                    // update the actual reference.
                    ((GraphInput) input).updateLast(collection, owner);
                }

                strategy.COLLECTION_SCHEMA.mergeFrom(input, collection);

                return collection;
            }

            default:
                throw new ProtostuffException("Corrupt input.");
        }

        if (0 != input.readFieldNumber(schema))
            throw new ProtostuffException("Corrupt input.");

        return ret;
    }

    private static Object readUnmodifiableCollectionFrom(Input input,
            Schema<?> schema, Object owner, IdStrategy strategy, boolean graph,
            Object collection, boolean ss, boolean list) throws IOException
    {
        if (graph)
        {
            // update the actual reference.
            ((GraphInput) input).updateLast(collection, owner);
        }

        final Wrapper wrapper = new Wrapper();
        Object c = input.mergeObject(wrapper,
                strategy.POLYMORPHIC_COLLECTION_SCHEMA);
        if (!graph || !((GraphInput) input).isCurrentMessageReference())
            c = wrapper.value;
        try
        {
            fUnmodifiableCollection_c.set(collection, c);

            if (ss)
                fUnmodifiableSortedSet_ss.set(collection, c);

            if (list)
                fUnmodifiableList_list.set(collection, c);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return collection;
    }

    private static Object readSynchronizedCollectionFrom(Input input,
            Schema<?> schema, Object owner, IdStrategy strategy, boolean graph,
            Object collection, boolean ss, boolean list) throws IOException
    {
        if (graph)
        {
            // update the actual reference.
            ((GraphInput) input).updateLast(collection, owner);
        }

        final Wrapper wrapper = new Wrapper();
        Object c = input.mergeObject(wrapper,
                strategy.POLYMORPHIC_COLLECTION_SCHEMA);
        if (!graph || !((GraphInput) input).isCurrentMessageReference())
            c = wrapper.value;
        try
        {
            fSynchronizedCollection_c.set(collection, c);
            // mutex is the object itself.
            fSynchronizedCollection_mutex.set(collection, collection);

            if (ss)
                fSynchronizedSortedSet_ss.set(collection, c);

            if (list)
                fSynchronizedList_list.set(collection, c);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return collection;
    }

    private static Object readCheckedCollectionFrom(Input input,
            Schema<?> schema, Object owner, IdStrategy strategy, boolean graph,
            Object collection, boolean ss, boolean list) throws IOException
    {
        if (graph)
        {
            // update the actual reference.
            ((GraphInput) input).updateLast(collection, owner);
        }

        final Wrapper wrapper = new Wrapper();
        Object c = input.mergeObject(wrapper,
                strategy.POLYMORPHIC_COLLECTION_SCHEMA);
        if (!graph || !((GraphInput) input).isCurrentMessageReference())
            c = wrapper.value;

        if (1 != input.readFieldNumber(schema))
            throw new ProtostuffException("Corrupt input.");

        Object type = input.mergeObject(wrapper, strategy.CLASS_SCHEMA);
        if (!graph || !((GraphInput) input).isCurrentMessageReference())
            type = wrapper.value;
        try
        {
            fCheckedCollection_c.set(collection, c);
            fCheckedCollection_type.set(collection, type);

            if (ss)
                fCheckedSortedSet_ss.set(collection, c);

            if (list)
                fCheckedList_list.set(collection, c);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return collection;
    }
    
    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy) throws IOException
    {
        transferObject(pipeSchema, pipe, input, output, strategy, 
                input.readFieldNumber(pipeSchema.wrappedSchema));
    }

    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy, 
            final int number) throws IOException
    {
        switch (number)
        {
            case ID_EMPTY_SET:
                output.writeUInt32(number, input.readUInt32(), false);
                break;

            case ID_EMPTY_LIST:
                output.writeUInt32(number, input.readUInt32(), false);
                break;

            case ID_SINGLETON_SET:
            case ID_SINGLETON_LIST:
            {
                output.writeUInt32(number, input.readUInt32(), false);

                final int next = input.readFieldNumber(pipeSchema.wrappedSchema);
                if (next == 0)
                {
                    // null element
                    return;
                }

                if (next != 1)
                    throw new ProtostuffException("Corrupt input.");

                output.writeObject(1, pipe, strategy.OBJECT_PIPE_SCHEMA, false);
                break;
            }
            case ID_SET_FROM_MAP:
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                break;

            case ID_COPIES_LIST:
            {
                output.writeUInt32(number, input.readUInt32(), false);

                if (1 != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");

                // size
                output.writeUInt32(1, input.readUInt32(), false);

                final int next = input.readFieldNumber(pipeSchema.wrappedSchema);
                if (next == 0)
                {
                    // null element
                    return;
                }

                if (next != 2)
                    throw new ProtostuffException("Corrupt input.");

                output.writeObject(2, pipe, strategy.OBJECT_PIPE_SCHEMA, false);
                break;
            }
            case ID_UNMODIFIABLE_COLLECTION:
            case ID_UNMODIFIABLE_SET:
            case ID_UNMODIFIABLE_SORTED_SET:
            case ID_UNMODIFIABLE_LIST:
            case ID_UNMODIFIABLE_RANDOM_ACCESS_LIST:
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, false);
                break;

            case ID_SYNCHRONIZED_COLLECTION:
            case ID_SYNCHRONIZED_SET:
            case ID_SYNCHRONIZED_SORTED_SET:
            case ID_SYNCHRONIZED_LIST:
            case ID_SYNCHRONIZED_RANDOM_ACCESS_LIST:
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, false);
                break;

            case ID_CHECKED_COLLECTION:
            case ID_CHECKED_SET:
            case ID_CHECKED_SORTED_SET:
            case ID_CHECKED_LIST:
            case ID_CHECKED_RANDOM_ACCESS_LIST:
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_COLLECTION_PIPE_SCHEMA, false);

                if (1 != input.readFieldNumber(pipeSchema.wrappedSchema))
                    throw new ProtostuffException("Corrupt input.");

                output.writeObject(1, pipe, strategy.CLASS_PIPE_SCHEMA, false);
                break;

            case ID_ENUM_SET:
                strategy.transferEnumId(input, output, number);

                if (output instanceof StatefulOutput)
                {
                    // update using the derived schema.
                    ((StatefulOutput) output).updateLast(
                            strategy.COLLECTION_PIPE_SCHEMA, pipeSchema);
                }

                // TODO use enum schema
                Pipe.transferDirect(strategy.COLLECTION_PIPE_SCHEMA, pipe, input,
                        output);
                return;
            case ID_COLLECTION:
                strategy.transferCollectionId(input, output, number);

                if (output instanceof StatefulOutput)
                {
                    // update using the derived schema.
                    ((StatefulOutput) output).updateLast(
                            strategy.COLLECTION_PIPE_SCHEMA, pipeSchema);
                }

                Pipe.transferDirect(strategy.COLLECTION_PIPE_SCHEMA, pipe, input,
                        output);
                return;
            default:
                throw new ProtostuffException("Corrupt input.");
        }

        if (0 != input.readFieldNumber(pipeSchema.wrappedSchema))
            throw new ProtostuffException("Corrupt input.");
    }
}
