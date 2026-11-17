package io.protostuff.runtime;

import static io.protostuff.runtime.ArrayFixtures.KINDS;

import io.protostuff.Pipe;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffException;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.ArrayFixtures.FieldHolder;
import io.protostuff.runtime.ArrayFixtures.Format;
import io.protostuff.runtime.ArrayFixtures.Kind;
import io.protostuff.runtime.ArrayFixtures.MapHolder;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Malformed-input tests for {@link ArraySchemas} (CI-1 .. CI-8).
 * <p>
 * Each case takes the real protobuf encoding of an array, splits the nested array message into its wire fields,
 * applies one mutation, and asserts the exact current outcome of deserializing it (and of piping it to protostuff).
 * Expected outcomes are either {@link #CORRUPT} ({@code ProtostuffException("Corrupt input.")} somewhere in the cause
 * chain) or a successful decode.
 */
public class ArraySchemasCorruptInputTest extends TestCase
{

    static final int PRESERVE_FLAGS = IdStrategy.DEFAULT_FLAGS | IdStrategy.PRESERVE_NULL_ELEMENTS;

    static final String CORRUPT = "CORRUPT";

    static final String OK = "OK";

    private final DefaultIdStrategy strategy = ArrayFixtures.newStrategy(PRESERVE_FLAGS);

    // ------------------------------------------------------------------
    // protobuf wire helpers

    /**
     * One encoded protobuf field (tag included).
     */
    static final class WireField
    {
        final int number;
        final int wireType;
        final byte[] raw;
        /**
         * Offset of the payload within {@link #raw} (only for length-delimited fields).
         */
        final int payloadOffset;

        WireField(int number, int wireType, byte[] raw, int payloadOffset)
        {
            this.number = number;
            this.wireType = wireType;
            this.raw = raw;
            this.payloadOffset = payloadOffset;
        }

        byte[] payload()
        {
            if (wireType != 2)
                throw new IllegalStateException("Not length-delimited: " + number);

            final byte[] payload = new byte[raw.length - payloadOffset];
            System.arraycopy(raw, payloadOffset, payload, 0, payload.length);
            return payload;
        }
    }

    static List<WireField> parse(byte[] data)
    {
        final ArrayList<WireField> fields = new ArrayList<WireField>();
        final int[] pos = new int[] { 0 };
        while (pos[0] < data.length)
        {
            final int start = pos[0];
            final int tag = (int) readVarint(data, pos);
            final int wireType = tag & 7;
            int payloadOffset = 0;
            switch (wireType)
            {
                case 0:
                    readVarint(data, pos);
                    break;
                case 1:
                    pos[0] += 8;
                    break;
                case 2:
                    final int len = (int) readVarint(data, pos);
                    payloadOffset = pos[0] - start;
                    pos[0] += len;
                    break;
                case 5:
                    pos[0] += 4;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected wire type " + wireType);
            }
            final byte[] raw = new byte[pos[0] - start];
            System.arraycopy(data, start, raw, 0, raw.length);
            fields.add(new WireField(tag >>> 3, wireType, raw, payloadOffset));
        }
        return fields;
    }

    static long readVarint(byte[] data, int[] pos)
    {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7)
        {
            final byte b = data[pos[0]++];
            result |= (long) (b & 0x7f) << shift;
            if ((b & 0x80) == 0)
                return result;
        }
        throw new IllegalArgumentException("Malformed varint");
    }

    static void writeVarint(ByteArrayOutputStream out, long value)
    {
        while ((value & ~0x7fL) != 0)
        {
            out.write((int) ((value & 0x7f) | 0x80));
            value >>>= 7;
        }
        out.write((int) value);
    }

    static byte[] varintField(int number, long value)
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeVarint(out, number << 3);
        writeVarint(out, value);
        return out.toByteArray();
    }

    static byte[] lengthDelimited(int number, byte[] payload)
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeVarint(out, (number << 3) | 2);
        writeVarint(out, payload.length);
        out.write(payload, 0, payload.length);
        return out.toByteArray();
    }

    static byte[] concat(List<byte[]> parts)
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] part : parts)
            out.write(part, 0, part.length);
        return out.toByteArray();
    }

    static List<byte[]> raws(List<WireField> fields)
    {
        final ArrayList<byte[]> parts = new ArrayList<byte[]>();
        for (WireField f : fields)
            parts.add(f.raw);
        return parts;
    }

    static WireField single(List<WireField> fields)
    {
        assertEquals("expected exactly one field", 1, fields.size());
        return fields.get(0);
    }

    // ------------------------------------------------------------------
    // mutations of the nested array message (fields: LEN, then DATA / NULLCOUNT)

    /**
     * A mutation of the array message's wire fields.
     */
    static abstract class Mutation
    {
        final String name;

        Mutation(String name)
        {
            this.name = name;
        }

        abstract List<byte[]> apply(List<WireField> arrayFields);
    }

    static int lastDataIndex(List<WireField> fields)
    {
        for (int i = fields.size() - 1; i >= 0; i--)
        {
            if (fields.get(i).number == ArraySchemas.ID_ARRAY_DATA)
                return i;
        }
        throw new IllegalStateException("No data field");
    }

    /**
     * CI-1: the length field is missing, so the first field is not {@code ID_ARRAY_LEN}.
     */
    static final Mutation CI1_MISSING_LEN = new Mutation("CI-1 missing length")
    {
        @Override
        List<byte[]> apply(List<WireField> fields)
        {
            assertEquals(ArraySchemas.ID_ARRAY_LEN, fields.get(0).number);
            return raws(fields.subList(1, fields.size()));
        }
    };

    /**
     * CI-2: the first element is replaced by a null count of 1 (element count stays consistent with the length; only
     * invalid for primitive arrays).
     */
    static final Mutation CI2_NULLCOUNT_IN_PRIMITIVE = new Mutation("CI-2 null count in primitive array")
    {
        @Override
        List<byte[]> apply(List<WireField> fields)
        {
            assertEquals(ArraySchemas.ID_ARRAY_DATA, fields.get(1).number);
            final List<byte[]> parts = raws(fields);
            parts.set(1, varintField(ArraySchemas.ID_ARRAY_NULLCOUNT, 1));
            return parts;
        }
    };

    /**
     * CI-3: an unknown field number (4) inside the element loop.
     */
    static final Mutation CI3_UNKNOWN_FIELD = new Mutation("CI-3 unknown field")
    {
        @Override
        List<byte[]> apply(List<WireField> fields)
        {
            final List<byte[]> parts = raws(fields);
            parts.add(1, varintField(4, 1));
            return parts;
        }
    };

    /**
     * CI-4: fewer elements than the declared length (last element dropped).
     */
    static final Mutation CI4_TOO_FEW = new Mutation("CI-4 fewer elements than length")
    {
        @Override
        List<byte[]> apply(List<WireField> fields)
        {
            final List<byte[]> parts = raws(fields);
            parts.remove(lastDataIndex(fields));
            return parts;
        }
    };

    /**
     * CI-5: more elements than the declared length (last element duplicated).
     */
    static final Mutation CI5_TOO_MANY = new Mutation("CI-5 more elements than length")
    {
        @Override
        List<byte[]> apply(List<WireField> fields)
        {
            final List<byte[]> parts = raws(fields);
            parts.add(fields.get(lastDataIndex(fields)).raw);
            return parts;
        }
    };

    /**
     * CI-6: length 2 followed by a null count of 5 (overshoots the length).
     */
    static final Mutation CI6_NULLCOUNT_OVERSHOOT = new Mutation("CI-6 null count overshoots length")
    {
        @Override
        List<byte[]> apply(List<WireField> fields)
        {
            final ArrayList<byte[]> parts = new ArrayList<byte[]>();
            parts.add(varintField(ArraySchemas.ID_ARRAY_LEN, 2));
            parts.add(varintField(ArraySchemas.ID_ARRAY_NULLCOUNT, 5));
            return parts;
        }
    };

    // ------------------------------------------------------------------
    // encoding / decoding

    @SuppressWarnings("unchecked")
    private Schema<Object> schema(Class<?> holderType)
    {
        return (Schema<Object>) RuntimeSchema.getSchema(holderType, strategy);
    }

    /**
     * Path A: replaces the nested array message of the kind's field.
     */
    private byte[] mutateFieldHolder(Kind kind, Mutation mutation)
    {
        final Object holder = ArrayFixtures.newHolder(FieldHolder.class, kind, kind.sample());
        final byte[] valid = Format.PROTOBUF.toByteArray(holder, schema(FieldHolder.class));

        final WireField field = single(parse(valid));
        final byte[] arrayMessage = concat(mutation.apply(parse(field.payload())));
        return lengthDelimited(field.number, arrayMessage);
    }

    /**
     * Path D: replaces the nested array message of the map value ({@code field -> entry -> value}).
     */
    private byte[] mutateMapHolder(Kind kind, Mutation mutation)
    {
        final Object holder = ArrayFixtures.newHolder(MapHolder.class, kind, kind.sample());
        final byte[] valid = Format.PROTOBUF.toByteArray(holder, schema(MapHolder.class));

        final WireField field = single(parse(valid));
        final WireField entry = single(parse(field.payload()));
        final List<WireField> entryFields = parse(entry.payload());
        assertEquals("map entry must be key + value", 2, entryFields.size());

        final WireField key = entryFields.get(0), value = entryFields.get(1);
        final byte[] arrayMessage = concat(mutation.apply(parse(value.payload())));

        final ArrayList<byte[]> newEntry = new ArrayList<byte[]>();
        newEntry.add(key.raw);
        newEntry.add(lengthDelimited(value.number, arrayMessage));
        return lengthDelimited(field.number, lengthDelimited(entry.number, concat(newEntry)));
    }

    static ProtostuffException findProtostuffException(Throwable t)
    {
        for (Throwable c = t; c != null; c = c.getCause())
        {
            if (c instanceof ProtostuffException)
                return (ProtostuffException) c;
        }
        return null;
    }

    static String outcomeOf(Throwable t)
    {
        final ProtostuffException pe = findProtostuffException(t);
        if (pe != null && "Corrupt input.".equals(pe.getMessage()))
            return CORRUPT;

        return "EXCEPTION " + t;
    }

    /**
     * Decodes {@code data} (protobuf) and returns {@link #CORRUPT}, {@link #OK} or a description of the unexpected
     * failure. On success, {@code decodedHolder[0]} is set.
     */
    private String decode(Class<?> holderType, byte[] data, Object[] decodedHolder)
    {
        final Schema<Object> schema = schema(holderType);
        final Object message = schema.newMessage();
        try
        {
            ProtobufIOUtil.mergeFrom(data, message, schema);
        }
        catch (Throwable t)
        {
            return outcomeOf(t);
        }
        if (decodedHolder != null)
            decodedHolder[0] = message;
        return OK;
    }

    /**
     * CI-8: pipes {@code data} (protobuf) to protostuff.
     */
    private String pipe(Class<?> holderType, byte[] data)
    {
        @SuppressWarnings("unchecked")
        final Pipe.Schema<Object> pipeSchema = ((RuntimeSchema<Object>) schema(holderType)).getPipeSchema();
        try
        {
            ProtostuffIOUtil.toByteArray(ProtobufIOUtil.newPipe(data, 0, data.length), pipeSchema,
                    ArrayFixtures.buf());
        }
        catch (Throwable t)
        {
            return outcomeOf(t);
        }
        return OK;
    }

    private static void check(List<String> failures, String id, String expected, String actual)
    {
        if (!expected.equals(actual))
            failures.add(id + ": expected " + expected + " but was " + actual);
    }

    private static void assertNoFailures(List<String> failures)
    {
        if (!failures.isEmpty())
        {
            final StringBuilder sb = new StringBuilder().append(failures.size()).append(" failures:");
            for (String failure : failures)
                sb.append('\n').append(failure);
            fail(sb.toString());
        }
    }

    // ------------------------------------------------------------------
    // tests

    /**
     * Sanity check: the unmutated encodings decode and pipe fine, so the failures below come from the mutations.
     */
    public void testValidInputDecodes()
    {
        final Mutation identity = new Mutation("identity")
        {
            @Override
            List<byte[]> apply(List<WireField> fields)
            {
                return raws(fields);
            }
        };

        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            final byte[] data = mutateFieldHolder(kind, identity);
            check(failures, "A." + kind + ".decode", OK, decode(FieldHolder.class, data, null));
            check(failures, "A." + kind + ".pipe", OK, pipe(FieldHolder.class, data));
        }
        assertNoFailures(failures);
    }

    /**
     * CI-1, CI-3, CI-4, CI-5 (all kinds) and CI-2 (primitive kinds), path A, decode and pipe (CI-8).
     */
    public void testMalformedArrayMessage()
    {
        final Mutation[] mutations = new Mutation[] { CI1_MISSING_LEN, CI3_UNKNOWN_FIELD, CI4_TOO_FEW,
                CI5_TOO_MANY };

        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            for (Mutation mutation : mutations)
            {
                final byte[] data = mutateFieldHolder(kind, mutation);
                check(failures, "A." + kind + "." + mutation.name + ".decode", CORRUPT,
                        decode(FieldHolder.class, data, null));
                check(failures, "A." + kind + "." + mutation.name + ".pipe", CORRUPT,
                        pipe(FieldHolder.class, data));
            }

            if (kind.primitive)
            {
                final byte[] data = mutateFieldHolder(kind, CI2_NULLCOUNT_IN_PRIMITIVE);
                check(failures, "A." + kind + ".CI-2.decode", CORRUPT, decode(FieldHolder.class, data, null));
                // CHARACTERIZATION: possible bug. The shared pipe transfer (ArraySchemas.transferObject) accepts a
                // null count regardless of the component type, so a primitive array with a null count is piped
                // without error even though reading it fails.
                check(failures, "A." + kind + ".CI-2.pipe", OK, pipe(FieldHolder.class, data));
            }
        }
        assertNoFailures(failures);
    }

    /**
     * CI-6: a null count larger than the declared length (non-primitive kinds, path A).
     */
    public void testNullCountOvershootsLength() throws Exception
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            if (kind.primitive)
                continue;

            final byte[] data = mutateFieldHolder(kind, CI6_NULLCOUNT_OVERSHOOT);

            // CHARACTERIZATION: possible bug. The reader adds the null count to its index without checking it
            // against the declared length, so the malformed input is accepted silently and yields an array of the
            // declared length (2) with only nulls. The pipe transfer behaves the same way.
            final Object[] decoded = new Object[1];
            final String outcome = decode(FieldHolder.class, data, decoded);
            check(failures, "A." + kind + ".CI-6.decode", OK, outcome);
            if (OK.equals(outcome))
            {
                final Field f = FieldHolder.class.getDeclaredField(kind.name);
                f.setAccessible(true);
                final Object array = f.get(decoded[0]);
                final Object expected = Array.newInstance(kind.componentType, 2);
                if (!ArrayFixtures.deepEquals(expected, array))
                {
                    failures.add("A." + kind + ".CI-6.decode: expected " + ArrayFixtures.deepToString(expected)
                            + " but was " + ArrayFixtures.deepToString(array));
                }
            }
            check(failures, "A." + kind + ".CI-6.pipe", OK, pipe(FieldHolder.class, data));
        }
        assertNoFailures(failures);
    }

    /**
     * CI-7: missing length inside a map value (path D: derived schemas for the scalar kinds, generic element
     * schemas for enum, pojo and delegate), decode and pipe (CI-8).
     */
    public void testDerivedMissingLength()
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            final byte[] data = mutateMapHolder(kind, CI1_MISSING_LEN);
            check(failures, "D." + kind + ".CI-7.decode", CORRUPT, decode(MapHolder.class, data, null));
            check(failures, "D." + kind + ".CI-7.pipe", CORRUPT, pipe(MapHolder.class, data));
        }
        assertNoFailures(failures);
    }

    /**
     * CI-3, CI-4, CI-5 inside a map value (path D), decode and pipe.
     */
    public void testDerivedMalformedArrayMessage()
    {
        final Mutation[] mutations = new Mutation[] { CI3_UNKNOWN_FIELD, CI4_TOO_FEW, CI5_TOO_MANY };

        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            for (Mutation mutation : mutations)
            {
                final byte[] data = mutateMapHolder(kind, mutation);
                check(failures, "D." + kind + "." + mutation.name + ".decode", CORRUPT,
                        decode(MapHolder.class, data, null));
                check(failures, "D." + kind + "." + mutation.name + ".pipe", CORRUPT,
                        pipe(MapHolder.class, data));
            }
        }
        assertNoFailures(failures);
    }

    /**
     * Sanity check for the map helper: the decoded map holds exactly one entry under {@link ArrayFixtures#MAP_KEY}.
     */
    public void testMapHelperRoundTrip()
    {
        final Mutation identity = new Mutation("identity")
        {
            @Override
            List<byte[]> apply(List<WireField> fields)
            {
                return raws(fields);
            }
        };

        final Object[] decoded = new Object[1];
        assertEquals(OK, decode(MapHolder.class, mutateMapHolder(ArrayFixtures.B_INT, identity), decoded));
        final Map<String, Integer[]> map = ((MapHolder) decoded[0]).bInt;
        assertEquals(1, map.size());
        assertTrue(ArrayFixtures.deepEquals(ArrayFixtures.B_INT.sample(), map.get(ArrayFixtures.MAP_KEY)));
    }
}
