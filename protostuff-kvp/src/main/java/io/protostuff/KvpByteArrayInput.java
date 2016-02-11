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

package io.protostuff;

import static io.protostuff.NumberParser.parseInt;
import static io.protostuff.NumberParser.parseLong;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.protostuff.StringSerializer.STRING;

/**
 * An input for deserializing kvp-encoded messages. A kvp encoding is a binary encoding w/c contains a key-value
 * sequence. On the wire, a serialized field (key-value) would look like: [key-len][key][value-len][value]
 * <p>
 * The keys and values are length-delimited (uint16 little endian).
 * <p>
 * Note that this encoding does not support nested messages. This encoding is mostly useful for headers w/c contain
 * information about the content it carries (see http://projects.unbit.it/uwsgi/wiki/uwsgiProtocol).
 * 
 * @author David Yu
 * @created Nov 19, 2010
 */
public final class KvpByteArrayInput implements Input
{

    private static final String MISREPORTED_SIZE = "Misreported size.";

    final byte[] buffer;
    int offset, limit;

    final boolean numeric;

    public KvpByteArrayInput(byte[] buffer, int offset, int len, boolean numeric)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.limit = offset + len;

        this.numeric = numeric;
    }

    @Override
    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        if (offset == limit)
            return 0;

        final int size = buffer[offset++] | (buffer[offset++] << 8);
        final int number = numeric ? parseInt(buffer, offset, size, 10, true) :
                schema.getFieldNumber(STRING.deser(buffer, offset, size));

        offset += size;

        if (number == 0)
        {
            // skip unknown fields.
            handleUnknownField(number, schema);
            return readFieldNumber(schema);
        }

        return number;
    }

    @Override
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        offset += size;
    }

    @Override
    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean readBool() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);

        if (size != 1)
            throw new ProtostuffException("Not a valid kvp boolean");

        return buffer[offset++] != 0x30;
    }

    @Override
    public byte[] readByteArray() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if (size == 0)
            return ByteString.EMPTY_BYTE_ARRAY;

        if (offset + size > limit)
            throw new ProtostuffException(MISREPORTED_SIZE);

        byte[] data = new byte[size];
        System.arraycopy(buffer, offset, data, 0, size);
        offset += size;
        return data;
    }

    @Override
    public ByteString readBytes() throws IOException
    {
        return ByteString.wrap(readByteArray());
    }

    @Override
    public double readDouble() throws IOException
    {
        // TODO efficiency

        return Double.parseDouble(readString());
    }

    @Override
    public float readFloat() throws IOException
    {
        // TODO efficiency

        return Float.parseFloat(readString());
    }

    @Override
    public int readUInt32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readUInt64() throws IOException
    {
        return readInt64();
    }

    @Override
    public int readInt32() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if (size == 0)
            return 0;

        if (offset + size > limit)
            throw new ProtostuffException(MISREPORTED_SIZE);

        final int number = parseInt(buffer, offset, size, 10);
        offset += size;
        return number;
    }

    @Override
    public long readInt64() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if (size == 0)
            return 0;

        if (offset + size > limit)
            throw new ProtostuffException(MISREPORTED_SIZE);

        final long number = parseLong(buffer, offset, size, 10);
        offset += size;
        return number;
    }

    @Override
    public int readEnum() throws IOException
    {
        return readInt32();
    }

    @Override
    public int readFixed32() throws IOException
    {
        return readUInt32();
    }

    @Override
    public long readFixed64() throws IOException
    {
        return readUInt64();
    }

    @Override
    public int readSFixed32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readSFixed64() throws IOException
    {
        return readInt64();
    }

    @Override
    public int readSInt32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readSInt64() throws IOException
    {
        return readInt64();
    }

    @Override
    public String readString() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if (size == 0)
            return ByteString.EMPTY_STRING;

        if (offset + size > limit)
            throw new ProtostuffException(MISREPORTED_SIZE);

        final String str = STRING.deser(buffer, offset, size);
        offset += size;
        return str;
    }

    @Override
    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber,
            boolean repeated) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Reads a byte array/ByteBuffer value.
     */
    @Override
    public ByteBuffer readByteBuffer() throws IOException
    {
        return ByteBuffer.wrap(readByteArray());
    }

}
