package com.dyuproject.protostuff;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ryan on 1/25/14.
 */
public class LowCopyProtobufOutputTest extends TestCase {


    public void testCompareVsOther() throws Exception {

        Baz aBaz = new Baz(1, "hello world", 1238372479L);
        ByteBuffer serForm1 = testObj(aBaz, aBaz);
        deserTest(aBaz, aBaz, serForm1);

        Bar testBar = new Bar(22,
                "some String",
                aBaz,
                Bar.Status.COMPLETED,
                ByteString.wrap("fuck yo test".getBytes()),
                false,
                3.14f,
                2.7182818284,
                599L
        );

        ByteBuffer serForm2 = testObj(testBar, testBar);
        deserTest(testBar, testBar, serForm2);
    }

    private void deserTest(Message origMsg,
                           Schema sch,
                           ByteBuffer buf) throws IOException {
        ByteBufferInput input = new ByteBufferInput(buf, false);
        Object newM = sch.newMessage();
        sch.mergeFrom(input, newM);
        assertEquals(origMsg, newM);
    }


    private ByteBuffer testObj(Message msg, Schema sch) throws java.io.IOException {
        // do protostuff now:
        ByteArrayOutputStream controlStream = new ByteArrayOutputStream();
        LinkedBuffer linkedBuffer = LinkedBuffer.allocate(512); // meh
        ProtobufIOUtil.writeTo(controlStream, msg, sch, linkedBuffer);

        byte[] controlData = controlStream.toByteArray();

        // now my new serialization:
        LowCopyProtobufOutput lcpo = new LowCopyProtobufOutput();
        sch.writeTo(lcpo, msg);
        List<ByteBuffer> testDatas = lcpo.buffer.finish();

        assertEquals(1, testDatas.size());


        ByteBuffer testData = testDatas.get(0);

        byte[] testByteArray = new byte[testData.remaining()];
        testData.get(testByteArray);

        assertTrue(
                Arrays.equals(controlData, testByteArray)
        );
        System.out.println("ctrl len = " + controlData.length);
        System.out.println("test len = " + testByteArray.length);
        System.out.println("test size() = " + lcpo.buffer.size());
        System.out.println("test buff count = " + lcpo.buffer.buffers.size());

        testData.rewind();
        return testData;
    }
}
