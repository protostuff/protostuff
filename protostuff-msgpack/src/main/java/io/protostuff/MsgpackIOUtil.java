//========================================================================
//Copyright (C) 2016 Alex Shvid
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

/**
 * Utility for the message pack serialization/deserialization of messages and objects tied to a schema.
 * 
 * @author Alex Shvid
 */
public final class MsgpackIOUtil
{

    public static final int END_OF_OBJECT = 0;

    private MsgpackIOUtil()
    {
    }

    /**
     * Creates a msgpack pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, boolean numeric) throws IOException
    {
        return newPipe(data, 0, data.length, numeric);
    }

    /**
     * Creates a msgpack pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, int offset, int length, boolean numeric) throws IOException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data, offset, length);
        return newPipe(in, numeric);
    }

    /**
     * Creates a msgpack pipe from an {@link InputStream}.
     */
    public static Pipe newPipe(InputStream in, boolean numeric) throws IOException
    {
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(in);
        return newPipe(unpacker, numeric);
    }

    /**
     * Creates a msgpack pipe from a {@link MessageUnpacker}.
     */
    public static Pipe newPipe(final MessageUnpacker unpacker, boolean numeric) throws IOException
    {
        MsgpackParser parser = new MsgpackParser(unpacker, numeric);
        final MsgpackInput msgpackInput = new MsgpackInput(parser);
        return new Pipe()
        {
            @Override
            protected Input begin(Pipe.Schema<?> pipeSchema) throws IOException
            {
                return msgpackInput;
            }

            @Override
            protected void end(Pipe.Schema<?> pipeSchema, Input input,
                    boolean cleanupOnly) throws IOException
            {
                if (cleanupOnly)
                {
                    unpacker.close();
                    return;
                }

                assert input == msgpackInput;

                unpacker.close();

            }
        };
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
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

        ByteArrayInputStream bios = new ByteArrayInputStream(data, offset, length);

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bios);

        try
        {
            mergeFrom(unpacker, message, schema, numeric);
        }
        finally
        {
            unpacker.close();
        }
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, boolean numeric) throws IOException
    {

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(in);

        try
        {
            mergeFrom(unpacker, message, schema, numeric);
        }
        finally
        {
            unpacker.close();
        }
    }

    /**
     * Merges the {@code message} from the JsonParser using the given {@code schema}.
     */
    public static <T> void mergeFrom(MessageUnpacker unpacker, T message, Schema<T> schema, boolean numeric)
            throws IOException
    {
        MsgpackParser parser = new MsgpackParser(unpacker, numeric);
        schema.mergeFrom(new MsgpackInput(parser), message);
    }

    /**
     * Serializes the {@code message} using the given {@code schema}.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, boolean numeric)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            writeTo(baos, message, schema, numeric);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException", e);
        }
        return baos.toByteArray();
    }

    /**
     * Serializes the {@code message} into an {@link OutputStream} using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, boolean numeric) throws IOException
    {

        MessagePacker packer = MessagePack.newDefaultPacker(out);

        try
        {
            writeTo(packer, message, schema, numeric);
        }
        finally
        {
            packer.flush();
        }
    }

    public static <T> void writeTo(MessagePacker packer, T message, Schema<T> schema, boolean numeric)
            throws IOException
    {
        MsgpackGenerator generator = new MsgpackGenerator(numeric);
        MsgpackOutput output = new MsgpackOutput(generator, schema);
        schema.writeTo(output, message);
        generator.writeTo(packer);
    }

    /**
     * Serializes the {@code messages} into the stream using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages,
            Schema<T> schema, boolean numeric) throws IOException
    {

        MessagePacker packer = MessagePack.newDefaultPacker(out);

        try
        {
            writeListTo(packer, messages, schema, numeric);
        }
        finally
        {
            packer.flush();
        }
    }

    /**
     * Serializes the {@code messages} into the generator using the given schema.
     */
    public static <T> void writeListTo(MessagePacker packer, List<T> messages,
            Schema<T> schema, boolean numeric) throws IOException
    {
        MsgpackGenerator generator = new MsgpackGenerator(numeric);
        MsgpackOutput output = new MsgpackOutput(generator, schema);

        for (T m : messages)
        {
            schema.writeTo(output, m);
            generator.writeTo(packer);
            generator.reset();
        }

    }

    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, boolean numeric) throws IOException
    {

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(in);

        try
        {
            return parseListFrom(unpacker, schema, numeric);
        }
        finally
        {
            unpacker.close();
        }
    }

    /**
     * Parses the {@code messages} from the parser using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(MessageUnpacker unpacker, Schema<T> schema, boolean numeric)
            throws IOException
    {

        MsgpackParser parser = new MsgpackParser(unpacker, numeric);
        MsgpackInput input = new MsgpackInput(parser);

        List<T> list = new ArrayList<T>();

        while (parser.hasNext())
        {

            T message = schema.newMessage();
            schema.mergeFrom(input, message);

            list.add(message);
            
            parser.reset();

        }

        return list;
    }
}
