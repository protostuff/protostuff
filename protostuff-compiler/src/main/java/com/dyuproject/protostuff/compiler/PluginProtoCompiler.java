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
import com.dyuproject.protostuff.parser.ProtoUtil;

/**
 * A plugin proto compiler whose output relies on the 'output' param configured 
 * in {@link ProtoModule}. The output param should point to a StringTemplate resource 
 * (file, url, or from classpath). 
 *
 * @author David Yu
 * @created May 25, 2010
 */
public class PluginProtoCompiler extends STCodeGenerator
{
    
    private final ProtoModule module;
    private final StringTemplateGroup group;
    private final boolean protoBlock, javaOutput;
    private final String fileExtension;

    public PluginProtoCompiler(ProtoModule module)
    {
        super(module.getOutput());
        
        group = resolveSTG(module);
        
        this.module = module;
        boolean protoBlock = false;
        try
        {
            protoBlock = group.lookupTemplate("proto_block") != null;
        }
        catch(IllegalArgumentException e)
        {
            protoBlock = false;
        }
        this.protoBlock = protoBlock;
        fileExtension = getFileExtension(module.getOutput());
        javaOutput = ".java".equalsIgnoreCase(fileExtension);
    }
    
    static String getFileExtension(String resource)
    {
        // E.g uf foo.bar.java.stg, it is the . before "java" 
        int secondToTheLastDot = resource.lastIndexOf('.', resource.length()-5);
        if(secondToTheLastDot == -1)
        {
            throw new IllegalArgumentException("The resource must be named like: 'foo.type.stg' " +
            		"where '.type' will be the file extension of the output files.");
        }
        String extension = resource.substring(secondToTheLastDot, resource.length()-4);
        // to protected against resources like "foo..stg"
        if(extension.length() < 2)
        {
            throw new IllegalArgumentException("The resource must be named like: 'foo.type.stg' " +
                        "where '.type' will be the file extension of the output files.");
        }
        
        return extension;
    }
    
    static StringTemplateGroup resolveSTG(ProtoModule module)
    {
        String resource = module.getOutput();
        try
        {
            File file = new File(resource);
            if(file.exists())
                return new StringTemplateGroup(new BufferedReader(new FileReader(file)));
            
            URL url = DefaultProtoLoader.getResource(resource, PluginProtoCompiler.class);
            if(url != null)
            {
                return new StringTemplateGroup(new BufferedReader(
                        new InputStreamReader(url.openStream(), "UTF-8")));
            }
            if(resource.startsWith("http://"))
            {
                return new StringTemplateGroup(new BufferedReader(
                        new InputStreamReader(new URL(resource).openStream(), "UTF-8")));
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Could not find " + resource);
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
        
        boolean hasEnumBlock = group.lookupTemplate("enum_block")!=null;
        boolean hasMessageBlock = group.lookupTemplate("message_block")!=null;
        
        String packageName = javaOutput ? proto.getJavaPackageName() : proto.getPackageName();
        
        if(hasEnumBlock)
        {
            for(EnumGroup eg : proto.getEnumGroups())
            {
                String fileName = eg.getName() + fileExtension;
                
                Writer writer = CompilerUtil.newWriter(module, packageName, fileName);
                AutoIndentWriter out = new AutoIndentWriter(writer);
                
                StringTemplate enumBlock = group.getInstanceOf("enum_block");
                enumBlock.setAttribute("eg", eg);
                enumBlock.setAttribute("module", module);
                enumBlock.setAttribute("options", module.getOptions());

                enumBlock.write(out);
                writer.close();
            }
        }

        if(hasMessageBlock)
        {
            for(Message m : proto.getMessages())
            {
                String fileName = m.getName() + fileExtension;
                
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
        
        if(!hasEnumBlock && !hasMessageBlock)
        {
            throw new IllegalStateException("At least one of these templates(proto_block| " +
            		"message_block|enum_block)need to be declared in the .stg resource.");
        }
    }
    
    protected void compileProtoBlock(ProtoModule module, Proto proto) throws IOException
    {
        String packageName = javaOutput ? proto.getJavaPackageName() : proto.getPackageName();

        String name = ProtoUtil.toPascalCase(proto.getFile().getName().replaceAll(".proto", 
                "")).toString();
        
        if(javaOutput)
        {
            String outerClassname = proto.getExtraOption("java_outer_classname");
            if(outerClassname != null)
                name = outerClassname;
        }
        
        String fileName = name + fileExtension;
        
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
