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

package io.protostuff.parser;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Represents an extend block declared in either the {@link Proto} or nested in a {@link Message}.
 * 
 * @author Philippe Laflamme
 */
public class Extension extends AnnotationContainer implements HasFields
{
    final Message parentMessage;
    final String packageName;
    final String type;
    Proto proto;
    final LinkedHashMap<String, Field<?>> fields = new LinkedHashMap<>();
    final LinkedHashMap<String, Object> standardOptions = new LinkedHashMap<>();
    final LinkedHashMap<String, Object> extraOptions = new LinkedHashMap<>();

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

    @Override
    public Proto getProto()
    {
        Proto p = proto;
        if (p == null)
            proto = p = parentMessage.getProto();
        return p;
    }

    @Override
    public Collection<Field<?>> getFields()
    {
        return fields.values();
    }

    @Override
    public Field<?> getField(String name)
    {
        return fields.get(name);
    }

    @Override
    public void addField(Field<?> field)
    {
        if (fields.put(field.name, field) != null)
            throw err("Duplicate extension field: " + field.name, getProto());
    }

    @Override
    public void putStandardOption(String key, Object value)
    {
        putExtraOption(key, value);
        standardOptions.put(key, value);
    }

    public LinkedHashMap<String, Object> getStandardOptions()
    {
        return standardOptions;
    }

    public Object getStandardOption(String key)
    {
        return standardOptions.get(key);
    }

    @Override
    public void putExtraOption(String key, Object value)
    {
        if (extraOptions.put(key, value) != null)
            throw err("Duplicate extension option: " + key, getProto());
    }

    public LinkedHashMap<String, Object> getExtraOptions()
    {
        return extraOptions;
    }

    public Object getExtraOption(String key)
    {
        return extraOptions.get(key);
    }

    public LinkedHashMap<String, Object> getO()
    {
        return getOptions();
    }

    @Override
    public LinkedHashMap<String, Object> getOptions()
    {
        return extraOptions;
    }

    public Message getExtendedMessage()
    {
        return extendedMessage;
    }

    void resolveReferences()
    {
        extendedMessage = getProto().findMessageReference(getExtendedMessageFullName(),
                getEnclosingNamespace());
        if (extendedMessage == null)
        {
            throw err("The message " + getExtendedMessageFullName()
                    + " is not defined", getProto());
        }
        extendedMessage.extend(this);

        if (!standardOptions.isEmpty())
            proto.references.add(new ConfiguredReference(standardOptions, extraOptions, getExtendedMessageFullName()));
    }

    public String getExtendedMessageFullName()
    {
        return this.packageName == null ? this.type : this.packageName + "." + this.type;
    }

    @Override
    public String getEnclosingNamespace()
    {
        return isNested() ? getParentMessage().getFullName() : getProto().getPackageName();
    }

    public String toString()
    {
        return new StringBuilder().append('{').append("extend:").append(
                getExtendedMessageFullName()).append(',').append("fields:").append(fields.values())
                .append('}').toString();
    }

}
