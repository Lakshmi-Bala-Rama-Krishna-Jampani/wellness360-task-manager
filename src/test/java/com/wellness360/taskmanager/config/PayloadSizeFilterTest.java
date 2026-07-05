package com.wellness360.taskmanager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PayloadSizeFilterTest {

    private PayloadSizeFilter filter;

    @BeforeEach
    void setUp() throws Exception {
        filter = new PayloadSizeFilter();
        var field = PayloadSizeFilter.class.getDeclaredField("maxPayloadBytes");
        field.setAccessible(true);
        field.set(filter, 100);
    }

    @Test
    @DisplayName("Should reject payloads exceeding configured limit")
    void rejectLargePayload() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(new byte[200]);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(413);
    }

    @Test
    @DisplayName("Should allow payloads within configured limit")
    void allowValidPayload() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(new byte[50]);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
