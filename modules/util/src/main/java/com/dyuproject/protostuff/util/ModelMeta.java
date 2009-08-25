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

import java.util.Map;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;

/**
 * @author David Yu
 * @created Aug 25, 2009
 */

public class ModelMeta
{    
    
    private Class<? extends AbstractMessageLite> _messageClass;
    private Class<? extends Builder<?>> _builderClass;
    private Map<String, ? extends PropertyMeta> _propertyMetaMap;
    private int _minNumber, _maxNumber;
    
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
