package com.wellness360.taskmanager.util;

import com.wellness360.taskmanager.constants.ApiConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.util.UUID;

public final class TraceIdUtil {

    private TraceIdUtil() {
    }

    public static String resolveTraceId(HttpServletRequest request) {
        String headerValue = request.getHeader(ApiConstants.TRACE_ID_HEADER);
        if (headerValue != null && !headerValue.isBlank()) {
            return headerValue.trim();
        }
        return UUID.randomUUID().toString();
    }

    public static String currentTraceId() {
        return MDC.get(ApiConstants.TRACE_ID_MDC_KEY);
    }
}
