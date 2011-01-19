//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.runtime;

import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;
import com.dyuproject.protostuff.runtime.RuntimeSchema.HasSchema;

/**
 * A runtime message field that lazily loads the schema to support cyclic dependencies. 
 *
 * @author David Yu
 * @created Jan 19, 2011
 */
public abstract class RuntimeMessageField<T,P> extends Field<T>
{
    
    /**
     * The class of the message field.
     */
    public final Class<P> typeClass;
    
    final HasSchema<P> hasSchema;
    
    private Pipe.Schema<P> pipeSchema;
    
    public RuntimeMessageField(Class<P> typeClass, HasSchema<P> hasSchema, 
            FieldType type, int number, String name, boolean repeated)
    {
        super(type, number, name, repeated);
        this.typeClass = typeClass;
        this.hasSchema = hasSchema;
    }
    
    /**
     * Returns the schema.
     */
    public Schema<P> getSchema()
    {
        return hasSchema.getSchema();
    }
    
    /**
     * Returns the lazy initialized pipe schema.
     */
    @SuppressWarnings("unchecked")
    public Pipe.Schema<P> getPipeSchema()
    {
        Pipe.Schema<P> pipeSchema = this.pipeSchema;
        if(pipeSchema == null)
        {
            synchronized(this)
            {
                if((pipeSchema = this.pipeSchema) == null)
                {
                    this.pipeSchema = pipeSchema = (Pipe.Schema<P>)resolvePipeSchema(
                            hasSchema.getSchema(), typeClass);
                }
            }
        }
        return pipeSchema;
    }
    
    private static Pipe.Schema<?> resolvePipeSchema(Schema<?> schema, Class<?> clazz)
    {
        if(Message.class.isAssignableFrom(clazz))
        {
            try
            {
                // use the pipe schema of code-generated messages if available.
                java.lang.reflect.Method m = clazz.getDeclaredMethod("getPipeSchema", 
                        new Class[]{});
                return (Pipe.Schema<?>)m.invoke(null, new Object[]{});
            }
            catch(Exception e)
            {
                // ignore
            }
        }
        
        if(MappedSchema.class.isAssignableFrom(schema.getClass()))
            return ((MappedSchema<?>)schema).pipeSchema;
        
        throw new RuntimeException("No pipe schema for: " + clazz);
    }

}
