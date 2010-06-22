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
     * Serializes the {@code message} into a byte array via {@link BufferedOutput} with the 
     * supplied buffer size.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, int bufferSize)
    {
        return toByteArray(message, schema, bufferSize, false);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link BufferedOutput} with the 
     * supplied buffer size.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, int bufferSize, 
            boolean encodeNestedMessageAsGroup)
    {
        final BufferedOutput output = new BufferedOutput(bufferSize, encodeNestedMessageAsGroup);
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
     * Serializes the {@code message} into a byte array via {@link BufferedOutput}.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        return toByteArray(message, schema, BufferedOutput.DEFAULT_BUFFER_SIZE, false);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link BufferedOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArray(T message)
    {
        return toByteArray(message, message.cachedSchema(), BufferedOutput.DEFAULT_BUFFER_SIZE, 
                false);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link BufferedOutput} 
     * with the supplied buffer size.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, int bufferSize)
    throws IOException
    {
        writeTo(out, message, schema, bufferSize, false);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link BufferedOutput} 
     * with the supplied buffer size.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, int bufferSize, 
            boolean encodeNestedMessageAsGroup) throws IOException
    {
        final BufferedOutput output = new BufferedOutput(bufferSize, encodeNestedMessageAsGroup);
        schema.writeTo(output, message);
        output.streamTo(out);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link BufferedOutput}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema)
    throws IOException
    {
        writeTo(out, message, schema, BufferedOutput.DEFAULT_BUFFER_SIZE, false);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link BufferedOutput}.
     */
    public static <T extends Message<T>> void writeTo(OutputStream out, T message)
    throws IOException
    {
        writeTo(out, message, message.cachedSchema(), BufferedOutput.DEFAULT_BUFFER_SIZE, false);
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema 
     * with the supplied buffer size.
     */
    public static <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema, 
            int bufferSize) throws IOException
    {
        writeDelimitedTo(out, message, schema, bufferSize, false);
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema 
     * with the supplied buffer size.
     */
    public static <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema, 
            int bufferSize, boolean encodeNestedMessageAsGroup) throws IOException
    {
        final BufferedOutput output = new BufferedOutput(bufferSize, encodeNestedMessageAsGroup);
        schema.writeTo(output, message);
        CodedOutput.writeRawVarInt32Bytes(out, output.getSize());
        output.streamTo(out);
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema.
     */
    public static <T> void writeDelimitedTo(OutputStream out, T message, Schema<T> schema)
    throws IOException
    {
        writeDelimitedTo(out, message, schema, BufferedOutput.DEFAULT_BUFFER_SIZE, false);
    }
    
    /**
     * Serializes the {@code message} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput}.
     */
    public static <T extends Message<T>> void writeDelimitedTo(OutputStream out, T message)
    throws IOException
    {
        writeDelimitedTo(out, message, message.cachedSchema(), BufferedOutput.DEFAULT_BUFFER_SIZE, 
                false);
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
            Schema<T> schema, boolean encodeNestedMessageAsGroup)
    {
        try
        {
            final ByteArrayInput input = new ByteArrayInput(data, offset, length, 
                    encodeNestedMessageAsGroup);
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
            boolean encodeNestedMessageAsGroup) throws IOException
    {
        final CodedInput input = new CodedInput(in, encodeNestedMessageAsGroup);
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
            boolean encodeNestedMessageAsGroup) throws IOException
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
                        encodeNestedMessageAsGroup);
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
                return;
            }
            
            byte[] buf = new byte[len];
            fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len, 
                    encodeNestedMessageAsGroup);
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
     * with the supplied buffer size.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            int bufferSize) throws IOException
    {
        writeListTo(out, messages, schema, bufferSize, false);
    }
    
    /**
     * Serializes the {@code messages} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema 
     * with the supplied buffer size.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            int bufferSize, boolean encodeNestedMessageAsGroup) throws IOException
    {
        final BufferedOutput output = new BufferedOutput(bufferSize, encodeNestedMessageAsGroup);
        for(T m : messages)
        {
            schema.writeTo(output, m);
            CodedOutput.writeRawVarInt32Bytes(out, output.getSize());
            output.streamTo(out);
            output.reset();
        }
    }
    
    /**
     * Serializes the {@code messages} (delimited) into 
     * an {@link OutputStream} via {@link BufferedOutput} using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema)
    throws IOException
    {
        writeListTo(out, messages, schema, BufferedOutput.DEFAULT_BUFFER_SIZE, false);
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
            boolean encodeNestedMessageAsGroup) throws IOException
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
                            encodeNestedMessageAsGroup);
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
                        encodeNestedMessageAsGroup);
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
     */
    public static <T> void writeDelimitedTo(DataOutput out, T message, Schema<T> schema) 
    throws IOException
    {
        writeDelimitedTo(out, message, schema, false);
    }
    
    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Writes to the {@link ObjectOutput}.
     */
    public static <T> void writeDelimitedTo(DataOutput out, T message, Schema<T> schema, 
            boolean encodeNestedMessageAsGroup) throws IOException
    {
        if(out instanceof OutputStream)
        {
            // write the bytes directly to the OutputStream without allocating a byte array that 
            // contains the whole message
            final BufferedOutput output = new BufferedOutput(BufferedOutput.DEFAULT_BUFFER_SIZE, 
                    encodeNestedMessageAsGroup);
            schema.writeTo(output, message);
            CodedOutput.writeRawVarInt32Bytes(out, output.getSize());
            output.streamTo((OutputStream)out);
        }
        else
        {
            final byte[] data = toByteArray(message, schema, BufferedOutput.DEFAULT_BUFFER_SIZE, 
                    encodeNestedMessageAsGroup);
            CodedOutput.writeRawVarInt32Bytes(out, data.length);
            out.write(data);
        }
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
            boolean encodeNestedMessageAsGroup) throws IOException
    {
        final int size = in.readByte();
        if(size == -1)
            throw ProtobufException.truncatedMessage();
        
        final int len = (size & 0x80)==0 ? (size & 0x7f) : CodedInput.readRawVarint32(in, size);
        
        if(len != 0)
        {
            // not an empty message
            if(len > CodedInput.BUFFER_SIZE && in instanceof InputStream)
            {
                // message too big
                final CodedInput input = new CodedInput(new LimitedInputStream((InputStream)in, len), 
                        encodeNestedMessageAsGroup);
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
            }
            else
            {
                final byte[] buf = new byte[len];
                in.readFully(buf, 0, len);
                final ByteArrayInput input = new ByteArrayInput(buf, 0, len, 
                        encodeNestedMessageAsGroup);
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
