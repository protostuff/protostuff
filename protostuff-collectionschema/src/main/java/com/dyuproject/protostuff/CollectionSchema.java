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

package com.dyuproject.protostuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A schema for standard jdk {@link Collection collections}.
 * Does not allow null values.
 *
 * @author David Yu
 * @created Jan 26, 2011
 */
public abstract class CollectionSchema<V> implements Schema<Collection<V>>
{
    
    public static final String FIELD_NAME_VALUE = "v";
    
    static final int ARRAY_BLOCKING_QUEUE_INITIAL_SIZE = 
        Integer.getInteger("protostuff.collectionschema.array_block_queue_initial_size", 
                16);
    
    /**
     * Creates new {@code Collection} messages.
     */
    public interface MessageFactory
    {
        
        /**
         * Creates a new {@code Collection} message.
         */
        public <V> Collection<V> newMessage();
    }
    
    public enum MessageFactories implements MessageFactory
    {
        // defaults to ArrayList
        Collection
        {
            public <V> Collection<V> newMessage()
            {
                return new ArrayList<V>();
            }
        },
        // defaults to ArrayList
        List
        {
            public <V> Collection<V> newMessage()
            {
                return new ArrayList<V>();
            }
        },
        ArrayList
        {
            public <V> Collection<V> newMessage()
            {
                return new ArrayList<V>();
            }
        },
        LinkedList
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedList<V>();
            }
        },
        CopyOnWriteArrayList
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.CopyOnWriteArrayList<V>();
            }
        },
        Stack
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.Stack<V>();
            }
        },
        Vector
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.Vector<V>();
            }
        },
        // defaults to HashSet
        Set
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.HashSet<V>();
            }
        },
        HashSet
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.HashSet<V>();
            }
        },
        LinkedHashSet
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedHashSet<V>();
            }
        },
        // defaults to TreeSet
        SortedSet
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.TreeSet<V>();
            }
        },
        // defaults to TreeSet
        NavigableSet
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.TreeSet<V>();
            }
        },
        TreeSet
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.TreeSet<V>();
            }
        },
        ConcurrentSkipListSet
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.ConcurrentSkipListSet<V>();
            }
        },
        CopyOnWriteArraySet
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.CopyOnWriteArraySet<V>();
            }
        },
        // defaults to LinkedList
        Queue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedList<V>();
            }
        },
        // defaults to LinkedList
        BlockingQueue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedList<V>();
            }
        },
        LinkedBlockingQueue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.LinkedBlockingQueue<V>();
            }
        },
        // defaults to LinkedList
        Dequeue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedList<V>();
            }
        },
        // defaults to LinkedBlockingDeque
        BlockingDequeue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.LinkedBlockingDeque<V>();
            }
        },
        LinkedBlockingDequeue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.LinkedBlockingDeque<V>();
            }
        },
        ArrayBlockingQueue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.ArrayBlockingQueue<V>(
                        ARRAY_BLOCKING_QUEUE_INITIAL_SIZE);
            }
        },
        ArrayDequeue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.ArrayDeque<V>();
            }
        },
        ConcurrentLinkedQueue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.ConcurrentLinkedQueue<V>();
            }
        },
        PriorityBlockingQueue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.PriorityBlockingQueue<V>();
            }
        },
        PriorityQueue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.PriorityQueue<V>();
            }
        },
        SynchronousQueue
        {
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.SynchronousQueue<V>();
            }
        };
        
        /**
         * Returns the message factory for the standard jdk {@link Collection} 
         * implementations.
         */
        public static MessageFactory getFactory(Class<? extends Collection<?>> clazz)
        {
            return clazz.getName().startsWith("java.util") ? 
                    MessageFactories.valueOf(clazz.getSimpleName()) : null;
        }
    }
    
    /**
     * Factory for creating {@link Collection} messages.
     */
    public final MessageFactory messageFactory;
    
    public CollectionSchema()
    {
        this(MessageFactories.ArrayList);
    }
    
    public CollectionSchema(MessageFactory messageFactory)
    {
        this.messageFactory = messageFactory;
    }
    
    /**
     * Adds the value from the input into the {@link Collection}.
     */
    protected abstract void addValueFrom(Input input, Collection<V> collection)
    throws IOException;
    
    /**
     * Writes the value to the output.
     */
    protected abstract void writeValueTo(Output output, int fieldNumber, V value, 
            boolean repeated) throws IOException;
    
    /**
     * Transfers the value from the input to the output.
     */
    protected abstract void transferValue(Pipe pipe, Input input, Output output, 
            int number, boolean repeated) throws IOException;
    
    
    public final String getFieldName(int number)
    {
        return number == 1 ? FIELD_NAME_VALUE : null;
    }

    public final int getFieldNumber(String name)
    {
        return name.length() == 1 && name.charAt(0) == 'v' ? 1 : 0; 
    }

    public final boolean isInitialized(Collection<V> map)
    {
        return true;
    }

    public final String messageFullName()
    {
        return Collection.class.getName();
    }

    public final String messageName()
    {
        return Collection.class.getSimpleName();
    }
    
    public final Class<? super Collection<V>> typeClass()
    {
        return Collection.class;
    }
    
    public final Collection<V> newMessage()
    {
        return messageFactory.newMessage();
    }

    public void mergeFrom(Input input, Collection<V> message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; 
                number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    addValueFrom(input, message);
                    break;
                default:
                    throw new ProtostuffException("The collection was incorrectly " + 
                            "serialized.");
            }
        }
        
    }

    public void writeTo(Output output, Collection<V> message) throws IOException
    {
        for(V value : message)
        {
            // null values not allowed.
            if(value != null)
                writeValueTo(output, 1, value, true);
        }
    }

}
