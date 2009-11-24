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
import java.io.InputStream;
import java.io.OutputStream;

import com.dyuproject.protostuff.CodedInput;
import com.dyuproject.protostuff.CodedOutput;
import com.dyuproject.protostuff.DeferredOutput;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Schema;

/**
 * Utility for the serialization/deserialization of messages and objects tied to a schema.
 * The schema can be generated and cached at runtime for other objects that are 
 * not yet tied to a schema.
 *
 * @author David Yu
 * @created Nov 18, 2009
 */
public final class RuntimeIOUtil
{
    
    private RuntimeIOUtil(){}
    
    /**
     * Serializes the {@code message} into a byte array via {@link DeferredOutput}.
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema)
    {
        DeferredOutput output = new DeferredOutput();
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
    public static <T extends Message<T>> byte[] toByteArray(T message)
    {
        return toByteArray(message, message.cachedSchema());
    }
    
    
    /**
     * Serializes the {@code message} into a byte array via {@link DeferredOutput}.
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] toByteArray(T message)
    {
        if(message instanceof Message)
            return toByteArray(message, ((Message)message).cachedSchema());
        
        Class<T> typeClass  = (Class<T>)message.getClass();
        return toByteArray(message, RuntimeSchema.getSchema(typeClass));
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link CodedOutput}.
     */
    public static <T> void writeTo(OutputStream out, T message, Schema<T> schema)
    throws IOException
    {
        schema.writeTo(CodedOutput.newInstance(out), message);
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link CodedOutput}.
     */
    public static <T extends Message<T>> void writeTo(OutputStream out, T message)
    throws IOException
    {
        writeTo(out, message, message.cachedSchema());
    }
    
    /**
     * Serializes the {@code message} into an {@link OutputStream} via {@link CodedOutput}.
     */
    @SuppressWarnings("unchecked")
    public static <T> void writeTo(OutputStream out, T message)
    throws IOException
    {
        if(message instanceof Message)
            writeTo(out, message, ((Message)message).cachedSchema());
        else
        {
            Class<T> typeClass = (Class<T>)message.getClass();
            writeTo(out, message, RuntimeSchema.getSchema(typeClass));
        }
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema)
    {
        mergeFrom(data, 0, data.length, message, schema);
    }
    
    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    public static <T> void mergeFrom(byte[] data, int offset, int length, T message, 
            Schema<T> schema)
    {
        try
        {
            CodedInput input = CodedInput.newInstance(data, offset, length);
            schema.mergeFrom(input, message);
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
        mergeFrom(data, 0, data.length, message, message.cachedSchema());
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    public static <T extends Message<T>> void mergeFrom(byte[] data, int offset, int length, 
            T message)
    {
        mergeFrom(data, offset, length, message, message.cachedSchema());
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    @SuppressWarnings("unchecked")
    public static <T> void mergeFrom(byte[] data, T message)
    {
        if(message instanceof Message)
            mergeFrom(data, 0, data.length, message, ((Message)message).cachedSchema());
        else
        {
            Class<T> typeClass = (Class<T>)message.getClass();
            mergeFrom(data, 0, data.length, message, RuntimeSchema.getSchema(typeClass));
        }
    }
    
    /**
     * Merges the {@code message} with the byte array.
     */
    @SuppressWarnings("unchecked")
    public static <T> void mergeFrom(byte[] data, int offset, int length, 
            T message)
    {
        if(message instanceof Message)
            mergeFrom(data, offset, length, message, ((Message)message).cachedSchema());
        else
        {
            Class<T> typeClass = (Class<T>)message.getClass();
            mergeFrom(data, offset, length, message, RuntimeSchema.getSchema(typeClass));
        }
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema) 
    throws IOException
    {
        CodedInput input = CodedInput.newInstance(in);
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream}.
     */
    public static <T extends Message<T>> void mergeFrom(InputStream in, T message) 
    throws IOException
    {
        mergeFrom(in, message, message.cachedSchema());
    }
    
    /**
     * Merges the {@code message} from the {@link InputStream}.
     */
    @SuppressWarnings("unchecked")
    public static <T> void mergeFrom(InputStream in, T message) 
    throws IOException
    {
        if(message instanceof Message)
            mergeFrom(in, message, ((Message)message).cachedSchema());
        else
        {
            Class<T> typeClass = (Class<T>)message.getClass();
            mergeFrom(in, message, RuntimeSchema.getSchema(typeClass));
        }
    }
}
