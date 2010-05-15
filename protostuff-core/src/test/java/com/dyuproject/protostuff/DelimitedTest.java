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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Testcase for multiple messages written as delimited bytes.
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public class DelimitedTest extends TestCase
{
    
    public void testBar() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(SerializableObjects.bar);
        bars.add(SerializableObjects.negativeBar);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = IOUtil.parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testEmptyBar() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        bars.add(new Bar());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = IOUtil.parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testEmptyBar2() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = IOUtil.parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testEmptyBarInner() throws Exception
    {
        Bar bar = new Bar();
        bar.setBaz(new Baz());
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = IOUtil.parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testFoo() throws Exception
    {
        ArrayList<Foo> foos = new ArrayList<Foo>();
        foos.add(SerializableObjects.foo);
        foos.add(SerializableObjects.foo);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = IOUtil.parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFoo() throws Exception
    {
        ArrayList<Foo> foos = new ArrayList<Foo>();
        foos.add(new Foo());
        foos.add(new Foo());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = IOUtil.parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFoo2() throws Exception
    {
        ArrayList<Foo> foos = new ArrayList<Foo>();
        foos.add(new Foo());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = IOUtil.parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFooInner() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        
        ArrayList<Foo> foos = new ArrayList<Foo>();
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        
        foos.add(foo);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = IOUtil.parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFooInner2() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        Bar bar = new Bar();
        bar.setBaz(new Baz());
        bars.add(bar);
        
        ArrayList<Foo> foos = new ArrayList<Foo>();
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        
        foos.add(foo);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = IOUtil.parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

}
