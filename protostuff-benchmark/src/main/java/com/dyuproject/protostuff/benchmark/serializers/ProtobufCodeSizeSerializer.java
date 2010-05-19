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

import com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image;
import com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media;
import com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.MediaContent;
import com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Image.Size;
import com.dyuproject.protostuff.benchmark.V2CodeSizeMedia.Media.Player;

/**
 * @author David Yu
 * @created Oct 3, 2009
 */

public class ProtobufCodeSizeSerializer implements ObjectSerializer<MediaContent>
{
    
    public MediaContent deserialize(byte[] array) throws Exception
    {
        return MediaContent.parseFrom(array);
    }

    public byte[] serialize(MediaContent content) throws IOException
    {
        return content.toByteArray();
    }

    public MediaContent create()
    {
        MediaContent contentProto = MediaContent
        .newBuilder().setMedia(
                Media.newBuilder()
                        .setFormat("video/mpg4")
                        .setPlayer(Player.JAVA)
                        .setTitle("Javaone Keynote")
                        .setUri("http://javaone.com/keynote.mpg")
                        .setDuration(1234567)
                        .setSize(123)
                        .setHeight(0)
                        .setWidth(0)
                        .setBitrate(123)
                        .addPerson("Bill Gates")
                        .addPerson("Steve Jobs")
                        .build()
                ).addImage(
                        Image.newBuilder()
                        .setHeight(0)
                        .setTitle("Javaone Keynote")
                        .setUri("http://javaone.com/keynote_large.jpg")
                        .setWidth(0)
                        .setSize(Size.LARGE)
                        .build()
                ).addImage(
                        Image.newBuilder()
                        .setHeight(0)
                        .setTitle("Javaone Keynote")
                        .setUri("http://javaone.com/keynote_thumbnail.jpg")
                        .setWidth(0)
                        .setSize(Size.SMALL)
                        .build()
                )
                .build();
        return contentProto;
    }

    public String getName()
    {
        return "protobuf-codesize";
    }

}
