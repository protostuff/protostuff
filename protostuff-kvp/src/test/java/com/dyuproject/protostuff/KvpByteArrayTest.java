//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


/**
 * A test for deserializing byte array fields that exceed the input's buffer size.
 *
 * @author David Yu
 * @created Jun 21, 2011
 */
public class KvpByteArrayTest extends AbstractTest
{
    
    static class PojoWithBiggerByteArray
    {
        int id;
        byte[] b;
        long ts;
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(b);
            result = prime * result + id;
            result = prime * result + (int)(ts ^ (ts >>> 32));
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoWithBiggerByteArray other = (PojoWithBiggerByteArray)obj;
            if (!Arrays.equals(b, other.b))
                return false;
            if (id != other.id)
                return false;
            if (ts != other.ts)
                return false;
            return true;
        }
        
        
    }
    
    static final Schema<PojoWithBiggerByteArray> SCHEMA = new Schema<PojoWithBiggerByteArray>()
    {

        final HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
        {
            fieldMap.put("id", 1);
            fieldMap.put("b", 2);
            fieldMap.put("ts", 3);
        }
        
        public String getFieldName(int number)
        {
            switch(number)
            {
                case 1:
                    return "id";
                case 2:
                    return "b";
                case 3:
                    return "ts";
                default:
                    return null;
            }
        }

        public int getFieldNumber(String name)
        {
            final Integer f = fieldMap.get(name);
            return f == null ? 0 : f.intValue();
        }

        public boolean isInitialized(PojoWithBiggerByteArray message)
        {
            return true;
        }

        public String messageFullName()
        {
            return PojoWithBiggerByteArray.class.getName();
        }

        public String messageName()
        {
            return PojoWithBiggerByteArray.class.getSimpleName();
        }

        public PojoWithBiggerByteArray newMessage()
        {
            return new PojoWithBiggerByteArray();
        }

        public Class<? super PojoWithBiggerByteArray> typeClass()
        {
            return PojoWithBiggerByteArray.class;
        }

        public void mergeFrom(Input input, PojoWithBiggerByteArray message) throws IOException
        {
            for(int number = input.readFieldNumber(this); ; number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        message.id = input.readInt32();
                        break;
                    case 2:
                        message.b = input.readByteArray();
                        break;
                    case 3:
                        message.ts = input.readInt64();
                        break;
                    default:
                        input.handleUnknownField(number, this);
                }
            }
        }

        public void writeTo(Output output, PojoWithBiggerByteArray message) throws IOException
        {
            output.writeInt32(1, message.id, false);
            output.writeByteArray(2, message.b, false);
            output.writeInt64(3, message.ts, false);
        }
        
    };

    
    public void testBiggerByteArray() throws Exception
    {
        int bufSize = 256;
        boolean numeric = false;
        Schema<PojoWithBiggerByteArray> schema = SCHEMA;
        
        PojoWithBiggerByteArray message = new PojoWithBiggerByteArray();
        message.id = 1;
        message.b = new byte[bufSize*3];
        for(int i=0; i<message.b.length; i++)
            message.b[i] = (byte)(i % message.b.length);
        
        message.ts = System.currentTimeMillis();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer buffer = LinkedBuffer.allocate(bufSize);
        final KvpOutput output = new KvpOutput(buffer, out, schema, numeric);
        try
        {
            schema.writeTo(output, message);
            LinkedBuffer.writeTo(out, buffer);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        byte[] data = out.toByteArray();
        
        PojoWithBiggerByteArray m2 = new PojoWithBiggerByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(data, 0, data.length);
        try
        {
            schema.mergeFrom(new KvpInput(in, new byte[bufSize], numeric), m2);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new ProtostuffException("Truncated message.", e);
        }
        
        assertEquals(message, m2);
    }

}
