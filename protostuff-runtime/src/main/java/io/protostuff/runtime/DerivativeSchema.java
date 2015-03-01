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

package io.protostuff.runtime;

import static io.protostuff.runtime.RuntimeFieldFactory.ID_POJO;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_POJO;

import java.io.IOException;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;
import io.protostuff.StatefulOutput;

/**
 * This schema delegates to another schema derived from the input.
 * 
 * @author David Yu
 * @created Jan 21, 2011
 */
public abstract class DerivativeSchema implements Schema<Object>
{

    public final IdStrategy strategy;

    public DerivativeSchema(IdStrategy strategy)
    {
        this.strategy = strategy;
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

    /**
     * Delegates to the schema derived from the input. The {@code owner} owns the message (polymorphic) that is tied to
     * this schema.
     */
    @Override
    public void mergeFrom(Input input, final Object owner) throws IOException
    {
        final int first = input.readFieldNumber(this);
        if (first != ID_POJO)
            throw new ProtostuffException("order not preserved.");

        doMergeFrom(input,
                strategy.resolvePojoFrom(input, ID_POJO).getSchema(), owner);
    }

    /**
     * Delegates to the schema derived from the {@code value}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(final Output output, final Object value)
            throws IOException
    {
        final Schema<Object> schema = strategy.writePojoIdTo(output, ID_POJO,
                (Class<Object>) value.getClass()).getSchema();

        if (output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput) output).updateLast(schema, this);
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
        @Override
        public void transfer(Pipe pipe, Input input, Output output)
                throws IOException
        {
            final int first = input.readFieldNumber(DerivativeSchema.this);
            if (first != ID_POJO)
                throw new ProtostuffException("order not preserved.");

            final Pipe.Schema<Object> pipeSchema = strategy.transferPojoId(
                    input, output, ID_POJO).getPipeSchema();

            if (output instanceof StatefulOutput)
            {
                // update using the derived schema.
                ((StatefulOutput) output).updateLast(pipeSchema, this);
            }

            Pipe.transferDirect(pipeSchema, pipe, input, output);
        }

    };

    protected abstract void doMergeFrom(Input input,
            Schema<Object> derivedSchema, Object owner) throws IOException;

}
