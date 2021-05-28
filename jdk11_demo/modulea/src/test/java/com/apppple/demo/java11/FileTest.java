package com.apppple.demo.java11;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * @author fanhui.mengfh on 2021/5/20
 */
public class FileTest {
    @Test
    public void writeString_readString() throws IOException {
        Path localFile = Path.of(System.getProperty("user.home") + "/test.txt");

        String sampleString = "Some example of text";
        Files.writeString(localFile, sampleString);

        String readData = Files.readString(localFile);
        assertEquals(sampleString, readData);
    }

    @Test
    public void writeString_readString_utf8() throws IOException {
        String sampleString = "Some example of text";
        Files.writeString(Path.of(System.getProperty("user.home") + "/test.txt"), sampleString,
            StandardCharsets.UTF_8);

        String readData = Files.readString(Path.of(System.getProperty("user.home") + "/test.txt"));
        assertEquals(sampleString, readData);
    }
}
