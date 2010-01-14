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

package com.dyuproject.protostuff.benchmark;

import java.net.URL;

import com.dyuproject.protostuff.benchmark.serializers.ProtostuffCoreSerializer;
import com.dyuproject.protostuff.benchmark.serializers.ProtostuffJsonSerializer;
import com.dyuproject.protostuff.benchmark.serializers.ProtostuffNumericJsonSerializer;
import com.dyuproject.protostuff.benchmark.serializers.ProtostuffRuntimeSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.GeneratedLiteNumericSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.GeneratedLiteSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.GeneratedSpeedNumericSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.GeneratedSpeedSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.ReflectionLiteNumericSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.ReflectionLiteSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.ReflectionSpeedNumericSerializer;
import com.dyuproject.protostuff.benchmark.serializers.model.ReflectionSpeedSerializer;
import com.dyuproject.protostuff.codegen.GeneratorMain;

/**
 * @author David Yu
 * @created Oct 16, 2009
 */

public class BenchmarkModules
{
    
    static final String RESOURCE_LOCATION = "benchmark_modules.properties";
    
    public static void main(String[] args) throws Exception
    {
        generateModules();
    }
    
    static void generateModules() throws Exception
    {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(RESOURCE_LOCATION);
        GeneratorMain.generateFrom(GeneratorMain.loadModules(resource.openStream()));
    }
    
    static void configure(BenchmarkRunner runner)
    {
        runner.addObjectSerializer(new ProtostuffCoreSerializer());
        runner.addObjectSerializer(new ProtostuffRuntimeSerializer());
        runner.addObjectSerializer(new ProtostuffJsonSerializer());
        runner.addObjectSerializer(new ProtostuffNumericJsonSerializer());
        
        runner.addObjectSerializer(new GeneratedSpeedSerializer());
        runner.addObjectSerializer(new ReflectionSpeedSerializer());
        runner.addObjectSerializer(new GeneratedLiteSerializer());
        runner.addObjectSerializer(new ReflectionLiteSerializer());
        
        runner.addObjectSerializer(new GeneratedSpeedNumericSerializer());
        runner.addObjectSerializer(new ReflectionSpeedNumericSerializer());
        runner.addObjectSerializer(new GeneratedLiteNumericSerializer());                       
        runner.addObjectSerializer(new ReflectionLiteNumericSerializer());
    }

}
