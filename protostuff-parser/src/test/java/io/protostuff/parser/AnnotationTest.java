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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

/**
 * Tests for annotations on messages, enums, fields, services, rpc methods, extensions
 * 
 * @author David Yu
 * @created Dec 30, 2010
 */
public class AnnotationTest
{

    @Test
    public void testIt() throws Exception
    {
        File f = ProtoParserTest.getFile("io/protostuff/parser/test_annotations.proto");
        assertTrue(f.exists());

        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);

        Message person = proto.getMessage("Person");
        assertNotNull(person);
        assertEquals("[ doc1]", person.getDocs().toString());

        Annotation defaultPerson = person.getAnnotation("DefaultPerson");
        assertNotNull(defaultPerson);
        assertEquals("Anonymous Coward", defaultPerson.getValue("name"));

        Field<?> age = person.getField("age");
        assertNotNull(age);
        assertEquals("[ doc2]", age.getDocs().toString());

        Annotation defaultAge = age.getAnnotation("DefaultAge");
        assertNotNull(defaultAge);
        assertTrue(defaultAge.getParams().isEmpty());

        EnumGroup gender = person.getNestedEnumGroup("Gender");
        assertNotNull(gender);
        assertEquals("[ doc3]", gender.getDocs().toString());

        Annotation defaultGender = gender.getAnnotation("DefaultGender");
        assertEquals("MALE", defaultGender.getValue("value"));

        EnumGroup.Value male = gender.getValue(0);
        assertNotNull(male);
        assertEquals("[ doc4]", male.getDocs().toString());

        Annotation maleA = male.getAnnotation("Alias");
        assertNotNull(maleA);
        assertEquals("m", maleA.getValue("value"));
        assertTrue(person == maleA.getValue("type"));

        EnumGroup.Value female = gender.getValue(1);
        assertNotNull(female);
        assertEquals("[ doc5]", female.getDocs().toString());

        Annotation femaleA = female.getAnnotation("Alias");
        assertNotNull(femaleA);
        assertEquals("f", femaleA.getValue("value"));
        assertTrue(person == femaleA.getValue("type"));

        Message listRequest = person.getNestedMessage("ListRequest");
        assertNotNull(listRequest);
        assertEquals("[ doc6]", listRequest.getDocs().toString());

        Annotation nestedMessageAnnotation = listRequest.getAnnotation("NestedMessageAnnotation");
        assertNotNull(nestedMessageAnnotation);
        assertTrue(nestedMessageAnnotation.getParams().isEmpty());

        Message response = listRequest.getNestedMessage("Response");
        assertNotNull(response);
        assertEquals("[ doc7]", response.getDocs().toString());

        Annotation deeperMessageAnnotation = response.getAnnotation("DeeperMessageAnnotation");
        assertNotNull(deeperMessageAnnotation);
        assertTrue(deeperMessageAnnotation.getParams().isEmpty());

        Field<?> personField = response.getField("person");
        assertNotNull(personField);
        assertEquals("[ doc8]", personField.getDocs().toString());

        Annotation deeperMessageFieldAnnotation = personField.getAnnotation("DeeperMessageFieldAnnotation");
        assertNotNull(deeperMessageFieldAnnotation);
        assertTrue(deeperMessageFieldAnnotation.getParams().size() == 2);
        assertEquals(false, deeperMessageFieldAnnotation.getValue("nullable"));
        assertEquals(Float.valueOf(1.1f), deeperMessageFieldAnnotation.getValue("version"));

        Field<?> keyField = response.getField("key");
        assertNotNull(keyField);
        assertEquals("[ doc9]", keyField.getDocs().toString());

        Annotation testNested = keyField.getAnnotation("TestNested");
        assertNotNull(testNested);
        assertTrue(person == testNested.getValue("type"));
        assertTrue(gender == testNested.getValue("g"));

        Collection<Extension> extensions = proto.getExtensions();
        assertTrue(extensions.size() == 1);
        Extension extendPerson = extensions.iterator().next();
        assertNotNull(extendPerson);
        assertEquals("[ doc10]", extendPerson.getDocs().toString());

        Annotation personExtras = extendPerson.getAnnotation("PersonExtras");
        assertNotNull(personExtras);
        assertTrue(personExtras.getParams().isEmpty());

        Field<?> country = extendPerson.getField("country");
        assertNotNull(country);
        assertEquals("[ doc11]", country.getDocs().toString());

        Field<?> k = extendPerson.getField("key");
        assertNotNull(k);
        assertEquals("[ doc12]", k.getDocs().toString());

        Annotation validate = country.getAnnotation("Validate");
        assertNotNull(validate);
        assertTrue(validate.getParams().isEmpty());

        Service personService = proto.getService("PersonService");
        assertNotNull(personService);
        assertEquals("[ doc13]", personService.getDocs().toString());

        assertTrue(personService.getAnnotationMap().size() == 2);

        Annotation someServiceAnnotation = personService.getAnnotation("SomeServiceAnnotation");
        Annotation anotherServiceAnnotation = personService.getAnnotation("AnotherServiceAnnotation");

        assertTrue(someServiceAnnotation != null && someServiceAnnotation.getParams().isEmpty());
        assertTrue(anotherServiceAnnotation != null && anotherServiceAnnotation.getParams().isEmpty());

        Service.RpcMethod put = personService.getRpcMethod("Put");
        assertNotNull(put);
        assertEquals("[ doc14]", put.getDocs().toString());

        Annotation authRequired = put.getAnnotation("AuthRequired");
        assertNotNull(authRequired);
        assertTrue(authRequired.getParams().size() == 1);
        assertEquals("admin", authRequired.getValue("role"));

        Service.RpcMethod list = personService.getRpcMethod("List");
        assertNotNull(list);
        assertEquals("[ doc15]", list.getDocs().toString());

        Annotation testRpc = list.getAnnotation("TestRpc");
        assertNotNull(testRpc);
        assertTrue(person == testRpc.getValue("type"));
        assertTrue(gender == testRpc.getValue("g"));
    }

}
