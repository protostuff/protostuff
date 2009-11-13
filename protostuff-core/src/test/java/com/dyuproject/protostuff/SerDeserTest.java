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

import static com.dyuproject.protostuff.SerializableObjects.bar;
import static com.dyuproject.protostuff.SerializableObjects.baz;
import static com.dyuproject.protostuff.SerializableObjects.foo;
import static com.dyuproject.protostuff.SerializableObjects.negativeBar;
import static com.dyuproject.protostuff.SerializableObjects.negativeBaz;
import junit.framework.TestCase;

/**
 * Serialization and deserialization test cases.
 *
 * @author David Yu
 * @created Nov 10, 2009
 */
public class SerDeserTest extends TestCase
{
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        Foo cfoo = new Foo();
        Foo dfoo = new Foo();        
        
        int expectedSize = ComputedSizeOutput.getSize(fooCompare, true);
        
        byte[] coded = IOUtil.toByteArrayComputed(fooCompare, true);
        assertTrue(coded.length == expectedSize);
        IOUtil.mergeFrom(coded, cfoo);      
        SerializableObjects.assertEquals(fooCompare, cfoo);

        byte[] deferred = IOUtil.toByteArrayDeferred(fooCompare, true);
        assertTrue(deferred.length == expectedSize);
        IOUtil.mergeFrom(deferred, dfoo);
        SerializableObjects.assertEquals(fooCompare, dfoo);
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar cbar = new Bar();
            Bar dbar = new Bar();            
            
            int expectedSize = ComputedSizeOutput.getSize(barCompare);
            
            byte[] coded = IOUtil.toByteArrayComputed(barCompare);
            assertTrue(coded.length == expectedSize);
            IOUtil.mergeFrom(coded, cbar);        
            SerializableObjects.assertEquals(barCompare, cbar);

            byte[] deferred = IOUtil.toByteArrayDeferred(barCompare);
            assertTrue(deferred.length == expectedSize);
            IOUtil.mergeFrom(deferred, dbar);
            SerializableObjects.assertEquals(barCompare, dbar);
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz cbaz = new Baz();
            Baz dbaz = new Baz();            
            
            int expectedSize = ComputedSizeOutput.getSize(bazCompare);
            
            byte[] coded = IOUtil.toByteArrayComputed(bazCompare);
            assertTrue(coded.length == expectedSize);
            IOUtil.mergeFrom(coded, cbaz);        
            SerializableObjects.assertEquals(bazCompare, cbaz);

            byte[] deferred = IOUtil.toByteArrayDeferred(bazCompare);
            assertTrue(deferred.length == expectedSize);
            IOUtil.mergeFrom(deferred, dbaz);
            SerializableObjects.assertEquals(bazCompare, dbaz);
        }
    }
    

}
