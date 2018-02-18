package com.mjrousos.breakfastquest.puzzleservice.services;

import com.microsoft.applicationinsights.TelemetryClient;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AppInsightsTelemetryServiceImpl implements TelemetryService {
    @Autowired
    private TelemetryClient telemetryClient;

    public void LogEvent(String event) {
        telemetryClient.trackEvent(event);
    }

    public void LogEvent(String event, Map<String, String> properties) {
        telemetryClient.trackEvent(event, properties, null);
    }

    public void LogEvent(String event, Map<String, String> properties, Map<String, Double> metrics) {
        telemetryClient.trackEvent(event, properties, metrics);
    }

    public void LogMetric(String name, Double value) {
        telemetryClient.trackMetric(name, value);
    }

    @Bean public TelemetryClient appInsightsTelemetryClient() {
        return new TelemetryClient();
    }

}
