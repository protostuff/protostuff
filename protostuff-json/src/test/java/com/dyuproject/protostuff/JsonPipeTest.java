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

package com.dyuproject.protostuff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Test case for json pipes.
 *
 * @author David Yu
 * @created Oct 8, 2010
 */
public class JsonPipeTest extends AbstractTest
{
    
    static <T> void protobufRoundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema, boolean numeric) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);
        
        byte[] json = JsonIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema, numeric);
        
        byte[] jsonFromStream = JsonIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema, numeric);
        
        assertTrue(json.length == jsonFromStream.length);
        assertEquals(STRING.deser(json), STRING.deser(jsonFromStream));
        
        T parsedMessage = schema.newMessage();
        JsonIOUtil.mergeFrom(json, parsedMessage, schema, numeric);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream jsonStream = new ByteArrayInputStream(json);
        
        byte[] protobufRoundTrip = ProtobufIOUtil.toByteArray(
                JsonIOUtil.newPipe(json, 0, json.length, numeric), pipeSchema, buf());
        
        byte[] protobufRoundTripFromStream = ProtobufIOUtil.toByteArray(
                JsonIOUtil.newPipe(jsonStream, numeric), pipeSchema, buf());
        
        assertTrue(protobufRoundTrip.length == protobufRoundTripFromStream.length);
        
        String strProtobufRoundTrip = STRING.deser(protobufRoundTrip);
        
        assertEquals(strProtobufRoundTrip, STRING.deser(protobufRoundTripFromStream));
        
        assertTrue(protobufRoundTrip.length == protobuf.length);
        
        assertEquals(strProtobufRoundTrip, STRING.deser(protobuf));
        
        checkCompat(json, jsonFromStream, protobuf, pipeSchema, numeric, false);
    }
    
    static <T> void protostuffRoundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema, boolean numeric) throws Exception
    {
        byte[] protostuff = ProtostuffIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);
        
        byte[] json = JsonIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, numeric);
        
        byte[] jsonFromStream = JsonIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, numeric);
        
        assertTrue(json.length == jsonFromStream.length);
        assertEquals(STRING.deser(json), STRING.deser(jsonFromStream));
        
        T parsedMessage = schema.newMessage();
        JsonIOUtil.mergeFrom(json, parsedMessage, schema, numeric);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream jsonStream = new ByteArrayInputStream(json);
        
        byte[] protostuffRoundTrip = ProtostuffIOUtil.toByteArray(
                JsonIOUtil.newPipe(json, 0, json.length, numeric), pipeSchema, buf());
        
        byte[] protostuffRoundTripFromStream = ProtostuffIOUtil.toByteArray(
                JsonIOUtil.newPipe(jsonStream, numeric), pipeSchema, buf());
        
        assertTrue(protostuffRoundTrip.length == protostuffRoundTripFromStream.length);
        
        String strProtostuffRoundTrip = STRING.deser(protostuffRoundTrip);
        
        assertEquals(strProtostuffRoundTrip, STRING.deser(protostuffRoundTripFromStream));
        
        assertTrue(protostuffRoundTrip.length == protostuff.length);
        
        assertEquals(strProtostuffRoundTrip, STRING.deser(protostuff));
        
        checkCompat(json, jsonFromStream, protostuff, pipeSchema, numeric, true);
    }
    
    
    static <T> void checkCompat(byte[] json, byte[] jsonFromStream, 
            byte[] source, Pipe.Schema<T> pipeSchema, boolean numeric, boolean protostuff) 
            throws IOException
    {
        // check compatibilty for JsonOutput and JsonXOutput
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(source);
        LinkedBuffer buffer = buf();
        try
        {
            JsonXIOUtil.writeTo(out, 
                    protostuff ? ProtostuffIOUtil.newPipe(in) : ProtobufIOUtil.newPipe(in), 
                    pipeSchema, numeric, buffer);
        }
        finally
        {
            buffer.clear();
        }
        
        byte[] jsonxFromStream = out.toByteArray();
        
        byte[] jsonx = JsonXIOUtil.toByteArray(
                protostuff ? ProtostuffIOUtil.newPipe(source, 0, source.length) : 
                    ProtobufIOUtil.newPipe(source, 0, source.length), 
                pipeSchema, numeric, buf());
        
        assertTrue(jsonx.length == jsonxFromStream.length);
        assertEquals(STRING.deser(jsonx), STRING.deser(jsonxFromStream));
        assertEquals(STRING.deser(json), STRING.deser(jsonx));
    }
    
    public void testFoo() throws Exception
    {
        Foo foo = SerializableObjects.foo;
        protobufRoundTrip(foo, Foo.getSchema(), Foo.getPipeSchema(), false);
        protostuffRoundTrip(foo, Foo.getSchema(), Foo.getPipeSchema(), false);
        
        // numeric
        protobufRoundTrip(foo, Foo.getSchema(), Foo.getPipeSchema(), true);
        protostuffRoundTrip(foo, Foo.getSchema(), Foo.getPipeSchema(), true);
    }
    
    public void testBar() throws Exception
    {
        Bar bar = SerializableObjects.bar;
        protobufRoundTrip(bar, Bar.getSchema(), Bar.getPipeSchema(), false);
        protostuffRoundTrip(bar, Bar.getSchema(), Bar.getPipeSchema(), false);
        
        // numeric
        protobufRoundTrip(bar, Bar.getSchema(), Bar.getPipeSchema(), true);
        protostuffRoundTrip(bar, Bar.getSchema(), Bar.getPipeSchema(), true);
    }
    
    public void testBaz() throws Exception
    {
        Baz baz = SerializableObjects.baz;
        protobufRoundTrip(baz, Baz.getSchema(), Baz.getPipeSchema(), false);
        protostuffRoundTrip(baz, Baz.getSchema(), Baz.getPipeSchema(), false);
        
        // numeric
        protobufRoundTrip(baz, Baz.getSchema(), Baz.getPipeSchema(), true);
        protostuffRoundTrip(baz, Baz.getSchema(), Baz.getPipeSchema(), true);
    }

}
