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

import static io.protostuff.runtime.RuntimeFieldFactory.ID_CLASS;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_CLASS_ARRAY;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_CLASS_ARRAY_MAPPED;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_CLASS_MAPPED;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_CLASS;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_CLASS_ARRAY;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_CLASS_ARRAY_MAPPED;
import static io.protostuff.runtime.RuntimeFieldFactory.STR_CLASS_MAPPED;

import java.io.IOException;

import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.Schema;

/**
 * Used when a field is declared as {@code Class<?>} (with or with-out generics).
 * 
 * @author David Yu
 * @created Apr 25, 2012
 */
public abstract class ClassSchema extends PolymorphicSchema
{

    static final int ID_ARRAY_DIMENSION = 2;
    static final String STR_ARRAY_DIMENSION = "b";

    static String name(int number)
    {
        switch (number)
        {
            case ID_ARRAY_DIMENSION:
                return STR_ARRAY_DIMENSION;
            case ID_CLASS:
                return STR_CLASS;
            case ID_CLASS_MAPPED:
                return STR_CLASS_MAPPED;
            case ID_CLASS_ARRAY:
                return STR_CLASS_ARRAY;
            case ID_CLASS_ARRAY_MAPPED:
                return STR_CLASS_ARRAY_MAPPED;
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
            case 'r':
                return ID_CLASS;
            case 's':
                return ID_CLASS_MAPPED;
            case 't':
                return ID_CLASS_ARRAY;
            case 'u':
                return ID_CLASS_ARRAY_MAPPED;
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

    public ClassSchema(IdStrategy strategy)
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
        return Class.class.getName();
    }

    @Override
    public String messageName()
    {
        return Class.class.getSimpleName();
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
        final Class<?> c = ((Class<?>) value);
        if (c.isArray())
        {
            int dimensions = 1;
            Class<?> componentType = c.getComponentType();
            while (componentType.isArray())
            {
                dimensions++;
                componentType = componentType.getComponentType();
            }

            strategy.writeClassIdTo(output, componentType, true);
            // write the dimensions of the array
            output.writeUInt32(ID_ARRAY_DIMENSION, dimensions, false);
            return;
        }

        strategy.writeClassIdTo(output, c, false);
    }

    static Object readObjectFrom(Input input, Schema<?> schema, Object owner,
            IdStrategy strategy) throws IOException
    {
        final int number = input.readFieldNumber(schema);
        final Object value;
        switch (number)
        {
            case ID_CLASS:
                value = strategy.resolveClassFrom(input, false, false);
                break;

            case ID_CLASS_MAPPED:
                value = strategy.resolveClassFrom(input, true, false);
                break;

            case ID_CLASS_ARRAY:
                value = ObjectSchema.getArrayClass(input, schema,
                        strategy.resolveClassFrom(input, false, true));
                break;

            case ID_CLASS_ARRAY_MAPPED:
                value = ObjectSchema.getArrayClass(input, schema,
                        strategy.resolveClassFrom(input, true, true));
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
        switch (number)
        {
            case ID_CLASS:
                ObjectSchema.transferClass(pipe, input, output, number, pipeSchema,
                        false, false, strategy);
                break;

            case ID_CLASS_MAPPED:
                ObjectSchema.transferClass(pipe, input, output, number, pipeSchema,
                        true, false, strategy);
                break;

            case ID_CLASS_ARRAY:
                ObjectSchema.transferClass(pipe, input, output, number, pipeSchema,
                        false, true, strategy);
                break;

            case ID_CLASS_ARRAY_MAPPED:
                ObjectSchema.transferClass(pipe, input, output, number, pipeSchema,
                        true, true, strategy);
                break;

            default:
                throw new ProtostuffException("Corrupt input.");
        }

        if (0 != input.readFieldNumber(pipeSchema.wrappedSchema))
            throw new ProtostuffException("Corrupt input.");
    }

}
