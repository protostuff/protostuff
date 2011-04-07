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

package com.dyuproject.protostuff.me;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test the buffering output capability of {@link ProtobufOutput}.
 *
 * @author David Yu
 * @created Oct 7, 2010
 */
public class ProtobufBufferedOutputTest extends SerDeserTest
{
    
    protected void mergeDelimitedFrom(InputStream in, Message message, Schema schema)
            throws IOException
    {
        ProtobufIOUtil.mergeDelimitedFrom(in, message, schema);
    }

    protected void writeDelimitedTo(OutputStream out, Message message, Schema schema)
            throws IOException
    {
        ProtobufIOUtil.writeDelimitedTo(out, message, schema, buf());
    }

    protected void mergeFrom(byte[] data, int offset, int length, Message message, Schema schema)
            throws IOException
    {
        ProtobufIOUtil.mergeFrom(data, offset, length, message, schema);
    }

    protected byte[] toByteArray(Message message, Schema schema)
    {
        return ProtobufIOUtil.toByteArray(message, schema, buf());
    }
    

}
