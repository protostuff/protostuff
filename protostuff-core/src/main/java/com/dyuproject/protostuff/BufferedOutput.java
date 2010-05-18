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

import static com.dyuproject.protostuff.StringSerializer.STRING;

import java.io.IOException;
import java.io.OutputStream;

/**
 * More or less the same as {@code DeferredOutput} but it maintains a decent-sized byte buffer 
 * for writing.  {@code DeferredOutput} converts every field to a btye array whereas 
 * {@code BufferedOutput} writes it to the buffer.
 *
 * @author David Yu
 * @created May 18, 2010
 */
public final class BufferedOutput implements Output
{

    static final int DEFAULT_BUFFER_SIZE = Integer.getInteger(
            "bufferedoutput.default_buffer_size", 256);
    
    private final OutputBuffer root;
    private OutputBuffer current;
    private int size = 0;
    private final int bufferSize;
    
    public BufferedOutput()
    {
        this(DEFAULT_BUFFER_SIZE);
    }
    
    public BufferedOutput(int bufferSize)
    {
        root = OutputBuffer.createRoot(bufferSize);
        current = root.next;
        this.bufferSize = bufferSize;
    }
    
    /**
     * Gets the current size of this output.
     */
    public int getSize()
    {
        return size;
    }
    
    /**
     * Writes the raw bytes into the {@link OutputStream}.
     */
    public void streamTo(OutputStream out) throws IOException
    {
        for(OutputBuffer node = root.next; node != null; node = node.next)
        {
            int len = node.offset - node.start;
            //if(len > 0)
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
        for(OutputBuffer node = root.next; node != null; node = node.next)
        {
            int len = node.offset - node.start;
            //if(len > 0)
            //{
                System.arraycopy(node.buffer, node.start, buffer, start, len);
                start += len;
            //}
        }
        return buffer;
    }
    
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = value<0 ? OutputBuffer.writeTagAndRawVarInt64Bytes(tag, value, current, bufferSize) : 
            OutputBuffer.writeTagAndRawVarInt32Bytes(tag, value, current, bufferSize);
        
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = OutputBuffer.writeTagAndRawVarInt32Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = OutputBuffer.writeTagAndRawVarInt32Bytes(tag, 
                CodedOutput.encodeZigZag32(value), current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
        OutputBuffer ob = OutputBuffer.writeTagAndRawLittleEndian32Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
        OutputBuffer ob = OutputBuffer.writeTagAndRawLittleEndian32Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = OutputBuffer.writeTagAndRawVarInt64Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = OutputBuffer.writeTagAndRawVarInt64Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = OutputBuffer.writeTagAndRawVarInt64Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
        OutputBuffer ob = OutputBuffer.writeTagAndRawLittleEndian64Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }
    
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
        OutputBuffer ob = OutputBuffer.writeTagAndRawLittleEndian64Bytes(tag, value, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
        OutputBuffer ob = OutputBuffer.writeTagAndRawLittleEndian32Bytes(tag, 
                Float.floatToRawIntBits(value), current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
        OutputBuffer ob = OutputBuffer.writeTagAndRawLittleEndian64Bytes(tag, 
                Double.doubleToRawLongBits(value), current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = OutputBuffer.writeTagAndRawVarInt32Bytes(tag, value ? 1 : 0, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
        OutputBuffer ob = OutputBuffer.writeTagAndRawVarInt32Bytes(tag, number, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        byte[] bytes = STRING.ser(value);
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        
        OutputBuffer ob = OutputBuffer.writeTagAndByteArray(tag, bytes, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }
    
    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException
    {
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        
        OutputBuffer ob = OutputBuffer.writeTagAndByteArray(tag, bytes, current, bufferSize);
        size += ob.currentSize;
        current = ob;
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value, 
            boolean repeated) throws IOException
    {
        writeObject(fieldNumber, value, value.cachedSchema(), repeated);
    }
    
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, 
            boolean repeated) throws IOException
    {
        OutputBuffer lastCurrent = current;
        int lastSize = size;
        lastCurrent.next = current = new OutputBuffer(lastCurrent);
        
        schema.writeTo(this, value);
        
        int msgSize = size - lastSize;
        
        int tag = WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
        byte[] delimited = CodedOutput.getTagAndRawVarInt32Bytes(tag, msgSize);
        
        size += delimited.length;
        
        // the first tag of the inner message
        OutputBuffer node = lastCurrent.next;
        
        // insert the byte array (message size)
        OutputBuffer ob = new OutputBuffer(delimited, lastCurrent);
        ob.offset = delimited.length;
        ob.next = node;
    }

}
