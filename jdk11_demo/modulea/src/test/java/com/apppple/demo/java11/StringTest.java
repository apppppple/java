package com.apppple.demo.java11;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author fanhui.mengfh on 2021/5/20
 */
public class StringTest {
    private static final char UNICODE_SPACE_VER52 = '\u2000';
    private static final char ASCII_UTF8 = '\u0020';

    @Test
    public void test_repeat() {
        String test = new String("Test");
        String longString = test.repeat(10);
        assertEquals("TestTestTestTestTestTestTestTestTestTest", longString);
    }

    @Test
    public void test_lines() {
        String multipleLinesString = "First line.\nSecond line.\nThird line.\n";
        List<String> lines = multipleLinesString.lines().collect(Collectors.toList());

        assertEquals("First line.", lines.get(0));
        assertEquals("Second line.", lines.get(1));
        assertEquals("Third line.", lines.get(2));
    }

    @Test
    public void test_stripLeading() {
        String hasLeadingWhiteSpace = " Test  ";
        String clearedTest = hasLeadingWhiteSpace.stripLeading();
        assertEquals("Test  ", clearedTest);
    }

    @Test
    public void test_stripTrailing() {
        String hasLeadingWhiteSpace = " Test  ";
        String clearedTest = hasLeadingWhiteSpace.stripTrailing();
        assertEquals(" Test", clearedTest);
    }

    @Test
    public void test_strip() {
        String hasLeadingWhiteSpace = " Test\t\n  ";
        String clearedTest = hasLeadingWhiteSpace.strip();
        assertEquals("Test", clearedTest);
    }

    @Test
    public void test_trim() {
        String hasLeadingWhiteSpace = " Test\t\n  ";
        String clearedTest = hasLeadingWhiteSpace.trim();
        assertEquals("Test", clearedTest);
    }

    @Test
    public void trim_vs_strip() {
        String testStr = UNICODE_SPACE_VER52 + "abc" + UNICODE_SPACE_VER52;

        assertTrue(Character.isWhitespace(UNICODE_SPACE_VER52));
        assertEquals(UNICODE_SPACE_VER52 + "abc" + UNICODE_SPACE_VER52, testStr.trim());

        // Strip is Unicode-aware
        assertEquals("abc", testStr.strip());

        testStr = ASCII_UTF8 + "abc" + ASCII_UTF8;
        assertTrue(Character.isWhitespace(ASCII_UTF8));
        assertEquals("abc", testStr.trim());
        assertEquals("abc", testStr.strip());

        testStr = '\u001F' + "abc" + '\u001F';
        assertTrue(Character.isWhitespace('\u001F'));
        assertEquals("abc", testStr.trim());
        assertEquals("abc", testStr.strip());

    }

    @Test
    public void test_isBlank() {
        assertTrue("".isBlank());
        assertTrue(System.getProperty("line.separator").isBlank());
        assertTrue("\t".isBlank());
        assertTrue("    ".isBlank());
        assertTrue("\t\n    ".isBlank());
    }
}
