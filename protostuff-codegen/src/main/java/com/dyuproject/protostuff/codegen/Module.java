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

/**
 * @author David Yu
 * @created Oct 15, 2009
 */

public class Module
{
    
    private String fullClassName;
    private String outputPackage;
    private String outputClassName;
    private File outputDir;
    
    /**
     * @return the fullClassName
     */
    public String getFullClassName()
    {
        return fullClassName;
    }
    /**
     * @param fullClassName the fullClassName to set
     */
    public void setFullClassName(String fullClassName)
    {
        this.fullClassName = fullClassName;
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
     * @return the outputClassName
     */
    public String getOutputClassName()
    {
        return outputClassName;
    }
    /**
     * @param outputClassName the outputClassName to set
     */
    public void setOutputClassName(String outputClassName)
    {
        this.outputClassName = outputClassName;
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

}
