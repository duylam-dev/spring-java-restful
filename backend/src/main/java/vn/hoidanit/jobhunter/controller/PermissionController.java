package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.hoidanit.jobhunter.domain.Permission;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PermissionController extends BaseController {
    private final PermissionService permissionService;

    @PostMapping("/permissions")
    public ResponseEntity<Permission> create(@RequestBody Permission permission) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.handleCreate(permission));
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permission> update(@RequestBody Permission permission) throws IdInvalidException {
        return ResponseEntity.ok(permissionService.handleUpdate(permission));
    }

    @GetMapping("/permissions")
    public ResponseEntity<ResPaginationDTO> findAllCompany(
            @Filter Specification<Permission> spec, Pageable pageable) {

        return ResponseEntity.ok(permissionService.fetchAllPermission(spec, pageable));
    }
}
