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
 * @author David Yu
 * @created Oct 16, 2009
 */

public final class GeneratorMain
{
    
    static final Pattern COMMA = Pattern.compile(",");
    
    private static final HashMap<String,CodeGenerator> __generators = 
        new HashMap<String,CodeGenerator>();
    
    static
    {        
        addGenerator(new ProtobufJSONGenerator());
        addGenerator(new ProtobufNumericJSONGenerator());
        
        addGenerator(new GwtJsonGenerator());
        addGenerator(new GwtNumericJsonGenerator());
    }
    
    public static void addGenerator(CodeGenerator generator)
    {
        __generators.put(generator.getId(), generator);
    }
    
    public static InputStream getInputStream(String resource) throws IOException
    {
        File file = new File(resource);
        return file.exists() ? new FileInputStream(file) : null;
    }
    
    public static List<Module> loadModules(InputStream in)
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
    
    public static List<Module> loadModules(Properties props)
    {
        String moduleString = props.getProperty("modules");
        if(moduleString==null || moduleString.length()==0)
        {
            propsErr();
            return null;
        }        
        
        ArrayList<Module> modules = new ArrayList<Module>();
        for(String m : COMMA.split(moduleString))
        {
            m = m.trim();
            String fullClassname = props.getProperty(m + ".fullClassname");
            if(fullClassname==null)
                throw new IllegalStateException(m + " must have a fullClassname");
            
            String outputPackage = props.getProperty(m + ".outputPackage");
            if(outputPackage==null)
                throw new IllegalStateException(m + " must have an outputPackage");
            
            String generator = props.getProperty(m + ".generator");
            if(generator==null)
                throw new IllegalStateException(m + " must have a generator");
            
            String encoding = props.getProperty(m + ".encoding");
            if(encoding==null)
                throw new IllegalStateException(m + " must have an encoding");
            
            String outputDir = props.getProperty(m + ".outputDir");
            if(outputDir==null)
                throw new IllegalStateException(m + " must have an outputDir");
            
            Module module = new Module(fullClassname, outputPackage, 
                    generator, encoding, new File(outputDir));
            
            modules.add(module);
        }
        
        return modules;
    }
    
    static void propsErr()
    {
        System.err.println("\nError parsing the properties file ...");
        
        System.err.println("\nThe properties file would look like:");
        System.err.println("modules = foo");
        System.err.println("foo.fullClassname = com.example.bar.OuterClassname");
        System.err.println("foo.outputPackage = com.example.bar.generated");
        System.err.println("foo.generator = json \n//" + __generators.keySet().toString());
        System.err.println("foo.encoding = UTF-8");
        System.err.println("foo.outputDir = src/main/java");
    }
    
    static void usage()
    {
        System.err.println("\nTo generate code for multiple modules:");
        System.err.println("  java -cp your_protobuf_modules.jar -jar protostuff-codegen.jar modules.properties");
        
        System.err.println("\nThe properties file would look like:\n");
        System.err.println("modules = foo");
        System.err.println("foo.fullClassname = com.example.bar.OuterClassname");
        System.err.println("foo.outputPackage = com.example.bar.generated");
        System.err.println("foo.generator = json \n//" + __generators.keySet().toString());
        System.err.println("foo.encoding = UTF-8");
        System.err.println("foo.outputDir = src/main/java");
        
        System.err.println("\n===================================================\n");
        
        System.err.println("\nTo generate code for a single module, execute the jar without args and specify:");
        System.err.println("  -DfullClassname=com.example.bar.OuterClassname");
        System.err.println("  -DoutputPackage=com.example.bar.generated");
        System.err.println("  -Dgenerator=json \n//" + __generators.keySet().toString());
        System.err.println("  -Dencoding=UTF-8");
        System.err.println("  -DoutputDir=src/main/java");
    }
    
    public static void generateFrom(Module m) throws Exception
    {
        for(String g : COMMA.split(m.getGenerator()))
        {
            g = g.trim();
            CodeGenerator cg = __generators.get(g);
            if(cg==null)
                throw new IllegalStateException("unknown generator: " + g);
            
            cg.generateFrom(m);
            System.out.println("Successfully generated " + g + " code from " + 
                    m.getFullClassname());
        }
    }
    
    public static void generateFrom(List<Module> modules) throws Exception
    {
        for(Module m : modules)
            generateFrom(m);
    }
    
    public static void main(String[] args) throws Exception
    {
        if(args.length==0)
        {
            Properties props = System.getProperties();            
            
            String fullClassname = props.getProperty("fullClassname");            
            String outputPackage = props.getProperty("outputPackage");
            String generator = props.getProperty("generator");
            String encoding = props.getProperty("encoding");
            String outputDir = props.getProperty("outputDir");
            
            if(fullClassname==null || outputPackage==null || generator==null || outputDir==null)
                usage();
            else
            {
                Module module = new Module(fullClassname, outputPackage, 
                        generator, encoding, new File(outputDir));
                
                generateFrom(module);
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
            
            List<Module> modules = loadModules(in);
            if(modules==null)
                return;
            
            generateFrom(modules);
        }
        
    }

}
