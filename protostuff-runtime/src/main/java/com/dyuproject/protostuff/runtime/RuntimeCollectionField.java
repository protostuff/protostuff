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

import java.io.IOException;
import java.util.Collection;

import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.CollectionSchema.MessageFactory;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * A runtime field for a {@link Collection}.
 * Null values are not written.
 *
 * @author David Yu
 * @created Jan 26, 2011
 */
abstract class RuntimeCollectionField<T,V> extends Field<T>
{
    
    /**
     * Since we cannot inherit multiple classes, we create this Collection schema simply 
     * to delegate to the wrapping class' abstract methods.
     */
    protected final CollectionSchema<V> schema;

    public RuntimeCollectionField(FieldType type, int number, String name, 
            MessageFactory messageFactory)
    {
        super(type,number,name, false);
        schema = new CollectionSchema<V>(messageFactory)
        {
            protected void addValueFrom(Input input, Collection<V> collection) 
            throws IOException
            {
                RuntimeCollectionField.this.addValueFrom(input, collection);
            }
            protected void writeValueTo(Output output, int fieldNumber, V value, 
                    boolean repeated) throws IOException
            {
                RuntimeCollectionField.this.writeValueTo(output, fieldNumber, value, 
                        repeated);
            }
            protected void transferValue(Pipe pipe, Input input, Output output, 
                    int number, boolean repeated) throws IOException
            {
                RuntimeCollectionField.this.transferValue(pipe, input, output, number, 
                        repeated);
            }
        };
    }
    
    protected abstract void addValueFrom(Input input, Collection<V> collection) 
            throws IOException;
    
    protected abstract void writeValueTo(Output output, int fieldNumber, V value, 
            boolean repeated) throws IOException;
    
    protected abstract void transferValue(Pipe pipe, Input input, Output output, 
            int number, boolean repeated) throws IOException;

}
