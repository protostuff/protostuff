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
        return getSize(message, message.cachedSchema());
    }
    
    /**
     * Computes the serialized size of a message.
     */
    public static <T> int getSize(T message, Schema<T> schema)
    {
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
    
    private int size = 0;
    
    public ComputedSizeOutput()
    {
        
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

    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT));
        s += value<0 ? 10 : CodedOutput.computeRawVarint32Size(value);
        size += s;
    }
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(value);
    }
    
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(
                        CodedOutput.encodeZigZag32(value));
    }
    
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
    }
    
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(value);
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(value);
    }
    
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(
                        CodedOutput.encodeZigZag64(value));
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
    }
    
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + 1;
    }

    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(value);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        byte[] bytes = value.getBytes(ByteString.UTF8);
        size += CodedOutput.computeRawVarint32Size(bytes.length) + bytes.length;
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        byte[] bytes = value.getBytes();
        size += CodedOutput.computeRawVarint32Size(bytes.length) + bytes.length;
    }
    
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        size += CodedOutput.computeRawVarint32Size(value.length) + value.length;
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value, boolean repeated) 
    throws IOException
    {
        Schema<T> schema = value.cachedSchema();

        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        int last = size;
        schema.writeTo(this, value);
        size += CodedOutput.computeRawVarint32Size(size - last);
    }
    
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, boolean repeated) 
    throws IOException
    {
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        int last = size;
        schema.writeTo(this, value);
        size += CodedOutput.computeRawVarint32Size(size - last);
    }

}
