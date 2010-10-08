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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test the buffering output capability of {@link ProtostuffOutput}.
 *
 * @author David Yu
 * @created Sep 19, 2010
 */
public class ProtostuffBufferedOutputTest extends SerDeserTest
{
    
    protected <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        ProtostuffIOUtil.mergeDelimitedFrom(in, message, schema);
    }

    protected <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
            throws IOException
    {
        ProtostuffIOUtil.writeDelimitedTo(out, message, schema, buf());
    }

    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema)
            throws IOException
    {
        ProtostuffIOUtil.mergeFrom(data, offset, length, message, schema);
    }
    
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return ProtostuffIOUtil.toByteArray(message, schema, buf());
    }


}
