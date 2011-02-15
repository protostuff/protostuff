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
import java.util.Map;

import com.dyuproject.protostuff.StringSerializer.STRING;
import com.dyuproject.protostuff.runtime.AbstractRuntimeMapTest;

/**
 * Test smile ser/deser for runtime {@link Map} fields.
 *
 * @author David Yu
 * @created Feb 11, 2011
 */
public class SmileRuntimeMapTest extends AbstractRuntimeMapTest
{
    
    protected boolean isPolymorphicPojoSupported()
    {
        return true;
    }
    
    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema) throws IOException
    {
        SmileIOUtil.mergeFrom(data, offset, length, message, schema, false);
    }

    protected <T> void mergeFrom(InputStream in, T message, Schema<T> schema) 
    throws IOException
    {
        SmileIOUtil.mergeFrom(in, message, schema, false);
    }
    
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return SmileIOUtil.toByteArray(message, schema, false);
    }

    protected <T> void writeTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        SmileIOUtil.writeTo(out, message, schema, false);
    }
    
    protected <T> void roundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] smile = SmileIOUtil.toByteArray(message, schema, false);
        
        ByteArrayInputStream smileStream = new ByteArrayInputStream(smile);
        
        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                SmileIOUtil.newPipe(smile, 0, smile.length, false), pipeSchema, buf());
        
        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                SmileIOUtil.newPipe(smileStream, false), pipeSchema, buf());
        
        assertTrue(protostuff.length == protostuffFromStream.length);
        assertEquals(STRING.deser(protostuff), STRING.deser(protostuffFromStream));
        
        T parsedMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);
        
        byte[] smileRoundTrip = SmileIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, false);
        
        byte[] smileRoundTripFromStream = SmileIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, false);
        
        assertTrue(smileRoundTrip.length == smileRoundTripFromStream.length);
        
        String strSmileRoundTrip = STRING.deser(smileRoundTrip);
        
        assertEquals(strSmileRoundTrip, STRING.deser(smileRoundTripFromStream));
        
        assertTrue(smileRoundTrip.length == smile.length);
        
        // comment out since smile encodes strings differently when using direct
        // SmileGenerator.writeUTF8String() ... (w/c is only used when the pipe source 
        // is binary and the utf8 is intact (e.g protostuff,protobuf)
        
        // THE reason is that even if the string provided is ascii, smile encodes it
        // as unicode. See line 1043 of SmileGenerator.
        //assertEquals(strSmileRoundTrip, STRING.deser(smile));
        
        // This will fail on some messages.
        T messageSmile = schema.newMessage();
        SmileIOUtil.mergeFrom(smile, messageSmile, schema, false);
        T messageSmileRoundTrip = schema.newMessage();
        SmileIOUtil.mergeFrom(smileRoundTrip, messageSmileRoundTrip, schema, false);
        
        assertEquals(messageSmile, messageSmileRoundTrip);
    }

}
