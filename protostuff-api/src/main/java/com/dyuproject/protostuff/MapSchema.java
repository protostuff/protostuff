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

package com.dyuproject.protostuff;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A schema for a {@link Map} with single-typed object keys and single-typed object values.
 * Keys cannot be null otherwise the entry is ignored (not serialized).
 * Values however can be null.
 *
 * @author David Yu
 * @created Jun 26, 2010
 */
public abstract class MapSchema<K,V> implements Schema<Map<K,V>>
{
    
    protected abstract K readKeyFrom(Input input) throws IOException;
    
    protected abstract V readValueFrom(Input input) throws IOException;
    
    protected abstract void writeKeyTo(Output output, int fieldNumber, K value, 
            boolean repeated) throws IOException;
    
    protected abstract void writeValueTo(Output output, int fieldNumber, V value, 
            boolean repeated) throws IOException;

    
    public final String getFieldName(int number)
    {
        // alphanumeric to support xml
        // protobuf and json(numeric) does not call this method.
        return number % 2 == 0 ? "v" + number : "k" + number;
    }

    public final int getFieldNumber(String name)
    {
        // alphanumeric to support xml
        // protobuf and json(numeric) does not call this method.
        return Integer.parseInt(name.substring(1));
    }

    public final boolean isInitialized(Map<K, V> map)
    {
        return true;
    }

    public final String messageFullName()
    {
        return Map.class.getName();
    }

    public final String messageName()
    {
        return Map.class.getSimpleName();
    }
    
    public final Class<? super Map<K, V>> typeClass()
    {
        return Map.class;
    }
    
    public final Map<K, V> newMessage()
    {
        return new HashMap<K,V>();
    }
    
    public final void mergeFrom(Input input, Map<K, V> map) throws IOException
    {
        final int first = input.readFieldNumber(this);
        if(first == 0)
        {
            // empty map
            return;
        }
        if(first % 2 == 0)
        {
            // must start with a key (odd number)
            throw new ProtostuffException("The map was incorrectly serialized.");
        }
        
        while(true)
        {
            final K key = readKeyFrom(input);
            int next = input.readFieldNumber(this);
            if(next == 0)
            {
                // end of map
                // null value
                map.put(key, null);
                return;
            }
            
            if(next % 2 == 0)
            {
                // has value.
                map.put(key, readValueFrom(input));
                
                // move to next field number
                next = input.readFieldNumber(this);
                if(next == 0)
                {
                    // end of map
                    return;
                }
                if(next % 2 == 0)
                {
                    // next entry must start with a key (odd number)
                    throw new ProtostuffException("The map was incorrectly serialized.");
                }
            }
            else
            {
                // its the next entry's key (odd number) ... meaning null value for current entry
                map.put(key, null);
            }
        }
    }

    public final void writeTo(Output output, Map<K, V> map) throws IOException
    {
        int fieldNumber = 0;
        for(Entry<K, V> entry : map.entrySet())
        {
            final K key = entry.getKey();
            if(key == null)
                continue;
            
            writeKeyTo(output, ++fieldNumber, key, false);
            ++fieldNumber;
            
            final V value = entry.getValue();
            if(value == null)
                continue;
            
            writeValueTo(output, fieldNumber, value, false);
        }
    }


}
