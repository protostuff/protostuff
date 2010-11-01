//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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
import java.util.Date;
import java.util.List;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

/**
 * Test ser/deser of Date object.
 *
 * @author David Yu
 * @created Nov 1, 2010
 */
public class DateTest extends AbstractTest
{
    
    public static class Entity 
    {
        int    id;
        String name;
        Date   timestamp;
        
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + ((name == null)?0:name.hashCode());
            result = prime * result + ((timestamp == null)?0:timestamp.hashCode());
            return result;
        }
        
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Entity other = (Entity)obj;
            if (id != other.id)
                return false;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (timestamp == null)
            {
                if (other.timestamp != null)
                    return false;
            }
            else if (!timestamp.equals(other.timestamp))
                return false;
            return true;
        }
        
        
    }
    
    static Entity filledEntity()
    {
        Entity e = new Entity();
        e.id = 1;
        e.name = "entity";
        e.timestamp = new Date();
        return e;
    }

    
    public void testProtobuf() throws Exception
    {
        Schema<Entity> schema = RuntimeSchema.getSchema(Entity.class);
        Entity p = filledEntity();

        byte[] data = ProtobufIOUtil.toByteArray(p, schema, LinkedBuffer.allocate(512));
        
        Entity p2 = new Entity();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        
        assertEquals(p, p2);
        
        List<Entity> list = new ArrayList<Entity>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtobufIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<Entity> parsedList = ProtobufIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }
    
    public void testProtostuff() throws Exception
    {
        Schema<Entity> schema = RuntimeSchema.getSchema(Entity.class);
        Entity p = filledEntity();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, LinkedBuffer.allocate(512));
        
        Entity p2 = new Entity();
        ProtostuffIOUtil.mergeFrom(data, 0, data.length, p2, schema);
        
        assertEquals(p, p2);
        
        List<Entity> list = new ArrayList<Entity>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<Entity> parsedList = ProtostuffIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }

}
