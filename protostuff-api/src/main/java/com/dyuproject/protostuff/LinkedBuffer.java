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

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;


/**
 * A buffer that wraps a byte array and has a reference to the next buffer for dynamic increase.
 * 
 * @author David Yu
 * @created May 18, 2010
 */
public final class LinkedBuffer
{
    
    /**
     * Allocates a new buffer with the specified size.
     */
    public static LinkedBuffer allocate(int size)
    {
        if(size < 1)
            throw new IllegalArgumentException("invalid size.");
        
        return new LinkedBuffer(size);
    }
    
    /**
     * Wraps the byte array buffer as a read-only buffer.
     */
    public static LinkedBuffer wrap(byte[] array, int offset, int length)
    {
        return new LinkedBuffer(array, offset, offset + length);
    }
    
    /**
     * Uses the existing byte array as the internal buffer.
     */
    public static LinkedBuffer use(byte[] buffer)
    {
        if(buffer.length < 1)
            throw new IllegalArgumentException("invalid buffer size.");
        
        return new LinkedBuffer(buffer, 0, 0);
    }
    
    /**
     * Writes the contents of the {@link LinkedBuffer} into the {@link OutputStream}.
     * 
     * @return the total content size of the buffer.
     */
    public static int writeTo(final OutputStream out, final LinkedBuffer head) throws IOException
    {
        int contentSize = 0;
        for(LinkedBuffer node = head; node != null; node = node.next)
        {
            final int len = node.offset - node.start;
            if(len > 0)
            {
                out.write(node.buffer, node.start, len);
                contentSize += len;
            }
        }
        return contentSize;
    }
    
    /**
     * Writes the contents of the {@link LinkedBuffer} into the {@link DataOutput}.
     * 
     * @return the total content size of the buffer.
     */
    public static int writeTo(final DataOutput out, final LinkedBuffer head) throws IOException
    {
        int contentSize = 0;
        for(LinkedBuffer node = head; node != null; node = node.next)
        {
            final int len = node.offset - node.start;
            if(len > 0)
            {
                out.write(node.buffer, node.start, len);
                contentSize += len;
            }
        }
        return contentSize;
    }

    final byte[] buffer;
    
    final int start;
    
    int offset;

    LinkedBuffer next;
    
    /**
     * Creates a buffer with the specified {@code size}.
     */
    LinkedBuffer(int size)
    {
        this(new byte[size], 0, 0);
    }
    
    /**
     * Creates a buffer with the specified {@code size} and appends to the 
     * provided buffer {@code appendTarget}.
     */
    LinkedBuffer(int size, LinkedBuffer appendTarget)
    {
        this(new byte[size], 0, 0, appendTarget);
    }
    
    /**
     * Uses the buffer starting at the specified {@code offset} 
     */
    LinkedBuffer(byte[] buffer, int offset)
    {
        this(buffer, offset, offset);
    }

    LinkedBuffer(byte[] buffer, int start, int offset)
    {
        this.buffer = buffer;
        this.start = start;
        this.offset = offset;
    }
    
    /**
     * Uses the buffer starting at the specified {@code offset} and appends to the 
     * provided buffer {@code appendTarget}.
     */
    LinkedBuffer(byte[] buffer, int offset, LinkedBuffer appendTarget)
    {
        this(buffer, offset, offset);
        appendTarget.next = this;
    }
    
    LinkedBuffer(byte[] buffer, int start, int offset, LinkedBuffer appendTarget)
    {
        this(buffer, start, offset);
        appendTarget.next = this;
    }
    
    /**
     * Creates a view from the buffer {@code viewSource} and appends the view to the 
     * provided buffer {@code appendTarget}.
     */
    LinkedBuffer(LinkedBuffer viewSource, LinkedBuffer appendTarget)
    {
        buffer = viewSource.buffer;
        offset = start = viewSource.offset;
        appendTarget.next = this;
    }
    
    /**
     * The offset will be reset to its starting position.
     * The buffer next to this will be dereferenced.
     */
    public LinkedBuffer clear()
    {
        next = null;
        offset = start;
        return this;
    }
    
    /**
     * This is used when objects need to be serialzied/written into a {@code LinkedBuffer}.
     *
     */
    public static class WriteSession
    {
        
        public static final int DEFAULT_BUFFER_SIZE = Integer.getInteger(
                "writesession.default_buffer_size", 512);
        
        public static final int ARRAY_COPY_SIZE_LIMIT = Integer.getInteger(
                "writesession.array_copy_size_limit", 255);
        
        public final LinkedBuffer head;
        protected LinkedBuffer tail;
        
        protected int size = 0;
        //protected int physicalBufferCount = 0;
        //protected int virtualBufferCount = 0;
        
        public final int nextBufferSize;
        public final int arrayCopySizeLimit;
        
        public WriteSession(LinkedBuffer head)
        {
            this(head, DEFAULT_BUFFER_SIZE, ARRAY_COPY_SIZE_LIMIT);
        }
        
        public WriteSession(LinkedBuffer head, int nextBufferSize, int arrayCopySizeLimit)
        {
            tail = head;
            this.head = head;
            this.nextBufferSize = nextBufferSize;
            this.arrayCopySizeLimit = arrayCopySizeLimit;
        }
        
        /**
         * The tail will be point to the head and the size will be reset to zero.
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
            final LinkedBuffer head = this.head;

            final byte[] buf = new byte[size];
            
            int offset = head.offset - head.start;
            
            System.arraycopy(head.buffer, 0, buf, 0, offset);
            
            for(LinkedBuffer node = head.next; node != null; node = node.next)
            {
                final int len = node.offset - node.start;
                if(len > 0)
                {
                    System.arraycopy(node.buffer, node.start, buf, offset, len);
                    offset += len;
                }
            }
            
            return buf;
        }

    }

}
