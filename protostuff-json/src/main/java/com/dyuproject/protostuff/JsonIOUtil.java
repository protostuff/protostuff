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

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Schema;

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
    public static <T> byte[] toByteArray(T message, Schema<T> schema, boolean numeric)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            writeTo(baos, message, schema, numeric);
        }
        catch (IOException e)
        {
            System.err.println(new String(baos.toByteArray(), ByteString.UTF8));
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link JsonOutput}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(out, JsonEncoding.UTF8);
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
     * Serializes the {@code message} into a {@link Writer} via {@link JsonOutput}.
     */
    public static <T> void writeTo(Writer writer, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        JsonGenerator generator = DEFAULT_JSON_FACTORY.createJsonGenerator(writer);
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
     * Serializes the {@code message} into a JsonGenerator via {@link JsonOutput}.
     */
    public static <T> void writeTo(JsonGenerator generator, T message, Schema<T> schema, 
            boolean numeric) throws IOException
    {
        generator.writeStartObject();
        
        JsonOutput output = new JsonOutput(generator, numeric).use(schema);
        schema.writeTo(output, message);
        if(output.isLastRepeated())
            generator.writeEndArray();
        
        generator.writeEndObject();
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
        JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(data, offset, length);
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
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(in);
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
    public static <T> void mergeFrom(Reader reader, T message, Schema<T> schema, boolean numeric)
    throws IOException
    {
        JsonParser parser = DEFAULT_JSON_FACTORY.createJsonParser(reader);
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
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message " + 
                    schema.typeClass());
        }
        
        schema.mergeFrom(new JsonInput(parser, numeric), message);
        
        if(parser.getCurrentToken() != JsonToken.END_OBJECT)
        {
            throw new IOException("Expected token: } but was " + 
                    parser.getCurrentToken() + " on message " + 
                    schema.typeClass());
        }
    }

}
