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

import junit.framework.TestCase;

import com.dyuproject.protostuff.StringSerializer.STRING;

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
            
            assertEquals(str, STRING.deser(decoded));
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
            
            assertEquals(str, STRING.deser(decoded, 0, decodedLen));
        }
    }
    
    public void testStringDecodeTo() throws Exception
    {
        for(String str : new String[]{"abcdefgh", "1", "12", "123", "1234", "12345"})
        {
            byte[] b64Encoded = B64Code.encode(str.getBytes("UTF-8"));
            
            byte[] decoded = new byte[16];
            
            int decodedLen = B64Code.decodeTo(decoded, 0, b64Encoded, 0, b64Encoded.length);

            String encodedString = new String(b64Encoded, "UTF-8");
            
            byte[] decodedFromString = new byte[16];
            
            int decodedFromStringLen = B64Code.decodeTo(decodedFromString, 0, 
                    encodedString, 0, encodedString.length());
            
            assertEquals(STRING.deser(decoded, 0, decodedLen), 
                    STRING.deser(decodedFromString, 0, decodedFromStringLen));
            
            assertEquals(str, STRING.deser(decoded, 0, decodedLen));
        }
    }

    public void testRoundtripFromString() throws Exception
    {
        for(String str : new String[]{
                "abcdefghijklmnopqrstuvwxyz", 
                "0123456789", 
                "abcdefghijklmnopqrstuvwxyz0123456789"
                })
        {
            verifyRoundTrip(str.getBytes("UTF-8"));
        }
    }
    
    public void testRoundtrip()
    {
        for(byte[] b : new byte[][]{
                new byte[]{0,0,0,0}, 
                new byte[]{0,0,0,0,1,1,1,1}, 
                new byte[]{1,1,1,1,1,1,1,1}, 
                new byte[]{0,1,2,3,4,5,6,7,8,9},
                new byte[]{10,11,12,13,14,15,16,17,18,19}, 
                new byte[]{20,21,22,23,24,25,26,27,28,29}, 
                new byte[]{30,31,32,33,34,35,36,37,38,39}, 
                new byte[]{40,41,42,43,44,45,46,47,48,49}, 
                new byte[]{50,51,52,53,54,55,56,57,58,59}, 
                new byte[]{60,61,62,63,64,65,66,67,68,69}, 
                new byte[]{70,71,72,73,74,75,76,77,78,79}, 
                new byte[]{80,81,82,83,84,85,86,87,88,89}, 
                new byte[]{90,91,92,93,94,95,96,97,98,99}, 
                new byte[]{100,101,102,103,104,105,106,107,108,109},
                new byte[]{110,111,112,113,114,115,116,117,118,119}, 
                new byte[]{120,121,122,123,124,125,126,127}
                })
        {
            verifyRoundTrip(b);
        }
    }
    
    void verifyRoundTrip(final byte[] raw)
    {
        byte[] encoded = B64Code.encode(raw);
        byte[] decoded = B64Code.decode(encoded);
        assertTrue(Arrays.equals(raw, decoded));
        
        char[] cencoded = B64Code.cencode(raw);
        verifyArrayContentEqual(cencoded, encoded);
        assertTrue(Arrays.equals(raw, B64Code.cdecode(cencoded)));
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
        byte[] copy = new byte[tail.offset - tail.start];
        System.arraycopy(tail.buffer, tail.start, copy, 0, tail.offset - tail.start);
        
        byte[] data = str.getBytes();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WriteSession session = new WriteSession(tail, out);
        session.size += (tail.offset - tail.start);
        
        tail = B64Code.sencode(data, 0, data.length, session, tail);
        
        assertTrue(tail == session.head);
        
        LinkedBuffer.writeTo(out, tail);
        
        byte[] dataFromStream = out.toByteArray();
        verifyB64(str, prefix, dataFromStream);
        
        WriteSession bws = new WriteSession(new LinkedBuffer(copy, 0));
        bws.tail.offset += copy.length;
        bws.size += copy.length;
        bws.tail = B64Code.encode(data, 0, data.length, bws, bws.tail);
        
        assertTrue(Arrays.equals(dataFromStream, bws.toByteArray()));
        
        //System.err.println(gg.length + " == " + decoded.length + " | " + 
        //        str.equals(strd) + " | " + str + " == " + strd);
    }

    static void verifyB64(String str, String prefix, byte[] result)
    {
        //System.err.println(new String(result, prefix.length(), result.length - prefix.length()));
        byte[] decoded = B64Code.decode(result, prefix.length(), result.length - prefix.length());
        
        String strd = new String(decoded);
        
        assertEquals(str, strd);
        
        char[] cresult = toCharArray(result);
        byte[] decoded2 = B64Code.cdecode(cresult, prefix.length(), cresult.length - prefix.length());
        assertTrue(Arrays.equals(decoded, decoded2));
        
        char[] cencoded = B64Code.cencode(decoded);
        byte[] encoded = B64Code.encode(decoded);
        
        assertTrue(cencoded.length == encoded.length);
        
        verifyArrayContentEqual(cencoded, encoded);
    }
    
    static void verifyArrayContentEqual(char[] cencoded, byte[] encoded)
    {
        for(int i = 0, len = encoded.length; i < len; i++)
            assertTrue(cencoded[i] == encoded[i]);
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
        verifyB64(str, prefix, result);
    }
    
    static char[] toCharArray(byte[] buf)
    {
        char[] c = new char[buf.length];
        for(int i = 0; i < buf.length; i++)
            c[i] = (char)buf[i];
        return c;
    }
}
