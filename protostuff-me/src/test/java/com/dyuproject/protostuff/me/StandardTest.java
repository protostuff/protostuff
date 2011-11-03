//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.me;

import java.io.IOException;
import java.util.Vector;

/**
 * Base for all ser/deser test cases.
 *
 * @author David Yu
 * @created Oct 8, 2010
 */
public abstract class StandardTest extends AbstractTest
{
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    protected abstract byte[] toByteArray(Message message, Schema schema);
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    protected byte[] toByteArray(Message message)
    {
        return toByteArray(message, message.cachedSchema());
    }
    
    /**
     * Deserializes from the byte array and data is merged/saved to the message.
     */
    protected abstract void mergeFrom(byte[] data, int offset, int length, Message message, Schema schema) 
    throws IOException;
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = SerializableObjects.foo;
        Foo dfoo = new Foo();

        byte[] output = toByteArray(fooCompare);
        mergeFrom(output, 0, output.length, dfoo, dfoo.cachedSchema());
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{SerializableObjects.bar, SerializableObjects.negativeBar})
        {
            Bar dbar = new Bar();            

            byte[] output = toByteArray(barCompare);
            mergeFrom(output, 0, output.length, dbar, dbar.cachedSchema());
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{SerializableObjects.baz, SerializableObjects.negativeBaz})
        {
            Baz dbaz = new Baz();            

            byte[] output = toByteArray(bazCompare);
            mergeFrom(output, 0, output.length, dbaz, dbaz.cachedSchema());
            SerializableObjects.assertEquals(bazCompare, dbaz);
        }
    }
    
    // empty foo
    
    public void testEmptyFoo() throws Exception
    {
        Foo foo = new Foo();
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testEmptyFooInner() throws Exception
    {
        Bar bar = new Bar();
        Foo foo = new Foo();
        Vector bars = new Vector();
        bars.addElement(bar);
        foo.setSomeBarList(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFoo() throws Exception
    {
        Bar bar = new Bar();
        Vector bars = new Vector();
        bars.addElement(bar);
        Foo foo = new Foo();
        Vector someInt = new Vector();
        someInt.addElement(new Integer(1));
        foo.setSomeIntList(someInt);
        
        foo.setSomeBarList(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeBaz(baz);
        Foo foo = new Foo();
        Vector bars = new Vector();
        foo.setSomeBarList(bars);
        Vector strings = new Vector();
        strings.addElement("someString");
        foo.setSomeStringList(strings);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeBaz(baz);
        Foo foo = new Foo();
        Vector bars = new Vector();
        foo.setSomeBarList(bars);
        Vector strings = new Vector();
        strings.addElement("");
        foo.setSomeStringList(strings);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeBaz(baz);
        Foo foo = new Foo();
        Vector bars = new Vector();
        foo.setSomeBarList(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooInnerWithString() throws Exception
    {
        Baz baz = new Baz();
        baz.setName("asdfsf");
        Bar bar = new Bar();
        bar.setSomeBaz(baz);
        Foo foo = new Foo();
        Vector bars = new Vector();
        foo.setSomeBarList(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooInnerWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        baz.setName("");
        Bar bar = new Bar();
        bar.setSomeBaz(baz);
        Foo foo = new Foo();
        Vector bars = new Vector();
        foo.setSomeBarList(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    // bar
    
    public void testEmptyBar() throws Exception
    {
        Bar bar = new Bar();
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testEmptyBarInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBar() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeInt(1);
        bar.setSomeBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("someString");
        bar.setSomeBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("");
        bar.setSomeBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setId(2);
        bar.setSomeBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarInnerWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("asdfsf");
        bar.setSomeBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarInnerWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("");
        bar.setSomeBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }

}
