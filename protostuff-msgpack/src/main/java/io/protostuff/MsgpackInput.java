//========================================================================
//Copyright (C) 2016 Alex Shvid
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

package io.protostuff;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.msgpack.core.MessageUnpacker;

/**
 * Input is using to read source from messagepack unpacker
 * 
 * @author Alex Shvid
 *
 */

public class MsgpackInput implements Input
{

    private final MessageUnpacker unpacker;

    public MsgpackInput(MessageUnpacker unpacker)
    {
        this.unpacker = unpacker;
    }

    @Override
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        throw new MsgpackInputException("Unsupported");
    }

    @Override
    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        return unpacker.unpackInt();
    }

    @Override
    public int readInt32() throws IOException
    {
        return unpacker.unpackInt();
    }

    @Override
    public int readUInt32() throws IOException
    {
        return unpacker.unpackInt();
    }

    @Override
    public int readSInt32() throws IOException
    {
        return unpacker.unpackInt();
    }

    @Override
    public int readFixed32() throws IOException
    {
        return unpacker.unpackInt();
    }

    @Override
    public int readSFixed32() throws IOException
    {
        return unpacker.unpackInt();
    }

    @Override
    public long readInt64() throws IOException
    {
        return unpacker.unpackLong();
    }

    @Override
    public long readUInt64() throws IOException
    {
        return unpacker.unpackLong();
    }

    @Override
    public long readSInt64() throws IOException
    {
        return unpacker.unpackLong();
    }

    @Override
    public long readFixed64() throws IOException
    {
        return unpacker.unpackLong();
    }

    @Override
    public long readSFixed64() throws IOException
    {
        return unpacker.unpackLong();
    }

    @Override
    public float readFloat() throws IOException
    {
        return unpacker.unpackFloat();
    }

    @Override
    public double readDouble() throws IOException
    {
        return unpacker.unpackDouble();
    }

    @Override
    public boolean readBool() throws IOException
    {
        return unpacker.unpackBoolean();
    }

    @Override
    public int readEnum() throws IOException
    {
        return unpacker.unpackInt();
    }

    @Override
    public String readString() throws IOException
    {
        return unpacker.unpackString();
    }

    @Override
    public ByteString readBytes() throws IOException
    {
        int length = unpacker.unpackInt();
        return ByteString.wrap(unpacker.readPayload(length));
    }

    @Override
    public byte[] readByteArray() throws IOException
    {
        int length = unpacker.unpackInt();
        return unpacker.readPayload(length);
    }

    @Override
    public ByteBuffer readByteBuffer() throws IOException
    {
        int length = unpacker.unpackInt();
        ByteBuffer buffer = ByteBuffer.allocate(length);
        unpacker.readPayload(buffer);
        return buffer;
    }

    @Override
    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {

        if (value == null)
        {
            value = schema.newMessage();
        }

        schema.mergeFrom(this, value);

        return value;
    }

    @Override
    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber, boolean repeated)
            throws IOException
    {

        if (utf8String)
        {
            output.writeString(fieldNumber, readString(), repeated);
        }
        else
        {
            output.writeByteArray(fieldNumber, readByteArray(), repeated);
        }

    }

}
