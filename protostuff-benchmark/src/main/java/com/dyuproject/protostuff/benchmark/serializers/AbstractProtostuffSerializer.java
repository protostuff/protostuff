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
        Image image1 = new Image("http://javaone.com/keynote_large.jpg");
        image1.setTitle("Javaone Keynote");
        image1.setHeight(0);
        image1.setWidth(0);
        image1.setSize(Size.LARGE);
        
        Image image2 = new Image("http://javaone.com/keynote_thumbnail.jpg");
        image2.setTitle("Javaone Keynote");
        image2.setHeight(0);
        image2.setWidth(0);
        image2.setSize(Size.SMALL);
        
        ArrayList<Image> imageList = new ArrayList<Image>();
        imageList.add(image1);
        imageList.add(image2);

        ArrayList<String> personList = new ArrayList<String>();
        personList.add("Bill Gates");
        personList.add("Steve Jobs");
        
        Media media = new Media("http://javaone.com/keynote.mpg");
        media.setTitle("Javaone Keynote");
        media.setWidth(0);
        media.setHeight(0);
        media.setFormat("video/mpg4");
        media.setDuration(1234567l);
        media.setSize(123l);
        media.setBitrate(123);
        media.setPlayer(Player.JAVA);
        media.setPersonList(personList);

        MediaContent mediaContent = new MediaContent();
        mediaContent.setImageList(imageList);
        mediaContent.setMedia(media);
        
        return mediaContent;
    }

}
