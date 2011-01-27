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
 * A schema for a {@link Map}.
 * The key and value can be null (depending on the particular map impl).
 * 
 * The default {@link Map} message created will be an instance of {@link HashMap}.
 *
 * @author David Yu
 * @created Jun 26, 2010
 */
public abstract class MapSchema<K,V> implements Schema<Map<K,V>>
{
    
    /**
     * Creates new {@code Map} messages.
     */
    public interface MessageFactory
    {
        
        /**
         * Creates a new {@link Map} message.
         */
        public <K,V> Map<K,V> newMessage();
        
    }
    
    /**
     * A message factory for standard {@code Map} implementations.
     */
    public enum MessageFactories implements MessageFactory
    {
        // defaults to HashMap
        Map
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new HashMap<K,V>();
            }
        },
        // defaults to TreeMap
        SortedMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.TreeMap<K,V>();
            }
        },
        // defaults to TreeMap
        NavigableMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.TreeMap<K,V>();
            }
        },
        HashMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new HashMap<K,V>();
            }
        },
        LinkedHashMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.LinkedHashMap<K,V>();
            }
        },
        TreeMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.TreeMap<K,V>();
            }
        },
        WeakHashMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.WeakHashMap<K,V>();
            }
        },
        IdentityHashMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.IdentityHashMap<K,V>();
            }
        },
        Hashtable
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.Hashtable<K,V>();
            }
        },
        // defaults to ConcurrentHashMap
        ConcurrentMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.concurrent.ConcurrentHashMap<K,V>();
            }
        },
        ConcurrentHashMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.concurrent.ConcurrentHashMap<K,V>();
            }
        },
        // defaults to ConcurrentNavigableMap
        ConcurrentNavigableMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.concurrent.ConcurrentSkipListMap<K,V>();
            }
        },
        ConcurrentSkipListMap
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new java.util.concurrent.ConcurrentSkipListMap<K,V>();
            }
        };
        
        /**
         * Returns the message factory for the standard jdk {@link Map} implementations.
         */
        public static MessageFactory getFactory(Class<? extends Map<?,?>> mapType)
        {
            return mapType.getName().startsWith("java.util") ? 
                    MessageFactories.valueOf(mapType.getSimpleName()) : 
                        null;
        }
    }
    
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
    
    /**
     * Factory for creating {@code Map} messages.
     */
    public final MessageFactory messageFactory;
    
    public MapSchema()
    {
        this(MessageFactories.HashMap);
    }
    
    public MapSchema(MessageFactory messageFactory)
    {
        this.messageFactory = messageFactory;
    }
    
    /**
     * Reads the key from the input.
     */
    protected abstract K readKeyFrom(Input input) throws IOException;
    
    /**
     * Puts the entry(key and value), obtained from the input, into the {@code MapWrapper}.
     */
    protected abstract void putValueFrom(Input input, MapWrapper<K,V> wrapper, K key) 
    throws IOException;
    
    /**
     * Writes the key to the output.
     */
    protected abstract void writeKeyTo(Output output, int fieldNumber, K value, 
            boolean repeated) throws IOException;
    
    /**
     * Writes the value to the output.
     */
    protected abstract void writeValueTo(Output output, int fieldNumber, V value, 
            boolean repeated) throws IOException;
    
    /**
     * Transfers the key from the input to the output.
     */
    protected abstract void transferKey(Pipe pipe, Input input, Output output, 
            int number, boolean repeated) throws IOException;
    
    /**
     * Transfers the value from the input to the output.
     */
    protected abstract void transferValue(Pipe pipe, Input input, Output output, 
            int number, boolean repeated) throws IOException;

    
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
        return messageFactory.newMessage();
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
                    //if(entry != null)
                    //    entry.value = null;
                    return;
                case 1:
                    if(entry == null)
                    {
                        // lazy initialize
                        entry = new MapWrapper<K,V>(map);
                    }
                    
                    if(entry != input.mergeObject(entry, entrySchema))
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
            // allow null keys and values.
            output.writeObject(1, entry, entrySchema, true);
        }
    }
    
    /**
     * The pipe schema of the {@link Map}.
     */
    public final Pipe.Schema<Map<K,V>> pipeSchema = 
        new Pipe.Schema<Map<K,V>>(MapSchema.this)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(MapSchema.this);;
                    number = input.readFieldNumber(MapSchema.this))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, entryPipeSchema, true);
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly " + 
                                "serialized.");
                }
            }
        }
        
    };
    
    private final Schema<Entry<K,V>> entrySchema = new Schema<Entry<K,V>>()
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
            boolean valueRetrieved = false;
            for(int number = input.readFieldNumber(this);; 
                    number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        if(key == null)
                        {
                            // null key (and potentially null value)
                            wrapper.map.put(null, valueRetrieved ? wrapper.value : null);
                        }
                        else if(!valueRetrieved)
                        {
                            // null value
                            wrapper.map.put(key, null);
                        }
                        return;
                    case 1:
                        if(key != null)
                        {
                            throw new ProtostuffException("The map was incorrectly " + 
                                    "serialized.");
                        }
                        key = readKeyFrom(input);
                        assert key != null;
                        break;
                    case 2:
                        if(valueRetrieved)
                        {
                            throw new ProtostuffException("The map was incorrectly " + 
                                    "serialized.");
                        }
                        valueRetrieved = true;
                        
                        putValueFrom(input, wrapper, key);
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly " + 
                                    "serialized.");
                }
            }
        }

        public void writeTo(Output output, Entry<K, V> message) throws IOException
        {
            if(message.getKey() != null)
                writeKeyTo(output, 1, message.getKey(), false);
            
            if(message.getValue() != null)
                writeValueTo(output, 2, message.getValue(), false);
        }
        
    };
    
    private final Pipe.Schema<Entry<K,V>> entryPipeSchema = 
        new Pipe.Schema<Entry<K,V>>(entrySchema)
    {

        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(entrySchema);;
                    number = input.readFieldNumber(entrySchema))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        transferKey(pipe, input, output, 1, false);
                        break;
                    case 2:
                        transferValue(pipe, input, output, 2, false);
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly " +
                                        "serialized.");
                }
            }
        }
        
    };
    
    
    /**
     * A {@link Map.Entry} w/c wraps a {@link Map}.
     */
    public static final class MapWrapper<K,V> implements Entry<K,V>
    {
        
        /**
         * The actual map being operated on.
         */
        final Map<K,V> map;
        
        /**
         * A temporary storage for the value if the key is not yet available.
         */
        V value;
        
        MapWrapper(Map<K,V> map)
        {
            this.map = map;
        }

        /**
         * The key is provided as the last arg of 
         * {@link MapSchema#putValueFrom(Input, MapWrapper, Object)}.
         */
        public K getKey()
        {
            return null;
        }

        /**
         * Gets the last value set.
         */
        public V getValue()
        {
            return value;
        }

        /**
         * Sets the new value and returns the old one.
         * This method is useful for storage when deserializing cyclic object graphs.
         */
        public V setValue(V value)
        {
            final V last = this.value;
            this.value = value;
            return last;
        }
        
        /**
         * Puts the key-value entry.
         */
        public void put(K key, V value)
        {
            // Do not add the entry yet if the value is retrieved before the key.
            // This could happen when you're dealing with json serialized from the browser.
            if(key == null)
                this.value = value;
            else
                map.put(key, value);
        }
        
    }


}
