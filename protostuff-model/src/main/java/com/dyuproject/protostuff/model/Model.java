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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.AbstractMessageLite;

/**
 * Model - the protobuf model from its generated classes
 * 
 * @author David Yu
 * @created Aug 25, 2009
 */

public final class Model<T extends Property>
{
    
    public static Model<DefaultProperty> get(Class<? extends AbstractMessageLite> clazz)
    {
        return new Model<DefaultProperty>(LiteRuntime.getModelMeta(clazz), DefaultProperty.FACTORY);
    }
    
    public static Model<DefaultProperty> get(ModelMeta modelMeta)
    {
        return new Model<DefaultProperty>(modelMeta, DefaultProperty.FACTORY);
    }
    
    private ModelMeta _modelMeta;
    private T[] _propsByNumber, _props;
    private Map<String, T> _propsByName;
    
    @SuppressWarnings("unchecked")
    public Model(ModelMeta modelMeta, Property.Factory<T> pf)
    {
        _modelMeta = modelMeta;
        _propsByNumber = (T[])Array.newInstance(pf.getType(), _modelMeta.getMaxNumber()+1);
        _propsByName = new HashMap<String,T>(modelMeta.getPropertyCount());

        for(PropertyMeta pm : _modelMeta.getPropertyMetas())
        {
            T prop = pf.create(pm);
            _propsByNumber[pm.getNumber()] = prop;
            _propsByName.put(pm.getName(), prop);
        }
        
        _props = (T[])Array.newInstance(pf.getType(), _modelMeta.getPropertyCount());
        for(int i=1, j=0; i<_propsByNumber.length; i++)
        {
            if(_propsByNumber[i]!=null)
                _props[j++] = _propsByNumber[i];
        }
    }
    
    public ModelMeta getModelMeta()
    {
        return _modelMeta;
    }
    
    public Property getPropertyByNormalizedName(String normalizedName)
    {
        PropertyMeta pm = _modelMeta.getPropertyMeta(normalizedName);
        return pm==null ? null : _propsByNumber[pm.getNumber()];
    }
    
    public T getProperty(int num)
    {
        return _propsByNumber[num];
    }
    
    public T getProperty(String name)
    {
        return _propsByName.get(name);
    }
    
    public T[] getProperties()
    {
        return _props;
    }
    
}
