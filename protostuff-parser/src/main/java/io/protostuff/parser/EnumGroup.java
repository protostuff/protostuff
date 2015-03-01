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

package io.protostuff.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * Represents an enum declared in either the {@link Proto} or nested in a {@link Message}.
 * 
 * @author David Yu
 * @created Dec 21, 2009
 */
public class EnumGroup extends AnnotationContainer implements HasName, HasOptions
{

    /**
     * Disabled by default (the earlier protoc 2.x versions enabled this by default, but was changed later on).
     */
    public static final boolean ENUM_ALLOW_ALIAS = Boolean.parseBoolean(
            "protostuff.enum_allow_alias");

    final String name;
    final Message parentMessage;
    final Proto proto;

    final LinkedHashMap<String, Value> values = new LinkedHashMap<>();
    final ArrayList<Value> sortedValues = new ArrayList<>();
    final LinkedHashMap<String, Object> standardOptions = new LinkedHashMap<>();
    final LinkedHashMap<String, Object> extraOptions = new LinkedHashMap<>();

    private ArrayList<Value> indexedValues;
    private ArrayList<Value> uniqueSortedValues;

    public EnumGroup(String name, Message parentMessage, Proto proto)
    {
        this.name = name;
        this.parentMessage = parentMessage;

        if (parentMessage != null)
        {
            this.proto = parentMessage.getProto();
            parentMessage.addNestedEnumGroup(this);
        }
        else
        {
            this.proto = proto;
            proto.addEnumGroup(this);
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    public String getFullName()
    {
        StringBuilder buffer = new StringBuilder();
        if (isNested())
            buffer.append(parentMessage.getFullName()).append('.').append(name);
        else
            buffer.append(getProto().getPackageName()).append('.').append(name);
        return buffer.toString();
    }

    public String getJavaFullName()
    {
        StringBuilder buffer = new StringBuilder();
        if (isNested())
            buffer.append(parentMessage.getJavaFullName()).append('.').append(name);
        else
            buffer.append(getProto().getJavaPackageName()).append('.').append(name);
        return buffer.toString();
    }

    public String getRelativeName()
    {
        return isNested() ? parentMessage.getRelativeName() + "." + name : name;
    }

    /* ================================================== */

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
        return proto;
    }

    /* ================================================== */

    @Override
    public void putStandardOption(String key, Object value)
    {
        putExtraOption(key, value);
        standardOptions.put(key, value);
    }

    @Override
    public void putExtraOption(String key, Object value)
    {
        if (extraOptions.put(key, value) != null)
            throw err("Duplicate enum option: " + key, getProto());
    }

    public LinkedHashMap<String, Object> getStandardOptions()
    {
        return standardOptions;
    }

    public Object getStandardOption(String key)
    {
        return standardOptions.get(key);
    }

    public LinkedHashMap<String, Object> getExtraOptions()
    {
        return extraOptions;
    }

    public LinkedHashMap<java.lang.String, Object> getO()
    {
        return getOptions();
    }

    @Override
    public LinkedHashMap<String, Object> getOptions()
    {
        return extraOptions;
    }

    @SuppressWarnings("unchecked")
    public <V> V getExtraOption(java.lang.String key)
    {
        return (V) extraOptions.get(key);
    }

    public Value getValue(int index)
    {
        if (indexedValues == null)
            indexedValues = new ArrayList<>(values.values());

        return indexedValues.get(index);
    }

    public Value getValue(String name)
    {
        return values.get(name);
    }

    public Collection<Value> getValues()
    {
        return values.values();
    }

    public LinkedHashMap<String, Value> getValueMap()
    {
        return values;
    }

    public ArrayList<Value> getSortedValues()
    {
        return sortedValues;
    }

    public Value getFirstValue()
    {
        if (indexedValues == null)
            indexedValues = new ArrayList<>(values.values());

        return indexedValues.get(0);
    }

    public int getValueCount()
    {
        return values.size();
    }

    void add(Value value)
    {
        if (values.put(value.name, value) != null)
        {
            throw err("The enum " + name + " has duplicate names: " + value.name,
                    getProto());
        }

        sortedValues.add(value);
    }

    void cacheFullyQualifiedName()
    {
        final Boolean b = (Boolean) getOptions().get("allow_alias");
        if (b != null)
        {
            if (b.booleanValue())
                Collections.sort(sortedValues);
            else
                Collections.sort(sortedValues, Value.NO_ALIAS_COMPARATOR);
        }
        else if (ENUM_ALLOW_ALIAS)
            Collections.sort(sortedValues);
        else
            Collections.sort(sortedValues, Value.NO_ALIAS_COMPARATOR);

        final Proto proto = getProto();
        final String fullName = getFullName();

        proto.fullyQualifiedEnumGroups.put(fullName, this);

        if (!standardOptions.isEmpty())
            proto.references.add(new ConfiguredReference(standardOptions, extraOptions, fullName));

        for (Value v : values.values())
        {
            if (!v.field.standardOptions.isEmpty())
            {
                proto.references.add(new ConfiguredReference(
                        v.field.standardOptions, v.field.extraOptions, fullName));
            }
        }
    }

    public ArrayList<Value> getUniqueSortedValues()
    {
        if (uniqueSortedValues != null)
            return uniqueSortedValues;

        uniqueSortedValues = new ArrayList<>();
        Value last = null;
        for (Value v : sortedValues)
        {
            if (last == null || v.number != last.number)
                uniqueSortedValues.add(v);
            last = v;
        }
        return uniqueSortedValues;
    }

    public String toString()
    {
        return new StringBuilder()
                .append('{')
                .append("name:").append(name)
                .append(',').append("values:").append(values)
                .append('}')
                .toString();
    }

    public static class Value extends AnnotationContainer implements Comparable<Value>, HasName
    {
        public static final Comparator<Value> NO_ALIAS_COMPARATOR =
                new Comparator<Value>()
                {
                    @Override
                    public int compare(Value v1, Value v2)
                    {
                        if (v1.number == v2.number)
                        {
                            throw err("The enum value " +
                                    v2.enumGroup.getName() + "." + v2.getName() +
                                    " cannot have the same number as " +
                                    v1.enumGroup.getName() + "." + v1.getName(),
                                    v2.enumGroup.getProto());
                        }

                        return v1.number - v2.number;
                    }
                };

        final String name;
        final int number;
        final EnumGroup enumGroup;

        public final EnumField field = new EnumField(this);

        public Value(String name, int number, EnumGroup enumGroup)
        {
            this.name = name;
            this.number = number;
            this.enumGroup = enumGroup;

            field.enumGroup = enumGroup;
            field.name = name;

            enumGroup.add(this);
        }

        /**
         * @return the name
         */
        @Override
        public String getName()
        {
            return name;
        }

        /**
         * @return the number
         */
        public int getNumber()
        {
            return number;
        }

        @Override
        public Proto getProto()
        {
            return enumGroup.getProto();
        }

        /**
         * @return the enumGroup
         */
        public EnumGroup getEnumGroup()
        {
            return enumGroup;
        }

        /**
         * Alias to {@link #getEnumGroup()}.
         */
        public EnumGroup getEg()
        {
            return enumGroup;
        }

        // options

        public LinkedHashMap<java.lang.String, Object> getStandardOptions()
        {
            return field.getStandardOptions();
        }

        public LinkedHashMap<java.lang.String, Object> getExtraOptions()
        {
            return field.getExtraOptions();
        }

        public LinkedHashMap<java.lang.String, Object> getO()
        {
            return getOptions();
        }

        /**
         * Returns the options configured.
         */
        public LinkedHashMap<java.lang.String, Object> getOptions()
        {
            return field.getOptions();
        }

        public String toString()
        {
            return new StringBuilder().append(name).append(':').append(number).toString();
        }

        @Override
        public int compareTo(Value o)
        {
            // if equal, sort by order of declaration
            return o.number < number ? 1 : -1;
        }
    }

}
