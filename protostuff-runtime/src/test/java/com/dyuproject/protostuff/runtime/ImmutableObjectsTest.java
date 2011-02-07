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
import java.util.List;
import java.util.UUID;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

/**
 * Test ser/deser for immutable objects.
 *
 * @author David Yu
 * @created Feb 7, 2011
 */
public class ImmutableObjectsTest extends AbstractTest
{
    
    public static class Pojo
    {
        int id;
        String name;
        ImmutablePojo ip;
        UUID uuid;
        
        public Pojo()
        {
            
        }

        Pojo fill()
        {
            id = 1;
            name = "pojo";
            ip = new ImmutablePojo(2, "ipojo");
            uuid = UUID.randomUUID();
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + ((ip == null)?0:ip.hashCode());
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((uuid == null)?0:uuid.hashCode());
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
            Pojo other = (Pojo)obj;
            if (id != other.id)
                return false;
            if (ip == null)
            {
                if (other.ip != null)
                    return false;
            }
            else if (!ip.equals(other.ip))
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (uuid == null)
            {
                if (other.uuid != null)
                    return false;
            }
            else if (uuid.compareTo(other.uuid) != 0)
            {
                return false;
            }
            return true;
        }

        @Override
        public String toString()
        {
            return "Pojo [id=" + id + ", ip=" + ip + ", name=" + name + ", uuid=" + uuid + "]";
        }
        
    }
    
    public static class ImmutablePojo
    {
        final int id;
        final String name;
        
        public ImmutablePojo(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

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
            ImmutablePojo other = (ImmutablePojo)obj;
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

        @Override
        public String toString()
        {
            return "ImmutablePojo [id=" + id + ", name=" + name + "]";
        }
        
    }
    
    public void testPojo() throws Exception
    {
        Schema<Pojo> schema = RuntimeSchema.getSchema(Pojo.class);
        Pojo p = new Pojo().fill();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, buf());
        
        Pojo p2 = new Pojo();
        ProtostuffIOUtil.mergeFrom(data, 0, data.length, p2, schema);
        
        assertEquals(p, p2);
        
        List<Pojo> list = new ArrayList<Pojo>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<Pojo> parsedList = ProtostuffIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }
    
    public void testImmutablePojo() throws Exception
    {
        Schema<ImmutablePojo> schema = RuntimeSchema.getSchema(ImmutablePojo.class);
        ImmutablePojo p = new ImmutablePojo(3, "ip");

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, buf());
        
        ImmutablePojo p2 = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, 0, data.length, p2, schema);
        
        assertEquals(p, p2);
        
        List<ImmutablePojo> list = new ArrayList<ImmutablePojo>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<ImmutablePojo> parsedList = ProtostuffIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }

}
