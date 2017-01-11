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

import org.msgpack.value.ImmutableBooleanValue;
import org.msgpack.value.impl.ImmutableBinaryValueImpl;
import org.msgpack.value.impl.ImmutableBooleanValueImpl;
import org.msgpack.value.impl.ImmutableDoubleValueImpl;
import org.msgpack.value.impl.ImmutableLongValueImpl;
import org.msgpack.value.impl.ImmutableStringValueImpl;

import io.protostuff.MsgpackGenerator.ImmutableFloatValueImpl;

/**
 * Output is using to write data in message pack format.
 * 
 * @author Alex Shvid
 */
public class MsgpackOutput implements Output, StatefulOutput
{

    private MsgpackGenerator generator;
    private Schema<?> schema;

    public MsgpackOutput(MsgpackGenerator generator, Schema<?> schema)
    {
        this.generator = generator;
        this.schema = schema;
    }

    @Override
    public void updateLast(Schema<?> schema, Schema<?> lastSchema)
    {

        if (lastSchema != null && lastSchema == this.schema)
        {
            this.schema = schema;
        }

    }

    /**
     * Before serializing a message/object tied to a schema, this should be called. This also resets the internal state
     * of this output.
     */
    public MsgpackOutput use(MsgpackGenerator generator, Schema<?> schema)
    {
        this.schema = schema;
        this.generator = generator;
        return this;
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableFloatValueImpl(value), repeated);
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableDoubleValueImpl(value), repeated);
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        ImmutableBooleanValue val = value ? ImmutableBooleanValueImpl.TRUE : ImmutableBooleanValueImpl.FALSE;
        generator.pushValue(schema, fieldNumber, val, repeated);
    }

    @Override
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableLongValueImpl(value), repeated);
    }

    @Override
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableStringValueImpl(value), repeated);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableBinaryValueImpl(value.getBytes()), repeated);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        generator.pushValue(schema, fieldNumber, new ImmutableBinaryValueImpl(value), repeated);
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, int offset, int length,
            boolean repeated) throws IOException
    {

        byte[] copiedValue = new byte[length];
        System.arraycopy(value, offset, copiedValue, 0, length);

        if (utf8String)
        {
            generator.pushValue(schema, fieldNumber, new ImmutableStringValueImpl(copiedValue), repeated);
        }
        else
        {
            generator.pushValue(schema, fieldNumber, new ImmutableBinaryValueImpl(copiedValue), repeated);
        }

    }

    @Override
    public <T> void writeObject(int fieldNumber, T value, Schema<T> innerSchema, boolean repeated) throws IOException
    {

        MsgpackGenerator innerGenerator = new MsgpackGenerator(generator.isNumeric());

        MsgpackGenerator thisGenerator = this.generator;
        Schema<?> thisSchema = this.schema;

        use(innerGenerator, innerSchema);

        innerSchema.writeTo(this, value);

        use(thisGenerator, thisSchema);

        generator.pushValue(this.schema, fieldNumber, innerGenerator.toValue(), repeated);

    }

    @Override
    public void writeBytes(int fieldNumber, ByteBuffer value, boolean repeated) throws IOException
    {

        writeByteRange(false, fieldNumber, value.array(), value.arrayOffset() + value.position(),
                value.remaining(), repeated);

    }

}
