//========================================================================
//Copyright 2016 David Yu
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

package io.protostuff.runtime;

import static io.protostuff.runtime.RuntimeFieldFactory.ID_POJO;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_POJO;
import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;
import io.protostuff.StatefulOutput;

import java.io.IOException;

/**
 * Will be used if either {@link RuntimeEnv#POJO_SCHEMA_ON_COLLECTION_FIELDS} or 
 * {@link RuntimeEnv#POJO_SCHEMA_ON_MAP_FIELDS} is set.
 * 
 * Applies to a field which is an interface and the type is assignable from List or Map.
 * 
 * @author David Yu
 * @created May 19, 2016
 */
public abstract class PolymorphicPojoSchema extends PolymorphicSchema
{
    
    protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
            this)
    {
        @Override
        protected void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            transferObject(this, pipe, input, output, strategy);
        }
    };
    
    public PolymorphicPojoSchema(IdStrategy strategy)
    {
        super(strategy);
    }
    
    @Override
    public Pipe.Schema<Object> getPipeSchema()
    {
        return pipeSchema;
    }
    
    @Override
    public String getFieldName(int number)
    {
        return number == ID_POJO ? STR_POJO : null;
    }
    
    @Override
    public int getFieldNumber(String name)
    {
        return name.length() == 1 && name.charAt(0) == '_' ? ID_POJO : 0;
    }
    
    @Override
    public boolean isInitialized(Object owner)
    {
        return true;
    }
    
    @Override
    public String messageFullName()
    {
        return Object.class.getName();
    }
    
    @Override
    public String messageName()
    {
        return Object.class.getSimpleName();
    }
    
    @Override
    public Object newMessage()
    {
        // cannot instantiate because the type is dynamic.
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<? super Object> typeClass()
    {
        return Object.class;
    }
    
    @Override
    public void mergeFrom(Input input, Object owner) throws IOException
    {
        setValue(readObjectFrom(input, this, owner, strategy), owner);
    }
    
    @Override
    public void writeTo(Output output, Object value) throws IOException
    {
        writeObjectTo(output, value, this, strategy);
    }
    
    @SuppressWarnings("unchecked")
    static void writeObjectTo(Output output, Object value,
            Schema<?> currentSchema, IdStrategy strategy) throws IOException
    {
        final Schema<Object> schema = strategy.writePojoIdTo(output,
                ID_POJO, (Class<Object>) value.getClass()).getSchema();
        
        if (output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput) output).updateLast(schema, currentSchema);
        }
        
        schema.writeTo(output, value);
    }
    
    static Object readObjectFrom(Input input, Schema<?> schema, Object owner,
            IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(schema);
        if (number != ID_POJO)
            throw new ProtostuffException("Corrupt input.");
        
        return readObjectFrom(input, schema, owner, strategy, number);
    }
    
    static Object readObjectFrom(Input input, Schema<?> schema, Object owner,
            IdStrategy strategy, int number) throws IOException
    {
        final Schema<Object> derivedSchema = strategy.resolvePojoFrom(input,
                number).getSchema();
        
        final Object pojo = derivedSchema.newMessage();
        
        if (input instanceof GraphInput)
        {
            // update the actual reference.
            ((GraphInput) input).updateLast(pojo, owner);
        }
        
        derivedSchema.mergeFrom(input, pojo);
        return pojo;
    }
    
    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        if (number != ID_POJO)
            throw new ProtostuffException("Corrupt input.");
        
        transferObject(pipeSchema, pipe, input, output, strategy, number);
    }
    
    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy, int number)
            throws IOException
    {
        final Pipe.Schema<Object> derivedPipeSchema = strategy.transferPojoId(
                input, output, number).getPipeSchema();
        
        if (output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput) output).updateLast(derivedPipeSchema, pipeSchema);
        }
        
        Pipe.transferDirect(derivedPipeSchema, pipe, input, output);
    }
}
