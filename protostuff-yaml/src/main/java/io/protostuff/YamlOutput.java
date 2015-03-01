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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * An output used for writing data with yaml format.
 * 
 * @author David Yu
 * @created Jun 27, 2010
 */
public final class YamlOutput extends WriteSession implements Output, StatefulOutput
{

    /**
     * Returns 2 if line break is using CRLF ("\r\n"), 1 if using LF ("\n")
     */
    public static final int LINE_BREAK_LEN = "crlf".equalsIgnoreCase(
            System.getProperty("yamloutput.linebreak")) ? 2 : 1;

    /**
     * The extra indention for the yaml output. (Increases readability)
     */
    public static final int EXTRA_INDENT = Integer.getInteger(
            "yamloutput.extra_indent", 0);

    private static final byte[] COLON_AND_SPACE = new byte[] {
            (byte) ':', (byte) ' '
    };

    private static final byte[] DASH_AND_SPACE = new byte[] {
            (byte) '-', (byte) ' '
    };

    private static final byte[] EMPTY_ARRAY = new byte[] {
            (byte) '[', (byte) ']'
    };

    private static final byte[] TRUE = new byte[] {
            (byte) 't', (byte) 'r', (byte) 'u', (byte) 'e'
    };

    private static final byte[] FALSE = new byte[] {
            (byte) 'f', (byte) 'a', (byte) 'l', (byte) 's', (byte) 'e'
    };

    private static final byte EXCLAMATION = (byte) '!';

    private int indent = 0, lastNumber = 0;

    private Schema<?> schema;

    public YamlOutput(LinkedBuffer buffer, Schema<?> schema)
    {
        super(buffer);
        this.schema = schema;
    }

    public YamlOutput(LinkedBuffer buffer, OutputStream out,
            FlushHandler flushHandler, int nextBufferSize,
            Schema<?> schema)
    {
        super(buffer, out, flushHandler, nextBufferSize);
        this.schema = schema;
    }

    public YamlOutput(LinkedBuffer buffer, OutputStream out, Schema<?> schema)
    {
        super(buffer, out);
        this.schema = schema;
    }

    /**
     * Resets this output for re-use.
     */
    @Override
    public void reset()
    {
        indent = 0;
        lastNumber = 0;
    }

    @Override
    public YamlOutput clear()
    {
        super.clear();

        return this;
    }

    /**
     * Before serializing a message/object tied to a schema, this should be called.
     */
    public YamlOutput use(Schema<?> schema)
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

    YamlOutput writeSequenceDelim() throws IOException
    {
        tail = sink.writeByteArray(
                DASH_AND_SPACE,
                this,
                newLine(
                        indent,
                        sink,
                        this,
                        tail));

        indent = inc(indent, 2);
        return this;
    }

    private static int inc(int target, int byAmount)
    {
        return target + byAmount + EXTRA_INDENT;
    }

    @Override
    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeByteArray(
                    value ? TRUE : FALSE,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeByteArray(
                value ? TRUE : FALSE,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
    }

    @Override
    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeStrFromDouble(
                    value,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeStrFromDouble(
                value,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
    }

    @Override
    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeStrFromFloat(
                    value,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeStrFromFloat(
                value,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
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
        final WriteSink sink = this.sink;
        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeStrFromInt(
                    value,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeStrFromInt(
                value,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
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
        final WriteSink sink = this.sink;
        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeStrFromLong(
                    value,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeStrFromLong(
                value,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
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
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeStrUTF8(
                    value,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeStrUTF8(
                value,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    @Override
    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeByteArrayB64(
                    value,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeByteArrayB64(
                value,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
    }

    @Override
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
            int offset, int length, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if (utf8String)
        {
            if (lastNumber == fieldNumber)
            {
                // repeated
                tail = sink.writeByteArray(
                        value, offset, length,
                        this,
                        sink.writeByteArray(
                                DASH_AND_SPACE,
                                this,
                                newLine(
                                        inc(indent, 2),
                                        sink,
                                        this,
                                        tail)));

                return;
            }

            tail = sink.writeByteArray(
                    value, offset, length,
                    this,
                    writeKey(
                            schema.getFieldName(fieldNumber),
                            indent,
                            repeated,
                            sink,
                            this,
                            tail));

            lastNumber = fieldNumber;
            return;
        }

        if (lastNumber == fieldNumber)
        {
            // repeated
            tail = sink.writeByteArrayB64(
                    value, offset, length,
                    this,
                    sink.writeByteArray(
                            DASH_AND_SPACE,
                            this,
                            newLine(
                                    inc(indent, 2),
                                    sink,
                                    this,
                                    tail)));

            return;
        }

        tail = sink.writeByteArrayB64(
                value, offset, length,
                this,
                writeKey(
                        schema.getFieldName(fieldNumber),
                        indent,
                        repeated,
                        sink,
                        this,
                        tail));

        lastNumber = fieldNumber;
    }

    @Override
    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema,
            final boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        final int lastIndent = indent;
        final Schema<?> lastSchema = this.schema;

        if (lastNumber != fieldNumber)
        {
            tail = writeTag(
                    schema.messageName(),
                    repeated,
                    sink,
                    this,
                    writeKey(
                            lastSchema.getFieldName(fieldNumber),
                            lastIndent,
                            false,
                            sink,
                            this,
                            tail));
        }

        if (repeated)
        {
            final int indentRepeated = inc(lastIndent, 2);
            tail = sink.writeByteArray(
                    DASH_AND_SPACE,
                    this,
                    newLine(
                            indentRepeated,
                            sink,
                            this,
                            tail));

            indent = inc(indentRepeated, 2);
        }
        else
            indent = inc(lastIndent, 2);

        // reset (indention kept though)
        this.schema = schema;
        lastNumber = 0;

        schema.writeTo(this, value);

        // restore state
        this.schema = lastSchema;
        lastNumber = fieldNumber;
        indent = lastIndent;
    }

    static LinkedBuffer writeTag(final String name, final boolean repeated,
            final WriteSink sink, final WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        lb = sink.writeStrAscii(name,
                session,
                sink.writeByte(
                        EXCLAMATION,
                        session,
                        lb));
        if (repeated)
            return sink.writeByteArray(EMPTY_ARRAY, session, lb);

        return lb;
    }

    private static LinkedBuffer writeKey(final String name, final int indent,
            final boolean repeated, final WriteSink sink,
            final WriteSession session, LinkedBuffer lb) throws IOException
    {
        lb = sink.writeByteArray(
                COLON_AND_SPACE,
                session,
                sink.writeStrAscii(
                        name,
                        session,
                        newLine(
                                indent,
                                sink,
                                session,
                                lb)));
        if (repeated)
        {
            return sink.writeByteArray(
                    DASH_AND_SPACE,
                    session,
                    newLine(
                            inc(indent, 2),
                            sink,
                            session,
                            lb));
        }

        return lb;
    }

    private static LinkedBuffer newLine(final int indent, final WriteSink sink,
            final WriteSession session, LinkedBuffer lb) throws IOException
    {
        final int totalSize = LINE_BREAK_LEN + indent;
        session.size += totalSize;

        if (lb.offset + totalSize > lb.buffer.length)
        {
            lb = sink.drain(session, lb);
        }

        final byte[] buffer = lb.buffer;
        int offset = lb.offset;

        if (LINE_BREAK_LEN == 2)
            buffer[offset++] = (byte) '\r';

        buffer[offset++] = (byte) '\n';

        for (int i = 0; i < indent; i++)
            buffer[offset++] = (byte) ' ';

        assert offset == lb.offset + totalSize;

        lb.offset = offset;

        return lb;
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
