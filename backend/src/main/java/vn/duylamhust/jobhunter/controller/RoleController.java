package vn.duylamhust.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.duylamhust.jobhunter.domain.Role;

import lombok.RequiredArgsConstructor;
import vn.duylamhust.jobhunter.service.RoleService;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;

@RestController
@RequiredArgsConstructor
public class RoleController extends BaseController {
    private final RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<Role> create(@RequestBody Role role) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.handleCreate(role));
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> update(@RequestBody Role role) throws IdInvalidException {
        return ResponseEntity.ok(roleService.handleUpdate(role));
    }

    @GetMapping("/roles")
    public ResponseEntity<ResPaginationDTO> findAll(
            @Filter Specification<Role> spec, Pageable pageable) {

        return ResponseEntity.ok(roleService.fetchAllPermission(spec, pageable));
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> findById(@PathVariable(name = "id") long id) throws IdInvalidException {

        return ResponseEntity.ok(roleService.findById(id));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
        roleService.delete(id);
        return ResponseEntity.ok(null);
    }
}
