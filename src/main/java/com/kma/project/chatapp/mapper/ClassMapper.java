package com.kma.project.chatapp.mapper;

import com.kma.project.chatapp.dto.request.cms.ClassRequestDto;
import com.kma.project.chatapp.dto.response.cms.ClassResponseDto;
import com.kma.project.chatapp.entity.ClassEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    ClassEntity convertToEntity(ClassRequestDto dto);

    ClassResponseDto convertToDto(ClassEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClassEntity update(ClassRequestDto dto, @MappingTarget ClassEntity entity);
}
