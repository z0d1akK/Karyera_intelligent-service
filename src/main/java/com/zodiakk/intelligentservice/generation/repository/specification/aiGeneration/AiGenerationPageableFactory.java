package com.zodiakk.intelligentservice.generation.repository.specification.aiGeneration;

import com.zodiakk.intelligentservice.common.dto.PageRequestDto;
import com.zodiakk.intelligentservice.common.dto.SortDirection;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

@UtilityClass
public class AiGenerationPageableFactory {

    private static final Map<String, String> ALLOWED_SORT_FIELDS = Map.ofEntries(
            Map.entry("taskType", "taskType"),
            Map.entry("status", "status"),
            Map.entry("createdAt", "createdAt"),
            Map.entry("durationMs", "durationMs"),
            Map.entry("completedAt", "completedAt")
    );

    public static Pageable create(PageRequestDto pageRequest) {
        String sortField = ALLOWED_SORT_FIELDS.getOrDefault(
                pageRequest.getSortBy(),
                "createdAt"
        );

        Sort.Direction direction = pageRequest.getSortDirection() == SortDirection.DESC
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(direction, sortField));
    }
}
