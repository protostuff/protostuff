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
import org.apache.maven.project.MavenProject;

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
     * The current Maven project.
     *
     * @parameter default-value="${project}"
     * @readonly
     * @required
     * @since 1.0.1
     */
    protected MavenProject project;

    /**
     * When {@code true}, skip the execution.
     *
     * @parameter expression="${protostuff.compiler.skip}" default-value="false"
     * @since 1.0.1
     */
    private boolean skip;

    /**
     * Usually most of protostuff mojos will not get executed on parent poms
     * (i.e. projects with packaging type 'pom').
     * Setting this parameter to {@code true} will force
     * the execution of this mojo, even if it would usually get skipped in this case.
     *
     * @parameter expression="${protostuff.compiler.force}" default-value="false"
     * @required
     * @since 1.0.1
     */
    private boolean forceMojoExecution;

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
    
    
    /**
     * @parameter expression="${project.basedir}"
     * @required
     */
    protected File baseDir;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (skipMojo())
        {
            return;
        }

        assert baseDir != null && baseDir.exists() && baseDir.isDirectory();
        
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
                {
                    if(!CompilerMain.isAvailableOutput(m.getOutput()) && 
                            !baseDir.getAbsoluteFile().equals(
                                    new File(".").getAbsoluteFile()))
                    {
                        // custom stg output executed on a child pom
                        try
                        {
                            File relativePath = new File(baseDir, m.getOutput());
                            if(relativePath.exists())
                            {
                                // update the path module.
                                m.setOutput(relativePath.getCanonicalPath());
                            }
                        }
                        catch(Exception e)
                        {
                            // ignore ... <output> might be an absolute path
                        }
                    }
                    CompilerMain.compile(m);

                    // enabled by default unless overridden
                    if(m.isAddToCompileSourceRoot())
                    {
                        // Include generated directory to the list of compilation sources
                        project.addCompileSourceRoot(m.getOutputDir().getAbsolutePath());
                    }
                }
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
                    for (ProtoModule m : protoModules)
                    {
                        CompilerMain.compile(m);

                        // enabled by default unless overridden
                        if(m.isAddToCompileSourceRoot())
                        {
                            // Include generated directory to the list of compilation sources
                            project.addCompileSourceRoot(m.getOutputDir().getAbsolutePath());
                        }
                    }
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

    /**
     * <p>Determine if the mojo execution should get skipped.</p>
     * This is the case if:
     * <ul>
     * <li>{@link #skip} is <code>true</code></li>
     * <li>if the mojo gets executed on a project with packaging type 'pom' and
     * {@link #forceMojoExecution} is <code>false</code></li>
     * </ul>
     *
     * @return <code>true</code> if the mojo execution should be skipped.
     * @since 1.0.1
     */
    protected boolean skipMojo() {
        if (skip) 
        {
            getLog().info("Skipping protostuff mojo execution");
            return true;
        }

        if (!forceMojoExecution && "pom".equals(this.project.getPackaging())) 
        {
            getLog().info("Skipping protostuff mojo execution for project with packaging type 'pom'");
            return true;
        }

        return false;
    }
}
