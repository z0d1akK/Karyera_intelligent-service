package com.zodiakk.intelligentservice.model.mapper;

import com.zodiakk.intelligentservice.config.MapStructConfig;
import com.zodiakk.intelligentservice.model.dto.request.create.AiModelConfigCreateRequestDto;
import com.zodiakk.intelligentservice.model.dto.request.update.AiModelConfigUpdateRequestDto;
import com.zodiakk.intelligentservice.model.dto.response.AiModelConfigResponseDto;
import com.zodiakk.intelligentservice.model.entity.AiModelConfig;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface AiModelConfigMapper {

    AiModelConfig toEntity(AiModelConfigCreateRequestDto dto);

    AiModelConfigResponseDto toResponseDto(AiModelConfig entity);

    void updateEntity(AiModelConfigUpdateRequestDto dto, @MappingTarget AiModelConfig entity);
}
