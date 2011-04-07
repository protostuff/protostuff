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

package com.dyuproject.protostuff.me;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Tests for various protobuf int flavors.
 *
 * @author David Yu
 * @created Feb 14, 2011
 */
public class IntTest extends AbstractTest
{
    
    static PojoWithInts filledPojoWithInts(int i, int ni, long j, long nj)
    {
        assertTrue(i >= 0);
        assertTrue(j >= 0);
        
        PojoWithInts p = new PojoWithInts();
        p.setSomeInt32(i);
        p.setSomeUint32(i);
        p.setSomeFixed32(i);
        p.setSomeSint32(ni);
        p.setSomeSfixed32(ni);
        
        p.setSomeInt64(j);
        p.setSomeUint64(j);
        p.setSomeFixed64(j);
        p.setSomeSint64(nj);
        p.setSomeSfixed64(nj);
        
        return p;
    }
    
    public void testProtostuff() throws IOException
    {
        verifyProtostuff(filledPojoWithInts(0, 0, 0, 0));
        verifyProtostuff(filledPojoWithInts(1, 1, 1, 1));
        verifyProtostuff(filledPojoWithInts(1, -1, 1, -1));
        verifyProtostuff(filledPojoWithInts(Integer.MAX_VALUE, Integer.MAX_VALUE, 
                Long.MAX_VALUE, Long.MAX_VALUE));
        verifyProtostuff(filledPojoWithInts(Integer.MAX_VALUE, Integer.MIN_VALUE, 
                Long.MAX_VALUE, Long.MIN_VALUE));
    }
    
    public void testProtobuf() throws IOException
    {
        verifyProtobuf(filledPojoWithInts(0, 0, 0, 0));
        verifyProtobuf(filledPojoWithInts(1, 1, 1, 1));
        verifyProtobuf(filledPojoWithInts(1, -1, 1, -1));
        verifyProtobuf(filledPojoWithInts(Integer.MAX_VALUE, Integer.MAX_VALUE, 
                Long.MAX_VALUE, Long.MAX_VALUE));
        verifyProtobuf(filledPojoWithInts(Integer.MAX_VALUE, Integer.MIN_VALUE, 
                Long.MAX_VALUE, Long.MIN_VALUE));
    }
    
    static void verifyProtostuff(PojoWithInts p) throws IOException
    {
        Schema schema = PojoWithInts.getSchema();
        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, buf());
        
        PojoWithInts pFromByteArray = new PojoWithInts();
        ProtostuffIOUtil.mergeFrom(data, pFromByteArray, schema);
        
        assertEquals(p, pFromByteArray);
        
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeTo(out, p, schema, buf());
        
        byte[] dataFromStream = out.toByteArray();
        assertTrue(Arrays.equals(data, dataFromStream));
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        PojoWithInts pFromStream = new PojoWithInts();
        ProtostuffIOUtil.mergeFrom(in, pFromStream, schema);
        
        assertEquals(p, pFromStream);
    }
    
    static void verifyProtobuf(PojoWithInts p) throws IOException
    {
        Schema schema = PojoWithInts.getSchema();
        byte[] data = ProtobufIOUtil.toByteArray(p, schema, buf());
        
        PojoWithInts pFromByteArray = new PojoWithInts();
        ProtobufIOUtil.mergeFrom(data, pFromByteArray, schema);
        
        assertEquals(p, pFromByteArray);
        
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtobufIOUtil.writeTo(out, p, schema, buf());
        
        byte[] dataFromStream = out.toByteArray();
        assertTrue(Arrays.equals(data, dataFromStream));
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        PojoWithInts pFromStream = new PojoWithInts();
        ProtobufIOUtil.mergeFrom(in, pFromStream, schema);
        
        assertEquals(p, pFromStream);
    }

}
