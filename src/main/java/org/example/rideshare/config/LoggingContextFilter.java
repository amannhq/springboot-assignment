package org.example.rideshare.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter to add request context to MDC for structured logging.
 * This enables tracing requests across log entries.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingContextFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String TRACE_ID = "traceId";
    private static final String CLIENT_IP = "clientIp";
    private static final String REQUEST_URI = "requestUri";
    private static final String REQUEST_METHOD = "requestMethod";

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Generate or use existing trace ID from header
            String traceId = request.getHeader("X-Trace-Id");
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            }

            String requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            // Add to MDC for logging
            MDC.put(REQUEST_ID, requestId);
            MDC.put(TRACE_ID, traceId);
            MDC.put(CLIENT_IP, getClientIP(request));
            MDC.put(REQUEST_URI, request.getRequestURI());
            MDC.put(REQUEST_METHOD, request.getMethod());

            // Add trace ID to response header for client correlation
            response.addHeader("X-Trace-Id", traceId);
            response.addHeader("X-Request-Id", requestId);

            filterChain.doFilter(request, response);
        } finally {
            // Clear MDC after request to prevent memory leaks
            MDC.clear();
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        return request.getRemoteAddr();
    }
}
