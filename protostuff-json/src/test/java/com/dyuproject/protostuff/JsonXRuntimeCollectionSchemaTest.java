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
import java.io.OutputStream;

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Test runtime collection fields with {@link CollectionSchema} via jsonx ser/deser.
 *
 * @author David Yu
 * @created Jan 27, 2011
 */
public class JsonXRuntimeCollectionSchemaTest extends AbstractJsonRuntimeCollectionSchemaTest
{
    
    protected boolean isNumeric()
    {
        return false;
    }
    
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return JsonXIOUtil.toByteArray(message, schema, isNumeric(), buf());
    }

    protected <T> void writeTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        JsonXIOUtil.writeTo(out, message, schema, isNumeric(), buf());
    }
    
    protected <T> void roundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] json = JsonXIOUtil.toByteArray(message, schema, isNumeric(), buf());
        
        ByteArrayInputStream jsonStream = new ByteArrayInputStream(json);
        
        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                JsonIOUtil.newPipe(json, 0, json.length, isNumeric()), pipeSchema, buf());
        
        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                JsonIOUtil.newPipe(jsonStream, isNumeric()), pipeSchema, buf());
        
        assertTrue(protostuff.length == protostuffFromStream.length);
        assertEquals(STRING.deser(protostuff), STRING.deser(protostuffFromStream));
        
        T parsedMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);
        
        byte[] jsonRoundTrip = JsonXIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, isNumeric(), buf());
        
        byte[] jsonRoundTripFromStream = JsonXIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, isNumeric(), buf());
        
        assertTrue(jsonRoundTrip.length == jsonRoundTripFromStream.length);
        
        String strJsonRoundTrip = STRING.deser(jsonRoundTrip);
        
        assertEquals(strJsonRoundTrip, STRING.deser(jsonRoundTripFromStream));
        
        assertTrue(jsonRoundTrip.length == json.length);
        
        assertEquals(strJsonRoundTrip, STRING.deser(json));
    }


}
