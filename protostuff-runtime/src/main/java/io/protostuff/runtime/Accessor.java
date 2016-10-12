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
 * Used for (speeding up deser on) repeated/collection fields.
 * 
 * @author David Yu
 * @created Oct 11, 2016
 */
public abstract class Accessor
{
    public interface Factory
    {
        Accessor create(java.lang.reflect.Field f);
    }
    
    public final java.lang.reflect.Field f;
    
    protected Accessor(java.lang.reflect.Field f)
    {
        this.f = f;
    }
    
    /**
     * Set the field value.
     */
    public abstract void set(Object owner, Object value);
    
    /**
     * Get the field value.
     */
    public abstract <T> T get(Object owner);
}
