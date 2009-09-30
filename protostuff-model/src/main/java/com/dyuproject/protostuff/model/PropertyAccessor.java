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


/**
 * PropertyAccessor - access to the equivalent property of a generated protobuf class
 * 
 * @author David Yu
 * @created Aug 23, 2009
 */

public abstract class PropertyAccessor
{
    
    public static final Object[] NO_ARG = new Object[]{};
    public static final Integer ZERO_COUNT = Integer.valueOf(0);
    public static final Class<?>[] NO_ARG_C = new Class<?>[]{};
    public static final Class<?>[] INT_ARG_C = new Class<?>[]{int.class};
    public static final Class<?>[] ITERABLE_ARG_C = new Class<?>[]{Iterable.class};
    
    static String toPrefixedPascalCase(String prefix, String target)
    {
        char c = target.charAt(0);
        if(c>96)
            c = (char)(c-32);
        
        return new StringBuilder(prefix.length() + target.length())
            .append(prefix)
            .append(c)
            .append(target, 1, target.length())
            .toString();
    }
    
    static String toCamelCase(int start, String target)
    {
        char[] prop = new char[target.length()-start];
        target.getChars(start, target.length(), prop, 0);
        if(prop[0]<91)
            prop[0] = (char)(prop[0] + 32);

        return new String(prop);
    }
    
    private final PropertyMeta _meta;
    
    public PropertyAccessor(PropertyMeta meta)
    {
        _meta = meta;
    }
    
    public PropertyMeta getMeta()
    {
        return _meta;
    }
    
    public abstract Object getValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    
    public abstract Object removeValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    
    public abstract boolean setValue(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    
    public abstract boolean replaceValueIfNone(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    
    public abstract Object replaceValueIfAny(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
    
    static class HasMethod
    {
        Method _method;
        
        protected void init(PropertyMeta meta, Class<?> targetClass)
        {
            try
            {
                _method = targetClass.getDeclaredMethod(
                        toPrefixedPascalCase("has", meta.getName()), NO_ARG_C);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public boolean hasValue(Object target) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return Boolean.TRUE.equals(_method.invoke(target, NO_ARG));
        }
    }
    
    static class RepeatedHasMethod extends HasMethod
    {
        protected void init(PropertyMeta meta, Class<?> targetClass)
        {
            try
            {
                _method = targetClass.getDeclaredMethod(
                        toPrefixedPascalCase("get", meta.getName() + "Count"), NO_ARG_C);
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public boolean hasValue(Object target) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return !ZERO_COUNT.equals(_method.invoke(target, NO_ARG));
        }
    }
    
    static class GetMethod
    {
        Method _method;
        
        protected void init(PropertyMeta meta, Class<?> targetClass)
        {
            try
            {
                if(meta.isRepeated())
                {
                    _method = targetClass.getDeclaredMethod(
                            toPrefixedPascalCase("get", meta.getName() + "List"), NO_ARG_C);
                }
                else
                {
                    _method = targetClass.getDeclaredMethod(
                            toPrefixedPascalCase("get", meta.getName()), NO_ARG_C);
                }
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        public Object getValue(Object target) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return _method.invoke(target, NO_ARG);
        }
    }
    
}
