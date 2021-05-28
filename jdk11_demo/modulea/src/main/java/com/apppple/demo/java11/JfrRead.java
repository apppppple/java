package com.apppple.demo.java11;

import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author fanhui.mengfh on 2021/5/27
 */
public class JfrRead {
    public static void main(String[] args) throws IOException {
        final Path path = Paths.get("/Users/apple/project/jdk11_demo/modulea/src/main/java/com/apppple/demo/java11/recording.jfr");
        final List<RecordedEvent> recordedEvents = RecordingFile.readAllEvents(path);
        for (RecordedEvent event : recordedEvents) {
            System.out.println(event.getStartTime() + "," + event.getEventType().getName());
        }
    }
}
