//========================================================================
//Copyright 2017 Alex Shvid
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

package io.protostuff;

import static io.protostuff.runtime.SerializableObjects.bar;
import static io.protostuff.runtime.SerializableObjects.baz;
import static io.protostuff.runtime.SerializableObjects.foo;
import static io.protostuff.runtime.SerializableObjects.negativeBar;
import static io.protostuff.runtime.SerializableObjects.negativeBaz;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.Assert;

import io.protostuff.runtime.Bar;
import io.protostuff.runtime.Baz;
import io.protostuff.runtime.Foo;
import io.protostuff.runtime.PolymorphicSerializationTest;
import io.protostuff.runtime.PolymorphicSerializationTest.Zoo;
import io.protostuff.runtime.RuntimeSchema;
import io.protostuff.runtime.SerializableObjects;

/**
 * Test for MsgpackXOutput on runtime pojos (polymorphic too).
 * 
 * @author Alex Shvid
 */
public class MsgpackXNumericOutputTest extends AbstractTest
{

    private static final Schema<Foo> FOO_SCHEMA = RuntimeSchema.getSchema(Foo.class);
    private static final Schema<Bar> BAR_SCHEMA = RuntimeSchema.getSchema(Bar.class);
    private static final Schema<Baz> BAZ_SCHEMA = RuntimeSchema.getSchema(Baz.class);
    private static final Schema<Zoo> ZOO_SCHEMA = RuntimeSchema.getSchema(Zoo.class);

    public void testFoo() throws Exception
    {
        byte[] xdata = MsgpackXIOUtil.toByteArray(foo, FOO_SCHEMA, true, buf());
        byte[] data = MsgpackIOUtil.toByteArray(foo, FOO_SCHEMA, true);
        
        Assert.assertTrue(Arrays.equals(data, xdata));
    }

    public void testFooStreamed() throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer buffer = buf();
        try
        {
            MsgpackXIOUtil.writeTo(out, foo, FOO_SCHEMA, true, buffer);
        }
        finally
        {
            buffer.clear();
        }
        byte[] xdata = out.toByteArray();
        byte[] data = MsgpackIOUtil.toByteArray(foo, FOO_SCHEMA, true);

        Assert.assertTrue(Arrays.equals(data, xdata));
    }

    public void testBar() throws Exception
    {

        for (Bar barCompare : new Bar[] { bar, negativeBar })
        {

            byte[] xdata = MsgpackXIOUtil.toByteArray(barCompare, BAR_SCHEMA, true, buf());
            byte[] data = MsgpackIOUtil.toByteArray(barCompare, BAR_SCHEMA, true);

            Assert.assertTrue(Arrays.equals(data, xdata));

        }
    }

    public void testBarStreamed() throws Exception
    {

        for (Bar barCompare : new Bar[] { bar, negativeBar })
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer buffer = buf();
            try
            {
                MsgpackXIOUtil.writeTo(out, barCompare, BAR_SCHEMA, true, buffer);
            }
            finally
            {
                buffer.clear();
            }
            byte[] xdata = out.toByteArray();
            byte[] data = MsgpackIOUtil.toByteArray(barCompare, BAR_SCHEMA, true);
            
            Assert.assertTrue(Arrays.equals(data, xdata));
        }
    }

    public void testBaz() throws Exception
    {

        for (Baz bazCompare : new Baz[] { baz, negativeBaz })
        {
            byte[] xdata = MsgpackXIOUtil.toByteArray(bazCompare, BAZ_SCHEMA, true, buf());
            byte[] data = MsgpackIOUtil.toByteArray(bazCompare, BAZ_SCHEMA, true);
            
            Assert.assertTrue(Arrays.equals(data, xdata));
        }
    }

    public void testBazStreamed() throws Exception
    {

        for (Baz bazCompare : new Baz[] { baz, negativeBaz })
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            LinkedBuffer buffer = buf();
            try
            {
                MsgpackXIOUtil.writeTo(out, bazCompare, BAZ_SCHEMA, true, buffer);
            }
            finally
            {
                buffer.clear();
            }
            byte[] xdata = out.toByteArray();
            byte[] data = MsgpackIOUtil.toByteArray(bazCompare, BAZ_SCHEMA, true);
            
            Assert.assertTrue(Arrays.equals(data, xdata));
        }
    }

    public void testPolymorphic() throws Exception
    {
        Zoo zooCompare = PolymorphicSerializationTest.filledZoo();

        Zoo dzoo = new Zoo();

        byte[] data = MsgpackXIOUtil.toByteArray(zooCompare, ZOO_SCHEMA, true, buf());

        MsgpackIOUtil.mergeFrom(data, dzoo, ZOO_SCHEMA, true);
        SerializableObjects.assertEquals(zooCompare, dzoo);
    }

    public void testPolymorphicStreamed() throws Exception
    {
        Zoo zooCompare = PolymorphicSerializationTest.filledZoo();

        Zoo dzoo = new Zoo();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LinkedBuffer buffer = buf();
        try
        {
            MsgpackXIOUtil.writeTo(out, zooCompare, ZOO_SCHEMA, true, buffer);
        }
        finally
        {
            buffer.clear();
        }
        byte[] data = out.toByteArray();

        MsgpackIOUtil.mergeFrom(data, dzoo, ZOO_SCHEMA, true);
        SerializableObjects.assertEquals(zooCompare, dzoo);
    }

}
