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

import java.io.IOException;

import junit.framework.TestCase;

/**
 * Test for parsing numbers from raw ascii buffers.
 *
 * @author David Yu
 * @created Dec 4, 2010
 */
public class NumberParserTest extends TestCase
{
    
    
    public void testParseInt() throws Exception
    {
        assertTrue(0 == NumberParser.parseInt(new byte[]{'0'}, 0, 1, 10));
        assertTrue(1 == NumberParser.parseInt(new byte[]{'1'}, 0, 1, 10));
        assertTrue(-1 == NumberParser.parseInt(new byte[]{'-', '1'}, 0, 2, 10));
        
        final LinkedBuffer lb = LinkedBuffer.allocate(256);
        final WriteSession session = new WriteSession(lb);
        
        assertTrue(lb == StringSerializer.writeInt(
                Integer.MAX_VALUE, session, session.tail));
        
        assertTrue(Integer.MAX_VALUE == NumberParser.parseInt(
                session.toByteArray(), 0, session.size, 10));
        
        session.clear();
        
        assertTrue(lb == StringSerializer.writeInt(
                Integer.MAX_VALUE-1, session, session.tail));
        
        assertTrue(Integer.MAX_VALUE-1 == NumberParser.parseInt(
                session.toByteArray(), 0, session.size, 10));
        
        session.clear();
        
        assertTrue(lb == StringSerializer.writeInt(
                Integer.MIN_VALUE, session, session.tail));
        
        assertTrue(Integer.MIN_VALUE == NumberParser.parseInt(
                session.toByteArray(), 0, session.size, 10));
        
        session.clear();
        
        assertTrue(lb == StringSerializer.writeInt(
                Integer.MIN_VALUE+1, session, session.tail));
        
        assertTrue(Integer.MIN_VALUE+1 == NumberParser.parseInt(
                session.toByteArray(), 0, session.size, 10));
        
    }
    
    public void testParseLong() throws IOException
    {
        assertTrue(0l == NumberParser.parseLong(new byte[]{'0'}, 0, 1, 10));
        assertTrue(1l == NumberParser.parseLong(new byte[]{'1'}, 0, 1, 10));
        assertTrue(-1l == NumberParser.parseLong(new byte[]{'-', '1'}, 0, 2, 10));
        
        final LinkedBuffer lb = LinkedBuffer.allocate(256);
        final WriteSession session = new WriteSession(lb);
        
        assertTrue(lb == StringSerializer.writeLong(
                Long.MAX_VALUE, session, session.tail));
        
        assertTrue(Long.MAX_VALUE == NumberParser.parseLong(
                session.toByteArray(), 0, session.size, 10));
        
        session.clear();
        
        assertTrue(lb == StringSerializer.writeLong(
                Long.MAX_VALUE-1, session, session.tail));
        
        assertTrue(Long.MAX_VALUE-1 == NumberParser.parseLong(
                session.toByteArray(), 0, session.size, 10));
        
        session.clear();
        
        assertTrue(lb == StringSerializer.writeLong(
                Long.MIN_VALUE, session, session.tail));
        
        assertTrue(Long.MIN_VALUE == NumberParser.parseLong(
                session.toByteArray(), 0, session.size, 10));
        
        session.clear();
        
        assertTrue(lb == StringSerializer.writeLong(
                Long.MIN_VALUE+1, session, session.tail));
        
        assertTrue(Long.MIN_VALUE+1 == NumberParser.parseLong(
                session.toByteArray(), 0, session.size, 10));
    }

}
