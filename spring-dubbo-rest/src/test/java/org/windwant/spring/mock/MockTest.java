package org.windwant.spring.mock;

import org.junit.Assert;
import org.junit.Test;
import org.windwant.common.api.model.Guest;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 18-7-23.
 */
public class MockTest {

    @Test
    public void test(){

        List<String> list = mock(List.class);
        when(list.get(0)).thenReturn("helloworld");
        String result = list.get(0);
        verify(list).get(0);
        Assert.assertEquals("helloworld", result);

        Guest g = mock(Guest.class);
        when(g.getName()).thenReturn("lilei");
        String name = g.getName();
        verify(g).getName();
        Assert.assertEquals("lilei", name);
    }
}
