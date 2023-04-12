package com.kma.project.chatapp.mapper;

import com.kma.project.chatapp.dto.response.UserOutputDto;
import com.kma.project.chatapp.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserOutputDto convertToDto(UserEntity userEntity);

}
