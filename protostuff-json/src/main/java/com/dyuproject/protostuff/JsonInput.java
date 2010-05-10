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

package com.dyuproject.protostuff;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Schema;

/**
 * An input used for reading data with json format.
 *
 * @author David Yu
 * @created Nov 20, 2009
 */
public final class JsonInput implements Input
{
    
    private final JsonParser parser;
    private final boolean numeric;
    private boolean lastRepeated;
    private String lastName;
    private int lastNumber;
    
    public JsonInput(JsonParser parser)
    {
        this(parser, false);
    }
    
    public JsonInput(JsonParser parser, boolean numeric)
    {
        this.parser = parser;
        this.numeric = numeric;
    }
    
    /**
     * Returns whether the incoming messages' field names are numeric.
     */
    public boolean isNumeric()
    {
        return numeric;
    }
    
    /**
     * Gets the last field number read.
     */
    public int getLastNumber()
    {
        return lastNumber;
    }
    
    /**
     * Returns true if the last read field was a repeated field.
     */
    public boolean isLastRepeated()
    {
        return lastRepeated;
    }
    
    void reset()
    {
        lastRepeated = false;
        lastName = null;
        lastNumber = 0;
    }

    public <T> void handleUnknownField(int fieldNumber, Schema<T> schema) throws IOException
    {
        // we can skip this unknown field
        if(parser.getCurrentToken().isScalarValue())
            return;
        
        throw new IOException("Unknown field: " + lastName + " on message " + schema.typeClass());
    }    

    public <T> int readFieldNumber(Schema<T> schema) throws IOException
    {
        if(lastRepeated)
            return lastNumber;
        
        JsonToken jt = parser.nextToken();
        if(jt == JsonToken.END_OBJECT)
            return 0;
        
        if(jt != JsonToken.FIELD_NAME)
        {
            throw new IOException("Expected token: $field: but was " + 
                    jt + " on message " + schema.typeClass());
        }
        String name = lastName = parser.getCurrentName();
        int number = lastNumber = numeric ? Integer.parseInt(name) : schema.getFieldNumber(name);
        // move to the next token
        if(parser.nextToken() == JsonToken.START_ARRAY)
        {
            // if empty array, read the next field
            if(parser.nextToken() == JsonToken.END_ARRAY)
                return readFieldNumber(schema);
            
            lastRepeated = true;
        }
        
        if(number == 0)
        {
            // we can skip this unknown field
            if(parser.getCurrentToken().isScalarValue())
                return readFieldNumber(schema);
            
            throw new IOException("Unknown field: " + lastName + " on message " + schema.typeClass());
        }
        
        return number;
    }

    public boolean readBool() throws IOException
    {
        JsonToken jt = parser.getCurrentToken();
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        if(jt==JsonToken.VALUE_TRUE)
            return true;
        if(jt==JsonToken.VALUE_FALSE)
            return false;
        
        throw new IOException("Expected token: true/false but was " + jt);
    }

    public byte[] readByteArray() throws IOException
    {
        byte[] value = parser.getBinaryValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public ByteString readBytes() throws IOException
    {
        return ByteString.wrap(readByteArray());
    }

    public double readDouble() throws IOException
    {
        double value = parser.getDoubleValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public int readEnum() throws IOException
    {
        int value = parser.getIntValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public int readFixed32() throws IOException
    {
        int value = parser.getIntValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public long readFixed64() throws IOException
    {
        long value = parser.getLongValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public float readFloat() throws IOException
    {
        float value = parser.getFloatValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public int readInt32() throws IOException
    {
        int value = parser.getIntValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public long readInt64() throws IOException
    {
        long value = parser.getLongValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public int readSFixed32() throws IOException
    {
        int value = parser.getIntValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public long readSFixed64() throws IOException
    {
        long value = parser.getLongValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public int readSInt32() throws IOException
    {
        int value = parser.getIntValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public long readSInt64() throws IOException
    {
        long value = parser.getLongValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public String readString() throws IOException
    {
        if(parser.getCurrentToken() != JsonToken.VALUE_STRING)
            throw new IOException("Expected token: string but was " + parser.getCurrentToken());
        
        String value = parser.getText();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public int readUInt32() throws IOException
    {
        int value = parser.getIntValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public long readUInt64() throws IOException
    {
        long value = parser.getLongValue();
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            lastRepeated = false;
        
        return value;
    }

    public <T extends Message<T>> T mergeMessage(T message) throws IOException
    {
        return mergeObject(message, message.cachedSchema());
    }

    public <T> T mergeObject(T value, Schema<T> schema) throws IOException
    {
        if(parser.getCurrentToken() != JsonToken.START_OBJECT)
        {
            throw new IOException("Expected token: { but was " + 
                    parser.getCurrentToken() + " on " + lastName + " of message " + 
                    schema.typeClass());
        }
        
        int lastNumber = this.lastNumber;
        boolean lastRepeated = this.lastRepeated;
        String lastName = this.lastName;
        
        // reset
        this.lastRepeated = false;
        
        schema.mergeFrom(this, value);
        
        if(parser.getCurrentToken() != JsonToken.END_OBJECT)
        {
            throw new IOException("Expected token: } but was " + 
                    parser.getCurrentToken() + " on " + lastName + " of message " + 
                    schema.typeClass());
        }
        
        if(!schema.isInitialized(value))
            throw new UninitializedMessageException(value, schema);
        
        // restore state
        this.lastNumber = lastNumber;
        this.lastRepeated = lastRepeated;
        this.lastName = lastName;
        
        if(lastRepeated && parser.nextToken()==JsonToken.END_ARRAY)
            this.lastRepeated = false;
        
        return value;
    }

}
