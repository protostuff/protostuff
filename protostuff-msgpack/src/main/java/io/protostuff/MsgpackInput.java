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

/**
 * Input is using to read source from messagepack unpacker
 * 
 * @author Alex Shvid
 *
 */

public class MsgpackInput implements Input
{

    private MsgpackParser parser;

    public MsgpackInput(MsgpackParser parser)
    {
        this.parser = parser;
    }

    /**
     * Use another parser in msgpack input
     */

    public MsgpackParser use(MsgpackParser newParser)
    {
        MsgpackParser old = this.parser;
        this.parser = newParser;
        return old;
    }

    @Override
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        parser.skipValue();
    }

    @Override
    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        return parser.parseFieldNumber(schema);
    }

    @Override
    public int readInt32() throws IOException
    {
        return parser.parseInt();
    }

    @Override
    public int readUInt32() throws IOException
    {
        return parser.parseInt();
    }

    @Override
    public int readSInt32() throws IOException
    {
        return parser.parseInt();
    }

    @Override
    public int readFixed32() throws IOException
    {
        return parser.parseInt();
    }

    @Override
    public int readSFixed32() throws IOException
    {
        return parser.parseInt();
    }

    @Override
    public long readInt64() throws IOException
    {
        return parser.parseLong();
    }

    @Override
    public long readUInt64() throws IOException
    {
        return parser.parseLong();
    }

    @Override
    public long readSInt64() throws IOException
    {
        return parser.parseLong();
    }

    @Override
    public long readFixed64() throws IOException
    {
        return parser.parseLong();
    }

    @Override
    public long readSFixed64() throws IOException
    {
        return parser.parseLong();
    }

    @Override
    public float readFloat() throws IOException
    {
        return parser.parseFloat();
    }

    @Override
    public double readDouble() throws IOException
    {
        return parser.parseDouble();
    }

    @Override
    public boolean readBool() throws IOException
    {
        return parser.parseBoolean();
    }

    @Override
    public int readEnum() throws IOException
    {
        return parser.parseInt();
    }

    @Override
    public String readString() throws IOException
    {
        return parser.parseString();
    }

    @Override
    public ByteString readBytes() throws IOException
    {
        return ByteString.wrap(parser.parsePayload());
    }

    @Override
    public byte[] readByteArray() throws IOException
    {
        return parser.parsePayload();
    }

    @Override
    public ByteBuffer readByteBuffer() throws IOException
    {
        return parser.readPayload();
    }

    @Override
    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {

        if (value == null)
        {
            value = schema.newMessage();
        }

        MsgpackParser innerParser = new MsgpackParser(this.parser);
        MsgpackParser thisParser = use(innerParser);

        schema.mergeFrom(this, value);

        use(thisParser);

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
