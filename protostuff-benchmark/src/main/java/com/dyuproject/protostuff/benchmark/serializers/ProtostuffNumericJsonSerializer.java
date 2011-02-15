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

package com.dyuproject.protostuff.benchmark.serializers;

import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.benchmark.MediaContent;

/**
 * TODO
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public class ProtostuffNumericJsonSerializer extends AbstractProtostuffSerializer
{
    
    public MediaContent deserialize(byte[] array) throws Exception
    {
        MediaContent mediaContent = new MediaContent();
        JsonIOUtil.mergeFrom(array, mediaContent, mediaContent.cachedSchema(), true);
        return mediaContent;
    }

    public String getName()
    {
        return "json/protostuff+numeric";
    }

    public byte[] serialize(MediaContent content) throws Exception
    {
        return JsonIOUtil.toByteArray(content, content.cachedSchema(), true);
    }

}
