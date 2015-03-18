package io.protostuff.compiler.java_bean_primitives;

import org.junit.Assert;
import org.junit.Test;

import io.protostuff.compiler.it.java_bean_primitives.PrimitiveFields;

/**
 * Integration tests for java_bean_primitives
 *
 * @author Konstantin Shchepanovskyi
 */
public class PrimitivesIT
{

    @Test
    public void testEquals() throws Exception
    {
        PrimitiveFields x = new PrimitiveFields(2, 3L);
        PrimitiveFields y = new PrimitiveFields(2, 3L);
        Assert.assertEquals(x, y);
    }

    @Test
    public void testNotEqual() throws Exception
    {
        PrimitiveFields x = new PrimitiveFields(2, 3L);
        PrimitiveFields y = new PrimitiveFields(2, 4L);
        Assert.assertNotEquals(x, y);
    }

    @Test
    public void testHashcode() throws Exception
    {
        PrimitiveFields x = new PrimitiveFields(2, 3L);
        PrimitiveFields y = new PrimitiveFields(2, 4L);
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
    }
}
