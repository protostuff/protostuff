package io.protostuff.compiler;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import io.protostuff.Input;
import io.protostuff.Schema;
import io.protostuff.compiler.it.Int32List;

/**
 * Integration tests for java_bean repeated fields
 *
 * @author Konstantin Shchepanovskyi
 */
public class RepeatedIT
{

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
