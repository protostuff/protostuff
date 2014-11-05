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

package io.protostuff;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Designed to be subclassed by implementations of {@link Output} for easier serialization code for streaming or full
 * buffering. This is used when objects need to be serialzied/written into a {@code LinkedBuffer}.
 * 
 * @author David Yu
 * @created Sep 20, 2010
 */
public class WriteSession
{

    public interface FlushHandler
    {
        int flush(WriteSession session,
                byte[] buf, int offset, int len) throws IOException;

        int flush(WriteSession session,
                byte[] buf, int offset, int len,
                byte[] next, int nextoffset, int nextlen) throws IOException;

        int flush(WriteSession session,
                LinkedBuffer lb,
                byte[] buf, int offset, int len) throws IOException;
    }

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

    public final FlushHandler flushHandler;

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
        out = null;
        flushHandler = null;

        sink = WriteSink.BUFFERED;
    }

    public WriteSession(LinkedBuffer head, OutputStream out, FlushHandler flushHandler,
            int nextBufferSize)
    {
        tail = head;
        this.head = head;
        this.nextBufferSize = nextBufferSize;
        this.out = out;
        this.flushHandler = flushHandler;

        sink = WriteSink.STREAMED;

        assert out != null;
    }

    public WriteSession(LinkedBuffer head, OutputStream out)
    {
        this(head, out, null, LinkedBuffer.DEFAULT_BUFFER_SIZE);
    }

    /**
     * Resets this session for re-use. Meant to be overridden by subclasses that have other state to reset.
     */
    public void reset()
    {

    }

    /**
     * The buffer will be cleared (tail will point to the head) and the size will be reset to zero.
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
            if ((len = node.offset - node.start) > 0)
            {
                System.arraycopy(node.buffer, node.start, buf, offset, len);
                offset += len;
            }
        } while ((node = node.next) != null);

        return buf;
    }

    protected int flush(byte[] buf, int offset, int len) throws IOException
    {
        if (flushHandler != null)
            return flushHandler.flush(this, buf, offset, len);

        out.write(buf, offset, len);
        return offset;
    }

    protected int flush(byte[] buf, int offset, int len,
            byte[] next, int nextoffset, int nextlen) throws IOException
    {
        if (flushHandler != null)
            return flushHandler.flush(this, buf, offset, len, next, nextoffset, nextlen);

        out.write(buf, offset, len);
        out.write(next, nextoffset, nextlen);
        return offset;
    }

    protected int flush(LinkedBuffer lb,
            byte[] buf, int offset, int len) throws IOException
    {
        if (flushHandler != null)
            return flushHandler.flush(this, lb, buf, offset, len);

        out.write(buf, offset, len);
        return lb.start;
    }

}