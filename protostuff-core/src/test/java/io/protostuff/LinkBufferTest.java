package io.protostuff;

import java.nio.ByteBuffer;
import java.util.List;

import junit.framework.TestCase;

public class LinkBufferTest extends TestCase
{

    public void testBasics() throws Exception
    {
        LinkBuffer buf = new LinkBuffer(8);

        // put in 4 longs:
        ByteBuffer bigBuf = ByteBuffer.allocate(100);
        bigBuf.limit(100);

        // each one of these writes gets its own byte buffer.
        buf.writeByteBuffer(bigBuf); // 0
        buf.writeByteArray(new byte[100]); // 1
        buf.writeByteArray(new byte[2]); // 2
        buf.writeByteArray(new byte[8]); // 3
        buf.writeInt64(1);
        buf.writeInt64(2);
        buf.writeInt64(3);
        buf.writeInt64(4);

        List<ByteBuffer> lbb = buf.finish();
        assertEquals(8, lbb.size());
        assertEquals(100, lbb.get(0).remaining());
        assertEquals(100, lbb.get(1).remaining());
        assertEquals(2, lbb.get(2).remaining());
        assertEquals(8, lbb.get(3).remaining());
        for (int i = 3; i < lbb.size(); i++)
        {
            assertEquals(8, lbb.get(i).remaining());
        }
    }
}
