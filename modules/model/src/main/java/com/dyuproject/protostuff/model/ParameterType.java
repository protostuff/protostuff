//================================================================================
//Copyright (c) 2009, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================


package com.dyuproject.protostuff.model;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageLite;

/**
 * @author David Yu
 * @created Aug 27, 2009
 */

public abstract class ParameterType
{
    
    public static final ParameterType MESSAGE_SPEED, MESSAGE_LITE_RUNTIME;
    
    public static final ParameterType INT = new ParameterType()
    {
        public Object resolveValue(Object value)
        {            
            return Integer.class==value.getClass() ? value : null;
        }
    };
    
    public static final ParameterType LONG = new ParameterType()
    {
        public Object resolveValue(Object value)
        {            
            return Long.class==value.getClass() ? value : null;
        }
    };
    
    public static final ParameterType BOOLEAN = new ParameterType()
    {
        public Object resolveValue(Object value)
        {            
            return Boolean.class==value.getClass() ? value : null;
        }
    };
    
    public static final ParameterType FLOAT = new ParameterType()
    {
        public Object resolveValue(Object value)
        {            
            return Float.class==value.getClass() ? value : null;
        }
    };
    
    public static final ParameterType DOUBLE = new ParameterType()
    {
        public Object resolveValue(Object value)
        {            
            return Double.class==value.getClass() ? value : null;
        }
    };
    
    public static final ParameterType STRING = new ParameterType()
    {
        public Object resolveValue(Object value)
        {            
            return String.class==value.getClass() ? value : null;
        }
    };
    
    public static final ParameterType BYTESTRING = new ParameterType()
    {
        public Object resolveValue(Object value)
        {            
            return ByteString.class==value.getClass() ? value : null;
        }
    };
    
    public static ParameterType getSimpleType(final Class<?> clazz)
    {
        if(clazz.isPrimitive())
            return getPrimitiveType(clazz);
        if(String.class==clazz)
            return STRING;
        if(ByteString.class==clazz)
            return BYTESTRING;
        
        return new ParameterType()
        {
            public Object resolveValue(Object value)
            {
                return clazz.isAssignableFrom(value.getClass()) ? value : null;
            }            
        };
    }
    
    public static ParameterType getPrimitiveType(Class<?> clazz)
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
    
    public static ParameterType getMessageType(Class<?> messageClass)
    {
        return new Message(messageClass, messageClass.getDeclaredClasses()[0],
                GeneratedMessageLite.class.isAssignableFrom(messageClass) ? MESSAGE_LITE_RUNTIME : 
                    MESSAGE_SPEED);
    }
    
    public abstract Object resolveValue(Object value);
    
    static class Message extends ParameterType
    {        
        private Class<?> _messageClass;
        private Class<?> _builderClass;
        private ParameterType _builderType;
        
        Message(Class<?> messageClass, Class<?> builderClass, ParameterType builderType)
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
            MESSAGE_SPEED = new ParameterType()
            {                
                public Object resolveValue(Object value)
                {
                    return ((AbstractMessageLite.Builder<?>)value).build();                    
                }
            };
        }
        else
            MESSAGE_SPEED = com.google.protobuf.PBSpeed.BUILDER_TO_MESSAGE;
    }

}
