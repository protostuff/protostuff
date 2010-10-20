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

package com.dyuproject.protostuff.runtime;

import java.io.IOException;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;

/**
 * Determines how enums are serialized/deserialized. 
 * Default is BY_NUMBER. 
 * To enable BY_NAME, set the property "protostuff.runtime.enum.by_name=true".
 *
 * @author David Yu
 * @created Oct 20, 2010
 */
public abstract class EnumIO<E extends Enum<E>>
{
    
    /**
     * Returns true if serializing enums by name is activated.
     */
    public static final boolean ENUMS_BY_NAME = Boolean.getBoolean("protostuff.runtime.enums_by_name");
    
    @SuppressWarnings("unchecked")
    public static final EnumIO<? extends Enum<?>> create(Class<?> enumClass, 
            java.lang.reflect.Field f)
    {
        if(!enumClass.isEnum())
            throw new RuntimeException("Not an enum class: " + enumClass);
        
        return ENUMS_BY_NAME ? new ByName(enumClass, f) : new ByNumber(enumClass, f); 
    }
    
    
    protected final Class<E> enumClass;
    protected final java.lang.reflect.Field f;
    
    public EnumIO(Class<E> enumClass, java.lang.reflect.Field f)
    {
        this.enumClass = enumClass;
        this.f = f;
        if(f != null)
            f.setAccessible(true);
    }

    /**
     * Merge the value (inserted into the message) from the input.
     */
    public abstract void mergeFrom(Input input, Object message) throws IOException,
            IllegalAccessException, IllegalArgumentException;
    
    /**
     * Write the value (extracted from the message) to the output.
     */
    public abstract void writeTo(Output output, int fieldNumber, 
            boolean repeated, Object message) throws IOException,
            IllegalAccessException, IllegalArgumentException;
    
    /**
     * Write the enum to the output.
     */
    public abstract void writeTo(Output output, int fieldNumber, 
            boolean repeated, Enum<?> e) throws IOException;
    
    /**
     * Read the enum from the input.
     */
    public abstract E readFrom(Input input) throws IOException;
    
    /**
     * Transfer the enum.
     */
    public abstract void transfer(Pipe pipe, Input input, Output output, 
            int number, boolean repeated) throws IOException;
    
    /**
     * Writes the enum by its name.
     *
     */
    public static final class ByName<E extends Enum<E>> extends EnumIO<E>
    {
        public ByName(Class<E> enumClass, java.lang.reflect.Field f)
        {
            super(enumClass, f);
        }

        public void mergeFrom(Input input, Object message) throws IOException, 
            IllegalArgumentException, IllegalAccessException
        {
            f.set(message, Enum.valueOf(enumClass, input.readString()));
        }

        @SuppressWarnings("unchecked")
        public void writeTo(Output output, int fieldNumber, boolean repeated, Object message)
                throws IOException, IllegalArgumentException, IllegalAccessException
        {
            E e = (E)f.get(message);
            if(e != null)
                output.writeString(fieldNumber, e.name(), repeated);
        }
        
        public void writeTo(Output output, int fieldNumber, 
                boolean repeated, Enum<?> e) throws IOException
        {
            output.writeString(fieldNumber, e.name(), repeated);
        }   
        
        public E readFrom(Input input) throws IOException
        {
            return Enum.valueOf(enumClass, input.readString());
        }

        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            input.transferByteRangeTo(output, true, number, repeated);
        }
        
    }
    
    /**
     * Writes the enum by its number.
     *
     */
    public static final class ByNumber<E extends Enum<E>> extends EnumIO<E>
    {
        public ByNumber(Class<E> enumClass, java.lang.reflect.Field f)
        {
            super(enumClass, f);
        }

        public void mergeFrom(Input input, Object message) throws IOException, 
        IllegalArgumentException, IllegalAccessException
        {
            f.set(message, enumClass.getEnumConstants()[input.readEnum()]);
        }

        @SuppressWarnings("unchecked")
        public void writeTo(Output output, int fieldNumber, boolean repeated, Object message)
                throws IOException, IllegalArgumentException, IllegalAccessException
        {
            E e = (E)f.get(message);
            if(e != null)
                output.writeEnum(fieldNumber, e.ordinal(), repeated);
        }
        
        public void writeTo(Output output, int fieldNumber, 
                boolean repeated, Enum<?> e) throws IOException
        {
            output.writeEnum(fieldNumber, e.ordinal(), repeated);
        }   
        
        public E readFrom(Input input) throws IOException
        {
            return enumClass.getEnumConstants()[input.readEnum()];
        }

        public void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            output.writeEnum(number, input.readEnum(), repeated);
        }
    }

}
