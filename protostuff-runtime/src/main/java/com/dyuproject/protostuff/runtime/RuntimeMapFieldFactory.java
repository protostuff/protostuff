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

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.EnumMap;
import java.util.Map;

import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.MapSchema.MapWrapper;
import com.dyuproject.protostuff.MapSchema.MessageFactories;
import com.dyuproject.protostuff.MapSchema.MessageFactory;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;
import com.dyuproject.protostuff.runtime.RuntimeSchema.HasSchema;

/**
 * Static utility for creating runtime {@link java.util.Map} fields.
 *
 * @author David Yu
 * @created Jan 21, 2011
 */
final class RuntimeMapFieldFactory
{

    private RuntimeMapFieldFactory() {}
    
    private static final DerivativeSchema POLYMORPHIC_MAP_VALUE_SCHEMA = 
        new DerivativeSchema()
    {
        @SuppressWarnings("unchecked")
        protected void doMergeFrom(Input input, Schema<Object> derivedSchema, 
                Object owner) throws IOException
        {
            final Object value = derivedSchema.newMessage();
            
            // the owner will always be the MapWrapper
            ((MapWrapper<Object,Object>)owner).setValue(value);
            
            if(input instanceof GraphInput)
            {
                // update the actual reference.
                ((GraphInput)input).updateLast(value, owner);
            }
            
            derivedSchema.mergeFrom(input, value);
        }
    };
    
    private static final ObjectSchema OBJECT_MAP_VALUE_SCHEMA = 
        new ObjectSchema()
    {
        @SuppressWarnings("unchecked")
        protected void setValue(Object value, Object owner)
        {
            // the owner will always be the MapWrapper
            ((MapWrapper<Object,Object>)owner).setValue(value);
        }
    };
    
    private static <T> Field<T> createMapInlineKEnumV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final RuntimeFieldFactory<Object> inlineK, 
            final Class<Object> clazzV)
    {
        final EnumIO<?> eioV = EnumIO.get(clazzV);
        
        return new RuntimeMapField<T,Object,Enum<?>>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Enum<?>>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Enum<?>> existing;
                try
                {
                    existing = (Map<Object,Enum<?>>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object, Enum<?>> wrapper) 
            throws IOException
            {
                return inlineK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object, Enum<?>> wrapper, 
                    Object key) throws IOException
            {
                wrapper.put(key, eioV.readFrom(input));
            }
            protected void vTo(Output output, int fieldNumber, Enum<?> val, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, val);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapInlineKInlineV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final RuntimeFieldFactory<Object> inlineK, 
            final RuntimeFieldFactory<Object> inlineV)
    {
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return inlineK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                wrapper.put(key, inlineV.readFrom(input));
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                inlineV.writeTo(output, fieldNumber, val, repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineV.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapInlineKPojoV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final RuntimeFieldFactory<Object> inlineK, 
            final Class<Object> clazzV)
    {
        final HasSchema<Object> schemaV = RuntimeSchema.getSchemaWrapper(clazzV);
        
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return inlineK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                wrapper.put(key, input.mergeObject(null, schemaV.getSchema()));
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, schemaV.getSchema(), repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(), repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapInlineKPolymorphicV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final RuntimeFieldFactory<Object> inlineK, 
            final Class<Object> clazzV)
    {
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return inlineK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, 
                        POLYMORPHIC_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    
                    wrapper.put(key, value);
                    return;
                }

                if(key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, POLYMORPHIC_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, POLYMORPHIC_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapInlineKObjectV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final RuntimeFieldFactory<Object> inlineK)
    {
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return inlineK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, 
                        OBJECT_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    
                    wrapper.put(key, value);
                    return;
                }

                if(key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, OBJECT_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, OBJECT_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapEnumKEnumV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory,  
            final Class<Object> clazzK, 
            final Class<Object> clazzV)
    {
        final EnumIO<?> eioK = EnumIO.get(clazzK);
        final EnumIO<?> eioV = EnumIO.get(clazzV);
        
        return new RuntimeMapField<T,Enum<?>,Enum<?>>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Enum<?>,Enum<?>>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>,Enum<?>> existing;
                try
                {
                    existing = (Map<Enum<?>,Enum<?>>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Enum<?> kFrom(Input input, MapWrapper<Enum<?>,Enum<?>> wrapper) 
            throws IOException
            {
                return eioK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Enum<?> key, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, key);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Enum<?>,Enum<?>> wrapper, 
                    Enum<?> key) throws IOException
            {
                wrapper.put(key, eioV.readFrom(input));
            }
            protected void vTo(Output output, int fieldNumber, Enum<?> val, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, val);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapEnumKInlineV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK, 
            final RuntimeFieldFactory<Object> inlineV)
    {
        final EnumIO<?> eioK = EnumIO.get(clazzK);
        
        return new RuntimeMapField<T,Enum<?>,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Enum<?>,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>,Object> existing;
                try
                {
                    existing = (Map<Enum<?>,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Enum<?> kFrom(Input input, MapWrapper<Enum<?>,Object> wrapper) 
            throws IOException
            {
                return eioK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Enum<?> key, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, key);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Enum<?>,Object> wrapper, 
                    Enum<?> key) throws IOException
            {
                wrapper.put(key, inlineV.readFrom(input));
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                inlineV.writeTo(output, fieldNumber, val, repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineV.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapEnumKPojoV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK, 
            final Class<Object> clazzV)
    {
        final EnumIO<?> eioK = EnumIO.get(clazzK);
        final HasSchema<Object> schemaV = RuntimeSchema.getSchemaWrapper(clazzV);
        
        return new RuntimeMapField<T,Enum<?>,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Enum<?>,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>,Object> existing;
                try
                {
                    existing = (Map<Enum<?>,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Enum<?> kFrom(Input input, MapWrapper<Enum<?>,Object> wrapper) 
            throws IOException
            {
                return eioK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Enum<?> key, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, key);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Enum<?>,Object> wrapper, 
                    Enum<?> key) throws IOException
            {
                wrapper.put(key, input.mergeObject(null, schemaV.getSchema()));
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, schemaV.getSchema(), repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(), repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapEnumKPolymorphicV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK, 
            final Class<Object> clazzV)
    {
        final EnumIO<?> eioK = EnumIO.get(clazzK);
        
        return new RuntimeMapField<T,Enum<?>,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Enum<?>,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>,Object> existing;
                try
                {
                    existing = (Map<Enum<?>,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Enum<?> kFrom(Input input, MapWrapper<Enum<?>,Object> wrapper) 
            throws IOException
            {
                return eioK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Enum<?> key, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, key);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Enum<?>,Object> wrapper, 
                    Enum<?> key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, 
                        POLYMORPHIC_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    
                    wrapper.put(key, value);
                    return;
                }
                
                if(key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, POLYMORPHIC_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, POLYMORPHIC_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapEnumKObjectV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK)
    {
        final EnumIO<?> eioK = EnumIO.get(clazzK);
        
        return new RuntimeMapField<T,Enum<?>,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Enum<?>,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>,Object> existing;
                try
                {
                    existing = (Map<Enum<?>,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Enum<?> kFrom(Input input, MapWrapper<Enum<?>,Object> wrapper) 
            throws IOException
            {
                return eioK.readFrom(input);
            }
            protected void kTo(Output output, int fieldNumber, Enum<?> key, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, key);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Enum<?>,Object> wrapper, 
                    Enum<?> key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, 
                        OBJECT_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    
                    wrapper.put(key, value);
                    return;
                }
                
                if(key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, OBJECT_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, OBJECT_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapPojoKEnumV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK, 
            final Class<Object> clazzV)
    {
        final HasSchema<Object> schemaK = RuntimeSchema.getSchemaWrapper(clazzK);
        final EnumIO<?> eioV = EnumIO.get(clazzV);
        
        return new RuntimeMapField<T,Object,Enum<?>>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Enum<?>>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Enum<?>> existing;
                try
                {
                    existing = (Map<Object,Enum<?>>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object, Enum<?>> wrapper) 
            throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(), repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(), repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object, Enum<?>> wrapper, 
                    Object key) throws IOException
            {
                wrapper.put(key, eioV.readFrom(input));
            }
            protected void vTo(Output output, int fieldNumber, Enum<?> val, 
                    boolean repeated) throws IOException
            {
                EnumIO.writeTo(output, fieldNumber, repeated, val);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapPojoKInlineV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK, 
            final RuntimeFieldFactory<Object> inlineV)
    {
        final HasSchema<Object> schemaK = RuntimeSchema.getSchemaWrapper(clazzK);
        
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(), repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(), repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                wrapper.put(key, inlineV.readFrom(input));
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                inlineV.writeTo(output, fieldNumber, val, repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                inlineV.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapPojoKPojoV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK, 
            final Class<Object> clazzV)
    {
        final HasSchema<Object> schemaK = RuntimeSchema.getSchemaWrapper(clazzK);
        final HasSchema<Object> schemaV = RuntimeSchema.getSchemaWrapper(clazzV);
        
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(), repeated);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(), repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                wrapper.put(key, input.mergeObject(null, schemaV.getSchema()));
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, schemaV.getSchema(), repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(), repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapPojoKPolymorphicV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK, 
            final Class<Object> clazzV)
    {
        final HasSchema<Object> schemaK = RuntimeSchema.getSchemaWrapper(clazzK);
        
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(), repeated);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(), repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, 
                        POLYMORPHIC_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    
                    wrapper.put(key, value);
                    return;
                }

                if(key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, POLYMORPHIC_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, POLYMORPHIC_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapPojoKObjectV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory, 
            final Class<Object> clazzK)
    {
        final HasSchema<Object> schemaK = RuntimeSchema.getSchemaWrapper(clazzK);
        
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(), repeated);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(), repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, 
                        OBJECT_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    
                    wrapper.put(key, value);
                    return;
                }

                if(key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, OBJECT_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, OBJECT_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
        };
    }
    
    private static <T> Field<T> createMapObjectKObjectV(int number, String name, 
            final java.lang.reflect.Field f, MessageFactory messageFactory)
    {
        return new RuntimeMapField<T,Object,Object>(FieldType.MESSAGE, 
                number, name, messageFactory)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject((Map<Object,Object>)f.get(message), 
                            schema));
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object,Object> existing;
                try
                {
                    existing = (Map<Object,Object>)f.get(message);
                }
                catch(IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null)
                    output.writeObject(number, existing, schema, false);
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            protected Object kFrom(Input input, MapWrapper<Object,Object> wrapper) 
            throws IOException
            {
                final Object value = input.mergeObject(wrapper, OBJECT_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    return value;
                }
                
                return wrapper.setValue(null);
            }
            protected void kTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, OBJECT_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
            protected void kTo(Output output, int fieldNumber, Object key, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, OBJECT_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vPutFrom(Input input, MapWrapper<Object,Object> wrapper, 
                    Object key) throws IOException
            {
                final Object value = input.mergeObject(wrapper, 
                        OBJECT_MAP_VALUE_SCHEMA);
                if(value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput)input).updateLast(value, wrapper);
                    
                    wrapper.put(key, value);
                    return;
                }

                if(key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }
            protected void vTo(Output output, int fieldNumber, Object val, 
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, OBJECT_MAP_VALUE_SCHEMA, 
                        repeated);
            }
            protected void vTransfer(Pipe pipe, Input input, Output output, int number, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, OBJECT_MAP_VALUE_SCHEMA.pipeSchema, 
                        repeated);
            }
        };
    }
    
    static final RuntimeFieldFactory<Map<?,?>> MAP = new RuntimeFieldFactory<Map<?,?>>(RuntimeFieldFactory.ID_MAP)
    {

        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            final MessageFactory messageFactory;
            if(EnumMap.class.isAssignableFrom(f.getType()))
            {
                final Class<Object> enumType;
                try
                {
                    enumType = (Class<Object>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
                }
                catch(Exception e)
                {
                    // still handle the serialization of EnumMaps even without generics
                    return RuntimeFieldFactory.OBJECT.create(number, name, f);
                }
                
                messageFactory = EnumIO.get(enumType).getEnumMapFactory();
            }
            else
            {
                messageFactory = MessageFactories.getFactory(
                        (Class<? extends Map<?,?>>)f.getType());
                
                if(messageFactory == null)
                {
                    // Not a standard jdk Map impl.
                    throw new RuntimeException("Not a standard jdk map: " + 
                            f.getType());
                }
            }
            
            final Class<Object> clazzK;
            try
            {
                clazzK = 
                    (Class<Object>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
            }
            catch(Exception e)
            {
                // the key is not a simple parameterized type.
                return createMapObjectKObjectV(number, name, f, messageFactory);
            }
            
            final Class<Object> clazzV;
            try
            {
                clazzV = 
                    (Class<Object>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[1];
            }
            catch(Exception e)
            {
                // the value is not a simple parameterized type.
                if(isComplexComponentType(clazzK))
                    return createMapObjectKObjectV(number, name, f, messageFactory);
                
                if(clazzK.isEnum())
                    return createMapEnumKObjectV(number, name, f, messageFactory, clazzK);
                
                final RuntimeFieldFactory<Object> inlineK = getInline(clazzK);
                if(inlineK != null)
                    return createMapInlineKObjectV(number, name, f, messageFactory, inlineK);
                
                if(POJO == pojo(clazzK) || RuntimeSchema.isRegistered(clazzK))
                    return createMapPojoKObjectV(number, name, f, messageFactory, clazzK);
                
                return createMapObjectKObjectV(number, name, f, messageFactory);
            }
            
            if(isComplexComponentType(clazzK))
                return createMapObjectKObjectV(number, name, f, messageFactory);

            if(clazzK.isEnum())
            {
                if(isComplexComponentType(clazzV))
                    return createMapEnumKObjectV(number, name, f, messageFactory, clazzK);
                
                if(clazzV.isEnum())
                    return createMapEnumKEnumV(number, name, f, messageFactory, clazzK, clazzV);
                
                final RuntimeFieldFactory<Object> inlineV = getInline(clazzV);
                if(inlineV != null)
                    return createMapEnumKInlineV(number, name, f, messageFactory, clazzK, inlineV);
                
                if(POJO == pojo(clazzV) || RuntimeSchema.isRegistered(clazzV))
                    return createMapEnumKPojoV(number, name, f, messageFactory, clazzK, clazzV);
                
                return createMapEnumKPolymorphicV(number, name, f, messageFactory, clazzK, clazzV);
            }

            final RuntimeFieldFactory<Object> inlineK = getInline(clazzK);
            
            if(inlineK != null)
            {
                if(isComplexComponentType(clazzV))
                    return createMapInlineKObjectV(number, name, f, messageFactory, inlineK);
                
                if(clazzV.isEnum())
                    return createMapInlineKEnumV(number, name, f, messageFactory, inlineK, clazzV);
                
                final RuntimeFieldFactory<Object> inlineV = getInline(clazzV);
                if(inlineV != null)
                    return createMapInlineKInlineV(number, name, f, messageFactory, inlineK, inlineV);
                
                if(POJO == pojo(clazzV) || RuntimeSchema.isRegistered(clazzV))
                    return createMapInlineKPojoV(number, name, f, messageFactory, inlineK, clazzV);
                
                return createMapInlineKPolymorphicV(number, name, f, messageFactory, inlineK, clazzV);
            }
            
            if(POJO == pojo(clazzK) || RuntimeSchema.isRegistered(clazzK))
            {
                if(isComplexComponentType(clazzV))
                    return createMapPojoKObjectV(number, name, f, messageFactory, clazzK);
                
                if(clazzV.isEnum())
                    return createMapPojoKEnumV(number, name, f, messageFactory, clazzK, clazzV);
                
                final RuntimeFieldFactory<Object> inlineV = getInline(clazzV);
                if(inlineV != null)
                    return createMapPojoKInlineV(number, name, f, messageFactory, clazzK, inlineV);
                
                if(POJO == pojo(clazzV) || RuntimeSchema.isRegistered(clazzV))
                    return createMapPojoKPojoV(number, name, f, messageFactory, clazzK, clazzV);
                
                return createMapPojoKPolymorphicV(number, name, f, messageFactory, clazzK, clazzV);
            }
            
            return createMapObjectKObjectV(number, name, f, messageFactory);
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected Map<?,?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, Map<?,?>  value, boolean repeated) 
        throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
        protected Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };
    
}
