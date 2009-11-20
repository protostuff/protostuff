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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.dyuproject.protostuff.ByteString;

/**
 * Foo - for testing
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Foo implements Serializable
{

    
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
    
    private void readObject(ObjectInputStream in) throws IOException
    {
        int length = in.readInt();
        byte[] data = new byte[length];
        for(int offset = 0; length > 0; length -= offset)
            offset = in.read(data, offset, length);
        
        in.close();
        RuntimeIOUtil.mergeFrom(data, this);
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        byte[] data = RuntimeIOUtil.toByteArray(this);
        out.writeInt(data.length);
        out.write(data);
        out.close();
    }

}
