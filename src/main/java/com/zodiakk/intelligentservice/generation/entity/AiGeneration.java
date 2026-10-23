package com.zodiakk.intelligentservice.generation.entity;

import com.zodiakk.intelligentservice.common.entity.CreatedEntity;
import com.zodiakk.intelligentservice.common.enums.AiGenerationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.OffsetDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@Table(name = "ai_generations")
@NoArgsConstructor
@SuperBuilder
public class AiGeneration extends CreatedEntity {

    @Column(name = "correlation_id", nullable = false)
    private UUID correlationId;

    @Column(name = "assessment_id")
    private UUID assessmentId;

    @Column(name = "model_config_id", nullable = false)
    private UUID modelConfigId;

    @Column(name = "prompt_template_id", nullable = false)
    private UUID promptTemplateId;

    @Column(name = "task_type", nullable = false, length = 50)
    private String taskType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 25)
    private AiGenerationStatus status;

    @Column(name = "input_hash", nullable = false, length = 64)
    private String inputHash;

    @Column(name = "idempotency_key", length = 120)
    private String idempotencyKey;

    @Column(name = "input_tokens")
    private Integer inputTokens;

    @Column(name = "output_tokens")
    private Integer outputTokens;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(name = "error_code", length = 100)
    private String errorCode;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

}
