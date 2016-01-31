package io.protostuff;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class UnsignedNumberUtilTest
{

	@Test
	public void testParseUnsignedInt() throws Exception
	{
		Assert.assertEquals(-1, UnsignedNumberUtil.parseUnsignedInt("4294967295"));
	}

	@Test
	public void testUnsignedIntToString() throws Exception
	{
		Assert.assertEquals("4294967295", UnsignedNumberUtil.unsignedIntToString(-1));
	}

	@Test
	public void testParseUnsignedLong() throws Exception
	{
		Assert.assertEquals(-1, UnsignedNumberUtil.parseUnsignedLong("18446744073709551615"));
	}

	@Test
	public void testUnsignedLongToString() throws Exception
	{
		Assert.assertEquals("18446744073709551615", UnsignedNumberUtil.unsignedLongToString(-1L));
	}
}