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
 * Single object parser for msgpack.
 * 
 * @author Alex Shvid
 */

public final class MsgpackParser
{

    private final MessageUnpacker unpacker;
    private final boolean numeric;

    /**
     * How many entires left to parse, -1 for uninitialized
     */

    private int leftEntries = -1;

    /**
     * How many items left to parse in array
     */

    private int leftItems;
    private int lastNumber;

    public MsgpackParser(MessageUnpacker unpacker, boolean numeric) throws IOException
    {
        this.unpacker = unpacker;
        this.numeric = numeric;
        reset();
    }
    
    public MsgpackParser(MsgpackParser parent) throws IOException
    {
        this.unpacker = parent.unpacker;
        this.numeric = parent.numeric;
        reset();
    }
    
    public boolean hasNext() throws IOException {
        return unpacker.hasNext();
    }
    
    public void reset() throws IOException
    {
        this.leftEntries = -1;
        this.leftItems = 0;
        this.lastNumber = 0;
    }

    public boolean isNumeric()
    {
        return numeric;
    }

    public int getLeftEntries()
    {
        return leftEntries;
    }

    public int getLeftItems()
    {
        return leftItems;
    }

    public int getLastNumber()
    {
        return lastNumber;
    }

    public <T> int parseFieldNumber(Schema<T> schema) throws IOException
    {
        
        if (leftItems != 0)
        {
            leftItems--;
            return lastNumber;
        }

        if (leftEntries == -1) {
            
            if (!unpacker.hasNext()) {
                return 0;
            }
            
            leftEntries = unpacker.unpackMapHeader();
        }
        
        if (leftEntries == 0)
        {
            return 0;
        }

        leftEntries--;

        int fieldNumber;

        if (numeric)
        {
            fieldNumber = unpacker.unpackInt();
        }
        else
        {
            String name = unpacker.unpackString();
            fieldNumber = schema.getFieldNumber(name);
        }

        if (unpacker.hasNext() && unpacker.getNextFormat().getValueType().isArrayType())
        {

            leftItems = unpacker.unpackArrayHeader();
            lastNumber = fieldNumber;

            if (leftItems == 0)
            {
                return parseFieldNumber(schema);
            }
            else
            {
                leftItems--;
            }

        }

        return fieldNumber;

    }

    public void skipValue() throws IOException
    {
        unpacker.skipValue();
    }

    public int parseInt() throws IOException
    {
        return unpacker.unpackInt();
    }

    public long parseLong() throws IOException
    {
        return unpacker.unpackLong();
    }

    public float parseFloat() throws IOException
    {
        return unpacker.unpackFloat();
    }

    public double parseDouble() throws IOException
    {
        return unpacker.unpackDouble();
    }

    public boolean parseBoolean() throws IOException
    {
        return unpacker.unpackBoolean();
    }

    public String parseString() throws IOException
    {
        return unpacker.unpackString();
    }

    public byte[] parsePayload() throws IOException
    {
        int length = unpacker.unpackBinaryHeader();
        return unpacker.readPayload(length);
    }

    public ByteBuffer readPayload() throws IOException
    {
        int length = unpacker.unpackBinaryHeader();
        ByteBuffer buffer = ByteBuffer.allocate(length);
        unpacker.readPayload(buffer);
        return buffer;
    }
}
