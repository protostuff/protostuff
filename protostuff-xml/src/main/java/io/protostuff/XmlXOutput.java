//========================================================================
//Copyright 2012 David Yu
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
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Optimized xml output. The string values are not escaped (its assumed are no xml control chars or that they've been
 * validated and xml-escaped on input).
 * 
 * @author David Yu
 * @created Aug 30, 2012
 */
public final class XmlXOutput extends WriteSession implements Output, StatefulOutput
{

    static final byte START_TAG = '<', END_TAG = '>', SLASH = '/';

    static final byte[] START_SLASH_TAG = new byte[] { '<', '/' },
            TRUE = new byte[] { 't', 'r', 'u', 'e' },
            FALSE = new byte[] { 'f', 'a', 'l', 's', 'e' };

    private Schema<?> schema;

    public XmlXOutput(LinkedBuffer head, Schema<?> schema)
    {
        super(head);
        this.schema = schema;
    }

    public XmlXOutput(LinkedBuffer head, OutputStream out,
            FlushHandler flushHandler, int nextBufferSize,
            Schema<?> schema)
    {
        super(head, out, flushHandler, nextBufferSize);
        this.schema = schema;
    }

    public XmlXOutput(LinkedBuffer head, OutputStream out, Schema<?> schema)
    {
        super(head, out);
        this.schema = schema;
    }

    @Override
    public XmlXOutput clear()
    {
        super.clear();

        return this;
    }

    /**
     * Before serializing a message/object tied to a schema, this should be called.
     */
    public XmlXOutput use(Schema<?> schema)
    {
        this.schema = schema;

        return this;
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
        final String name = schema.getFieldName(fieldNumber);

        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeByteArray(value ? TRUE : FALSE, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        final String name = schema.getFieldName(fieldNumber);

        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeStrFromDouble(value, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        final String name = schema.getFieldName(fieldNumber);

        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeStrFromFloat(value, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    @Override
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        final String name = schema.getFieldName(fieldNumber);

        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeStrFromInt(value, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    @Override
    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        final String name = schema.getFieldName(fieldNumber);

        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeStrFromLong(value, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeString(int fieldNumber, CharSequence value, boolean repeated) throws IOException
    {
        final String name = schema.getFieldName(fieldNumber);

        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeStrUTF8(value, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        if (!utf8String)
        {
            // B64 encode
            writeB64(schema.getFieldName(fieldNumber), value, offset, length, repeated);
            return;
        }

        // write direct
        final String name = schema.getFieldName(fieldNumber);
        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeByteArray(value, offset, length, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    private void writeB64(String name, byte[] value, int offset, int length,
            boolean repeated) throws IOException
    {
        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this,
                                sink.writeByteArrayB64(value, offset, length, this,
                                        sink.writeByte(END_TAG, this,
                                                sink.writeStrAscii(name, this,
                                                        sink.writeByte(START_TAG, this, tail)))))));
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        writeB64(schema.getFieldName(fieldNumber), value, 0, value.length, repeated);
    }

    @Override
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, boolean repeated)
            throws IOException
    {
        final Schema<?> lastSchema = this.schema;
        this.schema = schema;

        final String name = lastSchema.getFieldName(fieldNumber);

        // start tag
        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByte(START_TAG, this, tail)));

        schema.writeTo(this, value);

        // end tag
        tail = sink.writeByte(END_TAG, this,
                sink.writeStrAscii(name, this,
                        sink.writeByteArray(START_SLASH_TAG, this, tail)));

        // restore state
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
