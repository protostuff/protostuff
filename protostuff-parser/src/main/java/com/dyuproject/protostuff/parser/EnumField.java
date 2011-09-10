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




/**
 * Represents an enum field defined in a {@link Message}.
 *
 * @author David Yu
 * @created Dec 19, 2009
 */
public class EnumField extends Field<EnumGroup.Value>
{
    
    java.lang.String javaType;
    EnumGroup enumGroup;
    boolean defaultValueSet;
    
    public EnumField()
    {
        super(true);
    }

    public EnumField(EnumGroup enumGroup)
    {
        this();
        this.enumGroup = enumGroup;
    }
    
    public boolean isDefaultValueSet()
    {
        return defaultValueSet;
    }
    
    public EnumGroup getEnumGroup()
    {
        return enumGroup;
    }
    
    public java.lang.String getJavaType()
    {
        if(javaType!=null)
            return javaType;
        
        StringBuilder buffer = new StringBuilder();
        if(enumGroup.isNested())
        {
            if(enumGroup.parentMessage==owner)
                buffer.append(enumGroup.name);
            else
            {
                Message.computeName(enumGroup.parentMessage, owner, buffer);
                buffer.append('.').append(enumGroup.name);
            }
        }
        else if(enumGroup.getProto().getJavaPackageName().equals(owner.getProto().getJavaPackageName()))
            buffer.append(enumGroup.name);
        else
            buffer.append(enumGroup.getProto().getJavaPackageName()).append('.').append(enumGroup.getName());
        
        return (javaType=buffer.toString());
        
    }
    
    public java.lang.String getRegularType()
    {
        java.lang.String javaType = getJavaType();
        Proto egProto = enumGroup.getProto();
        java.lang.String javaPackage = egProto.getJavaPackageName();
        java.lang.String protoPackage = egProto.getPackageName();
        if(javaType.startsWith(javaPackage) && !javaPackage.equals(protoPackage))
            return javaType.replace(javaPackage, protoPackage);
        
        return javaType;
    }
    
    public java.lang.String getDefaultValueAsString()
    {
        return getJavaType() + "." + getDefaultValue().getName();
    }
    
    public boolean isSamePackage()
    {
        return getOwner().getProto() == getEnumGroup().getProto();
    }
    
    public java.lang.String getRelativePath()
    {
        if(isSamePackage())
            return "";
        
        java.lang.String currentPackage = getOwner().getProto().getPackageName();
        java.lang.String targetPackage = getEnumGroup().getProto().getPackageName();
        java.lang.String path = "../";
        for(int idx=currentPackage.indexOf('.'); idx!=-1; idx=currentPackage.indexOf('.', idx+1))
            path += "../";

        return path + targetPackage.replace('.', '/') + "/";
    }
}
