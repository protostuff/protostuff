package io.protostuff.runtime;

import static io.protostuff.runtime.ArrayFixtures.DELEGATE;
import static io.protostuff.runtime.ArrayFixtures.KINDS;

import io.protostuff.Schema;
import io.protostuff.runtime.ArrayFixtures.FieldHolder;
import io.protostuff.runtime.ArrayFixtures.Format;
import io.protostuff.runtime.ArrayFixtures.Kind;
import io.protostuff.runtime.ArrayFixtures.ListHolder;
import io.protostuff.runtime.ArrayFixtures.MapHolder;
import io.protostuff.runtime.ArrayFixtures.ObjectHolder;
import io.protostuff.runtime.SampleDelegates.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * Null-element policy tests for {@link ArraySchemas} (NP-1 .. NP-3), with {@link IdStrategy#PRESERVE_NULL_ELEMENTS}
 * off and on.
 */
public class ArraySchemasNullPolicyTest extends TestCase
{

    /**
     * Default flags, explicitly without {@link IdStrategy#PRESERVE_NULL_ELEMENTS} (in case the system property that
     * enables it by default is set).
     */
    static final int NO_PRESERVE_FLAGS = IdStrategy.DEFAULT_FLAGS & ~IdStrategy.PRESERVE_NULL_ELEMENTS;

    static final int PRESERVE_FLAGS = IdStrategy.DEFAULT_FLAGS | IdStrategy.PRESERVE_NULL_ELEMENTS;

    static final Class<?>[] HOLDERS = new Class<?>[] { FieldHolder.class, ObjectHolder.class, ListHolder.class,
            MapHolder.class };

    private final DefaultIdStrategy noPreserve = ArrayFixtures.newStrategy(NO_PRESERVE_FLAGS);

    private final DefaultIdStrategy preserve = ArrayFixtures.newStrategy(PRESERVE_FLAGS);

    @SuppressWarnings("unchecked")
    private static Schema<Object> schema(Class<?> holderType, IdStrategy strategy)
    {
        return (Schema<Object>) RuntimeSchema.getSchema(holderType, strategy);
    }

    private static String path(Class<?> holderType)
    {
        return holderType.getSimpleName();
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

    /**
     * Decodes and returns null on success, or the outcome description ({@code CORRUPT} / {@code EXCEPTION ...}).
     */
    private static String tryDecode(Format format, byte[] data, Object[] decoded, Schema<Object> schema)
    {
        final Object message = schema.newMessage();
        try
        {
            format.mergeFrom(data, message, schema);
        }
        catch (Throwable t)
        {
            return ArraySchemasCorruptInputTest.outcomeOf(t);
        }
        decoded[0] = message;
        return null;
    }

    /**
     * NP-1: with {@link IdStrategy#PRESERVE_NULL_ELEMENTS} off, an array containing nulls is written without error
     * (the nulls are dropped while the full length is still written), but reading it back fails with
     * {@code ProtostuffException("Corrupt input.")}. Every non-primitive kind, every path, every format.
     */
    public void testNullsWithoutPreserveCannotBeRead()
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (Class<?> holderType : HOLDERS)
        {
            final Schema<Object> schema = schema(holderType, noPreserve);
            for (Kind kind : KINDS)
            {
                if (kind.primitive)
                    continue;

                for (Format format : Format.values())
                {
                    final String id = path(holderType) + "." + kind + "." + format;
                    final Object holder = ArrayFixtures.newHolder(holderType, kind, kind.sampleWithNulls());

                    final byte[] data;
                    try
                    {
                        data = format.toByteArray(holder, schema);
                    }
                    catch (Throwable t)
                    {
                        failures.add(id + ": expected the write to succeed but it threw " + t);
                        continue;
                    }

                    // current behavior, kept on purpose (see ARRAYSCHEMAS_TEST_PLAN.md, decision 1)
                    final String outcome = tryDecode(format, data, new Object[1], schema);
                    if (!ArraySchemasCorruptInputTest.CORRUPT.equals(outcome))
                    {
                        failures.add(id + ": expected " + ArraySchemasCorruptInputTest.CORRUPT + " on read but was "
                                + (outcome == null ? "a successful read" : outcome));
                    }
                }
            }
        }
        assertNoFailures(failures);
    }

    /**
     * NP-2: arrays without nulls round-trip with the flag off. For paths A, B and D the bytes are identical to the
     * flag-on bytes.
     * <p>
     * Path C (repeated field) is the exception: {@link IdStrategy#PRESERVE_NULL_ELEMENTS} implicitly enables
     * {@link IdStrategy#COLLECTION_SCHEMA_ON_REPEATED_FIELDS} (see the {@code IdStrategy} constructor), which switches
     * the list field from the repeated-field factory to the collection-schema factory and therefore changes the bytes
     * even when there are no nulls.
     */
    public void testNullFreeArraysAreUnaffectedByTheFlag() throws Exception
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (Class<?> holderType : HOLDERS)
        {
            final Schema<Object> schemaOff = schema(holderType, noPreserve);
            final Schema<Object> schemaOn = schema(holderType, preserve);
            for (Kind kind : KINDS)
            {
                for (Format format : Format.values())
                {
                    final String id = path(holderType) + "." + kind + "." + format;
                    final Object holder = ArrayFixtures.newHolder(holderType, kind, kind.sample());

                    final byte[] off = format.toByteArray(holder, schemaOff);
                    final byte[] on = format.toByteArray(
                            ArrayFixtures.newHolder(holderType, kind, kind.sample()), schemaOn);

                    final boolean sameBytes = Arrays.equals(off, on);
                    if (ListHolder.class == holderType)
                    {
                        if (sameBytes)
                            failures.add(id + ": expected the bytes to differ (implicit collection schema)");
                    }
                    else if (!sameBytes)
                    {
                        failures.add(id + ": bytes differ\n  off: " + ArrayFixtures.toHex(off) + "\n  on:  "
                                + ArrayFixtures.toHex(on));
                    }

                    final Object[] decoded = new Object[1];
                    final String outcome = tryDecode(format, off, decoded, schemaOff);
                    if (ArraySchemasGoldenBytesTest.enumArrayDecodeThrowsNpe(kind, holderType, noPreserve))
                    {
                        // CHARACTERIZATION: possible bug, see ArraySchemasGoldenBytesTest#enumArrayDecodeThrowsNpe
                        if (outcome == null || !outcome.startsWith("EXCEPTION java.lang.NullPointerException"))
                            failures.add(id + ": expected the current NullPointerException but was " + outcome);
                        continue;
                    }

                    if (outcome != null)
                    {
                        failures.add(id + ": read failed: " + outcome);
                        continue;
                    }

                    final String diff = ArrayFixtures.diffHolders(holder, decoded[0]);
                    if (diff != null)
                        failures.add(id + ": " + diff);
                }
            }
        }
        assertNoFailures(failures);
    }

    /**
     * The null patterns of {@link NullArrayElementTest}, for the delegate kind.
     */
    static Singleton[][] delegateNullPatterns()
    {
        final Singleton s = Singleton.INSTANCE;
        return new Singleton[][] {
                { null, null, null }, // all
                { null, s, s }, // first
                { s, s, null }, // last
                { s, null, s }, // mid
                { null, s, null }, // first and last
                { s, null, null, s }, // in between
        };
    }

    /**
     * NP-3: with the flag on, delegate arrays keep every null pattern on every path and format, including the
     * protobuf/protostuff pipe (closes the delegate gap of {@link NullArrayElementTest}).
     */
    public void testDelegateArrayNullPatterns() throws Exception
    {
        final ArrayList<String> failures = new ArrayList<String>();
        final Singleton[][] patterns = delegateNullPatterns();
        for (Class<?> holderType : HOLDERS)
        {
            final Schema<Object> schema = schema(holderType, preserve);
            for (int p = 0; p < patterns.length; p++)
            {
                final Object holder = ArrayFixtures.newHolder(holderType, DELEGATE, patterns[p].clone());
                for (Format format : Format.values())
                {
                    final String id = path(holderType) + ".pattern" + p + "." + format;
                    final Object[] decoded = new Object[1];
                    final String outcome = tryDecode(format, format.toByteArray(holder, schema), decoded, schema);
                    if (outcome != null)
                    {
                        failures.add(id + ": read failed: " + outcome);
                        continue;
                    }

                    final String diff = ArrayFixtures.diffHolders(holder, decoded[0]);
                    if (diff != null)
                        failures.add(id + ": " + diff);
                }

                final String pipeDiff = ArrayFixtures.pipeRoundTrip(holder, schema);
                if (pipeDiff != null)
                    failures.add(path(holderType) + ".pattern" + p + ".pipe: " + pipeDiff);
            }
        }
        assertNoFailures(failures);
    }
}
