package io.protostuff.compiler;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.compiler.it.EmptyMessage;
import io.protostuff.compiler.it.MessageWithEmptyMessage;

/**
 * Test that protostuff compiler generates valid code for messages w/o fields
 *
 * @author Konstantin Shchepanovskyi
 */
public class EmptyMessageIT
{

    private Schema<EmptyMessage> EMPTY_MESSAGE_SCHEMA;
    private Schema<MessageWithEmptyMessage> MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA;

    @Before
    public void setUp() throws Exception
    {
        EMPTY_MESSAGE_SCHEMA = EmptyMessage.getSchema();
        MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA = MessageWithEmptyMessage.getSchema();
    }

    @Test
    public void testEmptyMessageSchema() throws Exception
    {
        EmptyMessage message = new EmptyMessage();
        Assert.assertEquals(true, EMPTY_MESSAGE_SCHEMA.isInitialized(message));
        Assert.assertNotNull(EMPTY_MESSAGE_SCHEMA.newMessage());
        Assert.assertEquals(EmptyMessage.class, EMPTY_MESSAGE_SCHEMA.typeClass());
    }

    @Test
    public void testSerializeDeserialize_EmptyMessage() throws Exception
    {
        EmptyMessage source = new EmptyMessage();
        LinkedBuffer buffer = LinkedBuffer.allocate();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeTo(outputStream, source, EMPTY_MESSAGE_SCHEMA, buffer);
        byte[] bytes = outputStream.toByteArray();
        EmptyMessage newMessage = EMPTY_MESSAGE_SCHEMA.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, EMPTY_MESSAGE_SCHEMA);
        Assert.assertEquals(0, bytes.length);
    }

    @Test
    public void testSerializeDeserialize_MessageWithEmptyMessage_unset() throws Exception
    {
        MessageWithEmptyMessage source = new MessageWithEmptyMessage();
        LinkedBuffer buffer = LinkedBuffer.allocate();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeTo(outputStream, source, MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA, buffer);
        byte[] bytes = outputStream.toByteArray();
        MessageWithEmptyMessage newMessage = MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA);
        Assert.assertEquals(0, bytes.length);
        Assert.assertNull(newMessage.getA());
    }

    @Test
    public void testSerializeDeserialize_MessageWithEmptyMessage_is_set() throws Exception
    {
        MessageWithEmptyMessage source = new MessageWithEmptyMessage();
        source.setA(new EmptyMessage());
        LinkedBuffer buffer = LinkedBuffer.allocate();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeTo(outputStream, source, MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA, buffer);
        byte[] bytes = outputStream.toByteArray();
        MessageWithEmptyMessage newMessage = MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, MESSAGE_WITH_EMPTY_MESSAGE_SCHEMA);
        Assert.assertNotNull(newMessage.getA());
    }

    @Test
    public void testEquals() throws Exception
    {
        Assert.assertEquals(new EmptyMessage(), new EmptyMessage());
    }

    @Test
    public void testHashcode() throws Exception
    {
        Assert.assertEquals(0, new EmptyMessage().hashCode());
    }

}
