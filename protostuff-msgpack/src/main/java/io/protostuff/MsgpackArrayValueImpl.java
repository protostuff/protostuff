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
import java.util.Iterator;
import java.util.List;

import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageTypeCastException;
import org.msgpack.value.ArrayValue;
import org.msgpack.value.ImmutableArrayValue;
import org.msgpack.value.ImmutableBinaryValue;
import org.msgpack.value.ImmutableBooleanValue;
import org.msgpack.value.ImmutableExtensionValue;
import org.msgpack.value.ImmutableFloatValue;
import org.msgpack.value.ImmutableIntegerValue;
import org.msgpack.value.ImmutableMapValue;
import org.msgpack.value.ImmutableNilValue;
import org.msgpack.value.ImmutableNumberValue;
import org.msgpack.value.ImmutableRawValue;
import org.msgpack.value.ImmutableStringValue;
import org.msgpack.value.ImmutableValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;
import org.msgpack.value.impl.ImmutableArrayValueImpl;
import org.msgpack.value.impl.ImmutableNilValueImpl;

/**
 * Simple implementation of the msgpack mutable array
 * 
 * @author Alex Shvid
 *
 */

public final class MsgpackArrayValueImpl implements ArrayValue
{

    private final List<Value> list = new ArrayList<Value>();

    public void reset()
    {
        list.clear();
    }

    public void add(Value value)
    {
        list.add(value);
    }

    @Override
    public ImmutableValue immutableValue()
    {
        return new ImmutableArrayValueImpl(list.toArray(new Value[list.size()]));
    }

    @Override
    public void writeTo(MessagePacker packer) throws IOException
    {
        int size = list.size();
        packer.packArrayHeader(size);
        for (int i = 0; i != size; i++)
        {
            list.get(i).writeTo(packer);
        }
    }

    @Override
    public String toJson()
    {
        int size = list.size();
        if (size == 0)
        {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(list.get(0).toJson());
        for (int i = 1; i != size; i++)
        {
            sb.append(",");
            sb.append(list.get(0).toJson());
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString()
    {
        int size = list.size();
        if (size == 0)
        {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        appendString(sb, list.get(0));
        for (int i = 1; i != size; i++)
        {
            sb.append(",");
            appendString(sb, list.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    private static void appendString(StringBuilder sb, Value value)
    {
        if (value.isRawValue())
        {
            sb.append(value.toJson());
        }
        else
        {
            sb.append(value.toString());
        }
    }

    @Override
    public int size()
    {
        return list.size();
    }

    @Override
    public Value get(int index)
    {
        return list.get(index);
    }

    @Override
    public Value getOrNilValue(int index)
    {
        if (index < list.size() && index >= 0)
        {
            return list.get(index);
        }
        return ImmutableNilValueImpl.get();
    }

    @Override
    public Iterator<Value> iterator()
    {
        return list.iterator();
    }

    @Override
    public List<Value> list()
    {
        return list;
    }

    @Override
    public ValueType getValueType()
    {
        return ValueType.ARRAY;
    }

    @Override
    public boolean isNilValue()
    {
        return getValueType().isNilType();
    }

    @Override
    public boolean isBooleanValue()
    {
        return getValueType().isBooleanType();
    }

    @Override
    public boolean isNumberValue()
    {
        return getValueType().isNumberType();
    }

    @Override
    public boolean isIntegerValue()
    {
        return getValueType().isIntegerType();
    }

    @Override
    public boolean isFloatValue()
    {
        return getValueType().isFloatType();
    }

    @Override
    public boolean isRawValue()
    {
        return getValueType().isRawType();
    }

    @Override
    public boolean isBinaryValue()
    {
        return getValueType().isBinaryType();
    }

    @Override
    public boolean isStringValue()
    {
        return getValueType().isStringType();
    }

    @Override
    public boolean isArrayValue()
    {
        return getValueType().isArrayType();
    }

    @Override
    public boolean isMapValue()
    {
        return getValueType().isMapType();
    }

    @Override
    public boolean isExtensionValue()
    {
        return getValueType().isExtensionType();
    }

    @Override
    public ImmutableNilValue asNilValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableBooleanValue asBooleanValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableNumberValue asNumberValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableIntegerValue asIntegerValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableFloatValue asFloatValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableRawValue asRawValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableBinaryValue asBinaryValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableStringValue asStringValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableArrayValue asArrayValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableMapValue asMapValue()
    {
        throw new MessageTypeCastException();
    }

    @Override
    public ImmutableExtensionValue asExtensionValue()
    {
        throw new MessageTypeCastException();
    }

}
