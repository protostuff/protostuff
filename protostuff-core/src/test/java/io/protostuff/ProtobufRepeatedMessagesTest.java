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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Testcase for ser/deser of multiple messages using protobuf.
 * 
 * @author David Yu
 * @created Oct 7, 2010
 */
public class ProtobufRepeatedMessagesTest extends RepeatedMessagesTest
{

    @Override
    protected <T> List<T> parseListFrom(InputStream in, Schema<T> schema) throws IOException
    {
        return ProtobufIOUtil.parseListFrom(in, schema);
    }

    @Override
    protected <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema)
            throws IOException
    {
        ProtobufIOUtil.writeListTo(out, messages, schema, buf());
    }

}
