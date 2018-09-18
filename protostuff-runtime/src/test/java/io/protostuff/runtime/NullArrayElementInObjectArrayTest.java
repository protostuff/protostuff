//========================================================================
//Copyright 2014 David Yu
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

package io.protostuff.runtime;

import io.protostuff.AbstractTest;
import io.protostuff.ByteString;
import io.protostuff.Pipe;
import io.protostuff.Schema;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test for arrays that contain null elements, and the array is in a object[].
 * 
 */
public abstract class NullArrayElementInObjectArrayTest extends AbstractTest
{

    /**
     * Serializes the {@code message} into a byte array.
     */
    protected abstract <T> byte[] toByteArray(T message, Schema<T> schema);

    protected abstract <T> void writeTo(OutputStream out, T message,
            Schema<T> schema) throws IOException;

    /**
     * Deserializes from the byte array.
     */
    protected abstract <T> void mergeFrom(byte[] data, int offset, int length,
            T message, Schema<T> schema) throws IOException;

    /**
     * Deserializes from the inputstream.
     */
    protected abstract <T> void mergeFrom(InputStream in, T message,
            Schema<T> schema) throws IOException;

    protected abstract <T> void roundTrip(T message, Schema<T> schema,
            Pipe.Schema<T> pipeSchema) throws IOException;

    public static final class PojoWithNonPrimitiveArrays
    {
        Object[] objectArray;

        public PojoWithNonPrimitiveArrays()
        {
        }

        public PojoWithNonPrimitiveArrays(Object[] objectArray)
        {
            this.objectArray = objectArray;
        }
    }

    public void testNullAll() throws IOException
    {
        if (!RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT)
        {
            System.err.println("Skipping " + getClass().getSimpleName() + "::testNullAll");
            return;
        }

        Schema<PojoWithNonPrimitiveArrays> schema =
                RuntimeSchema.getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, null, null },
                new Character[] { null, null, null },
                new Short[] { null, null, null },
                new Integer[] { null, null, null },
                new Long[] { null, null, null },
                new Float[] { null, null, null },
                new Double[] { null, null, null },
                new String[] { null, null, null },
                new ByteString[] { null, null, null },
                new byte[][] { null, null, null },
                new BigDecimal[] { null, null, null },
                new BigInteger[] { null, null, null },
                new Date[] { null, null, null }
        };
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(objectArray);

        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertArrayObjectEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertArrayObjectEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }

    public void testNullFirst() throws IOException
    {
        if (!RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT)
        {
            System.err.println("Skipping " + getClass().getSimpleName() + "::testNullFirst");
            return;
        }

        Schema<PojoWithNonPrimitiveArrays> schema =
                RuntimeSchema.getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, true, false },
                new Character[] { null, 'a', 'b' },
                new Short[] { null, 1, 2 },
                new Integer[] { null, 1, 2 },
                new Long[] { null, 1l, 2l },
                new Float[] { null, 1.1f, 2.2f },
                new Double[] { null, 1.1d, 2.2d },
                new String[] { null, "a", "b" },
                new ByteString[] { null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b") },
                new byte[][] { null, new byte[] { 'a' }, new byte[] { 'b' } },
                new BigDecimal[] { null, new BigDecimal(1.1d), new BigDecimal(2.2d) },
                new BigInteger[] { null, new BigInteger("1"), new BigInteger("2") },
                new Date[] { null, new Date(), new Date() }
        };
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(objectArray);

        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertArrayObjectEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertArrayObjectEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }

    public void testNullLast() throws IOException
    {
        if (!RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT)
        {
            System.err.println("Skipping " + getClass().getSimpleName() + "::testNullLast");
            return;
        }

        Schema<PojoWithNonPrimitiveArrays> schema =
                RuntimeSchema.getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { true, false, null },
                new Character[] { 'a', 'b', null },
                new Short[] { 1, 2, null },
                new Integer[] { 1, 2, null },
                new Long[] { 1l, 2l, null },
                new Float[] { 1.1f, 2.2f, null },
                new Double[] { 1.1d, 2.2d, null },
                new String[] { "a", "b", null },
                new ByteString[] { ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null },
                new byte[][] { new byte[] { 'a' }, new byte[] { 'b' }, null },
                new BigDecimal[] { new BigDecimal(1.1d), new BigDecimal(2.2d), null },
                new BigInteger[] { new BigInteger("1"), new BigInteger("2"), null },
                new Date[] { new Date(), new Date(), null }
        };
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(objectArray);

        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertArrayObjectEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertArrayObjectEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }

    public void testNullMid() throws IOException
    {
        if (!RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT)
        {
            System.err.println("Skipping " + getClass().getSimpleName() + "::testNullMid");
            return;
        }

        Schema<PojoWithNonPrimitiveArrays> schema =
                RuntimeSchema.getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { true, null, false },
                new Character[] { 'a', null, 'b' },
                new Short[] { 1, null, 2 },
                new Integer[] { 1, null, 2 },
                new Long[] { 1l, null, 2l },
                new Float[] { 1.1f, null, 2.2f },
                new Double[] { 1.1d, null, 2.2d },
                new String[] { "a", null, "b" },
                new ByteString[] { ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b") },
                new byte[][] { new byte[] { 'a' }, null, new byte[] { 'b' } },
                new BigDecimal[] { new BigDecimal(1.1d), null, new BigDecimal(2.2d) },
                new BigInteger[] { new BigInteger("1"), null, new BigInteger("2") },
                new Date[] { new Date(), null, new Date() }
        };
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(objectArray);

        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertArrayObjectEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertArrayObjectEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }

    public void testNullFirstAndLast() throws IOException
    {
        if (!RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT)
        {
            System.err.println("Skipping " + getClass().getSimpleName() + "::testNullFirstAndLast");
            return;
        }

        Schema<PojoWithNonPrimitiveArrays> schema =
                RuntimeSchema.getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, true, false, null },
                new Character[] { null, 'a', 'b', null },
                new Short[] { null, 1, 2, null },
                new Integer[] { null, 1, 2, null },
                new Long[] { null, 1l, 2l, null },
                new Float[] { null, 1.1f, 2.2f, null },
                new Double[] { null, 1.1d, 2.2d, null },
                new String[] { null, "a", "b", null },
                new ByteString[] { null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null },
                new byte[][] { null, new byte[] { 'a' }, new byte[] { 'b' }, null },
                new BigDecimal[] { null, new BigDecimal(1.1d), new BigDecimal(2.2d), null },
                new BigInteger[] { null, new BigInteger("1"), new BigInteger("2"), null },
                new Date[] { null, new Date(), new Date(), null }
        };
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(objectArray);

        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertArrayObjectEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertArrayObjectEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }

    public void testNullInBetween() throws IOException
    {
        if (!RuntimeEnv.ALLOW_NULL_ARRAY_ELEMENT)
        {
            System.err.println("Skipping " + getClass().getSimpleName() + "::testNullInBetween");
            return;
        }

        Schema<PojoWithNonPrimitiveArrays> schema =
                RuntimeSchema.getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, true, null, false, null },
                new Character[] { null, 'a', null, 'b', null },
                new Short[] { null, 1, null, 2, null },
                new Integer[] { null, 1, null, 2, null },
                new Long[] { null, 1l, null, 2l, null },
                new Float[] { null, 1.1f, null, 2.2f, null },
                new Double[] { null, 1.1d, null, 2.2d, null },
                new String[] { null, "a", null, "b", null },
                new ByteString[] { null, ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b"), null },
                new byte[][] { null, new byte[] { 'a' }, null, new byte[] { 'b' }, null },
                new BigDecimal[] { null, new BigDecimal(1.1d), null, new BigDecimal(2.2d), null },
                new BigInteger[] { null, new BigInteger("1"), null, new BigInteger("2"), null },
                new Date[] { null, new Date(), null, new Date(), null }
        };
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(objectArray);

        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertArrayObjectEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertArrayObjectEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }

    private static void assertArrayObjectEquals(PojoWithNonPrimitiveArrays a1, PojoWithNonPrimitiveArrays a2) {
        assertNotNull(a1);
        assertNotNull(a2);
        assertNotNull(a1.objectArray);
        assertNotNull(a2.objectArray);
        assertEquals(13, a1.objectArray.length);
        assertEquals(13, a2.objectArray.length);
        assertArrayEquals((Boolean[]) a1.objectArray[0], (Boolean[]) a2.objectArray[0]);
        assertArrayEquals((Character[]) a1.objectArray[1], (Character[]) a2.objectArray[1]);
        assertArrayEquals((Short[]) a1.objectArray[2], (Short[]) a2.objectArray[2]);
        assertArrayEquals((Integer[]) a1.objectArray[3], (Integer[]) a2.objectArray[3]);
        assertArrayEquals((Long[]) a1.objectArray[4], (Long[]) a2.objectArray[4]);
        assertArrayEquals((Float[]) a1.objectArray[5], (Float[]) a2.objectArray[5]);
        assertArrayEquals((Double[]) a1.objectArray[6], (Double[]) a2.objectArray[6]);
        assertArrayEquals((String[]) a1.objectArray[7], (String[]) a2.objectArray[7]);
        assertArrayEquals((ByteString[]) a1.objectArray[8], (ByteString[]) a2.objectArray[8]);
        assertArrayEquals((byte[][]) a1.objectArray[9], (byte[][]) a2.objectArray[9]);
        assertArrayEquals((BigDecimal[]) a1.objectArray[10], (BigDecimal[]) a2.objectArray[10]);
        assertArrayEquals((BigInteger[]) a1.objectArray[11], (BigInteger[]) a2.objectArray[11]);
        assertArrayEquals((Date[]) a1.objectArray[12], (Date[]) a2.objectArray[12]);
    }
}
