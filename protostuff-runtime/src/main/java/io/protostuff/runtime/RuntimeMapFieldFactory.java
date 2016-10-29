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

package io.protostuff.runtime;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import io.protostuff.GraphInput;
import io.protostuff.Input;
import io.protostuff.MapSchema.MapWrapper;
import io.protostuff.MapSchema.MessageFactory;
import io.protostuff.Message;
import io.protostuff.Morph;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.Schema;
import io.protostuff.Tag;
import io.protostuff.WireFormat.FieldType;

/**
 * Static utility for creating runtime {@link java.util.Map} fields.
 * 
 * @author David Yu
 * @created Jan 21, 2011
 */
final class RuntimeMapFieldFactory
{

    private RuntimeMapFieldFactory()
    {
    }

    /*
     * private static final DerivativeSchema POLYMORPHIC_MAP_VALUE_SCHEMA = new DerivativeSchema() {
     * 
     * @SuppressWarnings("unchecked") protected void doMergeFrom(Input input, Schema<Object> derivedSchema, Object
     * owner) throws IOException { final Object value = derivedSchema.newMessage();
     * 
     * // the owner will always be the MapWrapper ((MapWrapper<Object,Object>)owner).setValue(value);
     * 
     * if(input instanceof GraphInput) { // update the actual reference. ((GraphInput)input).updateLast(value, owner); }
     * 
     * derivedSchema.mergeFrom(input, value); } };
     */

    /*
     * private static final ObjectSchema OBJECT_MAP_VALUE_SCHEMA = new ObjectSchema() {
     * 
     * @SuppressWarnings("unchecked") protected void setValue(Object value, Object owner) { // the owner will always be
     * the MapWrapper ((MapWrapper<Object,Object>)owner).setValue(value); } };
     */

    private static <T> Field<T> createMapInlineKEnumV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Delegate<Object> inlineK, final Class<Object> clazzV,
            final IdStrategy strategy)
    {
        final EnumIO<?> eioV = strategy.getEnumIO(clazzV);

        return new RuntimeMapField<T, Object, Enum<?>>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Enum<?>>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Enum<?>> existing;
                try
                {
                    existing = (Map<Object, Enum<?>>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Enum<?>> wrapper) throws IOException
            {
                return inlineK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Enum<?>> wrapper, Object key)
                    throws IOException
            {
                wrapper.put(key, eioV.readFrom(input));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Enum<?> val,
                    boolean repeated) throws IOException
            {
                eioV.writeTo(output, fieldNumber, repeated, val);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }
        };
    }

    private static <T> Field<T> createMapInlineKInlineV(int number,
            String name, final java.lang.reflect.Field f,
            MessageFactory messageFactory, final Delegate<Object> inlineK,
            final Delegate<Object> inlineV)
    {
        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return inlineK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                wrapper.put(key, inlineV.readFrom(input));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                inlineV.writeTo(output, fieldNumber, val, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineV.transfer(pipe, input, output, number, repeated);
            }
        };
    }

    private static <T> Field<T> createMapInlineKPojoV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Delegate<Object> inlineK, final Class<Object> clazzV,
            IdStrategy strategy)
    {
        final HasSchema<Object> schemaV = strategy.getSchemaWrapper(clazzV,
                true);

        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return inlineK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                wrapper.put(key, input.mergeObject(null, schemaV.getSchema()));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, schemaV.getSchema(),
                        repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(),
                        repeated);
            }
        };
    }

    private static <T> Field<T> createMapInlineKPolymorphicV(int number,
            String name, final java.lang.reflect.Field f,
            MessageFactory messageFactory, final Delegate<Object> inlineK,
            final Class<Object> clazzV, final IdStrategy strategy)
    {
        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return inlineK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA.pipeSchema,
                        repeated);
            }
        };
    }

    private static <T> Field<T> createMapInlineKObjectV(int number,
            String name, final java.lang.reflect.Field f,
            MessageFactory messageFactory, final Delegate<Object> inlineK,
            final Schema<Object> valueSchema,
            final Pipe.Schema<Object> valuePipeSchema, final IdStrategy strategy)
    {
        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return inlineK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                inlineK.writeTo(output, fieldNumber, key, repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineK.transfer(pipe, input, output, number, repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper, valueSchema);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, valueSchema, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, valuePipeSchema, repeated);
            }
        };
    }

    private static <T> Field<T> createMapEnumKEnumV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Class<Object> clazzV,
            final IdStrategy strategy)
    {
        final EnumIO<?> eioK = strategy.getEnumIO(clazzK);
        final EnumIO<?> eioV = strategy.getEnumIO(clazzV);

        return new RuntimeMapField<T, Enum<?>, Enum<?>>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Enum<?>, Enum<?>>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>, Enum<?>> existing;
                try
                {
                    existing = (Map<Enum<?>, Enum<?>>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Enum<?> kFrom(Input input,
                    MapWrapper<Enum<?>, Enum<?>> wrapper) throws IOException
            {
                return eioK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Enum<?> key,
                    boolean repeated) throws IOException
            {
                eioK.writeTo(output, fieldNumber, repeated, key);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Enum<?>, Enum<?>> wrapper, Enum<?> key)
                    throws IOException
            {
                wrapper.put(key, eioV.readFrom(input));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Enum<?> val,
                    boolean repeated) throws IOException
            {
                eioV.writeTo(output, fieldNumber, repeated, val);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }
        };
    }

    private static <T> Field<T> createMapEnumKInlineV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Delegate<Object> inlineV,
            final IdStrategy strategy)
    {
        final EnumIO<?> eioK = strategy.getEnumIO(clazzK);

        return new RuntimeMapField<T, Enum<?>, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Enum<?>, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>, Object> existing;
                try
                {
                    existing = (Map<Enum<?>, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Enum<?> kFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper) throws IOException
            {
                return eioK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Enum<?> key,
                    boolean repeated) throws IOException
            {
                eioK.writeTo(output, fieldNumber, repeated, key);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper, Enum<?> key)
                    throws IOException
            {
                wrapper.put(key, inlineV.readFrom(input));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                inlineV.writeTo(output, fieldNumber, val, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineV.transfer(pipe, input, output, number, repeated);
            }
        };
    }

    private static <T> Field<T> createMapEnumKPojoV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Class<Object> clazzV,
            final IdStrategy strategy)
    {
        final EnumIO<?> eioK = strategy.getEnumIO(clazzK);
        final HasSchema<Object> schemaV = strategy.getSchemaWrapper(clazzV,
                true);

        return new RuntimeMapField<T, Enum<?>, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Enum<?>, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>, Object> existing;
                try
                {
                    existing = (Map<Enum<?>, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Enum<?> kFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper) throws IOException
            {
                return eioK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Enum<?> key,
                    boolean repeated) throws IOException
            {
                eioK.writeTo(output, fieldNumber, repeated, key);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper, Enum<?> key)
                    throws IOException
            {
                wrapper.put(key, input.mergeObject(null, schemaV.getSchema()));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, schemaV.getSchema(),
                        repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(),
                        repeated);
            }
        };
    }

    private static <T> Field<T> createMapEnumKPolymorphicV(int number,
            String name, final java.lang.reflect.Field f,
            MessageFactory messageFactory, final Class<Object> clazzK,
            final Class<Object> clazzV, final IdStrategy strategy)
    {
        final EnumIO<?> eioK = strategy.getEnumIO(clazzK);

        return new RuntimeMapField<T, Enum<?>, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Enum<?>, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>, Object> existing;
                try
                {
                    existing = (Map<Enum<?>, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Enum<?> kFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper) throws IOException
            {
                return eioK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Enum<?> key,
                    boolean repeated) throws IOException
            {
                eioK.writeTo(output, fieldNumber, repeated, key);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper, Enum<?> key)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA.pipeSchema,
                        repeated);
            }
        };
    }

    private static <T> Field<T> createMapEnumKObjectV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Schema<Object> valueSchema,
            final Pipe.Schema<Object> valuePipeSchema, final IdStrategy strategy)
    {
        final EnumIO<?> eioK = strategy.getEnumIO(clazzK);

        return new RuntimeMapField<T, Enum<?>, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Enum<?>, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Enum<?>, Object> existing;
                try
                {
                    existing = (Map<Enum<?>, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Enum<?> kFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper) throws IOException
            {
                return eioK.readFrom(input);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Enum<?> key,
                    boolean repeated) throws IOException
            {
                eioK.writeTo(output, fieldNumber, repeated, key);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Enum<?>, Object> wrapper, Enum<?> key)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper, valueSchema);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, valueSchema, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, valuePipeSchema, repeated);
            }
        };
    }

    private static <T> Field<T> createMapPojoKEnumV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Class<Object> clazzV,
            final IdStrategy strategy)
    {
        final HasSchema<Object> schemaK = strategy.getSchemaWrapper(clazzK,
                true);
        final EnumIO<?> eioV = strategy.getEnumIO(clazzV);

        return new RuntimeMapField<T, Object, Enum<?>>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Enum<?>>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Enum<?>> existing;
                try
                {
                    existing = (Map<Object, Enum<?>>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Enum<?>> wrapper) throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(),
                        repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(),
                        repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Enum<?>> wrapper, Object key)
                    throws IOException
            {
                wrapper.put(key, eioV.readFrom(input));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Enum<?> val,
                    boolean repeated) throws IOException
            {
                eioV.writeTo(output, fieldNumber, repeated, val);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                EnumIO.transfer(pipe, input, output, number, repeated, strategy);
            }
        };
    }

    private static <T> Field<T> createMapPojoKInlineV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Delegate<Object> inlineV,
            IdStrategy strategy)
    {
        final HasSchema<Object> schemaK = strategy.getSchemaWrapper(clazzK,
                true);

        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(),
                        repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(),
                        repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                wrapper.put(key, inlineV.readFrom(input));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                inlineV.writeTo(output, fieldNumber, val, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                inlineV.transfer(pipe, input, output, number, repeated);
            }
        };
    }

    private static <T> Field<T> createMapPojoKPojoV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Class<Object> clazzV,
            IdStrategy strategy)
    {
        final HasSchema<Object> schemaK = strategy.getSchemaWrapper(clazzK,
                true);
        final HasSchema<Object> schemaV = strategy.getSchemaWrapper(clazzV,
                true);

        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {
            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(),
                        repeated);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(),
                        repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                wrapper.put(key, input.mergeObject(null, schemaV.getSchema()));
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, schemaV.getSchema(),
                        repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaV.getPipeSchema(),
                        repeated);
            }
        };
    }

    private static <T> Field<T> createMapPojoKPolymorphicV(int number,
            String name, final java.lang.reflect.Field f,
            MessageFactory messageFactory, final Class<Object> clazzK,
            final Class<Object> clazzV, final IdStrategy strategy)
    {
        final HasSchema<Object> schemaK = strategy.getSchemaWrapper(clazzK,
                true);

        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(),
                        repeated);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(),
                        repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe,
                        strategy.POLYMORPHIC_POJO_ELEMENT_SCHEMA.pipeSchema,
                        repeated);
            }
        };
    }

    private static <T> Field<T> createMapPojoKObjectV(int number, String name,
            final java.lang.reflect.Field f, MessageFactory messageFactory,
            final Class<Object> clazzK, final Schema<Object> valueSchema,
            final Pipe.Schema<Object> valuePipeSchema, final IdStrategy strategy)
    {
        final HasSchema<Object> schemaK = strategy.getSchemaWrapper(clazzK,
                true);

        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                return input.mergeObject(null, schemaK.getSchema());
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schemaK.getPipeSchema(),
                        repeated);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, schemaK.getSchema(),
                        repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper, valueSchema);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, valueSchema, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, valuePipeSchema, repeated);
            }
        };
    }

    private static <T> Field<T> createMapObjectKObjectV(int number,
            String name, final java.lang.reflect.Field f,
            MessageFactory messageFactory, final Schema<Object> keySchema,
            final Pipe.Schema<Object> keyPipeSchema,
            final Schema<Object> valueSchema,
            final Pipe.Schema<Object> valuePipeSchema, final IdStrategy strategy)
    {
        return new RuntimeMapField<T, Object, Object>(FieldType.MESSAGE,
                number, name, f.getAnnotation(Tag.class), messageFactory)
        {

            {
                f.setAccessible(true);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                try
                {
                    f.set(message, input.mergeObject(
                            (Map<Object, Object>) f.get(message), schema));
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Map<Object, Object> existing;
                try
                {
                    existing = (Map<Object, Object>) f.get(message);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                if (existing != null)
                    output.writeObject(number, existing, schema, false);
            }

            @Override
            protected void transfer(Pipe pipe, Input input, Output output,
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }

            @Override
            protected Object kFrom(Input input,
                    MapWrapper<Object, Object> wrapper) throws IOException
            {
                final Object value = input.mergeObject(wrapper, keySchema);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);
                    return value;
                }

                return wrapper.setValue(null);
            }

            @Override
            protected void kTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, keyPipeSchema, repeated);
            }

            @Override
            protected void kTo(Output output, int fieldNumber, Object key,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, key, keySchema, repeated);
            }

            @Override
            protected void vPutFrom(Input input,
                    MapWrapper<Object, Object> wrapper, Object key)
                    throws IOException
            {
                final Object value = input.mergeObject(wrapper, valueSchema);
                if (value != wrapper)
                {
                    // referenced.
                    // An entry would never have a cyclic reference.
                    ((GraphInput) input).updateLast(value, wrapper);

                    wrapper.put(key, value);
                    return;
                }

                if (key != null)
                {
                    // we can already add the entry.
                    wrapper.put(key, wrapper.setValue(null));
                }
            }

            @Override
            protected void vTo(Output output, int fieldNumber, Object val,
                    boolean repeated) throws IOException
            {
                output.writeObject(fieldNumber, val, valueSchema, repeated);
            }

            @Override
            protected void vTransfer(Pipe pipe, Input input, Output output,
                    int number, boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, valuePipeSchema, repeated);
            }
        };
    }

    static final RuntimeFieldFactory<Map<?, ?>> MAP = new RuntimeFieldFactory<Map<?, ?>>(
            RuntimeFieldFactory.ID_MAP)
    {

        @Override
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name,
                final java.lang.reflect.Field f, IdStrategy strategy)
        {
            final Class<?> clazz = f.getType();
            final Morph morph = f.getAnnotation(Morph.class);
            
            if (0 != (IdStrategy.POJO_SCHEMA_ON_MAP_FIELDS & strategy.flags) && 
                    (morph == null || morph.value()))
            {
                if (!clazz.getName().startsWith("java.util") && 
                        pojo(clazz, morph, strategy))
                {
                    return POJO.create(number, name, f, strategy);
                }
                
                return OBJECT.create(number, name, f, strategy);
            }
            
            if (Modifier.isAbstract(clazz.getModifiers()))
            {
                if (!clazz.isInterface())
                {
                    // abstract class
                    return OBJECT.create(number, name, f, strategy);
                }

                if (morph == null)
                {
                    if (0 != (IdStrategy.MORPH_MAP_INTERFACES & strategy.flags))
                        return OBJECT.create(number, name, f, strategy);
                }
                else if (morph.value())
                    return OBJECT.create(number, name, f, strategy);
            }

            final MessageFactory messageFactory;
            if (EnumMap.class.isAssignableFrom(f.getType()))
            {
                final Class<Object> enumType = (Class<Object>) getGenericType(
                        f, 0);
                if (enumType == null)
                {
                    // still handle the serialization of EnumMaps even without
                    // generics
                    return RuntimeFieldFactory.OBJECT.create(number, name, f,
                            strategy);
                }

                messageFactory = strategy.getEnumIO(enumType)
                        .getEnumMapFactory();
            }
            else
            {
                messageFactory = strategy.getMapFactory(f.getType());
            }

            final Class<Object> clazzK = (Class<Object>) getGenericType(f, 0);
            if (clazzK == null || ((Map.class.isAssignableFrom(clazzK) || 
                    Collection.class.isAssignableFrom(clazzK)) && 
                    !strategy.isRegistered(clazzK)))
            {
                // the key is not a simple parameterized type.
                return createMapObjectKObjectV(number, name, f, messageFactory,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }

            final Class<Object> clazzV = (Class<Object>) getGenericType(f, 1);
            if (clazzV == null || ((Map.class.isAssignableFrom(clazzV) || 
                    Collection.class.isAssignableFrom(clazzV)) && 
                    !strategy.isRegistered(clazzV)))
            {
                // the value is not a simple parameterized type.
                final Delegate<Object> inlineK = getDelegateOrInline(clazzK,
                        strategy);
                if (inlineK != null)
                {
                    return createMapInlineKObjectV(number, name, f,
                            messageFactory, inlineK,
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                if (Message.class.isAssignableFrom(clazzK))
                {
                    return createMapPojoKObjectV(number, name, f,
                            messageFactory, clazzK,
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                if (clazzK.isEnum())
                {
                    return createMapEnumKObjectV(number, name, f,
                            messageFactory, clazzK,
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                final PolymorphicSchema psK = PolymorphicSchemaFactories
                        .getSchemaFromCollectionOrMapGenericType(clazzK,
                                strategy);

                if (psK != null)
                {
                    return createMapObjectKObjectV(number, name, f,
                            messageFactory, psK, psK.getPipeSchema(),
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                if (pojo(clazzK, morph, strategy))
                {
                    return createMapPojoKObjectV(number, name, f,
                            messageFactory, clazzK,
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                /*
                 * if(clazzK.isInterface()) { return createMapObjectKObjectV(number, name, f, messageFactory,
                 * strategy.OBJECT_COLLECTION_VALUE_SCHEMA, strategy.OBJECT_COLLECTION_VALUE_SCHEMA.pipeSchema,
                 * strategy.OBJECT_COLLECTION_VALUE_SCHEMA, strategy.OBJECT_COLLECTION_VALUE_SCHEMA.pipeSchema,
                 * strategy); }
                 * 
                 * // TODO add createMapPolymorphicKObjectV?
                 */
                return createMapObjectKObjectV(number, name, f, messageFactory,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema,
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }

            final Delegate<Object> inlineK = getDelegateOrInline(clazzK,
                    strategy);

            if (inlineK != null)
            {
                final Delegate<Object> inlineV = getDelegateOrInline(clazzV,
                        strategy);
                if (inlineV != null)
                    return createMapInlineKInlineV(number, name, f,
                            messageFactory, inlineK, inlineV);

                if (Message.class.isAssignableFrom(clazzV))
                    return createMapInlineKPojoV(number, name, f,
                            messageFactory, inlineK, clazzV, strategy);

                if (clazzV.isEnum())
                    return createMapInlineKEnumV(number, name, f,
                            messageFactory, inlineK, clazzV, strategy);

                final PolymorphicSchema psV = PolymorphicSchemaFactories
                        .getSchemaFromCollectionOrMapGenericType(clazzV,
                                strategy);
                if (psV != null)
                {
                    return createMapInlineKObjectV(number, name, f,
                            messageFactory, inlineK, psV, psV.getPipeSchema(),
                            strategy);
                }

                if (pojo(clazzV, f.getAnnotation(Morph.class), strategy))
                    return createMapInlineKPojoV(number, name, f,
                            messageFactory, inlineK, clazzV, strategy);

                if (clazzV.isInterface())
                {
                    return createMapInlineKObjectV(number, name, f,
                            messageFactory, inlineK,
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                return createMapInlineKPolymorphicV(number, name, f,
                        messageFactory, inlineK, clazzV, strategy);
            }

            if (clazzK.isEnum())
            {
                final Delegate<Object> inlineV = getDelegateOrInline(clazzV,
                        strategy);
                if (inlineV != null)
                    return createMapEnumKInlineV(number, name, f,
                            messageFactory, clazzK, inlineV, strategy);

                if (Message.class.isAssignableFrom(clazzV))
                    return createMapEnumKPojoV(number, name, f, messageFactory,
                            clazzK, clazzV, strategy);

                if (clazzV.isEnum())
                    return createMapEnumKEnumV(number, name, f, messageFactory,
                            clazzK, clazzV, strategy);

                final PolymorphicSchema psV = PolymorphicSchemaFactories
                        .getSchemaFromCollectionOrMapGenericType(clazzV,
                                strategy);
                if (psV != null)
                {
                    return createMapEnumKObjectV(number, name, f,
                            messageFactory, clazzK, psV, psV.getPipeSchema(),
                            strategy);
                }

                if (pojo(clazzV, f.getAnnotation(Morph.class), strategy))
                    return createMapEnumKPojoV(number, name, f, messageFactory,
                            clazzK, clazzV, strategy);

                if (clazzV.isInterface())
                {
                    return createMapEnumKObjectV(number, name, f,
                            messageFactory, clazzK,
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                return createMapEnumKPolymorphicV(number, name, f,
                        messageFactory, clazzK, clazzV, strategy);
            }

            final PolymorphicSchema psK = PolymorphicSchemaFactories
                    .getSchemaFromCollectionOrMapGenericType(clazzK, strategy);
            if (psK != null)
            {
                return createMapObjectKObjectV(number, name, f, messageFactory,
                        psK, psK.getPipeSchema(),
                        strategy.OBJECT_ELEMENT_SCHEMA,
                        strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
            }

            if (pojo(clazzK, f.getAnnotation(Morph.class), strategy))
            {
                final Delegate<Object> inlineV = getDelegateOrInline(clazzV,
                        strategy);
                if (inlineV != null)
                    return createMapPojoKInlineV(number, name, f,
                            messageFactory, clazzK, inlineV, strategy);

                if (Message.class.isAssignableFrom(clazzV))
                    return createMapPojoKPojoV(number, name, f, messageFactory,
                            clazzK, clazzV, strategy);

                if (clazzV.isEnum())
                    return createMapPojoKEnumV(number, name, f, messageFactory,
                            clazzK, clazzV, strategy);

                final PolymorphicSchema psV = PolymorphicSchemaFactories
                        .getSchemaFromCollectionOrMapGenericType(clazzV,
                                strategy);
                if (psV != null)
                {
                    return createMapPojoKObjectV(number, name, f,
                            messageFactory, clazzK, psV, psV.getPipeSchema(),
                            strategy);
                }

                if (pojo(clazzV, f.getAnnotation(Morph.class), strategy))
                    return createMapPojoKPojoV(number, name, f, messageFactory,
                            clazzK, clazzV, strategy);

                if (clazzV.isInterface())
                {
                    return createMapPojoKObjectV(number, name, f,
                            messageFactory, clazzK,
                            strategy.OBJECT_ELEMENT_SCHEMA,
                            strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
                }

                return createMapPojoKPolymorphicV(number, name, f,
                        messageFactory, clazzK, clazzV, strategy);
            }

            return createMapObjectKObjectV(number, name, f, messageFactory,
                    strategy.OBJECT_ELEMENT_SCHEMA,
                    strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema,
                    strategy.OBJECT_ELEMENT_SCHEMA,
                    strategy.OBJECT_ELEMENT_SCHEMA.pipeSchema, strategy);
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<?, ?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeTo(Output output, int number, Map<?, ?> value,
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<?> typeClass()
        {
            throw new UnsupportedOperationException();
        }
    };

}
