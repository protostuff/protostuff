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

import static com.dyuproject.protostuff.SerializableObjects.baz;
import static com.dyuproject.protostuff.SerializableObjects.negativeBaz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.dyuproject.protostuff.Foo.EnumSample;

/**
 * Base test class for simple pojos that have scalar fields (no nested messages/pojos).
 *
 * @author David Yu
 * @created Dec 2, 2010
 */
public abstract class NoNestedMessageTest extends AbstractTest
{
    
    public static final Foo foo = newFoo(
            new Integer[]{90210,-90210, 0}, 
            new String[]{"ab", "cd"}, 
            new EnumSample[]{EnumSample.TYPE0, EnumSample.TYPE2}, 
            new ByteString[]{ByteString.copyFromUtf8("ef"), ByteString.copyFromUtf8("gh")}, 
            new Boolean[]{true, false}, 
            new Float[]{1234.4321f, -1234.4321f, 0f}, 
            new Double[]{12345678.87654321d, -12345678.87654321d, 0d}, 
            new Long[]{7060504030201l, -7060504030201l, 0l});
    
    public static final Bar bar = new Bar(890, "bar", null, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("b2"), true, 150.051f, 2000.0002d, 303030303);
    
    public static final Bar negativeBar = new Bar(-12, "negativeBar", null, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("a1"), true, -130.031f, -1000.0001d, -101010101);

    static Foo newFoo(
            Integer[] someInt, 
            String[] someString, 
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
                null, 
                Arrays.asList(someEnum), 
                Arrays.asList(someBytes), 
                Arrays.asList(someBoolean), 
                Arrays.asList(someFloat), 
                Arrays.asList(someDouble),
                Arrays.asList(someLong));
    }
    
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
    
    public void testPartialEmptyFooWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeBaz(baz);
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
        bar.setSomeBaz(baz);
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
        bar.setSomeBaz(baz);
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
        bar.setSomeBaz(baz);
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
        bar.setSomeBaz(baz);
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
    
    public void testBarWithEmptyString() throws Exception
    {
        Bar bar = new Bar();
        bar.setSomeString("someString");
        
        byte[] output = toByteArray(bar);
        
        Bar parsedBar= new Bar();
        mergeFrom(output, 0, output.length, parsedBar, parsedBar.cachedSchema());
    }
    
}
