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
 */
public interface Input
{

    /**
     * The underlying implementation should handle the unknown field.
     *
     * @param fieldNumber the field number
     * @param schema the message schema
     * @param <T> the message type
     * @throws IOException when I/O operation fails
     */
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException;

    /**
     * Reads the field number of a message/object tied to the given {@link Schema schema}.
     *
     * @param schema the given message schema
     * @param <T> the message type
     * @return the field number
     * @throws IOException when I/O operation fails
     */
    public <T> int readFieldNumber(Schema<T> schema) throws IOException;

    /**
     * Reads a variable int field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public int readInt32() throws IOException;

    /**
     * Reads an unsigned int field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public int readUInt32() throws IOException;

    /**
     * Reads a signed int field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public int readSInt32() throws IOException;

    /**
     * Reads a fixed int(4 bytes) field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public int readFixed32() throws IOException;

    /**
     * Reads a signed+fixed int(4 bytes) field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public int readSFixed32() throws IOException;

    /**
     * Reads a variable long field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public long readInt64() throws IOException;

    /**
     * Reads an unsigned long field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public long readUInt64() throws IOException;

    /**
     * Reads a signed long field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public long readSInt64() throws IOException;

    /**
     * Reads a fixed long(8 bytes) field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public long readFixed64() throws IOException;

    /**
     * Reads a signed+fixed long(8 bytes) field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public long readSFixed64() throws IOException;

    /**
     * Reads a float field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public float readFloat() throws IOException;

    /**
     * Reads a double field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public double readDouble() throws IOException;

    /**
     * Reads a boolean field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public boolean readBool() throws IOException;

    /**
     * Reads an enum(its number) field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public int readEnum() throws IOException;

    /**
     * Reads a {@link String} field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public String readString() throws IOException;

    /**
     * Reads a {@link ByteString} field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public ByteString readBytes() throws IOException;

    /**
     * Reads a byte array field value.
     * @return the field value
     * @throws IOException when I/O operation fails
     */
    public byte[] readByteArray() throws IOException;

    public ByteBuffer readByteBuffer() throws IOException;

    /**
     * Merges an object (with schema) field value. The provided {@link Schema schema} handles the deserialization for the
     * object.
     *
     * @param value optional object instance to merge data
     * @param schema the message schema
     * @param <T> the message type
     * @return given object instance or newly instantiated instance with merged data
     * @throws IOException when I/O operation fails
     */
    public <T> T mergeObject(T value, Schema<T> schema) throws IOException;

    /**
     * Transfer the byte range to the output. Capable of zero-copy transfer depending on the type of input.
     *
     * @param output the output
     * @param utf8String true if field is an UTF-8 encoded string
     * @param fieldNumber the field number
     * @param repeated true if field is a collection
     * @throws IOException when I/O operation fails
     */
    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber,
            boolean repeated) throws IOException;

}
