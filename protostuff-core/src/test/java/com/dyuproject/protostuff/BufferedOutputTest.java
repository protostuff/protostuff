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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Test case for {@code BufferedOutput}.
 *
 * @author David Yu
 * @created May 18, 2010
 */
public class BufferedOutputTest extends SerDeserTest
{
    
    public boolean isGroupEncoded()
    {
        return false;
    }
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public static <T> byte[] getByteArray(T message, Schema<T> schema)
    {
        BufferedOutput output = new BufferedOutput();
        try
        {
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.toByteArray();
    }
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return getByteArray(message, schema);
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema.
     */
    public <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
    throws IOException
    {
        BufferedOutput output = new BufferedOutput();
        schema.writeTo(output, message);
        CodedOutput.writeRawVarInt32Bytes(out, output.getSize());
        output.streamTo(out);
    }
    
    /*static void print(byte[] data)
    {
        System.err.println("_______");
        for(int i=0; i<data.length; i++)
        {
            System.err.print((int)data[i] + " ");
        }
        System.err.println("(" + data.length + ")");
    }*/
    
    //128-byte string
    static final String STRING_128_BYTES = 
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567890" +
        "1234567!";
    
    public void testString128Bytes() throws Exception
    {
        assertTrue(StringSerializer.STRING.ser(STRING_128_BYTES).length == 128);
    }
    
    public void testEqualBufferSize() throws Exception
    {
        Bar bar = new Bar();
        bar.setSomeString("................................................................" +
                "...........................................................................");
        
        int expectedSize = ComputedSizeOutput.getSize(bar);
        
        BufferedOutput output = new BufferedOutput(expectedSize);
        bar.cachedSchema().writeTo(output, bar);
        byte[] data = output.toByteArray();
        
        assertTrue(data.length == expectedSize);
        
        Bar parsedBar = new Bar();
        IOUtil.mergeFrom(data, parsedBar);
        SerializableObjects.assertEquals(parsedBar, bar);
    }
    
    public void testStringValueLimitCopy() throws Exception
    {
        Bar bar = new Bar();
        bar.setSomeString(STRING_128_BYTES);
        
        int expectedSize = ComputedSizeOutput.getSize(bar);
        
        BufferedOutput output = new BufferedOutput(expectedSize);
        bar.cachedSchema().writeTo(output, bar);
        byte[] data = output.toByteArray();
        
        assertTrue(data.length == expectedSize);
        
        Bar parsedBar = new Bar();
        IOUtil.mergeFrom(data, parsedBar);
        SerializableObjects.assertEquals(parsedBar, bar);
    }
    
    public void testSmallerBufferSize() throws Exception
    {
        Bar bar = new Bar();
        bar.setSomeString("................................................................" +
                "...........................................................................");
        
        int expectedSize = ComputedSizeOutput.getSize(bar);
        
        BufferedOutput output = new BufferedOutput(expectedSize-1);
        bar.cachedSchema().writeTo(output, bar);
        byte[] data = output.toByteArray();
        
        assertTrue(data.length == expectedSize);
        
        Bar parsedBar = new Bar();
        IOUtil.mergeFrom(data, parsedBar);
        SerializableObjects.assertEquals(parsedBar, bar);
    }
    
    public void testSmallerBufferSize2() throws Exception
    {
        String str = "................................................................" +
        "...........................................................................";
        Baz baz = new Baz();
        baz.setName(str);
        baz.setTimestamp(System.currentTimeMillis());
        
        Bar bar = new Bar();
        bar.setSomeString(str);
        bar.setBaz(baz);
        
        int expectedSize = ComputedSizeOutput.getSize(bar);
        
        BufferedOutput output = new BufferedOutput(64);
        bar.cachedSchema().writeTo(output, bar);
        byte[] data = output.toByteArray();
        
        assertTrue(data.length == expectedSize);
        
        Bar parsedBar = new Bar();
        IOUtil.mergeFrom(data, parsedBar);
        SerializableObjects.assertEquals(parsedBar, bar);
    }
    
    public void testSmallerBufferSize3() throws Exception
    {
        String str = "................................................................" +
        "...........................................................................";
        Baz baz = new Baz();
        baz.setName(str);
        baz.setTimestamp(System.currentTimeMillis());
        
        Bar bar = new Bar();
        bar.setSomeString(str);
        bar.setBaz(baz);
        
        ArrayList<Bar> bars = new ArrayList<Bar>();
        bars.add(bar);
        
        Foo foo = new Foo();
        ArrayList<String> someString = new ArrayList<String>();
        someString.add(str);
        
        String test = STRING_128_BYTES;
        
        someString.add(test);
        
        foo.setSomeString(someString);
        foo.setSomeBar(bars);
        
        int expectedSize = ComputedSizeOutput.getSize(foo);
        
        BufferedOutput output = new BufferedOutput(64);
        foo.cachedSchema().writeTo(output, foo);
        byte[] data = output.toByteArray();
        
        assertTrue(data.length == expectedSize);
        
        Foo parsedFoo = new Foo();
        IOUtil.mergeFrom(data, parsedFoo);
        SerializableObjects.assertEquals(parsedFoo, foo);
    }


}
