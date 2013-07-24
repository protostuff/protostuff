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

package com.dyuproject.protostuff.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Represents an enum declared in either the {@link Proto} or nested in a {@link Message}.
 *
 * @author David Yu
 * @created Dec 21, 2009
 */
public class EnumGroup extends AnnotationContainer implements HasName, HasOptions
{
    
    String name;
    Message parentMessage;
    Proto proto;
    final LinkedHashMap<String,Value> values = new LinkedHashMap<String,Value>();
    final ArrayList<Value> sortedValues = new ArrayList<Value>();
    final LinkedHashMap<String,Object> standardOptions = new LinkedHashMap<String,Object>();
    final LinkedHashMap<String,Object> extraOptions = new LinkedHashMap<String,Object>();
    
    private ArrayList<Value> indexedValues;
    private ArrayList<Value> uniqueSortedValues;
    
    public EnumGroup()
    {
        
    }
    
    public EnumGroup(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getFullName()
    {
        StringBuilder buffer = new StringBuilder();
        if(isNested())
            buffer.append(parentMessage.getFullName()).append('.').append(name);
        else
            buffer.append(getProto().getPackageName()).append('.').append(name);
        return buffer.toString();
    }
    
    public String getJavaFullName()
    {
        StringBuilder buffer = new StringBuilder();
        if(isNested())
            buffer.append(parentMessage.getJavaFullName()).append('.').append(name);
        else
            buffer.append(getProto().getJavaPackageName()).append('.').append(name);
        return buffer.toString();
    }
    
    public String getRelativeName()
    {
        return isNested() ? parentMessage.getRelativeName() + "." + name : name;
    }
    
    public Message getParentMessage()
    {
        return parentMessage;
    }
    
    public boolean isNested()
    {
        return parentMessage!=null;
    }
    
    public Proto getProto()
    {
        Proto p = proto;
        if(p==null)
            p = proto = parentMessage.getProto();
        return p;
    }
    
    public void putStandardOption(String key, Object value)
    {
        putExtraOption(key, value);
        standardOptions.put(key, value);
    }
    
    public void putExtraOption(String key, Object value)
    {
        if(extraOptions.put(key, value) != null)
            throw err("Duplicate enum option: " + key, getProto());
    }
    
    public LinkedHashMap<String,Object> getStandardOptions()
    {
        return standardOptions;
    }
    
    public Object getStandardOption(String key)
    {
        return standardOptions.get(key);
    }
    
    public LinkedHashMap<String,Object> getExtraOptions()
    {
        return extraOptions;
    }
    
    public LinkedHashMap<String,Object> getOptions()
    {
        return extraOptions;
    }
    
    @SuppressWarnings("unchecked")
    public <V> V getExtraOption(java.lang.String key)
    {
        return (V)extraOptions.get(key);
    }
    
    public Value getValue(int index)
    {
        if(indexedValues == null)
            indexedValues = new ArrayList<Value>(values.values());
        
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
    
    public LinkedHashMap<String,Value> getValueMap()
    {
        return values;
    }
    
    public ArrayList<Value> getSortedValues()
    {
        return sortedValues;
    }
    
    public Value getFirstValue()
    {
        if(indexedValues == null)
            indexedValues = new ArrayList<Value>(values.values());
        
        return indexedValues.get(0);
    }
    
    public int getValueCount()
    {
        return values.size();
    }
    
    void add(Value value)
    {
        if(values.put(value.name, value) != null)
        {
            throw err("The enum " + name + " has duplicate names: " + value.name, 
                    getProto());
        }
        
        value.setEnumGroup(this);
        
        sortedValues.add(value);
    }
    
    void cacheFullyQualifiedName()
    {
        Collections.sort(sortedValues);
        
        Proto proto = getProto();
        proto.fullyQualifiedEnumGroups.put(getFullName(), this);
        
        if(!standardOptions.isEmpty())
            proto.references.add(new ConfiguredReference(standardOptions, extraOptions, getFullName()));
        
        for(Value v : values.values())
            proto.references.add(new ConfiguredReference(v.field.standardOptions, v.field.extraOptions, getFullName()));
    }
    
    public ArrayList<Value> getUniqueSortedValues()
    {
        if(uniqueSortedValues != null)
            return uniqueSortedValues;
        
        uniqueSortedValues = new ArrayList<Value>();
        Value last = null;
        for(Value v : sortedValues)
        {
            if(last==null || v.number!=last.number)
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
        final String name;
        final int number;
        EnumGroup enumGroup;
        
        final EnumField field = new EnumField();

        public Value(String name, int number)
        {
            this.name = name;
            this.number = number;
        }

        /**
         * @return the name
         */
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
        
        void setEnumGroup(EnumGroup enumGroup)
        {
            this.enumGroup = enumGroup;
            field.enumGroup = enumGroup;
        }
        
        /**
         * @return the enumGroup
         */
        public EnumGroup getEnumGroup()
        {
            return enumGroup;
        }
        
        // options
        
        public LinkedHashMap<java.lang.String,Object> getStandardOptions()
        {
            return field.getStandardOptions();
        }
        
        public LinkedHashMap<java.lang.String,Object> getExtraOptions()
        {
            return field.getExtraOptions();
        }
        
        /**
         * Returns the options configured.
         */
        public LinkedHashMap<java.lang.String,Object> getOptions()
        {
            return field.getOptions();
        }

        public String toString()
        {
            return new StringBuilder().append(name).append(':').append(number).toString();
        }

        public int compareTo(Value o)
        {
            // if equal, sort by order of declaration
            return o.number < number ? 1 : -1;
        }
    }

}
