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

package com.dyuproject.protostuff.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonParser;

import com.dyuproject.protostuff.codegen.V22Lite.Bar;
import com.dyuproject.protostuff.codegen.V22Lite.Baz;
import com.dyuproject.protostuff.codegen.V22Lite.Foo;
import com.dyuproject.protostuff.json.ProtobufJSON;
import com.dyuproject.protostuff.json.ReflectionJSON;
import com.dyuproject.protostuff.json.ReflectionNumericJSON;
import com.google.protobuf.ByteString;

/**
 * @author David Yu
 * @created Oct 15, 2009
 */

public class GeneratedProtobufJSONTest extends TestCase
{
    static final ReflectionJSON REFLECTION_LITE = new ReflectionJSON(new Class[]{V22Lite.class});
    static final ReflectionNumericJSON REFLECTION_LITE_NUM = new ReflectionNumericJSON(new Class[]{V22Lite.class});
    static final V22LiteJSON GENERATED_LITE = new V22LiteJSON();
    static final V22LiteNumericJSON GENERATED_LITE_NUM = new V22LiteNumericJSON();
    
    static final Charset utf8 = Charset.forName("UTF-8");
    
    static final Baz baz = Baz.newBuilder()
        .setId(1)
        .setName("some_name")
        .setTimestamp(System.currentTimeMillis())
        .build();
    
    static final Bar bar = Bar.newBuilder()
        .setSomeInt(1)
        .setSomeLong(2)
        .setSomeFloat(3.03f)
        .setSomeDouble(4.0404d)
        .setSomeBoolean(true)
        .setSomeString("some_string")
        .setSomeEnum(Bar.Status.STARTED)
        .setSomeBytes(ByteString.copyFrom(new byte[]{0x30,0x31,0x32,0x33,0x34}))
        .setBaz(baz)        
        .build();
    
    static final Foo foo = Foo.newBuilder()
        .addSomeInt(1)
        .addSomeInt(10)
        .addSomeLong(2)
        .addSomeLong(20)
        .addSomeFloat(3.03f)
        .addSomeFloat(30.03f)
        .addSomeDouble(4.0404d)
        .addSomeDouble(40.0404d)
        .addSomeBoolean(true)
        .addSomeBoolean(false)
        .addSomeString("some_string")
        .addSomeString("another_string")
        .addSomeEnum(Foo.EnumSample.TYPE1)
        .addSomeEnum(Foo.EnumSample.TYPE2)
        .addSomeBytes(ByteString.copyFrom(new byte[]{0x30,0x31,0x32,0x33,0x34}))
        .addSomeBytes(ByteString.copyFrom(new byte[]{0x35,0x36,0x37,0x38,0x39}))
        .addBar(bar)
        .addBar(bar)
        .build();
    
    public static void generateAndParse(PrintStream out) throws Exception
    {
        String generatedLite = generateAndParse(GENERATED_LITE);
        String lite = generateAndParse(REFLECTION_LITE);
        String generatedLiteNum = generateAndParse(GENERATED_LITE_NUM);
        String liteNum = generateAndParse(REFLECTION_LITE_NUM);
        
        assertEquals(generatedLite, lite);
        assertEquals(generatedLiteNum, liteNum);
        
        out.println(generatedLite);
        out.println();
        out.println(lite);
        out.println();
        out.println(generatedLiteNum);
        out.println();
        out.println(liteNum);
    }
    
    static String generateAndParse(ProtobufJSON json) 
    throws Exception
    {
        StringWriter sw = new StringWriter();
        json.writeTo(sw, foo);
        String generated = sw.toString();

        JsonParser parser = json.getJsonFactory().createJsonParser(generated);
        Foo parsedFoo = json.parseFrom(parser, Foo.class);
        parser.close();
        
        assertEquals(foo, parsedFoo);        
        
        return generated;
    }
    
    static void assertEquals(Foo f, Foo f2)
    {
        assertTrue(f.getBarCount() == f2.getBarCount());
        for(int i=0, len=f.getBarCount(); i<len; i++)
            assertEquals(f.getBar(i), f2.getBar(i));
        
        assertTrue(f.getSomeBooleanCount() == f2.getSomeBooleanCount());
        for(int i=0, len=f.getSomeBooleanCount(); i<len; i++)
            assertEquals(f.getSomeBoolean(i), f2.getSomeBoolean(i));
        
        assertTrue(f.getSomeDoubleCount() == f2.getSomeDoubleCount());
        for(int i=0, len=f.getSomeDoubleCount(); i<len; i++)
            assertEquals(f.getSomeDouble(i), f2.getSomeDouble(i));
        
        assertTrue(f.getSomeFloatCount() == f2.getSomeFloatCount());
        for(int i=0, len=f.getSomeFloatCount(); i<len; i++)
            assertEquals(f.getSomeFloat(i), f2.getSomeFloat(i));
        
        assertTrue(f.getSomeIntCount() == f2.getSomeIntCount());
        for(int i=0, len=f.getSomeIntCount(); i<len; i++)
            assertEquals(f.getSomeInt(i), f2.getSomeInt(i));
        
        assertTrue(f.getSomeLongCount() == f2.getSomeLongCount());
        for(int i=0, len=f.getSomeLongCount(); i<len; i++)
            assertEquals(f.getSomeLong(i), f2.getSomeLong(i));
        
        assertTrue(f.getSomeStringCount() == f2.getSomeStringCount());
        for(int i=0, len=f.getSomeStringCount(); i<len; i++)
            assertEquals(f.getSomeString(i), f2.getSomeString(i));
        
        assertTrue(f.getSomeBytesCount() == f2.getSomeBytesCount());
        for(int i=0, len=f.getSomeBytesCount(); i<len; i++)
            assertEquals(f.getSomeBytes(i), f2.getSomeBytes(i));
        
        assertTrue(f.getSomeEnumCount() == f2.getSomeEnumCount());
        for(int i=0, len=f.getSomeEnumCount(); i<len; i++)
            assertEquals(f.getSomeEnum(i), f2.getSomeEnum(i));
    }
    
    static void assertEquals(Bar b, Bar b2)
    {
        assertEquals(b.getBaz(), b2.getBaz());
        
        assertEquals(b.getSomeBoolean(), b2.getSomeBoolean());
        assertEquals(b.getSomeDouble(), b2.getSomeDouble());
        assertEquals(b.getSomeFloat(), b2.getSomeFloat());
        assertEquals(b.getSomeInt(), b2.getSomeInt());
        assertEquals(b.getSomeLong(), b2.getSomeLong());
        assertEquals(b.getSomeString(), b2.getSomeString());
        assertEquals(b.getSomeBytes(), b2.getSomeBytes());
        assertEquals(b.getSomeEnum(), b2.getSomeEnum());
    }
    
    static void assertEquals(Baz b, Baz b2)
    {
        assertEquals(b.getId(), b2.getId());
        assertEquals(b.getName(), b2.getName());
        assertEquals(b.getTimestamp(), b2.getTimestamp());
    }
    
    static String generatedAndParse(ProtobufJSON json, List<Foo> fooList, 
            List<Foo> parsedFooList) throws Exception
    {
        StringWriter sw = new StringWriter();
        json.writeTo(sw, fooList, Foo.class);
        String generated = sw.toString();
        
        JsonParser parser = json.getJsonFactory().createJsonParser(generated);
        json.appendMessageFrom(parser, parsedFooList, Foo.class);
        parser.close();
        assertTrue(fooList.size() == parsedFooList.size());
        for(int i=0,len=fooList.size(); i<len; i++)
            assertEquals(fooList.get(i), parsedFooList.get(i));

        return generated;
    }
    
    public void testCollection() throws Exception
    {
        ArrayList<Foo> fooList = new ArrayList<Foo>();   
        ArrayList<Foo> parsedFooList = new ArrayList<Foo>();
        fooList.add(foo);
        fooList.add(foo);
        
        //System.err.println(generatedAndParse(GENERATED_LITE, fooList, parsedFooList));
        parsedFooList.clear();

        //System.err.println(generatedAndParse(REFLECTION_LITE, fooList, parsedFooList));
        parsedFooList.clear();
        
        //System.err.println(generatedAndParse(GENERATED_LITE_NUM, fooList, parsedFooList));
        parsedFooList.clear();
        
        //System.err.println(generatedAndParse(REFLECTION_LITE_NUM, fooList, parsedFooList));
        parsedFooList.clear();
    }
    
    public void testGenerateAndParse() throws Exception
    {
        generateAndParse(GENERATED_LITE);
        generateAndParse(REFLECTION_LITE);
        generateAndParse(GENERATED_LITE_NUM);
        generateAndParse(REFLECTION_LITE_NUM);
        //System.err.println(generateAndParse(GENERATED_LITE));
        //System.err.println(generateAndParse(REFLECTION_LITE));
        //System.err.println(generateAndParse(GENERATED_LITE_NUM));
        //System.err.println(generateAndParse(REFLECTION_LITE_NUM));
    }
    
    public void testBenchmark() throws Exception
    {
        if(!"false".equals(System.getProperty("benchmark.skip")))
            return;

        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-benchmark-"+System.currentTimeMillis()+".html"), true));
        
        int warmups = Integer.getInteger("benchmark.warmups", 100000);
        int loops = Integer.getInteger("benchmark.loops", 1000000);
        String title = "protostuff-json ser/deser benchmark for " + loops + " runs";
        out.println("<html><head><title>");
        out.println(title);
        out.println("</title></head><body><p>");
        out.println(title);        
        out.println("</p><pre>\n\n");
        
        generateAndParse(out);
        out.println("\n\n</pre><hr/><pre>");
        start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void main(String[] args) throws Exception
    {
        String dir = System.getProperty("benchmark.output_dir");
        
        PrintStream out = dir==null ? System.out : 
            new PrintStream(new FileOutputStream(new File(new File(dir), 
                    "protostuff-benchmark-"+System.currentTimeMillis()+".html"), true));
        int runs = Integer.getInteger("benchmark.runs", 1);
        
        
        int warmups = Integer.getInteger("benchmark.warmups", 100000);
        int loops = Integer.getInteger("benchmark.loops", 100000);
        String title = "protostuff-json ser/deser benchmark for " + loops + " runs";
        out.println("<html><head><title>");
        out.println(title);
        out.println("</title></head><body><p>");
        out.println(title);
        out.println("</p><pre>\n\n");
        
        generateAndParse(out);
        out.println("\n\n</pre><hr/><pre>");
        for(int i=0; i<runs; i++)
            start(out, warmups, loops);
        
        if(System.out!=out)
            out.close();
    }
    
    public static void start(PrintStream out, int warmups, int loops) throws Exception
    {
        serDeser(out, GENERATED_LITE, "GENERATED_LITE", warmups, loops);
        serDeser(out, REFLECTION_LITE, "REFLECTION_LITE", warmups, loops);
        serDeser(out, GENERATED_LITE_NUM, "GENERATED_LITE_NUM", warmups, loops);
        serDeser(out, REFLECTION_LITE_NUM, "REFLECTION_LITE_NUM", warmups, loops);
    }
    
    static void serDeser(PrintStream out, ProtobufJSON json, String name, int warmups, int loops)
    throws Exception
    {
        int len = generateAndParse(json).getBytes(utf8).length;
        for(int i=0; i<warmups; i++)
            generateAndParse(json);
        long start = System.currentTimeMillis();
        for(int i=0; i<loops; i++)
            generateAndParse(json);
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        out.println(elapsed + " ms elapsed with " + len + " bytes for " + name);
    }

}
