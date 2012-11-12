//========================================================================
//Copyright 2012 David Yu
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

import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * A filter for the fields that need to be included.
 * 
 * @author David Yu
 * @created Nov 9, 2012
 */
public interface Predicate
{
    /**
     * Returns true if the field is included.
     */
    public boolean apply(Field<?> f);
    
    /**
     * Returns true if the field is included.
     * 
     * The predicate logic can be dynamic based on the contents of the message.
     */
    public boolean apply(Field<?> f, Object message);
    
    public interface Factory
    {
        /**
         * Creates a new predicate based from the args.
         */
        public Predicate create(String[] args);
    }
    
    
    /**
     * A predicate that includes only a single field with the provided number.
     */
    public static final class EQ implements Predicate, Predicate.Factory
    {
        final int num;
        EQ(int num) { this.num = num; }
        public boolean apply(Field<?> f) { return f.number == num; }
        public boolean apply(Field<?> f, Object message) { return f.number == num; }
        // instantiate this yourself and pass it to the view factory for re-use
        public Predicate create(String[] args) { return this; }
    }
    
    /**
     * A predicate that includes all fields except the provided number.
     */
    public static final class NOTEQ implements Predicate, Predicate.Factory
    {
        final int num;
        NOTEQ(int num) { this.num = num; }
        public boolean apply(Field<?> f) { return f.number != num; }
        public boolean apply(Field<?> f, Object message) { return f.number != num; }
        // instantiate this yourself and pass it to the view factory for re-use
        public Predicate create(String[] args) { return this; }
    }
    
    /**
     * A predicate that includes fields that are greater than the provider number.
     */
    public static final class GT implements Predicate, Predicate.Factory
    {
        final int num;
        GT(int num) { this.num = num; }
        public boolean apply(Field<?> f) { return f.number > num; }
        public boolean apply(Field<?> f, Object message) { return f.number > num; }
        // instantiate this yourself and pass it to the view factory for re-use
        public Predicate create(String[] args) { return this; }
    }
    
    /**
     * A predicate that includes fields that are lesser than the provider number.
     */
    public static final class LT implements Predicate, Predicate.Factory
    {
        final int num;
        LT(int num) { this.num = num; }
        public boolean apply(Field<?> f) { return f.number < num; }
        public boolean apply(Field<?> f, Object message) { return f.number < num; }
        // instantiate this yourself and pass it to the view factory for re-use
        public Predicate create(String[] args) { return this; }
    }
    
    /**
     * A predicate that includes fields if they are within range of the provided 
     * numbers, min and max.
     */
    public static final class RANGE implements Predicate, Predicate.Factory
    {
        final int min, max;
        RANGE(int min, int max) { this.min = min; this.max = max; }
        public boolean apply(Field<?> f) { return f.number >= min && f.number <= max; }
        public boolean apply(Field<?> f, Object message) { return f.number >= min && f.number <= max; }
        // instantiate this yourself and pass it to the view factory for re-use
        public Predicate create(String[] args) { return this; }
    }
    
    /**
     * The opposite of {@link RANGE}.
     */
    public static final class NOTRANGE implements Predicate, Predicate.Factory
    {
        final int min, max;
        NOTRANGE(int min, int max) { this.min = min; this.max = max; }
        public boolean apply(Field<?> f) { return f.number < min || f.number > max; }
        public boolean apply(Field<?> f, Object message) { return f.number < min || f.number > max; }
        // instantiate this yourself and pass it to the view factory for re-use
        public Predicate create(String[] args) { return this; }
    }
    
    /**
     * Built-in factories that filters based on the field number.
     */
    public enum Factories implements Predicate.Factory
    {
        EQ
        {
            public Predicate create(String[] args)
            {
                return new Predicate.EQ(Integer.parseInt(args[0]));
            }
        },
        NOTEQ
        {
            public Predicate create(String[] args)
            {
                return new Predicate.NOTEQ(Integer.parseInt(args[0]));
            }
        },
        GT
        {
            public Predicate create(String[] args)
            {
                return new Predicate.GT(Integer.parseInt(args[0]));
            }
        },
        LT
        {
            public Predicate create(String[] args)
            {
                return new Predicate.LT(Integer.parseInt(args[0]));
            }
        },
        RANGE
        {
            public Predicate create(String[] args)
            {
                return new Predicate.RANGE(Integer.parseInt(args[0]), 
                        Integer.parseInt(args[1]));
            }
        },
        NOTRANGE
        {
            public Predicate create(String[] args)
            {
                return new Predicate.NOTRANGE(Integer.parseInt(args[0]), 
                        Integer.parseInt(args[1]));
            }
        }
        ;
    }
}