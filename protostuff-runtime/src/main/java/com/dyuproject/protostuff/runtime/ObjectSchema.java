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

import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.BIGDECIMAL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.BIGINTEGER;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.BOOL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.BYTE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.BYTES;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.BYTE_ARRAY;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.CHAR;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.DATE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.DOUBLE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.FLOAT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_ARRAY;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BIGDECIMAL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BIGINTEGER;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BOOL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTES;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_BYTE_ARRAY;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_CHAR;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_COLLECTION;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_DATE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_DOUBLE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_ENUM;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_FLOAT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_INT32;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_INT64;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_MAP;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_OBJECT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_POJO;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_SHORT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.ID_STRING;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.INT32;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.INT64;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.SHORT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STRING;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_ARRAY;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_BIGDECIMAL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_BIGINTEGER;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_BOOL;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_BYTE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_BYTES;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_BYTE_ARRAY;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_CHAR;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_COLLECTION;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_DATE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_DOUBLE;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_ENUM;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_FLOAT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_INT32;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_INT64;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_MAP;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_OBJECT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_POJO;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_SHORT;
import static com.dyuproject.protostuff.runtime.RuntimeFieldFactory.STR_STRING;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.dyuproject.protostuff.CollectionSchema;
import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.MapSchema;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.ProtostuffException;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.StatefulOutput;
import com.dyuproject.protostuff.runtime.RuntimeSchema.HasSchema;

/**
 * A schema for dynamic types (fields where the type is {@link Object}).
 *
 * @author David Yu
 * @created Feb 1, 2011
 */
public abstract class ObjectSchema implements Schema<Object>
{
    
    static final int ID_ENUM_VALUE = 1;
    static final int ID_ARRAY_LEN = 3;
    static final int ID_ARRAY_DIMENSION = 2;
    
    static String name(int number)
    {
        switch(number)
        {
            case ID_BOOL:
                return STR_BOOL;
            case ID_BYTE:
                return STR_BYTE;
            case ID_CHAR:
                return STR_CHAR;
            case ID_SHORT:
                return STR_SHORT;
            case ID_INT32:
                return STR_INT32;
            case ID_INT64:
                return STR_INT64;
            case ID_FLOAT:
                return STR_FLOAT;
            case ID_DOUBLE:
                return STR_DOUBLE;
            case ID_STRING:
                return STR_STRING;
            case ID_BYTES:
                return STR_BYTES;
            case ID_BYTE_ARRAY:
                return STR_BYTE_ARRAY;
            case ID_BIGDECIMAL:
                return STR_BIGDECIMAL;
            case ID_BIGINTEGER:
                return STR_BIGINTEGER;
            case ID_DATE:
                return STR_DATE;
            case ID_OBJECT:
                return STR_OBJECT;
            
            // room for more scalar types (16-22)
                
            case ID_ENUM:
                return STR_ENUM;
            case ID_COLLECTION:
                return STR_COLLECTION;
            case ID_ARRAY:
                return STR_ARRAY;
            case ID_MAP:
                return STR_MAP;
            case ID_POJO:
                return STR_POJO;
            default:
                return null;
        }
    }
    
    static int number(String name)
    {
        if(name.length() != 1)
            return 0;
        
        switch(name.charAt(0))
        {
            case '_':
                return 127;
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
            case 'i':
                return 9;
            case 'j':
                return 10;
            case 'k':
                return 11;
            case 'l':
                return 12;
            case 'm':
                return 13;
            case 'n':
                return 14;
            case 'o':
                return 15;
                
            // room for more scalar types (16-22)
                
            case 'w':
                return 23;
            case 'x':
                return 24;
            case 'y':
                return 25;
            case 'z':
                return 26;
            default:
                return 0;
        }
    }

    public String getFieldName(int number)
    {
        return name(number);
    }

    public int getFieldNumber(String name)
    {
        return number(name);
    }

    public boolean isInitialized(Object owner)
    {
        return true;
    }

    public String messageFullName()
    {
        return Object.class.getName();
    }

    public String messageName()
    {
        return Object.class.getSimpleName();
    }

    public Object newMessage()
    {
        // cannot instantiate because the type is dynamic.
        throw new UnsupportedOperationException();
    }

    public Class<? super Object> typeClass()
    {
        return Object.class;
    }

    public void mergeFrom(Input input, Object owner) throws IOException
    {
        setValue(readObjectFrom(input, this), owner);
    }

    public void writeTo(Output output, Object value) throws IOException
    {
        writeObjectTo(output, value, this);
    }
    
    static ArrayWrapper newArrayWrapper(String className, int len, int dimensions) 
    throws IOException
    {
        final Class<?> clazz;
        
        final RuntimeFieldFactory<Object> inline = RuntimeFieldFactory.getInline(
                className);
        
        if(inline != null)
        {
            if(className.indexOf('.') == -1)
            {
                // primitive
                switch(inline.id)
                {
                    case ID_BOOL:
                        clazz = boolean.class;
                        break;
                    case ID_BYTE:
                        clazz = byte.class;
                        break;
                    case ID_CHAR:
                        clazz = char.class;
                        break;
                    case ID_SHORT:
                        clazz = short.class;
                        break;
                    case ID_INT32:
                        clazz = int.class;
                        break;
                    case ID_INT64:
                        clazz = long.class;
                        break;
                    case ID_FLOAT:
                        clazz = float.class;
                        break;
                    case ID_DOUBLE:
                        clazz = double.class;
                        break;
                    default:
                        throw new RuntimeException("Should never happen.");
                }
            }
            else
            {
                clazz = inline.typeClass();
            }
        }
        else
        {
            clazz = RuntimeSchema.loadClass(className);
        }
        
        if(dimensions == 1)
            return new ArrayWrapper(Array.newInstance(clazz, len));
        
        final int[] arg = new int[dimensions];
        arg[0] = len;
        return new ArrayWrapper(Array.newInstance(clazz, arg));
    }
    
    private static Object readObjectFrom(final Input input, final Schema<?> schema) 
    throws IOException
    {
        Object value = null;
        final int number = input.readFieldNumber(schema);
        switch(number)
        {
            case ID_BOOL:
                value = BOOL.readFrom(input);
                break;
            case ID_BYTE:
                value = BYTE.readFrom(input);
                break;
            case ID_CHAR:
                value = CHAR.readFrom(input);
                break;
            case ID_SHORT:
                value = SHORT.readFrom(input);
                break;
            case ID_INT32:
                value = INT32.readFrom(input);
                break;
            case ID_INT64:
                value = INT64.readFrom(input);
                break;
            case ID_FLOAT:
                value = FLOAT.readFrom(input);
                break;
            case ID_DOUBLE:
                value = DOUBLE.readFrom(input);
                break;
            case ID_STRING:
                value = STRING.readFrom(input);
                break;
            case ID_BYTES:
                value = BYTES.readFrom(input);
                break;
            case ID_BYTE_ARRAY:
                value = BYTE_ARRAY.readFrom(input);
                break;
            case ID_BIGDECIMAL:
                value = BIGDECIMAL.readFrom(input);
                break;
            case ID_BIGINTEGER:
                value = BIGINTEGER.readFrom(input);
                break;
            case ID_DATE:
                value = DATE.readFrom(input);
                break;
            case ID_OBJECT:
                if(input.readUInt32() != 0)
                    throw new ProtostuffException("Corrupt input.");
                
                value = new Object();
                break;
            case ID_ENUM:
                final String typeEnum = input.readString();
                final EnumIO<?> eio = EnumIO.get(typeEnum, 
                        RuntimeSchema.AUTO_LOAD_POLYMORPHIC_CLASSES);
                
                if(eio == null)
                    throw new ProtostuffException("Unknown enum class: " + typeEnum);
                
                if(input.readFieldNumber(schema) != ID_ENUM_VALUE)
                    throw new ProtostuffException("Corrupt input.");
                
                value = eio.readFrom(input);
                break;
            
            // objects ser/deser with schema
                
            case ID_COLLECTION:
                final Collection<Object> collection = 
                    CollectionSchema.MessageFactories.getFactory(
                            input.readString()).newMessage();
                
                COLLECTION_SCHEMA.mergeFrom(input, collection);
                
                return collection;
                
            case ID_ARRAY:
                final String typeArray = input.readString();
                
                if(input.readFieldNumber(schema) != ID_ARRAY_LEN)
                    throw new ProtostuffException("Corrupt input.");
                final int len = input.readUInt32();
                
                if(input.readFieldNumber(schema) != ID_ARRAY_DIMENSION)
                    throw new ProtostuffException("Corrupt input.");
                final int dimensions = input.readUInt32();

                final ArrayWrapper arrayWrapper = newArrayWrapper(typeArray, len, 
                        dimensions);
                
                COLLECTION_SCHEMA.mergeFrom(input, arrayWrapper);
                
                return arrayWrapper.array;
                
            case ID_MAP:
                final Map<Object,Object> map = 
                    MapSchema.MessageFactories.getFactory(
                            input.readString()).newMessage();
                
                MAP_SCHEMA.mergeFrom(input, map);
                
                return map;
                
            case ID_POJO:
                final String typePojo = input.readString();
                final HasSchema<Object> wrapper = RuntimeSchema.getSchemaWrapper(
                        typePojo, RuntimeSchema.AUTO_LOAD_POLYMORPHIC_CLASSES);
                
                if(wrapper == null)
                    throw new ProtostuffException("Unknown pojo class: " + typePojo);
                
                final Schema<Object> derivedSchema = wrapper.getSchema();
                final Object pojo = derivedSchema.newMessage();
                
                derivedSchema.mergeFrom(input, pojo);
                return pojo;
                
            default:
                throw new ProtostuffException("Corrupt input.  Unknown field number: " + number);
        }
        
        if(input.readFieldNumber(schema) != 0)
            throw new ProtostuffException("Corrupt input.");
        
        return value;
    }
    
    @SuppressWarnings("unchecked")
    private static void writeObjectTo(Output output, Object value,
            Schema<?> currentSchema) throws IOException
    {
        final Class<Object> clazz = (Class<Object>)value.getClass();
        
        if(clazz.isArray())
        {
            int dimensions = 1;
            Class<?> componentType = clazz.getComponentType();
            while(componentType.isArray())
            {
                dimensions++;
                componentType = componentType.getComponentType();
            }
            
            // write the class without the "["
            output.writeString(ID_ARRAY, componentType.getName(), false);
            // write the length of the array
            output.writeUInt32(ID_ARRAY_LEN, Array.getLength(value), false);
            // write the dimensions of the array
            output.writeUInt32(ID_ARRAY_DIMENSION, dimensions, false);
            
            if(output instanceof StatefulOutput)
            {
                // update using the derived schema.
                ((StatefulOutput)output).updateLast(ARRAY_SCHEMA, currentSchema);
            }
            
            ARRAY_SCHEMA.writeTo(output, value);
            return;
        }
        
        if(Object.class == clazz)
        {
            output.writeUInt32(ID_OBJECT, 0, false);
            return;
        }
        
        if(Message.class.isAssignableFrom(clazz))
        {
            output.writeString(ID_POJO, clazz.getName(), false);
            final Schema<Object> schema = ((Message<Object>)value).cachedSchema();
            
            if(output instanceof StatefulOutput)
            {
                // update using the derived schema.
                ((StatefulOutput)output).updateLast(schema, currentSchema);
            }
            
            schema.writeTo(output, value);
            return;
        }
        
        if(clazz.isEnum())
        {
            output.writeString(ID_ENUM, clazz.getName(), false);
            EnumIO.writeTo(output, ID_ENUM_VALUE, false, (Enum<?>)value);
            return;
        }
        
        final RuntimeFieldFactory<Object> inline = RuntimeFieldFactory.getInline(clazz);
        if(inline != null)
        {
            // scalar value
            inline.writeTo(output, inline.id, value, false);
            return;
        }
        
        if(Map.class.isAssignableFrom(clazz))
        {
            output.writeString(ID_MAP, MapSchema.MessageFactories.getFactory(
                    clazz.getSimpleName()).name(), false);
            
            if(output instanceof StatefulOutput)
            {
                // update using the derived schema.
                ((StatefulOutput)output).updateLast(MAP_SCHEMA, currentSchema);
            }
            
            MAP_SCHEMA.writeTo(output, (Map<Object,Object>)value);
            return;
        }
        
        if(Collection.class.isAssignableFrom(clazz))
        {
            output.writeString(ID_COLLECTION, 
                    CollectionSchema.MessageFactories.getFactory(
                    clazz.getSimpleName()).name(), false);
            
            if(output instanceof StatefulOutput)
            {
                // update using the derived schema.
                ((StatefulOutput)output).updateLast(COLLECTION_SCHEMA, currentSchema);
            }
            
            COLLECTION_SCHEMA.writeTo(output, (Collection<Object>)value);
            return;
        }
        
        // pojo
        output.writeString(ID_POJO, clazz.getName(), false);
        final Schema<Object> schema = RuntimeSchema.getSchema(clazz);
        
        if(output instanceof StatefulOutput)
        {
            // update using the derived schema.
            ((StatefulOutput)output).updateLast(schema, currentSchema);
        }
        
        schema.writeTo(output, value);
    }
    
    private static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe, 
            Input input, Output output) throws IOException
    {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        switch(number)
        {
            case 0:
                return;
            case ID_BOOL:
                BOOL.transfer(pipe, input, output, number, false);
                break;
            case ID_BYTE:
                BYTE.transfer(pipe, input, output, number, false);
                break;
            case ID_CHAR:
                CHAR.transfer(pipe, input, output, number, false);
                break;
            case ID_SHORT:
                SHORT.transfer(pipe, input, output, number, false);
                break;
            case ID_INT32:
                INT32.transfer(pipe, input, output, number, false);
                break;
            case ID_INT64:
                INT64.transfer(pipe, input, output, number, false);
                break;
            case ID_FLOAT:
                FLOAT.transfer(pipe, input, output, number, false);
                break;
            case ID_DOUBLE:
                DOUBLE.transfer(pipe, input, output, number, false);
                break;
            case ID_STRING:
                STRING.transfer(pipe, input, output, number, false);
                break;
            case ID_BYTES:
                BYTES.transfer(pipe, input, output, number, false);
                break;
            case ID_BYTE_ARRAY:
                BYTE_ARRAY.transfer(pipe, input, output, number, false);
                break;
            case ID_BIGDECIMAL:
                BIGDECIMAL.transfer(pipe, input, output, number, false);
                break;
            case ID_BIGINTEGER:
                BIGINTEGER.transfer(pipe, input, output, number, false);
                break;
            case ID_DATE:
                DATE.transfer(pipe, input, output, number, false);
                break;
            case ID_OBJECT:
                output.writeUInt32(number, input.readUInt32(), false);
                break;
            case ID_ENUM:
                input.transferByteRangeTo(output, true, number, false);
                
                if(input.readFieldNumber(pipeSchema.wrappedSchema) != ID_ENUM_VALUE)
                    throw new ProtostuffException("Corrupt input.");
                
                EnumIO.transfer(pipe, input, output, 1, false);
                break;
            case ID_COLLECTION:
                input.transferByteRangeTo(output, true, number, false);
                
                if(output instanceof StatefulOutput)
                {
                    // update using the derived schema.
                    ((StatefulOutput)output).updateLast(COLLECTION_PIPE_SCHEMA, pipeSchema);
                }
                
                Pipe.transferDirect(COLLECTION_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_ARRAY:
                input.transferByteRangeTo(output, true, number, false);
                
                if(input.readFieldNumber(pipeSchema.wrappedSchema) != ID_ARRAY_LEN)
                    throw new ProtostuffException("Corrupt input.");
                
                output.writeUInt32(ID_ARRAY_LEN, input.readUInt32(), false);
                
                if(input.readFieldNumber(pipeSchema.wrappedSchema) != ID_ARRAY_DIMENSION)
                    throw new ProtostuffException("Corrupt input.");
                
                output.writeUInt32(ID_ARRAY_DIMENSION, input.readUInt32(), false);
                
                if(output instanceof StatefulOutput)
                {
                    // update using the derived schema.
                    ((StatefulOutput)output).updateLast(ARRAY_PIPE_SCHEMA, pipeSchema);
                }
                
                Pipe.transferDirect(ARRAY_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_MAP:
                input.transferByteRangeTo(output, true, number, false);
                
                if(output instanceof StatefulOutput)
                {
                    // update using the derived schema.
                    ((StatefulOutput)output).updateLast(MAP_PIPE_SCHEMA, pipeSchema);
                }
                
                Pipe.transferDirect(MAP_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_POJO:
                final String typePojo = input.readString();
                final HasSchema<Object> wrapper = RuntimeSchema.getSchemaWrapper(
                        typePojo, RuntimeSchema.AUTO_LOAD_POLYMORPHIC_CLASSES);
                
                if(wrapper == null)
                    throw new ProtostuffException("Unknown pojo class: " + typePojo);
                
                output.writeString(number, typePojo, false);
                
                final Pipe.Schema<Object> derivedPipeSchema = wrapper.getPipeSchema();
                
                
                if(output instanceof StatefulOutput)
                {
                    // update using the derived schema.
                    ((StatefulOutput)output).updateLast(derivedPipeSchema, pipeSchema);
                }
                
                Pipe.transferDirect(derivedPipeSchema, pipe, input, output);
                return;
            default:
                throw new ProtostuffException("Corrupt input.  Unknown field number: " + number);
        }
        
        if(input.readFieldNumber(pipeSchema.wrappedSchema) != 0)
            throw new ProtostuffException("Corrupt input.");
    }
    
    protected abstract void setValue(Object value, Object owner);
    
    protected final Pipe.Schema<Object> pipeSchema = new Pipe.Schema<Object>(
            ObjectSchema.this)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            transferObject(this, pipe, input, output);
        }
    };
    
    private static final Schema<Object> DYNAMIC_VALUE_SCHEMA = new Schema<Object>()
    {
        public String getFieldName(int number)
        {
            return name(number);
        }

        public int getFieldNumber(String name)
        {
            return number(name);
        }

        public boolean isInitialized(Object owner)
        {
            return true;
        }

        public String messageFullName()
        {
            return Object.class.getName();
        }

        public String messageName()
        {
            return Object.class.getSimpleName();
        }

        public Object newMessage()
        {
            // cannot instantiate because the type is dynamic.
            throw new UnsupportedOperationException();
        }

        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        @SuppressWarnings("unchecked")
        public void mergeFrom(Input input, Object owner) throws IOException
        {
            if(MapWrapper.class.isAssignableFrom(owner.getClass()))
            {
                // called from ENTRY_SCHEMA
                ((MapWrapper)owner).setValue(readObjectFrom(input, this));
            }
            else
            {
                // called from COLLECTION_SCHEMA
                ((Collection<Object>)owner).add(readObjectFrom(input, this));
            }
        }

        public void writeTo(Output output, Object message) throws IOException
        {
            writeObjectTo(output, message, this);
        }
    };
    
    private static final Pipe.Schema<Object> DYNAMIC_VALUE_PIPE_SCHEMA = 
        new Pipe.Schema<Object>(DYNAMIC_VALUE_SCHEMA)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            transferObject(this, pipe, input, output);
        }
    };
    
    private static final Schema<Collection<Object>> COLLECTION_SCHEMA = 
        new Schema<Collection<Object>>()
    {
        public String getFieldName(int number)
        {
            return number == 1 ? CollectionSchema.FIELD_NAME_VALUE : null;
        }

        public int getFieldNumber(String name)
        {
            return name.length() == 1 && name.charAt(0) == 'v' ? 1 : 0;
        }

        public boolean isInitialized(Collection<Object> owner)
        {
            return true;
        }

        public String messageFullName()
        {
            return Collection.class.getName();
        }

        public String messageName()
        {
            return Collection.class.getSimpleName();
        }

        public Collection<Object> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        public Class<? super Collection<Object>> typeClass()
        {
            return Collection.class;
        }

        public void mergeFrom(Input input, Collection<Object> message) throws IOException
        {
            for(int number = input.readFieldNumber(this);;
                    number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        final Object value = input.mergeObject(message, 
                                DYNAMIC_VALUE_SCHEMA);
                        if(input instanceof GraphInput 
                                && ((GraphInput)input).isCurrentMessageReference())
                        {
                            // a reference from polymorphic+cyclic graph deser
                            message.add(value);
                        }
                        break;
                    default:
                        throw new ProtostuffException("Corrupt input.");
                }
            }
        }

        public void writeTo(Output output, Collection<Object> message) throws IOException
        {
            for(Object value : message)
            {
                if(value != null)
                    output.writeObject(1, value, DYNAMIC_VALUE_SCHEMA, true);
            }
        }
    };
    
    private static final Pipe.Schema<Collection<Object>> COLLECTION_PIPE_SCHEMA = 
        new Pipe.Schema<Collection<Object>>(COLLECTION_SCHEMA)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(wrappedSchema);; 
                    number = input.readFieldNumber(wrappedSchema))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, DYNAMIC_VALUE_PIPE_SCHEMA, true);
                        break;
                    default:
                        throw new ProtostuffException("The collection was incorrectly " + 
                                "serialized.");
                }
            }
        }
    };
    
    private static final Schema<Object> ARRAY_SCHEMA = new Schema<Object>()
    {
        public String getFieldName(int number)
        {
            return number == 1 ? CollectionSchema.FIELD_NAME_VALUE : null;
        }

        public int getFieldNumber(String name)
        {
            return name.length() == 1 && name.charAt(0) == 'v' ? 1 : 0;
        }

        public boolean isInitialized(Object owner)
        {
            return true;
        }

        public String messageFullName()
        {
            return Array.class.getName();
        }

        public String messageName()
        {
            return Array.class.getSimpleName();
        }

        public Object newMessage()
        {
            throw new UnsupportedOperationException();
        }

        public Class<? super Object> typeClass()
        {
            return Object.class;
        }

        public void mergeFrom(Input input, Object message) throws IOException
        {
            // using COLLECTION_SCHEMA instead.
            throw new UnsupportedOperationException();
        }

        public void writeTo(Output output, Object message) throws IOException
        {
            for(int i = 0, len = Array.getLength(message); i < len; i++)
            {
                final Object value = Array.get(message, i);
                if(value != null)
                {
                    output.writeObject(1, value, DYNAMIC_VALUE_SCHEMA, true);
                }
            }
        }
    };
    
    private static final Pipe.Schema<Object> ARRAY_PIPE_SCHEMA = 
        new Pipe.Schema<Object>(ARRAY_SCHEMA)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(wrappedSchema);; 
                    number = input.readFieldNumber(wrappedSchema))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, DYNAMIC_VALUE_PIPE_SCHEMA, true);
                        break;
                    default:
                        throw new ProtostuffException("The array was incorrectly " + 
                                "serialized.");
                }
            }
        }
    };
    
    private static final Schema<Map<Object,Object>> MAP_SCHEMA = 
        new Schema<Map<Object,Object>>()
    {
        public final String getFieldName(int number)
        {
            return number == 1 ? MapSchema.FIELD_NAME_ENTRY : null;
        }

        public final int getFieldNumber(String name)
        {
            return name.length() == 1 && name.charAt(0) == 'e' ? 1 : 0; 
        }

        public boolean isInitialized(Map<Object,Object> owner)
        {
            return true;
        }

        public String messageFullName()
        {
            return Map.class.getName();
        }

        public String messageName()
        {
            return Map.class.getSimpleName();
        }

        public Map<Object,Object> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        public Class<? super Map<Object,Object>> typeClass()
        {
            return Map.class;
        }

        public void mergeFrom(Input input, Map<Object,Object> message) throws IOException
        {
            MapWrapper entry = null;
            for(int number = input.readFieldNumber(this);; 
                    number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        if(entry == null)
                        {
                            // lazy initialize
                            entry = new MapWrapper(message);
                        }
                        
                        if(entry != input.mergeObject(entry, ENTRY_SCHEMA))
                        {
                            // an entry will always be unique
                            // it can never be a reference.
                            throw new IllegalStateException(
                                    "A Map.Entry will always be " +
                                    "unique, hence it cannot be a reference " +
                                    "obtained from " + input.getClass().getName());
                        }
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly serialized.");
                }
            }
        }

        public void writeTo(Output output, Map<Object,Object> message) throws IOException
        {
            for(Map.Entry<Object, Object> entry : message.entrySet())
            {
                output.writeObject(1, entry, ENTRY_SCHEMA, true);
            }
        }
    };
    
    private static final Pipe.Schema<Map<Object,Object>> MAP_PIPE_SCHEMA = 
        new Pipe.Schema<Map<Object,Object>>(MAP_SCHEMA)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(wrappedSchema);; 
                    number = input.readFieldNumber(wrappedSchema))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, ENTRY_PIPE_SCHEMA, true);
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly " + 
                                "serialized.");
                }
            }
        }
    };
    
    private static final Schema<Entry<Object,Object>> ENTRY_SCHEMA = 
        new Schema<Entry<Object,Object>>()
    {
        public final String getFieldName(int number)
        {
            switch(number)
            {
                case 1:
                    return MapSchema.FIELD_NAME_KEY;
                case 2:
                    return MapSchema.FIELD_NAME_VALUE;
                default:
                    return null;
            }
        }

        public final int getFieldNumber(String name)
        {
            if(name.length() != 1)
                return 0;
            
            switch(name.charAt(0))
            {
                case 'k':
                    return 1;
                case 'v':
                    return 2;
                default:
                    return 0;
            }
        }

        public boolean isInitialized(Entry<Object,Object> message)
        {
            return true;
        }

        public String messageFullName()
        {
            return Entry.class.getName();
        }

        public String messageName()
        {
            return Entry.class.getSimpleName();
        }

        public Entry<Object,Object> newMessage()
        {
            throw new UnsupportedOperationException();
        }

        public Class<? super Entry<Object,Object>> typeClass()
        {
            return Entry.class;
        }
        
        public void mergeFrom(Input input, Entry<Object,Object> message) 
        throws IOException
        {
            // Nobody else calls this except MAP_SCHEMA.mergeFrom
            final MapWrapper entry = (MapWrapper)message;
            
            Object key = null, value = null;
            for(int number = input.readFieldNumber(this);; 
                    number = input.readFieldNumber(this))
            {
                switch(number)
                {
                    case 0:
                        entry.map.put(key, value);
                        return;
                    case 1:
                        if(key != null)
                        {
                            throw new ProtostuffException("The map was incorrectly " + 
                                    "serialized.");
                        }
                        key = input.mergeObject(entry, DYNAMIC_VALUE_SCHEMA);
                        if(entry != key)
                        {
                            // a reference.
                            assert key != null;
                        }
                        else
                        {
                            // entry held the key
                            key = entry.setValue(null);
                            assert key != null;
                        }
                        break;
                    case 2:
                        if(value != null)
                        {
                            throw new ProtostuffException("The map was incorrectly " + 
                                    "serialized.");
                        }
                        value = input.mergeObject(entry, DYNAMIC_VALUE_SCHEMA);
                        if(entry != value)
                        {
                            // a reference.
                            assert value != null;
                        }
                        else
                        {
                            // entry held the value
                            value = entry.setValue(null);
                            assert value != null;
                        }
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly " + 
                                    "serialized.");
                }
            }
        }
        
        public void writeTo(Output output, Entry<Object,Object> entry) 
        throws IOException
        {
            if(entry.getKey() != null)
                output.writeObject(1, entry.getKey(), DYNAMIC_VALUE_SCHEMA, false);
            
            if(entry.getValue() != null)
                output.writeObject(2, entry.getValue(), DYNAMIC_VALUE_SCHEMA, false);
        }
    };
    
    private static final Pipe.Schema<Entry<Object,Object>> ENTRY_PIPE_SCHEMA = 
        new Pipe.Schema<Entry<Object,Object>>(ENTRY_SCHEMA)
    {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException
        {
            for(int number = input.readFieldNumber(wrappedSchema);; 
                    number = input.readFieldNumber(wrappedSchema))
            {
                switch(number)
                {
                    case 0:
                        return;
                    case 1:
                        output.writeObject(number, pipe, DYNAMIC_VALUE_PIPE_SCHEMA, false);
                        break;
                    case 2:
                        output.writeObject(number, pipe, DYNAMIC_VALUE_PIPE_SCHEMA, false);
                        break;
                    default:
                        throw new ProtostuffException("The map was incorrectly " +
                                        "serialized.");
                }
            }
        }
    };
    
    private static final class MapWrapper implements Entry<Object,Object>
    {
        
        final Map<Object,Object> map;
        private Object value;
        
        MapWrapper(Map<Object,Object> map)
        {
            this.map = map;
        }

        public Object getKey()
        {
            throw new UnsupportedOperationException();
        }

        public Object getValue()
        {
            return value;
        }

        public Object setValue(Object value)
        {
            final Object last = this.value;
            this.value = value;
            return last;
        }
        
    }
    
    /**
     * An array wrapper internally used for adding objects.
     */
    private static final class ArrayWrapper implements Collection<Object>
    {
        final Object array;
        int offset = 0;
        
        ArrayWrapper(Object array)
        {
            this.array = array;
        }

        public boolean add(Object value)
        {
            Array.set(array, offset++, value);
            return true;
        }

        public boolean addAll(Collection<? extends Object> arg0)
        {
            throw new UnsupportedOperationException();
        }

        public void clear()
        {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object arg0)
        {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection<?> arg0)
        {
            throw new UnsupportedOperationException();
        }

        public boolean isEmpty()
        {
            throw new UnsupportedOperationException();
        }

        public Iterator<Object> iterator()
        {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object arg0)
        {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> arg0)
        {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> arg0)
        {
            throw new UnsupportedOperationException();
        }

        public int size()
        {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray()
        {
            throw new UnsupportedOperationException();
        }

        public <T> T[] toArray(T[] arg0)
        {
            throw new UnsupportedOperationException();
        }
    }
    
}
