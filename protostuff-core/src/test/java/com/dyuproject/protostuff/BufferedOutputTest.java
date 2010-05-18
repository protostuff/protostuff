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
import java.io.OutputStream;

/**
 * Test case for {@code BufferedOutput}.
 *
 * @author David Yu
 * @created May 18, 2010
 */
public class BufferedOutputTest extends SerDeserTest
{
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        BufferedOutput output = new BufferedOutput();
        try
        {
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.toByteArray();
    }
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    public <T extends Message<T>> byte[] toByteArray(T message)
    {
        return toByteArray(message, message.cachedSchema());
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput}.
     */
    public <T extends Message<T>> void writeDelimitedTo(OutputStream out, T message)
    throws IOException
    {
        writeDelimitedTo(out, message, message.cachedSchema());
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema.
     */
    public <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
    throws IOException
    {
        BufferedOutput output = new BufferedOutput();
        schema.writeTo(output, message);
        CodedOutput.writeRawVarInt32Bytes(out, output.getSize());
        output.streamTo(out);
    }
    
    /*static void print(byte[] data)
    {
        System.err.println("_______");
        for(int i=0; i<data.length; i++)
        {
            System.err.print((int)data[i] + " ");
        }
        System.err.println("(" + data.length + ")");
    }*/


}
