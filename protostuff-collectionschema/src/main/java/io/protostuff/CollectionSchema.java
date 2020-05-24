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

package io.protostuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A schema for standard jdk {@link Collection collections}. Null values are not serialized/written.
 * <p>
 * If {@link #preserveNull} is false and your application relies on {@link Object#equals(Object)}, 
 * it will fail when a serialized collection contains null values.
 * 
 * @author David Yu
 * @created Jan 26, 2011
 */
public abstract class CollectionSchema<V> implements Schema<Collection<V>>
{

    public static final String FIELD_NAME_VALUE = "v";
    public static final String FIELD_NAME_NULL = "n";

    /**
     * Creates new {@code Collection} messages.
     */
    public interface MessageFactory
    {

        /**
         * Creates a new {@code Collection} message.
         */
        public <V> Collection<V> newMessage();

        /**
         * The type to instantiate.
         */
        public Class<?> typeClass();
    }

    public enum MessageFactories implements MessageFactory
    {
        // defaults to ArrayList
        Collection(java.util.ArrayList.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new ArrayList<V>();
            }
        },
        // defaults to ArrayList
        List(java.util.ArrayList.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new ArrayList<V>();
            }
        },
        ArrayList(java.util.ArrayList.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new ArrayList<V>();
            }
        },
        LinkedList(java.util.LinkedList.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedList<V>();
            }
        },
        CopyOnWriteArrayList(java.util.concurrent.CopyOnWriteArrayList.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.CopyOnWriteArrayList<V>();
            }
        },
        Stack(java.util.Stack.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.Stack<V>();
            }
        },
        Vector(java.util.Vector.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.Vector<V>();
            }
        },
        // defaults to HashSet
        Set(java.util.HashSet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.HashSet<V>();
            }
        },
        HashSet(java.util.HashSet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.HashSet<V>();
            }
        },
        LinkedHashSet(java.util.LinkedHashSet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedHashSet<V>();
            }
        },
        // defaults to TreeSet
        SortedSet(java.util.TreeSet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.TreeSet<V>();
            }
        },
        // defaults to TreeSet
        NavigableSet(java.util.TreeSet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.TreeSet<V>();
            }
        },
        TreeSet(java.util.TreeSet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.TreeSet<V>();
            }
        },
        ConcurrentSkipListSet(java.util.concurrent.ConcurrentSkipListSet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.ConcurrentSkipListSet<V>();
            }
        },
        CopyOnWriteArraySet(java.util.concurrent.CopyOnWriteArraySet.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.CopyOnWriteArraySet<V>();
            }
        },
        // defaults to LinkedList
        Queue(java.util.LinkedList.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedList<V>();
            }
        },
        // defaults to LinkedBlockingQueue
        BlockingQueue(java.util.concurrent.LinkedBlockingQueue.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.LinkedBlockingQueue<V>();
            }
        },
        LinkedBlockingQueue(java.util.concurrent.LinkedBlockingQueue.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.LinkedBlockingQueue<V>();
            }
        },
        // defaults to LinkedList
        Deque(java.util.LinkedList.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.LinkedList<V>();
            }
        },
        // defaults to LinkedBlockingDeque
        BlockingDeque(java.util.concurrent.LinkedBlockingDeque.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.LinkedBlockingDeque<V>();
            }
        },
        LinkedBlockingDeque(java.util.concurrent.LinkedBlockingDeque.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.LinkedBlockingDeque<V>();
            }
        },
        ArrayBlockingQueue(java.util.concurrent.ArrayBlockingQueue.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                // initialize to same initial value as ArrayList
                return new java.util.concurrent.ArrayBlockingQueue<V>(10);
            }
        },
        ArrayDeque(java.util.ArrayDeque.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.ArrayDeque<V>();
            }
        },
        ConcurrentLinkedQueue(java.util.concurrent.ConcurrentLinkedQueue.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.ConcurrentLinkedQueue<V>();
            }
        },
        ConcurrentLinkedDeque(java.util.concurrent.ConcurrentLinkedDeque.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.ConcurrentLinkedDeque<V>();
            }
        },
        PriorityBlockingQueue(java.util.concurrent.PriorityBlockingQueue.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.concurrent.PriorityBlockingQueue<V>();
            }
        },
        PriorityQueue(java.util.PriorityQueue.class)
        {
            @Override
            public <V> Collection<V> newMessage()
            {
                return new java.util.PriorityQueue<V>();
            }
        };

        public final Class<?> typeClass;

        private MessageFactories(Class<?> typeClass)
        {
            this.typeClass = typeClass;
        }

        @Override
        public Class<?> typeClass()
        {
            return typeClass;
        }

        /**
         * Returns the message factory for the standard jdk {@link Collection} implementations.
         */
        public static MessageFactories getFactory(Class<? extends Collection<?>> clazz)
        {
            return clazz.getName().startsWith("java.util") ?
                    MessageFactories.valueOf(clazz.getSimpleName()) : null;
        }

        /**
         * Returns the message factory for the standard jdk {@link Collection} implementations.
         */
        public static MessageFactories getFactory(String name)
        {
            return MessageFactories.valueOf(name);
        }

        /**
         * Check whether the specific class name can be accepted by factory.
         */
        public static boolean accept(String name)
        {
            return MESSAGE_FACTORIES_NAMES.contains(name);
        }
    }

    /**
     * This is used by {@link MessageFactories#accept(String)} method. Rather than iterating enums in runtime
     * which can be an expensive way to do, caching all the enums as static property will be a good way.
     */
    static final Set<String> MESSAGE_FACTORIES_NAMES;

    static
    {
        MessageFactories[] messageFactories = MessageFactories.values();
        MESSAGE_FACTORIES_NAMES = new HashSet<String>(messageFactories.length);
        for (MessageFactories messageFactory : messageFactories)
        {
            MESSAGE_FACTORIES_NAMES.add(messageFactory.name());
        }
    }

    /**
     * Factory for creating {@link Collection} messages.
     */
    public final MessageFactory messageFactory;
    
    public final boolean preserveNull;

    public CollectionSchema(boolean preserveNull)
    {
        this(MessageFactories.ArrayList, preserveNull);
    }

    public CollectionSchema(MessageFactory messageFactory, boolean preserveNull)
    {
        this.messageFactory = messageFactory;
        this.preserveNull = preserveNull;
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

    @Override
    public final String getFieldName(int number)
    {
        switch (number)
        {
            case 1: return FIELD_NAME_VALUE;
            case 2: return FIELD_NAME_NULL;
            default: return null;
        }
    }

    @Override
    public final int getFieldNumber(String name)
    {
        if (1 != name.length())
            return 0;
        
        switch (name.charAt(0))
        {
            case 'v': return 1;
            case 'n': return 2;
            default: return 0;
        }
    }

    @Override
    public final boolean isInitialized(Collection<V> map)
    {
        return true;
    }

    @Override
    public final String messageFullName()
    {
        return Collection.class.getName();
    }

    @Override
    public final String messageName()
    {
        return Collection.class.getSimpleName();
    }

    @Override
    public final Class<? super Collection<V>> typeClass()
    {
        return Collection.class;
    }

    @Override
    public final Collection<V> newMessage()
    {
        return messageFactory.newMessage();
    }

    @Override
    public void mergeFrom(Input input, Collection<V> message) throws IOException
    {
        for (int number = input.readFieldNumber(this), i, nullCount;; number = input.readFieldNumber(this))
        {
            switch (number)
            {
                case 0:
                    return;
                case 1:
                    addValueFrom(input, message);
                    break;
                case 2:
                    for (nullCount = input.readUInt32(), i = 0; i < nullCount; i++)
                        message.add(null);
                    break;
                default:
                    throw new ProtostuffException("The collection was incorrectly serialized.");
            }
        }
    }
    
    private void writeWithNullTo(Output output, Collection<V> message) throws IOException
    {
        int nullCount = 0;
        for (V value : message)
        {
            if (value != null)
            {
                if (nullCount != 0)
                {
                    output.writeUInt32(2, nullCount, false);
                    nullCount = 0;
                }
                writeValueTo(output, 1, value, true);
            }
            else
            {
                nullCount++;
            }
        }
        if (nullCount != 0)
            output.writeUInt32(2, nullCount, false);
    }
    
    private void writeWithoutNullTo(Output output, Collection<V> message) throws IOException
    {
        for (V value : message)
        {
            if (value != null)
                writeValueTo(output, 1, value, true);
        }
    }

    @Override
    public void writeTo(Output output, Collection<V> message) throws IOException
    {
        if (preserveNull)
            writeWithNullTo(output, message);
        else
            writeWithoutNullTo(output, message);
    }

    public final Pipe.Schema<Collection<V>> pipeSchema =
            new Pipe.Schema<Collection<V>>(CollectionSchema.this)
            {

                @Override
                protected void transfer(Pipe pipe, Input input, Output output) throws IOException
                {
                    for (int number = input.readFieldNumber(this);;
                    number = input.readFieldNumber(this))
                    {
                        switch (number)
                        {
                            case 0:
                                return;
                            case 1:
                                transferValue(pipe, input, output, 1, true);
                                break;
                            case 2:
                                output.writeUInt32(2, input.readUInt32(), false);
                                break;
                            default:
                                throw new ProtostuffException("The collection was incorrectly serialized.");
                        }
                    }
                }

            };

}
