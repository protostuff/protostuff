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

package com.dyuproject.protostuff.runtime;

import java.io.IOException;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Test that the runtime schema would have the same output as hand-coded/code-generated 
 * schema.
 *
 * @author David Yu
 * @created Oct 20, 2010
 */
public class CompatTest extends AbstractTest
{
    
    public void testIt() throws IOException
    {
        com.dyuproject.protostuff.Foo foo1 = com.dyuproject.protostuff.SerializableObjects.foo;
        Schema<com.dyuproject.protostuff.Foo> schema1 = com.dyuproject.protostuff.Foo.getSchema();
        
        Foo foo2 = SerializableObjects.foo;
        Schema<Foo> schema2 = RuntimeSchema.getSchema(Foo.class);
        
        Schema<com.dyuproject.protostuff.Foo> schema3 = RuntimeSchema.getSchema(com.dyuproject.protostuff.Foo.class);
        
        byte[] byte1 = ProtostuffIOUtil.toByteArray(foo1, schema1, buf());
        byte[] byte2 = ProtostuffIOUtil.toByteArray(foo2, schema2, buf());
        byte[] byte3 = ProtostuffIOUtil.toByteArray(foo1, schema3, buf());
        
        String str1 = STRING.deser(byte1);
        String str2 = STRING.deser(byte2);
        String str3 = STRING.deser(byte3);
        
        assertEquals(str1, str2);
        assertEquals(str1, str3);
    }

}
