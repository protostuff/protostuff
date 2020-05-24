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

import io.protostuff.ByteString;
import io.protostuff.Pipe;
import io.protostuff.Schema;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test for arrays that contain null elements, and the array is in a object[].
 * 
 */
public abstract class NullArrayElementInObjectArrayTest extends AbstractTest
{
    
    final DefaultIdStrategy strategy = new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS | 
            IdStrategy.PRESERVE_NULL_ELEMENTS);
    
    @Override
    protected IdStrategy newIdStrategy()
    {
        return strategy;
    }

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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, null, null },
                Arrays.asList(new Boolean[] { null, null, null }),
                
                new Character[] { null, null, null },
                Arrays.asList(new Character[] { null, null, null }),
                
                new Short[] { null, null, null },
                Arrays.asList(new Short[] { null, null, null }),
                
                new Integer[] { null, null, null },
                Arrays.asList(new Integer[] { null, null, null }),
                
                new Long[] { null, null, null },
                Arrays.asList(new Long[] { null, null, null }),
                
                new Float[] { null, null, null },
                Arrays.asList(new Float[] { null, null, null }),
                
                new Double[] { null, null, null },
                Arrays.asList(new Double[] { null, null, null }),
                
                new String[] { null, null, null },
                Arrays.asList(new String[] { null, null, null }),
                
                new ByteString[] { null, null, null },
                Arrays.asList(new ByteString[] { null, null, null }),
                
                new byte[][] { null, null, null },
                Arrays.asList(new byte[][] { null, null, null }),
                
                new BigDecimal[] { null, null, null },
                Arrays.asList(new BigDecimal[] { null, null, null }),
                
                new BigInteger[] { null, null, null },
                Arrays.asList(new BigInteger[] { null, null, null }),
                
                new Date[] { null, null, null },
                Arrays.asList(new Date[] { null, null, null }),
                
                new Baz[] { null, null, null },
                Arrays.asList(new Baz[] { null, null, null }),
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, true, false },
                Arrays.asList(new Boolean[] { null, true, false }),
                
                new Character[] { null, 'a', 'b' },
                Arrays.asList(new Character[] { null, 'a', 'b' }),
                
                new Short[] { null, 1, 2 },
                Arrays.asList(new Short[] { null, 1, 2 }),
                
                new Integer[] { null, 1, 2 },
                Arrays.asList(new Integer[] { null, 1, 2 }),
                
                new Long[] { null, 1l, 2l },
                Arrays.asList(new Long[] { null, 1l, 2l }),
                
                new Float[] { null, 1.1f, 2.2f },
                Arrays.asList(new Float[] { null, 1.1f, 2.2f }),
                
                new Double[] { null, 1.1d, 2.2d },
                Arrays.asList(new Double[] { null, 1.1d, 2.2d }),
                
                new String[] { null, "a", "b" },
                Arrays.asList(new String[] { null, "a", "b" }),
                
                new ByteString[] { null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b") },
                Arrays.asList(new ByteString[] { null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b") }),
                
                new byte[][] { null, new byte[] { 'a' }, new byte[] { 'b' } },
                Arrays.asList(new byte[][] { null, new byte[] { 'a' }, new byte[] { 'b' } }),
                
                new BigDecimal[] { null, new BigDecimal(1.1d), new BigDecimal(2.2d) },
                Arrays.asList(new BigDecimal[] { null, new BigDecimal(1.1d), new BigDecimal(2.2d) }),
                
                new BigInteger[] { null, new BigInteger("1"), new BigInteger("2") },
                Arrays.asList(new BigInteger[] { null, new BigInteger("1"), new BigInteger("2") }),
                
                new Date[] { null, new Date(), new Date() },
                Arrays.asList(new Date[] { null, new Date(), new Date() }),
                
                new Baz[] { null, new Baz(0, "0", 0), new Baz(1, "1", 1) },
                Arrays.asList(new Baz[] { null, new Baz(0, "0", 0), new Baz(1, "1", 1) })
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { true, false, null },
                Arrays.asList(new Boolean[] { true, false, null }),
                
                new Character[] { 'a', 'b', null },
                Arrays.asList(new Character[] { 'a', 'b', null }),
                
                new Short[] { 1, 2, null },
                Arrays.asList(new Short[] { 1, 2, null }),
                
                new Integer[] { 1, 2, null },
                Arrays.asList(new Integer[] { 1, 2, null }),
                
                new Long[] { 1l, 2l, null },
                Arrays.asList(new Long[] { 1l, 2l, null }),
                
                new Float[] { 1.1f, 2.2f, null },
                Arrays.asList(new Float[] { 1.1f, 2.2f, null }),
                
                new Double[] { 1.1d, 2.2d, null },
                Arrays.asList(new Double[] { 1.1d, 2.2d, null }),
                
                new String[] { "a", "b", null },
                Arrays.asList(new String[] { "a", "b", null }),
                
                new ByteString[] { ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null },
                Arrays.asList(new ByteString[] { ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null }),
                
                new byte[][] { new byte[] { 'a' }, new byte[] { 'b' }, null },
                Arrays.asList(new byte[][] { new byte[] { 'a' }, new byte[] { 'b' }, null }),
                
                new BigDecimal[] { new BigDecimal(1.1d), new BigDecimal(2.2d), null },
                Arrays.asList(new BigDecimal[] { new BigDecimal(1.1d), new BigDecimal(2.2d), null }),
                
                new BigInteger[] { new BigInteger("1"), new BigInteger("2"), null },
                Arrays.asList(new BigInteger[] { new BigInteger("1"), new BigInteger("2"), null }),
                
                new Date[] { new Date(), new Date(), null },
                Arrays.asList(new Date[] { new Date(), new Date(), null }),
                
                new Baz[] { new Baz(0, "0", 0), new Baz(1, "1", 1), null },
                Arrays.asList(new Baz[] { new Baz(0, "0", 0), new Baz(1, "1", 1), null })
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { true, null, false },
                Arrays.asList(new Boolean[] { true, null, false }),
                
                new Character[] { 'a', null, 'b' },
                Arrays.asList(new Character[] { 'a', null, 'b' }),
                
                new Short[] { 1, null, 2 },
                Arrays.asList(new Short[] { 1, null, 2 }),
                
                new Integer[] { 1, null, 2 },
                Arrays.asList(new Integer[] { 1, null, 2 }),
                
                new Long[] { 1l, null, 2l },
                Arrays.asList(new Long[] { 1l, null, 2l }),
                
                new Float[] { 1.1f, null, 2.2f },
                Arrays.asList(new Float[] { 1.1f, null, 2.2f }),
                
                new Double[] { 1.1d, null, 2.2d },
                Arrays.asList(new Double[] { 1.1d, null, 2.2d }),
                
                new String[] { "a", null, "b" },
                Arrays.asList(new String[] { "a", null, "b" }),
                
                new ByteString[] { ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b") },
                Arrays.asList(new ByteString[] { ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b") }),
                
                new byte[][] { new byte[] { 'a' }, null, new byte[] { 'b' } },
                Arrays.asList(new byte[][] { new byte[] { 'a' }, null, new byte[] { 'b' } }),
                
                new BigDecimal[] { new BigDecimal(1.1d), null, new BigDecimal(2.2d) },
                Arrays.asList(new BigDecimal[] { new BigDecimal(1.1d), null, new BigDecimal(2.2d) }),
                
                new BigInteger[] { new BigInteger("1"), null, new BigInteger("2") },
                Arrays.asList(new BigInteger[] { new BigInteger("1"), null, new BigInteger("2") }),
                
                new Date[] { new Date(), null, new Date() },
                Arrays.asList(new Date[] { new Date(), null, new Date() }),
                
                new Baz[] { new Baz(0, "0", 0), null, new Baz(1, "1", 1) },
                Arrays.asList(new Baz[] { new Baz(0, "0", 0), null, new Baz(1, "1", 1) })
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, true, false, null },
                Arrays.asList(new Boolean[] { null, true, false, null }),
                
                new Character[] { null, 'a', 'b', null },
                Arrays.asList(new Character[] { null, 'a', 'b', null }),
                
                new Short[] { null, 1, 2, null },
                Arrays.asList(new Short[] { null, 1, 2, null }),
                
                new Integer[] { null, 1, 2, null },
                Arrays.asList(new Integer[] { null, 1, 2, null }),
                
                new Long[] { null, 1l, 2l, null },
                Arrays.asList(new Long[] { null, 1l, 2l, null }),
                
                new Float[] { null, 1.1f, 2.2f, null },
                Arrays.asList(new Float[] { null, 1.1f, 2.2f, null }),
                
                new Double[] { null, 1.1d, 2.2d, null },
                Arrays.asList(new Double[] { null, 1.1d, 2.2d, null }),
                
                new String[] { null, "a", "b", null },
                Arrays.asList(new String[] { null, "a", "b", null }),
                
                new ByteString[] { null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null },
                Arrays.asList(new ByteString[] { null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null }),
                
                new byte[][] { null, new byte[] { 'a' }, new byte[] { 'b' }, null },
                Arrays.asList(new byte[][] { null, new byte[] { 'a' }, new byte[] { 'b' }, null }),
                
                new BigDecimal[] { null, new BigDecimal(1.1d), new BigDecimal(2.2d), null },
                Arrays.asList(new BigDecimal[] { null, new BigDecimal(1.1d), new BigDecimal(2.2d), null }),
                
                new BigInteger[] { null, new BigInteger("1"), new BigInteger("2"), null },
                Arrays.asList(new BigInteger[] { null, new BigInteger("1"), new BigInteger("2"), null }),
                
                new Date[] { null, new Date(), new Date(), null },
                Arrays.asList(new Date[] { null, new Date(), new Date(), null }),
                
                new Baz[] { null, new Baz(0, "0", 0), new Baz(1, "1", 1), null },
                Arrays.asList(new Baz[] { null, new Baz(0, "0", 0), new Baz(1, "1", 1), null })
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        Object[] objectArray = new Object[] {
                new Boolean[] { null, true, null, false, null },
                Arrays.asList(new Boolean[] { null, true, null, false, null }),
                
                new Character[] { null, 'a', null, 'b', null },
                Arrays.asList(new Character[] { null, 'a', null, 'b', null }),
                
                new Short[] { null, 1, null, 2, null },
                Arrays.asList(new Short[] { null, 1, null, 2, null }),
                
                new Integer[] { null, 1, null, 2, null },
                Arrays.asList(new Integer[] { null, 1, null, 2, null }),
                
                new Long[] { null, 1l, null, 2l, null },
                Arrays.asList(new Long[] { null, 1l, null, 2l, null }),
                
                new Float[] { null, 1.1f, null, 2.2f, null },
                Arrays.asList(new Float[] { null, 1.1f, null, 2.2f, null }),
                
                new Double[] { null, 1.1d, null, 2.2d, null },
                Arrays.asList(new Double[] { null, 1.1d, null, 2.2d, null }),
                
                new String[] { null, "a", null, "b", null },
                Arrays.asList(new String[] { null, "a", null, "b", null }),
                
                new ByteString[] { null, ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b"), null },
                Arrays.asList(new ByteString[] { null, ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b"), null }),
                
                new byte[][] { null, new byte[] { 'a' }, null, new byte[] { 'b' }, null },
                Arrays.asList(new byte[][] { null, new byte[] { 'a' }, null, new byte[] { 'b' }, null }),
                
                new BigDecimal[] { null, new BigDecimal(1.1d), null, new BigDecimal(2.2d), null },
                Arrays.asList(new BigDecimal[] { null, new BigDecimal(1.1d), null, new BigDecimal(2.2d), null }),
                
                new BigInteger[] { null, new BigInteger("1"), null, new BigInteger("2"), null },
                Arrays.asList(new BigInteger[] { null, new BigInteger("1"), null, new BigInteger("2"), null }),
                
                new Date[] { null, new Date(), null, new Date(), null },
                Arrays.asList(new Date[] { null, new Date(), null, new Date(), null }),
                
                new Baz[] { null, new Baz(0, "0", 0), null, new Baz(1, "1", 1), null },
                Arrays.asList(new Baz[] { null, new Baz(0, "0", 0), null, new Baz(1, "1", 1), null })
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
        assertEquals(28, a1.objectArray.length);
        assertEquals(28, a2.objectArray.length);
        
        assertArrayEquals((Boolean[]) a1.objectArray[0], (Boolean[]) a2.objectArray[0]);
        assertEquals(a1.objectArray[1], a2.objectArray[1]);
        
        assertArrayEquals((Character[]) a1.objectArray[2], (Character[]) a2.objectArray[2]);
        assertEquals(a1.objectArray[3], a2.objectArray[3]);
        
        assertArrayEquals((Short[]) a1.objectArray[4], (Short[]) a2.objectArray[4]);
        assertEquals(a1.objectArray[5], a2.objectArray[5]);
        
        assertArrayEquals((Integer[]) a1.objectArray[6], (Integer[]) a2.objectArray[6]);
        assertEquals(a1.objectArray[7], a2.objectArray[7]);
        
        assertArrayEquals((Long[]) a1.objectArray[8], (Long[]) a2.objectArray[8]);
        assertEquals(a1.objectArray[9], a2.objectArray[9]);
        
        assertArrayEquals((Float[]) a1.objectArray[10], (Float[]) a2.objectArray[10]);
        assertEquals(a1.objectArray[11], a2.objectArray[11]);
        
        assertArrayEquals((Double[]) a1.objectArray[12], (Double[]) a2.objectArray[12]);
        assertEquals(a1.objectArray[13], a2.objectArray[13]);
        
        assertArrayEquals((String[]) a1.objectArray[14], (String[]) a2.objectArray[14]);
        assertEquals(a1.objectArray[15], a2.objectArray[15]);
        
        assertArrayEquals((ByteString[]) a1.objectArray[16], (ByteString[]) a2.objectArray[16]);
        assertEquals(a1.objectArray[17], a2.objectArray[17]);
        
        assertArrayEquals((byte[][]) a1.objectArray[18], (byte[][]) a2.objectArray[18]);
        @SuppressWarnings("unchecked")
        List<byte[]> l1 = (List<byte[]>)a1.objectArray[19], l2 = (List<byte[]>)a2.objectArray[19];
        assertNotNull(l1);
        assertNotNull(l2);
        assertEquals(l1.size(), l2.size());
        for (int i = 0, len = l1.size(); i < len; i++)
            assertTrue(Arrays.equals(l1.get(i), l2.get(i)));
        
        assertArrayEquals((BigDecimal[]) a1.objectArray[20], (BigDecimal[]) a2.objectArray[20]);
        assertEquals(a1.objectArray[21], a2.objectArray[21]);
        
        assertArrayEquals((BigInteger[]) a1.objectArray[22], (BigInteger[]) a2.objectArray[22]);
        assertEquals(a1.objectArray[23], a2.objectArray[23]);
        
        assertArrayEquals((Date[]) a1.objectArray[24], (Date[]) a2.objectArray[24]);
        assertEquals(a1.objectArray[25], a2.objectArray[25]);
        
        assertArrayEquals((Baz[]) a1.objectArray[26], (Baz[]) a2.objectArray[26]);
        assertEquals(a1.objectArray[27], a2.objectArray[27]);
    }
}
