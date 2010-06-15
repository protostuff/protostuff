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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Maintains a decent-sized byte buffer for writing.  If the delimited field's byte-array-value 
 * is too large, it is wrapped by another buffer and linked together.
 *
 * @author David Yu
 * @created May 18, 2010
 */
public final class BufferedOutput implements Output
{

    public static final int DEFAULT_BUFFER_SIZE = Integer.getInteger(
            "bufferedoutput.default_buffer_size", 256);
    
    public static final int ARRAY_COPY_SIZE_LIMIT = Integer.getInteger(
            "bufferedoutput.array_copy_size_limit", 127);
    
    private final OutputBuffer root;
    private OutputBuffer current;
    private final int nextBufferSize;
    private int size = 0;
    private final boolean encodeNestedMessageAsGroup;
    
    public BufferedOutput()
    {
        this(DEFAULT_BUFFER_SIZE);
    }
    
    public BufferedOutput(int bufferSize)
    {
        this(new OutputBuffer(bufferSize), bufferSize, false);
    }
    
    public BufferedOutput(int bufferSize, boolean encodeNestedMessageAsGroup)
    {
        this(new OutputBuffer(bufferSize), bufferSize, encodeNestedMessageAsGroup);
    }
    
    public BufferedOutput(OutputBuffer root, int nextBufferSize, 
            boolean encodeNestedMessageAsGroup)
    {
        current = root;
        this.root = root;
        this.nextBufferSize = nextBufferSize;
        this.encodeNestedMessageAsGroup = encodeNestedMessageAsGroup;
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
    public BufferedOutput reset()
    {
        // dereference for gc
        root.next = null;
        // reuse the byte array, offset reset to 0
        root.offset = 0;
        size = 0;
        current = root;
        return this;
    }
    
    /**
     * Writes the raw bytes into the {@link OutputStream}.
     */
    public void streamTo(OutputStream out) throws IOException
    {
        for(OutputBuffer node = root; node != null; node = node.next)
        {
            int len = node.offset - node.start;
            if(len > 0)
                out.write(node.buffer, node.start, len);
        }
    }
    
    /**
     * Returns the data written to this output as a single byte array.
     */
    public byte[] toByteArray()
    {
        int start = 0;
        byte[] buffer = new byte[size];        
        for(OutputBuffer node = root; node != null; node = node.next)
        {
            int len = node.offset - node.start;
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
            writeTagAndRawVarInt64Bytes(
                    WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                    value, 
                    current);
        }
        else
        {
            writeTagAndRawVarInt32Bytes(
                    WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                    value, 
                    current);
        }
    }
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                encodeZigZag32(value), 
                current);
    }
    
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                current);
    }
    
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                current);
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt64Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt64Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt64Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                current);
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian64Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                current);
    }
    
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian64Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                current);
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED32), 
                Float.floatToRawIntBits(value), 
                current);
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        writeTagAndRawLittleEndian64Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED64), 
                Double.doubleToRawLongBits(value), 
                current);
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value ? 1 : 0, 
                current);
    }

    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        writeTagAndRawVarInt32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                number, 
                current);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        writeTagAndByteArray(
                WireFormat.makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
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
                WireFormat.makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
                bytes, 
                current);
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value, 
            boolean repeated) throws IOException
    {
        writeObject(fieldNumber, value, value.cachedSchema(), repeated);
    }
    
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, 
            boolean repeated) throws IOException
    {
        if(encodeNestedMessageAsGroup)
        {
            writeObjectEncodedAsGroup(fieldNumber, value, schema, repeated);
            return;
        }
        
        final OutputBuffer lastBuffer = current;
        final int lastSize = size;
        // view
        lastBuffer.next = current = new OutputBuffer(lastBuffer);
        
        schema.writeTo(this, value);
        
        final byte[] delimited = getTagAndRawVarInt32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
                size - lastSize);
        
        size += delimited.length;
        
        // the first tag of the inner message
        final OutputBuffer inner = lastBuffer.next;
        
        // wrap the byte array (delimited) and insert
        new OutputBuffer(delimited, 0, delimited.length, lastBuffer).next = inner;
    }
    
    /**
     * Write the nested message encoded as group.
     */
    <T> void writeObjectEncodedAsGroup(int fieldNumber, T value, Schema<T> schema, 
            boolean repeated) throws IOException
    {
        writeRawVarInt32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_START_GROUP), 
                current);
        
        schema.writeTo(this, value);
        
        writeRawVarInt32Bytes(
                WireFormat.makeTag(fieldNumber, WIRETYPE_END_GROUP), 
                current);
    }
    
    /* ----------------------------------------------------------------- */
    
    private void writeRawVarInt32Bytes(int value, OutputBuffer ob)
    {
        final int size = computeRawVarint32Size(value);

        if(ob.offset + size > ob.buffer.length)
            current = ob = new OutputBuffer(new byte[nextBufferSize], ob);
        
        final byte[] buffer = ob.buffer;
        int offset = ob.offset;
        ob.offset += size;
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
    private void writeTagAndByteArray(int tag, byte[] value, OutputBuffer ob)
    {
        final int valueLen = value.length;
        final OutputBuffer rb = writeTagAndRawVarInt32Bytes(tag, valueLen, ob);

        this.size += valueLen;
        
        if(valueLen > ARRAY_COPY_SIZE_LIMIT || rb.offset + valueLen > rb.buffer.length)
        {
            // huge string/byte array.
            OutputBuffer wrap = new OutputBuffer(value, rb);
            wrap.offset = valueLen;
            
            // view
            current = wrap.next = new OutputBuffer(rb);
            return;
        }

        System.arraycopy(value, 0, rb.buffer, rb.offset, valueLen);
        
        rb.offset += valueLen;
    }

    /** Returns the output buffer encoded with the tag and var int 32 */
    private OutputBuffer writeTagAndRawVarInt32Bytes(int tag, int value, OutputBuffer ob)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint32Size(value);
        final int totalSize = tagSize + size;

        if(ob.offset + totalSize > ob.buffer.length)
            current = ob = new OutputBuffer(new byte[nextBufferSize], ob);
        
        final byte[] buffer = ob.buffer;
        int offset = ob.offset;
        ob.offset += totalSize;
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
        
        return ob;
    }

    /** Returns the output buffer encoded with the tag and var int 64 */
    private void writeTagAndRawVarInt64Bytes(int tag, long value, OutputBuffer ob)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint64Size(value);
        final int totalSize = tagSize + size;
        
        if(ob.offset + totalSize > ob.buffer.length)
            current = ob = new OutputBuffer(new byte[nextBufferSize], ob);
        
        final byte[] buffer = ob.buffer;
        int offset = ob.offset;
        ob.offset += totalSize;
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
    private void writeTagAndRawLittleEndian32Bytes(int tag, int value, OutputBuffer ob)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_32_SIZE;
        
        if(ob.offset + totalSize > ob.buffer.length)
            current = ob = new OutputBuffer(new byte[nextBufferSize], ob);
        
        final byte[] buffer = ob.buffer;
        int offset = ob.offset;
        ob.offset += totalSize;
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
    private void writeTagAndRawLittleEndian64Bytes(int tag, long value, OutputBuffer ob)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_64_SIZE;

        if(ob.offset + totalSize > ob.buffer.length)
            current = ob = new OutputBuffer(new byte[nextBufferSize], ob);
        
        final byte[] buffer = ob.buffer;
        int offset = ob.offset;
        ob.offset += totalSize;
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
