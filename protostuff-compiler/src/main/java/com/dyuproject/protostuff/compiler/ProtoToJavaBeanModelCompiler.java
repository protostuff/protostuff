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

import com.dyuproject.protostuff.parser.EnumGroup;
import com.dyuproject.protostuff.parser.Message;
import com.dyuproject.protostuff.parser.Proto;
import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Compiles proto files to protobuf java messages (pojos).
 * Generates a {@code Schema} from the proto files.
 * <ul>
 * <li>Schemas are always separated from messages</li>
 * <li>Schemas are able to be inherited</li>
 * <li>Schemas have a lot of Annotation/Options to configure compiler output</li>
 * <li>@Transient support for Messages/Enumerations</li>
 * </ul>
 *
 * @author Ivan Prisyazhniy
 * @created Mar 9, 2012
 */
public class ProtoToJavaBeanModelCompiler extends STCodeGenerator
{
    public ProtoToJavaBeanModelCompiler()
    {
        super("java_bean_model");
    }

    public void compile(ProtoModule module, Proto proto) throws IOException
    {
        String javaPackageName = proto.getJavaPackageName();

        StringTemplateGroup modelTemplateGroup = getSTG("java_bean_model_model");
        StringTemplateGroup schemaTemplateGroup = getSTG("java_bean_model_schema");

        // Enum blocks as is
        for (EnumGroup eg : proto.getEnumGroups())
        {
            if (eg.getAnnotation("Transient") != null)
                continue;

            Writer writer = CompilerUtil.newWriter(module, javaPackageName, eg.getName() + ".java");
            AutoIndentWriter out = new AutoIndentWriter(writer);

            StringTemplate enumBlock = schemaTemplateGroup.getInstanceOf("enum_block");
            enumBlock.setAttribute("eg", eg);
            enumBlock.setAttribute("module", module);
            enumBlock.setAttribute("options", module.getOptions());

            enumBlock.write(out);
            writer.close();
        }

        // Messages and Schemas
        for (Message m : proto.getMessages())
        {
            if (m.getAnnotation("Transient") != null)
                continue;

            // true if its a service message w/c isn't supported atm
            if (m.getFields().isEmpty())
            {
                System.err.println("ignoring empty message: " + m.getFullName());
                continue;
            }

            // Generate model
            boolean generateModel = shouldGenerateModel(module, proto, m);
            if (generateModel)
            {
                // Get model name
                String modelFullName = getRemoteModelName(modelTemplateGroup, m);
                // Default package and name for model
                String modelPackage = javaPackageName;
                String modelName = modelFullName;
                // Detect if there is a package
                if (modelFullName.lastIndexOf('.') > -1)
                {
                    int lastDotIndex = modelFullName.lastIndexOf('.');
                    modelPackage = modelFullName.substring(0, lastDotIndex);
                    modelName = modelFullName.substring(lastDotIndex + 1);
                }

                Writer writer = CompilerUtil.newWriter(module, modelPackage, modelName + ".java");
                AutoIndentWriter out = new AutoIndentWriter(writer);

                StringTemplate messageBlock = modelTemplateGroup.getInstanceOf("message_block");
                messageBlock.setAttribute("message", m);
                messageBlock.setAttribute("module", module);
                messageBlock.setAttribute("options", module.getOptions());

                messageBlock.write(out);
                writer.close();
            }

            // Generate schema
            {
                Writer writer = CompilerUtil.newWriter(module, javaPackageName, getRemoteModelSchemaName(schemaTemplateGroup, m) + ".java");
                AutoIndentWriter out = new AutoIndentWriter(writer);

                StringTemplate messageBlock = schemaTemplateGroup.getInstanceOf("message_block");
                messageBlock.setAttribute("message", m);
                messageBlock.setAttribute("module", module);
                messageBlock.setAttribute("options", module.getOptions());

                messageBlock.write(out);
                writer.close();
            }
        }
    }

    public boolean shouldGenerateModel(ProtoModule module, Proto proto, Message m)
    {
        boolean generateModel = false;

        if (module.getOptions().containsKey("models"))
        {
            String optGenerateModel = module.getOption("models");
            generateModel =
                    optGenerateModel.equalsIgnoreCase("true") ||
                            optGenerateModel.equals("1");
        } else if (module.getOptions().containsKey("no_models"))
        {
            String optGenerateModel = module.getOption("no_models");
            generateModel =
                    !optGenerateModel.equalsIgnoreCase("true") &&
                            !optGenerateModel.equals("1");
        }

        if (proto.getOptions().containsKey("models"))
        {
            String optGenerateModel = proto.getExtraOption("models").toString();
            generateModel =
                    optGenerateModel.equalsIgnoreCase("true") ||
                            optGenerateModel.equals("1");
        } else if (proto.getOptions().containsKey("no_models"))
        {
            String optGenerateModel = proto.getExtraOption("no_models");
            generateModel =
                    !optGenerateModel.equalsIgnoreCase("true") &&
                            !optGenerateModel.equals("1");
        }

        if (m.getOptions().containsKey("model"))
        {
            String optGenerateModel = m.getExtraOption("model").toString();
            generateModel =
                    optGenerateModel.equalsIgnoreCase("true") ||
                            optGenerateModel.equals("1");
        } else if (m.getOptions().containsKey("no_model"))
        {
            String optGenerateModel = m.getExtraOption("no_model");
            generateModel =
                    !optGenerateModel.equalsIgnoreCase("true") &&
                            !optGenerateModel.equals("1");
        }

        return generateModel;
    }

    public String getRemoteModelName(StringTemplateGroup group, Message message) throws IOException
    {
        StringWriter writer = new StringWriter(16);
        NoIndentWriter out = new NoIndentWriter(writer);

        StringTemplate messageBlock = group.getInstanceOf("remote_model_name");
        messageBlock.setAttribute("message", message);
        messageBlock.setAttribute("name", message.getName());
        messageBlock.write(out);

        String result = writer.toString();
        writer.close();

        return result;
    }

    public String getRemoteModelSchemaName(StringTemplateGroup group, Message message) throws IOException
    {
        StringWriter writer = new StringWriter(16);
        NoIndentWriter out = new NoIndentWriter(writer);

        StringTemplate messageBlock = group.getInstanceOf("remote_model_schema_name");
        messageBlock.setAttribute("message", message);
        messageBlock.setAttribute("name", message.getName());
        messageBlock.write(out);

        String result = writer.toString();
        writer.close();

        return result;
    }
}
