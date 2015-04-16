package io.protostuff.compiler.custom;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.protostuff.compiler.it.custom.E;
import io.protostuff.compiler.it.custom.PluralToSingularTestMessage;

/**
 * Test for custom formats - SINGULAR & PLURAL See src/test/stg/java_bean_with_plural_to_singular_format.java.stg :
 * {@code <field.name; format="PC&&SINGULAR">} transforms {@code field.name} to PascalCase in singular form, for example
 * - {@code errors} -> {@code Error}
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class PluralToSingularIT
{
    @Test
    public void testGeneratedClass() throws Exception
    {
        PluralToSingularTestMessage message = new PluralToSingularTestMessage();
        message.addError(E.A);
        assertEquals(1, message.getErrorCount());
        assertEquals(singletonList(E.A), message.getErrorList());
    }
}
