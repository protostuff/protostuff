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

/**
 * Represents an enum field defined in a {@link Message}.
 * 
 * @author David Yu
 * @created Dec 19, 2009
 */
public class EnumField extends Field<EnumGroup.Value>
{

    // java.lang.String javaType;
    final EnumGroup.Value ev;
    EnumGroup enumGroup;
    boolean defaultValueSet;

    public EnumField()
    {
        this((EnumGroup.Value) null);
    }

    public EnumField(EnumGroup enumGroup)
    {
        this((EnumGroup.Value) null);
        this.enumGroup = enumGroup;
    }

    public EnumField(EnumGroup.Value ev)
    {
        super(true);
        this.ev = ev;
    }

    public EnumGroup.Value getEv()
    {
        return ev;
    }

    @Override
    public void putExtraOption(java.lang.String key, Object value)
    {
        if (extraOptions.put(key, value) != null)
        {
            final Proto proto;
            final java.lang.String ofOwner;
            if (owner != null)
            {
                // message field
                proto = owner.getProto();
                ofOwner = " of " + owner.getRelativeName();
            }
            else if (ev != null)
            {
                // enum field/value
                proto = enumGroup.getProto();
                ofOwner = " of the enum " + enumGroup.getRelativeName();
            }
            else
            {
                proto = null;
                ofOwner = "";
            }

            throw err("The field " + name + ofOwner +
                    " contains a duplicate option: " + key, proto);
        }
    }

    @Override
    public boolean isDefaultValueSet()
    {
        return defaultValueSet;
    }

    public EnumGroup getEnumGroup()
    {
        return enumGroup;
    }

    @Override
    public java.lang.String getJavaType()
    {
        // if(javaType!=null)
        // return javaType;

        StringBuilder buffer = new StringBuilder();
        if (enumGroup.isNested())
        {
            if (enumGroup.parentMessage == owner)
                buffer.append(enumGroup.name);
            else
            {
                Message.computeName(enumGroup.parentMessage, owner, buffer);
                buffer.append('.').append(enumGroup.name);
            }
        }
        else if (enumGroup.getProto().getJavaPackageName().equals(owner.getProto().getJavaPackageName()))
            buffer.append(enumGroup.name);
        else
            buffer.append(enumGroup.getProto().getJavaPackageName()).append('.').append(enumGroup.getName());

        return buffer.toString();
        // return (javaType=buffer.toString());

    }

    public java.lang.String getRegularType()
    {
        java.lang.String javaType = getJavaType();
        Proto egProto = enumGroup.getProto();
        java.lang.String javaPackage = egProto.getJavaPackageName();
        java.lang.String protoPackage = egProto.getPackageName();
        if (javaType.startsWith(javaPackage) && !javaPackage.equals(protoPackage))
            return javaType.replace(javaPackage, protoPackage);

        return javaType;
    }

    @Override
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
        if (isSamePackage())
            return "";

        java.lang.String currentPackage = getOwner().getProto().getPackageName();
        java.lang.String targetPackage = getEnumGroup().getProto().getPackageName();
        java.lang.String path = "../";
        for (int idx = currentPackage.indexOf('.'); idx != -1; idx = currentPackage.indexOf('.', idx + 1))
            path += "../";

        return path + targetPackage.replace('.', '/') + "/";
    }

    @Override
    public Proto getProto()
    {
        return ev != null ? ev.getProto() : super.getProto();
    }

    @Override
    public java.lang.String getProtoType()
    {
        return getRegularType();
    }
}
