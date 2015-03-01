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

package io.protostuff.runtime;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import io.protostuff.Schema;

/**
 * Test w/c fields should be excluded.
 * 
 * @author David Yu
 */
public class FieldExclusionTest
{

    @SuppressWarnings("unused")
    public static class EmptyFieldsPojo
    {

        transient int someInt;
        static long someLong;
        @Deprecated
        boolean g;
    }

    @SuppressWarnings("unused")
    public static class ComplexFieldsPojo
    {

        Collection<String[]> someCollectionValueArray;
        Collection<Collection<String>> someCollectionValueCollection;
        Collection<List<String>> someCollectionValueList;
        Collection<Set<String>> someCollectionValueSet;
        Collection<Map<String, Double>> someCollectionValueMap;
        Collection<String>[] arrayCollection;
        Collection<Object> someCollectionValueObject;
        Collection<?> someCollectionValueWildcard;

        List<String[]> someListValueArray;
        List<Collection<String>> someListValueCollection;
        List<List<String>> someListValueList;
        List<Set<String>> someListValueSet;
        List<Map<String, Double>> someListValueMap;
        List<String>[] arrayList;
        List<Object> someListValueObject;
        List<?> someListValueWildcard;

        Set<String[]> someSetValueArray;
        Set<Collection<String>> someSetValueCollection;
        Set<List<String>> someSetValueList;
        Set<Set<String>> someSetValueSet;
        Set<Map<String, Double>> someSetValueMap;
        Set<String>[] arraySet;
        Set<Object> someSetValueObject;
        Set<?> someSetValueWildcard;

        Map<String[], String> someMapKeyArray;
        Map<String, String[]> someMapValueArray;
        Map<String[], String[]> someMapBothArray;
        Map<String, String>[] arrayMap;
        Map<Object, Object> someMapBothObject;
        Map<?, ?> someMapBothWildcard;

        Map<Collection<String>, String> someMapKeyCollection;
        Map<String, Collection<String>> someMapValueCollection;
        Map<Collection<String>, Collection<String>> someMapBothCollection;

        Map<List<String>, String> someMapKeyList;
        Map<String, List<String>> someMapValueList;
        Map<List<String>, List<String>> someMapBothList;

        Map<Set<String>, String> someMapKeySet;
        Map<String, Set<String>> someMapValueSet;
        Map<Set<String>, Set<String>> someMapBothSet;

        Map<Map<String, String>, String> someMapKeyMap;
        Map<String, Map<String, String>> someMapValueMap;
        Map<Map<String, String>, Map<String, String>> someMapBothMap;

        Integer[][][][] someIntArray4D;
        long[][][] someLongArray3D;
        byte[][] someByteArray2D;
    }

    @Test
    public void testEmptyFieldsPojo()
    {
        RuntimeSchema<EmptyFieldsPojo> schema = RuntimeSchema.createFrom(EmptyFieldsPojo.class, RuntimeEnv.ID_STRATEGY);
        Assert.assertNotNull(schema);
        Assert.assertEquals(0, schema.getFieldCount());
    }

    static void assertAssignable(Object obj, Class<?> clazz)
    {
        assertTrue(clazz.isAssignableFrom(obj.getClass()));
    }

    @Test
    public void testComplexFieldsPojo()
    {
        Schema<ComplexFieldsPojo> schema = RuntimeSchema
                .getSchema(ComplexFieldsPojo.class);

        MappedSchema<ComplexFieldsPojo> mappedSchema = (MappedSchema<ComplexFieldsPojo>) schema;

        assertTrue(mappedSchema.fields.length == 45);

        Class<?> expectedCollectionClass = RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS ? RuntimeCollectionField.class
                : RuntimeObjectField.class;

        assertAssignable(
                mappedSchema.fieldsByName.get("someCollectionValueArray"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someCollectionValueCollection"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someCollectionValueList"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someCollectionValueSet"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someCollectionValueMap"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someCollectionValueObject"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someCollectionValueWildcard"),
                expectedCollectionClass);

        assertAssignable(mappedSchema.fieldsByName.get("someListValueArray"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someListValueCollection"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someListValueList"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someListValueSet"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someListValueMap"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someListValueObject"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someListValueWildcard"),
                expectedCollectionClass);

        assertAssignable(mappedSchema.fieldsByName.get("someSetValueArray"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.fieldsByName.get("someSetValueCollection"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someSetValueList"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someSetValueSet"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someSetValueMap"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someSetValueObject"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.fieldsByName.get("someSetValueWildcard"),
                expectedCollectionClass);

        assertTrue(mappedSchema.fieldsByName.get("someMapKeyArray") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueArray") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothArray") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothObject") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothWildcard") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.fieldsByName.get("someMapKeyCollection") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueCollection") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothCollection") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.fieldsByName.get("someMapKeyList") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueList") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothList") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.fieldsByName.get("someMapKeySet") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueSet") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothSet") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.fieldsByName.get("someMapKeyMap") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueMap") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothMap") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.fieldsByName.get("someIntArray4D") instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someLongArray3D") instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someByteArray2D") instanceof RuntimeObjectField<?>);

    }

}
