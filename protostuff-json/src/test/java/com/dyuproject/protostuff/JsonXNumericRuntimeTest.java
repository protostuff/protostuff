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

import static com.dyuproject.protostuff.runtime.SerializableObjects.bar;
import static com.dyuproject.protostuff.runtime.SerializableObjects.baz;
import static com.dyuproject.protostuff.runtime.SerializableObjects.foo;
import static com.dyuproject.protostuff.runtime.SerializableObjects.negativeBar;
import static com.dyuproject.protostuff.runtime.SerializableObjects.negativeBaz;

import java.io.ByteArrayOutputStream;

import com.dyuproject.protostuff.runtime.Bar;
import com.dyuproject.protostuff.runtime.Baz;
import com.dyuproject.protostuff.runtime.Foo;
import com.dyuproject.protostuff.runtime.PolymorphicSerializationTest;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.dyuproject.protostuff.runtime.SerializableObjects;
import com.dyuproject.protostuff.runtime.PolymorphicSerializationTest.Zoo;

/**
 * Test for JsonXOutput on runtime pojos (polymorphic too).
 *
 * @author David Yu
 * @created Jan 16, 2011
 */
public class JsonXNumericRuntimeTest extends AbstractTest
{
    
    public void testFoo() throws Exception
    {
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);
        
        Foo fooCompare = foo;
        Foo dfoo = new Foo();
        
        byte[] data = JsonXIOUtil.toByteArray(fooCompare, schema, true, buf());
        JsonIOUtil.mergeFrom(data, dfoo, schema, true);
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testFooStreamed() throws Exception
    {
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);
        
        Foo fooCompare = foo;
        Foo dfoo = new Foo();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer buffer = buf();
        try
        {
            JsonXIOUtil.writeTo(out, fooCompare, schema, true, buffer);
        }
        finally
        {
            buffer.clear();
        }
        byte[] data = out.toByteArray();
        
        JsonIOUtil.mergeFrom(data, dfoo, schema, true);
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        Schema<Bar> schema = RuntimeSchema.getSchema(Bar.class);
        
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar dbar = new Bar();

            byte[] data = JsonXIOUtil.toByteArray(barCompare, schema, true, buf());
            JsonIOUtil.mergeFrom(data, dbar, schema, true);
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBarStreamed() throws Exception
    {
        Schema<Bar> schema = RuntimeSchema.getSchema(Bar.class);
        
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar dbar = new Bar();
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer buffer = buf();
            try
            {
                JsonXIOUtil.writeTo(out, barCompare, schema, true, buffer);
            }
            finally
            {
                buffer.clear();
            }
            byte[] data = out.toByteArray();
            
            JsonIOUtil.mergeFrom(data, dbar, schema, true);
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBaz() throws Exception
    {
        Schema<Baz> schema = RuntimeSchema.getSchema(Baz.class);
        
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz dbaz = new Baz();            
            
            byte[] data = JsonXIOUtil.toByteArray(bazCompare, schema, true, buf());
            JsonIOUtil.mergeFrom(data, dbaz, schema, true);
            SerializableObjects.assertEquals(bazCompare, dbaz);
        }
    }
    
    public void testBazStreamed() throws Exception
    {
        Schema<Baz> schema = RuntimeSchema.getSchema(Baz.class);
        
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz dbaz = new Baz();            
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer buffer = buf();
            try
            {
                JsonXIOUtil.writeTo(out, bazCompare, schema, true, buffer);
            }
            finally
            {
                buffer.clear();
            }
            byte[] data = out.toByteArray();
            
            JsonIOUtil.mergeFrom(data, dbaz, schema, true);
            SerializableObjects.assertEquals(bazCompare, dbaz);
        }
    }
    
    public void testPolymorphic() throws Exception
    {
        Schema<Zoo> schema = RuntimeSchema.getSchema(Zoo.class);
        Zoo zooCompare = PolymorphicSerializationTest.filledZoo();
        
        Zoo dzoo = new Zoo();            
        
        byte[] data = JsonXIOUtil.toByteArray(zooCompare, schema, true, buf());
        
        JsonIOUtil.mergeFrom(data, dzoo, schema, true);
        SerializableObjects.assertEquals(zooCompare, dzoo);
    }
    
    public void testPolymorphicStreamed() throws Exception
    {
        Schema<Zoo> schema = RuntimeSchema.getSchema(Zoo.class);
        Zoo zooCompare = PolymorphicSerializationTest.filledZoo();
        
        Zoo dzoo = new Zoo();   
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer buffer = buf();
        try
        {
            JsonXIOUtil.writeTo(out, zooCompare, schema, true, buffer);
        }
        finally
        {
            buffer.clear();
        }
        byte[] data = out.toByteArray();
        
        JsonIOUtil.mergeFrom(data, dzoo, schema, true);
        SerializableObjects.assertEquals(zooCompare, dzoo);
    }

}
