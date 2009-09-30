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

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.AbstractMessageLite;

/**
 * Model - the protobuf model from its generated classes
 * 
 * @author David Yu
 * @created Aug 25, 2009
 */

public class Model
{
    
    public static Model get(Class<? extends AbstractMessageLite> clazz)
    {
        return get(clazz, false);
    }
    
    public static Model get(Class<? extends AbstractMessageLite> clazz, boolean readOnly)
    {
        return get(LiteRuntime.getModelMeta(clazz), readOnly);
    }
    
    public static Model get(ModelMeta modelMeta)
    {
        return get(modelMeta, false);
    }

    public static Model get(ModelMeta modelMeta, boolean readOnly)
    {
        return new Model(modelMeta, readOnly ? ReadOnlyProperty.FACTORY : DefaultProperty.FACTORY);
    }
    
    private final ModelMeta _modelMeta;
    private final Property[] _propsByNumber, _props;
    private final Map<String, Property> _propsByName;
    
    public Model(ModelMeta modelMeta, Property.Factory pf)
    {
        _modelMeta = modelMeta;
        _propsByNumber = new Property[_modelMeta.getMaxNumber()+1];
        _propsByName = new HashMap<String,Property>(modelMeta.getPropertyCount());
        
        for(PropertyMeta pm : _modelMeta.getPropertyMetaMap().values())
        {
            Property prop = pf.create(pm);
            _propsByNumber[pm.getNumber()] = prop;
            _propsByName.put(pm.getName(), prop);
        }
        
        _props = new Property[_modelMeta.getPropertyCount()];
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
        PropertyMeta pm = _modelMeta.getPropertyMetaMap().get(normalizedName);
        return pm==null ? null : _propsByNumber[pm.getNumber()];
    }
    
    public Property getProperty(int num)
    {
        return _propsByNumber[num];
    }
    
    public Property getProperty(String name)
    {
        return _propsByName.get(name);
    }
    
    public Property[] getProperties()
    {
        return _props;
    }
    
}
