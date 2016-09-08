//================================================================================
//Copyright (c) 2012, David Yu
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

package io.protostuff.runtime;

import static io.protostuff.runtime.RuntimeFieldFactory.ID_THROWABLE;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_THROWABLE;

import java.io.IOException;

import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;
import io.protostuff.StatefulOutput;

/**
 * Used when the type is assignable from {@link java.lang.Throwable}.
 * 
 * @author David Yu
 * @created May 2, 2012
 */
public abstract class PolymorphicThrowableSchema extends PolymorphicSchema
{

    static final java.lang.reflect.Field __cause;

    static
    {
        java.lang.reflect.Field cause;
        try
        {
            cause = Throwable.class.getDeclaredField("cause");
            cause.setAccessible(true);
        }
        catch (Exception e)
        {
            cause = null;
        }
        __cause = cause;
    }

    static String name(int number)
    {
        return number == ID_THROWABLE ? STR_THROWABLE : null;
    }

    static int number(String name)
    {
        return name.length() == 1 && name.charAt(0) == 'Z' ? ID_THROWABLE : 0;
    }

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

    public PolymorphicThrowableSchema(IdStrategy strategy)
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
        return name(number);
    }

    @Override
    public int getFieldNumber(String name)
    {
        return number(name);
    }

    @Override
    public String messageFullName()
    {
        return Throwable.class.getName();
    }

    @Override
    public String messageName()
    {
        return Throwable.class.getSimpleName();
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
                ID_THROWABLE, (Class<Object>) value.getClass()).getSchema();

        if (output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput) output).updateLast(schema, currentSchema);
        }

        if (tryWriteWithoutCause(output, value, schema))
            return;

        schema.writeTo(output, value);
    }

    static boolean tryWriteWithoutCause(Output output, Object value,
            Schema<Object> schema) throws IOException
    {
        if (schema instanceof RuntimeSchema && __cause != null)
        {
            // ignore the field "cause" if its references itself (cyclic)
            final RuntimeSchema<Object> ms = (RuntimeSchema<Object>) schema;
            if (ms.getFieldCount() > 1 && ms.getFields().get(1).name.equals("cause"))
            {
                final Object cause;
                try
                {
                    cause = __cause.get(value);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (cause == value)
                {
                    // its cyclic, skip the second field "cause"
                    ms.getFields().get(0).writeTo(output, value);

                    for (int i = 2, len = ms.getFieldCount(); i < len; i++)
                        ms.getFields().get(i).writeTo(output, value);

                    return true;
                }
            }
        }

        return false;
    }

    static Object readObjectFrom(Input input, Schema<?> schema, Object owner,
            IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(schema);
        if (number != ID_THROWABLE)
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

        if (__cause != null)
        {
            final Object cause;
            try
            {
                cause = __cause.get(pojo);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            if (cause == null)
            {
                // was not written because it was cyclic
                // so we set it here manually for correctness
                try
                {
                    __cause.set(pojo, cause);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        derivedSchema.mergeFrom(input, pojo);
        return pojo;
    }

    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        if (number != ID_THROWABLE)
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
