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

import io.protostuff.parser.DefaultProtoLoader;
import io.protostuff.parser.EnumGroup;
import io.protostuff.parser.Message;
import io.protostuff.parser.Proto;
import io.protostuff.parser.ProtoUtil;
import io.protostuff.parser.Service;

/**
 * A plugin proto compiler whose output relies on the 'output' param configured in {@link ProtoModule}. The output param
 * should point to a StringTemplate resource (file, url, or from classpath).
 * 
 * @author David Yu
 * @created May 25, 2010
 */
public class PluginProtoCompiler extends STCodeGenerator
{

    private static final String OPTIONS = "options";
    private static final String MODULE = "module";

    /**
     * To enable, specify -Dppc.check_filename_placeholder=true
     */
    protected static final boolean CHECK_FILENAME_PLACEHOLDER =
            Boolean.getBoolean("ppc.check_filename_placeholder");

    /**
     * Resolve the stg from the module.
     */
    public interface GroupResolver
    {

        /**
         * Resolve the stg.
         */
        StringTemplateGroup resolveSTG(String stgLocation);

    }

    public static final GroupResolver GROUP_RESOLVER = new GroupResolver()
    {

        @Override
        public StringTemplateGroup resolveSTG(String stgLocation)
        {
            try
            {
                File file = new File(stgLocation);
                if (file.exists())
                    return new StringTemplateGroup(new BufferedReader(new FileReader(file)));

                URL url = DefaultProtoLoader.getResource(stgLocation,
                        PluginProtoCompiler.class);
                if (url != null)
                {
                    return new StringTemplateGroup(new BufferedReader(
                            new InputStreamReader(url.openStream(), "UTF-8")));
                }
                if (stgLocation.startsWith("http://"))
                {
                    return new StringTemplateGroup(new BufferedReader(
                            new InputStreamReader(new URL(stgLocation).openStream(), "UTF-8")));
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            throw new IllegalStateException("Could not find " + stgLocation);
        }
    };

    public static void setGroupResolver(GroupResolver resolver)
    {
        if (resolver != null)
            __resolver = resolver;
    }

    /**
     * Returns null if template is not found.
     */
    public static StringTemplate getTemplateFrom(StringTemplateGroup group,
            String template)
    {
        try
        {
            return group.lookupTemplate(template);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    private static GroupResolver __resolver = GROUP_RESOLVER;

    public final ProtoModule module;

    public final StringTemplateGroup group;
    public final StringTemplate enumBlockTemplate;
    public final StringTemplate messageBlockTemplate;
    public final StringTemplate protoBlockTemplate;
    public final StringTemplate serviceBlockTemplate;
    public final boolean javaOutput;
    public final String fileExtension;
    public final String outputName;
    public final String outputPrefix;
    public final String outputSuffix;

    public PluginProtoCompiler(ProtoModule module, String stgLocation)
    {
        this(module, stgLocation, CHECK_FILENAME_PLACEHOLDER);
    }

    public PluginProtoCompiler(ProtoModule module, String stgLocation, boolean checkFilenamePlaceHolder)
    {
        this(module, checkFilenamePlaceHolder, resolveSTG(stgLocation));
    }

    public PluginProtoCompiler(ProtoModule module, boolean checkFilenamePlaceHolder,
            StringTemplateGroup group)
    {
        super(module.getOutput());

        this.module = module;
        this.group = group;

        protoBlockTemplate = getTemplateFrom(group, "proto_block");
        if (protoBlockTemplate == null)
        {
            // validate that at least enum_block or message_block is present.
            enumBlockTemplate = getTemplateFrom(group, "enum_block");
            messageBlockTemplate = getTemplateFrom(group, "message_block");
            serviceBlockTemplate = getTemplateFrom(group, "service_block");

            if (enumBlockTemplate == null && messageBlockTemplate == null && serviceBlockTemplate == null)
            {
                throw new IllegalStateException("At least one of these templates " +
                        "(proto_block|service_block|message_block|enum_block) " +
                        "need to be declared in " + module.getOutput());
            }
        }
        else
        {
            enumBlockTemplate = null;
            messageBlockTemplate = null;
            serviceBlockTemplate = null;
        }

        fileExtension = getFileExtension(module.getOutput());
        javaOutput = ".java".equalsIgnoreCase(fileExtension);

        outputName = getOutputName(module.getOutput());

        final int placeHolder = checkFilenamePlaceHolder ? outputName.indexOf('$') : -1;
        if (placeHolder == -1)
        {
            // no placeholder
            outputPrefix = "";
            outputSuffix = "";
        }
        else if (placeHolder == 0)
        {
            // suffix only
            outputPrefix = "";
            // check if provided text is only 1 char "$"
            outputSuffix = outputName.length() == 1 ? "" : outputName.substring(1);
        }
        else if (placeHolder == outputName.length() - 1)
        {
            // prefix only
            outputPrefix = outputName.substring(0, outputName.length() - 1);
            outputSuffix = "";
        }
        else
        {
            // has both prefix and suffix
            outputPrefix = outputName.substring(0, placeHolder);
            outputSuffix = outputName.substring(placeHolder + 1);
        }
    }

    /**
     * Returns "foo" from "path/to/foo.java.stg"
     */
    static String getOutputName(String resource)
    {
        String filename = FilenameUtil.getFileName(resource);
        int secondToTheLastDot = filename.lastIndexOf('.', filename.length() - 5);
        return filename.substring(0, secondToTheLastDot);
    }

    /**
     * Get the file extension of the provided stg resource.
     */
    public static String getFileExtension(String resource)
    {
        // E.g uf foo.bar.java.stg, it is the . before "java"
        int secondToTheLastDot = resource.lastIndexOf('.', resource.length() - 5);
        if (secondToTheLastDot == -1)
        {
            throw new IllegalArgumentException("The resource must be named like: 'foo.type.stg' " +
                    "where '.type' will be the file extension of the output files.");
        }
        String extension = resource.substring(secondToTheLastDot, resource.length() - 4);
        // to protected against resources like "foo..stg"
        if (extension.length() < 2)
        {
            throw new IllegalArgumentException("The resource must be named like: 'foo.type.stg' " +
                    "where '.type' will be the file extension of the output files.");
        }

        return extension;
    }

    /**
     * Finds the stg resource.
     */
    public static StringTemplateGroup resolveSTG(String stgLocation)
    {
        return __resolver.resolveSTG(stgLocation);
    }

    public String resolveFileName(String name)
    {
        return outputPrefix + name + outputSuffix + fileExtension;
    }

    @Override
    public void compile(ProtoModule module, Proto proto) throws IOException
    {
        if (!this.module.getOutput().startsWith(module.getOutput()))
        {
            throw new IllegalArgumentException("Wrong module: " +
                    this.module.getOutput() + " != " + module.getOutput());
        }

        final String packageName = javaOutput ? proto.getJavaPackageName() :
                proto.getPackageName();

        if (protoBlockTemplate != null)
        {
            compileProtoBlock(module, proto, packageName, protoBlockTemplate);
            return;
        }

        if (serviceBlockTemplate != null)
        {
            for (Service service : proto.getServices())
            {
                compileServiceBlock(module, service, packageName,
                        resolveFileName(service.getName()), serviceBlockTemplate);
            }
        }

        if (enumBlockTemplate != null)
        {
            for (EnumGroup eg : proto.getEnumGroups())
            {
                compileEnumBlock(module, eg, packageName,
                        resolveFileName(eg.getName()), enumBlockTemplate);
            }
        }

        if (messageBlockTemplate != null)
        {
            for (Message message : proto.getMessages())
            {
                compileMessageBlock(module, message, packageName,
                        resolveFileName(message.getName()), messageBlockTemplate);
            }
        }
    }

    private void compileServiceBlock(ProtoModule module, Service service, String packageName, String fileName,
            StringTemplate serviceBlockTemplate) throws IOException
    {
        Writer writer = CompilerUtil.newWriter(module, packageName, fileName);
        compileServiceBlockTo(writer, module, service, serviceBlockTemplate);
        writer.close();
    }

    public static void compileServiceBlockTo(Writer writer,
            ProtoModule module, Service service,
            StringTemplate serviceBlockTemplate) throws IOException
    {
        AutoIndentWriter out = new AutoIndentWriter(writer);

        StringTemplate messageBlock = serviceBlockTemplate.getInstanceOf();
        messageBlock.setAttribute("service", service);
        messageBlock.setAttribute(MODULE, module);
        messageBlock.setAttribute(OPTIONS, module.getOptions());

        messageBlock.write(out);
    }

    public static void compileEnumBlock(ProtoModule module, EnumGroup eg,
            String packageName, String fileName,
            StringTemplate enumBlockTemplate) throws IOException
    {
        Writer writer = CompilerUtil.newWriter(module, packageName, fileName);

        compileEnumBlockTo(writer, module, eg, enumBlockTemplate);

        writer.close();
    }

    public static void compileEnumBlockTo(Writer writer,
            ProtoModule module, EnumGroup eg,
            StringTemplate enumBlockTemplate) throws IOException
    {
        AutoIndentWriter out = new AutoIndentWriter(writer);

        StringTemplate enumBlock = enumBlockTemplate.getInstanceOf();
        enumBlock.setAttribute("eg", eg);
        enumBlock.setAttribute(MODULE, module);
        enumBlock.setAttribute(OPTIONS, module.getOptions());

        enumBlock.write(out);
    }

    public static void compileMessageBlock(ProtoModule module, Message message,
            String packageName, String fileName,
            StringTemplate messageBlockTemplate) throws IOException
    {
        Writer writer = CompilerUtil.newWriter(module, packageName, fileName);

        compileMessageBlockTo(writer, module, message, messageBlockTemplate);

        writer.close();
    }

    public static void compileMessageBlockTo(Writer writer,
            ProtoModule module, Message message,
            StringTemplate messageBlockTemplate) throws IOException
    {
        AutoIndentWriter out = new AutoIndentWriter(writer);

        StringTemplate messageBlock = messageBlockTemplate.getInstanceOf();
        messageBlock.setAttribute("message", message);
        messageBlock.setAttribute(MODULE, module);
        messageBlock.setAttribute(OPTIONS, module.getOptions());

        messageBlock.write(out);
    }

    public void compileProtoBlock(ProtoModule module, Proto proto,
            String packageName, StringTemplate protoBlockTemplate) throws IOException
    {
        String name = ProtoUtil.toPascalCase(proto.getFile().getName().replace(
                ".proto", "")).toString();

        if (javaOutput)
        {
            String outerClassname = proto.getExtraOption("java_outer_classname");
            if (outerClassname != null)
                name = outerClassname;
        }

        final String fileName;
        if (outputPrefix.isEmpty() && outputSuffix.isEmpty())
        {
            // resolve the prefix/suffix from module option
            String outerFilePrefix = module.getOption("outer_file_prefix");
            if (outerFilePrefix != null)
                name = outerFilePrefix + name;

            String outerFileSuffix = module.getOption("outer_file_suffix");
            if (outerFileSuffix != null)
                name += outerFileSuffix;

            fileName = name + fileExtension;
        }
        else
        {
            // use the placeholder in the output name
            fileName = resolveFileName(name);
        }

        Writer writer = CompilerUtil.newWriter(module, packageName, fileName);

        AutoIndentWriter out = new AutoIndentWriter(writer);

        StringTemplate protoBlock = protoBlockTemplate.getInstanceOf();
        protoBlock.setAttribute("proto", proto);
        protoBlock.setAttribute(MODULE, module);
        protoBlock.setAttribute(OPTIONS, module.getOptions());

        protoBlock.write(out);
        writer.close();
    }

}
