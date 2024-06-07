package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import vn.hoidanit.jobhunter.util.error.anotation.ApiMessage;

import org.springframework.web.bind.annotation.PutMapping;

@RestController

public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ApiMessage("create user")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.handleCreate(postManUser));

    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("id must be < 1500");
        }
        userService.handleDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    @ApiMessage("find user by id")
    public ResponseEntity<User> findUserById(@PathVariable("id") long id) {
        return ResponseEntity.ok(userService.handleFindUserById(id));
    }

    @GetMapping("/users")
    @ApiMessage("find all user")
    public ResponseEntity<ResPaginationDTO> findAllUser(
            @Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.ok(userService.handleFindAllUser(spec, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("update user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.handleUpdate(user));
    }
}
