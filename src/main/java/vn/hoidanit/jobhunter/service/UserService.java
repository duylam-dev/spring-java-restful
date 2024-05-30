package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreate(User user) {
        return userRepository.save(user);
    }

    public void handleDelete(long id) {
        userRepository.deleteById(id);
    }

    public User handleFindUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> handleFindAllUser() {
        return userRepository.findAll();
    }

    public User handleUpdate(User user) {
        User oldUser = userRepository.findById(user.getId()).orElse(null);
        if (oldUser != null) {
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.setPassword(user.getPassword());
            return userRepository.save(oldUser);
        }
        return null;

    }
}
