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

package io.protostuff;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Common io utils for the supported formats.
 * 
 * @author David Yu
 * @created Nov 12, 2009
 */
final class IOUtil
{

    private IOUtil()
    {
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    static <T> void mergeFrom(byte[] data, int offset, int length, T message,
            Schema<T> schema, boolean decodeNestedMessageAsGroup)
    {
        try
        {
            final ByteArrayInput input = new ByteArrayInput(data, offset, length,
                    decodeNestedMessageAsGroup);
            schema.mergeFrom(input, message);
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
     * Merges the {@code message} from the {@link InputStream} with the supplied {@code buf} to use.
     */
    static <T> void mergeFrom(InputStream in, byte[] buf, T message, Schema<T> schema,
            boolean decodeNestedMessageAsGroup) throws IOException
    {
        final CodedInput input = new CodedInput(in, buf, decodeNestedMessageAsGroup);
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    static <T> void mergeFrom(InputStream in, T message, Schema<T> schema,
            boolean decodeNestedMessageAsGroup) throws IOException
    {
        final CodedInput input = new CodedInput(in, decodeNestedMessageAsGroup);
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
    }

    /**
     * The {@code buf} size limits the size of the message that must be read. A ProtobufException (sizeLimitExceeded)
     * will be thrown if the size of the delimited message is larger.
     */
    static <T> int mergeDelimitedFrom(InputStream in, byte[] buf, T message,
            Schema<T> schema, boolean decodeNestedMessageAsGroup) throws IOException
    {
        final int size = in.read();
        if (size == -1)
            throw new EOFException("mergeDelimitedFrom");

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

            fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len,
                    decodeNestedMessageAsGroup);
            try
            {
                schema.mergeFrom(input, message);
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
     */
    static <T> int mergeDelimitedFrom(InputStream in, T message, Schema<T> schema,
            boolean decodeNestedMessageAsGroup) throws IOException
    {
        final int size = in.read();
        if (size == -1)
            throw new EOFException("mergeDelimitedFrom");

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
                        decodeNestedMessageAsGroup);
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
                return len;
            }

            byte[] buf = new byte[len];
            fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len,
                    decodeNestedMessageAsGroup);
            try
            {
                schema.mergeFrom(input, message);
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
     */
    static <T> int mergeDelimitedFrom(DataInput in, T message, Schema<T> schema,
            boolean decodeNestedMessageAsGroup) throws IOException
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
     * Fills the byte buffer from the {@link InputStream} with the specified length.
     */
    static void fillBufferFrom(InputStream in, byte[] buf, int offset, int len)
            throws IOException
    {
        for (int read = 0; len > 0; len -= read, offset += read)
        {
            read = in.read(buf, offset, len);
            if (read == -1)
                throw ProtobufException.truncatedMessage();
        }
    }

    /**
     * Fills the buffer based from the varint32 read from the input stream.
     * <p>
     * The buffer's read offset is not set if the data (varint32 size + message size) is too large to fit in the buffer.
     * 
     * @return the delimited size read.
     */
    static int fillBufferWithDelimitedMessageFrom(InputStream in,
            boolean drainRemainingBytesIfTooLarge,
            LinkedBuffer lb) throws IOException
    {
        final byte[] buf = lb.buffer;
        int offset = lb.start, len = buf.length - offset, read = in.read(buf, offset, len);

        if (read < 1)
            throw new EOFException("fillBufferWithDelimitedMessageFrom");

        int last = offset + read, size = buf[offset++];
        if (0 != (size & 0x80))
        {
            size = size & 0x7f;

            for (int shift = 7;; shift += 7)
            {
                if (offset == last)
                {
                    // read too few bytes
                    read = in.read(buf, last, len - (last - lb.start));
                    if (read < 1)
                        throw new EOFException("fillBufferWithDelimitedMessageFrom");

                    last += read;
                }

                byte b = buf[offset++];
                size |= (b & 0x7f) << shift;

                if (0 == (b & 0x80))
                    break;

                if (shift == 28)
                {
                    // discard the remaining bytes (5)
                    for (int i = 0;;)
                    {
                        if (offset == last)
                        {
                            // read more
                            read = in.read(buf, last, len - (last - lb.start));
                            if (read < 1)
                                throw new EOFException("fillBufferWithDelimitedMessageFrom");

                            last += read;
                        }

                        if (buf[offset++] >= 0)
                            break;

                        if (5 == ++i)
                        {
                            // we've already consumed 10 bytes
                            throw ProtobufException.malformedVarint();
                        }
                    }

                }
            }
        }

        if (size == 0)
        {
            if (offset != last)
                throw ProtobufException.misreportedSize();

            return size;
        }

        if (size < 0)
            throw ProtobufException.negativeSize();

        final int partial = last - offset;
        if (partial < size)
        {
            // need to read more
            final int delimSize = offset - lb.start;
            if ((size + delimSize) > len)
            {
                // too large.

                if (!drainRemainingBytesIfTooLarge)
                    return size;

                // drain the remaining bytes
                for (int remaining = size - partial; remaining > 0;)
                {
                    read = in.read(buf, lb.start, Math.min(remaining, len));
                    if (read < 1)
                        throw new EOFException("fillBufferWithDelimitedMessageFrom");

                    remaining -= read;
                }

                return size;
            }

            // read the remaining bytes
            fillBufferFrom(in, buf, last, size - partial);
        }

        // set the read offset (start of message)
        lb.offset = offset;

        return size;
    }

    /**
     * Returns the offset where the first byte is written. This method assumes that 5 bytes will be writable starting at
     * the {@code variableOffset}.
     */
    static int putVarInt32AndGetOffset(final int value,
            final byte[] buffer, final int variableOffset)
    {
        switch (ProtobufOutput.computeRawVarint32Size(value))
        {
            case 1:
                buffer[variableOffset + 4] = (byte) value;
                return variableOffset + 4;

            case 2:
                buffer[variableOffset + 3] = (byte) ((value & 0x7F) | 0x80);
                buffer[variableOffset + 4] = (byte) (value >>> 7);
                return variableOffset + 3;

            case 3:
                buffer[variableOffset + 2] = (byte) ((value & 0x7F) | 0x80);
                buffer[variableOffset + 3] = (byte) ((value >>> 7 & 0x7F) | 0x80);
                buffer[variableOffset + 4] = (byte) (value >>> 14);
                return variableOffset + 2;

            case 4:
                buffer[variableOffset + 1] = (byte) ((value & 0x7F) | 0x80);
                buffer[variableOffset + 2] = (byte) ((value >>> 7 & 0x7F) | 0x80);
                buffer[variableOffset + 3] = (byte) ((value >>> 14 & 0x7F) | 0x80);
                buffer[variableOffset + 4] = (byte) (value >>> 21);
                return variableOffset + 1;

            default:
                buffer[variableOffset] = (byte) ((value & 0x7F) | 0x80);
                buffer[variableOffset + 1] = (byte) ((value >>> 7 & 0x7F) | 0x80);
                buffer[variableOffset + 2] = (byte) ((value >>> 14 & 0x7F) | 0x80);
                buffer[variableOffset + 3] = (byte) ((value >>> 21 & 0x7F) | 0x80);
                buffer[variableOffset + 4] = (byte) (value >>> 28);
                return variableOffset;
        }

    }

}
