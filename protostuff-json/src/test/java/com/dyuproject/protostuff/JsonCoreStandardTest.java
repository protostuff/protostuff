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

/**
 * Json standard tests.
 *
 * @author David Yu
 * @created Oct 8, 2010
 */
public class JsonCoreStandardTest extends StandardTest
{

    protected <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema) throws IOException
    {
        JsonIOUtil.mergeFrom(data, 0, data.length, message, schema, false);
    }

    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return JsonIOUtil.toByteArray(message, schema, false);
    }

}
