//========================================================================
//Copyright 2007-2008 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.json;

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

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

/**
 * @author David Yu
 * @created Sep 30, 2009
 */

public abstract class ProtobufJSON<P extends ProtobufConvertor<MessageLite,Builder>>
{

    public static final JsonFactory DEFAULT_JSON_FACTORY = new JsonFactory();
    
    private final JsonFactory _jsonFactory;
    
    public ProtobufJSON()
    {
        this(DEFAULT_JSON_FACTORY);
    }
    
    public ProtobufJSON(JsonFactory jsonFactory)
    {
        _jsonFactory = jsonFactory;
    }
    
    public final JsonFactory getJsonFactory()
    {
        return _jsonFactory;
    }
    
    public final JsonParser createParser(InputStream in) throws IOException
    {
        return _jsonFactory.createJsonParser(in);
    }
    
    public final JsonParser createParser(Reader reader) throws IOException
    {
        return _jsonFactory.createJsonParser(reader);
    }
    
    public final JsonGenerator createGenerator(OutputStream out) throws IOException
    {
        return _jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8);
    }
    
    public final JsonGenerator createGenerator(Writer writer) throws IOException
    {
        return _jsonFactory.createJsonGenerator(writer);
    }
    
    public final <T extends MessageLite> T readMessage(InputStream in, Class<T> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(in);
        try
        {
            return readMessage(parser, type);
        }        
        finally
        {
            parser.close();
        }
    }
    
    public final <T extends MessageLite> T readMessage(Reader reader, Class<T> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(reader);
        try
        {
            return readMessage(parser, type);
        }
        finally
        {
            parser.close();
        }        
    }
    
    @SuppressWarnings("unchecked")
    public final <T extends MessageLite> T readMessage(JsonParser parser, Class<T> type)
    throws IOException
    {
        if(parser.nextToken()!=JsonToken.START_OBJECT)
        {
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    type);
        }
        
        P convertor = getConvertor(type);
        if(convertor==null)
        {
            throw new IllegalStateException("Convertor for message: " + 
                    type + " not found");
        } 
        
        return (T)convertor.parseFrom(parser).build();
    }
    
    public final <B extends Builder> B readBuilder(InputStream in, Class<B> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(in);
        try
        {
            return readBuilder(parser, type);
        }
        finally
        {
            parser.close();
        }        
    }
    
    public final <B extends Builder> B readBuilder(Reader reader, Class<B> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(reader);
        try
        {
            return readBuilder(parser, type);
        }
        finally
        {
            parser.close();
        }        
    }
    
    @SuppressWarnings("unchecked")
    public final <B extends Builder> B readBuilder(JsonParser parser, Class<B> type)
    throws IOException
    {
        if(parser.nextToken()!=JsonToken.START_OBJECT)
        {
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    type.getDeclaringClass());
        }
        
        P convertor = getConvertor(type.getDeclaringClass());
        if(convertor==null)
        {
            throw new IllegalStateException("Convertor for message: " + 
                    type.getDeclaringClass() + " not found");
        }
        
        return (B)convertor.parseFrom(parser);
    }
    
    public final void mergeFrom(InputStream in, Builder builder)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(in);
        try
        {
            mergeFrom(parser, builder);
        }
        finally
        {
            parser.close();
        }
    }
    
    public final void mergeFrom(Reader reader, Builder builder)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(reader);
        try
        {
            mergeFrom(parser, builder);
        }
        finally
        {
            parser.close();
        }
    }
    
    public final void mergeFrom(JsonParser parser, Builder builder)
    throws IOException
    {
        if(parser.nextToken()!=JsonToken.START_OBJECT)
        {
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    builder.getClass().getDeclaringClass());
        }
        
        P convertor = getConvertor(builder.getClass().getDeclaringClass());
        if(convertor==null)
        {
            throw new IllegalStateException("Convertor for message: " + 
                    builder.getClass().getDeclaringClass() + " not found");
        }
        
        convertor.mergeFrom(parser, builder);
    }
    
    public final void writeMessage(OutputStream out, MessageLite message)
    throws IOException
    {
        JsonGenerator generator = _jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8);
        try
        {
            writeMessage(generator, message);
        }
        finally
        {
            generator.close();
        }
    }
    
    public final void writeMessage(Writer writer, MessageLite message)
    throws IOException
    {
        JsonGenerator generator = _jsonFactory.createJsonGenerator(writer);
        try
        {
            writeMessage(generator, message);
        }
        finally
        {
            generator.close();
        }
    }
    
    public final void writeMessage(JsonGenerator generator, MessageLite message)
    throws IOException
    {
        P convertor = getConvertor(message.getClass());
        if(convertor==null)
        {
            throw new IllegalStateException("Convertor for message: " + 
                    message.getClass() + " not found");
        }
        
        convertor.generateTo(generator, message);
    }
    
    protected abstract <T extends MessageLite, B extends Builder> P get(Class<?> messageType);
    
    protected abstract P getConvertor(Class<?> messageClass);
}
