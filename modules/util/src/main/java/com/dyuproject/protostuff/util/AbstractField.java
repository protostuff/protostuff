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

package com.dyuproject.protostuff.util;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;


/**
 * @author David Yu
 * @created Aug 23, 2009
 */

public abstract class AbstractField
{
    
    public static final Object[] NO_ARG = new Object[]{};
    public static final Integer ZERO_COUNT = Integer.valueOf(0);
    public static final Class<?>[] NO_ARG_C = new Class<?>[]{};
    public static final Class<?>[] INT_ARG_C = new Class<?>[]{int.class};
    public static final Class<?>[] ITERABLE_ARG_C = new Class<?>[]{Iterable.class};
    
    static String toPrefixedPascalCase(String prefix, String target)
    {
        char c = target.charAt(0);
        if(c>96)
            c = (char)(c-32);
        
        return new StringBuilder(prefix.length() + target.length())
            .append(prefix)
            .append(c)
            .append(target, 1, target.length())
            .toString();
    }
    
    static String toCamelCase(int start, String target)
    {
        char[] prop = new char[target.length()-start];
        target.getChars(start, target.length(), prop, 0);
        if(prop[0]<91)
            prop[0] = (char)(prop[0] + 32);

        return new String(prop);
    }
    
    private Meta _meta;
    
    public AbstractField(Meta meta)
    {
        _meta = meta;
    }
    
    public Meta getMeta()
    {
        return _meta;
    }
    
    public interface Meta
    {
        public Class<? extends AbstractMessageLite> getMessageClass();
        public Class<? extends Builder<?>> getBuilderClass();
        public Class<?> getTypeClass();
        public Class<?> getComponentTypeClass();
        public int getNumber();
        public boolean isRepeated();
        public boolean isMessage();
        public String getName();
        public String getNormalizedName();
    }

}
