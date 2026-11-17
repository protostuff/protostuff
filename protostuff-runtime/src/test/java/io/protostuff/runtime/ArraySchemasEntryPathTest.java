package io.protostuff.runtime;

import static io.protostuff.runtime.ArrayFixtures.DELEGATE;
import static io.protostuff.runtime.ArrayFixtures.ENUM;
import static io.protostuff.runtime.ArrayFixtures.KINDS;
import static io.protostuff.runtime.ArrayFixtures.POJO;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGDECIMAL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BIGINTEGER;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BOOL;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTES;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_BYTE_ARRAY;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_CHAR;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DATE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import static io.protostuff.runtime.RuntimeFieldFactory.ID_STRING;

import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.ArrayFixtures.Format;
import io.protostuff.runtime.ArrayFixtures.Kind;
import io.protostuff.runtime.ArrayFixtures.ListHolder;
import io.protostuff.runtime.ArrayFixtures.MapHolder;
import io.protostuff.runtime.ArrayFixtures.ObjectHolder;
import io.protostuff.runtime.NullArrayElementTest.Size;
import io.protostuff.runtime.NullArrayElementTest.SomePojo;
import io.protostuff.runtime.SampleDelegates.Singleton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Entry-path tests for {@link ArraySchemas} (EP-1 .. EP-9, plus the field naming of TF-4).
 * <p>
 * Pins which schema instance each id resolves to, the inline array id arithmetic, the fallback branches, and the
 * multi-element round trip of arrays as list elements, map values and map keys (including the pipe).
 */
public class ArraySchemasEntryPathTest extends TestCase
{

    static final int NO_PRESERVE_FLAGS = IdStrategy.DEFAULT_FLAGS & ~IdStrategy.PRESERVE_NULL_ELEMENTS;

    static final int PRESERVE_FLAGS = IdStrategy.DEFAULT_FLAGS | IdStrategy.PRESERVE_NULL_ELEMENTS;

    /**
     * The scalar ids handled by {@link ArraySchemas}, in the order of {@link #PRIMITIVE_ARRAY_IDS} /
     * {@link #BOXED_ARRAY_IDS}.
     */
    static final int[] SCALAR_IDS = new int[] { ID_BOOL, ID_CHAR, ID_SHORT, ID_INT32, ID_INT64, ID_FLOAT,
            ID_DOUBLE, ID_STRING, ID_BYTES, ID_BYTE_ARRAY, ID_BIGDECIMAL, ID_BIGINTEGER, ID_DATE };

    /**
     * Inline array ids of the primitive arrays (-1 where there is no primitive form).
     */
    static final int[] PRIMITIVE_ARRAY_IDS = new int[] { 0, 2, 3, 4, 5, 6, 7, -1, -1, -1, -1, -1, -1 };

    /**
     * Inline array ids of the boxed / reference arrays.
     */
    static final int[] BOXED_ARRAY_IDS = new int[] { 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 };

    private final DefaultIdStrategy strategy = ArrayFixtures.newStrategy(PRESERVE_FLAGS);

    private final DefaultIdStrategy noPreserve = ArrayFixtures.newStrategy(NO_PRESERVE_FLAGS);

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
    // EP-1 .. EP-4: dispatch

    private static ArraySchemas.Base[] expectedPrimitiveSchemas(IdStrategy s)
    {
        return new ArraySchemas.Base[] { s.ARRAY_BOOL_PRIMITIVE_SCHEMA, s.ARRAY_CHAR_PRIMITIVE_SCHEMA,
                s.ARRAY_SHORT_PRIMITIVE_SCHEMA, s.ARRAY_INT32_PRIMITIVE_SCHEMA, s.ARRAY_INT64_PRIMITIVE_SCHEMA,
                s.ARRAY_FLOAT_PRIMITIVE_SCHEMA, s.ARRAY_DOUBLE_PRIMITIVE_SCHEMA };
    }

    private static ArraySchemas.Base[] expectedBoxedSchemas(IdStrategy s)
    {
        return new ArraySchemas.Base[] { s.ARRAY_BOOL_BOXED_SCHEMA, s.ARRAY_CHAR_BOXED_SCHEMA,
                s.ARRAY_SHORT_BOXED_SCHEMA, s.ARRAY_INT32_BOXED_SCHEMA, s.ARRAY_INT64_BOXED_SCHEMA,
                s.ARRAY_FLOAT_BOXED_SCHEMA, s.ARRAY_DOUBLE_BOXED_SCHEMA, s.ARRAY_STRING_SCHEMA,
                s.ARRAY_BYTESTRING_SCHEMA, s.ARRAY_BYTEARRAY_SCHEMA, s.ARRAY_BIGDECIMAL_SCHEMA,
                s.ARRAY_BIGINTEGER_SCHEMA, s.ARRAY_DATE_SCHEMA };
    }

    private static ArraySchemas.Base[] expectedGenericElementSchemas(IdStrategy s)
    {
        return new ArraySchemas.Base[] { s.ARRAY_BOOL_DERIVED_SCHEMA, s.ARRAY_CHAR_DERIVED_SCHEMA,
                s.ARRAY_SHORT_DERIVED_SCHEMA, s.ARRAY_INT32_DERIVED_SCHEMA, s.ARRAY_INT64_DERIVED_SCHEMA,
                s.ARRAY_FLOAT_DERIVED_SCHEMA, s.ARRAY_DOUBLE_DERIVED_SCHEMA, s.ARRAY_STRING_SCHEMA,
                s.ARRAY_BYTESTRING_SCHEMA, s.ARRAY_BYTEARRAY_SCHEMA, s.ARRAY_BIGDECIMAL_SCHEMA,
                s.ARRAY_BIGINTEGER_SCHEMA, s.ARRAY_DATE_SCHEMA };
    }

    /**
     * EP-1: {@code getSchema(id, primitive, strategy)} returns the strategy's shared instances.
     */
    public void testGetSchemaReturnsSharedInstances()
    {
        final ArraySchemas.Base[] primitive = expectedPrimitiveSchemas(strategy);
        final ArraySchemas.Base[] boxed = expectedBoxedSchemas(strategy);
        for (int i = 0; i < SCALAR_IDS.length; i++)
        {
            final int id = SCALAR_IDS[i];
            if (i < primitive.length)
                assertSame("primitive id " + id, primitive[i], ArraySchemas.getSchema(id, true, strategy));

            assertSame("boxed id " + id, boxed[i], ArraySchemas.getSchema(id, false, strategy));
        }

        // non-primitive ids ignore the primitive flag
        for (int i = primitive.length; i < SCALAR_IDS.length; i++)
            assertSame(boxed[i], ArraySchemas.getSchema(SCALAR_IDS[i], true, strategy));
    }

    /**
     * EP-2: {@code getGenericElementSchema(id, strategy)} returns the derived schemas for the primitive-capable ids
     * and the shared schemas otherwise (including {@code ID_BYTES} and {@code ID_BYTE_ARRAY}).
     */
    public void testGetGenericElementSchema()
    {
        final ArraySchemas.Base[] expected = expectedGenericElementSchemas(strategy);
        for (int i = 0; i < SCALAR_IDS.length; i++)
        {
            assertSame("id " + SCALAR_IDS[i], expected[i],
                    ArraySchemas.getGenericElementSchema(SCALAR_IDS[i], strategy));
        }
    }

    /**
     * EP-3: inline array id arithmetic ({@code toArrayId}, {@code toInlineId}, {@code isPrimitive}).
     */
    public void testInlineArrayIds()
    {
        for (int i = 0; i < SCALAR_IDS.length; i++)
        {
            final int id = SCALAR_IDS[i];

            final int boxedArrayId = ArraySchemas.toArrayId(id, false);
            assertEquals("boxed array id of " + id, BOXED_ARRAY_IDS[i], boxedArrayId);
            assertEquals(id, ArraySchemas.toInlineId(boxedArrayId));
            assertFalse(ArraySchemas.isPrimitive(boxedArrayId));

            if (PRIMITIVE_ARRAY_IDS[i] != -1)
            {
                final int primitiveArrayId = ArraySchemas.toArrayId(id, true);
                assertEquals("primitive array id of " + id, PRIMITIVE_ARRAY_IDS[i], primitiveArrayId);
                assertEquals(id, ArraySchemas.toInlineId(primitiveArrayId));
                assertTrue(ArraySchemas.isPrimitive(primitiveArrayId));
            }
        }

        // ID_BYTE has slots in the id arithmetic even though no Byte[] schema exists (see testBoxedByteArray...)
        assertEquals(1, ArraySchemas.toArrayId(ID_BYTE, true));
        assertEquals(9, ArraySchemas.toArrayId(ID_BYTE, false));
        assertEquals(ID_BYTE, ArraySchemas.toInlineId(1));
        assertEquals(ID_BYTE, ArraySchemas.toInlineId(9));
    }

    private static void assertShouldNotHappen(String label, Runnable call)
    {
        try
        {
            call.run();
            fail(label + ": expected RuntimeException(\"Should not happen.\")");
        }
        catch (RuntimeException e)
        {
            assertEquals(label, "Should not happen.", e.getMessage());
        }
    }

    /**
     * EP-4: unknown ids (and {@code ID_BYTE}) fall through to {@code RuntimeException("Should not happen.")}.
     */
    public void testUnknownIdsShouldNotHappen()
    {
        final int[] unknownIds = new int[] { 0, ID_BYTE, 99 };
        for (final int id : unknownIds)
        {
            assertShouldNotHappen("getSchema(" + id + ", true)", new Runnable()
            {
                @Override
                public void run()
                {
                    ArraySchemas.getSchema(id, true, strategy);
                }
            });
            assertShouldNotHappen("getSchema(" + id + ", false)", new Runnable()
            {
                @Override
                public void run()
                {
                    ArraySchemas.getSchema(id, false, strategy);
                }
            });
            assertShouldNotHappen("getGenericElementSchema(" + id + ")", new Runnable()
            {
                @Override
                public void run()
                {
                    ArraySchemas.getGenericElementSchema(id, strategy);
                }
            });
            assertShouldNotHappen("newSchema(" + id + ")", new Runnable()
            {
                @Override
                public void run()
                {
                    ArraySchemas.newSchema(id, Object.class, Object[].class, strategy, null);
                }
            });
        }
    }

    static final class BoxedByteField
    {
        @Tag(1) Byte[] value;
    }

    static final class BoxedByteList
    {
        @Tag(1) List<Byte[]> value;
    }

    static final class BoxedByteMap
    {
        @Tag(1) Map<String, Byte[]> value;
    }

    /**
     * CHARACTERIZATION: possible bug. {@code Byte[]} (boxed bytes) has no array schema: {@code ID_BYTE} is missing
     * from {@code getSchema} / {@code getGenericElementSchema} / {@code newSchema}. A pojo with a {@code Byte[]} field
     * (or a list / map of {@code Byte[]}) cannot even get a schema, and a {@code Byte[]} inside an {@code Object} field
     * fails when written.
     */
    public void testBoxedByteArrayIsNotSupported()
    {
        final Class<?>[] holders = new Class<?>[] { BoxedByteField.class, BoxedByteList.class,
                BoxedByteMap.class };
        for (final Class<?> holder : holders)
        {
            assertShouldNotHappen("schema of " + holder.getSimpleName(), new Runnable()
            {
                @Override
                public void run()
                {
                    RuntimeSchema.getSchema(holder, ArrayFixtures.newStrategy(PRESERVE_FLAGS));
                }
            });
        }

        @SuppressWarnings("unchecked")
        final Schema<Object> schema = (Schema<Object>) (Schema<?>) RuntimeSchema.getSchema(ObjectHolder.class,
                strategy);
        final ObjectHolder message = new ObjectHolder();
        message.value = new Byte[] { Byte.valueOf((byte) 1) };
        for (final Format format : Format.values())
        {
            assertShouldNotHappen("write Byte[] in Object field (" + format + ")", new Runnable()
            {
                @Override
                public void run()
                {
                    format.toByteArray(message, schema);
                }
            });
        }
    }

    // ------------------------------------------------------------------
    // TF-4: field naming used by the text formats

    private List<ArraySchemas.Base> allSchemas()
    {
        final ArrayList<ArraySchemas.Base> schemas = new ArrayList<ArraySchemas.Base>();
        for (ArraySchemas.Base s : expectedPrimitiveSchemas(strategy))
            schemas.add(s);
        for (ArraySchemas.Base s : expectedBoxedSchemas(strategy))
            schemas.add(s);
        for (ArraySchemas.Base s : expectedGenericElementSchemas(strategy))
            schemas.add(s);
        schemas.add((ArraySchemas.Base) strategy.getEnumIO(Size.class).genericElementSchema);
        schemas.add((ArraySchemas.Base) strategy.getSchemaWrapper(SomePojo.class, true).genericElementSchema);
        schemas.add((ArraySchemas.Base) strategy.getDelegateWrapper(Singleton.class).genericElementSchema);
        return schemas;
    }

    /**
     * TF-4: {@code getFieldName}, {@code getFieldNumber}, {@code messageName} and {@code messageFullName} of every
     * array schema.
     */
    public void testFieldNaming()
    {
        for (ArraySchemas.Base schema : allSchemas())
        {
            final String label = schema.getClass().getName();
            assertEquals(label, "a", schema.getFieldName(ArraySchemas.ID_ARRAY_LEN));
            assertEquals(label, "b", schema.getFieldName(ArraySchemas.ID_ARRAY_DATA));
            assertEquals(label, "c", schema.getFieldName(ArraySchemas.ID_ARRAY_NULLCOUNT));
            assertNull(label, schema.getFieldName(0));
            assertNull(label, schema.getFieldName(4));

            assertEquals(label, ArraySchemas.ID_ARRAY_LEN, schema.getFieldNumber("a"));
            assertEquals(label, ArraySchemas.ID_ARRAY_DATA, schema.getFieldNumber("b"));
            assertEquals(label, ArraySchemas.ID_ARRAY_NULLCOUNT, schema.getFieldNumber("c"));
            assertEquals(label, 0, schema.getFieldNumber("d"));
            assertEquals(label, 0, schema.getFieldNumber("ab"));
            assertEquals(label, 0, schema.getFieldNumber(""));

            assertEquals(label, "Array", schema.messageName());
            assertEquals(label, "java.lang.reflect.Array", schema.messageFullName());
        }
    }

    // ------------------------------------------------------------------
    // EP-5 .. EP-9: arrays as collection elements, map values and map keys

    /**
     * A path through which arrays are collection elements or map values.
     */
    static final class ElementPath
    {
        final String name;
        final Class<?> holderType;
        final IdStrategy strategy;
        final boolean nulls;

        ElementPath(String name, Class<?> holderType, IdStrategy strategy, boolean nulls)
        {
            this.name = name;
            this.holderType = holderType;
            this.strategy = strategy;
            this.nulls = nulls;
        }
    }

    private List<ElementPath> elementPaths()
    {
        final ArrayList<ElementPath> paths = new ArrayList<ElementPath>();
        // path C: repeated field, handler-bound schema (nulls are not supported without PRESERVE_NULL_ELEMENTS)
        paths.add(new ElementPath("C_repeated", ListHolder.class, noPreserve, false));
        // path D: collection schema / map, derived (generic element) schema
        paths.add(new ElementPath("D_listDerived", ListHolder.class, strategy, true));
        paths.add(new ElementPath("D_mapDerived", MapHolder.class, strategy, true));
        paths.add(new ElementPath("D_mapDerivedNoPreserve", MapHolder.class, noPreserve, false));
        return paths;
    }

    /**
     * A holder with 3 arrays (different lengths, and one with nulls when supported) as list elements or map values.
     */
    static Object newMultiHolder(ElementPath path, Kind kind) throws Exception
    {
        final Object[] arrays = new Object[] {
                kind.sample(),
                path.nulls && !kind.primitive ? kind.sampleWithNulls() : kind.large(5, false),
                kind.large(1, false) };

        final Object holder = path.holderType.newInstance();
        final Field f = path.holderType.getDeclaredField(kind.name);
        f.setAccessible(true);
        if (ListHolder.class == path.holderType)
        {
            final ArrayList<Object> list = new ArrayList<Object>();
            for (Object array : arrays)
                list.add(array);
            f.set(holder, list);
        }
        else
        {
            final LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            for (int i = 0; i < arrays.length; i++)
                map.put("key" + i, arrays[i]);
            f.set(holder, map);
        }
        return holder;
    }

    /**
     * EP-5, EP-6, EP-8, EP-9: every kind as multiple list elements / map values, through every format, compared
     * element by element, plus the protobuf/protostuff pipe.
     */
    public void testMultipleArraysAsElementsAndValues() throws Exception
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (ElementPath path : elementPaths())
        {
            @SuppressWarnings("unchecked")
            final Schema<Object> schema = (Schema<Object>) RuntimeSchema.getSchema(path.holderType,
                    path.strategy);
            for (Kind kind : KINDS)
            {
                final Object holder = newMultiHolder(path, kind);
                final boolean npe = ArraySchemasGoldenBytesTest.enumArrayDecodeThrowsNpe(kind, path.holderType,
                        path.strategy);
                for (Format format : Format.values())
                {
                    final String id = path.name + "." + kind + "." + format;
                    final Object decoded = schema.newMessage();
                    try
                    {
                        format.mergeFrom(format.toByteArray(holder, schema), decoded, schema);
                    }
                    catch (NullPointerException e)
                    {
                        // CHARACTERIZATION: possible bug, see ArraySchemasGoldenBytesTest#enumArrayDecodeThrowsNpe
                        if (!npe)
                            failures.add(id + ": " + e);
                        continue;
                    }
                    catch (Exception e)
                    {
                        failures.add(id + ": " + e);
                        continue;
                    }

                    if (npe)
                    {
                        failures.add(id + ": expected the current NullPointerException but decoding succeeded");
                        continue;
                    }

                    final String diff = ArrayFixtures.diffHolders(holder, decoded);
                    if (diff != null)
                        failures.add(id + ": " + diff);
                }

                // EP-8: the pipe does not use the handler, so it works for every kind (enum included)
                final String pipeDiff = ArrayFixtures.pipeRoundTrip(holder, schema);
                if (pipeDiff != null)
                    failures.add(path.name + "." + kind + ".pipe: " + pipeDiff);
            }
        }
        assertNoFailures(failures);
    }

    /**
     * EP-6: map values that are delegate / pojo arrays are restored with their component type through
     * {@code ArraySchemas.GENERIC_HANDLER}; enum arrays hit the null handler (characterized NPE).
     */
    public void testMapValuesUseTheGenericHandler() throws Exception
    {
        @SuppressWarnings("unchecked")
        final Schema<Object> schema = (Schema<Object>) (Schema<?>) RuntimeSchema.getSchema(MapHolder.class,
                strategy);

        final MapHolder holder = new MapHolder();
        holder.delegate = new HashMap<String, Singleton[]>();
        holder.delegate.put("d", new Singleton[] { Singleton.INSTANCE, null });
        holder.pojo = new HashMap<String, SomePojo[]>();
        holder.pojo.put("p", new SomePojo[] { new SomePojo("x"), null });

        for (Format format : Format.values())
        {
            final MapHolder decoded = (MapHolder) schema.newMessage();
            format.mergeFrom(format.toByteArray(holder, schema), decoded, schema);
            assertEquals(format.name(), Singleton[].class, decoded.delegate.get("d").getClass());
            assertEquals(format.name(), SomePojo[].class, decoded.pojo.get("p").getClass());
            assertNull(ArrayFixtures.diffHolders(holder, decoded));
        }

        final MapHolder enumHolder = (MapHolder) ArrayFixtures.newHolder(MapHolder.class, ENUM, ENUM.sample());
        for (Format format : Format.values())
        {
            final Object decoded = schema.newMessage();
            try
            {
                format.mergeFrom(format.toByteArray(enumHolder, schema), decoded, schema);
                fail(format + ": expected the current NullPointerException (enum array as a map value)");
            }
            catch (NullPointerException e)
            {
                // CHARACTERIZATION: possible bug, see ArraySchemasGoldenBytesTest#enumArrayDecodeThrowsNpe
            }
        }
    }

    /**
     * Arrays as map keys.
     */
    static final class KeyHolder
    {
        @Tag(1) Map<int[], String> pInt;
        @Tag(2) Map<Integer[], String> bInt;
        @Tag(3) Map<String[], String> string;
        @Tag(4) Map<byte[][], String> byteArray;
        @Tag(5) Map<Size[], String> enumSize;
        @Tag(6) Map<SomePojo[], String> pojo;
        @Tag(7) Map<Singleton[], String> delegate;
    }

    static final Kind[] KEY_KINDS = new Kind[] { ArrayFixtures.P_INT, ArrayFixtures.B_INT, ArrayFixtures.STRING,
            ArrayFixtures.BYTE_ARRAY, ENUM, POJO, DELEGATE };

    /**
     * EP-7: arrays as map keys (single entry, compared by content since array keys hash by identity).
     */
    public void testArraysAsMapKeys() throws Exception
    {
        @SuppressWarnings("unchecked")
        final Schema<Object> schema = (Schema<Object>) (Schema<?>) RuntimeSchema.getSchema(KeyHolder.class,
                strategy);

        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KEY_KINDS)
        {
            final Field f = KeyHolder.class.getDeclaredField(kind.name);
            f.setAccessible(true);

            final Object key = kind.primitive ? kind.sample() : kind.sampleWithNulls();
            final HashMap<Object, String> map = new HashMap<Object, String>();
            map.put(key, "value");
            final KeyHolder holder = new KeyHolder();
            f.set(holder, map);

            final boolean npe = kind == ENUM;
            for (Format format : Format.values())
            {
                final String id = kind + "." + format;
                final KeyHolder decoded = (KeyHolder) schema.newMessage();
                try
                {
                    format.mergeFrom(format.toByteArray(holder, schema), decoded, schema);
                }
                catch (NullPointerException e)
                {
                    // CHARACTERIZATION: possible bug, same null handler as for enum map values
                    if (!npe)
                        failures.add(id + ": " + e);
                    continue;
                }
                catch (Exception e)
                {
                    failures.add(id + ": " + e);
                    continue;
                }

                if (npe)
                {
                    failures.add(id + ": expected the current NullPointerException but decoding succeeded");
                    continue;
                }

                final Map<?, ?> decodedMap = (Map<?, ?>) f.get(decoded);
                if (decodedMap == null || decodedMap.size() != 1)
                {
                    failures.add(id + ": map = " + ArrayFixtures.deepToString(decodedMap));
                    continue;
                }

                final Map.Entry<?, ?> entry = decodedMap.entrySet().iterator().next();
                if (!ArrayFixtures.deepEquals(key, entry.getKey()))
                {
                    failures.add(id + ": key = " + ArrayFixtures.deepToString(entry.getKey()));
                }
                if (!"value".equals(entry.getValue()))
                    failures.add(id + ": value = " + entry.getValue());
            }

            final String pipeDiff = ArrayFixtures.pipeRoundTrip(holder, schema);
            if (pipeDiff != null)
                failures.add(kind + ".pipe: " + pipeDiff);
        }
        assertNoFailures(failures);
    }
}
