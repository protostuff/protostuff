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
 * Test for deep references in the message's fields
 *
 * @author David Yu
 * @created Jun 20, 2010
 */
public class DeepReferenceTest extends TestCase
{
    
    public void testIt() throws Exception
    {
        File f = ProtoParserTest.getFile("test_deep_reference.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        
        assertTrue(proto.getImportedProtos().size() == 2);
        
        Proto importedProto = proto.getImportedProto(ProtoParserTest.getFile("test_imported_inner.proto"));
        assertNotNull(importedProto);
        
        Proto jpImportedProto = proto.getImportedProto(ProtoParserTest.getFile("test_java_package_imported_inner.proto"));
        assertNotNull(jpImportedProto);
        
        Message request = proto.getMessage("Request");
        assertNotNull(request);
        
        Message requestInner = request.getNestedMessage("Inner");
        assertNotNull(requestInner);
        
        Message requestDeeper = requestInner.getNestedMessage("Deeper");
        assertNotNull(requestDeeper);
        
        Message response = proto.getMessage("Response");
        assertNotNull(response);
        
        Message responseInner = response.getNestedMessage("Inner");
        assertNotNull(responseInner);
        
        Message responseDeeper = responseInner.getNestedMessage("Deeper");
        assertNotNull(responseDeeper);
        
        Message foo = importedProto.getMessage("Foo");
        assertNotNull(foo);
        
        Message fooInner = foo.getNestedMessage("Inner");
        assertNotNull(fooInner);
        
        Message fooDeeper = fooInner.getNestedMessage("Deeper");
        assertNotNull(fooDeeper);
        
        Message bar = importedProto.getMessage("Bar");
        assertNotNull(bar);
        
        Message barInner = bar.getNestedMessage("Inner");
        assertNotNull(barInner);
        
        Message barDeeper = barInner.getNestedMessage("Deeper");
        assertNotNull(barDeeper);
        
        Message jpFoo = jpImportedProto.getMessage("JPFoo");
        assertNotNull(jpFoo);
        
        Message jpFooInner = jpFoo.getNestedMessage("Inner");
        assertNotNull(jpFooInner);
        
        Message jpFooDeeper = jpFooInner.getNestedMessage("Deeper");
        assertNotNull(jpFooDeeper);
        
        Message jpBar = jpImportedProto.getMessage("JPBar");
        assertNotNull(jpBar);
        
        Message jpBarInner = jpBar.getNestedMessage("Inner");
        assertNotNull(jpBarInner);
        
        Message jpBarDeeper = jpBarInner.getNestedMessage("Deeper");
        assertNotNull(jpBarDeeper);
        
        
        assertTrue(getMessageField("foo1", request) == foo);
        assertTrue(getMessageField("foo2", request) == foo);
        assertTrue(getMessageField("foo3", request) == jpFoo);
        
        assertTrue(getMessageField("inner1", request) == fooInner);
        assertTrue(getMessageField("inner2", request) == fooInner);
        assertTrue(getMessageField("inner3", request) == jpFooInner);
        
        assertTrue(getMessageField("deeper1", request) == fooDeeper);
        assertTrue(getMessageField("deeper2", request) == fooDeeper);
        assertTrue(getMessageField("deeper3", request) == jpFooDeeper);
        
        assertTrue(getMessageField("foo1", requestInner) == foo);
        assertTrue(getMessageField("foo2", requestInner) == foo);
        assertTrue(getMessageField("foo3", requestInner) == jpFoo);
        
        assertTrue(getMessageField("inner1", requestInner) == fooInner);
        assertTrue(getMessageField("inner2", requestInner) == fooInner);
        assertTrue(getMessageField("inner3", requestInner) == jpFooInner);
        
        assertTrue(getMessageField("deeper1", requestInner) == fooDeeper);
        assertTrue(getMessageField("deeper2", requestInner) == fooDeeper);
        assertTrue(getMessageField("deeper3", requestInner) == jpFooDeeper);
        
        assertTrue(getMessageField("foo1", requestDeeper) == foo);
        assertTrue(getMessageField("foo2", requestDeeper) == foo);
        assertTrue(getMessageField("foo3", requestDeeper) == jpFoo);
        
        assertTrue(getMessageField("inner1", requestDeeper) == fooInner);
        assertTrue(getMessageField("inner2", requestDeeper) == fooInner);
        assertTrue(getMessageField("inner3", requestDeeper) == jpFooInner);
        
        assertTrue(getMessageField("deeper1", requestDeeper) == fooDeeper);
        assertTrue(getMessageField("deeper2", requestDeeper) == fooDeeper);
        assertTrue(getMessageField("deeper3", requestDeeper) == jpFooDeeper);
        
        
        assertTrue(getMessageField("bar1", response) == bar);
        assertTrue(getMessageField("bar2", response) == bar);
        assertTrue(getMessageField("bar3", response) == jpBar);
        
        assertTrue(getMessageField("inner1", response) == barInner);
        assertTrue(getMessageField("inner2", response) == barInner);
        assertTrue(getMessageField("inner3", response) == jpBarInner);
        
        assertTrue(getMessageField("deeper1", response) == barDeeper);
        assertTrue(getMessageField("deeper2", response) == barDeeper);
        assertTrue(getMessageField("deeper3", response) == jpBarDeeper);
        
        assertTrue(getMessageField("bar1", responseInner) == bar);
        assertTrue(getMessageField("bar2", responseInner) == bar);
        assertTrue(getMessageField("bar3", responseInner) == jpBar);
        
        assertTrue(getMessageField("inner1", responseInner) == barInner);
        assertTrue(getMessageField("inner2", responseInner) == barInner);
        assertTrue(getMessageField("inner3", responseInner) == jpBarInner);
        
        assertTrue(getMessageField("deeper1", responseInner) == barDeeper);
        assertTrue(getMessageField("deeper2", responseInner) == barDeeper);
        assertTrue(getMessageField("deeper3", responseInner) == jpBarDeeper);
        
        assertTrue(getMessageField("bar1", responseDeeper) == bar);
        assertTrue(getMessageField("bar2", responseDeeper) == bar);
        assertTrue(getMessageField("bar3", responseDeeper) == jpBar);
        
        assertTrue(getMessageField("inner1", responseDeeper) == barInner);
        assertTrue(getMessageField("inner2", responseDeeper) == barInner);
        assertTrue(getMessageField("inner3", responseDeeper) == jpBarInner);
        
        assertTrue(getMessageField("deeper1", responseDeeper) == barDeeper);
        assertTrue(getMessageField("deeper2", responseDeeper) == barDeeper);
        assertTrue(getMessageField("deeper3", responseDeeper) == jpBarDeeper);
    }
    
    static Message getMessageField(String name, Message msg)
    {
        return ((MessageField)msg.getField(name)).getMessage();
    }

}
