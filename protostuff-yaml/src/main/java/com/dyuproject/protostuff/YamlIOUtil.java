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
 * Utility for the YAML serialization of messages and objects tied to a schema.
 *
 * @author David Yu
 * @created Jun 28, 2010
 */
public final class YamlIOUtil
{
    
    private static final byte[] START_DIRECTIVE = new byte[]{
        (byte)'-', (byte)'-', (byte)'-', (byte)' '
    };
    
    /**
     * Serializes the {@code message} into a byte array via {@link YamlOutput} with the 
     * supplied buffer.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final YamlOutput output = new YamlOutput(buffer, null, schema);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        buffer.offset += START_DIRECTIVE.length;
        
        output.size += START_DIRECTIVE.length;

        try
        {
            output.tail = YamlOutput.writeTag(schema.messageName(), false, null, output, buffer);
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
     * Serializes the {@code message} into a byte array via {@link YamlOutput} with the 
     * supplied buffer.
     * 
     */
    public static <T extends Message<T>> byte[] toByteArray(T message, LinkedBuffer buffer)
    {
        return toByteArray(message, message.cachedSchema(), buffer);
    }
    
    /**
     * Serializes the {@code message} into the {@link LinkedBuffer} via {@link YamlOutput}.
     * 
     * @return the total bytes written to the output.
     */
    public static <T> int writeTo(LinkedBuffer buffer, T message, Schema<T> schema) 
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final YamlOutput output = new YamlOutput(buffer, null, schema);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        buffer.offset += START_DIRECTIVE.length;
        
        output.size += START_DIRECTIVE.length;

        try
        {
            output.tail = YamlOutput.writeTag(schema.messageName(), false, null, output, buffer);
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a LinkedBuffer threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.getSize();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link YamlOutput} 
     * with the supplied buffer.
     * 
     * @return the total bytes written to the output.
     */
    public static <T> int writeTo(OutputStream out, T message, Schema<T> schema, 
            LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final YamlOutput output = new YamlOutput(buffer, out, schema);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        buffer.offset += START_DIRECTIVE.length;
        
        output.size += START_DIRECTIVE.length;
        
        output.tail = YamlOutput.writeTag(schema.messageName(), false, out, output, buffer);
        
        schema.writeTo(output, message);

        output.flushRemaining();
        
        return output.getSize();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link YamlOutput} with 
     * the supplied buffer.
     * 
     * @return the total bytes written to the output.
     */
    public static <T extends Message<T>> int writeTo(OutputStream out, T message, 
            LinkedBuffer buffer) throws IOException
    {
        return writeTo(out, message, message.cachedSchema(), buffer);
    }
    
    /**
     * Serializes the {@code messages} a {@link LinkedBuffer} via {@link YamlOutput} 
     * using the given schema.
     * 
     * @return the total bytes written to the output.
     */
    public static <T> int writeListTo(LinkedBuffer buffer, List<T> messages, Schema<T> schema) 
    throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final YamlOutput output = new YamlOutput(buffer, null, schema);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        buffer.offset += START_DIRECTIVE.length;
        
        output.size += START_DIRECTIVE.length;
        
        output.tail = YamlOutput.writeTag(schema.messageName(), true, null, output, buffer);
        
        try
        {
            for(T m : messages)
            {
                schema.writeTo(output.writeSequenceDelim(), m);
                output.clear(false, false);
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException("Serializing to a LinkedBuffer threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.getSize();
    }
    
    /**
     * Serializes the {@code messages} into an {@link OutputStream} via {@link YamlOutput} 
     * using the given schema with the supplied buffer.
     * 
     * @return the total bytes written to the output.
     */
    public static <T> int writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final YamlOutput output = new YamlOutput(buffer, out, schema);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        buffer.offset += START_DIRECTIVE.length;
        
        output.size += START_DIRECTIVE.length;
        
        output.tail = YamlOutput.writeTag(schema.messageName(), true, out, output, buffer);
        
        for(T m : messages)
        {
            schema.writeTo(output.writeSequenceDelim(), m);
            output.flushRemaining().clear(true, false);
        }
        
        return output.getSize();
    }

}
