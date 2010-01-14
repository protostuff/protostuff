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

import java.util.ArrayList;

import com.dyuproject.protostuff.benchmark.Image;
import com.dyuproject.protostuff.benchmark.Media;
import com.dyuproject.protostuff.benchmark.MediaContent;
import com.dyuproject.protostuff.benchmark.Image.Size;
import com.dyuproject.protostuff.benchmark.Media.Player;

/**
 * TODO
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public abstract class AbstractProtostuffSerializer implements ObjectSerializer<MediaContent>
{
    
    public MediaContent create()
    {
        ArrayList<Image> image = new ArrayList<Image>();
        image.add(new Image(
                "http://javaone.com/keynote_large.jpg",
                "Javaone Keynote",
                0,
                0,
                Size.LARGE)
        );
        image.add(new Image(
                "http://javaone.com/keynote_thumbnail.jpg",
                "Javaone Keynote",
                0,
                0,
                Size.SMALL)
        );
        ArrayList<String> person = new ArrayList<String>();
        person.add("Bill Gates");
        person.add("Steve Jobs");
        
        Media media = new Media(
                "http://javaone.com/keynote.mpg",
                "Javaone Keynote",
                0,
                0,
                "video/mpg4",
                1234567l,
                123l,
                123,
                person,
                Player.JAVA,
                null
                );

        return new MediaContent(image, media);
    }

}
