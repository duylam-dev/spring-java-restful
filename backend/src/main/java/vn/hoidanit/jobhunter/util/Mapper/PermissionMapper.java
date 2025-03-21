package vn.hoidanit.jobhunter.util.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.hoidanit.jobhunter.domain.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void update(@MappingTarget Permission permissionDB, Permission newPermission);
}
