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

package com.dyuproject.protostuff;

import java.io.IOException;
import java.io.OutputStream;

import com.dyuproject.protostuff.LinkedBuffer.WriteSession;


/**
 * An output used for writing data with yaml format.
 *
 * @author David Yu
 * @created Jun 27, 2010
 */
public final class YamlOutput extends WriteSession implements Output
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
    
    private final OutputStream out;
    
    private int indent = 0, lastNumber = 0;
    
    private Schema<?> schema;
    
    public YamlOutput(LinkedBuffer buffer)
    {
        this(buffer, null);
    }
    
    public YamlOutput(LinkedBuffer buffer, OutputStream out)
    {
        super(buffer);
        this.out = out;
    }
    
    public YamlOutput(LinkedBuffer buffer, OutputStream out, Schema<?> schema)
    {
        this(buffer, out);
        this.schema = schema;
    }
    
    public YamlOutput(LinkedBuffer buffer, int nextBufferSize, int arrayCopySizeLimit, 
            OutputStream out)
    {
        super(buffer, nextBufferSize, arrayCopySizeLimit);
        this.out = out;
    }
    
    public YamlOutput(LinkedBuffer head, int nextBufferSize, int arrayCopySizeLimit, 
            OutputStream out, Schema<?> schema)
    {
        this(head, nextBufferSize, arrayCopySizeLimit, out);
        this.schema = schema;
    }
    
    /**
     * Flush the buffered data written to the {@link OutputStream}.
     */
    public YamlOutput flushRemaining() throws IOException
    {
        if(out == null)
            throw new IllegalStateException("Nothing to flush.");

        LinkedBuffer.writeTo(out, head);
        
        return this;
    }
    
    /**
     * Resets this output for re-use.
     */
    public YamlOutput clear(boolean clearBuffer, boolean clearSize)
    {
        if(clearBuffer)
            tail = head.clear();
        if(clearSize)
            size = 0;
        
        indent = 0;
        lastNumber = 0;
        
        return this;
    }
    
    /**
     * Before serializing a message/object tied to a schema, this should be called.
     */
    public YamlOutput use(Schema<?> schema, boolean clearBuffer, boolean clearSize)
    {
        this.schema = schema;
        return clear(clearBuffer, clearSize);
    }
    
    YamlOutput writeSequenceDelim() throws IOException
    {
        tail = writeSequenceDelim(indent, out, this, tail);
        indent = inc(indent, 2);
        return this;
    }
    
    private static int inc(int target, int byAmount)
    {
        return target + byAmount + EXTRA_INDENT;
    }

    public void writeBool(int fieldNumber, boolean bvalue, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        final String value = bvalue ? "true" : "false";
        
        if(lastNumber == fieldNumber)
        {
            // repeated
            final LinkedBuffer lb = writeSequenceDelim(inc(indent, 2), out, this, tail);
            
            if(out != null && (lb.offset + value.length()) > lb.buffer.length)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }

            tail = StringSerializer.writeAscii(
                    value, 
                    this, 
                    lb);

            return;
        }
        
        lastNumber = fieldNumber;

        LinkedBuffer lb = writeFieldName(
                schema.getFieldName(fieldNumber), 
                indent, 
                repeated, 
                out, 
                this, 
                tail);
        
        if(out != null && (lb.offset + value.length()) > lb.buffer.length)
        {
            // flush
            out.write(lb.buffer, lb.start, lb.offset - lb.start);
            // reset
            lb.offset = lb.start;
        }
        
        tail = StringSerializer.writeAscii(value, this, lb);
    }

    public void writeDouble(int fieldNumber, double dvalue, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        final String value = Double.toString(dvalue);
        
        if(lastNumber == fieldNumber)
        {
            // repeated
            final LinkedBuffer lb = writeSequenceDelim(inc(indent, 2), out, this, tail);
            
            if(out != null && (lb.offset + value.length()) > lb.buffer.length)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }

            tail = StringSerializer.writeAscii(
                    value, 
                    this, 
                    lb);

            return;
        }
        
        lastNumber = fieldNumber;

        LinkedBuffer lb = writeFieldName(
                schema.getFieldName(fieldNumber), 
                indent, 
                repeated, 
                out, 
                this, 
                tail);
        
        if(out != null && (lb.offset + value.length()) > lb.buffer.length)
        {
            // flush
            out.write(lb.buffer, lb.start, lb.offset - lb.start);
            // reset
            lb.offset = lb.start;
        }
        
        tail = StringSerializer.writeAscii(value, this, lb);
    }

    public void writeFloat(int fieldNumber, float fvalue, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        final String value = Float.toString(fvalue);
        
        if(lastNumber == fieldNumber)
        {
            // repeated
            final LinkedBuffer lb = writeSequenceDelim(inc(indent, 2), out, this, tail);
            
            if(out != null && (lb.offset + value.length()) > lb.buffer.length)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }

            tail = StringSerializer.writeAscii(
                    value, 
                    this, 
                    lb);

            return;
        }
        
        lastNumber = fieldNumber;

        LinkedBuffer lb = writeFieldName(
                schema.getFieldName(fieldNumber), 
                indent, 
                repeated, 
                out, 
                this, 
                tail);
        
        if(out != null && (lb.offset + value.length()) > lb.buffer.length)
        {
            // flush
            out.write(lb.buffer, lb.start, lb.offset - lb.start);
            // reset
            lb.offset = lb.start;
        }
        
        tail = StringSerializer.writeAscii(value, this, lb);
    }
    
    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }
    
    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        
        if(lastNumber == fieldNumber)
        {
            // repeated
            final LinkedBuffer lb = writeSequenceDelim(inc(indent, 2), out, this, tail);
            
            // 11 is the largest possible size of the stringied int
            if(out != null && (lb.offset + 11) > lb.buffer.length)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }

            tail = StringSerializer.writeInt(
                    value, 
                    this, 
                    lb);

            return;
        }
        
        lastNumber = fieldNumber;

        LinkedBuffer lb = writeFieldName(
                schema.getFieldName(fieldNumber), 
                indent, 
                repeated, 
                out, 
                this, 
                tail);
        
        if(out != null && (lb.offset + 11) > lb.buffer.length)
        {
            // flush
            out.write(lb.buffer, lb.start, lb.offset - lb.start);
            // reset
            lb.offset = lb.start;
        }
        
        tail = StringSerializer.writeInt(value, this, lb);
    }

    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }
    
    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }
    
    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        
        if(lastNumber == fieldNumber)
        {
            // repeated
            final LinkedBuffer lb = writeSequenceDelim(inc(indent, 2), out, this, tail);
            
            // 20 is the largest possible size of the stringied long
            if(out != null && (lb.offset + 20) > lb.buffer.length)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }

            tail = StringSerializer.writeLong(
                    value, 
                    this, 
                    lb);

            return;
        }
        
        lastNumber = fieldNumber;

        LinkedBuffer lb = writeFieldName(
                schema.getFieldName(fieldNumber), 
                indent, 
                repeated, 
                out, 
                this, 
                tail);
        
        if(out != null && (lb.offset + 20) > lb.buffer.length)
        {
            // flush
            out.write(lb.buffer, lb.start, lb.offset - lb.start);
            // reset
            lb.offset = lb.start;
        }
        
        tail = StringSerializer.writeLong(value, this, lb);
    }

    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }
    
    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }
    
    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        
        if(lastNumber == fieldNumber)
        {
            // repeated
            final LinkedBuffer lb = writeSequenceDelim(inc(indent, 2), out, this, tail);
            
            if(out != null && (lb.offset + (value.length()*3)) > lb.buffer.length)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }

            tail = StringSerializer.writeUTF8(
                    value, 
                    this, 
                    lb);

            return;
        }
        
        lastNumber = fieldNumber;

        LinkedBuffer lb = writeFieldName(
                schema.getFieldName(fieldNumber), 
                indent, 
                repeated, 
                out, 
                this, 
                tail);
        
        if(out != null && (lb.offset + value.length()*30) > lb.buffer.length)
        {
            // flush
            out.write(lb.buffer, lb.start, lb.offset - lb.start);
            // reset
            lb.offset = lb.start;
        }
        
        tail = StringSerializer.writeUTF8(value, this, lb);
    }
    
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        
        if(lastNumber == fieldNumber)
        {
            // TODO Base64 encode
            // repeated
            tail = writeRaw(
                    value, 
                    out, 
                    this, 
                    writeSequenceDelim(inc(indent, 2), out, this, tail));

            return;
        }
        
        lastNumber = fieldNumber;

        LinkedBuffer lb = writeTag(
                "!binary", 
                false, 
                out, 
                this, 
                writeFieldName(
                        schema.getFieldName(fieldNumber),
                        indent,
                        false,
                        out,
                        this,
                        tail));
        
        lb = newLine(inc(indent, 2), out, this, lb);
        if(repeated)
        {
            if(lb.offset + 2 > lb.buffer.length)
            {
                if(out != null)
                {
                    // flush
                    out.write(lb.buffer, lb.start, lb.offset - lb.start);
                    // reset
                    lb.offset = lb.start;
                }
                else
                {
                    // dash and space doesn't fit buffer
                    lb = new LinkedBuffer(nextBufferSize, lb);
                }
            }
            
            lb.buffer[lb.offset++] = (byte)'-';
            lb.buffer[lb.offset++] = (byte)' ';
            
            // dash and space
            size += 2;
        }
        
        tail = writeRaw(value, out, this, lb);
        
        /*final OutputStream out = this.out;
        final LinkedBuffer tail = writeKV(
                STRING.ser(schema.getFieldName(fieldNumber)), 
                BINARY_TAG,
                true,
                out, 
                this, 
                newLine(indent, out, this, this.tail));
        
        this.tail = writeRaw(value, out, this, newLine(inc(indent, 2), out, this, tail));*/
    }
    
    public <T extends Message<T>> void writeMessage(int fieldNumber, T value, boolean repeated) 
    throws IOException
    {
        writeObject(fieldNumber, value, value.cachedSchema(), repeated);
    }

    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema, 
            final boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        final int lastIndent = indent;
        final Schema<?> lastSchema = this.schema;
        
        if(lastNumber != fieldNumber)
        {
            tail = writeTag(
                    schema.messageName(), 
                    repeated, 
                    out, 
                    this, 
                    writeFieldName(
                            lastSchema.getFieldName(fieldNumber),
                            lastIndent,
                            false,
                            out,
                            this,
                            tail));
        }
        
        if(repeated)
        {
            final int indentRepeated = inc(lastIndent, 2);
            LinkedBuffer lb = newLine(indentRepeated, out, this, tail);
            
            if(lb.offset + 2 > lb.buffer.length)
            {
                if(out != null)
                {
                    // flush
                    out.write(lb.buffer, lb.start, lb.offset - lb.start);
                    // reset
                    lb.offset = lb.start;
                }
                else
                {
                    // dash and space doesn't fit buffer
                    lb = new LinkedBuffer(nextBufferSize, lb);
                }
            }
            
            lb.buffer[lb.offset++] = (byte)'-';
            lb.buffer[lb.offset++] = (byte)' ';
            
            size += 2;
            
            indent = inc(indentRepeated, 2);
            
            tail = lb;
        }
        else
            indent = inc(lastIndent, 2);

        this.schema = schema;
        schema.writeTo(this, value);
        
        // restore state
        this.schema = lastSchema;
        lastNumber = fieldNumber;
        indent = lastIndent;
    }
    
    static LinkedBuffer writeTag(final String name, final boolean repeated, 
            final OutputStream out, final WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        boolean flushed = false;
        if(lb.offset == lb.buffer.length)
        {
            if(out != null)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
                flushed = true;
            }
            else
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
        }
        
        lb.buffer[lb.offset++] = (byte)'!';
        
        if(out != null && !flushed && lb.offset + name.length() > lb.buffer.length)
        {
            // flush
            out.write(lb.buffer, lb.start, lb.offset - lb.start);
            // reset
            lb.offset = lb.start;
            flushed = true;
        }
        
        lb = StringSerializer.writeAscii(name, session, lb);
        
        if(repeated)
        {
            if(lb.offset + 2 > lb.buffer.length)
            {
                if(out != null)
                {
                    // flush
                    out.write(lb.buffer, lb.start, lb.offset - lb.start);
                    // reset
                    lb.offset = lb.start;
                }
                else
                {
                    // grow
                    lb = new LinkedBuffer(session.nextBufferSize, lb);
                }
            }
            
            lb.buffer[lb.offset++] = (byte)'[';
            lb.buffer[lb.offset++] = (byte)']';
            
            // ! + [ + ]
            session.size += 3;
        }
        else
            session.size++;
        
        return lb;
    }
    
    private static LinkedBuffer writeFieldName(final String name, final int indent, 
            final boolean repeated, final OutputStream out, 
            final WriteSession session, LinkedBuffer lb) throws IOException
    {
        lb = newLine(indent, out, session, lb);
        
        if(out != null)
        {
            if(lb.offset + name.length() > lb.buffer.length)
            {
                // we avoid allocating another buffer if there's an outputstream.
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // restart
                lb.offset = lb.start;
                
                // field name surely cannot reach 511 bytes (minimum buffer size is 512)
                lb = StringSerializer.writeAscii(
                        name, 
                        session, 
                        lb);
            }
            else
            {
                lb = StringSerializer.writeAscii(
                        name, 
                        session, 
                        lb);
                
                if(lb.offset + 2 > lb.buffer.length)
                {
                    // flush
                    out.write(lb.buffer, lb.start, lb.offset - lb.start);
                    // reset
                    lb.offset = lb.start;
                }
            }
            
            lb.buffer[lb.offset++] = (byte)':';
            lb.buffer[lb.offset++] = (byte)' ';
        }
        else
        {
            lb = StringSerializer.writeAscii(
                    name, 
                    session, 
                    lb);
            
            if(lb.offset + 2 > lb.buffer.length)
            {
                // colon and space doesn't fit buffer
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
            
            lb.buffer[lb.offset++] = (byte)':';
            lb.buffer[lb.offset++] = (byte)' ';
        }
        
        // colon and space
        session.size += 2;
        
        if(repeated)
        {
            lb = newLine(inc(indent, 2), out, session, lb);
            if(lb.offset + 2 > lb.buffer.length)
            {
                if(out != null)
                {
                    // flush
                    out.write(lb.buffer, lb.start, lb.offset - lb.start);
                    // reset
                    lb.offset = lb.start;
                }
                else
                {
                    // dash and space doesn't fit buffer
                    lb = new LinkedBuffer(session.nextBufferSize, lb);
                }
            }
            
            lb.buffer[lb.offset++] = (byte)'-';
            lb.buffer[lb.offset++] = (byte)' ';
            
            // dash and space
            session.size += 2;
        }
        
        return lb;
    }
    
    private static LinkedBuffer newLine(final int indent, final OutputStream out, 
            final WriteSession session, LinkedBuffer lb) throws IOException
    {
        final int totalSize = LINE_BREAK_LEN + indent;
        session.size += totalSize;
        
        if(lb.offset + totalSize > lb.buffer.length)
        {
            if(out != null)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }
            else
            {
                // link(increase) it
                lb = new LinkedBuffer(new byte[lb.buffer.length], 0, lb);
            }
        }
        
        final byte[] buffer = lb.buffer;
        int offset = lb.offset;
        
        if(LINE_BREAK_LEN == 2)
            buffer[offset++] = (byte)'\r';
        
        buffer[offset++] = (byte)'\n';
        
        for(int i=0; i < indent; i++)
            buffer[offset++] = (byte)' ';
        
        assert offset == lb.offset + totalSize;
        
        lb.offset = offset;
        
        return lb;
    }
    
    static LinkedBuffer writeRaw(final byte[] value, final OutputStream out, 
            final WriteSession session, LinkedBuffer lb) throws IOException
    {
        final int valueLen = value.length;
        session.size += valueLen;
        
        if(lb.offset + valueLen > lb.buffer.length)
        {
            if(out != null)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                out.write(value);
                // reset
                lb.offset = lb.start;
                return lb;
            }
            
            final int remaining = lb.buffer.length - lb.offset;
            if(remaining + session.nextBufferSize < valueLen)
            {
                // too large ... must wrap and insert (zero copy)
                return new LinkedBuffer(lb, new LinkedBuffer(value, 0, valueLen, lb));
            }
            
            // copy what can fit
            System.arraycopy(value, 0, lb.buffer, lb.offset, remaining);
            
            lb.offset += remaining;
            
            // grow
            lb = new LinkedBuffer(session.nextBufferSize, lb);
            
            final int leftover = value.length - remaining;
            
            // copy what's left
            System.arraycopy(value, remaining, lb.buffer, 0, leftover);
            
            lb.offset += leftover;
            
            return lb;
        }

        // it fits
        System.arraycopy(value, 0, lb.buffer, lb.offset, valueLen);
        
        lb.offset += valueLen;
        
        return lb;
    }
    
    private static LinkedBuffer writeSequenceDelim(final int indent, final OutputStream out, 
            final WriteSession session, LinkedBuffer lb) throws IOException
    {
        lb = newLine(indent, out, session, lb);

        if(lb.offset + 2 > lb.buffer.length)
        {
            if(out != null)
            {
                // flush
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }
            else
            {
                // dash and space doesn't fit buffer
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
        }
        
        lb.buffer[lb.offset++] = (byte)'-';
        lb.buffer[lb.offset++] = (byte)' ';
        
        session.size += 2;

        return lb;
    }

}
