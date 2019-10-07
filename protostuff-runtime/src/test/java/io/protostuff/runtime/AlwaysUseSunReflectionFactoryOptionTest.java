package io.protostuff.runtime;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class AlwaysUseSunReflectionFactoryOptionTest extends AbstractTest
{
    
	@Override
    protected IdStrategy newIdStrategy()
    {
        return new DefaultIdStrategy(0);
    }

    public void testUseSunReflectionFactory() throws Exception {
		System.setProperty("protostuff.runtime.always_use_sun_reflection_factory", "true");
		Schema<MyClass> schema = RuntimeSchema.getSchema(MyClass.class);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		MyClass myClass = new MyClass(); // constructor initializes list with one element
		ProtostuffIOUtil.writeTo(output, myClass, schema, LinkedBuffer.allocate());
		byte[] bytes = output.toByteArray();
		Assert.assertEquals(1, myClass.getList().size());
		MyClass myClassNew = schema.newMessage(); // default constructor should not be used
		ProtostuffIOUtil.mergeFrom(bytes, myClassNew, schema);
		Assert.assertEquals(1, myClassNew.getList().size());
	}

	final static class MyClass {
		private List<String> list;

		public MyClass() {
			list = new ArrayList<String>();
			list.add("hello");
		}

		public List<String> getList() {
			return list;
		}
	}

}
