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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

/**
 * Tests for {@link BigDecimal} and {@link BigInteger}.
 *
 * @author David Yu
 * @created Sep 9, 2010
 */
public class MathObjectsTest extends AbstractTest
{
    
    public static class Payment
    {
        
        int id = 0;
        BigDecimal bd;
        BigInteger bi;
        List<BigDecimal> bdList;
        List<BigInteger> biList;
        
        public Payment()
        {
            
        }
        
        /**
         * @return the id
         */
        public int getId()
        {
            return id;
        }
        /**
         * @param id the id to set
         */
        public void setId(int id)
        {
            this.id = id;
        }
        /**
         * @return the bd
         */
        public BigDecimal getBd()
        {
            return bd;
        }
        /**
         * @param bd the bd to set
         */
        public void setBd(BigDecimal bd)
        {
            this.bd = bd;
        }

        /**
         * @return the bi
         */
        public BigInteger getBi()
        {
            return bi;
        }

        /**
         * @param bi the bi to set
         */
        public void setBi(BigInteger bi)
        {
            this.bi = bi;
        }
        
        /**
         * @return the bdList
         */
        public List<BigDecimal> getBdList()
        {
            return bdList;
        }

        /**
         * @param bdList the bdList to set
         */
        public void setBdList(List<BigDecimal> bdList)
        {
            this.bdList = bdList;
        }

        /**
         * @return the biList
         */
        public List<BigInteger> getBiList()
        {
            return biList;
        }

        /**
         * @param biList the biList to set
         */
        public void setBiList(List<BigInteger> biList)
        {
            this.biList = biList;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((bd == null)?0:bd.hashCode());
            result = prime * result + ((bdList == null)?0:bdList.hashCode());
            result = prime * result + ((bi == null)?0:bi.hashCode());
            result = prime * result + ((biList == null)?0:biList.hashCode());
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
            Payment other = (Payment)obj;
            if (bd == null)
            {
                if (other.bd != null)
                    return false;
            }
            else if (!bd.equals(other.bd))
                return false;
            if (bdList == null)
            {
                if (other.bdList != null)
                    return false;
            }
            else if (!bdList.equals(other.bdList))
                return false;
            if (bi == null)
            {
                if (other.bi != null)
                    return false;
            }
            else if (!bi.equals(other.bi))
                return false;
            if (biList == null)
            {
                if (other.biList != null)
                    return false;
            }
            else if (!biList.equals(other.biList))
                return false;
            if (id != other.id)
                return false;
            return true;
        }

        
        
    }
    
    static Payment filledPayment()
    {
        Payment p = new Payment();
        ArrayList<BigInteger> biList = new ArrayList<BigInteger>();
        ArrayList<BigDecimal> bdList = new ArrayList<BigDecimal>();
        p.setId(1);
        p.setBd(new BigDecimal("123456789.987654321"));
        p.setBi(BigInteger.valueOf(System.currentTimeMillis()));
        
        p.setBdList(bdList);
        p.setBiList(biList);
        
        biList.add(BigInteger.valueOf(123456789));
        biList.add(BigInteger.valueOf(987654321));
        
        bdList.add(new BigDecimal("123456789"));
        bdList.add(new BigDecimal("987654321"));
        
        return p;
    }
    
    public void testProtobuf() throws Exception
    {
        Schema<Payment> schema = RuntimeSchema.getSchema(Payment.class);
        Payment p = filledPayment();

        byte[] data = ProtobufIOUtil.toByteArray(p, schema, buf());
        
        Payment p2 = new Payment();
        ProtostuffIOUtil.mergeFrom(data, p2, schema);
        /*System.err.println(p2.getId());
        System.err.println(p2.getBd());
        System.err.println(p2.getBi());
        System.err.println(p2.getBdList());
        System.err.println(p2.getBiList());*/
        
        assertEquals(p, p2);
        
        List<Payment> list = new ArrayList<Payment>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtobufIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<Payment> parsedList = ProtobufIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }
    
    public void testProtostuff() throws Exception
    {
        Schema<Payment> schema = RuntimeSchema.getSchema(Payment.class);
        Payment p = filledPayment();

        byte[] data = ProtostuffIOUtil.toByteArray(p, schema, buf());
        
        Payment p2 = new Payment();
        ProtostuffIOUtil.mergeFrom(data, 0, data.length, p2, schema);
        /*System.err.println(p2.getId());
        System.err.println(p2.getBd());
        System.err.println(p2.getBi());
        System.err.println(p2.getBdList());
        System.err.println(p2.getBiList());*/
        
        assertEquals(p, p2);
        
        List<Payment> list = new ArrayList<Payment>();
        list.add(p);
        list.add(p2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeListTo(out, list, schema, buf());
        byte[] listData = out.toByteArray();
        
        ByteArrayInputStream in = new ByteArrayInputStream(listData);
        List<Payment> parsedList = ProtostuffIOUtil.parseListFrom(in, schema);
        
        assertEquals(list, parsedList);
    }

}
