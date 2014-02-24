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

package com.dyuproject.protostuff.me;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Dual output for streaming or full buffering.
 *
 * @author David Yu
 * @created Sep 19, 2010
 */
public final class ProtostuffOutput extends WriteSession implements Output {

    public ProtostuffOutput(LinkedBuffer buffer) {
        super(buffer);
    }

    public ProtostuffOutput(LinkedBuffer buffer, OutputStream out) {
        super(buffer, out);
    }

    /**
     * Resets this output for re-use.
     */
    public WriteSession clear() {
        super.clear();
        return this;
    }

    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException {
        if (value < 0) {
            tail = sink.writeVarInt64(
                    value,
                    this,
                    sink.writeVarInt32(
                            WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                            this,
                            tail));
        } else {
            tail = sink.writeVarInt32(
                    value,
                    this,
                    sink.writeVarInt32(
                            WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                            this,
                            tail));
        }
        
        
        /*if(value < 0)
        {
            tail = writeTagAndRawVarInt64(
                    WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                    value, 
                    this, 
                    tail);
        }
        else
        {
            tail = writeTagAndRawVarInt32(
                    WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                    value, 
                    this, 
                    tail);
        }*/
    }

    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException {
        tail = sink.writeVarInt32(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                        this,
                        tail));
        
        /*tail = writeTagAndRawVarInt32(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                this, 
                tail);*/
    }

    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException {
        tail = sink.writeVarInt32(
                ProtobufOutput.encodeZigZag32(value),
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                        this,
                        tail));
        
        /*tail = writeTagAndRawVarInt32(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                encodeZigZag32(value), 
                this, 
                tail);*/
    }

    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException {
        tail = sink.writeInt32LE(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32),
                        this,
                        tail));
        
        /*tail = writeTagAndRawLittleEndian32(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                this, 
                tail);*/
    }

    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException {
        tail = sink.writeInt32LE(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32),
                        this,
                        tail));
        
        /*tail = writeTagAndRawLittleEndian32(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED32), 
                value, 
                this, 
                tail);*/
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException {
        tail = sink.writeVarInt64(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                        this,
                        tail));
        
        /*tail = writeTagAndRawVarInt64(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                this, 
                tail);*/
    }

    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException {
        tail = sink.writeVarInt64(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                        this,
                        tail));
        
        /*tail = writeTagAndRawVarInt64(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value, 
                this, 
                tail);*/
    }

    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException {
        tail = sink.writeVarInt64(
                ProtobufOutput.encodeZigZag64(value),
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                        this,
                        tail));
        
        /*tail = writeTagAndRawVarInt64(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                encodeZigZag64(value), 
                this, 
                tail);*/
    }

    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException {
        tail = sink.writeInt64LE(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64),
                        this,
                        tail));
        
        /*tail = writeTagAndRawLittleEndian64(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                this, 
                tail);*/
    }

    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException {
        tail = sink.writeInt64LE(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64),
                        this,
                        tail));
        
        /*tail = writeTagAndRawLittleEndian64(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED64), 
                value, 
                this, 
                tail);*/
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException {
        tail = sink.writeInt32LE(
                Float.floatToIntBits(value),
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32),
                        this,
                        tail));
        
        /*tail = writeTagAndRawLittleEndian32(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED32), 
                Float.floatToRawIntBits(value), 
                this, 
                tail);*/
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException {
        tail = sink.writeInt64LE(
                Double.doubleToLongBits(value),
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64),
                        this,
                        tail));
        
        /*tail = writeTagAndRawLittleEndian64(
                WireFormat.makeTag(fieldNumber, WIRETYPE_FIXED64), 
                Double.doubleToRawLongBits(value), 
                this, 
                tail);*/
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException {
        tail = sink.writeByte(
                value ? (byte) 0x01 : 0x00,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT),
                        this,
                        tail));
        
        /*tail = writeTagAndRawVarInt32(
                WireFormat.makeTag(fieldNumber, WIRETYPE_VARINT), 
                value ? 1 : 0, 
                this, 
                tail);*/
    }

    public void writeEnum(int fieldNumber, int number, boolean repeated) throws IOException {
        writeInt32(fieldNumber, number, repeated);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException {
        tail = sink.writeStrUTF8VarDelimited(
                value,
                this,
                sink.writeVarInt32(
                        WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED),
                        this,
                        tail));
        
        /*tail = writeUTF8VarDelimited(
                value,
                this, 
                writeRawVarInt32(WireFormat.makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), this, tail));*/
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    public void writeByteArray(int fieldNumber, byte[] bytes, boolean repeated) throws IOException {
        tail = sink.writeByteArray(
                bytes, 0, bytes.length,
                this,
                sink.writeVarInt32(
                        bytes.length,
                        this,
                        sink.writeVarInt32(
                                WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED),
                                this,
                                tail)));
        
        /*tail = writeTagAndByteArray(
                WireFormat.makeTag(fieldNumber, WIRETYPE_LENGTH_DELIMITED), 
                bytes, 
                this, 
                tail);*/
    }

    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value,
                               int offset, int length, boolean repeated) throws IOException {
        tail = sink.writeByteArray(
                value, offset, length,
                this,
                sink.writeVarInt32(
                        length,
                        this,
                        sink.writeVarInt32(
                                WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED),
                                this,
                                tail)));
    }

    public void writeObject(final int fieldNumber, final Object value, final Schema schema,
                            final boolean repeated) throws IOException {
        tail = sink.writeVarInt32(
                WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_START_GROUP),
                this,
                tail);

        schema.writeTo(this, value);

        tail = sink.writeVarInt32(
                WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_END_GROUP),
                this,
                tail);
    }

}
