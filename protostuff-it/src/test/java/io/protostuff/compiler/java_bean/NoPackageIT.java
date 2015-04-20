package io.protostuff.compiler.java_bean;

import io.protostuff.compiler.it.java_bean.NoPackage;
import org.junit.Assert;
import org.junit.Test;

public class NoPackageIT
{

    @Test
    public void testThatClassExists() throws Exception
    {
        // just call some method
        Assert.assertNotNull(NoPackage.getDefaultInstance());
    }
}
