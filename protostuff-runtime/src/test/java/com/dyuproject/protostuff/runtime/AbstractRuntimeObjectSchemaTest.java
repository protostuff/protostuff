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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.ByteString;
import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.MapSchema;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;

/**
 * Ser/deser tests for {@link ObjectSchema}.
 *
 * @author David Yu
 * @created Feb 3, 2011
 */
public abstract class AbstractRuntimeObjectSchemaTest extends AbstractTest
{
    
    static
    {
        // to test varios runtime strategies, run:
        // mvn -Dtest_id_strategy=$1 -DforkMode=never -Dtest=*RuntimeObjectSchemaTest -DfailIfNoTests=false test
        
        // where $1 can be: incremental|explicit
        
        // check if the protostuff-runtime-registry test module is on the classpath
        boolean registryTestModuleExists = false;
        try
        {
            registryTestModuleExists = null != Class.forName(
                    "com.dyuproject.protostuff.runtime.TestDummy", 
                    false, 
                    Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            // ignore
        }
        
        if(registryTestModuleExists)
        {
            String strategy = System.getProperty("test_id_strategy");
            if("incremental".equals(strategy))
            {
                System.setProperty("protostuff.runtime.id_strategy_factory", 
                        "com.dyuproject.protostuff.runtime.IncrementalRuntimeObjectSchemaTest$IdStrategyFactory");
            }
            else if("explicit".equals(strategy))
            {
                System.setProperty("protostuff.runtime.id_strategy_factory", 
                        "com.dyuproject.protostuff.runtime.ExplicitRuntimeObjectSchemaTest$IdStrategyFactory");
            }
        }
    }
    
    static class CustomArrayList<T> extends ArrayList<T>
    {
        private static final long serialVersionUID = 1L;
        
        static final CollectionSchema.MessageFactory MESSAGE_FACTORY = 
                new CollectionSchema.MessageFactory()
        {
            public <V> Collection<V> newMessage()
            {
                return new CustomArrayList<V>();
            }

            public Class<?> typeClass()
            {
                return CustomArrayList.class;
            }
        };
    }
    
    static class CustomHashMap<K,V> extends HashMap<K,V>
    {
        private static final long serialVersionUID = 1L;
        
        static final MapSchema.MessageFactory MESSAGE_FACTORY = 
                new MapSchema.MessageFactory()
        {
            public <K, V> Map<K, V> newMessage()
            {
                return new CustomHashMap<K,V>();
            }
            
            public Class<?> typeClass()
            {
                return CustomHashMap.class;
            }
        };
    }
    
    /**
     * Serializes the {@code message} into a byte array.
     */
    protected abstract <T> byte[] toByteArray(T message, Schema<T> schema);
    
    protected abstract <T> void writeTo(OutputStream out, T message, Schema<T> schema)
            throws IOException;
    
    /**
     * Deserializes from the byte array.
     */
    protected abstract <T> void mergeFrom(byte[] data, int offset, int length, 
            T message, Schema<T> schema) throws IOException;
    
    /**
     * Deserializes from the inputstream.
     */
    protected abstract <T> void mergeFrom(InputStream in, T message, Schema<T> schema) 
            throws IOException;
    
    
    protected abstract <T> void roundTrip(T message, Schema<T> schema, 
            Pipe.Schema<T> pipeSchema) throws Exception;
    
    
    public interface HasType
    {
        int getType(); 
    }
    
    public enum Size implements HasType
    {
        SMALL
        {
            public int getType()
            {
                return 1;
            }
        }
        ,
        MEDIUM
        {
            public int getType()
            {
                return 2;
            }
        }
        ,
        LARGE
        {
            public int getType()
            {
                return 3;
            }
        }
        ;
    }
    
    public interface Instrument
    {
        
        public String getName();
        
    }
    
    public static abstract class AbstractInstrument implements Instrument
    {
        protected String name;
        
        public AbstractInstrument()
        {
            
        }
        
        public AbstractInstrument(String name)
        {
            this.name = name;
        }
        
        public String getName()
        {
            return name;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null)?0:name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            AbstractInstrument other = (AbstractInstrument)obj;
            if (name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            return true;
        }
        
    }
    
    public static abstract class Guitar extends AbstractInstrument
    {
        protected int stringCount;
        
        public Guitar()
        {
            
        }
        
        public Guitar(String name, int stringCount)
        {
            super(name);
            this.stringCount = stringCount;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + stringCount;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Guitar other = (Guitar)obj;
            if (stringCount != other.stringCount)
                return false;
            return true;
        }
        
    }
    
    public enum GuitarPickup
    {
        UNDER_THE_SADDLE,
        SOUNDHOLE,
        CONTACT,
        MICROPHONE;
    }
    
    public static final class AcousticGuitar extends Guitar
    {
        protected GuitarPickup guitarPickup;  
        
        public AcousticGuitar()
        {
            
        }
        
        public AcousticGuitar(GuitarPickup guitarPickup)
        {
            super("guitar", 6);
            this.guitarPickup = guitarPickup;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((guitarPickup == null)?0:guitarPickup.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            AcousticGuitar other = (AcousticGuitar)obj;
            if (guitarPickup == null)
            {
                if (other.guitarPickup != null)
                    return false;
            }
            else if (!guitarPickup.equals(other.guitarPickup))
                return false;
            return true;
        }
        
    }
    
    public static final class BassGuitar extends Guitar
    {
        protected boolean active;
        
        public BassGuitar()
        {
            
        }
        
        public BassGuitar(int stringCount, boolean active)
        {
            super("bass guitar", stringCount);
            this.active = active;
        }
    }
    
    public static class Pojo
    {
        Object expectBoolean;
        Object expectByte;
        Object expectCharacter;
        Object expectShort;
        Object expectInteger;
        Object expectLong;
        Object expectFloat;
        Object expectDouble;
        Object expectString;
        Object expectByteString;
        Object expectBigDecimal;
        Object expectBigInteger;
        Object expectDate;
        Object expectObject;
        Object expectEnum;

        Object expectBassGuitar;
        
        Object expectCollectionStringV;
        Object expectCollectionEnumV;
        Object expectCollectionGuitarV;
        
        Object expectCollectionIntegerCollectionV;
        Object expectCollectionEnumCollectionV;
        Object expectCollectionGuitarCollectionV;
        
        Object expectMapIntegerKStringV;
        Object expectMapEnumKGuitarV;
        Object expectMapGuitarKEnumCollectionV;
        Object expectMapGuitarCollectionKStringCollectionV;
        Object expectMapStringEnumKMapDateGuitarV;
        
        Pojo fill()
        {
            expectBoolean = Boolean.TRUE;
            expectByte = Byte.valueOf((byte)0);
            expectCharacter = Character.valueOf('c');
            expectShort = Short.valueOf((short)1);
            expectInteger = Integer.valueOf(2);
            expectLong = Long.valueOf(System.currentTimeMillis());
            expectFloat = Float.valueOf(123.321f);
            expectDouble = Double.valueOf(1234567.7654321d);
            expectString = "foo";
            expectByteString = ByteString.copyFromUtf8("gg");

            // comment out because xml format barfs (contains xml control chars)
            //expectBigDecimal = BigDecimal.valueOf(5555.5555d);
            //expectBigInteger = BigInteger.valueOf(123456789);
            expectDate = new Date();

            expectEnum = Size.LARGE;
            
            expectCollectionStringV = newList(new String[]{"bar", "baz"});

            expectBassGuitar = new BassGuitar(4, false);
            
            expectCollectionEnumV = newList(
                    new Size[]{Size.SMALL, Size.MEDIUM, Size.LARGE}
            );
            
            expectCollectionGuitarV = newList(
                    new Guitar[]{
                            new AcousticGuitar(GuitarPickup.MICROPHONE),
                            new BassGuitar(5, true)
                    }
            );
            
            List<Integer> intFirst = newList(
                    new Integer[]{1,2,3,4}
            );
            
            List<Integer> intSecond = newList(
                    new Integer[]{4,5,6,7}
            );
            
            expectCollectionIntegerCollectionV = newList(
                    new List[]{intFirst, intSecond}
            );
            
            List<Size> enumFirst = newList(new Size[]{Size.SMALL});
            List<Size> enumSecond = newList(new Size[]{Size.MEDIUM});
            List<Size> enumThird = newList(new Size[]{Size.LARGE});
            
            expectCollectionEnumCollectionV = newList(
                    new List[]{enumFirst, enumSecond, enumThird}
            );
            
            List<Guitar> guitarFirst = newList(
                    new Guitar[]{
                            new AcousticGuitar(GuitarPickup.MICROPHONE),
                            new BassGuitar(5, true)
                    }
            );
            
            List<Guitar> guitarSecond = newList(
                    new Guitar[]{
                            new AcousticGuitar(GuitarPickup.CONTACT),
                            new BassGuitar(6, true)
                    }
            );
            
            expectCollectionGuitarCollectionV = newList(
                    new List[]{guitarFirst, guitarSecond}
            );
            
            
            Map<Integer,String> mapIntegerKStringV = newMap();
            mapIntegerKStringV.put(1, "1");
            mapIntegerKStringV.put(null, "2");
            mapIntegerKStringV.put(3, null);
            mapIntegerKStringV.put(4, "4");

            expectMapIntegerKStringV = mapIntegerKStringV;
            
            Map<Size,Guitar> mapEnumKGuitarV = newMap();
            mapEnumKGuitarV.put(Size.SMALL, new AcousticGuitar(GuitarPickup.CONTACT));
            mapEnumKGuitarV.put(Size.MEDIUM, new BassGuitar(5, false));
            
            expectMapEnumKGuitarV = mapEnumKGuitarV;
            
            Map<Guitar,List<Size>> mapGuitarKEnumCollectionV = newMap();
            mapGuitarKEnumCollectionV.put(new BassGuitar(4, false), newList(new Size[]{Size.SMALL}));
            mapGuitarKEnumCollectionV.put(new BassGuitar(5, true), newList(new Size[]{Size.MEDIUM}));
            
            expectMapGuitarKEnumCollectionV = mapGuitarKEnumCollectionV;
            
            List<Guitar> guitarK1 = newList(new Guitar[]{
                    new BassGuitar(4, true)
            });
            
            List<Guitar> guitarK2 = newList(new Guitar[]{
                    new AcousticGuitar(GuitarPickup.UNDER_THE_SADDLE),
                    new BassGuitar(5, false)
            });
            
            Map<List<Guitar>,List<String>> mapGuitarCollectionKStringCollectionV = newMap();
            mapGuitarCollectionKStringCollectionV.put(guitarK1, newList(new String[]{"1"}));
            mapGuitarCollectionKStringCollectionV.put(guitarK2, newList(new String[]{"1", "2"}));
            
            expectMapGuitarCollectionKStringCollectionV = mapGuitarCollectionKStringCollectionV;
            
            Map<String,Size> mapKeyFirst = newMap();
            mapKeyFirst.put("foo", Size.LARGE);
            mapKeyFirst.put(null, null);
            
            Map<String,Size> mapKeySecond = newMap();
            mapKeySecond.put("bar", Size.MEDIUM);
            mapKeySecond.put("baz", null);
            
            Map<Date,Guitar> mapValueFirst = newMap();
            mapValueFirst.put(new Date(System.currentTimeMillis()-10000), new BassGuitar(5, false));
            
            Map<Date,Guitar> mapValueSecond = newMap();
            mapValueSecond.put(null, new BassGuitar(4, true));
            mapValueSecond.put(new Date(), new AcousticGuitar(GuitarPickup.CONTACT));
            
            Map<Map<String,Size>,Map<Date,Guitar>> mapStringEnumKMapDateGuitarV = newMap();
            mapStringEnumKMapDateGuitarV.put(mapKeyFirst, mapValueFirst);
            mapStringEnumKMapDateGuitarV.put(mapKeySecond, mapValueSecond);
            
            expectMapStringEnumKMapDateGuitarV = mapStringEnumKMapDateGuitarV;
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((expectBassGuitar == null)?0:expectBassGuitar.hashCode());
            result = prime * result + ((expectBigDecimal == null)?0:expectBigDecimal.hashCode());
            result = prime * result + ((expectBigInteger == null)?0:expectBigInteger.hashCode());
            result = prime * result + ((expectBoolean == null)?0:expectBoolean.hashCode());
            result = prime * result + ((expectByte == null)?0:expectByte.hashCode());
            result = prime * result + ((expectByteString == null)?0:expectByteString.hashCode());
            result = prime * result + ((expectCharacter == null)?0:expectCharacter.hashCode());
            result = prime * result + ((expectCollectionEnumCollectionV == null)?0:expectCollectionEnumCollectionV.hashCode());
            result = prime * result + ((expectCollectionEnumV == null)?0:expectCollectionEnumV.hashCode());
            result = prime * result + ((expectCollectionGuitarCollectionV == null)?0:expectCollectionGuitarCollectionV.hashCode());
            result = prime * result + ((expectCollectionGuitarV == null)?0:expectCollectionGuitarV.hashCode());
            result = prime * result + ((expectCollectionIntegerCollectionV == null)?0:expectCollectionIntegerCollectionV.hashCode());
            result = prime * result + ((expectCollectionStringV == null)?0:expectCollectionStringV.hashCode());
            result = prime * result + ((expectDate == null)?0:expectDate.hashCode());
            result = prime * result + ((expectDouble == null)?0:expectDouble.hashCode());
            result = prime * result + ((expectEnum == null)?0:expectEnum.hashCode());
            result = prime * result + ((expectFloat == null)?0:expectFloat.hashCode());
            result = prime * result + ((expectInteger == null)?0:expectInteger.hashCode());
            result = prime * result + ((expectLong == null)?0:expectLong.hashCode());
            result = prime * result + ((expectMapEnumKGuitarV == null)?0:expectMapEnumKGuitarV.hashCode());
            result = prime * result + ((expectMapGuitarCollectionKStringCollectionV == null)?0:expectMapGuitarCollectionKStringCollectionV.hashCode());
            result = prime * result + ((expectMapGuitarKEnumCollectionV == null)?0:expectMapGuitarKEnumCollectionV.hashCode());
            result = prime * result + ((expectMapIntegerKStringV == null)?0:expectMapIntegerKStringV.hashCode());
            result = prime * result + ((expectMapStringEnumKMapDateGuitarV == null)?0:expectMapStringEnumKMapDateGuitarV.hashCode());
            result = prime * result + ((expectObject == null)?0:expectObject.hashCode());
            result = prime * result + ((expectShort == null)?0:expectShort.hashCode());
            result = prime * result + ((expectString == null)?0:expectString.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pojo other = (Pojo)obj;
            if (expectBassGuitar == null)
            {
                if (other.expectBassGuitar != null)
                    return false;
            }
            else if (!expectBassGuitar.equals(other.expectBassGuitar))
                return false;
            if (expectBigDecimal == null)
            {
                if (other.expectBigDecimal != null)
                    return false;
            }
            else if (!expectBigDecimal.equals(other.expectBigDecimal))
                return false;
            if (expectBigInteger == null)
            {
                if (other.expectBigInteger != null)
                    return false;
            }
            else if (!expectBigInteger.equals(other.expectBigInteger))
                return false;
            if (expectBoolean == null)
            {
                if (other.expectBoolean != null)
                    return false;
            }
            else if (!expectBoolean.equals(other.expectBoolean))
                return false;
            if (expectByte == null)
            {
                if (other.expectByte != null)
                    return false;
            }
            else if (!expectByte.equals(other.expectByte))
                return false;
            if (expectByteString == null)
            {
                if (other.expectByteString != null)
                    return false;
            }
            else if (!expectByteString.equals(other.expectByteString))
                return false;
            if (expectCharacter == null)
            {
                if (other.expectCharacter != null)
                    return false;
            }
            else if (!expectCharacter.equals(other.expectCharacter))
                return false;
            if (expectCollectionEnumCollectionV == null)
            {
                if (other.expectCollectionEnumCollectionV != null)
                    return false;
            }
            else if (!expectCollectionEnumCollectionV.equals(other.expectCollectionEnumCollectionV))
                return false;
            if (expectCollectionEnumV == null)
            {
                if (other.expectCollectionEnumV != null)
                    return false;
            }
            else if (!expectCollectionEnumV.equals(other.expectCollectionEnumV))
                return false;
            if (expectCollectionGuitarCollectionV == null)
            {
                if (other.expectCollectionGuitarCollectionV != null)
                    return false;
            }
            else if (!expectCollectionGuitarCollectionV.equals(other.expectCollectionGuitarCollectionV))
                return false;
            if (expectCollectionGuitarV == null)
            {
                if (other.expectCollectionGuitarV != null)
                    return false;
            }
            else if (!expectCollectionGuitarV.equals(other.expectCollectionGuitarV))
                return false;
            if (expectCollectionIntegerCollectionV == null)
            {
                if (other.expectCollectionIntegerCollectionV != null)
                    return false;
            }
            else if (!expectCollectionIntegerCollectionV.equals(other.expectCollectionIntegerCollectionV))
                return false;
            if (expectCollectionStringV == null)
            {
                if (other.expectCollectionStringV != null)
                    return false;
            }
            else if (!expectCollectionStringV.equals(other.expectCollectionStringV))
                return false;
            if (expectDate == null)
            {
                if (other.expectDate != null)
                    return false;
            }
            else if (!expectDate.equals(other.expectDate))
                return false;
            if (expectDouble == null)
            {
                if (other.expectDouble != null)
                    return false;
            }
            else if (!expectDouble.equals(other.expectDouble))
                return false;
            if (expectEnum == null)
            {
                if (other.expectEnum != null)
                    return false;
            }
            else if (!expectEnum.equals(other.expectEnum))
                return false;
            if (expectFloat == null)
            {
                if (other.expectFloat != null)
                    return false;
            }
            else if (!expectFloat.equals(other.expectFloat))
                return false;
            if (expectInteger == null)
            {
                if (other.expectInteger != null)
                    return false;
            }
            else if (!expectInteger.equals(other.expectInteger))
                return false;
            if (expectLong == null)
            {
                if (other.expectLong != null)
                    return false;
            }
            else if (!expectLong.equals(other.expectLong))
                return false;
            if (expectMapEnumKGuitarV == null)
            {
                if (other.expectMapEnumKGuitarV != null)
                    return false;
            }
            else if (!expectMapEnumKGuitarV.equals(other.expectMapEnumKGuitarV))
                return false;
            if (expectMapGuitarCollectionKStringCollectionV == null)
            {
                if (other.expectMapGuitarCollectionKStringCollectionV != null)
                    return false;
            }
            else if (!expectMapGuitarCollectionKStringCollectionV.equals(other.expectMapGuitarCollectionKStringCollectionV))
                return false;
            if (expectMapGuitarKEnumCollectionV == null)
            {
                if (other.expectMapGuitarKEnumCollectionV != null)
                    return false;
            }
            else if (!expectMapGuitarKEnumCollectionV.equals(other.expectMapGuitarKEnumCollectionV))
                return false;
            if (expectMapIntegerKStringV == null)
            {
                if (other.expectMapIntegerKStringV != null)
                    return false;
            }
            else if (!expectMapIntegerKStringV.equals(other.expectMapIntegerKStringV))
                return false;
            if (expectMapStringEnumKMapDateGuitarV == null)
            {
                if (other.expectMapStringEnumKMapDateGuitarV != null)
                    return false;
            }
            else if (!expectMapStringEnumKMapDateGuitarV.equals(other.expectMapStringEnumKMapDateGuitarV))
                return false;
            if (expectObject == null)
            {
                if (other.expectObject != null)
                    return false;
            }
            else if (!expectObject.equals(other.expectObject))
                return false;
            if (expectShort == null)
            {
                if (other.expectShort != null)
                    return false;
            }
            else if (!expectShort.equals(other.expectShort))
                return false;
            if (expectString == null)
            {
                if (other.expectString != null)
                    return false;
            }
            else if (!expectString.equals(other.expectString))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Pojo [expectBassGuitar=" + expectBassGuitar + ", expectBigDecimal=" + expectBigDecimal + ", expectBigInteger=" + expectBigInteger
                    + ", expectBoolean=" + expectBoolean + ", expectByte=" + expectByte + ", expectByteString=" + expectByteString + ", expectCharacter="
                    + expectCharacter + ", expectCollectionEnumCollectionV=" + expectCollectionEnumCollectionV + ", expectCollectionEnumV="
                    + expectCollectionEnumV + ", expectCollectionGuitarCollectionV=" + expectCollectionGuitarCollectionV + ", expectCollectionGuitarV="
                    + expectCollectionGuitarV + ", expectCollectionIntegerCollectionV=" + expectCollectionIntegerCollectionV + ", expectCollectionStringV="
                    + expectCollectionStringV + ", expectDate=" + expectDate + ", expectDouble=" + expectDouble + ", expectEnum=" + expectEnum
                    + ", expectFloat=" + expectFloat + ", expectInteger=" + expectInteger + ", expectLong=" + expectLong + ", expectMapEnumKGuitarV="
                    + expectMapEnumKGuitarV + ", expectMapGuitarCollectionKStringCollectionV=" + expectMapGuitarCollectionKStringCollectionV
                    + ", expectMapGuitarKEnumCollectionV=" + expectMapGuitarKEnumCollectionV + ", expectMapIntegerKStringV=" + expectMapIntegerKStringV
                    + ", expectMapStringEnumKMapDateGuitarV=" + expectMapStringEnumKMapDateGuitarV + ", expectObject=" + expectObject + ", expectShort="
                    + expectShort + ", expectString=" + expectString + "]";
        }
        
    }
    
    static <T> ArrayList<T> newList(T[] args)
    {
        ArrayList<T> list = new ArrayList<T>();
        
        for(T v : args)
            list.add(v);
        
        return list;
    }
    
    static <K,V> HashMap<K,V> newMap()
    {
        return new HashMap<K,V>();
    }
    
    public static class PojoWithArray
    {
        Object expectByteArray;
        Object expectPrimitiveFloatArray;
        Object expectStringArray;
        
        Object expectEnumArray;
        
        // concrete pojo type
        Object expectPojoArray;
        // polymorphic pojo type
        Object expectGuitarArray;

        Object expectListStringVArray;
        Object expectListPojoVArray;
        Object expectListGuitarVArray;
        Object expectListObjectVArray;
        
        Object expectMapEnumKStringVArray;
        Object expectMapGuitarKPojoVArray;
        Object expectMapObjectKObjectVArray;
        
        PojoWithArray fill()
        {
            expectByteArray = new byte[]{100,101};
            
            expectPrimitiveFloatArray = new float[]{3.3f, 4.4f};
            
            expectStringArray = new String[]{"1", "2", "3"};
            
            expectEnumArray = new GuitarPickup[]{
                    GuitarPickup.CONTACT,
                    GuitarPickup.MICROPHONE
            };
            
            expectPojoArray = new Pojo[]{new Pojo().fill()};
            
            expectGuitarArray = new Guitar[]{
                    new BassGuitar(5, true), 
                    new AcousticGuitar(GuitarPickup.SOUNDHOLE)
            };
            
            expectListStringVArray = new List[]{
                    newList(new String[]{"foo", "bar", "baz"}), 
                    newList(new String[]{"1", "2"}),
                    newList(new String[]{"3"})
            };
            
            expectListPojoVArray = new List[]{
                    newList(new Pojo[]{ new Pojo().fill()})
            };
            
            expectListGuitarVArray = new List[]{
                    newList(new Guitar[]{
                            new BassGuitar(6, true),
                            new AcousticGuitar(GuitarPickup.UNDER_THE_SADDLE)
                    }),
                    newList(new Guitar[]{
                            new BassGuitar(5, false),
                    })
            };
                    
            expectListObjectVArray = new List[]{
                    newList(new Object[]{
                            new Pojo().fill(),
                            new AcousticGuitar(GuitarPickup.CONTACT),
                            new BassGuitar(4, true)
                    })
            };
            
            Map<Size,String> mapEnumKStringVFirst = newMap();
            mapEnumKStringVFirst.put(Size.SMALL, "s");
            mapEnumKStringVFirst.put(Size.MEDIUM, null);
            
            Map<Size,String> mapEnumKStringVSecond = newMap();
            mapEnumKStringVSecond.put(Size.LARGE, "l");
            mapEnumKStringVSecond.put(null, "m");
            
            expectMapEnumKStringVArray = new Map[]{
                    mapEnumKStringVFirst,
                    mapEnumKStringVSecond
            };
            
            Map<Guitar,Pojo> mapGuitarKPojoVFirst = newMap();
            mapGuitarKPojoVFirst.put(new AcousticGuitar(GuitarPickup.SOUNDHOLE), null);
            
            Map<Guitar,Pojo> mapGuitarKPojoVSecond = newMap();
            mapGuitarKPojoVSecond.put(null, new Pojo().fill());
            
            expectMapGuitarKPojoVArray = new Map[]{
                    mapGuitarKPojoVFirst,
                    mapGuitarKPojoVSecond
            };
            
            Map<Object,Object> mapObjectKObjectVFirst = newMap();
            mapObjectKObjectVFirst.put("foo", null);
            mapObjectKObjectVFirst.put(new Pojo().fill(), new BassGuitar(5, true));
            
            Map<Object,Object> mapObjectKObjectVSecond = newMap();
            mapObjectKObjectVSecond.put(null, new Date());
            mapObjectKObjectVSecond.put(Size.MEDIUM, "m");
            
            expectMapObjectKObjectVArray = new Map[]{
                    mapObjectKObjectVFirst,
                    mapObjectKObjectVSecond
            };
            
            return this;
        }


        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((expectByteArray == null)?0:Arrays.hashCode((byte[])expectByteArray));
            result = prime * result + ((expectPrimitiveFloatArray == null)?0:Arrays.hashCode((float[])expectPrimitiveFloatArray));
            result = prime * result + ((expectEnumArray == null)?0:Arrays.hashCode((Object[])expectEnumArray));
            result = prime * result + ((expectStringArray == null)?0:Arrays.hashCode((Object[])expectStringArray));
            result = prime * result + ((expectPojoArray == null)?0:Arrays.hashCode((Object[])expectPojoArray));
            result = prime * result + ((expectGuitarArray == null)?0:Arrays.hashCode((Object[])expectGuitarArray));
            result = prime * result + ((expectListStringVArray == null)?0:Arrays.hashCode((Object[])expectListStringVArray));
            result = prime * result + ((expectListPojoVArray == null)?0:Arrays.hashCode((Object[])expectListPojoVArray));
            result = prime * result + ((expectListGuitarVArray == null)?0:Arrays.hashCode((Object[])expectListGuitarVArray));
            result = prime * result + ((expectListObjectVArray == null)?0:Arrays.hashCode((Object[])expectListObjectVArray));
            result = prime * result + ((expectMapEnumKStringVArray == null)?0:Arrays.hashCode((Object[])expectMapEnumKStringVArray));
            result = prime * result + ((expectMapGuitarKPojoVArray == null)?0:Arrays.hashCode((Object[])expectMapGuitarKPojoVArray));
            result = prime * result + ((expectMapObjectKObjectVArray == null)?0:Arrays.hashCode((Object[])expectMapObjectKObjectVArray));
            return result;
        }


        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoWithArray other = (PojoWithArray)obj;
            if (expectByteArray == null)
            {
                if (other.expectByteArray != null)
                    return false;
            }
            else if (!Arrays.equals((byte[])expectByteArray, (byte[])other.expectByteArray))
                return false;
            if (expectPrimitiveFloatArray == null)
            {
                if (other.expectPrimitiveFloatArray != null)
                    return false;
            }
            else if (!Arrays.equals((float[])expectPrimitiveFloatArray, (float[])other.expectPrimitiveFloatArray))
                return false;
            if (expectStringArray == null)
            {
                if (other.expectStringArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectStringArray, (Object[])other.expectStringArray))
                return false;
            if (expectEnumArray == null)
            {
                if (other.expectEnumArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectEnumArray, (Object[])other.expectEnumArray))
                return false;
            if (expectPojoArray == null)
            {
                if (other.expectPojoArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectPojoArray, (Object[])other.expectPojoArray))
                return false;
            if (expectGuitarArray == null)
            {
                if (other.expectGuitarArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectGuitarArray, (Object[])other.expectGuitarArray))
                return false;
            if (expectListStringVArray == null)
            {
                if (other.expectListStringVArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectListStringVArray, (Object[])other.expectListStringVArray))
                return false;
            if (expectListPojoVArray == null)
            {
                if (other.expectListPojoVArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectListPojoVArray, (Object[])other.expectListPojoVArray))
                return false;
            if (expectListGuitarVArray == null)
            {
                if (other.expectListGuitarVArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectListGuitarVArray, (Object[])other.expectListGuitarVArray))
                return false;
            if (expectListObjectVArray == null)
            {
                if (other.expectListObjectVArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectListObjectVArray, (Object[])other.expectListObjectVArray))
                return false;
            if (expectMapEnumKStringVArray == null)
            {
                if (other.expectMapEnumKStringVArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectMapEnumKStringVArray, (Object[])other.expectMapEnumKStringVArray))
                return false;
            if (expectMapGuitarKPojoVArray == null)
            {
                if (other.expectMapGuitarKPojoVArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectMapGuitarKPojoVArray, (Object[])other.expectMapGuitarKPojoVArray))
                return false;
            if (expectMapObjectKObjectVArray == null)
            {
                if (other.expectMapObjectKObjectVArray != null)
                    return false;
            }
            else if (!Arrays.equals((Object[])expectMapObjectKObjectVArray, (Object[])other.expectMapObjectKObjectVArray))
                return false;
            return true;
        }


        @Override
        public String toString()
        {
            return "PojoWithArray [expectByteArray=" + expectByteArray + ", expectEnumArray=" + expectEnumArray + ", expectGuitarArray=" + expectGuitarArray
                    + ", expectListGuitarVArray=" + expectListGuitarVArray + ", expectListObjectVArray=" + expectListObjectVArray + ", expectListPojoVArray="
                    + expectListPojoVArray + ", expectListStringVArray=" + expectListStringVArray + ", expectMapEnumKStringVArray="
                    + expectMapEnumKStringVArray + ", expectMapGuitarKPojoVArray=" + expectMapGuitarKPojoVArray + ", expectMapObjectKObjectVArray="
                    + expectMapObjectKObjectVArray + ", expectPojoArray=" + expectPojoArray + ", expectPrimitiveFloatArray=" + expectPrimitiveFloatArray
                    + ", expectStringArray=" + expectStringArray + "]";
        }
        
    }
    
    public static class PojoWithArray2D
    {
        Object expectByteArray2D;
        Object expectPrimitiveIntArray2D;
        Object expectDateArray2D;
        
        
        PojoWithArray2D fill()
        {
            byte[][] byteArray2D = new byte[][]{ 
                    new byte[]{70, 71}, 
                    new byte[]{80, 81}
            };
            
            expectByteArray2D = byteArray2D;
            
            int[][] intArray2D = new int[][]{ 
                    new int[]{00, 01}, 
                    new int[]{10, 11}, 
                    new int[]{20, 21}
            };
            
            expectPrimitiveIntArray2D = intArray2D;
            
            long now = System.currentTimeMillis();
            
            Date[][] dateArray2D = new Date[][]{
                    new Date[]{new Date(now-1000)},
                    new Date[]{new Date(now)},
                    new Date[]{new Date(now+1000)}
            };
            
            expectDateArray2D = dateArray2D;
            
            return this;
        }


        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((expectByteArray2D == null)?0:Arrays.hashCode((Object[])expectByteArray2D));
            result = prime * result + ((expectPrimitiveIntArray2D == null)?0:Arrays.hashCode((Object[])expectPrimitiveIntArray2D));
            result = prime * result + ((expectDateArray2D == null)?0:Arrays.hashCode((Object[])expectDateArray2D));
            return result;
        }


        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoWithArray2D other = (PojoWithArray2D)obj;
            if (expectByteArray2D == null)
            {
                if (other.expectByteArray2D != null)
                    return false;
            }
            else if (!isEquals((byte[][])expectByteArray2D, (byte[][])other.expectByteArray2D))
                return false;
            if (expectPrimitiveIntArray2D == null)
            {
                if (other.expectPrimitiveIntArray2D != null)
                    return false;
            }
            else if (!isEquals((int[][])expectPrimitiveIntArray2D, (int[][])other.expectPrimitiveIntArray2D))
                return false;
            if (expectDateArray2D == null)
            {
                if (other.expectDateArray2D != null)
                    return false;
            }
            else if (!isEquals((Object[][])expectDateArray2D, (Object[][])other.expectDateArray2D))
                return false;
            return true;
        }


        @Override
        public String toString()
        {
            return "PojoWithArray2D [expectByteArray2D=" + expectByteArray2D + ", expectDateArray2D=" + expectDateArray2D + ", expectPrimitiveIntArray2D="
                    + expectPrimitiveIntArray2D + "]";
        }
        
    }
    
    public static class PojoWithCollection
    {
        Collection<Object> someCollectionObjectV;
        Collection<?> someCollectionWildcardV;
        
        List<Object> someListObjectV;
        List<?> someListWildcardV;
        
        PojoWithCollection fill()
        {
            someCollectionObjectV = newList(new Object[]{
                    "foo",
                    1,
                    1.1f,
                    100.001d,
                    System.currentTimeMillis(), 
                    new Date(),
                    Size.LARGE, 
                    new Pojo().fill(),
                    new BassGuitar(4, true)
            });
            
            Collection<Object> collectionWildcardV = newList(new Object[]{
                    "bar",
                    Size.SMALL, 
                    new AcousticGuitar(GuitarPickup.MICROPHONE)
            });
            
            someCollectionWildcardV = collectionWildcardV;
            
            someListObjectV = newList(new Object[]{
                    "baz",
                    2,
                    2.2f,
                    200.002d,
                    Size.MEDIUM, 
                    new BassGuitar(6, true)
            });
            
            List<Object> listWildcardV = newList(new Object[]{
                    "gg",
                    3,
                    30.03f,
                    300.003d,
                    300000l,
                    new BassGuitar(5, false)
            });
            
            someListWildcardV = listWildcardV;
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((someCollectionObjectV == null)?0:someCollectionObjectV.hashCode());
            result = prime * result + ((someCollectionWildcardV == null)?0:someCollectionWildcardV.hashCode());
            result = prime * result + ((someListObjectV == null)?0:someListObjectV.hashCode());
            result = prime * result + ((someListWildcardV == null)?0:someListWildcardV.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoWithCollection other = (PojoWithCollection)obj;
            if (someCollectionObjectV == null)
            {
                if (other.someCollectionObjectV != null)
                    return false;
            }
            else if (!someCollectionObjectV.equals(other.someCollectionObjectV))
                return false;
            if (someCollectionWildcardV == null)
            {
                if (other.someCollectionWildcardV != null)
                    return false;
            }
            else if (!someCollectionWildcardV.equals(other.someCollectionWildcardV))
                return false;
            if (someListObjectV == null)
            {
                if (other.someListObjectV != null)
                    return false;
            }
            else if (!someListObjectV.equals(other.someListObjectV))
                return false;
            if (someListWildcardV == null)
            {
                if (other.someListWildcardV != null)
                    return false;
            }
            else if (!someListWildcardV.equals(other.someListWildcardV))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PojoWithCollection [someCollectionObjectV=" + someCollectionObjectV + ", someCollectionWildcardV=" + someCollectionWildcardV
                    + ", someListObjectV=" + someListObjectV + ", someListWildcardV=" + someListWildcardV + "]";
        }
        
    }
    
    public static class PojoWithMap
    {
        Map<String,Object> someMapStringKObjectV;
        Map<Guitar,?> someMapGuitarKWildcardV;
        
        Map<Object,Integer> someMapObjectKIntegerV;
        Map<?,Size> someMapWildcardKEnumV;
        
        Map<Object,Object> someMapObjectKObjectV;
        Map<?,?> someMapWildcardKWildcardV;
        
        PojoWithMap fill()
        {
            someMapStringKObjectV = newMap();
            someMapStringKObjectV.put("1", new Pojo().fill());
            someMapStringKObjectV.put(null, Size.SMALL);
            someMapStringKObjectV.put("3", 3);
            
            Map<Guitar,Object> mapGuitarKWildcardV = newMap();
            mapGuitarKWildcardV.put(new BassGuitar(4, false), "passive 4 string");
            mapGuitarKWildcardV.put(null, 5);
            mapGuitarKWildcardV.put(new AcousticGuitar(GuitarPickup.CONTACT), new Date());
            
            someMapGuitarKWildcardV = mapGuitarKWildcardV;
            
            someMapObjectKIntegerV = newMap();
            someMapObjectKIntegerV.put(1, 1);
            someMapObjectKIntegerV.put(null, 2);
            someMapObjectKIntegerV.put("3", null);
            
            Map<Object,Size> mapWildcardKEnumV = newMap();
            mapWildcardKEnumV.put("1", Size.SMALL);
            mapWildcardKEnumV.put(null, Size.MEDIUM);
            mapWildcardKEnumV.put("3", null);
            
            someMapWildcardKEnumV = mapWildcardKEnumV;
            
            someMapObjectKObjectV = newMap();
            someMapObjectKObjectV.put("foo", "bar");
            someMapObjectKObjectV.put(1, Size.SMALL);
            someMapObjectKObjectV.put(null, null);
            
            Map<Object,Object> mapWildcardKWildcardV = newMap();
            mapWildcardKWildcardV.put(null, null);
            mapWildcardKWildcardV.put("foo", 1);
            mapWildcardKWildcardV.put(Size.LARGE, null);
            
            someMapWildcardKWildcardV = mapWildcardKWildcardV;
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((someMapGuitarKWildcardV == null)?0:someMapGuitarKWildcardV.hashCode());
            result = prime * result + ((someMapObjectKIntegerV == null)?0:someMapObjectKIntegerV.hashCode());
            result = prime * result + ((someMapObjectKObjectV == null)?0:someMapObjectKObjectV.hashCode());
            result = prime * result + ((someMapStringKObjectV == null)?0:someMapStringKObjectV.hashCode());
            result = prime * result + ((someMapWildcardKEnumV == null)?0:someMapWildcardKEnumV.hashCode());
            result = prime * result + ((someMapWildcardKWildcardV == null)?0:someMapWildcardKWildcardV.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoWithMap other = (PojoWithMap)obj;
            if (someMapGuitarKWildcardV == null)
            {
                if (other.someMapGuitarKWildcardV != null)
                    return false;
            }
            else if (!someMapGuitarKWildcardV.equals(other.someMapGuitarKWildcardV))
                return false;
            if (someMapObjectKIntegerV == null)
            {
                if (other.someMapObjectKIntegerV != null)
                    return false;
            }
            else if (!someMapObjectKIntegerV.equals(other.someMapObjectKIntegerV))
                return false;
            if (someMapObjectKObjectV == null)
            {
                if (other.someMapObjectKObjectV != null)
                    return false;
            }
            else if (!someMapObjectKObjectV.equals(other.someMapObjectKObjectV))
                return false;
            if (someMapStringKObjectV == null)
            {
                if (other.someMapStringKObjectV != null)
                    return false;
            }
            else if (!someMapStringKObjectV.equals(other.someMapStringKObjectV))
                return false;
            if (someMapWildcardKEnumV == null)
            {
                if (other.someMapWildcardKEnumV != null)
                    return false;
            }
            else if (!someMapWildcardKEnumV.equals(other.someMapWildcardKEnumV))
                return false;
            if (someMapWildcardKWildcardV == null)
            {
                if (other.someMapWildcardKWildcardV != null)
                    return false;
            }
            else if (!someMapWildcardKWildcardV.equals(other.someMapWildcardKWildcardV))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PojoWithMap [someMapGuitarKWildcardV=" + someMapGuitarKWildcardV + ", someMapObjectKIntegerV=" + someMapObjectKIntegerV
                    + ", someMapObjectKObjectV=" + someMapObjectKObjectV + ", someMapStringKObjectV=" + someMapStringKObjectV + ", someMapWildcardKEnumV="
                    + someMapWildcardKEnumV + ", someMapWildcardKWildcardV=" + someMapWildcardKWildcardV + "]";
        }
        
    }
    
    static boolean isEquals(byte[][] b1, byte[][] b2)
    {
        if(b1.length != b2.length)
            return false;
        
        for(int i=0; i<b1.length; i++)
        {
            if(!Arrays.equals(b1[i], b2[i]))
                return false;
        }
        return true;
    }
    
    static boolean isEquals(int[][] b1, int[][] b2)
    {
        if(b1.length != b2.length)
            return false;
        
        for(int i=0; i<b1.length; i++)
        {
            if(!Arrays.equals(b1[i], b2[i]))
                return false;
        }
        return true;
    }
    
    static boolean isEquals(Object[][] b1, Object[][] b2)
    {
        if(b1.length != b2.length)
            return false;
        
        for(int i=0; i<b1.length; i++)
        {
            if(!Arrays.equals(b1[i], b2[i]))
                return false;
        }
        return true;
    }
    
    public void testPojo() throws Exception
    {
        Schema<Pojo> schema = RuntimeSchema.getSchema(Pojo.class);
        Pipe.Schema<Pojo> pipeSchema = 
            ((MappedSchema<Pojo>)schema).pipeSchema;
        
        Pojo p = new Pojo().fill();

        byte[] data = toByteArray(p, schema);
        
        Pojo pFromByteArray = new Pojo();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        Pojo pFromStream = new Pojo();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoWithArray() throws Exception
    {
        Schema<PojoWithArray> schema = RuntimeSchema.getSchema(PojoWithArray.class);
        Pipe.Schema<PojoWithArray> pipeSchema = 
            ((MappedSchema<PojoWithArray>)schema).pipeSchema;
        
        PojoWithArray p = new PojoWithArray().fill();

        byte[] data = toByteArray(p, schema);
        
        PojoWithArray pFromByteArray = new PojoWithArray();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        PojoWithArray pFromStream = new PojoWithArray();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoWithArray2D() throws Exception
    {
        Schema<PojoWithArray2D> schema = RuntimeSchema.getSchema(PojoWithArray2D.class);
        Pipe.Schema<PojoWithArray2D> pipeSchema = 
            ((MappedSchema<PojoWithArray2D>)schema).pipeSchema;
        
        PojoWithArray2D p = new PojoWithArray2D().fill();

        byte[] data = toByteArray(p, schema);
        
        PojoWithArray2D pFromByteArray = new PojoWithArray2D();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        PojoWithArray2D pFromStream = new PojoWithArray2D();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoWithCollection() throws Exception
    {
        Schema<PojoWithCollection> schema = RuntimeSchema.getSchema(PojoWithCollection.class);
        Pipe.Schema<PojoWithCollection> pipeSchema = 
            ((MappedSchema<PojoWithCollection>)schema).pipeSchema;
        
        PojoWithCollection p = new PojoWithCollection().fill();

        byte[] data = toByteArray(p, schema);
        
        PojoWithCollection pFromByteArray = new PojoWithCollection();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        PojoWithCollection pFromStream = new PojoWithCollection();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public void testPojoWithMap() throws Exception
    {
        Schema<PojoWithMap> schema = RuntimeSchema.getSchema(PojoWithMap.class);
        Pipe.Schema<PojoWithMap> pipeSchema = 
            ((MappedSchema<PojoWithMap>)schema).pipeSchema;
        
        PojoWithMap p = new PojoWithMap().fill();

        byte[] data = toByteArray(p, schema);
        
        PojoWithMap pFromByteArray = new PojoWithMap();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        PojoWithMap pFromStream = new PojoWithMap();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public static final class WrapsBat
    {
        Bat bat;
        Object bat2;
        
        WrapsBat fill()
        {
            bat = new Bat(500);
            bat2 = new Bat(1000);
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((bat == null) ? 0 : bat.hashCode());
            result = prime * result + ((bat2 == null) ? 0 : bat2.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            WrapsBat other = (WrapsBat)obj;
            if (bat == null)
            {
                if (other.bat != null)
                    return false;
            }
            else if (!bat.equals(other.bat))
                return false;
            if (bat2 == null)
            {
                if (other.bat2 != null)
                    return false;
            }
            else if (!bat2.equals(other.bat2))
                return false;
            return true;
        }
        
        
    }
    
    static final class Bat implements Message<Bat>
    {
        
        int id;
        
        public Bat()
        {
            
        }
        
        public Bat(int id)
        {
            this.id = id;
        }
        
        
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Bat other = (Bat)obj;
            if (id != other.id)
                return false;
            return true;
        }



        static final Schema<Bat> SCHEMA = new Schema<Bat>()
        {

            public String getFieldName(int number)
            {
                return number == 1 ? "i" : null;
            }

            public int getFieldNumber(String name)
            {
                return name.length() == 1 && name.charAt(0) == 'i' ? 1 : 0;
            }

            public boolean isInitialized(Bat message)
            {
                return true;
            }

            public Bat newMessage()
            {
                return new Bat();
            }

            public String messageName()
            {
                return Bat.class.getSimpleName();
            }

            public String messageFullName()
            {
                return Bat.class.getName();
            }

            public Class<? super Bat> typeClass()
            {
                return Bat.class;
            }

            public void mergeFrom(Input input, Bat message) throws IOException
            {
                for(int number = input.readFieldNumber(this);;number = input.readFieldNumber(this))
                {
                    switch(number)
                    {
                        case 0:
                            return;
                        case 1:
                            message.id = input.readUInt32();
                            break;
                        default:
                            input.handleUnknownField(number, this);
                    }
                }
            }

            public void writeTo(Output output, Bat message) throws IOException
            {
                output.writeUInt32(1, message.id, false);
            }
            
        };
        
        static final Pipe.Schema<Bat> PIPE_SCHEMA = new Pipe.Schema<Bat>(SCHEMA)
        {

            @Override
            protected void transfer(Pipe pipe, Input input, Output output) throws IOException
            {
                for(int number = input.readFieldNumber(wrappedSchema);; number = input.readFieldNumber(wrappedSchema))
                {
                    switch(number)
                    {
                        case 0:
                            return;
                        case 1:
                            output.writeUInt32(number, input.readUInt32(), false);
                            break;
                        default:
                            input.handleUnknownField(number, this);
                    }
                }
            }
        };

        public Schema<Bat> cachedSchema()
        {
            return SCHEMA;
        }
        
        public static Schema<Bat> getSchema()
        {
            return SCHEMA;
        }
        
        public static Pipe.Schema<Bat> getPipeSchema()
        {
            return PIPE_SCHEMA;
        }
    }
    
    public void testBat() throws Exception
    {
        Schema<WrapsBat> schema = RuntimeSchema.getSchema(WrapsBat.class);
        Pipe.Schema<WrapsBat> pipeSchema = 
            ((MappedSchema<WrapsBat>)schema).pipeSchema;
        
        WrapsBat p = new WrapsBat().fill();

        byte[] data = toByteArray(p, schema);
        
        WrapsBat pFromByteArray = new WrapsBat();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        WrapsBat pFromStream = new WrapsBat();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }
    
    public static class PojoWithCustomArrayListAndHashMap
    {
        CustomArrayList<Size> one;
        List<Size> two;
        Object three;
        
        CustomHashMap<Size,Integer> map1;
        Map<Long,Size> map2;
        Object map3;
        
        Size size;
        HasType hasType;
        Object osize;
        Serializable s;
        
        PojoWithCustomArrayListAndHashMap fill()
        {
            
            one = new CustomArrayList<Size>();
            one.add(Size.SMALL);
            
            two = new CustomArrayList<Size>();
            two.add(Size.MEDIUM);
            two.add(Size.LARGE);
            
            CustomArrayList<String> three = new CustomArrayList<String>();
            three.add("1");
            three.add("2");
            three.add("3");
            
            map1 = new CustomHashMap<Size, Integer>();
            map1.put(Size.LARGE, 1);
            
            map2 = new CustomHashMap<Long,Size>();
            map2.put(100l, Size.MEDIUM);
            
            CustomHashMap<Size, Date> map3 = new CustomHashMap<Size, Date>();
            map3.put(Size.SMALL, new Date(System.currentTimeMillis()));
            
            this.map3 = map3;
            
            size = Size.SMALL;
            hasType = Size.MEDIUM;
            osize = Size.LARGE;
            
            s = GuitarPickup.CONTACT;
            
            return this;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((hasType == null) ? 0 : hasType.hashCode());
            result = prime * result + ((map1 == null) ? 0 : map1.hashCode());
            result = prime * result + ((map2 == null) ? 0 : map2.hashCode());
            result = prime * result + ((map3 == null) ? 0 : map3.hashCode());
            result = prime * result + ((one == null) ? 0 : one.hashCode());
            result = prime * result + ((osize == null) ? 0 : osize.hashCode());
            result = prime * result + ((s == null) ? 0 : s.hashCode());
            result = prime * result + ((size == null) ? 0 : size.hashCode());
            result = prime * result + ((three == null) ? 0 : three.hashCode());
            result = prime * result + ((two == null) ? 0 : two.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PojoWithCustomArrayListAndHashMap other = (PojoWithCustomArrayListAndHashMap)obj;
            if (hasType == null)
            {
                if (other.hasType != null)
                    return false;
            }
            else if (!hasType.equals(other.hasType))
                return false;
            if (map1 == null)
            {
                if (other.map1 != null)
                    return false;
            }
            else if (!map1.equals(other.map1))
                return false;
            if (map2 == null)
            {
                if (other.map2 != null)
                    return false;
            }
            else if (!map2.equals(other.map2))
                return false;
            if (map3 == null)
            {
                if (other.map3 != null)
                    return false;
            }
            else if (!map3.equals(other.map3))
                return false;
            if (one == null)
            {
                if (other.one != null)
                    return false;
            }
            else if (!one.equals(other.one))
                return false;
            if (osize == null)
            {
                if (other.osize != null)
                    return false;
            }
            else if (!osize.equals(other.osize))
                return false;
            if (s == null)
            {
                if (other.s != null)
                    return false;
            }
            else if (!s.equals(other.s))
                return false;
            if (size != other.size)
                return false;
            if (three == null)
            {
                if (other.three != null)
                    return false;
            }
            else if (!three.equals(other.three))
                return false;
            if (two == null)
            {
                if (other.two != null)
                    return false;
            }
            else if (!two.equals(other.two))
                return false;
            return true;
        }

        
    }
    
    public void testPojoWithCustomArrayListAndHashMapAndHashMap() throws Exception
    {
        Schema<PojoWithCustomArrayListAndHashMap> schema = RuntimeSchema.getSchema(PojoWithCustomArrayListAndHashMap.class);
        Pipe.Schema<PojoWithCustomArrayListAndHashMap> pipeSchema = 
            ((MappedSchema<PojoWithCustomArrayListAndHashMap>)schema).pipeSchema;
        
        PojoWithCustomArrayListAndHashMap p = new PojoWithCustomArrayListAndHashMap().fill();

        byte[] data = toByteArray(p, schema);
        
        PojoWithCustomArrayListAndHashMap pFromByteArray = new PojoWithCustomArrayListAndHashMap();
        mergeFrom(data, 0, data.length, pFromByteArray, schema);
        assertEquals(p, pFromByteArray);
        
        PojoWithCustomArrayListAndHashMap pFromStream = new PojoWithCustomArrayListAndHashMap();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        mergeFrom(in, pFromStream, schema);
        assertEquals(p, pFromByteArray);
        
        roundTrip(p, schema, pipeSchema);
    }

}
