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

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test for re-using a thread-local buffer across many serializations.
 *
 * @author David Yu
 * @created Jan 15, 2011
 */
public class JsonBufferReuseTest extends StandardTest
{
    
    private static final ThreadLocal<LinkedBuffer> localBuffer = 
        new ThreadLocal<LinkedBuffer>()
    {
        protected LinkedBuffer initialValue()
        {
            return buf();
        }
    };
    
    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema) throws IOException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data, offset, length);
        JsonIOUtil.mergeFrom(in, message, schema, false, localBuffer.get());
    }

    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        final LinkedBuffer buffer = localBuffer.get();
        try
        {
            return JsonXIOUtil.toByteArray(message, schema, false, buffer);
        }
        finally
        {
            buffer.clear();
        }
    }

}
