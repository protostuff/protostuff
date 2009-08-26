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

    FieldAccess _fa;
    
    public MessagePropertyAccessor(PropertyMeta meta)
    {
        super(meta);
        
        if(meta.isRepeated())
            _fa = meta.isMessage() ? new RepeatedMessageFieldAccess() : new RepeatedFieldAccess();
        else
            _fa = meta.isMessage() ? new MessageFieldAccess() : new FieldAccess();
            
        _fa.init(meta.getField());
    }
    
    public Field getField()
    {
        return _fa._field;
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
        
        protected void init(Field field)
        {
            try
            {
                _field = field;
                
                _has = getMeta().getMessageClass().getDeclaredField(toPrefixedPascalCase("has", 
                        getMeta().getName()));
                
                Field.setAccessible(new Field[]{_field, _has}, true);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }        
        
        protected Object resolveValue(Object value, PropertyMeta meta)
        {
            return meta.getTypeClass().isPrimitive() 
                || meta.getTypeClass().isAssignableFrom(value.getClass()) ? value : null;
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
                if((value=resolveValue(value, getMeta()))==null)
                    return false;
                
                _has.setBoolean(message, true);
                _field.set(message, value);
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
            
            if((value=resolveValue(value, getMeta()))==null)
                return false;
            
            _field.set(message, value);
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
                
                if((value=resolveValue(value, getMeta()))==null)
                    return null;
                
                Object last = _field.get(message);
                _field.set(message, value);               
                return last;
            }
            return null;
        }
    }
    
    class RepeatedFieldAccess extends FieldAccess
    {
        
        protected void init(Field field)
        {
            _field = field;
            _field.setAccessible(true);
        }
        
        protected Object resolveValue(Object value, PropertyMeta meta)
        {            
            return meta.getComponentTypeClass().isPrimitive() 
                || meta.getComponentTypeClass().isAssignableFrom(value.getClass()) ? value : null;
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
                    if((value=resolveValue(value, getMeta()))==null)
                        return false;
                    
                    ArrayList<Object> nl = new ArrayList<Object>(3);
                    nl.add(value);
                    _field.set(message, nl);
                }
                else
                    list.add(value);
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
                if((value=resolveValue(value, getMeta()))==null)
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
                if((value=resolveValue(value, getMeta()))==null)
                    return null;
                    
                ArrayList<Object> nl = new ArrayList<Object>(3);
                nl.add(value);
                _field.set(message, nl);
            }
            
            return list;
        }
    }
    
    class MessageFieldAccess extends FieldAccess
    {
        protected Object resolveValue(Object value, PropertyMeta meta)
        {
            if(meta.getTypeClass()==value.getClass())
                return value;
            else if(meta.getTypeBuilderClass()==value.getClass())
                return getMessageFromBuilder(value, meta);
            return null;
        }
    }
    
    class RepeatedMessageFieldAccess extends RepeatedFieldAccess
    {
        protected Object resolveValue(Object value, PropertyMeta meta)
        {
            if(meta.getComponentTypeClass()==value.getClass())
                return value;
            else if(meta.getTypeBuilderClass()==value.getClass())
                return getMessageFromBuilder(value, meta);
            return null;
        }
    }
    

}
