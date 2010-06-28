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
     * supplied buffer size.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, int bufferSize)
    {
        final LinkedBuffer buffer = new LinkedBuffer(bufferSize);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        final byte[] tag = YamlOutput.makeTag(schema.messageName(), false);
        System.arraycopy(tag, 0, buffer.buffer, START_DIRECTIVE.length, tag.length);
        
        buffer.offset = START_DIRECTIVE.length + tag.length;
        
        final YamlOutput output = new YamlOutput(buffer, null, schema);
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
     * Serializes the {@code message} into a byte array via {@link YamlOutput}.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return toByteArray(message, schema, YamlOutput.DEFAULT_BUFFER_SIZE);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link YamlOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArray(T message)
    {
        return toByteArray(message, message.cachedSchema(), YamlOutput.DEFAULT_BUFFER_SIZE);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link YamlOutput} 
     * with the supplied buffer size.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, int bufferSize) 
    throws IOException
    {
        final LinkedBuffer buffer = new LinkedBuffer(bufferSize);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        final byte[] tag = YamlOutput.makeTag(schema.messageName(), false);
        System.arraycopy(tag, 0, buffer.buffer, START_DIRECTIVE.length, tag.length);
        
        buffer.offset = START_DIRECTIVE.length + tag.length;
        
        final YamlOutput output = new YamlOutput(buffer, out, schema);
        
        schema.writeTo(output, message);

        output.flushRemaining();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link YamlOutput}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema)
    throws IOException
    {
        writeTo(out, message, schema, YamlOutput.DEFAULT_BUFFER_SIZE);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link YamlOutput}.
     */
    public static <T extends Message<T>> void writeTo(OutputStream out, T message)
    throws IOException
    {
        writeTo(out, message, message.cachedSchema(), YamlOutput.DEFAULT_BUFFER_SIZE);
    }
    
    /**
     * Serializes the {@code messages} (delimited) into 
     * an {@link OutputStream} via {@link YamlOutput} using the given schema 
     * with the supplied buffer size.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            int bufferSize) throws IOException
    {
        final LinkedBuffer buffer = new LinkedBuffer(bufferSize);
        
        System.arraycopy(START_DIRECTIVE, 0, buffer.buffer, 0, START_DIRECTIVE.length);
        
        final byte[] tag = YamlOutput.makeTag(schema.messageName(), true);
        System.arraycopy(tag, 0, buffer.buffer, START_DIRECTIVE.length, tag.length);
        
        buffer.offset = START_DIRECTIVE.length + tag.length;
        
        final YamlOutput output = new YamlOutput(buffer, out, schema);
        for(T m : messages)
        {
            schema.writeTo(output.writeSequenceDelim(), m);
            output.flushRemaining().use(schema);
        }
    }
    
    /**
     * Serializes the {@code messages} (delimited) into 
     * an {@link OutputStream} via {@link YamlOutput} using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema) 
    throws IOException
    {
        writeListTo(out, messages, schema, YamlOutput.DEFAULT_BUFFER_SIZE);
    }
    

}
