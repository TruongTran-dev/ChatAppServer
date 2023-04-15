package com.kma.project.chatapp.mapper;

import com.kma.project.chatapp.dto.request.cms.NewRequestDto;
import com.kma.project.chatapp.dto.response.cms.NewResponseDto;
import com.kma.project.chatapp.entity.NewEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface NewMapper {

    NewEntity convertToEntity(NewRequestDto dto);

    NewResponseDto convertToDto(NewEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    NewEntity update(NewRequestDto dto, @MappingTarget NewEntity entity);
}
