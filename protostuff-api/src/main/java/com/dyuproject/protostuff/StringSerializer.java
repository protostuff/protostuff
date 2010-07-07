package com.dyuproject.protostuff;

import java.io.UnsupportedEncodingException;

import com.dyuproject.protostuff.LinkedBuffer.WriteSession;

/**
 * UTF-8 String serialization
 *
 * @author David Yu
 * @created Feb 4, 2010
 */
public final class StringSerializer
{
    
    private StringSerializer() {}
    
    private static final int ONE_BYTE_EXCLUSIVE = 0x00000080/3 + 1;
    
    private static final int TWO_BYTE_LOWER_LIMIT = 0x00000080;

    private static final int TWO_BYTE_EXCLUSIVE = 0x00000800/3 + 1;
    
    private static final int THREE_BYTE_LOWER_LIMIT = 0x00000800;

    private static final int THREE_BYTE_EXCLUSIVE = 0x00008000/3 + 1;
    
    private static final int FOUR_BYTE_LOWER_LIMIT = 0x00008000;

    private static final int FOUR_BYTE_EXCLUSIVE = 0x00080000/3 + 1;
    
    private static final int FIVE_BYTE_LOWER_LIMIT = 0x00080000;
    
    /**
     * Computes the size of the utf8 string beginning at the specified {@code index} with the 
     * specified {@code length}.
     */
    public static int computeUTF8Size(final String str, final int index, final int len)
    {
        int size = len;
        for(int i = index; i < len; i++)
        {
            final char c = str.charAt(i);
            if(c < 0x0080)
                continue;
            
            if(c < 0x0800)
                size++;
            else
                size += 2;
        }
        return size;
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
                    // reset
                    start = offset = 0;
                    buffer = new byte[session.nextBufferSize];
                    limit = session.nextBufferSize;
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
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
                    // reset
                    start = offset = 0;
                    buffer = new byte[session.nextBufferSize];
                    limit = session.nextBufferSize;
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
                }
                buffer[offset++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    // reset
                    start = offset = 0;
                    buffer = new byte[session.nextBufferSize];
                    limit = session.nextBufferSize;
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
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
                    // reset
                    start = offset = 0;
                    buffer = new byte[session.nextBufferSize];
                    limit = session.nextBufferSize;
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
                }
                buffer[offset++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    // reset
                    start = offset = 0;
                    buffer = new byte[session.nextBufferSize];
                    limit = session.nextBufferSize;
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
                }
                buffer[offset++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                
                if(offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    session.size += (offset - start);
                    // reset
                    start = offset = 0;
                    buffer = new byte[session.nextBufferSize];
                    limit = session.nextBufferSize;
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
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
     * Writes the utf8-encoded bytes into the {@link LinkedBuffer}.
     */
    public static LinkedBuffer writeUTF8(final String str, final WriteSession session, 
            final LinkedBuffer lb)
    {
        final int len = str.length();
        if(len == 0)
            return lb;
        
        final int remaining = lb.buffer.length - lb.offset;
        
        return len > remaining ? writeUTF8(str, 0, len, lb.buffer, lb.offset, lb.buffer.length, 
                session, lb) : writeUTF8(str, 0, len, session, lb);
    }
    
    private static void writeFixed2ByteInt(final int value, final byte[] buffer, int offset)
    {
        buffer[offset++] = (byte)((value >>>  8) & 0xFF);
        buffer[offset] = (byte)((value >>>  0) & 0xFF);
    }
    
    /**
     * The length of the utf8 bytes is written first before the string - which is  
     * fixed 2-bytes.
     * Same behavior as {@link java.io.DataOutputStream#writeUTF(String)}.
     */
    public static LinkedBuffer writeUTF8FixedDelimited(final String str, 
            final WriteSession session, LinkedBuffer lb)
    {
        final int lastSize = session.size;
        final int len = str.length();
        
        int offset = lb.offset;
        final int withIntOffset = offset + 2;
        
        if(withIntOffset > lb.buffer.length)
        {
            // not enough space for int (2 bytes).
            // create a new buffer.
            lb = new LinkedBuffer(len+2 > session.nextBufferSize ? len+2 : session.nextBufferSize, 
                    lb);
            offset = 0;
            lb.offset = 2;
            
            if(len == 0)
            {
                writeFixed2ByteInt(0, lb.buffer, offset);
                // update size
                session.size += 2;
                return lb;
            }
            
            // fast path
            final LinkedBuffer rb = writeUTF8(str, 0, len, session, lb);
            
            final int size = session.size - lastSize;
            
            writeFixed2ByteInt(size, lb.buffer, offset);
            
            // update size
            session.size += 2;
            
            return rb;
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
            
            return rb;
        }

        // everything fits
        lb.offset = withIntOffset;
        
        final LinkedBuffer rb = writeUTF8(str, 0, len, session, lb);
        
        final int size = session.size - lastSize;
        
        writeFixed2ByteInt(size, lb.buffer, offset);
        
        // update size
        session.size += 2;
        
        return rb;
    }
    
    private static LinkedBuffer writeUTF8OneByteDelimited(final String str, final int index, 
            final int len, final WriteSession session, LinkedBuffer lb)
    {
        final int lastSize = session.size;
        int offset = lb.offset;
        
        if(offset == lb.buffer.length)
        {
            // not enough space for the 1-byte varint.
            // create a new buffer.
            lb = new LinkedBuffer(len+1 > session.nextBufferSize ? len+1 : session.nextBufferSize, 
                    lb);
            offset = 0;
            lb.offset = 1;
            
            // fast path
            final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);
            
            final int size = session.size - lastSize;
            
            lb.buffer[offset] = (byte)(size);
            
            // update size
            session.size++;
            
            return rb;
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
            
            return rb;
        }

        // everything fits
        lb.offset = withIntOffset;
        
        final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);
        
        final int size = session.size - lastSize;
        
        lb.buffer[offset] = (byte)(size);
        
        // update size
        session.size++;
        
        return rb;
    }
    
    private static LinkedBuffer writeUTF8VarDelimited(final String str, final int index, 
            final int len, final int lowerLimit, final int expectedSize,
            final WriteSession session, LinkedBuffer lb)
    {
        final int lastSize = session.size;
        
        int offset = lb.offset;
        final int withIntOffset = offset + expectedSize;
        
        if(withIntOffset > lb.buffer.length)
        {
            // not enough space for the varint.
            // create a new buffer.
            lb = new LinkedBuffer(len+1 > session.nextBufferSize ? len+1 : session.nextBufferSize, 
                    lb);
            offset = 0;
            lb.offset = expectedSize;
            
            // fast path
            final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);
            
            int size = session.size - lastSize;
            
            final byte[] buffer = lb.buffer;
            
            int last = expectedSize - 1;
            if(size < lowerLimit)
            {
                // move one space to the left since the varint is 1-byte smaller
                System.arraycopy(buffer, expectedSize, buffer, expectedSize-1, 
                        lb.offset - expectedSize);
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

            return rb;
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
            
            return rb;
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
        
        return rb;
    }
    
    /**
     * The length of the utf8 bytes is written first before the string - which is  
     * a variable int (1 to 5 bytes).
     */
    public static LinkedBuffer writeUTF8VarDelimited(final String str, final WriteSession session, 
            LinkedBuffer lb)
    {
        final int len = str.length();
        if(len == 0)
        {
            if(lb.offset == lb.buffer.length)
            {
                // buffer full
                lb = new LinkedBuffer(session.nextBufferSize, lb); 
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
            return writeUTF8OneByteDelimited(str, 0, len, session, lb);
        }
        
        if(len < TWO_BYTE_EXCLUSIVE)
        {
            // the varint will be max 2-bytes and could be 1-byte. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, TWO_BYTE_LOWER_LIMIT, 2, 
                    session, lb);
        }
        
        if(len < THREE_BYTE_EXCLUSIVE)
        {
            // the varint will be max 3-bytes and could be 2-bytes. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, THREE_BYTE_LOWER_LIMIT, 3, 
                    session, lb);
        }
        
        if(len < FOUR_BYTE_EXCLUSIVE)
        {
            // the varint will be max 4-bytes and could be 3-bytes. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, FOUR_BYTE_LOWER_LIMIT, 4,
                    session, lb);
        }
        
        // the varint will be max 5-bytes and could be 4-bytes. (even if all non-ascii)
        return writeUTF8VarDelimited(str, 0, len, FIVE_BYTE_LOWER_LIMIT, 5, session, lb);
    }
    
    public static final class STRING
    {
        private STRING() {}
        
        public static String deser(byte[] nonNullValue)
        {
            try
            {
                return new String(nonNullValue, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public static String deser(byte[] nonNullValue, int offset, int len)
        {
            try
            {
                return new String(nonNullValue, offset, len, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }
        }

        public static byte[] ser(String nonNullValue)
        {
            try
            {
                return nonNullValue.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}
