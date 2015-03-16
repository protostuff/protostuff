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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.fasterxml.jackson.dataformat.smile.SmileParser;

/**
 * Smile IO utilities for messages.
 * 
 * @author David Yu
 * @created Feb 10, 2011
 */
public final class SmileIOUtil
{

    private SmileIOUtil()
    {
    }

    /**
     * A custom factory simply to expose certain fields.
     */
    public static final class Factory extends SmileFactory
    {

        /**
         * Needed by jackson's internal utf8 stream parser.
         */
        public BytesToNameCanonicalizer getRootByteSymbols()
        {
            return _rootByteSymbols;
        }

        /**
         * Returns the parser feature flags.
         */
        public int getParserFeatures()
        {
            return _parserFeatures;
        }

        /**
         * Returns the smile-specific parser feature flags.
         */
        public int getSmileParserFeatures()
        {
            return _smileParserFeatures;
        }

        /**
         * Returns the generator feature flags.
         */
        public int getGeneratorFeatures()
        {
            return _generatorFeatures;
        }

        /**
         * Returns the smile-specific generator feature flags.
         */
        public int getSmileGeneratorFeatures()
        {
            return _smileGeneratorFeatures;
        }

    }

    /**
     * The default smile factory for creating smile parsers and generators.
     */
    public static final Factory DEFAULT_SMILE_FACTORY = new Factory();

    static
    {
        DEFAULT_SMILE_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        DEFAULT_SMILE_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    /**
     * Creates a smile pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, boolean numeric) throws IOException
    {
        return newPipe(data, 0, data.length, numeric);
    }

    /**
     * Creates a smile pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, int offset, int length, boolean numeric)
            throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                data, false);
        final SmileParser parser = newSmileParser(null, data, offset, offset + length, false,
                context);

        return JsonIOUtil.newPipe(parser, numeric);
        // return JsonIOUtil.newPipe(DEFAULT_SMILE_FACTORY.createJsonParser(data, offset, length), numeric);
    }

    /**
     * Creates a smile pipe from an {@link InputStream}.
     */
    public static Pipe newPipe(InputStream in, boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                in, false);
        final SmileParser parser = newSmileParser(in, context.allocReadIOBuffer(), 0, 0,
                true, context);

        return JsonIOUtil.newPipe(parser, numeric);
        // return JsonIOUtil.newPipe(DEFAULT_SMILE_FACTORY.createJsonParser(in), numeric);
    }

    /**
     * Creates a {@link SmileParser} from the inputstream with the supplied buf {@code inBuffer} to use.
     */
    public static SmileParser newSmileParser(InputStream in, byte[] buf,
            int offset, int limit) throws IOException
    {
        return newSmileParser(in, buf, offset, limit, false,
                new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(), in,
                        false));
    }

    /**
     * Creates a {@link SmileParser} from the inputstream with the supplied buf {@code inBuffer} to use.
     */
    static SmileParser newSmileParser(InputStream in, byte[] buf,
            int offset, int limit, boolean bufferRecyclable, IOContext context)
            throws IOException
    {
        return new SmileParser(context,
                DEFAULT_SMILE_FACTORY.getParserFeatures(),
                DEFAULT_SMILE_FACTORY.getSmileParserFeatures(),
                DEFAULT_SMILE_FACTORY.getCodec(),
                DEFAULT_SMILE_FACTORY.getRootByteSymbols().makeChild(true, true),
                in,
                buf, offset, limit, bufferRecyclable);
    }

    /**
     * Creates a {@link SmileGenerator} for the outputstream with the supplied buf {@code outBuffer} to use.
     */
    public static SmileGenerator newSmileGenerator(OutputStream out, byte[] buf)
    {
        return newSmileGenerator(out, buf, 0, false, new IOContext(
                DEFAULT_SMILE_FACTORY._getBufferRecycler(), out, false));
    }

    /**
     * Creates a {@link SmileGenerator} for the outputstream with the supplied buf {@code outBuffer} to use.
     */
    static SmileGenerator newSmileGenerator(OutputStream out, byte[] buf, int offset,
            boolean bufferRecyclable, IOContext context)
    {
        return new SmileGenerator(context,
                DEFAULT_SMILE_FACTORY.getGeneratorFeatures(),
                DEFAULT_SMILE_FACTORY.getSmileGeneratorFeatures(),
                DEFAULT_SMILE_FACTORY.getCodec(),
                out,
                buf,
                offset,
                bufferRecyclable);
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema,
            boolean numeric) throws IOException
    {
        mergeFrom(data, 0, data.length, message, schema, numeric);
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message,
            Schema<T> schema, boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                data, false);
        final SmileParser parser = newSmileParser(null, data, offset, offset + length, false,
                context);

        // final SmileParser parser = DEFAULT_SMILE_FACTORY.createJsonParser(data, offset, length);
        try
        {
            JsonIOUtil.mergeFrom(parser, message, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema,
            boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                in, false);
        final SmileParser parser = newSmileParser(in, context.allocReadIOBuffer(), 0, 0,
                true, context);

        // final SmileParser parser = DEFAULT_SMILE_FACTORY.createJsonParser(in);
        try
        {
            JsonIOUtil.mergeFrom(parser, message, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     * <p>
     * The {@link LinkedBuffer}'s internal byte array will be used when reading the message.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema,
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                in, false);
        final SmileParser parser = newSmileParser(in, buffer.buffer, 0, 0, false, context);

        // final SmileParser parser = DEFAULT_SMILE_FACTORY.createJsonParser(in);

        try
        {
            JsonIOUtil.mergeFrom(parser, message, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }

    /**
     * Serializes the {@code message} into a byte array using the given {@code schema}.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, boolean numeric)
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            writeTo(baos, message, schema, numeric);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " +
                    "(should never happen).", e);
        }
        return baos.toByteArray();
    }

    /**
     * Serializes the {@code message} into a byte array using the given {@code schema}.
     * <p>
     * The {@link LinkedBuffer}'s internal byte array will be used as the primary buffer when writing the message.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, boolean numeric,
            LinkedBuffer buffer)
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            writeTo(baos, message, schema, numeric, buffer);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " +
                    "(should never happen).", e);
        }
        return baos.toByteArray();
    }

    /**
     * Serializes the {@code message} into an {@link OutputStream} using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema,
            boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                out, false);

        final SmileGenerator generator = newSmileGenerator(out,
                context.allocWriteEncodingBuffer(), 0, true, context);

        // final SmileGenerator generator = DEFAULT_SMILE_FACTORY.createJsonGenerator(out);

        try
        {
            JsonIOUtil.writeTo(generator, message, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }

    /**
     * Serializes the {@code message} into an {@link OutputStream} using the given {@code schema}.
     * <p>
     * The {@link LinkedBuffer}'s internal byte array will be used as the primary buffer when writing the message.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema,
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                out, false);

        final SmileGenerator generator = newSmileGenerator(out, buffer.buffer, 0, false,
                context);

        // final SmileGenerator generator = DEFAULT_SMILE_FACTORY.createJsonGenerator(out);
        try
        {
            JsonIOUtil.writeTo(generator, message, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }

    /**
     * Serializes the {@code messages} into the stream using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages,
            Schema<T> schema, boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                out, false);

        final SmileGenerator generator = newSmileGenerator(out,
                context.allocWriteEncodingBuffer(), 0, true, context);

        // final SmileGenerator generator = DEFAULT_SMILE_FACTORY.createJsonGenerator(out);
        try
        {
            JsonIOUtil.writeListTo(generator, messages, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }

    /**
     * Serializes the {@code messages} into the stream using the given schema.
     * <p>
     * The {@link LinkedBuffer}'s internal byte array will be used as the primary buffer when writing the message.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages,
            Schema<T> schema, boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                out, false);

        final SmileGenerator generator = newSmileGenerator(out, buffer.buffer, 0, false,
                context);

        // final SmileGenerator generator = DEFAULT_SMILE_FACTORY.createJsonGenerator(out);
        try
        {
            JsonIOUtil.writeListTo(generator, messages, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }

    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema,
            boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                in, false);
        final SmileParser parser = newSmileParser(in, context.allocReadIOBuffer(), 0, 0,
                true, context);

        // final SmileParser parser = DEFAULT_SMILE_FACTORY.createJsonParser(in);
        try
        {
            return JsonIOUtil.parseListFrom(parser, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }

    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     * <p>
     * The {@link LinkedBuffer}'s internal byte array will be used when reading the message.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema,
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_SMILE_FACTORY._getBufferRecycler(),
                in, false);
        final SmileParser parser = newSmileParser(in, buffer.buffer, 0, 0, false, context);

        // final SmileParser parser = DEFAULT_SMILE_FACTORY.createJsonParser(in);
        try
        {
            return JsonIOUtil.parseListFrom(parser, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }

}
