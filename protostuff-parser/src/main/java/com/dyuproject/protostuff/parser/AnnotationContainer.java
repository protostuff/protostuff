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

package com.dyuproject.protostuff.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for components that contain annotations.
 *
 * @author David Yu
 * @created Dec 30, 2010
 */
public abstract class AnnotationContainer implements HasAnnotations, HasProto
{
    
    final LinkedHashMap<String,Annotation> annotations = 
        new LinkedHashMap<String,Annotation>();

    public void add(Annotation annotation)
    {
        if(annotations.put(annotation.name, annotation) != null)
            throw err("Duplicate annotation: " + annotation.name, null);
    }

    public Map<String, Annotation> getAnnotationMap()
    {
        return annotations;
    }
    
    /**
     * Short-hand for {@link #getAnnotationMap()}.
     * 
     * You then can use:
     * <pre>
     * &lt;if(message.a.("SomeAnnotation"))&gt;
     * </pre>
     */
    public final Map<String, Annotation> getA()
    {
        return annotations;
    } 

    public Collection<Annotation> getAnnotations()
    {
        return annotations.values();
    }
    
    public Annotation getAnnotation(String name)
    {
        return annotations.get(name);
    }

    public boolean addAnnotations(Map<String, Annotation> source, boolean clearSource)
    {
        if(source.isEmpty())
            return false;
        
        this.annotations.putAll(source);
        if(clearSource)
            source.clear();
        
        return true;
    }
    
    /**
     * Shorthand for annotations.isEmpty().
     * 
     * <pre>
     * You can then use:
     * &lt;if(message.emptyA)&gt;
     * </pre>
     * 
     * <pre>
     * Note that this does not work on stringtemplate: 
     * &lt;if(message.annotationMap.empty)&gt;
     * 
     * Even though {@link java.util.Map#isEmpty()} exists. 
     * </pre>
     * 
     * 
     */
    public final boolean isEmptyA()
    {
        return annotations.isEmpty();
    }
    
    public static IllegalStateException err(String msg, Proto proto)
    {
        if(proto == null)
            return new IllegalStateException(msg);
        
        return new IllegalStateException(msg + " [" + proto.getSourcePath() + "]");
    }

}
