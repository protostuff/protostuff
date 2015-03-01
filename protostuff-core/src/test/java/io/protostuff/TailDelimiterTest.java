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

package io.protostuff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Test case for tail-delimited protostuff messages.
 * 
 * @author David Yu
 * @created Oct 5, 2010
 */
public class TailDelimiterTest extends AbstractTest
{

    public <T> int writeListTo(OutputStream out, List<T> messages, Schema<T> schema)
            throws IOException
    {
        return ProtostuffIOUtil.writeListTo(out, messages, schema,
                new LinkedBuffer(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    public <T> List<T> parseListFrom(InputStream in, Schema<T> schema)
            throws IOException
    {
        return ProtostuffIOUtil.parseListFrom(in, schema);
    }

    public void testBar() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<>();
        bars.add(SerializableObjects.bar);
        bars.add(SerializableObjects.negativeBar);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());

        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testEmptyBar() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<>();
        bars.add(new Bar());
        bars.add(new Bar());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());

        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testEmptyBar2() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<>();
        bars.add(new Bar());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());

        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testEmptyBarInner() throws Exception
    {
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());
        ArrayList<Bar> bars = new ArrayList<>();
        bars.add(bar);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, bars, SerializableObjects.bar.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Bar> parsedBars = parseListFrom(in, SerializableObjects.bar.cachedSchema());

        assertTrue(parsedBars.size() == bars.size());
        int i = 0;
        for (Bar b : parsedBars)
            SerializableObjects.assertEquals(bars.get(i++), b);
    }

    public void testFoo() throws Exception
    {
        ArrayList<Foo> foos = new ArrayList<>();
        foos.add(SerializableObjects.foo);
        foos.add(SerializableObjects.foo);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());

        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyList() throws Exception
    {
        ArrayList<Foo> foos = new ArrayList<>();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());

        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFoo() throws Exception
    {
        ArrayList<Foo> foos = new ArrayList<>();
        foos.add(new Foo());
        foos.add(new Foo());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());

        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFoo2() throws Exception
    {
        ArrayList<Foo> foos = new ArrayList<>();
        foos.add(new Foo());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());

        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFooInner() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<>();
        bars.add(new Bar());

        ArrayList<Foo> foos = new ArrayList<>();
        Foo foo = new Foo();
        foo.setSomeBar(bars);

        foos.add(foo);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());

        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

    public void testEmptyFooInner2() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<>();
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());
        bars.add(bar);

        ArrayList<Foo> foos = new ArrayList<>();
        Foo foo = new Foo();
        foo.setSomeBar(bars);

        foos.add(foo);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeListTo(out, foos, SerializableObjects.foo.cachedSchema());
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        List<Foo> parsedFoos = parseListFrom(in, SerializableObjects.foo.cachedSchema());

        assertTrue(parsedFoos.size() == foos.size());
        int i = 0;
        for (Foo f : parsedFoos)
            SerializableObjects.assertEquals(foos.get(i++), f);
    }

}
