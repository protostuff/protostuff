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

package com.dyuproject.protostuff.protoparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * TODO
 *
 * @author David Yu
 * @created Dec 19, 2009
 */
public class Message
{
    
    String name;
    Proto proto;
    Message parentMessage;
    final LinkedHashMap<String, Message> nestedMessages = new LinkedHashMap<String, Message>();
    final LinkedHashMap<String,EnumGroup> nestedEnumGroups = new LinkedHashMap<String,EnumGroup>();
    final LinkedHashMap<String,Field<?>> fields = new LinkedHashMap<String,Field<?>>();
    final ArrayList<Field<?>> sortedFields = new ArrayList<Field<?>>();
    
    public Message()
    {
        
    }
    
    public Message(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Proto getProto()
    {
        Proto p = proto;
        if(p==null)
            p = proto = parentMessage.getProto();
        return p;
    }
    
    public Message getParentMessage()
    {
        return parentMessage;
    }
    
    public boolean isNested()
    {
        return parentMessage!=null;
    }
    
    public boolean hasNestedMessages()
    {
        return !nestedMessages.isEmpty();
    }
    
    public boolean hasNestedEnumGroups()
    {
        return !nestedEnumGroups.isEmpty();
    }
    
    public Collection<Message> getNestedMessages()
    {
        return nestedMessages.values();
    }
    
    public Message getNestedMessage(String name)
    {
        return nestedMessages.get(name);     
    }
    
    void addNestedMessage(Message message)
    {
        nestedMessages.put(message.name, message);
        message.parentMessage = this;
    }
    
    public Collection<EnumGroup> getNestedEnumGroups()
    {
        return nestedEnumGroups.values();
    }
    
    public EnumGroup getNestedEnumGroup(String name)
    {
        return nestedEnumGroups.get(name);
    }
    
    void addNestedEnumGroup(EnumGroup enumGroup)
    {
        nestedEnumGroups.put(enumGroup.name, enumGroup);
        enumGroup.parentMessage = this;
    }
    
    public Collection<Field<?>> getFields()
    {
        return sortedFields;
    }
    
    public Field<?> getField(String name)
    {
        return fields.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Field<?>> T getField(String name, Class<T> typeClass)
    {
        return (T)fields.get(name);
    }
    
    void addField(Field<?> field)
    {
        fields.put(field.name, field);
    }
    
    public String toString()
    {
        return new StringBuilder()
            .append('{')
            .append("name:").append(name)
            .append(',').append("enumGroups:").append(nestedEnumGroups.values())
            .append(',').append("fields:").append(fields.values())
            .append('}')
            .toString();
    }
    
    void resolveReferences()
    {
        Proto p = getProto();
        for(Field<?> f : fields.values())
        {
            if(f instanceof Field.Reference)
            {
                Field.Reference fr = (Field.Reference)f;
                //System.err.println(fr.refName + " @ " + fr.message.name);
                String refName = fr.refName;
                String packageName = fr.packageName;
                if(packageName==null)
                {
                    Message msg = fr.message.getNestedMessage(refName);
                    if(msg!=null || (msg=p.getMessage(refName))!=null)
                    {
                        MessageField mf = newMessageField(msg, fr);
                        fields.put(mf.name, mf);
                        continue;
                    }

                    EnumGroup eg = fr.message.getNestedEnumGroup(refName);
                    if(eg!=null || (eg=p.getEnumGroup(refName))!=null)
                    {
                        EnumField ef = newEnumField(eg, fr);
                        if(fr.defaultValue instanceof String)
                        {
                            String enumRefName = (String)fr.defaultValue;
                            //System.err.println(enumRefName);
                            EnumGroup.Value value = eg.getValue(enumRefName);
                            if(value==null)
                                throw new IllegalStateException("The field: " + ef.name + " contains an unknown enum value: " + enumRefName);
                        }
                        
                        fields.put(ef.name, ef);
                        continue;
                    }
                    
                    throw new IllegalStateException("unknown field: " + refName);
                }

                int dotIdx = packageName.indexOf('.');
                if(dotIdx==-1)
                {
                    // could be a nested message/enum
                    Message msg = fr.message.getNestedMessage(packageName);
                    if(msg!=null || (msg=p.getMessage(packageName))!=null)
                    {
                        Message nestedMsg = msg.getNestedMessage(refName);
                        if(nestedMsg!=null)
                        {
                            MessageField mf = newMessageField(nestedMsg, fr);
                            fields.put(mf.name, mf);
                            continue;
                        }
                        EnumGroup eg = msg.getNestedEnumGroup(refName);
                        if(eg!=null)
                        {
                            EnumField ef = newEnumField(eg, fr);
                            if(fr.defaultValue instanceof String)
                            {
                                String enumRefName = (String)fr.defaultValue;
                                //System.err.println(enumRefName);
                                EnumGroup.Value value = eg.getValue(enumRefName);
                                if(value==null)
                                    throw new IllegalStateException("The field: " + ef.name + " contains an unknown enum value: " + enumRefName);
                            }
                            
                            fields.put(ef.name, ef);
                            continue;
                        }
                    }
                }
                else
                {
                    Message m = null, parent = null;
                    int last = -1;
                    boolean found = false;
                    while(true)
                    {
                        String name = packageName.substring(last+1, dotIdx);
                        if(parent==null)
                        {
                            parent = m = fr.message.getNestedMessage(name);
                            if(m==null)
                                parent = m = p.getMessage(name);
                        }
                        else
                            parent = m = parent.getNestedMessage(name);
                        
                        if(m==null)
                            break;
                        
                        last = dotIdx;
                        dotIdx = packageName.indexOf('.', dotIdx+1);
                        if(dotIdx == -1)
                        {
                            // last
                            m = m.getNestedMessage(packageName.substring(last+1));
                            if(m==null)
                                break;
                            
                            Message nestedMsg = m.getNestedMessage(refName);
                            if(nestedMsg!=null)
                            {
                                MessageField mf = newMessageField(nestedMsg, fr);
                                fields.put(mf.name, mf);
                                found = true;
                            }
                            else
                            {
                                EnumGroup eg = m.getNestedEnumGroup(refName);
                                if(eg!=null)
                                {
                                    EnumField ef = newEnumField(eg, fr);
                                    if(fr.defaultValue instanceof String)
                                    {
                                        String enumRefName = (String)fr.defaultValue;
                                        //System.err.println(enumRefName);
                                        EnumGroup.Value value = eg.getValue(enumRefName);
                                        if(value==null)
                                            throw new IllegalStateException("The field: " + ef.name + " contains an unknown enum value: " + enumRefName);
                                    }
                                    
                                    fields.put(ef.name, ef);
                                    found = true;
                                }
                            }
                            break;
                        }
                    }
                    if(found)
                        continue;
                }

                
                Proto proto = p.getImportedProto(packageName);
                if(proto==null)
                    throw new IllegalStateException("unknown field: " + packageName + "." + refName);

                Message msg = proto.getMessage(refName);
                if(msg!=null)
                {
                    MessageField mf = newMessageField(msg, fr);
                    fields.put(mf.name, mf);
                    continue;
                }
                
                EnumGroup eg = proto.getEnumGroup(refName);
                if(eg!=null)
                {
                    EnumField ef = newEnumField(eg, fr);
                    if(fr.defaultValue instanceof String)
                    {
                        String enumRefName = (String)fr.defaultValue;
                        //System.err.println(enumRefName);
                        EnumGroup.Value value = eg.getValue(enumRefName);
                        if(value==null)
                            throw new IllegalStateException("The field: " + ef.name + " contains an unknown enum value: " + enumRefName);
                    }
                    
                    fields.put(ef.name, ef);
                    continue;
                }
                
                throw new IllegalStateException("unknown field: " + packageName + "." + refName);
            }
        }
        sortedFields.addAll(fields.values());
        Collections.sort(sortedFields);
        for(Message m : nestedMessages.values())
        {
            //System.err.println("nested start");
            m.resolveReferences();
            //System.err.println("nested end");
        }
    }
    
    static MessageField newMessageField(Message message, Field.Reference fr)
    {
        MessageField mf = new MessageField(message);
        mf.packable = false;
        copy(fr, mf);
        return mf;
    }
    
    static EnumField newEnumField(EnumGroup enumGroup, Field.Reference fr)
    {
        EnumField ef = new EnumField(enumGroup);
        ef.packable = true;
        copy(fr, ef);
        return ef;
    }
    
    static void copy(Field<?> from, Field<?> to)
    {
        to.name = from.name;
        to.number = from.number;
        to.modifier = from.modifier;
    }

}
