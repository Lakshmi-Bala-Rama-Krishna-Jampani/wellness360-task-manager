package com.wellness360.taskmanager.util;

import com.wellness360.taskmanager.constants.ApiConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class TraceIdUtilTest {

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("Should use trace id from request header when present")
    void resolveFromHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ApiConstants.TRACE_ID_HEADER, "custom-trace-id");

        assertThat(TraceIdUtil.resolveTraceId(request)).isEqualTo("custom-trace-id");
    }

    @Test
    @DisplayName("Should generate trace id when header is missing")
    void generateWhenMissing() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThat(TraceIdUtil.resolveTraceId(request)).isNotBlank();
    }

    @Test
    @DisplayName("Should read trace id from MDC")
    void currentTraceId() {
        MDC.put(ApiConstants.TRACE_ID_MDC_KEY, "mdc-trace");

        assertThat(TraceIdUtil.currentTraceId()).isEqualTo("mdc-trace");
    }
}
