//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;

/**
 * Test for runtime collection fields with {@link CollectionSchema}.
 *
 * @author David Yu
 * @created Jan 26, 2011
 */
public abstract class AbstractRuntimeCollectionSchemaTest extends AbstractTest
{
    
    static
    {
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", 
                "true");
    }
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    protected abstract <T> byte[] toByteArray(T message, Schema<T> schema);
    
    protected abstract <T> void writeTo(OutputStream out, T message, Schema<T> schema)
            throws IOException;
    
    /**
     * Deserializes from the byte array.
     */
    protected abstract <T> void mergeFrom(byte[] data, int offset, int length, 
            T message, Schema<T> schema) throws IOException;
    
    /**
     * Deserializes from the inputstream.
     */
    protected abstract <T> void mergeFrom(InputStream in, T message, Schema<T> schema) 
            throws IOException;
    
    
    protected abstract <T> void roundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception;
    
    
    public static class PojoFoo
    {
        String name;
        PojoBar bar;
        Collection<String> collection;
        List<String> list;
        ArrayList<String> arrayList;
        LinkedList<String> linkedList;
        CopyOnWriteArrayList<String> copyOnWriteArrayList;
        Stack<String> stack;
        Vector<String> vector;
        Set<String> set;
        HashSet<String> hashSet;
        LinkedHashSet<String> linkedHashSet;
        SortedSet<String> sortedSet;
        NavigableSet<String> navigableSet;
        TreeSet<String> treeSet;
        ConcurrentSkipListSet<String> concurrentSkipListSet;
        CopyOnWriteArraySet<String> copyOnWriteArraySet;
        Queue<String> queue;
        BlockingQueue<String> blockingQueue;
        LinkedBlockingQueue<String> linkedBlockingQueue;
        Deque<String> deque;
        BlockingDeque<String> blockingDeque;
        LinkedBlockingDeque<String> linkedBlockingDeque;
        ArrayBlockingQueue<String> arrayBlockingQueue;
        ArrayDeque<String> arrayDeque;
        ConcurrentLinkedQueue<String> concurrentLinkedQueue;
        PriorityBlockingQueue<String> priorityBlockingQueue;
        PriorityQueue<String> priorityQueue;
        
        PojoFoo fill()
        {
            name = getClass().getSimpleName();
            
            bar = new PojoBar().fill();
            
            collection = new ArrayList<String>();
            collection.add("1");
            
            list = new ArrayList<String>();
            list.add("2");
            
            arrayList = new ArrayList<String>();
            arrayList.add("3");
            
            linkedList = new LinkedList<String>();
            linkedList.add("4");
            
            copyOnWriteArrayList = new CopyOnWriteArrayList<String>();
            copyOnWriteArrayList.add("5");
            
            stack = new Stack<String>();
            stack.add("6");
            
            vector = new Vector<String>();
            vector.add("7");
            
            set = new HashSet<String>();
            set.add("8");
            
            hashSet = new HashSet<String>();
            hashSet.add("9");
            
            linkedHashSet = new LinkedHashSet<String>();
            linkedHashSet.add("10");
            
            sortedSet = new TreeSet<String>();
            sortedSet.add("11");
            
            navigableSet = new TreeSet<String>();
            navigableSet.add("12");
            
            treeSet = new TreeSet<String>();
            treeSet.add("13");
            
            concurrentSkipListSet = new ConcurrentSkipListSet<String>();
            concurrentSkipListSet.add("14");
            
            copyOnWriteArraySet = new CopyOnWriteArraySet<String>();
            copyOnWriteArraySet.add("15");
            
            queue = new LinkedList<String>();
            queue.add("16");
            
            /*blockingQueue = new LinkedBlockingQueue<String>();
            blockingQueue.add("17");
            
            linkedBlockingQueue = new LinkedBlockingQueue<String>();
            linkedBlockingQueue.add("18");*/
            
            deque = new LinkedList<String>();
            deque.add("19");
            
            /*blockingDeque = new LinkedBlockingDeque<String>();
            blockingDeque.add("20");
            
            linkedBlockingDeque = new LinkedBlockingDeque<String>();
            linkedBlockingDeque.add("21");
            
            arrayBlockingQueue = new ArrayBlockingQueue<String>(5);
            arrayBlockingQueue.add("22");
            
            arrayDeque = new ArrayDeque<String>();
            arrayDeque.add("23");
            
            concurrentLinkedQueue = new ConcurrentLinkedQueue<String>();
            concurrentLinkedQueue.add("24");
            
            /*priorityBlockingQueue = new PriorityBlockingQueue<String>();
            priorityBlockingQueue.add("25");
            
            priorityQueue = new PriorityQueue<String>();
            priorityQueue.add("26");*/
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((arrayBlockingQueue == null)?0:arrayBlockingQueue.hashCode());
            result = prime * result + ((arrayDeque == null)?0:arrayDeque.hashCode());
            result = prime * result + ((arrayList == null)?0:arrayList.hashCode());
            result = prime * result + ((bar == null)?0:bar.hashCode());
            result = prime * result + ((blockingDeque == null)?0:blockingDeque.hashCode());
            result = prime * result + ((blockingQueue == null)?0:blockingQueue.hashCode());
            result = prime * result + ((collection == null)?0:collection.hashCode());
            result = prime * result + ((concurrentLinkedQueue == null)?0:concurrentLinkedQueue.hashCode());
            result = prime * result + ((concurrentSkipListSet == null)?0:concurrentSkipListSet.hashCode());
            result = prime * result + ((copyOnWriteArrayList == null)?0:copyOnWriteArrayList.hashCode());
            result = prime * result + ((copyOnWriteArraySet == null)?0:copyOnWriteArraySet.hashCode());
            result = prime * result + ((deque == null)?0:deque.hashCode());
            result = prime * result + ((hashSet == null)?0:hashSet.hashCode());
            result = prime * result + ((linkedBlockingDeque == null)?0:linkedBlockingDeque.hashCode());
            result = prime * result + ((linkedBlockingQueue == null)?0:linkedBlockingQueue.hashCode());
            result = prime * result + ((linkedHashSet == null)?0:linkedHashSet.hashCode());
            result = prime * result + ((linkedList == null)?0:linkedList.hashCode());
            result = prime * result + ((list == null)?0:list.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((navigableSet == null)?0:navigableSet.hashCode());
            result = prime * result + ((priorityBlockingQueue == null)?0:priorityBlockingQueue.hashCode());
            result = prime * result + ((priorityQueue == null)?0:priorityQueue.hashCode());
            result = prime * result + ((queue == null)?0:queue.hashCode());
            result = prime * result + ((set == null)?0:set.hashCode());
            result = prime * result + ((sortedSet == null)?0:sortedSet.hashCode());
            result = prime * result + ((stack == null)?0:stack.hashCode());
            result = prime * result + ((treeSet == null)?0:treeSet.hashCode());
            result = prime * result + ((vector == null)?0:vector.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoFoo other = (PojoFoo)obj;
            if (arrayBlockingQueue == null)
            {
                if (other.arrayBlockingQueue != null)
                    return false;
            }
            else if (!arrayBlockingQueue.equals(other.arrayBlockingQueue))
                return false;
            if (arrayDeque == null)
            {
                if (other.arrayDeque != null)
                    return false;
            }
            else if (!arrayDeque.equals(other.arrayDeque))
                return false;
            if (arrayList == null)
            {
                if (other.arrayList != null)
                    return false;
            }
            else if (!arrayList.equals(other.arrayList))
                return false;
            if (bar == null)
            {
                if (other.bar != null)
                    return false;
            }
            else if (!bar.equals(other.bar))
                return false;
            if (blockingDeque == null)
            {
                if (other.blockingDeque != null)
                    return false;
            }
            else if (!blockingDeque.equals(other.blockingDeque))
                return false;
            if (blockingQueue == null)
            {
                if (other.blockingQueue != null)
                    return false;
            }
            else if (!blockingQueue.equals(other.blockingQueue))
                return false;
            if (collection == null)
            {
                if (other.collection != null)
                    return false;
            }
            else if (!collection.equals(other.collection))
                return false;
            if (concurrentLinkedQueue == null)
            {
                if (other.concurrentLinkedQueue != null)
                    return false;
            }
            else if (!concurrentLinkedQueue.equals(other.concurrentLinkedQueue))
                return false;
            if (concurrentSkipListSet == null)
            {
                if (other.concurrentSkipListSet != null)
                    return false;
            }
            else if (!concurrentSkipListSet.equals(other.concurrentSkipListSet))
                return false;
            if (copyOnWriteArrayList == null)
            {
                if (other.copyOnWriteArrayList != null)
                    return false;
            }
            else if (!copyOnWriteArrayList.equals(other.copyOnWriteArrayList))
                return false;
            if (copyOnWriteArraySet == null)
            {
                if (other.copyOnWriteArraySet != null)
                    return false;
            }
            else if (!copyOnWriteArraySet.equals(other.copyOnWriteArraySet))
                return false;
            if (deque == null)
            {
                if (other.deque != null)
                    return false;
            }
            else if (!deque.equals(other.deque))
                return false;
            if (hashSet == null)
            {
                if (other.hashSet != null)
                    return false;
            }
            else if (!hashSet.equals(other.hashSet))
                return false;
            if (linkedBlockingDeque == null)
            {
                if (other.linkedBlockingDeque != null)
                    return false;
            }
            else if (!linkedBlockingDeque.equals(other.linkedBlockingDeque))
                return false;
            if (linkedBlockingQueue == null)
            {
                if (other.linkedBlockingQueue != null)
                    return false;
            }
            else if (!linkedBlockingQueue.equals(other.linkedBlockingQueue))
                return false;
            if (linkedHashSet == null)
            {
                if (other.linkedHashSet != null)
                    return false;
            }
            else if (!linkedHashSet.equals(other.linkedHashSet))
                return false;
            if (linkedList == null)
            {
                if (other.linkedList != null)
                    return false;
            }
            else if (!linkedList.equals(other.linkedList))
                return false;
            if (list == null)
            {
                if (other.list != null)
                    return false;
            }
            else if (!list.equals(other.list))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (navigableSet == null)
            {
                if (other.navigableSet != null)
                    return false;
            }
            else if (!navigableSet.equals(other.navigableSet))
                return false;
            if (priorityBlockingQueue == null)
            {
                if (other.priorityBlockingQueue != null)
                    return false;
            }
            else if (!priorityBlockingQueue.equals(other.priorityBlockingQueue))
                return false;
            if (priorityQueue == null)
            {
                if (other.priorityQueue != null)
                    return false;
            }
            else if (!priorityQueue.equals(other.priorityQueue))
                return false;
            if (queue == null)
            {
                if (other.queue != null)
                    return false;
            }
            else if (!queue.equals(other.queue))
                return false;
            if (set == null)
            {
                if (other.set != null)
                    return false;
            }
            else if (!set.equals(other.set))
                return false;
            if (sortedSet == null)
            {
                if (other.sortedSet != null)
                    return false;
            }
            else if (!sortedSet.equals(other.sortedSet))
                return false;
            if (stack == null)
            {
                if (other.stack != null)
                    return false;
            }
            else if (!stack.equals(other.stack))
                return false;
            if (treeSet == null)
            {
                if (other.treeSet != null)
                    return false;
            }
            else if (!treeSet.equals(other.treeSet))
                return false;
            if (vector == null)
            {
                if (other.vector != null)
                    return false;
            }
            else if (!vector.equals(other.vector))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PojoFoo [arrayBlockingQueue=" + arrayBlockingQueue + ", arrayDeque=" + arrayDeque + ", arrayList=" + arrayList + ", bar=" + bar
                    + ", blockingDeque=" + blockingDeque + ", blockingQueue=" + blockingQueue + ", collection=" + collection + ", concurrentLinkedQueue="
                    + concurrentLinkedQueue + ", concurrentSkipListSet=" + concurrentSkipListSet + ", copyOnWriteArrayList=" + copyOnWriteArrayList
                    + ", copyOnWriteArraySet=" + copyOnWriteArraySet + ", deque=" + deque + ", hashSet=" + hashSet + ", linkedBlockingDeque="
                    + linkedBlockingDeque + ", linkedBlockingQueue=" + linkedBlockingQueue + ", linkedHashSet=" + linkedHashSet + ", linkedList=" + linkedList
                    + ", list=" + list + ", name=" + name + ", navigableSet=" + navigableSet + ", priorityBlockingQueue=" + priorityBlockingQueue
                    + ", priorityQueue=" + priorityQueue + ", queue=" + queue + ", set=" + set + ", sortedSet=" + sortedSet + ", stack=" + stack + ", treeSet="
                    + treeSet + ", vector=" + vector + "]";
        }
        
    }
    
    public enum Gender
    {
        MALE,
        FEMALE
    }
    
    
    public static class PojoBar
    {
        
        Map<String,String> map;
        SortedMap<String,String> sortedMap;
        NavigableMap<String,String> navigableMap;
        HashMap<String,String> hashMap;
        LinkedHashMap<String,String> linkedHashMap;
        TreeMap<String,String> treeMap;
        WeakHashMap<String,String> weakHashMap;
        IdentityHashMap<Gender,Gender> identityHashMap;
        Hashtable<String,String> hashtable;
        ConcurrentMap<String,String> concurrentMap;
        ConcurrentHashMap<String,String> concurrentHashMap;
        ConcurrentNavigableMap<String,String> concurrentNavigableMap;
        ConcurrentSkipListMap<String,String> concurrentSkipListMap;
        
        
        PojoBar fill()
        {
            map = new LinkedHashMap<String,String>();
            map.put("1", "2");
            
            sortedMap = new TreeMap<String,String>();
            sortedMap.put("3", "4");
            
            navigableMap = new TreeMap<String,String>();
            navigableMap.put("5", "6");
            
            hashMap = new HashMap<String,String>();
            hashMap.put("7", "8");
            
            linkedHashMap = new LinkedHashMap<String,String>();
            linkedHashMap.put("9", "10");
            
            treeMap = new TreeMap<String,String>();
            treeMap.put("11", "12");
            
            //weakHashMap = new WeakHashMap<String,String>();
            //weakHashMap.put("13", "14");
            
            identityHashMap = new IdentityHashMap<Gender,Gender>();
            identityHashMap.put(Gender.MALE, Gender.FEMALE);
            
            hashtable = new Hashtable<String,String>();
            hashtable.put("15", "16");
            
            concurrentMap = new ConcurrentHashMap<String,String>();
            concurrentMap.put("17", "18");
            
            concurrentHashMap = new ConcurrentHashMap<String,String>();
            concurrentHashMap.put("19", "20");
            
            concurrentNavigableMap = new ConcurrentSkipListMap<String,String>();
            concurrentNavigableMap.put("21", "22");
            
            concurrentSkipListMap = new ConcurrentSkipListMap<String,String>();
            concurrentSkipListMap.put("23", "24");
            
            return this;
        }


        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((concurrentHashMap == null)?0:concurrentHashMap.hashCode());
            result = prime * result + ((concurrentMap == null)?0:concurrentMap.hashCode());
            result = prime * result + ((concurrentNavigableMap == null)?0:concurrentNavigableMap.hashCode());
            result = prime * result + ((concurrentSkipListMap == null)?0:concurrentSkipListMap.hashCode());
            result = prime * result + ((hashMap == null)?0:hashMap.hashCode());
            result = prime * result + ((hashtable == null)?0:hashtable.hashCode());
            result = prime * result + ((identityHashMap == null)?0:identityHashMap.hashCode());
            result = prime * result + ((linkedHashMap == null)?0:linkedHashMap.hashCode());
            result = prime * result + ((map == null)?0:map.hashCode());
            result = prime * result + ((navigableMap == null)?0:navigableMap.hashCode());
            result = prime * result + ((sortedMap == null)?0:sortedMap.hashCode());
            result = prime * result + ((treeMap == null)?0:treeMap.hashCode());
            result = prime * result + ((weakHashMap == null)?0:weakHashMap.hashCode());
            return result;
        }


        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoBar other = (PojoBar)obj;
            if (concurrentHashMap == null)
            {
                if (other.concurrentHashMap != null)
                    return false;
            }
            else if (!concurrentHashMap.equals(other.concurrentHashMap))
                return false;
            if (concurrentMap == null)
            {
                if (other.concurrentMap != null)
                    return false;
            }
            else if (!concurrentMap.equals(other.concurrentMap))
                return false;
            if (concurrentNavigableMap == null)
            {
                if (other.concurrentNavigableMap != null)
                    return false;
            }
            else if (!concurrentNavigableMap.equals(other.concurrentNavigableMap))
                return false;
            if (concurrentSkipListMap == null)
            {
                if (other.concurrentSkipListMap != null)
                    return false;
            }
            else if (!concurrentSkipListMap.equals(other.concurrentSkipListMap))
                return false;
            if (hashMap == null)
            {
                if (other.hashMap != null)
                    return false;
            }
            else if (!hashMap.equals(other.hashMap))
                return false;
            if (hashtable == null)
            {
                if (other.hashtable != null)
                    return false;
            }
            else if (!hashtable.equals(other.hashtable))
                return false;
            if (identityHashMap == null)
            {
                if (other.identityHashMap != null)
                    return false;
            }
            else if (!identityHashMap.equals(other.identityHashMap))
                return false;
            if (linkedHashMap == null)
            {
                if (other.linkedHashMap != null)
                    return false;
            }
            else if (!linkedHashMap.equals(other.linkedHashMap))
                return false;
            if (map == null)
            {
                if (other.map != null)
                    return false;
            }
            else if (!map.equals(other.map))
                return false;
            if (navigableMap == null)
            {
                if (other.navigableMap != null)
                    return false;
            }
            else if (!navigableMap.equals(other.navigableMap))
                return false;
            if (sortedMap == null)
            {
                if (other.sortedMap != null)
                    return false;
            }
            else if (!sortedMap.equals(other.sortedMap))
                return false;
            if (treeMap == null)
            {
                if (other.treeMap != null)
                    return false;
            }
            else if (!treeMap.equals(other.treeMap))
                return false;
            if (weakHashMap == null)
            {
                if (other.weakHashMap != null)
                    return false;
            }
            else if (!weakHashMap.equals(other.weakHashMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PojoBar [concurrentHashMap=" + concurrentHashMap + ", concurrentMap=" + concurrentMap + ", concurrentNavigableMap="
                    + concurrentNavigableMap + ", concurrentSkipListMap=" + concurrentSkipListMap + ", hashMap=" + hashMap + ", hashtable=" + hashtable
                    + ", identityHashMap=" + identityHashMap + ", linkedHashMap=" + linkedHashMap + ", map=" + map + ", navigableMap=" + navigableMap
                    + ", sortedMap=" + sortedMap + ", treeMap=" + treeMap + ", weakHashMap=" + weakHashMap + "]";
        }
    }
    
    public void testCollectionAndMapSchema() throws Exception
    {
        if(!RuntimeSchema.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
        {
            System.err.println("RuntimeSchema.COLLECTION_SCHEMA_ON_REPEATED_FIELDS was not enabled.");
            return;
        }
        
        Schema<PojoFoo> schema = RuntimeSchema.getSchema(PojoFoo.class);
        Pipe.Schema<PojoFoo> pipeSchema = 
            ((MappedSchema<PojoFoo>)schema).pipeSchema;
        
        PojoFoo p = new PojoFoo().fill();

        byte[] data = toByteArray(p, schema);
        
        PojoFoo pFromByteArray = new PojoFoo();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        PojoFoo pFromStream = new PojoFoo();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }

}
