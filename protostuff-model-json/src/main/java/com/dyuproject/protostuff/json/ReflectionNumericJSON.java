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

import org.codehaus.jackson.JsonFactory;

import com.dyuproject.protostuff.json.ReflectionConvertor.Field;
import com.dyuproject.protostuff.model.Model;
import com.dyuproject.protostuff.model.ModelMeta.Factory;

/**
 * @author David Yu
 * @created Oct 2, 2009
 */

public class ReflectionNumericJSON extends ReflectionJSON
{
    
    public ReflectionNumericJSON(Class<?>[] moduleClasses)
    {
        super(moduleClasses);
    }
    
    public ReflectionNumericJSON(Factory modelMetaFactory, 
            Class<?>[] moduleClasses)
    {
        super(modelMetaFactory,moduleClasses);
    }

    public ReflectionNumericJSON(JsonFactory jsonFactory, Factory modelMetaFactory, 
            Class<?>[] moduleClasses)
    {
        super(jsonFactory,modelMetaFactory,moduleClasses);
    }
    
    protected Field getField(String name, Model<Field> model) throws IOException
    {
        return model.getProperty(Integer.parseInt(name));
    }
    
    protected String getFieldName(Field field)
    {
        return String.valueOf(field.getPropertyMeta().getNumber());
    }

}
