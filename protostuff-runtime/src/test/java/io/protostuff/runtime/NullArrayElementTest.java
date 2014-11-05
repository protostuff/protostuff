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
import java.util.Arrays;
import java.util.Date;

/**
 * Test for arrays that contain null elements.
 * 
 * @author David Yu
 * @created Sep 29, 2014
 */
public abstract class NullArrayElementTest extends AbstractTest
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
    
    public static final class SomePojo
    {
        String name;
        
        public SomePojo() {}
        
        public SomePojo(String name)
        {
            this.name = name;
        }
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SomePojo other = (SomePojo)obj;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            return true;
        }
    }
    
    public enum Size
    {
        SMALL,
        MEDIUM,
        LARGE;
    }
    
    public static final class PojoWithNonPrimitiveArrays
    {
        Boolean[] boolArray;
        Character[] charArray;
        Short[] shortArray;
        Integer[] intArray;
        Long[] longArray;
        Float[] floatArray;
        Double[] doubleArray;
        String[] stringArray;
        ByteString[] byteStringArray;
        byte[][] byteArrayArray;
        BigDecimal[] bigDecimalArray;
        BigInteger[] bigIntegerArray;
        Date[] dateArray;
        Size[] sizeArray;
        SomePojo[] somePojoArray;
        
        public PojoWithNonPrimitiveArrays() {}

        public PojoWithNonPrimitiveArrays(Boolean[] boolArray, 
                Character[] charArray,
                Short[] shortArray, 
                Integer[] intArray, 
                Long[] longArray, 
                Float[] floatArray,
                Double[] doubleArray, 
                String[] stringArray, 
                ByteString[] byteStringArray,
                byte[][] byteArrayArray, 
                BigDecimal[] bigDecimalArray,
                BigInteger[] bigIntegerArray, 
                Date[] dateArray, 
                Size[] sizeArray,
                SomePojo[] somePojoArray)
        {
            this.boolArray = boolArray;
            this.charArray = charArray;
            this.shortArray = shortArray;
            this.intArray = intArray;
            this.longArray = longArray;
            this.floatArray = floatArray;
            this.doubleArray = doubleArray;
            this.stringArray = stringArray;
            this.byteStringArray = byteStringArray;
            this.byteArrayArray = byteArrayArray;
            this.bigDecimalArray = bigDecimalArray;
            this.bigIntegerArray = bigIntegerArray;
            this.dateArray = dateArray;
            this.sizeArray = sizeArray;
            this.somePojoArray = somePojoArray;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(bigDecimalArray);
            result = prime * result + Arrays.hashCode(bigIntegerArray);
            result = prime * result + Arrays.hashCode(boolArray);
            result = prime * result + Arrays.hashCode(byteArrayArray);
            result = prime * result + Arrays.hashCode(byteStringArray);
            result = prime * result + Arrays.hashCode(charArray);
            result = prime * result + Arrays.hashCode(dateArray);
            result = prime * result + Arrays.hashCode(doubleArray);
            result = prime * result + Arrays.hashCode(floatArray);
            result = prime * result + Arrays.hashCode(intArray);
            result = prime * result + Arrays.hashCode(longArray);
            result = prime * result + Arrays.hashCode(shortArray);
            result = prime * result + Arrays.hashCode(sizeArray);
            result = prime * result + Arrays.hashCode(somePojoArray);
            result = prime * result + Arrays.hashCode(stringArray);
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoWithNonPrimitiveArrays other = (PojoWithNonPrimitiveArrays)obj;
            if (!Arrays.equals(bigDecimalArray, other.bigDecimalArray))
                return false;
            if (!Arrays.equals(bigIntegerArray, other.bigIntegerArray))
                return false;
            if (!Arrays.equals(boolArray, other.boolArray))
                return false;
            if (!Arrays.deepEquals(byteArrayArray, other.byteArrayArray))
                return false;
            if (!Arrays.equals(byteStringArray, other.byteStringArray))
                return false;
            if (!Arrays.equals(charArray, other.charArray))
                return false;
            if (!Arrays.equals(dateArray, other.dateArray))
                return false;
            if (!Arrays.equals(doubleArray, other.doubleArray))
                return false;
            if (!Arrays.equals(floatArray, other.floatArray))
                return false;
            if (!Arrays.equals(intArray, other.intArray))
                return false;
            if (!Arrays.equals(longArray, other.longArray))
                return false;
            if (!Arrays.equals(shortArray, other.shortArray))
                return false;
            if (!Arrays.equals(sizeArray, other.sizeArray))
                return false;
            if (!Arrays.equals(somePojoArray, other.somePojoArray))
                return false;
            if (!Arrays.equals(stringArray, other.stringArray))
                return false;
            return true;
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
                ((MappedSchema<PojoWithNonPrimitiveArrays>) schema).pipeSchema;
        
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
                new Boolean[]{null, null, null}, 
                new Character[]{null, null, null}, 
                new Short[]{null, null, null}, 
                new Integer[]{null, null, null}, 
                new Long[]{null, null, null}, 
                new Float[]{null, null, null}, 
                new Double[]{null, null, null}, 
                new String[]{null, null, null}, 
                new ByteString[]{null, null, null}, 
                new byte[][]{null, null, null}, 
                new BigDecimal[]{null, null, null}, 
                new BigInteger[]{null, null, null}, 
                new Date[]{null, null, null}, 
                new Size[]{null, null, null}, 
                new SomePojo[]{null, null, null}
                );
        
        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

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
                ((MappedSchema<PojoWithNonPrimitiveArrays>) schema).pipeSchema;
        
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
                new Boolean[]{null, true, false}, 
                new Character[]{null, 'a', 'b'}, 
                new Short[]{null, 1, 2}, 
                new Integer[]{null, 1, 2}, 
                new Long[]{null, 1l, 2l}, 
                new Float[]{null, 1.1f, 2.2f}, 
                new Double[]{null, 1.1d, 2.2d}, 
                new String[]{null, "a", "b"}, 
                new ByteString[]{null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b")}, 
                new byte[][]{null, new byte[]{'a'}, new byte[]{'b'}}, 
                new BigDecimal[]{null, new BigDecimal(1.1d), new BigDecimal(2.2d)}, 
                new BigInteger[]{null, new BigInteger("1"), new BigInteger("2")}, 
                new Date[]{null, new Date(), new Date()}, 
                new Size[]{null, Size.MEDIUM, Size.LARGE}, 
                new SomePojo[]{null, new SomePojo("a"), new SomePojo("b")}
                );
        
        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

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
                ((MappedSchema<PojoWithNonPrimitiveArrays>) schema).pipeSchema;
        
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
                new Boolean[]{true, false, null},
                new Character[]{'a', 'b', null},
                new Short[]{1, 2, null},
                new Integer[]{1, 2, null},
                new Long[]{1l, 2l, null},
                new Float[]{1.1f, 2.2f, null},
                new Double[]{1.1d, 2.2d, null},
                new String[]{"a", "b", null},
                new ByteString[]{ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null},
                new byte[][]{new byte[]{'a'}, new byte[]{'b'}, null},
                new BigDecimal[]{new BigDecimal(1.1d), new BigDecimal(2.2d), null},
                new BigInteger[]{new BigInteger("1"), new BigInteger("2"), null},
                new Date[]{new Date(), new Date(), null},
                new Size[]{Size.MEDIUM, Size.LARGE, null},
                new SomePojo[]{new SomePojo("a"), new SomePojo("b"), null}
                );
        
        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

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
                ((MappedSchema<PojoWithNonPrimitiveArrays>) schema).pipeSchema;
        
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
                new Boolean[]{true, null, false},
                new Character[]{'a', null, 'b'},
                new Short[]{1, null, 2},
                new Integer[]{1, null, 2},
                new Long[]{1l, null, 2l},
                new Float[]{1.1f, null, 2.2f},
                new Double[]{1.1d, null, 2.2d},
                new String[]{"a", null, "b"},
                new ByteString[]{ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b")},
                new byte[][]{new byte[]{'a'}, null, new byte[]{'b'}},
                new BigDecimal[]{new BigDecimal(1.1d), null, new BigDecimal(2.2d)},
                new BigInteger[]{new BigInteger("1"), null, new BigInteger("2")},
                new Date[]{new Date(), null, new Date()},
                new Size[]{Size.MEDIUM, null, Size.LARGE},
                new SomePojo[]{new SomePojo("a"), null, new SomePojo("b")}
                );
        
        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

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
                ((MappedSchema<PojoWithNonPrimitiveArrays>) schema).pipeSchema;
        
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
                new Boolean[]{null, true, false, null},
                new Character[]{null, 'a', 'b', null},
                new Short[]{null, 1, 2, null},
                new Integer[]{null, 1, 2, null},
                new Long[]{null, 1l, 2l, null},
                new Float[]{null, 1.1f, 2.2f, null},
                new Double[]{null, 1.1d, 2.2d, null},
                new String[]{null, "a", "b", null},
                new ByteString[]{null, ByteString.copyFromUtf8("a"), ByteString.copyFromUtf8("b"), null},
                new byte[][]{null, new byte[]{'a'},new byte[]{'b'}, null},
                new BigDecimal[]{null, new BigDecimal(1.1d), new BigDecimal(2.2d), null},
                new BigInteger[]{null, new BigInteger("1"), new BigInteger("2"), null},
                new Date[]{null, new Date(), new Date(), null},
                new Size[]{null, Size.MEDIUM, Size.LARGE, null},
                new SomePojo[]{null, new SomePojo("a"), new SomePojo("b"), null}
                );
        
        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

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
                ((MappedSchema<PojoWithNonPrimitiveArrays>) schema).pipeSchema;
        
        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
                new Boolean[]{null, true, null, false, null},
                new Character[]{null, 'a', null, 'b', null},
                new Short[]{null, 1, null, 2, null},
                new Integer[]{null, 1, null, 2, null},
                new Long[]{null, 1l, null, 2l, null},
                new Float[]{null, 1.1f, null, 2.2f, null},
                new Double[]{null, 1.1d, null, 2.2d, null},
                new String[]{null, "a", null, "b", null},
                new ByteString[]{null, ByteString.copyFromUtf8("a"), null, ByteString.copyFromUtf8("b"), null},
                new byte[][]{null, new byte[]{'a'}, null, new byte[]{'b'}, null},
                new BigDecimal[]{null, new BigDecimal(1.1d), null, new BigDecimal(2.2d), null},
                new BigInteger[]{null, new BigInteger("1"), null, new BigInteger("2"), null},
                new Date[]{null, new Date(), null, new Date(), null},
                new Size[]{null, Size.MEDIUM, null, Size.LARGE, null},
                new SomePojo[]{null, new SomePojo("a"), null, new SomePojo("b"), null}
                );
        
        byte[] data = toByteArray(p, schema);

        PojoWithNonPrimitiveArrays pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithNonPrimitiveArrays pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }
}
