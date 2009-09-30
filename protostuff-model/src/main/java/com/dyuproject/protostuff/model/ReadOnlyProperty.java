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

import com.dyuproject.protostuff.model.PropertyAccessor.GetMethod;
import com.dyuproject.protostuff.model.PropertyAccessor.HasMethod;
import com.dyuproject.protostuff.model.PropertyAccessor.RepeatedHasMethod;

/**
 * ReadOnlyProperty - read-only access on MessageLite and MessageLite.Builder
 * 
 * @author David Yu
 * @created Aug 26, 2009
 */

public class ReadOnlyProperty  extends Property
{
    
    public static final Property.Factory FACTORY = new Property.Factory()
    {        
        public Property create(PropertyMeta propertyMeta)
        {
            return new ReadOnlyProperty(propertyMeta);
        }
    };
    
    private final GetMethod _messageGet, _builderGet;
    private final HasMethod _messageHas, _builderHas;
    
    public ReadOnlyProperty(PropertyMeta propertyMeta)
    {
        super(propertyMeta);
        
        _messageGet = new GetMethod();
        _builderGet = new GetMethod();
        
        if(propertyMeta.isRepeated())
        {
            _messageHas = new RepeatedHasMethod();
            _builderHas = new RepeatedHasMethod();                
        }
        else
        {
            _messageHas = new HasMethod();
            _builderHas = new HasMethod();   
        }
        
        _messageHas.init(propertyMeta, propertyMeta.getMessageClass());
        _messageGet.init(propertyMeta, propertyMeta.getMessageClass());
        
        _builderHas.init(propertyMeta, propertyMeta.getBuilderClass());
        _builderGet.init(propertyMeta, propertyMeta.getBuilderClass());
    }
    
    public Object getValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if(_propertyMeta.getMessageClass()==target.getClass())
            return _messageHas.hasValue(target) ? _messageGet.getValue(target) : null;
        else if(_propertyMeta.getBuilderClass()==target.getClass())
            return _builderHas.hasValue(target) ? _builderGet.getValue(target) : null;
            
        return null;
    }
    
    public Object removeValue(Object target) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        throw new UnsupportedOperationException();
    }
    
    public boolean setValue(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        throw new UnsupportedOperationException();
    }
    
    public boolean replaceValueIfNone(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        throw new UnsupportedOperationException();
    }
    
    public Object replaceValueIfAny(Object target, Object value) 
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        throw new UnsupportedOperationException();
    }
}
