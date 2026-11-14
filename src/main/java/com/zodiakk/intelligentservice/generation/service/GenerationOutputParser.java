package com.zodiakk.intelligentservice.generation.service;

import com.zodiakk.intelligentservice.career.dto.response.CareerExplanationResponseDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathDirectionExplanationDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathInterpretationResponseDto;
import com.zodiakk.intelligentservice.common.dto.DevelopmentAreaExplanationDto;
import com.zodiakk.intelligentservice.common.enums.RecommendationType;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationDto;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationResponseDto;
import com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GenerationOutputParser {

    private final ObjectMapper objectMapper;

    public GenerationOutputParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T parse(String taskType, String raw, Class<T> type) {
        JsonNode node = tree(raw);
        if (node == null || !node.isObject()) {
            return null;
        }
        Object parsed = switch (taskType) {
            case GenerationService.CAREER_EXPLANATION -> explanation(node);
            case GenerationService.DEVELOPMENT_RECOMMENDATIONS -> recommendations(node);
            case GenerationService.CAREER_PATH_INTERPRETATION -> interpretation(node);
            case GenerationService.TALENT_SCREENING_EXPLANATION -> screening(node);
            default -> null;
        };
        return type.isInstance(parsed) ? type.cast(parsed) : null;
    }

    private CareerExplanationResponseDto explanation(JsonNode node) {
        String summary = text(node, "summary");
        if (summary.isBlank()) {
            return null;
        }
        return CareerExplanationResponseDto.builder()
                .summary(summary)
                .strengths(stringList(node.get("strengths")))
                .developmentAreas(areas(node.get("developmentAreas")))
                .limitations(stringList(node.get("limitations")))
                .build();
    }

    private DevelopmentRecommendationResponseDto recommendations(JsonNode node) {
        JsonNode items = node.get("recommendations");
        if (items == null || items.isNull()) {
            return null;
        }
        List<DevelopmentRecommendationDto> recommendations = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode item : items) {
                DevelopmentRecommendationDto mapped = recommendation(item);
                if (mapped != null) {
                    recommendations.add(mapped);
                }
            }
        } else {
            DevelopmentRecommendationDto mapped = recommendation(items);
            if (mapped != null) {
                recommendations.add(mapped);
            }
        }
        if (recommendations.isEmpty()) {
            return null;
        }
        return DevelopmentRecommendationResponseDto.builder().recommendations(recommendations).build();
    }

    private CareerPathInterpretationResponseDto interpretation(JsonNode node) {
        String summary = text(node, "summary");
        if (summary.isBlank()) {
            return null;
        }
        List<CareerPathDirectionExplanationDto> directions = new ArrayList<>();
        JsonNode rawDirections = node.get("directions");
        if (rawDirections != null && rawDirections.isArray()) {
            for (JsonNode item : rawDirections) {
                if (item == null || item.isNull()) {
                    continue;
                }
                if (item.isString()) {
                    directions.add(CareerPathDirectionExplanationDto.builder()
                            .fitSummary(item.asString())
                            .build());
                    continue;
                }
                directions.add(CareerPathDirectionExplanationDto.builder()
                        .targetPositionId(uuid(item.get("targetPositionId")))
                        .targetPositionName(firstText(item, "targetPositionName", "name"))
                        .fitSummary(firstText(item, "fitSummary", "summary", "explanation"))
                        .strengths(stringList(item.get("strengths")))
                        .developmentAreas(stringList(item.get("developmentAreas")))
                        .build());
            }
        }
        return CareerPathInterpretationResponseDto.builder()
                .summary(summary)
                .directions(directions)
                .recommendedDirection(firstText(node, "recommendedDirection", "recommended"))
                .recommendationRationale(firstText(node, "recommendationRationale", "rationale"))
                .limitations(stringList(node.get("limitations")))
                .build();
    }

    private TalentScreeningExplanationResponseDto screening(JsonNode node) {
        String summary = text(node, "summary");
        if (summary.isBlank()) {
            return null;
        }
        return TalentScreeningExplanationResponseDto.builder()
                .summary(summary)
                .highlights(stringList(node.get("highlights")))
                .limitations(stringList(node.get("limitations")))
                .build();
    }

    private DevelopmentRecommendationDto recommendation(JsonNode item) {
        if (item == null || item.isNull()) {
            return null;
        }
        if (item.isString()) {
            String title = item.asString("").trim();
            return title.isBlank() ? null : DevelopmentRecommendationDto.builder().title(title).build();
        }
        if (!item.isObject()) {
            return null;
        }
        String title = firstText(item, "title", "text", "name", "skillName");
        String description = firstText(item, "description", "explanation", "reason", "details");
        if (title.isBlank() && description.isBlank()) {
            return null;
        }
        return DevelopmentRecommendationDto.builder()
                .skillId(uuid(item.get("skillId")))
                .type(type(firstText(item, "type", "recommendationType")))
                .title(title.isBlank() ? description : title)
                .description(description)
                .suggestedActivities(stringList(item.get("suggestedActivities")))
                .expectedOutcome(firstText(item, "expectedOutcome", "outcome"))
                .estimatedDuration(firstText(item, "estimatedDuration", "duration"))
                .build();
    }

    private List<DevelopmentAreaExplanationDto> areas(JsonNode node) {
        List<DevelopmentAreaExplanationDto> areas = new ArrayList<>();
        if (node == null || node.isNull()) {
            return areas;
        }
        if (node.isString()) {
            String explanation = node.asString("").trim();
            if (!explanation.isBlank()) {
                areas.add(DevelopmentAreaExplanationDto.builder().explanation(explanation).build());
            }
            return areas;
        }
        if (!node.isArray()) {
            return areas;
        }
        for (JsonNode item : node) {
            if (item == null || item.isNull()) {
                continue;
            }
            if (item.isString()) {
                String explanation = item.asString("").trim();
                if (!explanation.isBlank()) {
                    areas.add(DevelopmentAreaExplanationDto.builder().explanation(explanation).build());
                }
                continue;
            }
            String explanation = firstText(item, "explanation", "text", "description", "summary");
            if (explanation.isBlank()) {
                continue;
            }
            areas.add(DevelopmentAreaExplanationDto.builder()
                    .skillId(uuid(item.get("skillId")))
                    .explanation(explanation)
                    .build());
        }
        return areas;
    }

    private JsonNode tree(String raw) {
        String json = extractJson(raw);
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readTree(json);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private String extractJson(String raw) {
        if (raw == null) {
            return "";
        }
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return raw.substring(start, end + 1);
        }
        return raw;
    }

    private List<String> stringList(JsonNode node) {
        List<String> values = new ArrayList<>();
        if (node == null || node.isNull()) {
            return values;
        }
        if (node.isString()) {
            String value = node.asString("").trim();
            if (!value.isBlank()) {
                values.add(value);
            }
            return values;
        }
        if (!node.isArray()) {
            return values;
        }
        for (JsonNode item : node) {
            if (item == null || item.isNull()) {
                continue;
            }
            if (item.isString()) {
                String value = item.asString("").trim();
                if (!value.isBlank()) {
                    values.add(value);
                }
                continue;
            }
            String value = firstText(item, "text", "explanation", "title", "summary", "name");
            if (!value.isBlank()) {
                values.add(value);
            }
        }
        return values;
    }

    private String firstText(JsonNode node, String... fields) {
        if (node == null || !node.isObject()) {
            return "";
        }
        for (String field : fields) {
            String value = text(node, field);
            if (!value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private String text(JsonNode node, String field) {
        if (node == null) {
            return "";
        }
        JsonNode value = node.get(field);
        if (value == null || value.isNull() || value.isMissingNode()) {
            return "";
        }
        return value.asString("").trim();
    }

    private UUID uuid(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        String value = node.asString("").trim();
        if (value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private RecommendationType type(String raw) {
        if (raw == null || raw.isBlank()) {
            return RecommendationType.SELF_STUDY;
        }
        String normalized = raw.trim().toUpperCase().replace('-', '_').replace(' ', '_');
        try {
            return RecommendationType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return RecommendationType.SELF_STUDY;
        }
    }
}
