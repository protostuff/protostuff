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
import java.util.List;

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
    
    public static String toPascal(String name)
    {
        return PropertyAccessor.toPascalCase(name);
    }
    
    public static String toCamel(String name)
    {
        return PropertyAccessor.toCamelCase(name);
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
            return "generator.writeString(t.name());";
        
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
    
    public static List<Class<?>> getEnumClasses(Model<?> model)
    {
        ArrayList<Class<?>> enumClasses = new ArrayList<Class<?>>();
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
        
        return enumClasses;
    }

}
