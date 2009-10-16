//========================================================================
//Copyright 2007-2008 David Yu dyuproject@gmail.com
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

import junit.framework.TestCase;

/**
 * @author David Yu
 * @created Oct 2, 2009
 */

public class BenchmarkTest extends TestCase
{
    
    static final int RUNS = Integer.getInteger("benchmark.runs", 1);
    
    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;
        
        BenchmarkRunner runner = new BenchmarkRunner();
        BenchmarkMain.configure(runner);        
        for(int i=0; i<RUNS; i++)
        {
            runner.start();
        }
    }
    
}
