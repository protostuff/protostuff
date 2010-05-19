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

import com.dyuproject.protostuff.benchmark.Image;
import com.dyuproject.protostuff.benchmark.Media;
import com.dyuproject.protostuff.benchmark.MediaContent;
import com.dyuproject.protostuff.benchmark.Image.Size;
import com.dyuproject.protostuff.benchmark.Media.Player;

/**
 * Base serializer for protostuff.
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public abstract class AbstractProtostuffSerializer implements ObjectSerializer<MediaContent>
{
    
    public MediaContent create()
    {
        MediaContent mediaContent = new MediaContent(
                new Media(
                        "http://javaone.com/keynote.mpg", 
                        0, 
                        0, 
                        "video/mpg4", 
                        1234567l, 
                        123l, 
                        Player.JAVA)
                    .setTitle("Javaone Keynote")
                    .setBitrate(123)
                    .addPerson("Bill Gates")
                    .addPerson("Steve Jobs")
            )
            .addImage(new Image(
                    "http://javaone.com/keynote_large.jpg", 
                    0, 
                    0, 
                    Size.LARGE)
                .setTitle("Javaone Keynote")
            )
            .addImage(new Image(
                    "http://javaone.com/keynote_thumbnail.jpg", 
                    0, 
                    0, 
                    Size.SMALL)
                .setTitle("Javaone Keynote")
            );
        
        return mediaContent;
    }

}
