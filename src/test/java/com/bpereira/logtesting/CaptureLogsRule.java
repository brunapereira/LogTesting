package com.bpereira.logtesting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CaptureLogsRule extends AbstractAppender implements TestRule {
    private Logger logger;
    private final Class clazz;
    private ArrayList<LogEvent> events = new ArrayList<>();

    @PluginFactory
    public static CaptureLogsRule createAppender() {
        return new CaptureLogsRule(CaptureLogsRule.class);
    }

    public CaptureLogsRule(final Class clazz) {
        super("", null, null, true, Property.EMPTY_ARRAY);
        this.start();
        this.clazz = clazz;
    }

    public List<LogEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public List<String> getMessages() {
        return getEvents().stream()
                .map(LogEvent::getMessage)
                .map(Message::getFormattedMessage)
                .collect(toList());
    }

    private void setUp() {
        logger = (Logger) LogManager.getLogger(clazz);
        logger.addAppender(this);
    }

    private void tearDown() {
        logger.removeAppender(this);
    }

    @Override
    public void append(final LogEvent event) {
        events.add(event);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        final CaptureLogsRule appender = this;
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                appender.setUp();
                try {
                    base.evaluate();
                } finally {
                    appender.tearDown();
                }
            }
        };
    }
}
