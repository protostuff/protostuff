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

import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.ProtostuffIOUtil;

/**
 * Bar - for testing
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public final class Bar implements Externalizable
{
    
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
    
    public void readExternal(ObjectInput in) throws IOException
    {
        ProtostuffIOUtil.mergeDelimitedFrom(in, this, RuntimeSchema.getSchema(Bar.class));
    }
    
    public void writeExternal(ObjectOutput out) throws IOException
    {
        ProtostuffIOUtil.writeDelimitedTo(out, this, RuntimeSchema.getSchema(Bar.class));
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((baz == null) ? 0 : baz.hashCode());
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
        if (baz == null)
        {
            if (other.baz != null)
                return false;
        }
        else if (!baz.equals(other.baz))
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
