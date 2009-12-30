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

package com.dyuproject.protostuff.protoparser;

/**
 * TODO
 *
 * @author David Yu
 * @created Dec 19, 2009
 */
public abstract class Field<T> implements Comparable<Field<?>>
{
    
    public enum Modifier
    {
        OPTIONAL,
        REQUIRED,
        REPEATED        
    }
   
    
    java.lang.String name;
    int number;
    Modifier modifier;
    boolean packable;
    T defaultValue;
    
    public Field()
    {
        
    }
    
    public Field(boolean packable)
    {
        this.packable = packable;
    }
    
    /**
     * @return the name
     */
    public java.lang.String getName()
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
     * @return the modifier
     */
    public Modifier getModifier()
    {
        return modifier;
    }

    /**
     * @return the packable
     */
    public boolean isPackable()
    {
        return packable;
    }

    /**
     * @return the defaultValue
     */
    public T getDefaultValue()
    {
        return defaultValue;
    }
    
    public java.lang.String toString()
    {
        return new StringBuilder()
            .append('{')
            .append("type:").append(getClass().getSimpleName())
            .append(',').append("name:").append(name)
            .append(',').append("number:").append(number)
            .append(',').append("modifier:").append(modifier)
            .append(',').append("packable:").append(packable)
            .append(',').append("defaultValue:").append(defaultValue)
            .append('}')                
            .toString();
    }
    
    public int compareTo(Field<?> f)
    {
        if(f.number==number)
            throw new IllegalStateException("The field: " + f.name + " cannot have the same number as " + name);
        
        return f.number < number ? 1 : -1;
    }
    
    static abstract class Number<T> extends Field<T>
    {
        public Number()
        {
            super(true);
        }
    }
    
    public static class Int32 extends Number<Integer>
    {

    }
    
    public static class UInt32 extends Number<Integer>
    {

    }
    
    public static class SInt32 extends Number<Integer>
    {

    }
    
    public static class Fixed32 extends Number<Integer>
    {

    }
    
    public static class SFixed32 extends Number<Integer>
    {

    }
    
    public static class Int64 extends Number<Long>
    {

    }
    
    public static class UInt64 extends Number<Long>
    {

    }
    
    public static class SInt64 extends Number<Long>
    {

    }
    
    public static class Fixed64 extends Number<Long>
    {
        
    }
    
    public static class SFixed64 extends Number<Long>
    {

    }
    
    public static class Float extends Number<Float>
    {

    }
    
    public static class Double extends Number<Double>
    {

    }
    
    public static class Bool extends Field<Boolean>
    {
        public Bool()
        {
            super(true);
        }
    }
    
    public static class String extends Field<java.lang.String>
    {
        public String()
        {
            super(false);
        }
    }
    
    public static class Bytes extends Field<byte[]>
    {
        public Bytes()
        {
            super(false);
        }
    }
    
    public static class Reference extends Field<Object>
    {
        java.lang.String refName, packageName;
        Message message;
        
        public Reference(java.lang.String packageName, java.lang.String refName, 
                Message message)
        {
            super();
            this.packageName = packageName;
            this.refName = refName;
            this.message = message;
        }
        
        public java.lang.String getRefName()
        {
            return refName;
        }
        
        public java.lang.String getPackageName()
        {
            return packageName;
        }
        
        public Message getMessage()
        {
            return message;
        }
    }

}
