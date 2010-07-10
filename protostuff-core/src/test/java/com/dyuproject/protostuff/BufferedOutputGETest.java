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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Test for group-encoded nested messages via {@link BufferedOutput}.
 *
 * @author David Yu
 * @created Jun 13, 2010
 */
public class BufferedOutputGETest extends GroupEncodedNestedMessageTest
{

    public static <T> byte[] getByteArray(T message, Schema<T> schema)
    {
        byte[] result = IOUtil.toByteArray(message, schema, 
                new LinkedBuffer(LinkedBuffer.DEFAULT_BUFFER_SIZE), GROUP_ENCODED);
        return result;
    }
    

    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return getByteArray(message, schema);
    }

    public <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        IOUtil.writeDelimitedTo(out, message, schema, 
                new LinkedBuffer(LinkedBuffer.DEFAULT_BUFFER_SIZE), GROUP_ENCODED);
    }

}
