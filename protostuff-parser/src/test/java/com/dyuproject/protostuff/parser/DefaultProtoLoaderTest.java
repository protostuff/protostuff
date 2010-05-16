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

package com.dyuproject.protostuff.parser;

import java.io.File;

import junit.framework.TestCase;

/**
 * Test for the default proto loader.
 *
 * @author David Yu
 * @created May 16, 2010
 */
public class DefaultProtoLoaderTest extends TestCase
{
    
    public void testPackageBaseDir() throws Exception
    {
        File f = ProtoParserTest.getFile(
                "com/dyuproject/protostuff/parser/test_default_proto_loader.proto");
        assertTrue(f.exists());
        Proto p = ProtoUtil.parseProto(f);
        assertEquals("com.dyuproject.protostuff.parser", p.getPackageName());
    }
    
    public void testLoadProtoFromClasspath() throws Exception
    {
        Proto proto = DefaultProtoLoader.loadFromClasspath(
                "google/protobuf/unittest_import.proto", null);
        assertNotNull(proto);
        assertEquals("protobuf_unittest_import", proto.getPackageName());
    }

}
