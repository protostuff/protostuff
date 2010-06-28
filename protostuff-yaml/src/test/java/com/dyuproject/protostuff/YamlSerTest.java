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
import static com.dyuproject.protostuff.StringSerializer.STRING;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * Serialization tests for yaml.
 *
 * @author David Yu
 * @created Jun 28, 2010
 */
public class YamlSerTest extends TestCase
{
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] data = YamlIOUtil.toByteArray(fooCompare, fooCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YamlIOUtil.writeTo(out, fooCompare, fooCompare.cachedSchema());
        byte[] data2 = out.toByteArray();

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
    }
    
    public void testFooEmpty() throws Exception
    {
        Foo fooCompare = new Foo();
        
        byte[] data = YamlIOUtil.toByteArray(fooCompare, fooCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YamlIOUtil.writeTo(out, fooCompare, fooCompare.cachedSchema());
        byte[] data2 = out.toByteArray();

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
        
        YamlIOUtil.writeListTo(out, list, fooCompare.cachedSchema());
        
        byte[] data = out.toByteArray();
        print(STRING.deser(data));
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            byte[] data = YamlIOUtil.toByteArray(barCompare, barCompare.cachedSchema());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            YamlIOUtil.writeTo(out, barCompare, barCompare.cachedSchema());
            byte[] data2 = out.toByteArray();

            String text = STRING.deser(data);
            String text2 = STRING.deser(data2);

            assertEquals(text, text2);
        }
    }
    
    public void testBarEmpty() throws Exception
    {
        Bar barCompare = new Bar();
        
        byte[] data = YamlIOUtil.toByteArray(barCompare, barCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YamlIOUtil.writeTo(out, barCompare, barCompare.cachedSchema());
        byte[] data2 = out.toByteArray();

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
        print(text);
    }
    
    public void testBarList() throws Exception
    {
        Bar barCompare = bar;
        ArrayList<Bar> list = new ArrayList<Bar>();
        list.add(bar);
        list.add(negativeBar);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        YamlIOUtil.writeListTo(out, list, barCompare.cachedSchema());
        
        byte[] data = out.toByteArray();
        print(STRING.deser(data));
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            byte[] data = YamlIOUtil.toByteArray(bazCompare, bazCompare.cachedSchema());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            YamlIOUtil.writeTo(out, bazCompare, bazCompare.cachedSchema());
            byte[] data2 = out.toByteArray();

            String text = STRING.deser(data);
            String text2 = STRING.deser(data2);

            assertEquals(text, text2);
        }
    }
    
    public void testBazEmpty() throws Exception
    {
        Baz bazCompare = new Baz();
        
        byte[] data = YamlIOUtil.toByteArray(bazCompare, bazCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YamlIOUtil.writeTo(out, bazCompare, bazCompare.cachedSchema());
        byte[] data2 = out.toByteArray();

        String text = STRING.deser(data);
        String text2 = STRING.deser(data2);

        assertEquals(text, text2);
        print(text);
    }
    
    public void testBazList() throws Exception
    {
        Baz bazCompare = baz;
        ArrayList<Baz> list = new ArrayList<Baz>();
        list.add(baz);
        list.add(negativeBaz);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        YamlIOUtil.writeListTo(out, list, bazCompare.cachedSchema());
        
        byte[] data = out.toByteArray();
        print(STRING.deser(data));
    }
    
    public void testEmptyInnerBar() throws Exception
    {
        Bar barCompare = new Bar();
        Baz baz = new Baz();
        barCompare.setBaz(baz);
        
        byte[] data = YamlIOUtil.toByteArray(barCompare, barCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YamlIOUtil.writeTo(out, barCompare, barCompare.cachedSchema());
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
        
        byte[] data = YamlIOUtil.toByteArray(fooCompare, fooCompare.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YamlIOUtil.writeTo(out, fooCompare, fooCompare.cachedSchema());
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
        bar.setBaz(baz);
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        
        byte[] data = YamlIOUtil.toByteArray(foo, foo.cachedSchema());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YamlIOUtil.writeTo(out, foo, foo.cachedSchema());
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
