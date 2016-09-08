package io.protostuff;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ListAdapterTest
{

    @Test
    public void checkConvertedValues() throws Exception
    {
        List<Integer> a = new ArrayList<Integer>();
        ListAdapter<Integer, String> adapter = new ListAdapter<Integer, String>(a, new ListAdapter.Converter<Integer, String>()
        {
            @Override
            public String convert(Integer from)
            {
                return from.toString();
            }
        });
        a.add(5);
        a.add(10);
        Assert.assertEquals(2, adapter.size());
        Assert.assertEquals("5", adapter.get(0));
        Assert.assertEquals("10", adapter.get(1));

    }
}