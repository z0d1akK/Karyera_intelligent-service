package com.zodiakk.intelligentservice.generation.mapper;

import com.zodiakk.intelligentservice.config.MapStructConfig;
import com.zodiakk.intelligentservice.generation.dto.request.create.AiGenerationCreateRequestDto;
import com.zodiakk.intelligentservice.generation.dto.request.update.AiGenerationUpdateRequestDto;
import com.zodiakk.intelligentservice.generation.dto.response.AiGenerationResponseDto;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface AiGenerationMapper {

    AiGeneration toEntity(AiGenerationCreateRequestDto dto);

    AiGenerationResponseDto toResponseDto(AiGeneration entity);

    void updateEntity(AiGenerationUpdateRequestDto dto, @MappingTarget AiGeneration entity);
}
