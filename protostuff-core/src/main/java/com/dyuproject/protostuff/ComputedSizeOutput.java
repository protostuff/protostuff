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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
    
    public static <T extends Message<T>> int getSize(T message)
    {
        return getSize(message, false);
    }
    
    public static <T extends Message<T>> int getSize(T message, boolean forceWritePrimitives)
    {
        Schema<T> schema = message.getSchema();
        ComputedSizeOutput sizeCount = new ComputedSizeOutput(forceWritePrimitives);
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
    
    private final boolean forceWritePrimitives;
    private ByteArrayNode root, current;
    
    public ComputedSizeOutput(boolean forceWritePrimitives)
    {
        this.forceWritePrimitives = forceWritePrimitives;
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
     * Resets the size count.
     */
    public ComputedSizeOutput resetCount()
    {
        size = 0;
        return this;
    }
    
    ByteArrayNode getRoot()
    {
        return root;
    }

    public void writeInt32(int fieldNumber, int value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT));
        s += value<0 ? 10 : CodedOutput.computeRawVarint32Size(value);
        size += s;
    }
    
    public void writeUInt32(int fieldNumber, int value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(value);
        size += s;
    }
    
    public void writeSInt32(int fieldNumber, int value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(
                        CodedOutput.encodeZigZag32(value));
        size += s;
    }
    
    public void writeFixed32(int fieldNumber, int value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
        size += s;
    }
    
    public void writeSFixed32(int fieldNumber, int value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
        size += s;
    }

    public void writeInt64(int fieldNumber, long value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(value);
        size += s;
    }
    
    public void writeUInt64(int fieldNumber, long value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(value);
        size += s;
    }
    
    public void writeSInt64(int fieldNumber, long value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint64Size(
                        CodedOutput.encodeZigZag64(value));
        size += s;
    }
    
    public void writeFixed64(int fieldNumber, long value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;

        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
        size += s;
    }
    
    public void writeSFixed64(int fieldNumber, long value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;

        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
        size += s;
    }

    public void writeFloat(int fieldNumber, float value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED32)) + CodedOutput.LITTLE_ENDIAN_32_SIZE;
        size += s;
    }

    public void writeDouble(int fieldNumber, double value) throws IOException
    {
        if(value==0 && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_FIXED64)) + CodedOutput.LITTLE_ENDIAN_64_SIZE;
        size += s;
    }

    public void writeBool(int fieldNumber, boolean value) throws IOException
    {
        if(!value && !forceWritePrimitives)
            return;
        
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + 1;
        size += s;
    }

    public void writeEnum(int fieldNumber, int value) throws IOException
    {
        int s = CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_VARINT)) + CodedOutput.computeRawVarint32Size(value);
        size += s;
    }

    public void writeString(int fieldNumber, String value) throws IOException
    {
        if(value==null)
            return;
        
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        byte[] bytes = value.getBytes(ByteString.UTF8);
        size += CodedOutput.computeRawVarint32Size(bytes.length) + bytes.length;
    }

    public void writeBytes(int fieldNumber, ByteString value) throws IOException
    {
        if(value==null)
            return;
        
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        byte[] bytes = value.getBytes();
        size += CodedOutput.computeRawVarint32Size(bytes.length) + bytes.length;
    }
    
    public void writeByteArray(int fieldNumber, byte[] value) throws IOException
    {
        if(value==null)
            return;
        
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        size += CodedOutput.computeRawVarint32Size(value.length) + value.length;
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value) throws IOException
    {
        if(value==null)
            return;
        
        Schema<T> schema = value.getSchema();
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        int last = size;
        schema.writeTo(this, value);
        size += CodedOutput.computeRawVarint32Size(size - last);
    }
    
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema) throws IOException
    {
        if(value==null)
            return;
        
        size += CodedOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, 
                WireFormat.WIRETYPE_LENGTH_DELIMITED));
        int last = size;
        schema.writeTo(this, value);
        size += CodedOutput.computeRawVarint32Size(size - last);
    }

    public <T> void writeObject(int fieldNumber, T value, Class<T> typeClass) throws IOException
    {
        if(value==null)
            return;

        // Persist the serialized data for efficiency.
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream(CodedOutput.DEFAULT_BUFFER_SIZE);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(value);
        byte[] bytes = baos.toByteArray();
        
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        byte[] delimited = CodedOutput.getTagAndRawVarInt32Bytes(tag, bytes.length);
        size += delimited.length + bytes.length;
        
        if(root==null)
            current = new ByteArrayNode(bytes, (root=new ByteArrayNode(delimited)));
        else
            current = new ByteArrayNode(bytes, new ByteArrayNode(delimited, current));
    }

}
