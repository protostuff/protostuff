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

package com.dyuproject.protostuff.model;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.WireFormat.JavaType;

/**
 * Type basically for SPEED and LITE_RUNTIME.
 * 
 * @author David Yu
 * @created Aug 27, 2009
 */

public abstract class ParamType
{
    
    public static final ParamType MESSAGE_SPEED, MESSAGE_LITE_RUNTIME;
    
    public static final ParamType INT = new ParamType()
    {
        public Object resolveValue(Object value)
        {
            if(Integer.class==value.getClass())
                return value;
            
            return value instanceof Number ? Integer.valueOf(((Number)value).intValue()) : null;
        }
        public JavaType getJavaType()
        {
            return JavaType.INT;
        }
        public boolean isPrimitive()
        {
            return true;
        }        
        public boolean isMessage()
        {
            return false;
        }
    };
    
    public static final ParamType LONG = new ParamType()
    {
        public Object resolveValue(Object value)
        {
            if(Long.class==value.getClass())
                return value;
            
            return value instanceof Number ? Long.valueOf(((Number)value).longValue()) : null;
        }
        public JavaType getJavaType()
        {
            return JavaType.LONG;
        }
        public boolean isPrimitive()
        {
            return true;
        }        
        public boolean isMessage()
        {
            return false;
        }
    };
    
    public static final ParamType BOOLEAN = new ParamType()
    {
        public Object resolveValue(Object value)
        {            
            return Boolean.class==value.getClass() ? value : null;
        }
        public JavaType getJavaType()
        {
            return JavaType.BOOLEAN;
        }
        public boolean isPrimitive()
        {
            return true;
        }        
        public boolean isMessage()
        {
            return false;
        }
    };
    
    public static final ParamType FLOAT = new ParamType()
    {
        public Object resolveValue(Object value)
        {
            if(Float.class==value.getClass())
                return value;
            
            return value instanceof Number ? Float.valueOf(((Number)value).floatValue()) : null;
        }
        public JavaType getJavaType()
        {
            return JavaType.FLOAT;
        }
        public boolean isPrimitive()
        {
            return true;
        }        
        public boolean isMessage()
        {
            return false;
        }
    };
    
    public static final ParamType DOUBLE = new ParamType()
    {
        public Object resolveValue(Object value)
        {
            if(Double.class==value.getClass())
                return value;
            
            return value instanceof Number ? Double.valueOf(((Number)value).doubleValue()) : null;
        }
        public JavaType getJavaType()
        {
            return JavaType.DOUBLE;
        }
        public boolean isPrimitive()
        {
            return true;
        }        
        public boolean isMessage()
        {
            return false;
        }
    };
    
    public static final ParamType STRING = new ParamType()
    {
        public Object resolveValue(Object value)
        {            
            return String.class==value.getClass() ? value : null;
        }
        public JavaType getJavaType()
        {
            return JavaType.STRING;
        }
        public boolean isPrimitive()
        {
            return true;
        }        
        public boolean isMessage()
        {
            return false;
        }
    };
    
    public static final ParamType BYTESTRING = new ParamType()
    {
        public Object resolveValue(Object value)
        {
            if(value.getClass()==byte[].class)
                return ByteString.copyFrom((byte[])value);
            
            return ByteString.class==value.getClass() ? value : null;
        }
        public JavaType getJavaType()
        {
            return JavaType.BYTE_STRING;
        }
        public boolean isPrimitive()
        {
            return true;
        }        
        public boolean isMessage()
        {
            return false;
        }
    };
    
    public static ParamType getSimpleType(final Class<?> clazz)
    {
        if(clazz.isPrimitive())
            return getPrimitiveType(clazz);
        if(String.class==clazz)
            return STRING;
        if(ByteString.class==clazz)
            return BYTESTRING;
        
        return new ParamType()
        {
            public Object resolveValue(Object value)
            {
                return clazz.isAssignableFrom(value.getClass()) ? value : null;
            }
            public JavaType getJavaType()
            {
                return JavaType.ENUM;
            }
            public boolean isPrimitive()
            {
                return false;
            }
            public boolean isMessage()
            {
                return false;
            }
        };
    }
    
    public static ParamType getPrimitiveType(Class<?> clazz)
    {
        if(Integer.TYPE==clazz)
            return INT;
        if(Long.TYPE==clazz)
            return LONG;
        if(Boolean.TYPE==clazz)
            return BOOLEAN;
        if(Float.TYPE==clazz)
            return FLOAT;
        if(Double.TYPE==clazz)
            return DOUBLE;
        
        throw new IllegalArgumentException(clazz + " is not a simple primitive type.");
    }
    
    public static ParamType getMessageType(Class<?> messageClass)
    {
        return new Message(messageClass, messageClass.getDeclaredClasses()[0],
                GeneratedMessageLite.class.isAssignableFrom(messageClass) ? MESSAGE_LITE_RUNTIME : 
                    MESSAGE_SPEED);
    }
    
    public abstract Object resolveValue(Object value);
    public abstract JavaType getJavaType();
    public abstract boolean isPrimitive();
    public abstract boolean isMessage();
    
    
    static class Message extends ParamType
    {        
        private final Class<?> _messageClass;
        private final Class<?> _builderClass;
        private final ParamType _builderType;
        
        Message(Class<?> messageClass, Class<?> builderClass, ParamType builderType)
        {
            _messageClass = messageClass;
            _builderClass = builderClass;
            _builderType = builderType;
        }

        public Object resolveValue(Object value)
        {
            if(_messageClass==value.getClass())
                return value;
            else if(_builderClass==value.getClass())
                return _builderType.resolveValue(value);
            return null;
        }
        
        public JavaType getJavaType()
        {
            return JavaType.MESSAGE;
        }
        
        public boolean isPrimitive()
        {
            return false;
        }
        
        public boolean isMessage()
        {
            return true;
        }
    }
    
    static
    {
        MESSAGE_LITE_RUNTIME = com.google.protobuf.PBLiteRuntime.BUILDER_TO_MESSAGE;
        Class<?> clazz = null;
        try
        {
            
            clazz = Class.forName("com.google.protobuf.GeneratedMessage", false, 
                    Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            
        }
        if(clazz==null)
        {
            MESSAGE_SPEED = new ParamType()
            {                
                public Object resolveValue(Object value)
                {
                    return ((MessageLite.Builder)value).build();                    
                }
                public JavaType getJavaType()
                {
                    return JavaType.MESSAGE;
                }
                public boolean isPrimitive()
                {
                    return false;
                }        
                public boolean isMessage()
                {
                    return true;
                }
            };
        }
        else
            MESSAGE_SPEED = com.google.protobuf.PBSpeed.BUILDER_TO_MESSAGE;
    }
    

}
