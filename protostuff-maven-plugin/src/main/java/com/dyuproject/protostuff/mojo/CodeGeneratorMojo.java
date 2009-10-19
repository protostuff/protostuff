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

package com.dyuproject.protostuff.mojo;

import java.io.File;
import java.io.FileInputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.dyuproject.protostuff.codegen.GeneratorMain;

/**
 * Generates code for protobuf-json, gwt-json
 * The protobuf generated module/classes are required 
 * to be on the classpath on execution.
 * 
 * @author David Yu
 * @created Oct 17, 2009
 * @goal codegen
 * @requiresDependencyResolution runtime
 */

public class CodeGeneratorMojo extends AbstractMojo
{
    
    /**
     * The properties file that contains the modules
     *
     * @parameter
     */
    private File modulesFile;    
    
    /**
     * The modules to generate code from
     *
     * @parameter
     */
    private Module[] modules;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if(modulesFile==null)
        {
            if(modules==null)
            {
                throw new MojoExecutionException("Either <modulesFile> or <modules> " +
                		"should be provided.");
            }
            try
            {
                for(Module m : modules)
                    GeneratorMain.generateFrom(m);
            }
            catch(Exception e)
            {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }
        else
        {            
            try
            {
                if(modules!=null)
                {
                    for(Module m : modules)
                        GeneratorMain.generateFrom(m);
                }
                
                if(!modulesFile.exists())
                    throw new MojoExecutionException(modulesFile + " does not exist.");
                
                GeneratorMain.generateFrom(GeneratorMain.loadModules(
                        new FileInputStream(modulesFile)));
            }
            catch(Exception e)
            {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }
    }

}
