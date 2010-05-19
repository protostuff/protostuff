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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * Base class for code generators using velocity templates.
 * 
 * @author David Yu
 * @created Oct 16, 2009
 */

public abstract class VelocityCodeGenerator extends CodeGenerator
{
    
    public static final VelocityEngine ENGINE = new VelocityEngine();
    {
        ENGINE.setProperty(Velocity.RESOURCE_LOADER, "class");
        ENGINE.setProperty("class.resource.loader.class", 
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    }
    
    private String _id;
    private String _templateResource;
    
    public VelocityCodeGenerator(String id)
    {
        _id = id;
        
        String fullClassname = getClass().getName();
        String packageName = fullClassname.substring(0, 
                fullClassname.lastIndexOf('.')).replace('.', '/');        
        _templateResource = packageName + "/" + id + ".vm"; 
    }
    
    public String getId()
    {
        return _id;
    }
    
    public String getTemplateResource()
    {
        return _templateResource;
    }    

    public VelocityContext newVelocityContext()
    {
        VelocityContext context = new VelocityContext();        
        context.put("Util", VelocityUtil.class);        
        return context;
    }

}
