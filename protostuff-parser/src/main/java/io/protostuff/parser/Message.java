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

package io.protostuff.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Represents the message defined in the {@link Proto}.
 * 
 * @author David Yu
 * @created Dec 19, 2009
 */
public class Message extends AnnotationContainer implements HasName, HasFields
{

    final String name;
    final Message parentMessage;
    final Proto proto;

    final LinkedHashMap<String, Message> nestedMessages = new LinkedHashMap<>();
    final LinkedHashMap<String, EnumGroup> nestedEnumGroups = new LinkedHashMap<>();
    final LinkedHashMap<String, Service> nestedServices = new LinkedHashMap<>();

    final LinkedHashMap<String, Field<?>> fields = new LinkedHashMap<>();
    final ArrayList<Extension> nestedExtensions = new ArrayList<>();
    final ArrayList<Field<?>> sortedFields = new ArrayList<>();

    final ArrayList<int[]> extensionRanges = new ArrayList<>();
    final LinkedHashMap<Integer, Field<?>> extensions = new LinkedHashMap<>();
    final LinkedHashMap<String, Object> standardOptions = new LinkedHashMap<>();
    final LinkedHashMap<String, Object> extraOptions = new LinkedHashMap<>();
    boolean extensible;

    // code generator helpers

    // for root message only
    boolean bytesFieldPresent, repeatedFieldPresent, requiredFieldPresent;
    boolean bytesOrStringDefaultValuePresent;
    private boolean byteBufferFieldPresent;

    // for every message
    boolean annotationPresentOnFields;
    int requiredFieldCount, repeatedFieldCount, singularFieldCount;
    int requiredMessageFieldCount, repeatedMessageFieldCount, singularMessageFieldCount;
    int requiredEnumFieldCount, repeatedEnumFieldCount, singularEnumFieldCount;
    int requiredBytesFieldCount, repeatedBytesFieldCount, singularBytesFieldCount;
    int requiredStringFieldCount, repeatedStringFieldCount, singularStringFieldCount;

    public Message(String name, Message parentMessage, Proto proto)
    {
        this.name = name;
        this.parentMessage = parentMessage;

        if (parentMessage != null)
        {
            this.proto = parentMessage.getProto();
            parentMessage.addNestedMessage(this);
        }
        else
        {
            this.proto = proto;
            proto.addMessage(this);
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Proto getProto()
    {
        return proto;
    }

    public Message getRootMessage()
    {
        return parentMessage == null ? null : getRoot(parentMessage);
    }

    public Message getParentMessage()
    {
        return parentMessage;
    }

    public boolean isNested()
    {
        return parentMessage != null;
    }

    public boolean hasNestedMessages()
    {
        return !nestedMessages.isEmpty();
    }

    public boolean hasNestedEnumGroups()
    {
        return !nestedEnumGroups.isEmpty();
    }

    public LinkedHashMap<String, Message> getNestedMessageMap()
    {
        return nestedMessages;
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
        if (nestedMessages.put(message.name, message) != null)
        {
            throw err("Duplicate nested message: " +
                    message.name + " from message: " + name, getProto());
        }
    }

    /* ================================================== */

    public LinkedHashMap<String, EnumGroup> getNestedEnumGroupMap()
    {
        return nestedEnumGroups;
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
        if (nestedEnumGroups.put(enumGroup.name, enumGroup) != null)
        {
            throw err("Duplicate nested enum: " +
                    enumGroup.name + " from message: " + name, getProto());
        }
    }

    /* ================================================== */

    public LinkedHashMap<String, Service> getNestedServiceMap()
    {
        return nestedServices;
    }

    public Collection<Service> getNestedServices()
    {
        return nestedServices.values();
    }

    public Service getNestedService(String name)
    {
        return nestedServices.get(name);
    }

    void addNestedService(Service service)
    {
        if (nestedServices.put(service.name, service) != null)
        {
            throw err("Duplicate nested service: " +
                    service.name + " from message: " + name, getProto());
        }
    }

    /* ================================================== */

    public LinkedHashMap<String, Field<?>> getFieldMap()
    {
        return fields;
    }

    @Override
    public List<Field<?>> getFields()
    {
        return sortedFields;
    }

    @Override
    public Field<?> getField(String name)
    {
        return fields.get(name);
    }

    public boolean isDescendant(Message other)
    {
        if (parentMessage == null)
            return false;
        return parentMessage == other || parentMessage.isDescendant(other);
    }

    public Message getDescendant(String name)
    {
        if (parentMessage == null)
            return null;

        return name.equals(parentMessage.name) ? parentMessage : parentMessage.getDescendant(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Field<?>> T getField(String name, Class<T> typeClass)
    {
        return (T) fields.get(name);
    }

    @Override
    public void addField(Field<?> field)
    {
        if (field.number < 1)
        {
            throw new IllegalArgumentException("Invalid field number " + field.number
                    + " from field " + field.name);
        }

        if (fields.put(field.name, field) != null)
            throw err("Duplicate message field: " + field.name, getProto());
    }

    public void defineExtensionRange(int first, int last)
    {
        extensionRanges.add(new int[] { first, last });
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
            throw err("Message " + getFullName()
                    + " does not define extension range", getProto());
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
                throw err("Extension '" + field.getName()
                        + "' is outside extension range", getProto());
            }
            if (this.extensions.containsKey(number))
            {
                throw err("Extension already defined for number '" + number
                        + "'", getProto());
            }
            this.extensions.put(number, field);
        }
    }

    @Override
    public void putStandardOption(String key, Object value)
    {
        putExtraOption(key, value);
        standardOptions.put(key, value);
    }

    @Override
    public void putExtraOption(String key, Object value)
    {
        if (extraOptions.put(key, value) != null)
            throw err("Duplicate message option: " + key, getProto());
    }

    public LinkedHashMap<String, Object> getStandardOptions()
    {
        return standardOptions;
    }

    public Object getStandardOption(String key)
    {
        return standardOptions.get(key);
    }

    public LinkedHashMap<String, Object> getExtraOptions()
    {
        return extraOptions;
    }

    @SuppressWarnings("unchecked")
    public <V> V getExtraOption(java.lang.String key)
    {
        return (V) extraOptions.get(key);
    }

    public LinkedHashMap<String, Object> getO()
    {
        return getOptions();
    }

    @Override
    public LinkedHashMap<String, Object> getOptions()
    {
        return extraOptions;
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

    @Override
    public String getEnclosingNamespace()
    {
        return getFullName();
    }

    public String getFullName()
    {
        StringBuilder buffer = new StringBuilder();
        resolveFullName(this, buffer);
        return buffer.toString();
    }

    public String getJavaFullName()
    {
        StringBuilder buffer = new StringBuilder();
        resolveJavaFullName(this, buffer);
        return buffer.toString();
    }

    public String getRelativeName()
    {
        StringBuilder buffer = new StringBuilder();
        resolveRelativeName(this, buffer, null);
        return buffer.toString();
    }

    public boolean isExtensible()
    {
        return extensible;
    }

    // codegen helpers

    public boolean isAnnotationPresentOnFields()
    {
        return annotationPresentOnFields;
    }

    public boolean isRepeatedFieldPresent()
    {
        return repeatedFieldPresent;
    }

    public boolean isBytesFieldPresent()
    {
        return bytesFieldPresent;
    }

    public boolean isByteBufferFieldPresent()
    {
        return byteBufferFieldPresent;
    }

    public void setByteBufferFieldPresent(boolean v)
    {
        byteBufferFieldPresent = v;
    }

    public boolean isBytesOrStringDefaultValuePresent()
    {
        return bytesOrStringDefaultValuePresent;
    }

    public boolean isRequiredFieldPresent()
    {
        return requiredFieldPresent;
    }

    public boolean isRequiredFieldPresentOnCurrent()
    {
        return requiredFieldCount != 0;
    }

    // field count

    public int getFieldCount()
    {
        return fields.size();
    }

    public int getRequiredFieldCount()
    {
        return requiredFieldCount;
    }

    public int getRepeatedFieldCount()
    {
        return repeatedFieldCount;
    }

    public int getOptionalFieldCount()
    {
        return singularFieldCount - requiredFieldCount;
    }

    public int getSingularFieldCount()
    {
        return singularFieldCount;
    }

    // message field count

    public int getMessageFieldCount()
    {
        return repeatedMessageFieldCount + singularMessageFieldCount;
    }

    public int getRequiredMessageFieldCount()
    {
        return requiredMessageFieldCount;
    }

    public int getRepeatedMessageFieldCount()
    {
        return repeatedMessageFieldCount;
    }

    public int getOptionalMessageFieldCount()
    {
        return singularMessageFieldCount - requiredMessageFieldCount;
    }

    public int getSingularMessageFieldCount()
    {
        return singularMessageFieldCount;
    }

    // enum field count

    public int getEnumFieldCount()
    {
        return repeatedEnumFieldCount + singularEnumFieldCount;
    }

    public int getRequiredEnumFieldCount()
    {
        return requiredEnumFieldCount;
    }

    public int getRepeatedEnumFieldCount()
    {
        return repeatedEnumFieldCount;
    }

    public int getOptionalEnumFieldCount()
    {
        return singularEnumFieldCount - requiredEnumFieldCount;
    }

    public int getSingularEnumFieldCount()
    {
        return singularEnumFieldCount;
    }

    // bytes field count

    public int getBytesFieldCount()
    {
        return repeatedBytesFieldCount + singularBytesFieldCount;
    }

    public int getRequiredBytesFieldCount()
    {
        return requiredBytesFieldCount;
    }

    public int getRepeatedBytesFieldCount()
    {
        return repeatedBytesFieldCount;
    }

    public int getOptionalBytesFieldCount()
    {
        return singularBytesFieldCount - requiredBytesFieldCount;
    }

    public int getSingularBytesFieldCount()
    {
        return singularBytesFieldCount;
    }

    // string field count

    public int getStringFieldCount()
    {
        return repeatedStringFieldCount + singularStringFieldCount;
    }

    public int getRequiredStringFieldCount()
    {
        return requiredStringFieldCount;
    }

    public int getRepeatedStringFieldCount()
    {
        return repeatedStringFieldCount;
    }

    public int getOptionalStringFieldCount()
    {
        return singularStringFieldCount - requiredStringFieldCount;
    }

    public int getSingularStringFieldCount()
    {
        return singularStringFieldCount;
    }

    // scalar field count

    public int getScalarFieldCount()
    {
        return getFields().size() - repeatedMessageFieldCount - singularMessageFieldCount;
    }

    public int getScalarWithoutEnumFieldCount()
    {
        return getScalarFieldCount() - repeatedEnumFieldCount - singularEnumFieldCount;
    }

    // post parse

    void resolveReferences(Message root)
    {
        final Proto proto = getProto();
        final String fullName = getFullName();

        for (Field<?> f : fields.values())
        {
            f.owner = this;

            if (f.isRepeated())
            {
                repeatedFieldCount++;
                root.repeatedFieldPresent = true;
            }
            else
            {
                singularFieldCount++;

                if (f.isRequired())
                {
                    requiredFieldCount++;
                    root.requiredFieldPresent = true;
                }
            }

            if (!annotationPresentOnFields && !f.annotations.isEmpty())
                annotationPresentOnFields = true;

            if (f instanceof Field.Bytes)
            {
                if (f.isRepeated())
                    repeatedBytesFieldCount++;
                else
                {
                    singularBytesFieldCount++;

                    if (f.isRequired())
                        requiredBytesFieldCount++;
                }

                if (f.hasOption("ByteBuffer"))
                {
                    root.byteBufferFieldPresent = true;
                }

                if (!root.bytesFieldPresent)
                    root.bytesFieldPresent = true;
                if (!root.bytesOrStringDefaultValuePresent && f.defaultValue != null)
                    root.bytesOrStringDefaultValuePresent = true;
            }
            else if (f instanceof Field.String)
            {
                if (f.isRepeated())
                    repeatedStringFieldCount++;
                else
                {
                    singularStringFieldCount++;

                    if (f.isRequired())
                        requiredStringFieldCount++;
                }

                if (!root.bytesOrStringDefaultValuePresent && f.defaultValue != null)
                    root.bytesOrStringDefaultValuePresent = true;
            }
            else if (f instanceof Field.Reference)
            {
                Field.Reference fr = (Field.Reference) f;
                String refName = fr.refName;
                String packageName = fr.packageName;
                String fullRefName = (packageName == null ? refName : packageName + '.' + refName);

                HasName refObj = proto.findReference(fullRefName, fullName);
                if (refObj instanceof Message)
                {
                    MessageField mf = newMessageField((Message) refObj, fr, this);
                    fields.put(mf.name, mf);

                    if (mf.isRepeated())
                        repeatedMessageFieldCount++;
                    else
                    {
                        singularMessageFieldCount++;

                        if (mf.isRequired())
                            requiredMessageFieldCount++;
                    }

                    // references inside options
                    if (!mf.standardOptions.isEmpty())
                        proto.references.add(new ConfiguredReference(mf.standardOptions, mf.extraOptions, fullName));

                    continue;
                }

                if (refObj instanceof EnumGroup)
                {
                    EnumField ef = newEnumField((EnumGroup) refObj, fr, this);
                    fields.put(ef.name, ef);

                    if (ef.isRepeated())
                        repeatedEnumFieldCount++;
                    else
                    {
                        singularEnumFieldCount++;

                        if (ef.isRequired())
                            requiredEnumFieldCount++;
                    }

                    // references inside options
                    if (!ef.standardOptions.isEmpty())
                        proto.references.add(new ConfiguredReference(ef.standardOptions, ef.extraOptions, fullName));

                    continue;
                }

                throw err("unknown field: " + fullRefName, getProto());
            }

            // references inside options
            if (!f.standardOptions.isEmpty())
                proto.references.add(new ConfiguredReference(f.standardOptions, f.extraOptions, fullName));
        }
        sortedFields.addAll(fields.values());
        Collections.sort(sortedFields);

        for (Extension extension : this.nestedExtensions)
            extension.resolveReferences();

        for (Service s : nestedServices.values())
            s.resolveReferences();

        for (Message m : nestedMessages.values())
            m.resolveReferences(root);
    }

    void cacheFullyQualifiedNames()
    {
        Proto proto = getProto();
        proto.fullyQualifiedMessages.put(getFullName(), this);

        for (Message m : nestedMessages.values())
            m.cacheFullyQualifiedNames();
        for (EnumGroup eg : nestedEnumGroups.values())
            eg.cacheFullyQualifiedName();

        if (!standardOptions.isEmpty())
            proto.references.add(new ConfiguredReference(standardOptions, extraOptions, getFullName()));
    }

    static MessageField newMessageField(Message message, Field.Reference fr, Message owner)
    {
        MessageField mf = new MessageField(message);
        mf.owner = owner;
        mf.packable = false;
        copy(fr, mf);
        // System.err.println(owner.getRelativeName() + "." + mf.name +": " + mf.getJavaType());
        return mf;
    }

    static EnumField newEnumField(EnumGroup enumGroup, Field.Reference fr, Message owner)
    {
        EnumField ef = new EnumField(enumGroup);
        ef.owner = owner;
        ef.packable = true;
        String refName = (String) fr.getDefaultValue();
        if (refName == null)
            ef.defaultValue = enumGroup.getFirstValue();
        else
        {
            ef.defaultValueSet = true;
            ef.defaultValue = enumGroup.getValue(refName);
            if (ef.defaultValue == null)
            {
                throw err("The field: " + ef.name +
                        " contains an unknown enum value: " + refName, owner.getProto());
            }
        }
        copy(fr, ef);
        // System.err.println(owner.getRelativeName() + "." + ef.name +": " + ef.getJavaType());
        return ef;
    }

    static void copy(Field<?> from, Field<?> to)
    {
        to.name = from.name;
        to.number = from.number;
        to.modifier = from.modifier;
        to.addAnnotations(from.annotations, true);
        to.standardOptions.putAll(from.standardOptions);
        to.extraOptions.putAll(from.extraOptions);
    }

    static void resolveFullName(Message message, StringBuilder buffer)
    {
        buffer.insert(0, message.name).insert(0, '.');
        if (message.isNested())
            resolveFullName(message.parentMessage, buffer);
        else
            buffer.insert(0, message.getProto().getPackageName());
    }

    static void resolveJavaFullName(Message message, StringBuilder buffer)
    {
        buffer.insert(0, message.name).insert(0, '.');
        if (message.isNested())
            resolveFullName(message.parentMessage, buffer);
        else
            buffer.insert(0, message.getProto().getJavaPackageName());
    }

    static void resolveRelativeName(Message message, StringBuilder buffer, Message descendant)
    {
        buffer.insert(0, message.name);
        if (message.parentMessage != null)
        {
            if (message.parentMessage != descendant)
            {
                buffer.insert(0, '.');
                resolveRelativeName(message.parentMessage, buffer, descendant);
            }
        }
    }

    static void computeName(Message message, Message owner, StringBuilder buffer)
    {
        if (owner == message || message.parentMessage == owner || owner.isDescendant(message))
            buffer.append(message.name);
        else if (message.isDescendant(owner))
            Message.resolveRelativeName(message, buffer, owner);
        else if (message.getProto().getJavaPackageName().equals(owner.getProto().getJavaPackageName()))
            buffer.append(message.getRelativeName());
        else
            buffer.append(message.getJavaFullName());
    }

    static Message getRoot(Message parent)
    {
        return parent.parentMessage == null ? parent : getRoot(parent.parentMessage);
    }

}
