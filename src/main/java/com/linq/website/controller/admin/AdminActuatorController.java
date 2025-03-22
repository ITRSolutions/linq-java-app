package com.linq.website.controller.admin;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/actuator")
public class AdminActuatorController {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private HealthEndpoint healthEndpoint;

    @GetMapping
    public Map<String, Object> getImportantMetrics() {
        Map<String, Object> importantMetrics = new LinkedHashMap<>();
        // Get application health status
        importantMetrics.put("health", getHealthStatus());

        // Retrieving specific metrics
        importantMetrics.put("applicationStartedTime", getMetricValue("application.started.time"));
        importantMetrics.put("activeUser", getMetricValue("tomcat.sessions.active.current"));
        importantMetrics.put("cpuUsage", getMetricValue("system.cpu.usage"));
        importantMetrics.put("diskFree", getMetricValue("disk.free"));
        importantMetrics.put("diskTotal", getMetricValue("disk.total"));

        return importantMetrics;
    }

    // Fetches the health status from the health endpoint
    private String getHealthStatus() {
        // Fetch the health status from the health endpoint
        HealthComponent health = healthEndpoint.health();

        // Get the status from the health object (UP, DOWN, OUT_OF_SERVICE)
        return health.getStatus().toString();
    }

    // Fetches the metric value from MeterRegistry
    private Object getMetricValue(String metricName) {
        Meter meter = meterRegistry.find(metricName).meter();
        if (meter != null) {
            // If the meter exists, return the value of the metric
            return meter.measure().iterator().next().getValue();
        }
        return "Not Available"; // Return if metric not found
    }
}


