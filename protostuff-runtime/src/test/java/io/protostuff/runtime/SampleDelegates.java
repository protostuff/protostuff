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

import java.io.IOException;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.ProtostuffException;
import io.protostuff.WireFormat.FieldType;

/**
 * Sample delegates for testing.
 * 
 * @author David Yu
 * @created May 2, 2012
 */
public final class SampleDelegates
{

    private SampleDelegates()
    {
    }

    public static final class ShortArrayDelegate implements Delegate<short[]>
    {
        int reads, writes, transfers;

        @Override
        public Class<?> typeClass()
        {
            return short[].class;
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.BYTES;
        }

        @Override
        public void writeTo(Output output, int number, short[] value,
                boolean repeated) throws IOException
        {
            writes++;

            byte[] buffer = new byte[value.length * 2];
            for (int i = 0, offset = 0; i < value.length; i++)
            {
                short s = value[i];
                buffer[offset++] = (byte) ((s >>> 8) & 0xFF);
                buffer[offset++] = (byte) ((s >>> 0) & 0xFF);
            }

            output.writeByteArray(number, buffer, repeated);
        }

        @Override
        public short[] readFrom(Input input) throws IOException
        {
            reads++;

            byte[] buffer = input.readByteArray();
            short[] s = new short[buffer.length / 2];
            for (int i = 0, offset = 0; i < s.length; i++)
            {
                s[i] = (short) ((buffer[offset++] & 0xFF) << 8 | (buffer[offset++] & 0xFF));
            }

            return s;
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            transfers++;

            input.transferByteRangeTo(output, false, number, repeated);
        }
    }

    ;

    public static final class Singleton
    {
        public static final Singleton INSTANCE = new Singleton();

        private Singleton()
        {
        }

        @Override
        public boolean equals(Object obj)
        {
            return this == obj && obj == INSTANCE;
        }

        public int hashCode()
        {
            return System.identityHashCode(this);
        }
    }

    public static final Delegate<Singleton> SINGLETON_DELEGATE = new Delegate<Singleton>()
    {
        @Override
        public Class<?> typeClass()
        {
            return Singleton.class;
        }

        @Override
        public FieldType getFieldType()
        {
            return FieldType.UINT32;
        }

        @Override
        public void writeTo(Output output, int number, Singleton value,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, 0, repeated);
        }

        @Override
        public Singleton readFrom(Input input) throws IOException
        {
            if (0 != input.readUInt32())
                throw new ProtostuffException("Corrupt input.");

            return Singleton.INSTANCE;
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            output.writeUInt32(number, input.readUInt32(), repeated);
        }
    };
}
