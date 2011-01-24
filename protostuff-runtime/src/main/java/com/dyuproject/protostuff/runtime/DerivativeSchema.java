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
import com.dyuproject.protostuff.StatefulOutput;
import com.dyuproject.protostuff.runtime.RuntimeSchema.HasSchema;

/**
 * This schema delegates to another schema derived from the input.
 *
 * @author David Yu
 * @created Jan 21, 2011
 */
public abstract class DerivativeSchema implements Schema<Object>
{
    
    private static final String FIELD_NAME_TYPE_METADATA = "_";

    public String getFieldName(int number)
    {
        return number == 127 ? FIELD_NAME_TYPE_METADATA : null;
    }

    public int getFieldNumber(String name)
    {
        return name.length() == 1 && name.charAt(0) == '_' ? 127 : 0;
    }

    public boolean isInitialized(Object owner)
    {
        return true;
    }

    public String messageFullName()
    {
        return Object.class.getName();
    }

    public String messageName()
    {
        return Object.class.getSimpleName();
    }

    public Object newMessage()
    {
        // cannot instantiate because the type is dynamic.
        throw new UnsupportedOperationException();
    }

    public Class<? super Object> typeClass()
    {
        return Object.class;
    }
    
    /**
     * Delegates to the schema derived from the input.
     * The {@code owner} owns the message (polymorphic) that is tied to this schema.
     */
    public void mergeFrom(Input input, final Object owner) throws IOException
    {
        final int first = input.readFieldNumber(this);
        if(first != 127)
            throw new ProtostuffException("order not preserved.");
        
        final String className = input.readString();
        final HasSchema<Object> wrapper = RuntimeSchema.getSchemaWrapper(className, 
                RuntimeSchema.AUTO_LOAD_POLYMORPHIC_CLASSES);
        
        if(wrapper == null)
            throw new ProtostuffException("polymorphic pojo not registered: " + className);
        
        doMergeFrom(input, wrapper.getSchema(), owner);
    }
    
    /**
     * Delegates to the schema derived from the {@code value}.
     */
    @SuppressWarnings("unchecked")
    public void writeTo(final Output output, final Object value) throws IOException
    {
        final Schema<Object> schema = 
            RuntimeSchema.getSchema((Class<Object>)value.getClass());
        
        // write the type
        output.writeString(127, value.getClass().getName(), false);
        
        if(output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput)output).updateLast(schema, this);
        }
        
        // write the rest of the fields of the exact type
        schema.writeTo(output, value);
    }
    
    /**
     * This pipe schema delegates to another schema derived from the input.
     */
    public final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
            DerivativeSchema.this)
    {
        public void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            final int first = input.readFieldNumber(DerivativeSchema.this);
            if(first != 127)
                throw new ProtostuffException("order not preserved.");
            
            final String className = input.readString();
            
            final HasSchema<Object> wrapper = RuntimeSchema.getSchemaWrapper(className, 
                    RuntimeSchema.AUTO_LOAD_POLYMORPHIC_CLASSES);
            
            if(wrapper == null)
            {
                throw new ProtostuffException("polymorphic pojo not registered: " + 
                        className);
            }
            
            output.writeString(127, className, false);
            
            final Pipe.Schema<Object> pipeSchema = wrapper.getPipeSchema();
            if(output instanceof StatefulOutput)
            {
                // update using the derived schema.
                ((StatefulOutput)output).updateLast(pipeSchema, this);
            }            
            
            Pipe.transferDirect(pipeSchema, pipe, input, output);
        }
        
    };
    
    protected abstract void doMergeFrom(Input input, Schema<Object> derivedSchema, 
            Object owner) throws IOException;

}
