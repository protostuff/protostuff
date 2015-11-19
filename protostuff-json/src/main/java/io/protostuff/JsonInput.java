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

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * An input used for reading data with json format.
 *
 * @author David Yu
 * @created Nov 20, 2009
 */
public final class JsonInput implements Input
{

    /**
     * The wrapped json parser.
     */
    public final JsonParser parser;

    /**
     * If true, the field number will be used on json keys.
     */
    public final boolean numeric;

    private boolean lastRepeated;
    private String lastName;
    private int lastNumber;

    public JsonInput(JsonParser parser)
    {
        this(parser, false);
    }

    public JsonInput(JsonParser parser, boolean numeric)
    {
        this.parser = parser;
        this.numeric = numeric;
    }

    /**
     * Returns whether the incoming messages' field names are numeric.
     */
    public boolean isNumeric()
    {
        return numeric;
    }

    /**
     * Gets the last field number read.
     */
    public int getLastNumber()
    {
        return lastNumber;
    }

    /**
     * Returns true if the last read field was a repeated field.
     */
    public boolean isLastRepeated()
    {
        return lastRepeated;
    }

    /**
     * Resets this input.
     */
    public JsonInput reset()
    {
        lastRepeated = false;
        lastName = null;
        lastNumber = 0;
        return this;
    }

    @Override
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        if (parser.getCurrentToken().isScalarValue())
        {
            // numeric json
            // we can skip this unknown field
            if (lastRepeated)
            {
                lastRepeated = false;
                // skip the scalar elements
                while (parser.nextToken() != END_ARRAY)
                    ;
            }
            return;
        }
        else
        {
            skipObjectField(parser);
        }
    }

    @Override
    public <T> int readFieldNumber(final Schema<T> schema) throws IOException
    {
        if (lastRepeated)
        {
            if (parser.getCurrentToken() == VALUE_NULL)
            {
                // skip null elements
                JsonToken jt;
                while (VALUE_NULL == (jt = parser.nextToken()))
                    ;

                if (jt == END_ARRAY)
                {
                    // remaining elements were all null

                    // move to the next field
                    lastRepeated = false;
                    return readFieldNumber(schema, parser);
                }
            }

            return lastNumber;
        }

        return readFieldNumber(schema, parser);
    }

    private <T> int readFieldNumber(final Schema<T> schema, final JsonParser parser)
            throws IOException
    {
        for (; ; )
        {
            if (parser.nextToken() == END_OBJECT)
                return 0;

            if (parser.getCurrentToken() != FIELD_NAME)
            {
                throw new JsonInputException("Expected token: $field: but was " +
                        parser.getCurrentToken() + " on message " + schema.messageFullName());
            }

            final String name = parser.getCurrentName();

            // move to the next token
            if (parser.nextToken() == START_ARRAY)
            {
                JsonToken jt = parser.nextToken();

                // if empty array, read the next field
                if (jt == END_ARRAY)
                    continue;

                if (jt == VALUE_NULL)
                {
                    // skip null elements
                    while (VALUE_NULL == (jt = parser.nextToken()))
                        ;

                    // all elements were null.
                    if (jt == END_ARRAY)
                        continue;
                }

                final int number = numeric ? Integer.parseInt(name) :
                        schema.getFieldNumber(name);

                if (number == 0)
                {
                    // unknown field
                    if (parser.getCurrentToken().isScalarValue())
                    {
                        // skip the scalar elements
                        while (parser.nextToken() != END_ARRAY)
                            ;
                    }
                    else
                    {
                        skipObjectField(parser);
                    }
                    continue;
                }

                lastRepeated = true;
                lastName = name;
                lastNumber = number;

                return number;
            }

            // skip null value
            if (parser.getCurrentToken() == VALUE_NULL)
                continue;

            final int number = numeric ? Integer.parseInt(name) :
                    schema.getFieldNumber(name);

            if (number == 0)
            {
                // we can skip this unknown field
                if (!parser.getCurrentToken().isScalarValue())
                {
                    skipObjectField(parser);
                }
                continue;
            }

            lastName = name;
            lastNumber = number;

            return number;
        }
    }

    @Override
    public boolean readBool() throws IOException
    {
        final JsonToken jt = parser.getCurrentToken();
        if (lastRepeated && parser.nextToken() == END_ARRAY)
            lastRepeated = false;

        if (jt == VALUE_TRUE)
            return true;
        if (jt == VALUE_FALSE)
            return false;

        throw new JsonInputException("Expected token: true/false but was " + jt);
    }

    @Override
    public byte[] readByteArray() throws IOException
    {
        final byte[] value = parser.getBinaryValue();

        if (lastRepeated && parser.nextToken() == END_ARRAY)
            lastRepeated = false;

        return value;
    }

    @Override
    public ByteString readBytes() throws IOException
    {
        return ByteString.wrap(readByteArray());
    }

    @Override
    public double readDouble() throws IOException
    {
        final double value = parser.getDoubleValue();

        if (lastRepeated && parser.nextToken() == END_ARRAY)
            lastRepeated = false;

        return value;
    }

    @Override
    public int readEnum() throws IOException
    {
        return readInt32();
    }

    @Override
    public int readFixed32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readFixed64() throws IOException
    {
        return readInt64();
    }

    @Override
    public float readFloat() throws IOException
    {
        final float value = parser.getFloatValue();

        if (lastRepeated && parser.nextToken() == END_ARRAY)
            lastRepeated = false;

        return value;
    }

    @Override
    public int readInt32() throws IOException
    {
        final int value = parser.getIntValue();

        if (lastRepeated && parser.nextToken() == END_ARRAY)
            lastRepeated = false;

        return value;
    }

    @Override
    public long readInt64() throws IOException
    {
        final long value = parser.getLongValue();

        if (lastRepeated && parser.nextToken() == END_ARRAY)
            lastRepeated = false;

        return value;
    }

    @Override
    public int readSFixed32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readSFixed64() throws IOException
    {
        return readInt64();
    }

    @Override
    public int readSInt32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readSInt64() throws IOException
    {
        return readInt64();
    }

    @Override
    public String readString() throws IOException
    {
        if (parser.getCurrentToken() != VALUE_STRING)
            throw new JsonInputException("Expected token: string but was " + parser.getCurrentToken());

        final String value = parser.getText();

        if (lastRepeated && parser.nextToken() == END_ARRAY)
            lastRepeated = false;

        return value;
    }

    @Override
    public int readUInt32() throws IOException
    {
        return readInt32();
    }

    @Override
    public long readUInt64() throws IOException
    {
        return readInt64();
    }

    @Override
    public <T> T mergeObject(T value, final Schema<T> schema) throws IOException
    {
        if (parser.getCurrentToken() != START_OBJECT)
        {
            throw new JsonInputException("Expected token: { but was " +
                    parser.getCurrentToken() + " on " + lastName + " of message " +
                    schema.messageFullName());
        }

        final int lastNumber = this.lastNumber;
        final boolean lastRepeated = this.lastRepeated;
        final String lastName = this.lastName;

        // reset
        this.lastRepeated = false;

        if (value == null)
            value = schema.newMessage();

        schema.mergeFrom(this, value);

        if (parser.getCurrentToken() != END_OBJECT)
        {
            throw new JsonInputException("Expected token: } but was " +
                    parser.getCurrentToken() + " on " + lastName + " of message " +
                    schema.messageFullName());
        }

        if (!schema.isInitialized(value))
            throw new UninitializedMessageException(value, schema);

        // restore state
        this.lastNumber = lastNumber;
        this.lastRepeated = lastRepeated;
        this.lastName = lastName;

        if (lastRepeated && parser.nextToken() == END_ARRAY)
            this.lastRepeated = false;

        return value;
    }

    @Override
    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber,
            boolean repeated) throws IOException
    {
        if (utf8String)
            output.writeString(fieldNumber, readString(), repeated);
        else
            output.writeByteArray(fieldNumber, readByteArray(), repeated);
    }

    /**
     * Reads a byte array/ByteBuffer value.
     */
    @Override
    public ByteBuffer readByteBuffer() throws IOException
    {
        return ByteBuffer.wrap(readByteArray());
    }

    /**
     * Skip through the entire objct field and all nested objects inside it
     */
    private void skipObjectField(JsonParser parser) throws IOException
    {
        int nestedObjects = 0;
        while (parser.nextToken() != END_OBJECT || nestedObjects > 0)
        {
            JsonToken token = parser.getCurrentToken();
            if (token == START_OBJECT)
            {
                nestedObjects++;
            }
            else if (token == END_OBJECT)
            {
                nestedObjects--;
            }
        }
    }

}
