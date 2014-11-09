package io.protostuff.compiler;

import io.protostuff.parser.Field;
import io.protostuff.parser.Message;
import io.protostuff.parser.Proto;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.IOException;

/**
 * Created by ryan on 1/22/14.
 */
public class ProtoToJavaBeanPrimitiveCompiler extends ProtoToJavaBeanCompiler
{
    public ProtoToJavaBeanPrimitiveCompiler()
    {
        super("java_bean_primitives");
    }

    void setByteBuffer(Message m)
    {
        for (Field f : m.getFields())
        {
            if (f.isBytesField())
                f.putExtraOption("ByteBuffer", true);
        }

        for (Message m2 : m.getNestedMessages())
        {
            setByteBuffer(m2);
        }
    }

    @Override
    public void compile(ProtoModule module, Proto proto) throws IOException
    {
        String javaPackageName = proto.getJavaPackageName();
        StringTemplateGroup group = getSTG("java_bean_primitives");

        // this compiler doesnt allow setters
        module.getOptions().setProperty("no_setters", "true");

        // TODO find a way to push this up to the parsing step
        if (module.getOption("ByteBuffer") != null)
        {
            for (Message m : proto.getMessages())
            {
                m.setByteBufferFieldPresent(true);

                setByteBuffer(m);
            }
        }

        writeEnums(module, proto, javaPackageName, group);
        writeMessages(module, proto, javaPackageName, group);
    }
}
