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

import com.dyuproject.protostuff.me.Foo.EnumSample;

/**
 * Test for re-using a thread-local buffer across many serializations.
 *
 * @author David Yu
 * @created Jan 15, 2011
 */
public class BufferReuseTest extends StandardTest
{
    
    private static final ThreadLocal<LinkedBuffer> localBuffer = 
        new ThreadLocal<LinkedBuffer>()
    {
        protected LinkedBuffer initialValue()
        {
            return buf();
        }
    };

    protected void mergeFrom(byte[] data, int offset, int length, Message message, Schema schema) throws IOException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data, offset, length);
        ProtostuffIOUtil.mergeFrom(in, message, schema, localBuffer.get());
    }

    protected byte[] toByteArray(Message message, Schema schema)
    {
        final LinkedBuffer buffer = localBuffer.get();
        try
        {
            return ProtostuffIOUtil.toByteArray(message, schema, buffer);
        }
        finally
        {
            buffer.clear();
        }
    }
    
    public void testFooSizeLimited() throws Exception
    {
        final Foo fooCompare = SerializableObjects.newFoo(
                new Integer[]{90210,-90210, 0}, 
                new String[]{"ab", "cd"}, 
                new Bar[]{SerializableObjects.bar, SerializableObjects.negativeBar, 
                        SerializableObjects.bar, SerializableObjects.negativeBar, 
                        SerializableObjects.bar, SerializableObjects.negativeBar},
                new EnumSample[]{EnumSample.TYPE0, EnumSample.TYPE2}, 
                new ByteString[]{ByteString.copyFromUtf8("ef"), ByteString.copyFromUtf8("gh")}, 
                new Boolean[]{true, false}, 
                new Float[]{1234.4321f, -1234.4321f, 0f}, 
                new Double[]{12345678.87654321d, -12345678.87654321d, 0d}, 
                new Long[]{7060504030201l, -7060504030201l, 0l});
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        final LinkedBuffer buffer = LinkedBuffer.allocate(256);
        
        try
        {
            ProtostuffIOUtil.writeDelimitedTo(out, fooCompare, fooCompare.cachedSchema(), 
                    buffer);
        }
        finally
        {
            buffer.clear();
        }
        
        byte[] data = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Foo foo = new Foo();
        
        boolean hasException = true;
        try
        {
            ProtostuffIOUtil.mergeDelimitedFrom(in, foo, foo.cachedSchema(), buffer);
            hasException = false;
        }
        catch(ProtostuffException e)
        {
            assertTrue(e.getMessage().startsWith("size limit exceeded."));
        }
        
        assertTrue(hasException);
    }

}
