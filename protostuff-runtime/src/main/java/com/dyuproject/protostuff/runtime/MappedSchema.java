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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.WireFormat.FieldType;

/**
 * Base class for schemas that maps fields by number and name.
 * For fast initialization, the last field number is provided in the constructor.
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public abstract class MappedSchema<T> implements Schema<T>
{
    
    protected final Class<T> typeClass;
    protected final Field<T>[] fields, fieldsByNumber;
    protected final Map<String,Field<T>> fieldsByName;
    protected final PipeSchema pipeSchema;
    
    @SuppressWarnings("unchecked")
    public MappedSchema(Class<T> typeClass, Field<T>[] fields, int lastFieldNumber)
    {
        if(fields.length==0)
            throw new IllegalStateException("At least one field is required.");
        
        this.typeClass = typeClass;
        this.fields = fields;
        fieldsByName = new HashMap<String,Field<T>>();
        fieldsByNumber = (Field<T>[])new Field<?>[lastFieldNumber + 1];
        for(Field<T> f : fields)
        {
            Field<T> last = this.fieldsByName.put(f.name, f);
            if(last!=null)
            {
                throw new IllegalStateException(last + " and " + f + 
                        " cannot have the same name.");
            }
            if(fieldsByNumber[f.number]!=null)
            {
                throw new IllegalStateException(fieldsByNumber[f.number] + " and " + f + 
                        " cannot have the same number.");
            }
            
            fieldsByNumber[f.number] = f;
            //f.owner = this;
        }
        pipeSchema = new PipeSchema();
    }
    
    @SuppressWarnings("unchecked")
    public MappedSchema(Class<T> typeClass, Collection<Field<T>> fields, 
            int lastFieldNumber)
    {
        if(fields.isEmpty())
            throw new IllegalStateException("At least one field is required.");
        
        this.typeClass = typeClass;
        fieldsByName = new HashMap<String,Field<T>>();
        fieldsByNumber = (Field<T>[])new Field<?>[lastFieldNumber + 1];
        for(Field<T> f : fields)
        {
            Field<T> last = this.fieldsByName.put(f.name, f);
            if(last!=null)
            {
                throw new IllegalStateException(last + " and " + f + 
                        " cannot have the same name.");
            }
            if(fieldsByNumber[f.number]!=null)
            {
                throw new IllegalStateException(fieldsByNumber[f.number] + " and " + f + 
                        " cannot have the same number.");
            }
            
            fieldsByNumber[f.number] = f;
            //f.owner = this;
        }
        
        this.fields = (Field<T>[])new Field<?>[fields.size()];
        for(int i=1, j=0; i<fieldsByNumber.length; i++)
        {
            if(fieldsByNumber[i]!=null)
                this.fields[j++] = fieldsByNumber[i];
        }
        pipeSchema = new PipeSchema();
    }
    
    @SuppressWarnings("unchecked")
    public MappedSchema(Class<T> typeClass, Map<String,Field<T>> fieldsByName, 
            int lastFieldNumber)
    {
        if(fieldsByName.isEmpty())
            throw new IllegalStateException("At least one field is required.");
        
        this.typeClass = typeClass;
        this.fieldsByName = fieldsByName;
        Collection<Field<T>> fields = fieldsByName.values();
        fieldsByNumber = (Field<T>[])new Field<?>[lastFieldNumber + 1];
        
        for(Field<T> f : fields)
        {
            if(fieldsByNumber[f.number]!=null)
            {
                throw new IllegalStateException(fieldsByNumber[f.number] + " and " + f + 
                        " cannot have the same number.");
            }
            
            fieldsByNumber[f.number] = f;
            //f.owner = this;
        }
        
        this.fields = (Field<T>[])new Field<?>[fields.size()];
        for(int i=1, j=0; i<fieldsByNumber.length; i++)
        {
            if(fieldsByNumber[i]!=null)
                this.fields[j++] = fieldsByNumber[i];
        }
        pipeSchema = new PipeSchema();
    }
    
    public Class<T> typeClass()
    {
        return typeClass;
    }
    
    public String messageName()
    {
        return typeClass.getSimpleName();
    }
    
    public String messageFullName()
    {
        return typeClass.getName();
    }
    
    public String getFieldName(int number)
    {
        return fieldsByNumber.length>number ? fieldsByNumber[number].name : null;
    }
    
    public int getFieldNumber(String name)
    {
        final Field<T> field = fieldsByName.get(name);
        return field == null ? 0 : field.number;
    }
    
    public final void mergeFrom(Input input, T message) throws IOException
    {
        for (int number = input.readFieldNumber(this); number != 0; 
                number = input.readFieldNumber(this))
        {
            final Field<T> field = number < fieldsByNumber.length ? 
                    fieldsByNumber[number] : null;

            if(field == null)
                input.handleUnknownField(number, this);
            else
                field.mergeFrom(input, message);
        }
    }
    
    public final void writeTo(Output output, T message) throws IOException
    {
        for(Field<T> f : fields)
            f.writeTo(output, message);
    }
    
    /**
     * Returns the pipe schema linked to this.
     */
    public Pipe.Schema<T> getPipeSchema()
    {
        return pipeSchema;
    }
    
    final class PipeSchema extends Pipe.Schema<T>
    {
        public PipeSchema()
        {
            super(MappedSchema.this);
        }

        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(MappedSchema.this); number != 0; 
                    number = input.readFieldNumber(MappedSchema.this))
            {
                final Field<T> field = number < fieldsByNumber.length ? 
                        fieldsByNumber[number] : null;
                
                if(field == null)
                    input.handleUnknownField(number, MappedSchema.this);
                else
                    field.transfer(pipe, input, output, field.repeated);
            }
        }
    }
    
    /**
     * Represents a field of a message/pojo.
     *
     */
    public static abstract class Field<T>
    {
        public final FieldType type;
        public final int number;
        public final String name;
        public final boolean repeated;
        
        public Field(FieldType type, int number, String name, boolean repeated)
        {
            this.type = type;
            this.number = number;
            this.name = name;
            this.repeated = repeated;
        }
        
        public Field(FieldType type, int number, String name)
        {
            this(type, number, name, false);
        }

        /**
         * Writes the value of a field to the {@code output}.
         */
        protected abstract void writeTo(Output output, T message) throws IOException;
        
        /**
         * Reads the field value into the {@code message}.
         */
        protected abstract void mergeFrom(Input input, T message) throws IOException;
        
        /**
         * Transfer the input field to the output field.
         */
        protected abstract void transfer(Pipe pipe, Input input, Output output, 
                boolean repeated) throws IOException;
    }

}
