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
import java.io.IOException;
import java.util.Arrays;

import com.dyuproject.protostuff.StringSerializer.STRING;

import junit.framework.TestCase;

/**
 * Tests for base 64 encoding.
 *
 * @author David Yu
 * @created Sep 27, 2010
 */
public class B64CodeTest extends TestCase
{
    
    public void testStream() throws Exception
    {
        // everything will fit
        testStream("1234567", new LinkedBuffer(12));
        // need to flush once
        testStream("1234567", str('a', 4), newBuffer(12, 'a', 4));
        // 1 write chunk and encode remaining
        testStream("1234567", new LinkedBuffer(4));
        // no write chunks and encode remaining
        testStream("1234567", str('a', 1), newBuffer(4, 'a', 1));
        // 1 write chunk and encode remaining
        testStream("1234567", str('a', 4), newBuffer(8, 'a', 4));
    }
    
    public void testBuffer() throws Exception
    {
        // everything will fit
        testBuffer("1234567", new LinkedBuffer(12));
        // no write chunks and will need to grow
        testBuffer("1234567", new LinkedBuffer(3));
        // 1 write chunk and will need to grow
        testBuffer("1234567", new LinkedBuffer(4));
        // no write chunks and will need to grow
        testBuffer("1234567", str('a', 1), newBuffer(4, 'a', 1));
        // no write chunks and will need to grow (not using nextBufferSize)
        testBuffer("1234567", str('a', 1), newBuffer(4, 'a', 1), 4);
        // 1 write chunk and will need to grow
        testBuffer("1234567", str('a', 4), newBuffer(8, 'a', 4));
        // 1 write chunk and will need to grow (not using nextBufferSize)
        testBuffer("1234567", str('a', 4), newBuffer(8, 'a', 4), 3);
    }
    
    public void testDecodeFromString() throws Exception
    {
        for(String str : new String[]{"abcdefgh", "1", "12", "123", "1234", "12345"})
        {
            byte[] b64Encoded = B64Code.encode(str.getBytes("UTF-8"));
            
            byte[] decoded = B64Code.decode(b64Encoded);
            
            byte[] decodedFromString = B64Code.decode(new String(b64Encoded, "UTF-8"));
            
            assertEquals(STRING.deser(decoded), STRING.deser(decodedFromString));
        }
    }
    
    public void testDecodeTo() throws Exception
    {
        for(String str : new String[]{"abcdefgh", "1", "12", "123", "1234", "12345"})
        {
            byte[] b64Encoded = B64Code.encode(str.getBytes("UTF-8"));
            
            byte[] decoded = new byte[16];
            
            int decodedLen = B64Code.decodeTo(decoded, 0, b64Encoded, 0, b64Encoded.length);
            
            byte[] decodedFromString = B64Code.decode(new String(b64Encoded, "UTF-8"));
            
            assertEquals(STRING.deser(decoded, 0, decodedLen), STRING.deser(decodedFromString));
        }
    }

    
    static String str(char c, int size)
    {
        char[] array = new char[size];
        Arrays.fill(array, c);
        return new String(array);
    }
    
    static LinkedBuffer newBuffer(int size, char c, int loops)
    {
        LinkedBuffer lb = new LinkedBuffer(size);
        while(loops-->0)
        {
            lb.buffer[lb.offset++] = (byte)c;
        }
        return lb;
    }
    
    static void testStream(String str, LinkedBuffer tail) throws IOException
    {
        testStream(str, "", tail);
    }
    
    static void testStream(String str, String prefix, LinkedBuffer tail) throws IOException
    {
        byte[] data = str.getBytes();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WriteSession session = new WriteSession(tail, out);
        session.size += (tail.offset - tail.start);
        
        tail = B64Code.encode(data, 0, data.length, session, out, tail);
        
        assertTrue(tail == session.head);
        
        LinkedBuffer.writeTo(out, tail);
        
        
        byte[] result = out.toByteArray();
        //System.err.println(new String(result, prefix.length(), result.length - prefix.length()));
        byte[] decoded = B64Code.decode(result, prefix.length(), result.length - prefix.length());
        
        String strd = new String(decoded);
        
        assertEquals(str, strd);
        
        //System.err.println(gg.length + " == " + decoded.length + " | " + 
        //        str.equals(strd) + " | " + str + " == " + strd);
    }
    
    static void testBuffer(String str, LinkedBuffer tail) throws IOException
    {
        testBuffer(str, "", tail);
    }
    
    static void testBuffer(String str, String prefix, LinkedBuffer tail) throws IOException
    {
        testBuffer(str, prefix, tail, LinkedBuffer.DEFAULT_BUFFER_SIZE);
    }
    
    static void testBuffer(String str, String prefix, LinkedBuffer tail, int nextBufferSize) throws IOException
    {
        byte[] data = str.getBytes();
        WriteSession session = new WriteSession(tail, nextBufferSize);
        session.size += (tail.offset - tail.start);
        tail = B64Code.encode(data, 0, data.length, session, tail);
        
        byte[] result = session.toByteArray();
        //System.err.println(result.length - prefix.length());
        //System.err.println(new String(result, prefix.length(), result.length - prefix.length()));
        byte[] decoded = B64Code.decode(result, prefix.length(), result.length - prefix.length());
        String strd = new String(decoded);
        assertEquals(str, strd);
    }
}
