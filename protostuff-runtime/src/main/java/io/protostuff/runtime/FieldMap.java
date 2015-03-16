package io.protostuff.runtime;

import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface FieldMap<T>
{
    Field<T> getFieldByNumber(int n);

    Field<T> getFieldByName(String fieldName);

    int getFieldCount();

    List<Field<T>> getFields();
}
