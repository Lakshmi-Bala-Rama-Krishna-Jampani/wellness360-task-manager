package com.wellness360.taskmanager.config;

import com.wellness360.taskmanager.constants.TaskConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class PayloadSizeFilter extends OncePerRequestFilter {

    @Value("${app.task.max-payload-bytes:" + TaskConstants.DEFAULT_MAX_PAYLOAD_BYTES + "}")
    private int maxPayloadBytes;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        int contentLength = request.getContentLength();
        if (contentLength > maxPayloadBytes) {
            response.sendError(HttpStatus.PAYLOAD_TOO_LARGE.value(), "Request payload too large");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
