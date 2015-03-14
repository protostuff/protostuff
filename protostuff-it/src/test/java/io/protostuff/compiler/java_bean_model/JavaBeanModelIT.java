package io.protostuff.compiler.java_bean_model;

import org.junit.Assert;
import org.junit.Test;

import io.protostuff.compiler.it.java_bean_model.JavaBeanModelSimpleMessage;

/**
 * @author Konstantin Shchepanovskyi
 */
public class JavaBeanModelIT
{

    @Test
    public void testEquals() throws Exception
    {
        JavaBeanModelSimpleMessage x = new JavaBeanModelSimpleMessage();
        x.setA(2);
        x.setB(3L);
        JavaBeanModelSimpleMessage y = new JavaBeanModelSimpleMessage();
        y.setA(2);
        y.setB(3L);
        Assert.assertEquals(x, y);
    }

    @Test
    public void testNotEqual() throws Exception
    {
        JavaBeanModelSimpleMessage x = new JavaBeanModelSimpleMessage();
        x.setA(2);
        x.setB(3L);
        JavaBeanModelSimpleMessage y = new JavaBeanModelSimpleMessage();
        y.setA(2);
        y.setB(4L);
        Assert.assertNotEquals(x, y);
    }

    @Test
    public void testHashcode() throws Exception
    {
        JavaBeanModelSimpleMessage x = new JavaBeanModelSimpleMessage();
        x.setA(2);
        x.setB(3L);
        JavaBeanModelSimpleMessage y = new JavaBeanModelSimpleMessage();
        y.setA(2);
        y.setB(4L);
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
    }
}
