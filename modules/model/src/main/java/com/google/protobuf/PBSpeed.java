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


package com.google.protobuf;

import java.io.IOException;
import java.io.OutputStream;

import com.dyuproject.protostuff.model.ParameterType;
import com.google.protobuf.GeneratedMessage.Builder;

/**
 * @author David Yu
 * @created Aug 26, 2009
 */

public abstract class PBSpeed
{

    public static final ParameterType BUILDER_TO_MESSAGE = new ParameterType()
    {
        public Object resolveValue(Object builder)
        {
            return getGeneratedMessage((Builder<?>)builder);
        }
    };
    
    public static GeneratedMessage getGeneratedMessage(Builder<?> builder)
    {
        return builder.internalGetResult();
    }
    
    public static byte[] toByteArray(Builder<?> builder)
    {
        return builder.internalGetResult().toByteArray();
    }
    
    public static void writeTo(CodedOutputStream output, Builder<?> builder) 
    throws IOException
    {
        builder.internalGetResult().writeTo(output);
    }    
    
    public static void writeTo(OutputStream output, Builder<?> builder) 
    throws IOException
    {
        builder.internalGetResult().writeTo(output);
    }
    
    public static void writeDelimitedTo(OutputStream output, Builder<?> builder) 
    throws IOException
    {
        builder.internalGetResult().writeDelimitedTo(output);
    }

}
