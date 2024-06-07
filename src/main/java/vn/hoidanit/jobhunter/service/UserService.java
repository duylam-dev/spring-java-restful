package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResPaginationDTO;
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

    public ResPaginationDTO handleFindAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(userPage.getTotalPages());
        mt.setTotal(userPage.getTotalElements());

        rp.setMeta(mt);
        rp.setResult(userPage.getContent());

        return rp;
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
