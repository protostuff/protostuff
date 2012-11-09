//========================================================================
//Copyright 2012 David Yu
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

package com.dyuproject.protostuff.parser;

import junit.framework.TestCase;

/**
 * Tests for {@link Formatter}.
 * 
 * @author David Yu
 * @created Nov 9, 2012
 */
public class FomatterTest extends TestCase
{
    
    static void verify(Formatter f, String expect, String value)
    {
        assertEquals(expect, f.format(value));
    }
    
    public void testCC()
    {
        final Formatter f = Formatter.BUILTIN.CC;
        
        verify(f, "someFoo", "some_foo");
        verify(f, "someFoo", "SomeFoo");
        
        // verify that it does not change anything
        verify(f, "someFoo", "someFoo");
    }
    
    public void testCCU()
    {
        final Formatter f = Formatter.BUILTIN.CCU;
        
        verify(f, "someFoo_", "some_foo");
        verify(f, "someFoo_", "SomeFoo");
        verify(f, "someFoo_", "someFoo");
    }
    
    public void testUC()
    {
        final Formatter f = Formatter.BUILTIN.UC;
        
        verify(f, "some_foo", "someFoo");
        verify(f, "some_foo", "SomeFoo");
        
        // verify that it does not change anything
        verify(f, "some_foo", "some_foo");
    }
    
    public void testUCU()
    {
        final Formatter f = Formatter.BUILTIN.UCU;
        
        verify(f, "some_foo_", "someFoo");
        verify(f, "some_foo_", "SomeFoo");
        verify(f, "some_foo_", "some_foo");
    }
    
    public void testUUC()
    {
        final Formatter f = Formatter.BUILTIN.UUC;
        
        verify(f, "SOME_FOO", "someFoo");
        verify(f, "SOME_FOO", "SomeFoo");
        verify(f, "SOME_FOO", "some_foo");
        
        // verify that it does not change anything
        verify(f, "SOME_FOO", "SOME_FOO");
    }
    
    public void testPC()
    {
        final Formatter f = Formatter.BUILTIN.PC;
        
        verify(f, "SomeFoo", "someFoo");
        verify(f, "SomeFoo", "some_foo");
        
        // verify that it does not change anything
        verify(f, "SomeFoo", "SomeFoo");
    }
    
    public void testPCS()
    {
        final Formatter f = Formatter.BUILTIN.PCS;
        
        verify(f, "Some Foo", "someFoo");
        verify(f, "Some Foo", "some_foo");
        verify(f, "Some Foo", "SomeFoo");
        
        // verify that it does not change anything
        verify(f, "Some Foo", "Some Foo");
    }
}
