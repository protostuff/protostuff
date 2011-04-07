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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;

/**
 * Bar - for testing
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Bar implements Message, Schema, Externalizable
{
    
    static final Bar DEFAULT_INSTANCE = new Bar();
    
    public static Bar getSchema()
    {
        return DEFAULT_INSTANCE;
    }
    
    private static final HashMap<String,Integer> __fieldMap = new HashMap<String,Integer>();    
    static
    {
        __fieldMap.put("someInt", 1);
        __fieldMap.put("someString", 2);
        __fieldMap.put("someBaz", 3);
        __fieldMap.put("someEnum", 4);
        __fieldMap.put("someBytes", 5);
        __fieldMap.put("someBoolean", 6);
        __fieldMap.put("someFloat", 7);
        __fieldMap.put("someDouble", 8);
        __fieldMap.put("someLong", 9);
    }
    
    public enum Status
    {
        PENDING(0), STARTED(1), COMPLETED(2);
        
        public final int number;
        
        Status(int number)
        {
            this.number = number;
        }       
        
        public int getNumber()
        {
            return number;
        }
        
        public static Status valueOf(int number)
        {
            switch(number)
            {
                case 0:
                    return PENDING;
                case 1:
                    return STARTED;
                case 2:
                    return COMPLETED;
            }
            return null;
        }
    }
    
    private int someInt;
    private String someString;
    private Baz someBaz;
    private Status someEnum;
    private ByteString someBytes;
    private boolean someBoolean;
    private float someFloat;
    private double someDouble;
    private long someLong;
    
    public Bar()
    {
        
    }

    public Bar(
            int someInt, 
            String someString, 
            Baz baz, 
            Status someEnum, 
            ByteString someBytes, 
            boolean someBoolean, 
            float someFloat, 
            double someDouble, 
            long someLong)
    {
        this.someInt = someInt;
        this.someString = someString;
        this.someBaz = baz;
        this.someEnum = someEnum;
        this.someBytes = someBytes;
        this.someBoolean = someBoolean;
        this.someFloat = someFloat;
        this.someDouble = someDouble;
        this.someLong = someLong;
    }

    

    /**
     * @return the someInt
     */
    public int getSomeInt()
    {
        return someInt;
    }

    /**
     * @param someInt the someInt to set
     */
    public void setSomeInt(int someInt)
    {
        this.someInt = someInt;
    }

    /**
     * @return the someString
     */
    public String getSomeString()
    {
        return someString;
    }

    /**
     * @param someString the someString to set
     */
    public void setSomeString(String someString)
    {
        this.someString = someString;
    }

    /**
     * @return the someBaz
     */
    public Baz getSomeBaz()
    {
        return someBaz;
    }

    /**
     * @param baz the someBaz to set
     */
    public void setSomeBaz(Baz baz)
    {
        this.someBaz = baz;
    }

    /**
     * @return the someEnum
     */
    public Status getSomeEnum()
    {
        return someEnum;
    }

    /**
     * @param someEnum the someEnum to set
     */
    public void setSomeEnum(Status someEnum)
    {
        this.someEnum = someEnum;
    }

    /**
     * @return the someBytes
     */
    public ByteString getSomeBytes()
    {
        return someBytes;
    }

    /**
     * @param someBytes the someBytes to set
     */
    public void setSomeBytes(ByteString someBytes)
    {
        this.someBytes = someBytes;
    }

    /**
     * @return the someBoolean
     */
    public boolean getSomeBoolean()
    {
        return someBoolean;
    }

    /**
     * @param someBoolean the someBoolean to set
     */
    public void setSomeBoolean(boolean someBoolean)
    {
        this.someBoolean = someBoolean;
    }

    /**
     * @return the someFloat
     */
    public float getSomeFloat()
    {
        return someFloat;
    }

    /**
     * @param someFloat the someFloat to set
     */
    public void setSomeFloat(float someFloat)
    {
        this.someFloat = someFloat;
    }

    /**
     * @return the someDouble
     */
    public double getSomeDouble()
    {
        return someDouble;
    }

    /**
     * @param someDouble the someDouble to set
     */
    public void setSomeDouble(double someDouble)
    {
        this.someDouble = someDouble;
    }

    /**
     * @return the someLong
     */
    public long getSomeLong()
    {
        return someLong;
    }

    /**
     * @param someLong the someLong to set
     */
    public void setSomeLong(long someLong)
    {
        this.someLong = someLong;
    }

    public Schema cachedSchema()
    {
        return this;
    }
    
    public boolean isInitialized(Object message)
    {
        return true;
    }

    public Bar newMessage()
    {
        return new Bar();
    }
    
    public Class<Bar> typeClass()
    {
        return Bar.class;
    }
    
    public String messageName()
    {
        return getClass().getSimpleName();
    }
    
    public String messageFullName()
    {
        return getClass().getName();
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
            case 1:
                return "someInt";
            case 2:
                return "someString";
            case 3:
                return "someBaz";
            case 4:
                return "someEnum";
            case 5:
                return "someBytes";
            case 6:
                return "someBoolean";
            case 7:
                return "someFloat";
            case 8:
                return "someDouble";
            case 9:
                return "someLong";
            default:
                return null;
        }
    }

    public int getFieldNumber(String name)
    {
        Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }
    
    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    public void writeTo(Output output, Object messageObj) throws IOException
    {
        Bar message = (Bar)messageObj;
        if(message.someInt != 0)
            output.writeInt32(1, message.someInt, false);
        
        if(message.someString != null)
            output.writeString(2, message.someString, false);
        
        if(message.someBaz != null)
            output.writeObject(3, message.someBaz, Baz.getSchema(), false);
        
        if(message.someEnum != null) 
            output.writeEnum(4, message.someEnum.number, false);
        
        if(message.someBytes != null)
            output.writeBytes(5, message.someBytes, false);
        
        if(message.someBoolean)
            output.writeBool(6, message.someBoolean, false);
        
        if(message.someFloat != 0f)
            output.writeFloat(7, message.someFloat, false);
        
        if(message.someDouble != 0d)
            output.writeDouble(8, message.someDouble, false);
        
        if(message.someLong != 0l)
            output.writeInt64(9, message.someLong, false);
    }

    public void mergeFrom(Input input, Object messageObj) throws IOException
    {
        Bar message = (Bar)messageObj;
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.someInt = input.readInt32();
                    break;
                case 2:
                    message.someString = input.readString();
                    break;
                case 3:
                    message.someBaz = (Baz)input.mergeObject(message.someBaz, Baz.getSchema());
                    break;
                case 4:
                    message.someEnum = Status.valueOf(input.readEnum());
                    break;
                case 5:
                    message.someBytes = input.readBytes();
                    break;
                case 6:
                    message.someBoolean = input.readBool();
                    break;
                case 7:
                    message.someFloat = input.readFloat();
                    break;
                case 8:
                    message.someDouble = input.readDouble();
                    break;
                case 9:
                    message.someLong = input.readInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }
    }
    
    static final Pipe.Schema PIPE_SCHEMA = new Pipe.Schema(DEFAULT_INSTANCE)
    {

        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(wrappedSchema);; number = input.readFieldNumber(wrappedSchema))
            {
                switch(number)
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
                        output.writeObject(number, pipe, Baz.getPipeSchema(), false);
                        break;
                    case 4:
                        output.writeEnum(number, input.readEnum(), false);
                        break;
                    case 5:
                        input.transferByteRangeTo(output, false, number, false);
                        break;
                    case 6:
                        output.writeBool(number, input.readBool(), false);
                        break;
                    case 7:
                        output.writeFloat(number, input.readFloat(), false);
                        break;
                    case 8:
                        output.writeDouble(number, input.readDouble(), false);
                        break;
                    case 9:
                        output.writeInt64(number, input.readInt64(), false);
                        break;
                    default:
                        input.handleUnknownField(number, wrappedSchema);
                }
            }
        }
    };
    
    public static Pipe.Schema getPipeSchema()
    {
        return PIPE_SCHEMA;
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((someBaz == null) ? 0 : someBaz.hashCode());
        result = prime * result + (someBoolean ? 1231 : 1237);
        result = prime * result + ((someBytes == null) ? 0 : someBytes.hashCode());
        long temp;
        temp = Double.doubleToLongBits(someDouble);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        result = prime * result + ((someEnum == null) ? 0 : someEnum.hashCode());
        result = prime * result + Float.floatToIntBits(someFloat);
        result = prime * result + someInt;
        result = prime * result + (int)(someLong ^ (someLong >>> 32));
        result = prime * result + ((someString == null) ? 0 : someString.hashCode());
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
        Bar other = (Bar)obj;
        if (someBaz == null)
        {
            if (other.someBaz != null)
                return false;
        }
        else if (!someBaz.equals(other.someBaz))
            return false;
        if (someBoolean != other.someBoolean)
            return false;
        if (someBytes == null)
        {
            if (other.someBytes != null)
                return false;
        }
        else if (!someBytes.equals(other.someBytes))
            return false;
        if (Double.doubleToLongBits(someDouble) != Double.doubleToLongBits(other.someDouble))
            return false;
        if (someEnum == null)
        {
            if (other.someEnum != null)
                return false;
        }
        else if (!someEnum.equals(other.someEnum))
            return false;
        if (Float.floatToIntBits(someFloat) != Float.floatToIntBits(other.someFloat))
            return false;
        if (someInt != other.someInt)
            return false;
        if (someLong != other.someLong)
            return false;
        if (someString == null)
        {
            if (other.someString != null)
                return false;
        }
        else if (!someString.equals(other.someString))
            return false;
        return true;
    }
    
    
}
