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
    

    @Test
    public void testUserid(){
        ChatUser cu = new ChatUser();
        Assert.assertNotNull(cu.getEmail());
    }



}