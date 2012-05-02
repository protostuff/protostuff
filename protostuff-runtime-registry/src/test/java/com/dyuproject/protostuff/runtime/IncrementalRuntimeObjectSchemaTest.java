//================================================================================
//Copyright (c) 2012, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================


package com.dyuproject.protostuff.runtime;

import static com.dyuproject.protostuff.runtime.SampleDelegates.SINGLETON_DELEGATE;
import junit.framework.TestCase;

import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.MapSchema;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.AcousticGuitar;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.BassGuitar;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.Bat;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.CustomArrayList;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.CustomHashMap;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.GuitarPickup;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.Pojo;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithArray;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithArray2D;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithCollection;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithCustomArrayListAndHashMap;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithMap;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.Size;
import com.dyuproject.protostuff.runtime.AbstractRuntimeObjectSchemaTest.WrapsBat;
import com.dyuproject.protostuff.runtime.SampleDelegates.ShortArrayDelegate;

/**
 * Test for {@link IncrementalIdStrategy}.
 *
 * @author David Yu
 * @created Mar 29, 2012
 */
public class IncrementalRuntimeObjectSchemaTest extends TestCase
{
    
    static final boolean runTest;
    
    static
    {
        // check whether test/run from root module
        String strategy = System.getProperty("test_id_strategy");
        runTest = strategy == null || strategy.equals("incremental");
        
        if(runTest)
        {
            System.setProperty("protostuff.runtime.id_strategy_factory", 
                    "com.dyuproject.protostuff.runtime.IncrementalRuntimeObjectSchemaTest$IdStrategyFactory");
        }
    }
    
    public static class IdStrategyFactory implements IdStrategy.Factory
    {
        
        static int INSTANCE_COUNT = 0;
        
        IncrementalIdStrategy.Registry r = new IncrementalIdStrategy.Registry(
                20, 11, 
                20, 11, 
                20, 11, 
                80, 11);
        
        public IdStrategyFactory()
        {
            ++INSTANCE_COUNT;
            System.out.println("@INCREMENTAL");
        }

        public IdStrategy create()
        {
            return r.strategy;
        }

        public void postCreate()
        {
            r.registerCollection(CollectionSchema.MessageFactories.ArrayList, 1)
                .registerCollection(CollectionSchema.MessageFactories.HashSet, 2)
                .registerCollection(CustomArrayList.MESSAGE_FACTORY, 3);
                
            
            r.registerMap(MapSchema.MessageFactories.HashMap, 1)
                .registerMap(MapSchema.MessageFactories.LinkedHashMap, 2)
                .registerMap(CustomHashMap.MESSAGE_FACTORY, 3);
            
            r.registerEnum(Size.class, 1)
                .registerEnum(GuitarPickup.class, 2);
            
            r.registerPojo(AcousticGuitar.class, 1)
                .registerPojo(BassGuitar.class, 2)
                .registerPojo(Pojo.class, 3)
                .registerPojo(PojoWithArray.class, 4)
                .registerPojo(PojoWithArray2D.class, 5)
                .registerPojo(PojoWithCollection.class, 6)
                .registerPojo(PojoWithMap.class, 7)
                .registerPojo(Bat.SCHEMA, Bat.PIPE_SCHEMA, 8)
                .registerPojo(WrapsBat.class, 9)
                .registerPojo(PojoWithCustomArrayListAndHashMap.class, 10);
            
            r.registerDelegate(new ShortArrayDelegate(), 1);
            r.registerDelegate(SINGLETON_DELEGATE, 2);
            
            r = null;
        }
        
    }
    
    public void testProtostuff() throws Exception
    {
        if(runTest && RuntimeEnv.ID_STRATEGY instanceof IncrementalIdStrategy)
        {
            junit.textui.TestRunner tr = new junit.textui.TestRunner();
            tr.doRun(tr.getTest(
                    "com.dyuproject.protostuff.runtime.ProtostuffRuntimeObjectSchemaTest"
                    ), false);
            
            assertTrue(IdStrategyFactory.INSTANCE_COUNT != 0);
        }
    }
    
    public void testProtobuf() throws Exception
    {
        if(runTest && RuntimeEnv.ID_STRATEGY instanceof IncrementalIdStrategy)
        {
            junit.textui.TestRunner tr = new junit.textui.TestRunner();
            tr.doRun(tr.getTest(
                    "com.dyuproject.protostuff.runtime.ProtobufRuntimeObjectSchemaTest"
                    ), false);
            
            assertTrue(IdStrategyFactory.INSTANCE_COUNT != 0);
        }
    }

}
