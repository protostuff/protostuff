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

import java.io.Writer;
import java.util.ArrayList;

import org.apache.velocity.VelocityContext;

import com.dyuproject.protostuff.model.Model;

/**
 * @author David Yu
 * @created Oct 17, 2009
 */

public class GwtJsonGenerator extends VelocityCodeGenerator
{
    
    public static final String ID = "gwt_json";
    static final String DEFAULT_TEMPLATE_RESOURCE = ID + ".vm";
    
    public GwtJsonGenerator()
    {
        this(DEFAULT_TEMPLATE_RESOURCE);
    }

    public GwtJsonGenerator(String templateResource)
    {
        super(templateResource);
    }
    
    @Override
    public String getId()
    {
        return ID;
    }

    @Override
    protected void generateFrom(Module module, ArrayList<Model<?>> models) throws Exception
    {
        for(Model<?> model : models)
        {
            String outputClassname = model.getModelMeta().getMessageClass().getSimpleName();
            VelocityContext context = newVelocityContext();
            context.put("module", module);
            context.put("model", model);
            Writer writer = newWriter(module, outputClassname + ".java");
            try
            {
                ENGINE.mergeTemplate(getTemplateResource(), module.getEncoding(), context, writer);
            }
            finally
            {
                writer.close();
            }
        }        
    }

}
