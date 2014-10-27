package io.protostuff.parser;

import org.junit.Test;

import static io.protostuff.parser.ProtoUtil.*;
import static org.junit.Assert.assertEquals;

public class ProtoUtilTest {

    @Test
    public void testToCamelCase() throws Exception {
        assertEquals("fooBarBaz", toCamelCase("foo_bar_baz").toString());
        assertEquals("fooBarBaz", toCamelCase("fooBarBaz").toString());
        assertEquals("fooBarBaz", toCamelCase("FooBarBaz").toString());
        assertEquals("fooBarBaz", toCamelCase("foo_bar_baz").toString());
        assertEquals("fooBarBaz", toCamelCase("____Foo____Bar___Baz____").toString());
    }

    @Test
    public void testToPascalCase() throws Exception {
        assertEquals("FooBarBaz", toPascalCase("foo_bar_baz").toString());
        assertEquals("FooBarBaz", toPascalCase("fooBarBaz").toString());
        assertEquals("FooBarBaz", toPascalCase("FooBarBaz").toString());
        assertEquals("FooBarBaz", toPascalCase("foo_bar_baz").toString());
        assertEquals("FooBarBaz", toPascalCase("____Foo____Bar___Baz____").toString());
    }


    @Test
    public void testToUnderscoreCase() throws Exception {
        assertEquals("foo_bar_baz", toUnderscoreCase("foo_bar_baz").toString());
        assertEquals("foo_bar_baz", toUnderscoreCase("fooBarBaz").toString());
        assertEquals("foo_bar_baz", toUnderscoreCase("FooBarBaz").toString());
        assertEquals("foo_bar_baz", toUnderscoreCase("foo_bar_baz").toString());
        assertEquals("foo_bar_baz", toUnderscoreCase("____Foo____Bar___Baz____").toString());
    }

}