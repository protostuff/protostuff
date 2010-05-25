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
import java.util.Set;

import com.dyuproject.protostuff.IOUtil;
import com.dyuproject.protostuff.runtime.Bar.Status;

/**
 * PojoWithArrayAndSet - for testing
 *
 * @author David Yu
 * @created Nov 20, 2009
 */
public final class PojoWithArrayAndSet implements Serializable
{
    
    private Set<Status> someEnumAsSet;
    private Status[] someEnumAsArray;
    
    private Set<Bar> someBarAsSet;
    private Bar[] someBarAsArray;
    
    private Set<Float> someFloatAsSet;
    private Float[] someFloatAsArray;
    
    private Double[] someDoubleAsArray;
    private double[] somePrimitiveDoubleAsArray;
    
    
    public PojoWithArrayAndSet()
    {
        
    }

    public PojoWithArrayAndSet(
            Set<Status> someEnumAsSet, 
            Status[] someEnumAsArray, 
            Set<Bar> someBarAsSet, 
            Bar[] someBarAsArray, 
            Set<Float> someFloatAsSet,
            Float[] someFloatAsArray, 
            Double[] someDoubleAsArray, 
            double[] somePrimitiveDoubleAsArray)
    {
        this.someEnumAsSet = someEnumAsSet;
        this.someEnumAsArray = someEnumAsArray;
        this.someBarAsSet = someBarAsSet;
        this.someBarAsArray = someBarAsArray;
        this.someFloatAsSet = someFloatAsSet;
        this.someFloatAsArray = someFloatAsArray;
        this.someDoubleAsArray = someDoubleAsArray;
        this.somePrimitiveDoubleAsArray = somePrimitiveDoubleAsArray;
    }

    /**
     * @return the someEnumAsSet
     */
    public Set<Bar.Status> getSomeEnumAsSet()
    {
        return someEnumAsSet;
    }

    /**
     * @param someEnumAsSet the someEnumAsSet to set
     */
    public void setSomeEnumAsSet(Set<Bar.Status> someEnumAsSet)
    {
        this.someEnumAsSet = someEnumAsSet;
    }

    /**
     * @return the someEnumAsArray
     */
    public Bar.Status[] getSomeEnumAsArray()
    {
        return someEnumAsArray;
    }

    /**
     * @param someEnumAsArray the someEnumAsArray to set
     */
    public void setSomeEnumAsArray(Bar.Status[] someEnumAsArray)
    {
        this.someEnumAsArray = someEnumAsArray;
    }

    /**
     * @return the someBarAsSet
     */
    public Set<Bar> getSomeBarAsSet()
    {
        return someBarAsSet;
    }

    /**
     * @param someBarAsSet the someBarAsSet to set
     */
    public void setSomeBarAsSet(Set<Bar> someBarAsSet)
    {
        this.someBarAsSet = someBarAsSet;
    }

    /**
     * @return the someBarAsArray
     */
    public Bar[] getSomeBarAsArray()
    {
        return someBarAsArray;
    }

    /**
     * @param someBarAsArray the someBarAsArray to set
     */
    public void setSomeBarAsArray(Bar[] someBarAsArray)
    {
        this.someBarAsArray = someBarAsArray;
    }

    /**
     * @return the someFloatAsSet
     */
    public Set<Float> getSomeFloatAsSet()
    {
        return someFloatAsSet;
    }

    /**
     * @param someFloatAsSet the someFloatAsSet to set
     */
    public void setSomeFloatAsSet(Set<Float> someFloatAsSet)
    {
        this.someFloatAsSet = someFloatAsSet;
    }

    /**
     * @return the someFloatAsArray
     */
    public Float[] getSomeFloatAsArray()
    {
        return someFloatAsArray;
    }

    /**
     * @param someFloatAsArray the someFloatAsArray to set
     */
    public void setSomeFloatAsArray(Float[] someFloatAsArray)
    {
        this.someFloatAsArray = someFloatAsArray;
    }

    /**
     * @return the someDoubleAsArray
     */
    public Double[] getSomeDoubleAsArray()
    {
        return someDoubleAsArray;
    }

    /**
     * @param someDoubleAsArray the someDoubleAsArray to set
     */
    public void setSomeDoubleAsArray(Double[] someDoubleAsArray)
    {
        this.someDoubleAsArray = someDoubleAsArray;
    }

    /**
     * @return the somePrimitiveDoubleAsArray
     */
    public double[] getSomePrimitiveDoubleAsArray()
    {
        return somePrimitiveDoubleAsArray;
    }

    /**
     * @param somePrimitiveDoubleAsArray the somePrimitiveDoubleAsArray to set
     */
    public void setSomePrimitiveDoubleAsArray(double[] somePrimitiveDoubleAsArray)
    {
        this.somePrimitiveDoubleAsArray = somePrimitiveDoubleAsArray;
    }

    private void readObject(ObjectInputStream in) throws IOException
    {
        int length = in.readInt();
        byte[] data = new byte[length];
        for(int offset = 0; length > 0; length -= offset)
            offset = in.read(data, offset, length);
        
        in.close();
        IOUtil.mergeFrom(data, this, RuntimeSchema.getSchema(PojoWithArrayAndSet.class));
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        byte[] data = IOUtil.toByteArray(this, RuntimeSchema.getSchema(PojoWithArrayAndSet.class));
        out.writeInt(data.length);
        out.write(data);
        out.close();
    }

}
