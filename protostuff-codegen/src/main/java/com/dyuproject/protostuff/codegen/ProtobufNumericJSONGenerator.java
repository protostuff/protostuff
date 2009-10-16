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
 * @created Oct 14, 2009
 */

public class ProtobufNumericJSONGenerator extends ProtobufJSONGenerator
{
    
    static final String TEMPLATE_LOCATION = "protobuf_numeric_json.vm";
    
    protected String getDefaultOutputClassName(String moduleClassName)
    {
        return moduleClassName + "NumericJSON";
    }
    
    protected String getTemplateLocation()
    {
        return TEMPLATE_LOCATION;
    }
    
    public static void main(String[] args) throws Exception
    {
        File outputDir = new File("src/test/java");
        if(!outputDir.exists())
            throw new IllegalStateException("outputDir " + outputDir + "  does not exist");

        Module module = new Module();
        module.setFullClassName("com.dyuproject.protostuff.codegen.benchmark.V22LiteMedia");
        module.setOutputPackage("com.dyuproject.protostuff.codegen.benchmark");
        module.setOutputDir(outputDir);
        
        ProtobufNumericJSONGenerator generator = new ProtobufNumericJSONGenerator();
        generator.generateFrom(module);
    }

}
