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
 * Custom eXtream IO output implementation that is 3x faster than MsgpackOutput
 * 
 * @author Alex Shvid
 *
 */

public class MsgpackXOutput extends WriteSession implements Output, StatefulOutput
{

    public static final int MAX_MAPHEADER_SIZE = 5;
    public static final int MAX_ARRAYHEADER_SIZE = 5;

    private Schema<?> schema;
    private final boolean numeric;

    private LinkedBuffer arrayHeader;
    private boolean lastRepeated;
    private int lastNumber;
    private int arraySize;
    private int mapSize;

    private MsgpackWriteSink packSink;

    public MsgpackXOutput(LinkedBuffer head, boolean numeric, Schema<?> schema)
    {
        super(head);
        this.numeric = numeric;
        this.schema = schema;
        this.packSink = new MsgpackWriteSink(this.sink, true);
    }

    public LinkedBuffer writeStartObject()
    {

        LinkedBuffer objectHeader;
        
        if (tail.buffer.length - tail.offset < MAX_MAPHEADER_SIZE)
        {
            tail = new LinkedBuffer(this.nextBufferSize, tail);
        }

        objectHeader = tail;
        tail = LinkedBuffer.wrap(tail.buffer, tail.offset + MAX_MAPHEADER_SIZE, 0);
        objectHeader.next = tail;

        return objectHeader;
    }

    public void writeEndObject(LinkedBuffer lb) throws IOException
    {
        this.packSink.packMapHeader(mapSize, this, lb);
    }

    private void writeStartArray()
    {

        if (tail.buffer.length - tail.offset < MAX_ARRAYHEADER_SIZE)
        {
            tail = new LinkedBuffer(this.nextBufferSize, tail);
        }

        arrayHeader = tail;
        tail = LinkedBuffer.wrap(tail.buffer, tail.offset + MAX_ARRAYHEADER_SIZE, 0);
        arrayHeader.next = tail;

        arraySize = 1;

    }

    public void writeEndArray() throws IOException
    {
        this.packSink.packArrayHeader(arraySize, this, arrayHeader);
    }

    /**
     * Resets this output for re-use.
     */
    @Override
    public void reset()
    {
        arrayHeader = null;
        lastRepeated = false;
        lastNumber = 0;
        arraySize = 0;
        mapSize = 0;
    }

    @Override
    public MsgpackXOutput clear()
    {
        super.clear();
        reset();
        return this;
    }

    /**
     * Before serializing a message/object tied to a schema, this should be called.
     */
    public MsgpackXOutput use(Schema<?> schema)
    {
        this.schema = schema;
        return this;
    }

    /**
     * Returns whether the incoming messages' field names are numeric.
     */
    public boolean isNumeric()
    {
        return numeric;
    }

    /**
     * Gets the last field number written.
     */
    public int getLastNumber()
    {
        return lastNumber;
    }

    /**
     * Returns true if the last written field was a repeated field.
     */
    public boolean isLastRepeated()
    {
        return lastRepeated;
    }

    @Override
    public void updateLast(Schema<?> schema, Schema<?> lastSchema)
    {
        if (lastSchema != null && lastSchema == this.schema)
        {
            this.schema = schema;
        }
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt(fieldNumber, value, repeated);
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt(fieldNumber, value, repeated);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt(fieldNumber, value, repeated);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt(fieldNumber, value, repeated);
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt(fieldNumber, value, repeated);
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeLong(fieldNumber, value, repeated);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeLong(fieldNumber, value, repeated);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeLong(fieldNumber, value, repeated);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeLong(fieldNumber, value, repeated);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeLong(fieldNumber, value, repeated);
    }

    @Override
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt(fieldNumber, value, repeated);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeBytes(fieldNumber, value.getBytes(), 0, value.size(), repeated, false);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        writeBytes(fieldNumber, value, 0, value.length, repeated, false);
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, int offset, int length,
            boolean repeated) throws IOException
    {
        writeBytes(fieldNumber, value, offset, length, repeated, utf8String);
    }

    @Override
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, boolean repeated) throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            writeSingleObject(fieldNumber, value, schema);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        writeSingleObject(fieldNumber, value, schema);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    private <T> void writeSingleObject(int fieldNumber, T value, Schema<T> schema) throws IOException
    {
        Schema<?> lastSchema = this.schema;
        int lastMapSize = this.mapSize;
        int lastArraySize = this.arraySize;
        boolean saveLastRepeated = this.lastRepeated;
        LinkedBuffer lastArrayHeader = this.arrayHeader;

        // reset
        reset();
        use(schema);

        LinkedBuffer objectHeader = writeStartObject();

        // recursive write
        schema.writeTo(this, value);

        if (isLastRepeated()) {
            writeEndArray();
        }
        
        writeEndObject(objectHeader);

        this.schema = lastSchema;
        this.mapSize = lastMapSize;
        this.arraySize = lastArraySize;
        this.arrayHeader = lastArrayHeader;
        this.lastRepeated = saveLastRepeated;
        this.lastNumber = fieldNumber;
    }

    @Override
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {
        writeBytes(fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated, false);
    }

    private void writeInt(int fieldNumber, int value, boolean repeated) throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packInt(value, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packInt(value, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    private void writeLong(int fieldNumber, long value, boolean repeated) throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packLong(value, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packLong(value, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packFloat(value, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packFloat(value, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packDouble(value, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packDouble(value, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packBoolean(value, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packBoolean(value, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    @Override
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packString(value, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packString(value, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    @Override
    public void writeString(int fieldNumber, StringBuilder value, boolean repeated) throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packString(value, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packString(value, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    private void writeBytes(int fieldNumber, byte[] src, int offset, int length, boolean repeated, boolean utf8)
            throws IOException
    {

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            tail = packSink.packBinaryHeader(length, this, tail);
            tail = packSink.packBytes(src, offset, length, this, tail);
            arraySize++;
            return;
        }

        if (lastRepeated)
        {
            writeEndArray();
        }

        writeFieldNumber(fieldNumber);

        if (repeated)
        {
            writeStartArray();
        }

        tail = packSink.packBinaryHeader(length, this, tail);
        tail = packSink.packBytes(src, offset, length, this, tail);

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    private void writeFieldNumber(int fieldNumber) throws IOException
    {
        if (numeric)
        {
            tail = packSink.packInt(fieldNumber, this, tail);
        }
        else
        {
            String fieldName = schema.getFieldName(fieldNumber);
            tail = packSink.packString(fieldName, this, tail);
        }

        mapSize++;
    }

}
