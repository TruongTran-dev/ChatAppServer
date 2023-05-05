package com.kma.project.chatapp.mapper;

import com.kma.project.chatapp.dto.request.auth.UserUpdateDto;
import com.kma.project.chatapp.dto.response.auth.UserOutputDto;
import com.kma.project.chatapp.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserOutputDto convertToDto(UserEntity userEntity);

    @Mapping(target = "roles", source = "roles", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity update(UserUpdateDto dto, @MappingTarget UserEntity entity);

}
