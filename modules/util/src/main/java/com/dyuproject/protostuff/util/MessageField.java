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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.protobuf.AbstractMessageLite;


/**
 * @author David Yu
 * @created Aug 23, 2009
 */

public class MessageField extends AbstractField
{

    FieldAccessor _fa;
    //HasAccessor _has;
    //GetAccessor _get;
    
    
    /*protected void initHasAccessor(Method method)
    {
        if(_has!=null)
            throw new IllegalStateException("HasAccessor already set.");
        
        _has = isRepeated() ? new RepeatedHasAccessor() : new HasAccessor();
        _has._method = method;
    }
    
    protected void initGetAccessor(Method method)
    {
        if(_get!=null)
            throw new IllegalStateException("GetAccessor already set.");
        
        _get = new GetAccessor();
        _get._method = method;
    }
    
    public Object getValue(AbstractMessageLite message)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _has.hasValue(message) ? _get.getValue(message) : null;
    }*/
    
    public MessageField(Meta meta, Field field)
    {
        super(meta);
        _fa = meta.isRepeated() ? new RepeatedFieldAccessor() : new FieldAccessor();
        _fa.init(field);
    }
    
    public Object getValue(AbstractMessageLite message)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _fa.getValue(message);
    }
    
    public Object removeValue(AbstractMessageLite message) 
    throws IllegalArgumentException, IllegalAccessException
    {
        return _fa.removeValue(message);
    }
    
    public void setValue(AbstractMessageLite message, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        _fa.setValue(message, value, true);
    }
    
    public boolean replaceValueIfNone(AbstractMessageLite message, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _fa.replaceValueIfNone(message, value);
    }
    
    public Object replaceValueIfAny(AbstractMessageLite message, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _fa.replaceValueIfAny(message, value);
    }
    
    /*class HasAccessor
    {
        Method _method;
        public boolean hasValue(AbstractMessageLite message) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return Boolean.TRUE.equals(_method.invoke(message, NO_ARG));
        }
    }
    
    class RepeatedHasAccessor extends HasAccessor
    {
        public boolean hasValue(AbstractMessageLite message) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return !ZERO_COUNT.equals(_method.invoke(message, NO_ARG));
        }
    }
    
    class GetAccessor
    {
        Method _method;        
        public Object getValue(AbstractMessageLite message) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return _method.invoke(message, NO_ARG);
        }
    }*/
    
    class FieldAccessor
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
        
        public Object getValue(AbstractMessageLite message) 
        throws IllegalArgumentException, IllegalAccessException
        {
            return _has.getBoolean(message) ? _field.get(message) : null;
        }
        
        public Object removeValue(AbstractMessageLite message)
        throws IllegalArgumentException, IllegalAccessException
        {
            Object value = null;
            if(_has.getBoolean(message))
            {
                value = _field.get(message);
                _has.setBoolean(message, false);
            }
            return value;
        }
        
        public void setValue(AbstractMessageLite message, Object value, boolean clearIfNullOrEmpty) 
        throws IllegalArgumentException, IllegalAccessException
        {
            if(value!=null)
            {
                _has.setBoolean(message, true);
                _field.set(message, value);
            }
            else if(clearIfNullOrEmpty)
                _has.setBoolean(message, false);
        }
        
        public boolean replaceValueIfNone(AbstractMessageLite message, Object value)
        throws IllegalArgumentException, IllegalAccessException
        {
            if(value==null || _has.getBoolean(message))
                return false;
            
            _field.set(message, value);
            return true;
        }
        
        public Object replaceValueIfAny(AbstractMessageLite message, Object value)
        throws IllegalArgumentException, IllegalAccessException
        {
            if(_has.getBoolean(message))
            {
                Object last = _field.get(message);
                if(value==null)
                    _has.setBoolean(message, false);
                else
                    _field.set(message, value);                
                return last;
            }
            return null;
        }
    }
    
    class RepeatedFieldAccessor extends FieldAccessor
    {
        
        protected void init(Field field)
        {
            _field = field;
            _field.setAccessible(true);
        }
        
        @SuppressWarnings("unchecked")
        public Object getValue(AbstractMessageLite message) 
        throws IllegalArgumentException, IllegalAccessException
        {
            List<Object> list = (List<Object>)_field.get(message);
            return list.size()==0 ? null : list;
        }
        
        @SuppressWarnings("unchecked")
        public Object removeValue(AbstractMessageLite message)
        throws IllegalArgumentException, IllegalAccessException
        {
            List<Object> list = (List<Object>)_field.get(message);
            if(list.size()!=0)
            {
                _field.set(message, Collections.emptyList());
                return list;
            }
            return null;
        }
        
        @SuppressWarnings("unchecked")
        public void setValue(AbstractMessageLite message, Object value, boolean clearIfNullOrEmpty) 
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
                        if(list.size()!=0 && clearIfNullOrEmpty)
                            _field.set(message, Collections.emptyList());
                        return;
                    }
                    list.addAll(vl);
                }                    
                else if(list.size()==0)
                {
                    ArrayList<Object> nl = new ArrayList<Object>();
                    nl.add(value);
                    _field.set(message, nl);
                }
                else
                    list.add(value);
            }
            else if(list.size()!=0 && clearIfNullOrEmpty)
                _field.set(message, Collections.emptyList());
        }
        
        @SuppressWarnings("unchecked")
        public boolean replaceValueIfNone(AbstractMessageLite message, Object value)
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
                ArrayList<Object> nl = new ArrayList<Object>();
                nl.add(value);
                _field.set(message, nl);
            }
            
            return true;
        }
        
        @SuppressWarnings("unchecked")
        public Object replaceValueIfAny(AbstractMessageLite message, Object value)
        throws IllegalArgumentException, IllegalAccessException
        {
            List<Object> list = (List<Object>)_field.get(message);
            if(list.size()==0)
                return null;
            
            if(value==null)
                _field.set(message, Collections.emptyList());
            else if(List.class.isAssignableFrom(value.getClass()))
            {
                List<Object> vl = (List<Object>)value;
                if(vl.size()==0)
                    return null;
                
                _field.set(message, vl);
            }
            else
            {
                ArrayList<Object> nl = new ArrayList<Object>();
                nl.add(value);
                _field.set(message, nl);
            }
            
            return list;
        }
    }
    

}
