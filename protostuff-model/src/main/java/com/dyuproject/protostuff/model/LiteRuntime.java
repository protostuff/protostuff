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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.AbstractMessageLite;
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
        private Class<?> _typeClass, _componentTypeClass;
        private int _number;
        private String _name, _normalizedName;
        private boolean _repeated, _message;
        private Field _field;
        private ParameterType _resolver;
        
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
                _message = true;
            else if(List.class.isAssignableFrom(_typeClass))
                _repeated = true;
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
        
        public ParameterType getParameterType()
        {
            return _resolver;
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
