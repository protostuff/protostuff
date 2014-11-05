//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

package io.protostuff;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO Utilities for graph objects (references and cyclic dependencies).
 * 
 * @author David Yu
 * @created Jan 19, 2011
 */
public final class GraphIOUtil
{

    private GraphIOUtil()
    {
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema)
    {
        mergeFrom(data, 0, data.length, message, schema);
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message,
            Schema<T> schema)
    {
        try
        {
            final ByteArrayInput input = new ByteArrayInput(data, offset, length, true);
            final GraphByteArrayInput graphInput = new GraphByteArrayInput(input);
            schema.mergeFrom(graphInput, message);
            input.checkLastTagWas(0);
        }
        catch (ArrayIndexOutOfBoundsException ae)
        {
            throw new RuntimeException("Truncated.", ProtobufException.truncatedMessage(ae));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Reading from a byte array threw an IOException (should " +
                    "never happen).", e);
        }
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        final CodedInput input = new CodedInput(in, true);
        final GraphCodedInput graphInput = new GraphCodedInput(input);
        schema.mergeFrom(graphInput, message);
        input.checkLastTagWas(0);
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     * <p>
     * The {@code buffer}'s internal byte array will be used for reading the message.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema,
            LinkedBuffer buffer) throws IOException
    {
        final CodedInput input = new CodedInput(in, buffer.buffer, true);
        final GraphCodedInput graphInput = new GraphCodedInput(input);
        schema.mergeFrom(graphInput, message);
        input.checkLastTagWas(0);
    }

    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} using the given {@code schema}.
     * 
     * @return the size of the message
     */
    public static <T> int mergeDelimitedFrom(InputStream in, T message, Schema<T> schema)
            throws IOException
    {
        final int size = in.read();
        if (size == -1)
            throw ProtobufException.truncatedMessage();

        final int len = size < 0x80 ? size : CodedInput.readRawVarint32(in, size);

        if (len < 0)
            throw ProtobufException.negativeSize();

        if (len != 0)
        {
            // not an empty message
            if (len > CodedInput.DEFAULT_BUFFER_SIZE)
            {
                // message too big
                final CodedInput input = new CodedInput(new LimitedInputStream(in, len),
                        true);
                final GraphCodedInput graphInput = new GraphCodedInput(input);
                schema.mergeFrom(graphInput, message);
                input.checkLastTagWas(0);
                return len;
            }

            final byte[] buf = new byte[len];
            IOUtil.fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len,
                    true);
            final GraphByteArrayInput graphInput = new GraphByteArrayInput(input);
            try
            {
                schema.mergeFrom(graphInput, message);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                throw ProtobufException.truncatedMessage(e);
            }
            input.checkLastTagWas(0);
        }

        return len;
    }

    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} using the given {@code schema}.
     * <p>
     * The delimited message size must not be larger than the {@code buffer}'s size/capacity. {@link ProtobufException}
     * "size limit exceeded" is thrown otherwise.
     * 
     * @return the size of the message
     */
    public static <T> int mergeDelimitedFrom(InputStream in, T message, Schema<T> schema,
            LinkedBuffer buffer) throws IOException
    {
        final int size = in.read();
        if (size == -1)
            throw ProtobufException.truncatedMessage();

        final byte[] buf = buffer.buffer;

        final int len = size < 0x80 ? size : CodedInput.readRawVarint32(in, size);

        if (len < 0)
            throw ProtobufException.negativeSize();

        if (len != 0)
        {
            // not an empty message
            if (len > buf.length)
            {
                // size limit exceeded.
                throw new ProtobufException("size limit exceeded. " +
                        len + " > " + buf.length);
            }

            IOUtil.fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len,
                    true);
            final GraphByteArrayInput graphInput = new GraphByteArrayInput(input);
            try
            {
                schema.mergeFrom(graphInput, message);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                throw ProtobufException.truncatedMessage(e);
            }
            input.checkLastTagWas(0);
        }

        return len;
    }

    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}. Merges from the
     * {@link DataInput}.
     * 
     * @return the size of the message
     */
    public static <T> int mergeDelimitedFrom(DataInput in, T message, Schema<T> schema)
            throws IOException
    {
        final byte size = in.readByte();
        final int len = 0 == (size & 0x80) ? size : CodedInput.readRawVarint32(in, size);

        if (len < 0)
            throw ProtobufException.negativeSize();

        if (len != 0)
        {
            // not an empty message
            if (len > CodedInput.DEFAULT_BUFFER_SIZE && in instanceof InputStream)
            {
                // message too big
                final CodedInput input = new CodedInput(new LimitedInputStream((InputStream) in, len),
                        true);
                final GraphCodedInput graphInput = new GraphCodedInput(input);
                schema.mergeFrom(graphInput, message);
                input.checkLastTagWas(0);
            }
            else
            {
                final byte[] buf = new byte[len];
                in.readFully(buf, 0, len);
                final ByteArrayInput input = new ByteArrayInput(buf, 0, len,
                        true);
                final GraphByteArrayInput graphInput = new GraphByteArrayInput(input);
                try
                {
                    schema.mergeFrom(graphInput, message);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    throw ProtobufException.truncatedMessage(e);
                }
                input.checkLastTagWas(0);
            }
        }

        // check it since this message is embedded in the DataInput.
        if (!schema.isInitialized(message))
            throw new UninitializedMessageException(message, schema);

        return len;
    }

    /**
     * Serializes the {@code message} into a byte array using the given schema.
     * 
     * @return the byte array containing the data.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer)
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final ProtostuffOutput output = new ProtostuffOutput(buffer);
        final GraphProtostuffOutput graphOutput = new GraphProtostuffOutput(output);
        try
        {
            schema.writeTo(graphOutput, message);
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
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final ProtostuffOutput output = new ProtostuffOutput(buffer);
        final GraphProtostuffOutput graphOutput = new GraphProtostuffOutput(output);
        try
        {
            schema.writeTo(graphOutput, message);
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
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final ProtostuffOutput output = new ProtostuffOutput(buffer, out);
        final GraphProtostuffOutput graphOutput = new GraphProtostuffOutput(output);
        schema.writeTo(graphOutput, message);
        LinkedBuffer.writeTo(out, buffer);
        return output.size;
    }

    /**
     * Serializes the {@code message}, prefixed with its length, into an {@link OutputStream}.
     * 
     * @return the size of the message
     */
    public static <T> int writeDelimitedTo(final OutputStream out, final T message,
            final Schema<T> schema, final LinkedBuffer buffer) throws IOException
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final ProtostuffOutput output = new ProtostuffOutput(buffer);
        final GraphProtostuffOutput graphOutput = new GraphProtostuffOutput(output);
        schema.writeTo(graphOutput, message);
        ProtobufOutput.writeRawVarInt32Bytes(out, output.size);
        LinkedBuffer.writeTo(out, buffer);
        return output.size;
    }

    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}. Writes to the
     * {@link DataOutput} .
     * 
     * @return the size of the message.
     */
    public static <T> int writeDelimitedTo(DataOutput out, T message, Schema<T> schema)
            throws IOException
    {
        final LinkedBuffer buffer = new LinkedBuffer(LinkedBuffer.MIN_BUFFER_SIZE);
        final ProtostuffOutput output = new ProtostuffOutput(buffer);
        final GraphProtostuffOutput graphOutput = new GraphProtostuffOutput(output);
        schema.writeTo(graphOutput, message);
        ProtobufOutput.writeRawVarInt32Bytes(out, output.size);
        LinkedBuffer.writeTo(out, buffer);
        return output.size;
    }

    /**
     * Optimal/Optional mergeDelimitedFrom - If the message does not fit the buffer, no merge is done and this method
     * will return false.
     * <p>
     * This is strictly for reading a single message from the stream because the buffer is aggressively filled when
     * reading the delimited size (which could result into reading more bytes than it has to).
     * <p>
     * The remaining bytes will be drained (consumed and discared) when the message is too large.
     */
    public static <T> boolean optMergeDelimitedFrom(InputStream in,
            T message, Schema<T> schema,
            LinkedBuffer buffer) throws IOException
    {
        return optMergeDelimitedFrom(in, message, schema, true, buffer);
    }

    /**
     * Optimal/Optional mergeDelimitedFrom - If the message does not fit the buffer, no merge is done and this method
     * will return false.
     * <p>
     * This is strictly for reading a single message from the stream because the buffer is aggressively filled when
     * reading the delimited size (which could result into reading more bytes than it has to).
     */
    public static <T> boolean optMergeDelimitedFrom(InputStream in,
            T message, Schema<T> schema, boolean drainRemainingBytesIfTooLarge,
            LinkedBuffer buffer) throws IOException
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final int size = IOUtil.fillBufferWithDelimitedMessageFrom(in,
                drainRemainingBytesIfTooLarge, buffer);

        if (size == 0)
        {
            // empty message
            return true;
        }

        if (buffer.start == buffer.offset)
        {
            // offset not set ... message too large
            return false;
        }

        final ByteArrayInput input = new ByteArrayInput(buffer.buffer,
                buffer.offset, size, true);
        final GraphByteArrayInput graphInput = new GraphByteArrayInput(input);
        try
        {
            schema.mergeFrom(graphInput, message);
            input.checkLastTagWas(0);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            throw ProtobufException.truncatedMessage(e);
        }
        finally
        {
            // reset
            buffer.offset = buffer.start;
        }

        return true;
    }

    /**
     * Optimal writeDelimitedTo - The varint32 prefix is written to the buffer instead of directly writing to
     * outputstream.
     * 
     * @return the size of the message
     */
    public static <T> int optWriteDelimitedTo(final OutputStream out, final T message,
            final Schema<T> schema, final LinkedBuffer buffer) throws IOException
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final ProtostuffOutput output = new ProtostuffOutput(buffer);
        final GraphProtostuffOutput graphOutput = new GraphProtostuffOutput(output);

        // leave space for varint32
        buffer.offset = buffer.start + 5;
        output.size += 5;

        schema.writeTo(graphOutput, message);

        final int size = output.size - 5;

        final int delimOffset = IOUtil.putVarInt32AndGetOffset(size, buffer.buffer,
                buffer.start);

        // write to stream
        out.write(buffer.buffer, delimOffset, buffer.offset - delimOffset);

        // flush remaining
        if (buffer.next != null)
            LinkedBuffer.writeTo(out, buffer.next);

        return size;
    }

}
