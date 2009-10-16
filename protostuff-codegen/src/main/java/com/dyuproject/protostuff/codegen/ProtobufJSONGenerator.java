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
import java.io.Writer;
import java.util.ArrayList;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.dyuproject.protostuff.model.Model;

/**
 * @author David Yu
 * @created Oct 14, 2009
 */

public class ProtobufJSONGenerator extends CodeGenerator
{
    
    static final String TEMPLATE_LOCATION = "protobuf_json.vm";
    
    protected final VelocityEngine _engine = new VelocityEngine();
    {
        _engine.setProperty(Velocity.RESOURCE_LOADER, "class");
        _engine.setProperty("class.resource.loader.class", 
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    }    

    protected VelocityContext newVelocityContext()
    {
        VelocityContext context = new VelocityContext();        
        context.put("Util", VelocityUtil.class);        
        return context;
    }
    
    protected String getDefaultOutputClassName(String moduleClassName)
    {
        return moduleClassName + "JSON";
    }
    
    protected String getTemplateLocation()
    {
        return TEMPLATE_LOCATION;
    }

    @Override
    protected void generateFrom(Module module, ArrayList<Model<?>> models) 
    throws Exception
    {
        VelocityContext context = newVelocityContext();
        context.put("module", module);
        context.put("models", models);
        Writer writer = newWriter(module, module.getOutputClassName() + ".java");
        try
        {
            _engine.mergeTemplate(getTemplateLocation(), "UTF-8", context, writer);
        }
        finally
        {
            writer.close();
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        File outputDir = new File("src/test/java");
        if(!outputDir.exists())
            throw new IllegalStateException("outputDir " + outputDir + "  does not exist");

        Module module = new Module();
        module.setFullClassName("com.dyuproject.protostuff.codegen.benchmark.V22LiteMedia");
        module.setOutputPackage("com.dyuproject.protostuff.codegen.benchmark");
        module.setOutputDir(outputDir);
        
        ProtobufJSONGenerator generator = new ProtobufJSONGenerator();
        generator.generateFrom(module);
    }


}
