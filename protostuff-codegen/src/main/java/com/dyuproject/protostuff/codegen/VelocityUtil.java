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

package com.dyuproject.protostuff.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import com.dyuproject.protostuff.model.Model;
import com.dyuproject.protostuff.model.Property;
import com.dyuproject.protostuff.model.PropertyAccessor;
import com.dyuproject.protostuff.model.PropertyMeta;
import com.google.protobuf.ByteString;

/**
 * @author David Yu
 * @created Oct 15, 2009
 */

public final class VelocityUtil
{
    
    static final String CAMEL = "camel", PASCAL = "pascal", UNDERSCORE = "underscore";
    static final String FIELD_CASE = "fieldCase";
    static final String METHOD_CASE = "methodCase";
    
    public static String toPascal(String name)
    {
        return PropertyAccessor.toPascalCase(name);
    }
    
    public static String toCamel(String name)
    {
        return PropertyAccessor.toCamelCase(name);
    }
    
    public static String toUnderscore(String camelCase)
    {
        StringBuilder buffer = new StringBuilder();
        for(char c : camelCase.toCharArray())
        {
            if(c>64 && c<91)
            {
                buffer.append('_');
                buffer.append((char)(c+32));
            }
            else
                buffer.append(c);
        }
        return buffer.toString();
    }
    
    public static String getDerivedPropName(Module module, PropertyMeta meta)
    {
        String fieldCase = module.getOption(FIELD_CASE);
        if(PASCAL.equals(fieldCase))
            return toPascal(meta.getName());
        if(UNDERSCORE.equals(fieldCase))
            return toUnderscore(meta.getName());
        
        // default camel
        return meta.getName();
    }
    
    public static String getDerivedArgName(Module module, PropertyMeta meta)
    {
        String fieldCase = module.getOption(FIELD_CASE);
        if(UNDERSCORE.equals(fieldCase))
            return toUnderscore(meta.getName());
        
        // default camel ... pascal can't be used because it conflicts with classes
        return meta.getName();
    }
    
    public static String getDerivedMethodName(Module module, String camelCase)
    {
        String methodCase = module.getOption(METHOD_CASE);
        if(UNDERSCORE.equals(methodCase))
            return toUnderscore(camelCase);
        
        if(PASCAL.equals(methodCase))
            return toPascal(camelCase);
        
        // default camel
        return camelCase;
    }
    
    public static String getDerivedMethodName(Module module, String camelPrefix, 
            PropertyMeta meta)
    {
        String methodCase = module.getOption(METHOD_CASE);
        if(UNDERSCORE.equals(methodCase))
        {
            return new StringBuilder()
                .append(toUnderscore(camelPrefix))
                .append('_')
                .append(toUnderscore(meta.getName()))
                .toString();
        }
        if(PASCAL.equals(methodCase))
        {
            return new StringBuilder()
                .append(toPascal(camelPrefix))
                .append(toPascal(meta.getName()))
                .toString();
        }
        
        // default camel
        return new StringBuilder()
            .append(camelPrefix)
            .append(toPascal(meta.getName()))
            .toString();
    }
    
    public static String getDerivedMethodName(Module module, String camelPrefix, 
            PropertyMeta meta, String pascalSuffix)
    {
        String methodCase = module.getOption(METHOD_CASE);
        if(UNDERSCORE.equals(methodCase))
        {
            return new StringBuilder()
                .append(toUnderscore(camelPrefix))
                .append('_')
                .append(toUnderscore(meta.getName()))
                .append(toUnderscore(pascalSuffix))
                .toString();
        }
        if(PASCAL.equals(methodCase))
        {
            return new StringBuilder()
                .append(toPascal(camelPrefix))
                .append(toPascal(meta.getName()))
                .append(pascalSuffix)
                .toString();
        }
        
        // default camel
        return new StringBuilder()
            .append(camelPrefix)
            .append(toPascal(meta.getName()))
            .append(pascalSuffix).toString();
    }
    
    public static String printRGen(PropertyMeta meta)
    {
        Class<?> type = meta.getComponentTypeClass();
        
        if(meta.isMessage())
        {
            return new StringBuilder()
                .append("CONVERTOR_")
                .append(toPascal(type.getSimpleName()))
                .append(".generateTo(generator, t);")
                .toString();
        }

        if(ByteString.class==type)
            return "generator.writeBinary(t.toByteArray());";
        
        if(type.isEnum())
            return "generator.writeNumber(t.getNumber());";
        
        if(String.class==type)
            return "generator.writeString(t);";

        if(boolean.class==type)
            return "generator.writeBoolean(t);";
        
        return "generator.writeNumber(t);";
    }
    
    public static String printNumberParse(Class<?> type)
    {
        if(int.class==type)
            return "parser.getIntValue()";
        
        if(long.class==type)
            return "parser.getLongValue()";
        
        if(float.class==type)
            return "parser.getFloatValue()";
        
        if(double.class==type)
            return "parser.getDoubleValue()";
        
        throw new IllegalStateException();
    }
    
    public static ArrayList<Class<?>> getEnumClasses(Model<?> model)
    {
        HashSet<Class<?>> enumClasses = new HashSet<Class<?>>();
        for(Property p : model.getProperties())
        {
            PropertyMeta pm = p.getPropertyMeta();
            if(pm.isRepeated())
            {
                if(pm.getComponentTypeClass().isEnum())
                    enumClasses.add(pm.getComponentTypeClass());
            }
            else if(pm.getTypeClass().isEnum())
                enumClasses.add(pm.getTypeClass());
        }
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>(enumClasses);
        Collections.sort(classes, new Comparator<Class<?>>(){
            public int compare(Class<?> c, Class<?> c2)
            {                
                return c.getSimpleName().compareTo(c2.getSimpleName());
            }            
        });
        
        return classes;
    }
    
    public static int getLastIndexOfRepeated(Model<?> model)
    {
        Property[] props = model.getProperties();
        for(int i=props.length;i-->0;)
        {
            if(props[i].getPropertyMeta().isRepeated())
                return i;
        }
        return -1;
    }
    
    public static String getOption(Module module, String key)
    {
        return module.getOptions().getProperty(key, "");
    }
    
    public static String getOption(Module module, String prefix, String key)
    {
        return module.getOptions().getProperty(prefix+key, "");
    }
    
    public static String getOption(Module module, String prefix, String key, String append)
    {
        String value = module.getOption(prefix+key);
        return value==null ? "" : value + append;
    }

}
