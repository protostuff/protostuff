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

import static com.dyuproject.protostuff.me.StringSerializerTest.BUILT_IN_SERIALIZER;
import static com.dyuproject.protostuff.me.StringSerializerTest.alphabet;
import static com.dyuproject.protostuff.me.StringSerializerTest.alphabet_to_upper;
import static com.dyuproject.protostuff.me.StringSerializerTest.ascii_targets;
import static com.dyuproject.protostuff.me.StringSerializerTest.double_targets;
import static com.dyuproject.protostuff.me.StringSerializerTest.float_targets;
import static com.dyuproject.protostuff.me.StringSerializerTest.foo;
import static com.dyuproject.protostuff.me.StringSerializerTest.int_targets;
import static com.dyuproject.protostuff.me.StringSerializerTest.long_targets;
import static com.dyuproject.protostuff.me.StringSerializerTest.numeric;
import static com.dyuproject.protostuff.me.StringSerializerTest.readRawVarint32;
import static com.dyuproject.protostuff.me.StringSerializerTest.str_len_130;
import static com.dyuproject.protostuff.me.StringSerializerTest.targets;
import static com.dyuproject.protostuff.me.StringSerializerTest.three_byte_utf8;
import static com.dyuproject.protostuff.me.StringSerializerTest.two_byte_utf8;
import static com.dyuproject.protostuff.me.StringSerializerTest.whitespace;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import com.dyuproject.protostuff.me.StringSerializer.STRING;

/**
 * Tests for streaming UTF-8 Encoding
 *
 * @author David Yu
 * @created Sep 18, 2010
 */
public class StreamedStringSerializerTest extends TestCase
{
    
    static final int NUM_BUF_SIZE = 32;
    static final int BUF_SIZE = 256;

    public void testVarDelimitedBoundryTwoByte() throws Exception
    {
        int size = StringSerializer.THREE_BYTE_LOWER_LIMIT-1; // takes 2 bytes for size and is larger than buffer

        checkVarDelimitedBoundry(1, size); // 1st str does not fit
        checkVarDelimitedBoundry(2, size); // 1st str fits
        checkVarDelimitedBoundry(3, size); // 2nd str varint doesn't fit
        checkVarDelimitedBoundry(4, size); // Only 2nd varint fits (slow)
    }

    public void testVarDelimitedBoundryThreeByte() throws Exception
    {
        int size = StringSerializer.FOUR_BYTE_LOWER_LIMIT-1; // takes 3 bytes for size

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
        for(int i = 0; i < times; i++)
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
        WriteSession session = new WriteSession(lb, bufferSize);

        // Should fill up the buffer with initialGap byte(s) left
        StreamedStringSerializer.writeUTF8(repeatChar('a', bufferSize-initialGap), session, out, lb);

        // Write a string of length secondWriteSize that should be larger
        // than the next buffer size
        assertTrue(secondWriteSize > bufferSize);
        StreamedStringSerializer.writeUTF8VarDelimited(repeatChar('a', secondWriteSize), session, out, lb);
    }

    public void testInt() throws Exception
    {
        for(int i : int_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb);
            StreamedStringSerializer.writeInt(i, session, out, lb);
            LinkedBuffer.writeTo(out, lb);
            
            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2);
            StreamedStringSerializer.writeInt(i, session2, out2, lb2);
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
        for(long i : long_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb);
            StreamedStringSerializer.writeLong(i, session, out, lb);
            LinkedBuffer.writeTo(out, lb);
            
            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2);
            StreamedStringSerializer.writeLong(i, session2, out2, lb2);
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
        for(float i : float_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb);
            StreamedStringSerializer.writeFloat(i, session, out, lb);
            LinkedBuffer.writeTo(out, lb);
            
            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2);
            StreamedStringSerializer.writeFloat(i, session2, out2, lb2);
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
        for(double i : double_targets)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
            WriteSession session = new WriteSession(lb);
            StreamedStringSerializer.writeDouble(i, session, out, lb);
            LinkedBuffer.writeTo(out, lb);
            
            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
            LinkedBuffer lb2 = new LinkedBuffer(NUM_BUF_SIZE);
            WriteSession session2 = new WriteSession(lb2);
            StreamedStringSerializer.writeDouble(i, session2, out2, lb2);
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
        for(String s : ascii_targets)
            checkAscii(s);
    }
    
    public void testUTF8() throws Exception
    {
        for(String s : targets)
            check(s);
        
        check("");
        check(str_len_130);
        
        String lessThan2048 = str_len_130;
        for(int i=0; i<14; i++)
            lessThan2048 += str_len_130;
        
        int lt2048Len = 130 * 15;
        assertTrue(lessThan2048.length() == lt2048Len);
        check(lessThan2048);
        
        String moreThan2048 = str_len_130;
        for(int i=0; i<20; i++)
            moreThan2048 += str_len_130;
        
        int expectedLen = 130 * 21;
        assertTrue(moreThan2048.length() == expectedLen);
        
        check(moreThan2048);
    }
    
    public void testUTF8VarDelimited() throws Exception
    {
        checkVarDelimited(foo, 1, 59);
        checkVarDelimited(whitespace, 1, 3);
        checkVarDelimited(numeric, 1, 10);
        checkVarDelimited(alphabet, 1, 26);
        checkVarDelimited(alphabet_to_upper, 1, 26);
        checkVarDelimited(two_byte_utf8, 1, 4*2);
        checkVarDelimited(three_byte_utf8, 1, 4*3);
        checkVarDelimited("1234567890123456789012345678901234567890", 1, 40);
        checkVarDelimited("", 1, 0);
        checkVarDelimited(str_len_130, 2, 130);
        checkVarDelimited(str_len_130.substring(10), 1, 120);
        
        String lessThan2048 = str_len_130;
        for(int i=0; i<14; i++)
            lessThan2048 += str_len_130;
        
        int lt2048Len = 130 * 15;
        assertTrue(lessThan2048.length() == lt2048Len);
        checkVarDelimited(lessThan2048, 2, lt2048Len);
        
        String moreThan2048 = str_len_130;
        for(int i=0; i<20; i++)
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
        for(String s : targets)
            checkFixedDelimited(s);
        
        checkFixedDelimited("1234567890123456789012345678901234567890");
        checkFixedDelimited("");
        checkFixedDelimited(str_len_130);
        
        String lessThan2048 = str_len_130;
        for(int i=0; i<14; i++)
            lessThan2048 += str_len_130;
        
        int lt2048Len = 130 * 15;
        assertTrue(lessThan2048.length() == lt2048Len);
        checkFixedDelimited(lessThan2048);
        
        String moreThan2048 = str_len_130;
        for(int i=0; i<20; i++)
            moreThan2048 += str_len_130;
        
        int expectedLen = 130 * 21;
        assertTrue(moreThan2048.length() == expectedLen);
        
        checkFixedDelimited(moreThan2048);
    }
    
    static void checkVarDelimited(String str, int size, int stringLen) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
        WriteSession session = new WriteSession(lb);
        StreamedStringSerializer.writeUTF8VarDelimited(str, session, out, lb);
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
        DataOutputStream dout = new DataOutputStream(bout);
        dout.writeUTF(str);
        dout.close();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
        WriteSession session = new WriteSession(lb);
        StreamedStringSerializer.writeUTF8FixedDelimited(str, session, out, lb);
        LinkedBuffer.writeTo(out, lb);
        
        byte[] b1 = bout.toByteArray();
        
        
        byte[] b2 = out.toByteArray();
        
        assertEquals(b1, b2);
    }
    
    static void assertEquals(byte[] b1, byte[] b2) throws Exception
    {
        String s1 = new String(b1, "UTF-8");
        String s2 = new String(b2, "UTF-8");
        //System.err.println(s1 + " == " + s2);
        assertEquals(s1, s2);
    }
    
    static void checkAscii(String str) throws Exception
    {
        byte[] builtin = BUILT_IN_SERIALIZER.serialize(str);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        LinkedBuffer lb = new LinkedBuffer(BUF_SIZE);
        WriteSession session = new WriteSession(lb);
        
        StreamedStringSerializer.writeAscii(str, session, out, lb);
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
        WriteSession session = new WriteSession(lb);
        
        StreamedStringSerializer.writeUTF8(str, session, out, lb);
        LinkedBuffer.writeTo(out, lb);
        
        assertTrue(builtin.length == session.size);
        
        byte[] buffered = out.toByteArray();
        
        assertTrue(builtin.length == buffered.length);
        
        String strBuiltin = new String(builtin, "UTF-8");
        String strBuffered = new String(buffered, "UTF-8");

        assertEquals(strBuiltin, strBuffered);
        print(strBuiltin);
        print("len: " + builtin.length);
    }
    
    public void testMultipleLargeStringsExceedingBufferSize() throws Exception
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(256);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WriteSession session = new WriteSession(buffer, out);
        String utf8OneByte = repeatChar('a', 1024);
        String utf8TwoBytes = repeatChar((char)0x7ff, 1024/2);
        String utf8ThreeBytes = repeatChar((char)0x800, 1024/3);
        
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
        String utf8TwoBytes = repeatChar((char)0x7ff, 1024/2);
        String utf8ThreeBytes = repeatChar((char)0x800, 1024/3);
        
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
        
        assertEquals(STRING.deser(data), STRING.deser(data2));
    }
    
    static void writeToSession(String str1, String str2, String str3, 
            WriteSession session, boolean delimited) throws IOException
    {
        if(delimited)
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
        //System.err.println(msg);
    }
}
