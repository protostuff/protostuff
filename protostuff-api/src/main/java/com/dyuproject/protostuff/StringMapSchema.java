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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A utility schema for a {@link Map} with {@link String} keys and values.
 * Keys cannot be null otherwise the entry is ignored (not serialized).
 * Values however can be null.
 *
 * @author David Yu
 * @created Jun 25, 2010
 */
public final class StringMapSchema implements Schema<Map<String,String>>
{
    
    private static final Map<String,String> EMPTY_MAP = Collections.emptyMap();
    
    /**
     * The single instance.
     */
    public static final StringMapSchema INSTANCE = new StringMapSchema();
    
    /**
     * Gets the single instance.
     */
    public static StringMapSchema getInstance()
    {
        return INSTANCE;
    }
    
    private StringMapSchema(){}

    public String getFieldName(int number)
    {
        // alphanumeric to support xml
        // protobuf and json(numeric) does not call this method.
        return number % 2 == 0 ? "v" + number : "k" + number;
    }

    public int getFieldNumber(String name)
    {
        // alphanumeric to support xml
        // protobuf and json(numeric) does not call this method.
        return Integer.parseInt(name.substring(1));
    }

    public boolean isInitialized(Map<String, String> message)
    {
        return true;
    }

    public String messageFullName()
    {
        return Map.class.getName();
    }

    public String messageName()
    {
        return Map.class.getSimpleName();
    }

    public Map<String, String> newMessage()
    {
        return new HashMap<String,String>();
    }

    @SuppressWarnings("unchecked")
    public Class<Map<String, String>> typeClass()
    {
        return (Class<Map<String, String>>)EMPTY_MAP.getClass();
    }
    
    public void mergeFrom(Input input, Map<String, String> message) throws IOException
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
            throw new ProtostuffException("The string map was incorrectly serialized.");
        }
        
        while(true)
        {
            final String key = input.readString();
            int next = input.readFieldNumber(this);
            if(next == 0)
            {
                // end of map
                // null value
                message.put(key, null);
                return;
            }
            
            if(next % 2 == 0)
            {
                // has value.
                message.put(key, input.readString());
                
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
                    throw new ProtostuffException("The string map was incorrectly serialized.");
                }
            }
            else
            {
                // its the next entry's key (odd number) ... meaning null value for current entry
                message.put(key, null);
            }
        }
    }

    public void writeTo(Output output, Map<String, String> message) throws IOException
    {
        int fieldNumber = 0;
        for(Entry<String, String> entry : message.entrySet())
        {
            final String key = entry.getKey();
            if(key == null)
                continue;
            output.writeString(++fieldNumber, key, false);
            
            final String value = entry.getValue();
            ++fieldNumber;
            if(value == null)
                continue;
            output.writeString(fieldNumber, value, false);
        }
        
    }

}
