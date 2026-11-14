package com.zodiakk.intelligentservice.generation.llm;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OllamaTextGenerationClientTest {

    @Test
    void forwardsSystemAndUserPrompts() {
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system("system").user("user").call().content()).thenReturn("generated");

        String result = new OllamaTextGenerationClient(builder).generate("system", "user");

        assertEquals("generated", result);
    }
}
