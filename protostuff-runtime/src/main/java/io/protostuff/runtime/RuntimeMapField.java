//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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
import java.util.Map;

import io.protostuff.Input;
import io.protostuff.MapSchema;
import io.protostuff.MapSchema.MapWrapper;
import io.protostuff.MapSchema.MessageFactory;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;

/**
 * A runtime field for a {@link Map}. Allows null keys and values.
 * 
 * @author David Yu
 * @created Jan 21, 2011
 */
abstract class RuntimeMapField<T, K, V> extends Field<T>
{

    /**
     * Since we cannot inherit multiple classes, we create this Map schema simply to delegate to the wrapping class'
     * abstract methods.
     */
    protected final MapSchema<K, V> schema;

    public RuntimeMapField(FieldType type, int number, String name, Tag tag,
            MessageFactory messageFactory)
    {
        super(type, number, name, false, tag);
        schema = new MapSchema<K, V>(messageFactory)
        {
            @Override
            protected K readKeyFrom(Input input, MapWrapper<K, V> wrapper)
                    throws IOException
            {
                return kFrom(input, wrapper);
            }

            @Override
            protected void putValueFrom(Input input, MapWrapper<K, V> wrapper,
                    K key) throws IOException
            {
                vPutFrom(input, wrapper, key);
            }

            @Override
            protected void writeKeyTo(Output output, int fieldNumber, K key,
                    boolean repeated) throws IOException
            {
                kTo(output, fieldNumber, key, repeated);
            }

            @Override
            protected void writeValueTo(Output output, int fieldNumber, V val,
                    boolean repeated) throws IOException
            {
                vTo(output, fieldNumber, val, repeated);
            }

            @Override
            protected void transferKey(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                kTransfer(pipe, input, output, number, repeated);
            }

            @Override
            protected void transferValue(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                vTransfer(pipe, input, output, number, repeated);
            }
        };
    }

    protected abstract K kFrom(Input input, MapWrapper<K, V> wrapper)
            throws IOException;

    protected abstract void vPutFrom(Input input, MapWrapper<K, V> wrapper,
            K key) throws IOException;

    protected abstract void kTo(Output output, int fieldNumber, K key,
            boolean repeated) throws IOException;

    protected abstract void vTo(Output output, int fieldNumber, V value,
            boolean repeated) throws IOException;

    protected abstract void kTransfer(Pipe pipe, Input input, Output output,
            int number, boolean repeated) throws IOException;

    protected abstract void vTransfer(Pipe pipe, Input input, Output output,
            int number, boolean repeated) throws IOException;

}
