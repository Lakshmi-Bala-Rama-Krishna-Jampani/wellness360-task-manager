package com.wellness360.taskmanager.constants;

public final class ApiConstants {

    public static final String API_VERSION = "v1";
    public static final String TASKS_BASE_PATH = "/tasks";
    public static final String HEALTH_PATH = "/actuator/health";
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String TRACE_ID_MDC_KEY = "traceId";

    private ApiConstants() {
    }
}
