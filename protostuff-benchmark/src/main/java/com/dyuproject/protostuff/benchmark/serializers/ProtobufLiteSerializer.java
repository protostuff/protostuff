//========================================================================
//Copyright 2007-2008 David Yu dyuproject@gmail.com
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

import java.io.IOException;

import com.dyuproject.protostuff.benchmark.V2LiteMedia.MediaContent;

/**
 * @author David Yu
 * @created Oct 3, 2009
 */

public class ProtobufLiteSerializer extends AbstractLiteMediaSerializer
{
    
    public MediaContent deserialize(byte[] array) throws Exception
    {
        return MediaContent.parseFrom(array);
    }

    public byte[] serialize(MediaContent content) throws IOException
    {
        return content.toByteArray();
    }

    public String getName()
    {
        return "protobuf-lite";
    }

}
