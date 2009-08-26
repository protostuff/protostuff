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

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.AbstractMessageLite;

/**
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
    
    private ModelMeta _modelMeta;
    private Property[] _propsByNumber, _props;
    private Map<String, Property> _propsByName;
    
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
