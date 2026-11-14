package com.zodiakk.intelligentservice.prompt.repository.specification.promptTemplate;

import com.zodiakk.intelligentservice.prompt.dto.request.filter.PromptTemplateFilterDto;
import com.zodiakk.intelligentservice.prompt.entity.PromptTemplate;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

@UtilityClass
public class PromptTemplateSpecification {

    public static Specification<PromptTemplate> filter(PromptTemplateFilterDto filter) {
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

            if (filter.getVersion() != null) {
                predicates.add(criteriaBuilder.equal(root.get("version"), filter.getVersion()));
            }

            if (filter.getTaskType() != null && !filter.getTaskType().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("taskType")),
                        "%" + filter.getTaskType().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), filter.getActive()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
