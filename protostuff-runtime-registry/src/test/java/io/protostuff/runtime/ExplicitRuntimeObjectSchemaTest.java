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

package io.protostuff.runtime;

import static io.protostuff.runtime.SampleDelegates.SINGLETON_DELEGATE;

import junit.framework.TestCase;

import io.protostuff.CollectionSchema;
import io.protostuff.MapSchema;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.AcousticGuitar;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.BassGuitar;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.Bat;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.CustomArrayList;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.CustomHashMap;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.GuitarPickup;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.Pojo;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithArray;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithArray2D;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithClassFields;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithCollection;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithCustomArrayListAndHashMap;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithMap;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithObjectCollectionFields;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithObjectCollectionNullKV;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithObjectMapFields;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithShortArrayAsDelegate;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithSingletonAsDelegate;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithSingletonMapNullKV;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithThrowable;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.PojoWithThrowableArray;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.Size;
import io.protostuff.runtime.AbstractRuntimeObjectSchemaTest.WrapsBat;
import io.protostuff.runtime.SampleDelegates.ShortArrayDelegate;

/**
 * Test for {@link ExplicitIdStrategy}.
 * 
 * @author David Yu
 * @created Mar 29, 2012
 */
public class ExplicitRuntimeObjectSchemaTest extends TestCase
{

    static final boolean runTest;

    static
    {
        // check whether test/run from root module
        String strategy = System.getProperty("test_id_strategy");
        runTest = strategy == null || strategy.equals("explicit");

        if (runTest)
        {
            System.setProperty("protostuff.runtime.id_strategy_factory",
                    "io.protostuff.runtime.ExplicitRuntimeObjectSchemaTest$IdStrategyFactory");
        }
    }

    public static class IdStrategyFactory implements IdStrategy.Factory
    {

        static int INSTANCE_COUNT = 0;

        ExplicitIdStrategy.Registry r = new ExplicitIdStrategy.Registry();

        public IdStrategyFactory()
        {
            ++INSTANCE_COUNT;
            System.out.println("@EXPLICIT");
        }

        public IdStrategy create()
        {
            return r.strategy;
        }

        public void postCreate()
        {
            r.registerCollection(CollectionSchema.MessageFactories.ArrayList, 1)
                    .registerCollection(CollectionSchema.MessageFactories.HashSet, 2)
                    .registerCollection(CustomArrayList.MESSAGE_FACTORY, 3)
                    .registerCollection(CollectionSchema.MessageFactories.LinkedList, 4)
                    .registerCollection(CollectionSchema.MessageFactories.TreeSet, 5);

            r.registerMap(MapSchema.MessageFactories.HashMap, 1)
                    .registerMap(MapSchema.MessageFactories.LinkedHashMap, 2)
                    .registerMap(CustomHashMap.MESSAGE_FACTORY, 3)
                    .registerMap(MapSchema.MessageFactories.TreeMap, 4);

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
                    .registerPojo(PojoWithCustomArrayListAndHashMap.class, 10)
                    .registerPojo(PojoWithClassFields.class, 11)
                    .registerPojo(PojoWithObjectCollectionFields.class, 12)
                    .registerPojo(PojoWithObjectCollectionNullKV.class, 13)
                    .registerPojo(PojoWithObjectMapFields.class, 14)
                    .registerPojo(PojoWithSingletonMapNullKV.class, 15)
                    .registerPojo(PojoWithThrowable.class, 16)
                    .registerPojo(Throwable.class, 17)
                    .registerPojo(Exception.class, 18)
                    .registerPojo(RuntimeException.class, 19)
                    .registerPojo(PojoWithThrowableArray.class, 20)
                    .registerPojo(PojoWithSingletonAsDelegate.class, 21)
                    .registerPojo(PojoWithShortArrayAsDelegate.class, 22)
                    .registerPojo(StackTraceElement.class, 23);

            r.registerDelegate(new ShortArrayDelegate(), 1);
            r.registerDelegate(SINGLETON_DELEGATE, 2);

            r = null;
        }

    }

    public void testProtostuff() throws Exception
    {
        if (runTest && RuntimeEnv.ID_STRATEGY instanceof ExplicitIdStrategy)
        {
            junit.textui.TestRunner tr = new junit.textui.TestRunner();
            tr.doRun(tr.getTest(
                    "io.protostuff.runtime.ProtostuffRuntimeObjectSchemaTest"
                    ), false);

            assertTrue(IdStrategyFactory.INSTANCE_COUNT != 0);
        }
    }

    public void testProtobuf() throws Exception
    {
        if (runTest && RuntimeEnv.ID_STRATEGY instanceof ExplicitIdStrategy)
        {
            junit.textui.TestRunner tr = new junit.textui.TestRunner();
            tr.doRun(tr.getTest(
                    "io.protostuff.runtime.ProtobufRuntimeObjectSchemaTest"
                    ), false);

            assertTrue(IdStrategyFactory.INSTANCE_COUNT != 0);
        }
    }
}
