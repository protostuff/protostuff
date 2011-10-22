//================================================================================
//Copyright (c) 2011, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================


package com.dyuproject.protostuff;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Test for Map impls that returns non-unique entries across iterator calls.
 *
 * @author David Yu
 * @created Oct 22, 2011
 */
public class UniqueMapEntryTest extends TestCase
{
    
    enum Gender
    {
        MALE, FEMALE
    }
    
    public void testIt()
    {
        for(MapSchema.MessageFactory mf : MapSchema.MessageFactories.values())
        {
            Map<String,String> map = mf.newMessage();
            verify(map, "key1", "value1", "key2", "value2");
        }
        
        EnumMap<Gender,String> map = new EnumMap<Gender,String>(Gender.class);
        verify(map, Gender.MALE, "m", Gender.FEMALE, "f");
        
    }
    
    static <K,V> void verify(Map<K,V> map, K k1, V v1, K k2, V v2)
    {
        map.put(k1, v1);
        map.put(k2, v2);
        
        Iterator<Map.Entry<K,V>> iter = map.entrySet().iterator();
        
        assertTrue(iter.hasNext());
        Map.Entry<K,V> first = iter.next();
        
        assertTrue(iter.hasNext());
        Map.Entry<K, V> second = iter.next();
        
        if(first == second)
        {
            // either IdentityHashMap or EnumMap
            System.err.println(map.getClass().getName() + " with entry: " + first.getClass().getName());
        }
    }
}
