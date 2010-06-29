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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.TestCase;

import com.dyuproject.protostuff.Foo.EnumSample;

/**
 * Benchmark to compare the serialization speed of 3 types. 
 * CodedOutput, BufferedOutput and DeferredOutput.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public class CompareOutputsTest extends TestCase
{
    
    static final Baz negativeBaz = new Baz(-567, null, -202020202);
    static final Bar negativeBar = new Bar(-12, null, negativeBaz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("a1"), true, -130.031f, -1000.0001d, -101010101);
    
    static final Baz baz = new Baz(567, null, 202020202);  
    static final Bar bar = new Bar(890, null, baz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("b2"), true, 150.051f, 2000.0002d, 303030303);

    // a total of 915 bytes
    public static final Foo foo = SerializableObjects.newFoo(
            new Integer[]{90210,-90210, 0}, 
            new String[]{}, 
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
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] coded = CodedOutput.toByteArray(fooCompare, fooCompare.cachedSchema());
        byte[] deferred = DeferredOutputTest.getByteArray(fooCompare, fooCompare.cachedSchema());
        byte[] buffered = BufferedOutputTest.getByteArray(fooCompare, fooCompare.cachedSchema());
        
        //System.err.println(coded.length + " == " + deferred.length + " == " + buffered.length);
        assertTrue(coded.length == deferred.length);
        assertTrue(coded.length == buffered.length);
        
        assertEquals(new String(coded, "UTF-8"), new String(deferred, "UTF-8"));
        assertEquals(new String(coded, "UTF-8"), new String(buffered, "UTF-8"));
    }
    
    public void testFooGE() throws Exception
    {
        boolean groupEncoded = true;
        Foo fooCompare = foo;
        
        byte[] coded = CodedOutput.toByteArray(fooCompare, fooCompare.cachedSchema(), groupEncoded);
        byte[] deferred = DeferredOutputGETest.getByteArray(fooCompare, fooCompare.cachedSchema());
        byte[] buffered = BufferedOutputGETest.getByteArray(fooCompare, fooCompare.cachedSchema());
        
        //System.err.println(coded.length + " == " + deferred.length + " == " + buffered.length);
        assertTrue(coded.length == deferred.length);
        assertTrue(coded.length == buffered.length);
        
        assertEquals(new String(coded, "UTF-8"), new String(deferred, "UTF-8"));
        assertEquals(new String(coded, "UTF-8"), new String(buffered, "UTF-8"));
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            byte[] coded = CodedOutput.toByteArray(barCompare, barCompare.cachedSchema());
            byte[] deferred = DeferredOutputTest.getByteArray(barCompare, barCompare.cachedSchema());
            byte[] buffered = BufferedOutputTest.getByteArray(barCompare, barCompare.cachedSchema());
            
            //System.err.println(coded.length + " == " + deferred.length + " == " + buffered.length);
            assertTrue(coded.length == deferred.length);
            assertTrue(coded.length == buffered.length);
            
            assertEquals(new String(coded, "UTF-8"), new String(deferred, "UTF-8"));
            assertEquals(new String(coded, "UTF-8"), new String(buffered, "UTF-8"));
        }
    }
    
    public void testBarGE() throws Exception
    {
        boolean groupEncoded = true;
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            byte[] coded = CodedOutput.toByteArray(barCompare, barCompare.cachedSchema(), groupEncoded);
            byte[] deferred = DeferredOutputGETest.getByteArray(barCompare, barCompare.cachedSchema());
            byte[] buffered = BufferedOutputGETest.getByteArray(barCompare, barCompare.cachedSchema());
            
            //System.err.println(coded.length + " == " + deferred.length + " == " + buffered.length);
            assertTrue(coded.length == deferred.length);
            assertTrue(coded.length == buffered.length);
            
            assertEquals(new String(coded, "UTF-8"), new String(deferred, "UTF-8"));
            assertEquals(new String(coded, "UTF-8"), new String(buffered, "UTF-8"));
        }
    }
    
    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;

        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-core-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 100000);
        int loops = Integer.getInteger("benchmark.loops", 1000000);
        
        String title = "protostuff-core serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void start(PrintStream out, int warmups, int loops) throws Exception
    {
        for(Serializer s : SERIALIZERS)
            ser(out, s, s.getName(), warmups, loops);
    }
    
    static void ser(PrintStream out, Serializer serializer, String name, int warmups, 
            int loops) throws Exception
    {
        int len = serializer.serialize(foo).length;
        for(int i=0; i<warmups; i++)
            serializer.serialize(foo);
        long start = System.currentTimeMillis();
        for(int i=0; i<loops; i++)
            serializer.serialize(foo);
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
        
        String title = "protostuff-core serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    
    public interface Serializer
    {
        
        public <T extends Message<T>> byte[] serialize(T message);
        
        public String getName();
        
    }
    
    public static final Serializer CODED_OUTPUT = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            return CodedOutput.toByteArray(message, message.cachedSchema(), false);
        }

        public String getName()
        {
            return "codedoutput";
        }
        
    };
    
    public static final Serializer CODED_OUTPUT_GE = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            return CodedOutput.toByteArray(message, message.cachedSchema(), true);
        }

        public String getName()
        {
            return "codedoutput-ge";
        }
        
    };

    public static final Serializer DEFERRED_OUTPUT = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            DeferredOutput output = new DeferredOutput(false);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return output.toByteArray();
        }

        public String getName()
        {
            return "deferredoutput";
        }

    };
    
    public static final Serializer DEFERRED_OUTPUT_GE = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            DeferredOutput output = new DeferredOutput(true);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return output.toByteArray();
        }

        public String getName()
        {
            return "deferredoutput-ge";
        }

    };
    
    public static final Serializer BUFFERED_OUTPUT = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            BufferedOutput output = new BufferedOutput(new LinkedBuffer(1024), 256, 351, false);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return output.toByteArray();
        }
        
        public String getName()
        {
            return "bufferedoutput";
        }
        
    };
    
    public static final Serializer BUFFERED_OUTPUT_GE = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            BufferedOutput output = new BufferedOutput(new LinkedBuffer(1024), 256, 351, true);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return output.toByteArray();
        }
        
        public String getName()
        {
            return "bufferedoutput-ge";
        }
        
    };
    
    public static final Serializer BUFFERED_OUTPUT_ZC = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {
            BufferedOutput output = new BufferedOutput(new LinkedBuffer(1024), 256, 339, false);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return output.toByteArray();
        }
        
        public String getName()
        {
            return "bufferedoutput-zc";
        }
        
    };
    
    public static final Serializer BUFFERED_OUTPUT_GE_ZC = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            BufferedOutput output = new BufferedOutput(new LinkedBuffer(1024), 256, 339, true);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return output.toByteArray();
        }
        
        public String getName()
        {
            return "bufferedoutput-ge-zc";
        }
        
    };
    
    static final Serializer[] SERIALIZERS = new Serializer[]{
        CODED_OUTPUT, CODED_OUTPUT_GE,
        DEFERRED_OUTPUT, DEFERRED_OUTPUT_GE,
        BUFFERED_OUTPUT, BUFFERED_OUTPUT_GE,
        BUFFERED_OUTPUT_ZC, BUFFERED_OUTPUT_GE_ZC
    };

}
