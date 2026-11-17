package io.protostuff.runtime;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Pipe;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.NullArrayElementTest.Size;
import io.protostuff.runtime.NullArrayElementTest.SomePojo;
import io.protostuff.runtime.SampleDelegates.Singleton;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared fixtures for the {@link ArraySchemas} characterization tests.
 * <p>
 * Every array element kind handled by {@link ArraySchemas} (plus the delegate, enum and pojo arrays built by
 * {@link HasDelegate}, {@link EnumIO} and {@link HasSchema}) is described by a {@link Kind}. The holder classes expose
 * one field per kind (named after {@link Kind#name}) so that each of the four entry paths can be exercised:
 * <ul>
 * <li>{@link FieldHolder} - path A, array-typed field</li>
 * <li>{@link ObjectHolder} - path B, array inside an {@code Object} field</li>
 * <li>{@link ListHolder} - path C, array as a repeated-field element (or path D with
 * {@link IdStrategy#COLLECTION_SCHEMA_ON_REPEATED_FIELDS})</li>
 * <li>{@link MapHolder} - path D, array as a map value (derived schema)</li>
 * </ul>
 * All holder fields carry a {@link Tag} so that the wire format does not depend on reflection field order.
 */
final class ArrayFixtures
{

    private ArrayFixtures()
    {
    }

    // ------------------------------------------------------------------
    // kinds

    /**
     * An array element kind, with a deterministic sample array.
     */
    static abstract class Kind
    {
        final String name;
        final Class<?> arrayType;
        final Class<?> componentType;
        final boolean primitive;

        Kind(String name, Class<?> arrayType)
        {
            this.name = name;
            this.arrayType = arrayType;
            this.componentType = arrayType.getComponentType();
            this.primitive = componentType.isPrimitive();
        }

        /**
         * A new sample array (at least 2 elements, no nulls) on every call.
         */
        abstract Object sample();

        /**
         * A new array {@code [null, e0, null, null, e1, null]} built from the first two sample elements. Covers a
         * leading null, a null run in between, and a trailing null.
         */
        Object sampleWithNulls()
        {
            if (primitive)
                throw new IllegalStateException(name + " cannot hold nulls.");

            final Object sample = sample();
            final Object array = Array.newInstance(componentType, 6);
            Array.set(array, 1, Array.get(sample, 0));
            Array.set(array, 4, Array.get(sample, 1));
            return array;
        }

        /**
         * A new array of {@code len} elements cycling through the sample elements. When {@code withNullRun} is true,
         * the elements from index {@code len / 8} up to (excluding) {@code len - len / 8} are null.
         */
        Object large(int len, boolean withNullRun)
        {
            if (withNullRun && primitive)
                throw new IllegalStateException(name + " cannot hold nulls.");

            final Object sample = sample();
            final int sampleLen = Array.getLength(sample);
            final int nullStart = len / 8, nullEnd = len - len / 8;
            final Object array = Array.newInstance(componentType, len);
            for (int i = 0; i < len; i++)
            {
                if (withNullRun && i >= nullStart && i < nullEnd)
                    continue;

                Array.set(array, i, Array.get(sample, i % sampleLen));
            }
            return array;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    static final Kind P_BOOL = new Kind("pBool", boolean[].class)
    {
        @Override
        Object sample()
        {
            return new boolean[] { true, false, true };
        }
    };

    static final Kind B_BOOL = new Kind("bBool", Boolean[].class)
    {
        @Override
        Object sample()
        {
            return new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE };
        }
    };

    static final Kind P_CHAR = new Kind("pChar", char[].class)
    {
        @Override
        Object sample()
        {
            return new char[] { 'a', 'é', '\uD83D' };
        }
    };

    static final Kind B_CHAR = new Kind("bChar", Character[].class)
    {
        @Override
        Object sample()
        {
            return new Character[] { Character.valueOf('a'), Character.valueOf('é'),
                    Character.valueOf('\uD83D') };
        }
    };

    static final Kind P_SHORT = new Kind("pShort", short[].class)
    {
        @Override
        Object sample()
        {
            return new short[] { Short.MIN_VALUE, -1, Short.MAX_VALUE };
        }
    };

    static final Kind B_SHORT = new Kind("bShort", Short[].class)
    {
        @Override
        Object sample()
        {
            return new Short[] { Short.valueOf(Short.MIN_VALUE), Short.valueOf((short) -1),
                    Short.valueOf(Short.MAX_VALUE) };
        }
    };

    static final Kind P_INT = new Kind("pInt", int[].class)
    {
        @Override
        Object sample()
        {
            return new int[] { Integer.MIN_VALUE, -1, 0, Integer.MAX_VALUE };
        }
    };

    static final Kind B_INT = new Kind("bInt", Integer[].class)
    {
        @Override
        Object sample()
        {
            return new Integer[] { Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(-1),
                    Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE) };
        }
    };

    static final Kind P_LONG = new Kind("pLong", long[].class)
    {
        @Override
        Object sample()
        {
            return new long[] { Long.MIN_VALUE, -1L, Long.MAX_VALUE };
        }
    };

    static final Kind B_LONG = new Kind("bLong", Long[].class)
    {
        @Override
        Object sample()
        {
            return new Long[] { Long.valueOf(Long.MIN_VALUE), Long.valueOf(-1L),
                    Long.valueOf(Long.MAX_VALUE) };
        }
    };

    static final Kind P_FLOAT = new Kind("pFloat", float[].class)
    {
        @Override
        Object sample()
        {
            return new float[] { -0.0f, Float.NaN, Float.POSITIVE_INFINITY, 1.5f };
        }
    };

    static final Kind B_FLOAT = new Kind("bFloat", Float[].class)
    {
        @Override
        Object sample()
        {
            return new Float[] { Float.valueOf(-0.0f), Float.valueOf(Float.NaN),
                    Float.valueOf(Float.POSITIVE_INFINITY), Float.valueOf(1.5f) };
        }
    };

    static final Kind P_DOUBLE = new Kind("pDouble", double[].class)
    {
        @Override
        Object sample()
        {
            return new double[] { -0.0d, Double.NaN, Double.NEGATIVE_INFINITY, 2.25d };
        }
    };

    static final Kind B_DOUBLE = new Kind("bDouble", Double[].class)
    {
        @Override
        Object sample()
        {
            return new Double[] { Double.valueOf(-0.0d), Double.valueOf(Double.NaN),
                    Double.valueOf(Double.NEGATIVE_INFINITY), Double.valueOf(2.25d) };
        }
    };

    static final Kind STRING = new Kind("string", String[].class)
    {
        @Override
        Object sample()
        {
            return new String[] { "", "abc", "é中" };
        }
    };

    static final Kind BYTE_STRING = new Kind("byteString", ByteString[].class)
    {
        @Override
        Object sample()
        {
            return new ByteString[] { ByteString.EMPTY, ByteString.copyFromUtf8("xyz") };
        }
    };

    static final Kind BYTE_ARRAY = new Kind("byteArray", byte[][].class)
    {
        @Override
        Object sample()
        {
            return new byte[][] { new byte[0], new byte[] { 1, 2, 3 } };
        }
    };

    static final Kind BIG_DECIMAL = new Kind("bigDecimal", BigDecimal[].class)
    {
        @Override
        Object sample()
        {
            // 1E+3 has a negative scale
            return new BigDecimal[] { new BigDecimal("-1.50"), new BigDecimal("1E+3") };
        }
    };

    static final Kind BIG_INTEGER = new Kind("bigInteger", BigInteger[].class)
    {
        @Override
        Object sample()
        {
            return new BigInteger[] { BigInteger.ZERO,
                    new BigInteger("-123456789012345678901234567890") };
        }
    };

    static final Kind DATE = new Kind("date", Date[].class)
    {
        @Override
        Object sample()
        {
            return new Date[] { new Date(0L), new Date(-1000L), new Date(1700000000000L) };
        }
    };

    static final Kind ENUM = new Kind("enumSize", Size[].class)
    {
        @Override
        Object sample()
        {
            return new Size[] { Size.SMALL, Size.LARGE, Size.MEDIUM };
        }
    };

    static final Kind POJO = new Kind("pojo", SomePojo[].class)
    {
        @Override
        Object sample()
        {
            return new SomePojo[] { new SomePojo("a"), new SomePojo("") };
        }
    };

    static final Kind DELEGATE = new Kind("delegate", Singleton[].class)
    {
        @Override
        Object sample()
        {
            return new Singleton[] { Singleton.INSTANCE, Singleton.INSTANCE };
        }
    };

    /**
     * All 23 kinds: the 16 element kinds, where the 7 primitives appear both as primitive and boxed arrays.
     */
    static final List<Kind> KINDS = Collections.unmodifiableList(Arrays.asList(
            P_BOOL, B_BOOL, P_CHAR, B_CHAR, P_SHORT, B_SHORT, P_INT, B_INT,
            P_LONG, B_LONG, P_FLOAT, B_FLOAT, P_DOUBLE, B_DOUBLE,
            STRING, BYTE_STRING, BYTE_ARRAY, BIG_DECIMAL, BIG_INTEGER, DATE,
            ENUM, POJO, DELEGATE));

    // ------------------------------------------------------------------
    // holders (field names must match Kind.name)

    /**
     * Path A: array-typed fields.
     */
    static final class FieldHolder
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
     * Path B: an array inside an {@code Object} field.
     */
    static final class ObjectHolder
    {
        @Tag(1) Object value;
    }

    /**
     * Path C (or path D with {@link IdStrategy#COLLECTION_SCHEMA_ON_REPEATED_FIELDS}): arrays as list elements.
     */
    static final class ListHolder
    {
        @Tag(1) List<boolean[]> pBool;
        @Tag(2) List<Boolean[]> bBool;
        @Tag(3) List<char[]> pChar;
        @Tag(4) List<Character[]> bChar;
        @Tag(5) List<short[]> pShort;
        @Tag(6) List<Short[]> bShort;
        @Tag(7) List<int[]> pInt;
        @Tag(8) List<Integer[]> bInt;
        @Tag(9) List<long[]> pLong;
        @Tag(10) List<Long[]> bLong;
        @Tag(11) List<float[]> pFloat;
        @Tag(12) List<Float[]> bFloat;
        @Tag(13) List<double[]> pDouble;
        @Tag(14) List<Double[]> bDouble;
        @Tag(15) List<String[]> string;
        @Tag(16) List<ByteString[]> byteString;
        @Tag(17) List<byte[][]> byteArray;
        @Tag(18) List<BigDecimal[]> bigDecimal;
        @Tag(19) List<BigInteger[]> bigInteger;
        @Tag(20) List<Date[]> date;
        @Tag(21) List<Size[]> enumSize;
        @Tag(22) List<SomePojo[]> pojo;
        @Tag(23) List<Singleton[]> delegate;
    }

    /**
     * Path D: arrays as map values (derived schema).
     */
    static final class MapHolder
    {
        @Tag(1) Map<String, boolean[]> pBool;
        @Tag(2) Map<String, Boolean[]> bBool;
        @Tag(3) Map<String, char[]> pChar;
        @Tag(4) Map<String, Character[]> bChar;
        @Tag(5) Map<String, short[]> pShort;
        @Tag(6) Map<String, Short[]> bShort;
        @Tag(7) Map<String, int[]> pInt;
        @Tag(8) Map<String, Integer[]> bInt;
        @Tag(9) Map<String, long[]> pLong;
        @Tag(10) Map<String, Long[]> bLong;
        @Tag(11) Map<String, float[]> pFloat;
        @Tag(12) Map<String, Float[]> bFloat;
        @Tag(13) Map<String, double[]> pDouble;
        @Tag(14) Map<String, Double[]> bDouble;
        @Tag(15) Map<String, String[]> string;
        @Tag(16) Map<String, ByteString[]> byteString;
        @Tag(17) Map<String, byte[][]> byteArray;
        @Tag(18) Map<String, BigDecimal[]> bigDecimal;
        @Tag(19) Map<String, BigInteger[]> bigInteger;
        @Tag(20) Map<String, Date[]> date;
        @Tag(21) Map<String, Size[]> enumSize;
        @Tag(22) Map<String, SomePojo[]> pojo;
        @Tag(23) Map<String, Singleton[]> delegate;
    }

    /**
     * The map key used by {@link #newHolder} for {@link MapHolder}.
     */
    static final String MAP_KEY = "k";

    /**
     * Creates a holder whose field for {@code kind} contains {@code array} (wrapped in a list or map when needed).
     * All other fields are left null.
     */
    static Object newHolder(Class<?> holderType, Kind kind, Object array)
    {
        try
        {
            final Object holder = holderType.newInstance();
            if (holderType == ObjectHolder.class)
            {
                ((ObjectHolder) holder).value = array;
                return holder;
            }

            final Field f = holderType.getDeclaredField(kind.name);
            f.setAccessible(true);
            if (holderType == FieldHolder.class)
            {
                f.set(holder, array);
            }
            else if (holderType == ListHolder.class)
            {
                final ArrayList<Object> list = new ArrayList<Object>();
                list.add(array);
                f.set(holder, list);
            }
            else if (holderType == MapHolder.class)
            {
                final HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(MAP_KEY, array);
                f.set(holder, map);
            }
            else
            {
                throw new IllegalArgumentException("Unknown holder: " + holderType);
            }
            return holder;
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
    }

    // ------------------------------------------------------------------
    // strategies and formats

    /**
     * A new {@link DefaultIdStrategy} with the given flags and {@link SampleDelegates#SINGLETON_DELEGATE} registered.
     */
    static DefaultIdStrategy newStrategy(int flags)
    {
        final DefaultIdStrategy strategy = new DefaultIdStrategy(flags);
        strategy.registerDelegate(SampleDelegates.SINGLETON_DELEGATE);
        return strategy;
    }

    static LinkedBuffer buf()
    {
        return LinkedBuffer.allocate(256);
    }

    /**
     * The binary formats that go through {@link ArraySchemas}.
     */
    enum Format
    {
        PROTOSTUFF
        {
            @Override
            <T> byte[] toByteArray(T message, Schema<T> schema)
            {
                return ProtostuffIOUtil.toByteArray(message, schema, buf());
            }

            @Override
            <T> void mergeFrom(byte[] data, T message, Schema<T> schema) throws IOException
            {
                ProtostuffIOUtil.mergeFrom(data, message, schema);
            }
        },
        PROTOBUF
        {
            @Override
            <T> byte[] toByteArray(T message, Schema<T> schema)
            {
                return ProtobufIOUtil.toByteArray(message, schema, buf());
            }

            @Override
            <T> void mergeFrom(byte[] data, T message, Schema<T> schema) throws IOException
            {
                ProtobufIOUtil.mergeFrom(data, message, schema);
            }
        },
        GRAPH
        {
            @Override
            <T> byte[] toByteArray(T message, Schema<T> schema)
            {
                return GraphIOUtil.toByteArray(message, schema, buf());
            }

            @Override
            <T> void mergeFrom(byte[] data, T message, Schema<T> schema) throws IOException
            {
                GraphIOUtil.mergeFrom(data, message, schema);
            }
        };

        abstract <T> byte[] toByteArray(T message, Schema<T> schema);

        abstract <T> void mergeFrom(byte[] data, T message, Schema<T> schema) throws IOException;
    }

    /**
     * Pipes {@code message} protobuf -&gt; protostuff -&gt; protobuf. Returns null when the piped protostuff bytes equal the
     * directly serialized protostuff bytes and the piped-back protobuf bytes equal the directly serialized protobuf
     * bytes, otherwise a description of the difference.
     */
    static String pipeRoundTrip(Object message, Schema<Object> schema) throws IOException
    {
        @SuppressWarnings("unchecked")
        final Pipe.Schema<Object> pipeSchema = ((RuntimeSchema<Object>) schema).getPipeSchema();

        final byte[] protobuf = Format.PROTOBUF.toByteArray(message, schema);
        final byte[] protostuff = Format.PROTOSTUFF.toByteArray(message, schema);

        final byte[] pipedProtostuff = ProtostuffIOUtil.toByteArray(
                ProtobufIOUtil.newPipe(protobuf, 0, protobuf.length), pipeSchema, buf());
        if (!Arrays.equals(protostuff, pipedProtostuff))
        {
            return "protobuf -> protostuff pipe: expected " + toHex(protostuff) + " but was "
                    + toHex(pipedProtostuff);
        }

        final byte[] pipedProtobuf = ProtobufIOUtil.toByteArray(
                ProtostuffIOUtil.newPipe(pipedProtostuff, 0, pipedProtostuff.length), pipeSchema, buf());
        if (!Arrays.equals(protobuf, pipedProtobuf))
        {
            return "protostuff -> protobuf pipe: expected " + toHex(protobuf) + " but was "
                    + toHex(pipedProtobuf);
        }
        return null;
    }

    // ------------------------------------------------------------------
    // comparison

    /**
     * Deep equality where arrays (primitive or not) are compared by content, lists element-wise and maps entry-wise.
     * Float/double values are compared via {@link Float#equals}/{@link Double#equals} (bit patterns), so {@code NaN}
     * equals {@code NaN} and {@code -0.0} differs from {@code 0.0}.
     */
    static boolean deepEquals(Object a, Object b)
    {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;

        if (a.getClass().isArray())
        {
            if (a.getClass() != b.getClass())
                return false;

            final int len = Array.getLength(a);
            if (len != Array.getLength(b))
                return false;

            for (int i = 0; i < len; i++)
            {
                if (!deepEquals(Array.get(a, i), Array.get(b, i)))
                    return false;
            }
            return true;
        }

        if (a instanceof List)
        {
            if (!(b instanceof List))
                return false;

            final List<?> la = (List<?>) a, lb = (List<?>) b;
            if (la.size() != lb.size())
                return false;

            for (int i = 0, len = la.size(); i < len; i++)
            {
                if (!deepEquals(la.get(i), lb.get(i)))
                    return false;
            }
            return true;
        }

        if (a instanceof Map)
        {
            if (!(b instanceof Map))
                return false;

            final Map<?, ?> ma = (Map<?, ?>) a, mb = (Map<?, ?>) b;
            if (ma.size() != mb.size())
                return false;

            for (Map.Entry<?, ?> entry : ma.entrySet())
            {
                if (!mb.containsKey(entry.getKey()) || !deepEquals(entry.getValue(), mb.get(entry.getKey())))
                    return false;
            }
            return true;
        }

        return a.equals(b);
    }

    /**
     * String form of {@code value} where nested arrays are expanded.
     */
    static String deepToString(Object value)
    {
        if (value == null || !value.getClass().isArray())
        {
            if (value instanceof List)
            {
                final StringBuilder sb = new StringBuilder("List[");
                for (Object item : (List<?>) value)
                    sb.append(deepToString(item)).append(", ");
                return sb.append(']').toString();
            }
            if (value instanceof Map)
            {
                final StringBuilder sb = new StringBuilder("Map{");
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet())
                    sb.append(entry.getKey()).append('=').append(deepToString(entry.getValue())).append(", ");
                return sb.append('}').toString();
            }
            return String.valueOf(value);
        }

        final StringBuilder sb = new StringBuilder(value.getClass().getComponentType().getSimpleName())
                .append("[]{");
        for (int i = 0, len = Array.getLength(value); i < len; i++)
        {
            if (i != 0)
                sb.append(", ");
            sb.append(deepToString(Array.get(value, i)));
        }
        return sb.append('}').toString();
    }

    /**
     * Returns null if every instance field of both holders is {@link #deepEquals}, otherwise a description of the
     * first difference.
     */
    static String diffHolders(Object expected, Object actual)
    {
        if (expected.getClass() != actual.getClass())
            return "holder type " + expected.getClass() + " != " + actual.getClass();

        try
        {
            for (Field f : expected.getClass().getDeclaredFields())
            {
                if (Modifier.isStatic(f.getModifiers()))
                    continue;

                f.setAccessible(true);
                final Object e = f.get(expected), a = f.get(actual);
                if (!deepEquals(e, a))
                {
                    return f.getName() + ": expected " + deepToString(e) + " but was " + deepToString(a);
                }
            }
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    // ------------------------------------------------------------------
    // hex

    private static final char[] HEX = "0123456789abcdef".toCharArray();

    static String toHex(byte[] data)
    {
        final char[] out = new char[data.length * 2];
        for (int i = 0; i < data.length; i++)
        {
            out[i * 2] = HEX[(data[i] >> 4) & 0x0f];
            out[i * 2 + 1] = HEX[data[i] & 0x0f];
        }
        return new String(out);
    }

    static byte[] fromHex(String hex)
    {
        if (hex.length() % 2 != 0)
            throw new IllegalArgumentException("Odd hex length: " + hex.length());

        final byte[] out = new byte[hex.length() / 2];
        for (int i = 0; i < out.length; i++)
        {
            out[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }
}
