//========================================================================
//Copyright 2007-2020 Mr14huashao Mr11huashao@gmail.com
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

package io.protostuff.runtime;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.*;
import com.google.common.collect.Multimap;
import io.protostuff.*;

/**
 * A schema for a {@link Multimap}. The key and value can be null (depending on the particular Multimap impl).
 * <p>
 * The default {@link Multimap} message created will be an instance of {@link ArrayListMultimap}.
 *
 * @author Mr14huashao
 * @created Oct 9, 2020
 */
public abstract class MultiMapSchema<K, V> implements Schema<Multimap<K, V>>
{

    /**
     * Creates new {@code Multimap} messages.
     */
    public interface MessageFactory
    {

        /**
         * Creates a new {@link Multimap} message.
         */
        public <K, V> Multimap<K, V> newMessage();

        /**
         * The type to instantiate.
         */
        public Class<?> typeClass();
    }

    /**
     * A message factory for standard {@code Multimap} implementations.
     */
    public enum MessageFactories implements MessageFactory
    {
        // defaults to Multimap
        Multimap(com.google.common.collect.Multimap.class) {
            @Override
            public <K, V> ArrayListMultimap<K, V> newMessage() {
                return com.google.common.collect.ArrayListMultimap.create();
            }
        },

        // defaults to HashMultimap
        HashMultimap(com.google.common.collect.HashMultimap.class) {
            @Override
            public <K, V> HashMultimap<K, V> newMessage() {
                return com.google.common.collect.HashMultimap.create();
            }
        },
        // defaults to TreeMultimap
        TreeMultimap(com.google.common.collect.TreeMultimap.class) {
            @Override
            public <K, V> TreeMultimap<K, V> newMessage() {
                return (com.google.common.collect.TreeMultimap<K, V>)com.google.common.collect.TreeMultimap.create();
            }
        },
        // defaults to ArrayListMultimap
        ArrayListMultimap(com.google.common.collect.ArrayListMultimap.class) {
            @Override
            public <K, V> ArrayListMultimap<K, V> newMessage() {
                return com.google.common.collect.ArrayListMultimap.create();
            }
        },
        // defaults to LinkedHashMultimap
        LinkedListMultimap(com.google.common.collect.LinkedHashMultimap.class) {
            @Override
            public <K, V> LinkedListMultimap<K, V> newMessage() {
                return com.google.common.collect.LinkedListMultimap.create();
            }
        },
        // defaults to LinkedHashMultimap
        LinkedHashMultimap(com.google.common.collect.LinkedHashMultimap.class) {
            @Override
            public <K, V> LinkedHashMultimap<K, V> newMessage() {
                return com.google.common.collect.LinkedHashMultimap.create();
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
         * Returns the message factory for the standard jdk {@link Multimap} implementations.
         */
        public static MessageFactories getFactory(Class<? extends Multimap<?, ?>> mapType)
        {
            return mapType.getName().startsWith("com.google.common.collect") ?
                    MessageFactories.valueOf(mapType.getSimpleName()) :
                    null;
        }

        /**
         * Returns the message factory for the standard jdk {@link Multimap} implementations.
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
        MessageFactories[] messageFactories = MultiMapSchema.MessageFactories.values();
        MESSAGE_FACTORIES_NAMES = new HashSet<String>(messageFactories.length);
        for (MessageFactories messageFactory : messageFactories)
        {
            MESSAGE_FACTORIES_NAMES.add(messageFactory.name());
        }
    }

    /**
     * The field name of the Multimap.Entry.
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
     * Factory for creating {@code Multimap} messages.
     */
    public final MessageFactory messageFactory;

    public MultiMapSchema()
    {
        this( MessageFactories.ArrayListMultimap);
    }

    public MultiMapSchema(MessageFactory messageFactory)
    {
        this.messageFactory = messageFactory;
    }

    /**
     * Reads the key from the input.
     * <p>
     * The extra wrapper arg is internally used as an object placeholder during polymorhic deserialization.
     */
    protected abstract K readKeyFrom(Input input, MultiMapWrapper<K, V> wrapper)
            throws IOException;

    /**
     * Puts the entry(key and value), obtained from the input, into the {@code MapWrapper}.
     */
    protected abstract void putValueFrom(Input input, MultiMapWrapper<K, V> wrapper, K key)
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

    @Override
    public final String getFieldName(int number)
    {
        return number == 1 ? FIELD_NAME_ENTRY : null;
    }

    @Override
    public final int getFieldNumber(String name)
    {
        return name.length() == 1 && name.charAt(0) == 'e' ? 1 : 0;
    }

    @Override
    public final boolean isInitialized(Multimap<K, V> map)
    {
        return true;
    }

    @Override
    public final String messageFullName()
    {
        return Map.class.getName();
    }

    @Override
    public final String messageName()
    {
        return Map.class.getSimpleName();
    }

    @Override
    public final Class<? super Multimap<K, V>> typeClass()
    {
        return Multimap.class;
    }

    @Override
    public final Multimap<K, V> newMessage()
    {
        return messageFactory.newMessage();
    }

    @Override
    public final void mergeFrom(Input input, Multimap<K, V> map) throws IOException
    {
        MultiMapWrapper<K, V> entry = null;
        for (int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch (number)
            {
                case 0:
                    // if(entry != null)
                    // entry.value = null;
                    return;
                case 1:
                    if (entry == null)
                    {
                        // lazy initialize
                        entry = new MultiMapWrapper<K, V>(map);
                    }

                    if (entry != input.mergeObject(entry, entrySchema))
                    {
                        // an entry will always be unique
                        // it can never be a reference.
                        throw new IllegalStateException("A Multimap.Entry will always be " +
                                "unique, hence it cannot be a reference " +
                                "obtained from " + input.getClass().getName());
                    }
                    break;
                default:
                    throw new ProtostuffException("The multimap was incorrectly serialized.");
            }
        }
    }

    @Override
    public final void writeTo(Output output, Multimap<K, V> map) throws IOException
    {
        for (Entry<K, V> entry : map.entries())
        {
            // allow null keys and values.
            output.writeObject(1, entry, entrySchema, true);
        }
    }

    /**
     * The pipe schema of the {@link Multimap}.
     */
    public final Pipe.Schema<Multimap<K, V>> pipeSchema =
            new Pipe.Schema<Multimap<K, V>>( MultiMapSchema.this)
            {
                @Override
                protected void transfer(Pipe pipe, Input input, Output output) throws IOException
                {
                    for (int number = input.readFieldNumber( MultiMapSchema.this);;
                         number = input.readFieldNumber( MultiMapSchema.this))
                    {
                        switch (number)
                        {
                            case 0:
                                return;
                            case 1:
                                output.writeObject(number, pipe, entryPipeSchema, true);
                                break;
                            default:
                                throw new ProtostuffException("The multimap was incorrectly " +
                                        "serialized.");
                        }
                    }
                }

            };

    private final Schema<Entry<K, V>> entrySchema = new Schema<Entry<K, V>>()
    {

        @Override
        public final String getFieldName(int number)
        {
            switch (number)
            {
                case 1:
                    return FIELD_NAME_KEY;
                case 2:
                    return FIELD_NAME_VALUE;
                default:
                    return null;
            }
        }

        @Override
        public final int getFieldNumber(String name)
        {
            if (name.length() != 1)
                return 0;

            switch (name.charAt(0))
            {
                case 'k':
                    return 1;
                case 'v':
                    return 2;
                default:
                    return 0;
            }
        }

        @Override
        public boolean isInitialized(Entry<K, V> message)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return Entry.class.getName();
        }

        @Override
        public String messageName()
        {
            return Entry.class.getSimpleName();
        }

        @Override
        public Entry<K, V> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? super Entry<K, V>> typeClass()
        {
            return Entry.class;
        }

        @Override
        public void mergeFrom(Input input, Entry<K, V> message) throws IOException
        {
            // Nobody else calls this except MapSchema.mergeFrom
            final MultiMapWrapper<K, V> wrapper = (MultiMapWrapper<K, V>) message;

            K key = null;
            boolean valueRetrieved = false;
            for (int number = input.readFieldNumber(this);;
            number = input.readFieldNumber(this))
            {
                switch (number)
                {
                    case 0:
                        if (key == null)
                        {
                            // null key (and potentially null value)
                            wrapper.map.put(null, valueRetrieved ? wrapper.value : null);
                        }
                        else if (!valueRetrieved)
                        {
                            // null value
                            wrapper.map.put(key, null);
                        }
                        return;
                    case 1:
                        if (key != null)
                        {
                            throw new ProtostuffException("The Multimap was incorrectly " +
                                    "serialized.");
                        }
                        key = readKeyFrom(input, wrapper);
                        assert key != null;
                        break;
                    case 2:
                        if (valueRetrieved)
                        {
                            throw new ProtostuffException("The Multimap was incorrectly " +
                                    "serialized.");
                        }
                        valueRetrieved = true;

                        putValueFrom(input, wrapper, key);
                        break;
                    default:
                        throw new ProtostuffException("The Multimap was incorrectly " +
                                "serialized.");
                }
            }
        }

        @Override
        public void writeTo(Output output, Entry<K, V> message) throws IOException
        {
            final K key = message.getKey();
            final V value = message.getValue();
            if (key != null)
                writeKeyTo(output, 1, key, false);

            if (value != null)
                writeValueTo(output, 2, value, false);
        }

    };

    private final Pipe.Schema<Entry<K, V>> entryPipeSchema =
            new Pipe.Schema<Entry<K, V>>(entrySchema)
            {

                @Override
                protected void transfer(Pipe pipe, Input input, Output output) throws IOException
                {
                    for (int number = input.readFieldNumber(entrySchema);;
                    number = input.readFieldNumber(entrySchema))
                    {
                        switch (number)
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
                                throw new ProtostuffException("The Multimap was incorrectly " +
                                        "serialized.");
                        }
                    }
                }

            };

    /**
     * A {@link Entry} w/c wraps a {@link Multimap}.
     */
    public static final class MultiMapWrapper<K, V> implements Entry<K, V>
    {

        /**
         * The actual map being operated on.
         */
        final Multimap<K, V> map;

        /**
         * A temporary storage for the value if the key is not yet available.
         */
        V value;

        MultiMapWrapper(Multimap<K, V> map)
        {
            this.map = map;
        }

        /**
         * The key is provided as the last arg of {@link MultiMapSchema#putValueFrom(Input, MultiMapWrapper, Object)}.
         */
        @Override
        public K getKey()
        {
            return null;
        }

        /**
         * Gets the last value set.
         */
        @Override
        public V getValue()
        {
            return value;
        }

        /**
         * Sets the new value and returns the old one. This method is useful for storage when deserializing cyclic
         * object graphs.
         */
        @Override
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
            if (key == null)
                this.value = value;
            else
                map.put(key, value);
        }

    }

}
