//================================================================================
//Copyright (c) 2012, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================

package io.protostuff.runtime;

import java.util.Collection;
import java.util.Map;

import io.protostuff.runtime.PolymorphicSchema.Handler;

/**
 * Polymorphic types.
 * 
 * @author David Yu
 * @created Apr 30, 2012
 */
public enum PolymorphicSchemaFactories implements PolymorphicSchema.Factory
{

    ARRAY
    {
        @Override
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            @SuppressWarnings("unchecked")
            Class<Object> ct = (Class<Object>) typeClass.getComponentType();

            RuntimeFieldFactory<?> rff = RuntimeFieldFactory.getFieldFactory(
                    ct, strategy);

            if (rff == RuntimeFieldFactory.DELEGATE)
            {
                // delegate
                return strategy.getDelegateWrapper(ct).newSchema(typeClass,
                        strategy, handler);
            }

            if (rff.id > 0 && rff.id < 15)
            {
                // scalar
                return ArraySchemas.newSchema(rff.id, ct, typeClass, strategy,
                        handler);
            }

            if (ct.isEnum())
            {
                // enum
                return strategy.getEnumIO(ct).newSchema(typeClass, strategy,
                        handler);
            }

            if (rff == RuntimeFieldFactory.POJO
                    || (rff == RuntimeFieldFactory.POLYMORPHIC_POJO && RuntimeFieldFactory
                            .pojo(ct, null, strategy)))
            {
                // pojo
                return strategy.getSchemaWrapper(ct, true).newSchema(typeClass,
                        strategy, handler);
            }

            return new ArraySchema(strategy)
            {
                @Override
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    },
    NUMBER
    {
        @Override
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            return new NumberSchema(strategy)
            {
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    },
    CLASS
    {
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            return new ClassSchema(strategy)
            {
                @Override
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    },
    ENUM
    {
        @Override
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            return new PolymorphicEnumSchema(strategy)
            {
                @Override
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    },
    COLLECTION
    {
        @Override
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            return new PolymorphicCollectionSchema(strategy)
            {
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    },
    MAP
    {
        @Override
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            return new PolymorphicMapSchema(strategy)
            {
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    },
    THROWABLE
    {
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            return new PolymorphicThrowableSchema(strategy)
            {
                @Override
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    },
    OBJECT
    {
        @Override
        public PolymorphicSchema newSchema(Class<?> typeClass,
                IdStrategy strategy, final Handler handler)
        {
            return new ObjectSchema(strategy)
            {
                @Override
                protected void setValue(Object value, Object owner)
                {
                    handler.setValue(value, owner);
                }
            };
        }
    };

    public static PolymorphicSchema.Factory getFactoryFromField(Class<?> clazz)
    {
        if (clazz.isArray())
            return ARRAY;

        if (Number.class == clazz)
            return NUMBER;

        if (Class.class == clazz)
            return CLASS;

        if (Enum.class == clazz)
            return ENUM;

        if (Map.class.isAssignableFrom(clazz))
            return MAP;

        if (Collection.class.isAssignableFrom(clazz))
            return COLLECTION;

        if (Throwable.class.isAssignableFrom(clazz))
            return THROWABLE;

        return OBJECT;
    }

    public static PolymorphicSchema.Factory getFactoryFromRepeatedValueGenericType(
            Class<?> clazz)
    {
        if (clazz.isArray())
            return ARRAY;

        if (Number.class == clazz)
            return NUMBER;

        if (Class.class == clazz)
            return CLASS;

        if (Enum.class == clazz)
            return ENUM;

        if (Throwable.class.isAssignableFrom(clazz))
            return THROWABLE;

        if (Object.class == clazz)
            return OBJECT;

        return null;
    }

    public static PolymorphicSchema getSchemaFromCollectionOrMapGenericType(
            Class<?> clazz, IdStrategy strategy)
    {
        if (clazz.isArray())
        {
            @SuppressWarnings("unchecked")
            Class<Object> ct = (Class<Object>) clazz.getComponentType();

            RuntimeFieldFactory<?> rff = RuntimeFieldFactory.getFieldFactory(
                    ct, strategy);

            if (rff == RuntimeFieldFactory.DELEGATE)
            {
                // delegate
                return strategy.getDelegateWrapper(ct).genericElementSchema;
            }

            if (rff.id > 0 && rff.id < 15)
            {
                // scalar
                return ArraySchemas.getGenericElementSchema(rff.id);
            }

            if (ct.isEnum())
            {
                // enum
                return strategy.getEnumIO(ct).genericElementSchema;
            }

            if (rff == RuntimeFieldFactory.POJO
                    || (rff == RuntimeFieldFactory.POLYMORPHIC_POJO && RuntimeFieldFactory
                            .pojo(ct, null, strategy)))
            {
                // pojo
                return strategy.getSchemaWrapper(ct, true).genericElementSchema;
            }

            return strategy.ARRAY_ELEMENT_SCHEMA;
        }

        if (Number.class == clazz)
            return strategy.NUMBER_ELEMENT_SCHEMA;

        if (Class.class == clazz)
            return strategy.CLASS_ELEMENT_SCHEMA;

        if (Enum.class == clazz)
            return strategy.POLYMORPHIC_ENUM_ELEMENT_SCHEMA;

        if (Throwable.class.isAssignableFrom(clazz))
            return strategy.POLYMORPHIC_THROWABLE_ELEMENT_SCHEMA;

        if (Object.class == clazz)
            return strategy.OBJECT_ELEMENT_SCHEMA;

        return null;
    }
}
