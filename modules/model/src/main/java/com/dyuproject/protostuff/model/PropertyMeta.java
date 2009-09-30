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

import java.lang.reflect.Field;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;

/**
 * PropertyMeta - the metadata of a protobuf (generated) field. 
 * 
 * @author David Yu
 * @created Aug 25, 2009
 */

public interface PropertyMeta
{
    
    public Class<? extends AbstractMessageLite> getMessageClass();
    public Class<? extends Builder<?>> getBuilderClass();
    public Class<?> getTypeClass();
    public Class<?> getComponentTypeClass();
    public int getNumber();
    public boolean isRepeated();
    public boolean isMessage();
    public String getName();
    public String getNormalizedName();
    public Field getField();

}
