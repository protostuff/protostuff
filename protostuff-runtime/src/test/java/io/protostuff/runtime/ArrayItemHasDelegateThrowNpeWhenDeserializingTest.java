package io.protostuff.runtime;

import io.protostuff.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Test for #issue336
 *
 * @author rxh
 * @created Jul 21, 2022
 */
public class ArrayItemHasDelegateThrowNpeWhenDeserializingTest {
    @Test
    public void test() {
        try {
            DefaultIdStrategy idStrategy = new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS | IdStrategy.COLLECTION_SCHEMA_ON_REPEATED_FIELDS);
            idStrategy.registerDelegate(new ProtobufCalendar());

            ClassThrowNpeWhenDeserializing originMsg = new ClassThrowNpeWhenDeserializing();
            originMsg.value = new ArrayList<Calendar[]>();
            Calendar[] vArray = new Calendar[1];
            vArray[0] = Calendar.getInstance();
            originMsg.value.add(vArray);

            RuntimeSchema<ClassThrowNpeWhenDeserializing> schema = RuntimeSchema.createFrom(ClassThrowNpeWhenDeserializing.class, idStrategy);
            byte[] bytes = ProtobufIOUtil.toByteArray(originMsg, schema, LinkedBuffer.allocate(512));
            ClassThrowNpeWhenDeserializing msg = schema.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, msg, schema);

            Assert.assertEquals(1, originMsg.value.size());
            Assert.assertEquals(1, msg.value.size());
            Assert.assertEquals(1, originMsg.value.get(0).length);
            Assert.assertEquals(1, msg.value.get(0).length);
            Assert.assertEquals(originMsg.value.get(0)[0], msg.value.get(0)[0]);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    static class ClassThrowNpeWhenDeserializing {
        List<Calendar[]> value = new ArrayList<Calendar[]>();
    }

    public static class ProtobufCalendar implements Delegate<Calendar> {

        @Override
        public WireFormat.FieldType getFieldType() {
            return WireFormat.FieldType.FIXED64;
        }

        @Override
        public Calendar readFrom(Input input) throws IOException {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(input.readFixed64());
            return calendar;
        }

        @Override
        public void writeTo(Output output, int number, Calendar value, boolean repeated) throws IOException {
            output.writeFixed64(number, value.getTimeInMillis(), repeated);
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            output.writeFixed64(number, input.readSInt64(), repeated);
        }

        @Override
        public Class<?> typeClass() {
            return Calendar.class;
        }

    }
}