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

import io.protostuff.Exclude;
import io.protostuff.Tag;

/**
 * Test for runtime schemas to skip fields annotated with @Exclude and still allow backward-forward compatibility.
 * 
 * @author Johannes Elgh
 * @created Jul 30, 2014
 */
public class ExcludeFieldTest
{

    public static class Entity
    {

        int id;
        String name;
        @Exclude
        String alias;
        long timestamp;
    }

    public static class MuchExcludedEntity
    {
        @Exclude
        int id;
        @Exclude
        String name;
        String alias;
        @Exclude
        long timestamp;
    }

    public static class TaggedAndExcludedEntity
    {
        @Exclude
        int id;
        @Tag(4)
        String name;
        @Tag(2)
        String alias;
        @Exclude
        long timestamp;
    }

    @Test
    public void testIt() throws Exception
    {
        RuntimeSchema<Entity> schema = (RuntimeSchema<Entity>) RuntimeSchema
                .getSchema(Entity.class);
        assertTrue(schema.getFieldCount() == 3);
        assertEquals(schema.getFields().get(0).name, "id");
        assertEquals(schema.getFields().get(0).number, 1);

        assertEquals(schema.getFields().get(1).name, "name");
        assertEquals(schema.getFields().get(1).number, 2);

        assertEquals(schema.getFields().get(2).name, "timestamp");
        assertEquals(schema.getFields().get(2).number, 3);

        assertTrue(schema.getFieldNumber("alias") == 0);
        assertNull(schema.getFieldByName("alias"));
    }

    @Test
    public void testMuchExcludedEntity() throws Exception
    {
        RuntimeSchema<MuchExcludedEntity> schema = (RuntimeSchema<MuchExcludedEntity>) RuntimeSchema
                .getSchema(MuchExcludedEntity.class);

        assertTrue(schema.getFieldCount() == 1);

        assertTrue(schema.getFieldNumber("id") == 0);
        assertNull(schema.getFieldByName("id"));

        assertTrue(schema.getFieldNumber("name") == 0);
        assertNull(schema.getFieldByName("name"));

        assertEquals(schema.getFields().get(0).name, "alias");
        assertEquals(schema.getFields().get(0).number, 1);

        assertTrue(schema.getFieldNumber("timestamp") == 0);
        assertNull(schema.getFieldByName("timestamp"));
    }

    @Test
    public void testTaggedAndExcludedEntity() throws Exception
    {
        RuntimeSchema<TaggedAndExcludedEntity> schema = (RuntimeSchema<TaggedAndExcludedEntity>) RuntimeSchema
                .getSchema(TaggedAndExcludedEntity.class);
        assertEquals(2, schema.getFieldCount());

        assertEquals(0, schema.getFieldNumber("id"));
        assertNull(schema.getFieldByName("id"));

        assertEquals(schema.getFields().get(0).name, "alias");
        assertEquals(schema.getFields().get(0).number, 2);

        assertEquals("name", schema.getFields().get(1).name);
        assertEquals(4, schema.getFields().get(1).number);

        assertTrue(schema.getFieldNumber("timestamp") == 0);
        assertNull(schema.getFieldByName("timestamp"));
    }

}
