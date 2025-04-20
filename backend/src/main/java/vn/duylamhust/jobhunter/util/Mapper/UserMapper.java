package vn.duylamhust.jobhunter.util.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.duylamhust.jobhunter.domain.User;
import vn.duylamhust.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.duylamhust.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.duylamhust.jobhunter.domain.response.user.ResUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResCreateUserDTO toCreateDTO(User user);

    ResUpdateUserDTO toUpdateDTO(User user);

    ResUserDTO toUserDTO(User user);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateUser(@MappingTarget User userDB, User userNew);

}
