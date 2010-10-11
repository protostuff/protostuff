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
import java.util.List;

/**
 * Json serialization via {@link JsonXOutput}.
 * 
 *
 * @author David Yu
 * @created Jul 2, 2010
 */
public final class JsonXIOUtil
{
    
    private static final byte[] EMPTY_ARRAY = new byte[]{
        (byte)'[', (byte)']'
    };
    
    public static <T> byte[] toByteArray(T message, Schema<T> schema, boolean numeric, 
            LinkedBuffer buffer)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final JsonXOutput output = new JsonXOutput(buffer, numeric, schema);
        
        try
        {
            output.writeStartObject();
            
            schema.writeTo(output, message);
            
            if(output.isLastRepeated())
                output.writeEndArray();
            
            output.writeEndObject();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.toByteArray();
    }
    
    /**
     * Serializes the {@code message} into a {@link LinkedBuffer} via {@link JsonXOutput} 
     * using the given {@code schema} with the supplied buffer.
     */
    public static <T> void writeTo(LinkedBuffer buffer, T message, Schema<T> schema, 
            boolean numeric)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final JsonXOutput output = new JsonXOutput(buffer, numeric, schema);
        
        try
        {
            output.writeStartObject();
            
            schema.writeTo(output, message);
            
            if(output.isLastRepeated())
                output.writeEndArray();
            
            output.writeEndObject();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link JsonXOutput} with the 
     * supplied buffer.
     */
    public static <T extends Message<T>> void writeTo(OutputStream out, T message, boolean numeric, 
            LinkedBuffer buffer)
    throws IOException
    {
        writeTo(out, message, message.cachedSchema(), numeric, buffer);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link JsonXOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, boolean numeric, 
            LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final JsonXOutput output = new JsonXOutput(buffer, out, numeric, schema);

        output.writeStartObject();
        
        schema.writeTo(output, message);
        
        if(output.isLastRepeated())
            output.writeEndArray();
        
        output.writeEndObject();
        
        LinkedBuffer.writeTo(out, buffer);
    }
    
    /**
     * Serializes the {@code messages} into the {@link LinkedBuffer} using the given schema.
     */
    public static <T> void writeListTo(LinkedBuffer buffer, List<T> messages, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        if(messages.isEmpty())
        {
            System.arraycopy(EMPTY_ARRAY, 0, buffer.buffer, buffer.offset, EMPTY_ARRAY.length);
            buffer.offset += EMPTY_ARRAY.length;
            return;
        }
        
        final JsonXOutput output = new JsonXOutput(buffer, numeric, schema);
        
        output.writeStartArray();
        
        boolean first = true;
        for(T m : messages)
        {
            if(first)
            {
                first = false;
                output.writeStartObject();
            }
            else
                output.writeCommaAndStartObject();
            
            schema.writeTo(output, m);
            if(output.isLastRepeated())
                output.writeEndArray();
            
            output.writeEndObject().clear(false);
        }
        
        output.writeEndArray();
    }
    
    /**
     * Serializes the {@code messages} into the stream using the given schema with the 
     * supplied buffer.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        if(messages.isEmpty())
        {
            System.arraycopy(EMPTY_ARRAY, 0, buffer.buffer, buffer.offset, EMPTY_ARRAY.length);
            buffer.offset += EMPTY_ARRAY.length;
            return;
        }
        
        final JsonXOutput output = new JsonXOutput(buffer, out, numeric, schema);
        
        output.writeStartArray();
        
        boolean first = true;
        for(T m : messages)
        {
            if(first)
            {
                first = false;
                output.writeStartObject();
            }
            else
                output.writeCommaAndStartObject();
            
            schema.writeTo(output, m);
            if(output.isLastRepeated())
                output.writeEndArray();
            
            output.writeEndObject().clear(false);
        }
        
        output.writeEndArray();
        LinkedBuffer.writeTo(out, buffer);
    }

}
