//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.protostuff.Pipe;
import io.protostuff.Schema;

/**
 * Base class for schemas that maps fields by number and name. For fast initialization, the last field number is
 * provided in the constructor.
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class MappedSchema<T> implements Schema<T>
{

    private final Field<T>[] fields;
    private final List<Field<T>> fieldList;
    private final Field<T>[] fieldsByNumber;
    private final Map<String, Field<T>> fieldsByName;
    private final Pipe.Schema<T> pipeSchema;

    @SuppressWarnings("unchecked")
    public MappedSchema(Collection<Field<T>> fields)
    {
        int lastFieldNumber = 0;
        for (Field<T> field : fields)
        {
            if (field.number > lastFieldNumber)
            {
                lastFieldNumber = field.number;
            }
        }

        fieldsByName = new HashMap<>();
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

        this.fields = (Field<T>[]) new Field<?>[fields.size()];
        for (int i = 1, j = 0; i < fieldsByNumber.length; i++)
        {
            if (fieldsByNumber[i] != null)
                this.fields[j++] = fieldsByNumber[i];
        }
        List<Field<T>> fieldList = new ArrayList<>(fields.size());
        Collections.addAll(fieldList, this.fields);
        this.fieldList = Collections.unmodifiableList(fieldList);
        pipeSchema = new RuntimePipeSchema<>(this, fieldsByNumber);
    }

    public Field<T> getFieldByNumber(int n)
    {
        return n < fieldsByNumber.length ? fieldsByNumber[n] : null;
    }

    public Field<T> getFieldByName(String fieldName)
    {
        return fieldsByName.get(fieldName);
    }

    /**
     * Returns the message's total number of fields.
     */
    public int getFieldCount()
    {
        return fields.length;
    }

    /**
     * Returns the pipe schema linked to this.
     */
    public Pipe.Schema<T> getPipeSchema()
    {
        return pipeSchema;
    }

    public List<Field<T>> getFields()
    {
        return fieldList;
    }

}
