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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;

/**
 * LiteRuntime - generates metadata from a generated protobuf class that is optimized 
 * for LITE_RUNTIME (as well as SPEED).
 * 
 * @author David Yu
 * @created Aug 25, 2009
 */

public class LiteRuntime 
{
    
    static final String FIELD_NUMBER_SUFFIX = "_FIELD_NUMBER";
    
    @SuppressWarnings("unchecked")
    public static ModelMeta getModelMeta(Class<? extends AbstractMessageLite> messageClass)
    {
        Class<? extends Builder<?>> builderClass = 
            (Class<? extends Builder<?>>)messageClass.getDeclaredClasses()[0];
        
        Map<String,PropMeta> propertyMetaMap = new HashMap<String,PropMeta>();
        int[] minMax = parse(messageClass, builderClass, propertyMetaMap);        
        return new ModelMeta(messageClass, builderClass, propertyMetaMap, minMax[0], minMax[1]);
    }
    
    static int[] parse(Class<? extends AbstractMessageLite> messageClass, 
            Class<? extends Builder<?>> builderClass, Map<String,PropMeta> propertyMetaMap)
    {
        int min = 100, max = 1;
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
                    PropMeta meta = propertyMetaMap.get(nn);
                    if(meta==null)
                    {
                        meta = new PropMeta(messageClass, builderClass);
                        propertyMetaMap.put(nn, meta);
                    }
                    try
                    {
                        int number = f.getInt(null);
                        meta.setNumber(number);
                        
                        if(min>number)
                            min = number;
                        if(max<number)
                            max = number;
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
                String ccn = name.charAt(0)<91 ? PropertyAccessor.toCamelCase(0, n) : n;
                PropMeta meta = propertyMetaMap.get(nn);
                if(meta==null)
                {
                    meta = new PropMeta(messageClass, builderClass);
                    propertyMetaMap.put(nn, meta);
                }
                
                meta.setTypeClass(f.getType());
                if(meta.isRepeated())
                {
                    try
                    {
                        Method method = builderClass.getDeclaredMethod(
                                PropertyAccessor.toPrefixedPascalCase("get", ccn), 
                                PropertyAccessor.INT_ARG_C);
                        meta.setComponentTypeClass(method.getReturnType());
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }

                meta.setName(ccn);
                meta.setNormalizedName(nn);
                meta.setTypeField(f);
            }
        }
        
        return new int[]{min, max};
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
    
    
    public static class PropMeta implements PropertyMeta
    {
        private Class<? extends AbstractMessageLite> _messageClass;
        private Class<? extends Builder<?>> _builderClass;
        private Class<?> _typeClass, _typeBuilderClass, _componentTypeClass;
        private int _number;
        private String _name, _normalizedName;
        private boolean _repeated, _message, _liteRuntime;
        private Field _field;
        
        public PropMeta(Class<? extends AbstractMessageLite> messageClass, 
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
        
        private void setTypeClass(Class<?> typeClass)
        {
            if(_typeClass!=null)
                throw new IllegalStateException("typeClass already set.");
            
            _typeClass = typeClass;
            if(AbstractMessageLite.class.isAssignableFrom(_typeClass))
            {
                _message = true;
                _typeBuilderClass = _typeClass.getDeclaredClasses()[0];
                _liteRuntime = GeneratedMessageLite.class.isAssignableFrom(_typeClass);
            }
            else if(List.class.isAssignableFrom(_typeClass))
                _repeated = true;
        }
        
        public Class<?> getTypeBuilderClass()
        {
            return _typeBuilderClass;
        }
        
        public Class<?> getComponentTypeClass()
        {
            return _componentTypeClass;
        }
        
        private void setComponentTypeClass(Class<?> componentTypeClass)
        {
            if(_componentTypeClass!=null)
                throw new IllegalStateException("componentTypeClass already set.");
            
            _componentTypeClass = componentTypeClass;
            _repeated = true;
            _message = AbstractMessageLite.class.isAssignableFrom(_componentTypeClass);
            if(_message)
            {
                _typeBuilderClass = _componentTypeClass.getDeclaredClasses()[0];
                _liteRuntime = GeneratedMessageLite.class.isAssignableFrom(_componentTypeClass);
            }
        }
        
        public int getNumber()
        {
            return _number;
        }
        
        private void setNumber(int number)
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
        
        public boolean isLiteRuntime()
        {
            return _liteRuntime;
        }
        
        public String getName()
        {
            return _name;
        }
        
        private void setName(String name)
        {
            _name = name;
        }
        
        public String getNormalizedName()
        {
            return _normalizedName;
        }
        
        private void setNormalizedName(String normalizedName)
        {
            _normalizedName = normalizedName;
        }
        
        public Field getField()
        {
            return _field;
        }
        
        private void setTypeField(Field field)
        {
            _field = field;
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
