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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.model.Model;
import com.google.protobuf.AbstractMessageLite;

/**
 * @author David Yu
 * @created Oct 14, 2009
 */

public abstract class CodeGenerator
{

    @SuppressWarnings("unchecked")
    protected static void appendModel(Class<?> moduleClass, List<Model<?>> models)
    {
        Class<?>[] declaredClasses = moduleClass.getDeclaredClasses();
        if(declaredClasses.length==0)
            throw new IllegalStateException(moduleClass + " is not a protobuf generated module");
        
        for(Class<?> c : declaredClasses)
        {
            if(AbstractMessageLite.class.isAssignableFrom(c))
            {
                Class<? extends AbstractMessageLite> clazz = (Class<? extends AbstractMessageLite>)c;
                models.add(Model.get(clazz));
            }
        }
    }
    
    protected static Writer newWriter(Module module, String fileName) throws IOException
    {
        File packageDir = new File(module.getOutputDir(), module.getOutputPackage().replace('.', '/'));
        if(!packageDir.exists())
            packageDir.mkdirs();
        
        File outputFile = new File(packageDir, fileName);
        FileOutputStream out = new FileOutputStream(outputFile);
        return new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
    }
    
    public void generateFrom(Module module) throws Exception
    {
        if(module.getFullClassName()==null)
            throw new IllegalStateException("fullClassName is required.");
        
        if(module.getOutputPackage()==null)
            throw new IllegalStateException("outputPackage is required.");
        
        if(module.getOutputDir()==null)
            throw new IllegalStateException("outputDir is required.");
        
        if(module.getOutputClassName()==null)
        {
            String fullClassName = module.getFullClassName();
            String outputClassName = fullClassName.substring(fullClassName.lastIndexOf('.'));
            module.setOutputClassName(getDefaultOutputClassName(outputClassName));
        }        
        
        Class<?> moduleClass = null;
        try
        {
            moduleClass = Class.forName(module.getFullClassName(), true, 
                    Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        ArrayList<Model<?>> models = new ArrayList<Model<?>>();
        appendModel(moduleClass, models);
        if(models.size()==0)
            throw new IllegalStateException("No protobuf messages found.");
        
        if(!module.getOutputDir().exists())
            module.getOutputDir().mkdirs();
        
        generateFrom(module, models);
    }
    
    protected abstract String getDefaultOutputClassName(String moduleClassName);
    
    protected abstract void generateFrom(Module module, ArrayList<Model<?>> models) 
    throws Exception;

}
