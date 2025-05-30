package vn.duylamhust.jobhunter.controller;

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

import vn.duylamhust.jobhunter.domain.User;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;
import vn.duylamhust.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.duylamhust.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.duylamhust.jobhunter.domain.response.user.ResUserDTO;
import vn.duylamhust.jobhunter.service.UserService;
import vn.duylamhust.jobhunter.util.anotation.ApiMessage;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.PutMapping;

@RestController

public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ApiMessage("create user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User postManUser) throws IdInvalidException {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.handleCreate(postManUser));

    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        userService.handleDelete(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("find user by id")
    public ResponseEntity<ResUserDTO> findUserById(@PathVariable("id") long id) throws IdInvalidException {
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
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        return ResponseEntity.ok(userService.handleUpdate(user));
    }

}
