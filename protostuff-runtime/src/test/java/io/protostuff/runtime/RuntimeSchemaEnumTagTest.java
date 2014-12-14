package io.protostuff.runtime;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import io.protostuff.Output;
import io.protostuff.Tag;

import java.io.ByteArrayOutputStream;

/**
 * @author Konstantin Shchepanovskyi
 */
public class RuntimeSchemaEnumTagTest
{

    @Test
    public void testWriteNumericEnum() throws Exception
    {
        RuntimeSchema<A> schema = RuntimeSchema.createFrom(A.class);
        A a = new A(TaggedEnum.TEN);
        Output output = Mockito.mock(Output.class);
        schema.writeTo(output, a);
        Mockito.verify(output).writeEnum(1, 10, false);
        Mockito.verifyNoMoreInteractions(output);
    }

    @Test
    public void testSerializeDeserializeNumericEnum() throws Exception
    {
        RuntimeSchema<A> schema = RuntimeSchema.createFrom(A.class);
        A source = new A(TaggedEnum.TEN);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LinkedBuffer buffer = LinkedBuffer.allocate();
        ProtostuffIOUtil.writeTo(outputStream, source, schema, buffer);
        byte[] bytes = outputStream.toByteArray();
        A newMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, schema);
        Assert.assertEquals(source, newMessage);
    }

    @Test
    @Ignore("https://github.com/protostuff/protostuff/issues/69")
    public void testWriteStringEnum() throws Exception {
        // TODO: it is not possible to create this test in simple way until we are using RuntimeEnv singleton
    }

    static enum TaggedEnum
    {

        @Tag(value = 1, alias = "one")
        ONE,

        @Tag(value = 2, alias = "two")
        TWO,

        @Tag(value = 3, alias = "three")
        THREE,

        @Tag(value = 10, alias = "ten")
        TEN

    }

    static class A
    {
        private TaggedEnum x;

        public A() {
        }

        public A(TaggedEnum x) {
            this.x = x;
        }

        public TaggedEnum getX() {
            return x;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof A)) return false;

            A a = (A) o;

            if (x != a.x) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return x != null ? x.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "A{" +
                    "x=" + x +
                    '}';
        }

    }
}
