//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.benchmark.serializers;

import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.JsonXIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.benchmark.MediaContent;

/**
 * Compare regular JsonOutput vs JsonXOutput for numeric json output.
 *
 * @author David Yu
 * @created Jan 17, 2011
 */
public class ProtostuffNumericJsonXSerializer extends AbstractProtostuffSerializer
{
    
    final LinkedBuffer buffer = LinkedBuffer.allocate(512);
    
    public MediaContent deserialize(byte[] array) throws Exception
    {
        MediaContent mediaContent = new MediaContent();
        JsonIOUtil.mergeFrom(array, mediaContent, mediaContent.cachedSchema(), true);
        return mediaContent;
    }

    public String getName()
    {
        return "protostuff-numeric-jsonx";
    }

    public byte[] serialize(MediaContent content) throws Exception
    {
        try
        {
            return JsonXIOUtil.toByteArray(content, content.cachedSchema(), true, 
                    buffer);
        }
        finally
        {
            buffer.clear();
        }
    }

}
