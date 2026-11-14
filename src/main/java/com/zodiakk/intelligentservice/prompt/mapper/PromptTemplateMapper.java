package com.zodiakk.intelligentservice.prompt.mapper;

import com.zodiakk.intelligentservice.config.MapStructConfig;
import com.zodiakk.intelligentservice.prompt.dto.request.create.PromptTemplateCreateRequestDto;
import com.zodiakk.intelligentservice.prompt.dto.request.update.PromptTemplateUpdateRequestDto;
import com.zodiakk.intelligentservice.prompt.dto.response.PromptTemplateResponseDto;
import com.zodiakk.intelligentservice.prompt.entity.PromptTemplate;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface PromptTemplateMapper {

    PromptTemplate toEntity(PromptTemplateCreateRequestDto dto);

    PromptTemplateResponseDto toResponseDto(PromptTemplate entity);

    void updateEntity(PromptTemplateUpdateRequestDto dto, @MappingTarget PromptTemplate entity);
}
