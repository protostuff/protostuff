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

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Contains the metadata of parsed protos.
 * Basically repesents the .proto file.
 *
 * @author David Yu
 * @created Dec 18, 2009
 */
public class Proto
{
    
    final File file;
    final Loader loader;
    final Proto importer;
    private Mutable<String> packageName, javaPackageName;
    final LinkedHashMap<String, Proto> importedProtos = new LinkedHashMap<String, Proto>();
    final LinkedHashMap<String,String> standardOptions = new LinkedHashMap<String,String>(5);
    final LinkedHashMap<String,String> extraOptions = new LinkedHashMap<String,String>(5);
    final LinkedHashMap<String, Message> messages = new LinkedHashMap<String, Message>();
    final LinkedHashMap<String, EnumGroup> enumGroups = new LinkedHashMap<String, EnumGroup>();
    
    public Proto()
    {
        this(null, DefaultProtoLoader.DEFAULT_INSTANCE, null);
    }
    
    public Proto(File file)
    {
        this(file, DefaultProtoLoader.DEFAULT_INSTANCE, null);
    }
    
    public Proto(Loader loader)
    {
        this(null, loader, null);
    }
    
    public Proto(File file, Loader loader)
    {
        this(file, loader, null);
    }
    
    public Proto(File file, Loader loader, Proto importer)
    {
        this.file = file;
        this.loader = loader;
        this.importer = importer;
    }
    
    public File getFile()
    {
        return file;
    }
    
    public Mutable<String> getMutablePackageName()
    {
        return packageName;
    }
    
    public String getPackageName()
    {
        return packageName == null ? null : packageName.getValue();
    }
    
    public Mutable<String> getMutableJavaPackageName()
    {
        return javaPackageName;
    }
    
    public String getJavaPackageName()
    {
        return javaPackageName.getValue();
    }
    
    void setPackageName(String packageName)
    {
        if(this.packageName == null)
            this.packageName = new Mutable<String>(packageName);
    }
    
    public LinkedHashMap<String,String> getExtraOptions()
    {
        return extraOptions;
    }
    
    public String getExtraOption(String name)
    {
        return extraOptions.get(name);
    }
    
    public Collection<Message> getMessages()
    {
        return messages.values();
    }
    
    public Message getMessage(String name)
    {
        return messages.get(name);
    }
    
    void addMessage(Message message)
    {
        messages.put(message.name, message);
        message.proto = this;
    }
    
    public Collection<EnumGroup> getEnumGroups()
    {
        return enumGroups.values();
    }
    
    public EnumGroup getEnumGroup(String name)
    {
        return enumGroups.get(name);
    }
    
    void addEnumGroup(EnumGroup enumGroup)
    {
        enumGroups.put(enumGroup.name, enumGroup);
        enumGroup.proto = this;
    }
    
    public Collection<Proto> getImportedProtos()
    {
        return importedProtos.values();
    }
    
    public Proto getImportedProto(String packageName)
    {
        return importedProtos.get(packageName);
    }
    
    void importProto(String path)
    {
        try
        {
            addImportedProto(loader.load(path, this));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    void addImportedProto(Proto proto)
    {
        importedProtos.put(proto.packageName.getValue(), proto);
    }
    
    void postParse()
    {
        if(packageName == null)
            throw new IllegalStateException("proto package not defined.");
        
        String javaPkg = extraOptions.get("java_package");
        String javaPackageName = javaPkg==null || javaPkg.length()==0 ? 
                packageName.getValue() : javaPkg;
        this.javaPackageName = new Mutable<String>(javaPackageName);
        
        for(Message m : getMessages())
            m.resolveReferences(m);
    }
    
    public String toString()
    {
        return new StringBuilder()
            .append('{')
            .append("packageName:").append(packageName)
            .append(',').append("standardOptions:").append(standardOptions)
            .append(',').append("extraOptions:").append(extraOptions)
            .append(',').append("messages:").append(getMessages())
            .append('}')
            .toString();
    }
    
    public interface Loader
    {
        public Proto load(String path, Proto importer) throws Exception;
    }

}
