//================================================================================
//Copyright (c) 2009, David Yu
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


package com.dyuproject.protostuff.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author David Yu
 * @created Aug 25, 2009
 */

public class V22Test extends TestCase
{
    
    public void testLite() throws Exception
    {

        ProtobufModel taskModel = new ProtobufModel(LiteRuntime.getModelMeta(V22Lite.Task.class));
        ProtobufModel personModel = new ProtobufModel(LiteRuntime.getModelMeta(V22Lite.Person.class));        
        
        System.err.println(taskModel.getModelMeta());        
        assertTrue(taskModel.getModelMeta().getPropertyCount()==4);
        assertTrue(taskModel.getModelMeta().getMinNumber()==1);
        assertTrue(taskModel.getModelMeta().getMaxNumber()==4);
        
        System.err.println(personModel.getModelMeta());
        assertTrue(personModel.getModelMeta().getPropertyCount()==7);
        assertTrue(personModel.getModelMeta().getMinNumber()==1);
        assertTrue(personModel.getModelMeta().getMaxNumber()==7);        
        
        V22Lite.Task task = V22Lite.Task.newBuilder()
            .setId(1)
            .build();
        
        V22Lite.Task.Builder taskBuilder = task.toBuilder();
        
        V22Lite.Person person = V22Lite.Person.newBuilder()
            .setId(2)
            .build();
        
        V22Lite.Person.Builder personBuilder = person.toBuilder();
        
        taskModel.getProperty("name").setValue(task, "foo");
        taskModel.getProperty("description").setValue(task, "bar");
        taskModel.getProperty("status").setValue(task, V22Lite.Task.Status.STARTED);
        
        assertTrue(task.getName().equals("foo"));
        assertTrue(task.getDescription().equals("bar"));
        assertTrue(task.getStatus() == V22Lite.Task.Status.STARTED);
        
        taskModel.getProperty("name").replaceValueIfNone(task, "foo1");
        taskModel.getProperty("description").removeValue(task);
        
        assertTrue(task.getName().equals("foo"));
        assertTrue(!task.hasDescription());
        assertTrue(taskModel.getProperty("description").getValue(task)==null);
        
        taskModel.getProperty("name").replaceValueIfAny(task, "food");
        taskModel.getProperty("description").replaceValueIfAny(task, "bar");
        
        assertTrue(task.getName().equals("food"));
        assertTrue(!task.hasDescription());
        assertTrue(taskModel.getProperty("description").getValue(task)==null);
        
        taskModel.getProperty("id").setValue(taskBuilder, 3);
        taskModel.getProperty("name").setValue(taskBuilder, "foo1");
        taskModel.getProperty("description").setValue(taskBuilder, "bar1");
        taskModel.getProperty("status").setValue(taskBuilder, V22Lite.Task.Status.COMPLETED);
        
        assertTrue(taskBuilder.getId()==3);
        assertTrue(taskBuilder.getName().equals("foo1"));
        assertTrue(taskBuilder.getDescription().equals("bar1"));
        assertTrue(taskBuilder.getStatus() == V22Lite.Task.Status.COMPLETED);
        
        personModel.getProperty("firstName").setValue(person, "John");
        personModel.getProperty("lastName").setValue(person, "Doe");
        personModel.getProperty("aGe").setValue(person, 20);
        personModel.getProperty("priorityTask").setValue(person, task);        
        
        assertTrue(person.getFirstName().equals("John"));
        assertTrue(person.getLastName().equals("Doe"));
        assertTrue(person.getAGe()==20);
        assertTrue(person.getPriorityTask(0).getId()==1);        
        
        personModel.getProperty("id").setValue(personBuilder, 4);
        personModel.getProperty("priorityTask").setValue(personBuilder, task);
        personModel.getProperty("delegatedTask").setValue(personBuilder, taskBuilder);
        
        taskBuilder = task.toBuilder();
        
        assertTrue(personBuilder.getId()==4);
        assertTrue(personBuilder.getPriorityTask(0).getId()==task.getId());
        assertTrue(personBuilder.getDelegatedTask(0).getId()==3);

        List<V22Lite.Task> taskList = new ArrayList<V22Lite.Task>();
        taskList.add(task);
        
        personModel.getProperty("delegatedTask").setValue(personBuilder, taskList);
        
        assertTrue(personBuilder.getDelegatedTask(1).getId()==taskList.get(0).getId());
        
        personModel.getProperty("delegatedTask").replaceValueIfNone(personBuilder, task.toBuilder().setId(0));
        
        assertTrue(personBuilder.getDelegatedTask(0).getId()!=0);
        
        personModel.getProperty("delegatedTask").replaceValueIfAny(personBuilder, task.toBuilder().setId(5));
        
        assertTrue(personBuilder.getDelegatedTask(0).getId()==5);
        
        personModel.getProperty("priorityTask").replaceValueIfNone(person, task.toBuilder().setId(0).build());
        
        assertTrue(person.getPriorityTask(0).getId()!=0);
        
        personModel.getProperty("priorityTask").replaceValueIfAny(person, task.toBuilder().setId(6).build());
        
        assertTrue(person.getPriorityTask(0).getId()==6);
        
        ReadOnlyModel taskModelR = new ReadOnlyModel(taskModel.getModelMeta());        
        ReadOnlyModel personModelR = new ReadOnlyModel(personModel.getModelMeta());
        
        assertEquals(task.getName(), taskModelR.getProperty("name").getValue(task));
        assertEquals(task.getStatus(), taskModelR.getProperty("status").getValue(task));
        
        assertEquals(person.getFirstName(), "John");
        assertEquals(person.getLastName(), "Doe");
        assertEquals(person.getFirstName(), personModelR.getProperty("firstName").getValue(person));
        assertEquals(person.getLastName(), personModelR.getProperty("lastName").getValue(person));
        
        assertEquals("J", personBuilder.setFirstName("J").getFirstName());
        assertEquals("D", personBuilder.setLastName("D").getLastName());
        assertEquals(personBuilder.getFirstName(), personModelR.getProperty("firstName").getValue(personBuilder));
        assertEquals(personBuilder.getLastName(), personModelR.getProperty("lastName").getValue(personBuilder));
    }
    
    public void testSpeed() throws Exception
    {

        ProtobufModel taskModel = new ProtobufModel(LiteRuntime.getModelMeta(V22Speed.Task.class));
        ProtobufModel personModel = new ProtobufModel(LiteRuntime.getModelMeta(V22Speed.Person.class));        
        
        System.err.println(taskModel.getModelMeta());        
        assertTrue(taskModel.getModelMeta().getPropertyCount()==4);
        assertTrue(taskModel.getModelMeta().getMinNumber()==1);
        assertTrue(taskModel.getModelMeta().getMaxNumber()==4);
        
        System.err.println(personModel.getModelMeta());
        assertTrue(personModel.getModelMeta().getPropertyCount()==7);
        assertTrue(personModel.getModelMeta().getMinNumber()==1);
        assertTrue(personModel.getModelMeta().getMaxNumber()==7);        
        
        V22Speed.Task task = V22Speed.Task.newBuilder()
            .setId(1)
            .build();
        
        V22Speed.Task.Builder taskBuilder = task.toBuilder();
        
        V22Speed.Person person = V22Speed.Person.newBuilder()
            .setId(2)
            .build();
        
        V22Speed.Person.Builder personBuilder = person.toBuilder();
        
        taskModel.getProperty("name").setValue(task, "foo");
        taskModel.getProperty("description").setValue(task, "bar");
        taskModel.getProperty("status").setValue(task, V22Speed.Task.Status.STARTED);
        
        assertTrue(task.getName().equals("foo"));
        assertTrue(task.getDescription().equals("bar"));
        assertTrue(task.getStatus() == V22Speed.Task.Status.STARTED);
        
        taskModel.getProperty("name").replaceValueIfNone(task, "foo1");
        taskModel.getProperty("description").removeValue(task);
        
        assertTrue(task.getName().equals("foo"));
        assertTrue(!task.hasDescription());
        assertTrue(taskModel.getProperty("description").getValue(task)==null);
        
        taskModel.getProperty("name").replaceValueIfAny(task, "food");
        taskModel.getProperty("description").replaceValueIfAny(task, "bar");
        
        assertTrue(task.getName().equals("food"));
        assertTrue(!task.hasDescription());
        assertTrue(taskModel.getProperty("description").getValue(task)==null);
        
        taskModel.getProperty("id").setValue(taskBuilder, 3);
        taskModel.getProperty("name").setValue(taskBuilder, "foo1");
        taskModel.getProperty("description").setValue(taskBuilder, "bar1");
        taskModel.getProperty("status").setValue(taskBuilder, V22Speed.Task.Status.COMPLETED);
        
        assertTrue(taskBuilder.getId()==3);
        assertTrue(taskBuilder.getName().equals("foo1"));
        assertTrue(taskBuilder.getDescription().equals("bar1"));
        assertTrue(taskBuilder.getStatus() == V22Speed.Task.Status.COMPLETED);
        
        personModel.getProperty("firstName").setValue(person, "John");
        personModel.getProperty("lastName").setValue(person, "Doe");
        personModel.getProperty("aGe").setValue(person, 20);
        personModel.getProperty("priorityTask").setValue(person, task);        
        
        assertTrue(person.getFirstName().equals("John"));
        assertTrue(person.getLastName().equals("Doe"));
        assertTrue(person.getAGe()==20);
        assertTrue(person.getPriorityTask(0).getId()==1);        
        
        personModel.getProperty("id").setValue(personBuilder, 4);
        personModel.getProperty("priorityTask").setValue(personBuilder, task);
        personModel.getProperty("delegatedTask").setValue(personBuilder, taskBuilder);
        
        taskBuilder = task.toBuilder();
        
        assertTrue(personBuilder.getId()==4);
        assertTrue(personBuilder.getPriorityTask(0).getId()==task.getId());
        assertTrue(personBuilder.getDelegatedTask(0).getId()==3);

        List<V22Speed.Task> taskList = new ArrayList<V22Speed.Task>();
        taskList.add(task);
        
        personModel.getProperty("delegatedTask").setValue(personBuilder, taskList);
        
        assertTrue(personBuilder.getDelegatedTask(1).getId()==taskList.get(0).getId());
        
        personModel.getProperty("delegatedTask").replaceValueIfNone(personBuilder, task.toBuilder().setId(0));
        
        assertTrue(personBuilder.getDelegatedTask(0).getId()!=0);
        
        personModel.getProperty("delegatedTask").replaceValueIfAny(personBuilder, task.toBuilder().setId(5));
        
        assertTrue(personBuilder.getDelegatedTask(0).getId()==5);
        
        personModel.getProperty("priorityTask").replaceValueIfNone(person, task.toBuilder().setId(0).build());
        
        assertTrue(person.getPriorityTask(0).getId()!=0);
        
        personModel.getProperty("priorityTask").replaceValueIfAny(person, task.toBuilder().setId(6).build());
        
        assertTrue(person.getPriorityTask(0).getId()==6);
        
        ReadOnlyModel taskModelR = new ReadOnlyModel(taskModel.getModelMeta());        
        ReadOnlyModel personModelR = new ReadOnlyModel(personModel.getModelMeta());
        
        assertEquals(task.getName(), taskModelR.getProperty("name").getValue(task));
        assertEquals(task.getStatus(), taskModelR.getProperty("status").getValue(task));
        
        assertEquals(person.getFirstName(), "John");
        assertEquals(person.getLastName(), "Doe");
        assertEquals(person.getFirstName(), personModelR.getProperty("firstName").getValue(person));
        assertEquals(person.getLastName(), personModelR.getProperty("lastName").getValue(person));
        
        assertEquals("J", personBuilder.setFirstName("J").getFirstName());
        assertEquals("D", personBuilder.setLastName("D").getLastName());
        assertEquals(personBuilder.getFirstName(), personModelR.getProperty("firstName").getValue(personBuilder));
        assertEquals(personBuilder.getLastName(), personModelR.getProperty("lastName").getValue(personBuilder));
    }

}
