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
import java.io.Serializable;
import java.util.Properties;

/**
 * @author David Yu
 * @created Oct 15, 2009
 */

public class Module implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 8503087348372596796L;
    
    private String fullClassname;
    private String outputPackage;
    private String generator;
    private String encoding;

    private File outputDir;
    
    private Properties options = new Properties();
    
    public Module()
    {
        
    }
    
    public Module(String fullClassname, String outputPackage, 
            String generator, String encoding, File outputDir)
    {
        this.fullClassname = fullClassname;
        this.outputPackage = outputPackage;
        this.generator = generator;
        this.encoding = encoding;
        this.outputDir = outputDir;
    }
    
    public Module(String fullClassname, String outputPackage, 
            String generator, String encoding, File outputDir, Properties options)
    {
        this(fullClassname, outputPackage, generator, encoding, outputDir);
        this.options = options;
    }
    
    /**
     * @return the fullClassName
     */
    public String getFullClassname()
    {
        return fullClassname;
    }
    /**
     * @param fullClassName the fullClassName to set
     */
    public void setFullClassname(String fullClassname)
    {
        this.fullClassname = fullClassname;
    }
    /**
     * @return the outputPackage
     */
    public String getOutputPackage()
    {
        return outputPackage;
    }
    /**
     * @param outputPackage the outputPackage to set
     */
    public void setOutputPackage(String outputPackage)
    {
        this.outputPackage = outputPackage;
    }

    /**
     * @return the generator
     */
    public String getGenerator()
    {
        return generator;
    }

    /**
     * @param generator the generator to set
     */
    public void setGenerator(String generator)
    {
        this.generator = generator;
    }
    
    /**
     * @return the encoding
     */
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
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
     * @param outputDir the outputDir to set
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

}
