//========================================================================
//Copyright 2012 David Yu
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.dyuproject.protostuff.Foo.EnumSample;

/**
 * Test writing/reading to/from streams using writeDelimitedTo, mergeDelimitedFrom, 
 * optWriteDelimitedTo and optMergeDelimitedFrom.
 * 
 * @author David Yu
 * @created Aug 29, 2012
 */
public abstract class DelimiterTest extends AbstractTest
{
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link DeferredOutput} using the given schema.
     */
    protected abstract <T> int writeDelimitedTo(OutputStream out, T message, 
            Schema<T> schema, LinkedBuffer buffer) throws IOException;
    
    /**
     * Deserializes from the byte array and data is merged/saved to the message.
     */
    protected abstract <T> void mergeDelimitedFrom(InputStream in, T message, 
            Schema<T> schema, LinkedBuffer buffer) throws IOException;
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link DeferredOutput} using the given schema.
     */
    protected abstract <T> int optWriteDelimitedTo(OutputStream out, T message, 
            Schema<T> schema, LinkedBuffer buffer) throws IOException;
    
    /**
     * Deserializes from the byte array and data is merged/saved to the message.
     */
    protected abstract <T> boolean optMergeDelimitedFrom(InputStream in, T message, 
            Schema<T> schema, LinkedBuffer buffer) throws IOException;
    
    <T> void verifyOptData(byte[] optData, T message, Schema<T> schema, 
            LinkedBuffer buffer) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = writeDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize;
        assertEquals(expectedSize, data.length);
        
        // compare both outputs
        assertEquals(optData.length, data.length);
        
        /*StringBuilder s1 = new StringBuilder();
        for(int b : optData)
            s1.append(b).append(' ');
        
        StringBuilder s2 = new StringBuilder();
        for(int b : data)
            s2.append(b).append(' ');
        
        assertEquals(s1.toString(), s2.toString());*/
        
        assertTrue(Arrays.equals(optData, data));
        
        
    }
    
    public void testFoo() throws Exception
    {
        Schema<Foo> schema = Foo.getSchema();
        Foo message = SerializableObjects.foo;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = optWriteDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize;
        assertEquals(expectedSize, data.length);
        verifyOptData(data, message, schema, buf());
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        Foo parsedMessage = schema.newMessage();
        boolean merged = optMergeDelimitedFrom(in, parsedMessage, schema, buf(512));
        assertTrue(merged);
        
        assertEquals(message, parsedMessage);
    }
    
    public void testFooEmpty() throws Exception
    {
        Schema<Foo> schema = Foo.getSchema();
        Foo message = new Foo();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = optWriteDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize;
        assertEquals(expectedSize, data.length);
        verifyOptData(data, message, schema, buf());
        
        // empty
        assertEquals(size, 0);
        assertEquals(delimSize, 1);
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        Foo parsedMessage = schema.newMessage();
        boolean merged = optMergeDelimitedFrom(in, parsedMessage, schema, buf());
        assertTrue(merged);
        
        assertEquals(message, parsedMessage);
    }
    
    public void testFooTooLarge() throws Exception
    {
        Schema<Foo> schema = Foo.getSchema();
        Foo message = SerializableObjects.newFoo(
                new Integer[]{90210,-90210, 0, 128}, 
                new String[]{"ab", "cd"}, 
                new Bar[]{SerializableObjects.bar, SerializableObjects.negativeBar},
                new EnumSample[]{EnumSample.TYPE0, EnumSample.TYPE2}, 
                new ByteString[]{ByteString.copyFromUtf8("ef"), ByteString.copyFromUtf8("gh")}, 
                new Boolean[]{true, false}, 
                new Float[]{1234.4321f, -1234.4321f, 0f}, 
                new Double[]{12345678.87654321d, -12345678.87654321d, 0d}, 
                new Long[]{7060504030201l, -7060504030201l, 0l});
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = optWriteDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize; // 258
        assertEquals(expectedSize, data.length);
        verifyOptData(data, message, schema, buf());
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        Foo parsedMessage = schema.newMessage();
        boolean merged = optMergeDelimitedFrom(in, parsedMessage, schema, buf(256));
        assertFalse(merged);
    }
    
    public void testBar() throws Exception
    {
        Schema<Bar> schema = Bar.getSchema();
        Bar message = SerializableObjects.bar;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = optWriteDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize;
        assertEquals(expectedSize, data.length);
        verifyOptData(data, message, schema, buf());
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        Bar parsedMessage = schema.newMessage();
        boolean merged = optMergeDelimitedFrom(in, parsedMessage, schema, buf());
        assertTrue(merged);
        
        assertEquals(message, parsedMessage);
    }
    
    public void testBaz() throws Exception
    {
        Schema<Baz> schema = Baz.getSchema();
        Baz message = SerializableObjects.baz;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = optWriteDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize;
        assertEquals(expectedSize, data.length);
        verifyOptData(data, message, schema, buf());
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        Baz parsedMessage = schema.newMessage();
        boolean merged = optMergeDelimitedFrom(in, parsedMessage, schema, buf());
        assertTrue(merged);
        
        assertEquals(message, parsedMessage);
    }
    
    public void testBarTooLarge2() throws Exception
    {
        Schema<Bar> schema = Bar.getSchema();
        Bar message = new Bar();
        message.setSomeBytes(ByteString.wrap(
                new byte[StringSerializer.THREE_BYTE_LOWER_LIMIT-1]));
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = optWriteDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize;
        assertEquals(expectedSize, data.length);
        verifyOptData(data, message, schema, buf());
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        Bar parsedMessage = schema.newMessage();
        boolean merged = optMergeDelimitedFrom(in, parsedMessage, schema, buf(256));
        assertFalse(merged);
    }
    
    public void testBarTooLarge3() throws Exception
    {
        Schema<Bar> schema = Bar.getSchema();
        Bar message = new Bar();
        message.setSomeBytes(ByteString.wrap(
                new byte[StringSerializer.FOUR_BYTE_LOWER_LIMIT-1]));
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int size = optWriteDelimitedTo(out, message, schema, buf());
        int delimSize = ProtobufOutput.computeRawVarint32Size(size);
        byte[] data = out.toByteArray();
        
        int expectedSize = size + delimSize;
        assertEquals(expectedSize, data.length);
        verifyOptData(data, message, schema, buf());
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        Bar parsedMessage = schema.newMessage();
        boolean merged = optMergeDelimitedFrom(in, parsedMessage, schema, buf(256));
        assertFalse(merged);
    }
}
