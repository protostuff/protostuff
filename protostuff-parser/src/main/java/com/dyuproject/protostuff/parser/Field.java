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

import java.nio.ByteBuffer;

/**
 * Base class for fields defined in a {@link Message}.
 *
 * @author David Yu
 * @created Dec 19, 2009
 */
public abstract class Field<T> extends AnnotationContainer implements Comparable<Field<?>>, HasName
{
    
    public enum Modifier
    {
        OPTIONAL,
        REQUIRED,
        REPEATED;
        
        public java.lang.String getName()
        {
            return name();
        }
    }
   
    
    java.lang.String name, defaultValueConstant;
    int number;
    Modifier modifier;
    boolean packable;
    T defaultValue;
    Message owner;
    
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
    
    public java.lang.String getDefaultValueConstant()
    {
        return defaultValueConstant;
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
    
    public boolean isRepeated()
    {
        return modifier == Modifier.REPEATED;
    }
    
    public boolean isRequired()
    {
        return modifier == Modifier.REQUIRED;
    }
    
    public boolean isOptional()
    {
        return modifier == Modifier.OPTIONAL;
    }
    
    public boolean isNumberField()
    {
        return Number.class.isAssignableFrom(getClass());
    }
    
    public boolean isEnumField()
    {
        return EnumField.class.isAssignableFrom(getClass());
    }
    
    public boolean isMessageField()
    {
        return MessageField.class.isAssignableFrom(getClass());
    }
    
    public boolean isBytesField()
    {
        return Bytes.class.isAssignableFrom(getClass());
    }
    
    public boolean isStringField()
    {
        return String.class.isAssignableFrom(getClass());
    }
    
    public boolean isDelimited()
    {
        return false;
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
    
    public abstract java.lang.String getJavaType();
    
    public java.lang.String getDefaultValueAsString()
    {
        return getDefaultValue().toString();
    }
    
    public Message getOwner()
    {
        return owner;
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
        public java.lang.String getJavaType()
        {
            return "int";
        }
    }
    
    public static class UInt32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return "int";
        }
    }
    
    public static class SInt32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return "int";
        }
    }
    
    public static class Fixed32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return "int";
        }
    }
    
    public static class SFixed32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return "int";
        }
    }
    
    public static class Int64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return "long";
        }
    }
    
    public static class UInt64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return "long";
        }
    }
    
    public static class SInt64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return "long";
        }
    }
    
    public static class Fixed64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return "long";
        }
    }
    
    public static class SFixed64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return "long";
        }
    }
    
    public static class Float extends Number<java.lang.Float>
    {
        public java.lang.String getJavaType()
        {
            return "float";
        }
    }
    
    public static class Double extends Number<java.lang.Double>
    {
        public java.lang.String getJavaType()
        {
            return "double";
        }
    }
    
    public static class Bool extends Field<Boolean>
    {
        public Bool()
        {
            super(true);
        }
        public java.lang.String getJavaType()
        {
            return "boolean";
        }
    }
    
    public static class String extends Field<java.lang.String>
    {
        public String()
        {
            super(false);
        }
        public java.lang.String getJavaType()
        {
            return "String";
        }
        public java.lang.String getDefaultValueAsString()
        {
            return TextFormat.escapeText(getDefaultValue());
        }
        public boolean isDelimited()
        {
            return true;
        }
    }
    
    public static class Bytes extends Field<byte[]>
    {
        public Bytes()
        {
            super(false);
        }
        public java.lang.String getJavaType()
        {
            return "ByteString";
        }
        public java.lang.String getDefaultValueAsString()
        {
            return TextFormat.escapeBytes(ByteBuffer.wrap(getDefaultValue())).toString();
        }
        public boolean isDelimited()
        {
            return true;
        }
    }
    
    public static class Reference extends Field<Object>
    {
        java.lang.String refName, packageName;
        HasFields hasFields;
        
        public Reference(java.lang.String packageName, java.lang.String refName, 
                HasFields hasFields)
        {
            this.packageName = packageName;
            this.refName = refName;
            this.hasFields = hasFields;
        }
        
        public java.lang.String getRefName()
        {
            return refName;
        }
        
        public java.lang.String getPackageName()
        {
            return packageName;
        }
        
        public java.lang.String getJavaType()
        {
            return refName;
        }
    }

}
