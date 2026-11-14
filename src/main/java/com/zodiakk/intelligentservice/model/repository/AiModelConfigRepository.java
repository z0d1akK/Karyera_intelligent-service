package com.zodiakk.intelligentservice.model.repository;

import com.zodiakk.intelligentservice.model.entity.AiModelConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AiModelConfigRepository extends JpaRepository<AiModelConfig, UUID>, JpaSpecificationExecutor<AiModelConfig> {

    Optional<AiModelConfig> findByCode(String code);

    boolean existsByCode(String code);

    List<AiModelConfig> findAllByActiveTrue();

    List<AiModelConfig> findAllByProviderAndActiveTrue(String provider);

}
