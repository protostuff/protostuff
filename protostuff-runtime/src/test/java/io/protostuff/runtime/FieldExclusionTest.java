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

        RuntimeSchema<ComplexFieldsPojo> mappedSchema = (RuntimeSchema<ComplexFieldsPojo>) schema;

        assertTrue(mappedSchema.getFieldCount() == 45);

        Class<?> expectedCollectionClass = RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS ? RuntimeCollectionField.class
                : RuntimeObjectField.class;

        assertAssignable(
                mappedSchema.getFieldByName("someCollectionValueArray"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someCollectionValueCollection"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someCollectionValueList"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someCollectionValueSet"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someCollectionValueMap"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someCollectionValueObject"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someCollectionValueWildcard"),
                expectedCollectionClass);

        assertAssignable(mappedSchema.getFieldByName("someListValueArray"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someListValueCollection"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someListValueList"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someListValueSet"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someListValueMap"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someListValueObject"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someListValueWildcard"),
                expectedCollectionClass);

        assertAssignable(mappedSchema.getFieldByName("someSetValueArray"),
                expectedCollectionClass);
        assertAssignable(
                mappedSchema.getFieldByName("someSetValueCollection"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someSetValueList"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someSetValueSet"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someSetValueMap"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someSetValueObject"),
                expectedCollectionClass);
        assertAssignable(mappedSchema.getFieldByName("someSetValueWildcard"),
                expectedCollectionClass);

        assertTrue(mappedSchema.getFieldByName("someMapKeyArray") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapValueArray") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapBothArray") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapBothObject") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapBothWildcard") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.getFieldByName("someMapKeyCollection") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapValueCollection") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapBothCollection") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.getFieldByName("someMapKeyList") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapValueList") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapBothList") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.getFieldByName("someMapKeySet") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapValueSet") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapBothSet") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.getFieldByName("someMapKeyMap") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapValueMap") instanceof RuntimeMapField<?, ?, ?>);
        assertTrue(mappedSchema.getFieldByName("someMapBothMap") instanceof RuntimeMapField<?, ?, ?>);

        assertTrue(mappedSchema.getFieldByName("someIntArray4D") instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.getFieldByName("someLongArray3D") instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.getFieldByName("someByteArray2D") instanceof RuntimeObjectField<?>);

    }

}
