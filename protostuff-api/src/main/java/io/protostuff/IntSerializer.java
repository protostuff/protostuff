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

import java.nio.ByteBuffer;

/**
 * Int32/Int64 serialization
 * 
 * @author David Yu
 * @created Sep 20, 2010
 */
public final class IntSerializer
{

    private IntSerializer()
    {
    }

    /**
     * Writes the 16-bit int into the buffer starting with the most significant byte.
     */
    public static void writeInt16(final int value, final byte[] buffer, int offset)
    {
        buffer[offset++] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset] = (byte) value;
    }

    /**
     * Writes the 16-bit int into the buffer starting with the least significant byte.
     */
    public static void writeInt16LE(final int value, final byte[] buffer, int offset)
    {
        buffer[offset++] = (byte) value;
        buffer[offset] = (byte) ((value >>> 8) & 0xFF);
    }

    public static void writeInt16LE(final int value, ByteBuffer buffer)
    {
        buffer.put((byte) value);
        buffer.put((byte) ((value >>> 8) & 0xFF));
    }

    /**
     * Writes the 32-bit int into the buffer starting with the most significant byte.
     */
    public static void writeInt32(final int value, final byte[] buffer, int offset)
    {
        buffer[offset++] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset++] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset++] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset] = (byte) ((value >>> 0) & 0xFF);
    }

    /**
     * Writes the 32-bit int into the buffer starting with the least significant byte.
     */
    public static void writeInt32LE(final int value, final byte[] buffer, int offset)
    {
        buffer[offset++] = (byte) ((value >>> 0) & 0xFF);
        buffer[offset++] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset++] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset] = (byte) ((value >>> 24) & 0xFF);
    }

    public static void writeInt32LE(final int value, ByteBuffer buffer)
    {
        buffer.put((byte) ((value >>> 0) & 0xFF));
        buffer.put((byte) ((value >>> 8) & 0xFF));
        buffer.put((byte) ((value >>> 16) & 0xFF));
        buffer.put((byte) ((value >>> 24) & 0xFF));
    }

    /**
     * Writes the 64-bit int into the buffer starting with the most significant byte.
     */
    public static void writeInt64(final long value, final byte[] buffer, int offset)
    {
        buffer[offset++] = (byte) (value >>> 56);
        buffer[offset++] = (byte) (value >>> 48);
        buffer[offset++] = (byte) (value >>> 40);
        buffer[offset++] = (byte) (value >>> 32);
        buffer[offset++] = (byte) (value >>> 24);
        buffer[offset++] = (byte) (value >>> 16);
        buffer[offset++] = (byte) (value >>> 8);
        buffer[offset] = (byte) (value >>> 0);
    }

    /**
     * Writes the 64-bit int into the buffer starting with the least significant byte.
     */
    public static void writeInt64LE(final long value, final byte[] buffer, int offset)
    {
        buffer[offset++] = (byte) (value >>> 0);
        buffer[offset++] = (byte) (value >>> 8);
        buffer[offset++] = (byte) (value >>> 16);
        buffer[offset++] = (byte) (value >>> 24);
        buffer[offset++] = (byte) (value >>> 32);
        buffer[offset++] = (byte) (value >>> 40);
        buffer[offset++] = (byte) (value >>> 48);
        buffer[offset] = (byte) (value >>> 56);
    }

    public static void writeInt64LE(final long value, ByteBuffer buffer)
    {
        buffer.put((byte) (value >>> 0));
        buffer.put((byte) (value >>> 8));
        buffer.put((byte) (value >>> 16));
        buffer.put((byte) (value >>> 24));
        buffer.put((byte) (value >>> 32));
        buffer.put((byte) (value >>> 40));
        buffer.put((byte) (value >>> 48));
        buffer.put((byte) (value >>> 56));
    }

}
