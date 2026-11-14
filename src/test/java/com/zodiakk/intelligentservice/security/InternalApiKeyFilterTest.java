package com.zodiakk.intelligentservice.security;

import com.zodiakk.intelligentservice.security.filter.InternalApiKeyFilter;
import com.zodiakk.intelligentservice.security.handler.CustomAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class InternalApiKeyFilterTest {

    private final InternalApiKeyFilter filter = new InternalApiKeyFilter(
            "test-internal-api-key",
            new CustomAuthenticationEntryPoint()
    );

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void skipsPublicAndOptionsAndAuthenticatesInternalCalls() throws Exception {
        FilterChain chain = mock(FilterChain.class);

        MockHttpServletRequest publicRequest = new MockHttpServletRequest("GET", "/actuator/health");
        MockHttpServletResponse publicResponse = new MockHttpServletResponse();
        filter.doFilter(publicRequest, publicResponse, chain);

        MockHttpServletRequest options = new MockHttpServletRequest("OPTIONS", "/internal/v1/intelligence/x");
        filter.doFilter(options, new MockHttpServletResponse(), chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        MockHttpServletRequest bad = new MockHttpServletRequest("POST", "/internal/v1/intelligence/career-explanations");
        bad.addHeader("X-Internal-Key", "wrong");
        MockHttpServletResponse unauthorized = new MockHttpServletResponse();
        filter.doFilter(bad, unauthorized, chain);
        assertEquals(401, unauthorized.getStatus());

        MockHttpServletRequest ok = new MockHttpServletRequest("POST", "/internal/v1/intelligence/career-explanations");
        ok.addHeader("X-Internal-Key", "test-internal-api-key");
        filter.doFilter(ok, new MockHttpServletResponse(), chain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("internal-service", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(chain, times(3)).doFilter(any(), any());

        MockHttpServletRequest missing = new MockHttpServletRequest("POST", "/internal/v1/intelligence/career-explanations");
        MockHttpServletResponse missingResponse = new MockHttpServletResponse();
        filter.doFilter(missing, missingResponse, chain);
        assertEquals(401, missingResponse.getStatus());
    }
}
