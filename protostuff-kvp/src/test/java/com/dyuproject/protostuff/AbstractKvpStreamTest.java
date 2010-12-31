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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Test for reading/writing from stream.
 *
 * @author David Yu
 * @created Dec 4, 2010
 */
public abstract class AbstractKvpStreamTest extends NoNestedMessageTest
{
    
    protected final boolean numeric;
    
    public AbstractKvpStreamTest(boolean numeric)
    {
        this.numeric = numeric;
    }
    
    protected <T> void mergeFrom(byte[] data, int offset, int length, 
            T message, Schema<T> schema) throws IOException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data, offset, length);
        try
        {
            schema.mergeFrom(new KvpInput(in, numeric), message);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new ProtostuffException("Truncated message.", e);
        }
    }

    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer buffer = buf();
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
        //System.err.println(data.length + " | " + getClass().getSimpleName());
        return data;
    }

}
