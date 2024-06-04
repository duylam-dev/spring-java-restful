package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User handleCreate(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void handleDelete(long id) {
        userRepository.deleteById(id);
    }

    public User handleFindUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User handleFindUserByUserName(String username) {
        return userRepository.findByEmail(username);
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
