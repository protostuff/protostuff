package io.protostuff;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ProtobufBoolTest extends AbstractTest
{
    private static final byte[] MULTI_BYTE_BOOL = { 0x30, (byte) 0x81, 0x01 };
    private static final byte[] PACKED_BOOL = { 0x32, 0x03, 0x00, (byte) 0x81, 0x01 };
    private static final byte[] TRUNCATED_BOOL = { 0x68, (byte) 0x80 };
    private static final byte[] BOOL_ABOVE_INT32_RANGE = {
            0x30, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, 0x10 };

    public void testByteArrayInputReadsMultiByteBool() throws Exception
    {
        Foo message = new Foo();
        ProtobufIOUtil.mergeFrom(MULTI_BYTE_BOOL, message, Foo.getSchema());
        assertEquals(Arrays.asList(Boolean.TRUE), message.getSomeBoolean());
    }

    public void testByteBufferInputReadsPackedBools() throws Exception
    {
        ByteBufferInput input = new ByteBufferInput(ByteBuffer.wrap(PACKED_BOOL), false);
        Foo message = new Foo();
        Foo.getSchema().mergeFrom(input, message);
        input.checkLastTagWas(0);
        assertEquals(Arrays.asList(Boolean.FALSE, Boolean.TRUE), message.getSomeBoolean());
    }

    public void testInputStreamReadsBoolAboveInt32Range() throws Exception
    {
        Foo message = new Foo();
        ProtobufIOUtil.mergeFrom(new ByteArrayInputStream(BOOL_ABOVE_INT32_RANGE), message,
                Foo.getSchema());
        assertEquals(Arrays.asList(Boolean.TRUE), message.getSomeBoolean());
    }

    public void testByteArrayInputRejectsTruncatedBool() throws Exception
    {
        ByteArrayInput input = new ByteArrayInput(TRUNCATED_BOOL, false);
        assertEquals(13, input.readFieldNumber(Foo.getSchema()));
        try
        {
            input.readBool();
            fail("Expected truncated bool varint to be rejected");
        }
        catch (ArrayIndexOutOfBoundsException expected)
        {
        }
    }
}
