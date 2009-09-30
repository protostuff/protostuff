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
