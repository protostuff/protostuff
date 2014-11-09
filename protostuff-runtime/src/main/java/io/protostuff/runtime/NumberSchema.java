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

import static io.protostuff.runtime.RuntimeFieldFactory.BIGDECIMAL;
import static io.protostuff.runtime.RuntimeFieldFactory.BIGINTEGER;
import static io.protostuff.runtime.RuntimeFieldFactory.BYTE;
import static io.protostuff.runtime.RuntimeFieldFactory.DOUBLE;
import static io.protostuff.runtime.RuntimeFieldFactory.FLOAT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGDECIMAL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGINTEGER;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_POJO;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import static io.protostuff.runtime.RuntimeFieldFactory.INT32;
import static io.protostuff.runtime.RuntimeFieldFactory.INT64;
import static io.protostuff.runtime.RuntimeFieldFactory.SHORT;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_BIGDECIMAL;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_BIGINTEGER;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_BYTE;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_DOUBLE;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_FLOAT;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_INT32;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_INT64;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_POJO;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_SHORT;

import java.io.IOException;

import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;
import io.protostuff.StatefulOutput;

/**
 * Used when the type is {@link java.lang.Number}.
 * 
 * @author David Yu
 * @created Apr 30, 2012
 */
public abstract class NumberSchema extends PolymorphicSchema
{

    static String name(int number)
    {
        switch (number)
        {
            case ID_BYTE:
                return STR_BYTE;
            case ID_SHORT:
                return STR_SHORT;
            case ID_INT32:
                return STR_INT32;
            case ID_INT64:
                return STR_INT64;
            case ID_FLOAT:
                return STR_FLOAT;
            case ID_DOUBLE:
                return STR_DOUBLE;
            case ID_BIGDECIMAL:
                return STR_BIGDECIMAL;
            case ID_BIGINTEGER:
                return STR_BIGINTEGER;
                // AtomicInteger and AtomicLong
            case ID_POJO:
                return STR_POJO;
            default:
                return null;
        }
    }

    static int number(String name)
    {
        if (name.length() != 1)
            return 0;

        switch (name.charAt(0))
        {
            case '_':
                return 127;
            case 'b':
                return 2;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
            case 'l':
                return 12;
            case 'm':
                return 13;
            default:
                return 0;
        }
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

    public NumberSchema(IdStrategy strategy)
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
        return Number.class.getName();
    }

    @Override
    public String messageName()
    {
        return Number.class.getSimpleName();
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
        final Class<Object> clazz = (Class<Object>) value.getClass();

        final RuntimeFieldFactory<Object> inline = RuntimeFieldFactory
                .getInline(clazz);
        if (inline != null)
        {
            // scalar value
            inline.writeTo(output, inline.id, value, false);
            return;
        }

        // AtomicInteger/AtomicLong
        final Schema<Object> schema = strategy.writePojoIdTo(output, ID_POJO,
                clazz).getSchema();

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

        if (number == ID_POJO)
        {
            // AtomicInteger/AtomicLong
            final Schema<Object> derivedSchema = strategy.resolvePojoFrom(
                    input, number).getSchema();

            final Object pojo = derivedSchema.newMessage();

            if (input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput) input).updateLast(pojo, owner);
            }

            derivedSchema.mergeFrom(input, pojo);
            return pojo;
        }

        final Object value;
        switch (number)
        {
            case ID_BYTE:
                value = BYTE.readFrom(input);
                break;
            case ID_SHORT:
                value = SHORT.readFrom(input);
                break;
            case ID_INT32:
                value = INT32.readFrom(input);
                break;
            case ID_INT64:
                value = INT64.readFrom(input);
                break;
            case ID_FLOAT:
                value = FLOAT.readFrom(input);
                break;
            case ID_DOUBLE:
                value = DOUBLE.readFrom(input);
                break;
            case ID_BIGDECIMAL:
                value = BIGDECIMAL.readFrom(input);
                break;
            case ID_BIGINTEGER:
                value = BIGINTEGER.readFrom(input);
                break;
            default:
                throw new ProtostuffException("Corrupt input.");
        }

        if (input instanceof GraphInput)
        {
            // update the actual reference.
            ((GraphInput) input).updateLast(value, owner);
        }

        if (0 != input.readFieldNumber(schema))
            throw new ProtostuffException("Corrupt input.");

        return value;
    }

    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        if (number == ID_POJO)
        {
            // AtomicInteger/AtomicLong
            final Pipe.Schema<Object> derivedPipeSchema = strategy
                    .transferPojoId(input, output, number).getPipeSchema();

            if (output instanceof StatefulOutput)
            {
                // update using the derived schema.
                ((StatefulOutput) output).updateLast(derivedPipeSchema,
                        pipeSchema);
            }

            Pipe.transferDirect(derivedPipeSchema, pipe, input, output);
            return;
        }

        switch (number)
        {
            case ID_BYTE:
                BYTE.transfer(pipe, input, output, number, false);
                break;
            case ID_SHORT:
                SHORT.transfer(pipe, input, output, number, false);
                break;
            case ID_INT32:
                INT32.transfer(pipe, input, output, number, false);
                break;
            case ID_INT64:
                INT64.transfer(pipe, input, output, number, false);
                break;
            case ID_FLOAT:
                FLOAT.transfer(pipe, input, output, number, false);
                break;
            case ID_DOUBLE:
                DOUBLE.transfer(pipe, input, output, number, false);
                break;
            case ID_BIGDECIMAL:
                BIGDECIMAL.transfer(pipe, input, output, number, false);
                break;
            case ID_BIGINTEGER:
                BIGINTEGER.transfer(pipe, input, output, number, false);
                break;
            default:
                throw new ProtostuffException("Corrupt input.");
        }
    }

}
