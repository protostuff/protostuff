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

package io.protostuff.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.Bar.Status;

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

    public PojoWithArrayAndSet(Set<Status> someEnumAsSet,
            Status[] someEnumAsArray, Set<Bar> someBarAsSet,
            Bar[] someBarAsArray, Set<Float> someFloatAsSet,
            Float[] someFloatAsArray, Double[] someDoubleAsArray,
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
     * @param someEnumAsSet
     *            the someEnumAsSet to set
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
     * @param someEnumAsArray
     *            the someEnumAsArray to set
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
     * @param someBarAsSet
     *            the someBarAsSet to set
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
     * @param someBarAsArray
     *            the someBarAsArray to set
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
     * @param someFloatAsSet
     *            the someFloatAsSet to set
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
     * @param someFloatAsArray
     *            the someFloatAsArray to set
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
     * @param someDoubleAsArray
     *            the someDoubleAsArray to set
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
     * @param somePrimitiveDoubleAsArray
     *            the somePrimitiveDoubleAsArray to set
     */
    public void setSomePrimitiveDoubleAsArray(
            double[] somePrimitiveDoubleAsArray)
    {
        this.somePrimitiveDoubleAsArray = somePrimitiveDoubleAsArray;
    }

    private void readObject(ObjectInputStream in) throws IOException
    {
        int length = in.readInt();
        byte[] data = new byte[length];
        for (int offset = 0; length > 0; length -= offset)
            offset = in.read(data, offset, length);

        in.close();
        ProtostuffIOUtil.mergeFrom(data, this,
                RuntimeSchema.getSchema(PojoWithArrayAndSet.class));
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        byte[] data = ProtostuffIOUtil.toByteArray(this,
                RuntimeSchema.getSchema(PojoWithArrayAndSet.class),
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        out.writeInt(data.length);
        out.write(data);
        out.close();
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(someBarAsArray);
        result = prime * result
                + ((someBarAsSet == null) ? 0 : someBarAsSet.hashCode());
        result = prime * result + Arrays.hashCode(someDoubleAsArray);
        result = prime * result + Arrays.hashCode(someEnumAsArray);
        result = prime * result
                + ((someEnumAsSet == null) ? 0 : someEnumAsSet.hashCode());
        result = prime * result + Arrays.hashCode(someFloatAsArray);
        result = prime * result
                + ((someFloatAsSet == null) ? 0 : someFloatAsSet.hashCode());
        result = prime * result + Arrays.hashCode(somePrimitiveDoubleAsArray);
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
        PojoWithArrayAndSet other = (PojoWithArrayAndSet) obj;
        if (!Arrays.equals(someBarAsArray, other.someBarAsArray))
            return false;
        if (someBarAsSet == null)
        {
            if (other.someBarAsSet != null)
                return false;
        }
        else if (!someBarAsSet.equals(other.someBarAsSet))
            return false;
        if (!Arrays.equals(someDoubleAsArray, other.someDoubleAsArray))
            return false;
        if (!Arrays.equals(someEnumAsArray, other.someEnumAsArray))
            return false;
        if (someEnumAsSet == null)
        {
            if (other.someEnumAsSet != null)
                return false;
        }
        else if (!someEnumAsSet.equals(other.someEnumAsSet))
            return false;
        if (!Arrays.equals(someFloatAsArray, other.someFloatAsArray))
            return false;
        if (someFloatAsSet == null)
        {
            if (other.someFloatAsSet != null)
                return false;
        }
        else if (!someFloatAsSet.equals(other.someFloatAsSet))
            return false;
        if (!Arrays.equals(somePrimitiveDoubleAsArray,
                other.somePrimitiveDoubleAsArray))
            return false;
        return true;
    }

}
