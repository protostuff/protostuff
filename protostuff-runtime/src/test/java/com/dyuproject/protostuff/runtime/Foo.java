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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.ProtostuffIOUtil;

/**
 * Foo - for testing
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Foo implements Externalizable
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
    
    public void readExternal(ObjectInput in) throws IOException
    {
        ProtostuffIOUtil.mergeDelimitedFrom(in, this, RuntimeSchema.getSchema(Foo.class));
    }
    
    public void writeExternal(ObjectOutput out) throws IOException
    {
        ProtostuffIOUtil.writeDelimitedTo(out, this, RuntimeSchema.getSchema(Foo.class));
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((someBar == null) ? 0 : someBar.hashCode());
        result = prime * result + ((someBoolean == null) ? 0 : someBoolean.hashCode());
        result = prime * result + ((someBytes == null) ? 0 : someBytes.hashCode());
        result = prime * result + ((someDouble == null) ? 0 : someDouble.hashCode());
        result = prime * result + ((someEnum == null) ? 0 : someEnum.hashCode());
        result = prime * result + ((someFloat == null) ? 0 : someFloat.hashCode());
        result = prime * result + ((someInt == null) ? 0 : someInt.hashCode());
        result = prime * result + ((someLong == null) ? 0 : someLong.hashCode());
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
        Foo other = (Foo)obj;
        if (someBar == null)
        {
            if (other.someBar != null)
                return false;
        }
        else if (!someBar.equals(other.someBar))
            return false;
        if (someBoolean == null)
        {
            if (other.someBoolean != null)
                return false;
        }
        else if (!someBoolean.equals(other.someBoolean))
            return false;
        if (someBytes == null)
        {
            if (other.someBytes != null)
                return false;
        }
        else if (!someBytes.equals(other.someBytes))
            return false;
        if (someDouble == null)
        {
            if (other.someDouble != null)
                return false;
        }
        else if (!someDouble.equals(other.someDouble))
            return false;
        if (someEnum == null)
        {
            if (other.someEnum != null)
                return false;
        }
        else if (!someEnum.equals(other.someEnum))
            return false;
        if (someFloat == null)
        {
            if (other.someFloat != null)
                return false;
        }
        else if (!someFloat.equals(other.someFloat))
            return false;
        if (someInt == null)
        {
            if (other.someInt != null)
                return false;
        }
        else if (!someInt.equals(other.someInt))
            return false;
        if (someLong == null)
        {
            if (other.someLong != null)
                return false;
        }
        else if (!someLong.equals(other.someLong))
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
