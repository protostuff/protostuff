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

import java.io.IOException;

/**
 * The flexible output for outputs that use {@link WriteSession}.
 * 
 * @author David Yu
 * @created Sep 20, 2010
 */
public enum WriteSink
{
    BUFFERED
    {
        @Override
        public LinkedBuffer drain(final WriteSession session,
                final LinkedBuffer lb) throws IOException
        {
            // grow
            return new LinkedBuffer(session.nextBufferSize, lb);
        }

        @Override
        public LinkedBuffer writeByteArrayB64(final byte[] value,
                final int offset, final int valueLen,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return B64Code.encode(value, offset, valueLen, session, lb);
        }

        @Override
        public LinkedBuffer writeByteArray(final byte[] value,
                final int offset, final int valueLen,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            if (valueLen == 0)
                return lb;

            session.size += valueLen;

            final int available = lb.buffer.length - lb.offset;
            if (valueLen > available)
            {
                if (available + session.nextBufferSize < valueLen)
                {
                    // too large ... so we wrap and insert (zero-copy)
                    if (available == 0)
                    {
                        // buffer was actually full ... return a fresh buffer
                        return new LinkedBuffer(session.nextBufferSize,
                                new LinkedBuffer(value, offset, offset + valueLen, lb));
                    }

                    // continue with the existing byte array of the previous buffer
                    return new LinkedBuffer(lb,
                            new LinkedBuffer(value, offset, offset + valueLen, lb));
                }

                // copy what can fit
                System.arraycopy(value, offset, lb.buffer, lb.offset, available);

                lb.offset += available;

                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);

                final int leftover = valueLen - available;

                // copy what's left
                System.arraycopy(value, offset + available, lb.buffer, 0, leftover);

                lb.offset += leftover;

                return lb;
            }

            // it fits
            System.arraycopy(value, offset, lb.buffer, lb.offset, valueLen);

            lb.offset += valueLen;

            return lb;
        }

        @Override
        public LinkedBuffer writeByte(final byte value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size++;

            if (lb.offset == lb.buffer.length)
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }
            lb.buffer[lb.offset++] = value;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt16(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 2;

            if (lb.offset + 2 > lb.buffer.length)
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }

            IntSerializer.writeInt16(value, lb.buffer, lb.offset);
            lb.offset += 2;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt16LE(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 2;

            if (lb.offset + 2 > lb.buffer.length)
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }

            IntSerializer.writeInt16LE(value, lb.buffer, lb.offset);
            lb.offset += 2;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt32(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 4;

            if (lb.offset + 4 > lb.buffer.length)
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }

            IntSerializer.writeInt32(value, lb.buffer, lb.offset);
            lb.offset += 4;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt64(final long value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 8;

            if (lb.offset + 8 > lb.buffer.length)
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }

            IntSerializer.writeInt64(value, lb.buffer, lb.offset);
            lb.offset += 8;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt32LE(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 4;

            if (lb.offset + 4 > lb.buffer.length)
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }

            IntSerializer.writeInt32LE(value, lb.buffer, lb.offset);
            lb.offset += 4;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt64LE(final long value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 8;

            if (lb.offset + 8 > lb.buffer.length)
            {
                // grow
                lb = new LinkedBuffer(session.nextBufferSize, lb);
            }

            IntSerializer.writeInt64LE(value, lb.buffer, lb.offset);
            lb.offset += 8;

            return lb;
        }

        @Override
        public LinkedBuffer writeVarInt32(int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            while (true)
            {
                session.size++;
                if (lb.offset == lb.buffer.length)
                {
                    // grow
                    lb = new LinkedBuffer(session.nextBufferSize, lb);
                }

                if ((value & ~0x7F) == 0)
                {
                    lb.buffer[lb.offset++] = (byte) value;
                    return lb;
                }

                lb.buffer[lb.offset++] = (byte) ((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }

        @Override
        public LinkedBuffer writeVarInt64(long value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            while (true)
            {
                session.size++;
                if (lb.offset == lb.buffer.length)
                {
                    // grow
                    lb = new LinkedBuffer(session.nextBufferSize, lb);
                }

                if ((value & ~0x7FL) == 0)
                {
                    lb.buffer[lb.offset++] = (byte) value;
                    return lb;
                }

                lb.buffer[lb.offset++] = (byte) (((int) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }

        @Override
        public LinkedBuffer writeStrFromInt(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            return StringSerializer.writeInt(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrFromLong(final long value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            return StringSerializer.writeLong(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrFromFloat(final float value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            return StringSerializer.writeFloat(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrFromDouble(final double value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            return StringSerializer.writeDouble(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrAscii(final String value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            return StringSerializer.writeAscii(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrUTF8(final String value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            return StringSerializer.writeUTF8(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrUTF8VarDelimited(final String value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            return StringSerializer.writeUTF8VarDelimited(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrUTF8FixedDelimited(final String value,
                final boolean littleEndian, final WriteSession session, LinkedBuffer lb)
                throws IOException
        {
            return StringSerializer.writeUTF8FixedDelimited(value, littleEndian, session,
                    lb);
        }
    },
    STREAMED
    {
        @Override
        public LinkedBuffer drain(final WriteSession session,
                final LinkedBuffer lb) throws IOException
        {
            // flush and reset
            lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            return lb;
        }

        @Override
        public LinkedBuffer writeByteArrayB64(final byte[] value,
                final int offset, final int valueLen,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return B64Code.sencode(value, offset, valueLen, session, lb);
        }

        @Override
        public LinkedBuffer writeByteArray(final byte[] value,
                final int offset, final int valueLen,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            if (valueLen == 0)
                return lb;

            session.size += valueLen;

            if (lb.offset + valueLen > lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start,
                        value, offset, valueLen);

                return lb;
            }

            System.arraycopy(value, offset, lb.buffer, lb.offset, valueLen);
            lb.offset += valueLen;

            return lb;
        }

        @Override
        public LinkedBuffer writeByte(final byte value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size++;

            if (lb.offset == lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            }
            lb.buffer[lb.offset++] = value;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt16(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 2;

            if (lb.offset + 2 > lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            }

            IntSerializer.writeInt16(value, lb.buffer, lb.offset);
            lb.offset += 2;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt16LE(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 2;

            if (lb.offset + 2 > lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            }

            IntSerializer.writeInt16LE(value, lb.buffer, lb.offset);
            lb.offset += 2;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt32(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 4;

            if (lb.offset + 4 > lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            }

            IntSerializer.writeInt32(value, lb.buffer, lb.offset);
            lb.offset += 4;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt64(final long value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 8;

            if (lb.offset + 8 > lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            }

            IntSerializer.writeInt64(value, lb.buffer, lb.offset);
            lb.offset += 8;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt32LE(final int value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 4;

            if (lb.offset + 4 > lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            }

            IntSerializer.writeInt32LE(value, lb.buffer, lb.offset);
            lb.offset += 4;

            return lb;
        }

        @Override
        public LinkedBuffer writeInt64LE(final long value,
                final WriteSession session, LinkedBuffer lb) throws IOException
        {
            session.size += 8;

            if (lb.offset + 8 > lb.buffer.length)
            {
                // flush and reset
                lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
            }

            IntSerializer.writeInt64LE(value, lb.buffer, lb.offset);
            lb.offset += 8;

            return lb;
        }

        @Override
        public LinkedBuffer writeVarInt32(int value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            while (true)
            {
                session.size++;
                if (lb.offset == lb.buffer.length)
                {
                    // flush and reset
                    lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
                }

                if ((value & ~0x7F) == 0)
                {
                    lb.buffer[lb.offset++] = (byte) value;
                    return lb;
                }

                lb.buffer[lb.offset++] = (byte) ((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }

        @Override
        public LinkedBuffer writeVarInt64(long value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            while (true)
            {
                session.size++;
                if (lb.offset == lb.buffer.length)
                {
                    // flush and reset
                    lb.offset = session.flush(lb.buffer, lb.start, lb.offset - lb.start);
                }

                if ((value & ~0x7FL) == 0)
                {
                    lb.buffer[lb.offset++] = (byte) value;
                    return lb;
                }

                lb.buffer[lb.offset++] = (byte) (((int) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }

        @Override
        public LinkedBuffer writeStrFromInt(final int value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeInt(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrFromLong(final long value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeLong(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrFromFloat(final float value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeFloat(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrFromDouble(final double value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeDouble(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrAscii(final String value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeAscii(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrUTF8(final String value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeUTF8(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrUTF8VarDelimited(final String value,
                final WriteSession session, final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeUTF8VarDelimited(value, session, lb);
        }

        @Override
        public LinkedBuffer writeStrUTF8FixedDelimited(final String value,
                final boolean littleEndian, final WriteSession session,
                final LinkedBuffer lb) throws IOException
        {
            return StreamedStringSerializer.writeUTF8FixedDelimited(value,
                    littleEndian, session, lb);
        }
    };

    public abstract LinkedBuffer drain(final WriteSession session,
            final LinkedBuffer lb) throws IOException;

    public final LinkedBuffer writeByteArrayB64(final byte[] value,
            final WriteSession session, final LinkedBuffer lb) throws IOException
    {
        return writeByteArrayB64(value, 0, value.length, session, lb);
    }

    public abstract LinkedBuffer writeByteArrayB64(final byte[] value,
            final int offset, final int length, final WriteSession session, final LinkedBuffer lb)
            throws IOException;

    public final LinkedBuffer writeByteArray(final byte[] value,
            final WriteSession session, final LinkedBuffer lb) throws IOException
    {
        return writeByteArray(value, 0, value.length, session, lb);
    }

    public abstract LinkedBuffer writeByteArray(final byte[] value,
            final int offset, final int length, final WriteSession session, final LinkedBuffer lb)
            throws IOException;

    public abstract LinkedBuffer writeByte(final byte value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    // public abstract LinkedBuffer writeBool(final boolean value,
    // final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeInt32(final int value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeInt64(final long value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public final LinkedBuffer writeFloat(final float value,
            final WriteSession session, final LinkedBuffer lb) throws IOException
    {
        return writeInt32(Float.floatToRawIntBits(value), session, lb);
    }

    public final LinkedBuffer writeDouble(final double value,
            final WriteSession session, final LinkedBuffer lb) throws IOException
    {
        return writeInt64(Double.doubleToRawLongBits(value), session, lb);
    }

    public abstract LinkedBuffer writeInt16(final int value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeInt16LE(final int value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeInt32LE(final int value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeInt64LE(final long value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public final LinkedBuffer writeFloatLE(final float value,
            final WriteSession session, final LinkedBuffer lb) throws IOException
    {
        return writeInt32LE(Float.floatToRawIntBits(value), session, lb);
    }

    public final LinkedBuffer writeDoubleLE(final double value,
            final WriteSession session, final LinkedBuffer lb) throws IOException
    {
        return writeInt64LE(Double.doubleToRawLongBits(value), session, lb);
    }

    public abstract LinkedBuffer writeVarInt32(final int value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeVarInt64(final long value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrFromInt(final int value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrFromLong(final long value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrFromFloat(final float value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrFromDouble(final double value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrAscii(final String value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrUTF8(final String value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrUTF8VarDelimited(final String value,
            final WriteSession session, final LinkedBuffer lb) throws IOException;

    public abstract LinkedBuffer writeStrUTF8FixedDelimited(final String value,
            final boolean littleEndian, final WriteSession session,
            final LinkedBuffer lb) throws IOException;
}
