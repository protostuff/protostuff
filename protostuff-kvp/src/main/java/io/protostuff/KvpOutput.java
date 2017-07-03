//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

import static io.protostuff.StringSerializer.stringSize;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * An output for serializing kvp-encoded messages (from a byte array as source). A kvp encoding is a binary encoding w/c
 * contains a key-value sequence. On the wire, a serialized field (key-value) would look like:
 * [key-len][key][value-len][value]
 * <p>
 * The keys and values are length-delimited (uint16 little endian).
 * <p>
 * Note that this encoding does not support nested messages. This encoding is mostly useful for headers w/c contain
 * information about the content it carries (see http://projects.unbit.it/uwsgi/wiki/uwsgiProtocol).
 * 
 * @author David Yu
 * @created Nov 30, 2010
 */
public final class KvpOutput extends WriteSession implements Output
{

    final byte[] numBuf = new byte[2];
    final boolean numeric;
    private Schema<?> schema;

    public KvpOutput(LinkedBuffer head, Schema<?> schema, boolean numeric)
    {
        super(head);
        this.schema = schema;
        this.numeric = numeric;
    }

    public KvpOutput(LinkedBuffer head, OutputStream out, Schema<?> schema,
            boolean numeric)
    {
        super(head, out);
        this.schema = schema;
        this.numeric = numeric;
    }

    public KvpOutput use(Schema<?> schema, boolean clearBuffer)
    {
        this.schema = schema;
        if (clearBuffer)
            tail = head.clear();

        return this;
    }

    private LinkedBuffer writeField(final int number, final LinkedBuffer lb)
            throws IOException
    {
        if (numeric)
        {
            final int len = stringSize(number);
            numBuf[0] = (byte) len;
            numBuf[1] = (byte) ((len >>> 8) & 0xFF);

            return sink.writeStrFromInt(
                    number,
                    this,
                    sink.writeByteArray(
                            numBuf, 0, 2,
                            this,
                            lb));
        }

        return sink.writeStrUTF8FixedDelimited(
                schema.getFieldName(number),
                true,
                this,
                lb);
    }

    private LinkedBuffer writeField(final int number, final int valueLen,
            LinkedBuffer lb) throws IOException
    {
        if (numeric)
        {
            final int len = stringSize(number);
            numBuf[0] = (byte) len;
            numBuf[1] = (byte) ((len >>> 8) & 0xFF);

            lb = sink.writeStrFromInt(
                    number,
                    this,
                    sink.writeByteArray(
                            numBuf, 0, 2,
                            this,
                            lb));

            // value len prefix
            numBuf[0] = (byte) valueLen;
            numBuf[1] = (byte) ((valueLen >>> 8) & 0xFF);

            return sink.writeByteArray(numBuf, 0, 2, this, lb);
        }

        // value len prefix
        numBuf[0] = (byte) valueLen;
        numBuf[1] = (byte) ((valueLen >>> 8) & 0xFF);

        return sink.writeByteArray(
                numBuf, 0, 2,
                this,
                sink.writeStrUTF8FixedDelimited(
                        schema.getFieldName(number),
                        true,
                        this,
                        lb));
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        tail = sink.writeByte(
                (byte) (value ? 0x31 : 0x30),
                this,
                writeField(
                        fieldNumber,
                        1,
                        tail));
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        tail = sink.writeByteArray(
                value,
                this,
                writeField(
                        fieldNumber,
                        value.length,
                        tail));
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, int offset, int length,
            boolean repeated) throws IOException
    {
        tail = sink.writeByteArray(
                value, offset, length,
                this,
                writeField(
                        fieldNumber,
                        length,
                        tail));
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        // TODO optimize
        final String str = Double.toString(value);
        tail = sink.writeStrAscii(
                str,
                this,
                writeField(
                        fieldNumber,
                        str.length(),
                        tail));
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
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        // TODO optimize
        final String str = Float.toString(value);
        tail = sink.writeStrAscii(
                str,
                this,
                writeField(
                        fieldNumber,
                        str.length(),
                        tail));
    }

    @Override
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        final int size = (value < 0) ? stringSize(-value) + 1 : stringSize(value);
        tail = sink.writeStrFromInt(
                value,
                this,
                writeField(
                        fieldNumber,
                        size,
                        tail));

    }

    @Override
    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        final int size = (value < 0) ? stringSize(-value) + 1 : stringSize(value);
        tail = sink.writeStrFromLong(
                value,
                this,
                writeField(
                        fieldNumber,
                        size,
                        tail));
    }

    @Override
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, boolean repeated) throws IOException
    {
        throw new UnsupportedOperationException();
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
        tail = sink.writeStrUTF8FixedDelimited(
                value,
                true,
                this,
                writeField(
                        fieldNumber,
                        tail));
    }

    @Override
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
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
