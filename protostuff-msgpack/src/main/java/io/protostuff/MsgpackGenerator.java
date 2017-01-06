//========================================================================
//Copyright (C) 2016 Alex Shvid
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

package io.protostuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.core.MessagePacker;
import org.msgpack.value.ImmutableMapValue;
import org.msgpack.value.Value;
import org.msgpack.value.impl.ImmutableLongValueImpl;
import org.msgpack.value.impl.ImmutableMapValueImpl;
import org.msgpack.value.impl.ImmutableStringValueImpl;

/**
 * Single object generator for msgpack.
 * 
 * @author Alex Shvid
 */

public final class MsgpackGenerator
{
    private final List<Value> kvs = new ArrayList<Value>();
    private final boolean numeric;

    private boolean lastRepeated;
    private int lastNumber;

    public MsgpackGenerator(boolean numeric)
    {
        this.numeric = numeric;
    }

    public boolean isNumeric()
    {
        return numeric;
    }

    public boolean isLastRepeated()
    {
        return lastRepeated;
    }

    public int getLastNumber()
    {
        return lastNumber;
    }

    public MsgpackGenerator reset()
    {
        kvs.clear();
        lastRepeated = false;
        lastNumber = 0;
        return this;
    }

    private MsgpackArrayValueImpl getLastArray()
    {
        return (MsgpackArrayValueImpl) kvs.get(kvs.size() - 1);
    }

    public void pushValue(Schema<?> schema, int fieldNumber, Value value, boolean repeated)
    {

        // System.out.println("pushValue: schema=" + schema + ", fieldNumber=" + fieldNumber + ", value=" + value + ",
        // repeated=" + repeated);

        if (lastNumber == fieldNumber && lastRepeated)
        {
            // repeated field
            getLastArray().add(value);
            return;
        }

        if (numeric)
        {
            kvs.add(new ImmutableLongValueImpl(fieldNumber));
        }
        else
        {
            kvs.add(new ImmutableStringValueImpl(schema.getFieldName(fieldNumber)));
        }

        if (repeated)
        {
            MsgpackArrayValueImpl array = new MsgpackArrayValueImpl();
            array.add(value);

            kvs.add(array);
        }
        else
        {
            kvs.add(value);
        }

        lastNumber = fieldNumber;
        lastRepeated = repeated;

    }

    public void writeTo(MessagePacker packer) throws IOException
    {
        toValue().writeTo(packer);
    }

    public ImmutableMapValue toValue()
    {
        return new ImmutableMapValueImpl(kvs.toArray(new Value[kvs.size()]));
    }

}
