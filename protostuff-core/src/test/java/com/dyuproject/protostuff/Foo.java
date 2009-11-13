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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Foo - for testing
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Foo implements Message<Foo>, Schema<Foo>, Serializable
{
    
    private static final HashMap<String,Integer> __fieldMap = new HashMap<String,Integer>();    
    static
    {
        __fieldMap.put("someInt", 1);
        __fieldMap.put("someString", 2);
        __fieldMap.put("bar", 3);
        __fieldMap.put("someEnum", 4);
        __fieldMap.put("someBytes", 5);
        __fieldMap.put("someBoolean", 6);
        __fieldMap.put("someFloat", 7);
        __fieldMap.put("someDouble", 8);
        __fieldMap.put("someLong", 9);
    }
    
    public enum EnumSample
    {
        TYPE0(0), TYPE1(1), TYPE2(2), TYPE3(3), TYPE4(4);
        
        public final int number;
        
        EnumSample(int number)
        {
            this.number = number;
        }       
        
        public int getNumber()
        {
            return number;
        }
        
        public static EnumSample valueOf(int number)
        {
            switch(number)
            {
                case 0:
                    return TYPE0;
                case 1:
                    return TYPE1;
                case 2:
                    return TYPE2;
                case 3:
                    return TYPE3;
                case 4:
                    return TYPE4;
            }
            return null;
        }
    }
    
    private List<Integer> someInt;
    private List<String> someString;
    private List<Bar> someBar;
    private List<EnumSample> someEnum;
    private List<ByteString> someBytes;
    private List<Boolean> someBoolean;
    private List<Float> someFloat;
    private List<Double> someDouble;
    private List<Long> someLong;
    
    public Foo()
    {

    }

    public Foo(
            List<Integer> someInt, 
            List<String> someString, 
            List<Bar> someBar, 
            List<EnumSample> someEnum, 
            List<ByteString> someBytes,
            List<Boolean> someBoolean, 
            List<Float> someFloat, 
            List<Double> someDouble, 
            List<Long> someLong)
    {
        this.someInt = someInt;
        this.someString = someString;
        this.someBar = someBar;
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
    public List<Integer> getSomeInt()
    {
        return someInt;
    }

    /**
     * @param someInt the someInt to set
     */
    public void setSomeInt(List<Integer> someInt)
    {
        this.someInt = someInt;
    }

    /**
     * @return the someString
     */
    public List<String> getSomeString()
    {
        return someString;
    }

    /**
     * @param someString the someString to set
     */
    public void setSomeString(List<String> someString)
    {
        this.someString = someString;
    }

    /**
     * @return the someBar
     */
    public List<Bar> getSomeBar()
    {
        return someBar;
    }

    /**
     * @param someBar the someBar to set
     */
    public void setSomeBar(List<Bar> someBar)
    {
        this.someBar = someBar;
    }

    /**
     * @return the someEnum
     */
    public List<EnumSample> getSomeEnum()
    {
        return someEnum;
    }

    /**
     * @param someEnum the someEnum to set
     */
    public void setSomeEnum(List<EnumSample> someEnum)
    {
        this.someEnum = someEnum;
    }

    /**
     * @return the someBytes
     */
    public List<ByteString> getSomeBytes()
    {
        return someBytes;
    }

    /**
     * @param someBytes the someBytes to set
     */
    public void setSomeBytes(List<ByteString> someBytes)
    {
        this.someBytes = someBytes;
    }

    /**
     * @return the someBoolean
     */
    public List<Boolean> getSomeBoolean()
    {
        return someBoolean;
    }

    /**
     * @param someBoolean the someBoolean to set
     */
    public void setSomeBoolean(List<Boolean> someBoolean)
    {
        this.someBoolean = someBoolean;
    }

    /**
     * @return the someFloat
     */
    public List<Float> getSomeFloat()
    {
        return someFloat;
    }

    /**
     * @param someFloat the someFloat to set
     */
    public void setSomeFloat(List<Float> someFloat)
    {
        this.someFloat = someFloat;
    }

    /**
     * @return the someDouble
     */
    public List<Double> getSomeDouble()
    {
        return someDouble;
    }

    /**
     * @param someDouble the someDouble to set
     */
    public void setSomeDouble(List<Double> someDouble)
    {
        this.someDouble = someDouble;
    }

    /**
     * @return the someLong
     */
    public List<Long> getSomeLong()
    {
        return someLong;
    }

    /**
     * @param someLong the someLong to set
     */
    public void setSomeLong(List<Long> someLong)
    {
        this.someLong = someLong;
    }

    public Schema<Foo> cachedSchema()
    {
        return this;
    }

    public Foo newMessage()
    {
        return new Foo();
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
                return "bar";
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
        return number==null ? 0 : number.intValue();
    }

    public void mergeFrom(Input input, Foo message) throws IOException
    {
        while(true)
        {
            int number = input.readFieldNumber(this);
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    if(message.someInt==null)
                        message.someInt = new ArrayList<Integer>();
                    message.someInt.add(input.readInt32());
                    break;
                case 2:
                    if(message.someString==null)
                        message.someString = new ArrayList<String>();
                    message.someString.add(input.readString());
                    break;
                case 3:
                    if(message.someBar==null)
                        message.someBar = new ArrayList<Bar>();
                    message.someBar.add(input.mergeMessage(new Bar()));
                    break;
                case 4:
                    if(message.someEnum==null)
                        message.someEnum = new ArrayList<EnumSample>();
                    message.someEnum.add(EnumSample.valueOf(input.readEnum()));
                    break;
                case 5:
                    if(message.someBytes==null)
                        message.someBytes = new ArrayList<ByteString>();
                    message.someBytes.add(input.readBytes());
                    break;
                case 6:
                    if(message.someBoolean==null)
                        message.someBoolean = new ArrayList<Boolean>();
                    message.someBoolean.add(input.readBool());
                    break;
                case 7:
                    if(message.someFloat==null)
                        message.someFloat = new ArrayList<Float>();
                    message.someFloat.add(input.readFloat());
                    break;
                case 8:
                    if(message.someDouble==null)
                        message.someDouble = new ArrayList<Double>();
                    message.someDouble.add(input.readDouble());
                    break;
                case 9:
                    if(message.someLong==null)
                        message.someLong = new ArrayList<Long>();
                    message.someLong.add(input.readInt64());
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }        
    }

    public void writeTo(Output output, Foo message) throws IOException
    {
        if(message.someInt!=null)
        {
            for(int value : message.someInt)
                output.writeInt32(1, value);
        }
        if(message.someString!=null)
        {
            for(String value : message.someString)
                output.writeString(2, value);
        }
        if(message.someBar!=null)
        {
            for(Bar value : message.someBar)
                output.writeMessage(3, value);
        }
        if(message.someEnum!=null)
        {
            for(EnumSample value : message.someEnum)
                output.writeEnum(4, value.number);
        }
        if(message.someBytes!=null)
        {
            for(ByteString value : message.someBytes)
                output.writeBytes(5, value);
        }
        if(message.someBoolean!=null)
        {
            for(boolean value : message.someBoolean)
                output.writeBool(6, value);
        }
        if(message.someFloat!=null)
        {
            for(Float value : message.someFloat)
                output.writeFloat(7, value);
        }
        if(message.someDouble!=null)
        {
            for(Double value : message.someDouble)
                output.writeDouble(8, value);
        }
        if(message.someLong!=null)
        {
            for(Long value : message.someLong)
                output.writeInt64(9, value);
        }
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
