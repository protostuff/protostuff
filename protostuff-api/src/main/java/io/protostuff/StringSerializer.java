package io.protostuff;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;

import static java.lang.Character.MIN_HIGH_SURROGATE;
import static java.lang.Character.MIN_LOW_SURROGATE;
import static java.lang.Character.MIN_SUPPLEMENTARY_CODE_POINT;

/**
 * UTF-8 String serialization
 *
 * @author David Yu
 * @created Feb 4, 2010
 */
public final class StringSerializer
{

    private StringSerializer()
    {
    }

    /**
     * From {@link java.lang.Integer#toString(int)}
     */
    static final int[] sizeTable = new int[] {
            9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE
    };

    static final char[] DigitTens = {
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

    static final char[] DigitOnes = {
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

    static final char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    static final byte[] INT_MIN_VALUE = new byte[] {
            (byte) '-',
            (byte) '2',
            (byte) '1', (byte) '4', (byte) '7',
            (byte) '4', (byte) '8', (byte) '3',
            (byte) '6', (byte) '4', (byte) '8'
    };

    static final byte[] LONG_MIN_VALUE = new byte[] {
            (byte) '-',
            (byte) '9',
            (byte) '2', (byte) '2', (byte) '3',
            (byte) '3', (byte) '7', (byte) '2',
            (byte) '0', (byte) '3', (byte) '6',
            (byte) '8', (byte) '5', (byte) '4',
            (byte) '7', (byte) '7', (byte) '5',
            (byte) '8', (byte) '0', (byte) '8'
    };

    static final int TWO_BYTE_LOWER_LIMIT = 1 << 7;

    static final int ONE_BYTE_EXCLUSIVE = TWO_BYTE_LOWER_LIMIT / 3 + 1;

    static final int THREE_BYTE_LOWER_LIMIT = 1 << 14;

    static final int TWO_BYTE_EXCLUSIVE = THREE_BYTE_LOWER_LIMIT / 3 + 1;

    static final int FOUR_BYTE_LOWER_LIMIT = 1 << 21;

    static final int THREE_BYTE_EXCLUSIVE = FOUR_BYTE_LOWER_LIMIT / 3 + 1;

    static final int FIVE_BYTE_LOWER_LIMIT = 1 << 28;

    static final int FOUR_BYTE_EXCLUSIVE = FIVE_BYTE_LOWER_LIMIT / 3 + 1;

    static void putBytesFromInt(int i, final int offset, final int size, final byte[] buf)
    {
        int q, r;
        int charPos = offset + size;
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
            buf[--charPos] = (byte) DigitOnes[r];
            buf[--charPos] = (byte) DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (;;)
        {
            q = (i * 52429) >>> (16 + 3);
            r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
            buf[--charPos] = (byte) digits[r];
            i = q;
            if (i == 0)
                break;
        }
        if (sign != 0)
        {
            buf[--charPos] = (byte) sign;
        }
    }

    static void putBytesFromLong(long i, final int offset, int size, final byte[] buf)
    {
        long q;
        int r;
        int charPos = offset + size;
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
            r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = (byte) DigitOnes[r];
            buf[--charPos] = (byte) DigitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int) i;
        while (i2 >= 65536)
        {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = (byte) DigitOnes[r];
            buf[--charPos] = (byte) DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (;;)
        {
            q2 = (i2 * 52429) >>> (16 + 3);
            r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
            buf[--charPos] = (byte) digits[r];
            i2 = q2;
            if (i2 == 0)
                break;
        }
        if (sign != 0)
        {
            buf[--charPos] = (byte) sign;
        }
    }

    // Requires positive x
    static int stringSize(int x)
    {
        for (int i = 0;; i++)
        {
            if (x <= sizeTable[i])
                return i + 1;
        }
    }

    // Requires positive x
    static int stringSize(long x)
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
     * Writes the stringified int into the {@link LinkedBuffer}.
     */
    public static LinkedBuffer writeInt(final int value, final WriteSession session,
            LinkedBuffer lb)
    {
        if (value == Integer.MIN_VALUE)
        {
            final int valueLen = INT_MIN_VALUE.length;
            if (lb.offset + valueLen > lb.buffer.length)
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

        if (lb.offset + size > lb.buffer.length)
        {
            // not enough size
            lb = new LinkedBuffer(session.nextBufferSize, lb);
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
            LinkedBuffer lb)
    {
        if (value == Long.MIN_VALUE)
        {
            final int valueLen = LONG_MIN_VALUE.length;
            if (lb.offset + valueLen > lb.buffer.length)
            {
                // TODO space efficiency (slower path)
                // not enough size
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }

            System.arraycopy(LONG_MIN_VALUE, 0, lb.buffer, lb.offset, valueLen);

            lb.offset += valueLen;
            session.size += valueLen;

            return lb;
        }

        final int size = (value < 0) ? stringSize(-value) + 1 : stringSize(value);

        if (lb.offset + size > lb.buffer.length)
        {
            // TODO space efficiency (slower path)
            // not enough size
            lb = new LinkedBuffer(session.nextBufferSize, lb);
        }

        putBytesFromLong(value, lb.offset, size, lb.buffer);

        lb.offset += size;
        session.size += size;

        return lb;
    }

    /**
     * Writes the stringified float into the {@link LinkedBuffer}. TODO - skip string conversion and write directly to
     * buffer
     */
    public static LinkedBuffer writeFloat(final float value, final WriteSession session,
            final LinkedBuffer lb)
    {
        return writeAscii(Float.toString(value), session, lb);
    }

    /**
     * Writes the stringified double into the {@link LinkedBuffer}. TODO - skip string conversion and write directly to
     * buffer
     */
    public static LinkedBuffer writeDouble(final double value, final WriteSession session,
            final LinkedBuffer lb)
    {
        return writeAscii(Double.toString(value), session, lb);
    }

    /**
     * Computes the size of the utf8 string beginning at the specified {@code index} with the specified {@code length}.
     */
    public static int computeUTF8Size(final String str, final int index, final int len)
    {
        int size = len;
        for (int i = index; i < len; i++)
        {
            final char c = str.charAt(i);
            if (c < 0x0080)
                continue;

            if (c < 0x0800)
                size++;
            else
                size += 2;
        }
        return size;
    }

    /**
     * Slow path. It checks the limit before every write. Shared with StreamedStringSerializer.
     */
    static LinkedBuffer writeUTF8(final String str, int i, final int len,
            byte[] buffer, int offset, int limit,
            final WriteSession session, LinkedBuffer lb)
    {
        for (char c = 0;; c = 0)
        {
            while (i != len && offset != limit && (c = str.charAt(i++)) < 0x0080)
                buffer[offset++] = (byte) c;

            if (i == len && c < 0x0080)
            {
                session.size += (offset - lb.offset);
                lb.offset = offset;
                return lb;
            }

            if (offset == limit)
            {
                // we are done with this LinkedBuffer
                session.size += (offset - lb.offset);
                lb.offset = offset;

                if (lb.next == null)
                {
                    // reset
                    offset = 0;
                    limit = session.nextBufferSize;
                    buffer = new byte[limit];
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
                }
                else
                {
                    // use the existing buffer from previous utf8 write.
                    // this condition happens only on streaming mode
                    lb = lb.next;
                    // reset
                    lb.offset = offset = lb.start;
                    buffer = lb.buffer;
                    limit = buffer.length;
                }

                continue;
            }

            if (c < 0x0800)
            {
                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0xC0 | ((c >> 6) & 0x1F));

                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
            else if (Character.isHighSurrogate((char) c) && i < len && Character.isLowSurrogate((char) str.charAt(i)))
            {
                // We have a surrogate pair, so use the 4-byte encoding.
                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                int codePoint = Character.toCodePoint((char) c, (char) str.charAt(i));

                buffer[offset++] = (byte) (0xF0 | ((codePoint >> 18) & 0x07));

                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0x80 | ((codePoint >> 12) & 0x3F));

                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0x80 | ((codePoint >> 6) & 0x3F));

                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0x80 | ((codePoint >> 0) & 0x3F));

                i++;
            }
            else
            {
                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0xE0 | ((c >> 12) & 0x0F));

                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0x80 | ((c >> 6) & 0x3F));

                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    session.size += (offset - lb.offset);
                    lb.offset = offset;

                    if (lb.next == null)
                    {
                        // reset
                        offset = 0;
                        limit = session.nextBufferSize;
                        buffer = new byte[limit];
                        // grow
                        lb = new LinkedBuffer(buffer, 0, lb);
                    }
                    else
                    {
                        // use the existing buffer from previous utf8 write.
                        // this condition happens only on streaming mode
                        lb = lb.next;
                        // reset
                        lb.offset = offset = lb.start;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                }

                buffer[offset++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
    }

    /**
     * Fast path. The {@link LinkedBuffer}'s capacity is >= string length.
     */
    static LinkedBuffer writeUTF8(final String str, int i, final int len,
            final WriteSession session, final LinkedBuffer lb)
    {
        final byte[] buffer = lb.buffer;
        for (int c = 0, offset = lb.offset, adjustableLimit = offset + len;; c = 0)
        {
            while (i != len && (c = str.charAt(i++)) < 0x0080)
                buffer[offset++] = (byte) c;

            if (i == len && c < 0x0080)
            {
                session.size += (offset - lb.offset);
                lb.offset = offset;
                return lb;
            }

            if (c < 0x0800)
            {
                if (++adjustableLimit > buffer.length)
                {
                    session.size += (offset - lb.offset);
                    lb.offset = offset;
                    return writeUTF8(str, i - 1, len, buffer, offset, buffer.length, session, lb);
                }

                buffer[offset++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                buffer[offset++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
            else if (Character.isHighSurrogate((char) c) && i < len && Character.isLowSurrogate((char) str.charAt(i)))
            {
                // We have a surrogate pair, so use the 4-byte encoding.
                adjustableLimit += 3;
                if (adjustableLimit > buffer.length)
                {
                    session.size += (offset - lb.offset);
                    lb.offset = offset;
                    return writeUTF8(str, i - 1, len, buffer, offset, buffer.length, session, lb);
                }

                int codePoint = Character.toCodePoint((char) c, (char) str.charAt(i));
                buffer[offset++] = (byte) (0xF0 | ((codePoint >> 18) & 0x07));
                buffer[offset++] = (byte) (0x80 | ((codePoint >> 12) & 0x3F));
                buffer[offset++] = (byte) (0x80 | ((codePoint >> 6) & 0x3F));
                buffer[offset++] = (byte) (0x80 | ((codePoint >> 0) & 0x3F));

                i++;
            }
            else
            {
                adjustableLimit += 2;
                if (adjustableLimit > buffer.length)
                {
                    session.size += (offset - lb.offset);
                    lb.offset = offset;
                    return writeUTF8(str, i - 1, len, buffer, offset, buffer.length, session, lb);
                }

                buffer[offset++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                buffer[offset++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                buffer[offset++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
    }

    /**
     * Writes the utf8-encoded bytes from the string into the {@link LinkedBuffer}.
     */
    public static LinkedBuffer writeUTF8(final String str, final WriteSession session,
            final LinkedBuffer lb)
    {
        final int len = str.length();
        if (len == 0)
            return lb;

        return lb.offset + len > lb.buffer.length ? writeUTF8(str, 0, len, lb.buffer, lb.offset,
                lb.buffer.length, session, lb) : writeUTF8(str, 0, len, session, lb);
    }

    /**
     * Writes the ascii bytes from the string into the {@link LinkedBuffer}. It is the responsibility of the caller to
     * know in advance that the string is 100% ascii. E.g if you convert a double/float to a string, you are sure it
     * only contains ascii chars.
     */
    public static LinkedBuffer writeAscii(final String str, final WriteSession session,
            LinkedBuffer lb)
    {
        final int len = str.length();
        if (len == 0)
            return lb;

        byte[] buffer = lb.buffer;
        int offset = lb.offset, limit = lb.buffer.length;

        // actual size
        session.size += len;

        if (offset + len > limit)
        {
            // slow path
            for (int i = 0; i < len; i++)
            {
                if (offset == limit)
                {
                    // we are done with this LinkedBuffer
                    lb.offset = offset;
                    // reset
                    offset = 0;
                    limit = session.nextBufferSize;
                    buffer = new byte[limit];
                    // grow
                    lb = new LinkedBuffer(buffer, 0, lb);
                }
                buffer[offset++] = (byte) str.charAt(i);
            }
        }
        else
        {
            // fast path
            for (int i = 0; i < len; i++)
                buffer[offset++] = (byte) str.charAt(i);
        }

        lb.offset = offset;

        return lb;
    }

    static void writeFixed2ByteInt(final int value, final byte[] buffer, int offset,
            final boolean littleEndian)
    {
        if (littleEndian)
        {
            buffer[offset++] = (byte) value;
            buffer[offset] = (byte) ((value >>> 8) & 0xFF);
        }
        else
        {
            buffer[offset++] = (byte) ((value >>> 8) & 0xFF);
            buffer[offset] = (byte) value;
        }
    }

    /**
     * The length of the utf8 bytes is written first (big endian) before the string - which is fixed 2-bytes. Same
     * behavior as {@link java.io.DataOutputStream#writeUTF(String)}.
     */
    public static LinkedBuffer writeUTF8FixedDelimited(final String str,
            final WriteSession session, LinkedBuffer lb)
    {
        return writeUTF8FixedDelimited(str, false, session, lb);
    }

    /**
     * The length of the utf8 bytes is written first before the string - which is fixed 2-bytes.
     */
    public static LinkedBuffer writeUTF8FixedDelimited(final String str,
            final boolean littleEndian, final WriteSession session, LinkedBuffer lb)
    {
        final int lastSize = session.size, len = str.length(), withIntOffset = lb.offset + 2;

        if (withIntOffset > lb.buffer.length)
        {
            // not enough space for int (2 bytes).
            // create a new buffer.
            lb = new LinkedBuffer(len + 2 > session.nextBufferSize ? len + 2 : session.nextBufferSize,
                    lb);

            lb.offset = 2;

            if (len == 0)
            {
                writeFixed2ByteInt(0, lb.buffer, 0, littleEndian);
                // update size
                session.size += 2;
                return lb;
            }

            // fast path
            final LinkedBuffer rb = writeUTF8(str, 0, len, session, lb);

            writeFixed2ByteInt((session.size - lastSize), lb.buffer, 0, littleEndian);

            // update size
            session.size += 2;

            return rb;
        }

        if (len == 0)
        {
            writeFixed2ByteInt(0, lb.buffer, lb.offset, littleEndian);
            lb.offset = withIntOffset;
            // update size
            session.size += 2;
            return lb;
        }

        if (withIntOffset + len > lb.buffer.length)
        {
            // not enough space for the string.
            lb.offset = withIntOffset;

            // slow path
            final LinkedBuffer rb = writeUTF8(str, 0, len,
                    lb.buffer, withIntOffset, lb.buffer.length, session, lb);

            writeFixed2ByteInt((session.size - lastSize), lb.buffer,
                    withIntOffset - 2, littleEndian);

            // update size
            session.size += 2;

            return rb;
        }

        // everything fits
        lb.offset = withIntOffset;

        final LinkedBuffer rb = writeUTF8(str, 0, len, session, lb);

        writeFixed2ByteInt((session.size - lastSize), lb.buffer,
                withIntOffset - 2, littleEndian);

        // update size
        session.size += 2;

        return rb;
    }

    private static LinkedBuffer writeUTF8OneByteDelimited(final String str, final int index,
            final int len, final WriteSession session, LinkedBuffer lb)
    {
        final int lastSize = session.size;

        if (lb.offset == lb.buffer.length)
        {
            // create a new buffer.
            lb = new LinkedBuffer(len + 1 > session.nextBufferSize ? len + 1 : session.nextBufferSize,
                    lb);

            lb.offset = 1;

            // fast path
            final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);

            lb.buffer[0] = (byte) (session.size - lastSize);

            // update size
            session.size++;

            return rb;
        }

        final int withIntOffset = lb.offset + 1;
        if (withIntOffset + len > lb.buffer.length)
        {
            // not enough space for the string.
            lb.offset = withIntOffset;

            final byte[] buffer = lb.buffer;

            // slow path
            final LinkedBuffer rb = writeUTF8(str, index, len, buffer, withIntOffset, buffer.length,
                    session, lb);

            buffer[withIntOffset - 1] = (byte) (session.size - lastSize);

            // update size
            session.size++;

            return rb;
        }

        // everything fits
        lb.offset = withIntOffset;

        final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);

        lb.buffer[withIntOffset - 1] = (byte) (session.size - lastSize);

        // update size
        session.size++;

        return rb;
    }

    private static LinkedBuffer writeUTF8VarDelimited(final String str, final int index,
            final int len, final int lowerLimit, int expectedSize,
            final WriteSession session, LinkedBuffer lb)
    {
        int lastSize = session.size, offset = lb.offset, withIntOffset = offset + expectedSize;

        if (withIntOffset > lb.buffer.length)
        {
            // not enough space for the varint.
            // create a new buffer.
            lb = new LinkedBuffer(len + expectedSize > session.nextBufferSize ? len + expectedSize
                    : session.nextBufferSize,
                    lb);
            offset = lb.start;
            lb.offset = withIntOffset = offset + expectedSize;

            // fast path
            final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);

            int size = session.size - lastSize;

            if (size < lowerLimit)
            {
                // move one space to the left since the varint is 1-byte smaller
                System.arraycopy(lb.buffer, withIntOffset, lb.buffer, withIntOffset - 1,
                        lb.offset - withIntOffset);

                expectedSize--;
                lb.offset--;
            }

            // update size
            session.size += expectedSize;

            for (; --expectedSize > 0; size >>>= 7)
                lb.buffer[offset++] = (byte) ((size & 0x7F) | 0x80);

            lb.buffer[offset] = (byte) (size);

            return rb;
        }

        if (withIntOffset + len > lb.buffer.length)
        {
            // not enough space for the string.
            lb.offset = withIntOffset;

            // slow path
            final LinkedBuffer rb = writeUTF8(str, index, len,
                    lb.buffer, withIntOffset, lb.buffer.length, session, lb);

            int size = session.size - lastSize;

            if (size < lowerLimit)
            {
                // move one space to the left since the varint is 1-byte smaller
                System.arraycopy(lb.buffer, withIntOffset, lb.buffer, withIntOffset - 1,
                        lb.offset - withIntOffset);

                expectedSize--;
                lb.offset--;
            }

            // update size
            session.size += expectedSize;

            for (; --expectedSize > 0; size >>>= 7)
                lb.buffer[offset++] = (byte) ((size & 0x7F) | 0x80);

            lb.buffer[offset] = (byte) (size);

            return rb;
        }

        // everything fits
        lb.offset = withIntOffset;

        final LinkedBuffer rb = writeUTF8(str, index, len, session, lb);

        int size = session.size - lastSize;

        if (size < lowerLimit)
        {
            // move one space to the left since the varint is 1-byte smaller
            System.arraycopy(lb.buffer, withIntOffset, lb.buffer, withIntOffset - 1,
                    lb.offset - withIntOffset);

            expectedSize--;
            lb.offset--;
        }

        // update size
        session.size += expectedSize;

        for (; --expectedSize > 0; size >>>= 7)
            lb.buffer[offset++] = (byte) ((size & 0x7F) | 0x80);

        lb.buffer[offset] = (byte) (size);

        return rb;
    }

    /**
     * The length of the utf8 bytes is written first before the string - which is a variable int (1 to 5 bytes).
     */
    public static LinkedBuffer writeUTF8VarDelimited(final String str, final WriteSession session,
            LinkedBuffer lb)
    {
        final int len = str.length();
        if (len == 0)
        {
            if (lb.offset == lb.buffer.length)
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

        if (len < ONE_BYTE_EXCLUSIVE)
        {
            // the varint will be max 1-byte. (even if all chars are non-ascii)
            return writeUTF8OneByteDelimited(str, 0, len, session, lb);
        }

        if (len < TWO_BYTE_EXCLUSIVE)
        {
            // the varint will be max 2-bytes and could be 1-byte. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, TWO_BYTE_LOWER_LIMIT, 2,
                    session, lb);
        }

        if (len < THREE_BYTE_EXCLUSIVE)
        {
            // the varint will be max 3-bytes and could be 2-bytes. (even if all non-ascii)
            return writeUTF8VarDelimited(str, 0, len, THREE_BYTE_LOWER_LIMIT, 3,
                    session, lb);
        }

        if (len < FOUR_BYTE_EXCLUSIVE)
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
        static final boolean CESU8_COMPAT = Boolean.getBoolean("io.protostuff.cesu8_compat");

        private STRING()
        {
        }

        public static String deser(byte[] nonNullValue)
        {
            return deser(nonNullValue, 0, nonNullValue.length);
        }

        public static String deser(byte[] nonNullValue, int offset, int len)
        {
            final String result;
            try
            {
                // Try to use the built in deserialization first, since we expect
                // that the most likely case is a valid UTF-8 encoded byte array.
                // Additionally, the built in serialization method has one less
                // char[] copy than readUTF.
                //
                // If, however, there are invalid/malformed characters, i.e. 3-byte
                // surrogates / 3-byte surrogate pairs, we should fall back to the
                // readUTF method as it should be able to properly handle 3-byte surrogates
                // (and therefore 6-byte surrogate pairs) in Java 8+.
                //
                // While Protostuff and many other applications, still use 3-byte surrogates
                // / 6-byte surrogate pairs, the standard 'forbids' their use, and Java 8
                // has started to enforce the standard, resulting in 'corrupted' data in
                // strings when decoding using new String(nonNullValue, "UTF-8");
                //
                // While the readUTF should be able to handle both Standard Unicode
                // (i.e. new String().getBytes("UTF-8") and the Legacy Unicode
                // (with 3-byte surrogates, used in CESU-8 and Modified UTF-8),
                // we don't want to introduce an unexpected loss of data due to
                // some unforseen bug. As a result, a fallback mechanism is in
                // place such that the worst case scenario results in the previous
                // implementation's behaviour.
                //
                // For the Java 8 change, see: https://bugs.openjdk.java.net/browse/JDK-7096080

                result = new String(nonNullValue, offset, len, "UTF-8");

                // Check if we should scan the string to make sure there were no
                // corrupt characters caused by 3-byte / 6-byte surrogate pairs.
                //
                // In general, this *should* only be required for systems reading
                // data stored using legacy protostuff. Moving forward, the data
                // should be readable by new String("UTF-8"), so the scan is unnecessary.
                if (CESU8_COMPAT)
                {
                    // If it contains the REPLACEMENT character, then there's a strong
                    // possibility of it containing 3-byte surrogates / 6-byte surrogate
                    // pairs, and we should try decoding using readUTF to handle it.
                    if (result.indexOf(0xfffd) != -1)
                    {
                        try
                        {
                            return readUTF(nonNullValue, offset, len);
                        }
                        catch (UTFDataFormatException e)
                        {
                            // Unexpected, but most systems previously using
                            // Protostuff don't expect error to occur from
                            // String deserialization, so we use this just in case.
                            return result;
                        }
                    }
                }
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }

            return result;
        }

        /**
         * Deserialize using readUTF only.
         * 
         * @param nonNullValue
         * @return
         */
        static String deserCustomOnly(byte[] nonNullValue)
        {
            try
            {
                // Same behaviour as deser(), but does NOT
                // fall back to old implementation.
                return readUTF(nonNullValue, 0, nonNullValue.length);
            }
            catch (UTFDataFormatException e)
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

        /**
         * Reads the string from a byte[] using that was encoded a using Modified UTF-8 format. Additionally supports
         * 4-byte surrogates, de-serializing them as surrogate pairs.
         *
         * See: http://en.wikipedia.org/wiki/UTF-8#Description for encoding details.
         */
        private static String readUTF(byte[] buffer, int offset, int len) throws UTFDataFormatException
        {
            char[] charArray = new char[len];

            int i = 0;
            int c = 0;

            // Optimizaiton: Assume that the characters are all 7-bits encodable
            // (which is most likely the standard case).
            // If they're not, break out and take the 'slow' path.
            for (; i < len; i++)
            {
                int ch = (int) buffer[offset + i] & 0xff;

                // If it's not 7-bit character, break out
                if (ch > 127)
                    break;

                charArray[c++] = (char) ch;
            }

            // 'Slow' path
            while (i < len)
            {
                int ch = (int) buffer[offset + i] & 0xff;

                // Determine how to decode based on 'bits of code point'
                // See: http://en.wikipedia.org/wiki/UTF-8#Description
                int upperBits = ch >> 4;

                if (upperBits <= 7)
                {
                    // 1-byte: 0xxxxxxx
                    charArray[c++] = (char) ch;
                    i++;
                }
                else if (upperBits == 0x0C || upperBits == 0x0D)
                {
                    // 2-byte: 110xxxxx 10xxxxxx
                    i += 2;

                    if (i > len)
                        throw new UTFDataFormatException("Malformed input: Partial character at end");

                    int ch2 = (int) buffer[offset + i - 1];

                    // Make sure the second byte has the form 10xxxxxx
                    if ((ch2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException("Malformed input around byte " + i);

                    charArray[c++] = (char) (((ch & 0x1F) << 6) | (ch2 & 0x3F));
                }
                else if (upperBits == 0xE)
                {
                    // 3-byte: 1110xxxx 10xxxxxx 10xxxxxx
                    i += 3;

                    if (i > len)
                        throw new UTFDataFormatException("Malformed input: Partial character at end");

                    int ch2 = (int) buffer[offset + i - 2];
                    int ch3 = (int) buffer[offset + i - 1];

                    // Check the 10xxxxxx 10xxxxxx of second two bytes
                    if (((ch2 & 0xC0) != 0x80) || ((ch3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException("Malformed input around byte " + (i - 1));

                    charArray[c++] = (char) (((ch & 0x0F) << 12) | ((ch2 & 0x3F) << 6) | (ch3 & 0x3F));
                }
                else
                {
                    // 4-byte: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                    upperBits = ch >> 3;
                    if (upperBits == 0x1E)
                    {
                        // Because we're now in the UTF-32 bit range, we must
                        // break it down into the UTF-16 surrogate pairs for
                        // Java's String class (which is UTF-16).

                        i += 4;
                        if (i > len)
                            throw new UTFDataFormatException("Malformed input: Partial character at end");

                        int ch2 = (int) buffer[offset + i - 3];
                        int ch3 = (int) buffer[offset + i - 2];
                        int ch4 = (int) buffer[offset + i - 1];

                        int value =
                                ((ch & 0x07) << 18) |
                                        ((ch2 & 0x3F) << 12) |
                                        ((ch3 & 0x3F) << 6) |
                                        ((ch4 & 0x3F));

                        charArray[c++] = highSurrogate(value);
                        charArray[c++] = lowSurrogate(value);
                    }
                    else
                    {
                        // Anything above
                        throw new UTFDataFormatException("Malformed input at byte " + i);
                    }
                }
            }

            return new String(charArray, 0, c);
        }
    }


    public static char highSurrogate(int codePoint) {
        return (char) ((codePoint >>> 10)
                + (MIN_HIGH_SURROGATE - (MIN_SUPPLEMENTARY_CODE_POINT >>> 10)));
    }

    public static char lowSurrogate(int codePoint) {
        return (char) ((codePoint & 0x3ff) + MIN_LOW_SURROGATE);
    }

}
