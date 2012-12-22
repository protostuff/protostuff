//========================================================================
//Copyright 2012 David Yu
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

/**
 * Tests for {@link CodedInput}.
 * 
 * @author Max Lanin
 * @created Dec 22, 2012
 */
public class CodedInputTest extends AbstractTest
{

    public void testSkipFieldOverTheBufferBoundary() throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int tag = WireFormat.makeTag(1, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        int anotherTag = WireFormat.makeTag(2, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        int msgLength = 10;

        ProtobufOutput.writeRawVarInt32Bytes(out, tag);
        ProtobufOutput.writeRawVarInt32Bytes(out, msgLength);
        for (int i = 1; i <= msgLength; i++) ProtobufOutput.writeRawVarInt32Bytes(out, i);
        ProtobufOutput.writeRawVarInt32Bytes(out, anotherTag);

        byte[] data = out.toByteArray();

        CodedInput ci = new CodedInput(new ByteArrayInputStream(data), new byte[10], false);
        ci.pushLimit(msgLength + 2);    // +2 for tag and length
        assertEquals(tag, ci.readTag());
        ci.skipField(tag);
        assertEquals(0, ci.readTag());
    }
    
}
