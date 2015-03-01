package io.protostuff.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class FieldTest
{
    @Test
    public void testBasicProtoTypes() throws Exception
    {
        Assert.assertEquals("double", new Field.Double().getProtoType());
        Assert.assertEquals("float", new Field.Float().getProtoType());
        Assert.assertEquals("int32", new Field.Int32().getProtoType());
        Assert.assertEquals("int64", new Field.Int64().getProtoType());
        Assert.assertEquals("uint32", new Field.UInt32().getProtoType());
        Assert.assertEquals("uint64", new Field.UInt64().getProtoType());
        Assert.assertEquals("sint32", new Field.SInt32().getProtoType());
        Assert.assertEquals("sint64", new Field.SInt64().getProtoType());
        Assert.assertEquals("fixed32", new Field.Fixed32().getProtoType());
        Assert.assertEquals("fixed64", new Field.Fixed64().getProtoType());
        Assert.assertEquals("sfixed32", new Field.SFixed32().getProtoType());
        Assert.assertEquals("sfixed64", new Field.SFixed64().getProtoType());
        Assert.assertEquals("bool", new Field.Bool().getProtoType());
        Assert.assertEquals("string", new Field.String().getProtoType());
        Assert.assertEquals("bytes", new Field.Bytes().getProtoType());
    }
}
