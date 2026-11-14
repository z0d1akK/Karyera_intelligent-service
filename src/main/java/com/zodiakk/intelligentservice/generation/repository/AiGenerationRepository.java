package com.zodiakk.intelligentservice.generation.repository;

import com.zodiakk.intelligentservice.common.enums.*;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AiGenerationRepository extends JpaRepository<AiGeneration, UUID>, JpaSpecificationExecutor<AiGeneration> {

    List<AiGeneration> findAllByCorrelationIdOrderByCreatedAtDesc(UUID correlationId);

    List<AiGeneration> findAllByAssessmentIdOrderByCreatedAtDesc(UUID assessmentId);

    List<AiGeneration> findAllByStatus(AiGenerationStatus status);

    List<AiGeneration> findAllByTaskTypeAndStatus(String taskType, AiGenerationStatus status);

    Optional<AiGeneration> findFirstByIdempotencyKeyOrderByCreatedAtDesc(String idempotencyKey);

    Optional<AiGeneration> findFirstByTaskTypeAndInputHashAndStatusOrderByCreatedAtDesc(
            String taskType,
            String inputHash,
            AiGenerationStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM AiGeneration g WHERE g.id = :generationId")
    Optional<AiGeneration> findByIdForUpdate(@Param("generationId") UUID generationId);

}
