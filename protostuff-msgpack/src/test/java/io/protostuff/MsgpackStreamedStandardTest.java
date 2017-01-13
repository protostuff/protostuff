//========================================================================
//Copyright (C) 2017 Alex Shvid
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

/**
 * Message pack standard tests that are using MsgpackXOutput
 * 
 * @author Alex Shvid
 */

public class MsgpackStreamedStandardTest extends StandardTest
{

    @Override
    protected <T> void mergeFrom(byte[] data, int offset, int length, T message,
            Schema<T> schema) throws IOException
    {
        MsgpackIOUtil.mergeFrom(data, 0, data.length, message, schema, false);
    }

    @Override
    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            MsgpackXIOUtil.writeTo(out, message, schema, false, buf());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }
    
}
