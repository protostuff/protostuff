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
 * Testing for json ser/deser against messages.
 *
 * @author David Yu
 * @created Nov 20, 2009
 */
public class JsonCoreSerDeserTest extends TestCase
{
    
    public void testFoo() throws Exception
    {
        Foo fooCompare = foo;
        Foo dfoo = new Foo();
        
        byte[] data = JsonIOUtil.toByteArray(fooCompare, fooCompare.cachedSchema(), false);
        JsonIOUtil.mergeFrom(data, dfoo, dfoo.cachedSchema(), false);
        SerializableObjects.assertEquals(fooCompare, dfoo);
        System.err.println(new String(data, ByteString.UTF8));
    }
    
    public void testBar() throws Exception
    {
        for(Bar barCompare : new Bar[]{bar, negativeBar})
        {
            Bar dbar = new Bar();            
            
            byte[] data = JsonIOUtil.toByteArray(barCompare, barCompare.cachedSchema(), false);
            JsonIOUtil.mergeFrom(data, dbar, dbar.cachedSchema(), false);
            SerializableObjects.assertEquals(barCompare, dbar);
            System.err.println(new String(data, ByteString.UTF8));
        }
    }
    
    public void testBaz() throws Exception
    {
        for(Baz bazCompare : new Baz[]{baz, negativeBaz})
        {
            Baz dbaz = new Baz();            
            
            byte[] data = JsonIOUtil.toByteArray(bazCompare, bazCompare.cachedSchema(), false);
            JsonIOUtil.mergeFrom(data, dbaz, dbaz.cachedSchema(), false);
            SerializableObjects.assertEquals(bazCompare, dbaz);
            System.err.println(new String(data, ByteString.UTF8));
        }
    }

}
