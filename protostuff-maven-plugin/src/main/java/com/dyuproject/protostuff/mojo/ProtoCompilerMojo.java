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

package com.dyuproject.protostuff.mojo;

import java.io.File;
import java.io.FileInputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.dyuproject.protostuff.compiler.CompilerMain;

/**
 * Compiles proto files to java/gwt/etc.
 *
 * @author David Yu
 * @created Jan 14, 2010
 * @goal compile
 * @requiresDependencyResolution runtime
 */
public class ProtoCompilerMojo extends AbstractMojo
{
    /**
     * The properties file that contains the modules
     *
     * @parameter
     */
    protected File modulesFile;    
    
    /**
     * The modules to generate code from
     *
     * @parameter
     */
    protected ProtoModule[] protoModules;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if(modulesFile==null)
        {
            if(protoModules==null)
            {
                throw new MojoExecutionException("Either <modules> or <modulesFile> " +
                                "should be provided.");
            }
            try
            {
                for(ProtoModule m : protoModules)
                    CompilerMain.compile(m);
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
                if(protoModules!=null)
                {
                    for(ProtoModule m : protoModules)
                        CompilerMain.compile(m);
                }
                
                if(!modulesFile.exists())
                    throw new MojoExecutionException(modulesFile + " does not exist.");
                
                CompilerMain.compile(CompilerMain.loadModules(
                        new FileInputStream(modulesFile)));
            }
            catch(Exception e)
            {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }
    }
}
