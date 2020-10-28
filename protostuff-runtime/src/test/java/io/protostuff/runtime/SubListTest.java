package io.protostuff.runtime;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SubList ser & deser tests
 *
 * @author witis
 */
public class SubListTest extends AbstractTest {

    public void testSubList()
    {
    	List<String> arrayList = new ArrayList<>(Arrays.asList("one", "two", "three"));
    	List<String> subList = arrayList.subList(0, 2);
    	SerializableObjects.assertEquals(subList, deserialize(serialize(subList)));
    }

	private <T> byte[] serialize(T object)
	{
		WrappedObject wrappedObject = new WrappedObject(object);
		Schema<WrappedObject> schema = getSchema(WrappedObject.class);
		LinkedBuffer buffer = LinkedBuffer.allocate();
		try {
			return GraphIOUtil.toByteArray(wrappedObject, schema, buffer);
		} finally {
			buffer.clear();
		}
	}

	private <T> T deserialize(byte[] bytes)
	{
		Schema<WrappedObject> schema = getSchema(WrappedObject.class);
		WrappedObject object = schema.newMessage();
		GraphIOUtil.mergeFrom(bytes, object, schema);
		return (T) object.rawObject;
	}

	private static class WrappedObject
	{
		private final Object rawObject;

		public WrappedObject(Object rawObject)
		{
			this.rawObject = rawObject;
		}

		@SuppressWarnings("unchecked")
		public <T> T rawObject()
		{
			return (T) rawObject;
		}
	}
}
