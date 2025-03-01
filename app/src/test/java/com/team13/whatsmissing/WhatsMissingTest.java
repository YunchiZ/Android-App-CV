package com.team13.whatsmissing;

import com.team13.whatsmissing.old.WhatsMissing;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class WhatsMissingTest {

    @Test
    public void testWhatsMissingValid() {
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("pen", "pencil", "laptop", "phone", "tv", "remote"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("pen", "pencil", "laptop", "tv", "remote"));
        Assert.assertEquals("phone", WhatsMissing.whatsMissing(list1, list2));
    }

    @Test
    public void testWhatsMissingInvalid() {
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("pen", "pencil", "laptop", "phone", "tv", "remote"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("pen", "pencil", "laptop", "tv", "phone", "remote"));
        Assert.assertNull(WhatsMissing.whatsMissing(list1, list2));
    }

    @Test
    public void testTwoItemsOneMissing() {
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("pen", "pencil"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("pen"));
        Assert.assertEquals("pencil", WhatsMissing.whatsMissing(list1, list2));
    }

}