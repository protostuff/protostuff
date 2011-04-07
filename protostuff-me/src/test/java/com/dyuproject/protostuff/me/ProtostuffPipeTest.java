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

package com.dyuproject.protostuff.me;

import java.io.ByteArrayInputStream;

/**
 * Test case for protostuff pipes and protobuf pipes.
 *
 * @author David Yu
 * @created Oct 8, 2010
 */
public class ProtostuffPipeTest extends AbstractTest
{

    public static void roundTrip(Message message, Schema schema, 
            Pipe.Schema pipeSchema) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);
        
        byte[] protostuff = ProtostuffIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema, buf());
        
        byte[] protostuffFromStream = ProtostuffIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema, buf());
        
        assertTrue(protostuff.length == protostuffFromStream.length);
        assertEquals(StringSerializer.STRING.deser(protostuff), StringSerializer.STRING.deser(protostuffFromStream));
        
        Message parsedMessage = (Message)schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);
        
        byte[] protobufRoundTrip = ProtobufIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, buf());
        
        byte[] protobufRoundTripFromStream = ProtobufIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, buf());
        
        assertTrue(protobufRoundTrip.length == protobufRoundTripFromStream.length);
        
        String strProtobufRoundTrip = StringSerializer.STRING.deser(protobufRoundTrip);
        
        assertEquals(strProtobufRoundTrip, StringSerializer.STRING.deser(protobufRoundTripFromStream));
        
        assertTrue(protobufRoundTrip.length == protobuf.length);
        
        assertEquals(strProtobufRoundTrip, StringSerializer.STRING.deser(protobuf));
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
