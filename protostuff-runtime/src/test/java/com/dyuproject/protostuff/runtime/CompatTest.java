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

package com.dyuproject.protostuff.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.StringSerializer.STRING;

/**
 * Test that the runtime schema would have the same output as hand-coded/code-generated 
 * schema.
 *
 * @author David Yu
 * @created Oct 20, 2010
 */
public class CompatTest extends AbstractTest
{
    
    public void testCompat() throws IOException
    {
        compareBar();
        
        if(!RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
        {
            compareFoo();
        }
    }
    
    static void compareBar()
    {
        com.dyuproject.protostuff.Bar bar1 = com.dyuproject.protostuff.SerializableObjects.bar;
        Schema<com.dyuproject.protostuff.Bar> schema1 = com.dyuproject.protostuff.Bar.getSchema();
        
        Bar bar2 = SerializableObjects.bar;
        Schema<Bar> schema2 = RuntimeSchema.getSchema(Bar.class);
        
        Schema<com.dyuproject.protostuff.Bar> schema3 = RuntimeSchema.getSchema(com.dyuproject.protostuff.Bar.class);
        
        byte[] byte1 = ProtostuffIOUtil.toByteArray(bar1, schema1, buf());
        byte[] byte2 = ProtostuffIOUtil.toByteArray(bar2, schema2, buf());
        byte[] byte3 = ProtostuffIOUtil.toByteArray(bar1, schema3, buf());
        
        String str1 = STRING.deser(byte1);
        String str2 = STRING.deser(byte2);
        String str3 = STRING.deser(byte3);
        
        assertEquals(str1, str2);
        assertEquals(str1, str3);
    }
    
    static void compareFoo()
    {
        com.dyuproject.protostuff.Foo foo1 = com.dyuproject.protostuff.SerializableObjects.foo;
        Schema<com.dyuproject.protostuff.Foo> schema1 = com.dyuproject.protostuff.Foo.getSchema();
        
        Foo foo2 = SerializableObjects.foo;
        Schema<Foo> schema2 = RuntimeSchema.getSchema(Foo.class);
        
        Schema<com.dyuproject.protostuff.Foo> schema3 = RuntimeSchema.getSchema(com.dyuproject.protostuff.Foo.class);
        
        byte[] byte1 = ProtostuffIOUtil.toByteArray(foo1, schema1, buf());
        byte[] byte2 = ProtostuffIOUtil.toByteArray(foo2, schema2, buf());
        byte[] byte3 = ProtostuffIOUtil.toByteArray(foo1, schema3, buf());
        
        String str1 = STRING.deser(byte1);
        String str2 = STRING.deser(byte2);
        String str3 = STRING.deser(byte3);
        
        assertEquals(str1, str2);
        assertEquals(str1, str3);
    }
    
    static <T extends Message<T>> Schema<T> getCachedSchema(Class<T> clazz) 
    throws InstantiationException, IllegalAccessException
    {
        Schema<T> schema = clazz.newInstance().cachedSchema();
        //System.err.println("! " + schema + " | " + System.identityHashCode(schema));
        return schema;
    }
    
    @SuppressWarnings("unchecked")
    public void testMixed() throws Exception
    {
        if(RuntimeEnv.COLLECTION_SCHEMA_ON_REPEATED_FIELDS)
            return;
        
        Schema<Mixed> schema = RuntimeSchema.getSchema(Mixed.class);
        
        assertTrue(MappedSchema.class.isAssignableFrom(schema.getClass()));
        
        MappedSchema<Mixed> mappedSchema = (MappedSchema<Mixed>)schema;
        
        assertTrue(
                RuntimeMessageField.class.isAssignableFrom(
                        mappedSchema.fieldsByName.get("rfoo").getClass())
                );
        assertTrue(
                RuntimeMessageField.class.isAssignableFrom(
                        mappedSchema.fieldsByName.get("rbar").getClass())
                );
        assertTrue(
                RuntimeMessageField.class.isAssignableFrom(
                        mappedSchema.fieldsByName.get("rbaz").getClass())
                );
        
        RuntimeMessageField<Mixed,com.dyuproject.protostuff.Foo> rfoo = 
            (RuntimeMessageField<Mixed,com.dyuproject.protostuff.Foo>)mappedSchema.fieldsByName.get("rfoo");
        
        RuntimeMessageField<Mixed,com.dyuproject.protostuff.Bar> rbar = 
            (RuntimeMessageField<Mixed,com.dyuproject.protostuff.Bar>)mappedSchema.fieldsByName.get("rbar");
        
        RuntimeMessageField<Mixed,com.dyuproject.protostuff.Baz> rbaz = 
            (RuntimeMessageField<Mixed,com.dyuproject.protostuff.Baz>)mappedSchema.fieldsByName.get("rbaz");
        
        assertTrue(rfoo.getSchema().getClass().isAssignableFrom(
                getCachedSchema(com.dyuproject.protostuff.Foo.class).getClass()));
        
        assertTrue(rbar.getSchema().getClass().isAssignableFrom(
                getCachedSchema(com.dyuproject.protostuff.Bar.class).getClass()));
        
        assertTrue(rbaz.getSchema().getClass().isAssignableFrom(
                getCachedSchema(com.dyuproject.protostuff.Baz.class).getClass()));
    }
    
    public static class Mixed
    {
        int id;
        
        Foo fo;
        Bar br;
        Baz bz;
        
        com.dyuproject.protostuff.Foo foo;
        com.dyuproject.protostuff.Bar bar;
        com.dyuproject.protostuff.Baz baz;
        
        List<com.dyuproject.protostuff.Foo> rfoo;
        Set<com.dyuproject.protostuff.Bar> rbar;
        Collection<com.dyuproject.protostuff.Baz> rbaz;
    }
    
    
    static byte[] ba(String text)
    {
        return STRING.ser(text);
    }
    
    static ByteString bs(String text)
    {
        return ByteString.copyFromUtf8(text);
    }
    
    static <T> List<T> newList()
    {
        return new ArrayList<T>();
    }
    
    static <K,V> Map<K,V> newMap()
    {
        return new LinkedHashMap<K,V>();
    }
    
    public void testByteArrayCompat()
    {
        PojoWithByteArray pwba = new PojoWithByteArray();
        PojoWithByteString pwbs = new PojoWithByteString();
        fill(pwba);
        fill(pwbs);
        
        byte[] b1 = ProtostuffIOUtil.toByteArray(pwba, 
                RuntimeSchema.getSchema(PojoWithByteArray.class), buf());
        
        byte[] b2 = ProtostuffIOUtil.toByteArray(pwbs, 
                RuntimeSchema.getSchema(PojoWithByteString.class), buf());
        
        assertTrue(Arrays.equals(b1, b2));
    }
    
    public enum Direction
    {
        NORTH,
        SOUTH,
        EAST,
        WEST;
    }
    
    public static class PojoWithByteArray
    {
        byte[] bytes;
        List<byte[]> bytesList;
        
        Map<Integer, byte[]> scalarKeyMap;
        Map<Direction, byte[]> enumKeyMap;
        Map<Baz, byte[]> pojoKeyMap;
        
        Map<byte[], Integer> scalarValueMap;
        Map<byte[], Direction> enumValueMap;
        Map<byte[], Baz> pojoValueMap;
        
        Map<byte[], byte[]> bytesMap;
    }
    
    public static class PojoWithByteString
    {
        ByteString bytes;
        List<ByteString> bytesList;
        
        Map<Integer, ByteString> scalarKeyMap;
        Map<Direction, ByteString> enumKeyMap;
        Map<Baz, ByteString> pojoKeyMap;
        
        Map<ByteString, Integer> scalarValueMap;
        Map<ByteString, Direction> enumValueMap;
        Map<ByteString, Baz> pojoValueMap;
        
        Map<ByteString, ByteString> bytesMap;
    }
    
    static void fill(PojoWithByteArray pwba)
    {
        pwba.bytes = ba("1");
        
        pwba.bytesList = newList();
        pwba.bytesList.add(ba("2"));
        pwba.bytesList.add(ba("3"));
        
        pwba.scalarKeyMap = newMap();
        pwba.scalarKeyMap.put(1, ba("4"));
        pwba.scalarKeyMap.put(2, ba("5"));
        
        pwba.enumKeyMap = newMap();
        pwba.enumKeyMap.put(Direction.EAST, ba("6"));
        pwba.enumKeyMap.put(Direction.WEST, ba("7"));
        
        pwba.pojoKeyMap = newMap();
        pwba.pojoKeyMap.put(new Baz(1, "bz1", 0), ba("8"));
        pwba.pojoKeyMap.put(new Baz(2, "bz2", 0), ba("9"));
        
        pwba.scalarValueMap = newMap();
        pwba.scalarValueMap.put(ba("10"), 1);
        pwba.scalarValueMap.put(ba("11"), 2);
        
        pwba.enumValueMap = newMap();
        pwba.enumValueMap.put(ba("12"), Direction.EAST);
        pwba.enumValueMap.put(ba("13"), Direction.WEST);
        
        pwba.pojoValueMap = newMap();
        pwba.pojoValueMap.put(ba("14"), new Baz(1, "bz1", 0));
        pwba.pojoValueMap.put(ba("15"), new Baz(2, "bz2", 0));
        
        pwba.bytesMap = newMap();
        pwba.bytesMap.put(ba("16"), ba("17"));
        pwba.bytesMap.put(ba("18"), ba("19"));
    }
    
    static void fill(PojoWithByteString pwbs)
    {
        pwbs.bytes = bs("1");
        
        pwbs.bytesList = newList();
        pwbs.bytesList.add(bs("2"));
        pwbs.bytesList.add(bs("3"));
        
        pwbs.scalarKeyMap = newMap();
        pwbs.scalarKeyMap.put(1, bs("4"));
        pwbs.scalarKeyMap.put(2, bs("5"));
        
        pwbs.enumKeyMap = newMap();
        pwbs.enumKeyMap.put(Direction.EAST, bs("6"));
        pwbs.enumKeyMap.put(Direction.WEST, bs("7"));
        
        pwbs.pojoKeyMap = newMap();
        pwbs.pojoKeyMap.put(new Baz(1, "bz1", 0), bs("8"));
        pwbs.pojoKeyMap.put(new Baz(2, "bz2", 0), bs("9"));
        
        pwbs.scalarValueMap = newMap();
        pwbs.scalarValueMap.put(bs("10"), 1);
        pwbs.scalarValueMap.put(bs("11"), 2);
        
        pwbs.enumValueMap = newMap();
        pwbs.enumValueMap.put(bs("12"), Direction.EAST);
        pwbs.enumValueMap.put(bs("13"), Direction.WEST);
        
        pwbs.pojoValueMap = newMap();
        pwbs.pojoValueMap.put(bs("14"), new Baz(1, "bz1", 0));
        pwbs.pojoValueMap.put(bs("15"), new Baz(2, "bz2", 0));
        
        pwbs.bytesMap = newMap();
        pwbs.bytesMap.put(bs("16"), bs("17"));
        pwbs.bytesMap.put(bs("18"), bs("19"));
    }

}
