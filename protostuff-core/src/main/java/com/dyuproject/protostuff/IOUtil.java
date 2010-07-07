//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for the serialization/deserialization of messages and objects tied to a schema.
 *
 * @author David Yu
 * @created Nov 12, 2009
 */
public final class IOUtil
{
    
    private IOUtil(){}
    
    /**
     * Serializes the {@code message} into a byte array via {@link BufferedOutput} with 
     * the supplied buffer.
     */
    public static <T extends Message<T>> byte[] toByteArray(T message, LinkedBuffer buffer)
    {
        return toByteArray(message, message.cachedSchema(), buffer, false);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link BufferedOutput} with the 
     * supplied buffer.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer)
    {
        return toByteArray(message, schema, buffer, false);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link BufferedOutput} with the 
     * supplied buffer.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer, 
            boolean encodeNestedMessageAsGroup)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final BufferedOutput output = new BufferedOutput(buffer, encodeNestedMessageAsGroup);
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
     * Writes the {@code message} into the {@link LinkedBuffer} via {@link BufferedOutput}.
     * 
     * @return the size of the message
     */
    public static <T> int writeTo(LinkedBuffer buffer, T message, Schema<T> schema, 
            boolean encodeNestedMessageAsGroup)
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final BufferedOutput output = new BufferedOutput(buffer, encodeNestedMessageAsGroup);
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
     * Serializes the {@code message} into an {@link OutputStream} via {@link BufferedOutput} 
     * with the supplied buffer.
     * 
     * @return the size of the message
     */
    public static <T> int writeTo(OutputStream out, T message, Schema<T> schema, 
            LinkedBuffer buffer) throws IOException
    {
        return writeTo(out, message, schema, buffer, false);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link BufferedOutput} 
     * with the supplied buffer.
     * 
     * @return the size of the message
     */
    public static <T> int writeTo(OutputStream out, T message, Schema<T> schema, 
            LinkedBuffer buffer, boolean encodeNestedMessageAsGroup) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final BufferedOutput output = new BufferedOutput(buffer, encodeNestedMessageAsGroup);
        schema.writeTo(output, message);
        return LinkedBuffer.writeTo(out, buffer);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link BufferedOutput} with 
     * the supplied buffer.
     * 
     * @return the size of the message.
     */
    public static <T extends Message<T>> int writeTo(OutputStream out, T message, 
            LinkedBuffer buffer) throws IOException
    {
        return writeTo(out, message, message.cachedSchema(), buffer, false);
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema 
     * with the supplied buffer.
     * 
     * @return the size of the message
     */
    public static <T> int writeDelimitedTo(OutputStream out, T message, Schema<T> schema, 
            LinkedBuffer buffer) throws IOException
    {
        return writeDelimitedTo(out, message, schema, buffer, false);
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema 
     * with the supplied buffer.
     * 
     * @return the size of the message
     */
    public static <T> int writeDelimitedTo(OutputStream out, T message, Schema<T> schema, 
            LinkedBuffer buffer, boolean encodeNestedMessageAsGroup) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final BufferedOutput output = new BufferedOutput(buffer, encodeNestedMessageAsGroup);
        schema.writeTo(output, message);
        final int size = output.getSize();
        CodedOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        
        assert size == msgSize;
        
        return size;
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} with the supplied buffer.
     * 
     * @return the size of the message
     */
    public static <T extends Message<T>> int writeDelimitedTo(OutputStream out, T message, 
            LinkedBuffer buffer) throws IOException
    {
        return writeDelimitedTo(out, message, message.cachedSchema(), buffer, false);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema)
    {
        mergeFrom(data, 0, data.length, message, schema, false);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema)
    {
        mergeFrom(data, offset, length, message, schema, false);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema, boolean decodeNestedMessageAsGroup)
    {
        try
        {
            final ByteArrayInput input = new ByteArrayInput(data, offset, length, 
                    decodeNestedMessageAsGroup);
            schema.mergeFrom(input, message);
            input.checkLastTagWas(0);
        }
        catch(ArrayIndexOutOfBoundsException ae)
        {
            throw new RuntimeException("Reading from a byte array threw an IOException (should " + 
                    "never happen).", ProtobufException.truncatedMessage(ae));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Reading from a byte array threw an IOException (should " + 
                    "never happen).",e);
        }
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    public static <T extends Message<T>> void mergeFrom(byte[] data, T message)
    {
        mergeFrom(data, 0, data.length, message, message.cachedSchema(), false);
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    public static <T extends Message<T>> void mergeFrom(byte[] data, int offset, int length, 
            T message)
    {
        mergeFrom(data, offset, length, message, message.cachedSchema(), false);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, 
            boolean decodeNestedMessageAsGroup) throws IOException
    {
        final CodedInput input = new CodedInput(in, decodeNestedMessageAsGroup);
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
    throws IOException
    {
        mergeFrom(in, message, schema, false);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream}.
     */
    public static <T extends Message<T>> void mergeFrom(InputStream in, T message) 
    throws IOException
    {
        mergeFrom(in, message, message.cachedSchema(), false);
    }
    
    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     */
    public static <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema) 
    throws IOException
    {
        mergeDelimitedFrom(in, message, schema, false);
    }
    
    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     */
    public static <T> void mergeDelimitedFrom(InputStream in, T message, Schema<T> schema, 
            boolean decodeNestedMessageAsGroup) throws IOException
    {
        final int size = in.read();
        if(size == -1)
            throw ProtobufException.truncatedMessage();
        
        final int len = (size & 0x80)==0 ? (size & 0x7f) : CodedInput.readRawVarint32(in, size);
        if(len != 0)
        {
            // not an empty message
            if(len > CodedInput.BUFFER_SIZE)
            {
                // message too big
                final CodedInput input = new CodedInput(new LimitedInputStream(in, len), 
                        decodeNestedMessageAsGroup);
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
                return;
            }
            
            byte[] buf = new byte[len];
            fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len, 
                    decodeNestedMessageAsGroup);
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
    
    /**
     * Merges the {@code message} (delimited) from the {@link InputStream}.
     */
    public static <T extends Message<T>> void mergeDelimitedFrom(InputStream in, T message) 
    throws IOException
    {
        mergeDelimitedFrom(in, message, message.cachedSchema(), false);
    }
    
    /**
     * Serializes the {@code messages} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema 
     * with the supplied buffer.
     * 
     * @return the total size of the messages
     */
    public static <T> int writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            LinkedBuffer buffer) throws IOException
    {
        return writeListTo(out, messages, schema, buffer, false);
    }
    
    /**
     * Serializes the {@code messages} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema 
     * with the supplied buffer.
     * 
     * @return the total size of the messages
     */
    public static <T> int writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            LinkedBuffer buffer, boolean encodeNestedMessageAsGroup) throws IOException
    {
        if(buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");
        
        final BufferedOutput output = new BufferedOutput(buffer, encodeNestedMessageAsGroup);
        int totalSize = 0;
        for(T m : messages)
        {
            schema.writeTo(output, m);
            final int size = output.getSize();
            CodedOutput.writeRawVarInt32Bytes(out, size);
            final int msgSize = LinkedBuffer.writeTo(out, buffer);
            
            assert size == msgSize;
            
            totalSize += size;
            output.clear();
        }
        return totalSize;
    }
    
    /**
     * Parses the {@code messages} (delimited) from the 
     * {@link InputStream} using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema) 
    throws IOException
    {
        return parseListFrom(in, schema, false);
    }
    
    /**
     * Parses the {@code messages} (delimited) from the 
     * {@link InputStream} using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, 
            boolean decodeNestedMessageAsGroup) throws IOException
    {
        final ArrayList<T> list = new ArrayList<T>();
        byte[] buf = null;
        int biggestLen = 0;
        LimitedInputStream lin = null;
        for(int size=in.read(); size!=-1; size=in.read())
        {
            final T message = schema.newMessage();
            list.add(message);
            final int len = (size & 0x80)==0 ? (size & 0x7f) : CodedInput.readRawVarint32(in, size);
            if(len != 0)
            {
                // not an empty message
                if(len > CodedInput.BUFFER_SIZE)
                {
                    // message too big
                    if(lin == null)
                        lin = new LimitedInputStream(in);
                    final CodedInput input = new CodedInput(lin.limit(len), 
                            decodeNestedMessageAsGroup);
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
                fillBufferFrom(in, buf, 0, len);
                final ByteArrayInput input = new ByteArrayInput(buf, 0, len, 
                        decodeNestedMessageAsGroup);
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
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Writes to the {@link ObjectOutput}.
     * 
     * @return the size of the message.
     */
    public static <T> int writeDelimitedTo(DataOutput out, T message, Schema<T> schema) 
    throws IOException
    {
        return writeDelimitedTo(out, message, schema, false);
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Writes to the {@link ObjectOutput}.
     * 
     * @return the size of the message.
     */
    public static <T> int writeDelimitedTo(DataOutput out, T message, Schema<T> schema, 
            boolean encodeNestedMessageAsGroup) throws IOException
    {
        final LinkedBuffer buffer = new LinkedBuffer(BufferedOutput.DEFAULT_BUFFER_SIZE);
        final BufferedOutput output = new BufferedOutput(buffer, encodeNestedMessageAsGroup);
        schema.writeTo(output, message);
        final int size = output.getSize();
        CodedOutput.writeRawVarInt32Bytes(out, size);
        
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        
        assert size == msgSize;
        
        return size;
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Merges from the {@link ObjectInput}.
     */
    public static <T> void mergeDelimitedFrom(DataInput in, T message, Schema<T> schema) 
    throws IOException
    {
        mergeDelimitedFrom(in, message, schema, false);
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Merges from the {@link ObjectInput}.
     */
    public static <T> void mergeDelimitedFrom(DataInput in, T message, Schema<T> schema, 
            boolean decodeNestedMessageAsGroup) throws IOException
    {
        final byte size = in.readByte();
        if(size == -1)
            throw ProtobufException.truncatedMessage();
        
        final int len = (size & 0x80)==0 ? size : CodedInput.readRawVarint32(in, size);
        
        if(len != 0)
        {
            // not an empty message
            if(len > CodedInput.BUFFER_SIZE && in instanceof InputStream)
            {
                // message too big
                final CodedInput input = new CodedInput(new LimitedInputStream((InputStream)in, len), 
                        decodeNestedMessageAsGroup);
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
            }
            else
            {
                final byte[] buf = new byte[len];
                in.readFully(buf, 0, len);
                final ByteArrayInput input = new ByteArrayInput(buf, 0, len, 
                        decodeNestedMessageAsGroup);
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

        // check it since this message is embedded in the DataInput.
        if(!schema.isInitialized(message))
            throw new UninitializedMessageException(message, schema);
    }
    
    /**
     * Fills the byte buffer from the {@link InputStream} with the specified length.
     */
    static void fillBufferFrom(InputStream in, byte[] buf, int offset, int len) 
    throws IOException
    {
        for(int read = 0; len > 0; len -= read, offset += read)
        {
            read = in.read(buf, offset, len);
            if(read == -1)
                throw ProtobufException.truncatedMessage();
        }
    }

}
