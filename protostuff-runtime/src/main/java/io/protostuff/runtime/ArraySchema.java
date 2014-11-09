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

import static io.protostuff.runtime.RuntimeFieldFactory.ID_ARRAY;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_ARRAY_MAPPED;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_ARRAY;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_ARRAY_MAPPED;

import java.io.IOException;
import java.lang.reflect.Array;

import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;
import io.protostuff.StatefulOutput;
import io.protostuff.runtime.ObjectSchema.ArrayWrapper;

/**
 * Used when a field is an array (Object[] or any polymorphic component type).
 * 
 * @author David Yu
 * @created Apr 25, 2012
 */
public abstract class ArraySchema extends PolymorphicSchema
{

    static final int ID_ARRAY_LEN = 3;
    static final int ID_ARRAY_DIMENSION = 2;

    static final String STR_ARRAY_LEN = "c";
    static final String STR_ARRAY_DIMENSION = "b";

    static String name(int number)
    {
        switch (number)
        {
            case ID_ARRAY_DIMENSION:
                return STR_ARRAY_DIMENSION;
            case ID_ARRAY_LEN:
                return STR_ARRAY_LEN;
            case ID_ARRAY:
                return STR_ARRAY;
            case ID_ARRAY_MAPPED:
                return STR_ARRAY_MAPPED;
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
            case 'b':
                return ID_ARRAY_DIMENSION;
            case 'c':
                return ID_ARRAY_LEN;
            case 'o':
                return ID_ARRAY;
            case 'q':
                return ID_ARRAY_MAPPED;
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

    public ArraySchema(IdStrategy strategy)
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
        return Array.class.getName();
    }

    @Override
    public String messageName()
    {
        return Array.class.getSimpleName();
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

    static void writeObjectTo(Output output, Object value,
            Schema<?> currentSchema, IdStrategy strategy) throws IOException
    {
        final Class<?> clazz = value.getClass();
        int dimensions = 1;
        Class<?> componentType = clazz.getComponentType();
        while (componentType.isArray())
        {
            dimensions++;
            componentType = componentType.getComponentType();
        }

        strategy.writeArrayIdTo(output, componentType);
        // write the length of the array
        output.writeUInt32(ID_ARRAY_LEN, Array.getLength(value), false);
        // write the dimensions of the array
        output.writeUInt32(ID_ARRAY_DIMENSION, dimensions, false);

        if (output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput) output).updateLast(strategy.ARRAY_SCHEMA,
                    currentSchema);
        }

        strategy.ARRAY_SCHEMA.writeTo(output, value);
    }

    static Object readObjectFrom(Input input, Schema<?> schema, Object owner,
            IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(schema);
        final boolean mapped;
        switch (number)
        {
            case ID_ARRAY:
                mapped = false;
                break;

            case ID_ARRAY_MAPPED:
                mapped = true;
                break;

            default:
                throw new ProtostuffException("Corrupt input.");
        }

        final ArrayWrapper mArrayWrapper = ObjectSchema.newArrayWrapper(input,
                schema, mapped, strategy);

        if (input instanceof GraphInput)
        {
            // update the actual reference.
            ((GraphInput) input).updateLast(mArrayWrapper.array, owner);
        }

        strategy.COLLECTION_SCHEMA.mergeFrom(input, mArrayWrapper);

        return mArrayWrapper.array;
    }

    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe,
            Input input, Output output, IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        switch (number)
        {
            case ID_ARRAY:
                ObjectSchema.transferArray(pipe, input, output, number, pipeSchema,
                        false, strategy);
                return;

            case ID_ARRAY_MAPPED:
                ObjectSchema.transferArray(pipe, input, output, number, pipeSchema,
                        true, strategy);
                return;

            default:
                throw new ProtostuffException("Corrupt input.");
        }
    }

}
