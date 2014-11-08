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

package io.protostuff;

import junit.framework.TestCase;

/**
 * Helper class for tests.
 * 
 * @author David Yu
 */
public abstract class AbstractTest extends TestCase
{

    static final int BUF_SIZE = 256;

    public static LinkedBuffer buf()
    {
        return LinkedBuffer.allocate(BUF_SIZE);
    }

    public static LinkedBuffer buf(int size)
    {
        return LinkedBuffer.allocate(size);
    }

}
