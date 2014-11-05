//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

package io.protostuff.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import io.protostuff.Pipe;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.SerializableObjects;
import java.util.Arrays;

/**
 * Test protobuf ser/deser for runtime {@link Map} fields.
 * 
 * @author David Yu
 * @created Jan 21, 2011
 */
public class ProtobufRuntimeMapTest extends AbstractRuntimeMapTest
{

    protected <T> void mergeFrom(byte[] data, int offset, int length,
            T message, Schema<T> schema) throws IOException
    {
        ProtobufIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    protected <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        ProtobufIOUtil.mergeFrom(in, message, schema);
    }

    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return ProtobufIOUtil.toByteArray(message, schema, buf());
    }

    protected <T> void writeTo(OutputStream out, T message, Schema<T> schema)
            throws IOException
    {
        ProtobufIOUtil.writeTo(out, message, schema, buf());
    }

    protected <T> void roundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());

        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);

        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length),
                pipeSchema, buf());

        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema, buf());

        assertTrue(Arrays.equals(protostuff, protostuffFromStream));

        T parsedMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);

        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(
                protostuff);

        byte[] protobufRoundTrip = ProtobufIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length),
                pipeSchema, buf());

        byte[] protobufRoundTripFromStream = ProtobufIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, buf());

        assertTrue(Arrays.equals(protobufRoundTrip, protobufRoundTripFromStream));

        assertTrue(Arrays.equals(protobufRoundTrip, protobuf));
    }

}
