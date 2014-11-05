//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

/**
 *
 */
package io.protostuff.runtime;

import java.lang.reflect.Constructor;

/**
 * This class is expected not to load unless {@link RuntimeEnv} made sure that sun.reflect.ReflectionFactory is in the
 * classpath.
 * 
 * @author David Yu
 * @created Jul 8, 2011
 */
final class OnDemandSunReflectionFactory
{

    private OnDemandSunReflectionFactory()
    {
    }

    @SuppressWarnings("unchecked")
    static <T> Constructor<T> getConstructor(Class<T> clazz,
            Constructor<Object> constructor)
    {
        return (Constructor<T>) sun.reflect.ReflectionFactory
                .getReflectionFactory().newConstructorForSerialization(clazz,
                        constructor);
    }

}
