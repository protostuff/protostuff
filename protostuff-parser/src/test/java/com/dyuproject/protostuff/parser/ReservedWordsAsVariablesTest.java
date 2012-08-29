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

package com.dyuproject.protostuff.parser;

import java.io.File;

import junit.framework.TestCase;

/**
 * Test for parser to accept variables that are reserved words.
 *
 * @author David Yu
 * @created Jul 4, 2011
 */
public class ReservedWordsAsVariablesTest extends TestCase
{
    
    public void testIt() throws Exception
    {
        File f = ProtoParserTest.getFile("com/dyuproject/protostuff/parser/test_reserved_words_as_variables.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        
        Message test = proto.getMessage("Test");
        assertNotNull(test);
        
        assertTrue(test.getFields().size() == 33);
        
        assertTrue(test.getField("optional").number == 1);
        assertTrue(test.getField("repeated").number == 2);
        assertTrue(test.getField("required").number == 3);
        assertTrue(test.getField("to").number == 4);
        assertTrue(test.getField("package").number == 5);
        assertTrue(test.getField("syntax").number == 6);
        assertTrue(test.getField("import").number == 7);
        assertTrue(test.getField("options").number == 8);
        assertTrue(test.getField("message").number == 9);
        assertTrue(test.getField("service").number == 10);
        assertTrue(test.getField("enum").number == 11);
        assertTrue(test.getField("extensions").number == 12);
        assertTrue(test.getField("extend").number == 13);
        assertTrue(test.getField("group").number == 14);
        assertTrue(test.getField("rpc").number == 15);
        assertTrue(test.getField("int32").number == 16);
        assertTrue(test.getField("int64").number == 17);
        assertTrue(test.getField("uint32").number == 18);
        assertTrue(test.getField("uint64").number == 19);
        assertTrue(test.getField("sint32").number == 20);
        assertTrue(test.getField("sint64").number == 21);
        assertTrue(test.getField("fixed32").number == 22);
        assertTrue(test.getField("fixed64").number == 23);
        assertTrue(test.getField("sfixed32").number == 24);
        assertTrue(test.getField("sfixed64").number == 25);
        assertTrue(test.getField("float").number == 26);
        assertTrue(test.getField("double").number == 27);
        assertTrue(test.getField("bool").number == 28);
        assertTrue(test.getField("string").number == 29);
        assertTrue(test.getField("bytes").number == 30);
        assertTrue(test.getField("default").number == 31);
        assertTrue(test.getField("max").number == 32);
        assertTrue(test.getField("option").number == 33);
    }

}
