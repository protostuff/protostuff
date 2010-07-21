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

package com.dyuproject.protostuff.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Represents the message defined in the {@link Proto}.
 *
 * @author David Yu
 * @created Dec 19, 2009
 */
public class Message implements HasName, HasFields
{
    
    String name;
    Proto proto;
    Message parentMessage;
    final LinkedHashMap<String, Message> nestedMessages = new LinkedHashMap<String, Message>();
    final LinkedHashMap<String,EnumGroup> nestedEnumGroups = new LinkedHashMap<String,EnumGroup>();
    final LinkedHashMap<String,Field<?>> fields = new LinkedHashMap<String,Field<?>>();
    final ArrayList<Extension> nestedExtensions = new ArrayList<Extension>();
    final ArrayList<Field<?>> sortedFields = new ArrayList<Field<?>>();
    // code generator helpers
    boolean bytesFieldPresent, repeatedFieldPresent, requiredFieldPresent, extensible;
    boolean requiredFieldPresentOnCurrent;
    
    final ArrayList<int[]> extensionRanges = new ArrayList<int[]>();
    final LinkedHashMap<Integer, Field<?>> extensions = new LinkedHashMap<Integer,Field<?>>();
    
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
            proto = p = parentMessage.getProto();
        return p;
    }
    
    public Message getRootMessage()
    {
        return parentMessage==null ? null : getRoot(parentMessage);
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
    
    public boolean isDescendant(Message other)
    {
        if(parentMessage==null)
            return false;
        return parentMessage == other || parentMessage.isDescendant(other);
    }
    
    public Message getDescendant(String name)
    {
        if(parentMessage==null)
            return null;
        
        return name.equals(parentMessage.name) ? parentMessage : parentMessage.getDescendant(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Field<?>> T getField(String name, Class<T> typeClass)
    {
        return (T)fields.get(name);
    }
    
    public void addField(Field<?> field)
    {
        if(field.number<1)
        {
            throw new IllegalArgumentException("Invalid field number " + field.number 
                    + " from field " + field.name);
        }
        fields.put(field.name, field);
    }
    
    public void defineExtensionRange(int first, int last)
    {
        extensionRanges.add(new int[]{ first, last });
        this.extensible = true;
    }

    public void addNestedExtension(Extension extension) 
    {
        this.nestedExtensions.add(extension);
    }

    public Collection<Extension> getNestedExtensions()
    {
        return this.nestedExtensions;
    }
    
    public void extend(Extension extension)
    {
        if (isExtensible() == false)
        {
            throw new IllegalStateException("Message " + getFullName()
                    + " does not define extension range.");
        }

        for (Field<?> field : extension.getFields())
        {
            int number = field.getNumber();
            boolean inRange = false;
            for (int[] range : extensionRanges)
            {
                if (number >= range[0] && number <= range[1])
                {
                    inRange = true;
                    break;
                }
            }
            if (inRange == false)
            {
                throw new IllegalStateException("Extension '" + field.getName()
                        + "' is outside extension range.");
            }
            if (this.extensions.containsKey(number))
            {
                throw new IllegalStateException("Extension already defined for number '" + number
                        + "'.");
            }
            this.extensions.put(number, field);
        }
    }
    
    public String toString()
    {
        return new StringBuilder()
            .append('{')
            .append("name:").append(name)
            .append(',').append("enumGroups:").append(nestedEnumGroups.values())
            .append(',').append("extensions:").append(nestedExtensions)
            .append(',').append("fields:").append(fields.values())
            .append('}')
            .toString();
    }
    
    public String getFullName()
    {
        StringBuilder buffer = new StringBuilder();
        resolveFullName(this, buffer);
        return buffer.toString();
    }
    
    public String getRelativeName()
    {
        StringBuilder buffer = new StringBuilder();
        resolveRelativeName(this, buffer, null);
        return buffer.toString();
    }
    
    public boolean isRepeatedFieldPresent()
    {
        return repeatedFieldPresent;
    }
    
    public boolean isBytesFieldPresent()
    {
        return bytesFieldPresent;
    }
    
    public boolean isRequiredFieldPresent()
    {
        return requiredFieldPresent;
    }
    
    public boolean isRequiredFieldPresentOnCurrent()
    {
        return requiredFieldPresentOnCurrent;
    }
    
    public boolean isExtensible() 
    {
        return extensible;
    }
    
    // post parse
    
    void resolveReferences(Message root)
    {
        Proto p = getProto();
        for(Field<?> f : fields.values())
        {
            f.owner = this;
            if(!root.repeatedFieldPresent && f.isRepeated())
                root.repeatedFieldPresent = true;
            
            if(!root.requiredFieldPresent && f.isRequired())
                root.requiredFieldPresent = true;
            
            if(!requiredFieldPresentOnCurrent && f.isRequired())
                requiredFieldPresentOnCurrent = true;
            
            if(f instanceof Field.Bytes)
            {
                if(!root.bytesFieldPresent)
                    root.bytesFieldPresent = true;
            }
            else if(f instanceof Field.String)
            {    
                if(!root.bytesFieldPresent && f.defaultValue!=null)
                    root.bytesFieldPresent = true;
            }
            else if(f instanceof Field.Reference)
            {
                Field.Reference fr = (Field.Reference)f;
                String refName = fr.refName;
                String packageName = fr.packageName;
                if(packageName==null)
                {
                    // field reference to owner (self).
                    if(name.equals(refName))
                    {
                        MessageField mf = newMessageField(this, fr, this);
                        fields.put(mf.name, mf);
                        continue;
                    }
                    
                    Message msg = findMessageFrom((Message)fr.hasFields, refName);
                    if(msg!=null 
                            || (msg=p.getMessage(refName))!=null 
                            || (msg=getImportedMessage(refName, p)) != null)
                    {
                        MessageField mf = newMessageField(msg, fr, this);
                        fields.put(mf.name, mf);
                        continue;
                    }

                    EnumGroup eg = findEnumGroupFrom((Message)fr.hasFields, refName);
                    if(eg!=null 
                            || (eg=p.getEnumGroup(refName))!=null 
                            || (eg=getImportedEnumGroup(refName, p)) != null)
                    {
                        EnumField ef = newEnumField(eg, fr, this);
                        fields.put(ef.name, ef);
                        continue;
                    }
                    
                    throw new IllegalStateException("unknown field: " + refName);
                }

                int dotIdx = packageName.indexOf('.');
                if(dotIdx==-1)
                {
                    // could be a nested message/enum
                    Message msg = findMessageFrom((Message)fr.hasFields, packageName);
                    if(msg!=null 
                            || (msg=p.getMessage(packageName))!=null 
                            || (msg=getImportedMessage(packageName, p)) != null)
                    {
                        Message nestedMsg = msg.getNestedMessage(refName);
                        if(nestedMsg!=null)
                        {
                            MessageField mf = newMessageField(nestedMsg, fr, this);
                            fields.put(mf.name, mf);
                            continue;
                        }
                        EnumGroup eg = msg.getNestedEnumGroup(refName);
                        if(eg!=null)
                        {
                            EnumField ef = newEnumField(eg, fr, this);
                            fields.put(ef.name, ef);
                            continue;
                        }
                    }
                }
                else
                {
                    final String name = packageName.substring(0, dotIdx);
                    // could be a local message
                    Message msg = resolveLocalMessage(name, packageName, dotIdx, proto);
                    if(msg != null)
                    {
                        Message nestedMsg = msg.getNestedMessage(refName);
                        if(nestedMsg!=null)
                        {
                            MessageField mf = newMessageField(nestedMsg, fr, this);
                            fields.put(mf.name, mf);
                            continue;
                        }
                        EnumGroup eg = msg.getNestedEnumGroup(refName);
                        if(eg!=null)
                        {
                            EnumField ef = newEnumField(eg, fr, this);
                            fields.put(ef.name, ef);
                            continue;
                        }
                    }
                    
                    // could be a foreign (imported) message
                    msg = resolveImportedMessage(name, packageName, dotIdx, proto);
                    if(msg != null)
                    {
                        Message nestedMsg = msg.getNestedMessage(refName);
                        if(nestedMsg!=null)
                        {
                            MessageField mf = newMessageField(nestedMsg, fr, this);
                            fields.put(mf.name, mf);
                            continue;
                        }
                        EnumGroup eg = msg.getNestedEnumGroup(refName);
                        if(eg!=null)
                        {
                            EnumField ef = newEnumField(eg, fr, this);
                            fields.put(ef.name, ef);
                            continue;
                        }
                    }

                    // try to resolve the fully referenced message (prefixed with package).
                    msg = resolveFullyReferencedMessage(packageName, dotIdx+1, proto);
                    if(msg != null)
                    {
                        Message nestedMsg = msg.getNestedMessage(refName);
                        if(nestedMsg!=null)
                        {
                            MessageField mf = newMessageField(nestedMsg, fr, this);
                            fields.put(mf.name, mf);
                            continue;
                        }
                        EnumGroup eg = msg.getNestedEnumGroup(refName);
                        if(eg!=null)
                        {
                            EnumField ef = newEnumField(eg, fr, this);
                            fields.put(ef.name, ef);
                            continue;
                        }
                    }
                }
                
                Proto proto = p.getImportedProto(packageName);
                if(proto==null)
                    throw new IllegalStateException("unknown field: " + packageName + "." + refName);

                Message msg = proto.getMessage(refName);
                if(msg!=null)
                {
                    MessageField mf = newMessageField(msg, fr, this);
                    fields.put(mf.name, mf);
                    continue;
                }
                
                EnumGroup eg = proto.getEnumGroup(refName);
                if(eg!=null)
                {
                    EnumField ef = newEnumField(eg, fr, this);
                    fields.put(ef.name, ef);
                    continue;
                }
                
                throw new IllegalStateException("unknown field: " + packageName + "." + refName);
            }
        }
        sortedFields.addAll(fields.values());
        Collections.sort(sortedFields);
        
        for(Extension extension : this.nestedExtensions)
            extension.resolveReferences();
        
        for(Message m : nestedMessages.values())
            m.resolveReferences(root);
    }
    
    static MessageField newMessageField(Message message, Field.Reference fr, Message owner)
    {
        MessageField mf = new MessageField(message);
        mf.owner = owner;
        mf.packable = false;
        copy(fr, mf);
        //System.err.println(owner.getRelativeName() + "." + mf.name +": " + mf.getJavaType());
        return mf;
    }
    
    static EnumField newEnumField(EnumGroup enumGroup, Field.Reference fr, Message owner)
    {
        EnumField ef = new EnumField(enumGroup);
        ef.owner = owner;
        ef.packable = true;
        String refName = (String)fr.getDefaultValue();
        if(refName == null)
            ef.defaultValue = enumGroup.getFirstValue();
        else
        {
            ef.defaultValue = enumGroup.getValue(refName);
            if(ef.defaultValue == null)
            {
                throw new IllegalStateException("The field: " + ef.name + 
                        " contains an unknown enum value: " + refName);
            }
        }
        copy(fr, ef);
        //System.err.println(owner.getRelativeName() + "." + ef.name +": " + ef.getJavaType());
        return ef;
    }
    
    static void copy(Field<?> from, Field<?> to)
    {
        to.name = from.name;
        to.number = from.number;
        to.modifier = from.modifier;
    }
    
    static Message findMessageFrom(Message message, String name)
    {
        Message m = message.getNestedMessage(name);
        if(m==null && message.isNested())
            return findMessageFrom(message.parentMessage, name);
        return m;
    }
    
    static EnumGroup findEnumGroupFrom(Message message, String name)
    {
        EnumGroup eg = message.getNestedEnumGroup(name);
        if(eg==null && message.isNested())
            return findEnumGroupFrom(message.parentMessage, name);
        return eg;
    }
    
    static void resolveFullName(Message message, StringBuilder buffer)
    {
        buffer.insert(0, message.name).insert(0, '.');
        if(message.isNested())
            resolveFullName(message.parentMessage, buffer);
        else
            buffer.insert(0, message.getProto().getJavaPackageName());
    }
    
    static void resolveRelativeName(Message message, StringBuilder buffer, Message descendant)
    {
        buffer.insert(0, message.name);
        if(message.parentMessage!=null)
        {
            if(message.parentMessage!=descendant)
            {
                buffer.insert(0, '.');
                resolveRelativeName(message.parentMessage, buffer, descendant);
            }
        }
    }
    
    static void computeName(Message message, Message owner, StringBuilder buffer)
    {
        if(owner==message || message.parentMessage==owner || owner.isDescendant(message))
            buffer.append(message.name);
        else if(message.isDescendant(owner))
            Message.resolveRelativeName(message, buffer, owner);
        else if(message.getProto().getJavaPackageName().equals(owner.getProto().getJavaPackageName()))
            buffer.append(message.getRelativeName());
        else
            buffer.append(message.getFullName());
    }
    
    static Message getRoot(Message parent)
    {
        return parent.parentMessage==null ? parent: getRoot(parent.parentMessage);
    }
    
    private static Message resolveLocalMessage(String name, String packageName, 
            int dotIdx, Proto proto)
    {
        Message m = proto.getMessage(name);
        if(m != null && (m=resolveNestedMessage(packageName, dotIdx+1, m)) != null)
            return m;
        
        if(!name.equals(proto.getPackageName()))
            return null;

        // could be a full reference to a local message
        int idx = packageName.indexOf('.', dotIdx+1);
        if(idx == -1)
        {
            // at end
            return proto.getMessage(packageName.substring(dotIdx+1));
        }

        // check if local message
        String n = packageName.substring(dotIdx+1, idx);
        m = proto.getMessage(n);
        return m == null ? null : resolveNestedMessage(packageName, idx+1, m);
    }
    
    private static Message resolveImportedMessage(String name, String packageName, 
            int dotIdx, Proto proto)
    {
        Message m = getImportedMessage(name, proto);
        if(m != null && (m=resolveNestedMessage(packageName, dotIdx+1, m)) != null)
            return m;
        
        final Proto importedProto = proto.getImportedProto(name);
        if(importedProto == null)
            return null;
        
        // could be a full reference to an imported message
        int idx = packageName.indexOf('.', dotIdx+1);
        if(idx == -1)
        {
            // at end
            return importedProto.getMessage(packageName.substring(dotIdx+1));
        }

        // check if local message of imported proto
        String n = packageName.substring(dotIdx+1, idx);
        m = importedProto.getMessage(n);
        return m == null ? null : resolveNestedMessage(packageName, idx+1, m);
    }
    
    private static Message resolveFullyReferencedMessage(String packageName, int start, 
            Proto proto)
    {
        for(int idx = packageName.indexOf('.', start); idx != -1; 
                start = idx+1, idx = packageName.indexOf('.', start))
        {
            String name = packageName.substring(0, idx);
            Proto p = name.equals(proto.getPackageName()) ? proto : proto.getImportedProto(name);
            if(p != null)
            {
                int i = packageName.indexOf('.', idx+1);
                if(i == -1)
                {
                    // last
                    return p.getMessage(packageName.substring(idx+1));
                }
                
                Message m = p.getMessage(packageName.substring(idx+1, i));
                if(m != null && (m=resolveNestedMessage(packageName, i+1, m)) != null)
                    return m;
            }
        }
        
        return null;
    }
    
    private static Message resolveNestedMessage(String packageName, int start, Message parent)
    {
        Message m = parent;
        for(int idx = packageName.indexOf('.', start); idx != -1; 
                start = idx+1, idx = packageName.indexOf('.', start))
        {
            String name = packageName.substring(start, idx);
            if((m=m.getNestedMessage(name)) == null)
                return null;
        }
        
        return m.getNestedMessage(packageName.substring(start));
    }
    
    static Proto resolveProto(String packageName, Proto proto)
    {
        return packageName.equals(proto.getPackageName()) ? proto : 
            proto.getImportedProto(packageName);
    }
    
    static Message getImportedMessage(String name, Proto proto)
    {
        for(Proto p : proto.getImportedProtos())
        {
            Message m = p.getMessage(name);
            if(m != null)
                return m;
        }
        return null;
    }
    
    static EnumGroup getImportedEnumGroup(String name, Proto proto)
    {
        for(Proto p : proto.getImportedProtos())
        {
            EnumGroup eg = p.getEnumGroup(name);
            if(eg != null)
                return eg;
        }
        return null;
    }
    
    /**
     * Finds a message from the provided {@link Proto} or any of its imports.
     */
    public static Message findMessage(final String refName, final String packageName, 
            final Proto proto)
    {
        if(packageName == null)
        {
            // could be a local message
            Message m = proto.getMessage(refName);
            if(m != null)
                return m;
            
            // could be a foreign (imported) message
            for(Proto p : proto.getImportedProtos())
            {
                m = p.getMessage(refName);
                if(m != null)
                    return m;
            }
            
            return null;
        }
        
        final int dotIdx = packageName.indexOf('.');
        if(dotIdx == -1)
        {
            // could be a local nested message
            Message m = proto.getMessage(packageName);
            if(m != null && (m=m.getNestedMessage(refName)) != null)
                return m;
            
            // could be a foreign (imported) nested message
            m = Message.getImportedMessage(packageName, proto);
            if(m != null && (m=m.getNestedMessage(refName)) != null)
                return m;

            // could be a full reference to a local or foreign (imported) message
            Proto p = Message.resolveProto(packageName, proto);
            if(p != null && (m=p.getMessage(refName))!= null)
                return m;
            
            return null;
        }
        
        final String name = packageName.substring(0, dotIdx);
        
        // could be a local message
        Message m = Message.resolveLocalMessage(name, packageName, dotIdx, proto);
        if(m != null && (m=m.getNestedMessage(refName)) != null)
            return m;
        
        // could be a foreign (imported) message
        m = Message.resolveImportedMessage(name, packageName, dotIdx, proto);
        if(m != null && (m=m.getNestedMessage(refName)) != null)
            return m;

        // try to resolve the fully referenced message (prefixed with package).
        m = Message.resolveFullyReferencedMessage(packageName, dotIdx+1, proto);
        if(m != null && (m=m.getNestedMessage(refName)) != null)
            return m;
        
        // last resort
        Proto importedProto = proto.getImportedProto(packageName);
        if(importedProto != null && (m=importedProto.getMessage(refName)) != null)
            return m;
        
        return null;
    }

}
