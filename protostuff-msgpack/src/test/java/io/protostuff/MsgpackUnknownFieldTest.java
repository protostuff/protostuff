//========================================================================
//Copyright (C) 2016 Alex Shvid
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package io.protostuff;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.protostuff.runtime.RuntimeSchema;

/**
 * Test for ignoring unknown fields during de-seralization
 */
public class MsgpackUnknownFieldTest
{

    protected static boolean numeric = false;
    
    public static final Schema<TestMessage> SCHEMA = RuntimeSchema.getSchema(TestMessage.class);
    public static final Schema<TestMessageExtended> EXTENDED_SCHEMA = RuntimeSchema
            .getSchema(TestMessageExtended.class);

    private TestMessage fixture;
    private TestMessageExtended fixtureExtended;

    @Before
    public void setUp() throws Exception
    {
        fixture = SCHEMA.newMessage();
        fixture.field1 = 42;
        fixture.field2 = "testValue";

        fixtureExtended = EXTENDED_SCHEMA.newMessage();
        fixtureExtended.field1 = 42;
        fixtureExtended.field2 = "testValue";
        fixtureExtended.field3 = "hello".getBytes();
    }

    @Test
    public void normalMessage() throws Exception
    {

        byte[] message = MsgpackIOUtil.toByteArray(fixture, SCHEMA, numeric);

        TestMessage instance = SCHEMA.newMessage();

        MsgpackIOUtil.mergeFrom(message, instance, SCHEMA, numeric);
        checkKnownFields(instance);
    }

    @Test
    public void normalExtendedMessage() throws Exception
    {

        byte[] message = MsgpackIOUtil.toByteArray(fixtureExtended, EXTENDED_SCHEMA, numeric);

        TestMessageExtended instance = EXTENDED_SCHEMA.newMessage();

        MsgpackIOUtil.mergeFrom(message, instance, EXTENDED_SCHEMA, numeric);
        checkKnownFields(instance);
    }

    @Test
    public void unknownField() throws Exception
    {
        byte[] extendedMessage = MsgpackIOUtil.toByteArray(fixtureExtended, EXTENDED_SCHEMA, numeric);

        TestMessage instance = SCHEMA.newMessage();

        // unknown field3
        MsgpackIOUtil.mergeFrom(extendedMessage, instance, SCHEMA, numeric);

        checkKnownFields(instance);

    }

    @Test
    public void missingField() throws Exception
    {
        byte[] message = MsgpackIOUtil.toByteArray(fixture, SCHEMA, numeric);

        TestMessageExtended instance = EXTENDED_SCHEMA.newMessage();

        // missing field3
        MsgpackIOUtil.mergeFrom(message, instance, EXTENDED_SCHEMA, numeric);

        Assert.assertEquals(fixtureExtended.field1, instance.field1);
        Assert.assertEquals(fixtureExtended.field2, instance.field2);
        Assert.assertNull(instance.field3);

    }

    private void checkKnownFields(TestMessage instance)
    {
        Assert.assertEquals(fixture.field1, instance.field1);
        Assert.assertEquals(fixture.field2, instance.field2);
    }

    private void checkKnownFields(TestMessageExtended instance)
    {
        Assert.assertEquals(fixtureExtended.field1, instance.field1);
        Assert.assertEquals(fixtureExtended.field2, instance.field2);
        Assert.assertTrue(Arrays.equals(fixtureExtended.field3, instance.field3));
    }

    static class TestMessage
    {
        public int field1;
        public String field2;
    }

    static class TestMessageExtended
    {
        public int field1;
        public String field2;
        public byte[] field3;
    }
}
