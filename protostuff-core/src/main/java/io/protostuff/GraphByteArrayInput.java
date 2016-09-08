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

package io.protostuff;

import static io.protostuff.WireFormat.WIRETYPE_REFERENCE;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A ByteArrayInput w/c can handle cyclic dependencies when deserializing objects with graph transformations.
 * 
 * @author David Yu
 * @created Dec 10, 2010
 */
public final class GraphByteArrayInput extends FilterInput<ByteArrayInput>
        implements GraphInput, Schema<Object>
{

    private final ArrayList<Object> references;
    private int lastRef = -1;

    private Schema<Object> lastSchema;
    private boolean messageReference = false;

    public GraphByteArrayInput(ByteArrayInput input)
    {
        super(input);

        // protostuff format only.
        assert input.decodeNestedMessageAsGroup;

        references = new ArrayList<Object>();
    }

    public GraphByteArrayInput(ByteArrayInput input, int initialCapacity)
    {
        super(input);

        // protostuff format only.
        assert input.decodeNestedMessageAsGroup;

        references = new ArrayList<Object>(initialCapacity);
    }

    @Override
    public void updateLast(Object morphedMessage, Object lastMessage)
    {
        final int last = references.size() - 1;
        if (lastMessage != null && lastMessage == references.get(last))
        {
            // update the reference
            references.set(last, morphedMessage);
        }
    }

    @Override
    public boolean isCurrentMessageReference()
    {
        return messageReference;
    }

    @Override
    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        final int fieldNumber = input.readFieldNumber(schema);
        if (WireFormat.getTagWireType(input.getLastTag()) == WIRETYPE_REFERENCE)
        {
            // a reference.
            lastRef = input.readUInt32();
            messageReference = true;
        }
        else
        {
            // always unset.
            messageReference = false;
        }

        return fieldNumber;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {
        if (messageReference)
        {
            // a reference.
            return (T) references.get(lastRef);
        }

        lastSchema = (Schema<Object>) schema;

        if (value == null)
            value = schema.newMessage();

        references.add(value);

        input.mergeObject(value, this);

        return value;
    }
    
    @Override
    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        if (!messageReference)
            input.skipField(input.getLastTag());
    }

    @Override
    public String getFieldName(int number)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFieldNumber(String name)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInitialized(Object owner)
    {
        return true;
    }

    @Override
    public String messageFullName()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String messageName()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object newMessage()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<? super Object> typeClass()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeFrom(Input input, final Object message) throws IOException
    {
        final Schema<Object> schema = lastSchema;

        // merge using this input.
        schema.mergeFrom(this, message);
        if (!schema.isInitialized(message))
            throw new UninitializedMessageException(message, schema);

        // restore
        lastSchema = schema;
    }

    @Override
    public void writeTo(Output output, Object message) throws IOException
    {
        // only using mergeFrom.
        throw new UnsupportedOperationException();
    }

}
