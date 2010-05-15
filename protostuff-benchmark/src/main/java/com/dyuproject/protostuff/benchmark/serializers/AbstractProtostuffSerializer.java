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
        Image image1 = new Image("http://javaone.com/keynote_large.jpg")
            .setTitle("Javaone Keynote")
            .setHeight(0)
            .setWidth(0)
            .setSize(Size.LARGE);
        
        Image image2 = new Image("http://javaone.com/keynote_thumbnail.jpg")
            .setTitle("Javaone Keynote")
            .setHeight(0)
            .setWidth(0)
            .setSize(Size.SMALL);
        
        ArrayList<Image> imageList = new ArrayList<Image>();
        imageList.add(image1);
        imageList.add(image2);

        ArrayList<String> personList = new ArrayList<String>();
        personList.add("Bill Gates");
        personList.add("Steve Jobs");
        
        Media media = new Media("http://javaone.com/keynote.mpg")
            .setTitle("Javaone Keynote")
            .setWidth(0)
            .setHeight(0)
            .setFormat("video/mpg4")
            .setDuration(1234567l)
            .setSize(123l)
            .setBitrate(123)
            .setPlayer(Player.JAVA)
            .setPersonList(personList);

        MediaContent mediaContent = new MediaContent();
        mediaContent.setImageList(imageList);
        mediaContent.setMedia(media);
        
        return mediaContent;
    }

}
