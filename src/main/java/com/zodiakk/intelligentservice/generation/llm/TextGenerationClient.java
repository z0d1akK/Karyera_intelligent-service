package com.zodiakk.intelligentservice.generation.llm;

public interface TextGenerationClient {

    String generate(String systemPrompt, String userPrompt);
}
