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

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import io.protostuff.StringSerializer.STRING;

/**
 * Test case for msgpack pipes.
 * 
 * @author Alex Shvid
 */
public class MsgpackPipeTest extends AbstractTest
{

    static <T> void protobufRoundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());

        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);

        byte[] msgpack = MsgpackIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema);

        byte[] msgpackFromStream = MsgpackIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema);

        assertTrue(msgpack.length == msgpackFromStream.length);
        assertEquals(STRING.deser(msgpack), STRING.deser(msgpackFromStream));

        T parsedMessage = schema.newMessage();
        MsgpackIOUtil.mergeFrom(msgpack, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);

        ByteArrayInputStream jsonStream = new ByteArrayInputStream(msgpack);

        byte[] protobufRoundTrip = ProtobufIOUtil.toByteArray(
                MsgpackIOUtil.newPipe(msgpack, 0, msgpack.length), pipeSchema, buf());

        byte[] protobufRoundTripFromStream = ProtobufIOUtil.toByteArray(
                MsgpackIOUtil.newPipe(jsonStream), pipeSchema, buf());

        assertTrue(Arrays.equals(protobufRoundTrip, protobufRoundTripFromStream));
        assertTrue(Arrays.equals(protobufRoundTrip, protobuf));
    }

    static <T> void protostuffRoundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protostuff = ProtostuffIOUtil.toByteArray(message, schema, buf());

        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);

        byte[] msgpack = MsgpackIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema);

        byte[] msgpackFromStream = MsgpackIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema);

        assertTrue(msgpack.length == msgpackFromStream.length);
        assertEquals(STRING.deser(msgpack), STRING.deser(msgpackFromStream));

        T parsedMessage = schema.newMessage();
        MsgpackIOUtil.mergeFrom(msgpack, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);

        ByteArrayInputStream msgpackStream = new ByteArrayInputStream(msgpack);

        byte[] protostuffRoundTrip = ProtostuffIOUtil.toByteArray(
                MsgpackIOUtil.newPipe(msgpack, 0, msgpack.length), pipeSchema, buf());

        byte[] protostuffRoundTripFromStream = ProtostuffIOUtil.toByteArray(
                MsgpackIOUtil.newPipe(msgpackStream), pipeSchema, buf());

        assertTrue(Arrays.equals(protostuffRoundTrip, protostuffRoundTripFromStream));
        assertTrue(Arrays.equals(protostuffRoundTrip, protostuff));

    }

    public void testFoo() throws Exception
    {
        Foo foo = SerializableObjects.foo;
        protobufRoundTrip(foo, Foo.getSchema(), Foo.getPipeSchema());
        protostuffRoundTrip(foo, Foo.getSchema(), Foo.getPipeSchema());
    }

    public void testBar() throws Exception
    {
        Bar bar = SerializableObjects.bar;
        protobufRoundTrip(bar, Bar.getSchema(), Bar.getPipeSchema());
        protostuffRoundTrip(bar, Bar.getSchema(), Bar.getPipeSchema());
    }

    public void testBaz() throws Exception
    {
        Baz baz = SerializableObjects.baz;
        protobufRoundTrip(baz, Baz.getSchema(), Baz.getPipeSchema());
        protostuffRoundTrip(baz, Baz.getSchema(), Baz.getPipeSchema());
    }

}
