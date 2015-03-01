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

package io.protostuff.parser;

import static io.protostuff.parser.ProtoUtil.toCamelCase;
import static io.protostuff.parser.ProtoUtil.toPascalCase;
import static io.protostuff.parser.ProtoUtil.toUnderscoreCase;

import java.util.Map;

/**
 * Formats a string.
 * 
 * @author David Yu
 * @created Nov 9, 2012
 */
public interface Formatter
{

    /**
     * Returns the formatted string.
     */
    public String format(String str);

    /**
     * Built-in formatters.
     */
    public enum BUILTIN implements Formatter
    {
        /**
         * uppercase.
         */
        UPPER
        {
            @Override
            public String format(String str)
            {
                return str.toUpperCase();
            }
        },

        /**
         * lowercase.
         */
        LOWER
        {
            @Override
            public String format(String str)
            {
                return str.toLowerCase();
            }
        },

        /**
         * camel-case.
         * <p>
         * 
         * <pre>
         * some_foo/SomeFoo becomes someFoo
         * </pre>
         */
        CC
        {
            @Override
            public String format(String str)
            {
                return toCamelCase(str).toString();
            }
        },

        /**
         * camel-case with trailing underscore.
         * <p>
         * 
         * <pre>
         * some_foo/SomeFoo/someFoo becomes someFoo_
         * </pre>
         */
        CCU
        {
            @Override
            public String format(String str)
            {
                return toCamelCase(str).append('_').toString();
            }
        },

        /**
         * underscore-case.
         * <p>
         * 
         * <pre>
         * someFoo/SomeFoo becomes some_foo
         * </pre>
         */
        UC
        {
            @Override
            public String format(String str)
            {
                return toUnderscoreCase(str).toString();
            }
        },

        /**
         * underscore-case with trailing underscore.
         * <p>
         * 
         * <pre>
         * someFoo/SomeFoo/some_foo becomes some_foo_
         * </pre>
         */
        UCU
        {
            @Override
            public String format(String str)
            {
                return toUnderscoreCase(str).append('_').toString();
            }
        },

        /**
         * "uppercased" underscore-case.
         * <p>
         * 
         * <pre>
         * someFoo/SomeFoo/some_foo becomes SOME_FOO
         * </pre>
         */
        UUC
        {
            @Override
            public String format(String str)
            {
                return toUnderscoreCase(str).toString().toUpperCase();
            }
        },

        /**
         * pascal-case.
         * <p>
         * 
         * <pre>
         * some_foo/someFoo becomes SomeFoo
         * </pre>
         */
        PC
        {
            @Override
            public String format(String str)
            {
                return toPascalCase(str).toString();
            }
        },

        /**
         * pascal-case with space in between.
         * <p>
         * 
         * <pre>
         * someFoo/some_foo/SomeFoo becomes "Some Foo"
         * </pre>
         */
        PCS
        {
            @Override
            public String format(String str)
            {
                final StringBuilder buffer = toUnderscoreCase(str);
                char c = buffer.charAt(0);
                if (c > 96 && c < 123)
                    buffer.setCharAt(0, (char) (c - 32));

                for (int i = 1, len = buffer.length(); i < len; i++)
                {
                    if (' ' == (c = buffer.charAt(i)))
                    {
                        // move to the next
                        if (++i != len && (c = buffer.charAt(i)) > 96 && c < 123)
                            buffer.setCharAt(i, (char) (c - 32));

                        continue;
                    }

                    if (c == '_' && len != i + 1)
                    {
                        buffer.setCharAt(i, ' ');

                        c = buffer.charAt(i + 1);
                        if (c > 96 && c < 123)
                            buffer.setCharAt(++i, (char) (c - 32));
                    }
                }

                return buffer.toString();
            }
        },

        /**
         * Same as PCS but with the more correct name :/.
         * <p>
         * The space "S" is really in-between.
         */
        PSC
        {
            @Override
            public String format(String str)
            {
                return PCS.format(str);
            }
        };

        /**
         * Add all the builtin formatters to the map using the enum's name as key.
         */
        public static void addAllTo(Map<String, Formatter> map)
        {
            for (BUILTIN bf : BUILTIN.values())
                map.put(bf.name(), bf);
        }
    }

}
