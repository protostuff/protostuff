package io.protostuff.runtime;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import io.protostuff.Output;
import io.protostuff.Tag;

/**
 * @author Konstantin Shchepanovskyi
 */
public class RuntimeSchemaEnumTagTest
{

    @Test
    public void testWriteNumericEnum() throws Exception
    {
        RuntimeSchema<A> schema = RuntimeSchema.createFrom(A.class);
        A a = new A();
        a.x = TaggedEnum.TEN;
        Output output = Mockito.mock(Output.class);
        schema.writeTo(output, a);
        Mockito.verify(output).writeEnum(1, 10, false);
        Mockito.verifyNoMoreInteractions(output);
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
        public TaggedEnum x;
    }
}
