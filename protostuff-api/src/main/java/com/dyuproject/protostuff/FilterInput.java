//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A FilterInput contains some other {@link Input input}, which it uses as its basic 
 * source of data.
 *
 * @author David Yu
 * @created Nov 11, 2009
 */
public class FilterInput<F extends Input> implements Input
{
    
    protected final F input;
    
    public FilterInput(F input)
    {
        this.input = input;
    }    

    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        input.handleUnknownField(fieldNumber, schema);
    }
    
    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        return input.readFieldNumber(schema);
    }

    public boolean readBool() throws IOException
    {
        return input.readBool();
    }

    public byte[] readByteArray() throws IOException
    {
        return input.readByteArray();
    }

    public ByteString readBytes() throws IOException
    {
        return input.readBytes();
    }

    public double readDouble() throws IOException
    {
        return input.readDouble();
    }

    public int readEnum() throws IOException
    {
        return input.readEnum();
    }

    public int readFixed32() throws IOException
    {
        return input.readFixed32();
    }

    public long readFixed64() throws IOException
    {
        return input.readFixed64();
    }

    public float readFloat() throws IOException
    {
        return input.readFloat();
    }

    public int readInt32() throws IOException
    {
        return input.readInt32();
    }

    public long readInt64() throws IOException
    {
        return input.readInt64();
    }

    public int readSFixed32() throws IOException
    {
        return input.readSFixed32();
    }

    public long readSFixed64() throws IOException
    {
        return input.readSFixed64();
    }

    public int readSInt32() throws IOException
    {
        return input.readSInt32();
    }

    public long readSInt64() throws IOException
    {
        return input.readSInt64();
    }

    public String readString() throws IOException
    {
        return input.readString();
    }

    public int readUInt32() throws IOException
    {
        return input.readUInt32();
    }

    public long readUInt64() throws IOException
    {
        return input.readUInt64();
    }

    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {
        return input.mergeObject(value, schema);
    }

    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber,
            boolean repeated) throws IOException
    {
        input.transferByteRangeTo(output, utf8String, fieldNumber, repeated);
    }

    /** Reads a byte array/ByteBuffer value. */
    public ByteBuffer readByteBuffer() throws IOException {
        return ByteBuffer.wrap(readByteArray());
    }


}
