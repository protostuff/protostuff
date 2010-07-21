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

package com.dyuproject.protostuff.parser;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Represents an extend block declared in either the {@link Proto} or nested in
 * a {@link Message}.
 * 
 * @author Philippe Laflamme
 */
public class Extension implements HasFields
{
    final Message parentMessage;
    final String packageName;
    final String type;
    Proto proto;
    final LinkedHashMap<String, Field<?>> fields = new LinkedHashMap<String, Field<?>>();

    Message extendedMessage;

    public Extension(Proto proto, Message parentMessage, String packageName, String type)
    {
        this.proto = proto;
        this.parentMessage = parentMessage;
        this.packageName = packageName;
        this.type = type;
    }

    public Message getParentMessage()
    {
        return parentMessage;
    }

    public boolean isNested()
    {
        return parentMessage != null;
    }

    public Proto getProto()
    {
        Proto p = proto;
        if (p == null)
            proto = p = parentMessage.getProto();
        return p;
    }

    public Collection<Field<?>> getFields()
    {
        return fields.values();
    }

    public Field<?> getField(String name)
    {
        return fields.get(name);
    }

    public void addField(Field<?> field)
    {
        this.fields.put(field.getName(), field);
    }

    public Message getExtendedMessage()
    {
        return extendedMessage;
    }

    void resolveReferences()
    {
        Message extendedMessage = Message.findMessage(this.type, this.packageName, getProto());
        if (extendedMessage == null)
        {
            throw new IllegalStateException("The message " + getExtendedMessageFullName()
                    + " is not defined.");
        }
        extendedMessage.extend(this);
        this.extendedMessage = extendedMessage;
    }

    private String getExtendedMessageFullName()
    {
        return this.packageName == null ? this.type : this.packageName + "." + this.type;
    }

    public String toString()
    {
        return new StringBuilder().append('{').append("extend:").append(
                getExtendedMessageFullName()).append(',').append("fields:").append(fields.values())
                .append('}').toString();
    }

}
