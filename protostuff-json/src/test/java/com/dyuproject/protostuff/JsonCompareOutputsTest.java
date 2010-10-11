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

import static com.dyuproject.protostuff.CompareOutputsTest.start;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.TestCase;

import com.dyuproject.protostuff.CompareOutputsTest.Serializer;
import com.dyuproject.protostuff.Foo.EnumSample;
import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Compare the outputs of different json impls.
 *
 * @author David Yu
 * @created Jul 2, 2010
 */
public class JsonCompareOutputsTest extends TestCase
{
    
    static final Baz negativeBaz = new Baz(-567, "negativeBaz", -202020202);
    static final Bar negativeBar = new Bar(-12, "negativeBar", negativeBaz, Bar.Status.STARTED, 
            null, true, -130.031f, -1000.0001d, -101010101);
    
    static final Baz baz = new Baz(567, "baz", 202020202);
    static final Bar bar = new Bar(890, "bar", baz, Bar.Status.STARTED, 
            ByteString.copyFromUtf8("byte_bar"), true, 150.051f, 2000.0002d, 303030303);

    // a total of 791 bytes
    public static final Foo foo = SerializableObjects.newFoo(
            new Integer[]{90210,-90210, 0}, 
            new String[]{
                    "a\u0008\u0009\u000B\r\n\f\t\b\"\\",
                    "ef",
                    "gh",
                    "12345678901234567890123456789012345678901234567890"
            }, 
            new Bar[]{bar, negativeBar},
            new EnumSample[]{EnumSample.TYPE0, EnumSample.TYPE2}, 
            new ByteString[]{ByteString.copyFromUtf8("byte1"), ByteString.copyFromUtf8("byte2")}, 
            new Boolean[]{true, false}, 
            new Float[]{1234.4321f, -1234.4321f, 0f}, 
            new Double[]{12345678.87654321d, -12345678.87654321d, 0d}, 
            new Long[]{7060504030201l, -7060504030201l, 0l});
    
    static final int BUF_SIZE = 1024;
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] jo = JSON_OUTPUT.serialize(fooCompare);
        byte[] jbo = JSON_BUFFERED_OUTPUT.serialize(fooCompare);
        byte[] jso = JSON_STREAMED_OUTPUT.serialize(fooCompare);
        
        assertTrue(jo.length == jbo.length);
        assertTrue(jso.length == jo.length);
        
        String joString = STRING.deser(jo);
        String jboString = STRING.deser(jbo);
        
        assertEquals(joString, jboString);
        assertEquals(joString, STRING.deser(jso));
    }
    
    public void testFooNumeric() throws Exception
    {
        Foo fooCompare = foo;
        
        byte[] jo = JSON_OUTPUT_NUMERIC.serialize(fooCompare);
        byte[] jbo = JSON_BUFFERED_OUTPUT_NUMERIC.serialize(fooCompare);
        byte[] jso = JSON_STREAMED_OUTPUT_NUMERIC.serialize(fooCompare);
        
        assertTrue(jo.length == jbo.length);
        assertTrue(jso.length == jo.length);
        
        String joString = STRING.deser(jo);
        String jboString = STRING.deser(jbo);
        
        assertEquals(joString, jboString);
        assertEquals(joString, STRING.deser(jso));
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            byte[] jo = JSON_OUTPUT.serialize(barCompare);
            byte[] jbo = JSON_BUFFERED_OUTPUT.serialize(barCompare);
            byte[] jso = JSON_STREAMED_OUTPUT.serialize(barCompare);
            
            assertTrue(jo.length == jbo.length);
            assertTrue(jso.length == jo.length);
            
            String joString = STRING.deser(jo);
            String jboString = STRING.deser(jbo);
            
            assertEquals(joString, jboString);
            assertEquals(joString, STRING.deser(jso));
        }
    }
    
    public void testBarNumeric() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            byte[] jo = JSON_OUTPUT_NUMERIC.serialize(barCompare);
            byte[] jbo = JSON_BUFFERED_OUTPUT_NUMERIC.serialize(barCompare);
            byte[] jso = JSON_STREAMED_OUTPUT_NUMERIC.serialize(barCompare);
            
            assertTrue(jo.length == jbo.length);
            assertTrue(jso.length == jo.length);
            
            String joString = STRING.deser(jo);
            String jboString = STRING.deser(jbo);
            
            assertEquals(joString, jboString);
            assertEquals(joString, STRING.deser(jso));
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            byte[] jo = JSON_OUTPUT.serialize(bazCompare);
            byte[] jbo = JSON_BUFFERED_OUTPUT.serialize(bazCompare);
            byte[] jso = JSON_STREAMED_OUTPUT.serialize(bazCompare);
            
            assertTrue(jo.length == jbo.length);
            assertTrue(jso.length == jo.length);
            
            String joString = STRING.deser(jo);
            String jboString = STRING.deser(jbo);
            
            assertEquals(joString, jboString);
            assertEquals(joString, STRING.deser(jso));
        }
    }
    
    public void testBazNumeric() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            byte[] jo = JSON_OUTPUT_NUMERIC.serialize(bazCompare);
            byte[] jbo = JSON_BUFFERED_OUTPUT_NUMERIC.serialize(bazCompare);
            byte[] jso = JSON_STREAMED_OUTPUT_NUMERIC.serialize(bazCompare);
            
            assertTrue(jo.length == jbo.length);
            assertTrue(jso.length == jo.length);
            
            String joString = STRING.deser(jo);
            String jboString = STRING.deser(jbo);
            
            assertEquals(joString, jboString);
            assertEquals(joString, STRING.deser(jso));
        }
    }
    
    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;

        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-json-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 200000);
        int loops = Integer.getInteger("benchmark.loops", 2000000);
        
        String title = "protostuff-json serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(foo, JSON_SERIALIZERS, out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void main(String[] args) throws Exception
    {
        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-json-"+System.currentTimeMillis()+".txt"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 100000);
        int loops = Integer.getInteger("benchmark.loops", 1000000);
        
        String title = "protostuff-json serialization benchmark for " + loops + " runs";
        out.println(title);
        out.println();

        start(foo, JSON_SERIALIZERS, out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static final Serializer JSON_OUTPUT = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {
            return JsonIOUtil.toByteArray(message, message.cachedSchema(), false);
        }
        
        public String getName()
        {
            return "json-output";
        }
        
    };
    
    public static final Serializer JSON_OUTPUT_NUMERIC = new Serializer()
    {

        public <T extends Message<T>> byte[] serialize(T message)
        {
            return JsonIOUtil.toByteArray(message, message.cachedSchema(), true);
        }
        
        public String getName()
        {
            return "json-output-numeric";
        }
        
    };
    
    public static final Serializer JSON_BUFFERED_OUTPUT = new Serializer()
    {
        
        final LinkedBuffer buffer = LinkedBuffer.allocate(BUF_SIZE);

        public <T extends Message<T>> byte[] serialize(T message)
        {
            try
            {
                return JsonXIOUtil.toByteArray(message, message.cachedSchema(), 
                        false, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }
        
        public String getName()
        {
            return "json-buffered-output";
        }
        
    };
    
    public static final Serializer JSON_STREAMED_OUTPUT = new Serializer()
    {
        
        final LinkedBuffer buffer = LinkedBuffer.allocate(BUF_SIZE);

        public <T extends Message<T>> byte[] serialize(T message)
        {
            try
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                JsonXIOUtil.writeTo(out, message, false, buffer);
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
            return "json-streamed-output";
        }
        
    };
    
    
    public static final Serializer JSON_BUFFERED_OUTPUT_NUMERIC = new Serializer()
    {

        final LinkedBuffer buffer = LinkedBuffer.allocate(BUF_SIZE);

        public <T extends Message<T>> byte[] serialize(T message)
        {
            try
            {
                return JsonXIOUtil.toByteArray(message, message.cachedSchema(), 
                        true, buffer);
            }
            finally
            {
                buffer.clear();
            }
        }
        
        public String getName()
        {
            return "json-buffered-output-numeric";
        }
        
    };
    
    public static final Serializer JSON_STREAMED_OUTPUT_NUMERIC = new Serializer()
    {
        
        final LinkedBuffer buffer = LinkedBuffer.allocate(BUF_SIZE);

        public <T extends Message<T>> byte[] serialize(T message)
        {
            try
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                JsonXIOUtil.writeTo(out, message, true, buffer);
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
            return "json-streamed-output-numeric";
        }
        
    };
    
    static final Serializer[] JSON_SERIALIZERS = new Serializer[]{
        JSON_OUTPUT, JSON_OUTPUT_NUMERIC,
        JSON_BUFFERED_OUTPUT, JSON_BUFFERED_OUTPUT_NUMERIC,
        JSON_STREAMED_OUTPUT, JSON_STREAMED_OUTPUT_NUMERIC
    };

}
