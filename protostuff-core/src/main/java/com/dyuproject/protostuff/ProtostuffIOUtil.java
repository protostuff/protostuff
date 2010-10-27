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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Protostuff ser/deser util for messages/objects.
 *
 * @author David Yu
 * @created Sep 20, 2010
 */
public final class ProtostuffIOUtil
{
    
    private ProtostuffIOUtil() {}
    
    /**
     * Creates a protostuff pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data)
    {
        return newPipe(data, 0, data.length);
    }
    
    /**
     * Creates a protostuff pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, int offset, int len)
    {
        final ByteArrayInput byteArrayInput = new ByteArrayInput(data, offset, len, true);
        return new Pipe()
        {
            protected Input begin(Pipe.Schema<?> pipeSchema) throws IOException
            {
                return byteArrayInput;
            }
            protected void end(Pipe.Schema<?> pipeSchema, Input input) throws IOException
            {
                assert input == byteArrayInput;
            }
        };
    }
    
    /**
     * Creates a protostuff pipe from an {@link InputStream}.
     */
    public static Pipe newPipe(final InputStream in)
    {
        final CodedInput codedInput = new CodedInput(in, true);
        return new Pipe()
        {
            protected Input begin(Pipe.Schema<?> pipeSchema) throws IOException
            {
                return codedInput;
            }
            protected void end(Pipe.Schema<?> pipeSchema, Input input) throws IOException
            {
                assert input == codedInput;
            }
        };
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema)
    {
        IOUtil.mergeFrom(data, 0, data.length, message, schema, true);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema)
    {
        IOUtil.mergeFrom(data, offset, length, message, schema, true);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using 
     * the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema) 
    throws IOException
    {
        IOUtil.mergeFrom(in, message, schema, true);
    }
    
    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     */
    public static <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema) 
    throws IOException
    {
        IOUtil.mergeDelimitedFrom(in, message, schema, true);
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Merges from the {@link DataInput}.
     */
    public static <T> void mergeDelimitedFrom(DataInput in, T message, Schema<T> schema) 
    throws IOException
    {
        IOUtil.mergeDelimitedFrom(in, message, schema, true);
    }
    
    /**
     * Serializes the {@code message} into a byte array using the given schema.
     * 
     * @return the byte array containing the data.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtostuffOutput output = new ProtostuffOutput(buffer);
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
     * Writes the {@code message} into the {@link LinkedBuffer} using the given schema.
     * 
     * @return the size of the message
     */
    public static <T> int writeTo(LinkedBuffer buffer, T message, Schema<T> schema)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtostuffOutput output = new ProtostuffOutput(buffer);
        try
        {
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
     * Serializes the {@code message} into an {@link OutputStream} using the given schema.
     * 
     * @return the size of the message
     */
    public static <T> int writeTo(final OutputStream out, final T message, 
            final Schema<T> schema, final LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtostuffOutput output = new ProtostuffOutput(buffer, out);
        schema.writeTo(output, message);
        LinkedBuffer.writeTo(out, buffer);
        return output.size;
    }
    
    /**
     * Serializes the {@code message}, prefixed with its length, into an 
     * {@link OutputStream}.
     * 
     * @return the size of the message
     */
    public static <T> int writeDelimitedTo(final OutputStream out, final T message, 
            final Schema<T> schema, final LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtostuffOutput output = new ProtostuffOutput(buffer);
        schema.writeTo(output, message);
        CodedOutput.writeRawVarInt32Bytes(out, output.size);
        LinkedBuffer.writeTo(out, buffer);
        return output.size;
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Writes to the {@link DataOutput}.
     * 
     * @return the size of the message.
     */
    public static <T> int writeDelimitedTo(DataOutput out, T message, Schema<T> schema) 
    throws IOException
    {
        return IOUtil.writeDelimitedTo(out, message, schema, true);
    }
    
    /**
     * Serializes the {@code messages} (delimited) into an {@link OutputStream} 
     * using the given schema.
     * 
     * @return the bytes written
     */
    public static <T> int writeListTo(final OutputStream out, final List<T> messages, 
            final Schema<T> schema, final LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final int size = messages.size();
        if(size == 0)
            return 0;
        
        final ProtostuffOutput output = new ProtostuffOutput(buffer, out);
        output.sink.writeVarInt32(size, output, buffer);

        for(T m : messages)
        {
            schema.writeTo(output, m);
            output.sink.writeByte((byte)WireFormat.WIRETYPE_TAIL_DELIMITER, output, 
                    buffer);
        }
        
        LinkedBuffer.writeTo(out, buffer);
        
        return output.size;
    }
    
    /**
     * Parses the {@code messages} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     * 
     * @return the list containing the messages.
     */
    public static <T> List<T> parseListFrom(final InputStream in, final Schema<T> schema) 
    throws IOException
    {
        int size = in.read();
        if(size == -1)
            return Collections.emptyList();
        
        if(size > 0x7f)
            size = CodedInput.readRawVarint32(in, size);
        
        final ArrayList<T> list = new ArrayList<T>(size);
        final CodedInput input = new CodedInput(in, true);
        for(int i = 0; i < size; i++)
        {
            final T message = schema.newMessage();
            list.add(message);
            schema.mergeFrom(input, message);
            input.checkLastTagWas(0);
        }
        
        assert in.read() == -1;

        return list;
    }

}
