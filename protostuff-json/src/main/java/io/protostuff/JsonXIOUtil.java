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

package io.protostuff;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Json serialization via {@link JsonXOutput}.
 * 
 * @author David Yu
 * @created Jul 2, 2010
 */
public final class JsonXIOUtil
{
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema, boolean numeric) throws IOException
    {
        mergeFrom(data, 0, data.length, message, schema, numeric);
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema, boolean numeric)
            throws IOException
    {
        mergeFrom(new ByteArrayInputStream(data, offset, length), message, schema, numeric);
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, boolean numeric) throws IOException
    {
        JsonXInput input = new JsonXInput(in, numeric);
        input.readNext('{');
        schema.mergeFrom(input, message);
    }

    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, boolean numeric)
            throws IOException
    {
        return parseListFrom(in, schema, numeric, DefaultListFormat.JSON);
    }

    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, boolean numeric, ListFormat fmt)
            throws IOException
    {
        final JsonXInput input = new JsonXInput(in, numeric);

        input.readNext(fmt.start());

        if (input.tryNext(fmt.end()))
            return Collections.emptyList();

        final List<T> list = new ArrayList<T>();
        do
        {
            input.readNext('{');
            final T message = schema.newMessage();
            schema.mergeFrom(input, message);
            list.add(message);
            input.reset();
        } while (input.tryNext(fmt.delimiter()));
        input.readNext(fmt.end());
        return list;
    }

    public static <T> byte[] toByteArray(T message, Schema<T> schema, boolean numeric,
            LinkedBuffer buffer)
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final JsonXOutput output = new JsonXOutput(buffer, numeric, schema);

        try
        {
            output.writeStartObject();

            schema.writeTo(output, message);

            if (output.isLastRepeated())
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
     * Serializes the {@code message} into a {@link LinkedBuffer} via {@link JsonXOutput} using the given {@code schema}
     * with the supplied buffer.
     */
    public static <T> void writeTo(LinkedBuffer buffer, T message, Schema<T> schema,
            boolean numeric)
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final JsonXOutput output = new JsonXOutput(buffer, numeric, schema);

        try
        {
            output.writeStartObject();

            schema.writeTo(output, message);

            if (output.isLastRepeated())
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
     * Serializes the {@code message} into an {@link OutputStream} via {@link JsonXOutput} with the supplied buffer.
     */
    public static <T extends Message<T>> void writeTo(OutputStream out, T message, boolean numeric,
            LinkedBuffer buffer)
            throws IOException
    {
        writeTo(out, message, message.cachedSchema(), numeric, buffer);
    }

    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link JsonXOutput} using the given
     * {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, boolean numeric,
            LinkedBuffer buffer) throws IOException
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        final JsonXOutput output = new JsonXOutput(buffer, out, numeric, schema);

        output.writeStartObject();

        schema.writeTo(output, message);

        if (output.isLastRepeated())
            output.writeEndArray();

        output.writeEndObject();

        LinkedBuffer.writeTo(out, buffer);
    }

    /**
     * Serializes the {@code messages} into the {@link LinkedBuffer} using the given schema.
     */
    public static <T> void writeListTo(LinkedBuffer buffer,
            List<T> messages, Schema<T> schema, boolean numeric)
    {
        writeListTo(buffer, messages, schema, numeric, DefaultListFormat.JSON);
    }

    /**
     * Serializes the {@code messages} into the stream using the given schema with the supplied buffer.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema,
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        writeListTo(out, messages, schema, numeric, buffer, DefaultListFormat.JSON);
    }

    /**
     * Serializes the {@code messages} into the {@link LinkedBuffer} using the given schema.
     */
    public static <T> void writeListTo(LinkedBuffer buffer, Collection<T> messages, Schema<T> schema, boolean numeric,
            ListFormat fmt)
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        if (messages.isEmpty())
        {
            byte[] b = fmt.empty();
            System.arraycopy(b, 0, buffer.buffer, buffer.offset, b.length);
            buffer.offset += b.length;
            return;
        }
        try
        {
            final JsonXOutput output = new JsonXOutput(buffer, numeric, schema);
            boolean first = true;
            for (T m : messages)
            {
                if (first)
                {
                    first = false;
                    write(output, fmt.start());
                }
                else
                    write(output, fmt.delimiter());

                output.writeStartObject();
                schema.writeTo(output, m);
                if (output.isLastRepeated())
                    output.writeEndArray();

                output.writeEndObject().reset();
            }

            write(output, fmt.end());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " +
                    "(should never happen).", e);
        }
    }

    private static void write(JsonXOutput o, byte[] b) throws IOException
    {
        if (b.length == 0)
            return;
        if (b.length == 1)
            o.tail = o.sink.writeByte(b[0], o, o.tail);
        else
            o.tail = o.sink.writeByteArray(b, o, o.tail);
    }

    /**
     * Serializes the {@code messages} into the stream using the given schema with the supplied buffer.
     */
    public static <T> void writeListTo(OutputStream out, Collection<T> messages, Schema<T> schema, boolean numeric,
            LinkedBuffer buffer, ListFormat fmt) throws IOException
    {
        if (buffer.start != buffer.offset)
            throw new IllegalArgumentException("Buffer previously used and had not been reset.");

        if (messages.isEmpty())
        {
            out.write(fmt.empty());
            return;
        }

        final JsonXOutput output = new JsonXOutput(buffer, out, numeric, schema);
        boolean first = true;
        for (T m : messages)
        {
            if (first)
            {
                first = false;
                write(output, fmt.start());
            }
            else
                write(output, fmt.delimiter());

            output.writeStartObject();
            schema.writeTo(output, m);
            if (output.isLastRepeated())
                output.writeEndArray();

            output.writeEndObject().reset();
        }
        write(output, fmt.end());
        LinkedBuffer.writeTo(out, buffer);
    }

    public interface ListFormat
    {
        byte[] empty();

        byte[] start();

        byte[] delimiter();

        byte[] end();
    }

    public enum DefaultListFormat implements ListFormat
    {
        JSON(new byte[] { '[' }, new byte[] { ',' }, new byte[] { ']' }), NDJSON(new byte[] {}, new byte[] { '\n' },
                new byte[] {});

        private final byte[] empty;
        private final byte[] start;
        private final byte[] delimiter;
        private final byte[] end;

        private DefaultListFormat(byte[] start, byte[] delimiter, byte[] end)
        {
            this.empty = new byte[start.length + end.length];
            this.start = start;
            this.delimiter = delimiter;
            this.end = end;
            System.arraycopy(start, 0, empty, 0, start.length);
            System.arraycopy(end, 0, empty, start.length, end.length);
        }

        @Override
        public byte[] empty()
        {
            return empty;
        }

        @Override
        public byte[] start()
        {
            return start;
        }

        @Override
        public byte[] delimiter()
        {
            return delimiter;
        }

        @Override
        public byte[] end()
        {
            return end;
        }
    }
}
