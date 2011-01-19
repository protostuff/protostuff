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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.Message;
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
    
    public void testCompat() throws IOException
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
    
    static <T extends Message<T>> Schema<T> getCachedSchema(Class<T> clazz) 
    throws InstantiationException, IllegalAccessException
    {
        Schema<T> schema = clazz.newInstance().cachedSchema();
        //System.err.println("! " + schema + " | " + System.identityHashCode(schema));
        return schema;
    }
    
    @SuppressWarnings("unchecked")
    public void testMixed() throws Exception
    {
        Schema<Mixed> schema = RuntimeSchema.getSchema(Mixed.class);
        
        assertTrue(MappedSchema.class.isAssignableFrom(schema.getClass()));
        
        MappedSchema<Mixed> mappedSchema = (MappedSchema<Mixed>)schema;
        
        assertTrue(
                RuntimeMessageField.class.isAssignableFrom(
                        mappedSchema.fieldsByName.get("rfoo").getClass())
                );
        assertTrue(
                RuntimeMessageField.class.isAssignableFrom(
                        mappedSchema.fieldsByName.get("rbar").getClass())
                );
        assertTrue(
                RuntimeMessageField.class.isAssignableFrom(
                        mappedSchema.fieldsByName.get("rbaz").getClass())
                );
        assertTrue(
                RuntimeMessageField.class.isAssignableFrom(
                        mappedSchema.fieldsByName.get("arrayBar").getClass())
                );
        
        RuntimeMessageField<Mixed,com.dyuproject.protostuff.Foo> rfoo = 
            (RuntimeMessageField<Mixed,com.dyuproject.protostuff.Foo>)mappedSchema.fieldsByName.get("rfoo");
        
        RuntimeMessageField<Mixed,com.dyuproject.protostuff.Bar> rbar = 
            (RuntimeMessageField<Mixed,com.dyuproject.protostuff.Bar>)mappedSchema.fieldsByName.get("rbar");
        
        RuntimeMessageField<Mixed,com.dyuproject.protostuff.Baz> rbaz = 
            (RuntimeMessageField<Mixed,com.dyuproject.protostuff.Baz>)mappedSchema.fieldsByName.get("rbaz");
        
        RuntimeMessageField<Mixed,com.dyuproject.protostuff.Bar> arrayBar = 
            (RuntimeMessageField<Mixed,com.dyuproject.protostuff.Bar>)mappedSchema.fieldsByName.get("arrayBar");
        
        assertTrue(rfoo.getSchema().getClass().isAssignableFrom(
                getCachedSchema(com.dyuproject.protostuff.Foo.class).getClass()));
        
        assertTrue(rbar.getSchema().getClass().isAssignableFrom(
                getCachedSchema(com.dyuproject.protostuff.Bar.class).getClass()));
        
        assertTrue(rbaz.getSchema().getClass().isAssignableFrom(
                getCachedSchema(com.dyuproject.protostuff.Baz.class).getClass()));
        
        assertTrue(arrayBar.getSchema().getClass().isAssignableFrom(
                getCachedSchema(com.dyuproject.protostuff.Bar.class).getClass()));
    }
    
    public static class Mixed
    {
        int id;
        
        Foo fo;
        Bar br;
        Baz bz;
        
        com.dyuproject.protostuff.Foo foo;
        com.dyuproject.protostuff.Bar bar;
        com.dyuproject.protostuff.Baz baz;
        
        List<com.dyuproject.protostuff.Foo> rfoo;
        Set<com.dyuproject.protostuff.Bar> rbar;
        Collection<com.dyuproject.protostuff.Baz> rbaz;
        
        com.dyuproject.protostuff.Bar[] arrayBar;
    }

}
