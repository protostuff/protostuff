//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import com.dyuproject.protostuff.parser.Field.Modifier;

/**
 * Various tests for the proto parser.
 *
 * @author David Yu
 * @created Dec 18, 2009
 */
public class ProtoParserTest extends TestCase
{
    
    static URL getResource(String path) throws IOException
    {
        return Thread.currentThread().getContextClassLoader().getResource(path);
    }
    
    static File getFile(String path)
    {
        return new File(new File("src/test/resources"), path);
    }
    
    public void testSimple() throws Exception
    {
        File f = getFile("TestModel.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        assertEquals(proto.getPackageName(), "simple");
        assertEquals(proto.getJavaPackageName(), "com.example.simple");
        assertTrue(proto.getEnumGroups().size() == 0);
        assertTrue(proto.getMessages().size() == 3);
        
        Message foo = proto.getMessage("Foo");
        Message bar = proto.getMessage("Bar");
        Message baz = proto.getMessage("Baz");
        assertNotNull(foo);
        assertNotNull(bar);
        assertNotNull(baz);
        
        assertTrue(foo.getNestedEnumGroups().size() == 1);
        EnumGroup enumSample = foo.getNestedEnumGroup("EnumSample");
        assertNotNull(enumSample);
        assertTrue(enumSample.getValues().size() == 5);
        assertEquals("TYPE0", enumSample.getValue(0).getName());
        assertEquals("TYPE1", enumSample.getValue(1).getName());
        assertEquals("TYPE2", enumSample.getValue(2).getName());
        assertEquals("TYPE3", enumSample.getValue(3).getName());
        assertEquals("TYPE4", enumSample.getValue(4).getName());
        
        assertTrue(foo.getFields().size() == 9);
        Field.Int32 foo_some_int = (Field.Int32)foo.getField("some_int");
        Field.String foo_some_string = (Field.String)foo.getField("some_string");
        MessageField foo_bar = (MessageField)foo.getField("bar");
        EnumField foo_some_enum = (EnumField)foo.getField("some_enum");
        Field.Bytes foo_some_bytes = (Field.Bytes)foo.getField("some_bytes");
        Field.Bool foo_some_boolean = (Field.Bool)foo.getField("some_boolean");
        Field.Float foo_some_float = (Field.Float)foo.getField("some_float");
        Field.Double foo_some_double = (Field.Double)foo.getField("some_double");
        Field.Int64 foo_some_long = (Field.Int64)foo.getField("some_long");
        
        assertTrue(foo_some_int !=null && foo_some_int.modifier == Modifier.REPEATED);
        assertTrue(foo_some_string !=null && foo_some_string.modifier == Modifier.REPEATED);
        assertTrue(foo_bar !=null && foo_bar.modifier == Modifier.REPEATED);
        assertTrue(foo_some_enum !=null && foo_some_enum.modifier == Modifier.REPEATED);
        assertTrue(foo_some_bytes !=null && foo_some_bytes.modifier == Modifier.REPEATED);
        assertTrue(foo_some_boolean !=null && foo_some_boolean.modifier == Modifier.REPEATED);
        assertTrue(foo_some_float !=null && foo_some_float.modifier == Modifier.REPEATED);
        assertTrue(foo_some_double !=null && foo_some_double.modifier == Modifier.REPEATED);
        assertTrue(foo_some_long !=null && foo_some_long.modifier == Modifier.REPEATED);
        
        Field.Int32 bar_some_int = (Field.Int32)bar.getField("some_int");
        Field.String bar_some_string = (Field.String)bar.getField("some_string");
        MessageField bar_baz = (MessageField)bar.getField("baz");
        EnumField bar_some_enum = (EnumField)bar.getField("some_enum");
        Field.Bytes bar_some_bytes = (Field.Bytes)bar.getField("some_bytes");
        Field.Bool bar_some_boolean = (Field.Bool)bar.getField("some_boolean");
        Field.Float bar_some_float = (Field.Float)bar.getField("some_float");
        Field.Double bar_some_double = (Field.Double)bar.getField("some_double");
        Field.Int64 bar_some_long = (Field.Int64)bar.getField("some_long");
        
        assertTrue(bar_some_int !=null && bar_some_int.modifier == Modifier.OPTIONAL);
        assertTrue(bar_some_string !=null && bar_some_string.modifier == Modifier.OPTIONAL);
        assertTrue(bar_baz !=null && bar_baz.modifier == Modifier.OPTIONAL);
        assertTrue(bar_some_enum !=null && bar_some_enum.modifier == Modifier.OPTIONAL);
        assertTrue(bar_some_bytes !=null && bar_some_bytes.modifier == Modifier.OPTIONAL);
        assertTrue(bar_some_boolean !=null && bar_some_boolean.modifier == Modifier.OPTIONAL);
        assertTrue(bar_some_float !=null && bar_some_float.modifier == Modifier.OPTIONAL);
        assertTrue(bar_some_double !=null && bar_some_double.modifier == Modifier.OPTIONAL);
        assertTrue(bar_some_long !=null && bar_some_long.modifier == Modifier.OPTIONAL);

        Field.Int64 baz_id = (Field.Int64)baz.getField("id");
        Field.String baz_name = (Field.String)baz.getField("name");
        Field.Int64 baz_timestamp = (Field.Int64)baz.getField("timestamp");
        Field.Bytes baz_data = (Field.Bytes)baz.getField("data");
        
        assertTrue(baz_id !=null && baz_id.modifier == Modifier.REQUIRED);
        assertTrue(baz_name !=null && baz_name.modifier == Modifier.OPTIONAL);
        assertTrue(baz_timestamp !=null && baz_timestamp.modifier == Modifier.OPTIONAL);
        assertTrue(baz_data !=null && baz_data.modifier == Modifier.OPTIONAL);
        
        assertEquals(bar_some_int.defaultValue, Integer.valueOf(127));
        assertEquals(new String(bar_some_string.defaultValue.getBytes(TextFormat.ISO_8859_1), "UTF-8"), "\u1234");
        assertEquals(bar_some_float.defaultValue, Float.valueOf(127.0f));
        assertEquals(bar_some_double.defaultValue, Double.valueOf(45.123));
        byte[] data = baz_data.getDefaultValue();
        assertTrue(data!=null && data.length == 2);
        assertTrue((data[0] & 0xFF) == 0xFA);
        assertTrue((data[1] & 0xFF)== 0xCE);
    }
    
    public void testImport() throws Exception
    {
        File f = getFile("unittest.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        
        Proto iProto = proto.getImportedProto(getFile("google/protobuf/unittest_import.proto"));
        assertNotNull(iProto);
        assertEquals("protobuf_unittest_import", iProto.getPackageName());
        assertEquals("com.google.protobuf.test", iProto.getJavaPackageName());
        assertTrue(iProto.getMessages().size() == 1);
        assertTrue(iProto.getEnumGroups().size() == 1);
        
        EnumGroup importEnum = iProto.getEnumGroup("ImportEnum");
        assertNotNull(importEnum);
        assertTrue(importEnum.values.size() == 3);
        assertTrue(importEnum.getValue("IMPORT_FOO").number == 7);
        assertTrue(importEnum.getValue("IMPORT_BAR").number == 8);
        assertTrue(importEnum.getValue("IMPORT_BAZ").number == 9);
        
        Message importMessage = iProto.getMessage("ImportMessage");        
        assertNotNull(importMessage);
        
        assertTrue(importMessage.getFields().size() == 1);
        Field.Int32 import_message_d = (Field.Int32)importMessage.getField("d");
        assertTrue(import_message_d != null);
        assertTrue(import_message_d.modifier == Modifier.OPTIONAL);
        assertTrue(import_message_d.number == 1);
        assertTrue(import_message_d.defaultValue == null);
        
        // unittest.proto
        
        assertEquals("protobuf_unittest", proto.getPackageName());
        assertEquals(proto.getJavaPackageName(), proto.getPackageName());

        assertTrue(proto.getEnumGroups().size() == 3);
        
        EnumGroup foreignEnum = proto.getEnumGroup("ForeignEnum");
        assertNotNull(foreignEnum);
        assertTrue(foreignEnum.getValues().size() == 3);
        
        EnumGroup testEnumWithDupValue = proto.getEnumGroup("TestEnumWithDupValue");
        assertNotNull(testEnumWithDupValue);
        assertTrue(testEnumWithDupValue.getValues().size() == 5);
        assertTrue(testEnumWithDupValue.getSortedValues().get(0).name.equals("FOO1"));
        assertTrue(testEnumWithDupValue.getSortedValues().get(0).number == 1);
        assertTrue(testEnumWithDupValue.getSortedValues().get(1).name.equals("FOO2"));
        assertTrue(testEnumWithDupValue.getSortedValues().get(1).number == 1);
        assertTrue(testEnumWithDupValue.getSortedValues().get(2).name.equals("BAR1"));
        assertTrue(testEnumWithDupValue.getSortedValues().get(2).number == 2);
        assertTrue(testEnumWithDupValue.getSortedValues().get(3).name.equals("BAR2"));
        assertTrue(testEnumWithDupValue.getSortedValues().get(3).number == 2);
        assertTrue(testEnumWithDupValue.getSortedValues().get(4).name.equals("BAZ"));
        assertTrue(testEnumWithDupValue.getSortedValues().get(4).number == 3);
        
        EnumGroup testSparseEnum = proto.getEnumGroup("TestSparseEnum");
        assertNotNull(testSparseEnum);
        assertTrue(testSparseEnum.getValues().size() == 7);
        assertTrue(testSparseEnum.getSortedValues().get(0).name.equals("SPARSE_E"));
        assertTrue(testSparseEnum.getSortedValues().get(0).number == -53452);
        assertTrue(testSparseEnum.getSortedValues().get(1).name.equals("SPARSE_D"));
        assertTrue(testSparseEnum.getSortedValues().get(1).number == -15);
        assertTrue(testSparseEnum.getSortedValues().get(2).name.equals("SPARSE_F"));
        assertTrue(testSparseEnum.getSortedValues().get(2).number == 0);
        assertTrue(testSparseEnum.getSortedValues().get(3).name.equals("SPARSE_G"));
        assertTrue(testSparseEnum.getSortedValues().get(3).number == 2);
        assertTrue(testSparseEnum.getSortedValues().get(4).name.equals("SPARSE_A"));
        assertTrue(testSparseEnum.getSortedValues().get(4).number == 123);
        assertTrue(testSparseEnum.getSortedValues().get(5).name.equals("SPARSE_B"));
        assertTrue(testSparseEnum.getSortedValues().get(5).number == 62374);
        assertTrue(testSparseEnum.getSortedValues().get(6).name.equals("SPARSE_C"));
        assertTrue(testSparseEnum.getSortedValues().get(6).number == 12589234);
        
        
        Message testAllTypes = proto.getMessage("TestAllTypes");
        assertNotNull(testAllTypes);
        assertTrue(testAllTypes.getNestedMessages().size() == 1);
        assertTrue(testAllTypes.getNestedEnumGroups().size() == 1);
        
        Field<?> defaultStringPiece = testAllTypes.getField("default_string_piece");
        Field<?> defaultCord = testAllTypes.getField("default_cord");
        
        assertNotNull(defaultStringPiece);
        assertEquals("STRING_PIECE", defaultStringPiece.getOption("ctype"));
        assertEquals("abc", defaultStringPiece.getOption("default"));
        assertEquals("abc", defaultStringPiece.defaultValue);
        
        assertNotNull(defaultCord);
        assertEquals("CORD", defaultCord.getOption("ctype"));
        assertEquals("123", defaultCord.getOption("default"));
        assertEquals("123", defaultCord.defaultValue);
        
        Message nestedMessage = testAllTypes.getNestedMessage("NestedMessage");
        assertNotNull(nestedMessage);
        EnumGroup nestedEnum = testAllTypes.getNestedEnumGroup("NestedEnum");
        assertNotNull(nestedEnum);
        
        Message foreignMessage = proto.getMessage("ForeignMessage");
        assertNotNull(foreignMessage);
        
        EnumField optional_nested_enum = testAllTypes.getField("optional_nested_enum", 
                EnumField.class);
        assertNotNull(optional_nested_enum);
        assertTrue(nestedEnum == optional_nested_enum.getEnumGroup());
        
        EnumField optional_foreign_enum = testAllTypes.getField("optional_foreign_enum", 
                EnumField.class);
        assertNotNull(optional_foreign_enum);
        assertTrue(foreignEnum == optional_foreign_enum.getEnumGroup());
        
        EnumField optional_import_enum = testAllTypes.getField("optional_import_enum", 
                EnumField.class);
        assertNotNull(optional_import_enum);
        assertTrue(importEnum == optional_import_enum.getEnumGroup());
        
        MessageField optional_nested_message = testAllTypes.getField("optional_nested_message", 
                MessageField.class);
        assertNotNull(optional_nested_message);
        assertTrue(nestedMessage == optional_nested_message.getMessage());
        
        MessageField optional_foreign_message = testAllTypes.getField("optional_foreign_message", 
                MessageField.class);
        assertNotNull(optional_foreign_message);
        assertTrue(foreignMessage == optional_foreign_message.getMessage());
        
        MessageField optional_import_message = testAllTypes.getField("optional_import_message", 
                MessageField.class);
        assertNotNull(optional_import_message);
        assertTrue(importMessage == optional_import_message.getMessage());
        
        Message testRequiredForeign = proto.getMessage("TestRequiredForeign");
        assertNotNull(testRequiredForeign);
        assertTrue(testRequiredForeign.getFields().size() == 3);
        
        MessageField test_required_foreign_optional_message = testRequiredForeign.getField("optional_message", 
                MessageField.class);
        assertNotNull(test_required_foreign_optional_message);
        assertTrue(test_required_foreign_optional_message.modifier == Modifier.OPTIONAL);
        
        MessageField test_required_foreign_repeated_message = testRequiredForeign.getField("repeated_message", 
                MessageField.class);
        assertNotNull(test_required_foreign_repeated_message);
        assertTrue(test_required_foreign_repeated_message.modifier == Modifier.REPEATED);
        
        Field.Int32 dummy = testRequiredForeign.getField("dummy", Field.Int32.class);
        assertNotNull(dummy);
        assertTrue(dummy.modifier == Modifier.OPTIONAL && dummy.number == 3);
        
        Message testForeignNested = proto.getMessage("TestForeignNested");
        assertNotNull(testForeignNested);
        MessageField foreign_nested = testForeignNested.getField("foreign_nested", 
                MessageField.class);
        assertNotNull(foreign_nested);
        assertTrue(nestedMessage == foreign_nested.getMessage());
        
        Message testEmptyMessage = proto.getMessage("TestEmptyMessage");
        assertNotNull(testEmptyMessage);
        assertTrue(testEmptyMessage.getFields().size() == 0);
        assertTrue(testEmptyMessage.getNestedEnumGroups().size() == 0);
        assertTrue(testEmptyMessage.getNestedMessages().size() == 0);
        
        Message testReallyLargeTagNumber = proto.getMessage("TestReallyLargeTagNumber");
        assertNotNull(testReallyLargeTagNumber);
        
        Field.Int32 a = testReallyLargeTagNumber.getField("a", Field.Int32.class);
        assertNotNull(a);
        assertTrue(a.number == 1);
        Field.Int32 bb = testReallyLargeTagNumber.getField("bb", Field.Int32.class);
        assertNotNull(bb);
        assertTrue(bb.number == 268435455);
        
        Message testRecursiveMessage = proto.getMessage("TestRecursiveMessage");
        assertNotNull(testRecursiveMessage);
        
        MessageField testRecursiveMessage_a = testRecursiveMessage.getField("a", 
                MessageField.class);        
        assertTrue(testRecursiveMessage == testRecursiveMessage_a.getMessage());
        
        Message testMutualRecursionA = proto.getMessage("TestMutualRecursionA");
        assertNotNull(testMutualRecursionA);
        Message testMutualRecursionB = proto.getMessage("TestMutualRecursionB");
        assertNotNull(testMutualRecursionB);
        
        MessageField testMutualRecursionA_bb = testMutualRecursionA.getField("bb", 
                MessageField.class);
        assertNotNull(testMutualRecursionA_bb);
        MessageField testMutualRecursionB_a = testMutualRecursionB.getField("a", 
                MessageField.class);
        assertNotNull(testMutualRecursionB_a);
        
        assertTrue(testMutualRecursionA == testMutualRecursionB_a.getMessage());
        assertTrue(testMutualRecursionB == testMutualRecursionA_bb.getMessage());
        
        Message testNestedMessageHasBits = proto.getMessage("TestNestedMessageHasBits");
        assertNotNull(testNestedMessageHasBits);
        Message tnmhb_nestedMessage = testNestedMessageHasBits.getNestedMessage("NestedMessage");
        assertNotNull(tnmhb_nestedMessage);
        
        MessageField tnmhb_optional_nested_message = testNestedMessageHasBits.getField("optional_nested_message", 
                MessageField.class);
        assertNotNull(tnmhb_optional_nested_message);
        assertTrue(tnmhb_nestedMessage == tnmhb_optional_nested_message.getMessage());
        
        MessageField nestedmessage_repeated_foreignmessage = tnmhb_nestedMessage.getField("nestedmessage_repeated_foreignmessage", 
                MessageField.class);
        assertNotNull(nestedmessage_repeated_foreignmessage);
        assertTrue(foreignMessage == nestedmessage_repeated_foreignmessage.getMessage());
        
        Message testFieldOrderings = proto.getMessage("TestFieldOrderings");
        assertNotNull(testFieldOrderings);
        assertTrue(testFieldOrderings.getFields().size() == 3);
        assertEquals(testFieldOrderings.sortedFields.get(0).name, "my_int");
        assertEquals(testFieldOrderings.sortedFields.get(1).name, "my_string");
        assertEquals(testFieldOrderings.sortedFields.get(2).name, "my_float");
        
        Message testExtremeDefaultValues = proto.getMessage("TestExtremeDefaultValues");
        assertNotNull(testExtremeDefaultValues);
        
        Field.UInt32 large_uint32 = testExtremeDefaultValues.getField("large_uint32", 
                Field.UInt32.class);
        assertNotNull(large_uint32);
        assertTrue((large_uint32.getDefaultValue().intValue() & 0xFFFFFFFF) == 0xFFFFFFFF);
        
        Field.UInt64 large_uint64 = testExtremeDefaultValues.getField("large_uint64", 
                Field.UInt64.class);
        assertNotNull(large_uint64);
        assertTrue(-1 == large_uint64.getDefaultValue().longValue());
        
        Field.Int32 small_int32 = testExtremeDefaultValues.getField("small_int32", 
                Field.Int32.class);
        assertNotNull(small_int32);
        assertTrue(small_int32.getDefaultValue().intValue() == -0x7FFFFFFF);
        
        Field.Int64 small_int64 = testExtremeDefaultValues.getField("small_int64", 
                Field.Int64.class);
        assertNotNull(small_int64);
        assertTrue(-Long.MAX_VALUE == small_int64.getDefaultValue().longValue());
        
        Message testAllExtensions = proto.getMessage("TestAllExtensions");
        assertNotNull(proto.getExtensions());
        assertTrue(proto.getExtensions().size() > 0);
        Extension extension = proto.getExtensions().iterator().next();
        assertEquals(testAllExtensions, extension.extendedMessage);
        assertNotNull(extension.getFields());
        assertTrue(extension.getFields().size() > 0);

        Message testNestedExtension = proto.getMessage("TestNestedExtension");
        assertNotNull(testNestedExtension.getNestedExtensions());
        assertEquals(1, testNestedExtension.getNestedExtensions().size());
        extension = testNestedExtension.getNestedExtensions().iterator().next();
        assertTrue(extension.isNested());
        
        Message testMultipleExtensionRanges = proto.getMessage("TestMultipleExtensionRanges");
        assertNotNull(testMultipleExtensionRanges);
        assertTrue(3 == testMultipleExtensionRanges.extensionRanges.size());
        int[] first = testMultipleExtensionRanges.extensionRanges.get(0);
        int[] second = testMultipleExtensionRanges.extensionRanges.get(1);
        int[] third = testMultipleExtensionRanges.extensionRanges.get(2);
        
        assertTrue(first[0] == first[1] && first[0] == 42);
        assertTrue(second[0] == 4143 && second[1] == 4243);
        assertTrue(third[0] == 65536 && third[1] == 536870911);
    }
    
    public void testEnumWithTrailingSemicolon() throws Exception
    {
        File f = getFile("enum_with_semicolon.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        assertEquals(proto.getPackageName(), "rpc");
    }
    
    public void testDescriptorProto() throws Exception
    {
        File f = getFile("descriptor.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        assertEquals(proto.getPackageName(), "google.protobuf");
    }
    
    /*public static void main(String[] args) throws Exception
    {
        File f = getFile("unittest.proto");
        assertTrue(f.exists());
        
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        System.err.println(Float.parseFloat("6.13e5"));
        byte[] b = "123".getBytes();
        for(int i=0; i<b.length; i++)
            System.err.println(b[i]);
        
    }*/

}
