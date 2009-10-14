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

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.google.protobuf.ByteString;
import com.google.protobuf.WireFormat.JavaType;

/**
 * @author David Yu
 * @created Oct 1, 2009
 */

public abstract class InlineValue<T>
{
    
    public static InlineValue<?> getPrimitive(JavaType type)
    {
        switch(type)
        {
            case INT:
                return INT_VALUE;
            case LONG:
                return LONG_VALUE;
            case BOOLEAN:
                return BOOLEAN_VALUE;
            case FLOAT:
                return FLOAT_VALUE;
            case DOUBLE:
                return DOUBLE_VALUE;
            case STRING:
                return STRING_VALUE;
            case BYTE_STRING:
                return BYTESTRING_VALUE;            
            default:
                return null;
        }
    }
    
    public static InlineValue<Integer> INT_VALUE = new InlineValue<Integer>()
    {
        public Integer readFrom(JsonParser parser) throws IOException
        {            
            return parser.getIntValue();
        }
        public void writeTo(JsonGenerator generator, Integer value) throws IOException
        {
            generator.writeNumber(value);
        }
        public JavaType getJavaType()
        {            
            return JavaType.INT;
        }        
    };
    
    public static InlineValue<Long> LONG_VALUE = new InlineValue<Long>()
    {
        public Long readFrom(JsonParser parser) throws IOException
        {            
            return parser.getLongValue();
        }
        public void writeTo(JsonGenerator generator, Long value) throws IOException
        {
            generator.writeNumber(value);
        }
        public JavaType getJavaType()
        {            
            return JavaType.LONG;
        }        
    };
    
    public static InlineValue<Boolean> BOOLEAN_VALUE = new InlineValue<Boolean>()
    {
        public Boolean readFrom(JsonParser parser) throws IOException
        {            
            JsonToken jt = parser.getCurrentToken();
            if(jt==JsonToken.VALUE_FALSE)
                return false;
            if(jt==JsonToken.VALUE_TRUE)
                return true;
            
            throw new IOException("Expected token: true/false but was " + jt);
        }
        public void writeTo(JsonGenerator generator, Boolean value) throws IOException
        {
            generator.writeBoolean(value);
        }
        public JavaType getJavaType()
        {            
            return JavaType.BOOLEAN;
        }        
    };
    
    public static InlineValue<Float> FLOAT_VALUE = new InlineValue<Float>()
    {
        public Float readFrom(JsonParser parser) throws IOException
        {            
            return parser.getFloatValue();
        }
        public void writeTo(JsonGenerator generator, Float value) throws IOException
        {
            generator.writeNumber(value);
        }
        public JavaType getJavaType()
        {            
            return JavaType.FLOAT;
        }        
    };
    
    public static InlineValue<Double> DOUBLE_VALUE = new InlineValue<Double>()
    {
        public Double readFrom(JsonParser parser) throws IOException
        {            
            return parser.getDoubleValue();
        }
        public void writeTo(JsonGenerator generator, Double value) throws IOException
        {
            generator.writeNumber(value);
        }
        public JavaType getJavaType()
        {            
            return JavaType.DOUBLE;
        }        
    };
    
    public static InlineValue<String> STRING_VALUE = new InlineValue<String>()
    {
        public String readFrom(JsonParser parser) throws IOException
        {            
            return parser.getText();
        }
        public void writeTo(JsonGenerator generator, String value) throws IOException
        {
            generator.writeString(value);
        }
        public JavaType getJavaType()
        {            
            return JavaType.STRING;
        }        
    };
    
    public static InlineValue<ByteString> BYTESTRING_VALUE = new InlineValue<ByteString>()
    {
        public ByteString readFrom(JsonParser parser) throws IOException
        {            
            //return ByteString.copyFrom(parser.getBinaryValue());
            return ByteString.copyFromUtf8(parser.getText());
        }
        public void writeTo(JsonGenerator generator, ByteString value) throws IOException
        {
            //generator.writeBinary(value.toByteArray());
            generator.writeString(value.toStringUtf8());
        }
        public JavaType getJavaType()
        {            
            return JavaType.BYTE_STRING;
        }
    };
    
    public abstract T readFrom(JsonParser parser) throws IOException;
    public abstract void writeTo(JsonGenerator generator, T value) throws IOException;
    public abstract JavaType getJavaType();
    
    @SuppressWarnings("unchecked")
    public void writeObjectTo(JsonGenerator generator, Object value) throws IOException
    {
        writeTo(generator, (T)value);
    }
    
    
    public static class EnumValue<E extends Enum<E>> extends InlineValue<E>
    {
        
        @SuppressWarnings("unchecked")
        public static final EnumValue<? extends Enum<?>> create(Class<?> enumClass)
        {
            if(!enumClass.isEnum())
                throw new RuntimeException("Not an enum class: " + enumClass);
            
            return new EnumValue(enumClass);
        }
        
        private final Class<E> _clazz;
        
        public EnumValue(Class<E> clazz)
        {
            _clazz = clazz;
        }
        
        public E readFrom(JsonParser parser) throws IOException
        {
            return Enum.valueOf(_clazz, parser.getText());
        }
        
        public void writeTo(JsonGenerator generator, E value) throws IOException
        {
            generator.writeString(value.name());
        }
        
        public JavaType getJavaType()
        {
            return JavaType.ENUM;
        }
    }

}
