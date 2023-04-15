package com.kma.project.chatapp.mapper;

import com.kma.project.chatapp.dto.request.cms.SubjectRequestDto;
import com.kma.project.chatapp.dto.response.cms.SubjectResponseDto;
import com.kma.project.chatapp.entity.SubjectEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectEntity convertToEntity(SubjectRequestDto dto);

    SubjectResponseDto convertToDto(SubjectEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SubjectEntity update(SubjectRequestDto dto, @MappingTarget SubjectEntity entity);
}
