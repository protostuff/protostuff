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
import static com.dyuproject.protostuff.StringSerializer.writeUTF8VarDelimited;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_END_GROUP;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_FIXED32;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_FIXED64;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_LENGTH_DELIMITED;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_START_GROUP;
import static com.dyuproject.protostuff.WireFormat.WIRETYPE_VARINT;
import static com.dyuproject.protostuff.WireFormat.makeTag;

import java.io.IOException;

import com.dyuproject.protostuff.LinkedBuffer.WriteSession;

/**
 * Maintains a decent-sized byte buffer for writing.  If the delimited field's byte-array-value 
 * is too large, it is wrapped by another buffer and linked together (basically zero copy).
 *
 * @author David Yu
 * @created May 18, 2010
 */
public final class BufferedOutput extends WriteSession implements Output
{
    private final boolean encodeNestedMessageAsGroup;
    
    public BufferedOutput(LinkedBuffer buffer, boolean encodeNestedMessageAsGroup)
    {
        super(buffer);
        this.encodeNestedMessageAsGroup = encodeNestedMessageAsGroup;
    }
    
    public BufferedOutput(LinkedBuffer buffer, int nextBufferSize, 
            boolean encodeNestedMessageAsGroup)
    {
        super(buffer, nextBufferSize);
        this.encodeNestedMessageAsGroup = encodeNestedMessageAsGroup;
    }
    
    /**
     * Resets this output for re-use.
     */
    public BufferedOutput clear()
    {
        super.clear();
        return this;
    }
    
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        if(value < 0)
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
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                this, 
                tail);
    }
    
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                encodeZigZag32(value), 
                this, 
                tail);
    }
    
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                this, 
                tail);
    }
    
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                this, 
                tail);
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                this, 
                tail);
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                this, 
                tail);
    }
    
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt64(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                this, 
                tail);
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                this, 
                tail);
    }
    
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                this, 
                tail);
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian32(
                makeTag(fieldNumber, WIRETYPE_FIXED32), 
                Float.floatToRawIntBits(value), 
                this, 
                tail);
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawLittleEndian64(
                makeTag(fieldNumber, WIRETYPE_FIXED64), 
                Double.doubleToRawLongBits(value), 
                this, 
                tail);
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        tail = writeTagAndRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_VARINT), 
                value ? 1 : 0, 
                this, 
                tail);
    }

    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, number, repeated);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        tail = writeUTF8VarDelimited(
                value,
                this, 
                writeRawVarInt32(makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), this, tail));
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }
    
    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException
    {
        tail = writeTagAndByteArray(
                makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
                bytes, 
                this, 
                tail);
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value, 
            boolean repeated) throws IOException
    {
        writeObject(fieldNumber, value, value.cachedSchema(), repeated);
    }
    
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema, 
            final boolean repeated) throws IOException
    {
        if(encodeNestedMessageAsGroup)
        {
            writeObjectEncodedAsGroup(fieldNumber, value, schema, repeated);
            return;
        }
        
        final LinkedBuffer lastBuffer = tail;
        final int lastSize = size;
        // view
        tail = new LinkedBuffer(lastBuffer, lastBuffer);
        
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
        tail = writeRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_START_GROUP), 
                this, 
                tail);
        
        schema.writeTo(this, value);
        
        tail = writeRawVarInt32(
                makeTag(fieldNumber, WIRETYPE_END_GROUP), 
                this, 
                tail);
    }
    
    /* ----------------------------------------------------------------- */
    
    /**
     * Returns the buffer encoded with the variable int 32.
     */
    public static LinkedBuffer writeRawVarInt32(int value, final WriteSession session, 
            LinkedBuffer lb)
    {
        final int size = computeRawVarint32Size(value);

        if(lb.offset + size > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += size;
        session.size += size;
        
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
    
    /** Returns the buffer encoded with the tag and byte array */
    public static LinkedBuffer writeTagAndByteArray(int tag, final byte[] value, 
            final WriteSession session, LinkedBuffer lb)
    {
        final int valueLen = value.length;
        lb = writeTagAndRawVarInt32(tag, valueLen, session, lb);

        session.size += valueLen;
        
        if(lb.offset + valueLen > lb.buffer.length)
        {
            final int remaining = lb.buffer.length - lb.offset;
            if(remaining + session.nextBufferSize < valueLen)
            {
                // too large ... so we wrap and insert (zero-copy)
                if(remaining == 0)
                {
                    // buffer was actually full ... return a fresh buffer 
                    return new LinkedBuffer(session.nextBufferSize, 
                            new LinkedBuffer(value, 0, valueLen, lb));
                }
                
                // continue with the existing byte array of the previous buffer
                return new LinkedBuffer(lb, new LinkedBuffer(value, 0, valueLen, lb));
            }
            
            // copy what can fit
            System.arraycopy(value, 0, lb.buffer, lb.offset, remaining);
            
            lb.offset += remaining;
            
            // grow
            lb = new LinkedBuffer(session.nextBufferSize, lb);
            
            final int leftover = value.length - remaining;
            
            // copy what's left
            System.arraycopy(value, remaining, lb.buffer, 0, leftover);
            
            lb.offset += leftover;
            
            return lb;
        }

        // it fits
        System.arraycopy(value, 0, lb.buffer, lb.offset, valueLen);
        
        lb.offset += valueLen;
        
        return lb;
    }

    /** Returns the buffer encoded with the tag and var int 32 */
    public static LinkedBuffer writeTagAndRawVarInt32(int tag, int value, 
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint32Size(value);
        final int totalSize = tagSize + size;

        if(lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;
        
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

    /** Returns the buffer encoded with the tag and var int 64 */
    public static LinkedBuffer writeTagAndRawVarInt64(int tag, long value, 
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int size = computeRawVarint64Size(value);
        final int totalSize = tagSize + size;
        
        if(lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;
        
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
        
        return lb;
    }
    

    /** Returns the buffer encoded with the tag and little endian 32 */
    public static LinkedBuffer writeTagAndRawLittleEndian32(int tag, int value, 
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_32_SIZE;
        
        if(lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;
        
        if (tagSize == 1)
            buffer[offset++] = (byte)tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        writeRawLittleEndian32(value, buffer, offset);
        
        return lb;
    }

    /** Returns the buffer encoded with the tag and little endian 64 */
    public static LinkedBuffer writeTagAndRawLittleEndian64(int tag, long value, 
            final WriteSession session, LinkedBuffer lb)
    {
        final int tagSize = computeRawVarint32Size(tag);
        final int totalSize = tagSize + LITTLE_ENDIAN_64_SIZE;

        if(lb.offset + totalSize > lb.buffer.length)
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        lb.offset += totalSize;
        session.size += totalSize;

        if (tagSize == 1)
            buffer[offset++] = (byte)tag;
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        writeRawLittleEndian64(value, buffer, offset);
        
        return lb;
    }

}
