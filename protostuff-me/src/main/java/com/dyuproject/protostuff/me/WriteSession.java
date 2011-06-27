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

package com.dyuproject.protostuff.me;

import java.io.OutputStream;

/**
 * Designed to be subclassed by implementations of {@link Output} for easier serialization 
 * code for streaming or full buffering.
 * This is used when objects need to be serialzied/written into a {@code LinkedBuffer}.
 *
 * @author David Yu
 * @created Sep 20, 2010
 */
public class WriteSession
{
    
    /**
     * The main/root/head buffer of this write session.
     */
    public final LinkedBuffer head;
    
    /**
     * The last buffer of this write session (This points to head if growing not needed).
     */
    protected LinkedBuffer tail;
    
    /**
     * The actual number of bytes written to the buffer.
     */
    protected int size = 0;
    
    /**
     * The next buffer size used when growing the buffer.
     */
    public final int nextBufferSize;
    
    /**
     * The sink of this buffer.
     */
    public final OutputStream out;
    
    /**
     * The sink of this write session.
     */
    public final WriteSink sink;
    
    public WriteSession(LinkedBuffer head)
    {
        this(head, LinkedBuffer.DEFAULT_BUFFER_SIZE);
    }
    
    public WriteSession(LinkedBuffer head, int nextBufferSize)
    {
        tail = head;
        this.head = head;
        this.nextBufferSize = nextBufferSize;
        this.out = null;
        sink = WriteSink.BUFFERED;
    }
    
    public WriteSession(LinkedBuffer head, OutputStream out)
    {
        tail = head;
        this.head = head;
        this.nextBufferSize = LinkedBuffer.DEFAULT_BUFFER_SIZE;
        this.out = out;
        sink = WriteSink.STREAMED;
        
        //assert out != null;
    }
    
    /**
     * The buffer will be cleared (tail will point to the head) and the size 
     * will be reset to zero.
     */
    public WriteSession clear()
    {
        tail = head.clear();
        size = 0;
        return this;
    }
    
    /**
     * Returns the amount of bytes written in this session.
     */
    public final int getSize()
    {
        return size;
    }
    
    /**
     * Returns a single byte array containg all the contents written to the buffer(s).
     */
    public final byte[] toByteArray()
    {
        LinkedBuffer node = head;
        int offset = 0, len;
        final byte[] buf = new byte[size];
        do
        {
            if((len = node.offset - node.start) > 0)
            {
                System.arraycopy(node.buffer, node.start, buf, offset, len);
                offset += len;
            }
        }
        while((node=node.next) != null);
        
        return buf;
    }

}