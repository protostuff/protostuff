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

package com.dyuproject.protostuff;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dyuproject.protostuff.StringSerializer.STRING;
import com.dyuproject.protostuff.runtime.AbstractRuntimeCollectionSchemaTest;

/**
 * Test runtime collection fields with {@link CollectionSchema} via xml ser/deser.
 *
 * @author David Yu
 * @created Jan 27, 2011
 */
public class XmlXRuntimeCollectionSchemaTest extends AbstractRuntimeCollectionSchemaTest
{

    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema) throws IOException
    {
        XmlIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    protected <T> void mergeFrom(InputStream in, T message, Schema<T> schema) 
    throws IOException
    {
        XmlIOUtil.mergeFrom(in, message, schema);
    }
    
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return XmlXIOUtil.toByteArray(message, schema, buf());
    }

    protected <T> void writeTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        XmlXIOUtil.writeTo(out, message, schema, buf());
    }
    
    protected <T> void roundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] xml = XmlXIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml);
        
        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                XmlIOUtil.newPipe(xml, 0, xml.length), pipeSchema, buf());
        
        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                XmlIOUtil.newPipe(xmlStream), pipeSchema, buf());
        
        assertTrue(protostuff.length == protostuffFromStream.length);
        assertEquals(STRING.deser(protostuff), STRING.deser(protostuffFromStream));
        
        T parsedMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);
        
        byte[] xmlRoundTrip = XmlXIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, buf());
        byte[] xmlRoundTripFromStream = XmlXIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, buf());
        
        assertTrue(xmlRoundTrip.length == xmlRoundTripFromStream.length);
        
        String strXmlRoundTrip = STRING.deser(xmlRoundTrip);
        
        assertEquals(strXmlRoundTrip, STRING.deser(xmlRoundTripFromStream));
        
        assertTrue(xmlRoundTrip.length == xml.length);
        
        assertEquals(strXmlRoundTrip, STRING.deser(xml));
    }
    
}
