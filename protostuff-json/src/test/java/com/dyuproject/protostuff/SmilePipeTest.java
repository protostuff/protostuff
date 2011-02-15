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

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Test case for smile pipes.
 *
 * @author David Yu
 * @created Feb 11, 2011
 */
public class SmilePipeTest extends AbstractTest
{
    
    
    static <T> void protobufRoundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);
        
        byte[] smile = SmileIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema, false);
        
        byte[] smileFromStream = SmileIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema, false);
        
        assertTrue(smile.length == smileFromStream.length);
        assertEquals(STRING.deser(smile), STRING.deser(smileFromStream));
        
        T parsedMessage = schema.newMessage();
        SmileIOUtil.mergeFrom(smile, parsedMessage, schema, false);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream smileStream = new ByteArrayInputStream(smile);
        
        byte[] protobufRoundTrip = ProtobufIOUtil.toByteArray(
                SmileIOUtil.newPipe(smile, 0, smile.length, false), pipeSchema, buf());
        
        byte[] protobufRoundTripFromStream = ProtobufIOUtil.toByteArray(
                SmileIOUtil.newPipe(smileStream, false), pipeSchema, buf());
        
        assertTrue(protobufRoundTrip.length == protobufRoundTripFromStream.length);
        
        String strProtobufRoundTrip = STRING.deser(protobufRoundTrip);
        
        assertEquals(strProtobufRoundTrip, STRING.deser(protobufRoundTripFromStream));
        
        assertTrue(protobufRoundTrip.length == protobuf.length);
        
        assertEquals(strProtobufRoundTrip, STRING.deser(protobuf));
    }
    
    static <T> void protostuffRoundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protostuff = ProtostuffIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);
        
        byte[] smile = SmileIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, false);
        
        byte[] smileFromStream = SmileIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, false);
        
        assertTrue(smile.length == smileFromStream.length);
        assertEquals(STRING.deser(smile), STRING.deser(smileFromStream));
        
        T parsedMessage = schema.newMessage();
        SmileIOUtil.mergeFrom(smile, parsedMessage, schema, false);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream smileStream = new ByteArrayInputStream(smile);
        
        byte[] protostuffRoundTrip = ProtostuffIOUtil.toByteArray(
                SmileIOUtil.newPipe(smile, 0, smile.length, false), pipeSchema, buf());
        
        byte[] protostuffRoundTripFromStream = ProtostuffIOUtil.toByteArray(
                SmileIOUtil.newPipe(smileStream, false), pipeSchema, buf());
        
        assertTrue(protostuffRoundTrip.length == protostuffRoundTripFromStream.length);
        
        String strProtostuffRoundTrip = STRING.deser(protostuffRoundTrip);
        
        assertEquals(strProtostuffRoundTrip, STRING.deser(protostuffRoundTripFromStream));
        
        assertTrue(protostuffRoundTrip.length == protostuff.length);
        
        assertEquals(strProtostuffRoundTrip, STRING.deser(protostuff));
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
