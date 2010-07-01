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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests for the {@link StringMapSchema}.
 *
 * @author David Yu
 * @created Jun 25, 2010
 */
public class StringMapSchemaTest extends TestCase
{
    
    public static final StringMapSchema<String> SCHEMA = StringMapSchema.VALUE_STRING;
    
    public <T extends Map<String,String>> void mergeFrom(byte[] data, int offset, int length, T message, Schema<T> schema) 
    throws IOException
    {
        IOUtil.mergeFrom(data, offset, length, message, schema);
    }
    
    private <T extends Map<String,String>> void mergeFrom(byte[] data, T message, Schema<T> schema) throws IOException
    {
        mergeFrom(data, 0, data.length, message, schema);
    }
    
    public <T extends Map<String,String>> byte[] toByteArray(T message, Schema<T> schema) throws IOException
    {
        return IOUtil.toByteArray(message, schema, 
                new LinkedBuffer(BufferedOutput.DEFAULT_BUFFER_SIZE));
    }
    
    protected Map<String,String> newMap()
    {
        return new HashMap<String,String>();
    }
    
    public void testEmptyMap() throws Exception
    {
        Map<String,String> map = newMap();
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testSingleEntry() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testSingleEntryNullValue() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("key", null);
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testSingleEntryEmptyString() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("", "");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testSingleEntryEmptyKey() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("", "value");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testSingleEntryEmptyValue() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("key", "");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testTwoEntries() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "baz");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testTwoEntriesContainsNullValue() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", null);
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testTwoEntriesContainsEmptyKey() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("", "");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testTwoEntriesContainsEmptyValue() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testMultipleEntries() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "baz");
        map.put("one", "oooooooonnnnnnnnneeeeeeee");
        map.put("ttttttttwwwwwwwwoooooooo", "two");
        map.put("three?", "3!");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testMultipleEntriesContainNullValue() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "baz");
        map.put("one", "oooooooonnnnnnnnneeeeeeee");
        map.put("ttttttttwwwwwwwwoooooooo", "two");
        map.put("three?", "3!");
        map.put("four", null);
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testMultipleEntriesContainNullValues() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "baz");
        map.put("one", "oooooooonnnnnnnnneeeeeeee");
        map.put("ttttttttwwwwwwwwoooooooo", "two");
        map.put("three?", "3!");
        map.put("four", null);
        map.put("five", null);
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testMultipleEntriesContainsEmptyKey() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "baz");
        map.put("one", "oooooooonnnnnnnnneeeeeeee");
        map.put("ttttttttwwwwwwwwoooooooo", "two");
        map.put("three?", "3!");
        map.put("", "value");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testMultipleEntriesContainsEmptyValue() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "baz");
        map.put("one", "oooooooonnnnnnnnneeeeeeee");
        map.put("ttttttttwwwwwwwwoooooooo", "two");
        map.put("three?", "3!");
        map.put("four", "");
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }
    
    public void testMultipleEntriesContainsEmptyKeyAndNullValues() throws Exception
    {
        Map<String,String> map = newMap();
        map.put("foo", "bar");
        map.put("bar", "baz");
        map.put("one", "oooooooonnnnnnnnneeeeeeee");
        map.put("ttttttttwwwwwwwwoooooooo", "two");
        map.put("three?", "3!");
        map.put("four", "");
        map.put("five", null);
        map.put("", null);
        map.put("seven", null);
        map.put("eight", null);
        
        byte[] data = toByteArray(map, SCHEMA);
        
        Map<String,String> mapCompare = newMap();
        mergeFrom(data, mapCompare, SCHEMA);
        assertEquals(map, mapCompare);
    }

}
