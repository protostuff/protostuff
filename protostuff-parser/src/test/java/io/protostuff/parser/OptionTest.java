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

package io.protostuff.parser;

import java.io.File;

import junit.framework.TestCase;

/**
 * Test proto options.
 * 
 * @author David Yu
 * @created Aug 11, 2011
 */
public class OptionTest extends TestCase
{

    public void testIt() throws Exception
    {
        File f = ProtoParserTest.getFile("io/protostuff/parser/test_options.proto");
        assertTrue(f.exists());

        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);

        Message aMessage = proto.getMessage("AMessage");
        assertNotNull(aMessage);
        assertEquals("something",
                aMessage.getField("anotherMessage").getOption("anOption"));
        assertEquals(Boolean.TRUE, aMessage.getExtraOption("message_set_wire_format"));

        Message anotherMessage = proto.getMessage("AnotherMessage");
        assertNotNull(anotherMessage);
        assertEquals("bar", anotherMessage.getExtraOption("foo"));

        EnumGroup baz = proto.getEnumGroup("Baz");
        assertNotNull(baz);
        assertEquals(Float.valueOf(1.0f), baz.getExtraOption("random.enum.option"));

        EnumGroup.Value a = baz.getValue("A");
        assertEquals(1, a.getOptions().get("some_int"));
        assertEquals(Boolean.TRUE, a.getOptions().get("some_bool"));
        assertEquals("foo", a.getOptions().get("some_string"));
        assertEquals("none", a.getOptions().get("default"));
    }

}
