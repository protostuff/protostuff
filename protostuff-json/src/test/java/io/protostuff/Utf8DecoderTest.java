package io.protostuff;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class Utf8DecoderTest
{

    @Parameterized.Parameters
    public static final Collection<Object[]> input()
    {
        return Arrays.asList(new Object[][] {
                { "test" }, { "ß»đßłµþæð丽􀀬😅𝐋j" }, { "val😜" }
        });
    }

    private String str;

    public Utf8DecoderTest(String str) {
        this.str=str;
    }

    @Test
    public void test()
    {
        Utf8Decoder d = new Utf8Decoder();
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++)
            d.append(bytes[i]);
        assertEquals(str, d.done());
    }
}
