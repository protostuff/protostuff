package io.protostuff;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mattia Barbon
 */
public class JsonInfNaNSerializationTest
{

    public static final String JSONPositiveInfinity = "{" +
            "\"float\":\"Infinity\"," +
            "\"double\":\"Infinity\"" +
            "}";

    public static final String JSONNegativeInfinity = "{" +
            "\"float\":\"-Infinity\"," +
            "\"double\":\"-Infinity\"" +
            "}";

    public static final String JSONNaN = "{" +
            "\"float\":\"NaN\"," +
            "\"double\":\"NaN\"" +
            "}";

    @Test
    public void testSerializePositiveInfinity() throws Exception
    {
        FPNumbers msg = new FPNumbers();
        msg.setFloat(Float.POSITIVE_INFINITY);
        msg.setDouble(Double.POSITIVE_INFINITY);
        byte[] bytes = JsonIOUtil.toByteArray(msg, FPNumbers.getSchema(), false);
        String result = new String(bytes);
        Assert.assertEquals(JSONPositiveInfinity, result);
    }

    @Test
    public void testDeserializePositiveInfinity() throws Exception
    {
        FPNumbers msg = new FPNumbers();
        JsonIOUtil.mergeFrom(JSONPositiveInfinity.getBytes(), msg, FPNumbers.getSchema(), false);
        Assert.assertEquals((Float) Float.POSITIVE_INFINITY, msg.getFloat());
        Assert.assertEquals((Double) Double.POSITIVE_INFINITY, msg.getDouble());
    }

    @Test
    public void testSerializeNegativeInfinity() throws Exception
    {
        FPNumbers msg = new FPNumbers();
        msg.setFloat(Float.NEGATIVE_INFINITY);
        msg.setDouble(Double.NEGATIVE_INFINITY);
        byte[] bytes = JsonIOUtil.toByteArray(msg, FPNumbers.getSchema(), false);
        String result = new String(bytes);
        Assert.assertEquals(JSONNegativeInfinity, result);
    }

    @Test
    public void testDeserializeNegativeInfinity() throws Exception
    {
        FPNumbers msg = new FPNumbers();
        JsonIOUtil.mergeFrom(JSONNegativeInfinity.getBytes(), msg, FPNumbers.getSchema(), false);
        Assert.assertEquals((Float) Float.NEGATIVE_INFINITY, msg.getFloat());
        Assert.assertEquals((Double) Double.NEGATIVE_INFINITY, msg.getDouble());
    }

    @Test
    public void testSerializeNaN() throws Exception
    {
        FPNumbers msg = new FPNumbers();
        msg.setFloat(Float.NaN);
        msg.setDouble(Double.NaN);
        byte[] bytes = JsonIOUtil.toByteArray(msg, FPNumbers.getSchema(), false);
        String result = new String(bytes);
        Assert.assertEquals(JSONNaN, result);
    }

    @Test
    public void testDeserializeNaN() throws Exception
    {
        FPNumbers msg = new FPNumbers();
        JsonIOUtil.mergeFrom(JSONNaN.getBytes(), msg, FPNumbers.getSchema(), false);
        Assert.assertEquals((Float) Float.NaN, msg.getFloat());
        Assert.assertEquals((Double) Double.NaN, msg.getDouble());
    }

}
