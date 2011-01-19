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

package com.dyuproject.protostuff;

import static com.dyuproject.protostuff.StringSerializer.STRING;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A protobuf output where the writing of bytes is deferred (buffered chunks).
 * 
 * Once the message serialization is done, data can then be streamed to 
 * an {@link OutputStream} or a {@link ByteBuffer}.
 *
 * @author David Yu
 * @created Nov 9, 2009
 */
public final class DeferredOutput implements Output
{

    private final ByteArrayNode root = new ByteArrayNode(null);
    private ByteArrayNode current = root;
    private int size = 0;
    private final boolean encodeNestedMessageAsGroup;

    public DeferredOutput()
    {
        this(false);
    }
    
    public DeferredOutput(boolean encodeNestedMessageAsGroup)
    {
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
     * Resets this output.
     */
    public DeferredOutput reset()
    {
        root.next = null;
        current = root;
        size = 0;
        return this;
    }
    
    /**
     * Writes the raw bytes into the {@link OutputStream}.
     */
    public void streamTo(OutputStream out) throws IOException
    {
        for(ByteArrayNode node = root.next; node != null; node = node.next)
            out.write(node.bytes);
    }
    
    /**
     * Returns the data written to this output as a single byte array.
     */
    public byte[] toByteArray()
    {
        int start = 0;
        byte[] buffer = new byte[size];        
        for(ByteArrayNode node = root.next; node != null; node = node.next)
        {
            byte[] bytes = node.bytes;
            System.arraycopy(bytes, 0, buffer, start, bytes.length);
            start += bytes.length;
        }
        return buffer;
    }

    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = value<0 ? CodedOutput.getTagAndRawVarInt64Bytes(tag, value) : 
            CodedOutput.getTagAndRawVarInt32Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = CodedOutput.getTagAndRawVarInt32Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = CodedOutput.getTagAndRawVarInt32Bytes(tag, CodedOutput.encodeZigZag32(value));
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
        byte[] bytes = CodedOutput.getTagAndRawLittleEndian32Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
        byte[] bytes = CodedOutput.getTagAndRawLittleEndian32Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = CodedOutput.getTagAndRawVarInt64Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = CodedOutput.getTagAndRawVarInt64Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = CodedOutput.getTagAndRawVarInt64Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
        byte[] bytes = CodedOutput.getTagAndRawLittleEndian64Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }
    
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
        byte[] bytes = CodedOutput.getTagAndRawLittleEndian64Bytes(tag, value);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
        byte[] bytes = CodedOutput.getTagAndRawLittleEndian32Bytes(tag, Float.floatToRawIntBits(value));
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
        byte[] bytes = CodedOutput.getTagAndRawLittleEndian64Bytes(tag, Double.doubleToRawLongBits(value));
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = CodedOutput.getTagAndRawVarInt32Bytes(tag, value ? 1 : 0);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }

    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        byte[] bytes = CodedOutput.getTagAndRawVarInt32Bytes(tag, number);
        size += bytes.length;
        current =  new ByteArrayNode(bytes, current);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        byte[] bytes = STRING.ser(value);
        
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        byte[] delimited = CodedOutput.getTagAndRawVarInt32Bytes(tag, bytes.length);
        size += delimited.length + bytes.length;
        
        current = new ByteArrayNode(bytes, new ByteArrayNode(delimited, current));
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }
    
    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        byte[] delimited = CodedOutput.getTagAndRawVarInt32Bytes(tag, bytes.length);
        size += delimited.length + bytes.length;
        
        current = new ByteArrayNode(bytes, new ByteArrayNode(delimited, current));
    }
    
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, 
            int offset, int length, boolean repeated) throws IOException
    {
        final byte[] bytes = new byte[length];
        System.arraycopy(value, offset, bytes, 0, length);
        writeByteArray(fieldNumber, bytes, repeated);
    }
    
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, 
            boolean repeated) throws IOException
    {
        if(encodeNestedMessageAsGroup)
        {
            writeObjectEncodedAsGroup(fieldNumber, value, schema, repeated);
            return;
        }
        
        ByteArrayNode lastCurrent = current;
        int lastSize = size;
        
        schema.writeTo(this, value);
        
        int msgSize = size - lastSize;
        
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        byte[] delimited = CodedOutput.getTagAndRawVarInt32Bytes(tag, msgSize);
        
        size += delimited.length;
        
        // the first tag of the message
        ByteArrayNode node = lastCurrent.next;
        
        // insert the byte array (message size)
        new ByteArrayNode(delimited, lastCurrent).next = node;
        
    }
    
    /**
     * Write the nested message encoded as group.
     */
    <T> void writeObjectEncodedAsGroup(int fieldNumber, T value, Schema<T> schema, 
            boolean repeated) throws IOException
    {
        int startTag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_START_GROUP);
        byte[] startBytes = getTagBytes(startTag);
        current = new ByteArrayNode(startBytes, current);
        
        schema.writeTo(this, value);
        
        int endTag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_END_GROUP);
        byte[] endBytes = getTagBytes(endTag);
        current = new ByteArrayNode(endBytes, current);
        
        size += (startBytes.length + endBytes.length);
    }
    
    static byte[] getTagBytes(int tag)
    {
        int tagSize = CodedOutput.computeRawVarint32Size(tag);
        if (tagSize == 1)
            return new byte[]{(byte)tag};

        byte[] buffer = new byte[tagSize];
        int offset = 0;
        for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
            buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

        buffer[offset++] = (byte)tag;
        return buffer;
    }

}
