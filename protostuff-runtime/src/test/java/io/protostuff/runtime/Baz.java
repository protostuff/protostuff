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

package io.protostuff.runtime;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import io.protostuff.ProtostuffIOUtil;

/**
 * Baz - for testing
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Baz implements Externalizable
{

    private int id;
    private String name;
    private long timestamp;

    public Baz()
    {

    }

    public Baz(int id, String name, long timestamp)
    {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
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
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException
    {
        ProtostuffIOUtil.mergeDelimitedFrom(in, this,
                RuntimeSchema.getSchema(Baz.class));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        ProtostuffIOUtil.writeDelimitedTo(out, this,
                RuntimeSchema.getSchema(Baz.class));
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Baz other = (Baz) obj;
        if (id != other.id)
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (timestamp != other.timestamp)
            return false;
        return true;
    }

}
