package com.zodiakk.intelligentservice.model.repository.specification.aiModelConfig;

import com.zodiakk.intelligentservice.model.dto.request.filter.AiModelConfigFilterDto;
import com.zodiakk.intelligentservice.model.entity.AiModelConfig;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

@UtilityClass
public class AiModelConfigSpecification {

    public static Specification<AiModelConfig> filter(AiModelConfigFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            if (filter.getCode() != null && !filter.getCode().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("code")),
                        "%" + filter.getCode().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getProvider() != null && !filter.getProvider().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("provider")),
                        "%" + filter.getProvider().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getModelName() != null && !filter.getModelName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("modelName")),
                        "%" + filter.getModelName().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), filter.getActive()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
