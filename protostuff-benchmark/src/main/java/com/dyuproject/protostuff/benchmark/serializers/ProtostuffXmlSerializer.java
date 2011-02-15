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

import com.dyuproject.protostuff.XmlIOUtil;
import com.dyuproject.protostuff.benchmark.MediaContent;

/**
 * xml ser/deser.
 *
 * @author David Yu
 * @created Jan 22, 2011
 */
public class ProtostuffXmlSerializer extends AbstractProtostuffSerializer
{

    public MediaContent deserialize(byte[] array) throws Exception
    {
        MediaContent mediaContent = new MediaContent();
        XmlIOUtil.mergeFrom(array, mediaContent, mediaContent.cachedSchema());
        return mediaContent;
    }

    public String getName()
    {
        return "xml/protostuff";
    }

    public byte[] serialize(MediaContent content) throws Exception
    {
        return XmlIOUtil.toByteArray(content, content.cachedSchema());
    }

}
