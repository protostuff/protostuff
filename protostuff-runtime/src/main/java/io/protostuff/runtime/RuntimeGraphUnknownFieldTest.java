//========================================================================
//Copyright 2015 David Yu
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

package io.protostuff.runtime;

import io.protostuff.AbstractTest;
import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.WireFormat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test unknown fields encoded with {@link WireFormat#WIRETYPE_REFERENCE}.
 * 
 * @author David Yu
 * @created May 20, 2016
 */
public class RuntimeGraphUnknownFieldTest extends AbstractTest
{
    
    static final class Client
    {
        static class ClassB
        {
            @Tag(1)
            String b1;

            ClassB()
            {
                this.b1 = "b1";
            }
        }

        static class SuperClassA
        {
            @Tag(201)
            String sa1;
        }

        static class ClassA extends SuperClassA
        {
            @Tag(101)
            String a1;

            @Tag(102)
            ClassB a2;

            public ClassA(ClassB a2)
            {
            }
        }
    }
    
    static final class Server
    {
        static class ClassB
        {
            @Tag(1)
            String b1;

            ClassB()
            {
                this.b1 = "b1";
            }
        }
        static class SuperClassA
        {
            @Tag(201)
            String sa1;

            @Tag(202)
            ClassB sa2;

            public SuperClassA(ClassB sa2)
            {
                this.sa1 = "sa1";
                this.sa2 = sa2;
            }
        }

        static class ClassA extends SuperClassA
        {
            @Tag(101)
            String a1;

            @Tag(102)
            ClassB a2;

            public ClassA(ClassB a2)
            {
                super(a2);
                this.a1 = "a1";
                this.a2 = a2;
            }
        }
    }
    
    public void testIt() throws IOException
    {
        Server.ClassB b = new Server.ClassB();
        Server.ClassA a = new Server.ClassA(b);
        byte[] data = serializeGraph(a);
        
        Client.ClassA aFromBuffer = deserializeGraph(data, 
                Client.ClassA.class);
        
        assertNotNull(aFromBuffer.sa1);
        
        Client.ClassA aFromStream = deserializeGraph(new ByteArrayInputStream(data), 
                Client.ClassA.class);
        
        assertNotNull(aFromStream.sa1);
    }

    private static <T> byte[] serializeGraph(T object)
    {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) object.getClass();
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        return GraphIOUtil.toByteArray(object, schema, LinkedBuffer.allocate());
    }

    private static <T> T deserializeGraph(byte[] bytes, Class<T> clz)
    {
        Schema<T> schema = RuntimeSchema.getSchema(clz);
        T obj = schema.newMessage();
        GraphIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
    
    private static <T> T deserializeGraph(InputStream in, Class<T> clz) throws IOException
    {
        Schema<T> schema = RuntimeSchema.getSchema(clz);
        T obj = schema.newMessage();
        GraphIOUtil.mergeFrom(in, obj, schema);
        return obj;
    }
    
}
