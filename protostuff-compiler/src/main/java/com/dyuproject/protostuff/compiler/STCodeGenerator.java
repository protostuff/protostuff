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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.CommonGroupLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateErrorListener;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.StringTemplateGroupLoader;

import com.dyuproject.protostuff.parser.Formatter;
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
    
    public static final String TEMPLATE_BASE = "com/dyuproject/protostuff/compiler";
    
    public static final Pattern FORMAT_DELIM = Pattern.compile("&&");
    
    static final ConcurrentHashMap<Class<?>, AttributeRenderer> DEFAULT_RENDERERS = 
            new ConcurrentHashMap<Class<?>, AttributeRenderer>();
    
    static final ConcurrentHashMap<String, Formatter> DEFAULT_FORMATTERS = 
            new ConcurrentHashMap<String, Formatter>();
    
    public static final CommonGroupLoader GROUP_LOADER = new CommonGroupLoader(
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
                
                String[] formats = FORMAT_DELIM.split(formatName);
                
                return formats.length == 0 ? format(str, formatName) : 
                    chainedFormat(str, formats);
            }
        });
        
        GROUP_LOADER.loadGroup("base").setAttributeRenderers(DEFAULT_RENDERERS);
        
        Formatter.BUILTIN.addAllTo(DEFAULT_FORMATTERS);
    }
    
    /**
     * Formats the string n times.
     * 
     * <pre>
     * For example:
     * input = "some_foo"
     * formatters = ["PCS", "UPPER"]
     * 
     * Output:
     * 1st pass: "Some Foo"
     * 2nd pass: "SOME FOO"
     * </pre>
     */
    public static String chainedFormat(String str, String[] formats)
    {
        // chained formatting
        String formatted = str;
        for(String f : formats)
            formatted = format(formatted, f);
        
        return formatted;
    }
    
    /**
     * Formats the string {@code str} using the format {@code formatName}.
     * 
     * If the formatter with the name does not exist, the input string will be 
     * appended with the formatName.
     */
    public static String format(String str, String formatName)
    {
        final Formatter formatter = DEFAULT_FORMATTERS.get(formatName);
        if(formatter != null)
            return formatter.format(str);
        
        // regex replace
        int eq = formatName.indexOf("==");
        if(eq > 0)
        {
            String toReplace = formatName.substring(0, eq);
            String replacement = formatName.substring(eq+2);
            
            if(toReplace.length()==1 && replacement.length()==1)
                return str.replace(toReplace.charAt(0), replacement.charAt(0));

            return str.replaceAll(toReplace, replacement);
        }
        
        return str + formatName;
    }
    
    /**
     * Returns true if there was no previous attribute renderer with the same class.
     */
    public static boolean setAttributeRenderer(Class<?> typeClass, AttributeRenderer ar)
    {
        return null == DEFAULT_RENDERERS.put(typeClass, ar);
    }
    
    /**
     * Returns true if there was no previous formatter with the same name.
     */
    public static boolean setFormatter(String name, Formatter f)
    {
        return null == DEFAULT_FORMATTERS.put(name, f);
    }
     
    public static StringTemplateGroup getSTG(String groupName)
    {
        return __loader.loadGroup(groupName);
    }

    public static StringTemplate getST(String groupName, String name)
    {
        return getSTG(groupName).getInstanceOf(name);
    }
    
    public static void setGroupLoader(StringTemplateGroupLoader loader)
    {
        if(loader != null)
        {
            __loader = loader;
            StringTemplateGroup.registerGroupLoader(loader);
        }
    }
    
    private static StringTemplateGroupLoader __loader = GROUP_LOADER;
    
    protected final String id;
    
    public STCodeGenerator(String id)
    {
        this.id = id;
    }

    
    public String getOutputId()
    {
        return id;
    }
    
    public void compile(ProtoModule module) throws IOException
    {
        String ci = module.getOption("compile_imports");
        boolean compileImports = ci != null && !"false".equalsIgnoreCase(ci);
        boolean recursive = "recursive".equalsIgnoreCase(ci);
        
        File source = module.getSource();
        if(source.isDirectory())
        {
            for(File f : CompilerUtil.getProtoFiles(source))
                compile(module, parseProto(f, module), compileImports, recursive);
        }
        else
            compile(module, parseProto(source, module), compileImports, recursive);
    }
    
    protected static Proto parseProto(File file, ProtoModule module)
    {
        CachingProtoLoader loader = module.getCachingProtoLoader();
        if(loader == null)
            return ProtoUtil.parseProto(file);

        try
        {
            return loader.loadFrom(file, null);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    protected void compile(ProtoModule module, Proto proto, boolean compileImports, 
            boolean recursive) throws IOException
    {
        final List<Proto> overridden = new ArrayList<Proto>();
        try
        {
            collect(module, proto, overridden, recursive);
            
            if(!recursive)
            {
                compile(module, proto);
                if(compileImports)
                {
                    for(Proto p : proto.getImportedProtos())
                        compile(module, p);
                }
            }
        }
        finally
        {
            for(Proto p : overridden)
                postCompile(module, p);
        }
    }
    
    protected void collect(ProtoModule module, Proto proto, 
            List<Proto> overridden, boolean compile) throws IOException
    {
        for(Proto p : proto.getImportedProtos())
            collect(module, p, overridden, compile);
        
        if(override(module, proto))
            overridden.add(proto);
        
        if(compile)
            compile(module, proto);
    }
    
    protected static boolean override(ProtoModule module, Proto proto)
    {
        String pkg = proto.getPackageName();
        String jpkg = proto.getJavaPackageName();
        String opkg = module.getOption(pkg);
        String ojpkg = module.getOption(jpkg);
        boolean override = false;
        if(opkg != null && opkg.length() != 0)
        {
            proto.getMutablePackageName().override(opkg);
            override = true;
        }
        if(ojpkg != null && ojpkg.length() != 0)
        {
            proto.getMutableJavaPackageName().override(ojpkg);
            override = true;
        }
        return override;
    }
    
    protected static void postCompile(ProtoModule module, Proto proto)
    {
        proto.getMutableJavaPackageName().reset();
        proto.getMutablePackageName().reset();
    }
    
    protected abstract void compile(ProtoModule module, Proto proto) throws IOException;

}
