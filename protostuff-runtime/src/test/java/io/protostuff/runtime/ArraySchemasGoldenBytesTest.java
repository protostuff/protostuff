package io.protostuff.runtime;

import static io.protostuff.runtime.ArrayFixtures.B_INT;
import static io.protostuff.runtime.ArrayFixtures.ENUM;
import static io.protostuff.runtime.ArrayFixtures.KINDS;
import static io.protostuff.runtime.ArrayFixtures.P_INT;

import io.protostuff.Schema;
import io.protostuff.runtime.ArrayFixtures.FieldHolder;
import io.protostuff.runtime.ArrayFixtures.Format;
import io.protostuff.runtime.ArrayFixtures.Kind;
import io.protostuff.runtime.ArrayFixtures.ListHolder;
import io.protostuff.runtime.ArrayFixtures.MapHolder;
import io.protostuff.runtime.ArrayFixtures.ObjectHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import junit.framework.TestCase;

/**
 * Golden-bytes (characterization) test for {@link ArraySchemas}: pins the exact wire encoding of every array element
 * kind, through every entry path, in the protostuff, protobuf and graph formats.
 * <p>
 * Round-trip tests cannot detect a change made symmetrically to the writer and the reader. This test compares the
 * serialized bytes against fixtures generated from the pre-refactor code, and also decodes each fixture to prove that
 * previously persisted data stays readable.
 * <p>
 * To regenerate the fixtures (only when a wire change is intended), run from the {@code protostuff-runtime} module:
 * {@code mvn test -Dtest=ArraySchemasGoldenBytesTest -Dprotostuff.golden.regen=true}
 * <p>
 * Case ids are {@code <path>.<kind>.<pattern>.<config>.<format>}. Fixtures are grouped in one file per path under
 * {@code src/test/resources/io/protostuff/runtime/array-golden/}.
 */
public class ArraySchemasGoldenBytesTest extends TestCase
{

    static final String REGEN_PROPERTY = "protostuff.golden.regen";

    static final String RESOURCE_DIR = "io/protostuff/runtime/array-golden/";

    static final String SOURCE_DIR = "src/test/resources/" + RESOURCE_DIR;

    /**
     * Fallback for modules (e.g. protostuff-runtime-md) that copy the test sources but not the test resources.
     */
    static final String FALLBACK_DIR = "../protostuff-runtime/" + SOURCE_DIR;

    // flag configs

    static final String CFG_PRESERVE = "preserve", CFG_DEFAULT = "default", CFG_ENUM_NAMES = "enumNames";

    // patterns

    static final String PAT_PLAIN = "plain", PAT_NULLS = "nulls", PAT_LARGE = "large",
            PAT_LARGE_NULLS = "largeNulls";

    /**
     * Length of the large arrays (GB-5): multi-byte varint length, and a multi-byte varint null count.
     */
    static final int LARGE_LEN = 200;

    /**
     * One golden case.
     */
    static final class Case
    {
        final String id;
        final Object holder;
        final Schema<Object> schema;
        final Format format;
        final boolean decodeThrowsNpe;

        Case(String id, Object holder, Schema<Object> schema, Format format, boolean decodeThrowsNpe)
        {
            this.id = id;
            this.holder = holder;
            this.schema = schema;
            this.format = format;
            this.decodeThrowsNpe = decodeThrowsNpe;
        }
    }

    /**
     * CHARACTERIZATION: possible bug. An enum array that goes through the generic element schema (a map value, or a
     * list element when {@link IdStrategy#COLLECTION_SCHEMA_ON_REPEATED_FIELDS} is in effect, which
     * {@link IdStrategy#PRESERVE_NULL_ELEMENTS} implies) is serialized, but deserializing it throws a
     * {@link NullPointerException}: {@code EnumIO} creates its {@code genericElementSchema} ({@code EnumArray}) with a
     * null {@code Handler}, whereas {@code HasSchema}/{@code HasDelegate} use {@code ArraySchemas.GENERIC_HANDLER}.
     */
    static boolean enumArrayDecodeThrowsNpe(Kind kind, Class<?> holderType, IdStrategy strategy)
    {
        if (kind != ENUM)
            return false;

        if (MapHolder.class == holderType)
            return true;

        return ListHolder.class == holderType
                && 0 != (strategy.flags & IdStrategy.COLLECTION_SCHEMA_ON_REPEATED_FIELDS);
    }

    /**
     * Strategies are cached per flags so that every case of a run uses one strategy instance per configuration.
     */
    private final HashMap<Integer, DefaultIdStrategy> strategies = new HashMap<Integer, DefaultIdStrategy>();

    private DefaultIdStrategy strategy(int flags)
    {
        final Integer key = Integer.valueOf(flags);
        DefaultIdStrategy strategy = strategies.get(key);
        if (strategy == null)
        {
            strategy = ArrayFixtures.newStrategy(flags);
            strategies.put(key, strategy);
        }
        return strategy;
    }

    @SuppressWarnings("unchecked")
    private Schema<Object> schema(Class<?> holderType, int flags)
    {
        return (Schema<Object>) RuntimeSchema.getSchema(holderType, strategy(flags));
    }

    private static int configFlags(String config)
    {
        if (CFG_PRESERVE.equals(config))
            return IdStrategy.DEFAULT_FLAGS | IdStrategy.PRESERVE_NULL_ELEMENTS;
        if (CFG_DEFAULT.equals(config))
            return IdStrategy.DEFAULT_FLAGS;
        if (CFG_ENUM_NAMES.equals(config))
            return IdStrategy.DEFAULT_FLAGS | IdStrategy.PRESERVE_NULL_ELEMENTS | IdStrategy.ENUMS_BY_NAME;

        throw new IllegalArgumentException(config);
    }

    private static Object array(Kind kind, String pattern)
    {
        if (PAT_PLAIN.equals(pattern))
            return kind.sample();
        if (PAT_NULLS.equals(pattern))
            return kind.sampleWithNulls();
        if (PAT_LARGE.equals(pattern))
            return kind.large(LARGE_LEN, false);
        if (PAT_LARGE_NULLS.equals(pattern))
            return kind.large(LARGE_LEN, true);

        throw new IllegalArgumentException(pattern);
    }

    private void addCase(List<Case> cases, String path, Class<?> holderType, int extraFlags,
            Kind kind, String pattern, String config)
    {
        final int flags = configFlags(config) | extraFlags;
        final Schema<Object> schema = schema(holderType, flags);
        final boolean decodeThrowsNpe = enumArrayDecodeThrowsNpe(kind, holderType, strategy(flags));
        for (Format format : Format.values())
        {
            final String id = path + "." + kind.name + "." + pattern + "." + config + "."
                    + format.name().toLowerCase();
            // a new holder (and array) per case, since graph serialization tracks references
            cases.add(new Case(id, ArrayFixtures.newHolder(holderType, kind, array(kind, pattern)),
                    schema, format, decodeThrowsNpe));
        }
    }

    /**
     * Builds the cases of one path:
     * <ul>
     * <li>{@code preserve} config: plain (all kinds) and nulls (non-primitive kinds)</li>
     * <li>{@code default} config: plain only (nulls without PRESERVE_NULL_ELEMENTS cannot be read back, see
     * {@link ArraySchemasNullPolicyTest})</li>
     * <li>{@code enumNames} config (GB-6): enum kind, plain and nulls</li>
     * <li>large arrays (GB-5): path A only, int and Integer</li>
     * </ul>
     */
    private List<Case> buildCases(String path, Class<?> holderType, int extraFlags)
    {
        final ArrayList<Case> cases = new ArrayList<Case>();
        for (Kind kind : KINDS)
        {
            addCase(cases, path, holderType, extraFlags, kind, PAT_PLAIN, CFG_PRESERVE);
            if (!kind.primitive)
                addCase(cases, path, holderType, extraFlags, kind, PAT_NULLS, CFG_PRESERVE);

            addCase(cases, path, holderType, extraFlags, kind, PAT_PLAIN, CFG_DEFAULT);
        }

        addCase(cases, path, holderType, extraFlags, ENUM, PAT_PLAIN, CFG_ENUM_NAMES);
        addCase(cases, path, holderType, extraFlags, ENUM, PAT_NULLS, CFG_ENUM_NAMES);

        if (FieldHolder.class == holderType)
        {
            addCase(cases, path, holderType, extraFlags, P_INT, PAT_LARGE, CFG_PRESERVE);
            addCase(cases, path, holderType, extraFlags, B_INT, PAT_LARGE, CFG_PRESERVE);
            addCase(cases, path, holderType, extraFlags, B_INT, PAT_LARGE_NULLS, CFG_PRESERVE);
        }

        return cases;
    }

    // ------------------------------------------------------------------
    // tests

    /**
     * Path A: array-typed fields (GB-4, GB-5, GB-6, GB-7 included).
     */
    public void testPathAField() throws IOException
    {
        verify("A_field", FieldHolder.class, 0);
    }

    /**
     * Path B: array inside an {@code Object} field. Pins the inline array ids (GB-3).
     */
    public void testPathBObjectField() throws IOException
    {
        verify("B_objectField", ObjectHolder.class, 0);
    }

    /**
     * Path C: array as a repeated-field element (default flags).
     */
    public void testPathCRepeated() throws IOException
    {
        verify("C_repeated", ListHolder.class, 0);
    }

    /**
     * Path D: array as a map value, through the derived schema. Pins the {@code -(len + 1)} length of boxed arrays
     * (GB-1) and the plain length of primitive arrays (GB-2).
     */
    public void testPathDMapDerived() throws IOException
    {
        verify("D_mapDerived", MapHolder.class, 0);
    }

    /**
     * Path D: array as a list element with {@link IdStrategy#COLLECTION_SCHEMA_ON_REPEATED_FIELDS}.
     */
    public void testPathDListDerived() throws IOException
    {
        verify("D_listDerived", ListHolder.class, IdStrategy.COLLECTION_SCHEMA_ON_REPEATED_FIELDS);
    }

    /**
     * GB-4, spelled out: {@code Integer[] {null, null, 1, null}} in an array-typed field (protobuf, nulls preserved).
     */
    public void testNullRunEncodingDecoded() throws IOException
    {
        final FieldHolder holder = new FieldHolder();
        holder.bInt = new Integer[] { null, null, Integer.valueOf(1), null };

        final byte[] data = Format.PROTOBUF.toByteArray(holder, schema(FieldHolder.class,
                configFlags(CFG_PRESERVE)));

        assertEquals(
                "4208" // field 8 (bInt), length-delimited, 8 bytes
                        + "0804" // ID_ARRAY_LEN = 4
                        + "1802" // ID_ARRAY_NULLCOUNT = 2
                        + "1001" // ID_ARRAY_DATA = 1
                        + "1801", // ID_ARRAY_NULLCOUNT = 1 (trailing null)
                ArrayFixtures.toHex(data));
    }

    /**
     * GB-1, spelled out: a boxed array through the derived schema writes its length as {@code -(len + 1)}.
     */
    public void testDerivedBoxedLengthDecoded() throws IOException
    {
        final MapHolder holder = new MapHolder();
        holder.bInt = new HashMap<String, Integer[]>();
        holder.bInt.put(ArrayFixtures.MAP_KEY, new Integer[] { Integer.valueOf(1), Integer.valueOf(2) });

        final byte[] data = Format.PROTOBUF.toByteArray(holder, schema(MapHolder.class,
                configFlags(CFG_PRESERVE)));

        assertEquals(
                "4216" // field 8 (bInt), length-delimited, 22 bytes
                        + "0a14" // map entry, 20 bytes
                        + "0a016b" // key "k"
                        + "120f" // value, 15 bytes
                        + "08fdffffffffffffffff01" // ID_ARRAY_LEN = -3, i.e. -(2 + 1)
                        + "1001" // ID_ARRAY_DATA = 1
                        + "1002", // ID_ARRAY_DATA = 2
                ArrayFixtures.toHex(data));
    }

    private void verify(String path, Class<?> holderType, int extraFlags) throws IOException
    {
        final List<Case> cases = buildCases(path, holderType, extraFlags);

        final TreeMap<String, String> actual = new TreeMap<String, String>();
        for (Case c : cases)
            actual.put(c.id, ArrayFixtures.toHex(c.format.toByteArray(c.holder, c.schema)));

        assertEquals("duplicate case ids", cases.size(), actual.size());

        final String fileName = path + ".hex";
        if (Boolean.getBoolean(REGEN_PROPERTY))
        {
            writeGolden(fileName, actual);
            return;
        }

        final Map<String, String> golden = readGolden(fileName);

        final TreeSet<String> missing = new TreeSet<String>(actual.keySet());
        missing.removeAll(golden.keySet());
        final TreeSet<String> extra = new TreeSet<String>(golden.keySet());
        extra.removeAll(actual.keySet());
        assertTrue("cases without golden bytes: " + missing, missing.isEmpty());
        assertTrue("golden bytes without cases: " + extra, extra.isEmpty());

        final ArrayList<String> failures = new ArrayList<String>();
        for (Case c : cases)
        {
            final String expectedHex = golden.get(c.id);

            // 1. the encoding must not change
            final String actualHex = actual.get(c.id);
            if (!expectedHex.equals(actualHex))
            {
                failures.add(c.id + ": bytes changed\n  golden: " + expectedHex + "\n  actual: " + actualHex);
                continue;
            }

            // 2. previously persisted data must stay readable
            final Object decoded = c.schema.newMessage();
            try
            {
                c.format.mergeFrom(ArrayFixtures.fromHex(expectedHex), decoded, c.schema);
            }
            catch (NullPointerException e)
            {
                // CHARACTERIZATION: possible bug, see enumArrayDecodeThrowsNpe
                if (!c.decodeThrowsNpe)
                    failures.add(c.id + ": golden bytes could not be decoded: " + e);
                continue;
            }
            catch (Exception e)
            {
                failures.add(c.id + ": golden bytes could not be decoded: " + e);
                continue;
            }

            if (c.decodeThrowsNpe)
            {
                failures.add(c.id + ": expected the current NullPointerException on decode (enum array as a "
                        + "generic element) but decoding succeeded; if this was fixed on purpose, update "
                        + "enumArrayDecodeThrowsNpe");
                continue;
            }

            final String diff = ArrayFixtures.diffHolders(c.holder, decoded);
            if (diff != null)
                failures.add(c.id + ": decoded value differs: " + diff);
        }

        if (!failures.isEmpty())
        {
            final StringBuilder sb = new StringBuilder();
            sb.append(failures.size()).append(" of ").append(cases.size()).append(" cases failed in ")
                    .append(fileName).append(':');
            for (String failure : failures)
                sb.append('\n').append(failure);
            fail(sb.toString());
        }
    }

    // ------------------------------------------------------------------
    // fixture files

    private static Map<String, String> readGolden(String fileName) throws IOException
    {
        InputStream in = ArraySchemasGoldenBytesTest.class.getClassLoader()
                .getResourceAsStream(RESOURCE_DIR + fileName);
        if (in == null)
        {
            final File fallback = new File(FALLBACK_DIR + fileName);
            if (!fallback.exists())
                fail("Golden file not found on the classpath nor at " + fallback.getAbsolutePath());

            in = new FileInputStream(fallback);
        }

        final LinkedHashMap<String, String> golden = new LinkedHashMap<String, String>();
        try
        {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                line = line.trim();
                if (line.length() == 0 || line.charAt(0) == '#')
                    continue;

                final int eq = line.indexOf('=');
                if (eq == -1)
                    fail("Malformed line in " + fileName + ": " + line);

                golden.put(line.substring(0, eq), line.substring(eq + 1));
            }
        }
        finally
        {
            in.close();
        }
        return golden;
    }

    private static void writeGolden(String fileName, Map<String, String> entries) throws IOException
    {
        // only allowed from the module that owns the fixtures
        final String module = new File(".").getCanonicalFile().getName();
        if (!"protostuff-runtime".equals(module))
            fail("Regenerate the golden files from protostuff-runtime, not " + module);

        final File dir = new File(SOURCE_DIR);
        if (!dir.exists() && !dir.mkdirs())
            fail("Could not create " + dir.getAbsolutePath());

        final Writer writer = new OutputStreamWriter(new FileOutputStream(new File(dir, fileName)), "UTF-8");
        try
        {
            writer.write("# Golden bytes for ArraySchemasGoldenBytesTest. Generated, do not edit by hand.\n");
            writer.write("# Regenerate with -D" + REGEN_PROPERTY + "=true (only for an intended wire change).\n");
            writer.write("# <path>.<kind>.<pattern>.<config>.<format>=<hex>\n");
            for (Map.Entry<String, String> entry : entries.entrySet())
            {
                writer.write(entry.getKey());
                writer.write('=');
                writer.write(entry.getValue());
                writer.write('\n');
            }
        }
        finally
        {
            writer.close();
        }
    }
}
