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

import static com.dyuproject.protostuff.NumberParser.parseInt;
import static com.dyuproject.protostuff.NumberParser.parseLong;

import java.io.IOException;

import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * An input for deserializing kvp-encoded messages.
 * A kvp encoding is a binary encoding w/c contains a key-value sequence.
 * On the wire, a serialized field (key-value) would look like:
 * [key-len][key][value-len][value]
 * 
 * The keys and values are length-delimited (uint16 little endian).
 * 
 * Note that this encoding does not support nested messages.
 * This encoding is mostly useful for headers w/c contain information about 
 * the content it carries (see http://projects.unbit.it/uwsgi/wiki/uwsgiProtocol).
 *
 * @author David Yu
 * @created Nov 19, 2010
 */
public final class KvpByteArrayInput implements Input
{
    
    final byte[] buffer;
    int offset, limit;
    
    final boolean numeric;
    
    public KvpByteArrayInput(byte[] buffer, int offset, int len, boolean numeric)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.limit = offset + len;
        
        this.numeric = numeric;
    }

    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        if(offset == limit)
            return 0;
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        final int number = numeric ? parseInt(buffer, offset, size, 10, true) :
            schema.getFieldNumber(STRING.deser(buffer, offset, size));
        
        offset += size;
        
        if(number == 0)
        {
            // skip unknown fields.
            handleUnknownField(number, schema);
            return readFieldNumber(schema);
        }

        return number;
    }

    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        offset += size;
    }

    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public boolean readBool() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        
        if(size != 1)
            throw new ProtostuffException("Not a valid kvp boolean");
        
        return buffer[offset++] != 0x30;
    }

    public byte[] readByteArray() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if(size == 0)
            return ByteString.EMPTY_BYTE_ARRAY;
        
        if(offset + size > limit)
            throw new ProtostuffException("Misreported size.");
        
        byte[] data = new byte[size];
        System.arraycopy(buffer, offset, data, 0, size);
        offset += size;
        return data;
    }

    public ByteString readBytes() throws IOException
    {
        return ByteString.wrap(readByteArray());
    }

    public double readDouble() throws IOException
    {
        // TODO efficiency
        
        return Double.parseDouble(readString());
    }
    
    public float readFloat() throws IOException
    {
        // TODO efficiency
        
        return Float.parseFloat(readString());
    }
    
    public int readUInt32() throws IOException
    {
        return readInt32();
    }

    public long readUInt64() throws IOException
    {
        return readInt64();
    }

    public int readInt32() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if(size == 0)
            return 0;
        
        if(offset + size > limit)
            throw new ProtostuffException("Misreported size.");
        
        final int number = parseInt(buffer, offset, size, 10);
        offset += size;
        return number;
    }

    public long readInt64() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if(size == 0)
            return 0;
        
        if(offset + size > limit)
            throw new ProtostuffException("Misreported size.");
        
        final long number = parseLong(buffer, offset, size, 10);
        offset += size;
        return number;
    }

    public int readEnum() throws IOException
    {
        return readInt32();
    }

    public int readFixed32() throws IOException
    {
        return readUInt32();
    }

    public long readFixed64() throws IOException
    {
        return readUInt64();
    }

    public int readSFixed32() throws IOException
    {
        return readInt32();
    }

    public long readSFixed64() throws IOException
    {
        return readInt64();
    }

    public int readSInt32() throws IOException
    {
        return readInt32();
    }

    public long readSInt64() throws IOException
    {
        return readInt64();
    }

    public String readString() throws IOException
    {
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if(size == 0)
            return ByteString.EMPTY_STRING;
        
        if(offset + size > limit)
            throw new ProtostuffException("Misreported size.");
        
        final String str = STRING.deser(buffer, offset, size);
        offset += size;
        return str;
    }

    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber, 
            boolean repeated) throws IOException
    {
        throw new UnsupportedOperationException();
    }

}
