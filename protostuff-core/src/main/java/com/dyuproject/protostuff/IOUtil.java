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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
        message.cachedSchema().writeTo(CodedOutput.newInstance(out), message);
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
     * Serializes the {@code messages} (delimited) into 
     * an {@link OutputStream} via {@link CodedOutput} using the given schema.
     */
    public static <T> void writeListTo(OutputStream out, List<T> messages, Schema<T> schema)
    throws IOException
    {
        final DeferredOutput output = new DeferredOutput();
        for(T m : messages)
        {
            schema.writeTo(output, m);
            CodedOutput.writeRawVarInt32Bytes(out, output.getSize());
            output.streamTo(out);
            output.reset();
        }
    }
    
    /**
     * Parses the {@code messages} (delimited) from the 
     * {@link InputStream} using the given {@code schema}.
     */
    public static <T> List<T> parseListFrom(InputStream in, Schema<T> schema) 
    throws IOException
    {
        final ArrayList<T> list = new ArrayList<T>();
        final LimitedInputStream lis = new LimitedInputStream(in);
        for(int size=in.read(); size!=-1; size=in.read())
        {
            T message = schema.newMessage();
            int len = (size & 0x80)==0 ? (size & 0x7f) : CodedInput.readRawVarint32(in, size);
            if(len != 0)
            {
                // not an empty message
                CodedInput input = CodedInput.newInstance(lis.limit(len));
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
            }
            list.add(message);
        }
        return list;
    }

}
