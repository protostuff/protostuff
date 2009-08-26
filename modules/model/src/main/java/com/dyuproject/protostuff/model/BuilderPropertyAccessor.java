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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * BuilderPropertyAccessor - read-and-write access on MessageLite.Builder
 * 
 * @author David Yu
 * @created Aug 23, 2009
 */

public class BuilderPropertyAccessor extends PropertyAccessor
{
    
    HasMethod _has;
    GetMethod _get;
    ClearMethod _clear;
    SetMethod _set;
    
    public BuilderPropertyAccessor(PropertyMeta meta)
    {
        super(meta);
        
        _get = new GetMethod();                
        _clear = new ClearMethod();
        
        if(meta.isRepeated())
        {
            _has = new RepeatedHasMethod();            
            _set = meta.isMessage() ? new RepeatedMessageSetMethod() : new RepeatedSetMethod();
        }
        else
        {
            _has = new HasMethod();
            _set = meta.isMessage() ? new MessageSetMethod() : new SetMethod();
        }
        
        _get.init(meta, meta.getBuilderClass());
        _clear.init(meta);
        _has.init(meta, meta.getBuilderClass());
        _set.init(meta);
    }
    
    public Object getValue(Object builder)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _has.hasValue(builder) ? _get.getValue(builder) : null;
    }
    
    public Object removeValue(Object builder)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_has.hasValue(builder))
        {
            Object value = _get.getValue(builder);
            _clear.clearValue(builder);
            return value;
        }
        return null;
    }
    
    public boolean setValue(Object builder, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(value==null)
        {
            _clear.clearValue(builder);
            return true;
        }
        
        return _set.setValue(builder, value);
    }
    
    public boolean replaceValueIfNone(Object builder, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(value==null || _has.hasValue(builder))
            return false;
        
        _set.setValue(builder, value);
        return true;
    }
    
    public Object replaceValueIfAny(Object builder, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_has.hasValue(builder))
        {
            Object last = _get.getValue(builder);
            if(value==null)
                _clear.clearValue(builder);
            else
                _set.setValue(builder, value);
            return last;
        }
        return null;
    }
    
    static class ClearMethod
    {
        Method _method;
        
        protected void init(PropertyMeta meta)
        {
            try
            {
                _method = meta.getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("clear", meta.getName()), NO_ARG_C);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public void clearValue(Object builder) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            _method.invoke(builder, NO_ARG);
        }
    }
    
    static class SetMethod
    {
        Method _method, _builderSet;
        PropertyMeta _meta;
        
        protected void init(PropertyMeta meta)
        {
            _meta = meta;
            try
            {
                _method = meta.getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("set", meta.getName()), 
                        new Class<?>[]{meta.getTypeClass()});
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        protected Object resolveValue(Object value)
        {
            return _meta.getTypeClass().isPrimitive() 
                || _meta.getTypeClass().isAssignableFrom(value.getClass()) ? value : null;
        }
        
        public boolean setValue(Object builder, Object value) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            if((value=resolveValue(value))==null)
                return false;
            
            _method.invoke(builder, value);
            return true;
        }
    }
    
    static class RepeatedSetMethod extends SetMethod
    {
        Method _componentAdd, _builderAdd;
        PropertyMeta _meta;
        
        protected void init(PropertyMeta meta)
        {
            _meta = meta;
            try
            {
                _method = meta.getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("addAll", meta.getName()), ITERABLE_ARG_C);
                _componentAdd = meta.getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("add", meta.getName()), 
                        new Class<?>[]{meta.getComponentTypeClass()});
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        protected Object resolveValue(Object value)
        {
            return _meta.getComponentTypeClass().isPrimitive() 
                || _meta.getComponentTypeClass().isAssignableFrom(value.getClass()) ? value : null;
        }        
        
        public boolean setValue(Object builder, Object value) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            if(List.class.isAssignableFrom(value.getClass()))
                _method.invoke(builder, value);
            else
            {
                if((value=resolveValue(value))==null)
                    return false;
                
                _componentAdd.invoke(builder, value);
            }
            
            return true;
        }
    }
    
    static class MessageSetMethod extends SetMethod
    {
        protected Object resolveValue(Object value)
        {
            if(_meta.getTypeClass()==value.getClass())
                return value;
            else if(_meta.getTypeBuilderClass()==value.getClass())
                return _meta.getResolver().resolveValue(value, _meta);
            return null;
        }
    }
    
    class RepeatedMessageSetMethod extends RepeatedSetMethod
    {
        protected Object resolveValue(Object value)
        {
            if(_meta.getComponentTypeClass()==value.getClass())
                return value;
            else if(_meta.getTypeBuilderClass()==value.getClass())
                return _meta.getResolver().resolveValue(value, _meta);
            return null;
        }
    }

}
