//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.model;

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

        Model<DefaultProperty> taskModel = Model.get(V22Lite.Task.class);
        Model<DefaultProperty> personModel = Model.get(V22Lite.Person.class);        
        
        System.err.println(taskModel.getModelMeta());        
        assertTrue(taskModel.getModelMeta().getPropertyCount()==4);
        assertTrue(taskModel.getModelMeta().getMinNumber()==1);
        assertTrue(taskModel.getModelMeta().getMaxNumber()==4);
        
        System.err.println(personModel.getModelMeta());
        assertTrue(personModel.getModelMeta().getPropertyCount()==9);
        assertTrue(personModel.getModelMeta().getMinNumber()==1);
        assertTrue(personModel.getModelMeta().getMaxNumber()==9);        
        
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
        personModel.getProperty("currentTask").setValue(person, task);
        personModel.getProperty("repeatedLong").setValue(person, 1);
        personModel.getProperty("repeatedLong").setValue(person, 2);
        
        assertTrue(person.getFirstName().equals("John"));
        assertTrue(person.getLastName().equals("Doe"));
        assertTrue(person.getAGe()==20);
        assertTrue(person.getPriorityTask(0).getId()==1);
        assertTrue(person.getCurrentTask().getId()==task.getId());
        assertTrue(person.getRepeatedLongCount()==2);
        assertTrue(person.getRepeatedLong(0)==1);
        assertTrue(person.getRepeatedLong(1)==2);
        
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
        
        assertTrue(personBuilder.getDelegatedTaskCount()==2);
        assertTrue(personBuilder.getDelegatedTask(1).getId()==taskList.get(0).getId());
        
        personModel.getProperty("delegatedTask").replaceValueIfNone(personBuilder, task.toBuilder().setId(0));
        
        assertTrue(personBuilder.getDelegatedTaskCount()==2);
        assertTrue(personBuilder.getDelegatedTask(0).getId()!=0);
        
        // this will not replace (but add) if the field is repeated
        personModel.getProperty("delegatedTask").replaceValueIfAny(personBuilder, task.toBuilder().setId(5));
        
        assertTrue(personBuilder.getDelegatedTaskCount()==3);
        assertTrue(personBuilder.getDelegatedTask(2).getId()==5);
        
        // this will not replace (but add) if the field is repeated
        personModel.getProperty("delegatedTask").replaceValueIfAny(personBuilder, taskList);
        
        assertTrue(personBuilder.getDelegatedTaskCount()==4);
        assertTrue(personBuilder.getDelegatedTask(3).getId()==taskList.get(0).getId());
        
        personModel.getProperty("priorityTask").replaceValueIfNone(person, task.toBuilder().setId(0).build());
        
        assertTrue(person.getPriorityTask(0).getId()!=0);
        
        personModel.getProperty("priorityTask").replaceValueIfAny(person, task.toBuilder().setId(6).build());
        
        assertTrue(person.getPriorityTask(0).getId()==6);
        
        Model<ReadOnlyProperty> taskModelR = new Model<ReadOnlyProperty>(taskModel.getModelMeta(), 
                ReadOnlyProperty.FACTORY);        
        Model<ReadOnlyProperty> personModelR = new Model<ReadOnlyProperty>(personModel.getModelMeta(), 
                ReadOnlyProperty.FACTORY);   
        
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

        Model<DefaultProperty> taskModel = Model.get(V22Speed.Task.class);
        Model<DefaultProperty> personModel = Model.get(V22Speed.Person.class);        
        
        System.err.println(taskModel.getModelMeta());        
        assertTrue(taskModel.getModelMeta().getPropertyCount()==4);
        assertTrue(taskModel.getModelMeta().getMinNumber()==1);
        assertTrue(taskModel.getModelMeta().getMaxNumber()==4);
        
        System.err.println(personModel.getModelMeta());
        assertTrue(personModel.getModelMeta().getPropertyCount()==9);
        assertTrue(personModel.getModelMeta().getMinNumber()==1);
        assertTrue(personModel.getModelMeta().getMaxNumber()==9);        
        
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
        personModel.getProperty("currentTask").setValue(person, task);
        personModel.getProperty("repeatedLong").setValue(person, 1);
        personModel.getProperty("repeatedLong").setValue(person, 2);
        
        assertTrue(person.getFirstName().equals("John"));
        assertTrue(person.getLastName().equals("Doe"));
        assertTrue(person.getAGe()==20);
        assertTrue(person.getPriorityTask(0).getId()==1);
        assertTrue(person.getCurrentTask().getId()==task.getId());
        assertTrue(person.getRepeatedLongCount()==2);
        assertTrue(person.getRepeatedLong(0)==1);
        assertTrue(person.getRepeatedLong(1)==2);
        
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
        
        assertTrue(personBuilder.getDelegatedTaskCount()==2);
        assertTrue(personBuilder.getDelegatedTask(1).getId()==taskList.get(0).getId());
        
        personModel.getProperty("delegatedTask").replaceValueIfNone(personBuilder, task.toBuilder().setId(0));
        
        assertTrue(personBuilder.getDelegatedTaskCount()==2);
        assertTrue(personBuilder.getDelegatedTask(0).getId()!=0);
        
        // this will not replace (but add) if the field is repeated
        personModel.getProperty("delegatedTask").replaceValueIfAny(personBuilder, task.toBuilder().setId(5));
        
        assertTrue(personBuilder.getDelegatedTaskCount()==3);
        assertTrue(personBuilder.getDelegatedTask(2).getId()==5);
        
        // this will not replace (but add) if the field is repeated
        personModel.getProperty("delegatedTask").replaceValueIfAny(personBuilder, taskList);
        
        assertTrue(personBuilder.getDelegatedTaskCount()==4);
        assertTrue(personBuilder.getDelegatedTask(3).getId()==taskList.get(0).getId());
        
        personModel.getProperty("priorityTask").replaceValueIfNone(person, task.toBuilder().setId(0).build());
        
        assertTrue(person.getPriorityTask(0).getId()!=0);
        
        personModel.getProperty("priorityTask").replaceValueIfAny(person, task.toBuilder().setId(6).build());
        
        assertTrue(person.getPriorityTask(0).getId()==6);
        
        Model<ReadOnlyProperty> taskModelR = new Model<ReadOnlyProperty>(taskModel.getModelMeta(), 
                ReadOnlyProperty.FACTORY);        
        Model<ReadOnlyProperty> personModelR = new Model<ReadOnlyProperty>(personModel.getModelMeta(), 
                ReadOnlyProperty.FACTORY);  
        
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
