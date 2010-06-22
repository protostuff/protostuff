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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * Serialization and deserialization test cases.
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class SerDeserTest extends TestCase
{
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public abstract <T> byte[] toByteArray(T message, Schema<T> schema);
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public <T extends Message<T>> byte[] toByteArray(T message)
    {
        return toByteArray(message, message.cachedSchema());
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link DeferredOutput}.
     */
    public <T extends Message<T>> void writeDelimitedTo(OutputStream out, T message)
    throws IOException
    {
        writeDelimitedTo(out, message, message.cachedSchema());
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link DeferredOutput} using the given schema.
     */
    public abstract <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
    throws IOException;
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public <T> byte[] toByteArrayViaCodedOutput(T message, Schema<T> schema)
    {
        return CodedOutput.toByteArray(message, schema);
    }
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public <T extends Message<T>> byte[] toByteArrayViaCodedOutput(T message)
    {
        return toByteArrayViaCodedOutput(message, message.cachedSchema());
    }
    
    /**
     * If true, a nested message is encoded as a group.
     */
    public abstract boolean isGroupEncoded();
    
    public <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema) 
    throws IOException
    {
        final CodedInput input = new CodedInput(data, offset, length, isGroupEncoded());
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
    }
    
    public <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema) 
    throws IOException
    {
        IOUtil.mergeDelimitedFrom(in, message, schema, isGroupEncoded());
    }
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        Foo cfoo = new Foo();
        Foo dfoo = new Foo();
        
        int expectedSize = ComputedSizeOutput.getSize(fooCompare, fooCompare.cachedSchema(), 
                isGroupEncoded());
        
        byte[] coded = toByteArrayViaCodedOutput(fooCompare);
        assertTrue(coded.length == expectedSize);
        mergeFrom(coded, 0, coded.length, cfoo, cfoo.cachedSchema());
        SerializableObjects.assertEquals(fooCompare, cfoo);

        byte[] output = toByteArray(fooCompare);
        assertTrue(output.length == expectedSize);
        mergeFrom(output, 0, output.length, dfoo, dfoo.cachedSchema());
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar cbar = new Bar();
            Bar dbar = new Bar();            
            
            int expectedSize = ComputedSizeOutput.getSize(barCompare, barCompare.cachedSchema(), 
                    isGroupEncoded());
            
            byte[] coded = toByteArrayViaCodedOutput(barCompare);
            assertTrue(coded.length == expectedSize);
            mergeFrom(coded, 0, coded.length, cbar, cbar.cachedSchema());        
            SerializableObjects.assertEquals(barCompare, cbar);

            byte[] output = toByteArray(barCompare);
            assertTrue(output.length == expectedSize);
            mergeFrom(output, 0, output.length, dbar, dbar.cachedSchema());
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz cbaz = new Baz();
            Baz dbaz = new Baz();            
            
            int expectedSize = ComputedSizeOutput.getSize(bazCompare, bazCompare.cachedSchema(), 
                    isGroupEncoded());
            
            byte[] coded = toByteArrayViaCodedOutput(bazCompare);
            assertTrue(coded.length == expectedSize);
            mergeFrom(coded, 0, coded.length, cbaz, cbaz.cachedSchema());        
            SerializableObjects.assertEquals(bazCompare, cbaz);

            byte[] output = toByteArray(bazCompare);
            assertTrue(output.length == expectedSize);
            mergeFrom(output, 0, output.length, dbaz, dbaz.cachedSchema());
            SerializableObjects.assertEquals(bazCompare, dbaz);
        }
    }
    
    public void testFooSkipMessage() throws Exception
    {
        final CustomSchema<Foo> fooSchema = new CustomSchema<Foo>(foo.cachedSchema())
        {
            public void writeTo(Output output, Foo message) throws IOException
            {
                // 10 is an unknown field
                output.writeMessage(10, baz, false);
                super.writeTo(output, message);
            }
        };
        
        Foo fooCompare = foo;
        Foo cfoo = new Foo();
        Foo dfoo = new Foo();
        
        int expectedSize = ComputedSizeOutput.getSize(fooCompare, fooSchema);
        
        byte[] coded = toByteArrayViaCodedOutput(fooCompare, fooSchema);
        assertTrue(coded.length == expectedSize);
        mergeFrom(coded, 0, coded.length, cfoo, cfoo.cachedSchema());
        SerializableObjects.assertEquals(fooCompare, cfoo);

        byte[] output = toByteArray(fooCompare, fooSchema);
        assertTrue(output.length == expectedSize);
        mergeFrom(output, 0, output.length, dfoo, dfoo.cachedSchema());
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBarSkipMessage() throws Exception
    {
        final CustomSchema<Bar> barSchema = new CustomSchema<Bar>(bar.cachedSchema())
        {
            public void writeTo(Output output, Bar message) throws IOException
            {
                // 10 is an unknown field
                output.writeMessage(10, baz, false);
                super.writeTo(output, message);
            }
        };
        
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar cbar = new Bar();
            Bar dbar = new Bar();            
            
            int expectedSize = ComputedSizeOutput.getSize(barCompare, barSchema, 
                    isGroupEncoded());
            
            byte[] coded = toByteArrayViaCodedOutput(barCompare, barSchema);
            assertTrue(coded.length == expectedSize);
            mergeFrom(coded, 0, coded.length, cbar, barSchema);        
            SerializableObjects.assertEquals(barCompare, cbar);

            byte[] output = toByteArray(barCompare, barSchema);
            assertTrue(output.length == expectedSize);
            mergeFrom(output, 0, output.length, dbar, barSchema);
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    /**
     * Foo shares field numbers (and type) with Bar except that foo's fields are 
     * all repeated (w/c is alright).
     * Bar also shares the same field and type (1&2) with Baz.
     */
    public void testShareFieldNumberAndTypeAndSkipMessage() throws Exception
    {
        final CustomSchema<Bar> barSchema = new CustomSchema<Bar>(bar.cachedSchema())
        {
            public void writeTo(Output output, Bar message) throws IOException
            {
                output.writeMessage(10, baz, false);
                super.writeTo(output, message);
            }
        };
        
        final Baz baz = new Baz();
        baz.setId(1);
        baz.setName("baz");
        final Bar bar = new Bar();
        bar.setBaz(baz);
        bar.setSomeInt(2);
        bar.setSomeString("bar");
        bar.setSomeDouble(100.001d);
        bar.setSomeFloat(10.01f);
        
        byte[] coded = toByteArrayViaCodedOutput(bar, barSchema);
        byte[] output = toByteArray(bar, barSchema);
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded, "UTF-8"), new String(output, "UTF-8"));
        
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
        barCompare.setBaz(new Baz());
        
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
        
        int expectedSize = ComputedSizeOutput.getSize(hhbCompare, hhbCompare.cachedSchema(), 
                isGroupEncoded());
        
        byte[] coded = toByteArrayViaCodedOutput(hhbCompare);
        assertTrue(coded.length == expectedSize);
        mergeFrom(coded, 0, coded.length, chhb, chhb.cachedSchema());      
        assertEquals(hhbCompare, chhb);

        byte[] output = toByteArray(hhbCompare);
        assertTrue(output.length == expectedSize);
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
        Bar parsedBar = (Bar)in.readObject();
        SerializableObjects.assertEquals(parsedBar, bar);
    }
    
    public void testJavaSerializableEmptyBarInner() throws Exception
    {
        Bar bar = new Bar();
        bar.setBaz(new Baz());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(bar);

        byte[] coded = out.toByteArray();
        
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        Bar parsedBar = (Bar)in.readObject();
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
        Foo parsedFoo = (Foo)in.readObject();
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
        Foo parsedFoo = (Foo)in.readObject();
        SerializableObjects.assertEquals(parsedFoo, foo);
    }
    
    public void testJavaSerializableEmptyFooInner() throws Exception
    {
        ArrayList<Bar> bars = new ArrayList<Bar>();
        Bar bar = new Bar();
        bar.setBaz(new Baz());
        bars.add(bar);
        Foo foo = new Foo();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(foo);

        byte[] coded = out.toByteArray();
        
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(coded));
        Foo parsedFoo = (Foo)in.readObject();
        SerializableObjects.assertEquals(parsedFoo, foo);
    }
    
    public void testEmptyBar() throws Exception
    {
        Bar bar = new Bar();
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testEmptyBarInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setBaz(baz);
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testPartialEmptyBar() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeInt(1);
        bar.setBaz(baz);
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testPartialEmptyBarWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("someString");
        bar.setBaz(baz);
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testPartialEmptyBarWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setSomeString("");
        bar.setBaz(baz);
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testPartialEmptyBarInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setId(2);
        bar.setBaz(baz);
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testPartialEmptyBarInnerWithString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("asdfsf");
        bar.setBaz(baz);
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testPartialEmptyBarInnerWithEmptyString() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        baz.setName("");
        bar.setBaz(baz);
        
        byte[] coded = toByteArrayViaCodedOutput(bar);
        byte[] output = toByteArray(bar);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    // empty foo
    
    public void testEmptyFoo() throws Exception
    {
        Foo foo = new Foo();
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testEmptyFooInner() throws Exception
    {
        Bar bar = new Bar();
        Foo foo = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        foo.setSomeBar(bars);
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
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
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
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
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
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
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
    }
    
    public void testPartialEmptyFooInner() throws Exception
    {
        Baz baz = new Baz();
        Bar bar = new Bar();
        bar.setBaz(baz);
        Foo foo = new Foo();
        ArrayList<Bar> bars = new ArrayList<Bar>();
        foo.setSomeBar(bars);
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
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
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
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
        
        byte[] coded = toByteArrayViaCodedOutput(foo);
        byte[] output = toByteArray(foo);
        
        assertTrue(coded.length == output.length);
        assertEquals(new String(coded), new String(output));
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
