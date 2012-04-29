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

import com.dyuproject.protostuff.parser.Annotation;
import com.dyuproject.protostuff.parser.Message;
import com.dyuproject.protostuff.parser.Proto;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kind of preprocessor for proto files.
 * Able to extend one messages with fields from other ones.
 *
 * @author Ivan Prisyazhniy, Igor Scherbak
 * @created Mar 9, 2012
 */
public class ProtoToProtoCompiler extends STCodeGenerator
{
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public ProtoToProtoCompiler()
    {
        super("proto_extender");
    }

    public void compile(ProtoModule module, Proto proto) throws IOException
    {
        StringTemplateGroup group = getSTG("proto_to_proto");

        // Prepare writer
        String src = module.getSource().getAbsolutePath();
        String path = proto.getFile().getAbsolutePath().replace(src, "").replace(proto.getFile().getName(), "");

        Writer writer = CompilerUtil.newWriter(module, path, proto.getFile().getName());
        // Read proto file in a buffer
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(proto.getFile()));
        String line = reader.readLine();
        while (line != null)
        {
            builder.append(line);
            builder.append(LINE_SEPARATOR);
            line = reader.readLine();
        }
        reader.close();

        String data = builder.toString();

        for (Message message : proto.getMessages())
        {
            Annotation annotation = message.getAnnotation("Extend");
            if (annotation != null)
            {
                Object byMessageRef = annotation.getValue("by");
                if (byMessageRef == null)
                    throw new IllegalArgumentException("By parameter of attribute @Extend is not specified");

                if (!(byMessageRef instanceof Message))
                    throw new IllegalArgumentException("By parameter have a non Message reference in your @Extend annotation");

                Message base = (Message) byMessageRef;
                String result = extendBy(group, message, base);
                if (result != null && result.length() > 0)
                    data = injectAfterAnnotation(message, base, data, result);
            }

            Object extOpt = message.getExtraOption("extends");
            if (extOpt != null)
            {
                if (!(extOpt instanceof Message))
                    throw new IllegalArgumentException("Option extends specified not a message reference");

                Message base = (Message) extOpt;
                String result = extendBy(group, message, base);
                if (result != null && result.length() > 0)
                    data = injectAfterOption(message, base, data, result);
            }
        }

        writer.write(data);
        writer.close();
    }

    public static String extendBy(StringTemplateGroup group, Message extend, Message by) throws IOException
    {
        StringWriter stringer = new StringWriter(16);
        NoIndentWriter out = new NoIndentWriter(stringer);

        StringTemplate messageBlock = group.getInstanceOf("extend_by");
        messageBlock.setAttribute("message", extend);
        messageBlock.setAttribute("by", by);
        messageBlock.write(out);

        return stringer.toString();
    }

    public static String injectAfterAnnotation(Message extend, Message by, String extendProto, String byContent)
    {
        // Insert after annotated message
        Pattern messageRegexp = Pattern.compile("[\\n\\r]?([ \\t]*)(message\\s+" + extend.getName() + "\\s+\\{)", Pattern.MULTILINE);

        int messageIndex = -1, openBracketIndex = -1;
        Matcher matcher = messageRegexp.matcher(extendProto);
        if (matcher.find())
        {
            // Calculate indentation of option
            int is = matcher.start(1), ie = matcher.end(1);
            String indentation = generateIndentation(extendProto.substring(is, ie), 4 /* spaces */);
            // Make a replace
            messageIndex = matcher.start(2);
            openBracketIndex = matcher.end(2);
            extendProto = extendProto.substring(0, openBracketIndex) +
                    LINE_SEPARATOR + indentation + "// " + generateTimestamp(extend, by) +
                    LINE_SEPARATOR + insertIndentation(byContent, indentation) +
                    LINE_SEPARATOR + extendProto.substring(openBracketIndex);
        }

        // Remove annotation
        Pattern annotationRegexp = Pattern.compile("[\\n\\r]?([ \\t]*@Extend\\s*\\([^)]+" + by.getName() + "[^)]*\\))");
        String annotationSpace = extendProto.substring(0, messageIndex);
        matcher = annotationRegexp.matcher(annotationSpace);
        int astart = -1, aend = 0;
        while (matcher.find(aend))
        {
            astart = matcher.start(1);
            aend = matcher.end(1);
        }
        if (astart > -1)
            extendProto = extendProto.substring(0, astart) + "// " + extendProto.substring(astart);

        return extendProto;
    }

    public static String injectAfterOption(Message extend, Message by, String extendProto, String byContent)
    {
        Pattern messageRegexp = Pattern.compile("([\\n\\r]?[ \\t]*message\\s+" + extend.getName() +
                "\\s+\\{[^{}]*[\\n\\r][ \\t]*)(option\\s+extends\\s+=\\s+" + by.getName() +
                "\\s*;)", Pattern.MULTILINE);

        Matcher matcher = messageRegexp.matcher(extendProto);

        if (matcher.find())
        {
            // Calculate indentation of option
            Pattern indentRegexp = Pattern.compile("[\\n\\r]([ \\t]+)option\\s+extends\\s+=\\s+" + by.getName() +
                    "\\s*;", Pattern.MULTILINE);
            Matcher indent = indentRegexp.matcher(extendProto.substring(matcher.start(), matcher.end()));
            String indentation = "";
            if (indent.find())
            {
                int is = matcher.start() + indent.start(1), ie = matcher.start() + indent.end(1);
                indentation = generateIndentation(extendProto.substring(is, ie), 0);
            }
            // Make a replace
            StringBuffer sb = new StringBuffer();
            matcher.appendReplacement(sb, "$1" +
                    "// " + generateTimestamp(extend, by) + LINE_SEPARATOR +
                    indentation + "// $2" + LINE_SEPARATOR + insertIndentation(byContent, indentation));
            matcher.appendTail(sb);
            return sb.toString();
        }

        return extendProto;
    }

    public static String insertIndentation(String content, String indent)
    {
        if (!content.startsWith(LINE_SEPARATOR)) content = indent + content;
        return content.replace(LINE_SEPARATOR, LINE_SEPARATOR + indent);
    }

    public static String generateIndentation(String indentation, int length)
    {
        if (indentation == null)
            indentation = "";

        StringBuilder builder = new StringBuilder(indentation);
        for (int i = 0; i < length; i++)
            builder.append(' ');

        return builder.toString();
    }

    public static String generateTimestamp(Message extend, Message by)
    {
        return "Extended by " + by.getName() + " at " + new Date();
    }
}
