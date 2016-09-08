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

package io.protostuff;

import static io.protostuff.SerializableObjects.bar;
import static io.protostuff.SerializableObjects.baz;
import static io.protostuff.SerializableObjects.foo;
import static io.protostuff.SerializableObjects.negativeBar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Serialization and deserialization test cases.
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class SerDeserTest extends StandardTest
{

    /**
     * Serializes the {@code message} (delimited) into an {@link OutputStream} via {@link DeferredOutput}.
     */
    protected <T extends Message<T>> void writeDelimitedTo(OutputStream out, T message)
            throws IOException
    {
        writeDelimitedTo(out, message, message.cachedSchema());
    }

    /**
     * Serializes the {@code message} (delimited) into an {@link OutputStream} via {@link DeferredOutput} using the
     * given schema.
     */
    protected abstract <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
            throws IOException;

    /**
     * Deserializes from the byte array and data is merged/saved to the message.
     */
    protected abstract <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema)
            throws IOException;

    public void testFooSkipMessage() throws Exception
    {
        final CustomSchema<Foo> fooSchema = new CustomSchema<Foo>(foo.cachedSchema())
        {
            @Override
            public void writeTo(Output output, Foo message) throws IOException
            {
                // 10 is an unknown field
                output.writeObject(10, baz, Baz.getSchema(), false);
                super.writeTo(output, message);
            }
        };

        Foo fooCompare = foo;
        Foo dfoo = new Foo();

        byte[] output = toByteArray(fooCompare, fooSchema);
        mergeFrom(output, 0, output.length, dfoo, dfoo.cachedSchema());
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }

    public void testBarSkipMessage() throws Exception
    {
        final CustomSchema<Bar> barSchema = new CustomSchema<Bar>(bar.cachedSchema())
        {
            @Override
            public void writeTo(Output output, Bar message) throws IOException
            {
                // 10 is an unknown field
                output.writeObject(10, baz, Baz.getSchema(), false);
                super.writeTo(output, message);
            }
        };

        for (Bar barCompare : new Bar[] { bar, negativeBar })
        {
            Bar dbar = new Bar();

            byte[] output = toByteArray(barCompare, barSchema);
            mergeFrom(output, 0, output.length, dbar, barSchema);
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }

    /**
     * Foo shares field numbers (and type) with Bar except that foo's fields are all repeated (w/c is alright). Bar also
     * shares the same field and type (1&2) with Baz.
     */
    public void testShareFieldNumberAndTypeAndSkipMessage() throws Exception
    {
        final CustomSchema<Bar> barSchema = new CustomSchema<Bar>(bar.cachedSchema())
        {
            @Override
            public void writeTo(Output output, Bar message) throws IOException
            {
                output.writeObject(10, baz, Baz.getSchema(), false);
                super.writeTo(output, message);
            }
        };

        final Baz baz = new Baz();
        baz.setId(1);
        baz.setName("baz");
        final Bar bar = new Bar();
        bar.setSomeBaz(baz);
        bar.setSomeInt(2);
        bar.setSomeString("bar");
        bar.setSomeDouble(100.001d);
        bar.setSomeFloat(10.01f);

        byte[] coded = toByteArray(bar, barSchema);

        Foo foo = new Foo();
        // we expect this to succeed, skipping the baz field.
        mergeFrom(coded, 0, coded.length, foo, foo.cachedSchema());

        assertTrue(bar.getSomeInt() == foo.getSomeInt().get(0));
        assertEquals(bar.getSomeString(), foo.getSomeString().get(0));
        assertTrue(bar.getSomeDouble() == foo.getSomeDouble().get(0));
        assertTrue(bar.getSomeFloat() == foo.getSomeFloat().get(0));
    }

    public void testFooDelimited() throws Exception
    {
        Foo fooCompare = SerializableObjects.foo;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, fooCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Foo foo = new Foo();
        mergeDelimitedFrom(in, foo, foo.cachedSchema());

        SerializableObjects.assertEquals(foo, fooCompare);
    }

    public void testEmptyFooDelimited() throws Exception
    {
        Foo fooCompare = new Foo();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, fooCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Foo foo = new Foo();
        mergeDelimitedFrom(in, foo, foo.cachedSchema());

        SerializableObjects.assertEquals(foo, fooCompare);
    }

    public void testEmptyInnerFooDelimited() throws Exception
    {
        Foo fooCompare = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(new Bar());
        fooCompare.setSomeBar(bars);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, fooCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Foo foo = new Foo();
        mergeDelimitedFrom(in, foo, foo.cachedSchema());

        SerializableObjects.assertEquals(foo, fooCompare);
    }

    public void testBarDelimited() throws Exception
    {
        Bar barCompare = SerializableObjects.bar;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, barCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Bar bar = new Bar();
        mergeDelimitedFrom(in, bar, bar.cachedSchema());

        SerializableObjects.assertEquals(bar, barCompare);
    }

    public void testEmptyBarDelimited() throws Exception
    {
        Bar barCompare = new Bar();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, barCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Bar bar = new Bar();
        mergeDelimitedFrom(in, bar, bar.cachedSchema());

        SerializableObjects.assertEquals(bar, barCompare);
    }

    public void testEmptyInnerBarDelimited() throws Exception
    {
        Bar barCompare = new Bar();
        barCompare.setSomeBaz(new Baz());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, barCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Bar bar = new Bar();
        mergeDelimitedFrom(in, bar, bar.cachedSchema());

        SerializableObjects.assertEquals(bar, barCompare);
    }

    public void testBazDelimited() throws Exception
    {
        Baz bazCompare = SerializableObjects.baz;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, bazCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Baz baz = new Baz();
        mergeDelimitedFrom(in, baz, baz.cachedSchema());

        SerializableObjects.assertEquals(baz, bazCompare);
    }

    public void testEmptyBazDelimited() throws Exception
    {
        Baz bazCompare = new Baz();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeDelimitedTo(out, bazCompare);
        byte[] data = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Baz baz = new Baz();
        mergeDelimitedFrom(in, baz, baz.cachedSchema());

        SerializableObjects.assertEquals(baz, bazCompare);
    }

    public void testJavaSerializableGraphIOUtil() throws Exception
    {
        ClubFounder founder = new ClubFounder();

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 25; i++)
            b.append("1234567890");

        // 250 length string
        // ~253 length message

        founder.setName(b.toString());

        WrapsClubFounder wrapper = new WrapsClubFounder(founder);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(wrapper);

        byte[] coded = out.toByteArray();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        WrapsClubFounder parsedWrapper = (WrapsClubFounder) in.readObject();

        assertEquals(founder.getName(), parsedWrapper.getClubFounder().getName());
    }

    /**
     * HasHasBar wraps an object without a schema. That object will have to be serialized via the default java
     * serialization and it will be delimited.
     * <p>
     * HasBar wraps a message {@link Bar}.
     */
    public void testJavaSerializable() throws Exception
    {
        HasHasBar hhbCompare = new HasHasBar("hhb",
                new HasBar(12345, "hb", SerializableObjects.bar));
        HasHasBar dhhb = new HasHasBar();

        byte[] output = toByteArray(hhbCompare);

        mergeFrom(output, 0, output.length, dhhb, dhhb.cachedSchema());
        assertEquals(hhbCompare, dhhb);
    }

    public void testJavaSerializableEmptyBar() throws Exception
    {
        Bar bar = new Bar();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(bar);

        byte[] coded = out.toByteArray();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        Bar parsedBar = (Bar) in.readObject();
        SerializableObjects.assertEquals(parsedBar, bar);
    }

    public void testJavaSerializableEmptyBarInner() throws Exception
    {
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(bar);

        byte[] coded = out.toByteArray();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        Bar parsedBar = (Bar) in.readObject();
        SerializableObjects.assertEquals(parsedBar, bar);
    }

    public void testJavaSerializableEmptyFoo() throws Exception
    {
        Foo foo = new Foo();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(foo);

        byte[] coded = out.toByteArray();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        Foo parsedFoo = (Foo) in.readObject();
        SerializableObjects.assertEquals(parsedFoo, foo);
    }

    public void testJavaSerializableEmptyFoo2() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        Bar bar = new Bar();
        bars.add(bar);
        Foo foo = new Foo();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(foo);

        byte[] coded = out.toByteArray();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        Foo parsedFoo = (Foo) in.readObject();
        SerializableObjects.assertEquals(parsedFoo, foo);
    }

    public void testJavaSerializableEmptyFooInner() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        Bar bar = new Bar();
        bar.setSomeBaz(new Baz());
        bars.add(bar);
        Foo foo = new Foo();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(foo);

        byte[] coded = out.toByteArray();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        Foo parsedFoo = (Foo) in.readObject();
        SerializableObjects.assertEquals(parsedFoo, foo);
    }

    public static String repeatChar(char c, int size)
    {
        StringBuilder sb = new StringBuilder(size);
        while (size-- > 0)
            sb.append(c);

        return sb.toString();
    }

    static void assertEquals(HasHasBar h1, HasHasBar h2)
    {
        // true if both are null
        if (h1 == h2)
            return;

        assertEquals(h1.getName(), h2.getName());
        assertEquals(h1.getHasBar(), h2.getHasBar());
    }

    static void assertEquals(HasBar h1, HasBar h2)
    {
        // true if both are null
        if (h1 == h2)
            return;

        assertTrue(h1.getId() == h2.getId());
        assertEquals(h1.getName(), h2.getName());
        SerializableObjects.assertEquals(h1.getBar(), h2.getBar());
    }

}
