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

package io.protostuff;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * An Input lets an application read primitive data types and objects from a source of data.
 * 
 * @author David Yu
 * @created Nov 9, 2009
 */
public interface Input
{

    /**
     * The underlying implementation should handle the unknown field.
     */
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException;

    /**
     * Reads the field number of a message/object tied to the given {@link Schema schema}.
     */
    public <T> int readFieldNumber(Schema<T> schema) throws IOException;

    /**
     * Reads a variable int field value.
     */
    public int readInt32() throws IOException;

    /**
     * Reads an unsigned int field value.
     */
    public int readUInt32() throws IOException;

    /**
     * Reads a signed int field value.
     */
    public int readSInt32() throws IOException;

    /**
     * Reads a fixed int(4 bytes) field value.
     */
    public int readFixed32() throws IOException;

    /**
     * Reads a signed+fixed int(4 bytes) field value.
     */
    public int readSFixed32() throws IOException;

    /**
     * Reads a variable long field value.
     */
    public long readInt64() throws IOException;

    /**
     * Reads an unsigned long field value.
     */
    public long readUInt64() throws IOException;

    /**
     * Reads a signed long field value.
     */
    public long readSInt64() throws IOException;

    /**
     * Reads a fixed long(8 bytes) field value.
     */
    public long readFixed64() throws IOException;

    /**
     * Reads a signed+fixed long(8 bytes) field value.
     */
    public long readSFixed64() throws IOException;

    /**
     * Reads a float field value.
     */
    public float readFloat() throws IOException;

    /**
     * Reads a double field value.
     */
    public double readDouble() throws IOException;

    /**
     * Reads a boolean field value.
     */
    public boolean readBool() throws IOException;

    /**
     * Reads an enum(its number) field value.
     */
    public int readEnum() throws IOException;

    /**
     * Reads a {@link String} field value.
     */
    public String readString() throws IOException;

    /**
     * Reads a {@link ByteString} field value.
     */
    public ByteString readBytes() throws IOException;

    /**
     * Reads a byte array field value.
     */
    public byte[] readByteArray() throws IOException;

    public ByteBuffer readByteBuffer() throws IOException;

    /**
     * Merges an object(with schema) field value. The provided {@link Schema schema} handles the deserialization for the
     * object.
     */
    public <T> T mergeObject(T value, Schema<T> schema) throws IOException;

    /**
     * Transfer the byte range to the output. Capable of zero-copy transfer depending on the type of input.
     */
    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber,
            boolean repeated) throws IOException;

}
