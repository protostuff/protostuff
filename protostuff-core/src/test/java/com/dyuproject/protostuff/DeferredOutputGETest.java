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
 * Test for group-encoded nested messages via {@link DeferredOutput}.
 *
 * @author David Yu
 * @created Jun 13, 2010
 */
public class DeferredOutputGETest extends GroupEncodedNestedMessageTest
{

    public static <T> byte[] getByteArray(T message, Schema<T> schema)
    {
        try
        {
            DeferredOutput output = new DeferredOutput(GROUP_ENCODED);
            schema.writeTo(output, message);
            byte[] result = output.toByteArray();
            return result;
        }
        catch(IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
    }

    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return getByteArray(message, schema);
    }

    public <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema) 
    throws IOException
    {
        DeferredOutput output = new DeferredOutput(GROUP_ENCODED);
        schema.writeTo(output, message);
        CodedOutput.writeRawVarInt32Bytes(out, output.getSize());
        output.streamTo(out);
    }

}
