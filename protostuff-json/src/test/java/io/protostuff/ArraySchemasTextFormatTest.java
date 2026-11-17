package io.protostuff;

import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.NullArrayElementTest.Size;
import io.protostuff.runtime.NullArrayElementTest.SomePojo;
import io.protostuff.runtime.RuntimeSchema;
import io.protostuff.runtime.SampleDelegates;
import io.protostuff.runtime.SampleDelegates.Singleton;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * JSON tests for the runtime array schemas (TF-1 .. TF-3): array fields are written as nested objects with the field
 * names {@code a} (length), {@code b} (data) and {@code c} (null count), or {@code 1}/{@code 2}/{@code 3} in numeric
 * mode.
 * <p>
 * TF-4 (the field naming methods of {@code ArraySchemas.Base}) lives in {@code ArraySchemasEntryPathTest} in
 * protostuff-runtime, because the array schema instances are package-private there.
 */
public class ArraySchemasTextFormatTest extends TestCase
{

    static final int PRESERVE_FLAGS = IdStrategy.DEFAULT_FLAGS | IdStrategy.PRESERVE_NULL_ELEMENTS;

    private final DefaultIdStrategy strategy = newStrategy();

    static DefaultIdStrategy newStrategy()
    {
        final DefaultIdStrategy strategy = new DefaultIdStrategy(PRESERVE_FLAGS);
        strategy.registerDelegate(SampleDelegates.SINGLETON_DELEGATE);
        return strategy;
    }

    /**
     * One field per entry path: typed (boxed and primitive), map value (derived schema) and {@code Object} field.
     */
    static final class Holder
    {
        @Tag(1) Integer[] ints;
        @Tag(2) int[] prims;
        @Tag(3) String[] strs;
        @Tag(4) Map<String, Integer[]> map;
        @Tag(5) Object obj;
    }

    static Holder newHolder()
    {
        final Holder holder = new Holder();
        holder.ints = new Integer[] { Integer.valueOf(1), null, Integer.valueOf(3) };
        holder.prims = new int[] { 4, 5 };
        holder.strs = new String[] { "x", null };
        holder.map = new HashMap<String, Integer[]>();
        holder.map.put("k", new Integer[] { Integer.valueOf(7), null });
        holder.obj = new long[] { 9L };
        return holder;
    }

    private Schema<Holder> holderSchema()
    {
        return RuntimeSchema.getSchema(Holder.class, strategy);
    }

    private static void assertHolderEquals(Holder expected, Holder actual)
    {
        assertTrue(Arrays.equals(expected.ints, actual.ints));
        assertTrue(Arrays.equals(expected.prims, actual.prims));
        assertTrue(Arrays.equals(expected.strs, actual.strs));
        assertEquals(1, actual.map.size());
        assertTrue(Arrays.equals(expected.map.get("k"), actual.map.get("k")));
        assertTrue(Arrays.equals((long[]) expected.obj, (long[]) actual.obj));
    }

    /**
     * TF-1: named output.
     * <p>
     * CHARACTERIZATION: possible bug. A boxed array with nulls repeats the {@code "b"} key inside the same JSON object
     * (e.g. {@code {"a":3,"b":[1],"c":1,"b":[3]}}). Protostuff reads it back, but strict JSON tools reject duplicate
     * keys or keep only the last one.
     */
    public void testNamedOutput() throws IOException
    {
        final Holder holder = newHolder();
        final byte[] json = JsonIOUtil.toByteArray(holder, holderSchema(), false);

        assertEquals("{"
                + "\"ints\":{\"a\":3,\"b\":[1],\"c\":1,\"b\":[3]},"
                + "\"prims\":{\"a\":2,\"b\":[4,5]},"
                + "\"strs\":{\"a\":2,\"b\":[\"x\"],\"c\":1},"
                // derived schema: boxed length written as -(len + 1)
                + "\"map\":{\"e\":[{\"k\":\"k\",\"v\":{\"a\":-3,\"b\":[7],\"c\":1}}]},"
                // Object field: field "G" (33, ID_ARRAY_SCALAR) holds the inline array id 5 (long[], see
                // ArraySchemas.toArrayId), followed by the array itself
                + "\"obj\":{\"G\":5,\"a\":1,\"b\":[9]}"
                + "}", new String(json, "UTF-8"));

        final Holder parsed = new Holder();
        JsonIOUtil.mergeFrom(json, parsed, holderSchema(), false);
        assertHolderEquals(holder, parsed);
    }

    /**
     * TF-2: numeric output (field numbers instead of names).
     */
    public void testNumericOutput() throws IOException
    {
        final Holder holder = newHolder();
        final byte[] json = JsonIOUtil.toByteArray(holder, holderSchema(), true);

        assertEquals("{"
                + "\"1\":{\"1\":3,\"2\":[1],\"3\":1,\"2\":[3]},"
                + "\"2\":{\"1\":2,\"2\":[4,5]},"
                + "\"3\":{\"1\":2,\"2\":[\"x\"],\"3\":1},"
                + "\"4\":{\"1\":[{\"1\":\"k\",\"2\":{\"1\":-3,\"2\":[7],\"3\":1}}]},"
                + "\"5\":{\"33\":5,\"1\":1,\"2\":[9]}"
                + "}", new String(json, "UTF-8"));

        final Holder parsed = new Holder();
        JsonIOUtil.mergeFrom(json, parsed, holderSchema(), true);
        assertHolderEquals(holder, parsed);
    }

    /**
     * Every array element kind in typed fields (the 7 primitives both primitive and boxed).
     */
    static final class AllKinds
    {
        @Tag(1) boolean[] pBool;
        @Tag(2) Boolean[] bBool;
        @Tag(3) char[] pChar;
        @Tag(4) Character[] bChar;
        @Tag(5) short[] pShort;
        @Tag(6) Short[] bShort;
        @Tag(7) int[] pInt;
        @Tag(8) Integer[] bInt;
        @Tag(9) long[] pLong;
        @Tag(10) Long[] bLong;
        @Tag(11) float[] pFloat;
        @Tag(12) Float[] bFloat;
        @Tag(13) double[] pDouble;
        @Tag(14) Double[] bDouble;
        @Tag(15) String[] string;
        @Tag(16) ByteString[] byteString;
        @Tag(17) byte[][] byteArray;
        @Tag(18) BigDecimal[] bigDecimal;
        @Tag(19) BigInteger[] bigInteger;
        @Tag(20) Date[] date;
        @Tag(21) Size[] enumSize;
        @Tag(22) SomePojo[] pojo;
        @Tag(23) Singleton[] delegate;
    }

    /**
     * Edge values for every kind; the non-primitive arrays contain a leading, a middle and a trailing null.
     */
    static AllKinds newAllKinds()
    {
        final AllKinds k = new AllKinds();
        k.pBool = new boolean[] { true, false };
        k.bBool = new Boolean[] { null, Boolean.TRUE, null, Boolean.FALSE, null };
        k.pChar = new char[] { 'a', 'é', '\uD83D' };
        k.bChar = new Character[] { null, Character.valueOf('\uD83D'), null, Character.valueOf('a'), null };
        k.pShort = new short[] { Short.MIN_VALUE, Short.MAX_VALUE };
        k.bShort = new Short[] { null, Short.valueOf(Short.MIN_VALUE), null, Short.valueOf((short) 0), null };
        k.pInt = new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE };
        k.bInt = new Integer[] { null, Integer.valueOf(Integer.MIN_VALUE), null, Integer.valueOf(-1), null };
        k.pLong = new long[] { Long.MIN_VALUE, Long.MAX_VALUE };
        k.bLong = new Long[] { null, Long.valueOf(Long.MIN_VALUE), null, Long.valueOf(1L), null };
        k.pFloat = new float[] { -0.0f, Float.NaN, Float.POSITIVE_INFINITY, 1.5f };
        k.bFloat = new Float[] { null, Float.valueOf(-0.0f), null, Float.valueOf(Float.NaN), null };
        k.pDouble = new double[] { -0.0d, Double.NaN, Double.NEGATIVE_INFINITY };
        k.bDouble = new Double[] { null, Double.valueOf(-0.0d), null, Double.valueOf(Double.NaN), null };
        k.string = new String[] { null, "", null, "é中", null };
        k.byteString = new ByteString[] { null, ByteString.EMPTY, null, ByteString.copyFromUtf8("xyz"), null };
        k.byteArray = new byte[][] { null, new byte[0], null, new byte[] { 1, 2 }, null };
        k.bigDecimal = new BigDecimal[] { null, new BigDecimal("-1.50"), null, new BigDecimal("1E+3"), null };
        k.bigInteger = new BigInteger[] { null, BigInteger.ZERO, null,
                new BigInteger("-123456789012345678901234567890"), null };
        k.date = new Date[] { null, new Date(-1000L), null, new Date(0L), null };
        k.enumSize = new Size[] { null, Size.SMALL, null, Size.LARGE, null };
        k.pojo = new SomePojo[] { null, new SomePojo("a"), null, new SomePojo(""), null };
        k.delegate = new Singleton[] { null, Singleton.INSTANCE, null, Singleton.INSTANCE, null };
        return k;
    }

    /**
     * An {@code Object} field (path B).
     */
    static final class ObjectHolder
    {
        @Tag(1) Object value;
    }

    /**
     * TF-3: every kind round-trips through JSON (named and numeric), in typed fields and in an {@code Object} field.
     */
    public void testAllKindsRoundTrip() throws Exception
    {
        final Schema<AllKinds> schema = RuntimeSchema.getSchema(AllKinds.class, strategy);
        final Schema<ObjectHolder> objectSchema = RuntimeSchema.getSchema(ObjectHolder.class, strategy);
        final AllKinds expected = newAllKinds();

        final StringBuilder failures = new StringBuilder();
        for (boolean numeric : new boolean[] { false, true })
        {
            final AllKinds parsed = new AllKinds();
            JsonIOUtil.mergeFrom(JsonIOUtil.toByteArray(expected, schema, numeric), parsed, schema, numeric);

            for (Field f : AllKinds.class.getDeclaredFields())
            {
                if (f.isSynthetic())
                    continue;

                f.setAccessible(true);
                final Object array = f.get(expected);
                final String id = (numeric ? "numeric." : "named.") + f.getName();
                if (!Arrays.deepEquals(new Object[] { array }, new Object[] { f.get(parsed) }))
                    failures.append('\n').append(id).append(" (typed field)");

                final ObjectHolder holder = new ObjectHolder();
                holder.value = array;
                final ObjectHolder parsedHolder = new ObjectHolder();
                JsonIOUtil.mergeFrom(JsonIOUtil.toByteArray(holder, objectSchema, numeric), parsedHolder,
                        objectSchema, numeric);
                if (!Arrays.deepEquals(new Object[] { array }, new Object[] { parsedHolder.value }))
                    failures.append('\n').append(id).append(" (Object field)");
            }
        }

        if (failures.length() != 0)
            fail("JSON round trip failed for:" + failures);
    }
}
