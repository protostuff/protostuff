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

package com.dyuproject.protostuff.benchmark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.SmileIOUtil;
import com.dyuproject.protostuff.XmlIOUtil;
import com.dyuproject.protostuff.benchmark.Image.Size;
import com.dyuproject.protostuff.benchmark.Media.Player;

/**
 * A test that asserts that the {@link InputStream} nor the {@link OutputStream} should 
 * be closed after IO operations.
 * 
 * Streams should be application managed.
 *
 * @author David Yu
 * @created Feb 11, 2011
 */
public class StreamCloseTest extends TestCase
{
    
    public static class Baos extends ByteArrayOutputStream
    {
        
        
        public void close() throws IOException
        {
            throw new RuntimeException("Should not be closed!");
        }
    }
    
    public static class Bais extends ByteArrayInputStream
    {

        public Bais(byte[] arg0)
        {
            super(arg0);
        }

        public void close() throws IOException
        {
            throw new RuntimeException("Should not be closed!");
        }
    }
    
    static LinkedBuffer buf()
    {
        return LinkedBuffer.allocate(256);
    }
    
    static MediaContent newMediaContent()
    {
        return new MediaContent(
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
    }
    
    public void testProtostuff() throws IOException
    {
        MediaContent content = newMediaContent();
        Baos out = new Baos();
        ProtostuffIOUtil.writeTo(out, content, MediaContent.getSchema(), buf());
        
        MediaContent mergedContent = new MediaContent();
        Bais in = new Bais(out.toByteArray());
        ProtostuffIOUtil.mergeFrom(in, mergedContent, MediaContent.getSchema());
    }
    
    public void testProtobuf() throws IOException
    {
        MediaContent content = newMediaContent();
        Baos out = new Baos();
        ProtobufIOUtil.writeTo(out, content, MediaContent.getSchema(), buf());
        
        MediaContent mergedContent = new MediaContent();
        Bais in = new Bais(out.toByteArray());
        ProtobufIOUtil.mergeFrom(in, mergedContent, MediaContent.getSchema());
    }
    
    public void testJson() throws IOException
    {
        MediaContent content = newMediaContent();
        Baos out = new Baos();
        JsonIOUtil.writeTo(out, content, MediaContent.getSchema(), false);
        
        MediaContent mergedContent = new MediaContent();
        Bais in = new Bais(out.toByteArray());
        JsonIOUtil.mergeFrom(in, mergedContent, MediaContent.getSchema(), false);
    }
    
    public void testSmile() throws IOException
    {
        MediaContent content = newMediaContent();
        Baos out = new Baos();
        SmileIOUtil.writeTo(out, content, MediaContent.getSchema(), false);
        
        MediaContent mergedContent = new MediaContent();
        Bais in = new Bais(out.toByteArray());
        SmileIOUtil.mergeFrom(in, mergedContent, MediaContent.getSchema(), false);
    }
    
    // this is provider dependent.
    public void $testXml() throws IOException
    {
        MediaContent content = newMediaContent();
        Baos out = new Baos();
        XmlIOUtil.writeTo(out, content, MediaContent.getSchema());
        
        MediaContent mergedContent = new MediaContent();
        Bais in = new Bais(out.toByteArray());
        XmlIOUtil.mergeFrom(in, mergedContent, MediaContent.getSchema());
    }

}
