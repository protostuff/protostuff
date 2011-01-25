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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;

/**
 * Test ser/deser for runtime {@link Map} fields.
 *
 * @author David Yu
 * @created Jan 21, 2011
 */
public abstract class AbstractRuntimeMapTest extends AbstractTest
{
    
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
    
    protected abstract boolean isPolymorphicPojoSupported();
    
    static <K,V> Map<K,V> newMap()
    {
        return new HashMap<K,V>();
    }
    
    static <V> List<V> newList()
    {
        return new ArrayList<V>();
    }
    
    
    public enum Gender
    {
        MALE,
        FEMALE
    }
    
    public enum Sequence
    {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE
    }
    
    public static final class SomePojo
    {
        String name;
        long timestamp;
        final List<String> stringList = newList();
        Map<Integer,Long> map;
        HashMap<Long,Integer> hashMap;
        LinkedHashMap<Float,Double> linkedHashMap;
        TreeMap<Double,Float> treeMap;
        WeakHashMap<Byte,Character> weakHashMap;
        IdentityHashMap<Sequence, Boolean> identityHashMap;
        Hashtable<String,String> hashTable;
        ConcurrentHashMap<String,String> concurrentHashMap;
        ConcurrentSkipListMap<String,String> concurrentSkipListMap;
        
        static SomePojo create(String name)
        {
            SomePojo p = new SomePojo();
            p.name = name;
            p.timestamp = System.currentTimeMillis();

            p.stringList.add("foo");
            p.stringList.add(name);
            
            p.hashMap = new HashMap<Long,Integer>();
            p.hashMap.put(System.currentTimeMillis(), 1);
            p.hashMap.put(null, null);
            
            p.linkedHashMap = new LinkedHashMap<Float,Double>();
            p.linkedHashMap.put(5.5f, 4.4d);
            p.linkedHashMap.put(null, null);
            
            p.treeMap = new TreeMap<Double,Float>();
            p.treeMap.put(324.43d, 234.1f);
            
            p.weakHashMap = new WeakHashMap<Byte,Character>();
            p.weakHashMap.put(new Byte((byte)0x89), 'f');
            p.weakHashMap.put(null, null);
            
            p.identityHashMap = new IdentityHashMap<Sequence, Boolean>();
            p.identityHashMap.put(Sequence.ONE, true);
            
            p.hashTable = new Hashtable<String,String>();
            p.hashTable.put("foo", "bar");
            p.hashTable.put("", "");
            
            p.concurrentHashMap = new ConcurrentHashMap<String,String>();
            p.concurrentHashMap.put("bar", "foo");
            
            p.concurrentSkipListMap = new ConcurrentSkipListMap<String,String>();
            p.concurrentSkipListMap.put("baz", "bar");
            
            return p;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((concurrentHashMap == null)?0:concurrentHashMap.hashCode());
            result = prime * result + ((concurrentSkipListMap == null)?0:concurrentSkipListMap.hashCode());
            result = prime * result + ((hashMap == null)?0:hashMap.hashCode());
            result = prime * result + ((hashTable == null)?0:hashTable.hashCode());
            result = prime * result + ((identityHashMap == null)?0:identityHashMap.hashCode());
            result = prime * result + ((linkedHashMap == null)?0:linkedHashMap.hashCode());
            result = prime * result + ((map == null)?0:map.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((stringList == null)?0:stringList.hashCode());
            result = prime * result + (int)(timestamp ^ (timestamp >>> 32));
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
            SomePojo other = (SomePojo)obj;
            if (concurrentHashMap == null)
            {
                if (other.concurrentHashMap != null)
                    return false;
            }
            else if (!concurrentHashMap.equals(other.concurrentHashMap))
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
            if (hashTable == null)
            {
                if (other.hashTable != null)
                    return false;
            }
            else if (!hashTable.equals(other.hashTable))
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
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (stringList == null)
            {
                if (other.stringList != null)
                    return false;
            }
            else if (!stringList.equals(other.stringList))
                return false;
            if (timestamp != other.timestamp)
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
        
    }
    
    /** 
     * Polymorphic.
     */
    public static abstract class Person
    {
        protected int id;
        protected String name;
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
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
            Person other = (Person)obj;
            if (id != other.id)
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
        
    }
    
    public static class Employee extends Person
    {
        protected double salary;

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            long temp;
            temp = Double.doubleToLongBits(salary);
            result = prime * result + (int)(temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Employee other = (Employee)obj;
            if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
                return false;
            return true;
        }
        
    }
    
    public static class Manager extends Employee
    {
        Map<Date,String> deadlines;
        
        static Manager create(int id, String name, double salary)
        {
            Manager m = new Manager();
            m.id = id;
            m.salary = salary;
            m.name = name;
            m.deadlines = newMap();
            m.deadlines.put(new Date(), "Project Foo");
            
            return m;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((deadlines == null)?0:deadlines.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Manager other = (Manager)obj;
            if (deadlines == null)
            {
                if (other.deadlines != null)
                    return false;
            }
            else if (!deadlines.equals(other.deadlines))
                return false;
            return true;
        }
        
    }
    
    public static class Accountant extends Employee
    {
        boolean certified;
        
        static Accountant create(int id, String name, boolean certified, double salary)
        {
            Accountant a = new Accountant();
            a.id = id;
            a.name = name;
            a.certified = certified;
            a.salary = salary;
            return a;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (certified?1231:1237);
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Accountant other = (Accountant)obj;
            if (certified != other.certified)
                return false;
            return true;
        }
        
    }
    
    public static final class Employer extends Person
    {
        Map<String,Date> partners;
        
        public static Employer create(int id)
        {
            Employer employer = new Employer();
            employer.id = id;
            employer.name = "John Doe";
            
            employer.partners = newMap();
            employer.partners.put("Joe Doe", new Date());
            
            return employer;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((partners == null)?0:partners.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Employer other = (Employer)obj;
            if (partners == null)
            {
                if (other.partners != null)
                    return false;
            }
            else if (!partners.equals(other.partners))
                return false;
            return true;
        }
        
    }
    
    // Root entities to test.
    
    public static class HasMapInlineKEnumV
    {
        String name;
        Map<Date,Gender> allowNullKeyMap;
        Map<Integer,Gender> allowNullValueMap;
        Map<Float,Sequence> allowEmptyMap;
        Map<Short,Gender> allowBothNullMap;
        Map<Byte,Sequence> nullMap;
        
        HasMapInlineKEnumV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(new Date(), Gender.MALE);
            allowNullKeyMap.put(null, Gender.FEMALE);
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(1, Gender.MALE);
            allowNullValueMap.put(2, null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapInlineKEnumV other = (HasMapInlineKEnumV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapInlineKEnumV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    public static class HasMapInlineKInlineV
    {
        
        String name;
        
        Map<String,Integer> allowNullKeyMap;
        Map<Character,Byte> allowNullValueMap;
        Map<Integer,Long> allowBothNullMap;
        Map<Float,Double> allowEmptyMap;
        Map<Short,Boolean> nullMap;
        
        HasMapInlineKInlineV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put("1", 1);
            allowNullKeyMap.put(null, 2);
            
            allowNullValueMap = newMap();
            allowNullValueMap.put('a', new Byte((byte)0));
            allowNullValueMap.put('2', null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapInlineKInlineV other = (HasMapInlineKInlineV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapInlineKInlineV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    
    public static class HasMapInlineKPojoV
    {
        String name;
        Map<String,SomePojo> allowNullKeyMap;
        Map<Integer,SomePojo> allowNullValueMap;
        Map<Float,SomePojo> allowEmptyMap;
        Map<Short,SomePojo> allowBothNullMap;
        Map<Byte,SomePojo> nullMap;
        
        HasMapInlineKPojoV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put("1", SomePojo.create("foo"));
            allowNullKeyMap.put(null, SomePojo.create("bar"));
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(1, SomePojo.create("baz"));
            allowNullValueMap.put(2, null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapInlineKPojoV other = (HasMapInlineKPojoV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapInlineKPojoV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    
    public static class HasMapInlineKPolymorphicV
    {
        String name;
        boolean startup;
        Employer employer;
        Map<Integer,Person> people;
        
        HasMapInlineKPolymorphicV fill()
        {
            name = getClass().getSimpleName();
            startup = true;
            employer = Employer.create(1);
            
            add(Manager.create(2, "Jane Doe", 85000d));
            
            add(Accountant.create(3, "Janice", true, 70000d));
            
            add(Accountant.create(4, "Jake", true, 72000d));
            
            return this;
        }
        
        void add(Person person)
        {
            if(people == null)
                people = newMap();
            
            people.put(person.id, person);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((employer == null)?0:employer.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((people == null)?0:people.hashCode());
            result = prime * result + (startup?1231:1237);
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
            HasMapInlineKPolymorphicV other = (HasMapInlineKPolymorphicV)obj;
            if (employer == null)
            {
                if (other.employer != null)
                    return false;
            }
            else if (!employer.equals(other.employer))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (people == null)
            {
                if (other.people != null)
                    return false;
            }
            else if (!people.equals(other.people))
                return false;
            if (startup != other.startup)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapInlineKPolymorphicV [employer=" + employer + ", name=" + name + ", people=" + people + ", startup=" + startup + "]";
        }
        
    }
    
    public static class HasMapEnumKEnumV
    {
        String name;
        
        Map<Gender,Gender> allowNullKeyMap;
        Map<Gender,Gender> allowNullValueMap;
        Map<Sequence,Gender> allowEmptyMap;
        Map<Gender,Gender> allowBothNullMap;
        Map<Sequence,Sequence> nullMap;
        
        HasMapEnumKEnumV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(Gender.MALE, Gender.MALE);
            allowNullKeyMap.put(null, Gender.FEMALE);
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(Gender.MALE, Gender.MALE);
            allowNullValueMap.put(Gender.FEMALE, null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapEnumKEnumV other = (HasMapEnumKEnumV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapEnumKEnumV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    
    public static class HasMapEnumKInlineV
    {
        String name;
        
        Map<Gender,Character> allowNullKeyMap;
        Map<Gender,Long> allowNullValueMap;
        Map<Gender,Double> allowEmptyMap;
        Map<Gender,Byte> allowBothNullMap;
        Map<Gender,Integer> nullMap;
        
        HasMapEnumKInlineV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(Gender.MALE, '1');
            allowNullKeyMap.put(null, '2');
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(Gender.MALE, System.currentTimeMillis());
            allowNullValueMap.put(Gender.FEMALE, null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapEnumKInlineV other = (HasMapEnumKInlineV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapEnumKInlineV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    public static class HasMapEnumKPojoV
    {
        String name;
        Map<Gender,SomePojo> allowNullKeyMap;
        Map<Gender,SomePojo> allowNullValueMap;
        Map<Gender,SomePojo> allowEmptyMap;
        Map<Gender,SomePojo> allowBothNullMap;
        Map<Gender,SomePojo> nullMap;
        
        HasMapEnumKPojoV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(Gender.MALE, SomePojo.create("foo"));
            allowNullKeyMap.put(null, SomePojo.create("bar"));
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(Gender.MALE, SomePojo.create("baz"));
            allowNullValueMap.put(Gender.FEMALE, null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapEnumKPojoV other = (HasMapEnumKPojoV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapEnumKPojoV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    public static class HasMapEnumKPolymorphicV
    {
        String name;
        Map<Sequence,Person> people;
        
        HasMapEnumKPolymorphicV fill()
        {
            name = getClass().getSimpleName();
            
            add(Sequence.ONE, Employer.create(1));
            
            add(Sequence.TWO, Manager.create(2, "Jane Doe", 85000d));
            
            add(Sequence.THREE, Accountant.create(3, "Janice", true, 70000d));
            
            add(Sequence.FOUR, Accountant.create(4, "Jake", true, 72000d));
            
            return this;
        }
        
        void add(Sequence seq, Person person)
        {
            if(people == null)
                people = newMap();
            
            people.put(seq, person);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((people == null)?0:people.hashCode());
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
            HasMapEnumKPolymorphicV other = (HasMapEnumKPolymorphicV)obj;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (people == null)
            {
                if (other.people != null)
                    return false;
            }
            else if (!people.equals(other.people))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapEnumKPolymorphicV [name=" + name + ", people=" + people + "]";
        }
        
    }
    
    public static class HasMapPojoKEnumV
    {
        String name;
        Map<SomePojo,Gender> allowNullKeyMap;
        Map<SomePojo,Gender> allowNullValueMap;
        Map<SomePojo,Sequence> allowEmptyMap;
        Map<SomePojo,Gender> allowBothNullMap;
        Map<SomePojo,Sequence> nullMap;
        
        HasMapPojoKEnumV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(SomePojo.create("1"), Gender.MALE);
            allowNullKeyMap.put(null, Gender.FEMALE);
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(SomePojo.create("a"), Gender.MALE);
            allowNullValueMap.put(SomePojo.create("b"), null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapPojoKEnumV other = (HasMapPojoKEnumV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapPojoKEnumV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    public static class HasMapPojoKInlineV
    {
        String name;
        
        Map<SomePojo,Integer> allowNullKeyMap;
        Map<SomePojo,Byte> allowNullValueMap;
        Map<SomePojo,Long> allowBothNullMap;
        Map<SomePojo,BigInteger> allowEmptyMap;
        Map<SomePojo,BigDecimal> nullMap;
        
        HasMapPojoKInlineV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(SomePojo.create("1"), 1);
            allowNullKeyMap.put(null, 2);
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(SomePojo.create("a"), new Byte((byte)0));
            allowNullValueMap.put(SomePojo.create("2"), null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapPojoKInlineV other = (HasMapPojoKInlineV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapPojoKInlineV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    public static class HasMapPojoKPojoV
    {
        String name;
        // Employer is a final class.
        Map<Employer,SomePojo> allowNullKeyMap;
        Map<SomePojo,SomePojo> allowNullValueMap;
        Map<SomePojo,SomePojo> allowEmptyMap;
        Map<SomePojo,Employer> allowBothNullMap;
        Map<SomePojo,SomePojo> nullMap;
        
        HasMapPojoKPojoV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(Employer.create(1), SomePojo.create("1"));
            allowNullKeyMap.put(null, SomePojo.create("2"));
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(SomePojo.create("10"), SomePojo.create("100"));
            allowNullValueMap.put(SomePojo.create("2"), null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapPojoKPojoV other = (HasMapPojoKPojoV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapPojoKPojoV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap=" + allowNullKeyMap
                    + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    public static class HasMapPojoKPolymorphicV
    {
        String name;
        Map<SomePojo,Person> allowNullKeyMap;
        Map<SomePojo,Person> allowNullValueMap;
        Map<SomePojo,Person> allowEmptyMap;
        Map<SomePojo,Person> allowBothNullMap;
        Map<SomePojo,Person> nullMap;
        
        HasMapPojoKPolymorphicV fill()
        {
            name = getClass().getSimpleName();
            
            allowNullKeyMap = newMap();
            allowNullKeyMap.put(SomePojo.create("1"), 
                    Manager.create(1, "Jake", 80000d));
            allowNullKeyMap.put(null, Employer.create(2));
            
            allowNullValueMap = newMap();
            allowNullValueMap.put(SomePojo.create("1"), 
                    Accountant.create(1, "Joy", true, 75000d));
            allowNullValueMap.put(SomePojo.create("2"), null);
            
            allowEmptyMap = newMap();
            
            allowBothNullMap = newMap();
            allowBothNullMap.put(null, null);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((allowBothNullMap == null)?0:allowBothNullMap.hashCode());
            result = prime * result + ((allowEmptyMap == null)?0:allowEmptyMap.hashCode());
            result = prime * result + ((allowNullKeyMap == null)?0:allowNullKeyMap.hashCode());
            result = prime * result + ((allowNullValueMap == null)?0:allowNullValueMap.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((nullMap == null)?0:nullMap.hashCode());
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
            HasMapPojoKPolymorphicV other = (HasMapPojoKPolymorphicV)obj;
            if (allowBothNullMap == null)
            {
                if (other.allowBothNullMap != null)
                    return false;
            }
            else if (!allowBothNullMap.equals(other.allowBothNullMap))
                return false;
            if (allowEmptyMap == null)
            {
                if (other.allowEmptyMap != null)
                    return false;
            }
            else if (!allowEmptyMap.equals(other.allowEmptyMap))
                return false;
            if (allowNullKeyMap == null)
            {
                if (other.allowNullKeyMap != null)
                    return false;
            }
            else if (!allowNullKeyMap.equals(other.allowNullKeyMap))
                return false;
            if (allowNullValueMap == null)
            {
                if (other.allowNullValueMap != null)
                    return false;
            }
            else if (!allowNullValueMap.equals(other.allowNullValueMap))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (nullMap == null)
            {
                if (other.nullMap != null)
                    return false;
            }
            else if (!nullMap.equals(other.nullMap))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "HasMapPojoKPolymorphicV [allowBothNullMap=" + allowBothNullMap + ", allowEmptyMap=" + allowEmptyMap + ", allowNullKeyMap="
                    + allowNullKeyMap + ", allowNullValueMap=" + allowNullValueMap + ", name=" + name + ", nullMap=" + nullMap + "]";
        }
        
    }
    
    // TESTS
    
    public void testInlineKEnumV() throws Exception
    {
        Schema<HasMapInlineKEnumV> schema = RuntimeSchema.getSchema(HasMapInlineKEnumV.class);
        Pipe.Schema<HasMapInlineKEnumV> pipeSchema = 
            ((MappedSchema<HasMapInlineKEnumV>)schema).pipeSchema;
        
        HasMapInlineKEnumV p = new HasMapInlineKEnumV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapInlineKEnumV pFromByteArray = new HasMapInlineKEnumV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapInlineKEnumV pFromStream = new HasMapInlineKEnumV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testInlineKInlineV() throws Exception
    {
        Schema<HasMapInlineKInlineV> schema = RuntimeSchema.getSchema(HasMapInlineKInlineV.class);
        Pipe.Schema<HasMapInlineKInlineV> pipeSchema = 
            ((MappedSchema<HasMapInlineKInlineV>)schema).pipeSchema;
        
        HasMapInlineKInlineV p = new HasMapInlineKInlineV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapInlineKInlineV pFromByteArray = new HasMapInlineKInlineV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapInlineKInlineV pFromStream = new HasMapInlineKInlineV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testInlineKPojoV() throws Exception
    {
        Schema<HasMapInlineKPojoV> schema = RuntimeSchema.getSchema(HasMapInlineKPojoV.class);
        Pipe.Schema<HasMapInlineKPojoV> pipeSchema = 
            ((MappedSchema<HasMapInlineKPojoV>)schema).pipeSchema;
        
        HasMapInlineKPojoV p = new HasMapInlineKPojoV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapInlineKPojoV pFromByteArray = new HasMapInlineKPojoV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapInlineKPojoV pFromStream = new HasMapInlineKPojoV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testInlineKPolymorphicV() throws Exception
    {
        if(!isPolymorphicPojoSupported())
            return;
        
        Schema<HasMapInlineKPolymorphicV> schema = RuntimeSchema.getSchema(HasMapInlineKPolymorphicV.class);
        Pipe.Schema<HasMapInlineKPolymorphicV> pipeSchema = 
            ((MappedSchema<HasMapInlineKPolymorphicV>)schema).pipeSchema;
        
        HasMapInlineKPolymorphicV p = new HasMapInlineKPolymorphicV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapInlineKPolymorphicV pFromByteArray = new HasMapInlineKPolymorphicV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapInlineKPolymorphicV pFromStream = new HasMapInlineKPolymorphicV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testEnumKEnumV() throws Exception
    {
        Schema<HasMapEnumKEnumV> schema = RuntimeSchema.getSchema(HasMapEnumKEnumV.class);
        Pipe.Schema<HasMapEnumKEnumV> pipeSchema = 
            ((MappedSchema<HasMapEnumKEnumV>)schema).pipeSchema;
        
        HasMapEnumKEnumV p = new HasMapEnumKEnumV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapEnumKEnumV pFromByteArray = new HasMapEnumKEnumV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapEnumKEnumV pFromStream = new HasMapEnumKEnumV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testEnumKInlineV() throws Exception
    {
        Schema<HasMapEnumKInlineV> schema = RuntimeSchema.getSchema(HasMapEnumKInlineV.class);
        Pipe.Schema<HasMapEnumKInlineV> pipeSchema = 
            ((MappedSchema<HasMapEnumKInlineV>)schema).pipeSchema;
        
        HasMapEnumKInlineV p = new HasMapEnumKInlineV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapEnumKInlineV pFromByteArray = new HasMapEnumKInlineV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapEnumKInlineV pFromStream = new HasMapEnumKInlineV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testEnumKPojoV() throws Exception
    {
        Schema<HasMapEnumKPojoV> schema = RuntimeSchema.getSchema(HasMapEnumKPojoV.class);
        Pipe.Schema<HasMapEnumKPojoV> pipeSchema = 
            ((MappedSchema<HasMapEnumKPojoV>)schema).pipeSchema;
        
        HasMapEnumKPojoV p = new HasMapEnumKPojoV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapEnumKPojoV pFromByteArray = new HasMapEnumKPojoV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapEnumKPojoV pFromStream = new HasMapEnumKPojoV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testEnumKPolymorphicV() throws Exception
    {
        if(!isPolymorphicPojoSupported())
            return;
        
        Schema<HasMapEnumKPolymorphicV> schema = RuntimeSchema.getSchema(HasMapEnumKPolymorphicV.class);
        Pipe.Schema<HasMapEnumKPolymorphicV> pipeSchema = 
            ((MappedSchema<HasMapEnumKPolymorphicV>)schema).pipeSchema;
        
        HasMapEnumKPolymorphicV p = new HasMapEnumKPolymorphicV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapEnumKPolymorphicV pFromByteArray = new HasMapEnumKPolymorphicV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapEnumKPolymorphicV pFromStream = new HasMapEnumKPolymorphicV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoKEnumV() throws Exception
    {
        Schema<HasMapPojoKEnumV> schema = RuntimeSchema.getSchema(HasMapPojoKEnumV.class);
        Pipe.Schema<HasMapPojoKEnumV> pipeSchema = 
            ((MappedSchema<HasMapPojoKEnumV>)schema).pipeSchema;
        
        HasMapPojoKEnumV p = new HasMapPojoKEnumV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapPojoKEnumV pFromByteArray = new HasMapPojoKEnumV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapPojoKEnumV pFromStream = new HasMapPojoKEnumV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoKInlineV() throws Exception
    {
        Schema<HasMapPojoKInlineV> schema = RuntimeSchema.getSchema(HasMapPojoKInlineV.class);
        Pipe.Schema<HasMapPojoKInlineV> pipeSchema = 
            ((MappedSchema<HasMapPojoKInlineV>)schema).pipeSchema;
        
        HasMapPojoKInlineV p = new HasMapPojoKInlineV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapPojoKInlineV pFromByteArray = new HasMapPojoKInlineV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapPojoKInlineV pFromStream = new HasMapPojoKInlineV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoKPojoV() throws Exception
    {
        Schema<HasMapPojoKPojoV> schema = RuntimeSchema.getSchema(HasMapPojoKPojoV.class);
        Pipe.Schema<HasMapPojoKPojoV> pipeSchema = 
            ((MappedSchema<HasMapPojoKPojoV>)schema).pipeSchema;
        
        HasMapPojoKPojoV p = new HasMapPojoKPojoV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapPojoKPojoV pFromByteArray = new HasMapPojoKPojoV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapPojoKPojoV pFromStream = new HasMapPojoKPojoV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoKPolymorphicV() throws Exception
    {
        if(!isPolymorphicPojoSupported())
            return;
        
        Schema<HasMapPojoKPolymorphicV> schema = RuntimeSchema.getSchema(HasMapPojoKPolymorphicV.class);
        Pipe.Schema<HasMapPojoKPolymorphicV> pipeSchema = 
            ((MappedSchema<HasMapPojoKPolymorphicV>)schema).pipeSchema;
        
        HasMapPojoKPolymorphicV p = new HasMapPojoKPolymorphicV().fill();

        byte[] data = toByteArray(p, schema);
        
        HasMapPojoKPolymorphicV pFromByteArray = new HasMapPojoKPolymorphicV();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        HasMapPojoKPolymorphicV pFromStream = new HasMapPojoKPolymorphicV();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }

}
