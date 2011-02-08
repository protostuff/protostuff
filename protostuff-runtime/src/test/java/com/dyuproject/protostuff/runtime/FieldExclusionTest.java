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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.Schema;

/**
 * Test w/c fields should be excluded.
 *
 * @author David Yu
 * @created Jan 20, 2011
 */
public class FieldExclusionTest extends AbstractTest
{
    
    public static class EmptyFieldsPojo
    {
        transient int someInt;
        static long someLong;
        @Deprecated
        boolean g;
    }
    
    public static class ComplexFieldsPojo
    {
        
        Collection<String[]> someCollectionValueArray;
        Collection<Collection<String>> someCollectionValueCollection;
        Collection<List<String>> someCollectionValueList;
        Collection<Set<String>> someCollectionValueSet;
        Collection<Map<String,Double>> someCollectionValueMap;
        Collection<String>[] arrayCollection;
        Collection<Object> someCollectionValueObject;
        Collection<?> someCollectionValueWildcard;
        
        List<String[]> someListValueArray;
        List<Collection<String>> someListValueCollection;
        List<List<String>> someListValueList;
        List<Set<String>> someListValueSet;
        List<Map<String,Double>> someListValueMap;
        List<String>[] arrayList;
        List<Object> someListValueObject;
        List<?> someListValueWildcard;
        
        Set<String[]> someSetValueArray;
        Set<Collection<String>> someSetValueCollection;
        Set<List<String>> someSetValueList;
        Set<Set<String>> someSetValueSet;
        Set<Map<String,Double>> someSetValueMap;
        Set<String>[] arraySet;
        Set<Object> someSetValueObject;
        Set<?> someSetValueWildcard;

        Map<String[], String> someMapKeyArray;
        Map<String, String[]> someMapValueArray;
        Map<String[], String[]> someMapBothArray;
        Map<String, String>[] arrayMap;
        Map<Object,Object> someMapBothObject;
        Map<?,?> someMapBothWildcard;
        
        Map<Collection<String>, String> someMapKeyCollection;
        Map<String, Collection<String>> someMapValueCollection;
        Map<Collection<String>, Collection<String>> someMapBothCollection;
        
        Map<List<String>, String> someMapKeyList;
        Map<String, List<String>> someMapValueList;
        Map<List<String>, List<String>> someMapBothList;
        
        Map<Set<String>, String> someMapKeySet;
        Map<String, Set<String>> someMapValueSet;
        Map<Set<String>, Set<String>> someMapBothSet;
        
        Map<Map<String,String>, String> someMapKeyMap;
        Map<String, Map<String,String>> someMapValueMap;
        Map<Map<String,String>, Map<String,String>> someMapBothMap;
        
        Integer[][][][] someIntArray4D;
        long[][][] someLongArray3D;
        byte[][] someByteArray2D;
    }
    
    public void testEmptyFieldsPojo()
    {
        try
        {
            RuntimeSchema.createFrom(EmptyFieldsPojo.class);
            assertTrue(false);
        }
        catch(RuntimeException e)
        {
            // expected
        }
    }
    
    public void testComplexFieldsPojo()
    {
        Schema<ComplexFieldsPojo> schema = 
            RuntimeSchema.getSchema(ComplexFieldsPojo.class);
        
        MappedSchema<ComplexFieldsPojo> mappedSchema = 
            (MappedSchema<ComplexFieldsPojo>)schema;
        
        assertTrue(mappedSchema.fields.length == 45);
        
        assertTrue(mappedSchema.fieldsByName.get("someCollectionValueArray") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someCollectionValueCollection") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someCollectionValueList") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someCollectionValueSet") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someCollectionValueMap") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someCollectionValueObject") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someCollectionValueWildcard") 
                instanceof RuntimeObjectField<?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someListValueArray") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someListValueCollection") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someListValueList") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someListValueSet") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someListValueMap") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someListValueObject") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someListValueWildcard") 
                instanceof RuntimeObjectField<?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someSetValueArray") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someSetValueCollection") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someSetValueList") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someSetValueSet") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someSetValueMap") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someSetValueObject") 
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someSetValueWildcard") 
                instanceof RuntimeObjectField<?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someMapKeyArray") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueArray") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothArray") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothObject") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothWildcard") 
                instanceof RuntimeMapField<?,?,?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someMapKeyCollection") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueCollection") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothCollection") 
                instanceof RuntimeMapField<?,?,?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someMapKeyList") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueList") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothList") 
                instanceof RuntimeMapField<?,?,?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someMapKeySet") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueSet") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothSet") 
                instanceof RuntimeMapField<?,?,?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someMapKeyMap") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapValueMap") 
                instanceof RuntimeMapField<?,?,?>);
        assertTrue(mappedSchema.fieldsByName.get("someMapBothMap") 
                instanceof RuntimeMapField<?,?,?>);
        
        assertTrue(mappedSchema.fieldsByName.get("someIntArray4D")
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someLongArray3D")
                instanceof RuntimeObjectField<?>);
        assertTrue(mappedSchema.fieldsByName.get("someByteArray2D")
                instanceof RuntimeObjectField<?>);
        
    }

}
