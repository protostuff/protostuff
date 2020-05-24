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
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Test for arrays that contain null elements.
 * 
 * @author David Yu
 * @created Sep 29, 2014
 */
public abstract class NullArrayElementTest extends AbstractTest
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

    public static final class SomePojo
    {
        String name;

        public SomePojo()
        {
        }

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
            SomePojo other = (SomePojo) obj;
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
        List<Boolean> boolList;
        
        Character[] charArray;
        List<Character> charList;
        
        Short[] shortArray;
        List<Short> shortList;
        
        Integer[] intArray;
        List<Integer> intList;
        
        Long[] longArray;
        List<Long> longList;
        
        Float[] floatArray;
        List<Float> floatList;
        
        Double[] doubleArray;
        List<Double> doubleList;
        
        String[] stringArray;
        List<String> stringList;
        
        ByteString[] byteStringArray;
        List<ByteString> byteStringList;
        
        byte[][] byteArrayArray;
        List<byte[]> byteArrayList;
        
        BigDecimal[] bigDecimalArray;
        List<BigDecimal> bigDecimalList;
        
        BigInteger[] bigIntegerArray;
        List<BigInteger> bigIntegerList;
        
        Date[] dateArray;
        List<Date> dateList;
        
        Size[] sizeArray;
        List<Size> sizeList;
        
        SomePojo[] somePojoArray;
        List<SomePojo> somePojoList;

        public PojoWithNonPrimitiveArrays()
        {
        }

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
            this.boolList = Arrays.asList(boolArray);
            
            this.charArray = charArray;
            this.charList = Arrays.asList(charArray);
            
            this.shortArray = shortArray;
            this.shortList = Arrays.asList(shortArray);
            
            this.intArray = intArray;
            this.intList = Arrays.asList(intArray);
            
            this.longArray = longArray;
            this.longList = Arrays.asList(longArray);
            
            this.floatArray = floatArray;
            this.floatList = Arrays.asList(floatArray);
            
            this.doubleArray = doubleArray;
            this.doubleList = Arrays.asList(doubleArray);
            
            this.stringArray = stringArray;
            this.stringList = Arrays.asList(stringArray);
            
            this.byteStringArray = byteStringArray;
            this.byteStringList = Arrays.asList(byteStringArray);
            
            this.byteArrayArray = byteArrayArray;
            this.byteArrayList = Arrays.asList(byteArrayArray);
            
            this.bigDecimalArray = bigDecimalArray;
            this.bigDecimalList = Arrays.asList(bigDecimalArray);
            
            this.bigIntegerArray = bigIntegerArray;
            this.bigIntegerList = Arrays.asList(bigIntegerArray);
            
            this.dateArray = dateArray;
            this.dateList = Arrays.asList(dateArray);
            
            this.sizeArray = sizeArray;
            this.sizeList = Arrays.asList(sizeArray);
            
            this.somePojoArray = somePojoArray;
            this.somePojoList = Arrays.asList(somePojoArray);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(bigDecimalArray);
            result = prime * result + ((bigDecimalList == null) ? 0 : bigDecimalList.hashCode());
            result = prime * result + Arrays.hashCode(bigIntegerArray);
            result = prime * result + ((bigIntegerList == null) ? 0 : bigIntegerList.hashCode());
            result = prime * result + Arrays.hashCode(boolArray);
            result = prime * result + ((boolList == null) ? 0 : boolList.hashCode());
            result = prime * result + Arrays.hashCode(byteArrayArray);
            result = prime * result + ((byteArrayList == null) ? 0 : byteArrayList.hashCode());
            result = prime * result + Arrays.hashCode(byteStringArray);
            result = prime * result + ((byteStringList == null) ? 0 : byteStringList.hashCode());
            result = prime * result + Arrays.hashCode(charArray);
            result = prime * result + ((charList == null) ? 0 : charList.hashCode());
            result = prime * result + Arrays.hashCode(dateArray);
            result = prime * result + ((dateList == null) ? 0 : dateList.hashCode());
            result = prime * result + Arrays.hashCode(doubleArray);
            result = prime * result + ((doubleList == null) ? 0 : doubleList.hashCode());
            result = prime * result + Arrays.hashCode(floatArray);
            result = prime * result + ((floatList == null) ? 0 : floatList.hashCode());
            result = prime * result + Arrays.hashCode(intArray);
            result = prime * result + ((intList == null) ? 0 : intList.hashCode());
            result = prime * result + Arrays.hashCode(longArray);
            result = prime * result + ((longList == null) ? 0 : longList.hashCode());
            result = prime * result + Arrays.hashCode(shortArray);
            result = prime * result + ((shortList == null) ? 0 : shortList.hashCode());
            result = prime * result + Arrays.hashCode(sizeArray);
            result = prime * result + ((sizeList == null) ? 0 : sizeList.hashCode());
            result = prime * result + Arrays.hashCode(somePojoArray);
            result = prime * result + ((somePojoList == null) ? 0 : somePojoList.hashCode());
            result = prime * result + Arrays.hashCode(stringArray);
            result = prime * result + ((stringList == null) ? 0 : stringList.hashCode());
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
            if (bigDecimalList == null)
            {
                if (other.bigDecimalList != null)
                    return false;
            }
            else if (!bigDecimalList.equals(other.bigDecimalList))
                return false;
            if (!Arrays.equals(bigIntegerArray, other.bigIntegerArray))
                return false;
            if (bigIntegerList == null)
            {
                if (other.bigIntegerList != null)
                    return false;
            }
            else if (!bigIntegerList.equals(other.bigIntegerList))
                return false;
            if (!Arrays.equals(boolArray, other.boolArray))
                return false;
            if (boolList == null)
            {
                if (other.boolList != null)
                    return false;
            }
            else if (!boolList.equals(other.boolList))
                return false;
            if (!Arrays.deepEquals(byteArrayArray, other.byteArrayArray))
                return false;
            if (byteArrayList == null)
            {
                if (other.byteArrayList != null)
                    return false;
            }
            //else if (!byteArrayList.equals(other.byteArrayList))
            //    return false;
            else
            {
                // custom code here because byte[] doesn't have an Object.equals impl.
                if (other.byteArrayList == null || byteArrayList.size() != other.byteArrayList.size())
                    return false;
                
                for (int i = 0, len = byteArrayList.size(); i < len; i++)
                {
                    if (!Arrays.equals(byteArrayList.get(i), other.byteArrayList.get(i)))
                        return false;
                }
            }
            if (!Arrays.equals(byteStringArray, other.byteStringArray))
                return false;
            if (byteStringList == null)
            {
                if (other.byteStringList != null)
                    return false;
            }
            else if (!byteStringList.equals(other.byteStringList))
                return false;
            if (!Arrays.equals(charArray, other.charArray))
                return false;
            if (charList == null)
            {
                if (other.charList != null)
                    return false;
            }
            else if (!charList.equals(other.charList))
                return false;
            if (!Arrays.equals(dateArray, other.dateArray))
                return false;
            if (dateList == null)
            {
                if (other.dateList != null)
                    return false;
            }
            else if (!dateList.equals(other.dateList))
                return false;
            if (!Arrays.equals(doubleArray, other.doubleArray))
                return false;
            if (doubleList == null)
            {
                if (other.doubleList != null)
                    return false;
            }
            else if (!doubleList.equals(other.doubleList))
                return false;
            if (!Arrays.equals(floatArray, other.floatArray))
                return false;
            if (floatList == null)
            {
                if (other.floatList != null)
                    return false;
            }
            else if (!floatList.equals(other.floatList))
                return false;
            if (!Arrays.equals(intArray, other.intArray))
                return false;
            if (intList == null)
            {
                if (other.intList != null)
                    return false;
            }
            else if (!intList.equals(other.intList))
                return false;
            if (!Arrays.equals(longArray, other.longArray))
                return false;
            if (longList == null)
            {
                if (other.longList != null)
                    return false;
            }
            else if (!longList.equals(other.longList))
                return false;
            if (!Arrays.equals(shortArray, other.shortArray))
                return false;
            if (shortList == null)
            {
                if (other.shortList != null)
                    return false;
            }
            else if (!shortList.equals(other.shortList))
                return false;
            if (!Arrays.equals(sizeArray, other.sizeArray))
                return false;
            if (sizeList == null)
            {
                if (other.sizeList != null)
                    return false;
            }
            else if (!sizeList.equals(other.sizeList))
                return false;
            if (!Arrays.equals(somePojoArray, other.somePojoArray))
                return false;
            if (somePojoList == null)
            {
                if (other.somePojoList != null)
                    return false;
            }
            else if (!somePojoList.equals(other.somePojoList))
                return false;
            if (!Arrays.equals(stringArray, other.stringArray))
                return false;
            if (stringList == null)
            {
                if (other.stringList != null)
                    return false;
            }
            else if (!stringList.equals(other.stringList))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PojoWithNonPrimitiveArrays [boolArray=" + Arrays.toString(boolArray)
                    + ", boolList=" + boolList + ", charArray=" + Arrays.toString(charArray)
                    + ", charList=" + charList + ", shortArray=" + Arrays.toString(shortArray)
                    + ", shortList=" + shortList + ", intArray=" + Arrays.toString(intArray)
                    + ", intList=" + intList + ", longArray=" + Arrays.toString(longArray)
                    + ", longList=" + longList + ", floatArray=" + Arrays.toString(floatArray)
                    + ", floatList=" + floatList + ", doubleArray=" + Arrays.toString(doubleArray)
                    + ", doubleList=" + doubleList + ", stringArray="
                    + Arrays.toString(stringArray) + ", stringList=" + stringList
                    + ", byteStringArray=" + Arrays.toString(byteStringArray) + ", byteStringList="
                    + byteStringList + ", byteArrayArray=" + Arrays.toString(byteArrayArray)
                    + ", byteArrayList=" + byteArrayList + ", bigDecimalArray="
                    + Arrays.toString(bigDecimalArray) + ", bigDecimalList=" + bigDecimalList
                    + ", bigIntegerArray=" + Arrays.toString(bigIntegerArray) + ", bigIntegerList="
                    + bigIntegerList + ", dateArray=" + Arrays.toString(dateArray) + ", dateList="
                    + dateList + ", sizeArray=" + Arrays.toString(sizeArray) + ", sizeList="
                    + sizeList + ", somePojoArray=" + Arrays.toString(somePojoArray)
                    + ", somePojoList=" + somePojoList + "]";
        }
    }

    public void testNullAll() throws IOException
    {
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
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
                new Date[] { null, null, null },
                new Size[] { null, null, null },
                new SomePojo[] { null, null, null }
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
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
                new Date[] { null, new Date(), new Date() },
                new Size[] { null, Size.MEDIUM, Size.LARGE },
                new SomePojo[] { null, new SomePojo("a"), new SomePojo("b") }
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
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
                new Date[] { new Date(), new Date(), null },
                new Size[] { Size.MEDIUM, Size.LARGE, null },
                new SomePojo[] { new SomePojo("a"), new SomePojo("b"), null }
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
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
                new Date[] { new Date(), null, new Date() },
                new Size[] { Size.MEDIUM, null, Size.LARGE },
                new SomePojo[] { new SomePojo("a"), null, new SomePojo("b") }
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
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
                new Date[] { null, new Date(), new Date(), null },
                new Size[] { null, Size.MEDIUM, Size.LARGE, null },
                new SomePojo[] { null, new SomePojo("a"), new SomePojo("b"), null }
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
        Schema<PojoWithNonPrimitiveArrays> schema =
                getSchema(PojoWithNonPrimitiveArrays.class);

        Pipe.Schema<PojoWithNonPrimitiveArrays> pipeSchema =
                ((RuntimeSchema<PojoWithNonPrimitiveArrays>) schema).getPipeSchema();

        PojoWithNonPrimitiveArrays p = new PojoWithNonPrimitiveArrays(
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
                new Date[] { null, new Date(), null, new Date(), null },
                new Size[] { null, Size.MEDIUM, null, Size.LARGE, null },
                new SomePojo[] { null, new SomePojo("a"), null, new SomePojo("b"), null }
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
    
    public static final class PojoWithArrayInList
    {
        ArrayList<Boolean[]> boolArrayList = new ArrayList<Boolean[]>();
        ArrayList<Character[]> charArrayList = new ArrayList<Character[]>();
        ArrayList<Short[]> shortArrayList = new ArrayList<Short[]>();
        ArrayList<Integer[]> intArrayList = new ArrayList<Integer[]>();
        ArrayList<Long[]> longArrayList = new ArrayList<Long[]>();
        ArrayList<Float[]> floatArrayList = new ArrayList<Float[]>();
        ArrayList<Double[]> doubleArrayList = new ArrayList<Double[]>();
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((boolArrayList == null) ? 0 : boolArrayList.hashCode());
            result = prime * result + ((charArrayList == null) ? 0 : charArrayList.hashCode());
            result = prime * result + ((doubleArrayList == null) ? 0 : doubleArrayList.hashCode());
            result = prime * result + ((floatArrayList == null) ? 0 : floatArrayList.hashCode());
            result = prime * result + ((intArrayList == null) ? 0 : intArrayList.hashCode());
            result = prime * result + ((longArrayList == null) ? 0 : longArrayList.hashCode());
            result = prime * result + ((shortArrayList == null) ? 0 : shortArrayList.hashCode());
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
            PojoWithArrayInList other = (PojoWithArrayInList)obj;
            if (boolArrayList == null)
            {
                if (other.boolArrayList != null)
                    return false;
            }
            else if (!Arrays.asList(boolArrayList.get(0)).equals(Arrays.asList(other.boolArrayList.get(0))))
                return false;
            if (charArrayList == null)
            {
                if (other.charArrayList != null)
                    return false;
            }
            else if (!Arrays.asList(charArrayList.get(0)).equals(Arrays.asList(other.charArrayList.get(0))))
                return false;
            if (doubleArrayList == null)
            {
                if (other.doubleArrayList != null)
                    return false;
            }
            else if (!Arrays.asList(doubleArrayList.get(0)).equals(Arrays.asList(other.doubleArrayList.get(0))))
                return false;
            if (floatArrayList == null)
            {
                if (other.floatArrayList != null)
                    return false;
            }
            else if (!Arrays.asList(floatArrayList.get(0)).equals(Arrays.asList(other.floatArrayList.get(0))))
                return false;
            if (intArrayList == null)
            {
                if (other.intArrayList != null)
                    return false;
            }
            else if (!Arrays.asList(intArrayList.get(0)).equals(Arrays.asList(other.intArrayList.get(0))))
                return false;
            if (longArrayList == null)
            {
                if (other.longArrayList != null)
                    return false;
            }
            else if (!Arrays.asList(longArrayList.get(0)).equals(Arrays.asList(other.longArrayList.get(0))))
                return false;
            if (shortArrayList == null)
            {
                if (other.shortArrayList != null)
                    return false;
            }
            else if (!Arrays.asList(shortArrayList.get(0)).equals(Arrays.asList(other.shortArrayList.get(0))))
                return false;
            return true;
        }
        
        @Override
        public String toString()
        {
            return "PojoWithArrayInList " +
            		"[boolArrayList=" + Arrays.asList(boolArrayList.get(0)) + 
            		", charArrayList=" + Arrays.asList(charArrayList.get(0)) + 
            		", shortArrayList=" + Arrays.asList(shortArrayList.get(0)) + 
            		", intArrayList=" + Arrays.asList(intArrayList.get(0)) + 
                    ", longArrayList=" + Arrays.asList(longArrayList.get(0)) + 
                    ", floatArrayList=" + Arrays.asList(floatArrayList.get(0)) + 
                    ", doubleArrayList=" + Arrays.asList(doubleArrayList.get(0)) + "]";
        }
    }
    
    public void testArrayInList() throws IOException
    {
        Schema<PojoWithArrayInList> schema =
                getSchema(PojoWithArrayInList.class);

        Pipe.Schema<PojoWithArrayInList> pipeSchema =
                ((RuntimeSchema<PojoWithArrayInList>) schema).getPipeSchema();

        PojoWithArrayInList p = new PojoWithArrayInList();
        p.boolArrayList.add(new Boolean[] { true, false });
        p.charArrayList.add(new Character[] { 'a', 'b' });
        p.shortArrayList.add(new Short[] { 1, 2 });
        p.intArrayList.add(new Integer[] { 1, 2 });
        p.longArrayList.add(new Long[] { 1l, 2l });
        p.floatArrayList.add(new Float[] { 1.1f, 2.2f });
        p.doubleArrayList.add(new Double[] { 1.1d, 2.2d });

        byte[] data = toByteArray(p, schema);

        PojoWithArrayInList pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithArrayInList pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }
    
    public static final class PojoWithArrayPrimitiveInList
    {
        ArrayList<boolean[]> boolArrayList = new ArrayList<boolean[]>();
        ArrayList<char[]> charArrayList = new ArrayList<char[]>();
        ArrayList<short[]> shortArrayList = new ArrayList<short[]>();
        ArrayList<int[]> intArrayList = new ArrayList<int[]>();
        ArrayList<long[]> longArrayList = new ArrayList<long[]>();
        ArrayList<float[]> floatArrayList = new ArrayList<float[]>();
        ArrayList<double[]> doubleArrayList = new ArrayList<double[]>();
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((boolArrayList == null) ? 0 : boolArrayList.hashCode());
            result = prime * result + ((charArrayList == null) ? 0 : charArrayList.hashCode());
            result = prime * result + ((doubleArrayList == null) ? 0 : doubleArrayList.hashCode());
            result = prime * result + ((floatArrayList == null) ? 0 : floatArrayList.hashCode());
            result = prime * result + ((intArrayList == null) ? 0 : intArrayList.hashCode());
            result = prime * result + ((longArrayList == null) ? 0 : longArrayList.hashCode());
            result = prime * result + ((shortArrayList == null) ? 0 : shortArrayList.hashCode());
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
            PojoWithArrayPrimitiveInList other = (PojoWithArrayPrimitiveInList)obj;
            if (boolArrayList == null)
            {
                if (other.boolArrayList != null)
                    return false;
            }
            else if (!Arrays.equals(boolArrayList.get(0), other.boolArrayList.get(0)))
                return false;
            if (charArrayList == null)
            {
                if (other.charArrayList != null)
                    return false;
            }
            else if (!Arrays.equals(charArrayList.get(0), other.charArrayList.get(0)))
                return false;
            if (doubleArrayList == null)
            {
                if (other.doubleArrayList != null)
                    return false;
            }
            else if (!Arrays.equals(doubleArrayList.get(0), other.doubleArrayList.get(0)))
                return false;
            if (floatArrayList == null)
            {
                if (other.floatArrayList != null)
                    return false;
            }
            else if (!Arrays.equals(floatArrayList.get(0), other.floatArrayList.get(0)))
                return false;
            if (intArrayList == null)
            {
                if (other.intArrayList != null)
                    return false;
            }
            else if (!Arrays.equals(intArrayList.get(0), other.intArrayList.get(0)))
                return false;
            if (longArrayList == null)
            {
                if (other.longArrayList != null)
                    return false;
            }
            else if (!Arrays.equals(longArrayList.get(0), other.longArrayList.get(0)))
                return false;
            if (shortArrayList == null)
            {
                if (other.shortArrayList != null)
                    return false;
            }
            else if (!Arrays.equals(shortArrayList.get(0), other.shortArrayList.get(0)))
                return false;
            return true;
        }
        
        @Override
        public String toString()
        {
            return "PojoWithArrayPrimitiveInList " +
                    "[boolArrayList=" + Arrays.asList(boolArrayList.get(0)) + 
                    ", charArrayList=" + Arrays.asList(charArrayList.get(0)) + 
                    ", shortArrayList=" + Arrays.asList(shortArrayList.get(0)) + 
                    ", intArrayList=" + Arrays.asList(intArrayList.get(0)) + 
                    ", longArrayList=" + Arrays.asList(longArrayList.get(0)) + 
                    ", floatArrayList=" + Arrays.asList(floatArrayList.get(0)) + 
                    ", doubleArrayList=" + Arrays.asList(doubleArrayList.get(0)) + "]";
        }
    }
    
    public void testArrayPrimitiveInList() throws IOException
    {
        Schema<PojoWithArrayPrimitiveInList> schema =
                getSchema(PojoWithArrayPrimitiveInList.class);

        Pipe.Schema<PojoWithArrayPrimitiveInList> pipeSchema =
                ((RuntimeSchema<PojoWithArrayPrimitiveInList>) schema).getPipeSchema();

        PojoWithArrayPrimitiveInList p = new PojoWithArrayPrimitiveInList();
        p.boolArrayList.add(new boolean[] { true, false });
        p.charArrayList.add(new char[] { 'a', 'b' });
        p.shortArrayList.add(new short[] { 1, 2 });
        p.intArrayList.add(new int[] { 1, 2 });
        p.longArrayList.add(new long[] { 1l, 2l });
        p.floatArrayList.add(new float[] { 1.1f, 2.2f });
        p.doubleArrayList.add(new double[] { 1.1d, 2.2d });

        byte[] data = toByteArray(p, schema);

        PojoWithArrayPrimitiveInList pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithArrayPrimitiveInList pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }
    
    public static final class PojoWithArrayInMap
    {
        HashMap<Integer,Boolean[]> boolHashMap = new HashMap<Integer,Boolean[]>();
        HashMap<Integer,Character[]> charHashMap = new HashMap<Integer,Character[]>();
        HashMap<Integer,Short[]> shortHashMap = new HashMap<Integer,Short[]>();
        HashMap<Integer,Integer[]> intHashMap = new HashMap<Integer,Integer[]>();
        HashMap<Integer,Long[]> longHashMap = new HashMap<Integer,Long[]>();
        HashMap<Integer,Float[]> floatHashMap = new HashMap<Integer,Float[]>();
        HashMap<Integer,Double[]> doubleHashMap = new HashMap<Integer,Double[]>();
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((boolHashMap == null) ? 0 : boolHashMap.hashCode());
            result = prime * result + ((charHashMap == null) ? 0 : charHashMap.hashCode());
            result = prime * result + ((doubleHashMap == null) ? 0 : doubleHashMap.hashCode());
            result = prime * result + ((floatHashMap == null) ? 0 : floatHashMap.hashCode());
            result = prime * result + ((intHashMap == null) ? 0 : intHashMap.hashCode());
            result = prime * result + ((longHashMap == null) ? 0 : longHashMap.hashCode());
            result = prime * result + ((shortHashMap == null) ? 0 : shortHashMap.hashCode());
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
            PojoWithArrayInMap other = (PojoWithArrayInMap)obj;
            if (boolHashMap == null)
            {
                if (other.boolHashMap != null)
                    return false;
            }
            else if (!Arrays.asList(boolHashMap.get(0)).equals(Arrays.asList(other.boolHashMap.get(0))))
                return false;
            if (charHashMap == null)
            {
                if (other.charHashMap != null)
                    return false;
            }
            else if (!Arrays.asList(charHashMap.get(0)).equals(Arrays.asList(other.charHashMap.get(0))))
                return false;
            if (doubleHashMap == null)
            {
                if (other.doubleHashMap != null)
                    return false;
            }
            else if (!Arrays.asList(doubleHashMap.get(0)).equals(Arrays.asList(other.doubleHashMap.get(0))))
                return false;
            if (floatHashMap == null)
            {
                if (other.floatHashMap != null)
                    return false;
            }
            else if (!Arrays.asList(floatHashMap.get(0)).equals(Arrays.asList(other.floatHashMap.get(0))))
                return false;
            if (intHashMap == null)
            {
                if (other.intHashMap != null)
                    return false;
            }
            else if (!Arrays.asList(intHashMap.get(0)).equals(Arrays.asList(other.intHashMap.get(0))))
                return false;
            if (longHashMap == null)
            {
                if (other.longHashMap != null)
                    return false;
            }
            else if (!Arrays.asList(longHashMap.get(0)).equals(Arrays.asList(other.longHashMap.get(0))))
                return false;
            if (shortHashMap == null)
            {
                if (other.shortHashMap != null)
                    return false;
            }
            else if (!Arrays.asList(shortHashMap.get(0)).equals(Arrays.asList(other.shortHashMap.get(0))))
                return false;
            return true;
        }
        
        @Override
        public String toString()
        {
            return "PojoWithArrayInMap " +
                    "[boolHashMap=" + Arrays.asList(boolHashMap.get(0)) + 
                    ", charHashMap=" + Arrays.asList(charHashMap.get(0)) + 
                    ", shortHashMap=" + Arrays.asList(shortHashMap.get(0)) + 
                    ", intHashMap=" + Arrays.asList(intHashMap.get(0)) + 
                    ", longHashMap=" + Arrays.asList(longHashMap.get(0)) + 
                    ", floatHashMap=" + Arrays.asList(floatHashMap.get(0)) + 
                    ", doubleHashMap=" + Arrays.asList(doubleHashMap.get(0)) + "]";
        }
    }
    
    public void testArrayInMap() throws IOException
    {
        Schema<PojoWithArrayInMap> schema =
                getSchema(PojoWithArrayInMap.class);

        Pipe.Schema<PojoWithArrayInMap> pipeSchema =
                ((RuntimeSchema<PojoWithArrayInMap>) schema).getPipeSchema();

        PojoWithArrayInMap p = new PojoWithArrayInMap();
        p.boolHashMap.put(0, new Boolean[] { true, false });
        p.charHashMap.put(0, new Character[] { 'a', 'b' });
        p.shortHashMap.put(0, new Short[] { 1, 2 });
        p.intHashMap.put(0, new Integer[] { 1, 2 });
        p.longHashMap.put(0, new Long[] { 1l, 2l });
        p.floatHashMap.put(0, new Float[] { 1.1f, 2.2f });
        p.doubleHashMap.put(0, new Double[] { 1.1d, 2.2d });

        byte[] data = toByteArray(p, schema);

        PojoWithArrayInMap pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithArrayInMap pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }
    
    static ArrayList<Object> asList(Object array)
    {
        ArrayList<Object> list = new ArrayList<Object>();
        if (array == null)
            return list;
        
        int len = Array.getLength(array);
        for (int i = 0; i < len; i++)
            list.add(Array.get(array, i));
        
        return list;
    }
    
    public static final class PojoWithArrayPrimitiveInMap
    {
        HashMap<Integer,boolean[]> boolHashMap = new HashMap<Integer,boolean[]>();
        HashMap<Integer,char[]> charHashMap = new HashMap<Integer,char[]>();
        HashMap<Integer,short[]> shortHashMap = new HashMap<Integer,short[]>();
        HashMap<Integer,int[]> intHashMap = new HashMap<Integer,int[]>();
        HashMap<Integer,long[]> longHashMap = new HashMap<Integer,long[]>();
        HashMap<Integer,float[]> floatHashMap = new HashMap<Integer,float[]>();
        HashMap<Integer,double[]> doubleHashMap = new HashMap<Integer,double[]>();
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((boolHashMap == null) ? 0 : boolHashMap.hashCode());
            result = prime * result + ((charHashMap == null) ? 0 : charHashMap.hashCode());
            result = prime * result + ((doubleHashMap == null) ? 0 : doubleHashMap.hashCode());
            result = prime * result + ((floatHashMap == null) ? 0 : floatHashMap.hashCode());
            result = prime * result + ((intHashMap == null) ? 0 : intHashMap.hashCode());
            result = prime * result + ((longHashMap == null) ? 0 : longHashMap.hashCode());
            result = prime * result + ((shortHashMap == null) ? 0 : shortHashMap.hashCode());
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
            PojoWithArrayPrimitiveInMap other = (PojoWithArrayPrimitiveInMap)obj;
            if (boolHashMap == null)
            {
                if (other.boolHashMap != null)
                    return false;
            }
            else if (!asList(boolHashMap.get(0)).equals(asList(other.boolHashMap.get(0))))
                return false;
            if (charHashMap == null)
            {
                if (other.charHashMap != null)
                    return false;
            }
            else if (!asList(charHashMap.get(0)).equals(asList(other.charHashMap.get(0))))
                return false;
            if (doubleHashMap == null)
            {
                if (other.doubleHashMap != null)
                    return false;
            }
            else if (!asList(doubleHashMap.get(0)).equals(asList(other.doubleHashMap.get(0))))
                return false;
            if (floatHashMap == null)
            {
                if (other.floatHashMap != null)
                    return false;
            }
            else if (!asList(floatHashMap.get(0)).equals(asList(other.floatHashMap.get(0))))
                return false;
            if (intHashMap == null)
            {
                if (other.intHashMap != null)
                    return false;
            }
            else if (!asList(intHashMap.get(0)).equals(asList(other.intHashMap.get(0))))
                return false;
            if (longHashMap == null)
            {
                if (other.longHashMap != null)
                    return false;
            }
            else if (!asList(longHashMap.get(0)).equals(asList(other.longHashMap.get(0))))
                return false;
            if (shortHashMap == null)
            {
                if (other.shortHashMap != null)
                    return false;
            }
            else if (!asList(shortHashMap.get(0)).equals(asList(other.shortHashMap.get(0))))
                return false;
            return true;
        }
        
        @Override
        public String toString()
        {
            return "PojoWithArrayPrimitiveInMap " +
                    "[boolHashMap=" + asList(boolHashMap.get(0)) + 
                    ", charHashMap=" + asList(charHashMap.get(0)) + 
                    ", shortHashMap=" + asList(shortHashMap.get(0)) + 
                    ", intHashMap=" + asList(intHashMap.get(0)) + 
                    ", longHashMap=" + asList(longHashMap.get(0)) + 
                    ", floatHashMap=" + asList(floatHashMap.get(0)) + 
                    ", doubleHashMap=" + asList(doubleHashMap.get(0)) + "]";
        }
    }
    
    public void testArrayPrimitiveInMap() throws IOException
    {
        Schema<PojoWithArrayPrimitiveInMap> schema =
                getSchema(PojoWithArrayPrimitiveInMap.class);

        Pipe.Schema<PojoWithArrayPrimitiveInMap> pipeSchema =
                ((RuntimeSchema<PojoWithArrayPrimitiveInMap>) schema).getPipeSchema();

        PojoWithArrayPrimitiveInMap p = new PojoWithArrayPrimitiveInMap();
        p.boolHashMap.put(0, new boolean[] { true, false });
        p.charHashMap.put(0, new char[] { 'a', 'b' });
        p.shortHashMap.put(0, new short[] { 1, 2 });
        p.intHashMap.put(0, new int[] { 1, 2 });
        p.longHashMap.put(0, new long[] { 1l, 2l });
        p.floatHashMap.put(0, new float[] { 1.1f, 2.2f });
        p.doubleHashMap.put(0, new double[] { 1.1d, 2.2d });

        byte[] data = toByteArray(p, schema);

        PojoWithArrayPrimitiveInMap pFromByteArray = schema.newMessage();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);

        PojoWithArrayPrimitiveInMap pFromStream = schema.newMessage();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromStream);

        roundTrip(p, schema, pipeSchema);
    }
}
