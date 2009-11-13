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

import static com.dyuproject.protostuff.SerializableObjects.foo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

/**
 * Benchmark to compare the serialization speed of 2 types.
 *
 * @author David Yu
 * @created Nov 13, 2009
 */
public class ComputedVSDeferredTest extends TestCase
{
    
    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;

        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-core-"+System.currentTimeMillis()+".html"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        String title = "protostuff-core ser/deser benchmark for " + loops + " runs";
        out.println("<html><head><title>");
        out.println(title);
        out.println("</title></head><body><p>");
        out.println(title);        
        out.println("</p><pre>\n\n");
        
        serDeser(out);
        out.println("\n\n</pre><hr/><pre>");
        start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void start(PrintStream out, int warmups, int loops) throws Exception
    {
        serDeser(out, COMPUTED, "computed", warmups, loops);
        serDeser(out, DEFERRED, "deferred", warmups, loops);
    }
    
    public static void serDeser(PrintStream out) throws Exception
    {
        String computed = new String(serDeser(COMPUTED), ByteString.UTF8);
        String deferred = new String(serDeser(DEFERRED), ByteString.UTF8);
        
        assertEquals(computed, deferred);
        
        out.println(computed);
        out.println();
        out.println(deferred);
    }
    
    public static byte[] serDeser(Serializer serializer) throws Exception
    {
        byte[] data = serializer.serialize(foo);
        Foo f = new Foo();
        IOUtil.mergeFrom(data, f);
        return data;
    }
    
    static void serDeser(PrintStream out, Serializer serializer, String name, int warmups, 
            int loops) throws Exception
    {
        int len = serDeser(serializer).length;
        for(int i=0; i<warmups; i++)
            serDeser(serializer);
        long start = System.currentTimeMillis();
        for(int i=0; i<loops; i++)
            serDeser(serializer);
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        out.println(elapsed + " ms elapsed with " + len + " bytes for " + name);
    }
    
    public static void main(String[] args) throws Exception
    {
        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-core-"+System.currentTimeMillis()+".html"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        String title = "protostuff-core ser/deser benchmark for " + loops + " runs";
        out.println("<html><head><title>");
        out.println(title);
        out.println("</title></head><body><p>");
        out.println(title);        
        out.println("</p><pre>\n\n");
        
        serDeser(out);
        out.println("\n\n</pre><hr/><pre>");
        start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public interface Serializer
    {
        
        public <T extends Message<T>> byte[] serialize(T message);
        
    }
    
    public static final Serializer COMPUTED = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            return IOUtil.toByteArrayComputed(message);
        }
        
    };

    public static final Serializer DEFERRED = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {            
            return IOUtil.toByteArrayDeferred(message);
        }
        
    };

}
