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

package io.protostuff.compiler;

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

    public static final boolean SILENT_MODE = Boolean.parseBoolean(
            System.getProperty("protostuff.compiler.silent_mode", "true"));

    public static final Pattern COMMA = Pattern.compile(",");

    private static final String DOES_NOT_EXIST = " does not exist.";
    private static final String CACHE_PROTOS = "cache_protos";
    
    static final HashMap<String, ProtoCompiler> __compilers =
            new HashMap<>();
    private static CompilerResolver __compilerResolver = null;
    static
    {
        addCompiler(new ProtoToJavaBeanCompiler());
        addCompiler(new ProtoToJavaBeanPrimitiveCompiler());
        addCompiler(new ProtoToJavaBeanMeCompiler());
        addCompiler(new ProtoToGwtOverlayCompiler());
        addCompiler(new ProtoToJavaV2ProtocSchemaCompiler());
        addCompiler(new ProtoToJavaBeanModelCompiler());
        addCompiler(new ProtoToProtoCompiler());
    }

    public static void setCompilerResolver(CompilerResolver resolver)
    {
        __compilerResolver = resolver;
    }

    public static void addCompiler(ProtoCompiler compiler)
    {
        __compilers.put(compiler.getOutputId(), compiler);
    }

    public static boolean isAvailableOutput(String output)
    {
        return __compilers.get(output) != null;
    }

    static Properties propsFrom(File file) throws IOException
    {
        Properties props = new Properties();
        try
        {
            props.load(new FileInputStream(file));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return props;
    }

    static Properties propsFrom(String resource) throws IOException
    {
        File file = new File(resource);
        return file.exists() ? propsFrom(file) : null;
    }

    public static List<ProtoModule> loadModules(File file, File baseDirForSource,
            File baseDirForOutput)
    {
        Properties props = new Properties();
        try
        {
            props.load(new FileInputStream(file));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return loadModules(props, baseDirForSource, baseDirForOutput);
    }

    public static List<ProtoModule> loadModules(InputStream in)
    {
        Properties props = new Properties();
        try
        {
            props.load(in);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return loadModules(props, null, null);
    }

    public static List<ProtoModule> loadModules(Properties props)
    {
        return loadModules(props, null, null);
    }

    public static List<ProtoModule> loadModules(Properties props,
            File baseDirForSource, File baseDirForOutput)
    {
        String moduleString = props.getProperty("modules");
        if (moduleString == null || moduleString.length() == 0)
        {
            propsErr();
            return null;
        }
        CachingProtoLoader loader = "true".equals(props.getProperty(CACHE_PROTOS)) ?
                new CachingProtoLoader() : null;

        Properties globalOptions = newGlobalOptions(props);

        ArrayList<ProtoModule> modules = new ArrayList<>();
        for (String m : COMMA.split(moduleString))
        {
            modules.add(loadModule(props, m.trim(), loader,
                    baseDirForSource, baseDirForOutput,
                    globalOptions, null, null));
        }

        return modules;
    }

    public static ProtoModule loadModule(Properties props,
            String name, CachingProtoLoader loader,
            File baseDirForSource, File baseDirForOutput,
            Properties globalOptions,
            String[] profileOptions, String[] rootProfileOptions)
    {
        String source = props.getProperty(name + ".source");
        if (source == null)
            throw new IllegalStateException(name + " must have a source");

        String output = props.getProperty(name + ".output");
        if (output == null)
            throw new IllegalStateException(name + " must have an output");

        String outputDir = props.getProperty(name + ".outputDir");
        if (outputDir == null)
            throw new IllegalStateException(name + " must have an outputDir");

        String encoding = props.getProperty(name + ".encoding");

        String options = props.getProperty(name + ".options");

        ProtoModule module = newProtoModule(source, output, encoding, outputDir,
                baseDirForSource, baseDirForOutput);

        module.setCachingProtoLoader(loader);

        module.getOptions().putAll(globalOptions);
        module.config = props;

        if (options != null)
            addOptionsTo(module.getOptions(), COMMA.split(options), props);

        // can override previous options
        if (profileOptions != null)
            addOptionsTo(module.getOptions(), profileOptions, props);

        if (rootProfileOptions != null)
            addOptionsTo(module.getOptions(), rootProfileOptions, props);

        return module;
    }

    static ProtoModule newProtoModule(String source, String output, String encoding,
            String outputDir, File baseDirForSource, File baseDirForOutput)
    {
        if (baseDirForSource != null)
        {
            if (baseDirForOutput != null)
            {
                return new ProtoModule(new File(baseDirForSource, source), output,
                        encoding, new File(baseDirForOutput, outputDir));
            }

            return new ProtoModule(new File(baseDirForSource, source), output,
                    encoding, new File(outputDir));
        }

        if (baseDirForOutput != null)
        {
            return new ProtoModule(new File(source), output,
                    encoding, new File(baseDirForOutput, outputDir));
        }

        return new ProtoModule(new File(source), output, encoding, new File(outputDir));
    }

    public static void addOptionsTo(ProtoModule module, String[] options)
    {
        addOptionsTo(module.getOptions(), options, module.getConfig());
    }

    public static void addOptionsTo(final Properties target, final String[] options,
            final Properties config)
    {
        for (String o : options)
        {
            int idx = o.indexOf(':');
            if (idx != -1)
            {
                target.setProperty(o.substring(0, idx).trim(), o.substring(idx + 1).trim());
                continue;
            }

            String key = o.trim();
            switch (key.charAt(0))
            {
                case '!':
                    // remove key
                    target.remove(key.substring(1));
                    break;

                case '_':
                    // could be a reference to a csv entry in config
                    if (config != null)
                    {
                        String csv = config.getProperty(key);
                        if (csv != null)
                        {
                            // include the csv from config props
                            addOptionsTo(target, COMMA.split(csv), config);
                        }
                    }

                default:
                    target.setProperty(key, "");
            }
        }
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
        System.err.println("modules = foo,bar");
        System.err.println("foo.source = path/to/your/proto/file/or/dir");
        System.err.println("foo.output = java_bean");
        System.err.println("foo.outputDir = src/main/java");
        System.err.println("foo.encoding = UTF-8");
        System.err.println("foo.options = key1:value1,key2:value2");

        System.err.println("bar.source = path/to/your/proto/file/or/dir");
        System.err.println("bar.output = java_bean");
        System.err.println("bar.outputDir = target/generated");
        System.err.println("bar.encoding = UTF-8");
        System.err.println("bar.options = separate_schema,generate_field_map");

        System.err.println("\n===================================================\n");

        System.err.println("\nTo generate code for a single module, execute the jar without args and specify:");
        System.err.println("  -Dsource=path/to/your/proto/file/or/dir");
        System.err.println("  -Doutput=java_bean");
        System.err.println("  -DoutputDir=src/main/java");
        System.err.println("  -Dencoding=UTF-8");
        System.err.println("  -Doptions=key1:value1,key2:value2");
    }

    public static void compile(ProtoModule module) throws Exception
    {
        String strOptions = null;
        String originalOutput = module.getOutput();
        try
        {
            for (String output : COMMA.split(originalOutput))
            {
                output = output.trim();
                // update output for each iteration
                // effectively it is different compiler
                module.setOutput(output);
                // default generator is "output"
                module.setGenerator(output);
                ProtoCompiler compiler = __compilers.get(output);
                if (compiler == null)
                {
                    if (__compilerResolver != null)
                        compiler = __compilerResolver.resolve(module);
                    else if (output.endsWith(".stg"))
                    {
                        // custom code generator
                        String generator = createGeneratorName(output);
                        module.setGenerator(generator);
                        compiler = new PluginProtoCompiler(module, output);
                    }
                    else
                        throw new IllegalStateException("unknown output: " + output);
                }

                compiler.compile(module);

                if (!SILENT_MODE)
                {
                    StringBuilder buffer = new StringBuilder()
                            .append("Successfully compiled proto from ")
                            .append(module.getSource())
                            .append(" to output: ")
                            .append(output);

                    // lazy
                    if (strOptions == null)
                        strOptions = module.getOptions().toString();

                    if (strOptions.length() > 2)
                        buffer.append(' ').append(strOptions);

                    System.out.println(buffer.toString());
                }
            }
        }
        finally
        {
            // reset original module output and generator
            module.setOutput(originalOutput);
            module.setGenerator(null);
        }
    }

    private static String createGeneratorName(String output)
    {
        String fileName = FilenameUtil.getFileName(output);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileName.length(); i++)
        {
            char c = fileName.charAt(i);
            if (isAlpha(c) || isNumber(c) || isAllowedCharacter(c))
            {
                sb.append(c);
            }
            else
            {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    private static boolean isAllowedCharacter(char c)
    {
        return c == '.' || c == '_' || c == '-' || c == '$';
    }

    private static boolean isNumber(char c)
    {
        return (c >= '0' && c <= '9');
    }

    private static boolean isAlpha(char c)
    {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static void compile(List<ProtoModule> modules) throws Exception
    {
        for (ProtoModule m : modules)
            compile(m);
    }

    static void compileProfile(Properties props, String profile,
            CachingProtoLoader loader,
            Properties globalOptions, String[] rootProfileOptions,
            final int nestCount) throws Exception
    {
        String moduleString = props.getProperty(profile);
        if (moduleString == null || moduleString.length() == 0)
        {
            throw new RuntimeException("No modules for profile: " + profile);
        }

        for (int i = 0; i < nestCount; i++)
        {
            System.out.print("  ");
        }
        System.out.println(profile);

        final long start = System.nanoTime();

        String profileOptionsParam = props.getProperty(profile + ".options");
        String[] profileOptions = profileOptionsParam == null ? null :
                COMMA.split(profileOptionsParam);

        for (String m : COMMA.split(moduleString))
        {
            m = m.trim();

            if (m.charAt(0) == '@')
            {
                // referencing another profile
                compileProfile(props, m, loader, globalOptions,
                        nestCount == 0 ? profileOptions : rootProfileOptions,
                        nestCount + 1);
                continue;
            }

            compile(loadModule(props, m, loader, null, null,
                    globalOptions, profileOptions,
                    nestCount == 0 ? profileOptions : rootProfileOptions));
        }

        final long end = System.nanoTime();

        for (int i = 0; i < nestCount; i++)
        {
            System.out.print("  ");
        }

        long ms = (end - start) / 1000000;
        System.out.println(ms + " ms\n");
    }

    static void compileWithNoArgs() throws Exception
    {
        Properties props = System.getProperties();

        String source = props.getProperty("source");
        String output = props.getProperty("output");
        String outputDir = props.getProperty("outputDir");
        String encoding = props.getProperty("encoding");
        String options = props.getProperty("options");

        if (source == null || output == null || outputDir == null)
        {
            usage();
            return;
        }

        ProtoModule module = new ProtoModule(new File(source), output,
                encoding, new File(outputDir));

        if (options != null)
        {
            for (String o : COMMA.split(options))
            {
                int idx = o.indexOf(':');
                if (idx == -1)
                    module.setOption(o.trim(), "");
                else
                    module.setOption(o.substring(0, idx).trim(), o.substring(idx + 1).trim());
            }
        }

        compile(module);
    }

    static void compileWithArgs(final String[] args, int offset, final int limit)
            throws Exception
    {
        String propsResource = args[offset++];
        Properties props = propsFrom(propsResource);
        if (props == null)
        {
            System.err.println(propsResource + DOES_NOT_EXIST);
            return;
        }

        Properties globalOptions = newGlobalOptions(props);

        final CachingProtoLoader loader =
                ("true".equals(props.getProperty(CACHE_PROTOS)) ||
                "true".equals(System.getProperty(CACHE_PROTOS))) ?
                        new CachingProtoLoader() : null;

        boolean selectedProfileOrModule = false;
        for (String arg = propsResource;;)
        {
            if (offset != limit)
            {
                if ((arg = args[offset]).charAt(0) == '@')
                {
                    // activating a profile
                    compileProfile(props, arg, loader, globalOptions, null, 0);

                    selectedProfileOrModule = true;
                    offset++;
                    continue;
                }

                if (!new File(arg).exists())
                {
                    // specific module
                    compile(loadModule(props, arg, loader, null, null,
                            globalOptions, null, null));

                    selectedProfileOrModule = true;
                    offset++;
                    continue;
                }
            }

            if (selectedProfileOrModule)
            {
                if (offset == limit)
                    return;

                selectedProfileOrModule = false;

                if ((props = propsFrom((arg = args[offset++]))) == null)
                {
                    // the next properties file does not exist.
                    System.err.println(arg + DOES_NOT_EXIST);
                    return;
                }

                globalOptions = newGlobalOptions(props);

                continue;
            }

            // compile the csv from "modules" property
            String moduleString = props.getProperty("modules");
            if (moduleString == null || moduleString.length() == 0)
            {
                System.err.println("Errors on: " + arg);
                propsErr();
                return;
            }

            for (String m : COMMA.split(moduleString))
            {
                m = m.trim();

                if (m.charAt(0) == '@')
                {
                    // referencing another profile
                    compileProfile(props, m, loader, globalOptions, null, 0);
                    continue;
                }

                compile(loadModule(props, m, loader, null, null,
                        globalOptions, null, null));
            }

            if (offset == limit)
                return;

            if ((props = propsFrom((arg = args[offset++]))) == null)
            {
                // the next properties file does not exist.
                System.err.println(arg + DOES_NOT_EXIST);
                return;
            }

            globalOptions = newGlobalOptions(props);
        }
    }

    static Properties putIncludes(Properties props)
    {
        String includes = props.getProperty("includes");
        if (includes == null)
            return props;

        for (String include : COMMA.split(includes))
        {
            final Properties p;
            try
            {
                p = propsFrom(include.trim());
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            for (Object key : p.keySet())
            {
                if (!props.containsKey(key))
                    props.put(key, p.get(key));
            }
        }

        return props;
    }

    public static Properties newGlobalOptions(Properties props)
    {
        return newOptions(putIncludes(props), "global_options");
    }

    /**
     * Returns an option ({@link Properties}) that contains the csv entries.
     */
    public static Properties newOptions(Properties props, String key)
    {
        Properties options = new Properties();

        String csv = props.getProperty(key);
        if (csv != null)
            addOptionsTo(options, COMMA.split(csv), props);

        return options;
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
            compileWithNoArgs();
        else
            compileWithArgs(args, 0, args.length);
    }

    /**
     * When there is no matching compiler for the {@link ProtoModule#getOutput()}.
     */
    public interface CompilerResolver
    {
        ProtoCompiler resolve(ProtoModule module);
    }

}
