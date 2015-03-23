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

package io.protostuff.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for runtime schemas to skip deprecated field and still allow backward-forward compatibility.
 * 
 * @author David Yu
 * @created Oct 28, 2010
 */
public class DeprecatedFieldTest
{

    public static class Entity
    {

        int id;
        String name;
        @Deprecated
        String alias;
        long timestamp;
    }

    @Test
    public void testIt() throws Exception
    {
        RuntimeSchema<Entity> schema = (RuntimeSchema<Entity>) RuntimeSchema
                .getSchema(Entity.class);
        assertTrue(schema.getFields().size() == 3);
        assertEquals(schema.getFields().get(0).name, "id");
        assertEquals(schema.getFields().get(0).number, 1);

        assertEquals(schema.getFields().get(1).name, "name");
        assertEquals(schema.getFields().get(1).number, 2);

        assertEquals(schema.getFields().get(2).name, "timestamp");
        assertEquals(schema.getFields().get(2).number, 4);

        assertTrue(schema.getFieldNumber("alias") == 0);
        assertNull(schema.getFieldByName("alias"));
    }

}
