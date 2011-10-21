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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

/**
 * Test ser/deser of pojos with {@link EnumSet} and {@link EnumMap} fields.
 *
 * @author David Yu
 * @created Feb 9, 2011
 */
public class EnumSetAndMapTest extends AbstractTest
{
    
    public enum Sequence
    {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE
    }
    
    public static class PojoWithEnumSet
    {
        String name;
        EnumSet<Sequence> enumSet;
        Collection<EnumSet<Sequence>> collectionWIthEnumSetV;
        Object expectEnumSet;
        Object expectListWithEnumSetV;
        
        PojoWithEnumSet fill()
        {
            name = getClass().getSimpleName();
            
            enumSet = EnumSet.noneOf(Sequence.class);
            enumSet.add(Sequence.TWO);
            enumSet.add(Sequence.FOUR);
            
            ArrayList<EnumSet<Sequence>> list = new ArrayList<EnumSet<Sequence>>();
            list.add(EnumSet.of(Sequence.ONE));
            
            collectionWIthEnumSetV = list;
            
            expectEnumSet = EnumSet.allOf(Sequence.class);
            expectListWithEnumSetV = list;
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((collectionWIthEnumSetV == null)?0:collectionWIthEnumSetV.hashCode());
            result = prime * result + ((enumSet == null)?0:enumSet.hashCode());
            result = prime * result + ((expectEnumSet == null)?0:expectEnumSet.hashCode());
            result = prime * result + ((expectListWithEnumSetV == null)?0:expectListWithEnumSetV.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
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
            PojoWithEnumSet other = (PojoWithEnumSet)obj;
            if (collectionWIthEnumSetV == null)
            {
                if (other.collectionWIthEnumSetV != null)
                    return false;
            }
            else if (!collectionWIthEnumSetV.equals(other.collectionWIthEnumSetV))
                return false;
            if (enumSet == null)
            {
                if (other.enumSet != null)
                    return false;
            }
            else if (!enumSet.equals(other.enumSet))
                return false;
            if (expectEnumSet == null)
            {
                if (other.expectEnumSet != null)
                    return false;
            }
            else if (!expectEnumSet.equals(other.expectEnumSet))
                return false;
            if (expectListWithEnumSetV == null)
            {
                if (other.expectListWithEnumSetV != null)
                    return false;
            }
            else if (!expectListWithEnumSetV.equals(other.expectListWithEnumSetV))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PojoWithEnumSet [collectionWIthEnumSetV=" + collectionWIthEnumSetV + ", enumSet=" + enumSet + ", expectEnumSet=" + expectEnumSet
                    + ", expectListWithEnumSetV=" + expectListWithEnumSetV + ", name=" + name + "]";
        }
        
    }
    
    public static class PojoWithEnumMap
    {
        String name;
        EnumMap<Sequence, Integer> enumMap;
        Map<String,EnumMap<Sequence,Integer>> mapWithStringKEnumMapV;
        Object expectEnumMap;
        Object expectMapWithStringKEnumMapV;
        
        PojoWithEnumMap fill()
        {
            name = getClass().getSimpleName();
            enumMap = new EnumMap<Sequence,Integer>(Sequence.class);
            enumMap.put(Sequence.ONE, 1);
            enumMap.put(Sequence.TWO, 2);
            enumMap.put(Sequence.THREE, 3);
            enumMap.put(Sequence.FOUR, 4);
            enumMap.put(Sequence.FIVE, 5);
            
            EnumMap<Sequence,Integer> oddMap = 
                new EnumMap<Sequence,Integer>(Sequence.class);
            
            oddMap.put(Sequence.ONE, 1);
            oddMap.put(Sequence.THREE, 3);
            oddMap.put(Sequence.FIVE, 5);
            
            HashMap<String,EnumMap<Sequence, Integer>> map = 
                new HashMap<String,EnumMap<Sequence, Integer>>();
            
            map.put("baz", oddMap);
            
            mapWithStringKEnumMapV = map;
            
            EnumMap<Sequence,Integer> evenMap = 
                new EnumMap<Sequence,Integer>(Sequence.class);
            
            evenMap.put(Sequence.TWO, 2);
            evenMap.put(Sequence.FOUR, 4);
            
            expectEnumMap = evenMap;
            
            expectMapWithStringKEnumMapV = map;
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((enumMap == null)?0:enumMap.hashCode());
            result = prime * result + ((expectEnumMap == null)?0:expectEnumMap.hashCode());
            result = prime * result + ((expectMapWithStringKEnumMapV == null)?0:expectMapWithStringKEnumMapV.hashCode());
            result = prime * result + ((mapWithStringKEnumMapV == null)?0:mapWithStringKEnumMapV.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
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
            PojoWithEnumMap other = (PojoWithEnumMap)obj;
            if (enumMap == null)
            {
                if (other.enumMap != null)
                    return false;
            }
            else if (!enumMap.equals(other.enumMap))
                return false;
            if (expectEnumMap == null)
            {
                if (other.expectEnumMap != null)
                    return false;
            }
            else if (!expectEnumMap.equals(other.expectEnumMap))
                return false;
            if (expectMapWithStringKEnumMapV == null)
            {
                if (other.expectMapWithStringKEnumMapV != null)
                    return false;
            }
            else if (!expectMapWithStringKEnumMapV.equals(other.expectMapWithStringKEnumMapV))
                return false;
            if (mapWithStringKEnumMapV == null)
            {
                if (other.mapWithStringKEnumMapV != null)
                    return false;
            }
            else if (!mapWithStringKEnumMapV.equals(other.mapWithStringKEnumMapV))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PojoWithEnumMap [enumMap=" + enumMap + ", expectEnumMap=" + expectEnumMap + ", expectMapWithStringKEnumMapV="
                    + expectMapWithStringKEnumMapV + ", mapWithStringKEnumMapV=" + mapWithStringKEnumMapV + ", name=" + name + "]";
        }
        
    }
    
    
    public void testPojoWithEnumSet() throws Exception
    {
        Schema<PojoWithEnumSet> schema = RuntimeSchema.getSchema(PojoWithEnumSet.class);
        PojoWithEnumSet p = new PojoWithEnumSet().fill();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, buf());
        
        PojoWithEnumSet p2 = new PojoWithEnumSet();
        ProtostuffIOUtil.mergeFrom(data, 0, data.length, p2, schema);
        
        assertEquals(p, p2);
        
        List<PojoWithEnumSet> list = new ArrayList<PojoWithEnumSet>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<PojoWithEnumSet> parsedList = ProtostuffIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }
    
    public void testPojoWithEnumMap() throws Exception
    {
        Schema<PojoWithEnumMap> schema = RuntimeSchema.getSchema(PojoWithEnumMap.class);
        PojoWithEnumMap p = new PojoWithEnumMap().fill();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, buf());
        
        PojoWithEnumMap p2 = new PojoWithEnumMap();
        ProtostuffIOUtil.mergeFrom(data, 0, data.length, p2, schema);
        
        assertEquals(p, p2);
        
        List<PojoWithEnumMap> list = new ArrayList<PojoWithEnumMap>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<PojoWithEnumMap> parsedList = ProtostuffIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }
    
    @SuppressWarnings("rawtypes") // explicitly without generics
    public static class Bean
    {
        
        EnumSet firstEnumSet, secondEnumSet;
        EnumMap firstEnumMap, secondEnumMap;
        
        LinkedHashSet firstSet, secondSet;
        LinkedHashMap firstMap, secondMap;
        
        String name;
    }
    
    static EnumMap<Sequence,EnumSet<Sequence>> newSequenceEnumMap()
    {
        EnumMap<Sequence,EnumSet<Sequence>> firstEnumMap = 
                new EnumMap<Sequence,EnumSet<Sequence>>(Sequence.class);
        
        for(Sequence s : Sequence.values())
        {
            firstEnumMap.put(s, EnumSet.of(s));
        }
        
        return firstEnumMap;
    }
    
    static LinkedHashMap<Sequence,EnumSet<Sequence>> newSequenceMap()
    {
        LinkedHashMap<Sequence,EnumSet<Sequence>> firstEnumMap = 
                new LinkedHashMap<Sequence,EnumSet<Sequence>>();
        
        for(Sequence s : Sequence.values())
            firstEnumMap.put(s, EnumSet.of(s));
        
        return firstEnumMap;
    }
    
    static LinkedHashSet<Sequence> newSequenceSet()
    {
        LinkedHashSet<Sequence> set = new LinkedHashSet<Sequence>();
        
        for(Sequence s : Sequence.values())
            set.add(s);
        
        return set;
    }
    
    static <K,V> Map<K,V> newMap()
    {
        return new HashMap<K,V>();
    }
    
    static Bean fill(Bean bean)
    {
        bean.name = "bean";
        
        bean.firstEnumSet = EnumSet.allOf(Sequence.class);
        
        bean.firstEnumMap = newSequenceEnumMap();
        
        bean.firstSet = newSequenceSet();
        
        bean.firstMap = newSequenceMap();
        
        return bean;
    }
    
    static void verify(Bean bean)
    {
        assertEquals(bean.name, "bean");
        
        assertNotNull(bean.firstEnumSet);
        assertNull(bean.secondEnumSet);
        
        assertEquals(EnumSet.allOf(Sequence.class), bean.firstEnumSet);
        
        assertNotNull(bean.firstEnumMap);
        assertNull(bean.secondEnumMap);
        
        assertTrue(bean.firstEnumMap.size() == Sequence.values().length);
        
        assertEquals(newSequenceEnumMap(), bean.firstEnumMap);
        
        assertNotNull(bean.firstSet);
        assertNull(bean.secondSet);
        assertEquals(newSequenceSet(), bean.firstSet);
        
        assertNotNull(bean.firstMap);
        assertNull(bean.secondMap);
        assertEquals(newSequenceMap(), bean.firstMap);
    }
    
    static Bean fillCyclic(Bean bean)
    {
        bean.name = "bean_cyclic";
        
        bean.firstEnumSet = bean.secondEnumSet = EnumSet.allOf(Sequence.class);
        
        // TODO uncomment
        //bean.firstEnumMap = bean.secondEnumMap = newSequenceEnumMap();
        
        bean.firstSet = bean.secondSet = newSequenceSet();
        
        bean.firstMap = bean.secondMap = newSequenceMap();
        
        return bean;
    }
    
    static void verifyCyclic(Bean bean)
    {
        assertEquals(bean.name, "bean_cyclic");
        
        assertNotNull(bean.firstEnumSet);
        if(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            assertTrue(bean.firstEnumSet == bean.secondEnumSet);
        else
            assertEquals(bean.firstEnumSet, bean.secondEnumSet);
        
        assertEquals(EnumSet.allOf(Sequence.class), bean.firstEnumSet);
        
        /* TODO uncomment
        assertNotNull(bean.firstEnumMap);
        if(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            assertTrue(bean.firstEnumMap == bean.secondEnumMap);
        else
            assertEquals(bean.firstEnumMap, bean.secondEnumMap);
        
        assertEquals(newSequenceMap(), bean.firstEnumMap);*/
        
        assertNotNull(bean.firstSet);
        
        if(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            assertTrue(bean.firstSet == bean.secondSet);
        else
            assertEquals(bean.firstSet, bean.secondSet);
        
        assertEquals(newSequenceSet(), bean.firstSet);
        
        assertNotNull(bean.firstMap);
        
        if(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            assertTrue(bean.firstMap == bean.secondMap);
        else
            assertEquals(bean.firstMap, bean.secondMap);
        
        assertEquals(newSequenceMap(), bean.firstMap);
    }
    
    public void testBean()
    {
        System.err.println(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS);
        
        Bean bean = fill(new Bean());
        
        verify(bean);

        Schema<Bean> schema = RuntimeSchema.getSchema(Bean.class);
        //print(schema);
        byte[] bytes = ProtostuffIOUtil.toByteArray(bean, schema, LinkedBuffer.allocate(256));

        Bean deBean = new Bean();
        ProtostuffIOUtil.mergeFrom(bytes, deBean, schema);
        
        verify(deBean);
    }
    
    public void testBeanCyclic()
    {
        System.err.println(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS);
        
        Bean bean = fillCyclic(new Bean());
        
        verifyCyclic(bean);

        Schema<Bean> schema = RuntimeSchema.getSchema(Bean.class);
        //print(schema);
        byte[] bytes = GraphIOUtil.toByteArray(bean, schema, LinkedBuffer.allocate(256));

        Bean deBean = new Bean();
        GraphIOUtil.mergeFrom(bytes, deBean, schema);
        
        verifyCyclic(deBean);
    }

}
