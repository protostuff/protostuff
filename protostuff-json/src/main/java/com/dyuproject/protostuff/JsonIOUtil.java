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
     * The default json factory for creating json parsers and generators.
     */
    public static final JsonFactory DEFAULT_JSON_FACTORY = new JsonFactory();
    
    /**
     * Serializes the {@code message} into a byte array via {@link JsonOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArray(T message, boolean numeric)
    {
        return toByteArray(message, message.cachedSchema(), numeric);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link JsonOutput} 
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
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link JsonOutput}.
     */
    public static <T extends Message<T>> void writeTo(OutputStream out, T message, boolean numeric)
    throws IOException
    {
        writeTo(out, message, message.cachedSchema(), numeric);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link JsonOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(out, 
                JsonEncoding.UTF8);
        writeTo(generator, message, schema, numeric);
        generator.close();
    }
    
    /**
     * Serializes the {@code message} into a {@link Writer} via {@link JsonOutput}.
     */
    public static <T extends Message<T>> void writeTo(Writer writer, T message, boolean numeric)
    throws IOException
    {
        writeTo(writer, message, message.cachedSchema(), numeric);
    }
    
    /**
     * Serializes the {@code message} into a {@link Writer} via {@link JsonOutput} using 
     * the given {@code schema}.
     */
    public static <T> void writeTo(Writer writer, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(writer);
        writeTo(generator, message, schema, numeric);
        generator.close();
    }
    
    /**
     * Serializes the {@code message} into a JsonGenerator via {@link JsonOutput} 
     * using the given {@code schema}.
     */
    public static <T> void writeTo(JsonGenerator generator, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        generator.writeStartObject();
        
        final JsonOutput output = new JsonOutput(generator, numeric).use(schema);
        schema.writeTo(output, message);
        if(output.isLastRepeated())
            generator.writeEndArray();
        
        generator.writeEndObject();
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    public static <T extends Message<T>> void mergeFrom(byte[] data, T message, boolean numeric) 
    throws IOException
    {
        mergeFrom(data, 0, data.length, message, message.cachedSchema(), numeric);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema, boolean numeric) 
    throws IOException
    {
        mergeFrom(data, 0, data.length, message, schema, numeric);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema, boolean numeric) throws IOException
    {
        final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(data, offset, length);
        mergeFrom(parser, message, schema, numeric);
        parser.close();
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream}.
     */
    public static <T extends Message<T>> void mergeFrom(InputStream in, T message, boolean numeric)
    throws IOException
    {
        mergeFrom(in, message, message.cachedSchema(), numeric);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(in);
        mergeFrom(parser, message, schema, numeric);
        parser.close();
    }
    
    /**
     * Merges the {@code message} from the {@link Reader}.
     */
    public static <T extends Message<T>> void mergeFrom(Reader reader, T message, boolean numeric)
    throws IOException
    {
        mergeFrom(reader, message, message.cachedSchema(), numeric);
    }
    
    /**
     * Merges the {@code message} from the {@link Reader} using the given {@code schema}.
     */
    public static <T> void mergeFrom(Reader reader, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(reader);
        mergeFrom(parser, message, schema, numeric);
        parser.close();
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
     * Serializes the {@code messages} into the stream using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(out, 
                JsonEncoding.UTF8);
        writeListTo(generator, messages, schema, numeric);
        generator.close();
    }
    
    /**
     * Serializes the {@code messages} into the writer using the given schema.
     */
    public static <T> void writeListTo(Writer writer, List<T> messages, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        final JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(writer);
        writeListTo(generator, messages, schema, numeric);
        generator.close();
    }
    
    /**
     * Serializes the {@code messages} into the generator using the given schema.
     */
    public static <T> void writeListTo(JsonGenerator generator, List<T> messages, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        generator.writeStartArray();
        if(messages.isEmpty())
        {
            generator.writeEndArray();
            return;
        }
        
        final JsonOutput output = new JsonOutput(generator, numeric);
        
        for(T m : messages)
        {
            output.use(schema);
            generator.writeStartObject();
            
            schema.writeTo(output, m);
            if(output.isLastRepeated())
                generator.writeEndArray();
            
            generator.writeEndObject();
        }
        
        generator.writeEndArray();
    }
    
    /**
     * Parses the {@code messages} from the stream using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema, boolean numeric) 
    throws IOException
    {
        final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(in);
        final List<T> list = parseListFrom(parser, schema, numeric);
        parser.close();
        return list;
    }
    
    /**
     * Parses the {@code messages} from the reader using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(Reader reader, Schema<T> schema, boolean numeric) 
    throws IOException
    {
        final JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(reader);
        final List<T> list = parseListFrom(parser, schema, numeric);
        parser.close();
        return list;
    }
    
    /**
     * Parses the {@code messages} from the parser using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(JsonParser parser, Schema<T> schema, boolean numeric) 
    throws IOException
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
