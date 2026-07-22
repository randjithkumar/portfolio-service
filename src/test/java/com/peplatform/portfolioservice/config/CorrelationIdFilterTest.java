package com.peplatform.portfolioservice.config;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CorrelationIdFilterTest {

    /**
     * Entity identifier or primary key for the entity.
     * <p>
     *
     * @see #// GENERATED_VALUE
     */
    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    /**
     * Tests the preservation of provided correlation ID.
     * <p>
     * Test scenario: When the user exists, the request header containing the
     * correlation ID "CORR-123" is added to the request object.
     * Expected result: The response header containing the same correlation ID should be retained.
     */
    @Test
    void shouldPreserveProvidedCorrelationId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CorrelationIdFilter.HEADER_NAME, "CORR-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertThat(response.getHeader(CorrelationIdFilter.HEADER_NAME)).isEqualTo("CORR-123");
    }

    /**
     * Tests the generation of a correlation ID when no header is present.
     *
     * <p>
     * Test scenario: When there is no request header with the name provided by {@link CorrelationIdFilter.HEADER_NAME}.
     * Expected result: A new correlation ID header should be added to the response with a non-blank value, indicating the presence of a correlation ID for this operation.
     */
    @Test
    void shouldGenerateCorrelationIdWhenMissing() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(new MockHttpServletRequest(), response, mock(FilterChain.class));

        assertThat(response.getHeader(CorrelationIdFilter.HEADER_NAME)).isNotBlank();
    }
}
