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

/**
 * A buffer that wraps a byte array and has a reference to the next node.
 * 
 * @author David Yu
 * @created May 18, 2010
 */
final class OutputBuffer
{

    final byte[] buffer;
    
    final int start;
    
    int offset, currentSize;

    OutputBuffer next;
    
    // view
    OutputBuffer(OutputBuffer viewSource)
    {
        buffer = viewSource.buffer;
        offset = start = viewSource.offset;
    }
    
    OutputBuffer(byte[] buffer)
    {
        this.buffer = buffer;
        offset = start = 0;
    }
    
    OutputBuffer(byte[] buffer, OutputBuffer ob)
    {
        this(buffer);
        ob.next = this;
    }
    
    static OutputBuffer createRoot(int bufferSize)
    {
        OutputBuffer root = new OutputBuffer((byte[])null);
        root.next = new OutputBuffer(new byte[bufferSize]);
        return root;
    }
    
    /** Returns the output buffer encoded with the tag and byte array */
    public static OutputBuffer writeTagAndByteArray(int tag, byte[] value, OutputBuffer ob, 
            int bufferSize)
    {
        ob = writeTagAndRawVarInt32Bytes(tag, value.length, ob, bufferSize);
        // delimited size + byte size 
        int totalSize = ob.currentSize + value.length;
        
        OutputBuffer rb = ob.offset + value.length < ob.buffer.length ? ob : 
            new OutputBuffer(new byte[bufferSize], ob);
        
        System.arraycopy(value, 0, rb.buffer, rb.offset, value.length);
        
        rb.offset += value.length;
        
        OutputBuffer view = new OutputBuffer(rb);
        
        view.currentSize = totalSize;
        
        new OutputBuffer(value, rb).next = view;
        
        return view;
    }

    /** Returns the output buffer encoded with the tag and var int 32 */
    public static OutputBuffer writeTagAndRawVarInt32Bytes(int tag, int value, OutputBuffer ob, 
            int bufferSize)
    {
        int tagSize = CodedOutput.computeRawVarint32Size(tag);
        int size = CodedOutput.computeRawVarint32Size(value);
        int totalSize = tagSize + size;
        
        OutputBuffer rb = ob.offset + totalSize < ob.buffer.length ? ob : 
            new OutputBuffer(new byte[bufferSize], ob);

        byte[] buffer = rb.buffer;
        int offset = rb.offset;
        rb.offset += totalSize;
        rb.currentSize = totalSize;
        
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
        
        return rb;
    }

    /** Returns the output buffer encoded with the tag and var int 64 */
    public static OutputBuffer writeTagAndRawVarInt64Bytes(int tag, long value, OutputBuffer ob, 
            int bufferSize)
    {
        int tagSize = CodedOutput.computeRawVarint32Size(tag);
        int size = CodedOutput.computeRawVarint64Size(value);
        int totalSize = tagSize + size;
        
        OutputBuffer rb = ob.offset + totalSize < ob.buffer.length ? ob : 
            new OutputBuffer(new byte[bufferSize], ob);

        byte[] buffer = rb.buffer;
        int offset = rb.offset;
        rb.offset += totalSize;
        rb.currentSize = totalSize;
        
        if (tagSize == 1)
        {
            buffer[offset++] = (byte)tag;
        }
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        if (size == 1)
        {
            buffer[offset] = (byte)value;
        }
        else
        {
            for (int i = 0, last = size - 1; i < last; i++, value >>>= 7)
                buffer[offset++] = (byte)(((int)value & 0x7F) | 0x80);

            buffer[offset] = (byte)value;
        }
        
        return rb;
    }
    

    /** Returns the output buffer encoded with the tag and little endian 32 */
    public static OutputBuffer writeTagAndRawLittleEndian32Bytes(int tag, int value, 
            OutputBuffer ob, int bufferSize)
    {
        int tagSize = CodedOutput.computeRawVarint32Size(tag);
        int totalSize = tagSize + CodedOutput.LITTLE_ENDIAN_32_SIZE;
        
        OutputBuffer rb = ob.offset + totalSize < ob.buffer.length ? ob : 
            new OutputBuffer(new byte[bufferSize], ob);

        byte[] buffer = rb.buffer;
        int offset = rb.offset;
        rb.offset += totalSize;
        rb.currentSize = totalSize;
        
        if (tagSize == 1)
        {
            buffer[offset++] = (byte)tag;
        }
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        CodedOutput.writeRawLittleEndian32(value, buffer, offset);
        
        return rb;
    }

    /** Returns the output buffer encoded with the tag and little endian 64 */
    public static OutputBuffer writeTagAndRawLittleEndian64Bytes(int tag, long value, 
            OutputBuffer ob, int bufferSize)
    {
        int tagSize = CodedOutput.computeRawVarint32Size(tag);
        int totalSize = tagSize + CodedOutput.LITTLE_ENDIAN_64_SIZE;
        
        OutputBuffer rb = ob.offset + totalSize < ob.buffer.length ? ob : 
            new OutputBuffer(new byte[bufferSize], ob);

        byte[] buffer = rb.buffer;
        int offset = rb.offset;
        rb.offset += totalSize;
        rb.currentSize = totalSize;

        if (tagSize == 1)
        {
            buffer[offset++] = (byte)tag;
        }
        else
        {
            for (int i = 0, last = tagSize - 1; i < last; i++, tag >>>= 7)
                buffer[offset++] = (byte)((tag & 0x7F) | 0x80);

            buffer[offset++] = (byte)tag;
        }

        CodedOutput.writeRawLittleEndian64(value, buffer, offset);

        return rb;
    }

}
