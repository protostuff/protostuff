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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.dyuproject.protostuff.LinkedBuffer.WriteSession;
import com.dyuproject.protostuff.StringSerializer.STRING;

import junit.framework.TestCase;

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
    
    // 10 total
    static final String numeric = "0123456789";
    
    // 3 total
    static final String whitespace = "\r\n\t";
    
    // 59 total
    static final String foo = alphabet + three_byte_utf8 + numeric + two_byte_utf8 + whitespace;
    
    static final String str_len_130 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    
    static final String[] targets = new String[]{
        three_byte_utf8,
        two_byte_utf8,
        alphabet,
        alphabet_to_upper,
        numeric,
        whitespace,
        foo,
    };
    
    static final int[] int_targets = new int[]{
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
    
    static final long[] long_targets = new long[]{
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
    
    static final float[] float_targets = new float[]{
        0.0f,
        10.01f,
        -10.01f,
        1234.4321f
        -1234.4321f,
        56789.98765f,
        -56789.98765f,
        Float.MAX_VALUE,
        Float.MIN_VALUE
    };
    
    static final double[] double_targets = new double[]{
        0.0d,
        10.01d,
        -10.01d,
        1234.4321d
        -1234.4321d,
        56789.98765d,
        -56789.98765d,
        1234567890.0987654321d,
        -1234567890.0987654321d,
        Double.MAX_VALUE,
        Double.MIN_VALUE
    };
    
    public void testUTF8FromInt() throws Exception
    {
        for(int i : int_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeUTF8FromInt(i, session, lb);
            
            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeUTF8FromInt(i, session2, lb2);
            
            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Integer.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
    }
    
    public void testUTF8FromLong() throws Exception
    {
        for(long i : int_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeUTF8FromLong(i, session, lb);
            
            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeUTF8FromLong(i, session2, lb2);
            
            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Long.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
    }
    
    public void testUTF8FromFloat() throws Exception
    {
        for(float i : float_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeUTF8FromFloat(i, session, lb);
            
            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeUTF8FromFloat(i, session2, lb2);
            
            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Float.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
    }
    
    public void testUTF8FromDouble() throws Exception
    {
        for(double i : double_targets)
        {
            LinkedBuffer lb = new LinkedBuffer(256);
            WriteSession session = new WriteSession(lb);
            StringSerializer.writeUTF8FromDouble(i, session, lb);
            
            LinkedBuffer lb2 = new LinkedBuffer(1);
            WriteSession session2 = new WriteSession(lb2);
            StringSerializer.writeUTF8FromDouble(i, session2, lb2);
            
            byte[] buffered = session.toByteArray();
            byte[] buffered_needed_to_grow = session2.toByteArray();
            byte[] builtin = STRING.ser(Double.toString(i));

            assertEquals(builtin, buffered);
            assertEquals(builtin, buffered_needed_to_grow);
        }
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
        
        checkVarDelimited(moreThan2048, 3, expectedLen);
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
        DataOutputStream dout = new DataOutputStream(out);
        dout.writeUTF(str);
        dout.close();
        
        LinkedBuffer lb = new LinkedBuffer(512);
        WriteSession session = new WriteSession(lb);
        StringSerializer.writeUTF8FixedDelimited(str, session, lb);
        
        byte[] b1 = out.toByteArray();
        byte[] b2 = session.toByteArray();
        
        assertEquals(b1, b2);
    }
    
    static void assertEquals(byte[] b1, byte[] b2) throws Exception
    {
        String s1 = new String(b1, "UTF-8");
        String s2 = new String(b2, "UTF-8");
        assertEquals(s1, s2);
    }
    
    static void check(String str) throws Exception
    {
        byte[] builtin = BUILT_IN_SERIALIZER.serialize(str);
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
        //System.err.println(msg);
    }
    
    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;

        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-string-bench-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 800000);
        int loops = Integer.getInteger("benchmark.loops", 8000000);
        
        String title = "protostuff-api string serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(foo, SERIALIZERS, out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void main(String[] args) throws Exception
    {
        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-string-bench-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 800000);
        int loops = Integer.getInteger("benchmark.loops", 8000000);
        
        String title = "protostuff-api string serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(foo, SERIALIZERS, out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void start(String message, Serializer[] serializers, 
            PrintStream out, int warmups, int loops) throws Exception
    {
        for(Serializer s : serializers)
            ser(message, s, out, s.getName(), warmups, loops);
    }
    
    static void ser(String message, Serializer serializer, PrintStream out, 
            String name, int warmups, int loops) throws Exception
    {
        int len = serializer.serialize(message).length;
        for(int i=0; i<warmups; i++)
            serializer.serialize(message);
        long start = System.currentTimeMillis();
        for(int i=0; i<loops; i++)
            serializer.serialize(message);
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        out.println(elapsed + " ms elapsed with " + len + " bytes for " + name);
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
    
    public interface Serializer
    {
        
        public byte[] serialize(String str);
        
        public String getName();
        
    }
    
    public static final Serializer BUILT_IN_SERIALIZER = new Serializer()
    {

        public byte[] serialize(String str)
        {
            try
            {
                return str.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(e);
            }
        }

        public String getName()
        {
            return "built-in";
        }
        
    };
    
    public static final Serializer BUFFERED_SERIALIZER = new Serializer()
    {
        
        final LinkedBuffer buffer = new LinkedBuffer(512);

        public byte[] serialize(String str)
        {
            final LinkedBuffer buffer = this.buffer;
            try
            {
                final WriteSession session = new WriteSession(buffer);
                StringSerializer.writeUTF8(str, session, buffer);
                return session.toByteArray();
            }
            finally
            {
                buffer.clear();
            }
        }

        public String getName()
        {
            return "buffered";
        }
        
    };
    
    public static final Serializer BUFFERED_RECYCLED_SESSION_SERIALIZER = new Serializer()
    {
        
        final WriteSession session = new WriteSession(new LinkedBuffer(512));

        public byte[] serialize(String str)
        {
            final WriteSession session = this.session;
            try
            {
                StringSerializer.writeUTF8(str, session, session.head);
                return session.toByteArray();
            }
            finally
            {
                session.clear();
            }
        }

        public String getName()
        {
            return "buffered-recycled-session";
        }
        
    };
    
    public static final Serializer[] SERIALIZERS = new Serializer[]{
        BUILT_IN_SERIALIZER, 
        BUFFERED_SERIALIZER, 
        BUFFERED_RECYCLED_SESSION_SERIALIZER, 
        BUILT_IN_SERIALIZER, 
        BUFFERED_SERIALIZER, 
        BUFFERED_RECYCLED_SESSION_SERIALIZER
    };

}
