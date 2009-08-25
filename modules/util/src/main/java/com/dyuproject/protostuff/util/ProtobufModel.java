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

package com.dyuproject.protostuff.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;

/**
 * @author David Yu
 * @created Aug 24, 2009
 */

public class ProtobufModel
{
    
    static final String FIELD_NUMBER_SUFFIX = "_FIELD_NUMBER";
    
    public static ProtobufModel get(Class<? extends AbstractMessageLite> clazz)
    {
        Map<String,Property> props = new HashMap<String,Property>();
        parse(clazz, props);
        return null;
    }
    
    static String normalizeStaticField(String name, int end)
    {
        int start = -1, j=0;
        char[] normalized = null;
        for(int i=0; i<end; i++)
        {
            char c = name.charAt(i);
            if(c>64 && c<91)
            {
                if(start==-1)
                {
                    start = i;
                    normalized = new char[end-start];
                }
                normalized[j++] = (char)(c + 32);
            }
        }
        return new String(normalized, 0, j);
    }
    
    @SuppressWarnings("unchecked")
    static void parse(Class<? extends AbstractMessageLite> messageClass, 
            Map<String,Property> props)
    {
        Class<? extends Builder<?>> builderClass = 
            (Class<? extends Builder<?>>)messageClass.getDeclaredClasses()[0];
        for(Field f : messageClass.getDeclaredFields())
        {
            int mod = f.getModifiers();
            String name = f.getName();
            if(Modifier.isStatic(mod))
            {
                if(name.endsWith(FIELD_NUMBER_SUFFIX))
                {
                    String nn = normalizeStaticField(name, 
                            name.length()-FIELD_NUMBER_SUFFIX.length());
                    Property p = props.get(nn);
                    if(p==null)
                    {
                        p = new Property(messageClass, builderClass);
                        props.put(nn, p);
                    }
                    try
                    {
                        p.setNumber(f.getInt(null));
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
            else if(name.charAt(name.length()-1)=='_')
            {
                String n = name.substring(0, name.length()-1);
                String nn = n.toLowerCase();
                String ccn = name.charAt(0)<91 ? AbstractField.toCamelCase(0, n) : n;
                Property p = props.get(nn);
                if(p==null)
                {
                    p = new Property(messageClass, builderClass);
                    props.put(nn, p);
                }
                
                p.setTypeClass(f.getType());
                if(p.isRepeated())
                {
                    try
                    {
                        Method method = builderClass.getDeclaredMethod(
                                AbstractField.toPrefixedPascalCase("get", ccn), 
                                AbstractField.INT_ARG_C);
                        p.setComponentTypeClass(method.getReturnType());
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }

                p.setName(ccn);
                p.setNormalizedName(nn);
                p.setMessageField(new MessageField(p, f));
                p.setBuilderField(new BuilderField(p));
            }
        }
    }
    
    public static Object getValue(Object target, Property prop)
    {
        try
        {
            if(prop._messageClass==target.getClass())
                return prop._messageField.getValue((AbstractMessageLite)target);
            else if(prop._builderClass==target.getClass())
                return prop._builderField.getValue((Builder<?>)target);
        }
        catch(Exception e)
        {
            
        }
        return null;
    }
    
    public static Object removeValue(Object target, Property prop)
    {
        try
        {
            if(prop._messageClass==target.getClass())
                return prop._messageField.removeValue((AbstractMessageLite)target);
            else if(prop._builderClass==target.getClass())
                return prop._builderField.removeValue((Builder<?>)target);
        }
        catch(Exception e)
        {
            
        }
        return null;
    }
    
    public static void setValue(Object target, Object value, Property prop)
    {
        try
        {
            if(prop._messageClass==target.getClass())
                prop._messageField.setValue((AbstractMessageLite)target, value);
            else if(prop._builderClass==target.getClass())
                prop._builderField.setValue((Builder<?>)target, value);
        }
        catch(Exception e)
        {
            
        }
    }
    
    public boolean replaceValueIfNone(Object target, Object value, Property prop)
    {
        try
        {
            if(prop._messageClass==target.getClass())
                return prop._messageField.replaceValueIfNone((AbstractMessageLite)target, value);
            else if(prop._builderClass==target.getClass())
                return prop._builderField.replaceValueIfNone((Builder<?>)target, value);
        }
        catch(Exception e)
        {
            
        }
        return false;
    }
    
    public Object replaceValueIfAny(Object target, Object value, Property prop)
    {
        try
        {
            if(prop._messageClass==target.getClass())
                return prop._messageField.replaceValueIfAny((AbstractMessageLite)target, value);
            else if(prop._builderClass==target.getClass())
                return prop._builderField.replaceValueIfAny((Builder<?>)target, value);
        }
        catch(Exception e)
        {
            
        }
        return null;
    }
    
    public static class Property implements AbstractField.Meta
    {
        Class<? extends AbstractMessageLite> _messageClass;
        Class<? extends Builder<?>> _builderClass;        
        Class<?> _typeClass, _componentTypeClass;
        int _number;
        String _name, _normalizedName;
        boolean _repeated, _message;
        MessageField _messageField;
        BuilderField _builderField;
        
        public Property(Class<? extends AbstractMessageLite> messageClass, 
                Class<? extends Builder<?>> builderClass)        
        {
            _messageClass = messageClass;
            _builderClass = builderClass;
        }
        
        public Class<? extends AbstractMessageLite> getMessageClass()
        {
            return _messageClass;
        }
        
        public Class<? extends Builder<?>> getBuilderClass()
        {
            return _builderClass;
        }
        
        public Class<?> getTypeClass()
        {
            return _typeClass;
        }
        
        void setTypeClass(Class<?> typeClass)
        {
            if(_typeClass!=null)
                throw new IllegalStateException("typeClass already set.");
            
            _typeClass = typeClass;
            if(MessageLite.class.isAssignableFrom(_typeClass))
                _message = true;
            else if(List.class.isAssignableFrom(_typeClass))
                _repeated = true;
        }
        
        public Class<?> getComponentTypeClass()
        {
            return _componentTypeClass;
        }
        
        void setComponentTypeClass(Class<?> componentTypeClass)
        {
            if(_componentTypeClass!=null)
                throw new IllegalStateException("componentTypeClass already set.");
            
            _componentTypeClass = componentTypeClass;
            _repeated = true;
            _message = MessageLite.class.isAssignableFrom(_componentTypeClass);
        }
        
        public int getNumber()
        {
            return _number;
        }
        
        void setNumber(int number)
        {
            _number = number;
        }
        
        public boolean isRepeated()
        {
            return _repeated;
        }
        
        public boolean isMessage()
        {
            return _message;
        }
        
        public String getName()
        {
            return _name;
        }
        
        void setName(String name)
        {
            _name = name;
        }
        
        public String getNormalizedName()
        {
            return _normalizedName;
        }
        
        void setNormalizedName(String normalizedName)
        {
            _normalizedName = normalizedName;
        }
        
        public MessageField getMessageField()
        {
            return _messageField;
        }
        
        void setMessageField(MessageField messageField)
        {
            _messageField = messageField;
        }
        
        public BuilderField getBuilderField()
        {
            return _builderField;
        }
        
        void setBuilderField (BuilderField builderField)
        {
            _builderField = builderField;
        }
        
        public String toString()
        {
            return new StringBuilder()
                .append('{').append(' ').append("num").append('=').append(getNumber())
                .append(',').append(' ').append("n").append('=').append(getName())
                .append(',').append(' ').append("nn").append('=').append(getNormalizedName())
                .append(',').append(' ').append('r').append('=').append(isRepeated())
                .append(',').append(' ').append('m').append('=').append(isMessage())
                .append(' ').append('}').append('\n').toString();
        }
        
    }

}
