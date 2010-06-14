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
 * A buffer that wraps a byte array and has a reference to the next node for dynamic increase.
 * 
 * @author David Yu
 * @created May 18, 2010
 */
public final class OutputBuffer
{

    final byte[] buffer;
    
    final int start;
    
    int offset;

    OutputBuffer next;
    
    // view
    OutputBuffer(OutputBuffer viewSource)
    {
        buffer = viewSource.buffer;
        offset = start = viewSource.offset;
    }
    
    public OutputBuffer(int size)
    {
        this(new byte[size], 0, 0);
    }
    
    public OutputBuffer(byte[] buffer)
    {
        this(buffer, 0, 0);
    }
    
    public OutputBuffer(byte[] buffer, int offset)
    {
        this(buffer, offset, offset);
    }

    OutputBuffer(byte[] buffer, int start, int offset)
    {
        this.buffer = buffer;
        this.start = start;
        this.offset = offset;
    }
    
    // links this node to the given output buffer.
    public OutputBuffer(byte[] buffer, OutputBuffer ob)
    {
        this(buffer, 0, 0);
        ob.next = this;
    }
    
    OutputBuffer(byte[] buffer, int start, int offset, OutputBuffer ob)
    {
        this(buffer, start, offset);
        ob.next = this;
    }
    
    /*//Wraps the byte array buffer as a read only buffer.
    public static OutputBuffer asReadOnlyBuffer(byte[] array, int offset, int length)
    {
        return new OutputBuffer(array, offset, offset + length);
    }
    
    //Returns the start index of the internal buffer.
    public int start()
    {
        return start;
    }
    
    //Returns the actual length of the internal buffer.
    public int length()
    {
        return offset - start;
    }
    
    //Returns the byte array buffer.
    public byte[] buffer()
    {
        return buffer;
    }
    
    //Returns the next node.
    public OutputBuffer next()
    {
        return next;
    }
    
    //Returns the number of elements remaining in this buffer.
    public int remaining()
    {
        return buffer.length - offset;
    }
    
    //Links this to the provided {@code buffer}.
    public OutputBuffer linkTo(OutputBuffer buffer)
    {
        buffer.next = this;
        return this;
    }*/
}
