package com.mjrousos.breakfastquest.puzzleservice.services;

import java.util.Map;

public interface TelemetryService {
    public void LogEvent(String event);
    public void LogEvent(String event, Map<String, String> properties);
    public void LogEvent(String event, Map<String, String> properties, Map<String, Double> metrics);
}
