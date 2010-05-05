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

import org.codehaus.jackson.JsonGenerator;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

/**
 * An output used for writing data with json format.
 *
 * @author David Yu
 * @created Nov 20, 2009
 */
public final class JsonOutput implements Output
{
    
    private final JsonGenerator generator;
    private Schema<?> schema;
    private final boolean numeric;
    private boolean lastRepeated;
    private int lastNumber;
    
    public JsonOutput(JsonGenerator generator)
    {
        this(generator, false);
    }
    
    public JsonOutput(JsonGenerator generator, boolean numeric)
    {
        this.generator = generator;
        this.numeric = numeric;
    }
    
    /**
     * Before serializing a message/object tied to a schema, this should be called.
     */
    public JsonOutput use(Schema<?> schema)
    {
        this.schema = schema;
        return this;
    }
    
    /**
     * Returns whether the incoming messages' field names are numeric.
     */
    public boolean isNumeric()
    {
        return numeric;
    }
    
    /**
     * Gets the last field number written.
     */
    public int getLastNumber()
    {
        return lastNumber;
    }
    
    /**
     * Returns true if the last written field was a repeated field.
     */
    public boolean isLastRepeated()
    {
        return lastRepeated;
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException
    {
        if(lastNumber != fieldNumber)
        {
            JsonGenerator generator = this.generator;
            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                schema.getFieldName(fieldNumber);
            
            if(repeated)
            {
                generator.writeArrayFieldStart(name);
                generator.writeBoolean(value);
            }
            else
                generator.writeBooleanField(name, value);
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
            generator.writeBoolean(value);
    }

    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException
    {
        if(lastNumber != fieldNumber)
        {
            JsonGenerator generator = this.generator;
            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                schema.getFieldName(fieldNumber);
            
            if(repeated)
            {
                generator.writeArrayFieldStart(name);
                generator.writeBinary(value);
            }
            else
            {
                generator.writeFieldName(name);
                generator.writeBinary(value);
            }
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
            generator.writeBinary(value);
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException
    {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException
    {
        if(lastNumber != fieldNumber)
        {
            JsonGenerator generator = this.generator;
            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                schema.getFieldName(fieldNumber);
            
            if(repeated)
            {
                generator.writeArrayFieldStart(name);
                generator.writeNumber(value);
            }
            else
                generator.writeNumberField(name, value);
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
            generator.writeNumber(value);
    }

    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException
    {
        if(lastNumber != fieldNumber)
        {            
            JsonGenerator generator = this.generator;
            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                schema.getFieldName(fieldNumber);
            
            if(repeated)
            {
                generator.writeArrayFieldStart(name);
                generator.writeNumber(value);
            }
            else
                generator.writeNumberField(name, value);
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
            generator.writeNumber(value);
    }

    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        if(lastNumber != fieldNumber)
        {
            JsonGenerator generator = this.generator;
            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                schema.getFieldName(fieldNumber);
            
            if(repeated)
            {
                generator.writeArrayFieldStart(name);
                generator.writeNumber(value);
            }
            else
                generator.writeNumberField(name, value);
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
            generator.writeNumber(value);
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        if(lastNumber != fieldNumber)
        {
            JsonGenerator generator = this.generator;
            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                schema.getFieldName(fieldNumber);
            
            if(repeated)
            {
                generator.writeArrayFieldStart(name);
                generator.writeNumber(value);
            }
            else
                generator.writeNumberField(name, value);
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
            generator.writeNumber(value);
    }

    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException
    {
        if(lastNumber != fieldNumber)
        {
            JsonGenerator generator = this.generator;
            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                schema.getFieldName(fieldNumber);
            
            if(repeated)
            {
                generator.writeArrayFieldStart(name);
                generator.writeString(value);
            }
            else
                generator.writeStringField(name, value);
            
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
            generator.writeString(value);
    }

    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException
    {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException
    {
        writeInt64(fieldNumber, value, repeated);
    }

    public <T extends Message<T>> void writeMessage(int fieldNumber, T value, boolean repeated) 
    throws IOException
    {
        writeObject(fieldNumber, value, value.cachedSchema(), repeated);
    }

    public <T> void writeObject(int fieldNumber, T value, Schema<T> schema, boolean repeated) 
    throws IOException
    {
        JsonGenerator generator = this.generator;
        Schema<?> lastSchema = this.schema;
        
        if(lastNumber != fieldNumber)
        {            
            if(lastRepeated)
                generator.writeEndArray();
            
            String name = numeric ? String.valueOf(fieldNumber) : 
                lastSchema.getFieldName(fieldNumber);
            if(repeated)
                generator.writeArrayFieldStart(name);
            else
                generator.writeFieldName(name);
            
            // reset
            this.schema = schema;
            lastNumber = 0;
            lastRepeated = false;
            // recursive write
            generator.writeStartObject();
            schema.writeTo(this, value);
            if(lastRepeated)
                generator.writeEndArray();
            generator.writeEndObject();
            // restore state
            lastNumber = fieldNumber;
            lastRepeated = repeated;
        }
        else
        {
            int lastNumber = this.lastNumber;
            boolean lastRepeated = this.lastRepeated;
            
            // reset
            this.schema = schema;
            this.lastNumber = 0;
            this.lastRepeated = false;
            // recursive write
            generator.writeStartObject();
            schema.writeTo(this, value);
            generator.writeEndObject();
            // restore state
            this.lastNumber = lastNumber;
            this.lastRepeated = lastRepeated;
        }
        
        // restore state
        this.schema = lastSchema;
    }

}
