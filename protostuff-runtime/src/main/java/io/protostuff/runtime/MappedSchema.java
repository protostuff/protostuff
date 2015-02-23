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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;

/**
 * Base class for schemas that maps fields by number and name. For fast initialization, the last field number is
 * provided in the constructor.
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class MappedSchema<T> implements Schema<T>
{

    public static final Comparator<Field<?>> FIELD_BY_NUMBER_COMPARATOR = new Comparator<Field<?>>()
    {
        @Override
        public int compare(Field<?> o1, Field<?> o2)
        {
            return Integer.compare(o1.number, o2.number);
        }
    };

    protected final Class<T> typeClass;

    // Array of fields ordered by tag number
    protected final Field<T>[] fields;
    protected final TIntObjectMap<Field<T>> fieldsByNumber;
    protected final Map<String, Field<T>> fieldsByName;
    protected final Pipe.Schema<T> pipeSchema;

    @SuppressWarnings("unchecked")
    public MappedSchema(Class<T> typeClass, Collection<Field<T>> fields)
    {
        this.typeClass = typeClass;
        fieldsByName = new HashMap<>();
        fieldsByNumber = new TIntObjectHashMap<>(fields.size(), 0.5f);
        this.fields = (Field<T>[]) new Field<?>[fields.size()];
        int i = 0;
        for (Field<T> f : fields)
        {
            this.fields[i++] = f;
            registerFieldByName(f);
            registerFieldByNumber(f);
        }
        Arrays.sort(this.fields, FIELD_BY_NUMBER_COMPARATOR);
        pipeSchema = new RuntimePipeSchema<>(this, fieldsByNumber);
    }

    private void registerFieldByNumber(Field<T> f)
    {
        Field<T> last = fieldsByNumber.put(f.number, f);
        if (last != null)
        {
            throw new IllegalStateException(last + " and " + f
                    + " cannot have the same number");
        }
    }

    private void registerFieldByName(Field<T> f)
    {
        Field<T> last = fieldsByName.put(f.name, f);
        if (last != null)
        {
            throw new IllegalStateException(last + " and " + f
                    + " cannot have the same name");
        }
    }

    /**
     * Returns the message's total number of fields.
     */
    public int getFieldCount()
    {
        return fields.length;
    }

    @Override
    public Class<T> typeClass()
    {
        return typeClass;
    }

    @Override
    public String messageName()
    {
        return typeClass.getSimpleName();
    }

    @Override
    public String messageFullName()
    {
        return typeClass.getName();
    }

    @Override
    public String getFieldName(int number)
    {
        // only called on writes
        final Field<T> field = fieldsByNumber.get(number);
        return field == null ? null : field.name;
    }

    @Override
    public int getFieldNumber(String name)
    {
        final Field<T> field = fieldsByName.get(name);
        return field == null ? 0 : field.number;
    }

    @Override
    public final void mergeFrom(Input input, T message) throws IOException
    {
        for (int number = input.readFieldNumber(this); number != 0; number = input
                .readFieldNumber(this))
        {
            final Field<T> field = fieldsByNumber.get(number);

            if (field == null)
                input.handleUnknownField(number, this);
            else
                field.mergeFrom(input, message);
        }
    }

    @Override
    public final void writeTo(Output output, T message) throws IOException
    {
        for (Field<T> f : fields)
            f.writeTo(output, message);
    }

    /**
     * Returns the pipe schema linked to this.
     */
    public Pipe.Schema<T> getPipeSchema()
    {
        return pipeSchema;
    }

    /**
     * Represents a field of a message/pojo.
     */
    public static abstract class Field<T>
    {
        public final FieldType type;
        public final int number;
        public final String name;
        public final boolean repeated;
        public final int groupFilter;

        // public final Tag tag;

        public Field(FieldType type, int number, String name, boolean repeated,
                Tag tag)
        {
            this.type = type;
            this.number = number;
            this.name = name;
            this.repeated = repeated;
            this.groupFilter = tag == null ? 0 : tag.groupFilter();
            // this.tag = tag;
        }

        public Field(FieldType type, int number, String name, Tag tag)
        {
            this(type, number, name, false, tag);
        }

        /**
         * Writes the value of a field to the {@code output}.
         */
        protected abstract void writeTo(Output output, T message)
                throws IOException;

        /**
         * Reads the field value into the {@code message}.
         */
        protected abstract void mergeFrom(Input input, T message)
                throws IOException;

        /**
         * Transfer the input field to the output field.
         */
        protected abstract void transfer(Pipe pipe, Input input, Output output,
                boolean repeated) throws IOException;
    }

}
