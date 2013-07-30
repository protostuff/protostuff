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

package com.dyuproject.protostuff.parser;

/**
 * An entity whose values can be overriden with new value.
 * It can also be reset to its initial state.
 *
 * @author David Yu
 * @created May 10, 2010
 */
public class Mutable<T>
{
    
    private T current, last;
    
    public Mutable(T value)
    {
        if(value == null)
            throw new IllegalArgumentException("The arg 'value' cannot be null.");
        
        current = value;
    }

    public void override(T newValue)
    {
        // can only override once.
        // needs to be reset before it can be overriden again.
        if(last == null)
        {
            last = current;
            current = newValue;
        }

    }
    
    public void reset()
    {
        if(last != null)
        {
            current = last;
            last = null;
        }
    }
    
    public T getValue()
    {
        return current;
    }
    
    public T getLast()
    {
        return last;
    }

}
