//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

package io.protostuff;

import static io.protostuff.StringSerializer.computeUTF8Size;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Computes the size of the data to be written.
 * <p>
 * Used internally by {@link CodedOutput}.
 * 
 * @author David Yu
 * @created Nov 9, 2009
 */
public final class ComputedSizeOutput implements Output
{

    /**
     * Computes the serialized size of a message.
     */
    public static <T extends Message<T>> int getSize(T message)
    {
        return getSize(message, message.cachedSchema(), false);
    }

    /**
     * Computes the serialized size of a message tied to a schema.
     */
    public static <T> int getSize(T message, Schema<T> schema)
    {
        return getSize(message, schema, false);
    }

    /**
     * Computes the serialized size of a message tied to a schema.
     */
    public static <T> int getSize(T message, Schema<T> schema, boolean encodeNestedMessageAsGroup)
    {
        ComputedSizeOutput sizeCount = new ComputedSizeOutput(encodeNestedMessageAsGroup);
        try
        {
            schema.writeTo(sizeCount, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " +
                    "(should never happen).", e);
        }
        return sizeCount.size;
    }

    private int size = 0;
    private final boolean encodeNestedMessageAsGroup;

    public ComputedSizeOutput()
    {
        this(false);
    }

    public ComputedSizeOutput(boolean encodeNestedMessageAsGroup)
    {
        this.encodeNestedMessageAsGroup = encodeNestedMessageAsGroup;
    }

    /**
     * Gets the size of the bytes written to this output.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Resets the size to zero.
     */
    ComputedSizeOutput reset()
    {
        size = 0;
        return this;
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int s = ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_VARINT));
        s += value < 0 ? 10 : ProtobufOutput.computeRawVarint32Size(value);
        size += s;
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint32Size(value);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint32Size(
                ProtobufOutput.encodeZigZag32(value));
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_FIXED32)) + ProtobufOutput.LITTLE_ENDIAN_32_SIZE;
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_FIXED32)) + ProtobufOutput.LITTLE_ENDIAN_32_SIZE;
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint64Size(value);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint64Size(value);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint64Size(
                ProtobufOutput.encodeZigZag64(value));
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_FIXED64)) + ProtobufOutput.LITTLE_ENDIAN_64_SIZE;
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_FIXED64)) + ProtobufOutput.LITTLE_ENDIAN_64_SIZE;
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_FIXED32)) + ProtobufOutput.LITTLE_ENDIAN_32_SIZE;
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_FIXED64)) + ProtobufOutput.LITTLE_ENDIAN_64_SIZE;
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_VARINT)) + 1;
    }

    @Override
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeString(int fieldNumber, CharSequence value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        final int strSize = computeUTF8Size(value, 0, value.length());
        size += ProtobufOutput.computeRawVarint32Size(strSize) + strSize;
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        size += ProtobufOutput.computeRawVarint32Size(value.length) + value.length;
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        size += ProtobufOutput.computeRawVarint32Size(length) + length;
    }

    @Override
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema,
            final boolean repeated) throws IOException
    {
        if (encodeNestedMessageAsGroup)
        {
            writeObjectEncodedAsGroup(fieldNumber, value, schema, repeated);
            return;
        }

        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        final int last = size;
        schema.writeTo(this, value);

        final int actualSize = ProtobufOutput.computeRawVarint32Size(size - last);
        size += actualSize;
    }

    <T> void writeObjectEncodedAsGroup(int fieldNumber, T value, Schema<T> schema,
            boolean repeated) throws IOException
    {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_START_GROUP));

        schema.writeTo(this, value);

        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber,
                WireFormat.WIRETYPE_END_GROUP));
    }

    /**
     * Writes a ByteBuffer field.
     */
    @Override
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {
        writeByteRange(false, fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated);
    }

}
