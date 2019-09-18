package com.bpereira.logtesting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExampleService {

    public void testLog() {
        log.info("This is a log");
    }
}
