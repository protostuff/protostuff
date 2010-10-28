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

import com.dyuproject.protostuff.AbstractTest;

/**
 * Test for runtime schemas to skip deprecated field and still allow 
 * backward-forward compatibility.
 * 
 *
 * @author David Yu
 * @created Oct 28, 2010
 */
public class DeprecatedFieldTest extends AbstractTest
{
    
    public static class Entity
    {

        int id;
        String name;
        @Deprecated
        String alias;
        long timestamp;
    }
    
    static Entity filledEntity()
    {
        Entity e = new Entity();
        e.id = 1;
        e.name = "entity";
        e.alias = "e";
        e.timestamp = System.currentTimeMillis();
        
        return e;
    }
    
    public void testIt() throws Exception
    {
        MappedSchema<Entity> schema = (MappedSchema<Entity>)RuntimeSchema.getSchema(Entity.class);
        System.err.println(schema.fields.length);
        assertTrue(schema.fields.length == 3);
        assertEquals(schema.fields[0].name, "id");
        assertEquals(schema.fields[0].number, 1);
        
        assertEquals(schema.fields[1].name, "name");
        assertEquals(schema.fields[1].number, 2);
        
        assertEquals(schema.fields[2].name, "timestamp");
        assertEquals(schema.fields[2].number, 4);
        
        assertTrue(schema.getFieldNumber("alias") == 0);
        assertNull(schema.fieldsByName.get("alias"));
    }
    

}
