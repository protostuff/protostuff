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

/**
 * Property - api to access to the equivalent property of a generated protobuf class
 * 
 * @author David Yu
 * @created Aug 26, 2009
 */

public abstract class Property
{

    protected PropertyMeta _propertyMeta;
    
    public Property(PropertyMeta propertyMeta)
    {
        _propertyMeta = propertyMeta;
    }
    
    public PropertyMeta getPropertyMeta()
    {
        return _propertyMeta;
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
    
    public interface Factory
    {
        public Property create(PropertyMeta propertyMeta);
    }
    
}
