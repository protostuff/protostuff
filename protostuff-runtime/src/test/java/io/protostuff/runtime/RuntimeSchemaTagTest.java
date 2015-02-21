package io.protostuff.runtime;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jol.info.GraphLayout;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Tag;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("unused")
public class RuntimeSchemaTagTest
{

    /**
     * Simple serialization/deserialization test
     *
     * @throws Exception
     */
    @Test
    public void testSerializeDeserialize() throws Exception
    {
        RuntimeSchema<A6> schema = RuntimeSchema.createFrom(A6.class);
        A6 source = new A6(42);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LinkedBuffer buffer = LinkedBuffer.allocate();
        ProtostuffIOUtil.writeTo(outputStream, source, schema, buffer);
        byte[] bytes = outputStream.toByteArray();
        A6 newMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, schema);
        Assert.assertEquals(source, newMessage);
    }

    @Test
    public void testSchemaSize() throws Exception
    {
        GraphLayout tag1 = GraphLayout.parseInstance(RuntimeSchema.createFrom(A1.class));
        GraphLayout tag6 = GraphLayout.parseInstance(RuntimeSchema.createFrom(A6.class));
        // tag value should not affect schema size on the heap
        // both messages have one field of same type, so their schemas should have same size
        Assert.assertEquals(tag1.totalSize(), tag6.totalSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTag() throws Exception
    {
        RuntimeSchema.createFrom(NegativeTag.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroTag() throws Exception
    {
        RuntimeSchema.createFrom(ZeroTag.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooBigTag() throws Exception
    {
        RuntimeSchema.createFrom(TooBigTag.class);
    }

    static class A1
    {
        @Tag(1)
        private int x;

        public A1()
        {
        }

        public A1(int x)
        {
            this.x = x;
        }

        public int getX()
        {
            return x;
        }

        public void setX(int x)
        {
            this.x = x;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (!(o instanceof A1))
                return false;

            A1 a1 = (A1) o;

            return x == a1.x;

        }

        @Override
        public int hashCode()
        {
            return x;
        }

        @Override
        public String toString()
        {
            return "A{" + "x=" + x + '}';
        }
    }

    static class A6
    {
        @Tag(1000_000)
        private int x;

        public A6()
        {
        }

        public A6(int x)
        {
            this.x = x;
        }

        public int getX()
        {
            return x;
        }

        public void setX(int x)
        {
            this.x = x;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (!(o instanceof A6))
                return false;

            A6 a6 = (A6) o;

            return x == a6.x;

        }

        @Override
        public int hashCode()
        {
            return x;
        }

        @Override
        public String toString()
        {
            return "A{" + "x=" + x + '}';
        }
    }

    static class NegativeTag
    {
        @Tag(-20)
        private int x;
    }

    static class ZeroTag
    {
        @Tag(0)
        private int x;
    }

    static class TooBigTag
    {
        @Tag(RuntimeSchema.MAX_TAG_VALUE + 1)
        private int x;
    }

}
