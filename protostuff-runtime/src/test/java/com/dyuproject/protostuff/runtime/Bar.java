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

}
