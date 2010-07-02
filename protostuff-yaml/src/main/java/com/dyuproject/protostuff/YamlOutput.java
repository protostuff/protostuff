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

import static com.dyuproject.protostuff.StringSerializer.STRING;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output used for writing data with yaml format.
 *
 * @author David Yu
 * @created Jun 27, 2010
 */
public final class YamlOutput implements Output
{
    
    /**
     * The default buffer size used by the internal buffer.
     */
    public static final int DEFAULT_BUFFER_SIZE = Integer.getInteger(
            "yamloutput.default_buffer_size", 512);
    
    /**
     * Large strings greater that this limit will be either zero-copied to the 
     * internal buffer or flushed to the {@link OutputStream} if streaming.  
     */
    public static final int ARRAY_COPY_SIZE_LIMIT = Integer.getInteger(
            "yamloutput.array_copy_size_limit", 255);
    
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
    
    private static final byte[] COLON_SPACE = new byte[]{
        (byte)':', (byte)' '
    };
    
    private static final byte[] TRUE = new byte[]{
        (byte)'t', (byte)'r', (byte)'u', (byte)'e'
    };
    
    private static final byte[] FALSE = new byte[]{
        (byte)'f', (byte)'a', (byte)'l', (byte)'s', (byte)'e'
    };
    
    private static final byte[] BINARY_TAG = new byte[]{
        (byte)'!', (byte)'!', 
        (byte)'b', (byte)'i', (byte)'n', (byte)'a', (byte)'r', (byte)'y',
        (byte)' ', (byte)'|'
    };
    
    private static final byte[] DELIM_REPEATED = new byte[]{
        (byte)'-', (byte)' '
    };
    
    private final LinkedBuffer root;
    private LinkedBuffer current;
    private final OutputStream out;
    
    private int indent = 0, lastNumber = 0;
    private byte[] lastTag;
    
    private Schema<?> schema;
    
    public YamlOutput(LinkedBuffer root)
    {
        this(root, null);
    }
    
    public YamlOutput(OutputStream out)
    {
        this(new LinkedBuffer(DEFAULT_BUFFER_SIZE), out);
    }
    
    public YamlOutput(LinkedBuffer root, OutputStream out)
    {
        if(root.buffer.length < 512)
            throw new IllegalArgumentException("buffer size too small.");
        
        current = root;
        this.root = root;
        this.out = out;
    }
    
    public YamlOutput(LinkedBuffer root, OutputStream out, Schema<?> schema)
    {
        this(root, out);
        this.schema = schema;
    }
    
    /**
     * Flush the buffered data written to the {@link OutputStream}.
     */
    public YamlOutput flushRemaining() throws IOException
    {
        if(out == null)
            throw new IllegalStateException("Nothing to flush.");
        
        LinkedBuffer.writeTo(out, root);
        
        return this;
    }
    
    /**
     * Returns the data written to this output as a single byte array.
     */
    byte[] toByteArray()
    {
        int size = 0;
        for(LinkedBuffer node = root; node != null; node = node.next)
            size += (node.offset - node.start);
        
        int start = 0;
        final byte[] buffer = new byte[size];     
        for(LinkedBuffer node = root; node != null; node = node.next)
        {
            final int len = node.offset - node.start;
            if(len > 0)
            {
                System.arraycopy(node.buffer, node.start, buffer, start, len);
                start += len;
            }
        }
        return buffer;
    }
    
    /**
     * Before serializing a message/object tied to a schema, this should be called.
     */
    public YamlOutput use(Schema<?> schema)
    {
        current = root.reset();
        
        indent = 0;
        lastNumber = 0;
        lastTag = null;
        
        this.schema = schema;
        
        return this;
    }
    
    YamlOutput writeSequenceDelim() throws IOException
    {
        current = writeRaw(
                DELIM_REPEATED,
                out,
                newLine(indent, out, current));
        
        indent = inc(indent, 2);
        return this;
    }
    
    static byte[] makeTag(String name, boolean repeated)
    {
        return STRING.ser(repeated ? ("!" + name + "[]") : ("!" + name));
    }
    
    private byte[] currentTag(int fieldNumber, boolean repeated, String name)
    {
        return lastNumber == fieldNumber ? lastTag : (lastTag = makeTag(name, repeated));
    }
    
    private static int inc(int target, int byAmount)
    {
        return target + byAmount + EXTRA_INDENT;
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        current = writeScalar(
                fieldNumber, 
                value ? TRUE : FALSE, 
                repeated, 
                out, 
                current, 
                indent, 
                schema);
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        current = writeScalar(
                fieldNumber, 
                STRING.ser(Double.toString(value)),
                repeated, 
                out, 
                current, 
                indent, 
                schema);
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        current = writeScalar(
                fieldNumber, 
                STRING.ser(Float.toString(value)),
                repeated, 
                out, 
                current, 
                indent, 
                schema);
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
        current = writeScalar(
                fieldNumber, 
                STRING.ser(Integer.toString(value)),
                repeated, 
                out, 
                current, 
                indent, 
                schema);
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
        current = writeScalar(
                fieldNumber, 
                STRING.ser(Long.toString(value)),
                repeated, 
                out, 
                current, 
                indent, 
                schema);
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
        current = writeScalar(
                fieldNumber, 
                STRING.ser(value),
                repeated, 
                out, 
                current, 
                indent, 
                schema);
    }
    
    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        final OutputStream out = this.out;
        final LinkedBuffer current = writeKV(
                STRING.ser(schema.getFieldName(fieldNumber)), 
                BINARY_TAG,
                true,
                out, 
                newLine(indent, out, this.current));
        
        this.current = writeRaw(value, out, newLine(inc(indent, 2), out, current));
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
        final byte[] lastTag = this.lastTag;
        final Schema<?> lastSchema = this.schema;
        
        if(lastNumber != fieldNumber)
        {
            current = writeKV(
                    STRING.ser(lastSchema.getFieldName(fieldNumber)), 
                    currentTag(fieldNumber, repeated, schema.messageName()), 
                    true,
                    out, 
                    newLine(lastIndent, out, current));
        }
        
        if(repeated)
        {
            final int indentRepeated = inc(lastIndent, 2);
            current = writeRaw(
                    DELIM_REPEATED, 
                    out, 
                    newLine(indentRepeated, out, current));
            
            indent = inc(indentRepeated, 2);
        }
        else
            indent = inc(lastIndent, 2);

        this.schema = schema;
        schema.writeTo(this, value);
        
        // restore state
        this.schema = lastSchema;
        lastNumber = fieldNumber;
        indent = lastIndent;
        this.lastTag = lastTag;
    }
    
    private LinkedBuffer writeScalar(int fieldNumber, byte[] value, boolean repeated, 
            OutputStream out, LinkedBuffer current, int indent, Schema<?> schema) 
            throws IOException
    {
        if(lastNumber == fieldNumber)
        {
            // repeated
            return writeKV(
                    DELIM_REPEATED, 
                    value, 
                    false,
                    out, 
                    newLine(inc(indent, 2), out, current));
        }
        
        lastNumber = fieldNumber;
        
        if(repeated)
        {
            current = writeKV(
                    STRING.ser(schema.getFieldName(fieldNumber)),
                    null, 
                    true, 
                    out,
                    newLine(indent, out, current));
            
            return writeKV(
                    DELIM_REPEATED, 
                    value, 
                    false, 
                    out, 
                    newLine(inc(indent, 2), out, current));
        }
        
        return writeKV( 
                STRING.ser(schema.getFieldName(fieldNumber)), 
                value, 
                true, 
                out, 
                newLine(indent, out, current));
    }
    
    private static LinkedBuffer writeKV(byte[] key, byte[] value, boolean includeColon, 
            OutputStream out, LinkedBuffer lb) throws IOException
    {
        final int totalSize = includeColon ? key.length + 2 : key.length; 
        
        if(lb.offset + totalSize > lb.buffer.length)
        {
            // does not fit in buffer
            if(out == null)
            {
                // link it
                lb = new LinkedBuffer(new byte[lb.buffer.length], 0, lb);
            }
            else
            {
                // flush
                // the buffer will be big enough (at least 512 bytes) to store the key, space and colon
                out.write(lb.buffer, lb.start, lb.offset - lb.start);
                // reset
                lb.offset = lb.start;
            }
        }
        
        int offset = lb.offset;
        final byte[] buffer = lb.buffer;
        
        System.arraycopy(key, 0, buffer, offset, key.length);
        
        if(includeColon)
        {
            offset += key.length;
            System.arraycopy(COLON_SPACE, 0, buffer, offset, 2);
        }
        
        lb.offset += totalSize;
        
        return value == null ? lb : writeRaw(value, out, lb);
    }
    
    private static LinkedBuffer newLine(int indent, OutputStream out, LinkedBuffer lb) 
    throws IOException
    {
        final int totalSize = LINE_BREAK_LEN + indent; 
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
    
    private static LinkedBuffer writeRaw(byte[] value, OutputStream out, LinkedBuffer lb)
    throws IOException
    {
        final int valueLen = value.length;
        if(valueLen > ARRAY_COPY_SIZE_LIMIT || lb.offset + valueLen > lb.buffer.length)
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
            
            // wrap and insert
            return new LinkedBuffer(lb, new LinkedBuffer(value, 0, valueLen, lb));
        }

        System.arraycopy(value, 0, lb.buffer, lb.offset, valueLen);
        
        lb.offset += valueLen;
        
        return lb;
    }

}
