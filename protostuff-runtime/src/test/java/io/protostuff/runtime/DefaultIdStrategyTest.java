package io.protostuff.runtime;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import io.protostuff.Message;
import io.protostuff.Schema;

public class DefaultIdStrategyTest
{

    private final IdStrategy strategy = new DefaultIdStrategy();

    @Test
    public void privateConstructors()
    {
        HasSchema<TestMessage> schema = strategy.getSchemaWrapper(TestMessage.class, true);
        assertEquals(TestMessage.SCHEMA, schema.getSchema());
    }

    public static class TestMessage implements Message<TestMessage>
    {

        private static final Schema<TestMessage> SCHEMA = Mockito.mock(Schema.class);

        private TestMessage()
        {
        }

        @Override
        public Schema<TestMessage> cachedSchema()
        {
            return SCHEMA;
        }
    }
}
