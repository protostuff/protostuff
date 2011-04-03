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

package com.dyuproject.protostuff.me;

import java.io.IOException;
import java.util.Vector;

/**
 * A ByteArrayInput w/c can handle cyclic dependencies when deserializing 
 * objects with graph transformations.
 *
 * @author David Yu
 * @created Dec 10, 2010
 */
public final class GraphByteArrayInput extends FilterInput 
    implements GraphInput, Schema
{

    private final Vector references;
    private int lastRef = -1;
    
    private Schema lastSchema;
    private boolean messageReference = false;
    
    public GraphByteArrayInput(ByteArrayInput input)
    {
        super(input);
        
        // protostuff format only.
        //assert input.decodeNestedMessageAsGroup;
        
        references = new Vector();
    }

    public GraphByteArrayInput(ByteArrayInput input, int initialCapacity)
    {
        super(input);
        
        // protostuff format only.
        //assert input.decodeNestedMessageAsGroup;
        
        references = new Vector(initialCapacity);
    }
    
    public void updateLast(Object morphedMessage, Object lastMessage)
    {
        final int last = references.size()-1;
        if(lastMessage != null && lastMessage == references.elementAt(last))
        {
            // update the reference
            references.setElementAt(morphedMessage, last);
        }
    }
    
    public boolean isCurrentMessageReference()
    {
        return messageReference;
    }
    
    public int readFieldNumber(Schema schema) throws IOException
    {
        final int fieldNumber = input.readFieldNumber(schema);
        if(WireFormat.getTagWireType(((ByteArrayInput) input).getLastTag()) == WireFormat.WIRETYPE_REFERENCE)
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
    
//    @SuppressWarnings("unchecked")
    public Object mergeObject(Object value, Schema schema) throws IOException
    {
        if(messageReference)
        {
            // a reference.
            return references.elementAt(lastRef);
        }

        lastSchema = (Schema)schema;
        
        if(value == null)
            value = schema.newMessage();
        
        references.addElement(value);
        
        input.mergeObject(value, this);
        
        return value;
    }
    
    public String getFieldName(int number)
    {
        throw new UnsupportedOperationException();
    }

    public int getFieldNumber(String name)
    {
        throw new UnsupportedOperationException();
    }

    public boolean isInitialized(Object owner)
    {
        return true;
    }

    public String messageFullName()
    {
        throw new UnsupportedOperationException();
    }

    public String messageName()
    {
        throw new UnsupportedOperationException();
    }

    public Object newMessage()
    {
        throw new UnsupportedOperationException();
    }

    public Class typeClass()
    {
        throw new UnsupportedOperationException();
    }

    public void mergeFrom(Input input, final Object message) throws IOException
    {
        final Schema schema = lastSchema;
        
        // merge using this input.
        schema.mergeFrom(this, message);
        if(!schema.isInitialized(message))
            throw new UninitializedMessageException(message, schema);
        
        // restore
        lastSchema = schema;
    }

    public void writeTo(Output output, Object message) throws IOException
    {
        // only using mergeFrom.
        throw new UnsupportedOperationException();
    }

}
