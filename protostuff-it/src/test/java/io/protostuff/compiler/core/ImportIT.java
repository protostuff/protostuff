package io.protostuff.compiler.core;

import core.imports.A;
import core.imports.B;
import org.junit.Test;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ImportIT
{
	@Test
	public void testImportedFieldIsAccessible() throws Exception
	{
		A a = new A();
		a.setB(new B());
	}
}
