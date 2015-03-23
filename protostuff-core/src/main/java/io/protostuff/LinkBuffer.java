package io.protostuff;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A list of ByteBuffers.
 * 
 * @author Ryan Rawson
 */
public class LinkBuffer
{

    public static final int DEFAULT_BUFFER_SIZE = 256;

    public final int allocSize;
    ByteBuffer current;
    List<ByteBuffer> buffers = new ArrayList<>();

    public LinkBuffer()
    {
        this(DEFAULT_BUFFER_SIZE);
    }
    public LinkBuffer(int allocSize)
    {

        assert allocSize >= 8;

        this.allocSize = allocSize;

        current = ByteBuffer.allocate(allocSize);
    }

    public long size()
    {
        long size = 0;
        for (ByteBuffer b : buffers)
        {
            size += b.remaining();
        }
        if (current != null)
            size += current.position();

        return size;
    }

    public List<ByteBuffer> getBuffers()
    {
        int size = buffers.size() + (current != null ? 1 : 0);
        List<ByteBuffer> copy = new ArrayList<>(size);
        for (ByteBuffer b : buffers)
        {
            copy.add(b.duplicate());
        }
        if (current != null)
        {
            ByteBuffer duplicate = current.duplicate();
            duplicate.flip();
            copy.add(duplicate);
        }
        return Collections.unmodifiableList(copy);
    }

    private void nextBuffer()
    {
        current.flip();
        buffers.add(current);
        current = ByteBuffer.allocate(allocSize);
    }

    private void spliceBuffer(ByteBuffer buf)
    {
        // the current buffer is empty case.
        if (current.position() == 0)
        {
            buffers.add(buf);
            return;
        }

        current.flip();
        buffers.add(current);
        buffers.add(buf);
        current = ByteBuffer.allocate(allocSize);
    }

    private void ensureCapacity(int needed)
    {
        if (current.remaining() < needed)
        {
            nextBuffer();
        }
    }

    public List<ByteBuffer> finish()
    {
        current.flip();
        buffers.add(current);
        current = null; // mark as finished.

        // make the buffers unmodifiable now.

        buffers = Collections.unmodifiableList(buffers);
        return getBuffers();
    }

    // lengthy implementation junk now.
    public LinkBuffer writeByte(final byte value) throws IOException
    {
        ensureCapacity(1);

        current.put(value);
        return this;
    }

    public LinkBuffer writeInt16(final int value) throws IOException
    {
        // need 2 bytes:
        ensureCapacity(2);

        current.putShort((short) value);

        return this;
    }

    public LinkBuffer writeInt16LE(final int value) throws IOException
    {
        ensureCapacity(2);
        IntSerializer.writeInt16LE(value, current);
        return this;
    }

    public LinkBuffer writeInt32(final int value) throws IOException
    {
        ensureCapacity(4);
        current.putInt(value);
        return this;
    }

    public LinkBuffer writeInt32LE(final int value) throws IOException
    {
        ensureCapacity(4);
        IntSerializer.writeInt32LE(value, current);
        return this;
    }

    public LinkBuffer writeInt64(final long value) throws IOException
    {
        ensureCapacity(8);
        current.putLong(value);
        return this;
    }

    public LinkBuffer writeInt64LE(final long value) throws IOException
    {
        ensureCapacity(8);
        IntSerializer.writeInt64LE(value, current);
        return this;
    }

    public LinkBuffer writeVarInt32(int value) throws IOException
    {
        byte[] buf = new byte[5];
        int locPtr = 0;
        while (true)
        {
            if ((value & ~0x7F) == 0)
            {
                buf[locPtr++] = (byte) value;
                // thing;
                ensureCapacity(locPtr);
                current.put(buf, 0, locPtr);

                return this;

            }
            else
            {
                buf[locPtr++] = (byte) ((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    public LinkBuffer writeVarInt64(long value) throws IOException
    {

        byte[] buf = new byte[10];
        int locPtr = 0;

        while (true)
        {
            if ((value & ~0x7FL) == 0)
            {
                buf[locPtr++] = (byte) value;
                ensureCapacity(locPtr);
                current.put(buf, 0, locPtr);
                return this;
            }
            else
            {
                buf[locPtr++] = (byte) (((int) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    public LinkBuffer writeDouble(final double value) throws IOException
    {
        return writeInt64(Double.doubleToRawLongBits(value));
    }

    public LinkBuffer writeFloat(final float value) throws IOException
    {
        return writeInt32(Float.floatToRawIntBits(value));
    }

    public LinkBuffer writeByteArray(final byte[] value,
            final int offset, final int length) throws IOException
    {
        // maybe splice in.
        if (current.remaining() >= length)
        {
            // copy in:
            current.put(value, offset, length);
        }
        else
        {
            // too big. splice in:
            ByteBuffer wrapped = ByteBuffer.wrap(value, offset, length);
            spliceBuffer(wrapped);
        }
        return this;
    }

    public LinkBuffer writeByteArray(final byte[] value) throws IOException
    {
        return writeByteArray(value, 0, value.length);
    }

    public LinkBuffer writeByteBuffer(ByteBuffer buf)
    {
        ByteBuffer cp = buf.slice();
        if (current.remaining() >= cp.remaining())
        {
            current.put(cp);
        }
        else
        {
            // splice it in if too large.
            spliceBuffer(cp);
        }
        return this;
    }
}
