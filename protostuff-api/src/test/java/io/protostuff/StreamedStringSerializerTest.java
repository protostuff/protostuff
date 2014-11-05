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

import static io.protostuff.StringSerializerTest.BUILT_IN_SERIALIZER;
import static io.protostuff.StringSerializerTest.alphabet;
import static io.protostuff.StringSerializerTest.alphabet_to_upper;
import static io.protostuff.StringSerializerTest.ascii_targets;
import static io.protostuff.StringSerializerTest.double_targets;
import static io.protostuff.StringSerializerTest.float_targets;
import static io.protostuff.StringSerializerTest.foo;
import static io.protostuff.StringSerializerTest.int_targets;
import static io.protostuff.StringSerializerTest.long_targets;
import static io.protostuff.StringSerializerTest.numeric;
import static io.protostuff.StringSerializerTest.readRawVarint32;
import static io.protostuff.StringSerializerTest.str_len_130;
import static io.protostuff.StringSerializerTest.targets;
import static io.protostuff.StringSerializerTest.three_byte_utf8;
import static io.protostuff.StringSerializerTest.two_byte_utf8;
import static io.protostuff.StringSerializerTest.whitespace;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import io.protostuff.StringSerializer.STRING;
import static io.protostuff.StringSerializerTest.getShortStringLengthInBytes;
import static io.protostuff.StringSerializerTest.legacySurrogatePairSerialized;
import static io.protostuff.StringSerializerTest.nativeSurrogatePairsSerialized;
import static io.protostuff.StringSerializerTest.surrogatePairs;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * Tests for streaming UTF-8 Encoding
 * 
 * @author David Yu
 */
public class StreamedStringSerializerTest extends TestCase
{

    static final int NUM_BUF_SIZE = 32;
    static final int BUF_SIZE = 256;

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

    public String repeatChar(char ch, int times)
    {
        StringBuilder sb = new StringBuilder(times);
        for (int i = 0; i < times; i++)
        {
            sb.append(ch);
        }
        return sb.toString();
    }

    public void checkVarDelimitedBoundry(int initialGap, int secondWriteSize) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bufferSize = BUF_SIZE;
        final LinkedBuffer lb = new LinkedBuffer(bufferSize);
        WriteSession session = new WriteSession(lb, out, null, bufferSize);

        // Should fill up the buffer with initialGap byte(s) left
        StreamedStringSerializer.writeUTF8(repeatChar('a', bufferSize - initialGap), session, lb);

        // Write a string of length secondWriteSize that should be larger
        // than the next buffer size
        assertTrue(secondWriteSize > bufferSize);
        StreamedStringSerializer.writeUTF8VarDelimited(repeatChar('a', secondWriteSize), session, lb);
    }

    public void testInt() throws Exception
    {
        for (int i : int_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb, out);
            StreamedStringSerializer.writeInt(i, session, lb);
            LinkedBuffer.writeTo(out, lb);

            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2, out2);
            StreamedStringSerializer.writeInt(i, session2, lb2);
            LinkedBuffer.writeTo(out2, lb2);

            byte[] buffered = out.toByteArray();
            byte[] buffered_needed_to_flush = out2.toByteArray();
            byte[] builtin = STRING.ser(Integer.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_flush);
        }
    }

    public void testLong() throws Exception
    {
        for (long i : long_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb, out);
            StreamedStringSerializer.writeLong(i, session, lb);
            LinkedBuffer.writeTo(out, lb);

            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2, out2);
            StreamedStringSerializer.writeLong(i, session2, lb2);
            LinkedBuffer.writeTo(out2, lb2);

            byte[] buffered = out.toByteArray();
            byte[] buffered_needed_to_flush = out2.toByteArray();
            byte[] builtin = STRING.ser(Long.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_flush);
        }
    }

    public void testFloat() throws Exception
    {
        for (float i : float_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb, out);
            StreamedStringSerializer.writeFloat(i, session, lb);
            LinkedBuffer.writeTo(out, lb);

            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2, out2);
            StreamedStringSerializer.writeFloat(i, session2, lb2);
            LinkedBuffer.writeTo(out2, lb2);

            byte[] buffered = out.toByteArray();
            byte[] buffered_needed_to_flush = out2.toByteArray();
            byte[] builtin = STRING.ser(Float.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_flush);
        }
    }

    public void testDouble() throws Exception
    {
        for (double i : double_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb, out);
            StreamedStringSerializer.writeDouble(i, session, lb);
            LinkedBuffer.writeTo(out, lb);

            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2, out2);
            StreamedStringSerializer.writeDouble(i, session2, lb2);
            LinkedBuffer.writeTo(out2, lb2);

            byte[] buffered = out.toByteArray();
            byte[] buffered_needed_to_flush = out2.toByteArray();
            byte[] builtin = STRING.ser(Double.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_flush);
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

    public void testSurrogatePairs() throws Exception
    {
        // This test is mainly for Java 8+, where 3-byte surrogates
        // and 6-byte surrogate pairs are not allowed.
        LinkedBuffer lb = new LinkedBuffer(256);
        WriteSession session = new WriteSession(lb);
        StreamedStringSerializer.writeUTF8(surrogatePairs, session, lb);

        byte[] buffered = session.toByteArray();

        // Does our own serialization match native?
        assertTrue(Arrays.equals(buffered, nativeSurrogatePairsSerialized));

        // Does our own serialization / deserialization work?
        assertEquals(surrogatePairs, STRING.deserCustomOnly(buffered));

        // Can we decode legacy encodings?
        assertEquals(surrogatePairs, STRING.deserCustomOnly(legacySurrogatePairSerialized));

        // Does the built in serialization work?
        assertEquals(surrogatePairs, new String(nativeSurrogatePairsSerialized, "UTF-8"));

        // Should be encoded using 4-byte code points, instead of 6-byte surrogate pairs.
        assertFalse(Arrays.equals(buffered, legacySurrogatePairSerialized));

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
        
        // 3 bytes can't hold a 4-byte encoding, but we
        // don't expect it to use the 4-byte encoding path,
        // since it's not a pair
        LinkedBuffer lb = new LinkedBuffer(3);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WriteSession session = new WriteSession(lb, out);
        StreamedStringSerializer.writeUTF8(partial, session, lb);

        byte[] buffered = session.toByteArray();
    }

    static void checkVarDelimited(String str, int size, int stringLen) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
        WriteSession session = new WriteSession(lb, out);
        StreamedStringSerializer.writeUTF8VarDelimited(str, session, lb);
        LinkedBuffer.writeTo(out, lb);

        byte[] buf = out.toByteArray();

        assertTrue(buf.length == stringLen + size);

        int len = readRawVarint32(buf, 0);
        assertTrue(len == stringLen);

        print("total len: " + buf.length);
    }

    static void checkFixedDelimited(String str) throws Exception
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bout, "UTF-8");
        bout.write(getShortStringLengthInBytes(str));
        writer.write(str, 0, str.length());
        writer.close();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
        WriteSession session = new WriteSession(lb, out);
        StreamedStringSerializer.writeUTF8FixedDelimited(str, session, lb);
        LinkedBuffer.writeTo(out, lb);

        byte[] b1 = bout.toByteArray();

        byte[] b2 = out.toByteArray();

        assertEquals(b1, b2);
    }

    static void assertEquals(byte[] b1, byte[] b2) throws Exception
    {
        String s1 = new String(b1, "UTF-8");
        String s2 = new String(b2, "UTF-8");
        // System.err.println(s1 + " == " + s2);
        assertEquals(s1, s2);
    }

    static void checkAscii(String str) throws Exception
    {
        byte[] builtin = BUILT_IN_SERIALIZER.serialize(str);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
        WriteSession session = new WriteSession(lb, out);

        StreamedStringSerializer.writeAscii(str, session, lb);
        LinkedBuffer.writeTo(out, lb);

        assertTrue(builtin.length == session.size);

        byte[] buffered = out.toByteArray();

        assertTrue(builtin.length == buffered.length);

        String strBuiltin = new String(builtin, "ASCII");
        String strBuffered = new String(buffered, "ASCII");

        assertEquals(strBuiltin, strBuffered);
        print(strBuiltin);
        print("len: " + builtin.length);
    }

    static void check(String str) throws Exception
    {
        byte[] builtin = BUILT_IN_SERIALIZER.serialize(str);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
        WriteSession session = new WriteSession(lb, out);

        StreamedStringSerializer.writeUTF8(str, session, lb);
        LinkedBuffer.writeTo(out, lb);

        assertTrue(builtin.length == session.size);

        byte[] buffered = out.toByteArray();

        assertTrue(builtin.length == buffered.length);

        String strBuiltin = new String(builtin, "UTF-8");
        String strBuffered = new String(buffered, "UTF-8");

        assertEquals(strBuiltin, strBuffered);
        assertTrue(Arrays.equals(builtin, buffered));
        assertEquals(STRING.deser(builtin), STRING.deser(buffered));
        print(strBuiltin);
        print("len: " + builtin.length);
    }

    static void checkEncodedStringWithVarDelimited(byte[] buffer, String str, int offset, int len) throws Exception
    {
        assertEquals(len, readRawVarint32(buffer, offset));

        String deser = STRING.deser(buffer, offset + 2, len);

        assertEquals(str.length(), deser.length());
        assertEquals(str, deser);
    }

    public void testMultipleLargeStringsExceedingBufferSize() throws Exception
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(256);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WriteSession session = new WriteSession(buffer, out);
        String utf8OneByte = repeatChar('a', 1024);
        String utf8TwoBytes = repeatChar((char) 0x7ff, 1024 / 2);
        String utf8ThreeBytes = repeatChar((char) 0x800, 1024 / 3);

        writeToSession(utf8OneByte, utf8TwoBytes, utf8ThreeBytes, session, false);
        assertTrue(session.tail == session.head);
        // flush remaining
        LinkedBuffer.writeTo(out, buffer);
        // clear
        buffer.clear();

        byte[] data = out.toByteArray();

        LinkedBuffer buffer2 = LinkedBuffer.allocate(256);
        WriteSession session2 = new WriteSession(buffer2);

        writeToSession(utf8OneByte, utf8TwoBytes, utf8ThreeBytes, session2, false);

        byte[] data2 = session2.toByteArray();

        assertEquals(STRING.deser(data), STRING.deser(data2));
    }

    public void testMultipleLargeStringsExceedingBufferSizeDelimited() throws Exception
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(256);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WriteSession session = new WriteSession(buffer, out);
        String utf8OneByte = repeatChar('a', 1024);
        String utf8TwoBytes = repeatChar((char) 0x7ff, 1024 / 2);
        String utf8ThreeBytes = repeatChar((char) 0x800, 1024 / 3);

        writeToSession(utf8OneByte, utf8TwoBytes, utf8ThreeBytes, session, true);
        assertTrue(session.tail == session.head);
        // temporary buffers will remain
        assertTrue(session.tail.next != null);
        // flush remaining
        LinkedBuffer.writeTo(out, buffer);
        // clear
        buffer.clear();

        byte[] data = out.toByteArray();

        LinkedBuffer buffer2 = LinkedBuffer.allocate(256);
        WriteSession session2 = new WriteSession(buffer2);

        writeToSession(utf8OneByte, utf8TwoBytes, utf8ThreeBytes, session2, true);

        byte[] data2 = session2.toByteArray();

        // We cannot do a direct STRING.deser, since it's not supposed to handle
        // multiple fields. Thus, in order to test this, we have to iterate through each
        // field, and compare the STRING.deser of that. Since we know the length ahead of time,
        // we can do simple verifications to make life easier.

        // Make sure the output bytes are the same.
        assertEquals(data.length, data2.length);

        // Make sure the data is identical.
        for (int i = 0; i < data.length; i++)
            assertEquals(data[i], data2[i]);

        // Now that we've confirmed they're identical, doing checks on the
        // validity is the same as doing it on the other.
        //
        // We will check that the VarDelimited value was encoded properly,
        // and that we can use that value to properly deserialize the String.

        int offset = 0;

        checkEncodedStringWithVarDelimited(data, utf8OneByte, offset, 1024);
        offset += 1024 + 2;
        checkEncodedStringWithVarDelimited(data, utf8OneByte, offset, 1024);
        offset += 1024 + 2;

        checkEncodedStringWithVarDelimited(data, utf8TwoBytes, offset, 1024);
        offset += 1024 + 2;
        checkEncodedStringWithVarDelimited(data, utf8TwoBytes, offset, 1024);
        offset += 1024 + 2;

        // These guy's length is actually 1023, because 1024/3 is 341.
        checkEncodedStringWithVarDelimited(data, utf8ThreeBytes, offset, 1023);
        offset += 1023 + 2;
        checkEncodedStringWithVarDelimited(data, utf8ThreeBytes, offset, 1023);
        offset += 1023 + 2;

        assertEquals(offset, data.length);
    }

    static void writeToSession(String str1, String str2, String str3,
            WriteSession session, boolean delimited) throws IOException
    {
        if (delimited)
        {
            session.tail = session.sink.writeStrUTF8VarDelimited(str1, session,
                    session.tail);
            session.tail = session.sink.writeStrUTF8VarDelimited(str1, session,
                    session.tail);

            session.tail = session.sink.writeStrUTF8VarDelimited(str2, session,
                    session.tail);
            session.tail = session.sink.writeStrUTF8VarDelimited(str2, session,
                    session.tail);

            session.tail = session.sink.writeStrUTF8VarDelimited(str3, session,
                    session.tail);
            session.tail = session.sink.writeStrUTF8VarDelimited(str3, session,
                    session.tail);
        }
        else
        {
            session.tail = session.sink.writeStrUTF8(str1, session,
                    session.tail);
            session.tail = session.sink.writeStrUTF8(str1, session,
                    session.tail);

            session.tail = session.sink.writeStrUTF8(str2, session,
                    session.tail);
            session.tail = session.sink.writeStrUTF8(str2, session,
                    session.tail);

            session.tail = session.sink.writeStrUTF8(str3, session,
                    session.tail);
            session.tail = session.sink.writeStrUTF8(str3, session,
                    session.tail);
        }
    }

    static void print(String msg)
    {
        // System.err.println(msg);
    }
}
