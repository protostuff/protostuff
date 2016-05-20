//================================================================================
//Copyright (c) 2012, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================

package io.protostuff.runtime;

import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.runtime.PolymorphicSchema.Handler;

/**
 * Wraps a schema.
 */
public abstract class HasSchema<T> implements PolymorphicSchema.Factory
{

    public final IdStrategy strategy;
    
    public final ArraySchemas.Base genericElementSchema;
    
    /**
     * Gets the schema.
     */
    public abstract Schema<T> getSchema();

    /**
     * Gets the pipe schema.
     */
    public abstract Pipe.Schema<T> getPipeSchema();

    // for the array of this type

    @SuppressWarnings("unchecked")
    protected HasSchema(IdStrategy strategy)
    {
        this.strategy = strategy;
        
        genericElementSchema = new ArraySchemas.PojoArray(strategy, 
                null, (HasSchema<Object>) this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PolymorphicSchema newSchema(Class<?> typeClass, IdStrategy strategy,
            Handler handler)
    {
        return new ArraySchemas.PojoArray(strategy, handler, (HasSchema<Object>) this);
    }

}