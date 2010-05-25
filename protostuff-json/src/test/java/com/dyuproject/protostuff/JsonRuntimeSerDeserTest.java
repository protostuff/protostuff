//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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
import junit.framework.TestCase;

import com.dyuproject.protostuff.runtime.Bar;
import com.dyuproject.protostuff.runtime.Baz;
import com.dyuproject.protostuff.runtime.Foo;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.dyuproject.protostuff.runtime.SerializableObjects;

/**
 * Testing for json ser/deser against runtime messages.
 *
 * @author David Yu
 * @created Nov 20, 2009
 */
public class JsonRuntimeSerDeserTest extends TestCase
{
    
    public void testFoo() throws Exception
    {
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class);
        
        Foo fooCompare = foo;
        Foo dfoo = new Foo();
        
        byte[] data = JsonIOUtil.toByteArray(fooCompare, schema, false);
        JsonIOUtil.mergeFrom(data, dfoo, schema, false);
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        Schema<Bar> schema = RuntimeSchema.getSchema(Bar.class);
        
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar dbar = new Bar();

            byte[] data = JsonIOUtil.toByteArray(barCompare, schema, false);
            JsonIOUtil.mergeFrom(data, dbar, schema, false);
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBaz() throws Exception
    {
        Schema<Baz> schema = RuntimeSchema.getSchema(Baz.class);
        
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz dbaz = new Baz();            
            
            byte[] data = JsonIOUtil.toByteArray(bazCompare, schema, false);
            JsonIOUtil.mergeFrom(data, dbaz, schema, false);
            SerializableObjects.assertEquals(bazCompare, dbaz);
        }
    }

}
