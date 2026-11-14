package com.zodiakk.intelligentservice.generation.repository.specification.aiGeneration;

import com.zodiakk.intelligentservice.generation.dto.request.filter.AiGenerationFilterDto;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

@UtilityClass
public class AiGenerationSpecification {

    public static Specification<AiGeneration> filter(AiGenerationFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            if (filter.getCorrelationId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("correlationId"), filter.getCorrelationId()));
            }

            if (filter.getAssessmentId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("assessmentId"), filter.getAssessmentId()));
            }

            if (filter.getModelConfigId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("modelConfigId"), filter.getModelConfigId()));
            }

            if (filter.getPromptTemplateId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("promptTemplateId"), filter.getPromptTemplateId()));
            }

            if (filter.getTaskType() != null && !filter.getTaskType().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("taskType")),
                        "%" + filter.getTaskType().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getIdempotencyKey() != null && !filter.getIdempotencyKey().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("idempotencyKey")),
                        "%" + filter.getIdempotencyKey().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getCreatedAtFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtFrom()));
            }

            if (filter.getCreatedAtTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
