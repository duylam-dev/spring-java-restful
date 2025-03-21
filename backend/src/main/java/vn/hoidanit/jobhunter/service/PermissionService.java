package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.util.Mapper.PermissionMapper;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public Permission handleCreate(Permission permission) throws IdInvalidException {
        if (permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule()))
            throw new IdInvalidException("permission existed!");

        return permissionRepository.save(permission);
    }

    public Permission fetchById(long id) throws IdInvalidException {
        return permissionRepository.findById(id).orElseThrow(() -> new IdInvalidException("permission not exist"));
    }

    public Permission handleUpdate(Permission permission) throws IdInvalidException {
        var permissionDB = fetchById(permission.getId());
        // convert
        if (permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule()))
            throw new IdInvalidException("permission existed!");
        permissionMapper.update(permissionDB, permission);
        return permissionRepository.save(permissionDB);
    }

    public ResPaginationDTO fetchAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                permissionPage.getTotalPages(),
                permissionPage.getTotalElements());
        rp.setMeta(mt);
        rp.setResult(permissionPage.getContent());
        return rp;
    }

}
