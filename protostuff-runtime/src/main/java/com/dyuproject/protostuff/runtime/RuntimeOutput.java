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

package com.dyuproject.protostuff.runtime;

import java.io.IOException;

import com.dyuproject.protostuff.FilterOutput;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

/**
 * Allows pojos without schema to be written at runtime.
 *
 * @author David Yu
 * @created Nov 12, 2009
 */
public class RuntimeOutput extends FilterOutput
{

    public RuntimeOutput(Output output)
    {
        super(output);
    }
    
    /**
     * Writes a pojo(plain old java object) - the schema is obtained at runtime.
     */
    public <T> void writePojo(int fieldNumber, T value, Class<T> typeClass) throws IOException
    {
        Schema<T> schema = RuntimeSchema.getSchema(typeClass);
        writeObject(fieldNumber, value, schema);
    }

}
