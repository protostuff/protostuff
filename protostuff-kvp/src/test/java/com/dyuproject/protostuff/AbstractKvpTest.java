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


/**
 * Base class for the kvp io tests.
 *
 * @author David Yu
 * @created Dec 4, 2010
 */
public abstract class AbstractKvpTest extends NoNestedMessageTest
{
    
    protected final boolean numeric;

    public AbstractKvpTest(boolean numeric)
    {
        this.numeric = numeric;
    }
    
    protected <T> void mergeFrom(byte[] data, int offset, int length, 
            T message, Schema<T> schema) throws IOException
    {
        try
        {
            schema.mergeFrom(new KvpByteArrayInput(data, offset, length, numeric), 
                    message);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new ProtostuffException("Truncated message.", e);
        }
    }

    protected <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        final KvpOutput output = new KvpOutput(buf(), schema, numeric);
        try
        {
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        byte[] data = output.toByteArray();
        //System.err.println(data.length + " | " + getClass().getSimpleName());
        return data;
    }
    
}
