package com.dyuproject.protostuff.benchmark.serializers;

import java.io.IOException;

import com.dyuproject.protostuff.benchmark.V2SpeedMedia.MediaContent;

public class ProtobufSpeedSerializer extends AbstractSpeedMediaSerializer
{

    public MediaContent deserialize(byte[] array) throws Exception
    {
        return MediaContent.parseFrom(array);
    }

    public byte[] serialize(MediaContent content) throws IOException
    {
        return content.toByteArray();
    }

    public String getName()
    {
        return "protobuf-speed";
    }
    
}
