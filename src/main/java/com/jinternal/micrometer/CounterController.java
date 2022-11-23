package com.jinternal.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController {
    @Autowired
    MeterRegistry meterRegistry;

    @GetMapping("/increase/count")
    String getMessage(){
        Counter counter = meterRegistry.counter("api-calls");
        counter.increment();
        return "count " + counter.count();
    }
}
