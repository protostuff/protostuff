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

/**
 * Represents an enum declared in either the {@link Proto} or nested in a {@link Message}.
 *
 * @author David Yu
 * @created Dec 21, 2009
 */
public class EnumGroup implements HasName
{
    
    String name;
    Message parentMessage;
    Proto proto;
    final ArrayList<Value> values = new ArrayList<Value>();
    final ArrayList<Value> sortedValues = new ArrayList<Value>();
    
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
            buffer.append(getProto().getJavaPackageName()).append('.').append(name);
        return buffer.toString();
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
    
    public Value getValue(int index)
    {
        return values.get(index);
    }
    
    public Value getValue(String name)
    {
        for(Value v : values)
        {
            if(v.name.equals(name))
                return v;
        }
        return null;
    }
    
    public Collection<Value> getValues()
    {
        return values;
    }
    
    public ArrayList<Value> getSortedValues()
    {
        return sortedValues;
    }
    
    public Value getFirstValue()
    {
        return values.get(0);
    }
    
    void add(Value value)
    {
        value.enumGroup = this;
        values.add(value);
        sortedValues.add(value);
        // sort along the way?
        Collections.sort(sortedValues);
    }
    
    public ArrayList<Value> getUniqueSortedValues()
    {
        ArrayList<Value> uniqueSorted = new ArrayList<Value>();
        Value last = null;
        for(Value v : sortedValues)
        {
            if(last==null || v.number!=last.number)
                uniqueSorted.add(v);
            last = v;
        }
        return uniqueSorted;
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

    public static class Value implements Comparable<Value>
    {
        final String name;
        final int number;
        EnumGroup enumGroup;

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
        
        /**
         * @return the enumGroup
         */
        public EnumGroup getEnumGroup()
        {
            return enumGroup;
        }

        public String toString()
        {
            return new StringBuilder().append(name).append(':').append(number).toString();
        }

        public int compareTo(Value o)
        {
            if(o.name.equals(name))
                throw new IllegalStateException("The enum " + enumGroup.name + " contains the same values.");
            
            // if equal, sort by order of declaration
            return o.number < number ? 1 : -1;
        }
    }

}
