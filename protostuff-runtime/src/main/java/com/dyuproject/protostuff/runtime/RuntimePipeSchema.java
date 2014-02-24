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

package com.dyuproject.protostuff.runtime;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

import java.io.IOException;

/**
 * Runtime pipe schema.
 *
 * @author David Yu
 * @created Nov 9, 2012
 */
public final class RuntimePipeSchema<T> extends Pipe.Schema<T> {

    final Field<T>[] fieldsByNumber;

    public RuntimePipeSchema(Schema<T> schema, Field<T>[] fieldsByNumber) {
        super(schema);

        this.fieldsByNumber = fieldsByNumber;
    }

    protected void transfer(Pipe pipe, Input input, Output output) throws IOException {
        for (int number = input.readFieldNumber(wrappedSchema); number != 0;
             number = input.readFieldNumber(wrappedSchema)) {
            final Field<T> field = number < fieldsByNumber.length ?
                    fieldsByNumber[number] : null;

            if (field == null)
                input.handleUnknownField(number, wrappedSchema);
            else
                field.transfer(pipe, input, output, field.repeated);
        }
    }
}