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

import com.dyuproject.protostuff.parser.Service.RpcMethod;

import junit.framework.TestCase;

/**
 * Tests for parsing service/rpc components in the .proto file.
 *
 * @author David Yu
 * @created Jun 19, 2010
 */
public class ProtoServiceTest extends TestCase
{
    
    public void testRpc() throws Exception
    {
        File f = ProtoParserTest.getFile("test_rpc.proto");
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
        
        Service service = proto.getService("SimpleRpc");
        assertNotNull(service);
        
        RpcMethod local = service.getRpcMethod("Local");
        assertNotNull(local);
        assertTrue(local.getArgType() == request);
        assertTrue(local.getReturnType() == response);
        
        RpcMethod localFull = service.getRpcMethod("LocalFull");
        assertNotNull(localFull);
        assertTrue(localFull.getArgType() == request);
        assertTrue(localFull.getReturnType() == response);
        
        RpcMethod localInner = service.getRpcMethod("LocalInner");
        assertNotNull(localInner);
        assertTrue(localInner.getArgType() == requestInner);
        assertTrue(localInner.getReturnType() == responseInner);
        
        RpcMethod localInnerFull = service.getRpcMethod("LocalInnerFull");
        assertNotNull(localInnerFull);
        assertTrue(localInnerFull.getArgType() == requestInner);
        assertTrue(localInnerFull.getReturnType() == responseInner);
        
        RpcMethod localDeeper = service.getRpcMethod("LocalDeeper");
        assertNotNull(localDeeper);
        assertTrue(localDeeper.getArgType() == requestDeeper);
        assertTrue(localDeeper.getReturnType() == responseDeeper);
        
        RpcMethod localDeeperFull = service.getRpcMethod("LocalDeeperFull");
        assertNotNull(localDeeperFull);
        assertTrue(localDeeperFull.getArgType() == requestDeeper);
        assertTrue(localDeeperFull.getReturnType() == responseDeeper);
        
        RpcMethod foreign = service.getRpcMethod("Foreign");
        assertNotNull(foreign);
        assertTrue(foreign.getArgType() == foo);
        assertTrue(foreign.getReturnType() == bar);
        
        RpcMethod foreignFull = service.getRpcMethod("ForeignFull");
        assertNotNull(foreignFull);
        assertTrue(foreignFull.getArgType() == foo);
        assertTrue(foreignFull.getReturnType() == bar);
        
        RpcMethod foreignInner = service.getRpcMethod("ForeignInner");
        assertNotNull(foreignInner);
        assertTrue(foreignInner.getArgType() == fooInner);
        assertTrue(foreignInner.getReturnType() == barInner);
        
        RpcMethod foreignInnerFull = service.getRpcMethod("ForeignInnerFull");
        assertNotNull(foreignInnerFull);
        assertTrue(foreignInnerFull.getArgType() == fooInner);
        assertTrue(foreignInnerFull.getReturnType() == barInner);
        
        RpcMethod foreignDeeper = service.getRpcMethod("ForeignDeeper");
        assertNotNull(foreignDeeper);
        assertTrue(foreignDeeper.getArgType() == fooDeeper);
        assertTrue(foreignDeeper.getReturnType() == barDeeper);
        
        RpcMethod foreignDeeperFull = service.getRpcMethod("ForeignDeeperFull");
        assertNotNull(foreignDeeperFull);
        assertTrue(foreignDeeperFull.getArgType() == fooDeeper);
        assertTrue(foreignDeeperFull.getReturnType() == barDeeper);
        
        RpcMethod jpForeign = service.getRpcMethod("JPForeign");
        assertNotNull(jpForeign);
        assertTrue(jpForeign.getArgType() == jpFoo);
        assertTrue(jpForeign.getReturnType() == jpBar);
        
        RpcMethod jpForeignFull = service.getRpcMethod("JPForeignFull");
        assertNotNull(jpForeignFull);
        assertTrue(jpForeignFull.getArgType() == jpFoo);
        assertTrue(jpForeignFull.getReturnType() == jpBar);
        
        RpcMethod jpForeignInner = service.getRpcMethod("JPForeignInner");
        assertNotNull(jpForeignInner);
        assertTrue(jpForeignInner.getArgType() == jpFooInner);
        assertTrue(jpForeignInner.getReturnType() == jpBarInner);
        
        RpcMethod jpForeignInnerFull = service.getRpcMethod("JPForeignInnerFull");
        assertNotNull(jpForeignInnerFull);
        assertTrue(jpForeignInnerFull.getArgType() == jpFooInner);
        assertTrue(jpForeignInnerFull.getReturnType() == jpBarInner);
        
        RpcMethod jpForeignDeeper = service.getRpcMethod("JPForeignDeeper");
        assertNotNull(jpForeignDeeper);
        assertTrue(jpForeignDeeper.getArgType() == jpFooDeeper);
        assertTrue(jpForeignDeeper.getReturnType() == jpBarDeeper);
        
        RpcMethod jpForeignDeeperFull = service.getRpcMethod("JPForeignDeeperFull");
        assertNotNull(jpForeignDeeperFull);
        assertTrue(jpForeignDeeperFull.getArgType() == jpFooDeeper);
        assertTrue(jpForeignDeeperFull.getReturnType() == jpBarDeeper);
    }

}
