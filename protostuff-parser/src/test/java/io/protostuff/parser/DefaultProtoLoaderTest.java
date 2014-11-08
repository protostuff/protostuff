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

package io.protostuff.parser;

import java.io.File;

import junit.framework.TestCase;

/**
 * Test for the default proto loader.
 * 
 * @author David Yu
 */
public class DefaultProtoLoaderTest extends TestCase
{

    public void testPackageBaseDir() throws Exception
    {
        File f = ProtoParserTest.getFile(
                "io/protostuff/parser/test_default_proto_loader.proto");
        assertTrue(f.exists());
        Proto p = ProtoUtil.parseProto(f);
        assertEquals("io.protostuff.parser", p.getPackageName());
    }

    public void testLoadProtoFromClasspath() throws Exception
    {
        Proto proto = DefaultProtoLoader.loadFromClasspath(
                "google/protobuf/unittest_import.proto", null);
        assertNotNull(proto);
        assertEquals("protobuf_unittest_import", proto.getPackageName());
    }

    public void testImportFromClasspath() throws Exception
    {
        File f = new File("src/main/etc/test_default_proto_loader.proto");
        assertTrue(f.exists());
        Proto p = ProtoUtil.parseProto(f);
        assertEquals("io.protostuff.parser", p.getPackageName());

        Message testMessage = p.getMessage("TestMessage");
        assertNotNull(testMessage);

        Field<?> f1 = testMessage.getField("imported_message1");
        Field<?> f2 = testMessage.getField("imported_message2");

        assertNotNull(f1);
        assertNotNull(f2);

        assertTrue(f1 instanceof MessageField);
        assertTrue(f2 instanceof MessageField);

        Message importedMessage1 = ((MessageField) f1).getMessage();
        Message importedMessage2 = ((MessageField) f2).getMessage();

        assertNotNull(importedMessage1);
        assertNotNull(importedMessage2);

        assertTrue(importedMessage1 == importedMessage2);
    }

}
