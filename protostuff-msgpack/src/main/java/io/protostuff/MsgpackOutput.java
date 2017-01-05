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

import org.msgpack.core.MessagePacker;

/**
 * Output is using to write data in message pack format.
 * 
 * @author Alex Shvid
 */
public class MsgpackOutput implements Output
{

    private final MessagePacker packer;

    public MsgpackOutput(MessagePacker packer)
    {
        this.packer = packer;
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value);
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value);
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value);
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packLong(value);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packLong(value);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packLong(value);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packLong(value);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packLong(value);
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packFloat(value);
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packDouble(value);
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packBoolean(value);
    }

    @Override
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value);
    }

    @Override
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packString(value);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value.size());
        packer.addPayload(value.getBytes());
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        packer.packInt(value.length);
        packer.writePayload(value);
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, int offset, int length,
            boolean repeated) throws IOException
    {

        packer.packInt(fieldNumber);

        if (utf8String)
        {
            String str = new String(value, offset, length, "UTF-8");
            packer.packString(str);
        }
        else
        {
            packer.packInt(length);
            packer.writePayload(value, offset, length);
        }

    }

    @Override
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, boolean repeated) throws IOException
    {
        packer.packInt(fieldNumber);
        schema.writeTo(this, value);
        writeEndObject();
    }

    @Override
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {

        writeByteRange(false, fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated);

    }

    public void writeEndObject() throws IOException
    {
        packer.packInt(MsgpackIOUtil.END_OF_OBJECT);
    }

}
