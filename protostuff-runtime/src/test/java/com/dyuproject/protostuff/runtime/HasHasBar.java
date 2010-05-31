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

package com.dyuproject.protostuff.runtime;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.dyuproject.protostuff.IOUtil;

/**
 * Ser/deser test object that wraps an object {@link HasBar} without any schema.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public final class HasHasBar implements Externalizable
{
    
    private String name;
    private HasBar hasBar;
    
    public HasHasBar()
    {
        
    }

    public HasHasBar(String name, HasBar hasBar)
    {
        this.name = name;
        this.hasBar = hasBar;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the hasBar
     */
    public HasBar getHasBar()
    {
        return hasBar;
    }

    /**
     * @param hasBar the hasBar to set
     */
    public void setHasBar(HasBar hasBar)
    {
        this.hasBar = hasBar;
    }

    public void readExternal(ObjectInput in) throws IOException
    {
        IOUtil.mergeDelimitedFrom(in, this, RuntimeSchema.getSchema(HasHasBar.class));
    }
    
    public void writeExternal(ObjectOutput out) throws IOException
    {
        IOUtil.writeDelimitedTo(out, this, RuntimeSchema.getSchema(HasHasBar.class));
    }
    
    

}
