package io.protostuff;

import static io.protostuff.ProtobufOutput.encodeZigZag32;
import static io.protostuff.ProtobufOutput.encodeZigZag64;
import static io.protostuff.WireFormat.WIRETYPE_END_GROUP;
import static io.protostuff.WireFormat.WIRETYPE_FIXED32;
import static io.protostuff.WireFormat.WIRETYPE_FIXED64;
import static io.protostuff.WireFormat.WIRETYPE_LENGTH_DELIMITED;
import static io.protostuff.WireFormat.WIRETYPE_START_GROUP;
import static io.protostuff.WireFormat.WIRETYPE_VARINT;
import static io.protostuff.WireFormat.makeTag;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Output that differs from the standard by attempting to avoid extra copies of large ByteBuffer fields. When used with
 * ByteBuffer=true compiler option, we can splice in ByteBuffer objects without copying them. Most of the magic lives in
 * LinkBuffer, so this class exists just to serialize to a LinkBuffer.
 *
 * @author Ryan Rawson
 */
public final class LowCopyProtostuffOutput implements Output
{

    public LinkBuffer buffer;

    public LowCopyProtostuffOutput()
    {
        buffer = new LinkBuffer();
    }

    public LowCopyProtostuffOutput(final LinkBuffer buffer)
    {
        this.buffer = buffer;
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        if (value < 0)
        {
            buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
            buffer.writeVarInt64(value);
        }
        else
        {
            buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
            buffer.writeVarInt32(value);
        }
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt32(value);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt32(encodeZigZag32(value));
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED32));
        buffer.writeInt32LE(value);
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED32));
        buffer.writeInt32LE(value);
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt64(value);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt64(value);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt64(encodeZigZag64(value));
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED64));
        buffer.writeInt64LE(value);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED64));
        buffer.writeInt64LE(value);
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED32));
        buffer.writeInt32LE(Float.floatToRawIntBits(value));
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED64));
        buffer.writeInt64LE(Double.doubleToRawLongBits(value));
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeByte(value ? (byte) 0x01 : 0x00);
    }

    @Override
    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, number, repeated);
    }

    @Override
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        // TODO the original implementation is a lot more complex, is this compatible?
        byte[] strbytes = value.getBytes("UTF-8");
        writeByteArray(fieldNumber, strbytes, repeated);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException
    {
        writeByteRange(false, fieldNumber, bytes, 0, bytes.length, repeated);
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED));
        buffer.writeVarInt32(length);
        buffer.writeByteArray(value, offset, length);
    }

    @Override
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema,
            final boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_START_GROUP));
        schema.writeTo(this, value);
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_END_GROUP));
    }

    @Override
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {
        writeByteRange(false, fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated);
    }

}
