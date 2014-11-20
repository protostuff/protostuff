//================================================================================
//Copyright (c) 2011, David Yu
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

package io.protostuff.parser;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A sort of runtime error mechanism for st code generation to fail fast.
 * 
 * @author David Yu
 * @created Nov 1, 2011
 */
public final class ErrorMap implements Map<String, Object>
{

    static final ErrorMap INSTANCE = new ErrorMap();

    private ErrorMap()
    {
    }

    @Override
    public void clear()
    {

    }

    @Override
    public boolean containsKey(Object arg0)
    {
        // error mechanism called by stringtemplate
        throw new IllegalStateException(String.valueOf(arg0));
    }

    @Override
    public Object get(Object arg0)
    {
        return containsKey(arg0);
    }

    @Override
    public boolean containsValue(Object arg0)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public Set<String> keySet()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object put(String arg0, Object arg1)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> arg0)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object arg0)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size()
    {
        return 1;
    }

    @Override
    public Collection<Object> values()
    {
        throw new UnsupportedOperationException();
    }

}
