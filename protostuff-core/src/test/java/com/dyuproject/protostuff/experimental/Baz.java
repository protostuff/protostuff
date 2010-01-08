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

package com.dyuproject.protostuff.experimental;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import com.dyuproject.protostuff.IOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;


/**
 * Baz - for testing
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Baz implements Message<Baz>, Schema<Baz>, Serializable
{
    
    private static final HashMap<String,Integer> __fieldMap = new HashMap<String,Integer>();    
    static
    {
        __fieldMap.put("id", 1);
        __fieldMap.put("name", 2);
        __fieldMap.put("timestamp", 3);
        __fieldMap.put("hasBar", 4);
    }
    
    private int id;
    private String name;
    private long timestamp;
    
    public Baz()
    {
        
    }
    
    public Baz(
            int id, 
            String name, 
            long timestamp)
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
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public Schema<Baz> cachedSchema()
    {        
        return this;
    }
    
    public boolean isInitialized(Baz message)
    {
        return true;
    }

    public Baz newMessage()
    {
        return new Baz();
    }
    
    public Class<Baz> typeClass()
    {
        return Baz.class;
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
            case 1:
                return "id";
            case 2:
                return "name";
            case 3:
                return "timestamp";
            case 4:
                return "hasBar";
            default:
                return null;
        }
    }

    public int getFieldNumber(String name)
    {        
        Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    public void mergeFrom(Input input, Baz message) throws IOException
    {
        while(true)
        {
            int number = input.readFieldNumber(this);
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.id = input.readInt32();
                    break;
                case 2:
                    message.name = input.readString();
                    break;
                case 3:
                    message.timestamp = input.readInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }
    }

    public void writeTo(Output output, Baz message) throws IOException
    {
        int id = message.id;
        if(id != 0)
            output.writeInt32(1, id, false);
        
        String name = message.name;
        if(name != null)
            output.writeString(2, name, false);
        
        long timestamp = message.timestamp;
        if(timestamp != 0l)
            output.writeInt64(3, timestamp, false);
    }
    
    private void readObject(ObjectInputStream in) throws IOException
    {
        int length = in.readInt();
        byte[] data = new byte[length];
        for(int offset = 0; length > 0; length -= offset)
            offset = in.read(data, offset, length);
        IOUtil.mergeFrom(data, this);
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        byte[] data = IOUtil.toByteArray(this);
        out.writeInt(data.length);
        out.write(data);
    }

}
