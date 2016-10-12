//========================================================================
//Copyright 2016 David Yu
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


/**
 * Read/write from/to fields using reflection.
 * 
 * @author David Yu
 * @created Oct 11, 2016
 */
public final class ReflectAccessor extends Accessor
{
    static final Accessor.Factory FACTORY = new Factory()
    {
        public Accessor create(java.lang.reflect.Field f)
        {
            return new ReflectAccessor(f);
        }
    };
    
    
    public ReflectAccessor(java.lang.reflect.Field f)
    {
        super(f);
        f.setAccessible(true);
    }
    
    @Override
    public void set(Object owner, Object value)
    {
        try
        {
            f.set(owner, value);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object owner)
    {
        try
        {
            return (T)f.get(owner);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}