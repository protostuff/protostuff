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
import java.util.List;

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

public abstract class ProtobufJSON
{

    public static final JsonFactory DEFAULT_JSON_FACTORY = new JsonFactory();
    
    private final JsonFactory _jsonFactory;
    
    public ProtobufJSON()
    {
        _jsonFactory = DEFAULT_JSON_FACTORY;
    }
    
    public ProtobufJSON(JsonFactory jsonFactory)
    {
        _jsonFactory = jsonFactory==null ? DEFAULT_JSON_FACTORY : jsonFactory;
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
    
    public final <T extends MessageLite> T parseFrom(InputStream in, Class<T> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(in);
        try
        {
            return parseFrom(parser, type);
        }        
        finally
        {
            parser.close();
        }
    }
    
    public final <T extends MessageLite> T parseFrom(Reader reader, Class<T> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(reader);
        try
        {
            return parseFrom(parser, type);
        }
        finally
        {
            parser.close();
        }        
    }
    
    @SuppressWarnings("unchecked")
    public final <T extends MessageLite> T parseFrom(JsonParser parser, Class<T> type)
    throws IOException
    {
        if(parser.nextToken()!=JsonToken.START_OBJECT)
        {
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    type);
        }
        
        ProtobufConvertor<T,Builder> convertor = getConvertor(type);
        if(convertor==null)
        {
            throw new IllegalStateException("Convertor for message: " + 
                    type + " not found");
        } 
        
        return (T)convertor.parseFrom(parser).build();
    }
    
    public final <B extends Builder> B parseFrom(InputStream in, Class<B> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(in);
        try
        {
            return parseFrom(parser, type);
        }
        finally
        {
            parser.close();
        }        
    }
    
    public final <B extends Builder> B parseFrom(Reader reader, Class<B> type)
    throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(reader);
        try
        {
            return parseFrom(parser, type);
        }
        finally
        {
            parser.close();
        }        
    }
    
    public final <B extends Builder> B parseFrom(JsonParser parser, Class<B> type)
    throws IOException
    {
        if(parser.nextToken()!=JsonToken.START_OBJECT)
        {
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    type.getDeclaringClass());
        }
        
        ProtobufConvertor<MessageLite,B> convertor = getConvertor(type.getDeclaringClass());
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
    
    public final <B extends Builder> void mergeFrom(JsonParser parser, B builder)
    throws IOException
    {
        if(parser.nextToken()!=JsonToken.START_OBJECT)
        {
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    builder.getClass().getDeclaringClass());
        }
        
        ProtobufConvertor<MessageLite,B> convertor = getConvertor(builder.getClass().getDeclaringClass());
        if(convertor==null)
        {
            throw new IllegalStateException("Convertor for message: " + 
                    builder.getClass().getDeclaringClass() + " not found");
        }
        
        convertor.mergeFrom(parser, builder);
    }
    
    public final void writeTo(OutputStream out, MessageLite message)
    throws IOException
    {
        JsonGenerator generator = _jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8);
        try
        {
            writeTo(generator, message);
        }
        finally
        {
            generator.close();
        }
    }
    
    public final void writeTo(Writer writer, MessageLite message)
    throws IOException
    {
        JsonGenerator generator = _jsonFactory.createJsonGenerator(writer);
        try
        {
            writeTo(generator, message);
        }
        finally
        {
            generator.close();
        }
    }
    
    public final <T extends MessageLite> void writeTo(JsonGenerator generator, T message)
    throws IOException
    {
        ProtobufConvertor<T,Builder> convertor = getConvertor(message.getClass());
        if(convertor==null)
        {
            throw new IllegalStateException("Convertor for message: " + 
                    message.getClass() + " not found");
        }
        
        convertor.generateTo(generator, message);
    }
    
    public final <T extends MessageLite> void writeTo(OutputStream out, List<T> messages, 
            Class<T> messageType) throws IOException
    {
        JsonGenerator generator = _jsonFactory.createJsonGenerator(out, JsonEncoding.UTF8);
        try
        {
            writeTo(generator, messages, messageType);
        }
        finally
        {
            generator.close();
        }
    }
    
    public final <T extends MessageLite> void writeTo(Writer writer, List<T> messages, 
            Class<T> messageType) throws IOException
    {
        JsonGenerator generator = _jsonFactory.createJsonGenerator(writer);
        try
        {
            writeTo(generator, messages, messageType);
        }
        finally
        {
            generator.close();
        }
    }
    
    public final <T extends MessageLite> void writeTo(JsonGenerator generator, List<T> messages, 
            Class<T> messageType) throws IOException
    {
        ProtobufConvertor<T,Builder> convertor = getConvertor(messageType);
        if(convertor==null)
            throw new IOException("Message not included: " + messageType);
        
        generator.writeStartArray();
        
        for(T m : messages)
            convertor.generateTo(generator, m);
        
        generator.writeEndArray();
    }
    
    public final <T extends MessageLite> void appendMessageFrom(InputStream in, List<? super T> messages, 
            Class<T> messageType) throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(in);
        try
        {
            appendMessageFrom(parser, messages, messageType);
        }
        finally
        {
            parser.close();
        }
    }
    
    public final <T extends MessageLite> void appendMessageFrom(Reader reader, List<? super T> messages, 
            Class<T> messageType) throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(reader);
        try
        {
            appendMessageFrom(parser, messages, messageType);
        }
        finally
        {
            parser.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    public final <T extends MessageLite> void appendMessageFrom(JsonParser parser, List<? super T> messages, 
            Class<T> messageType) throws IOException
    {        
        if(parser.nextToken()!=JsonToken.START_ARRAY)
        {
            throw new IOException("Expected token: [ but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    messageType);
        }
        
        ProtobufConvertor<T,Builder> convertor = getConvertor(
                messageType);
        if(convertor==null)
            throw new IOException("Message not included: " + messageType);
        
        for(JsonToken t=parser.nextToken(); t!=JsonToken.END_ARRAY; t=parser.nextToken())
        {
            if(t != JsonToken.START_OBJECT)
            {
                throw new IOException("Expected token: { but was " + 
                        parser.getCurrentToken() + " on parsing (list) message: " + 
                        messageType);
            }
            messages.add((T)convertor.parseFrom(parser).build());
        }
    }
    
    public final <B extends Builder> void appendBuilderFrom(InputStream in, List<? super B> builders, 
            Class<B> builderClass) throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(in);
        try
        {
            appendBuilderFrom(parser, builders, builderClass);
        }
        finally
        {
            parser.close();
        }
    }
    
    public final <B extends Builder> void appendBuilderFrom(Reader reader, List<? super B> builders, 
            Class<B> builderClass) throws IOException
    {
        JsonParser parser = _jsonFactory.createJsonParser(reader);
        try
        {
            appendBuilderFrom(parser, builders, builderClass);
        }
        finally
        {
            parser.close();
        }
    }
    
    public final <B extends Builder> void appendBuilderFrom(JsonParser parser, List<? super B> builders, 
            Class<B> builderClass) throws IOException
    {        
        if(parser.nextToken()!=JsonToken.START_ARRAY)
        {
            throw new IOException("Expected token: [ but was " + 
                    parser.getCurrentToken() + " on message: " + 
                    builderClass.getDeclaringClass());
        }
        
        ProtobufConvertor<MessageLite,B> convertor = getConvertor(
                builderClass.getDeclaringClass());
        if(convertor==null)
            throw new IOException("Message not included: " + builderClass.getDeclaringClass());
        
        for(JsonToken t=parser.nextToken(); t!=JsonToken.END_ARRAY; t=parser.nextToken())
        {
            if(t != JsonToken.START_OBJECT)
            {
                throw new IOException("Expected token: { but was " + 
                        parser.getCurrentToken() + " on parsing (list) message: " + 
                        builderClass.getDeclaringClass());
            }
            builders.add(convertor.parseFrom(parser));
        }
    }
    
    protected abstract <T extends MessageLite, B extends Builder> ProtobufConvertor<T,B> getConvertor(Class<?> messageType);

    
}
