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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.google.protobuf.AbstractMessageLite.Builder;

/**
 * @author David Yu
 * @created Aug 23, 2009
 */

public class BuilderField extends AbstractField
{
    
    HasAccessor _has;
    GetAccessor _get;
    ClearAccessor _clear;
    SetAccessor _set;
    
    public BuilderField(Meta meta)
    {
        super(meta);
        _get = new GetAccessor();                
        _clear = new ClearAccessor();
        
        if(meta.isRepeated())
        {
            _has = new RepeatedHasAccessor();            
            _set = new RepeatedSetAccessor();
        }
        else
        {
            _has = new HasAccessor();
            _set = new SetAccessor();
        }
        
        _get.init();
        _clear.init();
        _has.init();
        _set.init();
    }
    
    public Object getValue(Builder<?> builder)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        return _has.hasValue(builder) ? _get.getValue(builder) : null;
    }
    
    public Object removeValue(Builder<?> builder)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Object value = null;
        if(_has.hasValue(builder))
        {
            value = _get.getValue(builder);
            _clear.clearValue(builder);
        }
        return value;
    }
    
    public void setValue(Builder<?> builder, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(value==null)
            _clear.clearValue(builder);
        else
            _set.setValue(builder, value);
    }
    
    public boolean replaceValueIfNone(Builder<?> builder, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(value==null || _has.hasValue(builder))
            return false;
        
        _set.setValue(builder, value);
        return true;
    }
    
    public Object replaceValueIfAny(Builder<?> builder, Object value)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_has.hasValue(builder))
        {
            Object last = _get.getValue(builder);
            _clear.clearValue(builder);
            if(value!=null)
                _set.setValue(builder, value);
            return last;
        }
        return null;
    }
    
    class HasAccessor
    {
        Method _method;
        
        protected void init()
        {
            try
            {
                _method = getMeta().getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("has", getMeta().getName()), NO_ARG_C);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public boolean hasValue(Builder<?> builder) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return Boolean.TRUE.equals(_method.invoke(builder, NO_ARG));
        }
    }
    
    class RepeatedHasAccessor extends HasAccessor
    {
        protected void init()
        {
            try
            {
                _method = getMeta().getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("get", getMeta().getName() + "Count"), NO_ARG_C);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public boolean hasValue(Builder<?> builder) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return !ZERO_COUNT.equals(_method.invoke(builder, NO_ARG));
        }
    }
    
    class GetAccessor
    {
        Method _method;
        
        protected void init()
        {
            try
            {
                if(getMeta().isRepeated())
                {
                    _method = getMeta().getBuilderClass().getDeclaredMethod(
                            toPrefixedPascalCase("get", getMeta().getName() + "List"), NO_ARG_C);
                }
                else
                {
                    _method = getMeta().getBuilderClass().getDeclaredMethod(
                            toPrefixedPascalCase("get", getMeta().getName()), NO_ARG_C);
                }
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public Object getValue(Builder<?> builder) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return _method.invoke(builder, NO_ARG);
        }
    }
    
    class ClearAccessor
    {
        Method _method;
        
        protected void init()
        {
            try
            {
                _method = getMeta().getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("clear", getMeta().getName()), NO_ARG_C);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public void clearValue(Builder<?> builder) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            _method.invoke(builder, NO_ARG);
        }
    }
    
    class SetAccessor
    {
        Method _method, _builderSet;
        Class<?> _builderClass;
        
        protected void init()
        {
            try
            {
                _method = getMeta().getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("set", getMeta().getName()), 
                        new Class<?>[]{getMeta().getTypeClass()});
                
                if(getMeta().isMessage())
                {
                    _builderClass = getMeta().getTypeClass().getDeclaredClasses()[0];
                    _builderSet = getMeta().getBuilderClass().getDeclaredMethod(
                            toPrefixedPascalCase("set", getMeta().getName()), 
                            new Class<?>[]{_builderClass});
                }
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        public void setValue(Builder<?> builder, Object value) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            if(_builderClass==value.getClass())
                _builderSet.invoke(builder, value);
            else
                _method.invoke(builder, value);
        }
    }
    
    class RepeatedSetAccessor extends SetAccessor
    {
        Method _componentAdd, _builderAdd;
        Class<?> _builderClass;
        
        protected void init()
        {
            try
            {
                _method = getMeta().getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("addAll", getMeta().getName()), ITERABLE_ARG_C);
                _componentAdd = getMeta().getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("add", getMeta().getName()), 
                        new Class<?>[]{getMeta().getComponentTypeClass()});
                
                if(getMeta().isMessage())
                {
                    _builderClass = getMeta().getComponentTypeClass().getDeclaredClasses()[0];
                    _builderAdd = getMeta().getBuilderClass().getDeclaredMethod(
                            toPrefixedPascalCase("add", getMeta().getName()), 
                            new Class<?>[]{_builderClass});                    
                }
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public void setValue(Builder<?> builder, Object value) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            if(List.class.isAssignableFrom(value.getClass()))
                _method.invoke(builder, value);
            else if(_builderClass==value.getClass())
                _builderAdd.invoke(builder, value);
            else
                _componentAdd.invoke(builder, value);
        }
    }
    
    

}
