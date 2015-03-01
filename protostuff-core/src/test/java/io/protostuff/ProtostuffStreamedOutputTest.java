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

package io.protostuff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test the streaming output capability of {@link ProtostuffOutput}.
 * 
 * @author David Yu
 * @created Sep 19, 2010
 */
public class ProtostuffStreamedOutputTest extends SerDeserTest
{

    @Override
    protected <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        ProtostuffIOUtil.mergeDelimitedFrom(in, message, schema);
    }

    @Override
    protected <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
            throws IOException
    {
        ProtostuffIOUtil.writeDelimitedTo(out, message, schema, buf());
    }

    @Override
    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema)
            throws IOException
    {
        ProtostuffIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    @Override
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            ProtostuffIOUtil.writeTo(out, message, schema, buf());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " +
                    "(should never happen).", e);
        }

        return out.toByteArray();
    }

}
