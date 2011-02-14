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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.dyuproject.protostuff.Foo.EnumSample;

/**
 * Benchmark to compare the serialization speed of 3 types. 
 * CodedOutput, BufferedOutput and DeferredOutput.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public class CompareOutputsTest extends AbstractTest
{
    
    static final Baz negativeBaz = new Baz(-567, "negativeBaz", -202020202);
    static final Bar negativeBar = new Bar(-12, "negativeBar", negativeBaz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("a1"), true, -130.031f, -1000.0001d, -101010101);
    
    static final Baz baz = new Baz(567, "baz", 202020202);  
    static final Bar bar = new Bar(890, "bar", baz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("b2"), true, 150.051f, 2000.0002d, 303030303);

    // a total of 1000 bytes
    public static final Foo foo = SerializableObjects.newFoo(
            new Integer[]{90210,-90210, 0}, 
            new String[]{"foo", "123456789012345678901234567890123456789012"}, 
            new Bar[]{bar, negativeBar},
            new EnumSample[]{EnumSample.TYPE0, EnumSample.TYPE2}, 
            new ByteString[]{
                    ByteString.copyFromUtf8("ef"), 
                    ByteString.copyFromUtf8("gh"),
                    // two 350-byte bytestring.
                    ByteString.copyFromUtf8("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"),
                    ByteString.copyFromUtf8("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890")}, 
            new Boolean[]{true, false}, 
            new Float[]{1234.4321f, -1234.4321f, 0f}, 
            new Double[]{12345678.87654321d, -12345678.87654321d, 0d}, 
            new Long[]{7060504030201l, -7060504030201l, 0l});
    
    public void testFooProtobuf() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] computed = toByteArrayComputedProtobuf(fooCompare, fooCompare.cachedSchema());
        byte[] buffered = toByteArrayBufferedProtobuf(fooCompare, fooCompare.cachedSchema());
        byte[] streamed = toByteArrayStreamedProtobuf(fooCompare, fooCompare.cachedSchema());
        
        assertTrue(computed.length == buffered.length);
        assertTrue(computed.length == streamed.length);
        
        String strComputed = new String(computed, "UTF-8");
        
        assertEquals(strComputed, new String(buffered, "UTF-8"));
        assertEquals(strComputed, new String(streamed, "UTF-8"));
    }
    
    public void testFooProtostuff() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] computed = toByteArrayComputedProtostuff(fooCompare, fooCompare.cachedSchema());
        byte[] buffered = toByteArrayBufferedProtostuff(fooCompare, fooCompare.cachedSchema());
        byte[] streamed = toByteArrayStreamedProtostuff(fooCompare, fooCompare.cachedSchema());
        
        assertTrue(computed.length == buffered.length);
        assertTrue(computed.length == streamed.length);
        
        String strComputed = new String(computed, "UTF-8");
        
        assertEquals(strComputed, new String(buffered, "UTF-8"));
        assertEquals(strComputed, new String(streamed, "UTF-8"));
    }
    
    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;

        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-core-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        
        String title = "protostuff-core serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(foo, SERIALIZERS, out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void main(String[] args) throws Exception
    {
        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-core-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        
        String title = "protostuff-core serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(foo, SERIALIZERS, out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static  <T extends Message<T>> void start(T message, Serializer[] serializers, 
            PrintStream out, int warmups, int loops) throws Exception
    {
        for(Serializer s : serializers)
            ser(message, s, out, s.getName(), warmups, loops);
    }
    
    static <T extends Message<T>> void ser(T message, Serializer serializer, PrintStream out, 
            String name, int warmups, int loops) throws Exception
    {
        int len = serializer.serialize(message).length;
        for(int i=0; i<warmups; i++)
            serializer.serialize(message);
        long start = System.currentTimeMillis();
        for(int i=0; i<loops; i++)
            serializer.serialize(message);
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        out.println(elapsed + " ms elapsed with " + len + " bytes for " + name);
    }
    
    static <T> byte[] toByteArrayComputedProtobuf(T message, Schema<T> schema)
    {
        return CodedOutput.toByteArray(message, schema, false);
    }
    
    static <T> byte[] toByteArrayBufferedProtobuf(T message, Schema<T> schema)
    {
        final ProtobufOutput output = new ProtobufOutput(new LinkedBuffer(BUF_SIZE));
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
    
    static <T> byte[] toByteArrayStreamedProtobuf(T message, Schema<T> schema)
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CodedOutput output = new CodedOutput(out, new byte[BUF_SIZE], false);
        try
        {
            schema.writeTo(output, message);
            output.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return out.toByteArray();
    }
    
    static <T> byte[] toByteArrayComputedProtostuff(T message, Schema<T> schema)
    {
        return CodedOutput.toByteArray(message, schema, true);
    }
    
    static <T> byte[] toByteArrayBufferedProtostuff(T message, Schema<T> schema)
    {
        final ProtostuffOutput output = new ProtostuffOutput(new LinkedBuffer(BUF_SIZE));
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
    
    static <T> byte[] toByteArrayStreamedProtostuff(T message, Schema<T> schema)
    {
        /*final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            ProtostuffIOUtil.writeTo(out, message, schema, new LinkedBuffer(BUF_SIZE));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        return out.toByteArray();*/
        
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final LinkedBuffer buffer = new LinkedBuffer(BUF_SIZE);
        final ProtostuffOutput output = new ProtostuffOutput(buffer, out);
        
        try
        {
            schema.writeTo(output, message);
            LinkedBuffer.writeTo(out, buffer);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                    "(should never happen).", e);
        }
        
        return out.toByteArray();
    }
    
    static final int BUF_SIZE = 256;
    
    public interface Serializer
    {
        
        public <T extends Message<T>> byte[] serialize(T message);
        
        public String getName();
        
    }

    static final Serializer PROTOBUF_COMPUTED_OUTPUT = new Serializer()
    {
        public <T extends Message<T>> byte[] serialize(T message)
        {
            return toByteArrayComputedProtobuf(message, message.cachedSchema());
        }
        public String getName()
        {
            return "protobuf-computed-output";
        }
    };
    
    static final Serializer PROTOBUF_BUFFERED_OUTPUT = new Serializer()
    {
        public <T extends Message<T>> byte[] serialize(T message)
        {
            return toByteArrayBufferedProtobuf(message, message.cachedSchema());
        }
        public String getName()
        {
            return "protobuf-buffered-output";
        }
    };
    
    static final Serializer PROTOBUF_STREAMED_OUTPUT = new Serializer()
    {
        public <T extends Message<T>> byte[] serialize(T message)
        {
            return toByteArrayStreamedProtobuf(message, message.cachedSchema());
        }
        public String getName()
        {
            return "protobuf-streamed-output";
        }
    };
    
    static final Serializer PROTOSTUFF_COMPUTED_OUTPUT = new Serializer()
    {
        public <T extends Message<T>> byte[] serialize(T message)
        {
            return toByteArrayComputedProtostuff(message, message.cachedSchema());
        }
        public String getName()
        {
            return "protostuff-computed-output";
        }
    };
    
    static final Serializer PROTOSTUFF_BUFFERED_OUTPUT = new Serializer()
    {
        public <T extends Message<T>> byte[] serialize(T message)
        {
            return toByteArrayBufferedProtostuff(message, message.cachedSchema());
        }
        public String getName()
        {
            return "protostuff-buffered-output";
        }
    };
    
    static final Serializer PROTOSTUFF_STREAMED_OUTPUT = new Serializer()
    {
        public <T extends Message<T>> byte[] serialize(T message)
        {
            return toByteArrayStreamedProtostuff(message, message.cachedSchema());
        }
        public String getName()
        {
            return "protostuff-streamed-output";
        }
    };
    
    static final Serializer[] SERIALIZERS = new Serializer[]{
        PROTOBUF_COMPUTED_OUTPUT,
        PROTOBUF_BUFFERED_OUTPUT,
        PROTOBUF_STREAMED_OUTPUT,
        PROTOSTUFF_COMPUTED_OUTPUT,
        PROTOSTUFF_BUFFERED_OUTPUT,
        PROTOSTUFF_STREAMED_OUTPUT
    };

}
