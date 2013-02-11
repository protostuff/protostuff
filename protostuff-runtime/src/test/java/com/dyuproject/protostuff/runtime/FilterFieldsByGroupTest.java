//========================================================================
//Copyright 2012 David Yu
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

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.Tag;

/**
 * The fields are filtered dependending on the Tag annotation {@link Tag#groupFilter()}.
 * 
 * @author David Yu
 * @created Feb 11, 2013
 */
public class FilterFieldsByGroupTest extends AbstractTest
{
    
    // power of two (max of 31 bits/groups allowed)
    static final int GROUP1 = 1;
    static final int GROUP2 = 2;
    static final int GROUP3 = 4;
    
    static class Inner
    {
        
        // ============ ALL
        
        @Tag(2)
        public boolean propAll = false;
        
        // ============ GROUP 1 only
        
        @Tag(value = 3, groupFilter = GROUP1) // include
        public boolean prop1a = false;
        
        @Tag(value = 4, groupFilter = -(GROUP2|GROUP3)) // exclude
        public boolean prop1b = false;
        
        // ============ GROUP 2 only
        
        @Tag(value = 5, groupFilter = GROUP2) // include
        public boolean prop2a = false;
        
        @Tag(value = 6, groupFilter = -(GROUP1|GROUP3)) // exclude
        public boolean prop2b = false;
        
        // ============ GROUP 3 only
        
        @Tag(value = 7, groupFilter = GROUP3) // include
        public boolean prop3a = false;
        
        @Tag(value = 8, groupFilter = -(GROUP1|GROUP2)) //exclude
        public boolean prop3b = false;
        
        // ============ GROUP 1 and 2
        
        @Tag(value = 9, groupFilter = GROUP1|GROUP2) // include
        public boolean prop1and2a = false;
        
        @Tag(value = 10, groupFilter = -GROUP3) // exclude
        public boolean prop1and2b = false;
        
        // ============ GROUP 1 and 3
        
        @Tag(value = 11, groupFilter = GROUP1|GROUP3) // include
        public boolean prop1and3a = false;
        
        @Tag(value = 12, groupFilter = -GROUP2) // exclude
        public boolean prop1and3b = false;
        
        // ============ GROUP 2 and 3
        
        @Tag(value = 13, groupFilter = GROUP2|GROUP3) // include
        public boolean prop2and3a = false;
        
        @Tag(value = 14, groupFilter = -GROUP1) // exclude
        public boolean prop2and3b = false;
        
        public Inner fillAll(boolean value)
        {
            propAll = value;
            
            prop1a = value;
            prop1b = value;
            
            prop2a = value;
            prop2b = value;
            
            prop3a = value;
            prop3b = value;
            
            prop1and2a = value;
            prop1and2b = value;
            
            prop1and3a = value;
            prop1and3b = value;
            
            prop2and3a = value;
            prop2and3b = value;
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + (prop1a ? 1231 : 1237);
            result = prime * result + (prop1and2a ? 1231 : 1237);
            result = prime * result + (prop1and2b ? 1231 : 1237);
            result = prime * result + (prop1and3a ? 1231 : 1237);
            result = prime * result + (prop1and3b ? 1231 : 1237);
            result = prime * result + (prop1b ? 1231 : 1237);
            result = prime * result + (prop2a ? 1231 : 1237);
            result = prime * result + (prop2and3a ? 1231 : 1237);
            result = prime * result + (prop2and3b ? 1231 : 1237);
            result = prime * result + (prop2b ? 1231 : 1237);
            result = prime * result + (prop3a ? 1231 : 1237);
            result = prime * result + (prop3b ? 1231 : 1237);
            result = prime * result + (propAll ? 1231 : 1237);
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
            Inner other = (Inner)obj;
            if (prop1a != other.prop1a)
                return false;
            if (prop1and2a != other.prop1and2a)
                return false;
            if (prop1and2b != other.prop1and2b)
                return false;
            if (prop1and3a != other.prop1and3a)
                return false;
            if (prop1and3b != other.prop1and3b)
                return false;
            if (prop1b != other.prop1b)
                return false;
            if (prop2a != other.prop2a)
                return false;
            if (prop2and3a != other.prop2and3a)
                return false;
            if (prop2and3b != other.prop2and3b)
                return false;
            if (prop2b != other.prop2b)
                return false;
            if (prop3a != other.prop3a)
                return false;
            if (prop3b != other.prop3b)
                return false;
            if (propAll != other.propAll)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Inner [propAll=" + propAll + ", prop1a=" + prop1a + ", prop1b=" + prop1b
                    + ", prop2a=" + prop2a + ", prop2b=" + prop2b + ", prop3a=" + prop3a
                    + ", prop3b=" + prop3b + ", prop1and2a=" + prop1and2a + ", prop1and2b="
                    + prop1and2b + ", prop1and3a=" + prop1and3a + ", prop1and3b=" + prop1and3b
                    + ", prop2and3a=" + prop2and3a + ", prop2and3b=" + prop2and3b + "]";
        }
        
        
    }
    
    public void testIt() throws Exception
    {
        // contains all fields
        DefaultIdStrategy primary = new DefaultIdStrategy();
        
        DefaultIdStrategy g1 = new DefaultIdStrategy(primary, GROUP1);
        DefaultIdStrategy g2 = new DefaultIdStrategy(primary, GROUP2);
        DefaultIdStrategy g3 = new DefaultIdStrategy(primary, GROUP3);
        
        verifyPrimary(primary);
        verifyGroup1(g1);
        verifyGroup2(g2);
        verifyGroup3(g3);
    }
    
    static void verifyPrimary(IdStrategy strategy) throws Exception
    {
        Schema<Inner> schema = RuntimeSchema.getSchema(Inner.class, strategy);
        Inner message = new Inner().fillAll(true);
        
        byte[] data = ProtostuffIOUtil.toByteArray(message, schema, buf());
        Inner parsed = new Inner();
        ProtostuffIOUtil.mergeFrom(data, parsed, schema);
        
        assertTrue(message.equals(parsed));
    }
    
    static void verifyGroup1(IdStrategy strategy) throws Exception
    {
        Schema<Inner> schema = RuntimeSchema.getSchema(Inner.class, strategy);
        Inner message = new Inner().fillAll(true);
        
        byte[] data = ProtostuffIOUtil.toByteArray(message, schema, buf());
        Inner parsed = new Inner();
        ProtostuffIOUtil.mergeFrom(data, parsed, schema);
        
        assertTrue(!message.equals(parsed));
        
        assertTrue(message.propAll == parsed.propAll);
        
        assertTrue(message.prop1a == parsed.prop1a);
        assertTrue(message.prop1b == parsed.prop1b);
        
        assertTrue(message.prop2a != parsed.prop2a);
        assertTrue(message.prop2b != parsed.prop2b);
        
        assertTrue(message.prop3a != parsed.prop3a);
        assertTrue(message.prop3b != parsed.prop3b);
        
        assertTrue(message.prop1and2a == parsed.prop1and2a);
        assertTrue(message.prop1and2b == parsed.prop1and2b);
        
        assertTrue(message.prop1and3a == parsed.prop1and3a);
        assertTrue(message.prop1and3b == parsed.prop1and3b);
        
        assertTrue(message.prop2and3a != parsed.prop2and3a);
        assertTrue(message.prop2and3b != parsed.prop2and3b);
    }
    
    static void verifyGroup2(IdStrategy strategy) throws Exception
    {
        Schema<Inner> schema = RuntimeSchema.getSchema(Inner.class, strategy);
        Inner message = new Inner().fillAll(true);
        
        byte[] data = ProtostuffIOUtil.toByteArray(message, schema, buf());
        Inner parsed = new Inner();
        ProtostuffIOUtil.mergeFrom(data, parsed, schema);
        
        assertTrue(!message.equals(parsed));
        
        assertTrue(message.propAll == parsed.propAll);
        
        assertTrue(message.prop1a != parsed.prop1a);
        assertTrue(message.prop1b != parsed.prop1b);
        
        assertTrue(message.prop2a == parsed.prop2a);
        assertTrue(message.prop2b == parsed.prop2b);
        
        assertTrue(message.prop3a != parsed.prop3a);
        assertTrue(message.prop3b != parsed.prop3b);
        
        assertTrue(message.prop1and2a == parsed.prop1and2a);
        assertTrue(message.prop1and2b == parsed.prop1and2b);
        
        assertTrue(message.prop1and3a != parsed.prop1and3a);
        assertTrue(message.prop1and3b != parsed.prop1and3b);
        
        assertTrue(message.prop2and3a == parsed.prop2and3a);
        assertTrue(message.prop2and3b == parsed.prop2and3b);
    }
    
    static void verifyGroup3(IdStrategy strategy) throws Exception
    {
        Schema<Inner> schema = RuntimeSchema.getSchema(Inner.class, strategy);
        Inner message = new Inner().fillAll(true);
        
        byte[] data = ProtostuffIOUtil.toByteArray(message, schema, buf());
        Inner parsed = new Inner();
        ProtostuffIOUtil.mergeFrom(data, parsed, schema);
        
        assertTrue(!message.equals(parsed));
        
        assertTrue(message.propAll == parsed.propAll);
        
        assertTrue(message.prop1a != parsed.prop1a);
        assertTrue(message.prop1b != parsed.prop1b);
        
        assertTrue(message.prop2a != parsed.prop2a);
        assertTrue(message.prop2b != parsed.prop2b);
        
        assertTrue(message.prop3a == parsed.prop3a);
        assertTrue(message.prop3b == parsed.prop3b);
        
        assertTrue(message.prop1and2a != parsed.prop1and2a);
        assertTrue(message.prop1and2b != parsed.prop1and2b);
        
        assertTrue(message.prop1and3a == parsed.prop1and3a);
        assertTrue(message.prop1and3b == parsed.prop1and3b);
        
        assertTrue(message.prop2and3a == parsed.prop2and3a);
        assertTrue(message.prop2and3b == parsed.prop2and3b);
    }

    /*public static void main(String[] args)
    {
        int g = -(GROUP1|GROUP2);
        System.err.println(g);
        int k = ~g;
        System.err.println(k);
        System.err.println(GROUP3&k);
        System.err.println(Integer.toHexString(k));
        
        
        System.err.println("======================");
        int a = GROUP3;
        System.err.println(a);
        int b = ~GROUP3 & 0x7FFFFFFF;
        System.err.println(b);
        System.err.println(GROUP3&b);
        System.err.println(Integer.toHexString(b));
    }*/
}
