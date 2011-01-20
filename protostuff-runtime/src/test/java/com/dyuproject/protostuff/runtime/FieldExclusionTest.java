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

/**
 * Test w/c fields should be excluded.
 *
 * @author David Yu
 * @created Jan 20, 2011
 */
public class FieldExclusionTest extends AbstractTest
{
    
    static class EmptyFieldsPojo
    {
        transient int someInt;
        static long someLong;
        
        /* this works as well
        @Deprecated
        boolean g; */
        
        Collection<String[]> someCollectionValueArray;
        Collection<Collection<String>> someCollectionValueCollection;
        Collection<List<String>> someCollectionValueList;
        Collection<Set<String>> someCollectionValueSet;
        Collection<Map<String,Double>> someCollectionValueMap;
        Collection<String>[] arrayCollection;
        
        List<String[]> someListValueArray;
        List<Collection<String>> someListValueCollection;
        List<List<String>> someListValueList;
        List<Set<String>> someListValueSet;
        List<Map<String,Double>> someListValueMap;
        List<String>[] arrayList;
        
        Set<String[]> someSetValueArray;
        Set<Collection<String>> someSetValueCollection;
        Set<List<String>> someSetValueList;
        Set<Set<String>> someSetValueSet;
        Set<Map<String,Double>> someSetValueMap;
        Set<String>[] arraySet;

        Map<String, String> someMap;
        Map<String[], String> someMapKeyArray;
        Map<String, String[]> someMapValueArray;
        Map<String[], String[]> someMapBothArray;
        Map<String, String>[] arrayMap;
        
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
        
        Integer[][] someOtherInt;
        long[][] someOtherLong;
        byte[][] someBytes;
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

}
