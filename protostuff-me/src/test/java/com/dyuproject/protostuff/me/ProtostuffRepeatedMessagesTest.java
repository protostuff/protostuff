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
import java.util.Vector;

/**
 * Testcase for ser/deser of multiple messages using protostuff
 *
 * @author David Yu
 * @created Oct 7, 2010
 */
public class ProtostuffRepeatedMessagesTest extends RepeatedMessagesTest {

    protected Vector parseListFrom(InputStream in, Schema schema) throws IOException {
        return ProtostuffIOUtil.parseListFrom(in, schema);
    }

    protected void writeListTo(OutputStream out, Vector messages, Schema schema)
            throws IOException {
        ProtostuffIOUtil.writeListTo(out, messages, schema, buf());
    }

}
