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

package com.dyuproject.protostuff.me;

import java.io.Serializable;

/**
 * Ser/deser test object that wraps a message {@link Bar}.
 *
 * @author David Yu
 * @created Nov 12, 2009
 */
public class HasBar implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private Bar bar;

    public HasBar()
    {
        
    }
    
    public HasBar(int id, String name, Bar bar)
    {
        this.id = id;
        this.name = name;
        this.bar = bar;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
        this.id = id;
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
     * @return the bar
     */
    public Bar getBar()
    {
        return bar;
    }

    /**
     * @param bar the bar to set
     */
    public void setBar(Bar bar)
    {
        this.bar = bar;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bar == null) ? 0 : bar.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HasBar other = (HasBar)obj;
        if (bar == null)
        {
            if (other.bar != null)
                return false;
        }
        else if (!bar.equals(other.bar))
            return false;
        if (id != other.id)
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    

}
