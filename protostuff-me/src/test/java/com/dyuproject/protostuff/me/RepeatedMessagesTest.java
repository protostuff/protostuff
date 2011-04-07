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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Testcase for ser/deser of multiple messages.
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public abstract class RepeatedMessagesTest extends AbstractTest
{
    
    protected abstract void writeListTo(OutputStream out, Vector messages, 
            Schema schema) throws IOException;

    protected abstract Vector parseListFrom(InputStream in, Schema schema)
    throws IOException;
    
    public void testBar() throws Exception
    {
        Vector bars = new Vector();
        bars.add(SerializableObjects.bar);
        bars.add(SerializableObjects.negativeBar);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testEmptyBar() throws Exception
    {
        Vector bars = new Vector();
        bars.add(new Bar());
        bars.add(new Bar());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testEmptyBar2() throws Exception
    {
        Vector bars = new Vector();
        bars.add(new Bar());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testEmptyBarInner() throws Exception
    {
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());
        Vector bars = new Vector();
        bars.add(bar);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());
        
        assertTrue(parsedBars.size() == bars.size());
        int i=0;
        for(Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }
    
    public void testFoo() throws Exception
    {
        Vector foos = new Vector();
        foos.add(SerializableObjects.foo);
        foos.add(SerializableObjects.foo);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFoo() throws Exception
    {
        Vector foos = new Vector();
        foos.add(new Foo());
        foos.add(new Foo());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFoo2() throws Exception
    {
        Vector foos = new Vector();
        foos.add(new Foo());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFooInner() throws Exception
    {
        Vector bars = new Vector();
        bars.add(new Bar());
        
        Vector foos = new Vector();
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        
        foos.add(foo);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }
    
    public void testEmptyFooInner2() throws Exception
    {
        Vector bars = new Vector();
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());
        bars.add(bar);
        
        Vector foos = new Vector();
        Foo foo = new Foo();
        foo.setSomeBar(bars);
        
        foos.add(foo);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());
        
        assertTrue(parsedFoos.size() == foos.size());
        int i=0;
        for(Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

}
