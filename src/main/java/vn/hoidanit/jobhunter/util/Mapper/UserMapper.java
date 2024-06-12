package vn.hoidanit.jobhunter.util.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResCreateUserDTO toCreateDTO(User user);

    ResUserDTO toUserDTO(User user);

    void updateUser(@MappingTarget ResUpdateUserDTO updateUserDTO, User user);

}
