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

package io.protostuff.mojo;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Joiner;

import io.protostuff.compiler.CachingProtoLoader;
import io.protostuff.compiler.CompilerMain;

/**
 * Compiles proto files to java/gwt/etc.
 * 
 * @author David Yu
 */
@Mojo(
        name = "compile",
        configurator = "include-project-dependencies",
        requiresDependencyResolution = COMPILE_PLUS_RUNTIME)
public class ProtoCompilerMojo extends AbstractMojo
{

    public static final String GENERATE_TEST_SOURCES_PHASE = "generate-test-sources";
    /**
     * The current Maven project.
     */
    @Parameter(property = "project", required = true, readonly = true)
    protected MavenProject project;

    /**
     * When {@code true}, skip the execution.
     *
     * @since 1.0.1
     */
    @Parameter(property = "protostuff.compiler.skip", defaultValue = "false")
    private boolean skip;

    /**
     * When {@code true}, the protos are cached for re-use. This matters when a certain proto is also used/imported by
     * other modules.
     *
     * @since 1.0.5
     */
    @Parameter(property = "protostuff.compiler.cache_protos", defaultValue = "false")
    private boolean cacheProtos;

    /**
     * Usually most of protostuff mojos will not get executed on parent poms (i.e. projects with packaging type 'pom').
     * Setting this parameter to {@code true} will force the execution of this mojo, even if it would usually get
     * skipped in this case.
     *
     * @since 1.0.1
     */
    @Parameter(property = "protostuff.compiler.force", defaultValue = "false", required = true)
    private boolean forceMojoExecution;

    /**
     * The properties file that contains the modules
     *
     */
    @Parameter
    protected File modulesFile;

    /**
     * If not specified, the directory where the file is located will be used as its base dir.
     * <p>
     * This is only relevent when {@link #modulesFile is provided}.
     * 
     * @since 1.0.8
     */
    @Parameter
    protected File sourceBaseDir;

    /**
     * If not specified, the directory where the file is located will be used as its base dir.
     * <p>
     * This is only relevent when {@link #modulesFile is provided}.
     * 
     * @since 1.0.8
     */
    @Parameter
    protected File outputBaseDir;

    /**
     * The modules to generate code from
     * 
     */
    @Parameter
    protected ProtoModule[] protoModules;

    /**
     * Maven module base directory
     */
    @Parameter(property = "project.basedir", required = true)
    protected File baseDir;

    /**
     * Plugin properties that are passed to the compiler
     *
     * @since 1.3.1
     */
    @Parameter
    protected Properties properties;

    @Component
    private MojoExecution execution;


    private Properties systemPropertiesBackup;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (skipMojo())
        {
            return;
        }

        assert baseDir != null && baseDir.exists() && baseDir.isDirectory();

        CachingProtoLoader loader = cacheProtos ? new CachingProtoLoader() : null;

        try
        {
            setSystemProperties();
            if (modulesFile == null)
            {
                if (protoModules == null)
                {
                    throw new MojoExecutionException("Either <modules> or <modulesFile> " +
                            "should be provided.");
                }
                try
                {
                    for (ProtoModule m : protoModules)
                    {
                        m.setCachingProtoLoader(loader);
                        updateRelativeOutputLocation(m);
                        CompilerMain.compile(m);

                        // enabled by default unless overridden
                        if (m.isAddToCompileSourceRoot())
                        {
                            // Include generated directory to the list of compilation sources
                            if (GENERATE_TEST_SOURCES_PHASE.equals(execution.getLifecyclePhase()))
                            {
                                project.addTestCompileSourceRoot(m.getOutputDir().getAbsolutePath());
                            }
                            else
                            {
                                project.addCompileSourceRoot(m.getOutputDir().getAbsolutePath());
                            }
                        }

                    }
                }
                catch (Exception e)
                {
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }
            else
            {
                try
                {
                    if (protoModules != null)
                    {
                        for (ProtoModule m : protoModules)
                        {
                            m.setCachingProtoLoader(loader);
                            CompilerMain.compile(m);

                            // enabled by default unless overridden
                            if (m.isAddToCompileSourceRoot())
                            {
                                // Include generated directory to the list of compilation sources
                                if (GENERATE_TEST_SOURCES_PHASE.equals(execution.getLifecyclePhase()))
                                {
                                    project.addTestCompileSourceRoot(m.getOutputDir().getAbsolutePath());
                                }
                                else
                                {
                                    project.addCompileSourceRoot(m.getOutputDir().getAbsolutePath());
                                }
                            }
                        }
                    }

                    if (!modulesFile.exists())
                        throw new MojoExecutionException(modulesFile + " does not exist.");

                    File parent = modulesFile.getParentFile();
                    File sourceBaseDir = this.sourceBaseDir, outputBaseDir = this.outputBaseDir;

                    if (sourceBaseDir == null)
                        sourceBaseDir = parent;

                    if (outputBaseDir == null)
                        outputBaseDir = parent;

                    CompilerMain.compile(CompilerMain.loadModules(modulesFile,
                            sourceBaseDir, outputBaseDir));
                }
                catch (Exception e)
                {
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }
        }
        finally
        {
            resetSystemProperties();
        }
    }

    private void updateRelativeOutputLocation(ProtoModule m)
    {
        String originalOutput = m.getOutput();
        String[] outputList = CompilerMain.COMMA.split(originalOutput);
        List<String> result = new ArrayList<>();
        for (String output : outputList)
        {
            output = output.trim();
            boolean standardOutput = CompilerMain.isAvailableOutput(output);
            File basedir = baseDir.getAbsoluteFile();
            File currentDir = new File(".").getAbsoluteFile();
            if (!standardOutput)
            {
                // custom stg
                try
                {
                    File absolutePath = new File(output);
                    if (!absolutePath.exists())
                    {
                        File relativePath = new File(baseDir, output);
                        if (relativePath.exists())
                        {
                            // set full path to the stg
                            output = relativePath.getCanonicalPath();
                        }
                    }
                }
                catch (Exception e)
                {
                    getLog().debug("Can not determine full path for output=" + output, e);
                }
            }
            result.add(output);
        }
        String updatedOutput = Joiner.on(',').join(result);
        m.setOutput(updatedOutput);
    }

    private void setSystemProperties()
    {
        systemPropertiesBackup = System.getProperties();
        if (properties != null)
        {
            for (Object o : properties.keySet())
            {
                String key = (String) o;
                String value = properties.getProperty(key);

                System.setProperty(key, value);
                getLog().info("Set property: " + key + " = '" + value + "'");
            }
        }
    }

    private void resetSystemProperties()
    {
        System.setProperties(systemPropertiesBackup);
    }

    /**
     * <p>
     * Determine if the mojo execution should get skipped.
     * </p>
     * This is the case if:
     * <ul>
     * <li>{@link #skip} is <code>true</code></li>
     * <li>if the mojo gets executed on a project with packaging type 'pom' and {@link #forceMojoExecution} is
     * <code>false</code></li>
     * </ul>
     *
     * @return <code>true</code> if the mojo execution should be skipped.
     * @since 1.0.1
     */
    protected boolean skipMojo()
    {
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
