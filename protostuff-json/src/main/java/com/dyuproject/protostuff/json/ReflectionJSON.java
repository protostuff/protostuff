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
import java.util.ArrayList;
import java.util.HashMap;

import org.codehaus.jackson.JsonFactory;

import com.dyuproject.protostuff.json.ReflectionConvertor.Field;
import com.dyuproject.protostuff.model.LiteRuntime;
import com.dyuproject.protostuff.model.Model;
import com.dyuproject.protostuff.model.ModelMeta;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

/**
 * @author David Yu
 * @created Oct 2, 2009
 */

public class ReflectionJSON extends ProtobufJSON
{
    
    protected final HashMap<String,ReflectionConvertor> _convertors = 
        new HashMap<String,ReflectionConvertor>();
    
    protected final ArrayList<Class<?>> _modules = new ArrayList<Class<?>>();
    
    protected final ModelMeta.Factory _modelMetaFactory;
    
    public ReflectionJSON(Class<?>[] moduleClasses)
    {
        this(null, LiteRuntime.MODEL_META_FACTORY, moduleClasses);
    }
    
    public ReflectionJSON(ModelMeta.Factory modelMetaFactory, Class<?>[] moduleClasses)
    {
        this(null, modelMetaFactory, moduleClasses);
    }
    
    public ReflectionJSON(JsonFactory jsonFactory, ModelMeta.Factory modelMetaFactory,
            Class<?>[] moduleClasses)
    {
        super(jsonFactory);
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
                _convertors.put(clazz.getName(), newConvertor(_modelMetaFactory.create(clazz)));
            }
        }
        
        if(size<_convertors.size())
            _modules.add(moduleClass);
    }
    
    protected final ReflectionConvertor get(Class<?> messageType)
    {
        return _convertors.get(messageType.getName());
    }
    
    protected ReflectionConvertor newConvertor(ModelMeta modelMeta)
    {
        return new ReflectionConvertor(modelMeta, this);
    }

    /*
    public <T extends MessageLite, B extends Builder> LiteConvertor getConvertor(Class<?> messageType)
    {
        return _convertors.get(messageType.getName());
    }*/

    protected Field getField(String name, Model<Field> model) throws IOException
    {
        return model.getProperty(name);
    }
    
    protected String getFieldName(Field field)
    {
        return field.getPropertyMeta().getName();
    }

    @SuppressWarnings("unchecked")
    protected <T extends MessageLite, B extends Builder> ProtobufConvertor<T, B> getConvertor(Class<?> messageType)
    {        
        return (ProtobufConvertor<T, B>)_convertors.get(messageType.getName());
    }    


}
