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

import java.io.IOException;
import java.io.Writer;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import com.dyuproject.protostuff.parser.EnumGroup;
import com.dyuproject.protostuff.parser.Message;
import com.dyuproject.protostuff.parser.Proto;

/**
 * Compiles proto files to protobuf java messages (pojos).
 *
 * @author David Yu
 * @created Jan 4, 2010
 */
public class ProtoToJavaBeanCompiler extends STCodeGenerator
{
    
    public ProtoToJavaBeanCompiler()
    {
        super("java_bean");
    }
    
    public void compile(ProtoModule module, Proto proto) throws IOException
    {
        String javaPackageName = proto.getJavaPackageName();
        String template = module.getOption("separate_schema")==null ? 
                "java_bean" : "java_bean_separate_schema";
        StringTemplateGroup group = getSTG(template);
        
        for(EnumGroup eg : proto.getEnumGroups())
        {
            Writer writer = CompilerUtil.newWriter(module, 
                    javaPackageName, eg.getName()+".java");
            AutoIndentWriter out = new AutoIndentWriter(writer);
            
            StringTemplate enumBlock = group.getInstanceOf("enum_block");
            enumBlock.setAttribute("includeHeader", Boolean.TRUE);
            enumBlock.setAttribute("eg", eg);
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
            
            Writer writer = CompilerUtil.newWriter(module, 
                    javaPackageName, m.getName()+".java");
            AutoIndentWriter out = new AutoIndentWriter(writer);
            
            StringTemplate messageBlock = group.getInstanceOf("message_block");
            messageBlock.setAttribute("includeHeader", Boolean.TRUE);
            messageBlock.setAttribute("message", m);
            messageBlock.setAttribute("options", module.getOptions());
            messageBlock.write(out);
            writer.close();
        }
    }

}
