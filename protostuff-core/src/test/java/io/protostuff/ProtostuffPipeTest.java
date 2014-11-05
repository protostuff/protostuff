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

import java.util.Arrays;

/**
 * Test case for protostuff pipes and protobuf pipes.
 * 
 * @author David Yu
 * @created Oct 8, 2010
 */
public class ProtostuffPipeTest extends AbstractTest
{

    public static <T> void roundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());

        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);

        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema, buf());

        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema, buf());

        assertTrue(protostuff.length == protostuffFromStream.length);
        assertTrue(Arrays.equals(protostuff, protostuffFromStream));

        T parsedMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);

        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);

        byte[] protobufRoundTrip = ProtobufIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, buf());

        byte[] protobufRoundTripFromStream = ProtobufIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, buf());

        assertTrue(protobufRoundTrip.length == protobufRoundTripFromStream.length);

        assertTrue(Arrays.equals(protobufRoundTrip, protobufRoundTripFromStream));

        assertTrue(protobufRoundTrip.length == protobuf.length);

        assertTrue(Arrays.equals(protobufRoundTrip, protobuf));
    }

    public void testFoo() throws Exception
    {
        Foo foo = SerializableObjects.foo;
        roundTrip(foo, Foo.getSchema(), Foo.getPipeSchema());
    }

    public void testBar() throws Exception
    {
        Bar bar = SerializableObjects.bar;
        roundTrip(bar, Bar.getSchema(), Bar.getPipeSchema());
    }

    public void testBaz() throws Exception
    {
        Baz baz = SerializableObjects.baz;
        roundTrip(baz, Baz.getSchema(), Baz.getPipeSchema());
    }

}
