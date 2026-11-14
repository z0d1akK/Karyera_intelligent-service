package com.zodiakk.intelligentservice.generation.llm;

import org.junit.jupiter.api.Test;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OllamaTimeoutConfigTest {

    @Test
    void customizesRestClientTimeouts() {
        RestClient.Builder builder = mock(RestClient.Builder.class);
        new OllamaTimeoutConfig().ollamaTimeout(12).customize(builder);
        verify(builder).requestFactory(any(ClientHttpRequestFactory.class));
        assertNotNull(builder);
    }
}
