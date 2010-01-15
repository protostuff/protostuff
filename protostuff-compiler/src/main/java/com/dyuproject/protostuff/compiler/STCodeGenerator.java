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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.CommonGroupLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateErrorListener;
import org.antlr.stringtemplate.StringTemplateGroup;

import com.dyuproject.protostuff.parser.Proto;
import com.dyuproject.protostuff.parser.ProtoUtil;

/**
 * Base class for code generators using StringTemplate.
 *
 * @author David Yu
 * @created Jan 5, 2010
 */
public abstract class STCodeGenerator implements ProtoCompiler
{
    
    static final String TEMPLATE_BASE = "com/dyuproject/protostuff/compiler";
    
    static final ConcurrentHashMap<Class<?>, AttributeRenderer> DEFAULT_RENDERERS = 
        new ConcurrentHashMap<Class<?>, AttributeRenderer>();
    
    static final CommonGroupLoader GROUP_LOADER = new CommonGroupLoader(
            TEMPLATE_BASE, new StringTemplateErrorListener()
    {
        public void error(String msg, Throwable e)
        {
            System.err.println("error: " + msg);
        }
        public void warning(String msg)
        {
            System.err.println("warning: " + msg);
        }
    });
    
    static
    {
        StringTemplateGroup.registerGroupLoader(GROUP_LOADER);
        
        // attribute renderers
        
        setAttributeRenderer(String.class, new AttributeRenderer(){
            public String toString(Object o)
            {
                return (String)o;
            }

            public String toString(Object o, String formatName)
            {
                String str = (String)o;
                if(formatName==null)
                    return str;
                
                if("CC".equals(formatName))
                    return ProtoUtil.toCamelCase(str).toString();
                
                if("PC".equals(formatName))
                    return ProtoUtil.toPascalCase(str).toString();
                
                if("UC".equals(formatName))
                    return ProtoUtil.toUnderscoreCase(str).toString();
                
                if("UUC".equals(formatName))
                    return ProtoUtil.toUnderscoreCase(str).toString().toUpperCase();
                
                return str + formatName;
            }
        });
        
        GROUP_LOADER.loadGroup("base").setAttributeRenderers(DEFAULT_RENDERERS);
    }
    
    public static void setAttributeRenderer(Class<?> typeClass, AttributeRenderer ar)
    {
        DEFAULT_RENDERERS.put(typeClass, ar);
    }
     
    public static StringTemplateGroup getSTG(String groupName)
    {
        return GROUP_LOADER.loadGroup(groupName);
    }

    public static StringTemplate getST(String groupName, String name)
    {
        return getSTG(groupName).getInstanceOf(name);
    }
    
    protected final String id;
    
    public STCodeGenerator(String id)
    {
        this.id = id;
    }
    
    public STCodeGenerator(String id, String groupName)
    {
        this.id = id;
    }
    
    public String getOutputId()
    {
        return id;
    }
    
    public void compile(ProtoModule module) throws IOException
    {
        boolean compileImports = module.getOption("compile_imports")!=null;
        File source = module.getSource();
        if(source.isDirectory())
        {
            for(File f : CompilerUtil.getProtoFiles(source))
            {
                Proto proto = ProtoUtil.parseProto(f);
                compile(module, proto);
                if(compileImports)
                {
                    for(Proto p : proto.getImportedProtos())
                        compile(module, p);
                }
            }
        }
        else
        {
            Proto proto = ProtoUtil.parseProto(source);
            compile(module, proto);
            if(compileImports)
            {
                for(Proto p : proto.getImportedProtos())
                    compile(module, p);
            }
        }
    }
    
    protected abstract void compile(ProtoModule module, Proto proto) throws IOException;

}
