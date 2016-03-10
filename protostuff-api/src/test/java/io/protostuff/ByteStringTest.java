package io.protostuff;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ByteStringTest
{

    @Test
    public void testToString_emptyString() throws Exception
    {
        Assert.assertTrue(ByteString.EMPTY.toString().contains("size=0"));
    }

    @Test
    public void testToString() throws Exception
    {
        byte[] array = new byte[] { 1, 2, 3, 4 };
        ByteString s = ByteString.copyFrom(array);
        Assert.assertTrue(s.toString().contains("size=4"));
    }

}