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
import java.util.Collection;

import junit.framework.TestCase;

/**
 * Tests for annotations on messages, enums, fields, services, rpc methods, extensions
 *
 * @author David Yu
 * @created Dec 30, 2010
 */
public class AnnotationTest extends TestCase
{
    
    public void testIt() throws Exception
    {
        File f = ProtoParserTest.getFile("com/dyuproject/protostuff/parser/test_annotations.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        
        Message person = proto.getMessage("Person");
        assertNotNull(person);
        
        Annotation defaultPerson = person.getAnnotation("DefaultPerson");
        assertNotNull(defaultPerson);
        assertEquals("Anonymous Coward", defaultPerson.getValue("name"));
        
        Field<?> age = person.getField("age");
        assertNotNull(age);
        
        Annotation defaultAge = age.getAnnotation("DefaultAge");
        assertNotNull(defaultAge);
        assertTrue(defaultAge.getParams().isEmpty());
        
        EnumGroup gender = person.getNestedEnumGroup("Gender");
        assertNotNull(gender);
        
        Annotation defaultGender = gender.getAnnotation("DefaultGender");
        assertEquals("MALE", defaultGender.getValue("value"));
        
        Message listRequest = person.getNestedMessage("ListRequest");
        assertNotNull(listRequest);
        
        Annotation nestedMessageAnnotation = listRequest.getAnnotation("NestedMessageAnnotation");
        assertNotNull(nestedMessageAnnotation);
        assertTrue(nestedMessageAnnotation.getParams().isEmpty());
        
        Message response = listRequest.getNestedMessage("Response");
        assertNotNull(response);
        
        Annotation deeperMessageAnnotation = response.getAnnotation("DeeperMessageAnnotation");
        assertNotNull(deeperMessageAnnotation);
        assertTrue(deeperMessageAnnotation.getParams().isEmpty());
        
        Field<?> personField = response.getField("person");
        assertNotNull(personField);
        
        Annotation deeperMessageFieldAnnotation = personField.getAnnotation("DeeperMessageFieldAnnotation");
        assertNotNull(deeperMessageFieldAnnotation);
        assertTrue(deeperMessageFieldAnnotation.getParams().size() == 1);
        assertEquals(false, deeperMessageFieldAnnotation.getValue("nullable"));
        
        Collection<Extension> extensions = proto.getExtensions();
        assertTrue(extensions.size() == 1);
        Extension extendPerson = extensions.iterator().next();
        assertNotNull(extendPerson);
        
        Annotation personExtras = extendPerson.getAnnotation("PersonExtras");
        assertNotNull(personExtras);
        assertTrue(personExtras.getParams().isEmpty());
        
        Field<?> country = extendPerson.getField("country");
        assertNotNull(country);
        
        Annotation validate = country.getAnnotation("Validate");
        assertNotNull(validate);
        assertTrue(validate.getParams().isEmpty());
        
        Service personService = proto.getService("PersonService");
        assertNotNull(personService);
        assertTrue(personService.getAnnotationMap().size() == 2);
        
        Annotation someServiceAnnotation = personService.getAnnotation("SomeServiceAnnotation");
        Annotation anotherServiceAnnotation = personService.getAnnotation("AnotherServiceAnnotation");
        
        assertTrue(someServiceAnnotation != null && someServiceAnnotation.getParams().isEmpty());
        assertTrue(anotherServiceAnnotation != null && anotherServiceAnnotation.getParams().isEmpty());
        
        Service.RpcMethod put = personService.getRpcMethod("Put");
        assertNotNull(put);
        
        Annotation authRequired = put.getAnnotation("AuthRequired");
        assertNotNull(authRequired);
        assertTrue(authRequired.getParams().size() == 1);
        assertEquals("admin", authRequired.getValue("role"));
        
    }

}
