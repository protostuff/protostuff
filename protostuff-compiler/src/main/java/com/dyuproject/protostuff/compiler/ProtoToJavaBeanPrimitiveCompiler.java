package com.dyuproject.protostuff.compiler;

import com.dyuproject.protostuff.parser.Proto;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.IOException;

/**
 * Created by ryan on 1/22/14.
 */
public class ProtoToJavaBeanPrimitiveCompiler extends ProtoToJavaBeanCompiler {
    public ProtoToJavaBeanPrimitiveCompiler() {
        super("java_bean_primitives");
    }

    public void compile(ProtoModule module, Proto proto) throws IOException {
        String javaPackageName = proto.getJavaPackageName();
        StringTemplateGroup group = getSTG("java_bean_primitives");

        // this compiler doesnt allow setters
        module.getOptions().setProperty("no_setters", "true");

        writeEnums(module, proto, javaPackageName, group);
        writeMessages(module, proto, javaPackageName, group);
    }
}
