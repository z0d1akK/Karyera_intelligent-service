package com.zodiakk.intelligentservice.prompt.repository;

import com.zodiakk.intelligentservice.prompt.entity.PromptTemplate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PromptTemplateRepository extends JpaRepository<PromptTemplate, UUID>, JpaSpecificationExecutor<PromptTemplate> {

    Optional<PromptTemplate> findByCodeAndVersion(String code, Integer version);

    boolean existsByCodeAndVersion(String code, Integer version);

    List<PromptTemplate> findAllByCodeOrderByVersionDesc(String code);

    List<PromptTemplate> findAllByTaskTypeAndActiveTrue(String taskType);

    Optional<PromptTemplate> findFirstByTaskTypeAndActiveTrueOrderByVersionDesc(String taskType);

    Optional<PromptTemplate> findFirstByCodeAndActiveTrueOrderByVersionDesc(String code);

}
