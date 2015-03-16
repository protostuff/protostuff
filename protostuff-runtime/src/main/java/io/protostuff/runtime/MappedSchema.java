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

import java.util.Collection;
import java.util.List;

import io.protostuff.Pipe;
import io.protostuff.Schema;

/**
 * Base class for schemas that maps fields by number and name.
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class MappedSchema<T> implements Schema<T>,FieldMap<T>
{

	public static final int MIN_TAG_FOR_HASH_FIELD_MAP = 100;

	private final Pipe.Schema<T> pipeSchema;
	private final FieldMap<T> fieldMap;

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
		if (sparseFields(fields, lastFieldNumber))
		{
			fieldMap = new HashFieldMap<>(fields);
		} else {
			// array field map should be more efficient
			fieldMap = new ArrayFieldMap<>(fields, lastFieldNumber);
		}
        pipeSchema = new RuntimePipeSchema<>(this, fieldMap);
    }

	private boolean sparseFields(Collection<Field<T>> fields, int lastFieldNumber)
	{
		return lastFieldNumber > MIN_TAG_FOR_HASH_FIELD_MAP && lastFieldNumber >= 2*fields.size();
	}

	/**
     * Returns the pipe schema linked to this.
     */
    public Pipe.Schema<T> getPipeSchema()
    {
        return pipeSchema;
    }

	@Override
	public Field<T> getFieldByNumber(int n)
	{
		return fieldMap.getFieldByNumber(n);
	}

	@Override
	public Field<T> getFieldByName(String fieldName)
	{
		return fieldMap.getFieldByName(fieldName);
	}

	@Override
	public int getFieldCount()
	{
		return fieldMap.getFieldCount();
	}

	@Override
	public List<Field<T>> getFields()
	{
		return fieldMap.getFields();
	}
}
