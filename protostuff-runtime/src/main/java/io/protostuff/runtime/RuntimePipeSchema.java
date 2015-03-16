//========================================================================
//Copyright 2012 David Yu
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

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;

/**
 * Runtime pipe schema.
 * 
 * @author David Yu
 * @created Nov 9, 2012
 */
public final class RuntimePipeSchema<T> extends Pipe.Schema<T>
{

    final FieldMap<T> fieldsMap;

    public RuntimePipeSchema(Schema<T> schema, FieldMap<T> fieldMap)
    {
        super(schema);

        this.fieldsMap = fieldMap;
    }

    @Override
    protected void transfer(Pipe pipe, Input input, Output output)
            throws IOException
    {
        for (int number = input.readFieldNumber(wrappedSchema); number != 0; number = input
                .readFieldNumber(wrappedSchema))
        {
            final Field<T> field = fieldsMap.getFieldByNumber(number);
            if (field == null)
                input.handleUnknownField(number, wrappedSchema);
            else
                field.transfer(pipe, input, output, field.repeated);
        }
    }
}