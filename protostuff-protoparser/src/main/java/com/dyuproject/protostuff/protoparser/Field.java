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

import java.nio.ByteBuffer;

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
   
    
    java.lang.String name, camelCaseName;
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
    
    public java.lang.String getCamelCaseName()
    {
        if(camelCaseName==null)
            camelCaseName = ProtoUtil.toCamelCase(name).toString();
            
        return camelCaseName;
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
    
    public boolean isEnumField()
    {
        return EnumField.class.isAssignableFrom(getClass());
    }
    
    public boolean isMessageField()
    {
        return MessageField.class.isAssignableFrom(getClass());
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
    
    public abstract java.lang.String getJavaType();
    public abstract java.lang.String getDefaultValueAsString();
    
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
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Integer>" : "int";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            
            return defaultValue==null ? "0" : defaultValue.toString();
        }
    }
    
    public static class UInt32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Integer>" : "int";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0" : defaultValue.toString();
        }
    }
    
    public static class SInt32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Integer>" : "int";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0" : defaultValue.toString();
        }
    }
    
    public static class Fixed32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Integer>" : "int";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0" : defaultValue.toString();
        }
    }
    
    public static class SFixed32 extends Number<Integer>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Integer>" : "int";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0" : defaultValue.toString();
        }
    }
    
    public static class Int64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Long>" : "long";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0l" : defaultValue.toString() +"l";
        }
    }
    
    public static class UInt64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Long>" : "long";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0l" : defaultValue.toString()+"l";
        }
    }
    
    public static class SInt64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Long>" : "long";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0l" : defaultValue.toString()+"l";
        }
    }
    
    public static class Fixed64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Long>" : "long";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0l" : defaultValue.toString()+"l";
        }
    }
    
    public static class SFixed64 extends Number<Long>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Long>" : "long";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0l" : defaultValue.toString()+"l";
        }
    }
    
    public static class Float extends Number<java.lang.Float>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Float>" : "float";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0.0f" : defaultValue.toString()+"f";
        }
    }
    
    public static class Double extends Number<java.lang.Double>
    {
        public java.lang.String getJavaType()
        {
            return isRepeated() ? "List<Double>" : "double";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "0.0d" : defaultValue.toString()+"d";
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
            return isRepeated() ? "List<Boolean>" : "boolean";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            return defaultValue==null ? "false" : defaultValue.toString();
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
            return isRepeated() ? "List<String>" : "String";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            
            return defaultValue==null ? "null" : 
                "com.dyuproject.protostuff.Internal.stringDefaultValue(\"" + 
                    TextFormat.escapeText(getDefaultValue()) + "\")";
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
            return isRepeated() ? "List<ByteString>" : "ByteString";
        }
        public java.lang.String getDefaultValueAsString()
        {
            if(isRepeated())
                return "null";
            
            return defaultValue==null ? "null" : 
                "com.dyuproject.protostuff.Internal.bytesDefaultValue(\"" + 
                    TextFormat.escapeBytes(ByteBuffer.wrap(getDefaultValue())) + "\")";
        }
    }
    
    public static class Reference extends Field<Object>
    {
        java.lang.String refName, packageName;
        Message message;
        
        public Reference(java.lang.String packageName, java.lang.String refName, 
                Message message)
        {
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
        
        public java.lang.String getJavaType()
        {
            return refName;
        }
        
        public java.lang.String getDefaultValueAsString()
        {
            return "null";
        }
    }

}
