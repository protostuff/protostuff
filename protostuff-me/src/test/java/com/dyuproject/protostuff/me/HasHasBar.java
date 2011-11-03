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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Ser/deser test object that wraps an object {@link HasBar} without any schema.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public final class HasHasBar implements Message, Schema, Externalizable
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

    public Schema cachedSchema()
    {
        return this;
    }

    public String getFieldName(int number)
    {
        return String.valueOf(number);
    }

    public int getFieldNumber(String name)
    {
        return Integer.parseInt(name);
    }

    public boolean isInitialized(Object messageObj)
    {
        HasHasBar message = (HasHasBar)messageObj;
        return message.hasBar != null;
    }

    public HasHasBar newMessage()
    {
        return new HasHasBar();
    }
    
    public Class typeClass()
    {
        return HasHasBar.class;
    }
    
    public String messageName()
    {
        return "HasHasBar";
    }
    
    public String messageFullName()
    {
        return getClass().getName();
    }

    public void mergeFrom(Input input, Object messageObj) throws IOException
    {
        HasHasBar message = (HasHasBar)messageObj;
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.name = input.readString();
                    break;
                case 2:
                    message.hasBar = readHasBar(input);
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }
    }

    public void writeTo(Output output, Object messageObj) throws IOException
    {
        HasHasBar message = (HasHasBar)messageObj;
        if(message.name!=null)
            output.writeString(1, message.name, false);
        writeHasBar(output, 2, message.hasBar, false);
    }
    
    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }
    
    static HasBar readHasBar(Input input) throws IOException
    {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
                input.readByteArray()));
        try
        {
            return (HasBar)ois.readObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        finally
        {
            ois.close();
        }
    }
    
    static void writeHasBar(Output output, int fieldNumber, HasBar hasBar, boolean repeated) 
    throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        try
        {
            oos.writeObject(hasBar);
            output.writeByteArray(fieldNumber, baos.toByteArray(), repeated);
        }
        finally
        {
            oos.close();
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hasBar == null) ? 0 : hasBar.hashCode());
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
        HasHasBar other = (HasHasBar)obj;
        if (hasBar == null)
        {
            if (other.hasBar != null)
                return false;
        }
        else if (!hasBar.equals(other.hasBar))
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
