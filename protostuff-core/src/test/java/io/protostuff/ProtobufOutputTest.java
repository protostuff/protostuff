package io.protostuff;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class ProtobufOutputTest
{

    @Test
    public void testEncodeZigZag32()
    {
        Assert.assertEquals(0x00000000, ProtobufOutput.encodeZigZag32(0x00000000));
        Assert.assertEquals(0x02020202, ProtobufOutput.encodeZigZag32(0x01010101));
        Assert.assertEquals(0x000001FF, ProtobufOutput.encodeZigZag32(0xFFFFFF00));
        Assert.assertEquals(0x00000001, ProtobufOutput.encodeZigZag32(0xFFFFFFFF));
    }

    @Test
    public void testEncodeZigZag64()
    {
        Assert.assertEquals(0L, ProtobufOutput.encodeZigZag64(0L));
        Assert.assertEquals(0x0202020202020202L, ProtobufOutput.encodeZigZag64(0x0101010101010101L));
        Assert.assertEquals(0x00000000000001FFL, ProtobufOutput.encodeZigZag64(0xFFFFFFFFFFFFFF00L));
        Assert.assertEquals(000000000000000001L, ProtobufOutput.encodeZigZag64(0xFFFFFFFFFFFFFFFFL));
    }

    @Test
    public void testComputeRawVarint64Size()
    {
        Assert.assertEquals(1, ProtobufOutput.computeRawVarint64Size(0L));
        Assert.assertEquals(2, ProtobufOutput.computeRawVarint64Size(1024L));
        Assert.assertEquals(3, ProtobufOutput.computeRawVarint64Size(65536L));
        Assert.assertEquals(4, ProtobufOutput.computeRawVarint64Size(8388608L));
        Assert.assertEquals(5, ProtobufOutput.computeRawVarint64Size(1073807360L));
        Assert.assertEquals(6, ProtobufOutput.computeRawVarint64Size(549755879424L));
        Assert.assertEquals(7, ProtobufOutput.computeRawVarint64Size(4398046511104L));
        Assert.assertEquals(8, ProtobufOutput.computeRawVarint64Size(562949953421312L));
        Assert.assertEquals(9, ProtobufOutput.computeRawVarint64Size(4612248968380809216L));
        Assert.assertEquals(10, ProtobufOutput.computeRawVarint64Size(-9223372036854710272L));
    }

    @Test
    public void testGetRawVarInt32Bytes()
    {
        Assert.assertArrayEquals(new byte[]{0}, ProtobufOutput.getRawVarInt32Bytes(0));
        Assert.assertArrayEquals(new byte[]{-127, 8}, ProtobufOutput.getRawVarInt32Bytes(1025));
    }

    @Test
    public void testComputeRawVarint32Size()
    {
        Assert.assertEquals(1, ProtobufOutput.computeRawVarint32Size(0));
        Assert.assertEquals(2, ProtobufOutput.computeRawVarint32Size(2048));
        Assert.assertEquals(3, ProtobufOutput.computeRawVarint32Size(16384));
        Assert.assertEquals(4, ProtobufOutput.computeRawVarint32Size(2097152));
        Assert.assertEquals(5, ProtobufOutput.computeRawVarint32Size(268435456));
    }

    @Test
    public void testGetTagAndRawLittleEndian32Bytes()
    {
        Assert.assertArrayEquals(
            new byte[]{-98, 95, -37, 15, 95, 91},
            ProtobufOutput.getTagAndRawLittleEndian32Bytes(12190, 1532956635));

        Assert.assertArrayEquals(
            new byte[]{0, 0, 0, 0, 0},
            ProtobufOutput.getTagAndRawLittleEndian32Bytes(0, 0));
    }

    @Test
    public void testGetTagAndRawLittleEndian64Bytes()
    {
        Assert.assertArrayEquals(
            new byte[]{-52, 70, -56, -120, -53, -54, -55, -53, -56, -57},
            ProtobufOutput.getTagAndRawLittleEndian64Bytes(9036, 0xC7C8CBC9CACB88C8L));

        Assert.assertArrayEquals(
            new byte[9],
            ProtobufOutput.getTagAndRawLittleEndian64Bytes(0, 0L));
    }

    @Test
    public void testGetTagAndRawVarInt32Bytes()
    {
        Assert.assertArrayEquals(
            new byte[]{6, -128, 2},
            ProtobufOutput.getTagAndRawVarInt32Bytes(6, 256));

        Assert.assertArrayEquals(
            new byte[]{-122, 14, 0},
            ProtobufOutput.getTagAndRawVarInt32Bytes(1798, 0));

        Assert.assertArrayEquals(
            new byte[]{0, 0},
            ProtobufOutput.getTagAndRawVarInt32Bytes(0, 0));
    }

    @Test
    public void testGetTagAndRawVarInt64Bytes() {
        Assert.assertArrayEquals(
            new byte[]{103, -26, 96},
            ProtobufOutput.getTagAndRawVarInt64Bytes(103, 12390L));

        Assert.assertArrayEquals(
            new byte[]{-89, 38, 38},
            ProtobufOutput.getTagAndRawVarInt64Bytes(4903, 38L));

        Assert.assertArrayEquals(
            new byte[]{0, 0},
            ProtobufOutput.getTagAndRawVarInt64Bytes(0, 0L));
    }

    @Test
    public void testWriteRawVarInt32() throws IOException
    {
        byte[] buf = new byte[8];

        ProtobufOutput.writeRawVarInt32(0x1A2B3C4D, buf, 2);

        Assert.assertArrayEquals(
            new byte[]{0, 0, -0x33, -0x08, -0x54, -0x2F, 0x01, 0},
            buf);
    }

    @Test
    public void testWriteRawLittleEndian32() {
        byte[] buf = new byte[8];

        Assert.assertEquals(4,
            ProtobufOutput.writeRawLittleEndian32(0x1A2B3C4D, buf, 2));

        Assert.assertArrayEquals(
            new byte[]{0, 0, 0x4D, 0x3C, 0x2B, 0x1A, 0, 0},
            buf);
    }

    @Test
    public void testWriteRawLittleEndian64()
    {
        byte[] buf = new byte[10];

        Assert.assertEquals(8,
            ProtobufOutput.writeRawLittleEndian64(0x1A2B3C4D1A2B3C4DL, buf, 1));

        Assert.assertArrayEquals(
            new byte[]{0, 0x4D, 0x3C, 0x2B, 0x1A, 0x4D, 0x3C, 0x2B, 0x1A, 0},
            buf);
    }

    @Test
    public void testWriteRawVarInt32Bytes() throws IOException
    {
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();

        ProtobufOutput.writeRawVarInt32Bytes(testOutput, 0x00000001);
        Assert.assertArrayEquals(new byte[]{1}, testOutput.toByteArray());
        testOutput.reset();

        ProtobufOutput.writeRawVarInt32Bytes(testOutput, 0x01010101);
        Assert.assertArrayEquals(new byte[]{-127, -126, -124, 8}, testOutput.toByteArray());
        testOutput.reset();

        ProtobufOutput.writeRawVarInt32Bytes(testOutput, 0xFFFFFF00);
        Assert.assertArrayEquals(new byte[]{-128, -2, -1, -1, 15}, testOutput.toByteArray());
        testOutput.reset();

        DataOutput testDataOutput = new DataOutputStream(testOutput);

        ProtobufOutput.writeRawVarInt32Bytes(testDataOutput, 0x00000001);
        Assert.assertArrayEquals(new byte[]{1}, testOutput.toByteArray());
        testOutput.reset();

        ProtobufOutput.writeRawVarInt32Bytes(testDataOutput, 0x01010101);
        Assert.assertArrayEquals(new byte[]{-127, -126, -124, 8}, testOutput.toByteArray());
        testOutput.reset();

        ProtobufOutput.writeRawVarInt32Bytes(testDataOutput, 0xFFFFFF00);
        Assert.assertArrayEquals(new byte[]{-128, -2, -1, -1, 15}, testOutput.toByteArray());
        testOutput.reset();
    }
}
