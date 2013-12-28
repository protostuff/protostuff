//========================================================================
//Copyright 2012 David Yu
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

import static com.dyuproject.protostuff.SerializableObjects.bar;
import static com.dyuproject.protostuff.SerializableObjects.baz;
import static com.dyuproject.protostuff.SerializableObjects.foo;
import static com.dyuproject.protostuff.SerializableObjects.negativeBar;
import static com.dyuproject.protostuff.SerializableObjects.negativeBaz;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import com.dyuproject.protostuff.Bar;
import com.dyuproject.protostuff.Baz;
import com.dyuproject.protostuff.Foo;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.XmlXIOUtil;

/**
 * Tests for XmlXOutput.
 * 
 * @author David Yu
 * @created Aug 30, 2012
 */
public class XmlXTest extends com.dyuproject.protostuff.AbstractTest
{
    
    public void testFoo() throws Exception
    {
        Schema<Foo> schema = Foo.getSchema();
        Foo message = foo;

        byte[] data = XmlXIOUtil.toByteArray(message, schema, 
                buf());
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlXIOUtil.writeTo(out, message, schema, buf());
        
        assertTrue(Arrays.equals(data, out.toByteArray()));
    }
    
    public void testBar() throws Exception
    {
        Schema<Bar> schema = Bar.getSchema();
        for(Bar message : new Bar[]{bar, negativeBar})
        {
            byte[] data = XmlXIOUtil.toByteArray(message, schema, 
                    buf());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XmlXIOUtil.writeTo(out, message, schema, buf());
            
            assertTrue(Arrays.equals(data, out.toByteArray()));
        }
    }
    
    public void testBaz() throws Exception
    {
        Schema<Baz> schema = Baz.getSchema();
        for(Baz message : new Baz[]{baz, negativeBaz})
        {
            byte[] data = XmlXIOUtil.toByteArray(message, schema, 
                    buf());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XmlXIOUtil.writeTo(out, message, schema, buf());
            
            assertTrue(Arrays.equals(data, out.toByteArray()));
        }
    }
    
    /*static void zipAndWriteTo(OutputStream out, Map<String,byte[]> map) 
            throws IOException
    {
        ZipOutputStream zos = new ZipOutputStream(out);
        
        for(Map.Entry<String, byte[]> entry : map.entrySet())
        {
            ZipEntry zipEntry = new ZipEntry(entry.getKey());
            byte[] data = entry.getValue();
            zipEntry.setSize(data.length);
            zos.putNextEntry(zipEntry);
            zos.write(data);
            zos.closeEntry();
        }
        
        zos.close();
    }
    
    public static void main(String[] args) throws Exception
    {
        HashMap<String,byte[]> map = new HashMap<String,byte[]>();
        byte[] dataFoo = XmlXIOUtil.toByteArray(foo, Foo.getSchema(), buf());
        byte[] dataBar = XmlXIOUtil.toByteArray(bar, Bar.getSchema(), buf());
        map.put("foo", dataFoo);
        map.put("bar", dataBar);
        
        FileOutputStream out = new FileOutputStream("target/test.zip");
        zipAndWriteTo(out, map);
        
    }*/
}
