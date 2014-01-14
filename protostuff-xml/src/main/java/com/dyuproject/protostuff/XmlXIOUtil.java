//========================================================================
//Copyright 2012 David Yu
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
 * IO Utils for writing xml via {@link XmlXOutput}.
 * 
 * @author David Yu
 * @created Aug 30, 2012
 */
public final class XmlXIOUtil
{
    private XmlXIOUtil() {}
    
    static final byte[] HEADER = "<?xml version='1.0' encoding='UTF-8'?>".getBytes();
    
    
    /**
     * Serializes the {@code message} into a byte array using the given schema.
     * 
     * @return the byte array containing the data.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final XmlXOutput output = new XmlXOutput(buffer, schema);
        
        final String name = schema.messageName();
        try
        {
            // header and start root element
            output.tail = output.sink.writeByte(XmlXOutput.END_TAG, output, 
                    output.sink.writeStrAscii(name, output, 
                            output.sink.writeByte(XmlXOutput.START_TAG, output, 
                                    output.sink.writeByteArray(HEADER, output, output.tail))));
            
            schema.writeTo(output, message);
            
            // end root element
            output.tail = output.sink.writeByte(XmlXOutput.END_TAG, output, 
                    output.sink.writeStrAscii(name, output, 
                            output.sink.writeByteArray(XmlXOutput.START_SLASH_TAG, output, output.tail)));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.toByteArray();
    }
    
    /**
     * Writes the {@code message} into the {@link LinkedBuffer} using the given schema.
     * 
     * @return the size of the message
     */
    public static <T> int writeTo(LinkedBuffer buffer, T message, Schema<T> schema)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final XmlXOutput output = new XmlXOutput(buffer, schema);
        
        final String name = schema.messageName();
        try
        {
            // header and start root element
            output.tail = output.sink.writeByte(XmlXOutput.END_TAG, output, 
                    output.sink.writeStrAscii(name, output, 
                            output.sink.writeByte(XmlXOutput.START_TAG, output, 
                                    output.sink.writeByteArray(HEADER, output, output.tail))));
            
            schema.writeTo(output, message);
            
            // end root element
            output.tail = output.sink.writeByte(XmlXOutput.END_TAG, output, 
                    output.sink.writeStrAscii(name, output, 
                            output.sink.writeByteArray(XmlXOutput.START_SLASH_TAG, output, output.tail)));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a LinkedBuffer threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.getSize();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} using the given schema.
     * 
     * @return the size of the message
     */
    public static <T> int writeTo(final OutputStream out, final T message, 
            final Schema<T> schema, final LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final XmlXOutput output = new XmlXOutput(buffer, out, schema);
        
        final String name = schema.messageName();
        
        // header and start root element
        output.tail = output.sink.writeByte(XmlXOutput.END_TAG, output, 
                output.sink.writeStrAscii(name, output, 
                        output.sink.writeByte(XmlXOutput.START_TAG, output, 
                                output.sink.writeByteArray(HEADER, output, output.tail))));
        
        schema.writeTo(output, message);
        
        // end root element
        output.tail = output.sink.writeByte(XmlXOutput.END_TAG, output, 
                output.sink.writeStrAscii(name, output, 
                        output.sink.writeByteArray(XmlXOutput.START_SLASH_TAG, output, output.tail)));
        
        LinkedBuffer.writeTo(out, buffer);
        
        return output.size;
    }

}
