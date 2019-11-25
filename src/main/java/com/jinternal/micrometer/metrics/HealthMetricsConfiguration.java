package com.jinternal.micrometer.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.*;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Configuration
class HealthMetricsConfiguration {
    private CompositeHealthIndicator healthIndicator;

    public HealthMetricsConfiguration(HealthAggregator healthAggregator,
                                      Map<String,HealthIndicator> healthIndicators,
                                      MeterRegistry registry) {

        healthIndicator = new CompositeHealthIndicator(healthAggregator,new DefaultHealthIndicatorRegistry(healthIndicators));

//        for (Integer i = 0; i < healthIndicators.size(); i++) {
//            healthIndicator.addHealthIndicator(i.toString(), healthIndicators.get(i));
//        }

        // presumes there is a common tag applied elsewhere that adds tags for app, etc.
        registry.gauge("health", emptyList(), healthIndicator, health -> {
            Status status = health.health().getStatus();
            switch (status.getCode()) {
                case "UP":
                    return 3;
                case "OUT_OF_SERVICE":
                    return 2;
                case "DOWN":
                    return 1;
                case "UNKNOWN":
                default:
                    return 0;
            }
        });
    }
}
