package com.zodiakk.intelligentservice;

import com.zodiakk.intelligentservice.generation.llm.TextGenerationClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class IntelligenceFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TextGenerationClient textGenerationClient;

    @Test
    void fallsBackAndReusesTheSameGeneration() throws Exception {
        when(textGenerationClient.generate(anyString(), anyString())).thenThrow(new IllegalStateException("ollama down"));
        String body = explanation(UUID.randomUUID());

        String first = mockMvc.perform(post("/internal/v1/intelligence/career-explanations")
                        .header("X-Internal-Key", "test-internal-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelIdentifier").value("template-fallback"))
                .andExpect(jsonPath("$.summary").exists())
                .andReturn().getResponse().getContentAsString();

        String generationId = first.replaceAll("(?s).*\"generationId\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(post("/internal/v1/intelligence/career-explanations")
                        .header("X-Internal-Key", "test-internal-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.generationId").value(generationId));

        mockMvc.perform(get("/internal/v1/intelligence/generations/{id}", generationId)
                        .header("X-Internal-Key", "test-internal-api-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FALLBACK"))
                .andExpect(jsonPath("$.taskType").value("CAREER_EXPLANATION"));

        mockMvc.perform(post("/internal/v1/intelligence/career-explanations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void recommendsDevelopmentAndComparesDirections() throws Exception {
        when(textGenerationClient.generate(anyString(), anyString())).thenReturn("не json");

        mockMvc.perform(post("/internal/v1/intelligence/development-recommendations")
                        .header("X-Internal-Key", "test-internal-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"assessmentId":"%s","language":"ru","targetPositionName":"Senior","developmentAreas":[{"skillId":"%s","skillName":"Kafka","currentLevel":1,"targetLevel":3,"priorityScore":0.1}],"preferences":{"maxRecommendations":5,"includeProjectActivities":true,"includeMentoring":true}}
                                """.formatted(UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendations[0].title").value("Развитие навыка Kafka"));

        mockMvc.perform(post("/internal/v1/intelligence/career-path-interpretations")
                        .header("X-Internal-Key", "test-internal-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"employeeId":"%s","language":"ru","currentPositionName":"Middle","directions":[{"targetPositionId":"%s","targetPositionName":"Senior","totalScore":0.7,"mandatoryRequirementsMet":true,"strengths":[{"skillName":"Java"}],"skillGaps":[]}]}
                                """.formatted(UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendedDirection").value("Senior"));
    }

    private String explanation(UUID assessmentId) {
        return """
                {"correlationId":"%s","assessmentId":"%s","language":"ru","target":{"positionName":"Senior","gradeName":"Senior"},"scores":{"skill":0.25,"experience":0,"total":0.225},"mandatoryRequirementsMet":false,"dataQualityStatus":"PARTIAL","strengths":[],"skillGaps":[{"skillId":"%s","skillName":"Kafka","actualLevel":1,"requiredLevel":3,"priorityScore":0.1,"mandatory":true}]}
                """.formatted(UUID.randomUUID(), assessmentId, UUID.randomUUID());
    }
}
