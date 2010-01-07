package com.dyuproject.protostuff.codegen;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

import com.dyuproject.protostuff.json.ProtobufConvertor;
import com.dyuproject.protostuff.json.ProtobufJSON;

import com.dyuproject.protostuff.codegen.V22Lite.Bar;
import com.dyuproject.protostuff.codegen.V22Lite.Baz;
import com.dyuproject.protostuff.codegen.V22Lite.Foo;

public final class V22LiteNumericJSON extends ProtobufJSON
{

    public V22LiteNumericJSON()
    {
        super();
    }

    public V22LiteNumericJSON(JsonFactory factory)
    {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    protected <T extends MessageLite, B extends Builder> ProtobufConvertor<T, B> getConvertor(Class<?> messageType)
    {
        
        if(messageType==Bar.class)
            return (ProtobufConvertor<T, B>)CONVERTOR_Bar;
        
        if(messageType==Baz.class)
            return (ProtobufConvertor<T, B>)CONVERTOR_Baz;
        
        if(messageType==Foo.class)
            return (ProtobufConvertor<T, B>)CONVERTOR_Foo;
        
        return null;
    }

    
    static final ProtobufConvertor<Bar,Bar.Builder> CONVERTOR_Bar = new ProtobufConvertor<Bar,Bar.Builder>()
    {

        public final void generateTo(JsonGenerator generator, Bar message) throws IOException
        {
            generator.writeStartObject();
                        
            if(message.hasSomeInt())
                generator.writeNumberField("1", message.getSomeInt());
                                    
            if(message.hasSomeString())
                generator.writeStringField("2", message.getSomeString());
                                    
            if (message.hasBaz())
            {
                generator.writeFieldName("3");
                CONVERTOR_Baz.generateTo(generator, message.getBaz());
            }
                                    
            if(message.hasSomeEnum())
                generator.writeNumberField("4", message.getSomeEnum().getNumber());
                                    
            if (message.hasSomeBytes())
            {
                generator.writeFieldName("5");
                generator.writeBinary(message.getSomeBytes().toByteArray());
            }
                                    
            if(message.hasSomeBoolean())
                generator.writeBooleanField("6", message.getSomeBoolean());
                                    
            if(message.hasSomeFloat())
                generator.writeNumberField("7", message.getSomeFloat());
                                    
            if(message.hasSomeDouble())
                generator.writeNumberField("8", message.getSomeDouble());
                                    
            if(message.hasSomeLong())
                generator.writeNumberField("9", message.getSomeLong());
                        
            generator.writeEndObject();
        }

        public final Bar.Builder parseFrom(JsonParser parser) throws IOException
        {
            Bar.Builder builder = Bar.newBuilder();
            mergeFrom(parser, builder);
            return builder;
        }

        public final void mergeFrom(JsonParser parser, Bar.Builder builder) throws IOException
        {
            for(JsonToken t = parser.nextToken(); t!=JsonToken.END_OBJECT; t=parser.nextToken())
            {
                if(t!=JsonToken.FIELD_NAME)
                {
                    throw new IOException("Expected token: field_name but was " + 
                            parser.getCurrentToken() + " on message " + 
                            Bar.class);
                }
                String name = parser.getCurrentName();
                switch( Integer.parseInt(name) )
                {
                    
                    case 1:
                        
                        parser.nextToken();
                        builder.setSomeInt(parser.getIntValue());
                        
                        break;
                    
                    case 2:
                                                
                        parser.nextToken();
                        builder.setSomeString(parser.getText());
                        
                        break;
                    
                    case 3:
                        
                        parser.nextToken();
                        builder.setBaz(CONVERTOR_Baz.parseFrom(parser));
                        
                        break;
                    
                    case 4:
                        
                        parser.nextToken();
                        builder.setSomeEnum(Bar.Status.valueOf(parser.getIntValue()));
                        
                        break;
                    
                    case 5:
                        
                        parser.nextToken();
                        builder.setSomeBytes(ByteString.copyFrom(parser.getBinaryValue()));
                        
                        break;
                    
                    case 6:
                        
                        JsonToken jt = parser.nextToken();
                        if(jt==JsonToken.VALUE_TRUE)
                            builder.setSomeBoolean(true);
                        else if(jt==JsonToken.VALUE_FALSE)
                            builder.setSomeBoolean(false);
                        else
                            throw new IOException("Expected token: true/false but was " + jt);
                        
                        break;
                    
                    case 7:
                        
                        parser.nextToken();
                        builder.setSomeFloat(parser.getFloatValue());
                        
                        break;
                    
                    case 8:
                        
                        parser.nextToken();
                        builder.setSomeDouble(parser.getDoubleValue());
                        
                        break;
                    
                    case 9:
                        
                        parser.nextToken();
                        builder.setSomeLong(parser.getLongValue());
                        
                        break;
                    
                    default:
                        throw new IOException("Field unknown: " + name + " on message " + Bar.class);
                }
            }
        }

    };

    
    static final ProtobufConvertor<Baz,Baz.Builder> CONVERTOR_Baz = new ProtobufConvertor<Baz,Baz.Builder>()
    {

        public final void generateTo(JsonGenerator generator, Baz message) throws IOException
        {
            generator.writeStartObject();
                        
            if(message.hasId())
                generator.writeNumberField("1", message.getId());
                                    
            if(message.hasName())
                generator.writeStringField("2", message.getName());
                                    
            if(message.hasTimestamp())
                generator.writeNumberField("3", message.getTimestamp());
                        
            generator.writeEndObject();
        }

        public final Baz.Builder parseFrom(JsonParser parser) throws IOException
        {
            Baz.Builder builder = Baz.newBuilder();
            mergeFrom(parser, builder);
            return builder;
        }

        public final void mergeFrom(JsonParser parser, Baz.Builder builder) throws IOException
        {
            for(JsonToken t = parser.nextToken(); t!=JsonToken.END_OBJECT; t=parser.nextToken())
            {
                if(t!=JsonToken.FIELD_NAME)
                {
                    throw new IOException("Expected token: field_name but was " + 
                            parser.getCurrentToken() + " on message " + 
                            Baz.class);
                }
                String name = parser.getCurrentName();
                switch( Integer.parseInt(name) )
                {
                    
                    case 1:
                        
                        parser.nextToken();
                        builder.setId(parser.getLongValue());
                        
                        break;
                    
                    case 2:
                                                
                        parser.nextToken();
                        builder.setName(parser.getText());
                        
                        break;
                    
                    case 3:
                        
                        parser.nextToken();
                        builder.setTimestamp(parser.getLongValue());
                        
                        break;
                    
                    default:
                        throw new IOException("Field unknown: " + name + " on message " + Baz.class);
                }
            }
        }

    };

    
    static final ProtobufConvertor<Foo,Foo.Builder> CONVERTOR_Foo = new ProtobufConvertor<Foo,Foo.Builder>()
    {

        public final void generateTo(JsonGenerator generator, Foo message) throws IOException
        {
            generator.writeStartObject();
                        
            generator.writeFieldName("1");
            generator.writeStartArray();
            
            for (int t : message.getSomeIntList())
                generator.writeNumber(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("2");
            generator.writeStartArray();
            
            for (String t : message.getSomeStringList())
                generator.writeString(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("3");
            generator.writeStartArray();
            
            for (Bar t : message.getBarList())
                CONVERTOR_Bar.generateTo(generator, t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("4");
            generator.writeStartArray();
            
            for (Foo.EnumSample t : message.getSomeEnumList())
                generator.writeNumber(t.getNumber());
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("5");
            generator.writeStartArray();
            
            for (ByteString t : message.getSomeBytesList())
                generator.writeBinary(t.toByteArray());
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("6");
            generator.writeStartArray();
            
            for (boolean t : message.getSomeBooleanList())
                generator.writeBoolean(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("7");
            generator.writeStartArray();
            
            for (float t : message.getSomeFloatList())
                generator.writeNumber(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("8");
            generator.writeStartArray();
            
            for (double t : message.getSomeDoubleList())
                generator.writeNumber(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("9");
            generator.writeStartArray();
            
            for (long t : message.getSomeLongList())
                generator.writeNumber(t);
            
            generator.writeEndArray();
                        
            generator.writeEndObject();
        }

        public final Foo.Builder parseFrom(JsonParser parser) throws IOException
        {
            Foo.Builder builder = Foo.newBuilder();
            mergeFrom(parser, builder);
            return builder;
        }

        public final void mergeFrom(JsonParser parser, Foo.Builder builder) throws IOException
        {
            for(JsonToken t = parser.nextToken(); t!=JsonToken.END_OBJECT; t=parser.nextToken())
            {
                if(t!=JsonToken.FIELD_NAME)
                {
                    throw new IOException("Expected token: field_name but was " + 
                            parser.getCurrentToken() + " on message " + 
                            Foo.class);
                }
                String name = parser.getCurrentName();
                switch( Integer.parseInt(name) )
                {
                    
                    case 1:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addSomeInt(parser.getIntValue());
                            
                        }
                        
                        break;
                    
                    case 2:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addSomeString(parser.getText());
                            
                        }
                        
                        break;
                    
                    case 3:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addBar(CONVERTOR_Bar.parseFrom(parser));
                            
                        }
                        
                        break;
                    
                    case 4:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addSomeEnum(Foo.EnumSample.valueOf(parser.getIntValue()));
                            
                        }
                        
                        break;
                    
                    case 5:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addSomeBytes(ByteString.copyFrom(parser.getBinaryValue()));
                            
                        }
                        
                        break;
                    
                    case 6:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            if(t1==JsonToken.VALUE_TRUE)
                                builder.addSomeBoolean(true);
                            else if(t1==JsonToken.VALUE_FALSE)
                                builder.addSomeBoolean(false);
                            else
                                throw new IOException("Expected token: true/false but was " + t1);
                            
                        }
                        
                        break;
                    
                    case 7:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addSomeFloat(parser.getFloatValue());
                            
                        }
                        
                        break;
                    
                    case 8:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addSomeDouble(parser.getDoubleValue());
                            
                        }
                        
                        break;
                    
                    case 9:
                                                
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message " + 
                                    Foo.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                                                        
                            builder.addSomeLong(parser.getLongValue());
                            
                        }
                        
                        break;
                    
                    default:
                        throw new IOException("Field unknown: " + name + " on message " + Foo.class);
                }
            }
        }

    };

    
}
