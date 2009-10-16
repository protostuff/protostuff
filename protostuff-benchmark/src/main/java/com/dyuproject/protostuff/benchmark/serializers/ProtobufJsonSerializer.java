package com.dyuproject.protostuff.benchmark.serializers;

import java.io.IOException;
import java.nio.charset.Charset;

import com.dyuproject.protostuff.benchmark.V22SpeedMedia;
import com.dyuproject.protostuff.benchmark.V22SpeedMedia.Image;
import com.dyuproject.protostuff.benchmark.V22SpeedMedia.Media;
import com.dyuproject.protostuff.benchmark.V22SpeedMedia.MediaContent;
import com.dyuproject.protostuff.benchmark.V22SpeedMedia.Image.Size;
import com.dyuproject.protostuff.benchmark.V22SpeedMedia.Media.Player;
import com.google.protobuf.JsonFormat;

/**
 * Copied from http://thrift-protobuf-compare.googlecode.com/svn/trunk/tpc/src/serializers/
 *
 */
public class ProtobufJsonSerializer implements ObjectSerializer<V22SpeedMedia.MediaContent>
{
    private final Charset _charset = Charset.forName("UTF-8");
    
    public MediaContent create()
    {
        MediaContent contentProto = MediaContent
        .newBuilder().setMedia(
                Media.newBuilder()
                        .clearCopyright()
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

    public V22SpeedMedia.MediaContent deserialize (byte[] array) throws Exception
    {
      V22SpeedMedia.MediaContent.Builder builder = V22SpeedMedia.MediaContent.newBuilder();
      JsonFormat.merge(new String(array, _charset.name()), builder);
      return builder.build();
    }

    public byte[] serialize(V22SpeedMedia.MediaContent content) throws IOException
    {
      return JsonFormat.printToString(content).getBytes(_charset.name());
    }

    public String getName ()
    {
      return "protobuf-json(JsonFormat.java)";
    }
}
