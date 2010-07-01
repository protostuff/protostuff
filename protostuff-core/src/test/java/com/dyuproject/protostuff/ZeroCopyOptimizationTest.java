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
 * Benchmark to find out the number of bytes to start doing zero-copy. (In this case, >255)
 * We want to measure object creation vs System.arraycopy performance.
 * 
 * Instead of copying the byte array to the buffer, zero-copy basically just 
 * holds a reference to the byte array. The {@link LinkedBuffer} wraps byte array 
 * and appends it to the previous buffer.  
 * It will take either 1 {@link LinkedBuffer} (or 2 depending on the implementation) to 
 * be allocated(instantiated).
 * 
 * For smaller bytes, (<=140) it is better to copy the array.
 * For bytes that are >140 and <255, even though a bit slower (see below), it probably 
 * still will be worth it copying the array than allocating/instantiating a {@link LinkedBuffer}.
 * 
 * Note that zero-copy avoids buffer overflows as well.
 * 
 * On ubuntu 9.10 64-bit (java 6u20), without-zero-copy-serialization is ~7.6% slower 
 * with 255-byte bytestrings.
 * 
 * On windows xp-sp3 32-bit (java 6u20), without-zero-copy-serialization is ~4.4% slower 
 * with 255-byte bytestrings.
 * 
 * This means that object creation (possibly gc too) is a lot slower on windows 32-bit.
 * 
 * The data tested against is 1932 bytes (group-encoded) and 1934 bytes (regular protobuf).
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public class ZeroCopyOptimizationTest extends TestCase
{
    
    static final Baz negativeBaz = new Baz(-567, null, -202020202);
    static final Bar negativeBar = new Bar(-12, null, negativeBaz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8(        "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345"),
            true, -130.031f, -1000.0001d, -101010101);
    
    static final Baz baz = new Baz(567, null, 202020202);  
    static final Bar bar = new Bar(890, null, baz, Bar.Status.STARTED, null,
            true, 150.051f, 2000.0002d, 303030303);

    // total of 1932 bytes for group-encoded.
    // total of 1934 bytes for regular protobuf.
    public static final Foo foo = SerializableObjects.newFoo(
            new Integer[]{90210,-90210, 0}, 
            new String[]{}, 
            new Bar[]{bar, negativeBar, bar, bar, negativeBar, bar},
            new EnumSample[]{EnumSample.TYPE0, EnumSample.TYPE2}, 
            new ByteString[]{
                    ByteString.copyFromUtf8("ef"), 
                    ByteString.copyFromUtf8("gh"),
                    // 4 200-byte bytestring.
                    ByteString.copyFromUtf8("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345"),
                    ByteString.copyFromUtf8("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345"),
                    ByteString.copyFromUtf8("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345"),
                    ByteString.copyFromUtf8("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345")}, 
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
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        
        String title = "protostuff-core zero-copy serialization benchmark for " + loops + " runs";
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
        int len = ComputedSizeOutput.getSize(foo, foo.cachedSchema(), true);
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
        
        String title = "protostuff-core zero-copy serialization benchmark for " + loops + " runs";
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
            
            return null;
            //return output.toByteArray();
        }

        public String getName()
        {
            return "ignore-me";
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
            
            return null;
            //return output.toByteArray();
        }

        public String getName()
        {
            return "ignore-me-too";
        }

    };
    
    public static final Serializer BUFFERED_OUTPUT_GE = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            BufferedOutput output = new BufferedOutput(new LinkedBuffer(1940), 256, 300, true);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return null;
            //return output.toByteArray();
        }
        
        public String getName()
        {
            return "bufferedoutput-ge";
        }
        
    };
    
    public static final Serializer BUFFERED_OUTPUT_GE_ZC = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            BufferedOutput output = new BufferedOutput(new LinkedBuffer(1940), 256, 100, true);
            try
            {
                message.cachedSchema().writeTo(output, message);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                        "(should never happen).", e);
            }
            
            return null;
            //return output.toByteArray();
        }
        
        public String getName()
        {
            return "bufferedoutput-ge-zc";
        }
        
    };
    
    static final Serializer[] SERIALIZERS = new Serializer[]{
        // stabilizers
        DEFERRED_OUTPUT, DEFERRED_OUTPUT_GE,
        // with zero copy
        BUFFERED_OUTPUT_GE_ZC,
        // without zero copy
        BUFFERED_OUTPUT_GE,
    };

}
