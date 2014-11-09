//========================================================================
//Copyright 2012 David Yu
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

import io.protostuff.runtime.PolymorphicSchema.Handler;

/**
 * Wraps a delegate.
 * 
 * @author David Yu
 * @created Dec 5, 2012
 */
public class HasDelegate<T> implements PolymorphicSchema.Factory
{

    public final Delegate<T> delegate;

    public final ArraySchemas.Base genericElementSchema;

    @SuppressWarnings("unchecked")
    public HasDelegate(Delegate<T> delegate)
    {
        this.delegate = delegate;

        genericElementSchema = new ArraySchemas.DelegateArray(null,
                (Delegate<Object>) delegate);
    }

    /**
     * Returns the delegate.
     */
    public final Delegate<T> getDelegate()
    {
        return delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final PolymorphicSchema newSchema(Class<?> typeClass,
            IdStrategy strategy, Handler handler)
    {
        return new ArraySchemas.DelegateArray(handler,
                (Delegate<Object>) delegate);
    }

}
