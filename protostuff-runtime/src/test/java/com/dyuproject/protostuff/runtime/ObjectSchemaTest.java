//================================================================================
//Copyright (c) 2011, David Yu
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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;

/**
 * Test cyclic ser/deser on fields where the type is dynamic.
 * 
 * @author David Yu
 * @created Oct 20, 2011
 */

public class ObjectSchemaTest extends TestCase
{

    public void testGraph()
    {
        System.err.println(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS);
        
        Bean bean = fill(new Bean());
        
        verify(bean);

        Schema<Bean> schema = RuntimeSchema.getSchema(Bean.class);
        //print(schema);
        byte[] bytes = GraphIOUtil.toByteArray(bean, schema, LinkedBuffer.allocate(256));

        Bean deBean = new Bean();
        GraphIOUtil.mergeFrom(bytes, deBean, schema);
        
        verify(deBean);
    }
    
    static void print(Schema<?> schema)
    {
        int i = 1;
        for(String name = schema.getFieldName(i); name != null; 
                name = schema.getFieldName(++i))
        {
            System.err.println(name);
        }
    }
    
    static Bean fill(Bean bean)
    {
        bean.name = "test";
        
        Item share = new Item("share");
        
        List<Item> firstList = newList(share, new Item("new_a"));
        bean.firstList = firstList;
        
        List<Item> secondList = newList(share, new Item("new_a"));
        bean.secondList = secondList;
        
        Item share2;
        bean.firstItem = bean.secondItem = share2 = new Item("share2");
        
        Item share3 = new Item("share3");
        Map<String,Item> firstMap = newMap();
        firstMap.put("share", share);
        firstMap.put("share3", share3);
        bean.firstMap = firstMap;
        
        Map<String,Item> secondMap = newMap();
        secondMap.put("share", share);
        secondMap.put("share3", share3);
        bean.secondMap = secondMap;
        
        Item share4, share5;
        bean.firstHasName = bean.secondHashname = share4 = new Item("share4");
        
        bean.firstNamed = bean.secondNamed = share5 = new Item("share5");
        
        bean.firstObject = bean.secondObject = new Object();
        
        Item share6 = new Item("share6");
        Map<String,Item> firstStringMap = newMap();
        firstStringMap.put("share", share);
        firstStringMap.put("share6", share6);
        bean.firstStringMap = firstStringMap;
        
        Map<String,Item> secondStringMap = newMap();
        secondStringMap.put("share", share);
        secondStringMap.put("share6", share6);
        bean.secondStringMap = secondStringMap;
        
        Item share7 = new Item("share7");
        Map<HasName,Item> firstHasNameMap = newMap();
        firstHasNameMap.put(share, share);
        firstHasNameMap.put(share7, share7);
        bean.firstHasNameMap = firstHasNameMap;
        
        Map<HasName,Item> secondHasNameMap = newMap();
        secondHasNameMap.put(share, share);
        secondHasNameMap.put(share7, share7);
        bean.secondHasNameMap = secondHasNameMap;
        
        Item share8 = new Item("share8");
        Map<Named,Item> firstNamedMap = newMap();
        firstNamedMap.put(share, share);
        firstNamedMap.put(share8, share8);
        bean.firstNamedMap = firstNamedMap;
        
        Map<Named,Item> secondNamedMap = newMap();
        secondNamedMap.put(share, share);
        secondNamedMap.put(share8, share8);
        bean.secondNamedMap = secondNamedMap;
        
        bean.firstIntArray = bean.secondIntArray = new int[]{1,2,3};
        
        Item share9 = new Item("share9");
        Set<String> set = newSet("share9");
        Map<Set<String>,Item> firstSetMap = newMap();
        firstSetMap.put(set, share9);
        bean.firstSetMap = firstSetMap;
        
        Map<Set<String>,Item> secondSetMap = newMap();
        secondSetMap.put(set, share9);
        bean.secondSetMap = secondSetMap;
        
        Item share10 = new Item("share10");
        List<Order> orderList = newList(Order.ASCENDING, 
                Order.DESCENDING, Order.ASCENDING);
        
        Map<List<Order>,Item> firstListMap = newMap();
        firstListMap.put(orderList, share10);
        bean.firstListMap = firstListMap;
        
        Map<List<Order>,Item> secondListMap = newMap();
        secondListMap.put(orderList, share10);
        bean.secondListMap = secondListMap;
        
        Item share11 = new Item("share11");
        EnumSet<Order> orderEnumSet = EnumSet.allOf(Order.class);
        
        Map<EnumSet<Order>,Item> firstEnumSetMap = newMap();
        firstEnumSetMap.put(orderEnumSet, share11);
        bean.firstEnumSetMap = firstEnumSetMap;
        
        Map<EnumSet<Order>,Item> secondEnumSetMap = newMap();
        secondEnumSetMap.put(orderEnumSet, share11);
        bean.secondEnumSetMap = secondEnumSetMap;
        
        Item share12 = new Item("share12");
        Map<String,Item> itemMap = newMap();
        itemMap.put("share12", share12);
        itemMap.put("share", share);
        
        List<Map<String,?>> firstMapList = newList();
        firstMapList.add(itemMap);
        bean.firstMapList = firstMapList;
        
        List<Map<String,?>> secondMapList = newList();
        secondMapList.add(itemMap);
        bean.secondMapList = secondMapList;
        
        Item share13 = new Item("share13");
        EnumMap<Order,Item> orderMap = new EnumMap<Order,Item>(Order.class);
        orderMap.put(Order.ASCENDING, share13);
        orderMap.put(Order.DESCENDING, share);
        
        List<EnumMap<Order,Item>> firstEnumMapList = newList();
        firstEnumMapList.add(orderMap);
        bean.firstEnumMapList = firstEnumMapList;
        
        List<EnumMap<Order,Item>> secondEnumMapList = newList();
        secondEnumMapList.add(orderMap);
        bean.secondEnumMapList = secondEnumMapList;
        
        Item share14 = new Item("share14");
        bean.itemArray = bean.firstItemArray = bean.secondItemArray = new Item[]{
                share, share2, share3, share4, share5, share6, share7, share8, share9, 
                share10, share11, share12, share13, share14
        };
        
        bean.itemArray2d = new Item[1][];
        bean.itemArray2d[0] = bean.firstItemArray;
        
        bean.itemArrayWrapper = new Object[]{bean.itemArray, bean.itemArray2d, 
                share};
        
        Item share15 = new Item("share15");
        Set<Item> firstSet = newSet(share15);
        bean.firstSet = firstSet;
        
        Set<Item> secondSet = newSet(share15);
        bean.secondSet = secondSet;
        
        IdentityHashMap<Item,Item> identityMap = new IdentityHashMap<Item,Item>();
        identityMap.put(share, share);
        
        bean.identityMap = identityMap;
        
        Item share16 = new Item("share16");
        IdentityHashMap<Order,Item> anotherIdentityMap = new IdentityHashMap<Order,Item>();
        anotherIdentityMap.put(Order.ASCENDING, share16);
        anotherIdentityMap.put(Order.DESCENDING, share);
        
        bean.anotherIdentityMap = anotherIdentityMap;
        
        return bean;
    }
    
    static void verify(Bean bean)
    {
        assertEquals(bean.name, "test");
        
        assertNotNull(bean.firstList);
        assertTrue(bean.firstList.size() == 2);
        assertTrue(bean.firstList.get(0) == bean.secondList.get(0));
        
        assertNotNull(bean.secondItem);
        assertTrue(bean.secondItem == bean.firstItem);
        
        assertNotNull(bean.firstMap);
        assertTrue(bean.firstMap.size() == 2);
        
        assertTrue(bean.firstMap.get("share") == bean.secondMap.get("share"));
        assertTrue(bean.firstMap.get("share3") == bean.secondMap.get("share3"));
        
        assertNotNull(bean.firstHasName);
        assertTrue(bean.firstHasName == bean.secondHashname);
        
        assertNotNull(bean.firstNamed);
        assertTrue(bean.firstNamed == bean.secondNamed);
        
        assertNotNull(bean.firstObject);
        assertTrue(bean.firstObject == bean.secondObject);
        
        assertNotNull(bean.firstStringMap);
        assertTrue(bean.firstStringMap.size() == 2);
        assertTrue(bean.firstStringMap.get("share") == bean.secondStringMap.get("share"));
        assertTrue(bean.firstStringMap.get("share6") == bean.secondStringMap.get("share6"));
        
        assertNotNull(bean.firstHasNameMap);
        assertTrue(bean.firstHasNameMap.size() == 2);
        assertTrue(bean.firstHasNameMap.get("share") == bean.secondHasNameMap.get("share"));
        assertTrue(bean.firstHasNameMap.get("share7") == bean.secondHasNameMap.get("share7"));
        
        assertNotNull(bean.firstNamedMap);
        assertTrue(bean.firstNamedMap.size() == 2);
        assertTrue(bean.firstNamedMap.get("share") == bean.secondNamedMap.get("share"));
        assertTrue(bean.firstNamedMap.get("share8") == bean.secondNamedMap.get("share8"));
        
        assertNotNull(bean.firstIntArray);
        assertTrue(bean.firstIntArray.length == 3);
        assertTrue(bean.firstIntArray == bean.secondIntArray);
        
        assertNotNull(bean.firstSetMap);
        assertTrue(bean.firstSetMap.size() == 1);
        
        assertTrue(bean.firstSetMap.values().iterator().next() == 
                bean.secondSetMap.values().iterator().next());
        assertTrue(bean.firstSetMap.keySet().iterator().next() == 
                bean.secondSetMap.keySet().iterator().next());
        
        assertNotNull(bean.firstListMap);
        assertTrue(bean.firstListMap.size() == 1);
        
        List<Order> orderList = bean.firstListMap.keySet().iterator().next();
        assertTrue(orderList.size() == 3);
        assertTrue(orderList.get(0) == Order.ASCENDING);
        assertTrue(orderList.get(1) == Order.DESCENDING);
        assertTrue(orderList.get(2) == Order.ASCENDING);
        
        assertTrue(orderList == bean.secondListMap.keySet().iterator().next());
        
        assertTrue(bean.firstListMap.values().iterator().next() == 
                bean.secondListMap.values().iterator().next());
        
        assertNotNull(bean.firstEnumSetMap);
        assertTrue(bean.firstEnumSetMap.size() == 1);
        
        assertTrue(bean.firstEnumSetMap.values().iterator().next() == 
                bean.secondEnumSetMap.values().iterator().next());
        assertTrue(bean.firstEnumSetMap.keySet().iterator().next() == 
                bean.secondEnumSetMap.keySet().iterator().next());
        
        assertNotNull(bean.firstMapList);
        assertTrue(bean.firstMapList.size() == 1);
        assertTrue(bean.firstMapList.get(0) == bean.secondMapList.get(0));
        
        assertNotNull(bean.firstEnumMapList);
        assertTrue(bean.firstEnumMapList.size() == 1);
        assertTrue(bean.firstEnumMapList.get(0) == bean.secondEnumMapList.get(0));
        
        assertNotNull(bean.firstItemArray);
        assertTrue(bean.firstItemArray.length == 14);
        
        assertTrue(bean.firstItemArray == bean.secondItemArray);
        assertTrue(bean.firstItemArray == bean.itemArray);
        
        assertEquals(bean.firstItemArray[0].name, "share");
        assertEquals(bean.firstItemArray[1].name, "share2");
        assertEquals(bean.firstItemArray[2].name, "share3");
        assertEquals(bean.firstItemArray[3].name, "share4");
        assertEquals(bean.firstItemArray[4].name, "share5");
        assertEquals(bean.firstItemArray[5].name, "share6");
        assertEquals(bean.firstItemArray[6].name, "share7");
        assertEquals(bean.firstItemArray[7].name, "share8");
        assertEquals(bean.firstItemArray[8].name, "share9");
        assertEquals(bean.firstItemArray[9].name, "share10");
        assertEquals(bean.firstItemArray[10].name, "share11");
        assertEquals(bean.firstItemArray[11].name, "share12");
        assertEquals(bean.firstItemArray[12].name, "share13");
        assertEquals(bean.firstItemArray[13].name, "share14");
        
        assertNotNull(bean.itemArray2d);
        assertTrue(bean.itemArray2d.length == 1);
        assertTrue(bean.itemArray2d[0] == bean.firstItemArray);
        
        assertNotNull(bean.itemArrayWrapper);
        assertTrue(bean.itemArrayWrapper.length == 3);
        assertTrue(bean.itemArrayWrapper[0] == bean.itemArray);
        assertTrue(bean.itemArrayWrapper[1] == bean.itemArray2d);
        assertTrue(bean.itemArrayWrapper[2] == bean.firstItemArray[0]);
        
        assertNotNull(bean.firstSet);
        assertTrue(bean.firstSet.size() == 1);
        assertTrue(bean.firstSet.iterator().next() == bean.secondSet.iterator().next());
        
        assertNotNull(bean.identityMap);
        assertTrue(bean.identityMap.size() == 1);
        assertTrue(bean.identityMap.keySet().iterator().next() == 
                bean.identityMap.values().iterator().next());
        
        Item share = bean.firstItemArray[0];
        assertEquals(share.name, "share");
        assertTrue(bean.identityMap.containsKey(share));
        assertTrue(bean.identityMap.get(share) == share);
        
        assertNotNull(bean.anotherIdentityMap);
        assertTrue(bean.anotherIdentityMap.size() == 2);
        assertEquals(bean.anotherIdentityMap.get(Order.ASCENDING), new Item("share16"));
        assertTrue(bean.anotherIdentityMap.get(Order.DESCENDING) == share);
    }
    
    static <T> Set<T> newSet(T ... ts)
    {
        HashSet<T> set = new HashSet<T>();
        
        for(T t : ts)
            set.add(t);
        
        return set;
    }
    
    static <K,V> Map<K,V> newMap()
    {
        return new HashMap<K,V>();
    }
    
    static <T> Set<T> newSet()
    {
        return new HashSet<T>();
    }
    
    static <T> List<T> newList()
    {
        return new ArrayList<T>();
    }
    
    static <T> List<T> newList(T ... ts)
    {
        ArrayList<T> list = new ArrayList<T>();
        
        for(T t : ts)
            list.add(t);
        
        return list;
    }
    
    static <K,V> Map<K,V> newMap(K key, V value)
    {
        HashMap<K,V> map = new HashMap<K,V>();
        map.put(key, value);
        return map;
    }
    
    enum Order
    {
        ASCENDING,
        DESCENDING
    }

    @SuppressWarnings("rawtypes") // explicitly without generics
    public static class Bean
    {
        public String name;
        
        public List firstList, secondList;
        
        public Object firstItem, secondItem;
        
        public Map firstMap, secondMap;
        
        public HasName firstHasName, secondHashname;
        
        public Named firstNamed, secondNamed;
        
        public Object firstObject, secondObject;
        
        Map<String,?> firstStringMap, secondStringMap;
        
        Map<HasName,?> firstHasNameMap, secondHasNameMap;
        
        Map<Named,?> firstNamedMap, secondNamedMap;
        
        int[] firstIntArray, secondIntArray;
        
        Map<Set<String>,?> firstSetMap, secondSetMap;
        
        Map<List<Order>,?> firstListMap, secondListMap;
        
        Map<EnumSet<Order>,?> firstEnumSetMap, secondEnumSetMap;
        
        List<Map<String,?>> firstMapList, secondMapList;
        
        List<EnumMap<Order,Item>> firstEnumMapList, secondEnumMapList;
        
        Item[] firstItemArray, secondItemArray;
        
        Object itemArray;
        
        Item[][] itemArray2d;
        
        Object[] itemArrayWrapper;
        
        public Set firstSet, secondSet;
        
        public IdentityHashMap identityMap, anotherIdentityMap;
        
    }
    
    public interface HasName
    {
        String getName();
    }
    
    public static abstract class Named
    {
        public abstract String getName();
    }

    public static class Item extends Named implements HasName
    {

        public String name;
        
        public Item()
        {
            
        }

        public Item(String name)
        {
            this.name = name;
        }
        
        public String getName()
        {
            return name;
        }

        public String toString()
        {
            return "name:" + name;
        }

        @Override
        public int hashCode()
        {
            return name.hashCode();
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
            Item other = (Item)obj;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            return true;
        }
        
        
        
    }

}
