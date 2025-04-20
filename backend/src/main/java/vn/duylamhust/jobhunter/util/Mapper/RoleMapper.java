package vn.duylamhust.jobhunter.util.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.duylamhust.jobhunter.domain.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void update(@MappingTarget Role permissionDB, Role newPermission);
}
