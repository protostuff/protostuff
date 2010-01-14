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

import com.dyuproject.protostuff.IOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.benchmark.MediaContent;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * TODO
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public class ProtostuffRuntimeSerializer extends AbstractProtostuffSerializer
{
    
    static final Schema<MediaContent> schema = RuntimeSchema.getSchema(MediaContent.class);

    public MediaContent deserialize(byte[] array) throws Exception
    {
        MediaContent mediaContent = new MediaContent();
        IOUtil.mergeFrom(array, mediaContent, schema);
        return mediaContent;
    }

    public String getName()
    {
        return "protostuff-runtime";
    }

    public byte[] serialize(MediaContent content) throws Exception
    {
        return IOUtil.toByteArray(content, schema);
    }

}
