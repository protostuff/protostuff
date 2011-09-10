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
import java.io.InputStream;

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
 * @created Nov 30, 2010
 */
public final class KvpInput implements Input
{
    
    static final int DEFAULT_BUFFER_SIZE = 
        Integer.getInteger("kvpinput.default_buffer_size", 1024);
    
    static final int MAX_VALUE_SIZE = 
        Integer.getInteger("kvpinput.max_value_size", 8192);
    
    final InputStream in;
    final byte[] buffer;
    final boolean numeric;
    int offset, limit;
    
    public KvpInput(InputStream in, boolean numeric)
    {
        this(in, new byte[DEFAULT_BUFFER_SIZE], numeric);
    }
    
    public KvpInput(InputStream in, byte[] buffer, boolean numeric)
    {
        this(in, buffer, 0, 0, numeric);
    }
    
    public KvpInput(InputStream in, byte[] buffer, int offset, int limit, 
            boolean numeric)
    {
        this.in = in;
        
        this.buffer = buffer;
        this.offset = offset;
        this.limit = limit;
        
        this.numeric = numeric;
    }
    
    /**
     * Returns true if there are {@code minimum} bytes available for reading.
     * 
     * The caller is responsible that the arg {@code minimum} is not larger than 
     * the buffer size.
     */
    private boolean readable(final int minimum) throws IOException 
    {
        int existing = limit - offset;
        final int available = buffer.length - limit;
        
        if(minimum > available + existing)
        {
            // move to front.
            System.arraycopy(buffer, offset, buffer, 0, existing);
            offset = 0;
            limit = existing;
            
            int read;
            do
            {
                read = in.read(buffer, limit, buffer.length - limit);
                if(read == -1)
                    return false;
                
                limit += read;
                existing += read;
            }
            while(existing < minimum);
            
            return true;
        }
        
        int read;
        do
        {
            read = in.read(buffer, limit, buffer.length - limit);
            if(read == -1)
                return false;
            
            limit += read;
            existing += read;
        }
        while(existing < minimum);
        
        return true;
    }
    
    
    private byte[] fill(final byte[] data, int dataOffset, final int len) 
    throws IOException
    {
        final int existing = limit - offset;
        int toRead = len - existing, read = 0;
        if(existing != 0)
        {
            // copy existing
            System.arraycopy(buffer, offset, data, dataOffset, existing);
            dataOffset += existing;
        }
        // reset
        offset = 0;
        limit = 0;
        
        do
        {
            read = in.read(data, dataOffset, toRead);
            if(read == -1)
                throw new ProtostuffException("Truncated message.");
            
            dataOffset += read;
            toRead -= read;
        }
        while(toRead > 0);
        
        return data;
    }

    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        if(offset + 2 > limit && !readable(2))
        {
            if(offset != limit)
                throw new ProtostuffException("Truncated message.");
            
            return 0;
        }
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        
        if(offset + size > limit && !readable(size))
            throw new ProtostuffException("Truncated message.");
        
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
        if(offset + 2 > limit && !readable(2))
            throw new ProtostuffException("Truncated message.");
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        if(offset + size > limit)
        {
            int toRead = size - (limit - offset), read = 0;
            // read until toRead 
            do
            {
                read = in.read(buffer, 0, buffer.length);
                if(read == -1)
                    throw new ProtostuffException("Truncated message.");
                
                toRead -= read;
            }
            while(toRead > 0);
            
            offset = read - (-toRead);
            limit = read;
            
            return;
        }
        
        offset += size;
    }

    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {
        throw new ProtostuffException("Unsupported.");
    }

    public boolean readBool() throws IOException
    {
        if(offset + 3 > limit && !readable(3))
            throw new ProtostuffException("Truncated message.");
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        
        if(size != 1)
            throw new ProtostuffException("Invalid kvp boolean");
        
        return buffer[offset++] != 0x30;
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
        if(offset + 2 > limit && !readable(2))
            throw new ProtostuffException("Truncated message.");
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        
        if(size == 0)
            return 0;
        
        if(offset + size > limit && !readable(size))
            throw new ProtostuffException("Truncated message.");
        
        final int number = parseInt(buffer, offset, size, 10);
        
        offset += size;
        
        return number;
    }

    public long readInt64() throws IOException
    {
        if(offset + 2 > limit && !readable(2))
            throw new ProtostuffException("Truncated message.");
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        
        if(size == 0)
            return 0;
        
        if(offset + size > limit && !readable(size))
            throw new ProtostuffException("Truncated message.");
        
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

    public byte[] readByteArray() throws IOException
    {
        if(offset + 2 > limit && !readable(2))
            throw new ProtostuffException("Truncated message.");
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        
        if(size == 0)
            return ByteString.EMPTY_BYTE_ARRAY;
        
        if(size > MAX_VALUE_SIZE)
            throw new ProtostuffException("Exceeded kvp max value size.");

        if(offset + size > limit)
            return fill(new byte[size], 0, size);
        
        final byte[] data = new byte[size];
        System.arraycopy(buffer, offset, data, 0, size);
        offset += size;
        return data;
    }
    
    public String readString() throws IOException
    {
        if(offset + 2 > limit && !readable(2))
            throw new ProtostuffException("Truncated message.");
        
        final int size = buffer[offset++] | (buffer[offset++] << 8);
        
        if(size == 0)
            return ByteString.EMPTY_STRING;
        
        if(size > MAX_VALUE_SIZE)
            throw new ProtostuffException("Exceeded kvp max value size.");

        if(offset + size > limit)
        {
            if(size > buffer.length)
            {
                // need to create a copy.
                return STRING.deser(fill(new byte[size], 0, size));
            }
            
            // it can fit in the buffer.
            if(!readable(size))
                throw new ProtostuffException("Truncated Message.");
            
            //final String str = STRING.deser(buffer, offset, size);
            //offset += size;
            //return str;
        }
        
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
