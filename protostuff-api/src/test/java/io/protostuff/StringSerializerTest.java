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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import junit.framework.TestCase;
import io.protostuff.StringSerializer.STRING;

/**
 * Tests for UTF-8 Encoding
 *
 * @author David Yu
 * @created Jul 6, 2010
 */
public class StringSerializerTest extends TestCase
{

    // 4*3-byte
    static final String three_byte_utf8 = "\u1234\u8000\uF800\u0800";

    // 4*2-byte
    static final String two_byte_utf8 = "\u07FF\341\210\264";

    // 26 total
    static final String alphabet = "abcdefghijklmnopqrstuvwyxz";

    static final String alphabet_to_upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static final String surrogatePairs = "\uD83C\uDFE0\uD83C\uDF4E\uD83D\uDCA9";

    // Result of surrogatePairs.getBytes("UTF-8"), which is what protobuf uses.
    static final byte[] nativeSurrogatePairsSerialized = { -16, -97, -113, -96, -16, -97, -115, -114, -16, -97, -110,
            -87 };

    // Result of writeUTF8 before 3-byte surrogate fix (i.e. no 4-byte encoding)
    static final byte[] legacySurrogatePairSerialized = { -19, -96, -68, -19, -65, -96, -19, -96, -68, -19, -67, -114,
            -19, -96, -67, -19, -78, -87 };

    // 10 total
    static final String numeric = "0123456789";

    // 3 total
    static final String whitespace = "\r\n\t";

    // 71 total
    static final String foo = alphabet + three_byte_utf8 + numeric + two_byte_utf8 + whitespace + surrogatePairs;

    static final String str_len_130 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

    static final String[] targets = new String[] {
            three_byte_utf8,
            two_byte_utf8,
            alphabet,
            alphabet_to_upper,
            numeric,
            whitespace,
            foo,
            str_len_130,
            surrogatePairs,
            repeatChar('a', 0x800 - 16),
            repeatChar('a', 0x800 + 16),
            repeatChar('a', 0x8000 - 16),
            repeatChar('a', 0x8000 + 16)
    };

    static final String[] ascii_targets = new String[] {
            alphabet,
            alphabet_to_upper,
            numeric,
            whitespace,
            str_len_130,
            repeatChar('b', 0x800 - 16),
            repeatChar('b', 0x800 + 16),
            repeatChar('b', 0x8000 - 16),
            repeatChar('b', 0x8000 + 16)
    };

    static final int[] int_targets = new int[] {
            0,
            1,
            -1,
            10,
            -10,
            100,
            -100,
            1000,
            -1000,
            10001,
            -10001,
            1110001,
            -1110001,
            111110001,
            -111110001,
            1234567890,
            -1234567890,
            Integer.MAX_VALUE,
            Integer.MIN_VALUE,
    };

    static final long[] long_targets = new long[] {
            0l,
            1l,
            -1l,
            10l,
            -10l,
            100l,
            -100l,
            1000l,
            -1000l,
            10001l,
            -10001l,
            1110001l,
            -1110001l,
            111110001l,
            -111110001l,
            11111110001l,
            -11111110001l,
            1111111110001l,
            -1111111110001l,
            111111111110001l,
            -111111111110001l,
            11111111111110001l,
            -11111111111110001l,
            1234567890123456789l,
            -1234567890123456789l,
            Long.MAX_VALUE,
            Long.MIN_VALUE,
    };

    static final float[] float_targets = new float[] {
            0.0f,
            10.01f,
            -10.01f,
            1234.4321f
            - 1234.4321f,
            56789.98765f,
            -56789.98765f,
            Float.MAX_VALUE,
            Float.MIN_VALUE
    };

    static final double[] double_targets = new double[] {
            0.0d,
            10.01d,
            -10.01d,
            1234.4321d
            - 1234.4321d,
            56789.98765d,
            -56789.98765d,
            1234567890.0987654321d,
            -1234567890.0987654321d,
            Double.MAX_VALUE,
            Double.MIN_VALUE
    };

    public void testVarDelimitedBoundryTwoByte() throws Exception
    {
        int size = StringSerializer.THREE_BYTE_LOWER_LIMIT - 1; // takes 2 bytes for size and is larger than buffer

        checkVarDelimitedBoundry(1, size); // 1st str does not fit
        checkVarDelimitedBoundry(2, size); // 1st str fits
        checkVarDelimitedBoundry(3, size); // 2nd str varint doesn't fit
        checkVarDelimitedBoundry(4, size); // Only 2nd varint fits (slow)
    }

    public void testVarDelimitedBoundryThreeByte() throws Exception
    {
        int size = StringSerializer.FOUR_BYTE_LOWER_LIMIT - 1; // takes 3 bytes for size

        checkVarDelimitedBoundry(1, size); // 1st str does not fit
        checkVarDelimitedBoundry(2, size); // 1st str fits
        checkVarDelimitedBoundry(3, size); // 2nd str varint doesn't fit
        checkVarDelimitedBoundry(4, size); // same as above
        checkVarDelimitedBoundry(5, size); // Only 2nd varint fits (slow)
    }

    // 4-byte & 5-byte ommitted (256mb length string = eats 512mb memory as char is 16 bit)

    public static String repeatChar(char ch, int times)
    {
        StringBuilder sb = new StringBuilder(times);
        for (int i = 0; i < times; i++)
        {
            sb.append(ch);
        }
        return sb.toString();
    }

    public static void checkVarDelimitedBoundry(int initialGap, int secondWriteSize)
    {
        int bufferSize = 256;
        final LinkedBuffer lb = LinkedBuffer.allocate(bufferSize);
        WriteSession session = new WriteSession(lb, bufferSize);

        // Should fill up the buffer with initialGap byte(s) left
        StringSerializer.writeUTF8(repeatChar('a', bufferSize - initialGap), session, lb);

        // Write a string of length secondWriteSize that should be larger
        // than the next buffer size
        assertTrue(secondWriteSize > bufferSize);
        StringSerializer.writeUTF8VarDelimited(repeatChar('a', secondWriteSize), session, lb);
    }

    public void testInt() throws Exception
    {
        for (int i : int_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeInt(i, session, lb);

            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeInt(i, session2, lb2);

            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Integer.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
    }

    public void testLong() throws Exception
    {
        for (long i : long_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeLong(i, session, lb);

            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeLong(i, session2, lb2);

            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Long.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
    }

    public void testFloat() throws Exception
    {
        for (float i : float_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeFloat(i, session, lb);

            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeFloat(i, session2, lb2);

            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Float.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
    }

    public void testDouble() throws Exception
    {
        for (double i : double_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeDouble(i, session, lb);

            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeDouble(i, session2, lb2);

            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Double.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
    }

    public void testAscii() throws Exception
    {
        for (String s : ascii_targets)
            checkAscii(s);
    }

    public void testUTF8() throws Exception
    {
        for (String s : targets)
            check(s);

        check("");
        check(str_len_130);

        String lessThan2048 = str_len_130;
        for (int i = 0; i < 14; i++)
            lessThan2048 += str_len_130;

        int lt2048Len = 130 * 15;
        assertTrue(lessThan2048.length() == lt2048Len);
        check(lessThan2048);

        String moreThan2048 = str_len_130;
        for (int i = 0; i < 20; i++)
            moreThan2048 += str_len_130;

        int expectedLen = 130 * 21;
        assertTrue(moreThan2048.length() == expectedLen);

        check(moreThan2048);
    }

    public void testUTF8VarDelimited() throws Exception
    {
        checkVarDelimited(foo, 1, 71);
        checkVarDelimited(whitespace, 1, 3);
        checkVarDelimited(numeric, 1, 10);
        checkVarDelimited(alphabet, 1, 26);
        checkVarDelimited(alphabet_to_upper, 1, 26);
        checkVarDelimited(two_byte_utf8, 1, 4 * 2);
        checkVarDelimited(three_byte_utf8, 1, 4 * 3);
        checkVarDelimited("1234567890123456789012345678901234567890", 1, 40);
        checkVarDelimited("", 1, 0);
        checkVarDelimited(str_len_130, 2, 130);
        checkVarDelimited(str_len_130.substring(10), 1, 120);

        String lessThan2048 = str_len_130;
        for (int i = 0; i < 14; i++)
            lessThan2048 += str_len_130;

        int lt2048Len = 130 * 15;
        assertTrue(lessThan2048.length() == lt2048Len);
        checkVarDelimited(lessThan2048, 2, lt2048Len);

        String moreThan2048 = str_len_130;
        for (int i = 0; i < 20; i++)
            moreThan2048 += str_len_130;

        int expectedLen = 130 * 21;
        assertTrue(moreThan2048.length() == expectedLen);

        checkVarDelimited(moreThan2048, 2, expectedLen);

        String str16383 = repeatChar('z', 16383);
        String str16384 = str16383 + "g";

        checkVarDelimited(str16383, 2, str16383.length());
        checkVarDelimited(str16384, 3, str16384.length());
    }

    public void testUTF8FixedDelimited() throws Exception
    {
        for (String s : targets)
            checkFixedDelimited(s);

        checkFixedDelimited("1234567890123456789012345678901234567890");
        checkFixedDelimited("");
        checkFixedDelimited(str_len_130);

        String lessThan2048 = str_len_130;
        for (int i = 0; i < 14; i++)
            lessThan2048 += str_len_130;

        int lt2048Len = 130 * 15;
        assertTrue(lessThan2048.length() == lt2048Len);
        checkFixedDelimited(lessThan2048);

        String moreThan2048 = str_len_130;
        for (int i = 0; i < 20; i++)
            moreThan2048 += str_len_130;

        int expectedLen = 130 * 21;
        assertTrue(moreThan2048.length() == expectedLen);

        checkFixedDelimited(moreThan2048);
    }

    public void testLegacySurrogatePairs() throws Exception
    {
        LinkedBuffer lb = new LinkedBuffer(256);
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8(surrogatePairs, session, lb);

        byte[] buffered = session.toByteArray();

        // By default, STRING.deser should not use the custom
        // decoder (specifically, should not check the decoded
        // string for REPLACE characters). This means that legacy
        // encoded Strings with surrogate pairs will result in
        // malformed Strings
        assertNotSame(surrogatePairs, STRING.deser(legacySurrogatePairSerialized));

        // New Strings, however, should be deserialized fine
        assertEquals(surrogatePairs, STRING.deser(buffered));
    }

    public void testSurrogatePairsCustomOnly() throws Exception
    {
        // This test is mainly for Java 8+, where 3-byte surrogates
        // and 6-byte surrogate pairs are not allowed.
        LinkedBuffer lb = new LinkedBuffer(256);
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8(surrogatePairs, session, lb);

        byte[] fastPathBuffered = session.toByteArray();

        lb = new LinkedBuffer(1);
        session = new WriteSession(lb);
        StringSerializer.writeUTF8(surrogatePairs, session, lb);

        byte[] slowPathBuffered = session.toByteArray();

        // Output of fast path and slow path should be identical.
        assertTrue(Arrays.equals(fastPathBuffered, slowPathBuffered));

        // Does our own serialization match native?
        assertTrue(Arrays.equals(fastPathBuffered, nativeSurrogatePairsSerialized));

        // Does our own serialization / deserialization work?
        assertEquals(surrogatePairs, STRING.deserCustomOnly(fastPathBuffered));

        // Can we decode legacy encodings?
        assertEquals(surrogatePairs, STRING.deserCustomOnly(legacySurrogatePairSerialized));

        // Does the built in serialization work?
        assertEquals(surrogatePairs, new String(nativeSurrogatePairsSerialized, "UTF-8"));

        // Should be encoded using 4-byte code points, instead of 6-byte surrogate pairs.
        assertFalse(Arrays.equals(fastPathBuffered, legacySurrogatePairSerialized));

        try
        {
            // Can we deserialize from a protobuf source (specifically java generated)
            // using our first method?
            assertEquals(surrogatePairs, STRING.deserCustomOnly(nativeSurrogatePairsSerialized));
        }
        catch (RuntimeException ex)
        {
            // No? Fallback should catch this.
            assertEquals(surrogatePairs, STRING.deser(nativeSurrogatePairsSerialized));

            // But it means there's a bug in the deserializer
            fail("Deserializer should not have used built in decoder.");
        }
    }

    public void testPartialSurrogatePair() throws Exception
    {
        // Make sure that we don't overflow or get out of bounds,
        // since pairs require 2 characters.
        String partial = "\uD83C";

        LinkedBuffer lb = new LinkedBuffer(256);
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8(partial, session, lb);

        byte[] buffered = session.toByteArray();

        // Force the use of 'slow' path
        lb = new LinkedBuffer(1);
        session = new WriteSession(lb);
        StringSerializer.writeUTF8(partial, session, lb);

        buffered = session.toByteArray();

    }

    public void testDataInputStreamDecoding() throws Exception
    {
        // Unfortuneatley, DataInputStream uses Modified UTF-8,
        // which does not support 4-byte characters, as used in the
        // 'Standard UTF-8'. This is a sacrifice of generating
        // standard conformant UTF-8 Strings.
        //
        // Note: this only happens for surrogate pairs (e.g. emoji's)
        LinkedBuffer lb = new LinkedBuffer(256);
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8FixedDelimited(surrogatePairs, session, lb);

        byte[] buffered = session.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(buffered);
        DataInputStream din = new DataInputStream(in);

        try
        {
            String dinResult = din.readUTF();
            fail();
        }
        catch (IOException ex)
        {
            // Decoding failed of 4-byte format.
        }
    }

    public void testReallyLongString() throws Exception
    {
        LinkedBuffer lb = new LinkedBuffer(256);
        WriteSession session = new WriteSession(lb);

        // The motivation of this test is to make sure
        // that the serializer/deserializer can handle very large Strings.
        // DataInputStream only supports Strings up to Short.MAX_VALUE,
        // so we should make sure our implementation can support more than that.
        //
        // Ideally, we'd like to test all the way up to Integer.MAX_VALUE, but that
        // may be unfeasable in many test environments, so we will just do 3 * Short.MAX_VALUE,
        // which would overflow an unsigned short.

        StringBuilder sb = new StringBuilder(3 * Short.MAX_VALUE);
        for (int i = 0; i < 3 * Short.MAX_VALUE; i++)
        {
            sb.append(i % 10);
        }
        String bigString = sb.toString();

        StringSerializer.writeUTF8(bigString, session, lb);

        byte[] buffered = session.toByteArray();

        // We want to make sure it's our implementation
        // that can handle the large string
        assertEquals(bigString, STRING.deserCustomOnly(buffered));
    }

    static void checkVarDelimited(String str, int size, int stringLen) throws Exception
    {
        LinkedBuffer lb = new LinkedBuffer(512);
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8VarDelimited(str, session, lb);

        byte[] buf = session.toByteArray();

        assertTrue(buf.length == stringLen + size);

        int len = readRawVarint32(lb.buffer, 0);
        assertTrue(len == stringLen);

        print("total len: " + buf.length);
    }

    static void checkFixedDelimited(String str) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(getShortStringLengthInBytes(str));
        OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
        writer.write(str, 0, str.length());
        writer.close();

        LinkedBuffer lb = new LinkedBuffer(512);
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8FixedDelimited(str, session, lb);

        byte[] b1 = out.toByteArray();
        byte[] b2 = session.toByteArray();

        assertEquals(b1, b2);
    }

    static byte[] getShortStringLengthInBytes(String str) throws Exception
    {
        byte[] array = str.getBytes("UTF-8");
        return new byte[] { (byte) ((array.length >> 8) & 0xff), (byte) (array.length & 0xff) };
    }

    static void assertEquals(byte[] b1, byte[] b2) throws Exception
    {
        assertTrue(Arrays.equals(b1, b2));
    }

    static void checkAscii(String str) throws Exception
    {
        byte[] builtin = str.getBytes("UTF-8");
        LinkedBuffer lb = new LinkedBuffer(512);
        WriteSession session = new WriteSession(lb);

        StringSerializer.writeAscii(str, session, lb);

        assertTrue(builtin.length == session.size);

        byte[] buffered = session.toByteArray();

        assertTrue(builtin.length == buffered.length);

        String strBuiltin = new String(builtin, "ASCII");
        String strBuffered = new String(buffered, "ASCII");

        assertEquals(strBuiltin, strBuffered);
        print(strBuiltin);
        print("len: " + builtin.length);
    }

    static void check(String str) throws Exception
    {
        byte[] builtin = str.getBytes("UTF-8");
        LinkedBuffer lb = new LinkedBuffer(512);
        WriteSession session = new WriteSession(lb);

        StringSerializer.writeUTF8(str, session, lb);

        assertTrue(builtin.length == session.size);

        byte[] buffered = session.toByteArray();

        assertTrue(builtin.length == buffered.length);

        String strBuiltin = new String(builtin, "UTF-8");
        String strBuffered = new String(buffered, "UTF-8");

        assertEquals(strBuiltin, strBuffered);
        print(strBuiltin);
        print("len: " + builtin.length);
    }

    static void print(String msg)
    {
        // System.err.println(msg);
    }

    /**
     * Reads a var int 32 from the buffer.
     */
    static int readRawVarint32(final byte[] buffer, int offset) throws IOException
    {
        byte tmp = buffer[offset++];
        if (tmp >= 0)
        {
            return tmp;
        }
        int result = tmp & 0x7f;
        if ((tmp = buffer[offset++]) >= 0)
        {
            result |= tmp << 7;
        }
        else
        {
            result |= (tmp & 0x7f) << 7;
            if ((tmp = buffer[offset++]) >= 0)
            {
                result |= tmp << 14;
            }
            else
            {
                result |= (tmp & 0x7f) << 14;
                if ((tmp = buffer[offset++]) >= 0)
                {
                    result |= tmp << 21;
                }
                else
                {
                    result |= (tmp & 0x7f) << 21;
                    result |= (tmp = buffer[offset++]) << 28;
                    if (tmp < 0)
                    {
                        // Discard upper 32 bits.
                        for (int i = 0; i < 5; i++)
                        {
                            if (buffer[offset++] >= 0)
                            {
                                return result;
                            }
                        }
                        throw new RuntimeException("Malformed varint.");
                    }
                }
            }
        }
        return result;
    }

}
