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

package com.dyuproject.protostuff;

import java.io.IOException;

/**
 * A FilterOutput contains some other {@link Output output}, which it uses as its basic 
 * sink of data.
 *
 * @author David Yu
 * @created Nov 11, 2009
 */
public class FilterOutput implements Output
{
    
    protected final Output output;
    
    public FilterOutput(Output output)
    {
        this.output = output;
    }

    public void writeBool(int fieldNumber, boolean value) throws IOException
    {
        output.writeBool(fieldNumber, value);
    }

    public void writeByteArray(int fieldNumber, byte[] value) throws IOException
    {
        output.writeByteArray(fieldNumber, value);
    }

    public void writeBytes(int fieldNumber, ByteString value) throws IOException
    {
        output.writeBytes(fieldNumber, value);
    }

    public void writeDouble(int fieldNumber, double value) throws IOException
    {
        output.writeDouble(fieldNumber, value);
    }

    public void writeEnum(int fieldNumber, int value) throws IOException
    {
        output.writeEnum(fieldNumber, value);
    }

    public void writeFixed32(int fieldNumber, int value) throws IOException
    {
        output.writeFixed32(fieldNumber, value);
    }

    public void writeFixed64(int fieldNumber, long value) throws IOException
    {
        output.writeFixed64(fieldNumber, value);
    }

    public void writeFloat(int fieldNumber, float value) throws IOException
    {
        output.writeFloat(fieldNumber, value);
    }

    public void writeInt32(int fieldNumber, int value) throws IOException
    {
        output.writeInt32(fieldNumber, value);
    }

    public void writeInt64(int fieldNumber, long value) throws IOException
    {
        output.writeInt64(fieldNumber, value);
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value) throws IOException
    {
        output.writeMessage(fieldNumber, value);
    }
    
    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema) throws IOException
    {
        output.writeObject(fieldNumber, value, schema);
    }

    public <T> void writeObject(int fieldNumber, T value, Class<T> typeClass) throws IOException
    {
        output.writeObject(fieldNumber, value, typeClass);
    }

    public void writeSFixed32(int fieldNumber, int value) throws IOException
    {
        output.writeSFixed32(fieldNumber, value);
    }

    public void writeSFixed64(int fieldNumber, long value) throws IOException
    {
        output.writeSFixed64(fieldNumber, value);
    }

    public void writeSInt32(int fieldNumber, int value) throws IOException
    {
        output.writeSInt32(fieldNumber, value);
    }

    public void writeSInt64(int fieldNumber, long value) throws IOException
    {
        output.writeSInt64(fieldNumber, value);
    }

    public void writeString(int fieldNumber, String value) throws IOException
    {
        output.writeString(fieldNumber, value);
    }

    public void writeUInt32(int fieldNumber, int value) throws IOException
    {
        output.writeUInt32(fieldNumber, value);
    }

    public void writeUInt64(int fieldNumber, long value) throws IOException
    {
        output.writeUInt64(fieldNumber, value);
    }

}
