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
 * Property - api to access to the equivalent property of a generated protobuf class
 * 
 * @author David Yu
 * @created Aug 26, 2009
 */

public abstract class Property
{

    protected final PropertyMeta _propertyMeta;
    
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
    
    public interface Factory<T extends Property>
    {
        public T create(PropertyMeta propertyMeta);
        
        public Class<T> getType();
    }
    
}
