//========================================================================
//Copyright (C) 2016 Alex Shvid
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

import static org.msgpack.core.MessagePack.Code.ARRAY16;
import static org.msgpack.core.MessagePack.Code.ARRAY32;
import static org.msgpack.core.MessagePack.Code.BIN16;
import static org.msgpack.core.MessagePack.Code.BIN32;
import static org.msgpack.core.MessagePack.Code.BIN8;
import static org.msgpack.core.MessagePack.Code.FALSE;
import static org.msgpack.core.MessagePack.Code.FIXARRAY_PREFIX;
import static org.msgpack.core.MessagePack.Code.FIXMAP_PREFIX;
import static org.msgpack.core.MessagePack.Code.FIXSTR_PREFIX;
import static org.msgpack.core.MessagePack.Code.FLOAT32;
import static org.msgpack.core.MessagePack.Code.FLOAT64;
import static org.msgpack.core.MessagePack.Code.INT16;
import static org.msgpack.core.MessagePack.Code.INT32;
import static org.msgpack.core.MessagePack.Code.INT64;
import static org.msgpack.core.MessagePack.Code.INT8;
import static org.msgpack.core.MessagePack.Code.MAP16;
import static org.msgpack.core.MessagePack.Code.MAP32;
import static org.msgpack.core.MessagePack.Code.STR16;
import static org.msgpack.core.MessagePack.Code.STR32;
import static org.msgpack.core.MessagePack.Code.STR8;
import static org.msgpack.core.MessagePack.Code.TRUE;
import static org.msgpack.core.MessagePack.Code.UINT16;
import static org.msgpack.core.MessagePack.Code.UINT32;
import static org.msgpack.core.MessagePack.Code.UINT64;
import static org.msgpack.core.MessagePack.Code.UINT8;

import java.io.IOException;

import org.msgpack.core.MessagePack;

/**
 * Simple wrapper around WriteSink with methods specific for MessagePacker
 * 
 * @author Alex Shvid
 *
 */

public final class MsgpackWriteSink
{
    private final WriteSink sink;
    private final boolean str8FormatSupport;

    public MsgpackWriteSink(WriteSink sink, MessagePack.PackerConfig config)
    {
        this(sink, config.isStr8FormatSupport());
    }

    public MsgpackWriteSink(WriteSink sink, boolean str8FormatSupport)
    {
        this.sink = sink;
        this.str8FormatSupport = str8FormatSupport;
    }

    private LinkedBuffer writeByteAndByte(byte b, byte v, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return sink.writeByte(v, session,
                sink.writeByte(b, session, lb));
    }

    private LinkedBuffer writeByteAndShort(byte b, short v, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return sink.writeInt16(v, session,
                sink.writeByte(b, session, lb));
    }

    private LinkedBuffer writeByteAndInt(byte b, int v, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return sink.writeInt32(v, session,
                sink.writeByte(b, session, lb));
    }

    private LinkedBuffer writeByteAndLong(byte b, long v, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return sink.writeInt64(v, session,
                sink.writeByte(b, session, lb));
    }

    public LinkedBuffer packInt(final int r,
            WriteSession session, LinkedBuffer lb) throws IOException
    {
        if (r < -(1 << 5))
        {
            if (r < -(1 << 15))
            {
                return writeByteAndInt(INT32, r, session, lb);
            }
            else if (r < -(1 << 7))
            {
                return writeByteAndShort(INT16, (short) r, session, lb);
            }
            else
            {
                return writeByteAndByte(INT8, (byte) r, session, lb);
            }
        }
        else if (r < (1 << 7))
        {
            return sink.writeByte((byte) r, session, lb);
        }
        else
        {
            if (r < (1 << 8))
            {
                return writeByteAndByte(UINT8, (byte) r, session, lb);
            }
            else if (r < (1 << 16))
            {
                return writeByteAndShort(UINT16, (short) r, session, lb);
            }
            else
            {
                // unsigned 32
                return writeByteAndInt(UINT32, r, session, lb);
            }
        }
    }

    public LinkedBuffer packLong(long v, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        if (v < -(1L << 5))
        {
            if (v < -(1L << 15))
            {
                if (v < -(1L << 31))
                {
                    return writeByteAndLong(INT64, v, session, lb);
                }
                else
                {
                    return writeByteAndInt(INT32, (int) v, session, lb);
                }
            }
            else
            {
                if (v < -(1 << 7))
                {
                    return writeByteAndShort(INT16, (short) v, session, lb);
                }
                else
                {
                    return writeByteAndByte(INT8, (byte) v, session, lb);
                }
            }
        }
        else if (v < (1 << 7))
        {
            // fixnum
            return sink.writeByte((byte) v, session, lb);
        }
        else
        {
            if (v < (1L << 16))
            {
                if (v < (1 << 8))
                {
                    return writeByteAndByte(UINT8, (byte) v, session, lb);
                }
                else
                {
                    return writeByteAndShort(UINT16, (short) v, session, lb);
                }
            }
            else
            {
                if (v < (1L << 32))
                {
                    return writeByteAndInt(UINT32, (int) v, session, lb);
                }
                else
                {
                    return writeByteAndLong(UINT64, v, session, lb);
                }
            }
        }
    }

    public LinkedBuffer packFloat(float v, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return writeByteAndInt(FLOAT32, Float.floatToRawIntBits(v), session, lb);
    }

    public LinkedBuffer packDouble(double v, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return writeByteAndLong(FLOAT64, Double.doubleToRawLongBits(v), session, lb);
    }

    public LinkedBuffer packBoolean(boolean b, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return sink.writeByte(b ? TRUE : FALSE, session, lb);
    }

    public LinkedBuffer packBinaryHeader(int len, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        if (len < (1 << 8))
        {
            return writeByteAndByte(BIN8, (byte) len, session, lb);
        }
        else if (len < (1 << 16))
        {
            return writeByteAndShort(BIN16, (short) len, session, lb);
        }
        else
        {
            return writeByteAndInt(BIN32, len, session, lb);
        }
    }

    public LinkedBuffer packRawStringHeader(int len, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        if (len < (1 << 5))
        {
            return sink.writeByte((byte) (FIXSTR_PREFIX | len), session, lb);
        }
        else if (str8FormatSupport && len < (1 << 8))
        {
            return writeByteAndByte(STR8, (byte) len, session, lb);
        }
        else if (len < (1 << 16))
        {
            return writeByteAndShort(STR16, (short) len, session, lb);
        }
        else
        {
            return writeByteAndInt(STR32, len, session, lb);
        }
    }

    public LinkedBuffer packString(String value, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        if (value.length() == 0)
        {
            return packRawStringHeader(0, session, lb);
        }
        int sizeInBytes = StringSerializer.computeUTF8Size(value, 0, value.length());
        return StringSerializer.writeUTF8(value, session, 
                packRawStringHeader(sizeInBytes, session, lb));
    }

    public LinkedBuffer packString(StringBuilder value, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        if (value.length() == 0)
        {
            return packRawStringHeader(0, session, lb);
        }
        int sizeInBytes = StringSerializer.computeUTF8Size(value, 0, value.length());
        return StringSerializer.writeUTF8(value, session,
                packRawStringHeader(sizeInBytes, session, lb));
    }

    public LinkedBuffer packBytes(byte[] src, int offset, int length, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        return sink.writeByteArray(src, offset, length, session, lb);
    }

    public LinkedBuffer packArrayHeader(int arraySize, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        if (arraySize < 0)
        {
            throw new IllegalArgumentException("array size must be >= 0");
        }

        if (arraySize < (1 << 4))
        {
            return sink.writeByte((byte) (FIXARRAY_PREFIX | arraySize), session, lb);
        }
        else if (arraySize < (1 << 16))
        {
            return writeByteAndShort(ARRAY16, (short) arraySize, session, lb);
        }
        else
        {
            return writeByteAndInt(ARRAY32, arraySize, session, lb);
        }
    }

    public LinkedBuffer packMapHeader(int mapSize, WriteSession session, LinkedBuffer lb)
            throws IOException
    {
        if (mapSize < 0)
        {
            throw new IllegalArgumentException("map size must be >= 0");
        }

        if (mapSize < (1 << 4))
        {
            return sink.writeByte((byte) (FIXMAP_PREFIX | mapSize), session, lb);
        }
        else if (mapSize < (1 << 16))
        {
            return writeByteAndShort(MAP16, (short) mapSize, session, lb);
        }
        else
        {
            return writeByteAndInt(MAP32, mapSize, session, lb);
        }
    }

}
