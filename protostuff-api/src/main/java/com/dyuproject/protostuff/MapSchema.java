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
    
    /**
     * The field name of the Map.Entry.
     */
    public static final String FIELD_NAME_ENTRY = "e";
    /**
     * The field name of the key.
     */
    public static final String FIELD_NAME_KEY = "k";
    /**
     * The field name of the value;
     */
    public static final String FIELD_NAME_VALUE = "v";
    
    protected abstract K readKeyFrom(Input input) throws IOException;
    
    protected abstract V readValueFrom(Input input) throws IOException;
    
    protected abstract void writeKeyTo(Output output, int fieldNumber, K value, 
            boolean repeated) throws IOException;
    
    protected abstract void writeValueTo(Output output, int fieldNumber, V value, 
            boolean repeated) throws IOException;

    
    public final String getFieldName(int number)
    {
        return number == 1 ? FIELD_NAME_ENTRY : null;
    }

    public final int getFieldNumber(String name)
    {
        return name.length() == 1 && name.charAt(0) == 'e' ? 1 : 0; 
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
        MapWrapper<K,V> entry = null;
        for(int number = input.readFieldNumber(this);; 
                number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    if(entry == null)
                    {
                        // lazy initialize
                        entry = new MapWrapper<K,V>(map);
                    }
                    
                    if(entry != input.mergeObject(entry, ENTRY_SCHEMA))
                    {
                        // an entry will always be unique
                        // it can never be a reference.
                        throw new IllegalStateException("A Map.Entry will always be " +
                        		"unique, hence it cannot be a reference " +
                        		"obtained from " + input.getClass().getName());
                    }
                    break;
                default:
                    throw new ProtostuffException("The map was incorrectly serialized.");
            }
        }
    }

    public final void writeTo(Output output, Map<K, V> map) throws IOException
    {
        for(Entry<K, V> entry : map.entrySet())
        {
            // only inlude entries with keys.
            if(entry.getKey() != null)
            {
                // if the value is null, only the key is written.
                output.writeObject(1, entry, ENTRY_SCHEMA, true);
            }
        }
    }
    
    /**
     * A Map.Entry w/c wraps a Map.
     */
    static final class MapWrapper<K,V> implements Entry<K,V>
    {
        
        final Map<K,V> map;
        
        MapWrapper(Map<K,V> map)
        {
            this.map = map;
        }

        public K getKey()
        {
            throw new UnsupportedOperationException();
        }

        public V getValue()
        {
            throw new UnsupportedOperationException();
        }

        public V setValue(V arg0)
        {
            throw new UnsupportedOperationException();
        }
        
    }
    
    final Schema<Entry<K,V>> ENTRY_SCHEMA = new Schema<Entry<K,V>>()
    {

        public final String getFieldName(int number)
        {
            switch(number)
            {
                case 1:
                    return FIELD_NAME_KEY;
                case 2:
                    return FIELD_NAME_VALUE;
                default:
                    return null;
            }
        }

        public final int getFieldNumber(String name)
        {
            if(name.length() != 1)
                return 0;
            
            switch(name.charAt(0))
            {
                case 'k':
                    return 1;
                case 'v':
                    return 2;
                default:
                    return 0;
            }
        }

        public boolean isInitialized(Entry<K, V> message)
        {
            return true;
        }

        public String messageFullName()
        {
            return Entry.class.getName();
        }

        public String messageName()
        {
            return Entry.class.getSimpleName();
        }

        public Entry<K, V> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        public Class<? super Entry<K, V>> typeClass()
        {
            return Entry.class;
        }

        public void mergeFrom(Input input, Entry<K, V> message) throws IOException
        {
            // Nobody else calls this except MapSchema.mergeFrom
            final MapWrapper<K,V> wrapper = (MapWrapper<K,V>)message;
            
            K key = null; 
            for(int number = input.readFieldNumber(this);; 
                    number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        if(key != null)
                        {
                            // put last entry
                            wrapper.map.put(key, null);
                        }
                        return;
                    case 1:
                        if(key != null)
                        {
                            // more than one key
                            throw new ProtostuffException("The map was incorrectly " +
                            		"serialized.");
                        }

                        key = readKeyFrom(input);
                        assert key != null;
                        break;
                    case 2:
                        if(key == null)
                        {
                            // a value without a key.
                            throw new ProtostuffException("The map was incorrectly " +
                            		"serialized.");
                        }
                        
                        wrapper.map.put(key, readValueFrom(input));
                        key = null;
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly " +
                        		"serialized.");
                }
            }
        }

        public void writeTo(Output output, Entry<K, V> message) throws IOException
        {
            writeKeyTo(output, 1, message.getKey(), false);
            
            final V value = message.getValue();
            if(value != null)
                writeValueTo(output, 2, value, false);
        }
        
    };


}
