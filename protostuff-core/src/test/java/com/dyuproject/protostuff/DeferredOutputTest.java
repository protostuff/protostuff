//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

import junit.framework.TestCase;

/**
 * DeferredOutput test for the size of the bytes written.
 *
 * @author David Yu
 * @created Nov 11, 2009
 */
public class DeferredOutputTest extends TestCase
{
    
    DeferredOutput output;
    
    public void setUp()
    {
        output = new DeferredOutput();
    }
    
    public void testInt32() throws Exception
    {
        int size = 0;
        for(int i=1; i<10; i++)
        {
            int num = i;
            int value = i * i * i;
            output.writeInt32(num, value, false);
            size += CodedOutput.computeInt32Size(num, value);
            assertSize(size, output.getSize());
        }
    }
    
    public void testInt64() throws Exception
    {
        int size = 0;
        for(int i=1; i<10; i++)
        {
            int num = i;
            long value = i * i * i;
            output.writeInt64(num, value, false);
            size += CodedOutput.computeInt64Size(num, value);
            assertSize(size, output.getSize());
        }
    }

    public void testFloat() throws Exception
    {
        int size = 0;
        for(int i=1; i<10; i++)
        {
            int num = i;
            float value = i * i * i;
            output.writeFloat(num, value, false);
            size += CodedOutput.computeFloatSize(num, value);
            assertSize(size, output.getSize());
        }
    }
    
    public void testDouble() throws Exception
    {
        int size = 0;
        for(int i=1; i<10; i++)
        {
            int num = i;
            double value = i * i * i;
            output.writeDouble(num, value, false);
            size += CodedOutput.computeDoubleSize(num, value);
            assertSize(size, output.getSize());
        }
    }
    
    public void testBoolean() throws Exception
    {
        output.writeBool(1, true, false);
        assertSize(2, output.getSize());
        output.writeBool(2, false, false);
        assertSize(4, output.getSize());
    }
    
    public void testString() throws Exception
    {
        String s = "abcde12345";
        output.writeString(1, s, false);
        assertSize(1+1+s.getBytes(ByteString.UTF8).length, output.getSize());
    }
    
    public void testBytes() throws Exception
    {
        String s = "abcde12345";
        byte[] b = s.getBytes(ByteString.UTF8);
        ByteString bs = ByteString.copyFromUtf8(s);
        output.writeBytes(1, bs, false);
        assertSize(1+1+10, output.getSize());
        output.writeByteArray(2, b, false);
        assertSize(1+1+10+1+1+10, output.getSize());
    }
    
    static void assertSize(int size1, int size2)
    {
        //System.err.println(size1 + " == " + size2);
        assertTrue(size1 == size2);
    }
}
