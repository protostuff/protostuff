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

import static com.dyuproject.protostuff.CompareOutputsTest.bar;
import static com.dyuproject.protostuff.CompareOutputsTest.baz;
import static com.dyuproject.protostuff.CompareOutputsTest.foo;
import static com.dyuproject.protostuff.CompareOutputsTest.negativeBar;
import static com.dyuproject.protostuff.CompareOutputsTest.negativeBaz;
import static com.dyuproject.protostuff.CompareOutputsTest.start;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.TestCase;

import com.dyuproject.protostuff.CompareOutputsTest.Serializer;
import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Compare the outputs of different yaml impls.
 *
 * @author David Yu
 * @created Jul 9, 2010
 */
public class YamlCompareOutputsTest extends TestCase
{
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] yo = YAML_BUFFERED_OUTPUT.serialize(fooCompare);
        byte[] yso = YAML_STREAMED_OUTPUT.serialize(fooCompare);
        
        assertTrue(yo.length == yso.length);
        
        String yoString = STRING.deser(yo);
        String ysoString = STRING.deser(yso);
        
        assertEquals(yoString, ysoString);
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            byte[] yo = YAML_BUFFERED_OUTPUT.serialize(barCompare);
            byte[] yso = YAML_STREAMED_OUTPUT.serialize(barCompare);
            
            assertTrue(yo.length == yso.length);
            
            String yoString = STRING.deser(yo);
            String ysoString = STRING.deser(yso);
            
            assertEquals(yoString, ysoString);
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            byte[] yo = YAML_BUFFERED_OUTPUT.serialize(bazCompare);
            byte[] yso = YAML_STREAMED_OUTPUT.serialize(bazCompare);
            
            assertTrue(yo.length == yso.length);
            
            String yoString = STRING.deser(yo);
            String ysoString = STRING.deser(yso);
            
            assertEquals(yoString, ysoString);
        }
    }

    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;

        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-yaml-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        
        String title = "protostuff-yaml serialization benchmark for " + loops + " runs";
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
                    "protostuff-yaml-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 100000);
        int loops = Integer.getInteger("benchmark.loops", 1000000);
        
        String title = "protostuff-yaml serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(foo, SERIALIZERS, out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    
    public static final Serializer YAML_BUFFERED_OUTPUT = new Serializer()
    {
        
        final LinkedBuffer buffer = new LinkedBuffer(512);

        public <T extends Message<T>> byte[] serialize(T message)
        {
            try
            {
                return YamlIOUtil.toByteArray(message, message.cachedSchema(), buffer);
            }
            finally
            {
                buffer.clear();
            }
        }
        

        public String getName()
        {
            return "yaml-buffered-output";
        }
        
    };
    
    public static final Serializer YAML_STREAMED_OUTPUT = new Serializer()
    {
        
        final LinkedBuffer buffer = new LinkedBuffer(1024);

        public <T extends Message<T>> byte[] serialize(T message)
        {
            try
            {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                YamlIOUtil.writeTo(out, message, message.cachedSchema(), buffer);
                return out.toByteArray();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                buffer.clear();
            }
        }
        
        public String getName()
        {
            return "yaml-streamed-output";
        }
        
    };
    
    static final Serializer[] SERIALIZERS = new Serializer[]{
        YAML_BUFFERED_OUTPUT,
        YAML_STREAMED_OUTPUT, 
    };
    
}
