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

package io.protostuff.compiler;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

/**
 * Configuration for the proto w/c ontains the compile options and arguments.
 * 
 * @author David Yu
 * @created Jan 5, 2010
 */
public class ProtoModule implements Serializable
{

    private static final long serialVersionUID = 6231036933426077777L;

    private File source;
    private String output;
    private String encoding;
    private File outputDir;

    private Properties options = new Properties();
    Properties config;

    private CachingProtoLoader protoLoader;

    private HashMap<String, Object> attributes = new HashMap<String, Object>();

    public ProtoModule()
    {

    }

    public ProtoModule(File source, String output, String encoding, File outputDir)
    {
        super();
        this.source = source;
        this.output = output;
        this.encoding = encoding;
        this.outputDir = outputDir;
    }

    /**
     * @return the source
     */
    public File getSource()
    {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(File source)
    {
        this.source = source;
    }

    /**
     * @return the output
     */
    public String getOutput()
    {
        return output;
    }

    /**
     * @param output
     *            the output to set
     */
    public void setOutput(String output)
    {
        this.output = output;
    }

    /**
     * @return the encoding
     */
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * @param encoding
     *            the encoding to set
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    /**
     * @return the outputDir
     */
    public File getOutputDir()
    {
        return outputDir;
    }

    /**
     * @param outputDir
     *            the outputDir to set
     */
    public void setOutputDir(File outputDir)
    {
        this.outputDir = outputDir;
    }

    /**
     * @return the options
     */
    public Properties getOptions()
    {
        return options;
    }

    /**
     * @param options
     */
    public void setOptions(Properties options)
    {
        this.options.putAll(options);
    }

    public String getOption(String key)
    {
        return options.getProperty(key);
    }

    public void setOption(String key, String value)
    {
        options.setProperty(key, value);
    }

    public CachingProtoLoader getCachingProtoLoader()
    {
        return protoLoader;
    }

    public void setCachingProtoLoader(CachingProtoLoader protoLoader)
    {
        this.protoLoader = protoLoader;
    }

    public Properties getConfig()
    {
        return config;
    }

    public void setAttribute(String key, Object value)
    {
        attributes.put(key, value);
    }

    /**
     * Alias for {@link #getAttributes()}.
     */
    public HashMap<String, Object> getAttrs()
    {
        return attributes;
    }

    public HashMap<String, Object> getAttributes()
    {
        return attributes;
    }
}
