package com.wellness360.taskmanager.config;

import com.wellness360.taskmanager.constants.ApiConstants;
import com.wellness360.taskmanager.util.TraceIdUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String traceId = TraceIdUtil.resolveTraceId(request);
        MDC.put(ApiConstants.TRACE_ID_MDC_KEY, traceId);
        response.setHeader(ApiConstants.TRACE_ID_HEADER, traceId);

        long start = System.currentTimeMillis();
        try {
            log.info("Incoming {} {} from {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("Completed {} {} -> status={} durationMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
            MDC.remove(ApiConstants.TRACE_ID_MDC_KEY);
        }
    }
}
