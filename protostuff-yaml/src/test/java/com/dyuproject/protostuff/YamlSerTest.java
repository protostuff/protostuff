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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Serialization tests for yaml.
 *
 * @author David Yu
 * @created Jun 28, 2010
 */
public class YamlSerTest extends AbstractTest
{
    
    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return YamlIOUtil.toByteArray(message, schema, buf());
    }
    
    public <T> int writeTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        return YamlIOUtil.writeTo(out, message, schema, buf());
    }
    
    public <T> int writeListTo(OutputStream out, List<T> messages, Schema<T> schema) 
    throws IOException
    {
        return YamlIOUtil.writeListTo(out, messages, schema, buf());
    }
    
    public <T> int writeListTo(LinkedBuffer buffer, List<T> messages, Schema<T> schema) 
    throws IOException
    {
        return YamlIOUtil.writeListTo(buffer, messages, schema);
    }
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] data = toByteArray(fooCompare, fooCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = writeTo(out, fooCompare, fooCompare.cachedSchema());
        
        assertTrue(data.length == total);
        
        byte[] data2 = out.toByteArray();
        
        assertTrue(data.length == data2.length);

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
        print(text);
    }
    
    public void testFooEmpty() throws Exception
    {
        Foo fooCompare = new Foo();
        
        byte[] data = toByteArray(fooCompare, fooCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = writeTo(out, fooCompare, fooCompare.cachedSchema());
        
        assertTrue(data.length == total);
        
        byte[] data2 = out.toByteArray();
        
        assertTrue(data.length == data2.length);

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
        print(text);
    }
    
    public void testFooList() throws Exception
    {
        Foo fooCompare = foo;
        ArrayList<Foo> list = new ArrayList<Foo>();
        list.add(fooCompare);
        list.add(fooCompare);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = writeListTo(out, list, fooCompare.cachedSchema());
        
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        LinkedBuffer buffer = new LinkedBuffer(512);
        int total2 = writeListTo(buffer, list, fooCompare.cachedSchema());
        LinkedBuffer.writeTo(out2, buffer);
        
        byte[] data = out.toByteArray();
        byte[] data2 = out2.toByteArray();
        
        assertTrue(data.length == total);
        assertTrue(data2.length == total2);
        assertTrue(total == total2);
        
        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);
        
        assertEquals(text, text2);
        print(text);
        //System.err.println(text);
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            byte[] data = toByteArray(barCompare, barCompare.cachedSchema());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int total = writeTo(out, barCompare, barCompare.cachedSchema());
            
            assertTrue(data.length == total);
            
            byte[] data2 = out.toByteArray();
            
            assertTrue(data.length == data2.length);

            String text = STRING.deser(data);
            String text2 = STRING.deser(data2);

            assertEquals(text, text2);
        }
    }
    
    public void testBarEmpty() throws Exception
    {
        Bar barCompare = new Bar();
        
        byte[] data = toByteArray(barCompare, barCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = writeTo(out, barCompare, barCompare.cachedSchema());
        
        assertTrue(data.length == total);
        
        byte[] data2 = out.toByteArray();
        
        assertTrue(data.length == data2.length);

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
    }
    
    public void testBarList() throws Exception
    {
        Bar barCompare = bar;
        ArrayList<Bar> list = new ArrayList<Bar>();
        list.add(bar);
        list.add(negativeBar);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = writeListTo(out, list, barCompare.cachedSchema());
        
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        LinkedBuffer buffer = new LinkedBuffer(512);
        int total2 = writeListTo(buffer, list, barCompare.cachedSchema());
        LinkedBuffer.writeTo(out2, buffer);
        
        byte[] data = out.toByteArray();
        byte[] data2 = out2.toByteArray();
        
        assertTrue(data.length == total);
        assertTrue(data2.length == total2);
        assertTrue(total == total2);
        
        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);
        
        assertEquals(text, text2);
        print(text);
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            byte[] data = toByteArray(bazCompare, bazCompare.cachedSchema());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int total = writeTo(out, bazCompare, bazCompare.cachedSchema());
            
            assertTrue(data.length == total);
            
            byte[] data2 = out.toByteArray();
            
            assertTrue(data.length == data2.length);

            String text = STRING.deser(data);
            String text2 = STRING.deser(data2);

            assertEquals(text, text2);
        }
    }
    
    public void testBazEmpty() throws Exception
    {
        Baz bazCompare = new Baz();
        
        byte[] data = toByteArray(bazCompare, bazCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = writeTo(out, bazCompare, bazCompare.cachedSchema());
        
        assertTrue(data.length == total);
        
        byte[] data2 = out.toByteArray();
        
        assertTrue(data.length == data2.length);

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
    }
    
    public void testBazList() throws Exception
    {
        Baz bazCompare = baz;
        ArrayList<Baz> list = new ArrayList<Baz>();
        list.add(baz);
        list.add(negativeBaz);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int total = writeListTo(out, list, bazCompare.cachedSchema());
        
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        LinkedBuffer buffer = new LinkedBuffer(512);
        int total2 = writeListTo(buffer, list, bazCompare.cachedSchema());
        LinkedBuffer.writeTo(out2, buffer);
        
        byte[] data = out.toByteArray();
        byte[] data2 = out2.toByteArray();
        
        assertTrue(data.length == total);
        assertTrue(data2.length == total2);
        assertTrue(total == total2);
        
        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);
        
        assertEquals(text, text2);
        print(text);
    }
    
    public void testEmptyInnerBar() throws Exception
    {
        Bar barCompare = new Bar();
        Baz baz = new Baz();
        barCompare.setSomeBaz(baz);
        
        byte[] data = toByteArray(barCompare, barCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(out, barCompare, barCompare.cachedSchema());
        byte[] data2 = out.toByteArray();

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
        print(text);
    }
    
    public void testEmptyInnerFoo() throws Exception
    {
        Foo fooCompare = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        fooCompare.setSomeBar(bars);
        
        byte[] data = toByteArray(fooCompare, fooCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(out, fooCompare, fooCompare.cachedSchema());
        byte[] data2 = out.toByteArray();

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
        print(text);
    }
    
    public void testEmptyDeeperFoo() throws Exception
    {
        Bar bar = new Bar();
        Baz baz = new Baz();
        bar.setSomeBaz(baz);
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        
        byte[] data = toByteArray(foo, foo.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(out, foo, foo.cachedSchema());
        byte[] data2 = out.toByteArray();

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
        print(text);
    }

    static void print(String str)
    {
        //System.err.println(str);
    }
}
