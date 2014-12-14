package io.protostuff;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 */
public class RepeatedTest extends AbstractTest
{
    public void testPackedRepeatedByteArray() throws IOException
    {
        final LinkedBuffer buffer = getProtobufBuffer();

        PojoWithRepeated test = new PojoWithRepeated();
        test.mergeFrom(new ByteArrayInput(buffer.buffer, 0, buffer.offset, false), test);

        verify(test);
    }

    public void testPackedRepeatedByteBuffer() throws IOException
    {
        final LinkedBuffer buffer = getProtobufBuffer();

        PojoWithRepeated test = new PojoWithRepeated();
        test.mergeFrom(new ByteBufferInput(
                ByteBuffer.wrap(buffer.buffer, 0, buffer.offset), false), test);

        verify(test);
    }

    public void testPackedRepeatedCodedInput() throws IOException
    {
        final LinkedBuffer buffer = getProtobufBuffer();

        PojoWithRepeated test = new PojoWithRepeated();
        test.mergeFrom(new CodedInput(buffer.buffer, 0, buffer.offset, false), test);

        verify(test);
    }

    private LinkedBuffer getProtobufBuffer() throws IOException
    {
        // Generate protobuf with packed repeated fields
        final LinkedBuffer buffer = new LinkedBuffer(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        final ProtobufOutput output = new ProtobufOutput(buffer);
        // 03 // first element (varint 3)
        // 8E 02 // second element (varint 270)
        output.writeByteRange(
                false, 1, new byte[] { (byte) 0x03, (byte) 0x8E, (byte) 0x02 }, 0, 3, true);
        // Interleave
        output.writeFixed64(2, 8, true);
        // Non packed
        output.writeInt32(1, 1234, true);
        // Interleave
        output.writeByteRange(
                false, 2, new byte[] { 9, 0, 0, 0, 0, 0, 0, 0 }, 0, 8, true);
        // 9E A7 05 // third element (varint 86942)
        output.writeByteRange(
                false, 1, new byte[] { (byte) 0x9E, (byte) 0xA7, (byte) 0x05 }, 0, 3, true);

        return buffer;
    }

    private void verify(final PojoWithRepeated test)
    {
        assertEquals(4, test.getSomeInt32Count());
        assertEquals((Integer) 3, test.getSomeInt32(0));
        assertEquals((Integer) 270, test.getSomeInt32(1));
        assertEquals((Integer) 1234, test.getSomeInt32(2));
        assertEquals((Integer) 86942, test.getSomeInt32(3));

        assertEquals(2, test.getSomeFixed64Count());
        assertEquals((Long) 8L, test.getSomeFixed64(0));
        assertEquals((Long) 9L, test.getSomeFixed64(1));
    }
}
