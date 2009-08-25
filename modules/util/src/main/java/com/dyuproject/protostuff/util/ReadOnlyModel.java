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
import java.util.HashMap;
import java.util.Map;

import com.dyuproject.protostuff.util.PropertyAccessor.GetMethod;
import com.dyuproject.protostuff.util.PropertyAccessor.HasMethod;
import com.dyuproject.protostuff.util.PropertyAccessor.RepeatedHasMethod;

/**
 * @author David Yu
 * @created Aug 25, 2009
 */

public class ReadOnlyModel
{
    
    private ModelMeta _modelMeta;
    private Property[] _props;
    private Map<String, Property> _propMap;
    
    public ReadOnlyModel(ModelMeta modelMeta)
    {
        _modelMeta = modelMeta;
        _props = new Property[_modelMeta.getMaxNumber()+1];
        _propMap = new HashMap<String,Property>(modelMeta.getPropertyCount());
        
        for(PropertyMeta pm : modelMeta.getPropertyMetaMap().values())
        {
            Property prop = new Property(pm);
            _props[pm.getNumber()] = prop;
            _propMap.put(pm.getName(), prop);
        }
    }
    
    public Property getProperty(int num)
    {
        return _props[num];
    }
    
    public Property getProperty(String name)
    {
        return _propMap.get(name);
    }
    
    public static class Property
    {
        
        private PropertyMeta _propertyMeta;
        private GetMethod _messageGet = new GetMethod(), _builderGet = new GetMethod();
        private HasMethod _messageHas, _builderHas;
        
        Property(PropertyMeta propertyMeta)
        {
            _propertyMeta = propertyMeta;
            _messageGet = new GetMethod();
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
        
        public PropertyMeta getMeta()
        {
            return _propertyMeta;
        }
        
        public Object getValue(Object target) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            if(_propertyMeta.getMessageClass()==target.getClass())
            {
                return _messageHas.hasValue(target) ? _messageGet.getValue(target) : null;
            }
            else if(_propertyMeta.getBuilderClass()==target.getClass())
            {
                return _builderHas.hasValue(target) ? _builderGet.getValue(target) : null;
            }
            return null;
        }
        
        
    }

}
