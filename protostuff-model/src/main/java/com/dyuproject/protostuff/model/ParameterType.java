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
