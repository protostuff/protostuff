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

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Test case for xml pipes.
 *
 * @author David Yu
 * @created Oct 8, 2010
 */
public class XmlPipeTest extends AbstractTest
{
    
    static <T> void protobufRoundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);
        
        byte[] xml = XmlIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema);
        
        byte[] xmlFromStream = XmlIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema);
        
        assertTrue(xml.length == xmlFromStream.length);
        assertEquals(STRING.deser(xml), STRING.deser(xmlFromStream));
        
        T parsedMessage = schema.newMessage();
        XmlIOUtil.mergeFrom(xml, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml);
        
        byte[] protobufRoundTrip = ProtobufIOUtil.toByteArray(
                XmlIOUtil.newPipe(xml, 0, xml.length), pipeSchema, buf());
        
        byte[] protobufRoundTripFromStream = ProtobufIOUtil.toByteArray(
                XmlIOUtil.newPipe(xmlStream), pipeSchema, buf());
        
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
        
        byte[] xml = XmlIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema);
        
        byte[] xmlFromStream = XmlIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema);
        
        assertTrue(xml.length == xmlFromStream.length);
        assertEquals(STRING.deser(xml), STRING.deser(xmlFromStream));
        
        T parsedMessage = schema.newMessage();
        XmlIOUtil.mergeFrom(xml, parsedMessage, schema);
        SerializableObjects.assertEquals(message, parsedMessage);
        
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xml);
        
        byte[] protostuffRoundTrip = ProtostuffIOUtil.toByteArray(
                XmlIOUtil.newPipe(xml, 0, xml.length), pipeSchema, buf());
        
        byte[] protostuffRoundTripFromStream = ProtostuffIOUtil.toByteArray(
                XmlIOUtil.newPipe(xmlStream), pipeSchema, buf());
        
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
