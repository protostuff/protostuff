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

package com.dyuproject.protostuff.runtime;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.runtime.Foo.EnumSample;

/**
 * The objects to be tested.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public final class SerializableObjects
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
            new EnumSample[]{EnumSample.TYPE1, EnumSample.TYPE2}, 
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
    
    static void assertEquals(Baz baz1, Baz baz2)
    {
        // true if both are null
        if(baz1 == baz2)
            return;
        
        Assert.assertTrue(baz1.getId() == baz2.getId());
        Assert.assertEquals(baz1.getName(), baz2.getName());
        Assert.assertTrue(baz1.getTimestamp() == baz2.getTimestamp());
    }
    
    static void assertEquals(Bar bar1, Bar bar2)
    {
        // true if both are null
        if(bar1 == bar2)
            return;
        
        Assert.assertTrue(bar1.getSomeInt() == bar2.getSomeInt());
        Assert.assertEquals(bar1.getSomeString(), bar2.getSomeString());
        assertEquals(bar1.getBaz(), bar2.getBaz());
        Assert.assertTrue(bar1.getSomeEnum() == bar2.getSomeEnum());
        Assert.assertEquals(bar1.getSomeBytes(), bar2.getSomeBytes());
        Assert.assertTrue(bar1.getSomeBoolean() == bar2.getSomeBoolean());
        Assert.assertTrue(bar1.getSomeFloat() == bar2.getSomeFloat());
        Assert.assertTrue(bar1.getSomeDouble() == bar2.getSomeDouble());
        Assert.assertTrue(bar1.getSomeLong() == bar2.getSomeLong());
    }
    
    static void assertEquals(Foo f1, Foo f2)
    {
        // true if both are null
        if(f1 == f2)
            return;
        
        Assert.assertEquals(f1.getSomeInt(), f2.getSomeInt());
        Assert.assertEquals(f1.getSomeString(), f2.getSomeString());
        
        List<Bar> bar1 = f1.getSomeBar();
        List<Bar> bar2 = f2.getSomeBar();
        if(bar1!=null && bar2!=null)
        {
            Assert.assertTrue(bar1.size() == bar2.size());
            for(int i=0, size=bar1.size(); i<size; i++)
                assertEquals(bar1.get(i), bar2.get(i));
        }
        
        
        //Assert.assertEquals(f1.getSomeEnum(), f2.getSomeEnum());
        Assert.assertEquals(f1.getSomeBytes(), f2.getSomeBytes());
        Assert.assertEquals(f1.getSomeBoolean(), f2.getSomeBoolean());
        Assert.assertEquals(f1.getSomeFloat(), f2.getSomeFloat());
        Assert.assertEquals(f1.getSomeDouble(), f2.getSomeDouble());
        Assert.assertEquals(f1.getSomeLong(), f2.getSomeLong());
    }

}
