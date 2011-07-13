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

/**
 * Specifically for the maven-plugin.  This allows you to not specify 
 * implementation="com.dyuproject.protostuff.protocompiler.ProtoModule"
 * on your <pre><protoModule></protoModule></pre> tags
 *
 * @author David Yu
 * @created Jan 14, 2010
 */
public class ProtoModule extends com.dyuproject.protostuff.compiler.ProtoModule
{
    
    private static final long serialVersionUID = -1491907620177689686L;

    private boolean addToCompileSourceRoot = true;

    /**
     * @return the addToCompileSourceRoot
     */
    public boolean isAddToCompileSourceRoot()
    {
        return addToCompileSourceRoot;
    }

    /**
     * Set this to true to include the output dir to the list of compilation sources.
     * 
     * @param addToCompileSourceRoot the addToCompileSourceRoot to set
     */
    public void setAddToCompileSourceRoot(boolean addToCompileSourceRoot)
    {
        this.addToCompileSourceRoot = addToCompileSourceRoot;
    }
    
    

}
