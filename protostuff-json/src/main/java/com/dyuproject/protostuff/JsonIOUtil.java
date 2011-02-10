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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.impl.Utf8Generator;
import org.codehaus.jackson.impl.Utf8StreamParser;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;

/**
 * Utility for the JSON serialization/deserialization of messages and objects tied to a schema.
 *
 * @author David Yu
 * @created Nov 20, 2009
 */
public final class JsonIOUtil
{
    
    private JsonIOUtil(){}
    
    /**
     * A custom factory simply to expose certain fields.
     *
     */
    public static final class Factory extends JsonFactory
    {
        
        /**
         * Needed by jackson's internal utf8 strema parser.
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
         * Returns the generator feature flags.
         */
        public int getGeneratorFeatures()
        {
            return _generatorFeatures;
        }
        
    }
    
    /**
     * The default json factory for creating json parsers and generators.
     */
    public static final Factory DEFAULT_JSON_FACTORY = new Factory();
    
    static
    {
        // disable auto-close to have same behavior as protostuff-core utility io methods
        DEFAULT_JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        DEFAULT_JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }
    
    /**
     * Creates a json pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, boolean numeric) throws IOException
    {
        return newPipe(data, 0, data.length, numeric);
    }
    
    /**
     * Creates a json pipe from a byte array.
     */
    public static Pipe newPipe(byte[] data, int offset, int length, boolean numeric) 
    throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                data, false);
        final JsonParser parser = newJsonParser(null, data, offset, offset+length, false, 
                context);
        
        return newPipe(parser, numeric);
    }
    
    /**
     * Creates a json pipe from an {@link InputStream}.
     */
    public static Pipe newPipe(InputStream in, boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                in, false);
        final JsonParser parser = newJsonParser(in, context.allocReadIOBuffer(), 0, 0, 
                true, context);
        
        return newPipe(parser, numeric);
    }
    
    /**
     * Creates a json pipe from a {@link Reader}.
     */
    public static Pipe newPipe(Reader reader, boolean numeric) throws IOException
    {
        return newPipe(DEFAULT_JSON_FACTORY.createJsonParser(reader), numeric);
    }
    
    /**
     * Creates a json pipe from a {@link JsonParser}.
     */
    public static Pipe newPipe(final JsonParser parser, boolean numeric) throws IOException
    {
        final JsonInput jsonInput = new JsonInput(parser, numeric);
        return new Pipe()
        {
            protected Input begin(Pipe.Schema<?> pipeSchema) throws IOException
            {
                if(parser.nextToken() != JsonToken.START_OBJECT)
                {
                    throw new JsonInputException("Expected token: { but was " + 
                            parser.getCurrentToken() + " on message " + 
                            pipeSchema.wrappedSchema.messageFullName());
                }
                
                return jsonInput;
            }
            
            protected void end(Pipe.Schema<?> pipeSchema, Input input, 
                    boolean cleanupOnly) throws IOException
            {
                if(cleanupOnly)
                {
                    parser.close();
                    return;
                }
                
                assert input == jsonInput;
                final JsonToken token = parser.getCurrentToken();
                
                parser.close();
                
                if(token != JsonToken.END_OBJECT)
                {
                    throw new JsonInputException("Expected token: } but was " + 
                            token + " on message " + 
                            pipeSchema.wrappedSchema.messageFullName());
                }
            }
        };
    }
    
    /**
     * Creates a {@link Utf8StreamParser} from the inputstream with the supplied 
     * buf {@code inBuffer} to use.
     */
    public static Utf8StreamParser newJsonParser(InputStream in, byte[] buf, 
            int offset, int limit) throws IOException
    {
        return newJsonParser(in, buf, offset, limit, false, 
                new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), in, 
                        false));
    }
    
    /**
     * Creates a {@link Utf8StreamParser} from the inputstream with the supplied 
     * buf {@code inBuffer} to use.
     */
    static Utf8StreamParser newJsonParser(InputStream in, byte[] buf, 
            int offset, int limit, boolean bufferRecyclable, IOContext context) 
            throws IOException
    {
        return new Utf8StreamParser(context, 
                DEFAULT_JSON_FACTORY.getParserFeatures(), in, 
                DEFAULT_JSON_FACTORY.getCodec(), 
                DEFAULT_JSON_FACTORY.getRootByteSymbols().makeChild(true, true), 
                buf, offset, limit, bufferRecyclable);
    }
    
    /**
     * Creates a {@link Utf8Generator} for the outputstream with the supplied buf 
     * {@code outBuffer} to use.
     */
    public static Utf8Generator newJsonGenerator(OutputStream out, byte[] buf)
    {
        return newJsonGenerator(out, buf, 0, false, new IOContext(
                DEFAULT_JSON_FACTORY._getBufferRecycler(), out, false));
    }
    
    /**
     * Creates a {@link Utf8Generator} for the outputstream with the supplied buf 
     * {@code outBuffer} to use.
     */
    static Utf8Generator newJsonGenerator(OutputStream out, byte[] buf, int offset, 
            boolean bufferRecyclable, IOContext context)
    {
        context.setEncoding(JsonEncoding.UTF8);
        
        return new Utf8Generator(context, 
                DEFAULT_JSON_FACTORY.getGeneratorFeatures(), 
                DEFAULT_JSON_FACTORY.getCodec(), 
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
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                data, false);
        final JsonParser parser = newJsonParser(null, data, offset, offset+length, false, 
                context);
        /*final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(data, offset, 
                length);*/
        try
        {
            mergeFrom(parser, message, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the 
     * given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                in, false);
        final JsonParser parser = newJsonParser(in, context.allocReadIOBuffer(), 0, 0, 
                true, context);
        //final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(in);
        try
        {
            mergeFrom(parser, message, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the 
     * given {@code schema}.
     * 
     * The {@link LinkedBuffer}'s internal byte array will be used when reading the 
     * message.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, 
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                in, false);
        final JsonParser parser = newJsonParser(in, buffer.buffer, 0, 0, false, context);
        try
        {
            mergeFrom(parser, message, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }
    
    /**
     * Merges the {@code message} from the {@link Reader} using the given {@code schema}.
     */
    public static <T> void mergeFrom(Reader reader, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(reader);
        try
        {
            mergeFrom(parser, message, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }
    
    /**
     * Merges the {@code message} from the JsonParser using the given {@code schema}.
     */
    public static <T> void mergeFrom(JsonParser parser, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        if(parser.nextToken() != JsonToken.START_OBJECT)
        {
            throw new JsonInputException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message " + 
                    schema.messageFullName());
        }
        
        schema.mergeFrom(new JsonInput(parser, numeric), message);
        
        if(parser.getCurrentToken() != JsonToken.END_OBJECT)
        {
            throw new JsonInputException("Expected token: } but was " + 
                    parser.getCurrentToken() + " on message " + 
                    schema.messageFullName());
        }
    }
    
    /**
     * Serializes the {@code message} into a byte array 
     * using the given {@code schema}.
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
     * Serializes the {@code message} into an {@link OutputStream} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                out, false);
        
        final JsonGenerator generator = newJsonGenerator(out, 
                context.allocWriteEncodingBuffer(), 0, true, context);
        
        /*final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(out, 
                JsonEncoding.UTF8);*/
        try
        {
            writeTo(generator, message, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} 
     * using the given {@code schema}.
     * 
     * The {@link LinkedBuffer}'s internal byte array will be used as the primary buffer 
     * when writing the message.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, 
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                out, false);
        
        final JsonGenerator generator = newJsonGenerator(out, buffer.buffer, 0, false, 
                context);
        try
        {
            writeTo(generator, message, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }
    
    /**
     * Serializes the {@code message} into a {@link Writer} using 
     * the given {@code schema}.
     */
    public static <T> void writeTo(Writer writer, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(writer);
        try
        {
            writeTo(generator, message, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }
    
    /**
     * Serializes the {@code message} into a JsonGenerator 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(JsonGenerator generator, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        generator.writeStartObject();
        
        final JsonOutput output = new JsonOutput(generator, numeric, schema);
        schema.writeTo(output, message);
        if(output.isLastRepeated())
            generator.writeEndArray();
        
        generator.writeEndObject();
    }
    
    /**
     * Serializes the {@code messages} into the stream using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, 
            Schema<T> schema, boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                out, false);
        
        final JsonGenerator generator = newJsonGenerator(out, 
                context.allocWriteEncodingBuffer(), 0, true, context);
        /*final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(out, 
                JsonEncoding.UTF8);*/
        try
        {
            writeListTo(generator, messages, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }
    
    /**
     * Serializes the {@code messages} into the stream using the given schema.
     * 
     * The {@link LinkedBuffer}'s internal byte array will be used as the primary buffer 
     * when writing the message.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, 
            Schema<T> schema, boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                out, false);
        
        final JsonGenerator generator = newJsonGenerator(out, buffer.buffer, 0, false, 
                context);
        try
        {
            writeListTo(generator, messages, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }
    
    /**
     * Serializes the {@code messages} into the writer using the given schema.
     */
    public static <T> void writeListTo(Writer writer, List<T> messages, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(writer);
        try
        {
            writeListTo(generator, messages, schema, numeric);
        }
        finally
        {
            generator.close();
        }
    }
    
    /**
     * Serializes the {@code messages} into the generator using the given schema.
     */
    public static <T> void writeListTo(JsonGenerator generator, List<T> messages, 
            Schema<T> schema, boolean numeric) throws IOException
    {
        generator.writeStartArray();
        if(messages.isEmpty())
        {
            generator.writeEndArray();
            return;
        }
        
        final JsonOutput output = new JsonOutput(generator, numeric, schema);
        
        for(T m : messages)
        {
            generator.writeStartObject();
            
            schema.writeTo(output, m);
            if(output.isLastRepeated())
                generator.writeEndArray();
            
            generator.writeEndObject();
            output.reset();
        }
        
        generator.writeEndArray();
    }
    
    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                in, false);
        final JsonParser parser = newJsonParser(in, context.allocReadIOBuffer(), 0, 0, 
                true, context);
        //final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(in);
        try
        {
            return parseListFrom(parser, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }
    
    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     * 
     * The {@link LinkedBuffer}'s internal byte array will be used when reading the 
     * message.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, 
            boolean numeric, LinkedBuffer buffer) throws IOException
    {
        final IOContext context = new IOContext(DEFAULT_JSON_FACTORY._getBufferRecycler(), 
                in, false);
        final JsonParser parser = newJsonParser(in, buffer.buffer, 0, 0, false, context);
        try
        {
            return parseListFrom(parser, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }
    
    /**
     * Parses the {@code messages} from the reader using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(Reader reader, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(reader);
        try
        {
            return parseListFrom(parser, schema, numeric);
        }
        finally
        {
            parser.close();
        }
    }
    
    /**
     * Parses the {@code messages} from the parser using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(JsonParser parser, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        if(parser.nextToken()!=JsonToken.START_ARRAY)
        {
            throw new JsonInputException("Expected token: [ but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    schema.messageFullName());
        }
        
        final JsonInput input = new JsonInput(parser, numeric);
        final List<T> list = new ArrayList<T>();
        for(JsonToken t=parser.nextToken(); t!=JsonToken.END_ARRAY; t=parser.nextToken())
        {
            if(t != JsonToken.START_OBJECT)
            {
                throw new JsonInputException("Expected token: { but was " + 
                        parser.getCurrentToken() + " on message " + 
                        schema.messageFullName());
            }
            
            final T message = schema.newMessage();
            schema.mergeFrom(input, message);
            
            if(parser.getCurrentToken() != JsonToken.END_OBJECT)
            {
                throw new JsonInputException("Expected token: } but was " + 
                        parser.getCurrentToken() + " on message " + 
                        schema.messageFullName());
            }
            
            list.add(message);
            input.reset();
        }
        return list;
    }
}
