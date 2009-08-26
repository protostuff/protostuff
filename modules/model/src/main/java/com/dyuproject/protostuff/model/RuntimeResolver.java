//================================================================================
//Copyright (c) 2009, David Yu
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


package com.dyuproject.protostuff.model;

import com.google.protobuf.AbstractMessageLite;

/**
 * @author David Yu
 * @created Aug 27, 2009
 */

public abstract class RuntimeResolver
{
    
    public static final RuntimeResolver MESSAGE_SPEED, MESSAGE_LITE_RUNTIME;
    
    static
    {
        MESSAGE_LITE_RUNTIME = com.google.protobuf.PBLiteRuntime.MESSAGE_RESOLVER;
        Class<?> clazz = null;
        try
        {
            
            clazz = Class.forName("com.google.protobuf.GeneratedMessage", false, 
                    Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            
        }
        if(clazz==null)
        {
            MESSAGE_SPEED = new RuntimeResolver()
            {                
                public Object resolveValue(Object value, PropertyMeta meta)
                {
                    return ((AbstractMessageLite.Builder<?>)value).build();                    
                }
            };
        }
        else
            MESSAGE_SPEED = com.google.protobuf.PBSpeed.MESSAGE_RESOLVER;
    }
    
    public static final Factory DEFAULT_FACTORY = new Factory()
    {
        public RuntimeResolver create(PropertyMeta propertyMeta)
        {
            
            return null;
        }        
    };
    
    public abstract Object resolveValue(Object value, PropertyMeta meta);
    
    public interface Factory
    {
        public RuntimeResolver create(PropertyMeta propertyMeta);
    }
    


}
