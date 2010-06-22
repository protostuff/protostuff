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
 * Various tests for the {@link ByteArrayInput}.
 *
 * @author David Yu
 * @created Jun 22, 2010
 */
public class ByteArrayInputTest extends SerDeserTest
{
    
    static final boolean GROUP_ENCODED = false;

    public <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema) 
    throws IOException
    {
        ByteArrayInput input = new ByteArrayInput(data, offset, length, GROUP_ENCODED);
        try
        {
            schema.mergeFrom(input, message);
            input.checkLastTagWas(0);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw ProtobufException.truncatedMessage();
        }
    }
    
    public boolean isGroupEncoded()
    {
        return GROUP_ENCODED;
    }

    public static <T> byte[] getByteArray(T message, Schema<T> schema)
    {
        byte[] result = CodedOutput.toByteArray(message, schema, GROUP_ENCODED);
        return result;
    }

    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return getByteArray(message, schema);
    }

    public <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema) throws IOException
    {
        int size = ComputedSizeOutput.getSize(message, schema, GROUP_ENCODED);
        CodedOutput.writeRawVarInt32Bytes(out, size);
        
        CodedOutput output = new CodedOutput(out, new byte[CodedOutput.DEFAULT_BUFFER_SIZE], 
                GROUP_ENCODED);
        
        schema.writeTo(output, message);
        output.flush();
    }

}
