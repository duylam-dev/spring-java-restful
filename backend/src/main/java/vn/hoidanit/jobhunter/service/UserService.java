package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUserDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.Mapper.UserMapper;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final CompanyRepository companyRepository;
    private final RoleRepository repository;

    public ResCreateUserDTO handleCreate(User user) throws IdInvalidException {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new IdInvalidException("email: " + user.getEmail() + " existed, please use other email!");

        user.setPassword(encoder.encode(user.getPassword()));
        var company = user.getCompany();
        if (company != null) {
            company = companyRepository.findById(user.getCompany().getId()).orElseThrow(
                    () -> new IdInvalidException("company not exist"));
        }
        var role = user.getRole();
        if (role != null) {
            role = repository.findById(role.getId()).orElseThrow(
                    () -> new IdInvalidException("role not exist"));
        }

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
        return userRepository.findByEmail(username).orElseGet(null);
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
        var userDB = userRepository.findById(user.getId()).orElseThrow(() -> new IdInvalidException("user not found"));
        if (userDB.getEmail() != user.getEmail()) {
            if (userRepository.existsByEmail(user.getEmail()))
                throw new IdInvalidException("email existed!");
        }
        if (user.getPassword() != null)
            user.setPassword(encoder.encode(user.getPassword()));

        var company = user.getCompany();
        if (company != null) {
            company = companyRepository.findById(user.getCompany().getId()).orElseThrow(
                    () -> new IdInvalidException("company not exist"));
        }

        var role = user.getRole();
        if (role != null) {
            role = repository.findById(role.getId()).orElseThrow(
                    () -> new IdInvalidException("role not exist"));
        }
        user.setRole(role);
        user.setCompany(company);
        userMapper.updateUser(userDB, user);

        return userMapper.toUpdateDTO(userRepository.save(userDB));
    }

    public void updateRefreshToken(String email, String token) {
        var currentUser = handleFindUserByUserName(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            userRepository.save(currentUser);
        }

    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public boolean checkExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
