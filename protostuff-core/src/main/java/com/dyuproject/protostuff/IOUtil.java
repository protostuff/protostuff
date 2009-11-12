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

/**
 * Utility for the serialization/deserialization of messages and objects tied to a schema.
 *
 * @author David Yu
 * @created Nov 12, 2009
 */
public final class IOUtil
{
    
    private IOUtil(){}
    
    /**
     * Computes the buffer size and serializes the {@code object} into 
     * a byte array via {@link CodedOutput}.
     */
    public static <T> byte[] toByteArrayComputed(T object, Schema<T> schema)
    {
        return toByteArrayComputed(object, schema, false);
    }
    
    /**
     * Computes the buffer size and serializes the {@code object} into 
     * a byte array via {@link CodedOutput}.
     */
    public static <T> byte[] toByteArrayComputed(T object, Schema<T> schema, 
            boolean forceWritePrimitives)
    {
        try
        {
            ComputedSizeOutput sizeCount = new ComputedSizeOutput(forceWritePrimitives);
            schema.writeTo(sizeCount, object);
            byte[] result = new byte[sizeCount.getSize()];
            CodedOutput output = CodedOutput.newInstance(result, forceWritePrimitives, sizeCount);
            schema.writeTo(output, object);
            output.checkNoSpaceLeft();
            return result;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).",e);
        }
    }
    
    /**
     * Computes the buffer size and serializes the {@code message} into 
     * a byte array via {@link CodedOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArrayComputed(T message)
    {
        return toByteArrayComputed(message, false);
    }
    
    /**
     * Computes the buffer size and serializes the {@code message} into 
     * a byte array via {@link CodedOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArrayComputed(T message, 
            boolean forceWritePrimitives)
    {
        try
        {
            Schema<T> schema = message.cachedSchema();
            ComputedSizeOutput sizeCount = new ComputedSizeOutput(forceWritePrimitives);
            schema.writeTo(sizeCount, message);
            byte[] result = new byte[sizeCount.getSize()];
            CodedOutput output = CodedOutput.newInstance(result, forceWritePrimitives, sizeCount);
            schema.writeTo(output, message);
            output.checkNoSpaceLeft();
            return result;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).",e);
        }
    }
    
    /**
     * Serializes the {@code object} into a byte array via {@link DeferredOutput}.
     */
    public static <T> byte[] toByteArrayDeferred(T object, Schema<T> schema)
    {
        return toByteArrayDeferred(object, schema, false);
    }
    
    /**
     * Serializes the {@code object} into a byte array via {@link DeferredOutput}.
     */
    public static <T> byte[] toByteArrayDeferred(T message, Schema<T> schema, 
            boolean forceWritePrimitives)
    {
        DeferredOutput output = new DeferredOutput(forceWritePrimitives);
        try
        {
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.toByteArray();
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link DeferredOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArrayDeferred(T message)
    {
        return toByteArrayDeferred(message, false);
    }
    
    /**
     * Serializes the {@code message} into a byte array via {@link DeferredOutput}.
     */
    public static <T extends Message<T>> byte[] toByteArrayDeferred(T message, 
            boolean forceWritePrimitives)
    {
        Schema<T> schema = message.cachedSchema();
        DeferredOutput output = new DeferredOutput(forceWritePrimitives);
        try
        {
            schema.writeTo(output, message);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return output.toByteArray();
    }
    
    /**
     * Merges the {@code object} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T object, Schema<T> schema)
    {
        try
        {
            CodedInput input = CodedInput.newInstance(data);
            schema.mergeFrom(input, object);
            input.checkLastTagWas(0);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Reading from a byte array threw an IOException (should " + 
                    "never happen).",e);
        }
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    public static <T extends Message<T>> void mergeFrom(byte[] data, T message)
    {
        try
        {
            Schema<T> schema = message.cachedSchema();
            CodedInput input = CodedInput.newInstance(data);
            schema.mergeFrom(input, message);
            input.checkLastTagWas(0);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Reading from a byte array threw an IOException (should " + 
                    "never happen).",e);
        }
    }

}
