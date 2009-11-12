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

import com.dyuproject.protostuff.WireFormat.FieldType;

import junit.framework.TestCase;

/**
 * CodedOutput test for the size of the bytes written.
 *
 * @author David Yu
 * @created Nov 11, 2009
 */
public class CodedOutputTest extends TestCase
{
    
    static final int[][] int32values = new int[][]{
        {1,2},
        {10,20},
        {101,202},
        {1001,2002},
        {10001,20002},
        {100001,200002},
        {1000001,2000002},
        {10000001,20000002},
        {100000001,200000002},
        {1000000001,2000000002},
        {Integer.MAX_VALUE}
    };
    
    static final long[][] int64values = new long[][]{
        {1,2},
        {10,20},
        {101,202},
        {1001,2002},
        {10001,20002},
        {100001,200002},
        {1000001,2000002},
        {10000001,20000002},
        {100000001,200000002},
        {1000000001,2000000002},
        {Long.MAX_VALUE}
    };
    
    static final float[][] floatValues = new float[][]{
        {1,2},
        {10,20},
        {101,202},
        {1001,2002},
        {10001,20002},
        {100001,200002},
        {1000001,2000002},
        {10000001,20000002},
        {100000001,200000002},
        {1000000001,2000000002},
        {Float.MAX_VALUE}
    };
    
    static final double[][] doubleValues = new double[][]{
        {1,2},
        {10,20},
        {101,202},
        {1001,2002},
        {10001,20002},
        {100001,200002},
        {1000001,2000002},
        {10000001,20000002},
        {100000001,200000002},
        {1000000001,2000000002},
        {Double.MIN_VALUE}
    };
    
    public void testBool() throws Exception
    {
        int num = 1;
        boolean value = true;
        int valueSize = 1;
        int tag = WireFormat.makeTag(num, FieldType.BOOL.wireType);
        int tagSize = CodedOutput.computeRawVarint32Size(tag);
        int expect = tagSize + valueSize;
        
        assertSize("boolean1", CodedOutput.computeBoolSize(num, value), expect);
        assertSize("boolean2", CodedOutput.getTagAndRawVarInt32Bytes(tag, 1).length, expect);
    }
    
    public void testRawVarInt32Bytes() throws Exception
    {
        String n1 = "int1", n2 = "int2";
        for(int i=0; i<int32values.length;)
        {
            int[] inner = int32values[i++];
            int num = i;
            for(int j=0; j<inner.length; j++)
            {
                int value = inner[j];
                int valueSize = CodedOutput.computeRawVarint32Size(value);
                int tag = WireFormat.makeTag(num, FieldType.INT32.wireType);
                int tagSize = CodedOutput.computeRawVarint32Size(tag);
                int expect = tagSize + valueSize;
                
                assertSize(n1, CodedOutput.computeInt32Size(num, value), expect);
                assertSize(n2, CodedOutput.getTagAndRawVarInt32Bytes(tag, value).length, expect);
            }            
        }

    }
    
    public void testRawVarInt64Bytes() throws Exception
    {
        String n1 = "long1", n2 = "long2";
        for(int i=0; i<int64values.length;)
        {
            long[] inner = int64values[i++];
            int num = i;
            for(int j=0; j<inner.length; j++)
            {
                long value = inner[j];
                int valueSize = CodedOutput.computeRawVarint64Size(value);
                int tag = WireFormat.makeTag(num, FieldType.INT64.wireType);
                int tagSize = CodedOutput.computeRawVarint32Size(tag);
                int expect = tagSize + valueSize;
                
                assertSize(n1, CodedOutput.computeInt64Size(num, value), expect);
                assertSize(n2, CodedOutput.getTagAndRawVarInt64Bytes(tag, value).length, expect);
            }            
        }
    }
    
    public void testRawLittleEndian32Bytes() throws Exception
    {
        String n1 = "float1", n2 = "float2";
        for(int i=0; i<floatValues.length;)
        {
            float[] inner = floatValues[i++];
            int num = i;
            for(int j=0; j<inner.length; j++)
            {
                int value = Float.floatToRawIntBits(inner[j]);
                int valueSize = CodedOutput.LITTLE_ENDIAN_32_SIZE;
                int tag = WireFormat.makeTag(num, FieldType.FLOAT.wireType);
                int tagSize = CodedOutput.computeRawVarint32Size(tag);
                int expect = tagSize + valueSize;
                
                assertSize(n1, CodedOutput.computeFloatSize(num, inner[j]), expect);
                assertSize(n2, CodedOutput.getTagAndRawLittleEndian32Bytes(tag, value).length, expect);
            }            
        }
    }
    
    public void testRawLittleEndian64Bytes() throws Exception
    {
        String n1 = "double1", n2 = "double2";
        for(int i=0; i<doubleValues.length;)
        {
            double[] inner = doubleValues[i++];
            int num = i;
            for(int j=0; j<inner.length; j++)
            {
                long value = Double.doubleToRawLongBits(inner[j]);
                int tag = WireFormat.makeTag(num, FieldType.DOUBLE.wireType);
                int tagSize = CodedOutput.computeRawVarint32Size(tag);
                int expect = tagSize + CodedOutput.LITTLE_ENDIAN_64_SIZE;
                
                assertSize(n1, CodedOutput.computeDoubleSize(num, inner[j]), expect);
                assertSize(n2, CodedOutput.getTagAndRawLittleEndian64Bytes(tag, value).length, expect);
            }            
        }
    }
    
    static void assertSize(String name, int size1, int size2)
    {
        //System.err.println(size1 + " == " + size2 + " " + name);
        assertTrue(size1 == size2);
    }

}
