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

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.ProtostuffException;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;


/**
 * A runtime field w/c represents a polymorphic pojo(abstract class or interface).
 * 
 * The type metadata is written for the deserializer to know the actual/exact schema to 
 * use upon deserialization.
 * 
 * Limitations:
 * The number of fields are limited to 126 (127 is the usual limit anyway).
 * The order of the fields being written must be preserved.
 * It will only work with protostuff, protobuf and json(numeric) format.
 * It will not work if the message serialized is coming from the browser 
 * since the fields will most likey be out-of-order 
 * (unless you have control of the json serialization).
 *
 * @author David Yu
 * @created Jan 16, 2011
 */
public abstract class PolymorphicRuntimeField<T> extends Field<T>
{

    /**
     * The polymorphic schema.
     */
    final Schema<Object> schema = new Schema<Object>()
    {

        public String getFieldName(int number)
        {
            return Integer.toString(number);
        }

        public int getFieldNumber(String name)
        {
            return Integer.parseInt(name);
        }

        public boolean isInitialized(Object owner)
        {
            return true;
        }

        public String messageFullName()
        {
            return baseClass.getName();
        }

        public String messageName()
        {
            return baseClass.getSimpleName();
        }

        public Object newMessage()
        {
            // cannot instantiate an abstract type.
            throw new UnsupportedOperationException();
        }

        public Class<? super Object> typeClass()
        {
            return baseClass;
        }
        
        /**
         * Delegates to the derived schema from the type metadata.
         */
        public void mergeFrom(Input input, final Object owner) throws IOException
        {
            final int first = input.readFieldNumber(schema);
            if(first != 127)
                throw new ProtostuffException("order not preserved.");
            
            final String className = input.readString();
            final Schema<Object> schema = RuntimeSchema.getSchema(className, 
                    RuntimeSchema.AUTO_LOAD_POLYMORPHIC_CLASSES);
            
            if(schema == null)
                throw new ProtostuffException("polymorphic pojo not registered: " + className);
            
            doMergeFrom(input, schema, owner);
        }
        
        /**
         * Delegates to the derived schema from the type metadata.
         */
        @SuppressWarnings("unchecked")
        public void writeTo(final Output output, final Object value) throws IOException
        {
            final Schema<Object> schema = 
                RuntimeSchema.getSchema((Class<Object>)value.getClass());
            
            // write the type
            output.writeString(127, value.getClass().getName(), false);
            
            // write the rest of the fields of the exact type
            schema.writeTo(output, value);
        }
    };
    
    /**
     * The polymorphic pipe schema.
     */
    final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(schema)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            final int first = input.readFieldNumber(schema);
            if(first != 127)
                throw new ProtostuffException("order not preserved.");
            
            final String className = input.readString();
            
            final MappedSchema<Object> schema;
            try
            {
                schema = (MappedSchema<Object>)RuntimeSchema.getSchema(className, 
                        RuntimeSchema.AUTO_LOAD_POLYMORPHIC_CLASSES);
            }
            catch(ClassCastException e)
            {
                throw new IllegalStateException("PipeSchema not available");
            }
            
            if(schema == null)
            {
                throw new ProtostuffException("polymorphic pojo not registered: " + 
                        className);
            }
            
            output.writeString(127, className, false);
            schema.pipeSchema.transfer(pipe, input, output);
        }
        
    };
    
    final Class<Object> baseClass;
    
    public PolymorphicRuntimeField(Class<Object> baseClass, 
            FieldType type, int number, String name)
    {
        super(type, number, name);
        this.baseClass = baseClass;
    }
    
    public PolymorphicRuntimeField(Class<Object> baseClass, 
            FieldType type, int number, String name, boolean repeated)
    {
        super(type, number, name, repeated);
        this.baseClass = baseClass;
    }
    
    protected abstract void doMergeFrom(Input input, Schema<Object> derivedSchema, 
            Object owner) throws IOException;
}
