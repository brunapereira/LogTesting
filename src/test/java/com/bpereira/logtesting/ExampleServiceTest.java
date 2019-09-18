package com.bpereira.logtesting;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleServiceTest {
    @Rule
    public CaptureLogsRule captureLogsRule = new CaptureLogsRule(ExampleService.class);

    private ExampleService exampleService = new ExampleService();

    @Test
    public void logsAMessage() {
        exampleService.testLog();

        List<String> loggedMessages = captureLogsRule.getMessages();

        assertThat(loggedMessages).containsExactly("This is a log");
    }
}
