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

package io.protostuff;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.protostuff.StringSerializer.STRING;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest;
import java.util.Arrays;

/**
 * Test xml ser/deser for runtime {@link Object} fields.
 * 
 * @author David Yu
 * @created Feb 4, 2011
 */
public class XmlRuntimeObjectSchemaTest extends AbstractRuntimeObjectSchemaTest
{

    @Override
    protected <T> void mergeFrom(byte[] data, int offset, int length, T message,
            Schema<T> schema) throws IOException
    {
        XmlIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    @Override
    protected <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        XmlIOUtil.mergeFrom(in, message, schema);
    }

    @Override
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return XmlIOUtil.toByteArray(message, schema);
    }

    @Override
    protected <T> void writeTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        XmlIOUtil.writeTo(out, message, schema);
    }

    @Override
    protected <T> void roundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] xml = XmlIOUtil.toByteArray(message, schema);

        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml);

        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                XmlIOUtil.newPipe(xml, 0, xml.length), pipeSchema, buf());

        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                XmlIOUtil.newPipe(xmlStream), pipeSchema, buf());

        assertTrue(Arrays.equals(protostuff, protostuffFromStream));

        T parsedMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);

        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);

        byte[] xmlRoundTrip = XmlIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema);
        byte[] xmlRoundTripFromStream = XmlIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema);

        assertTrue(xmlRoundTrip.length == xmlRoundTripFromStream.length);

        String strXmlRoundTrip = STRING.deser(xmlRoundTrip);

        assertEquals(strXmlRoundTrip, STRING.deser(xmlRoundTripFromStream));

        assertTrue(xmlRoundTrip.length == xml.length);

        assertEquals(strXmlRoundTrip, STRING.deser(xml));
    }

}
