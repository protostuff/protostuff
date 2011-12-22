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
import java.util.Map;

/**
 * An entity that containts annotations.
 *
 * @author David Yu
 * @created Dec 30, 2010
 */
public interface HasAnnotations
{
    
    /**
     * Adds the annoation.
     */
    public void add(Annotation annotation);
    
    /**
     * Adds all the annotations to this container.
     * If {@code clearSource} is true, the {@code source} arg is cleared.
     */
    public boolean addAnnotations(Map<String,Annotation> source, boolean clearSource);
    
    /**
     * Gets the annotation map.
     */
    public Map<String,Annotation> getAnnotationMap();
    
    /**
     * Gets the annotations.
     */
    public Collection<Annotation> getAnnotations();
    
    /**
     * Gets the annotation by key/name.
     */
    public Annotation getAnnotation(String name);

}
