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

package io.protostuff;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * An output used for writing data with json format.
 * 
 * @author David Yu
 * @created Nov 20, 2009
 */
public final class JsonOutput implements Output, StatefulOutput
{

    public static final boolean FIX_UNSIGNED_INT = Boolean.getBoolean("io.protostuff.json.fix_unsigned_int");

    private final JsonGenerator generator;
    private Schema<?> schema;
    private final boolean numeric;
    private boolean lastRepeated;
    private int lastNumber;

    public JsonOutput(JsonGenerator generator)
    {
        this(generator, false);
    }

    public JsonOutput(JsonGenerator generator, boolean numeric)
    {
        this.generator = generator;
        this.numeric = numeric;
    }

    public JsonOutput(JsonGenerator generator, boolean numeric, Schema<?> schema)
    {
        this(generator, numeric);
        this.schema = schema;
    }

    /**
     * Resets this output for re-use.
     */
    public JsonOutput reset()
    {
        lastRepeated = false;
        lastNumber = 0;
        return this;
    }

    /**
     * Before serializing a message/object tied to a schema, this should be called. This also resets the internal state
     * of this output.
     */
    public JsonOutput use(Schema<?> schema)
    {
        this.schema = schema;
        return reset();
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
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeBoolean(value);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeBoolean(value);
        }
        else
            generator.writeBooleanField(name, value);

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeBinary(value);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeBinary(value);
        }
        else
        {
            generator.writeFieldName(name);
            generator.writeBinary(value);
        }

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            if (utf8String)
                generator.writeUTF8String(value, offset, length);
            else
                generator.writeBinary(value, offset, length);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            if (utf8String)
                generator.writeUTF8String(value, offset, length);
            else
                generator.writeBinary(value, offset, length);
        }
        else
        {
            generator.writeFieldName(name);
            if (utf8String)
                generator.writeUTF8String(value, offset, length);
            else
                generator.writeBinary(value, offset, length);
        }

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeNumber(value);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeNumber(value);
        }
        else
            generator.writeNumberField(name, value);

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeUInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeUInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeNumber(value);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeNumber(value);
        }
        else
            generator.writeNumberField(name, value);

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeNumber(value);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeNumber(value);
        }
        else
            generator.writeNumberField(name, value);

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeNumber(value);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeNumber(value);
        }
        else
            generator.writeNumberField(name, value);

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeString(int fieldNumber, CharSequence value, boolean repeated) throws IOException
    {
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeString(value.toString());
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeString(value.toString());
        }
        else
            generator.writeStringField(name, value.toString());

        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        String unsignedValue = UnsignedNumberUtil.unsignedIntToString(value);
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeNumber(unsignedValue);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeNumber(unsignedValue);
        }
        else
        {
            generator.writeFieldName(name);
            generator.writeNumber(unsignedValue);
        }
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        String unsignedValue = UnsignedNumberUtil.unsignedLongToString(value);
        if (lastNumber == fieldNumber)
        {
            // repeated field
            generator.writeNumber(unsignedValue);
            return;
        }

        final JsonGenerator generator = this.generator;

        if (lastRepeated)
            generator.writeEndArray();

        final String name = numeric ? Integer.toString(fieldNumber) :
                schema.getFieldName(fieldNumber);

        if (repeated)
        {
            generator.writeArrayFieldStart(name);
            generator.writeNumber(unsignedValue);
        }
        else
        {
            generator.writeFieldName(name);
            generator.writeNumber(unsignedValue);
        }
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    @Override
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema,
            final boolean repeated) throws IOException
    {
        final JsonGenerator generator = this.generator;
        final Schema<?> lastSchema = this.schema;

        if (lastNumber != fieldNumber)
        {
            if (lastRepeated)
                generator.writeEndArray();

            final String name = numeric ? Integer.toString(fieldNumber) :
                    lastSchema.getFieldName(fieldNumber);

            if (repeated)
                generator.writeArrayFieldStart(name);
            else
                generator.writeFieldName(name);
        }

        // reset
        this.schema = schema;
        lastNumber = 0;
        lastRepeated = false;

        generator.writeStartObject();
        // recursive write
        schema.writeTo(this, value);

        if (lastRepeated)
            generator.writeEndArray();

        generator.writeEndObject();

        // restore state
        lastNumber = fieldNumber;
        lastRepeated = repeated;
        this.schema = lastSchema;
    }

    /**
     * Writes a ByteBuffer field.
     */
    @Override
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {
        writeByteRange(false, fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated);
    }

}
