package com.dyuproject.protostuff;

import java.io.IOException;
import java.nio.ByteBuffer;

import static com.dyuproject.protostuff.ProtobufOutput.encodeZigZag32;
import static com.dyuproject.protostuff.ProtobufOutput.encodeZigZag64;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_END_GROUP;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_FIXED32;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_FIXED64;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_LENGTH_DELIMITED;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_START_GROUP;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_VARINT;
import static com.dyuproject.protostuff.WireFormat.makeTag;

/**
 * Created by ryan on 1/16/14.
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

    // public ProtostuffOutput(LinkedBuffer buffer)
    // {
    // super(buffer);
    // }
    //
    // public ProtostuffOutput(LinkedBuffer buffer, OutputStream out)
    // {
    // super(buffer, out);
    // }

    /**
     * Resets this output for re-use.
     */
    // public ProtostuffOutput clear()
    // {
    // super.clear();
    // return this;
    // }
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        if (value < 0)
        {
            buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
            buffer.writeVarInt64(value);

            // tail = sink.writeVarInt64(
            // value,
            // this,
            // sink.writeVarInt32(
            // makeTag(fieldNumber, WIRETYPE_VARINT),
            // this,
            // tail));
        }
        else
        {
            buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
            buffer.writeVarInt32(value);

            // tail = sink.writeVarInt32(
            // value,
            // this,
            // sink.writeVarInt32(
            // makeTag(fieldNumber, WIRETYPE_VARINT),
            // this,
            // tail));
        }

        /*
         * if(value < 0) { tail = writeTagAndRawVarInt64( makeTag(fieldNumber, WIRETYPE_VARINT), value, this, tail); }
         * else { tail = writeTagAndRawVarInt32( makeTag(fieldNumber, WIRETYPE_VARINT), value, this, tail); }
         */
    }

    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt32(value);
        // tail = sink.writeVarInt32(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_VARINT),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawVarInt32( makeTag(fieldNumber, WIRETYPE_VARINT), value, this, tail);
         */
    }

    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt32(encodeZigZag32(value));
        // tail = sink.writeVarInt32(
        // encodeZigZag32(value),
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_VARINT),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawVarInt32( makeTag(fieldNumber, WIRETYPE_VARINT), encodeZigZag32(value), this, tail);
         */
    }

    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED32));
        buffer.writeInt32LE(value);
        // tail = sink.writeInt32LE(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_FIXED32),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawLittleEndian32( makeTag(fieldNumber, WIRETYPE_FIXED32), value, this, tail);
         */
    }

    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED32));
        buffer.writeInt32LE(value);
        // tail = sink.writeInt32LE(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_FIXED32),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawLittleEndian32( makeTag(fieldNumber, WIRETYPE_FIXED32), value, this, tail);
         */
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt64(value);
        // tail = sink.writeVarInt64(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_VARINT),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawVarInt64( makeTag(fieldNumber, WIRETYPE_VARINT), value, this, tail);
         */
    }

    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt64(value);
        // tail = sink.writeVarInt64(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_VARINT),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawVarInt64( makeTag(fieldNumber, WIRETYPE_VARINT), value, this, tail);
         */
    }

    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeVarInt64(encodeZigZag64(value));
        // tail = sink.writeVarInt64(
        // encodeZigZag64(value),
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_VARINT),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawVarInt64( makeTag(fieldNumber, WIRETYPE_VARINT), encodeZigZag64(value), this, tail);
         */
    }

    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED64));
        buffer.writeInt64LE(value);
        // tail = sink.writeInt64LE(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_FIXED64),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawLittleEndian64( makeTag(fieldNumber, WIRETYPE_FIXED64), value, this, tail);
         */
    }

    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED64));
        buffer.writeInt64LE(value);
        // tail = sink.writeInt64LE(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_FIXED64),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawLittleEndian64( makeTag(fieldNumber, WIRETYPE_FIXED64), value, this, tail);
         */
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED32));
        buffer.writeInt32LE(Float.floatToRawIntBits(value));
        // tail = sink.writeInt32LE(
        // Float.floatToRawIntBits(value),
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_FIXED32),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawLittleEndian32( makeTag(fieldNumber, WIRETYPE_FIXED32), Float.floatToRawIntBits(value),
         * this, tail);
         */
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_FIXED64));
        buffer.writeInt64LE(Double.doubleToRawLongBits(value));
        // tail = sink.writeInt64LE(
        // Double.doubleToRawLongBits(value),
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_FIXED64),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawLittleEndian64( makeTag(fieldNumber, WIRETYPE_FIXED64),
         * Double.doubleToRawLongBits(value), this, tail);
         */
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_VARINT));
        buffer.writeByte(value ? (byte) 0x01 : 0x00);
        // tail = sink.writeByte(
        // value ? (byte)0x01 : 0x00,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_VARINT),
        // this,
        // tail));

        /*
         * tail = writeTagAndRawVarInt32( makeTag(fieldNumber, WIRETYPE_VARINT), value ? 1 : 0, this, tail);
         */
    }

    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, number, repeated);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        // TODO the original implementation is a lot more complex, is this compatible?
        byte[] strbytes = value.getBytes("UTF-8");
        writeByteArray(fieldNumber, strbytes, repeated);

        // tail = sink.writeStrUTF8VarDelimited(
        // value,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED),
        // this,
        // tail));

        /*
         * tail = writeUTF8VarDelimited( value, this, writeRawVarInt32(makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED),
         * this, tail));
         */
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException
    {
        writeByteRange(false, fieldNumber, bytes, 0, bytes.length, repeated);
        // buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED));
        // buffer.writeVarInt32(bytes.length);
        // tail = sink.writeByteArray(
        // bytes, 0, bytes.length,
        // this,
        // sink.writeVarInt32(
        // bytes.length,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED),
        // this,
        // tail)));

        /*
         * tail = writeTagAndByteArray( makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), bytes, this, tail);
         */
    }

    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED));
        buffer.writeVarInt32(length);
        buffer.writeByteArray(value, offset, length);

        // tail = sink.writeByteArray(
        // value, offset, length,
        // this,
        // sink.writeVarInt32(
        // length,
        // this,
        // sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED),
        // this,
        // tail)));
    }

    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema,
            final boolean repeated) throws IOException
    {
        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_START_GROUP));
        // tail = sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_START_GROUP),
        // this,
        // tail);

        schema.writeTo(this, value);

        buffer.writeVarInt32(makeTag(fieldNumber, WIRETYPE_END_GROUP));
        // tail = sink.writeVarInt32(
        // makeTag(fieldNumber, WIRETYPE_END_GROUP),
        // this,
        // tail);
    }

    /**
     * Writes a ByteBuffer field.
     */
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {
        writeByteRange(false, fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated);
    }

}
