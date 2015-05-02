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

package io.protostuff.compiler;

import static io.protostuff.parser.ProtoUtil.toCamelCase;
import static io.protostuff.parser.ProtoUtil.toPascalCase;
import static io.protostuff.parser.ProtoUtil.toUnderscoreCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        },

        /**
         * Transform word in singular form to plural form. If given word is already in the plural form, it should not be
         * changed.
         *
         * Please not that this formatter does not guarantee that all words can be transformed correctly.
         *
         * The implementation was cloned from
         * https://github.com/javalite/activejdbc/blob/master/javalite-common/src/main
         * /java/org/javalite/common/Inflector.java
         *
         * Copyright 2009-2014 Igor Polevoy Licensed under the Apache License, Version 2.0 (the "License"); you may not
         * use this file except in compliance with the License. You may obtain a copy of the License at
         * http://www.apache.org/licenses/LICENSE-2.0
         */
        PLURAL
        {
            @Override
            public String format(String str)
            {
                return pluralize(str);
            }
        },

        /**
         * Transform word in plural form to singular form. If given word is already in the singular form, it should not
         * be changed.
         *
         * Please not that this formatter does not guarantee that all words can be transformed correctly.
         *
         * The implementation was cloned from
         * https://github.com/javalite/activejdbc/blob/master/javalite-common/src/main
         * /java/org/javalite/common/Inflector.java
         *
         * Copyright 2009-2014 Igor Polevoy Licensed under the Apache License, Version 2.0 (the "License"); you may not
         * use this file except in compliance with the License. You may obtain a copy of the License at
         * http://www.apache.org/licenses/LICENSE-2.0
         */
        SINGULAR
        {
            @Override
            public String format(String str)
            {
                return singularize(str);
            }
        },

        TRIM
        {
            @Override
            public String format(String str) {
                return str.trim();
            }
        },

        CUT_L
        {
            @Override
            public String format(String str) {
                if (str.isEmpty()) {
                    return str;
                }
                return str.substring(1, str.length());
            }
        },

        CUT_R
        {
            @Override
            public String format(String str) {
                if (str.isEmpty()) {
                    return str;
                }
                return str.substring(0, str.length()-1);
            }
        },

        ;
        private static List<String[]> singulars, plurals, irregulars;
        private static List<String> uncountables;

        static
        {
            singulars = new ArrayList<>();
            plurals = new ArrayList<>();
            irregulars = new ArrayList<>();
            uncountables = new ArrayList<>();

            addPlural("$", "s");
            addPlural("s$", "s");
            addPlural("(ax|test)is$", "$1es");
            addPlural("(octop|vir)us$", "$1i");
            addPlural("(alias|status)$", "$1es");
            addPlural("(bu)s$", "$1ses");
            addPlural("(buffal|tomat)o$", "$1oes");
            addPlural("([ti])um$", "$1a");
            addPlural("sis$", "ses");
            addPlural("(?:([^f])fe|([lr])f)$", "$1$2ves");
            addPlural("(hive)$", "$1s");
            addPlural("([^aeiouy]|qu)y$", "$1ies");
            addPlural("(x|ch|ss|sh)$", "$1es");
            addPlural("(matr|vert|ind)(?:ix|ex)$", "$1ices");
            addPlural("([m|l])ouse$", "$1ice");
            addPlural("^(ox)$", "$1en");
            addPlural("(quiz)$", "$1zes");

            addSingular("s$", "");
            addSingular("(n)ews$", "$1ews");
            // this rule cause issues with words like "delta"
            // as we do not use latin words in the technical documentation
            // we disable this rule
            // addSingular("([ti])a$", "$1um");
            addSingular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1sis");
            addSingular("(^analy)ses$", "$1sis");
            addSingular("([^f])ves$", "$1fe");
            addSingular("(hive)s$", "$1");
            addSingular("(tive)s$", "$1");
            addSingular("([lr])ves$", "$1f");
            addSingular("([^aeiouy]|qu)ies$", "$1y");
            addSingular("(s)eries$", "$1eries");
            addSingular("(m)ovies$", "$1ovie");
            addSingular("(x|ch|ss|sh)es$", "$1");
            addSingular("([m|l])ice$", "$1ouse");
            addSingular("(bus)es$", "$1");
            addSingular("(o)es$", "$1");
            addSingular("(shoe)s$", "$1");
            addSingular("(cris|ax|test)es$", "$1is");
            addSingular("(octop|vir)i$", "$1us");
            addSingular("(alias|status)es$", "$1");
            addSingular("^(ox)en", "$1");
            addSingular("(vert|ind)ices$", "$1ex");
            addSingular("(matr)ices$", "$1ix");
            addSingular("(quiz)zes$", "$1");
            addSingular("(database)s$", "$1");

            addIrregular("person", "people");
            addIrregular("man", "men");
            addIrregular("child", "children");
            addIrregular("sex", "sexes");
            addIrregular("move", "moves");

            uncountables = Arrays.asList("equipment", "information", "rice", "money", "species", "series", "fish",
                    "sheep", "data", "metadata");
        }

        private static void addPlural(String rule, String replacement)
        {
            plurals.add(0, new String[] { rule, replacement });
        }

        private static void addSingular(String rule, String replacement)
        {
            singulars.add(0, new String[] { rule, replacement });
        }

        private static void addIrregular(String rule, String replacement)
        {
            irregulars.add(new String[] { rule, replacement });
        }

        /**
         * Replaces a found pattern in a word and returns a transformed word. Null is pattern does not match.
         */
        private static String gsub(String word, String rule, String replacement)
        {
            Pattern pattern = Pattern.compile(rule, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(word);
            return matcher.find() ? matcher.replaceFirst(replacement) : null;
        }

        private static String pluralize(String word)
        {

            if (uncountables.contains(word))
                return word;

            for (String[] irregular : irregulars)
            {
                if (irregular[0].equalsIgnoreCase(word))
                {
                    return irregular[1];
                }
            }

            for (String[] pair : plurals)
            {
                String plural = gsub(word, pair[0], pair[1]);
                if (plural != null)
                    return plural;
            }

            return word;
        }

        private static String singularize(String word)
        {

            if (uncountables.contains(word))
                return word;

            for (String[] irregular : irregulars)
            {
                if (irregular[1].equalsIgnoreCase(word))
                {
                    return irregular[0];
                }
            }

            for (String[] pair : singulars)
            {
                String singular = gsub(word, pair[0], pair[1]);
                if (singular != null)
                    return singular;
            }

            return word;
        }

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
