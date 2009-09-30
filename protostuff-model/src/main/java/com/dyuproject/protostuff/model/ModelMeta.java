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

import java.util.Map;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;

/**
 * ModelMeta - metadata for the compiled protobuf classes
 * 
 * @author David Yu
 * @created Aug 25, 2009
 */

public class ModelMeta
{    
    
    private final Class<? extends AbstractMessageLite> _messageClass;
    private final Class<? extends Builder<?>> _builderClass;
    private final Map<String, ? extends PropertyMeta> _propertyMetaMap;
    private final int _minNumber, _maxNumber;
    
    public ModelMeta(Class<? extends AbstractMessageLite> messageClass,
            Class<? extends Builder<?>> builderClass, 
            Map<String, ? extends PropertyMeta> propertyMetaMap,
            int minNumber,
            int maxNumber)
    {
        _messageClass = messageClass;
        _builderClass = builderClass;
        _propertyMetaMap = propertyMetaMap;
        _minNumber = minNumber;
        _maxNumber = maxNumber;
    }
                    
    
    public Class<? extends AbstractMessageLite> getMessageClass()
    {
        return _messageClass;
    }
    
    public Class<? extends Builder<?>> getBuilderClass()
    {
        return _builderClass;
    }
    
    public Map<String, ? extends PropertyMeta> getPropertyMetaMap()
    {
        return _propertyMetaMap;
    }
    
    public int getPropertyCount()
    {
        return _propertyMetaMap.size();
    }
    
    public int getMinNumber()
    {
        return _minNumber;
    }
    
    public int getMaxNumber()
    {
        return _maxNumber;
    }
    
    public String toString()
    {
        return new StringBuilder()
            .append(_messageClass.getSimpleName())
            .append(" properties=").append(getPropertyCount())
            .append(" minNum=").append(getMinNumber())
            .append(" maxNum=").append(getMaxNumber())
            .append('\n').append(_propertyMetaMap.values())
            .toString();
    }
    
    public interface Factory
    {
        public ModelMeta create(Class<? extends AbstractMessageLite> messageClass);
    }
 

}
