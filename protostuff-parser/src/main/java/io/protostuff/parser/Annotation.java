//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

package io.protostuff.parser;

import java.util.LinkedHashMap;

/**
 * Annotation for messages, enums, services, rpc, fields
 * 
 * @author David Yu
 * @created Dec 30, 2010
 */
public class Annotation implements HasName
{

    final String name;

    final LinkedHashMap<String, Object> refs = new LinkedHashMap<>();
    final LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    public Annotation(String name)
    {
        this.name = name;
    }

    public LinkedHashMap<String, Object> getParams()
    {
        return params;
    }

    /**
     * Shorthand for {@link #getParams()}.
     * <p>
     * 
     * <pre>
     * If you have an annotation like &#64;Foo(id = 1), you then can use:
     * &lt;if(message.a.("Foo")&gt;
     * &lt;if(message.a.("Foo").p.("id")&gt;
     * ...
     * &lt;endif&gt;
     * &lt;endif&gt;
     * </pre>
     */
    public final LinkedHashMap<String, Object> getP()
    {
        return params;
    }

    void put(String key, Object value)
    {
        if (params.put(key, value) != null)
            throw new IllegalStateException("Duplicate annotation key: " + key);
    }

    void putRef(String key, Object value)
    {
        put(key, value);
        refs.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key)
    {
        return (T) params.get(key);
    }

    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Shorthand for params.isEmpty().
     * <p>
     * 
     * <pre>
     * You can then use:
     * &lt;if(message.a.("Foo").emptyP&gt;
     * </pre>
     * <p>
     * 
     * <pre>
     * Note that this does not work on stringtemplate:
     * &lt;if(message.a.("Foo").empty)&gt;
     * 
     * Even though {@link java.util.Map#isEmpty()} exists.
     * </pre>
     */
    public final boolean isEmptyP()
    {
        return params.isEmpty();
    }

    public String toString()
    {
        return new StringBuilder()
                .append("Annotation|")
                .append(name)
                .append('|')
                .append(params)
                .toString();
    }

}
