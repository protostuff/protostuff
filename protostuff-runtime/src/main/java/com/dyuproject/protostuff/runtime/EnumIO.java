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
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;

/**
 * Determines how enums are serialized/deserialized. 
 * Default is BY_NUMBER. 
 * To enable BY_NAME, set the property "protostuff.runtime.enums_by_name=true".
 *
 * @author David Yu
 * @created Oct 20, 2010
 */
public abstract class EnumIO<E extends Enum<E>>
{
    
    /**
     * Returns true if serializing enums by name is activated.
     */
    public static final boolean ENUMS_BY_NAME = 
        Boolean.getBoolean("protostuff.runtime.enums_by_name");
    
    /**
     * A cache to prevent creating the same eio over and over.
     */
    private static final ConcurrentHashMap<String,EnumIO<?>> __eioCache = 
        new ConcurrentHashMap<String,EnumIO<?>>();
    
    @SuppressWarnings("unchecked")
    static EnumIO<? extends Enum<?>> get(Class<?> enumClass)
    {
        EnumIO<?> eio = __eioCache.get(enumClass.getName());
        if(eio == null)
        {
            eio = ENUMS_BY_NAME ? new ByName(enumClass) : new ByNumber(enumClass);
            
            final EnumIO<?> existing = __eioCache.putIfAbsent(enumClass.getName(), eio);
            if(existing != null)
                eio = existing;
        }
        
        return eio;
    }
    
    @SuppressWarnings("unchecked")
    static EnumIO<? extends Enum<?>> get(String className, boolean load)
    {
        EnumIO<?> eio = __eioCache.get(className);
        if(eio == null)
        {
            if(!load)
                return null;
            
            final Class<?> enumClass;
            try
            {
                enumClass = Thread.currentThread().getContextClassLoader().loadClass(
                        className);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            
            eio = ENUMS_BY_NAME ? new ByName(enumClass) : new ByNumber(enumClass);
            
            final EnumIO<?> existing = __eioCache.putIfAbsent(enumClass.getName(), eio);
            if(existing != null)
                eio = existing;
        }
        
        return eio;
    }
    
    /**
     * Writes the {@link Enum} to the output.
     */
    public static void writeTo(Output output, int number, boolean repeated, Enum<?> e) 
    throws IOException
    {
        if(ENUMS_BY_NAME)
            output.writeString(number, e.name(), repeated);
        else
            output.writeEnum(number, e.ordinal(), repeated);
    }
    
    /**
     * Transfers the {@link Enum} from the input to the output.
     */
    public static void transfer(Pipe pipe, Input input, Output output, int number, 
            boolean repeated) throws IOException
    {
        if(ENUMS_BY_NAME)
            input.transferByteRangeTo(output, true, number, repeated);
        else
            output.writeEnum(number, input.readEnum(), repeated);
    }
    
    
    protected final Class<E> enumClass;
    
    public EnumIO(Class<E> enumClass)
    {
        this.enumClass = enumClass;
    }
    
    /**
     * Read the enum from the input.
     */
    public abstract E readFrom(Input input) throws IOException;
    
    /**
     * Reads the enum by its name.
     *
     */
    public static final class ByName<E extends Enum<E>> extends EnumIO<E>
    {
        public ByName(Class<E> enumClass)
        {
            super(enumClass);
        }
        
        public E readFrom(Input input) throws IOException
        {
            return Enum.valueOf(enumClass, input.readString());
        }
    }
    
    /**
     * Reads the enum by its number.
     *
     */
    public static final class ByNumber<E extends Enum<E>> extends EnumIO<E>
    {
        public ByNumber(Class<E> enumClass)
        {
            super(enumClass);
        } 
        
        public E readFrom(Input input) throws IOException
        {
            return enumClass.getEnumConstants()[input.readEnum()];
        }
    }

}
