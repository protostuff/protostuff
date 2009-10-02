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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.dyuproject.protostuff.model.BuilderPropertyAccessor;
import com.dyuproject.protostuff.model.DefaultProperty;
import com.dyuproject.protostuff.model.LiteRuntime;
import com.dyuproject.protostuff.model.MessagePropertyAccessor;
import com.dyuproject.protostuff.model.Model;
import com.dyuproject.protostuff.model.ModelMeta;
import com.dyuproject.protostuff.model.ParamType;
import com.dyuproject.protostuff.model.Property;
import com.dyuproject.protostuff.model.PropertyMeta;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

/**
 * @author David Yu
 * @created Oct 1, 2009
 */

public class DefaultProtobufConvertor implements ProtobufConvertor<MessageLite, Builder>
{
    
    static final Property.Factory<Field> PROPERTY_FACTORY = new Property.Factory<Field>()
    {

        public Field create(PropertyMeta propertyMeta)
        {
            if(propertyMeta.isMessage())
            {
                return propertyMeta.isRepeated() ? new RepeatedMessageField(propertyMeta) : 
                    new MessageField(propertyMeta);
            }
            
            return propertyMeta.isRepeated() ? new RepeatedInlineField(propertyMeta) :
                new InlineField(propertyMeta);
        }

        public Class<Field> getType()
        {
            return Field.class;
        }
    };
    
    private final Model<Field> _model;
    private final ProtobufConvertorFactory<DefaultProtobufConvertor> _factory;
    
    public DefaultProtobufConvertor(ModelMeta modelMeta, 
            ProtobufConvertorFactory<DefaultProtobufConvertor> factory)
    {
        _model = new Model<Field>(modelMeta, PROPERTY_FACTORY);
        _factory = factory;
        for(Field f : _model.getProperties())
        {
            f._factory = _factory;
            f._convertor = this;
        }
    }    
    
    protected Field getField(String name, Model<Field> model)
    {
        return model.getProperty(name);
    }
    
    protected String getFieldName(Field field)
    {
        return field.getPropertyMeta().getName();
    }

    public Builder parseFrom(JsonParser parser) throws IOException
    {        
        Builder builder = _model.getModelMeta().getPrototype().newBuilderForType();
        mergeFrom(parser, builder);        
        return builder;
    }
    
    public void mergeFrom(JsonParser parser, Builder builder) throws IOException
    {
        for(JsonToken t = parser.nextToken(); t!=JsonToken.END_OBJECT; t=parser.nextToken())
        {
            if(t!=JsonToken.FIELD_NAME)
            {
                throw new IOException("Expected token: $field: but was " + 
                        parser.getCurrentToken() + " on message: " + 
                        _model.getModelMeta().getMessageClass());
            }
            String name = parser.getCurrentName();
            Field field = getField(name, _model);
            if(field==null)
                throw new IOException("unknown field: " + name);
            
            field.readFrom(parser, builder);
        }
    }

    public void generateTo(JsonGenerator generator, MessageLite message) throws IOException
    {
        generator.writeStartObject();

        for(Field f : _model.getProperties())
        {
            f.writeTo(generator, message, getFieldName(f));
        }
        generator.writeEndObject();        
    }
    
    public static class Factory implements ProtobufConvertorFactory<DefaultProtobufConvertor>
    {
        
        private final HashMap<String,DefaultProtobufConvertor> _convertors = 
            new HashMap<String,DefaultProtobufConvertor>();
        
        private final ArrayList<Class<?>> _modules = new ArrayList<Class<?>>();
        
        private final ModelMeta.Factory _modelMetaFactory;

        public Factory(Class<?>[] moduleClasses)
        {
            this(LiteRuntime.MODEL_META_FACTORY, moduleClasses);
        }
        
        public Factory(ModelMeta.Factory modelMetaFactory, Class<?>[] moduleClasses)
        {
            _modelMetaFactory = modelMetaFactory;
            for(Class<?> c : moduleClasses)
                check(c);
        }

        protected void check(Class<?> c)
        {
            if(MessageLite.class.isAssignableFrom(c))
                addModule(c.getDeclaringClass());
            else if(c.getDeclaringClass()!=null)
                check(c.getDeclaringClass());
            else
            {
                Class<?>[] declaredClasses = c.getDeclaredClasses();
                if(declaredClasses.length==0)
                {
                    // could be a builder
                    if(c.getDeclaringClass()!=null)
                        check(c.getDeclaringClass());
                }
                else
                {
                    // search twice
                    boolean continueSearch = true;
                    for(int i=declaredClasses.length; i-->0;)
                    {
                        if(MessageLite.class.isAssignableFrom(declaredClasses[i]))
                        {
                            addModule(c);
                            break;
                        }
                        else if(continueSearch)
                            continueSearch = false;
                        else
                            break;
                    }
                }
            }            
        }
        
        @SuppressWarnings("unchecked")
        protected void addModule(Class<?> moduleClass)
        {
            if(_modules.contains(moduleClass))
                return;            
            
            int size = _convertors.size();            
            
            Class<?>[] messageClasses = moduleClass.getDeclaredClasses();
            for(int i=0; i<messageClasses.length; i++)
            {
                if(MessageLite.class.isAssignableFrom(messageClasses[i]))
                {
                    Class<? extends AbstractMessageLite> clazz = (Class<? extends AbstractMessageLite>)messageClasses[i];
                    _convertors.put(clazz.getName(), new DefaultProtobufConvertor(
                            _modelMetaFactory.create(clazz), this));
                }
            }
            
            if(size<_convertors.size())
                _modules.add(moduleClass);
        }

        public <T extends MessageLite, B extends Builder> DefaultProtobufConvertor get(Class<?> messageType)
        {            
            return _convertors.get(messageType.getName());
        }
        
    }
    
    static abstract class Field extends DefaultProperty implements ProtobufField<MessageLite,Builder>
    {
        
        protected ProtobufConvertorFactory<DefaultProtobufConvertor> _factory;
        protected DefaultProtobufConvertor _convertor;

        public Field(PropertyMeta propertyMeta)
        {
            super(propertyMeta, new MessagePropertyAccessor(propertyMeta), 
                    new BuilderPropertyAccessor(propertyMeta)
            {            
                protected boolean set(Object builder, Object value, Method method)
                throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
                {
                    method.invoke(builder, value);
                    return true;
                }
            });
        }
        
    }

    static class MessageField extends Field
    {        

        public MessageField(PropertyMeta propertyMeta)
        {
            super(propertyMeta);
        }

        public void readFrom(JsonParser parser, Builder owner) throws IOException
        {
            if(parser.nextToken() != JsonToken.START_OBJECT)
            {
                throw new IOException("Expected token: { but was " + 
                        parser.getCurrentToken() + " on message: " + 
                        _propertyMeta.getMessageClass());
            }
            
            DefaultProtobufConvertor convertor = _factory.get(_messagePropertyAccessor.getTypeClass());
            if(convertor==null)
            {
                throw new IOException("Message not included: " + 
                        _messagePropertyAccessor.getTypeClass());
            }
            
            try
            {
                _builderPropertyAccessor.setValue(owner, convertor.parseFrom(parser).build());
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            }
            
        }

        public void writeTo(JsonGenerator generator, MessageLite owner, String fieldName) throws IOException
        {
            MessageLite message;
            try
            {
                message = (MessageLite)_messagePropertyAccessor.getValue(owner);
                if(message==null)
                    return;
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            }
            
            DefaultProtobufConvertor convertor = _factory.get(_messagePropertyAccessor.getTypeClass());
            if(convertor==null)
                throw new IOException("Message not included: " + message.getClass());
            
            generator.writeFieldName(fieldName);
            convertor.generateTo(generator, message);
        }
        
    }
    
    static class RepeatedMessageField extends Field
    {

        public RepeatedMessageField(PropertyMeta propertyMeta)
        {
            super(propertyMeta);
        }
        
        public void readFrom(JsonParser parser, Builder owner) throws IOException
        {
            if(parser.nextToken()!=JsonToken.START_ARRAY)
            {
                throw new IOException("Expected token: [ but was " + 
                        parser.getCurrentToken() + " on message: " + 
                        _propertyMeta.getMessageClass());
            }
            
            DefaultProtobufConvertor convertor = _factory.get(_messagePropertyAccessor.getTypeClass());
            if(convertor==null)
            {
                throw new IOException("Message not included: " + 
                        _messagePropertyAccessor.getTypeClass());
            }

            try
            {
                for(JsonToken t=parser.nextToken(); t!=JsonToken.END_ARRAY; t=parser.nextToken())
                {
                    if(t != JsonToken.START_OBJECT)
                    {
                        throw new IOException("Expected token: { but was " + 
                                parser.getCurrentToken() + " on " + _propertyMeta.getName()+ " of message: " + 
                                _propertyMeta.getMessageClass());
                    }
                    
                    _builderPropertyAccessor.setValue(owner, convertor.parseFrom(parser).build());
                }
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            }
        }
        
        @SuppressWarnings("unchecked")
        public void writeTo(JsonGenerator generator, MessageLite owner, String fieldName) throws IOException
        {
            try
            {
                List<MessageLite> messages = (List<MessageLite>)_messagePropertyAccessor.getValue(owner);
                if(messages!=null)
                {
                    DefaultProtobufConvertor convertor = _factory.get(_messagePropertyAccessor.getTypeClass());
                    if(convertor==null)
                    {
                        throw new IOException("Message not included: " + 
                                _messagePropertyAccessor.getTypeClass());
                    }
                    
                    generator.writeFieldName(fieldName);
                    generator.writeStartArray();
                    
                    for(MessageLite m : messages)
                        convertor.generateTo(generator, m);
                    
                    generator.writeEndArray();
                }
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            }
        }


        
    }
    
    static class InlineField extends Field
    {
        
        protected final InlineValue<?> _inlineValue;
        
        public InlineField(PropertyMeta propertyMeta)
        {
            super(propertyMeta);
            ParamType type = _messagePropertyAccessor.getParamType();
            _inlineValue = type.isPrimitive() ? InlineValue.getPrimitive(type.getJavaType()) : 
                InlineValue.EnumValue.create(_messagePropertyAccessor.getTypeClass());
        }

        public void readFrom(JsonParser parser, Builder builder) throws IOException
        {
            try
            {
                parser.nextToken();
                _builderPropertyAccessor.setValue(builder, _inlineValue.readFrom(parser));
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            }
            
        }

        public void writeTo(JsonGenerator generator, MessageLite message, String fieldName) throws IOException
        {
            try
            {
                Object value = _messagePropertyAccessor.getValue(message);
                if(value!=null)
                {
                    generator.writeFieldName(fieldName);
                    _inlineValue.writeObjectTo(generator, value);
                }
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            }            
        }
        
    }
    
    static class RepeatedInlineField extends Field
    {
        
        protected final InlineValue<?> _inlineValue;

        public RepeatedInlineField(PropertyMeta propertyMeta)
        {
            super(propertyMeta);
            ParamType type = _messagePropertyAccessor.getParamType();
            _inlineValue = type.isPrimitive() ? InlineValue.getPrimitive(type.getJavaType()) : 
                InlineValue.EnumValue.create(_messagePropertyAccessor.getTypeClass());
        }

        public void readFrom(JsonParser parser, Builder owner) throws IOException
        {
            if(parser.nextToken()!=JsonToken.START_ARRAY)
            {
                throw new IOException("Expected token: [ but was " + 
                        parser.getCurrentToken() + " on " + _propertyMeta.getName() + " of message: " + 
                        _propertyMeta.getMessageClass());
            }

            try
            {
                for(JsonToken t = parser.nextToken(); t!=JsonToken.END_ARRAY; t=parser.nextToken())
                    _builderPropertyAccessor.setValue(owner, _inlineValue.readFrom(parser));
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            }
            
        }

        public void writeTo(JsonGenerator generator, MessageLite owner, String fieldName) throws IOException
        {
            try
            {
                List<?> values = (List<?>)_messagePropertyAccessor.getValue(owner);
                if(values!=null)
                {
                    generator.writeFieldName(fieldName);
                    generator.writeStartArray();
                    
                    for(Object value : values)
                        _inlineValue.writeObjectTo(generator, value);
                    
                    generator.writeEndArray();
                }
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                if(e.getCause() instanceof IOException)
                    throw (IOException)e.getCause();
                
                throw new IOException(e);
            } 
            
        }
        
    }


}
