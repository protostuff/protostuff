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

package com.dyuproject.protostuff;

import static com.dyuproject.protostuff.SerializableObjects.bar;
import static com.dyuproject.protostuff.SerializableObjects.baz;
import static com.dyuproject.protostuff.SerializableObjects.foo;
import static com.dyuproject.protostuff.SerializableObjects.negativeBar;
import static com.dyuproject.protostuff.SerializableObjects.negativeBaz;

import java.io.IOException;
import java.util.ArrayList;

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
    protected abstract <T> byte[] toByteArray(T message, Schema<T> schema);
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    protected <T extends Message<T>> byte[] toByteArray(T message)
    {
        return toByteArray(message, message.cachedSchema());
    }
    
    /**
     * Deserializes from the byte array and data is merged/saved to the message.
     */
    protected abstract <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema) 
    throws IOException;
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        Foo dfoo = new Foo();

        byte[] output = toByteArray(fooCompare);
        mergeFrom(output, 0, output.length, dfoo, dfoo.cachedSchema());
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar dbar = new Bar();            

            byte[] output = toByteArray(barCompare);
            mergeFrom(output, 0, output.length, dbar, dbar.cachedSchema());
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
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
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        foo.setSomeBar(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFoo() throws Exception
    {
        Bar bar = new Bar();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        Foo foo = new Foo();
        ArrayList<Integer> someInt = new ArrayList<Integer>();
        someInt.add(1);
        foo.setSomeInt(someInt);
        
        foo.setSomeBar(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setBaz(baz);
        Foo foo = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        foo.setSomeBar(bars);
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("someString");
        foo.setSomeString(strings);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setBaz(baz);
        Foo foo = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        foo.setSomeBar(bars);
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("");
        foo.setSomeString(strings);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setBaz(baz);
        Foo foo = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        foo.setSomeBar(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooInnerWithString() throws Exception
    {
        Baz baz = new Baz();
        baz.setName("asdfsf");
        Bar bar = new Bar();
        bar.setBaz(baz);
        Foo foo = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        foo.setSomeBar(bars);
        
        byte[] output = toByteArray(foo);
        
        Foo parsedFoo = new Foo();
        mergeFrom(output, 0, output.length, parsedFoo, parsedFoo.cachedSchema());
    }
    
    public void testPartialEmptyFooInnerWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        baz.setName("");
        Bar bar = new Bar();
        bar.setBaz(baz);
        Foo foo = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        foo.setSomeBar(bars);
        
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
        bar.setBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBar() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeInt(1);
        bar.setBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("someString");
        bar.setBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("");
        bar.setBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setId(2);
        bar.setBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarInnerWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("asdfsf");
        bar.setBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
    public void testPartialEmptyBarInnerWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("");
        bar.setBaz(baz);
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }

}
