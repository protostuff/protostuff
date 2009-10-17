package com.dyuproject.protostuff.benchmark.serializers;

import java.io.IOException;
import java.nio.charset.Charset;

import com.dyuproject.protostuff.benchmark.V22SpeedMedia;
import com.google.protobuf.JsonFormat;

/**
 * Copied from http://thrift-protobuf-compare.googlecode.com/svn/trunk/tpc/src/serializers/
 * Tweaked a bit to subclass AbstractSpeedMediaSerializer
 *
 */
public class ProtobufJsonSerializer extends AbstractSpeedMediaSerializer
{
    
    private final Charset _charset = Charset.forName("UTF-8");

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
