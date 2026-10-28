package com.zodiakk.intelligentservice.prompt.entity;

import com.zodiakk.intelligentservice.common.entity.CreatedEntity;

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
@Table(name = "prompt_templates")
@NoArgsConstructor
@SuperBuilder
public class PromptTemplate extends CreatedEntity {

    @Column(name = "code", nullable = false, length = 80)
    private String code;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "task_type", nullable = false, length = 50)
    private String taskType;

    @Column(name = "system_prompt", nullable = false, columnDefinition = "text")
    private String systemPrompt;

    @Column(name = "user_prompt_template", nullable = false, columnDefinition = "text")
    private String userPromptTemplate;

    @Column(name = "output_schema_version", nullable = false, length = 20)
    private String outputSchemaVersion;

    @Column(name = "active", nullable = false)
    private Boolean active;

}
