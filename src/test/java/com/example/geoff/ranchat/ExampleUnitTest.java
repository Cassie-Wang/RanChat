package com.example.geoff.ranchat;

import org.junit.Test;
import com.example.geoff.ranchat.*;

import junit.framework.Assert;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public final static String userName = "Geoff Lee";

    @Test
    public void testUserName(){
        ChatUser cu = new ChatUser();
        cu.setUsername(userName);
        Assert.assertNotNull(cu.getUsername());
        Assert. assertEquals(cu.getUsername(), userName);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

}