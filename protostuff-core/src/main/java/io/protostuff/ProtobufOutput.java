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

import static io.protostuff.StringSerializer.writeUTF8VarDelimited;
import static io.protostuff.WireFormat.WIRETYPE_FIXED32;
import static io.protostuff.WireFormat.WIRETYPE_FIXED64;
import static io.protostuff.WireFormat.WIRETYPE_LENGTH_DELIMITED;
import static io.protostuff.WireFormat.WIRETYPE_VARINT;
import static io.protostuff.WireFormat.makeTag;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Protobuf serialization where the messages must be fully buffered on memory before it can be written to the socket (
 * {@link OutputStream}).
 * 
 * @author David Yu
 * @created May 18, 2010
 */
public final class ProtobufOutput extends WriteSession implements Output
{

    public static final int LITTLE_ENDIAN_32_SIZE = 4, LITTLE_ENDIAN_64_SIZE = 8;

    public ProtobufOutput(LinkedBuffer buffer)
    {
        super(buffer);
    }

    public ProtobufOutput(LinkedBuffer buffer, int nextBufferSize)
    {
        super(buffer, nextBufferSize);
    }

    /**
     * Resets this output for re-use.
     */
    @Override
    public ProtobufOutput clear()
    {
        super.clear();
        return this;
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        if (value < 0)
        {
            tail = writeTagAndRawVarInt64(
                    makeTag(fieldNumber, WIRETYPE_VARINT),
                    value,
                    this,
                    tail);
        }
        else
        {
            tail = writeTagAndRawVarInt32(
                    makeTag(fieldNumber, WIRETYPE_VARINT),
                    value,
                    this,
                    tail);
        }
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT),
                value,
                this,
                tail);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT),
                encodeZigZag32(value),
                this,
                tail);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32),
                value,
                this,
                tail);
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32),
                value,
                this,
                tail);
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT),
                value,
                this,
                tail);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT),
                value,
                this,
                tail);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT),
                encodeZigZag64(value),
                this,
                tail);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64),
                value,
                this,
                tail);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64),
                value,
                this,
                tail);
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32),
                Float.floatToRawIntBits(value),
                this,
                tail);
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64),
                Double.doubleToRawLongBits(value),
                this,
                tail);
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT),
                value ? 1 : 0,
                this,
                tail);
    }

    @Override
    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, number, repeated);
    }

    @Override
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        tail = writeUTF8VarDelimited(
                value,
                this,
                writeRawVarInt32(makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), this, tail));
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException
    {
        tail = writeTagAndByteArray(
                makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED),
                bytes, 0, bytes.length,
                this,
                tail);
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        tail = writeTagAndByteArray(
                makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED),
                value, offset, length,
                this,
                tail);
    }

    @Override
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema,
            final boolean repeated) throws IOException
    {
        final LinkedBuffer lastBuffer;

        // write the tag
        if (fieldNumber < 16 && tail.offset != tail.buffer.length)
        {
            lastBuffer = tail;
            size++;
            lastBuffer.buffer[lastBuffer.offset++] =
                    (byte) makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED);
        }
        else
        {
            tail = lastBuffer = writeRawVarInt32(
                    makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), this, tail);
        }

        final int lastOffset = tail.offset, lastSize = size;
        
        if (lastOffset == lastBuffer.buffer.length)
        {
            // not enough size for the 1-byte delimiter
            final LinkedBuffer nextBuffer = new LinkedBuffer(nextBufferSize);
            // new buffer for the content
            tail = nextBuffer;

            schema.writeTo(this, value);

            final int msgSize = size - lastSize;

            final byte[] delimited = new byte[computeRawVarint32Size(msgSize)];
            writeRawVarInt32(msgSize, delimited, 0);

            size += delimited.length;

            // wrap the byte array (delimited) and insert between the two buffers
            new LinkedBuffer(delimited, 0, delimited.length, lastBuffer).next = nextBuffer;
            return;
        }
        
        // we have enough space for the 1-byte delim
        lastBuffer.offset++;
        size++;

        schema.writeTo(this, value);

        final int msgSize = size - lastSize - 1;

        // optimize for small messages
        if (msgSize < 128)
        {
            // fits
            lastBuffer.buffer[lastOffset] = (byte) msgSize;
            return;
        }

        // split into two buffers

        // the second buffer (contains the message contents)
        final LinkedBuffer view = new LinkedBuffer(lastBuffer.buffer,
                lastOffset + 1, lastBuffer.offset);

        if (lastBuffer == tail)
            tail = view;
        else
            view.next = lastBuffer.next;

        // the first buffer (contains the tag)
        lastBuffer.offset = lastOffset;

        final byte[] delimited = new byte[computeRawVarint32Size(msgSize)];
        writeRawVarInt32(msgSize, delimited, 0);

        // add the difference
        size += (delimited.length - 1);

        // wrap the byte array (delimited) and insert between the two buffers
        new LinkedBuffer(delimited, 0, delimited.length, lastBuffer).next = view;
    }

    /*
     * Write the nested message encoded as group.
     * 
     * <T> void writeObjectEncodedAsGroup(final int fieldNumber, final T value, final Schema<T> schema, final boolean
     * repeated) throws IOException { tail = writeRawVarInt32( makeTag(fieldNumber, WIRETYPE_START_GROUP), this, tail);
     * 
     * schema.writeTo(this, value);
     * 
     * tail = writeRawVarInt32( makeTag(fieldNumber, WIRETYPE_END_GROUP), this, tail); }
     */

    /* ----------------------------------------------------------------- */

    /**
     * Returns the buffer encoded with the variable int 32.
     */
    public static LinkedBuffer writeRawVarInt32(int value, final WriteSession session,
            LinkedBuffer lb)
    {
        final int size = computeRawVarint32Size(value);

        if (lb.offset + size > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);

        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += size;
        session.size += size;

        if (size == 1)
            buffer[offset] = (byte) value;
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte) ((value & 0x7F) | 0x80);

            buffer[offset] = (byte) value;
        }

        return lb;
    }

    /**
     * Returns the buffer encoded with the tag and LinkedBuffer (zero-copy)
     */
    public static LinkedBuffer writeTagAndLinkedBuffer(int tag,
            final LinkedBuffer buffer, final WriteSession session, LinkedBuffer lb)
    {
        final int valueLen = buffer.offset - buffer.start;
        if (valueLen == 0)
        {
            // write only the tag and delimiter
            return writeTagAndRawVarInt32(tag, valueLen, session, lb);
        }

        lb = writeTagAndRawVarInt32(tag, valueLen, session, lb);
        // zero copy
        lb.next = buffer;

        final int remaining = lb.buffer.length - lb.offset;
        // if all filled up, return a fresh buffer.
        return remaining == 0 ? new LinkedBuffer(session.nextBufferSize, buffer) :
                new LinkedBuffer(lb, buffer);
    }

    /**
     * Returns the buffer encoded with the tag and byte array
     */
    public static LinkedBuffer writeTagAndByteArray(int tag, final byte[] value,
            int offset, int valueLen,
            final WriteSession session, LinkedBuffer lb)
    {
        if (valueLen == 0)
        {
            // write only the tag and delimiter
            return writeTagAndRawVarInt32(tag, valueLen, session, lb);
        }

        lb = writeTagAndRawVarInt32(tag, valueLen, session, lb);

        session.size += valueLen;

        final int available = lb.buffer.length - lb.offset;
        if (valueLen > available)
        {
            if (available + session.nextBufferSize < valueLen)
            {
                // too large ... so we wrap and insert (zero-copy)
                if (available == 0)
                {
                    // buffer was actually full ... return a fresh buffer
                    return new LinkedBuffer(session.nextBufferSize,
                            new LinkedBuffer(value, offset, offset + valueLen, lb));
                }

                // continue with the existing byte array of the previous buffer
                return new LinkedBuffer(lb,
                        new LinkedBuffer(value, offset, offset + valueLen, lb));
            }

            // copy what can fit
            System.arraycopy(value, offset, lb.buffer, lb.offset, available);

            lb.offset += available;

            // grow
            lb = new LinkedBuffer(session.nextBufferSize, lb);

            final int leftover = valueLen - available;

            // copy what's left
            System.arraycopy(value, offset + available, lb.buffer, 0, leftover);

            lb.offset += leftover;

            return lb;
        }

        // it fits
        System.arraycopy(value, offset, lb.buffer, lb.offset, valueLen);

        lb.offset += valueLen;

        return lb;
    }

    /**
     * Returns the buffer encoded with the tag and var int 32
     */
    public static LinkedBuffer writeTagAndRawVarInt32(int tag, int value,
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint32Size(value);
        final int totalSize = tagSize + size;

        if (lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);

        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;

        if (tagSize == 1)
            buffer[offset++] = (byte) tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        if (size == 1)
            buffer[offset] = (byte) value;
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte) ((value & 0x7F) | 0x80);

            buffer[offset] = (byte) value;
        }

        return lb;
    }

    /**
     * Returns the buffer encoded with the tag and var int 64
     */
    public static LinkedBuffer writeTagAndRawVarInt64(int tag, long value,
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint64Size(value);
        final int totalSize = tagSize + size;

        if (lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);

        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;

        if (tagSize == 1)
            buffer[offset++] = (byte) tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        if (size == 1)
            buffer[offset] = (byte) value;
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte) (((int) value & 0x7F) | 0x80);

            buffer[offset] = (byte) value;
        }

        return lb;
    }

    /**
     * Returns the buffer encoded with the tag and little endian 32
     */
    public static LinkedBuffer writeTagAndRawLittleEndian32(int tag, int value,
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_32_SIZE;

        if (lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);

        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;

        if (tagSize == 1)
            buffer[offset++] = (byte) tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        writeRawLittleEndian32(value, buffer, offset);

        return lb;
    }

    /**
     * Returns the buffer encoded with the tag and little endian 64
     */
    public static LinkedBuffer writeTagAndRawLittleEndian64(int tag, long value,
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_64_SIZE;

        if (lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);

        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;

        if (tagSize == 1)
            buffer[offset++] = (byte) tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        writeRawLittleEndian64(value, buffer, offset);

        return lb;
    }

    /** Encode and write a varint to the byte array */
    public static void writeRawVarInt32(int value, final byte[] buf, int offset) throws IOException
    {
        while (true)
        {
            if ((value & ~0x7F) == 0)
            {
                buf[offset] = (byte) value;
                return;
            }
            else
            {
                buf[offset++] = (byte) ((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * Encode and write a varint to the {@link OutputStream}
     */
    public static void writeRawVarInt32Bytes(OutputStream out, int value) throws IOException
    {
        while (true)
        {
            if ((value & ~0x7F) == 0)
            {
                out.write(value);
                return;
            }
            else
            {
                out.write((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * Encode and write a varint to the {@link DataOutput}
     */
    public static void writeRawVarInt32Bytes(DataOutput out, int value) throws IOException
    {
        while (true)
        {
            if ((value & ~0x7F) == 0)
            {
                out.write(value);
                return;
            }
            else
            {
                out.write((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * Returns a byte array encoded with the tag and var int 32
     */
    public static byte[] getTagAndRawVarInt32Bytes(int tag, int value)
    {
        int tagSize = computeRawVarint32Size(tag);
        int size = computeRawVarint32Size(value);
        int offset = 0;
        byte[] buffer = new byte[tagSize + size];
        if (tagSize == 1)
            buffer[offset++] = (byte) tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        if (size == 1)
            buffer[offset] = (byte) value;
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte) ((value & 0x7F) | 0x80);

            buffer[offset] = (byte) value;
        }

        return buffer;
    }

    /**
     * Returns a byte array encoded with the tag and var int 64
     */
    public static byte[] getTagAndRawVarInt64Bytes(int tag, long value)
    {
        int tagSize = computeRawVarint32Size(tag);
        int size = computeRawVarint64Size(value);
        int offset = 0;
        byte[] buffer = new byte[tagSize + size];

        if (tagSize == 1)
        {
            buffer[offset++] = (byte) tag;
        }
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        if (size == 1)
        {
            buffer[offset] = (byte) value;
        }
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte) (((int) value & 0x7F) | 0x80);

            buffer[offset] = (byte) value;
        }

        return buffer;
    }

    /**
     * Returns a byte array encoded with the tag and little endian 32
     */
    public static byte[] getTagAndRawLittleEndian32Bytes(int tag, int value)
    {
        int tagSize = computeRawVarint32Size(tag);
        int offset = 0;
        byte[] buffer = new byte[tagSize + LITTLE_ENDIAN_32_SIZE];

        if (tagSize == 1)
        {
            buffer[offset++] = (byte) tag;
        }
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        writeRawLittleEndian32(value, buffer, offset);

        return buffer;
    }

    /**
     * Returns a byte array encoded with the tag and little endian 64
     */
    public static byte[] getTagAndRawLittleEndian64Bytes(int tag, long value)
    {
        int tagSize = computeRawVarint32Size(tag);
        int offset = 0;
        byte[] buffer = new byte[tagSize + LITTLE_ENDIAN_64_SIZE];

        if (tagSize == 1)
        {
            buffer[offset++] = (byte) tag;
        }
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte) ((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte) tag;
        }

        writeRawLittleEndian64(value, buffer, offset);

        return buffer;
    }

    /**
     * Writes the encoded little endian 32 and returns the bytes written
     */
    public static int writeRawLittleEndian32(int value, byte[] buffer, int offset)
    {
        if (buffer.length - offset < LITTLE_ENDIAN_32_SIZE)
            throw new IllegalArgumentException("buffer capacity not enough.");

        buffer[offset++] = (byte) (value & 0xFF);
        buffer[offset++] = (byte) (value >> 8 & 0xFF);
        buffer[offset++] = (byte) (value >> 16 & 0xFF);
        buffer[offset] = (byte) (value >> 24 & 0xFF);

        return LITTLE_ENDIAN_32_SIZE;
    }

    /**
     * Writes the encoded little endian 64 and returns the bytes written
     */
    public static int writeRawLittleEndian64(long value, byte[] buffer, int offset)
    {
        if (buffer.length - offset < LITTLE_ENDIAN_64_SIZE)
            throw new IllegalArgumentException("buffer capacity not enough.");

        buffer[offset++] = (byte) (value & 0xFF);
        buffer[offset++] = (byte) (value >> 8 & 0xFF);
        buffer[offset++] = (byte) (value >> 16 & 0xFF);
        buffer[offset++] = (byte) (value >> 24 & 0xFF);
        buffer[offset++] = (byte) (value >> 32 & 0xFF);
        buffer[offset++] = (byte) (value >> 40 & 0xFF);
        buffer[offset++] = (byte) (value >> 48 & 0xFF);
        buffer[offset] = (byte) (value >> 56 & 0xFF);

        return LITTLE_ENDIAN_64_SIZE;
    }

    /**
     * Returns the byte array computed from the var int 32 size
     */
    public static byte[] getRawVarInt32Bytes(int value)
    {
        int size = computeRawVarint32Size(value);
        if (size == 1)
            return new byte[] { (byte) value };

        int offset = 0;
        byte[] buffer = new byte[size];
        for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
            buffer[offset++] = (byte) ((value & 0x7F) | 0x80);

        buffer[offset] = (byte) value;
        return buffer;
    }

    /* METHODS FROM CodedOutput */

    // Protocol Buffers - Google's data interchange format
    // Copyright 2008 Google Inc. All rights reserved.
    // http://code.google.com/p/protobuf/
    //
    // Redistribution and use in source and binary forms, with or without
    // modification, are permitted provided that the following conditions are
    // met:
    //
    // * Redistributions of source code must retain the above copyright
    // notice, this list of conditions and the following disclaimer.
    // * Redistributions in binary form must reproduce the above
    // copyright notice, this list of conditions and the following disclaimer
    // in the documentation and/or other materials provided with the
    // distribution.
    // * Neither the name of Google Inc. nor the names of its
    // contributors may be used to endorse or promote products derived from
    // this software without specific prior written permission.
    //
    // THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    // "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    // LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
    // A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
    // OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
    // SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
    // LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    // DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    // THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    // (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    // OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

    /**
     * Compute the number of bytes that would be needed to encode a varint. {@code value} is treated as unsigned, so it
     * won't be sign-extended if negative.
     */
    public static int computeRawVarint32Size(final int value)
    {
        if ((value & (0xffffffff << 7)) == 0)
            return 1;
        if ((value & (0xffffffff << 14)) == 0)
            return 2;
        if ((value & (0xffffffff << 21)) == 0)
            return 3;
        if ((value & (0xffffffff << 28)) == 0)
            return 4;
        return 5;
    }

    /**
     * Compute the number of bytes that would be needed to encode a varint.
     */
    public static int computeRawVarint64Size(final long value)
    {
        if ((value & (0xffffffffffffffffL << 7)) == 0)
            return 1;
        if ((value & (0xffffffffffffffffL << 14)) == 0)
            return 2;
        if ((value & (0xffffffffffffffffL << 21)) == 0)
            return 3;
        if ((value & (0xffffffffffffffffL << 28)) == 0)
            return 4;
        if ((value & (0xffffffffffffffffL << 35)) == 0)
            return 5;
        if ((value & (0xffffffffffffffffL << 42)) == 0)
            return 6;
        if ((value & (0xffffffffffffffffL << 49)) == 0)
            return 7;
        if ((value & (0xffffffffffffffffL << 56)) == 0)
            return 8;
        if ((value & (0xffffffffffffffffL << 63)) == 0)
            return 9;
        return 10;
    }

    /**
     * Encode a ZigZag-encoded 32-bit value. ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint. (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     * 
     * @param n
     *            A signed 32-bit integer.
     * @return An unsigned 32-bit integer, stored in a signed int because Java has no explicit unsigned support.
     */
    public static int encodeZigZag32(final int n)
    {
        // Note: the right-shift must be arithmetic
        return (n << 1) ^ (n >> 31);
    }

    /**
     * Encode a ZigZag-encoded 64-bit value. ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint. (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     * 
     * @param n
     *            A signed 64-bit integer.
     * @return An unsigned 64-bit integer, stored in a signed int because Java has no explicit unsigned support.
     */
    public static long encodeZigZag64(final long n)
    {
        // Note: the right-shift must be arithmetic
        return (n << 1) ^ (n >> 63);
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
