package io.protostuff.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Field mapping implemented on top of java array for lookup by number.
 *
 * This is the most efficient implementation for almost all cases. But
 * it should not be used when field numbers are sparse and especially
 * when max field number is big - as this mapping internally uses array
 * of integers with size equal to max field number. In latter case
 * {@code HashFieldMap} should be used.
 *
 * @see io.protostuff.runtime.HashFieldMap
 *
 * @author Kostiantyn Shchepanovskyi
 */
final class ArrayFieldMap<T> implements FieldMap<T>
{
    private final List<Field<T>> fields;
    private final Field<T>[] fieldsByNumber;
    private final Map<String, Field<T>> fieldsByName;

    @SuppressWarnings("unchecked")
    public ArrayFieldMap(Collection<Field<T>> fields, int lastFieldNumber)
    {
        fieldsByName = new HashMap<String, Field<T>>();
        fieldsByNumber = (Field<T>[]) new Field<?>[lastFieldNumber + 1];
        for (Field<T> f : fields)
        {
            Field<T> last = this.fieldsByName.put(f.name, f);
            if (last != null)
            {
                throw new IllegalStateException(last + " and " + f
                        + " cannot have the same name.");
            }
            if (fieldsByNumber[f.number] != null)
            {
                throw new IllegalStateException(fieldsByNumber[f.number]
                        + " and " + f + " cannot have the same number.");
            }

            fieldsByNumber[f.number] = f;
        }

        List<Field<T>> fieldList = new ArrayList<Field<T>>(fields.size());
        for (Field<T> field : fieldsByNumber)
        {
            if (field != null)
                fieldList.add(field);
        }
        this.fields = Collections.unmodifiableList(fieldList);
    }

    @Override
    public Field<T> getFieldByNumber(int n)
    {
        return n < fieldsByNumber.length ? fieldsByNumber[n] : null;
    }

    @Override
    public Field<T> getFieldByName(String fieldName)
    {
        return fieldsByName.get(fieldName);
    }

    /**
     * Returns the message's total number of fields.
     */
    @Override
    public int getFieldCount()
    {
        return fields.size();
    }

    @Override
    public List<Field<T>> getFields()
    {
        return fields;
    }
}
