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

import java.util.LinkedHashSet;

import junit.framework.TestCase;

import com.dyuproject.protostuff.ComputedSizeOutput;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
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
    
    static final int BUF_SIZE = 256;
    
    public static LinkedBuffer buf()
    {
        return LinkedBuffer.allocate(BUF_SIZE);
    }
    
    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return ProtostuffIOUtil.toByteArray(message, schema, buf());
    }
    
    public void testFoo() throws Exception
    {
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);
        
        Foo fooCompare = foo;
        Foo dfoo = new Foo();
        
        byte[] deferred = toByteArray(fooCompare, schema);
        
        // ComputedSizeOutput is format compatible with protobuf 
        // E.g collections are not serialized ... only its members/elements are.
        if(!RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            assertTrue(deferred.length == ComputedSizeOutput.getSize(fooCompare, schema));
        
        ProtostuffIOUtil.mergeFrom(deferred, dfoo, schema);
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
            ProtostuffIOUtil.mergeFrom(deferred, dbar, schema);
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
            ProtostuffIOUtil.mergeFrom(deferred, dbaz, schema);
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
        ProtostuffIOUtil.mergeFrom(deferred, dhhb, schema);
        assertEquals(hhbCompare, dhhb);
    }
    
    static PojoWithArrayAndSet filledPojoWithArrayAndSet()
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
        
        return new PojoWithArrayAndSet(someEnumAsSet, 
                someEnumAsSet.toArray(new Status[someEnumAsSet.size()]),
                someBarAsSet,
                someBarAsSet.toArray(new Bar[someBarAsSet.size()]),
                someFloatAsSet, 
                someFloatAsSet.toArray(new Float[someFloatAsSet.size()]), 
                new Double[]{112233.332211d, 445566.665544d},
                new double[]{-112233.332211d, -445566.665544d});
    }
    
    public void testPojoWithArrayAndSet() throws Exception
    {
        PojoWithArrayAndSet pojoCompare = filledPojoWithArrayAndSet();
        
        Schema<PojoWithArrayAndSet> schema = RuntimeSchema.getSchema(PojoWithArrayAndSet.class);
        
        PojoWithArrayAndSet dpojo = new PojoWithArrayAndSet();
        
        int expectedSize = ComputedSizeOutput.getSize(pojoCompare, schema, true);

        byte[] deferred = toByteArray(pojoCompare, schema);
        assertTrue(deferred.length == expectedSize);
        ProtostuffIOUtil.mergeFrom(deferred, dpojo, schema);
        assertEquals(pojoCompare, dpojo);
        //System.err.println(dpojo.getSomeEnumAsSet());
        //System.err.println(dpojo.getSomeFloatAsSet());
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
    
}
