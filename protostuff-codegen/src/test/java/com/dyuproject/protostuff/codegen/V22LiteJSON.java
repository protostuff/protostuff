package com.dyuproject.protostuff.codegen;

import java.io.IOException;
import java.util.HashMap;

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

public final class V22LiteJSON extends ProtobufJSON
{

    public V22LiteJSON()
    {
        super();
    }

    public V22LiteJSON(JsonFactory factory)
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
        final HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
        {
                        
            fieldMap.put("someInt", 1);
                        
            fieldMap.put("someString", 2);
                        
            fieldMap.put("baz", 3);
                        
            fieldMap.put("someEnum", 4);
                        
            fieldMap.put("someBytes", 5);
                        
            fieldMap.put("someBoolean", 6);
                        
            fieldMap.put("someFloat", 7);
                        
            fieldMap.put("someDouble", 8);
                        
            fieldMap.put("someLong", 9);
            
        };

        final int getFieldNumber(String name) throws IOException
        {
            Integer num = fieldMap.get(name);
            if(num==null)
                throw new IOException("Field unknown: " + name + " on message " + Bar.class);

            return num.intValue();
        }

        public final void generateTo(JsonGenerator generator, Bar message) throws IOException
        {
            generator.writeStartObject();
                        
            if(message.hasSomeInt())
                generator.writeNumberField("someInt", message.getSomeInt());
                                    
            if(message.hasSomeString())
                generator.writeStringField("someString", message.getSomeString());
                                    
            if (message.hasBaz())
            {
                generator.writeFieldName("baz");
                CONVERTOR_Baz.generateTo(generator, message.getBaz());
            }
                                    
            if(message.hasSomeEnum())
                generator.writeStringField("someEnum", message.getSomeEnum().name());
                                    
            if (message.hasSomeBytes())
            {
                generator.writeFieldName("someBytes");
                generator.writeBinary(message.getSomeBytes().toByteArray());
            }
                                    
            if(message.hasSomeBoolean())
                generator.writeBooleanField("someBoolean", message.getSomeBoolean());
                                    
            if(message.hasSomeFloat())
                generator.writeNumberField("someFloat", message.getSomeFloat());
                                    
            if(message.hasSomeDouble())
                generator.writeNumberField("someDouble", message.getSomeDouble());
                                    
            if(message.hasSomeLong())
                generator.writeNumberField("someLong", message.getSomeLong());
                        
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
                switch( getFieldNumber(name) )
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
                        builder.setSomeEnum(Bar.Status.valueOf(parser.getText()));
                        
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
        final HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
        {
                        
            fieldMap.put("id", 1);
                        
            fieldMap.put("name", 2);
                        
            fieldMap.put("timestamp", 3);
            
        };

        final int getFieldNumber(String name) throws IOException
        {
            Integer num = fieldMap.get(name);
            if(num==null)
                throw new IOException("Field unknown: " + name + " on message " + Baz.class);

            return num.intValue();
        }

        public final void generateTo(JsonGenerator generator, Baz message) throws IOException
        {
            generator.writeStartObject();
                        
            if(message.hasId())
                generator.writeNumberField("id", message.getId());
                                    
            if(message.hasName())
                generator.writeStringField("name", message.getName());
                                    
            if(message.hasTimestamp())
                generator.writeNumberField("timestamp", message.getTimestamp());
                        
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
                switch( getFieldNumber(name) )
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
        final HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
        {
                        
            fieldMap.put("someInt", 1);
                        
            fieldMap.put("someString", 2);
                        
            fieldMap.put("bar", 3);
                        
            fieldMap.put("someEnum", 4);
                        
            fieldMap.put("someBytes", 5);
                        
            fieldMap.put("someBoolean", 6);
                        
            fieldMap.put("someFloat", 7);
                        
            fieldMap.put("someDouble", 8);
                        
            fieldMap.put("someLong", 9);
            
        };

        final int getFieldNumber(String name) throws IOException
        {
            Integer num = fieldMap.get(name);
            if(num==null)
                throw new IOException("Field unknown: " + name + " on message " + Foo.class);

            return num.intValue();
        }

        public final void generateTo(JsonGenerator generator, Foo message) throws IOException
        {
            generator.writeStartObject();
                        
            generator.writeFieldName("someInt");
            generator.writeStartArray();
            
            for (int t : message.getSomeIntList())
                generator.writeNumber(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("someString");
            generator.writeStartArray();
            
            for (String t : message.getSomeStringList())
                generator.writeString(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("bar");
            generator.writeStartArray();
            
            for (Bar t : message.getBarList())
                CONVERTOR_Bar.generateTo(generator, t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("someEnum");
            generator.writeStartArray();
            
            for (Foo.EnumSample t : message.getSomeEnumList())
                generator.writeString(t.name());
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("someBytes");
            generator.writeStartArray();
            
            for (ByteString t : message.getSomeBytesList())
                generator.writeBinary(t.toByteArray());
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("someBoolean");
            generator.writeStartArray();
            
            for (boolean t : message.getSomeBooleanList())
                generator.writeBoolean(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("someFloat");
            generator.writeStartArray();
            
            for (float t : message.getSomeFloatList())
                generator.writeNumber(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("someDouble");
            generator.writeStartArray();
            
            for (double t : message.getSomeDoubleList())
                generator.writeNumber(t);
            
            generator.writeEndArray();
                                    
            generator.writeFieldName("someLong");
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
                switch( getFieldNumber(name) )
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
                                                        
                            builder.addSomeEnum(Foo.EnumSample.valueOf(parser.getText()));
                            
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
