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
 * Test the streaming (computed) output capability of {@link CodedOutput}.
 *
 * @author David Yu
 * @created Oct 7, 2010
 */
public class ProtobufCodedOutputTest extends SerDeserTest
{
    
    protected <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        ProtobufIOUtil.mergeDelimitedFrom(in, message, schema);
    }

    protected <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
            throws IOException
    {
        final ComputedSizeOutput sizeCount = new ComputedSizeOutput(false);
        schema.writeTo(sizeCount, message);
        CodedOutput.writeRawVarInt32Bytes(out, sizeCount.getSize());
        final CodedOutput output = CodedOutput.newInstance(out);
        schema.writeTo(output, message);
        output.flush();
    }

    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema)
            throws IOException
    {
        final CodedInput input = new CodedInput(data, offset, length, false);
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
        assertTrue(input.isAtEnd());
    }

    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return CodedOutput.toByteArray(message, schema, false);
    }

}
