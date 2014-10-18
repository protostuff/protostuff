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

package io.protostuff;

import java.io.IOException;

/**
 * Handles the serialization and deserialization of a message/object tied to this.
 * <p>
 * Basically, any object can be serialized via protobuf. As long as its schema is provided, it does not need to
 * implement {@link Message}. This was designed with "unobtrusive" in mind. The goal was to be able to
 * serialize/deserialize any existing object without having to touch its source. This will enable you to customize the
 * serialization of objects from 3rd party libraries.
 * 
 * @author David Yu
 * @created Nov 9, 2009
 */
public interface Schema<T>
{

    /**
     * Gets the field name associated with the number. This is particularly useful when serializing to different formats
     * (Eg. JSON). When using numeric field names:
     * 
     * <pre>
     * return String.valueOf(number);
     * </pre>
     */
    public String getFieldName(int number);

    /**
     * Gets the field number associated with the name. This is particularly useful when serializing to different formats
     * (Eg. JSON). When using numeric field names:
     * 
     * <pre>
     * return Integer.parseInt(name);
     * </pre>
     */
    public int getFieldNumber(String name);

    /**
     * Returns true if there is no required field or if all the required fields are set.
     */
    public boolean isInitialized(T message);

    /**
     * Creates the message/object tied to this schema.
     */
    public T newMessage();

    /**
     * Returns the simple name of the message tied to this schema. Allows custom schemas to provide a custom name other
     * than typeClass().getSimpleName();
     */
    public String messageName();

    /**
     * Returns the full name of the message tied to this schema. Allows custom schemas to provide a custom name other
     * than typeClass().getName();
     */
    public String messageFullName();

    /**
     * Gets the class of the message.
     */
    public Class<? super T> typeClass();

    /**
     * Deserializes a message/object from the {@link Input input}.
     */
    public void mergeFrom(Input input, T message) throws IOException;

    /**
     * Serializes a message/object to the {@link Output output}.
     */
    public void writeTo(Output output, T message) throws IOException;

}
