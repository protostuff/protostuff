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
 * Test protostuff/protobuf to yaml pipe.
 *
 * @author David Yu
 * @created Oct 9, 2010
 */
public class YamlPipeTest extends AbstractTest
{
    
    static <T> void protobufRoundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protobuf = ProtobufIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protobufStream = new ByteArrayInputStream(protobuf);
        
        byte[] yaml = YamlIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema, buf());
        
        byte[] yamlFromStream = YamlIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobufStream), pipeSchema, buf());
        
        assertTrue(yaml.length == yamlFromStream.length);
        
        String strYaml = STRING.deser(yaml);
        String strYamlFromStream = STRING.deser(yamlFromStream);
        assertEquals(strYaml, strYamlFromStream);
    }
    
    static <T> void protostuffRoundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception
    {
        byte[] protostuff = ProtostuffIOUtil.toByteArray(message, schema, buf());
        
        ByteArrayInputStream protostuffStream = new ByteArrayInputStream(protostuff);
        
        byte[] yaml = YamlIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuff, 0, protostuff.length), pipeSchema, buf());
        
        byte[] yamlFromStream = YamlIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(protostuffStream), pipeSchema, buf());
        
        assertTrue(yaml.length == yamlFromStream.length);
        
        String strYaml = STRING.deser(yaml);
        String strYamlFromStream = STRING.deser(yamlFromStream);
        assertEquals(strYaml, strYamlFromStream);
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
