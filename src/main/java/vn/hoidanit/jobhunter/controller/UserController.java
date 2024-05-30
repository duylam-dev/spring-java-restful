package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User postManUser) {

        return userService.handleCreate(postManUser);

    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.handleDelete(id);
        return "user delete";

    }

    @GetMapping("/user/{id}")
    public User findUserById(@PathVariable("id") long id) {
        return userService.handleFindUserById(id);
    }

    @GetMapping("/user")
    public List<User> findAllUser() {
        return userService.handleFindAllUser();
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        return userService.handleUpdate(user);
    }
}
