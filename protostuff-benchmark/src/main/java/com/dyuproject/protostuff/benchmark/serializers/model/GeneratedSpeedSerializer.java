//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.benchmark.serializers.model;

import java.io.ByteArrayOutputStream;

import org.codehaus.jackson.JsonParser;

import com.dyuproject.protostuff.benchmark.V2SpeedMedia;
import com.dyuproject.protostuff.benchmark.V2SpeedMedia.MediaContent;
import com.dyuproject.protostuff.benchmark.generated.V2SpeedMediaJSON;
import com.dyuproject.protostuff.benchmark.serializers.AbstractSpeedMediaSerializer;

/**
 * @author David Yu
 * @created Oct 16, 2009
 */

public class GeneratedSpeedSerializer extends AbstractSpeedMediaSerializer
{
    
    final V2SpeedMediaJSON pbJSON = new V2SpeedMediaJSON();

    public MediaContent deserialize(byte[] array) throws Exception
    {
        V2SpeedMedia.MediaContent.Builder builder = V2SpeedMedia.MediaContent.newBuilder();
        JsonParser parser = pbJSON.getJsonFactory().createJsonParser(array);
        pbJSON.mergeFrom(parser, builder);
        parser.close();
        return builder.build();
    }

    public String getName()
    {        
        return "generated-speed-json";
    }

    public byte[] serialize(MediaContent content) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        pbJSON.writeTo(out, content);
        return out.toByteArray();
    }

}
