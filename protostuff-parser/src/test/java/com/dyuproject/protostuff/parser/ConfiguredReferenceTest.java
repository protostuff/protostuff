//================================================================================
//Copyright (c) 2011, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================


package com.dyuproject.protostuff.parser;

import java.io.File;

import junit.framework.TestCase;

/**
 * Test for references configured via options and annotations.
 *
 * @author David Yu
 * @created Dec 22, 2011
 */
public class ConfiguredReferenceTest extends TestCase
{
    
    public void testIt() throws Exception
    {
        File f = ProtoParserTest.getFile("com/dyuproject/protostuff/parser/test_option_annotation_reference.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        
        Message car = proto.getMessage("Car");
        assertNotNull(car);

        EnumGroup condition = car.getNestedEnumGroup("Condition");
        assertNotNull(condition);
        
        Message part = car.getNestedMessage("Part");
        assertNotNull(part);
        
        Message tire = part.getNestedMessage("Tire");
        assertNotNull(tire);
        
        Message person = (Message)proto.findReference("Person", proto.getPackageName());
        assertNotNull(person);
        
        Message listRequest = person.getNestedMessage("ListRequest");
        assertNotNull(listRequest);
        
        EnumGroup importedGender = (EnumGroup)proto.findReference("Person.Gender", proto.getPackageName()); 
        assertNotNull(importedGender);
        
        EnumGroup localGender = proto.getEnumGroup("Gender");
        assertNotNull(localGender);
        
        verifyCarAnnotations(car, part, tire, localGender, importedGender, condition, 
                person, listRequest);
        
        verifyCarOptions(car, part, tire, localGender, importedGender, condition, 
                person, listRequest);
        
        verifyPart(part);
        verifyCondition(condition);
        
        Service carService = proto.getService("CarService");
        assertNotNull(carService);
        
        verifyCarService(carService, car, condition);
    }
    
    static void verifyCarAnnotations(Message car, Message part, Message tire, 
            EnumGroup localGender, EnumGroup importedGender, EnumGroup condition, 
            Message person, Message listRequest)
    {
        Annotation name = car.getAnnotation("Name");
        assertNotNull(name);
        assertEquals(name.getValue("value"), "Car");
        
        Annotation local = car.getAnnotation("Local");
        assertNotNull(local);
        assertTrue(local.getValue("self") == car);
        assertTrue(local.getValue("local_enum") == localGender);
        assertTrue(local.getValue("nested") == part);
        assertTrue(local.getValue("nested_enum") == condition);
        assertTrue(local.getValue("deeper") == tire);
        
        Annotation imported = car.getAnnotation("Imported");
        assertNotNull(imported);
        assertTrue(imported.getValue("imported_message") == person);
        assertTrue(imported.getValue("imported_enum") == importedGender);
        
        Annotation fqcn = car.getAnnotation("FQCN");
        assertNotNull(fqcn);
        assertTrue(fqcn.getValue("fqcn_message") == listRequest);
        assertTrue(fqcn.getValue("fqcn_enum") == importedGender);
        
    }
    
    static void verifyCarOptions(Message car, Message part, Message tire, 
            EnumGroup localGender, EnumGroup importedGender, EnumGroup condition, 
            Message person, Message listRequest)
    {
        assertEquals("Car", car.getOptions().get("name"));
        assertTrue(car.getOptions().get("self") == car);
        assertTrue(car.getOptions().get("local_enum") == localGender);
        assertTrue(car.getOptions().get("nested") == part);
        assertTrue(car.getOptions().get("nested_enum") == condition);
        assertTrue(car.getOptions().get("deeper") == tire);
        assertTrue(car.getOptions().get("imported_message") == person);
        assertTrue(car.getOptions().get("imported_enum") == importedGender);
        assertTrue(car.getOptions().get("fqcn_message") == listRequest);
        assertTrue(car.getOptions().get("fqcn_enum") == importedGender);
        
        Field.Int32 id = (Field.Int32)car.getField("id");
        assertNotNull(id);
        
        assertTrue(id.getOptions().get("self") == car);
        assertTrue(id.getOptions().get("local_enum") == localGender);
        assertTrue(id.getOptions().get("nested") == part);
        assertTrue(id.getOptions().get("nested_enum") == condition);
        assertTrue(id.getOptions().get("deeper") == tire);
        assertTrue(id.getOptions().get("imported_message") == person);
        assertTrue(id.getOptions().get("imported_enum") == importedGender);
        assertTrue(id.getOptions().get("fqcn_message") == listRequest);
        assertTrue(id.getOptions().get("fqcn_enum") == importedGender);
        
        
        EnumGroup.Value male = localGender.getValue("MALE");
        assertNotNull(male);
        
        assertTrue(male.getOptions().get("self") == car);
        assertTrue(male.getOptions().get("local_enum") == localGender);
        assertTrue(male.getOptions().get("nested") == part);
        assertTrue(male.getOptions().get("nested_enum") == condition);
        assertTrue(male.getOptions().get("deeper") == tire);
        assertTrue(male.getOptions().get("imported_message") == person);
        assertTrue(male.getOptions().get("imported_enum") == importedGender);
        assertTrue(male.getOptions().get("fqcn_message") == listRequest);
        assertTrue(male.getOptions().get("fqcn_enum") == importedGender);
    }
    
    static void verifyPart(Message part)
    {
        assertNotNull(part.getParentMessage());
        
        Annotation owner = part.getAnnotation("Owner");
        assertNotNull(owner);
        
        assertTrue(owner.getValue("type") == part.getParentMessage());
        
        assertTrue(part.getOptions().get("owner") == part.getParentMessage());
    }
    
    static void verifyCondition(EnumGroup condition)
    {
        assertNotNull(condition.getParentMessage());
        
        Annotation owner = condition.getAnnotation("Owner");
        assertNotNull(owner);
        
        assertTrue(owner.getValue("type") == condition.getParentMessage());
        
        assertTrue(condition.getOptions().get("owner") == condition.getParentMessage());
    }
    
    static void verifyCarService(Service carService, Message car, EnumGroup condition)
    {
        Annotation a = carService.getAnnotation("A");
        assertNotNull(a);
        
        assertEquals(Boolean.TRUE, a.getValue("public"));
        assertTrue(car == a.getValue("target"));
        
        assertEquals(Boolean.TRUE, carService.getOptions().get("public"));
        assertTrue(car == carService.getOptions().get("target"));
        
        Service.RpcMethod rm = carService.getRpcMethod("getMostRecentCar");
        assertNotNull(rm);
        
        Annotation rmA = rm.getAnnotation("A");
        assertNotNull(rmA);
        
        assertEquals(Boolean.FALSE, rmA.getValue("throttle"));
        assertTrue(condition == rmA.getValue("might_wanna_specify"));
        
        assertEquals(Boolean.FALSE, rm.getOptions().get("throttle"));
        assertTrue(condition == rm.getOptions().get("might_wanna_specify"));
    }

}
