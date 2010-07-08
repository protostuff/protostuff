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
    
    /**
     * From {@link java.lang.Integer#toString(int)}
     */
    private static final int[] sizeTable = new int[]{ 
        9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE 
    };
    
    private static final char [] DigitTens = {
        '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
        '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
        '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
        '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
        '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
        '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
        '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
        '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
        '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
        '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
    }; 

    private static final char [] DigitOnes = { 
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };
    
    private static final char[] digits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
        'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
        'o' , 'p' , 'q' , 'r' , 's' , 't' ,
        'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    };
    
    private static final byte[] INT_MIN_VALUE = new byte[]{
        (byte)'-', 
        (byte)'2', 
        (byte)'1', (byte)'4', (byte)'7', 
        (byte)'4', (byte)'8', (byte)'3', 
        (byte)'6', (byte)'4', (byte)'8'
    };
    
    private static final byte[] LONG_MIN_VALUE = new byte[]{
        (byte)'-', 
        (byte)'9', 
        (byte)'2', (byte)'2', (byte)'3', 
        (byte)'3', (byte)'7', (byte)'2', 
        (byte)'0', (byte)'3', (byte)'6', 
        (byte)'8', (byte)'5', (byte)'4', 
        (byte)'7', (byte)'7', (byte)'5', 
        (byte)'8', (byte)'0', (byte)'8'
    };
    
    private static final int ONE_BYTE_EXCLUSIVE = 0x00000080/3 + 1;
    
    private static final int TWO_BYTE_LOWER_LIMIT = 0x00000080;

    private static final int TWO_BYTE_EXCLUSIVE = 0x00000800/3 + 1;
    
    private static final int THREE_BYTE_LOWER_LIMIT = 0x00000800;

    private static final int THREE_BYTE_EXCLUSIVE = 0x00008000/3 + 1;
    
    private static final int FOUR_BYTE_LOWER_LIMIT = 0x00008000;

    private static final int FOUR_BYTE_EXCLUSIVE = 0x00080000/3 + 1;
    
    private static final int FIVE_BYTE_LOWER_LIMIT = 0x00080000;
    
    private static void putBytesFromInt(int i, final int index, final byte[] buf)
    {
        int q, r;
        int charPos = index;
        char sign = 0;

        if (i < 0)
        {
            sign = '-';
            i = -i;
        }

        // Generate two digits per iteration
        while (i >= 65536)
        {
            q = i / 100;
            // really: r = i - (q * 100);
            r = i - ((q << 6) + (q << 5) + (q << 2));
            i = q;
            buf[--charPos] = (byte)DigitOnes[r];
            buf[--charPos] = (byte)DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (;;)
        {
            q = (i * 52429) >>> (16 + 3);
            r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
            buf[--charPos] = (byte)digits[r];
            i = q;
            if (i == 0)
                break;
        }
        if (sign != 0)
        {
            buf[--charPos] = (byte)sign;
        }
    }
    
    private static void putBytesFromLong(long i, int index, final byte[] buf)
    {
        long q;
        int r;
        int charPos = index;
        char sign = 0;

        if (i < 0)
        {
            sign = '-';
            i = -i;
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > Integer.MAX_VALUE)
        {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int)(i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = (byte)DigitOnes[r];
            buf[--charPos] = (byte)DigitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int)i;
        while (i2 >= 65536)
        {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = (byte)DigitOnes[r];
            buf[--charPos] = (byte)DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (;;)
        {
            q2 = (i2 * 52429) >>> (16 + 3);
            r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
            buf[--charPos] = (byte)digits[r];
            i2 = q2;
            if (i2 == 0)
                break;
        }
        if (sign != 0)
        {
            buf[--charPos] = (byte)sign;
        }
    }

    // Requires positive x
    private static int stringSize(int x)
    {
        for (int i = 0;; i++)
        {
            if (x <= sizeTable[i])
                return i + 1;
        }
    }
    
    // Requires positive x
    private static int stringSize(long x)
    {
        long p = 10;
        for (int i = 1; i < 19; i++)
        {
            if (x < p)
                return i;
            p = 10 * p;
        }
        return 19;
    }
    
    /**
     * Encodes the int to utf8 bytes (like converting an int to a string) and is directly
     * written to the buffer.
     */
    public static LinkedBuffer writeUTF8FromInt(final int value, final WriteSession session, 
            LinkedBuffer lb)
    {
        if(value == Integer.MIN_VALUE)
        {
            final int valueLen = INT_MIN_VALUE.length;
            if(lb.offset + valueLen > lb.buffer.length)
            {
                // not enough size
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
            
            System.arraycopy(INT_MIN_VALUE, 0, lb.buffer, lb.offset, valueLen);
            
            lb.offset += valueLen;
            session.size += valueLen;
            
            return lb;
        }
        
        final int size = (value < 0) ? stringSize(-value) + 1 : stringSize(value);
        
        if(lb.offset + size > lb.buffer.length)
        {
            // not enough size
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        }
        
        putBytesFromInt(value, size, lb.buffer);
        
        lb.offset += size;
        session.size += size;
        
        return lb;
    }
    
    /**
     * Encodes the long to utf8 bytes (like converting a long to a string) and is directly
     * written to the buffer.
     */
    public static LinkedBuffer writeUTF8FromLong(final long value, final WriteSession session, 
            LinkedBuffer lb)
    {
        if(value == Long.MIN_VALUE)
        {
            final int valueLen = LONG_MIN_VALUE.length;
            if(lb.offset + valueLen > lb.buffer.length)
            {
                // not enough size
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
            
            System.arraycopy(LONG_MIN_VALUE, 0, lb.buffer, lb.offset, valueLen);
            
            lb.offset += valueLen;
            session.size += valueLen;
            
            return lb;
        }
        
        final int size = (value < 0) ? stringSize(-value) + 1 : stringSize(value);
        
        if(lb.offset + size > lb.buffer.length)
        {
            // not enough size
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        }
        
        putBytesFromLong(value, size, lb.buffer);
        
        lb.offset += size;
        session.size += size;
        
        return lb;
    }
    
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
