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

package com.dyuproject.protostuff.me;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Protobuf ser/deser util for messages/objects.
 *
 * @author David Yu
 * @created Oct 5, 2010
 */
public final class ProtobufIOUtil
{

    private ProtobufIOUtil() {}
    
    /**
     * Creates a protobuf pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data)
    {
        return newPipe(data, 0, data.length);
    }
    
    /**
     * Creates a protobuf pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, int offset, int len)
    {
        final ByteArrayInput byteArrayInput = new ByteArrayInput(data, offset, len, false);
        return new Pipe()
        {
            protected Input begin(Pipe.Schema pipeSchema) throws IOException
            {
                return byteArrayInput;
            }
            protected void end(Pipe.Schema pipeSchema, Input input, 
                    boolean cleanupOnly) throws IOException
            {
                if(cleanupOnly)
                    return;
                
                //assert input == byteArrayInput;
            }
        };
    }
    
    /**
     * Creates a protobuf pipe from an {@link InputStream}.
     */
    public static Pipe newPipe(final InputStream in)
    {
        final CodedInput codedInput = new CodedInput(in, false);
        return new Pipe()
        {
            protected Input begin(Pipe.Schema pipeSchema) throws IOException
            {
                return codedInput;
            }
            protected void end(Pipe.Schema pipeSchema, Input input, 
                    boolean cleanupOnly) throws IOException
            {
                if(cleanupOnly)
                    return;
                
                //assert input == codedInput;
            }
        };
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static void mergeFrom(byte[] data, Object message, Schema schema)
    {
        IOUtil.mergeFrom(data, 0, data.length, message, schema, false);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static void mergeFrom(byte[] data, int offset, int length, Object message, 
            Schema schema)
    {
        IOUtil.mergeFrom(data, offset, length, message, schema, false);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using 
     * the given {@code schema}.
     */
    public static void mergeFrom(InputStream in, Object message, Schema schema) 
    throws IOException
    {
        IOUtil.mergeFrom(in, message, schema, false);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using 
     * the given {@code schema}.
     * 
     * The {@code buffer}'s internal byte array will be used for reading the message.
     */
    public static void mergeFrom(InputStream in, Object message, Schema schema, 
            LinkedBuffer buffer) throws IOException
    {
        IOUtil.mergeFrom(in, buffer.buffer, message, schema, false);
    }
    
    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     */
    public static void mergeDelimitedFrom(InputStream in, Object message, Schema schema) 
    throws IOException
    {
        IOUtil.mergeDelimitedFrom(in, message, schema, false);
    }
    
    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     * 
     * The delimited message size must not be larger than the 
     * {@code buffer}'s size/capacity.
     * {@link ProtobufException} "size limit exceeded" is thrown otherwise.
     */
    public static void mergeDelimitedFrom(InputStream in, Object message, Schema schema, 
            LinkedBuffer buffer) throws IOException
    {
        IOUtil.mergeDelimitedFrom(in, buffer.buffer, message, schema, false);
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Merges from the {@link DataInput}.
     */
    public static void mergeDelimitedFrom(DataInput in, Object message, Schema schema) 
    throws IOException
    {
        IOUtil.mergeDelimitedFrom(in, message, schema, false);
    }
    
    /**
     * Serializes the {@code message} into a byte array using the given schema.
     * 
     * @return the byte array containing the data.
     */
    public static byte[] toByteArray(Object message, Schema schema, LinkedBuffer buffer)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtobufOutput output = new ProtobufOutput(buffer);
        try
        {
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).");
        }
        
        return output.toByteArray();
    }
    
    /**
     * Writes the {@code message} into the {@link LinkedBuffer} using the given schema.
     * 
     * @return the size of the message
     */
    public static int writeTo(LinkedBuffer buffer, Object message, Schema schema)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtobufOutput output = new ProtobufOutput(buffer);
        try
        {
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a LinkedBuffer threw an IOException " + 
                    "(should never happen).");
        }
        
        return output.getSize();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} using the given schema.
     * 
     * @return the size of the message
     */
    public static int writeTo(OutputStream out, Object message, Schema schema, 
            LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtobufOutput output = new ProtobufOutput(buffer);
        schema.writeTo(output, message);
        return LinkedBuffer.writeTo(out, buffer);
    }
    
    /**
     * Serializes the {@code message}, prefixed with its length, into an 
     * {@link OutputStream}.
     * 
     * @return the size of the message
     */
    public static int writeDelimitedTo(OutputStream out, Object message, Schema schema, 
            LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtobufOutput output = new ProtobufOutput(buffer);
        schema.writeTo(output, message);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        LinkedBuffer.writeTo(out, buffer);
        //final int msgSize = LinkedBuffer.writeTo(out, buffer);
        //assert size == msgSize;
        
        return size;
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Writes to the {@link DataOutput}.
     * 
     * @return the size of the message.
     */
    public static int writeDelimitedTo(DataOutput out, Object message, Schema schema) 
    throws IOException
    {
        final LinkedBuffer buffer = new LinkedBuffer(LinkedBuffer.MIN_BUFFER_SIZE);
        final ProtobufOutput output = new ProtobufOutput(buffer);
        schema.writeTo(output, message);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        
        LinkedBuffer.writeTo(out, buffer);
        //final int msgSize = LinkedBuffer.writeTo(out, buffer);
        //assert size == msgSize;
        
        return size;
    }
    
    /**
     * Serializes the {@code messages} (delimited) into an {@link OutputStream} 
     * using the given schema.
     * 
     * @return the total size of the messages (excluding the length prefix varint)
     */
    public static int writeListTo(OutputStream out, Vector messages, Schema schema, 
            LinkedBuffer buffer) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final ProtobufOutput output = new ProtobufOutput(buffer);
        int totalSize = 0;
        for(int i = 0; i < messages.size(); i++)
        {
        	Object m = messages.elementAt(i);
            schema.writeTo(output, m);
            final int size = output.getSize();
            ProtobufOutput.writeRawVarInt32Bytes(out, size);
            LinkedBuffer.writeTo(out, buffer);
            //final int msgSize = LinkedBuffer.writeTo(out, buffer);
            //assert size == msgSize;
            
            totalSize += size;
            output.clear();
        }
        return totalSize;
    }
    
    /**
     * Parses the {@code messages} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     * 
     * @return the list containing the messages.
     */
    public static Vector parseListFrom(InputStream in, Schema schema) throws IOException
    {
        final Vector list = new Vector();
        byte[] buf = null;
        int biggestLen = 0;
        LimitedInputStream lin = null;
        for(int size=in.read(); size!=-1; size=in.read())
        {
            final Object message = schema.newMessage();
            list.addElement(message);
            final int len = size < 0x80 ? size : CodedInput.readRawVarint32(in, size);
            if(len != 0)
            {
                // not an empty message
                if(len > CodedInput.DEFAULT_BUFFER_SIZE)
                {
                    // message too big
                    if(lin == null)
                        lin = new LimitedInputStream(in);
                    final CodedInput input = new CodedInput(lin.limit(len), false);
                    schema.mergeFrom(input, message);
                    input.checkLastTagWas(0);
                    continue;
                }
                
                if(biggestLen < len)
                {
                    // cannot reuse buffer, allocate a bigger buffer
                    // discard the last one for gc
                    buf = new byte[len];
                    biggestLen = len;
                }
                IOUtil.fillBufferFrom(in, buf, 0, len);
                final ByteArrayInput input = new ByteArrayInput(buf, 0, len, false);
                try
                {
                    schema.mergeFrom(input, message);
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    throw ProtobufException.truncatedMessage(e);
                }
                input.checkLastTagWas(0);
            }
        }
        return list;
    }
    
}
