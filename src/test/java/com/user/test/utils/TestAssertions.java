package com.user.test.utils;

import net.serenitybdd.core.Serenity;
import org.junit.Assert;

public class TestAssertions {
    public static void assertIsTrue(String msg, String title, String expected, String actual) {
        Serenity.recordReportData().withTitle(title).andContents("Expected: " + expected + "   Actual: " + actual);
        Assert.assertEquals(msg, expected, actual);
    }

}
