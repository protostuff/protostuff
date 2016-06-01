package io.protostuff;

import io.protostuff.runtime.RuntimeSchema;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for ignoring unknown fields during de-seralization
 */
public class JsonUnknownFieldTest
{

    public static final String NORMAL_MESSAGE = "{"
            + "\"field1\":42,"
            + "\"field2\":\"testValue\""
            + "}";

    public static final String UNKNOWN_SCALAR_FIELD = "{"
            + "\"field1\":42,"
            + "\"unknownField\":42,"
            + "\"field2\":\"testValue\""
            + "}";

    public static final String UNKNOWN_ARRAY_FIELD = "{"
            + "\"field1\":42,"
            + "\"unknownField\":["
            + "{\"x\":1}, "
            + "{\"x\":1, \"y\": [1,2,3]}],"
            + "\"field2\":\"testValue\""
            + "}";

    public static final String UNKNOWN_EMPTY_MESSAGE_FIELD = "{"
            + "\"field1\":42,"
            + "\"unknownField\":{},"
            + "\"field2\":\"testValue\""
            + "}";

    public static final String UNKNOWN_NESTED_MESSAGE_FIELD = "{"
            + "\"field1\":42,"
            + "\"unknownField\":{"
            + "\"a\":0,"
            + "\"field1\":43,"
            + "\"anotherNestedField\":{"
            + "\"b\":0,"
            + "\"c\":[1, 2, 3],"
            + "\"thirdNestedField\":{"
            + "\"e\":1,"
            + "\"f\":\"foobar\""
            + "}"
            + "}"
            + "},"
            + "\"field2\":\"testValue\""
            + "}";

    public static final Schema<TestMessage> SCHEMA = RuntimeSchema.getSchema(TestMessage.class);

    private TestMessage instance;

    @Before
    public void setUp() throws Exception
    {
        instance = SCHEMA.newMessage();
    }

    @Test
    public void normalMessage() throws Exception
    {
        JsonIOUtil.mergeFrom(NORMAL_MESSAGE.getBytes(), instance, SCHEMA, false);
        checkKnownFields(instance);
    }

    @Test
    public void unknownScalarField() throws Exception
    {
        JsonIOUtil.mergeFrom(UNKNOWN_SCALAR_FIELD.getBytes(), instance, SCHEMA, false);
        checkKnownFields(instance);
    }

    @Test
    public void unknownArrayField() throws Exception
    {
        JsonIOUtil.mergeFrom(UNKNOWN_ARRAY_FIELD.getBytes(), instance, SCHEMA, false);
        checkKnownFields(instance);
    }

    @Test
    public void unknownEmptyMessageField() throws Exception
    {
        JsonIOUtil.mergeFrom(UNKNOWN_EMPTY_MESSAGE_FIELD.getBytes(), instance, SCHEMA, false);
        checkKnownFields(instance);
    }

    @Test
    public void unknownNestedMessageField() throws Exception
    {
        JsonIOUtil.mergeFrom(UNKNOWN_NESTED_MESSAGE_FIELD.getBytes(), instance, SCHEMA, false);
        checkKnownFields(instance);
    }

    private void checkKnownFields(TestMessage instance)
    {
        Assert.assertEquals(42, instance.field1);
        Assert.assertEquals("testValue", instance.field2);
    }

    static class TestMessage
    {
        public int field1;
        public String field2;
    }
}
