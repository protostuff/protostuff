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

/**
 * An optimized json output which is efficient in writing numeric keys and 
 * pre-encoded utf8 strings (in byte array form).
 * 
 * This is the appropriate output sink to use when writing from 
 * binary (protostuff,protobuf,etc) pipes.
 *
 * @author David Yu
 * @created Jul 2, 2010
 */
public final class JsonXOutput extends WriteSession implements Output
{
    
    private static final byte START_OBJECT = (byte)'{', END_OBJECT = (byte)'}', 
            START_ARRAY = (byte)'[', END_ARRAY = (byte)']', 
            COMMA = (byte)',', QUOTE = (byte)'"';
    
    private static final byte[] TRUE = new byte[]{
        (byte)'t', (byte)'r', (byte)'u', (byte)'e'
    };
    
    private static final byte[] FALSE = new byte[]{
        (byte)'f', (byte)'a', (byte)'l', (byte)'s', (byte)'e'
    };
    
    private static final byte[] KEY_SUFFIX_ARRAY = new byte[]{
        (byte)'"', (byte)':', (byte)'['
    };
    
    private static final byte[] KEY_SUFFIX_ARRAY_OBJECT = new byte[]{
        (byte)'"', (byte)':', (byte)'[', (byte)'{'
    };
    
    private static final byte[] KEY_SUFFIX_ARRAY_STRING = new byte[]{
        (byte)'"', (byte)':', (byte)'[', (byte)'"'
    };
    
    private static final byte[] KEY_SUFFIX_OBJECT = new byte[]{
        (byte)'"', (byte)':', (byte)'{'
    };
    
    private static final byte[] KEY_SUFFIX_STRING = new byte[]{
        (byte)'"', (byte)':', (byte)'"'
    };
    
    private static final byte[] KEY_SUFFIX = new byte[]{
        (byte)'"', (byte)':',
    };
    
    private static final byte[] COMMA_AND_QUOTE = new byte[]{
        (byte)',', (byte)'"',
    };
    
    private static final byte[] COMMA_AND_START_OBJECT = new byte[]{
        (byte)',', (byte)'{',
    };
    
    private static final byte[] END_ARRAY_AND_END_OBJECT = new byte[]{
        (byte)']', (byte)'}'
    };
    
    private static final byte[] END_ARRAY__COMMA__QUOTE = new byte[]{
        (byte)']', (byte)',', (byte)'"'
    };
    
    private Schema<?> schema;
    private final boolean numeric;
    private boolean lastRepeated;
    private int lastNumber;
    
    public JsonXOutput(LinkedBuffer head, boolean numeric, Schema<?> schema)
    {
        super(head);
        this.numeric = numeric;
        this.schema = schema;
    }
    
    public JsonXOutput(LinkedBuffer head, OutputStream out, boolean numeric, 
            Schema<?> schema)
    {
        super(head, out);
        this.numeric = numeric;
        this.schema = schema;
    }
    
    /**
     * Resets this output for re-use.
     */
    public JsonXOutput clear(boolean clearBuffer)
    {
        if(clearBuffer)
            tail = head.clear();
        
        lastRepeated = false;
        lastNumber = 0;
        
        return this;
    }
    
    /**
     * Before serializing a message/object tied to a schema, this should be called.
     */
    public JsonXOutput use(Schema<?> schema, boolean clearBuffer)
    {
        this.schema = schema;
        
        return clear(clearBuffer);
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
    
    JsonXOutput writeCommaAndStartObject() throws IOException
    {
        tail = sink.writeByteArray(COMMA_AND_START_OBJECT, this, tail);
        return this;
    }
    
    JsonXOutput writeStartObject() throws IOException
    {
        tail = sink.writeByte(START_OBJECT, this, tail);
        return this;
    }
    
    JsonXOutput writeEndObject() throws IOException
    {
        tail = sink.writeByte(END_OBJECT, this, tail);
        return this;
    }
    
    JsonXOutput writeStartArray() throws IOException
    {
        tail = sink.writeByte(START_ARRAY, this, tail);
        return this;
    }
    
    JsonXOutput writeEndArray() throws IOException
    {
        tail = sink.writeByte(END_ARRAY, this, tail);
        return this;
    }
    
    private LinkedBuffer writeKey(final int fieldNumber, final WriteSink sink, 
            final byte[] keySuffix) throws IOException
    {
        if(numeric)
        {
            if(lastRepeated)
            {
                return sink.writeByteArray(
                        keySuffix, 
                        this, 
                        sink.writeStrFromInt(
                                fieldNumber, 
                                this, 
                                sink.writeByteArray(
                                        END_ARRAY__COMMA__QUOTE, 
                                        this, 
                                        tail)));
            }
            
            if(lastNumber == 0)
            {
                return sink.writeByteArray(
                        keySuffix, 
                        this, 
                        sink.writeStrFromInt(
                                fieldNumber, 
                                this, 
                                sink.writeByte(
                                        QUOTE, 
                                        this, 
                                        tail)));
            }
            
            return sink.writeByteArray(
                    keySuffix, 
                    this, 
                    sink.writeStrFromInt(
                            fieldNumber, 
                            this, 
                            sink.writeByteArray(
                                    COMMA_AND_QUOTE, 
                                    this, 
                                    tail)));
        }
        
        
        if(lastRepeated)
        {
            return sink.writeByteArray(
                    keySuffix, 
                    this, 
                    sink.writeStrAscii(
                            schema.getFieldName(fieldNumber), 
                            this, 
                            sink.writeByteArray(
                                    END_ARRAY__COMMA__QUOTE, 
                                    this, 
                                    tail)));
        }
        
        if(lastNumber == 0)
        {
            return sink.writeByteArray(
                    keySuffix, 
                    this, 
                    sink.writeStrAscii(
                            schema.getFieldName(fieldNumber), 
                            this, 
                            sink.writeByte(
                                    QUOTE, 
                                    this, 
                                    tail)));
        }
        
        return sink.writeByteArray(
                keySuffix, 
                this, 
                sink.writeStrAscii(
                        schema.getFieldName(fieldNumber), 
                        this, 
                        sink.writeByteArray(
                                COMMA_AND_QUOTE, 
                                this, 
                                tail)));
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeByteArray(
                    value ? TRUE : FALSE, 
                    this, 
                    sink.writeByte(
                            COMMA, 
                            this, 
                            tail));
            return;
        }
        
        tail = sink.writeByteArray(
                value ? TRUE : FALSE, 
                this, 
                writeKey(
                        fieldNumber, 
                        sink, 
                        repeated ? KEY_SUFFIX_ARRAY : KEY_SUFFIX));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeByte(
                    QUOTE, 
                    this, 
                    sink.writeByteArrayB64(
                            value, 0, value.length, 
                            this, 
                            sink.writeByteArray(
                                    COMMA_AND_QUOTE, 
                                    this, 
                                    tail)));
            return;
        }
        
        tail = sink.writeByte(
                QUOTE, 
                this, 
                sink.writeByteArrayB64(
                        value, 0, value.length,
                        this, 
                        writeKey(
                                fieldNumber, 
                                sink, 
                                repeated ? KEY_SUFFIX_ARRAY_STRING : KEY_SUFFIX_STRING)));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }
    
    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, 
            int offset, int length, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(utf8String)
        {
            if(lastNumber == fieldNumber)
            {
                // repeated field
                tail = sink.writeByte(
                        QUOTE, 
                        this, 
                        writeUTF8Escaped(
                                value, offset, length,
                                sink,
                                this, 
                                sink.writeByteArray(
                                        COMMA_AND_QUOTE, 
                                        this, 
                                        tail)));
                return;
            }
            
            tail = sink.writeByte(
                    QUOTE, 
                    this, 
                    writeUTF8Escaped(
                            value, offset, length,
                            sink,
                            this, 
                            writeKey(
                                    fieldNumber, 
                                    sink, 
                                    repeated ? KEY_SUFFIX_ARRAY_STRING : KEY_SUFFIX_STRING)));
            
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
            return;
        }

        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeByte(
                    QUOTE, 
                    this, 
                    sink.writeByteArrayB64(
                            value, offset, length,
                            this, 
                            sink.writeByteArray(
                                    COMMA_AND_QUOTE, 
                                    this, 
                                    tail)));
            return;
        }
        
        tail = sink.writeByte(
                QUOTE, 
                this, 
                sink.writeByteArrayB64(
                        value, offset, length,
                        this, 
                        writeKey(
                                fieldNumber, 
                                sink, 
                                repeated ? KEY_SUFFIX_ARRAY_STRING : KEY_SUFFIX_STRING)));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeStrFromDouble(
                    value, 
                    this, 
                    sink.writeByte(
                            COMMA, 
                            this, 
                            tail));
            return;
        }
        
        tail = sink.writeStrFromDouble(
                value, 
                this, 
                writeKey(
                        fieldNumber, 
                        sink, 
                        repeated ? KEY_SUFFIX_ARRAY : KEY_SUFFIX));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeStrFromFloat(
                    value, 
                    this, 
                    sink.writeByte(
                            COMMA, 
                            this, 
                            tail));
            return;
        }
        
        tail = sink.writeStrFromFloat(
                value, 
                this, 
                writeKey(
                        fieldNumber, 
                        sink, 
                        repeated ? KEY_SUFFIX_ARRAY : KEY_SUFFIX));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeStrFromInt(
                    value, 
                    this, 
                    sink.writeByte(
                            COMMA, 
                            this, 
                            tail));
            return;
        }
        
        tail = sink.writeStrFromInt(
                value, 
                this, 
                writeKey(
                        fieldNumber, 
                        sink, 
                        repeated ? KEY_SUFFIX_ARRAY : KEY_SUFFIX));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeStrFromLong(
                    value, 
                    this, 
                    sink.writeByte(
                            COMMA, 
                            this, 
                            tail));
            return;
        }
        
        tail = sink.writeStrFromLong(
                value, 
                this, 
                writeKey(
                        fieldNumber, 
                        sink, 
                        repeated ? KEY_SUFFIX_ARRAY : KEY_SUFFIX));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        if(lastNumber == fieldNumber)
        {
            // repeated field
            tail = sink.writeByte(
                    QUOTE, 
                    this, 
                    writeUTF8Escaped(
                            value, 
                            sink,
                            this, 
                            sink.writeByteArray(
                                    COMMA_AND_QUOTE, 
                                    this, 
                                    tail)));
            return;
        }
        
        tail = sink.writeByte(
                QUOTE, 
                this, 
                writeUTF8Escaped(
                        value, 
                        sink,
                        this, 
                        writeKey(
                                fieldNumber, 
                                sink, 
                                repeated ? KEY_SUFFIX_ARRAY_STRING : KEY_SUFFIX_STRING)));
        
        lastNumber = fieldNumber;
        lastRepeated = repeated;
    }

    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value, boolean repeated) 
    throws IOException
    {
        writeObject(fieldNumber, value, value.cachedSchema(), repeated);
    }

    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema, 
            final boolean repeated) throws IOException
    {
        final WriteSink sink = this.sink;
        final Schema<?> lastSchema = this.schema;
        
        if(lastNumber == fieldNumber)
        {
            tail = sink.writeByteArray(
                    COMMA_AND_START_OBJECT, 
                    this, 
                    tail);
        }
        else
        {
            tail = writeKey(
                    fieldNumber, 
                    sink, 
                    repeated ? KEY_SUFFIX_ARRAY_OBJECT : KEY_SUFFIX_OBJECT);
        }
        
        // reset
        this.schema = schema;
        lastNumber = 0;
        lastRepeated = false;

        // recursive write
        schema.writeTo(this, value);
        
        tail = lastRepeated ? sink.writeByteArray(END_ARRAY_AND_END_OBJECT, this, tail) :
            sink.writeByte(END_OBJECT, this, tail);
        
        // restore state
        lastNumber = fieldNumber;
        lastRepeated = repeated;
        this.schema = lastSchema;
    }
    
    private static LinkedBuffer writeUTF8Escaped(final byte[] input, int inStart, 
            int inLen, final WriteSink sink, final WriteSession session, 
            LinkedBuffer lb) throws IOException
    {
        int lastStart = inStart;
        
        for(int i = 0; i < inLen; i++)
        {
            final byte b = input[inStart++];
            if(b > 0x7f)
                continue;
            
            // ascii
            final int escape = sOutputEscapes[b];
            if(escape == 0)
            {
                // nothing to escape
                continue;
            }
            
            if(escape < 0)
            {
                // hex escape
                
                // dump the bytes before this offset.
                final int dumpLen = inStart-lastStart-1;
                if(dumpLen != 0)
                    lb = sink.writeByteArray(input, lastStart, dumpLen, session, lb);
                
                // update
                lastStart = inStart;
                
                if(lb.offset + 6 > lb.buffer.length)
                    lb = sink.drain(session, lb);
                
                final int value = -(escape + 1);

                lb.buffer[lb.offset++] = (byte)'\\';
                lb.buffer[lb.offset++] = (byte)'u';
                lb.buffer[lb.offset++] = (byte)'0';
                lb.buffer[lb.offset++] = (byte)'0';
                lb.buffer[lb.offset++] = HEX_BYTES[value >> 4];
                lb.buffer[lb.offset++] = HEX_BYTES[value & 0x0F];
                
                session.size += 6;
            }
            else
            {
                // dump the bytes before this offset.
                final int dumpLen = inStart-lastStart-1;
                if(dumpLen != 0)
                    lb = sink.writeByteArray(input, lastStart, dumpLen, session, lb);
                
                // update
                lastStart = inStart;
                
                if(lb.offset + 2 > lb.buffer.length)
                    lb = sink.drain(session, lb);
                
                lb.buffer[lb.offset++] = (byte)'\\';
                lb.buffer[lb.offset++] = (byte)escape;
                
                session.size += 2;
            }
        }
        
        final int remaining = inStart - lastStart;
        
        return remaining == 0 ? lb : sink.writeByteArray(input, lastStart, remaining, 
                session, lb);
    }
    
    private static LinkedBuffer writeUTF8Escaped(final String str, final WriteSink sink, 
            final WriteSession session, LinkedBuffer lb) throws IOException
    {
        final int len = str.length();
        if(len == 0)
            return lb;
        
        byte[] buffer = lb.buffer;
        int limit = buffer.length, offset = lb.offset, size = len;
        
        for(int i = 0; i < len; i++)
        {
            final char c = str.charAt(i);
            if(c < 0x0080)
            {
                final int escape = sOutputEscapes[c];
                //System.out.print(c + "|" + escape + " ");
                if(escape == 0)
                {
                    // nothing to escape
                    if(offset == limit)
                    {
                        lb.offset = offset;
                        lb = sink.drain(session, lb);
                        offset = lb.offset;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                    // ascii
                    buffer[offset++] = (byte)c;
                }
                else if(escape < 0)
                {
                    // hex escape
                    if(offset + 6 > limit)
                    {
                        lb.offset = offset;
                        lb = sink.drain(session, lb);
                        offset = lb.offset;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                    
                    final int value = -(escape + 1);

                    buffer[offset++] = (byte)'\\';
                    buffer[offset++] = (byte)'u';
                    buffer[offset++] = (byte)'0';
                    buffer[offset++] = (byte)'0';
                    buffer[offset++] = HEX_BYTES[value >> 4];
                    buffer[offset++] = HEX_BYTES[value & 0x0F];
                    
                    size += 5;
                }
                else
                {
                    if(offset + 2 > limit)
                    {
                        lb.offset = offset;
                        lb = sink.drain(session, lb);
                        offset = lb.offset;
                        buffer = lb.buffer;
                        limit = buffer.length;
                    }
                    
                    buffer[offset++] = (byte)'\\';
                    buffer[offset++] = (byte)escape;
                    
                    size++;
                }
            }
            else if(c < 0x0800)
            {
                if(offset + 2 > limit)
                {
                    lb.offset = offset;
                    lb = sink.drain(session, lb);
                    offset = lb.offset;
                    buffer = lb.buffer;
                    limit = buffer.length;
                }
                
                buffer[offset++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
                size++;
            }
            else
            {
                if(offset + 3 > limit)
                {
                    lb.offset = offset;
                    lb = sink.drain(session, lb);
                    offset = lb.offset;
                    buffer = lb.buffer;
                    limit = buffer.length;
                }
                
                buffer[offset++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                buffer[offset++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                buffer[offset++] = (byte) (0x80 | ((c >>  0) & 0x3F));
                size+=2;
            }
        }
        
        session.size += size;
        lb.offset = offset;
        
        return lb;
    }
    
    static final byte[] HEX_BYTES = new byte[]{
        (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', 
        (byte)'5', (byte)'6', (byte)'7', (byte)'8', (byte)'9', 
        (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F'
    };
    
    // jackson output escaping compabtility
    static final int[] sOutputEscapes;
    static 
    {
        int[] table = new int[128];
        // Control chars need generic escape sequence
        for (int i = 0; i < 32; ++i) {
            table[i] = -(i + 1);
        }
        /* Others (and some within that range too) have explicit shorter
         * sequences
         */
        table['"'] = '"';
        table['\\'] = '\\';
        // Escaping of slash is optional, so let's not add it
        table[0x08] = 'b';
        table[0x09] = 't';
        table[0x0C] = 'f';
        table[0x0A] = 'n';
        table[0x0D] = 'r';
        sOutputEscapes = table;
    }

}
