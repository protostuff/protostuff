//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.runtime;

import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * A runtime field whose field type is {@link Object} (dynamic).
 *
 * @author David Yu
 * @created Feb 1, 2011
 */
abstract class RuntimeObjectField<T> extends Field<T>
{
    
    /**
     * The schema of the object (dynamic) field.
     */
    public final ObjectSchema schema = new ObjectSchema()
    {
        protected void setValue(Object value, Object owner)
        {
            RuntimeObjectField.this.setValue(value, owner);
        }
    };
    
    public RuntimeObjectField(FieldType type, int number, String name, boolean repeated)
    {
        super(type, number, name, repeated);
    }
    
    protected abstract void setValue(Object value, Object owner);

}
