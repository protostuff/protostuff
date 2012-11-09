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

package com.dyuproject.protostuff.runtime;

import com.dyuproject.protostuff.AbstractTest;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

/**
 * Tests for {@link RuntimeView}.
 * 
 * @author David Yu
 * @created Nov 9, 2012
 */
public class RuntimeViewTest extends AbstractTest
{
    
    //                                id    name    timestamp
    static final Baz BAZ = newBaz(    128,  "baz",  0);
    static final int EXPECT_BAZ_LEN = 1+2 + 1+1+3 + 1+1;
    static final int ID_LEN =         3;
    static final int NAME_LEN =             5;
    static final int TIMESTAMP_LEN =                2;
    
    static final int WITHOUT_ID_LEN = EXPECT_BAZ_LEN - ID_LEN;
    static final int WITHOUT_NAME_LEN = EXPECT_BAZ_LEN - NAME_LEN;
    static final int WITHOUT_TIMESTAMP_LEN = EXPECT_BAZ_LEN - TIMESTAMP_LEN;
    
    static final String STR_FN_ID = "1";
    static final String STR_FN_NAME = "2";
    static final String STR_FN_TIMESTAMP = "3";
    
    static Baz newBaz(int id, String name, long timestamp)
    {
        Baz message = new Baz();
        
        message.setId(id);
        message.setName(name);
        message.setTimestamp(timestamp);
        
        return message;
    }
    
    static byte[] ser(Schema<Baz> schema)
    {
        return ProtostuffIOUtil.toByteArray(BAZ, schema, buf());
    }
    
    static int len(Schema<Baz> schema)
    {
        return ser(schema).length;
    }
    
    static RuntimeSchema<Baz> rs()
    {
        return (RuntimeSchema<Baz>)RuntimeSchema.getSchema(Baz.class);
    }
    
    static Schema<Baz> ex1(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.EXCLUDE_VIA_COPY_MAP, 
                null, args);
    }
    
    static Schema<Baz> ex2(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.EXCLUDE_VIA_COPY_BOTH, 
                null, args);
    }
    
    static Schema<Baz> EQ(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.PREDICATE, 
                Predicate.FACTORY.EQ, args);
    }
    
    static Schema<Baz> NOTEQ(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.PREDICATE, 
                Predicate.FACTORY.NOTEQ, args);
    }
    
    static Schema<Baz> GT(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.PREDICATE, 
                Predicate.FACTORY.GT, args);
    }
    
    static Schema<Baz> LT(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.PREDICATE, 
                Predicate.FACTORY.LT, args);
    }
    
    static Schema<Baz> RANGE(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.PREDICATE, 
                Predicate.FACTORY.RANGE, args);
    }
    
    static Schema<Baz> NOTRANGE(String ... args)
    {
        return RuntimeView.createFrom(rs(), 
                RuntimeView.FACTORY.PREDICATE, 
                Predicate.FACTORY.NOTRANGE, args);
    }
    
    // tests
    
    public void testLen()
    {
        assertEquals(EXPECT_BAZ_LEN, len(rs()));
    }
    
    public void testExcludeBazId()
    {
        assertEquals(WITHOUT_ID_LEN, len(ex1("id")));
        assertEquals(WITHOUT_ID_LEN, len(ex2("id")));
        
        assertEquals(WITHOUT_ID_LEN, len(NOTEQ(STR_FN_ID)));
        assertEquals(WITHOUT_ID_LEN, len(GT(STR_FN_ID)));
        assertEquals(WITHOUT_ID_LEN, len(RANGE(STR_FN_NAME, STR_FN_TIMESTAMP)));
    }
    
    public void testExcludeBazName()
    {
        assertEquals(WITHOUT_NAME_LEN, len(ex1("name")));
        assertEquals(WITHOUT_NAME_LEN, len(ex2("name")));
        
        assertEquals(WITHOUT_NAME_LEN, len(NOTEQ(STR_FN_NAME)));
        assertEquals(WITHOUT_NAME_LEN, len(NOTRANGE(STR_FN_NAME, STR_FN_NAME)));
    }
    
    public void testExcludeBazTimestamp()
    {
        assertEquals(WITHOUT_TIMESTAMP_LEN, len(ex1("timestamp")));
        assertEquals(WITHOUT_TIMESTAMP_LEN, len(ex2("timestamp")));
        
        assertEquals(WITHOUT_TIMESTAMP_LEN, len(NOTEQ(STR_FN_TIMESTAMP)));
        assertEquals(WITHOUT_TIMESTAMP_LEN, len(LT(STR_FN_TIMESTAMP)));
        assertEquals(WITHOUT_TIMESTAMP_LEN, len(RANGE(STR_FN_ID, STR_FN_NAME)));
    }
    
    public void testIncludeOnlyBazId()
    {
        assertEquals(ID_LEN, len(ex1("name", "timestamp")));
        assertEquals(ID_LEN, len(ex2("name", "timestamp")));
        
        assertEquals(ID_LEN, len(EQ(STR_FN_ID)));
        assertEquals(ID_LEN, len(LT(STR_FN_NAME)));
        assertEquals(ID_LEN, len(RANGE(STR_FN_ID, STR_FN_ID)));
    }
    
    public void testIncludeOnlyBazName()
    {
        assertEquals(NAME_LEN, len(ex1("id", "timestamp")));
        assertEquals(NAME_LEN, len(ex2("id", "timestamp")));
        
        assertEquals(NAME_LEN, len(EQ(STR_FN_NAME)));
        assertEquals(NAME_LEN, len(RANGE(STR_FN_NAME, STR_FN_NAME)));
    }
    
    public void testIncludeOnlyBazTimestamp()
    {
        assertEquals(TIMESTAMP_LEN, len(ex1("id", "name")));
        assertEquals(TIMESTAMP_LEN, len(ex2("id", "name")));
        
        assertEquals(TIMESTAMP_LEN, len(EQ(STR_FN_TIMESTAMP)));
        assertEquals(TIMESTAMP_LEN, len(GT(STR_FN_NAME)));
        assertEquals(TIMESTAMP_LEN, len(RANGE(STR_FN_TIMESTAMP, STR_FN_TIMESTAMP)));
    }

}
