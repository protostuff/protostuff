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

import static com.dyuproject.protostuff.SerializableObjects.bar;
import static com.dyuproject.protostuff.SerializableObjects.baz;
import static com.dyuproject.protostuff.SerializableObjects.foo;
import static com.dyuproject.protostuff.SerializableObjects.negativeBar;
import static com.dyuproject.protostuff.SerializableObjects.negativeBaz;
import junit.framework.TestCase;

/**
 * Serialization and deserialization test cases.
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public class SerDeserTest extends TestCase
{
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        Foo cfoo = new Foo();
        Foo dfoo = new Foo();        
        
        int expectedSize = ComputedSizeOutput.getSize(fooCompare);
        
        byte[] coded = CodedOutput.toByteArray(fooCompare);
        assertTrue(coded.length == expectedSize);
        IOUtil.mergeFrom(coded, cfoo);      
        SerializableObjects.assertEquals(fooCompare, cfoo);

        byte[] deferred = IOUtil.toByteArray(fooCompare);
        assertTrue(deferred.length == expectedSize);
        IOUtil.mergeFrom(deferred, dfoo);
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar cbar = new Bar();
            Bar dbar = new Bar();            
            
            int expectedSize = ComputedSizeOutput.getSize(barCompare);
            
            byte[] coded = CodedOutput.toByteArray(barCompare);
            assertTrue(coded.length == expectedSize);
            IOUtil.mergeFrom(coded, cbar);        
            SerializableObjects.assertEquals(barCompare, cbar);

            byte[] deferred = IOUtil.toByteArray(barCompare);
            assertTrue(deferred.length == expectedSize);
            IOUtil.mergeFrom(deferred, dbar);
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz cbaz = new Baz();
            Baz dbaz = new Baz();            
            
            int expectedSize = ComputedSizeOutput.getSize(bazCompare);
            
            byte[] coded = CodedOutput.toByteArray(bazCompare);
            assertTrue(coded.length == expectedSize);
            IOUtil.mergeFrom(coded, cbaz);        
            SerializableObjects.assertEquals(bazCompare, cbaz);

            byte[] deferred = IOUtil.toByteArray(bazCompare);
            assertTrue(deferred.length == expectedSize);
            IOUtil.mergeFrom(deferred, dbaz);
            SerializableObjects.assertEquals(bazCompare, dbaz);
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
        HasHasBar hhbCompare = new HasHasBar("hhb", 
                new HasBar(12345, "hb", SerializableObjects.bar));
        HasHasBar chhb = new HasHasBar();
        HasHasBar dhhb = new HasHasBar();        
        
        int expectedSize = ComputedSizeOutput.getSize(hhbCompare);
        
        byte[] coded = CodedOutput.toByteArray(hhbCompare);
        assertTrue(coded.length == expectedSize);
        IOUtil.mergeFrom(coded, chhb);      
        assertEquals(hhbCompare, chhb);

        byte[] deferred = IOUtil.toByteArray(hhbCompare);
        assertTrue(deferred.length == expectedSize);
        IOUtil.mergeFrom(deferred, dhhb);
        assertEquals(hhbCompare, dhhb);
    }
    
    public void testEmptyMessage() throws Exception
    {
        Bar bar = new Bar();
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
    }
    
    public void testEmptyMessageInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setBaz(baz);
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
    }
    
    public void testPartialEmptyMessage() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeInt(1);
        bar.setBaz(baz);
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
    }
    
    public void testPartialEmptyMessageWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("someString");
        bar.setBaz(baz);
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
    }
    
    public void testPartialEmptyMessageWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("");
        bar.setBaz(baz);
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
    }
    
    public void testPartialEmptyMessageInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setId(2);
        bar.setBaz(baz);
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
    }
    
    public void testPartialEmptyMessageInnerWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("asdfsf");
        bar.setBaz(baz);
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
    }
    
    public void testPartialEmptyMessageInnerWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("");
        bar.setBaz(baz);
        
        byte[] coded = CodedOutput.toByteArray(bar);
        byte[] deferred = IOUtil.toByteArray(bar);
        
        assertTrue(coded.length == deferred.length);
        assertEquals(new String(coded), new String(deferred));
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
