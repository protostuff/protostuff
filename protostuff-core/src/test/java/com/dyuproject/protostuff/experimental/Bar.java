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

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.IOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

/**
 * Bar - for testing
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Bar implements Message<Bar>, Schema<Bar>, Serializable
{
    
    private static final HashMap<String,Integer> __fieldMap = new HashMap<String,Integer>();    
    static
    {
        __fieldMap.put("someInt", 1);
        __fieldMap.put("someString", 2);
        __fieldMap.put("baz", 3);
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
    private Baz baz;
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
        this.baz = baz;
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
    public Baz getBaz()
    {
        return baz;
    }

    /**
     * @param baz the someBaz to set
     */
    public void setBaz(Baz baz)
    {
        this.baz = baz;
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

    public Schema<Bar> cachedSchema()
    {
        return this;
    }

    public Bar newMessage()
    {
        return new Bar();
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

    public void mergeFrom(Input input, Bar message) throws IOException
    {
        while(true)
        {
            int number = input.readFieldNumber(this);
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
                    Baz baz = message.baz;
                    if(baz==null)
                        baz = message.baz = new Baz();
                    input.mergeMessage(baz);
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

    public void writeTo(Output output, Bar message) throws IOException
    {
        int someInt = message.someInt;
        if(someInt != 0)
            output.writeInt32(1, someInt);
        
        String someString = message.someString;
        if(someString != null)
            output.writeString(2, someString);
        
        Baz baz = message.baz;
        if(baz != null)
            output.writeMessage(3, baz);
        
        Status someEnum = message.someEnum;
        if(someEnum != null) 
            output.writeEnum(4, someEnum.number);
        
        ByteString someBytes = message.someBytes;
        if(someBytes != null)
            output.writeBytes(5, someBytes);
        
        boolean someBoolean = message.someBoolean;
        if(someBoolean)
            output.writeBool(6, someBoolean);
        
        float someFloat = message.someFloat;
        if(someFloat != 0f)
            output.writeFloat(7, someFloat);
        
        double someDouble = message.someDouble;
        if(someDouble != 0d)
            output.writeDouble(8, someDouble);
        
        long someLong = message.someLong;
        if(someLong != 0l)
            output.writeInt64(9, someLong);
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
        byte[] data = IOUtil.toByteArrayDeferred(this);
        out.writeInt(data.length);
        out.write(data);
        out.close();
    }

}
