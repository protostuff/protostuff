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

import static com.dyuproject.protostuff.runtime.SerializableObjects.bar;
import static com.dyuproject.protostuff.runtime.SerializableObjects.baz;
import static com.dyuproject.protostuff.runtime.SerializableObjects.foo;
import static com.dyuproject.protostuff.runtime.SerializableObjects.negativeBar;
import static com.dyuproject.protostuff.runtime.SerializableObjects.negativeBaz;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.dyuproject.protostuff.BufferedOutput;
import com.dyuproject.protostuff.ComputedSizeOutput;
import com.dyuproject.protostuff.IOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.Bar.Status;

/**
 * Serialization and deserialization test cases.
 *
 * @author David Yu
 * @created Nov 18, 2009
 */
public class SerDeserTest extends TestCase
{
    
    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return IOUtil.toByteArray(message, schema, 
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }
    
    public void testFoo() throws Exception
    {
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);
        
        Foo fooCompare = foo;
        Foo dfoo = new Foo();
        
        int expectedSize = ComputedSizeOutput.getSize(fooCompare, schema);

        byte[] deferred = toByteArray(fooCompare, schema);
        assertTrue(deferred.length == expectedSize);
        IOUtil.mergeFrom(deferred, dfoo, schema);
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        Schema<Bar> schema = RuntimeSchema.getSchema(Bar.class);
        
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar dbar = new Bar();            
            
            int expectedSize = ComputedSizeOutput.getSize(barCompare, schema);

            byte[] deferred = toByteArray(barCompare, schema);
            assertTrue(deferred.length == expectedSize);
            IOUtil.mergeFrom(deferred, dbar, schema);
            SerializableObjects.assertEquals(barCompare, dbar);
            //System.err.println(dbar.getSomeInt());
            //System.err.println(dbar.getSomeLong());
            //System.err.println(dbar.getSomeFloat());
            //System.err.println(dbar.getSomeDouble());
            //System.err.println(dbar.getSomeBytes());
            //System.err.println(dbar.getSomeString());
            //System.err.println(dbar.getSomeEnum());
            //System.err.println(dbar.getSomeBoolean());
        }
    }
    
    public void testBaz() throws Exception
    {
        Schema<Baz> schema = RuntimeSchema.getSchema(Baz.class);
        
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz dbaz = new Baz();            
            
            int expectedSize = ComputedSizeOutput.getSize(bazCompare, schema);

            byte[] deferred = toByteArray(bazCompare, schema);
            assertTrue(deferred.length == expectedSize);
            IOUtil.mergeFrom(deferred, dbaz, schema);
            SerializableObjects.assertEquals(bazCompare, dbaz);
            //System.err.println(dbaz.getId());
            //System.err.println(dbaz.getName());
            //System.err.println(dbaz.getTimestamp());
        }
    }
    
    /**
     * HasHasBar wraps an object without a schema.
     * That object will have to be serialized via the default java serialization 
     * and it will be delimited.
     * 
     * HasBar wraps a message {@link Bar}.
     */
    public void testJavaSerializable() throws Exception
    {
        Schema<HasHasBar> schema = RuntimeSchema.getSchema(HasHasBar.class);
        
        HasHasBar hhbCompare = new HasHasBar("hhb", 
                new HasBar(12345, "hb", SerializableObjects.bar));
        HasHasBar dhhb = new HasHasBar();        
        
        int expectedSize = ComputedSizeOutput.getSize(hhbCompare, schema);

        byte[] deferred = toByteArray(hhbCompare, schema);
        assertTrue(deferred.length == expectedSize);
        IOUtil.mergeFrom(deferred, dhhb, schema);
        assertEquals(hhbCompare, dhhb);
    }
    
    public void testPojoWithArrayAndSet() throws Exception
    {
        LinkedHashSet<Status> someEnumAsSet = new LinkedHashSet<Status>();
        someEnumAsSet.add(Status.PENDING);
        someEnumAsSet.add(Status.STARTED);
        someEnumAsSet.add(Status.COMPLETED);
        
        LinkedHashSet<Bar> someBarAsSet = new LinkedHashSet<Bar>();
        someBarAsSet.add(bar);
        someBarAsSet.add(negativeBar);
        
        LinkedHashSet<Float> someFloatAsSet = new LinkedHashSet<Float>();
        someFloatAsSet.add(123.321f);
        someFloatAsSet.add(-456.654f);
        
        PojoWithArrayAndSet pojoCompare = new PojoWithArrayAndSet(someEnumAsSet, 
                someEnumAsSet.toArray(new Status[someEnumAsSet.size()]),
                someBarAsSet,
                someBarAsSet.toArray(new Bar[someBarAsSet.size()]),
                someFloatAsSet, 
                someFloatAsSet.toArray(new Float[someFloatAsSet.size()]), 
                new Double[]{112233.332211d, 445566.665544d},
                new double[]{-112233.332211d, -445566.665544d});
        
        Schema<PojoWithArrayAndSet> schema = RuntimeSchema.getSchema(PojoWithArrayAndSet.class);
        
        PojoWithArrayAndSet dpojo = new PojoWithArrayAndSet();
        
        int expectedSize = ComputedSizeOutput.getSize(pojoCompare, schema);

        byte[] deferred = toByteArray(pojoCompare, schema);
        assertTrue(deferred.length == expectedSize);
        IOUtil.mergeFrom(deferred, dpojo, schema);
        assertEquals(pojoCompare, dpojo);
        //System.err.println(dpojo.getSomeEnumAsSet());
        //System.err.println(dpojo.getSomeFloatAsSet());
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
    
    static void assertEquals(HasHasBar h1, HasHasBar h2)
    {
        // true if both are null
        if(h1 == h2)
            return;
        
        assertEquals(h1.getName(), h2.getName());
        assertEquals(h1.getHasBar(), h2.getHasBar());
    }
    
    static void assertEquals(HasBar h1, HasBar h2)
    {
        // true if both are null
        if(h1 == h2)
            return;
        
        assertTrue(h1.getId() == h2.getId());
        assertEquals(h1.getName(), h2.getName());
        SerializableObjects.assertEquals(h1.getBar(), h2.getBar());
    }
    
    static void assertEquals(PojoWithArrayAndSet p1, PojoWithArrayAndSet p2)
    {
        // true if both are null
        if(p1 == p2)
            return;
        
        Set<Status> s1 = p1.getSomeEnumAsSet();
        Set<Status> s2 = p2.getSomeEnumAsSet();
        if(s1!=null)
        {
            assertNotNull(s2);
            assertEquals(s1, s2);
        }
        else
            assertNull(s2);
        
        Status[] sa1 = p1.getSomeEnumAsArray();
        Status[] sa2 = p2.getSomeEnumAsArray();
        if(sa1!=null)
        {
            assertNotNull(sa2);
            assertTrue(sa1.length == sa2.length);
            
            for(int i=0,len=sa1.length; i<len; i++)
                assertEquals(sa1[i], sa2[i]);
        }
        else
            assertNull(sa2);
        
        
        Set<Bar> b1 = p1.getSomeBarAsSet();
        Set<Bar> b2 = p2.getSomeBarAsSet();
        if(b1!=null)
        {
            assertNotNull(b2);
            assertTrue(b1.size() == b2.size());
            Iterator<Bar> i1 = b1.iterator();
            Iterator<Bar> i2 = b2.iterator();
            while(i1.hasNext() && i2.hasNext())
                SerializableObjects.assertEquals(i1.next(), i2.next());
        }
        else
            assertNull(b2);
        
        Bar[] ba1 = p1.getSomeBarAsArray();
        Bar[] ba2 = p2.getSomeBarAsArray();
        if(b1!=null)
        {
            assertNotNull(ba2);
            assertTrue(ba1.length == ba2.length);
            for(int i=0,len=ba1.length; i<len; i++)
                SerializableObjects.assertEquals(ba1[i], ba2[i]);
        }
        else
            assertNull(b2);
        
        
        Set<Float> f1 = p1.getSomeFloatAsSet();
        Set<Float> f2 = p2.getSomeFloatAsSet();
        if(f1!=null)
        {
            assertNotNull(f2);
            assertEquals(f1, f2);
        }
        else
            assertNull(f2);
        
        Float[] fa1 = p1.getSomeFloatAsArray();
        Float[] fa2 = p2.getSomeFloatAsArray();
        if(fa1!=null)
        {
            assertNotNull(fa2);
            assertTrue(fa1.length == fa2.length);
            for(int i=0,len=fa1.length; i<len; i++)
                assertEquals(fa1[i], fa2[i]);
        }
        else
            assertNull(fa2);
        
        Double[] d1 = p1.getSomeDoubleAsArray();
        Double[] d2 = p2.getSomeDoubleAsArray();
        if(d1!=null)
        {
            assertNotNull(d2);
            assertTrue(d1.length == d2.length);
            for(int i=0,len=d1.length; i<len; i++)
                assertEquals(d1[i], d2[i]);
        }
        else
            assertNull(d2);
        
        double[] dp1 = p1.getSomePrimitiveDoubleAsArray();
        double[] dp2 = p2.getSomePrimitiveDoubleAsArray();
        if(dp1!=null)
        {
            assertNotNull(dp2);
            assertTrue(dp1.length == dp2.length);
            for(int i=0,len=dp1.length; i<len; i++)
                assertTrue(dp1[i] == dp2[i]);
        }
        else
            assertNull(dp2);
        
    }
    
    static class EmptyFieldsPojo
    {
        transient int someInt;
        static long someLong;
        
        Map<String,Float> someFloat;
        List<Map<String,Double>> someDouble;
        Map<String,Boolean>[] someBoolean;
        
        Set<Map<String,String>> someString;
        Set<String>[] someOtherString;
        
        Integer[][] someOtherInt;
        long[][] someOtherLong;
        byte[][] someBytes;
    }


}
