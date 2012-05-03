//================================================================================
//Copyright (c) 2012, David Yu
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


package com.dyuproject.protostuff.runtime;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import junit.framework.TestCase;

/**
 * Test generic types.
 *
 * @author David Yu
 * @created May 3, 2012
 */
public class GenericTypeTest extends TestCase
{
    
    static class PojoWithCollectionAndMapGenericTypes
    {
        Collection<byte[]> cByteArray;
        List<int[]> cIntArray;
        Set<String[]> cStringArray;
        SortedSet<Throwable[]> cThrowableArray;
        
        Collection<long[][]> cLongArray2D;
        List<long[][][]> cLongArray3D;
        
        Collection<Class<?>> cClass;
        List<Enum<?>> cEnum;
        
        @SuppressWarnings("rawtypes")
        Set<Class[]> cClassArray;
        
        // Not handled. ObjectSchema will be used which means it will not be 
        // as fast as ArraySchema (same serialized output though).
        //Set<Class<?>[]> cClassArray2;
        
        @SuppressWarnings("rawtypes")
        Map<Class,Class<?>> mClass;
        
        @SuppressWarnings("rawtypes")
        SortedMap<Enum,Enum<?>> mEnum;
    }
    
    static Class<?> genericTypeFrom(Class<?> c, String fieldName, int index) 
            throws Exception
    {
        return RuntimeFieldFactory.getGenericType(c.getDeclaredField(fieldName), index);
    }
    
    public void testIt() throws Exception
    {
        Class<PojoWithCollectionAndMapGenericTypes> c = 
                PojoWithCollectionAndMapGenericTypes.class;
        
        assertTrue(byte[].class == genericTypeFrom(c, "cByteArray", 0));
        assertTrue(int[].class == genericTypeFrom(c, "cIntArray", 0));
        assertTrue(String[].class == genericTypeFrom(c, "cStringArray", 0));
        assertTrue(Throwable[].class == genericTypeFrom(c, "cThrowableArray", 0));
        
        assertTrue(long[][].class == genericTypeFrom(c, "cLongArray2D", 0));
        assertTrue(long[][][].class == genericTypeFrom(c, "cLongArray3D", 0));
        
        assertTrue(Class.class == genericTypeFrom(c, "cClass", 0));
        assertTrue(Enum.class == genericTypeFrom(c, "cEnum", 0));
        
        assertTrue(Class[].class == genericTypeFrom(c, "cClassArray", 0));
        
        assertTrue(Class.class == genericTypeFrom(c, "mClass", 0));
        assertTrue(Class.class == genericTypeFrom(c, "mClass", 1));
        
        assertTrue(Enum.class == genericTypeFrom(c, "mEnum", 0));
        assertTrue(Enum.class == genericTypeFrom(c, "mEnum", 1));
    }

}
