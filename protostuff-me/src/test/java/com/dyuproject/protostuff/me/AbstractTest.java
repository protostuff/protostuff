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

package com.dyuproject.protostuff.me;

import java.util.Vector;

import junit.framework.TestCase;

/**
 * Helper class for tests.
 *
 * @author David Yu
 * @created Oct 8, 2010
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
    
    static int getInteger(String propertyName, int defaultIfNone)
    {
        String prop = System.getProperty(propertyName);
        return prop == null ? defaultIfNone : Integer.parseInt(prop);
    }
    
    public static boolean isEqual(Vector v1, Vector v2)
    {
        if(v1 == null && v2 == null)
            return true;
        
        if(v1 == null || v2 == null)
            return false;
        
        if(v1 == v2)
            return true;
        
        if(v1.size() != v2.size())
            return false;
        
        for(int i=0; i<v1.size(); i++)
        {
            if(!v1.elementAt(i).equals(v2.elementAt(i)))
                return false;
        }
        
        return true;
    }

}
