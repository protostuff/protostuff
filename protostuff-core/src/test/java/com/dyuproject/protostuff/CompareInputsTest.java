//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

import static com.dyuproject.protostuff.SerializableObjects.foo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.dyuproject.protostuff.CompareOutputsTest.Serializer;

/**
 * Benchmark to compare the deserialization speed of 2 types. 
 * CodedInput and ByteArrayInput.
 *
 * @author David Yu
 * @created Jun 22, 2010
 */
public class CompareInputsTest extends AbstractTest
{
    
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
        
        String title = "protostuff-core deserialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void start(PrintStream out, int warmups, int loops) throws Exception
    {
        for(Deserializer s : DESERIALIZERS)
            deser(out, s, s.getName(), warmups, loops);
    }
    
    static void deser(PrintStream out, Deserializer deserializer, String name, int warmups, 
            int loops) throws Exception
    {
        final byte[] data = deserializer.getSerializer().serialize(foo);
        int len = data.length;
        Foo f = new Foo();
        deserializer.mergeFrom(data, f);
        SerializableObjects.assertEquals(foo, f);
        
        for(int i=0; i<warmups; i++)
            deserializer.mergeFrom(data, new Foo());
        
        long start = System.currentTimeMillis();
        
        for(int i=0; i<loops; i++)
            deserializer.mergeFrom(data, new Foo());
        
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        out.println(elapsed + " ms elapsed with " + len + " bytes for " + name);
    }
    
    public static void main(String[] args) throws Exception
    {
        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-core-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        
        String title = "protostuff-core deserialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public interface Deserializer
    {
        public <T extends Message<T>> void mergeFrom(byte[] data, T message) throws IOException;
        
        public String getName();
        
        public Serializer getSerializer();
    }
    
    public static final Deserializer PROTOBUF_CODED_INPUT = new Deserializer()
    {
        
        public <T extends Message<T>> void mergeFrom(byte[] data, T message) throws IOException
        {
            final CodedInput input = new CodedInput(data, 0, data.length, false);
            message.cachedSchema().mergeFrom(input, message);
            input.checkLastTagWas(0);
        }
        
        public String getName()
        {
            return "protobuf-coded-input";
        }
        
        public Serializer getSerializer()
        {
            return CompareOutputsTest.PROTOBUF_COMPUTED_OUTPUT;
        }
        
    };
    
    public static final Deserializer PROTOSTUFF_CODED_INPUT = new Deserializer()
    {
        
        public <T extends Message<T>> void mergeFrom(byte[] data, T message) throws IOException
        {
            final CodedInput input = new CodedInput(data, 0, data.length, true);
            message.cachedSchema().mergeFrom(input, message);
            input.checkLastTagWas(0);
        }
        
        public String getName()
        {
            return "protostuff-coded-input";
        }
        
        public Serializer getSerializer()
        {
            return CompareOutputsTest.PROTOSTUFF_COMPUTED_OUTPUT;
        }
        
    };
    
    public static final Deserializer PROTOBUF_BYTE_ARRAY_INPUT = new Deserializer()
    {
        
        public <T extends Message<T>> void mergeFrom(byte[] data, T message) throws IOException
        {
            try
            {
                final ByteArrayInput input = new ByteArrayInput(data, 0, data.length, false);
                message.cachedSchema().mergeFrom(input, message);
                input.checkLastTagWas(0);
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                throw ProtobufException.truncatedMessage(e);
            }
        }
        
        public String getName()
        {
            return "protobuf-bytearray-input";
        }
        
        public Serializer getSerializer()
        {
            return CompareOutputsTest.PROTOBUF_BUFFERED_OUTPUT;
        }
        
    };
    
    public static final Deserializer PROTOSTUFF_BYTE_ARRAY_INPUT = new Deserializer()
    {
        
        public <T extends Message<T>> void mergeFrom(byte[] data, T message) throws IOException
        {
            try
            {
                final ByteArrayInput input = new ByteArrayInput(data, 0, data.length, true);
                message.cachedSchema().mergeFrom(input, message);
                input.checkLastTagWas(0);
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                throw ProtobufException.truncatedMessage(e);
            }
        }
        
        public String getName()
        {
            return "protostuff-bytearray-input";
        }
        
        public Serializer getSerializer()
        {
            return CompareOutputsTest.PROTOSTUFF_BUFFERED_OUTPUT;
        }
        
    };
    
    static final Deserializer[] DESERIALIZERS = new Deserializer[]{
        PROTOBUF_CODED_INPUT, 
        PROTOSTUFF_CODED_INPUT,
        PROTOBUF_BYTE_ARRAY_INPUT, 
        PROTOSTUFF_BYTE_ARRAY_INPUT
    };

}
