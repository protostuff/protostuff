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

import io.protostuff.AbstractTest;
import io.protostuff.Exclude;
import io.protostuff.Tag;

/**
 * Test for runtime schemas to skip fields annotated with @Exclude and still allow backward-forward compatibility.
 * 
 * @author Johannes Elgh
 * @created Jul 30, 2014
 */
public class ExcludeFieldTest extends AbstractTest
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

    public void testIt() throws Exception
    {
        MappedSchema<Entity> schema = (MappedSchema<Entity>) RuntimeSchema
                .getSchema(Entity.class);
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

    public void testMuchExcludedEntity() throws Exception
    {
        MappedSchema<MuchExcludedEntity> schema = (MappedSchema<MuchExcludedEntity>) RuntimeSchema
                .getSchema(MuchExcludedEntity.class);
        System.err.println(schema.fields.length);
        assertTrue(schema.fields.length == 1);

        assertTrue(schema.getFieldNumber("id") == 0);
        assertNull(schema.fieldsByName.get("id"));

        assertTrue(schema.getFieldNumber("name") == 0);
        assertNull(schema.fieldsByName.get("name"));

        assertEquals(schema.fields[0].name, "alias");
        assertEquals(schema.fields[0].number, 3);

        assertTrue(schema.getFieldNumber("timestamp") == 0);
        assertNull(schema.fieldsByName.get("timestamp"));
    }

    public void testTaggedAndExcludedEntity() throws Exception
    {
        MappedSchema<TaggedAndExcludedEntity> schema = (MappedSchema<TaggedAndExcludedEntity>) RuntimeSchema
                .getSchema(TaggedAndExcludedEntity.class);
        System.err.println(schema.fields.length);
        assertTrue(schema.fields.length == 2);

        assertTrue(schema.getFieldNumber("id") == 0);
        assertNull(schema.fieldsByName.get("id"));

        assertEquals(schema.fields[0].name, "alias");
        assertEquals(schema.fields[0].number, 2);

        assertEquals(schema.fields[1].name, "name");
        assertEquals(schema.fields[1].number, 4);

        assertTrue(schema.getFieldNumber("timestamp") == 0);
        assertNull(schema.fieldsByName.get("timestamp"));
    }

}
