package io.protostuff.runtime;

import static io.protostuff.runtime.ArrayFixtures.KINDS;

import io.protostuff.GraphIOUtil;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.runtime.ArrayFixtures.FieldHolder;
import io.protostuff.runtime.ArrayFixtures.Kind;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Graph (reference tracking) tests for {@link ArraySchemas} (GR-1 .. GR-5): shared and cyclic references to arrays must
 * survive a {@link GraphIOUtil} round trip with their identity intact.
 * <p>
 * Every array schema class registers the array it creates via {@code GraphInput.updateLast}, so each kind is checked.
 */
public class ArraySchemasGraphTest extends TestCase
{

    private final DefaultIdStrategy strategy = ArrayFixtures.newStrategy(IdStrategy.DEFAULT_FLAGS
            | IdStrategy.PRESERVE_NULL_ELEMENTS);

    /**
     * The same array referenced from two {@code Object} fields (path B).
     */
    static final class SharedObjects
    {
        @Tag(1) Object a;
        @Tag(2) Object b;
    }

    /**
     * The same array referenced from the typed fields of two different holders (path A).
     */
    static final class SharedFields
    {
        @Tag(1) FieldHolder first;
        @Tag(2) FieldHolder second;
    }

    /**
     * The same array referenced from an {@code Object} field and from inside a list.
     */
    static final class FieldAndList
    {
        @Tag(1) Object field;
        @Tag(2) List<Object> list;
    }

    /**
     * A pojo whose array elements point back to their owner.
     */
    static final class Node
    {
        @Tag(1) String name;
        @Tag(2) Node[] children;
        @Tag(3) Node parent;
    }

    /**
     * Holds the owner of a {@link Node} tree, so that the owner is not the root message (back-references to the root
     * message itself are not restored by {@link GraphIOUtil}, independent of arrays).
     */
    static final class NodeBox
    {
        @Tag(1) Node node;
    }

    /**
     * An {@code Object} field.
     */
    static final class ObjectValue
    {
        @Tag(1) Object value;
    }

    @SuppressWarnings("unchecked")
    private <T> T roundTrip(T message)
    {
        final Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(message.getClass(), strategy);
        final byte[] data = GraphIOUtil.toByteArray(message, schema, ArrayFixtures.buf());
        final T parsed = schema.newMessage();
        GraphIOUtil.mergeFrom(data, parsed, schema);
        return parsed;
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
     * GR-1 / GR-5: one array instance in two {@code Object} fields, for every kind, plain and with nulls.
     */
    public void testSharedArrayInObjectFields()
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            for (int withNulls = 0; withNulls < (kind.primitive ? 1 : 2); withNulls++)
            {
                final String id = kind + (withNulls == 0 ? ".plain" : ".nulls");
                final Object array = withNulls == 0 ? kind.sample() : kind.sampleWithNulls();

                final SharedObjects message = new SharedObjects();
                message.a = array;
                message.b = array;

                final SharedObjects parsed = roundTrip(message);
                if (!ArrayFixtures.deepEquals(array, parsed.a))
                    failures.add(id + ": a = " + ArrayFixtures.deepToString(parsed.a));
                if (parsed.a != parsed.b)
                    failures.add(id + ": identity lost (a != b)");
            }
        }
        assertNoFailures(failures);
    }

    /**
     * GR-5: one array instance in the typed fields of two holders (path A), for every kind, plain and with nulls.
     */
    public void testSharedArrayInTypedFields() throws Exception
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            final Field f = FieldHolder.class.getDeclaredField(kind.name);
            f.setAccessible(true);

            for (int withNulls = 0; withNulls < (kind.primitive ? 1 : 2); withNulls++)
            {
                final String id = kind + (withNulls == 0 ? ".plain" : ".nulls");
                final Object array = withNulls == 0 ? kind.sample() : kind.sampleWithNulls();

                final SharedFields message = new SharedFields();
                message.first = (FieldHolder) ArrayFixtures.newHolder(FieldHolder.class, kind, array);
                message.second = (FieldHolder) ArrayFixtures.newHolder(FieldHolder.class, kind, array);

                final SharedFields parsed = roundTrip(message);
                final Object first = f.get(parsed.first), second = f.get(parsed.second);
                if (!ArrayFixtures.deepEquals(array, first))
                    failures.add(id + ": first = " + ArrayFixtures.deepToString(first));
                if (first != second)
                    failures.add(id + ": identity lost (first != second)");
            }
        }
        assertNoFailures(failures);
    }

    /**
     * GR-2: the same array in a field and inside a {@code List<Object>}, for every kind.
     */
    public void testSharedArrayInFieldAndList()
    {
        final ArrayList<String> failures = new ArrayList<String>();
        for (Kind kind : KINDS)
        {
            final Object array = kind.sample();

            final FieldAndList message = new FieldAndList();
            message.field = array;
            message.list = new ArrayList<Object>();
            message.list.add(array);

            final FieldAndList parsed = roundTrip(message);
            if (!ArrayFixtures.deepEquals(array, parsed.field))
                failures.add(kind + ": field = " + ArrayFixtures.deepToString(parsed.field));
            if (parsed.list == null || parsed.list.size() != 1)
                failures.add(kind + ": list = " + ArrayFixtures.deepToString(parsed.list));
            else if (parsed.field != parsed.list.get(0))
                failures.add(kind + ": identity lost (field != list[0])");
        }
        assertNoFailures(failures);
    }

    /**
     * GR-3: pojo array whose elements point back to the owner.
     */
    public void testPojoArrayElementsReferenceOwner()
    {
        final Node root = new Node();
        root.name = "root";
        final Node c1 = new Node(), c2 = new Node();
        c1.name = "c1";
        c1.parent = root;
        c2.name = "c2";
        c2.parent = root;
        root.children = new Node[] { c1, null, c2 };

        final NodeBox box = new NodeBox();
        box.node = root;

        final Node parsed = roundTrip(box).node;
        assertEquals("root", parsed.name);
        assertEquals(3, parsed.children.length);
        assertEquals("c1", parsed.children[0].name);
        assertNull(parsed.children[1]);
        assertEquals("c2", parsed.children[2].name);
        assertSame(parsed, parsed.children[0].parent);
        assertSame(parsed, parsed.children[2].parent);
    }

    /**
     * GR-4: an {@code Object[]} that contains itself.
     */
    public void testObjectArrayContainingItself()
    {
        final Object[] array = new Object[2];
        array[0] = array;
        array[1] = "x";

        final ObjectValue message = new ObjectValue();
        message.value = array;

        final ObjectValue parsed = roundTrip(message);
        assertTrue(parsed.value instanceof Object[]);
        final Object[] parsedArray = (Object[]) parsed.value;
        assertEquals(2, parsedArray.length);
        assertSame(parsedArray, parsedArray[0]);
        assertEquals("x", parsedArray[1]);
    }

    /**
     * Sanity check for the identity assertions above: two equal but distinct arrays stay distinct.
     */
    public void testDistinctArraysStayDistinct()
    {
        final SharedObjects message = new SharedObjects();
        message.a = new int[] { 1, 2 };
        message.b = new int[] { 1, 2 };

        final SharedObjects parsed = roundTrip(message);
        assertTrue(ArrayFixtures.deepEquals(parsed.a, parsed.b));
        assertNotSame(parsed.a, parsed.b);
    }
}
