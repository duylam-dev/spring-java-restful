package vn.duylamhust.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.duylamhust.jobhunter.repository.PermissionRepository;
import vn.duylamhust.jobhunter.repository.RoleRepository;
import vn.duylamhust.jobhunter.domain.Permission;
import vn.duylamhust.jobhunter.domain.Role;
import vn.duylamhust.jobhunter.util.Mapper.RoleMapper;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public Role handleCreate(Role role) throws IdInvalidException {
        // check name
        if (roleRepository.existsByName(role.getName()))
            throw new IdInvalidException("role existed!");
        // check permission
        // get id
        List<Long> permission_id = role.getPermissions().stream().map(item -> item.getId())
                .collect(Collectors.toList());
        // check id exist
        List<Permission> permissions = permissionRepository.findAllById(permission_id);
        role.setPermissions(permissions);

        return roleRepository.save(role);
    }

    public Role handleUpdate(Role role) throws IdInvalidException {
        var roleDb = roleRepository.findById(role.getId()).orElseThrow(() -> new IdInvalidException("role not exist!"));
        List<Long> permission_id = role.getPermissions().stream().map(item -> item.getId())
                .collect(Collectors.toList());
        // check id exist
        List<Permission> permissions = permissionRepository.findAllById(permission_id);
        roleDb.setPermissions(permissions);
        roleMapper.update(roleDb, role);
        return roleRepository.save(roleDb);
    }

    public ResPaginationDTO fetchAllPermission(Specification<Role> spec, Pageable pageable) {
        Page<Role> rolePage = roleRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                rolePage.getTotalPages(),
                rolePage.getTotalElements());
        rp.setMeta(mt);
        rp.setResult(rolePage.getContent());
        return rp;
    }

    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    public Role findById(long id) throws IdInvalidException {
        return roleRepository.findById(id).orElseThrow(() -> new IdInvalidException("role not exist!"));
    }
}
