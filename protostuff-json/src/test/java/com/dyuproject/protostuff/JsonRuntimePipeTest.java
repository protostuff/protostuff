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

import com.dyuproject.protostuff.runtime.MappedSchema;
import com.dyuproject.protostuff.runtime.PolymorphicSerializationTest;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.dyuproject.protostuff.runtime.PolymorphicSerializationTest.Zoo;

import com.dyuproject.protostuff.runtime.Bar;
import com.dyuproject.protostuff.runtime.Baz;
import com.dyuproject.protostuff.runtime.Foo;
import com.dyuproject.protostuff.runtime.SerializableObjects;

import static com.dyuproject.protostuff.JsonPipeTest.protobufRoundTrip;
import static com.dyuproject.protostuff.JsonPipeTest.protostuffRoundTrip;

/**
 * Test case for json runtime pipes.
 *
 * @author David Yu
 * @created Jan 16, 2011
 */
public class JsonRuntimePipeTest extends AbstractTest
{
    
    public void testFoo() throws Exception
    {
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);
        Pipe.Schema<Foo> pipeSchema = ((MappedSchema<Foo>)schema).getPipeSchema();
        
        Foo foo = SerializableObjects.foo;
        
        
        protobufRoundTrip(foo, schema, pipeSchema, false);
        protostuffRoundTrip(foo, schema, pipeSchema, false);
        
        // numeric
        protobufRoundTrip(foo, schema, pipeSchema, true);
        protostuffRoundTrip(foo, schema, pipeSchema, true);
    }
    
    public void testBar() throws Exception
    {
        Schema<Bar> schema = RuntimeSchema.getSchema(Bar.class);
        Pipe.Schema<Bar> pipeSchema = ((MappedSchema<Bar>)schema).getPipeSchema();
        
        Bar bar = SerializableObjects.bar;
        
        
        protobufRoundTrip(bar, schema, pipeSchema, false);
        protostuffRoundTrip(bar, schema, pipeSchema, false);
        
        // numeric
        protobufRoundTrip(bar, schema, pipeSchema, true);
        protostuffRoundTrip(bar, schema, pipeSchema, true);
    }
    
    public void testBaz() throws Exception
    {
        Schema<Baz> schema = RuntimeSchema.getSchema(Baz.class);
        Pipe.Schema<Baz> pipeSchema = ((MappedSchema<Baz>)schema).getPipeSchema();
        
        Baz baz = SerializableObjects.baz;
        
        
        protobufRoundTrip(baz, schema, pipeSchema, false);
        protostuffRoundTrip(baz, schema, pipeSchema, false);
        
        // numeric
        protobufRoundTrip(baz, schema, pipeSchema, true);
        protostuffRoundTrip(baz, schema, pipeSchema, true);
    }
    
    public void testPolymorphic() throws Exception
    {
        Schema<Zoo> schema = RuntimeSchema.getSchema(Zoo.class);
        Pipe.Schema<Zoo> pipeSchema = ((MappedSchema<Zoo>)schema).getPipeSchema();
        
        Zoo zoo = PolymorphicSerializationTest.filledZoo();
        
        // numeric only
        protobufRoundTrip(zoo, schema, pipeSchema, true);
        protostuffRoundTrip(zoo, schema, pipeSchema, true);
    }

}
