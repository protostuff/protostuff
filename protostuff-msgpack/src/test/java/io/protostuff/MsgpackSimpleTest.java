//========================================================================
//Copyright (C) 2016 Alex Shvid
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.msgpack.core.MessagePack;

import io.protostuff.runtime.RuntimeSchema;

/**
 * Simple test for msgpack objects
 * 
 * @author Alex Shvid
 *
 */

public class MsgpackSimpleTest
{

    public static final Schema<ExampleMessage> SCHEMA = RuntimeSchema.getSchema(ExampleMessage.class);
    public static final Schema<FooMessage> FOO_SCHEMA = RuntimeSchema.getSchema(FooMessage.class);

    // @Test
    public void testSimple() throws IOException
    {

        InnerMessage inner = new InnerMessage();
        inner.inner1 = "inner".getBytes();
        inner.inner2 = 12.34;

        ExampleMessage message = new ExampleMessage();
        message.field1 = 42;
        message.field2 = "hello";
        message.field3 = Arrays.asList(true, false, true);
        message.field4 = inner;

        byte[] blob = MsgpackIOUtil.toByteArray(message, SCHEMA, false);

        /**
         * Compatible with
         * 
         * http://kawanet.github.io/msgpack-lite/
         * 
         */

        // System.out.println(Arrays.toString(blob));

        ExampleMessage actual = new ExampleMessage();

        MsgpackIOUtil.mergeFrom(blob, actual, SCHEMA, false);

        Assert.assertEquals(message.field1, actual.field1);
        Assert.assertEquals(message.field2, actual.field2);
        Assert.assertEquals(message.field3, actual.field3);

        Assert.assertTrue(Arrays.equals(message.field4.inner1, actual.field4.inner1));
        Assert.assertTrue(Math.abs(message.field4.inner2 - message.field4.inner2) < 0.001);

    }

    @Test
    public void testXIO() throws IOException
    {

        InnerMessage inner = new InnerMessage();
        inner.inner1 = "inner".getBytes();
        inner.inner2 = 12.34;

        ExampleMessage message = new ExampleMessage();
        message.field1 = 42;
        message.field2 = "hello";
        message.field3 = Arrays.asList(true, false, true);
        message.field4 = inner;

        byte[] xdata = MsgpackXIOUtil.toByteArray(message, SCHEMA, false, LinkedBuffer.allocate());
        byte[] data = MsgpackIOUtil.toByteArray(message, SCHEMA, false);

        // System.out.println(Arrays.toString(xdata));
        // System.out.println(Arrays.toString(data));

        Assert.assertTrue(Arrays.equals(data, xdata));
    }

    @Test
    public void testFooArray() throws IOException
    {

        FooMessage foo = new FooMessage();
        foo.field = Arrays.asList("a", "b");

        byte[] xdata = MsgpackXIOUtil.toByteArray(foo, FOO_SCHEMA, false, LinkedBuffer.allocate());
        byte[] data = MsgpackIOUtil.toByteArray(foo, FOO_SCHEMA, false);

        Assert.assertTrue(Arrays.equals(data, xdata));
    }

    @Test
    public void testXIOCharset() throws IOException
    {

        ExampleMessage message = new ExampleMessage();
        message.field2 = "ч";

        byte[] xdata = MsgpackXIOUtil.toByteArray(message, SCHEMA, false, LinkedBuffer.allocate());
        byte[] data = MsgpackIOUtil.toByteArray(message, SCHEMA, false);

        Assert.assertTrue(Arrays.equals(data, xdata));
    }

    @Test
    public void testString() throws IOException {
        
        String string = "ч";
        
        byte[] data = string.getBytes(MessagePack.UTF8);
        //System.out.println(Arrays.toString(data));
     
        LinkedBuffer lb = LinkedBuffer.allocate();
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8(string, session, session.tail);
        
        byte[] xdata = session.toByteArray();
        //System.out.println(Arrays.toString(xdata));
        
        Assert.assertTrue(Arrays.equals(data, xdata));
    }
    
    static class FooMessage
    {
        List<String> field;
    }

    static class ExampleMessage
    {
        public int field1;
        public String field2;
        public List<Boolean> field3;
        public InnerMessage field4;
    }

    static class InnerMessage
    {
        public byte[] inner1;
        public double inner2;
    }

}
