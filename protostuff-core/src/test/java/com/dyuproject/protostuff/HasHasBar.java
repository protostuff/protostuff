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

package com.dyuproject.protostuff;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Ser/deser test object that wraps an object {@link HasBar} without any schema.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public final class HasHasBar implements Message<HasHasBar>, Schema<HasHasBar>, Serializable
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

    public Schema<HasHasBar> cachedSchema()
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

    public boolean isInitialized(HasHasBar message)
    {
        return message.hasBar != null;
    }

    public HasHasBar newMessage()
    {
        return new HasHasBar();
    }
    

    public void mergeFrom(Input input, HasHasBar message) throws IOException
    {
        while(true)
        {
            int number = input.readFieldNumber(this);
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.name = input.readString();
                    break;
                case 2:
                    message.hasBar = input.readPojo(HasBar.class);
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }
    }

    public void writeTo(Output output, HasHasBar message) throws IOException
    {
        output.writeString(1, message.name);
        output.writePojo(2, message.hasBar, HasBar.class);
    }
    
    private void readObject(ObjectInputStream in) throws IOException
    {
        int length = in.readInt();
        byte[] data = new byte[length];
        for(int offset = 0; length > 0; length -= offset)
            offset = in.read(data, offset, length);
        
        in.close();
        IOUtil.mergeFrom(data, this);
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        byte[] data = IOUtil.toByteArray(this);
        out.writeInt(data.length);
        out.write(data);
        out.close();
    }

}
