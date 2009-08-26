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
 * DefaultProperty - read-and-write access on MessageLite and MessageLite.Builder
 * 
 * @author David Yu
 * @created Aug 26, 2009
 */

public class DefaultProperty extends Property
{
    
    public static final Property.Factory FACTORY = new Property.Factory()
    {        
        public Property create(PropertyMeta propertyMeta)
        {
            return new DefaultProperty(propertyMeta);
        }
    };
    
    private MessagePropertyAccessor _messageAccessor;
    private BuilderPropertyAccessor _builderAccessor;
    
    public DefaultProperty(PropertyMeta propertyMeta)
    {
        super(propertyMeta);
        _messageAccessor = new MessagePropertyAccessor(propertyMeta);
        _builderAccessor = new BuilderPropertyAccessor(propertyMeta);
    }
    
    public MessagePropertyAccessor getMessageAccessor()
    {
        return _messageAccessor;
    }
    
    public BuilderPropertyAccessor getBuilderAccessor()
    {
        return _builderAccessor;
    }

    public Object getValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messageAccessor.getValue(target);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderAccessor.getValue(target);
        
        return null;
    }
    
    public Object removeValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messageAccessor.removeValue(target);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderAccessor.removeValue(target);
        
        return null;
    }
    
    public boolean setValue(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messageAccessor.setValue(target, value);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderAccessor.setValue(target, value);
        
        return false;
    }
    
    public boolean replaceValueIfNone(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messageAccessor.replaceValueIfNone(target, value);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderAccessor.replaceValueIfNone(target, value);
        
        return false;
    }
    
    public Object replaceValueIfAny(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messageAccessor.replaceValueIfAny(target, value);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderAccessor.replaceValueIfAny(target, value);
        
        return null;
    }

}
