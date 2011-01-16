//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.ProtostuffPipeTest;
import com.dyuproject.protostuff.Schema;

/**
 * Test for polymorphic serialization.
 * 
 * See {@link PolymorphicRuntimeField} for details.
 *
 * @author David Yu
 * @created Jan 15, 2011
 */
public class PolymorphicSerializationTest extends AbstractTest
{
    
    public static abstract class Animal
    {
        protected String properName;

        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((properName == null)?0:properName.hashCode());
            return result;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Animal other = (Animal)obj;
            if (properName == null)
            {
                if (other.properName != null)
                    return false;
            }
            else if (!properName.equals(other.properName))
                return false;
            return true;
        }

        
    }

    public static class Zoo
    {
        protected Animal largestAnimal;
        protected List<Animal> animals = new ArrayList<Animal>();

        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((animals == null)?0:animals.hashCode());
            result = prime * result + ((largestAnimal == null)?0:largestAnimal.hashCode());
            return result;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Zoo other = (Zoo)obj;
            if (animals == null)
            {
                if (other.animals != null)
                    return false;
            }
            else if (!animals.equals(other.animals))
                return false;
            if (largestAnimal == null)
            {
                if (other.largestAnimal != null)
                    return false;
            }
            else if (!largestAnimal.equals(other.largestAnimal))
                return false;
            return true;
        }
        
    }

    public static abstract class Mammal extends Animal
    {
        protected float normalBodyTemperature;

        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Float.floatToIntBits(normalBodyTemperature);
            return result;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Mammal other = (Mammal)obj;
            if (Float.floatToIntBits(normalBodyTemperature) != Float.floatToIntBits(other.normalBodyTemperature))
                return false;
            return true;
        }
        
        
    }

    public static class Bear extends Mammal
    {
        protected String someBearField;

        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((someBearField == null)?0:someBearField.hashCode());
            return result;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Bear other = (Bear)obj;
            if (someBearField == null)
            {
                if (other.someBearField != null)
                    return false;
            }
            else if (!someBearField.equals(other.someBearField))
                return false;
            return true;
        }
        
        
    }

    public static class Tiger extends Mammal
    {
        protected String someTigerField;

        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((someTigerField == null)?0:someTigerField.hashCode());
            return result;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Tiger other = (Tiger)obj;
            if (someTigerField == null)
            {
                if (other.someTigerField != null)
                    return false;
            }
            else if (!someTigerField.equals(other.someTigerField))
                return false;
            return true;
        }
        
        
    }
    
    public static class Elephant extends Mammal
    {
        protected String someElephantField;

        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((someElephantField == null)?0:someElephantField.hashCode());
            return result;
        }

        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Elephant other = (Elephant)obj;
            if (someElephantField == null)
            {
                if (other.someElephantField != null)
                    return false;
            }
            else if (!someElephantField.equals(other.someElephantField))
                return false;
            return true;
        }
        
        
    }
    
    static Bear filledBear()
    {
        Bear bear = new Bear();
        bear.someBearField = "bearField";
        bear.normalBodyTemperature = 20f;
        bear.properName = "bear";
        return bear;
    }
    
    static Tiger filledTiger()
    {
        Tiger tiger = new Tiger();
        tiger.someTigerField = "tigerField";
        tiger.normalBodyTemperature = 30f;
        tiger.properName = "tiger";
        return tiger;
    }
    
    static Elephant filledElephant()
    {
        Elephant elephant = new Elephant();
        elephant.someElephantField = "elephantField";
        elephant.normalBodyTemperature = 25f;
        elephant.properName = "elephant";
        return elephant;
    }
    
    static Zoo filledZoo()
    {
        Zoo zoo = new Zoo();
        
        Elephant elephant = filledElephant();
        Bear bear = filledBear(); 
        Tiger tiger = filledTiger(); 
        
        zoo.largestAnimal = elephant;
        
        zoo.animals.add(bear);
        zoo.animals.add(tiger);
        zoo.animals.add(elephant);
        
        return zoo;
    }
    
    public void testProtobuf() throws Exception
    {
        Schema<Zoo> schema = RuntimeSchema.getSchema(Zoo.class);
        Zoo p = filledZoo();

        byte[] data = ProtobufIOUtil.toByteArray(p, schema, buf());
        //System.err.println("protobuf: " + data.length);
        
        Zoo p2 = new Zoo();
        ProtobufIOUtil.mergeFrom(data, p2, schema);
        
        assertEquals(p, p2);
        
        List<Zoo> list = new ArrayList<Zoo>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtobufIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<Zoo> parsedList = ProtobufIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }
    
    public void testProtostuff() throws Exception
    {
        Schema<Zoo> schema = RuntimeSchema.getSchema(Zoo.class);
        Zoo p = filledZoo();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, buf());
        //System.err.println("protostuff: " + data.length);
        
        Zoo p2 = new Zoo();
        ProtostuffIOUtil.mergeFrom(data, 0, data.length, p2, schema);
        
        assertEquals(p, p2);
        
        List<Zoo> list = new ArrayList<Zoo>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<Zoo> parsedList = ProtostuffIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }
    
    public void testPipe() throws Exception
    {
        Schema<Zoo> schema = RuntimeSchema.getSchema(Zoo.class);
        Pipe.Schema<Zoo> pipeSchema = ((MappedSchema<Zoo>)schema).pipeSchema;
        Zoo p = filledZoo();
        
        ProtostuffPipeTest.roundTrip(p, schema, pipeSchema);
    }

}
