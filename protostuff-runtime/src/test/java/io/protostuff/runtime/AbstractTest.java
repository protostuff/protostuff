//========================================================================
//Copyright 2015 David Yu
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

package io.protostuff.runtime;

import io.protostuff.Schema;

/**
 * Allows any subclass to supply a different IdStrategy.
 * 
 * @author David Yu
 * @created Oct 7, 2019
 */
public abstract class AbstractTest extends io.protostuff.AbstractTest
{
    private IdStrategy strategy;

    @Override
    protected void setUp() throws Exception
    {
        strategy = newIdStrategy();
        super.setUp();
    }
    
    protected IdStrategy newIdStrategy()
    {
        return RuntimeEnv.ID_STRATEGY;
    }
    
    protected <T> Schema<T> getSchema(Class<T> clazz)
    {
        return RuntimeSchema.getSchema(clazz, strategy);
    }
    

}
