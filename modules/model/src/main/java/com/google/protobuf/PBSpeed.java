//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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
