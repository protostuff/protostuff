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

package com.dyuproject.protostuff.compiler;

import java.io.IOException;

/**
 * Proto compiler.
 *
 * @author David Yu
 * @created Jan 4, 2010
 */
public interface ProtoCompiler
{
    
    /**
     * The unique id that basically is the target output.
     */
    public String getOutputId();
    /**
     * Compiles the proto configured/encapsulated in the module.
     */
    public void compile(ProtoModule module) throws IOException;

}
