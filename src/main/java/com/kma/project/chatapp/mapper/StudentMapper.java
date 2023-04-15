package com.kma.project.chatapp.mapper;

import com.kma.project.chatapp.dto.request.cms.StudentRequestDto;
import com.kma.project.chatapp.dto.response.cms.StudentResponseDto;
import com.kma.project.chatapp.entity.StudentEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentEntity convertToEntity(StudentRequestDto dto);

    StudentResponseDto convertToDto(StudentEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    StudentEntity update(StudentRequestDto dto, @MappingTarget StudentEntity entity);
}
