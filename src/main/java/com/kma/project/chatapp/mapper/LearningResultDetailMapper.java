package com.kma.project.chatapp.mapper;

import com.kma.project.chatapp.dto.request.cms.LearningResultDetailRequestDto;
import com.kma.project.chatapp.dto.response.cms.LearningResultDetailResponseDto;
import com.kma.project.chatapp.entity.LearningResultDetailEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LearningResultDetailMapper {

    LearningResultDetailResponseDto convertToDto(LearningResultDetailEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LearningResultDetailEntity update(LearningResultDetailRequestDto dto, @MappingTarget LearningResultDetailEntity entity);
}
