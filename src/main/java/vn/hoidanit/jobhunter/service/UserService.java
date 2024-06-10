package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.Mapper.UserMapper;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    public ResCreateUserDTO handleCreate(User user) throws IdInvalidException {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new IdInvalidException("email: " + user.getEmail() + "existed, please use other email!");

        user.setPassword(encoder.encode(user.getPassword()));

        return userMapper.toCreateDTO(userRepository.save(user));
    }

    public void handleDelete(long id) throws IdInvalidException {
        userRepository.findById(id).orElseThrow(() -> new IdInvalidException("user with id =" + id + "not exits"));
        userRepository.deleteById(id);
    }

    public ResUserDTO handleFindUserById(long id) throws IdInvalidException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("user with id =" + id + " not exits"));
        return userMapper.toUserDTO(user);
    }

    public User handleFindUserByUserName(String username) {
        return userRepository.findByEmail(username);
    }

    public ResPaginationDTO handleFindAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                userPage.getTotalPages(),
                userPage.getTotalElements());
        rp.setMeta(mt);

        List<ResUserDTO> ans = userPage.getContent().stream().map(item -> userMapper.toUserDTO(item))
                .collect(Collectors.toList());
        rp.setResult(ans);

        return rp;
    }

    public ResUpdateUserDTO handleUpdate(User user) throws IdInvalidException {
        userRepository.findById(user.getId()).orElseThrow(() -> new IdInvalidException("user not exist"));
        if (user.getPassword() != null)
            user.setPassword(encoder.encode(user.getPassword()));
        var res = new ResUpdateUserDTO();
        userMapper.updateUser(res, userRepository.save(user));
        return res;
    }
}
