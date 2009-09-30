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
            _set = new RepeatedSetMethod();
        }
        else
        {
            _has = new HasMethod();
            _set = new SetMethod();
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
        ParameterType _type;
        
        protected void init(PropertyMeta meta)
        {
            _meta = meta;
            try
            {
                _method = meta.getBuilderClass().getDeclaredMethod(
                        toPrefixedPascalCase("set", meta.getName()), 
                        new Class<?>[]{meta.getTypeClass()});
                
                _type = _meta.isMessage() ? 
                        ParameterType.getMessageType(_meta.getTypeClass()) : 
                            ParameterType.getSimpleType(_meta.getTypeClass());
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public boolean setValue(Object builder, Object value) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            if((value=_type.resolveValue(value))==null)
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
                
                _type = _meta.isMessage() ? 
                        ParameterType.getMessageType(_meta.getComponentTypeClass()) : 
                            ParameterType.getSimpleType(_meta.getComponentTypeClass());
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }      
        
        public boolean setValue(Object builder, Object value) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            if(List.class.isAssignableFrom(value.getClass()))
                _method.invoke(builder, value);
            else
            {
                if((value=_type.resolveValue(value))==null)
                    return false;
                
                _componentAdd.invoke(builder, value);
            }
            
            return true;
        }
    }

}
