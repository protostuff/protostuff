package io.protostuff.compiler.java_bean;

import io.protostuff.compiler.it.java_bean.MessageWithDefaultValues;
import io.protostuff.compiler.it.java_bean.MessageWithoutDefaultValues;
import io.protostuff.compiler.it.java_bean.TestEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class DefaultValueIT
{

	@Test
	public void test_newInstance_withoutDefaultValues() throws Exception
	{
		MessageWithoutDefaultValues message = MessageWithoutDefaultValues.getSchema().newMessage();
		Assert.assertNull(message.getA());
		Assert.assertNull(message.getE());
		Assert.assertNull(message.getS());
	}

	@Test
	public void test_newInstance_withDefaultValues() throws Exception
	{
		MessageWithDefaultValues message = MessageWithDefaultValues.getSchema().newMessage();
		Assert.assertEquals(Integer.valueOf(1), message.getA());
		Assert.assertEquals(TestEnum.D, message.getE());
		Assert.assertEquals("string", message.getS());
	}
}
