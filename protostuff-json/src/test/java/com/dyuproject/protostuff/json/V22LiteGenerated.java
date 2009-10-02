//========================================================================
//Copyright 2007-2008 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.json;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.dyuproject.protostuff.model.V22Lite.Person;
import com.dyuproject.protostuff.model.V22Lite.Task;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

/**
 * @author David Yu
 * @created Oct 2, 2009
 */

public class V22LiteGenerated
{
    
    
    @SuppressWarnings("unchecked")
    public static <P extends ProtobufConvertor<T,B>, T extends MessageLite, B extends Builder> 
        ProtobufJSON<ProtobufConvertor<MessageLite,Builder>> getJSON()
    {
        return JSON;
    }
    
    @SuppressWarnings("unchecked")
    static final ProtobufJSON JSON = new ProtobufJSON()
    {

        @Override
        protected ProtobufConvertor getConvertor(Class messageType)
        {
            if(messageType==Person.class)
                return PERSON_CONVERTOR;
            if(messageType==Task.class)
                return TASK_CONVERTOR;
            return null;
        }
        
    };
    
    static final ProtobufConvertor<Task,Task.Builder> TASK_CONVERTOR = new ProtobufConvertor<Task,Task.Builder>()
    {
        
        final HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
        {
            fieldMap.put("id", 1);
            fieldMap.put("name", 2);
            fieldMap.put("description", 3);
            fieldMap.put("status", 4);
        };
        
        final int getFieldNumber(String name) throws IOException
        {
            Integer num = fieldMap.get(name);
            if(num==null)
                throw new IOException("Field unknown: " + name + " on message: " + Task.class);
            
            return num.intValue();
        }

        public void generateTo(JsonGenerator generator, Task task) throws IOException
        {
            generator.writeStartObject();
            
            if(task.hasId())
                generator.writeNumberField("id", task.getId());
            
            if(task.hasName())
                generator.writeStringField("name", task.getName());
            
            if(task.hasDescription())
                generator.writeStringField("description", task.getDescription());
            
            if(task.hasStatus())
                generator.writeStringField("status", task.getStatus().name());
            
            generator.writeEndObject();            
        }

        public Task.Builder parseFrom(JsonParser parser) throws IOException
        {
            Task.Builder builder = Task.newBuilder();
            mergeFrom(parser, builder);
            return builder;
        }

        public void mergeFrom(JsonParser parser, Task.Builder builder) throws IOException
        {
            for(JsonToken t = parser.nextToken(); t!=JsonToken.END_OBJECT; t=parser.nextToken())
            {
                if(t!=JsonToken.FIELD_NAME)
                {
                    throw new IOException("Expected token: $field: but was " + 
                            parser.getCurrentToken() + " on message: " + 
                            Task.class);
                }
                String name = parser.getCurrentName();
                switch(getFieldNumber(name))
                {
                    case 1:
                        parser.nextToken();
                        builder.setId(parser.getIntValue());
                        break;
                    case 2:
                        parser.nextToken();
                        builder.setName(parser.getText());
                        break;
                    case 3:
                        parser.nextToken();
                        builder.setDescription(parser.getText());
                        break;
                    case 4:
                        parser.nextToken();
                        builder.setStatus(Task.Status.valueOf(parser.getText()));
                        break;
                    default:
                        throw new IOException("Field unknown: " + name + " on message: " + Task.class);
                }
            }
        }
        
    };
    
    static final ProtobufConvertor<Person,Person.Builder> PERSON_CONVERTOR = 
        new ProtobufConvertor<Person,Person.Builder>()
    {
        
        final HashMap<String,Integer> fieldMap = new HashMap<String,Integer>();
        {
            fieldMap.put("id", 1);
            fieldMap.put("email", 2);
            fieldMap.put("firstName", 3);
            fieldMap.put("lastName", 4);
            fieldMap.put("delegatedTask", 5);
            fieldMap.put("priorityTask", 6);
            fieldMap.put("aGe", 7);
            fieldMap.put("currentTask", 8);
            fieldMap.put("repeatedLong", 9);
        };
        
        final int getFieldNumber(String name) throws IOException
        {
            Integer num = fieldMap.get(name);
            if(num==null)
                throw new IOException("Field unknown: " + name + " on message: " + Person.class);
            
            return num.intValue();
        }
        
        public final void generateTo(JsonGenerator generator, Person person) throws IOException
        {
            generator.writeStartObject();
            
            if(person.hasId())
                generator.writeNumberField("id", person.getId());
            
            if(person.hasEmail())
                generator.writeStringField("email", person.getEmail());
            
            if(person.hasFirstName())
                generator.writeStringField("firstName", person.getFirstName());
            
            if(person.hasLastName())
                generator.writeStringField("lastName", person.getLastName());
            
            generator.writeFieldName("delegatedTask");
            generator.writeStartArray();
            for(Task t : person.getDelegatedTaskList())
                TASK_CONVERTOR.generateTo(generator, t);
            generator.writeEndArray();
            
            generator.writeFieldName("priorityTask");
            generator.writeStartArray();
            for(Task t : person.getPriorityTaskList())
                TASK_CONVERTOR.generateTo(generator, t);
            generator.writeEndArray();
            
            if(person.hasAGe())
                generator.writeNumberField("aGe", person.getAGe());
            
            if(person.hasCurrentTask())
            {
                generator.writeFieldName("currentTask");
                TASK_CONVERTOR.generateTo(generator, person.getCurrentTask());
            }
            
            generator.writeFieldName("repeatedLong");
            generator.writeStartArray();
            for(Long l : person.getRepeatedLongList())
                generator.writeNumber(l);
            generator.writeEndArray();
            
            generator.writeEndObject();
            
        }

        public final void mergeFrom(JsonParser parser, Person.Builder builder) throws IOException
        {
            for(JsonToken t = parser.nextToken(); t!=JsonToken.END_OBJECT; t=parser.nextToken())
            {
                if(t!=JsonToken.FIELD_NAME)
                {
                    throw new IOException("Expected token: $field: but was " + 
                            parser.getCurrentToken() + " on message: " + 
                            Person.class);
                }
                String name = parser.getCurrentName();
                switch(getFieldNumber(name))
                {
                    case 1:
                        parser.nextToken();
                        builder.setId(parser.getIntValue());
                        break;
                    case 2:
                        parser.nextToken();
                        builder.setEmail(parser.getText());
                        break;
                    case 3:
                        parser.nextToken();
                        builder.setFirstName(parser.getText());
                        break;
                    case 4:
                        parser.nextToken();
                        builder.setLastName(parser.getText());
                        break;
                    case 5:
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message: " + 
                                    Person.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                            if(t1 != JsonToken.START_OBJECT)
                            {
                                throw new IOException("Expected token: { but was " + 
                                        parser.getCurrentToken() + " on " + name + " of message: " + 
                                        Person.class);
                            }
                            
                            builder.addDelegatedTask(TASK_CONVERTOR.parseFrom(parser));
                        }
                        break;
                    case 6:
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message: " + 
                                    Person.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                            if(t1 != JsonToken.START_OBJECT)
                            {
                                throw new IOException("Expected token: { but was " + 
                                        parser.getCurrentToken() + " on " + name + " of message: " + 
                                        Person.class);
                            }
                            
                            builder.addPriorityTask(TASK_CONVERTOR.parseFrom(parser));
                        }
                        break;
                    case 7:
                        parser.nextToken();
                        builder.setAGe(parser.getIntValue());
                        break;
                    case 8:
                        parser.nextToken();
                        builder.setCurrentTask(TASK_CONVERTOR.parseFrom(parser));
                        break;
                    case 9:
                        if(parser.nextToken()!=JsonToken.START_ARRAY)
                        {
                            throw new IOException("Expected token: [ but was " + 
                                    parser.getCurrentToken() + " on message: " + 
                                    Person.class);
                        }
                        for(JsonToken t1=parser.nextToken(); t1!=JsonToken.END_ARRAY; t1=parser.nextToken())
                        {
                            builder.addRepeatedLong(parser.getLongValue());
                        }
                        break;
                    default:
                        throw new IOException("Field unknown: " + name + " on message: " + Person.class);
                }
            }
        }

        public final Person.Builder parseFrom(JsonParser parser) throws IOException
        {
            Person.Builder builder = Person.newBuilder();
            mergeFrom(parser, builder);
            return builder;
        }
        
    };

    /*static final HashMap<String,Convertor<? extends MessageLite,? extends Builder>> __convertors = 
        new HashMap<String,Convertor<? extends MessageLite,? extends Builder>>();

    static final ProtobufJSON<Convertor<?,?>> JSON = new ProtobufJSON<Convertor<?,?>>()
    {
        protected <T extends MessageLite, B extends Builder> Convertor<?, ?> getConvertor(Class<?> messageType)
        {
            return __convertors.get(messageType.getName());
        }        
    };
    
    static void add(Class<?> messageClass, Convertor<?,?> convertor)
    {
        __convertors.put(messageClass.getName(), convertor);
    }
    
    static abstract class Convertor<T extends MessageLite,B extends Builder> implements ProtobufConvertor<MessageLite,Builder>{}   
    
    static final Convertor<Task,Task.Builder> TASK_CONVERTOR = new Convertor<Task, Task.Builder>()
    {

        public void generateTo(JsonGenerator generator, MessageLite message) throws IOException
        {
            
        }

        public void mergeFrom(JsonParser parser, Builder builder) throws IOException
        {
            
        }

        public Builder parseFrom(JsonParser parser) throws IOException
        {
            return null;
        }
        
    };
    
    static final Convertor<Person, Person.Builder> PERSON_CONVERTOR = new Convertor<Person, Person.Builder>()
    {

        public void generateTo(JsonGenerator generator, MessageLite message) throws IOException
        {
            
        }

        public void mergeFrom(JsonParser parser, Builder builder) throws IOException
        {
            
        }

        public Builder parseFrom(JsonParser parser) throws IOException
        {
            return null;
        }
        
    };*/


    /*static
    {
        add(Task.class, TASK_CONVERTOR);
    }*/








}
