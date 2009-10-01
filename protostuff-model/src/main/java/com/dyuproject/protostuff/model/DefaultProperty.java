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
    
    public static final Property.Factory<DefaultProperty> FACTORY = 
        new Property.Factory<DefaultProperty>()
    {        
        public DefaultProperty create(PropertyMeta propertyMeta)
        {
            return new DefaultProperty(propertyMeta);
        }

        public Class<DefaultProperty> getType()
        {
            return DefaultProperty.class;
        }
    };
    
    protected final MessagePropertyAccessor _messagePropertyAccessor;
    protected final BuilderPropertyAccessor _builderPropertyAccessor;
    
    public DefaultProperty(PropertyMeta propertyMeta)
    {
        this(propertyMeta, new MessagePropertyAccessor(propertyMeta), 
                new BuilderPropertyAccessor(propertyMeta));
    }
    
    public DefaultProperty(PropertyMeta propertyMeta, MessagePropertyAccessor messageAccessor, 
            BuilderPropertyAccessor builderPropertyAccessor)
    {
        super(propertyMeta);
        _messagePropertyAccessor = messageAccessor;
        _builderPropertyAccessor = builderPropertyAccessor;
    }
    
    public MessagePropertyAccessor getMessagePropertyAccessor()
    {
        return _messagePropertyAccessor;
    }
    
    public BuilderPropertyAccessor getBuilderPropertyAccessor()
    {
        return _builderPropertyAccessor;
    }

    public Object getValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messagePropertyAccessor.getValue(target);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderPropertyAccessor.getValue(target);
        
        return null;
    }
    
    public Object removeValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messagePropertyAccessor.removeValue(target);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderPropertyAccessor.removeValue(target);
        
        return null;
    }
    
    public boolean setValue(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messagePropertyAccessor.setValue(target, value);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderPropertyAccessor.setValue(target, value);
        
        return false;
    }
    
    public boolean replaceValueIfNone(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messagePropertyAccessor.replaceValueIfNone(target, value);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderPropertyAccessor.replaceValueIfNone(target, value);
        
        return false;
    }
    
    public Object replaceValueIfAny(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messagePropertyAccessor.replaceValueIfAny(target, value);
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderPropertyAccessor.replaceValueIfAny(target, value);
        
        return null;
    }

}
