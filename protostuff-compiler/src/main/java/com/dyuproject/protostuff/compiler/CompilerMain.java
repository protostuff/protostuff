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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * The main execution point of compiling protos.
 *
 * @author David Yu
 * @created Jan 5, 2010
 */
public final class CompilerMain
{
    
    static final Pattern COMMA = Pattern.compile(",");
    
    private static final HashMap<String,ProtoCompiler> __compilers = 
        new HashMap<String,ProtoCompiler>();
    
    static
    {        
        addCompiler(new ProtoToJavaBeanCompiler());
        addCompiler(new ProtoToGwtOverlayCompiler());
        addCompiler(new ProtoToJavaV2ProtocSchemaCompiler());
    }
    
    public static void addCompiler(ProtoCompiler compiler)
    {
        __compilers.put(compiler.getOutputId(), compiler);
    }
    
    public static InputStream getInputStream(String resource) throws IOException
    {
        File file = new File(resource);
        return file.exists() ? new FileInputStream(file) : null;
    }
    
    public static List<ProtoModule> loadModules(InputStream in)
    {
        Properties props = new Properties();
        try
        {
            props.load(in);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        
        return loadModules(props);
    }
    
    public static List<ProtoModule> loadModules(Properties props)
    {
        String moduleString = props.getProperty("modules");
        if(moduleString==null || moduleString.length()==0)
        {
            propsErr();
            return null;
        }        
        
        ArrayList<ProtoModule> modules = new ArrayList<ProtoModule>();
        for(String m : COMMA.split(moduleString))
        {
            m = m.trim();
            String source = props.getProperty(m + ".source");
            if(source==null)
                throw new IllegalStateException(m + " must have a source");
            
            String output = props.getProperty(m + ".output");
            if(output==null)
                throw new IllegalStateException(m + " must have an output");
            
            String outputDir = props.getProperty(m + ".outputDir");
            if(outputDir==null)
                throw new IllegalStateException(m + " must have an outputDir");
            
            String encoding = props.getProperty(m + ".encoding");
            
            String options = props.getProperty(m + ".options");
            
            ProtoModule module = new ProtoModule(new File(source), output, 
                    encoding, new File(outputDir));
            
            if(options!=null)
            {
                for(String o : COMMA.split(options))
                {
                    int idx = o.indexOf(':');
                    if(idx==-1)
                        module.setOption(o.trim(), "");
                    else
                        module.setOption(o.substring(0, idx).trim(), o.substring(idx+1).trim());
                }
            }
            
            modules.add(module);
        }
        
        return modules;
    }
    
    static void propsErr()
    {
        System.err.println("\nError parsing the properties file ...");
        
        System.err.println("\nThe properties file would look like:");
        System.err.println("modules = foo");
        System.err.println("foo.source = path/to/your/proto/file/or/dir");
        System.err.println("foo.output = java_bean");
        System.err.println("foo.outputDir = src/main/java");
        System.err.println("foo.encoding = UTF-8");
        System.err.println("foo.options = key1:value1,key2:value2");
    }
    
    static void usage()
    {
        System.err.println("\nTo generate code for multiple modules:");
        System.err.println("  java -cp -jar protostuff-compiler.jar modules.properties");
        
        System.err.println("\nThe properties file would look like:\n");
        System.err.println("modules = foo");
        System.err.println("foo.source = path/to/your/proto/file/or/dir");
        System.err.println("foo.output = java_bean");
        System.err.println("foo.outputDir = src/main/java");
        System.err.println("foo.encoding = UTF-8");
        System.err.println("foo.options = key1:value1,key2:value2");
        
        System.err.println("\n===================================================\n");
        
        System.err.println("\nTo generate code for a single module, execute the jar without args and specify:");
        System.err.println("  -Dsource=path/to/your/proto/file/or/dir");
        System.err.println("  -Doutput=java_bean");
        System.err.println("  -DoutputDir=src/main/java");
        System.err.println("  -Dencoding=UTF-8");
        System.err.println("  -Doptions=key1:value1,key2:value2");
    }
    
    public static void compile(ProtoModule m) throws Exception
    {
        String options = m.getOptions().toString();
        for(String g : COMMA.split(m.getOutput()))
        {
            g = g.trim();
            ProtoCompiler cg = __compilers.get(g);
            if(cg==null)
                throw new IllegalStateException("unknown output: " + g);
            
            cg.compile(m);
            
            StringBuilder buffer = new StringBuilder()
                .append("Successfully compiled proto from ")
                .append(m.getSource())
                .append(" to output: ")
                .append(g);
            
            if(options.length()>2)
                buffer.append(' ').append(options);
            
            System.out.println(buffer.toString());
        }
    }
    
    public static void compile(List<ProtoModule> modules) throws Exception
    {
        for(ProtoModule m : modules)
            compile(m);
    }
    
    public static void main(String[] args) throws Exception
    {
        if(args.length==0)
        {
            Properties props = System.getProperties();            
            
            String source = props.getProperty("source");            
            String output = props.getProperty("output");
            String outputDir = props.getProperty("outputDir");
            String encoding = props.getProperty("encoding");
            String options = props.getProperty("options");
            
            if(source==null || output==null || outputDir==null)
                usage();
            else
            {
                ProtoModule module = new ProtoModule(new File(source), output, 
                        encoding, new File(outputDir));
                
                if(options!=null)
                {
                    for(String o : COMMA.split(options))
                    {
                        int idx = o.indexOf(':');
                        if(idx==-1)
                            module.setOption(o.trim(), "");
                        else
                            module.setOption(o.substring(0, idx).trim(), o.substring(idx+1).trim());
                    }
                }
                
                compile(module);
            }
            return;
        }
        
        for(String s : args)
        {
            InputStream in = getInputStream(s);
            if(in==null)
            {
                System.err.println(s + " does not exist");
                continue;
            }
            
            List<ProtoModule> modules = loadModules(in);
            if(modules==null)
                return;
            
            compile(modules);
        }
        
    }

}
