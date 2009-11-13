//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff;

import java.util.Arrays;

import com.dyuproject.protostuff.Foo.EnumSample;

/**
 * The objects to be tested.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public final class TestObjects
{
    
    static final Baz negativeBaz = new Baz(-567, "negativeBaz", -202020202);
    static final Bar negativeBar = new Bar(-12, "negativeBar", negativeBaz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("a1"), true, -130.031f, -1000.0001d, -101010101);

    
    static final Baz baz = new Baz(567, "baz", 202020202);    
    static final Bar bar = new Bar(890, "bar", baz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("b2"), true, 150.051f, 2000.0002d, 303030303);
    
    static final Foo foo = newFoo(
            new Integer[]{90210,-90210, 0}, 
            new String[]{"ab", "cd"}, 
            new Bar[]{bar, negativeBar},
            new EnumSample[]{EnumSample.TYPE0, EnumSample.TYPE2}, 
            new ByteString[]{ByteString.copyFromUtf8("ef"), ByteString.copyFromUtf8("gh")}, 
            new Boolean[]{true, false}, 
            new Float[]{1234.4321f, -1234.4321f, 0f}, 
            new Double[]{12345678.87654321d, -12345678.87654321d, 0d}, 
            new Long[]{7060504030201l, -7060504030201l, 0l});

    
    static Foo newFoo(
            Integer[] someInt, 
            String[] someString, 
            Bar[] someBar, 
            EnumSample[] someEnum,
            ByteString[] someBytes,
            Boolean[] someBoolean,
            Float[] someFloat,
            Double[] someDouble,
            Long[] someLong)
    {
        
        return new Foo(
                Arrays.asList(someInt),
                Arrays.asList(someString), 
                Arrays.asList(someBar), 
                Arrays.asList(someEnum), 
                Arrays.asList(someBytes), 
                Arrays.asList(someBoolean), 
                Arrays.asList(someFloat), 
                Arrays.asList(someDouble),
                Arrays.asList(someLong));
    }

}
