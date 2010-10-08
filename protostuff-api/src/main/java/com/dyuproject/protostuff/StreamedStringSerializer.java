package com.dyuproject.protostuff;

import static com.dyuproject.protostuff.StringSerializer.FIVE_BYTE_LOWER_LIMIT;
import static com.dyuproject.protostuff.StringSerializer.FOUR_BYTE_EXCLUSIVE;
import static com.dyuproject.protostuff.StringSerializer.FOUR_BYTE_LOWER_LIMIT;
import static com.dyuproject.protostuff.StringSerializer.INT_MIN_VALUE;
import static com.dyuproject.protostuff.StringSerializer.LONG_MIN_VALUE;
import static com.dyuproject.protostuff.StringSerializer.ONE_BYTE_EXCLUSIVE;
import static com.dyuproject.protostuff.StringSerializer.THREE_BYTE_EXCLUSIVE;
import static com.dyuproject.protostuff.StringSerializer.THREE_BYTE_LOWER_LIMIT;
import static com.dyuproject.protostuff.StringSerializer.TWO_BYTE_EXCLUSIVE;
import static com.dyuproject.protostuff.StringSerializer.TWO_BYTE_LOWER_LIMIT;
import static com.dyuproject.protostuff.StringSerializer.putBytesFromInt;
import static com.dyuproject.protostuff.StringSerializer.putBytesFromLong;
import static com.dyuproject.protostuff.StringSerializer.writeFixed2ByteInt;

import java.io.IOException;
import java.io.OutputStream;

/**
 * UTF-8 String serialization
 *
 * @author David Yu
 * @created Feb 4, 2010
 */
public final class StreamedStringSerializer
{
    
    private StreamedStringSerializer() {}
    
    /**
     * Writes the stringified int into the {@link LinkedBuffer}.
     */
    public static LinkedBuffer writeInt(final int value, final WriteSession session, 
            final OutputStream out, LinkedBuffer lb) throws IOException
    {
        if(value == Integer.MIN_VALUE)
        {
            final int valueLen = INT_MIN_VALUE.length;
            if(lb.offset + valueLen > lb.buffer.length)
            {
                // not enough size
                out.write(lb.buffer, lb.start, lb.offset-lb.start);
                lb.offset = lb.start;
                //lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
            
            System.arraycopy(INT_MIN_VALUE, 0, lb.buffer, lb.offset, valueLen);
            
            lb.offset += valueLen;
            session.size += valueLen;
            
            return lb;
        }
        
        final int size = (value < 0) ? StringSerializer.stringSize(-value) + 1 : StringSerializer.stringSize(value);
        
        if(lb.offset + size > lb.buffer.length)
        {
            // not enough size
            out.write(lb.buffer, lb.start, lb.offset-lb.start);
            lb.offset = lb.start;
            //lb = new LinkedBuffer(session.nextBufferSize, lb);
        }
        
        putBytesFromInt(value, lb.offset, size, lb.buffer);
        
        lb.offset += size;
        session.size += size;
        
        return lb;
    }
    
    /**
     * Writes the stringified long into the {@link LinkedBuffer}.
     */
    public static LinkedBuffer writeLong(final long value, final WriteSession session, 
            final OutputStream out, LinkedBuffer lb) throws IOException
    {
        if(value == Long.MIN_VALUE)
        {
            final int valueLen = LONG_MIN_VALUE.length;
            if(lb.offset + valueLen > lb.buffer.length)
            {
                //TODO space efficiency (slower path)
                // not enough size
                out.write(lb.buffer, lb.start, lb.offset-lb.start);
                lb.offset = lb.start;
                //lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
            
            System.arraycopy(LONG_MIN_VALUE, 0, lb.buffer, lb.offset, valueLen);
            
            lb.offset += valueLen;
            session.size += valueLen;
            
            return lb;
        }
        
        final int size = (value < 0) ? StringSerializer.stringSize(-value) + 1 : StringSerializer.stringSize(value);
        
        if(lb.offset + size > lb.buffer.length)
        {
            //TODO space efficiency (slower path)
            // not enough size
            out.write(lb.buffer, lb.start, lb.offset-lb.start);
            lb.offset = lb.start;
            //lb = new LinkedBuffer(session.nextBufferSize, lb);
        }
        
        putBytesFromLong(value, lb.offset, size, lb.buffer);
        
        lb.offset += size;
        session.size += size;
        
        return lb;
    }
    
    /**
     * Writes the stringified float into the {@link LinkedBuffer}.
     * TODO - skip string conversion and write directly to buffer
     */
    public static LinkedBuffer writeFloat(final float value, final WriteSession session, 
            final OutputStream out, final LinkedBuffer lb) throws IOException
    {
        return writeAscii(Float.toString(value), session, out, lb);
    }
    
    /**
     * Writes the stringified double into the {@link LinkedBuffer}.
     * TODO - skip string conversion and write directly to buffer
     */
    public static LinkedBuffer writeDouble(final double value, final WriteSession session, 
            final OutputStream out, final LinkedBuffer lb) throws IOException
    {
        return writeAscii(Double.toString(value), session, out, lb);
    }
    
    /**
     * Slow path.  It checks the limit before every write.
     */
    private static LinkedBuffer writeUTF8(final String str, final int index, final int len, 
            byte[] buffer, int offset, int limit,
            final WriteSession session, LinkedBuffer lb)
    {
        int start = offset;
        
        for(int i = index; i < len; i++)
        {
            final char c = str.charAt(i);
            if(c < 0x0080)
            {
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    if(lb.next == null)
                    {
                        // reset
                        offset = start = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        lb = lb.next;
                        // reset
                        lb.offset = lb.start;
                    }
                }
                buffer[offset++] = (byte)c;
            }
            else if(c < 0x0800)
            {
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    if(lb.next == null)
                    {
                        // reset
                        offset = start = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        lb = lb.next;
                        // reset
                        lb.offset = lb.start;
                    }
                }
                buffer[offset++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    if(lb.next == null)
                    {
                        // reset
                        offset = start = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        lb = lb.next;
                        // reset
                        lb.offset = lb.start;
                    }
                }
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
            else
            {
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    if(lb.next == null)
                    {
                        // reset
                        offset = start = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        lb = lb.next;
                        // reset
                        lb.offset = lb.start;
                    }
                }
                buffer[offset++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    if(lb.next == null)
                    {
                        // reset
                        offset = start = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        lb = lb.next;
                        // reset
                        lb.offset = lb.start;
                    }
                }
                buffer[offset++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    if(lb.next == null)
                    {
                        // reset
                        offset = start = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        lb = lb.next;
                        // reset
                        lb.offset = lb.start;
                    }
                }
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
        }
        
        lb.offset = offset;
        session.size += (offset - start);
        
        return lb;
    }
    
    /**
     * Fast path.  The {@link LinkedBuffer}'s capacity is >= string length.
     */
    private static LinkedBuffer writeUTF8(final String str, final int index, final int len, 
            final WriteSession session, final LinkedBuffer lb)
    {
        final byte[] buffer = lb.buffer;
        final int limit = buffer.length;
        final int start = lb.offset;
        
        int offset = start;
        int adjustableLimit = offset + len;

        for(int i=index; i<len; i++)
        {
            final char c = str.charAt(i);
            if(c < 0x0080)
            {
                // ascii
                buffer[offset++] = (byte)c;
            }
            else if(c < 0x0800)
            {
                if(++adjustableLimit > limit)
                {
                    lb.offset = offset;
                    session.size += (offset - start);
                    return writeUTF8(str, i, len, buffer, offset, limit, session, lb);
                }
                
                buffer[offset++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
            else
            {
                adjustableLimit += 2;
                if(adjustableLimit > limit)
                {
                    lb.offset = offset;
                    session.size += (offset - start);
                    return writeUTF8(str, i, len, buffer, offset, limit, session, lb);
                }
                
                buffer[offset++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                buffer[offset++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
        }
        
        lb.offset = offset;
        session.size += (offset - start);
        
        return lb;
    }
    
    /**
     * Writes the utf8-encoded bytes from the string into the {@link LinkedBuffer}.
     */
    public static LinkedBuffer writeUTF8(final String str, final WriteSession session, 
            final OutputStream out, final LinkedBuffer lb) throws IOException
    {
        final int len = str.length();
        if(len == 0)
            return lb;
        
        final byte[] buffer = lb.buffer;
        final int limit = buffer.length, start = lb.start;
        int offset = lb.offset, size = len;
        
        for(int i = 0; i < len; i++)
        {
            final char c = str.charAt(i);
            if(c < 0x0080)
            {
                if(offset == limit)
                {
                    out.write(buffer, start, offset-start);
                    offset = start;
                }
                // ascii
                buffer[offset++] = (byte)c;
            }
            else if(c < 0x0800)
            {
                if(offset + 2 > limit)
                {
                    out.write(buffer, start, offset-start);
                    offset = start;
                }
                
                buffer[offset++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
                size++;
            }
            else
            {
                if(offset + 3 > limit)
                {
                    out.write(buffer, start, offset-start);
                    offset = start;
                }
                
                buffer[offset++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                buffer[offset++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
                size+=2;
            }
        }
        
        session.size += size;
        lb.offset = offset;
        
        return lb;
    }
    
    /**
     * Writes the ascii bytes from the string into the {@link LinkedBuffer}.
     * It is the responsibility of the caller to know in advance that the string is 100% ascii.
     * E.g if you convert a double/float to a string, you are sure it only contains ascii chars.
     */
    public static LinkedBuffer writeAscii(final String str, final WriteSession session, 
            final OutputStream out, final LinkedBuffer lb) throws IOException
    {
        final int len = str.length();
        if(len == 0)
            return lb;
        
        int offset = lb.offset;
        final int limit = lb.buffer.length;
        final byte[] buffer = lb.buffer;
        
        // actual size
        session.size += len;
        
        if(offset + len > limit)
        {
            // need to flush
            int index = 0, 
                start = lb.start, 
                bufSize = limit - start, 
                available = limit - offset, 
                remaining = len - available,
                loops = remaining/bufSize,
                extra = remaining%bufSize;
            
            // write available space
            while(available-->0)
                buffer[offset++] = (byte)str.charAt(index++);
            
            // flush and reset
            out.write(buffer, start, bufSize);
            offset = start;
            
            while(loops-->0)
            {
                for(int i = 0; i < bufSize; i++)
                    buffer[offset++] = (byte)str.charAt(index++);
                
                // flush and reset
                out.write(buffer, start, bufSize);
                offset = start;
            }
            
            while(extra-->0)
            {
                buffer[offset++] = (byte)str.charAt(index++);
            }

        }
        else
        {
            // fast path
            for(int i = 0; i < len; i++)
                buffer[offset++] = (byte)str.charAt(i);
        }
        
        lb.offset = offset;
        
        return lb;
    }
    
    private static void flushAndReset(final LinkedBuffer head, final OutputStream out) 
    throws IOException
    {
        for(LinkedBuffer node = head; node != null; node = node.next)
        {
            final int len = node.offset - node.start;
            if(len > 0)
            {
                out.write(node.buffer, node.start, len);
                node.offset = node.start;
            }
        }
    }
    
    /**
     * The length of the utf8 bytes is written first before the string - which is  
     * fixed 2-bytes.
     * Same behavior as {@link java.io.DataOutputStream#writeUTF(String)}.
     */
    public static LinkedBuffer writeUTF8FixedDelimited(final String str, 
            final WriteSession session, final OutputStream out, final LinkedBuffer lb) 
            throws IOException
    {
        final int lastSize = session.size;
        final int len = str.length();
        
        int offset = lb.offset;
        int withIntOffset = offset + 2;
        
        if(withIntOffset > lb.buffer.length)
        {
            // not enough space for int (2 bytes).
            out.write(lb.buffer, lb.start, lb.offset-lb.start);
            offset = lb.start;
            withIntOffset = offset + 2;
        }
        
        if(len == 0)
        {
            writeFixed2ByteInt(0, lb.buffer, offset);
            lb.offset = withIntOffset;
            // update size
            session.size += 2;
            return lb;
        }
        
        if(withIntOffset + len > lb.buffer.length)
        {
            // not enough space for the string.
            lb.offset = withIntOffset;
            
            final byte[] buffer = lb.buffer;
            
            // slow path
            final LinkedBuffer rb = writeUTF8(str, 0, len, buffer, withIntOffset, buffer.length, 
                    session, lb);
            
            final int size = session.size - lastSize;
            
            writeFixed2ByteInt(size, buffer, offset);
            
            // update size
            session.size += 2;
            
            assert rb != lb;
            // flush and reset nodes
            flushAndReset(lb, out);
            
            return lb;
        }

        // everything fits
        lb.offset = withIntOffset;
        
        final LinkedBuffer rb = writeUTF8(str, 0, len, session, lb);
        
        final int size = session.size - lastSize;
        
        writeFixed2ByteInt(size, lb.buffer, offset);
        
        // update size
        session.size += 2;
        
        if(rb != lb)
        {
            // flush and reset nodes
            flushAndReset(lb, out);
        }
        
        return lb;
    }
    
    private static LinkedBuffer writeUTF8OneByteDelimited(final String str, final int index, 
            final int len, final WriteSession session, final OutputStream out, 
            final LinkedBuffer lb) throws IOException
    {
        final int lastSize = session.size;
        int offset = lb.offset;
        
        if(offset == lb.buffer.length)
        {
            // not enough space for the 1-byte varint.
            // flush
            out.write(lb.buffer, lb.start, lb.offset-lb.start);
            offset = lb.start;
        }
        
        final int withIntOffset = offset + 1;
        if(withIntOffset + len > lb.buffer.length)
        {
            // not enough space for the string.
            lb.offset = withIntOffset;
            
            final byte[] buffer = lb.buffer;
            
            // slow path
            final LinkedBuffer rb = writeUTF8(str, index, len, buffer, withIntOffset, buffer.length, 
                    session, lb);
            
            final int size = session.size - lastSize;
            
            lb.buffer[offset] = (byte)(size);
            
            // update size
            session.size++;
            
            assert rb != lb;
            
            // flush and reset nodes
            flushAndReset(lb, out);
            
            return lb;
        }

        // everything fits
        lb.offset = withIntOffset;
        
        final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);
        
        final int size = session.size - lastSize;
        
        lb.buffer[offset] = (byte)(size);
        
        // update size
        session.size++;
        
        if(rb != lb)
        {
            // flush and reset nodes
            flushAndReset(lb, out);
        }
        
        return lb;
    }
    
    private static LinkedBuffer writeUTF8VarDelimited(final String str, final int index, 
            final int len, final int lowerLimit, final int expectedSize,
            final WriteSession session, final OutputStream out, final LinkedBuffer lb)
            throws IOException
    {
        final int lastSize = session.size;
        
        int offset = lb.offset;
        int withIntOffset = offset + expectedSize;
        
        if(withIntOffset > lb.buffer.length)
        {
            // not enough space for the varint.
            // flush
            out.write(lb.buffer, lb.start, lb.offset-lb.start);
            offset = lb.start;
            withIntOffset = offset + expectedSize;
        }
        
        if(withIntOffset + len > lb.buffer.length)
        {
            // not enough space for the string.
            lb.offset = withIntOffset;
            
            final byte[] buffer = lb.buffer;
            
            // slow path
            final LinkedBuffer rb = writeUTF8(str, index, len, buffer, withIntOffset, buffer.length, 
                    session, lb);
            
            int size = session.size - lastSize;

            int last = expectedSize - 1;
            if(size < lowerLimit)
            {
                // move one space to the left since the varint is 1-byte smaller
                System.arraycopy(buffer, withIntOffset, buffer, withIntOffset - 1, 
                        lb.offset - withIntOffset);
                // update size
                session.size += last;
                lb.offset--;
                last--;
            }
            else
            {
                // update size
                session.size += expectedSize;
            }
            
            for (int i = 0; i < last; i++, size >>>= 7)
                buffer[offset++] = (byte)((size & 0x7F) | 0x80);
            
            buffer[offset] = (byte)(size);
            
            assert rb != lb;
            
            // flush and reset nodes
            flushAndReset(lb, out);
            
            return lb;
        }

        // everything fits
        lb.offset = withIntOffset;
        
        final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);
        
        int size = session.size - lastSize;
        
        final byte[] buffer = lb.buffer;
        
        int last = expectedSize - 1;
        if(size < lowerLimit)
        {
            // move one space to the left since the varint is 1-byte smaller
            System.arraycopy(buffer, withIntOffset, buffer, withIntOffset - 1, 
                    lb.offset - withIntOffset);
            // update size
            session.size += last;
            lb.offset--;
            last--;
        }
        else
        {
            // update size
            session.size += expectedSize;
        }
        
        for (int i = 0; i < last; i++, size >>>= 7)
            buffer[offset++] = (byte)((size & 0x7F) | 0x80);
        
        buffer[offset] = (byte)(size);
        
        if(rb != lb)
        {
            // flush and reset nodes
            flushAndReset(lb, out);
        }
        
        return lb;
    }
    
    /**
     * The length of the utf8 bytes is written first before the string - which is  
     * a variable int (1 to 5 bytes).
     */
    public static LinkedBuffer writeUTF8VarDelimited(final String str, final WriteSession session, 
            final OutputStream out, final LinkedBuffer lb) throws IOException
    {
        final int len = str.length();
        if(len == 0)
        {
            if(lb.offset == lb.buffer.length)
            {
                // buffer full
                // flush
                out.write(lb.buffer, lb.start, lb.offset-lb.start);
                lb.offset = lb.start; 
            }
            
            // write zero
            lb.buffer[lb.offset++] = 0x00;
            // update size
            session.size++;
            return lb;
        }
        
        if(len < ONE_BYTE_EXCLUSIVE)
        {
            // the varint will be max 1-byte. (even if all chars are non-ascii)
            return writeUTF8OneByteDelimited(str, 0, len, session, out, lb);
        }
        
        if(len < TWO_BYTE_EXCLUSIVE)
        {
            // the varint will be max 2-bytes and could be 1-byte. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, TWO_BYTE_LOWER_LIMIT, 2, 
                    session, out, lb);
        }
        
        if(len < THREE_BYTE_EXCLUSIVE)
        {
            // the varint will be max 3-bytes and could be 2-bytes. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, THREE_BYTE_LOWER_LIMIT, 3, 
                    session, out, lb);
        }
        
        if(len < FOUR_BYTE_EXCLUSIVE)
        {
            // the varint will be max 4-bytes and could be 3-bytes. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, FOUR_BYTE_LOWER_LIMIT, 4,
                    session, out, lb);
        }
        
        // the varint will be max 5-bytes and could be 4-bytes. (even if all non-ascii)
        return writeUTF8VarDelimited(str, 0, len, FIVE_BYTE_LOWER_LIMIT, 5, session, out, lb);
    }

}
