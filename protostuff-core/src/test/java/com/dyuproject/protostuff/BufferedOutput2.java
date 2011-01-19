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

package com.dyuproject.protostuff;

import static com.dyuproject.protostuff.CodedOutput.LITTLE_ENDIAN_32_SIZE;
import static com.dyuproject.protostuff.CodedOutput.LITTLE_ENDIAN_64_SIZE;
import static com.dyuproject.protostuff.CodedOutput.computeRawVarint32Size;
import static com.dyuproject.protostuff.CodedOutput.computeRawVarint64Size;
import static com.dyuproject.protostuff.CodedOutput.encodeZigZag32;
import static com.dyuproject.protostuff.CodedOutput.getTagAndRawVarInt32Bytes;
import static com.dyuproject.protostuff.CodedOutput.writeRawLittleEndian32;
import static com.dyuproject.protostuff.CodedOutput.writeRawLittleEndian64;
import static com.dyuproject.protostuff.StringSerializer.STRING;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_END_GROUP;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_FIXED32;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_FIXED64;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_LENGTH_DELIMITED;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_START_GROUP;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_VARINT;
import static com.dyuproject.protostuff.WireFormat.makeTag;

import java.io.IOException;

/**
 * The previous version of {@link BufferedOutput}.
 * This exists for comparison purposes.
 *
 * @author David Yu
 * @created May 18, 2010
 */
public final class BufferedOutput2 implements Output
{

    public static final int DEFAULT_BUFFER_SIZE = Integer.getInteger(
            "BufferedOutput2.default_buffer_size", 512);
    
    public static final int ARRAY_COPY_SIZE_LIMIT = Integer.getInteger(
            "protostuff.array_copy_size_limit", 255);
    
    private final LinkedBuffer root;
    private LinkedBuffer current;
    private final int nextBufferSize, arrayCopySizeLimit;
    private int size = 0;
    private final boolean encodeNestedMessageAsGroup;
    
    public BufferedOutput2(LinkedBuffer buffer, boolean encodeNestedMessageAsGroup)
    {
        this(buffer, DEFAULT_BUFFER_SIZE, ARRAY_COPY_SIZE_LIMIT, encodeNestedMessageAsGroup);
    }
    
    public BufferedOutput2(LinkedBuffer root, int nextBufferSize, int arrayCopySizeLimit, 
            boolean encodeNestedMessageAsGroup)
    {
        current = root;
        this.root = root;
        this.nextBufferSize = nextBufferSize;
        this.arrayCopySizeLimit = arrayCopySizeLimit;
        this.encodeNestedMessageAsGroup = encodeNestedMessageAsGroup;
    }
    
    /**
     * Gets the buffer used by this output.
     */
    public LinkedBuffer getBuffer()
    {
        return root;
    }
    
    /**
     * Gets the current size of this output.
     */
    public int getSize()
    {
        return size;
    }
    
    /**
     * Resets this output for re-use.
     */
    public BufferedOutput2 reset()
    {
        current = root.clear();
        size = 0;
        return this;
    }
    
    /**
     * Returns the data written to this output as a single byte array.
     */
    public byte[] toByteArray()
    {
        int start = 0;
        final byte[] buffer = new byte[size];        
        for(LinkedBuffer node = root; node != null; node = node.next)
        {
            final int len = node.offset - node.start;
            if(len > 0)
            {
                System.arraycopy(node.buffer, node.start, buffer, start, len);
                start += len;
            }
        }
        return buffer;
    }
    
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        if(value < 0)
        {
            writeTagAndRawVarInt64(
                    makeTag(fieldNumber, WIRETYPE_VARINT), 
                    value, 
                    current);
        }
        else
        {
            writeTagAndRawVarInt32(
                    makeTag(fieldNumber, WIRETYPE_VARINT), 
                    value, 
                    current);
        }
    }
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                encodeZigZag32(value), 
                current);
    }
    
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                current);
    }
    
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                current);
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                current);
    }
    
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                current);
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32), 
                Float.floatToRawIntBits(value), 
                current);
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64), 
                Double.doubleToRawLongBits(value), 
                current);
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value ? 1 : 0, 
                current);
    }

    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, number, repeated);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        writeTagAndByteArray(
                makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
                STRING.ser(value), 
                current);
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }
    
    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException
    {
        writeTagAndByteArray(
                makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
                bytes, 
                current);
    }
    
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, 
            int offset, int length, boolean repeated) throws IOException
    {
        final byte[] bytes = new byte[length];
        System.arraycopy(value, offset, bytes, 0, length);
        writeByteArray(fieldNumber, bytes, repeated);
    }
    
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema, 
            final boolean repeated) throws IOException
    {
        if(encodeNestedMessageAsGroup)
        {
            writeObjectEncodedAsGroup(fieldNumber, value, schema, repeated);
            return;
        }
        
        final LinkedBuffer lastBuffer = current;
        final int lastSize = size;
        // view
        current = new LinkedBuffer(lastBuffer, lastBuffer);
        
        schema.writeTo(this, value);
        
        final byte[] delimited = getTagAndRawVarInt32Bytes(
                makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
                size - lastSize);
        
        size += delimited.length;
        
        // the first tag of the inner message
        final LinkedBuffer inner = lastBuffer.next;
        
        // wrap the byte array (delimited) and insert
        new LinkedBuffer(delimited, 0, delimited.length, lastBuffer).next = inner;
    }
    
    /**
     * Write the nested message encoded as group.
     */
    <T> void writeObjectEncodedAsGroup(final int fieldNumber, final T value, 
            final Schema<T> schema, final boolean repeated) throws IOException
    {
        writeRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_START_GROUP), 
                current);
        
        schema.writeTo(this, value);
        
        writeRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_END_GROUP), 
                current);
    }
    
    /* ----------------------------------------------------------------- */
    
    private void writeRawVarInt32(int value, LinkedBuffer lb)
    {
        final int size = computeRawVarint32Size(value);

        if(lb.offset + size > lb.buffer.length)
            current = lb = new LinkedBuffer(new byte[nextBufferSize], 0, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += size;
        this.size += size;
        
        if (size == 1)
            buffer[offset] = (byte)value;
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte)((value & 0x7F) | 0x80);

            buffer[offset] = (byte)value;
        }
    }
    
    /** Returns the output buffer encoded with the tag and byte array */
    private void writeTagAndByteArray(int tag, byte[] value, LinkedBuffer lb)
    {
        final int valueLen = value.length;
        lb = writeTagAndRawVarInt32(tag, valueLen, lb);

        this.size += valueLen;
        
        if(valueLen > arrayCopySizeLimit || lb.offset + valueLen > lb.buffer.length)
        {
            // huge string/byte array.
            // wrap, insert and create a view (e.g zero copy)
            current = new LinkedBuffer(lb, new LinkedBuffer(value, 0, valueLen, lb));
            return;
        }

        System.arraycopy(value, 0, lb.buffer, lb.offset, valueLen);
        
        lb.offset += valueLen;
    }

    /** Returns the output buffer encoded with the tag and var int 32 */
    private LinkedBuffer writeTagAndRawVarInt32(int tag, int value, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint32Size(value);
        final int totalSize = tagSize + size;

        if(lb.offset + totalSize > lb.buffer.length)
            current = lb = new LinkedBuffer(new byte[nextBufferSize], 0, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        this.size += totalSize;
        
        if (tagSize == 1)
            buffer[offset++] = (byte)tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        if (size == 1)
            buffer[offset] = (byte)value;
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte)((value & 0x7F) | 0x80);

            buffer[offset] = (byte)value;
        }
        
        return lb;
    }

    /** Returns the output buffer encoded with the tag and var int 64 */
    private void writeTagAndRawVarInt64(int tag, long value, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint64Size(value);
        final int totalSize = tagSize + size;
        
        if(lb.offset + totalSize > lb.buffer.length)
            current = lb = new LinkedBuffer(new byte[nextBufferSize], 0, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        this.size += totalSize;
        
        if (tagSize == 1)
            buffer[offset++] = (byte)tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        if (size == 1)
            buffer[offset] = (byte)value;
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte)(((int)value & 0x7F) | 0x80);

            buffer[offset] = (byte)value;
        }
    }
    

    /** Returns the output buffer encoded with the tag and little endian 32 */
    private void writeTagAndRawLittleEndian32(int tag, int value, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_32_SIZE;
        
        if(lb.offset + totalSize > lb.buffer.length)
            current = lb = new LinkedBuffer(new byte[nextBufferSize], 0, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        this.size += totalSize;
        
        if (tagSize == 1)
            buffer[offset++] = (byte)tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        writeRawLittleEndian32(value, buffer, offset);
    }

    /** Returns the output buffer encoded with the tag and little endian 64 */
    private void writeTagAndRawLittleEndian64(int tag, long value, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_64_SIZE;

        if(lb.offset + totalSize > lb.buffer.length)
            current = lb = new LinkedBuffer(new byte[nextBufferSize], 0, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        this.size += totalSize;

        if (tagSize == 1)
            buffer[offset++] = (byte)tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        writeRawLittleEndian64(value, buffer, offset);
    }

}
