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

import java.io.ByteArrayOutputStream;

import org.codehaus.jackson.JsonParser;

import com.dyuproject.protostuff.benchmark.V22LiteMedia;
import com.dyuproject.protostuff.benchmark.V22LiteMedia.MediaContent;
import com.dyuproject.protostuff.json.ReflectionJSON;

/**
 * @author David Yu
 * @created Oct 2, 2009
 */

public class ReflectionLiteSerializer extends AbstractLiteMediaSerializer
{
    
    final ReflectionJSON pbJSON = new ReflectionJSON(new Class[]{V22LiteMedia.class});

    public MediaContent deserialize(byte[] array) throws Exception
    {
        V22LiteMedia.MediaContent.Builder builder = V22LiteMedia.MediaContent.newBuilder();
        JsonParser parser = pbJSON.getJsonFactory().createJsonParser(array);
        pbJSON.mergeFrom(pbJSON.getJsonFactory().createJsonParser(array), builder);
        parser.close();
        return builder.build();
    }

    public String getName()
    {        
        return "reflection-lite-json";
    }

    public byte[] serialize(MediaContent content) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        pbJSON.writeTo(out, content);
        return out.toByteArray();
    }
    
}
