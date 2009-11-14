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

import java.io.IOException;

/**
 * Computes the size of the data to be written.
 * 
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
        Schema<T> schema = message.cachedSchema();
        ComputedSizeOutput sizeCount = new ComputedSizeOutput();
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

    private ByteArrayNode root, current;
    private boolean recomputed;
    
    public ComputedSizeOutput()
    {
        
    }
    
    private int size = 0;
    
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
    ComputedSizeOutput reset(boolean recomputed)
    {
        this.recomputed = recomputed;
        size = 0;
        return this;
    }
    
    ByteArrayNode getRoot()
    {
        return root;
    }

    public void writeInt32(int fieldNumber, int value) throws IOException
    {
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT));
        s += value<0 ? 10 : CodedOutput.computeRawVarint32Size(value);
        size += s;
    }
    
    public void writeUInt32(int fieldNumber, int value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(value);
    }
    
    public void writeSInt32(int fieldNumber, int value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(
                        CodedOutput.encodeZigZag32(value));
    }
    
    public void writeFixed32(int fieldNumber, int value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
    }
    
    public void writeSFixed32(int fieldNumber, int value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
    }

    public void writeInt64(int fieldNumber, long value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(value);
    }
    
    public void writeUInt64(int fieldNumber, long value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(value);
    }
    
    public void writeSInt64(int fieldNumber, long value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(
                        CodedOutput.encodeZigZag64(value));
    }
    
    public void writeFixed64(int fieldNumber, long value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
    }
    
    public void writeSFixed64(int fieldNumber, long value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
    }

    public void writeFloat(int fieldNumber, float value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
    }

    public void writeDouble(int fieldNumber, double value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
    }

    public void writeBool(int fieldNumber, boolean value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + 1;
    }

    public void writeEnum(int fieldNumber, int value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(value);
    }

    public void writeString(int fieldNumber, String value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        byte[] bytes = value.getBytes(ByteString.UTF8);
        size += CodedOutput.computeRawVarint32Size(bytes.length) + bytes.length;
    }

    public void writeBytes(int fieldNumber, ByteString value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        byte[] bytes = value.getBytes();
        size += CodedOutput.computeRawVarint32Size(bytes.length) + bytes.length;
    }
    
    public void writeByteArray(int fieldNumber, byte[] value) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        size += CodedOutput.computeRawVarint32Size(value.length) + value.length;
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value) throws IOException
    {
        Schema<T> schema = value.cachedSchema();
        // fail fast
        if(!recomputed && !schema.isInitialized(value))
            throw new UninitializedMessageException(value);
        
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        int last = size;
        schema.writeTo(this, value);
        size += CodedOutput.computeRawVarint32Size(size - last);
    }
    
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema) throws IOException
    {
        // fail fast
        if(!recomputed && !schema.isInitialized(value))
            throw new UninitializedMessageException(value);
        
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        int last = size;
        schema.writeTo(this, value);
        size += CodedOutput.computeRawVarint32Size(size - last);
    }

    public <T> void writeObject(int fieldNumber, T value, Class<T> typeClass) throws IOException
    {
        // Persist the serialized data for efficiency.
        byte[] bytes = CodedOutput.getByteArrayFromSerializable(value);
        
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        byte[] delimited = CodedOutput.getTagAndRawVarInt32Bytes(tag, bytes.length);
        size += delimited.length + bytes.length;
        
        if(root==null)
            current = new ByteArrayNode(bytes, (root=new ByteArrayNode(delimited)));
        else
            current = new ByteArrayNode(bytes, new ByteArrayNode(delimited, current));
    }

}
