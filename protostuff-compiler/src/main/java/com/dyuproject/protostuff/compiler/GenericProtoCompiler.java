//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import com.dyuproject.protostuff.parser.DefaultProtoLoader;
import com.dyuproject.protostuff.parser.EnumGroup;
import com.dyuproject.protostuff.parser.Message;
import com.dyuproject.protostuff.parser.Proto;

/**
 * A generic compiler whose output relies on the output param configured in {@link ProtoModule}.
 * The output param is a StringTemplate resource (file, url, or from classpath). 
 *
 * @author David Yu
 * @created May 25, 2010
 */
public class GenericProtoCompiler extends STCodeGenerator
{
    
    /**
     * The files will be written to the dirs translated from the java package.
     * By default, the proto's package name will be used.
     * If the java_outer_classname is defined, the file name will be derived from that.
     * By default, the proto file's name (pascal-case) will be used.
     */
    public static final String OPTION_JAVA_OUTPUT = "java_output";
    /**
     * If the source is descriptor.proto and the file_prefix is "foo", 
     * the file will be written as "foodescriptor"
     */
    public static final String OPTION_FILE_PREFIX = "file_prefix";
    /**
     * If the source is descriptor.proto and the file_suffix is ".txt", 
     * the file will be written as "descriptor.txt"
     */
    public static final String OPTION_FILE_SUFFIX = "file_suffix";
    /**
     * If the source is some_descriptor.proto and the name_rewrite is "PC", 
     * the file will be written as "SomeDescriptor"
     * A choice of pascal-case "PC" (SomeDescriptor), 
     * camel-case "CC" (someDescriptor), 
     * underscore-case "UC" (some_descriptor)
     * and uppercased-underscore-case "UUC" (SOME_DESCRIPTOR)
     */
    public static final String OPTION_NAME_REWRITE = "name_rewrite";
    
    private final ProtoModule module;
    private final StringTemplateGroup group;
    private final boolean protoBlock, javaOutput;

    public GenericProtoCompiler(ProtoModule module)
    {
        super(module.getOutput());
        
        group = resolveSTG(module);
        
        this.module = module;
        
        javaOutput = module.getOption(OPTION_JAVA_OUTPUT) != null;
        protoBlock = group.lookupTemplate("proto_block") != null;
    }
    
    static StringTemplateGroup resolveSTG(ProtoModule module)
    {
        String resource = module.getOutput();
        try
        {
            File file = new File(resource);
            if(file.exists())
                return new StringTemplateGroup(new BufferedReader(new FileReader(file)));
            
            URL url = DefaultProtoLoader.getResource(resource, GenericProtoCompiler.class);
            if(url != null)
            {
                return new StringTemplateGroup(new BufferedReader(
                        new InputStreamReader(url.openStream(), "UTF-8")));
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Could not find " + resource);
    }
    
    protected String resolveFileName(String name, ProtoModule module, Proto proto)
    {
        if(javaOutput && protoBlock)
        {
            String outerClassname = proto.getExtraOption("java_outer_classname");
            if(outerClassname != null)
                name = outerClassname;
        }
        else if(name.endsWith(".proto"))
            name = name.substring(0, name.length()-6);
        
        String format = module.getOption(OPTION_NAME_REWRITE);
        
        if(format != null)
            name = DEFAULT_RENDERERS.get(String.class).toString(name, format);
        
        String prefix = module.getOption(OPTION_FILE_PREFIX);
        if(prefix != null)
            name = prefix + name;
        
        String suffix = module.getOption(OPTION_FILE_SUFFIX);
        if(suffix != null)
            name += suffix;
        
        return name;
    }

    protected void compile(ProtoModule module, Proto proto) throws IOException
    {
        if(this.module != module)
            throw new IllegalArgumentException("Wrong module.");
        
        if(protoBlock)
        {
            compileProtoBlock(module, proto);
            return;
        }
        
        String packageName = javaOutput ? proto.getJavaPackageName() : proto.getPackageName();
        String enumPrefix = module.getOption("enum_prefix");
        String enumSuffix = module.getOption("enum_suffix");
        
        for(EnumGroup eg : proto.getEnumGroups())
        {
            String fileName = resolveFileName(eg.getName(), module, proto);
            if(enumPrefix != null)
                fileName = enumPrefix + fileName;
            if(enumSuffix != null)
                fileName += enumSuffix;
            
            Writer writer = CompilerUtil.newWriter(module, packageName, fileName);
            AutoIndentWriter out = new AutoIndentWriter(writer);
            
            StringTemplate enumBlock = group.getInstanceOf("enum_block");
            enumBlock.setAttribute("eg", eg);
            enumBlock.setAttribute("module", module);
            enumBlock.setAttribute("options", module.getOptions());

            enumBlock.write(out);
            writer.close();
        }
        
        for(Message m : proto.getMessages())
        {
            // true if its a service message w/c isn't supported atm
            if(m.getFields().isEmpty())
            {
                System.err.println("ignoring empty message: " + m.getFullName());
                continue;
            }
            
            String fileName = resolveFileName(m.getName(), module, proto);
            
            Writer writer = CompilerUtil.newWriter(module, packageName, fileName);
            AutoIndentWriter out = new AutoIndentWriter(writer);
            
            StringTemplate messageBlock = group.getInstanceOf("message_block");
            messageBlock.setAttribute("message", m);
            messageBlock.setAttribute("module", module);
            messageBlock.setAttribute("options", module.getOptions());

            messageBlock.write(out);
            writer.close();
        }
        
    }
    
    protected void compileProtoBlock(ProtoModule module, Proto proto) throws IOException
    {
        String packageName = javaOutput ? proto.getJavaPackageName() : proto.getPackageName();
        
        String fileName = resolveFileName(proto.getFile().getName(), module, proto);
        
        Writer writer = CompilerUtil.newWriter(module, packageName, fileName);
        
        AutoIndentWriter out = new AutoIndentWriter(writer);
        StringTemplate protoOuterBlock = group.getInstanceOf("proto_block");
        
        protoOuterBlock.setAttribute("proto", proto);
        protoOuterBlock.setAttribute("module", module);
        protoOuterBlock.setAttribute("options", module.getOptions());
        
        protoOuterBlock.write(out);
        writer.close();
    }

}
