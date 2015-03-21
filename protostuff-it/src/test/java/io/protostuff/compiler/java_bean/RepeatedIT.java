package io.protostuff.compiler.java_bean;

import java.io.IOException;
import java.util.Arrays;

import io.protostuff.compiler.it.java_bean.Int32List;
import io.protostuff.compiler.it.java_bean.UnmodifiableInt32List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import io.protostuff.Input;
import io.protostuff.Schema;

/**
 * Integration tests for java_bean repeated fields
 *
 * @author Konstantin Shchepanovskyi
 */
public class RepeatedIT
{

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test that generated #mergeFrom method can be used multiple times
     *
     * @throws Exception
     */
    @Test
    public void testMergeTwice() throws Exception
    {
        Int32List list = Int32List.getSchema().newMessage();
        list.mergeFrom(createInput(42), list);
        list.mergeFrom(createInput(43), list);
        Assert.assertEquals(Arrays.asList(42, 43), list.getNumbersList());
    }

    @Test
    public void testUnmodifiableList() throws Exception
    {
        UnmodifiableInt32List list = UnmodifiableInt32List.getSchema().newMessage();
        list.mergeFrom(createInput(42), list);
        exception.expect(UnsupportedOperationException.class);
        list.mergeFrom(createInput(43), list);
    }

    private Input createInput(int result) throws IOException
    {
        Input input = Mockito.mock(Input.class);
        Mockito.when(input.readFieldNumber(Mockito.any(Schema.class)))
                .thenReturn(1)
                .thenReturn(0);
        Mockito.when(input.readInt32())
                .thenReturn(result);
        return input;
    }
}
