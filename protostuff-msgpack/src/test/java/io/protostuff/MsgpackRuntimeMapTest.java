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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

import io.protostuff.StringSerializer.STRING;
import io.protostuff.runtime.AbstractRuntimeMapTest;

/**
 * Test msgpack with {@link Map} fields.
 * 
 * @author Alex Shvid
 */
public class MsgpackRuntimeMapTest extends AbstractRuntimeMapTest
{

    @Override
    protected <T> void mergeFrom(byte[] data, int offset, int length, T message,
            Schema<T> schema) throws IOException
    {
        MsgpackIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    @Override
    protected <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        MsgpackIOUtil.mergeFrom(in, message, schema);
    }

    @Override
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return MsgpackIOUtil.toByteArray(message, schema);
    }

    @Override
    protected <T> void writeTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        MsgpackIOUtil.writeTo(out, message, schema);
    }

    @Override
    protected <T> void roundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] msgpack = MsgpackIOUtil.toByteArray(message, schema);

        ByteArrayInputStream jsonStream = new ByteArrayInputStream(msgpack);

        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                MsgpackIOUtil.newPipe(msgpack, 0, msgpack.length), pipeSchema, buf());

        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                MsgpackIOUtil.newPipe(jsonStream), pipeSchema, buf());

        assertTrue(Arrays.equals(protostuff, protostuffFromStream));

        T parsedMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);

        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);

        byte[] msgpackRoundTrip = MsgpackIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema);

        byte[] msgpackRoundTripFromStream = MsgpackIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema);

        assertTrue(msgpackRoundTrip.length == msgpackRoundTripFromStream.length);

        String strMsgpackRoundTrip = STRING.deser(msgpackRoundTrip);

        assertEquals(strMsgpackRoundTrip, STRING.deser(msgpackRoundTripFromStream));

        assertTrue(msgpackRoundTrip.length == msgpack.length);

        assertEquals(strMsgpackRoundTrip, STRING.deser(msgpack));
    }
}
