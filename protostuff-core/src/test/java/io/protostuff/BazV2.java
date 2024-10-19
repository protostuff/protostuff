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

package io.protostuff;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;

/**
 * Baz - for testing
 * 
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class BazV2 implements Message<BazV2>, Schema<BazV2>, Externalizable
{

    static final BazV2 DEFAULT_INSTANCE = new BazV2();

    public static Schema<BazV2> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    private static final HashMap<String, Integer> __fieldMap = new HashMap<String, Integer>();

    static
    {
        __fieldMap.put("id", 1);
        __fieldMap.put("name", 2);
        __fieldMap.put("timestamp", 3);
        __fieldMap.put("newvalue", 4);
    }

    private int id;
    private String name;
    private long timestamp;
    private String newvalue;

    public BazV2()
    {

    }

    public BazV2(
            int id,
            String name,
            long timestamp,
            String newvalue)
    {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.newvalue = newvalue;
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

    /**
     * @return the newvalue
     */
    public String getNewvalue()
    {
        return newvalue;
    }

    /**
     * @param newvalue
     *            the newvalue to set
     */
    public void setNewvalue(String newvalue)
    {
        this.newvalue = newvalue;
    }

    @Override
    public Schema<BazV2> cachedSchema()
    {
        return this;
    }

    @Override
    public boolean isInitialized(BazV2 message)
    {
        return true;
    }

    @Override
    public BazV2 newMessage()
    {
        return new BazV2();
    }

    @Override
    public Class<BazV2> typeClass()
    {
        return BazV2.class;
    }

    @Override
    public String messageName()
    {
        return "Baz";
    }

    @Override
    public String messageFullName()
    {
        return Baz.class.getName();
    }

    @Override
    public String getFieldName(int number)
    {
        switch (number)
        {
            case 1:
                return "id";
            case 2:
                return "name";
            case 3:
                return "timestamp";
            case 4:
                return "newvalue";
            default:
                return null;
        }
    }

    @Override
    public int getFieldNumber(String name)
    {
        Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    @Override
    public void writeTo(Output output, BazV2 message) throws IOException
    {
        if (message.id != 0)
            output.writeInt32(1, message.id, false);

        if (message.name != null)
            output.writeString(2, message.name, false);

        if (message.timestamp != 0l)
            output.writeInt64(3, message.timestamp, false);

        if (message.newvalue != null)
            output.writeString(4, message.newvalue, false);
    }

    @Override
    public void mergeFrom(Input input, BazV2 message) throws IOException
    {
        for (int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch (number)
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
                case 4:
                    message.newvalue = input.readString();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }
    }

    static final Pipe.Schema<BazV2> PIPE_SCHEMA = new Pipe.Schema<BazV2>(DEFAULT_INSTANCE)
    {

        @Override
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for (int number = input.readFieldNumber(wrappedSchema);; number = input.readFieldNumber(wrappedSchema))
            {
                switch (number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeInt32(number, input.readInt32(), false);
                        break;
                    case 2:
                        input.transferByteRangeTo(output, true, number, false);
                        break;
                    case 3:
                        output.writeInt64(number, input.readInt64(), false);
                        break;
                    default:
                        input.handleUnknownField(number, wrappedSchema);
                }
            }

        }

    };

    public static Pipe.Schema<BazV2> getPipeSchema()
    {
        return PIPE_SCHEMA;
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        result = prime * result + ((newvalue == null) ? 0 : newvalue.hashCode());
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
        BazV2 other = (BazV2) obj;
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

    @Override
    public String toString()
    {
        return "Baz [id=" + id + ", name=" + name + ", timestamp=" + timestamp + "]";
    }

}
