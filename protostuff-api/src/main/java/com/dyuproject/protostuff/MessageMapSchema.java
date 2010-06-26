//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff;

import java.io.IOException;
import java.util.Map;

/**
 * A utility schema for a {@link Map} with single-typed message/pojo keys and 
 * single-typed message/pojo values.
 * Keys cannot be null otherwise the entry is ignored (not serialized).
 * Values however can be null.
 *
 * @author David Yu
 * @created Jun 26, 2010
 */
public final class MessageMapSchema<K,V> extends MapSchema<K,V>
{
    
    final Schema<K> kSchema;
    final Schema<V> vSchema;
    
    public MessageMapSchema(Schema<K> kSchema, Schema<V> vSchema)
    {
        this.kSchema = kSchema;
        this.vSchema = vSchema;
    }

    protected K readKeyFrom(Input input) throws IOException
    {
        return input.mergeObject(kSchema.newMessage(), kSchema);
    }

    protected V readValueFrom(Input input) throws IOException
    {
        return input.mergeObject(vSchema.newMessage(), vSchema);
    }

    protected void writeKeyTo(Output output, int fieldNumber, K value, boolean repeated) 
    throws IOException
    {
        output.writeObject(fieldNumber, value, kSchema, repeated);
    }

    protected void writeValueTo(Output output, int fieldNumber, V value, boolean repeated) 
    throws IOException
    {
        output.writeObject(fieldNumber, value, vSchema, repeated);
    }

}
