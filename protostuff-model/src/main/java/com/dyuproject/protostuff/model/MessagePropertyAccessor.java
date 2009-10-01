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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * MessagePropertyAccessor - read-and-write access on MessageLite
 * 
 * @author David Yu
 * @created Aug 23, 2009
 */

public class MessagePropertyAccessor extends PropertyAccessor
{

    protected final FieldAccess _fa;
    protected final ParamType _paramType;
    protected final Class<?> _typeClass;
    
    public MessagePropertyAccessor(PropertyMeta meta)
    {
        super(meta);
        
        _fa = meta.isRepeated() ? new RepeatedFieldAccess() : new FieldAccess();
            
        _paramType = _fa.init(meta.getField());
        
        _typeClass = _fa.getTypeClass();
    }
    
    public ParamType getParamType()
    {
        return _paramType;
    }
    
    public Class<?> getTypeClass()
    {
        return _typeClass;
    }
    
    public Object getValue(Object message)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _fa.getValue(message);
    }
    
    public Object removeValue(Object message) 
    throws IllegalArgumentException, IllegalAccessException
    {
        return _fa.removeValue(message);
    }
    
    public boolean setValue(Object message, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _fa.setValue(message, value, true);
    }
    
    public boolean replaceValueIfNone(Object message, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _fa.replaceValueIfNone(message, value);
    }
    
    public Object replaceValueIfAny(Object message, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _fa.replaceValueIfAny(message, value);
    }
    
    class FieldAccess
    {
        Field _field, _has;
        
        protected ParamType init(Field field)
        {
            try
            {
                _field = field;
                
                _has = getMeta().getMessageClass().getDeclaredField(toPrefixedPascalCase("has", 
                        getMeta().getName()));
                
                Field.setAccessible(new Field[]{_field, _has}, true);
                
                return getMeta().isMessage() ? 
                        ParamType.getMessageType(getMeta().getTypeClass()) : 
                            ParamType.getSimpleType(getMeta().getTypeClass());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public Object getValue(Object message) 
        throws IllegalArgumentException, IllegalAccessException
        {
            return _has.getBoolean(message) ? _field.get(message) : null;
        }
        
        public Object removeValue(Object message)
        throws IllegalArgumentException, IllegalAccessException
        {
            if(_has.getBoolean(message))
            {
                Object value = _field.get(message);
                _has.setBoolean(message, false);
                return value;
            }
            return null;
        }
        
        public boolean setValue(Object message, Object value, boolean clearIfNullOrEmpty) 
        throws IllegalArgumentException, IllegalAccessException
        {
            if(value!=null)
            {
                if((value=_paramType.resolveValue(value))==null)
                    return false;
                
                _field.set(message, value);
                _has.setBoolean(message, true);
                return true;
            }
            
            if(clearIfNullOrEmpty)
                _has.setBoolean(message, false);
            
            return clearIfNullOrEmpty;
        }
        
        public boolean replaceValueIfNone(Object message, Object value)
        throws IllegalArgumentException, IllegalAccessException
        {
            if(value==null || _has.getBoolean(message))
                return false;
            
            if((value=_paramType.resolveValue(value))==null)
                return false;
            
            _field.set(message, value);
            _has.setBoolean(message, true);
            return true;
        }
        
        public Object replaceValueIfAny(Object message, Object value)
        throws IllegalArgumentException, IllegalAccessException
        {
            if(_has.getBoolean(message))
            {                
                if(value==null)
                {
                    Object last = _field.get(message);
                    _has.setBoolean(message, false);
                    return last;
                }
                
                if((value=_paramType.resolveValue(value))==null)
                    return null;
                
                Object last = _field.get(message);
                _field.set(message, value);               
                return last;
            }
            return null;
        }
        
        public Class<?> getTypeClass()
        {
            return getMeta().getTypeClass();
        }
    }
    
    class RepeatedFieldAccess extends FieldAccess
    {
        
        protected ParamType init(Field field)
        {
            _field = field;
            _field.setAccessible(true);
            return getMeta().isMessage() ? 
                    ParamType.getMessageType(getMeta().getComponentTypeClass()) : 
                        ParamType.getSimpleType(getMeta().getComponentTypeClass());
        }
        
        @SuppressWarnings("unchecked")
        public Object getValue(Object message) 
        throws IllegalArgumentException, IllegalAccessException
        {
            List<Object> list = (List<Object>)_field.get(message);
            return list.size()==0 ? null : list;
        }
        
        @SuppressWarnings("unchecked")
        public Object removeValue(Object message)
        throws IllegalArgumentException, IllegalAccessException
        {
            List<Object> list = (List<Object>)_field.get(message);
            if(list.size()==0)
                return null;
            
            _field.set(message, Collections.emptyList());
            return list;
        }
        
        @SuppressWarnings("unchecked")
        public boolean setValue(Object message, Object value, boolean clearIfNullOrEmpty) 
        throws IllegalArgumentException, IllegalAccessException
        {
            List<Object> list = (List<Object>)_field.get(message);
            if(value!=null)
            {
                if(List.class.isAssignableFrom(value.getClass()))
                {
                    List<Object> vl = (List<Object>)value;
                    if(vl.size()==0)
                    {
                        if(list.size()==0)
                            return true;
                        
                        if(clearIfNullOrEmpty)
                            _field.set(message, Collections.emptyList());
                        
                        return clearIfNullOrEmpty;
                    }
                    
                    if(list.size()==0)
                        _field.set(message, vl);
                    else
                        list.addAll(vl);
                }                    
                else if(list.size()==0)
                {
                    if((value=_paramType.resolveValue(value))==null)
                        return false;
                    
                    ArrayList<Object> nl = new ArrayList<Object>(3);
                    nl.add(value);
                    _field.set(message, nl);
                }
                else
                {
                    if((value=_paramType.resolveValue(value))==null)
                        return false;
                    list.add(value);
                }
            }
            else if(list.size()!=0)
            {
                if(clearIfNullOrEmpty)
                    _field.set(message, Collections.emptyList());
                
                return clearIfNullOrEmpty;
            }
            
            return true;
        }
        
        @SuppressWarnings("unchecked")
        public boolean replaceValueIfNone(Object message, Object value)
        throws IllegalArgumentException, IllegalAccessException
        {
            if(value==null)
                return false;
            
            List<Object> list = (List<Object>)_field.get(message);
            if(list.size()!=0)
                return false;
            
            if(List.class.isAssignableFrom(value.getClass()))
            {
                List<Object> vl = (List<Object>)value;
                if(vl.size()==0)
                    return false;

                _field.set(message, vl);
            }                    
            else
            {
                if((value=_paramType.resolveValue(value))==null)
                    return false;
                
                ArrayList<Object> nl = new ArrayList<Object>(3);
                nl.add(value);
                _field.set(message, nl);
            }
            
            return true;
        }
        
        @SuppressWarnings("unchecked")
        public Object replaceValueIfAny(Object message, Object value)
        throws IllegalArgumentException, IllegalAccessException
        {
            List<Object> list = (List<Object>)_field.get(message);
            if(list.size()==0)
                return null;
            
            if(value==null)
                _field.set(message, Collections.emptyList());
            else if(List.class.isAssignableFrom(value.getClass()))
                _field.set(message, value);
            else
            {
                if((value=_paramType.resolveValue(value))==null)
                    return null;
                    
                ArrayList<Object> nl = new ArrayList<Object>(3);
                nl.add(value);
                _field.set(message, nl);
            }
            
            return list;
        }
        
        public Class<?> getTypeClass()
        {
            return getMeta().getComponentTypeClass();
        }
    }
    

}
