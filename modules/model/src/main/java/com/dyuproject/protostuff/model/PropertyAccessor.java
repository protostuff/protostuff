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

import com.google.protobuf.AbstractMessageLite;


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
    
    static MessageSource __speed, __liteRuntime;
    
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
    
    static Object getMessageFromBuilder(Object builder, PropertyMeta meta)
    {
        return meta.getMessageSource().getMessage(builder);
    }
    
    private PropertyMeta _meta;
    
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
    
    public interface MessageSource
    {
        public AbstractMessageLite getMessage(Object builder);
    }
    
    static
    {        
        try
        {
            __liteRuntime = com.google.protobuf.PBLiteRuntime.MESSAGE_SOURCE;
            Class.forName("com.google.protobuf.GeneratedMessage", false, Thread.currentThread().getContextClassLoader());
            __speed = com.google.protobuf.PBSpeed.MESSAGE_SOURCE;
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Running purely in protobuf lite mode.");
        }
    }
    
}
