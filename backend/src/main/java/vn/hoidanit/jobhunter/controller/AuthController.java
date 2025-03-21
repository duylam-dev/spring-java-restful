package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {

        private final SecurityUtil securityUtil;

        @PostMapping("/auth/login")
        @ApiMessage("login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
                var res = securityUtil.authentication(loginDTO);
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, res.getSecond().toString())
                                .body(res.getFirst());
        }

        @GetMapping("/auth/account")
        @ApiMessage("fetch account")
        public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
                var userLogin = securityUtil.getAccount();
                return ResponseEntity.ok().body(userLogin);
        }

        @GetMapping("/auth/refresh")
        @ApiMessage("get user by refresh token")
        public ResponseEntity<ResLoginDTO> getRefreshToken(
                        @CookieValue(name = "refresh_token", defaultValue = "error") String refresh_token)
                        throws IdInvalidException {
                var res = securityUtil.refreshToken(refresh_token);

                return ResponseEntity.ok(res);
        }

        @PostMapping("/auth/logout")
        @ApiMessage("logout")
        public ResponseEntity<Void> logout() throws IdInvalidException {
                var cookie = securityUtil.logout();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(null);
        }

        @PostMapping("/auth/register")
        public ResponseEntity<ResCreateUserDTO> register(@RequestBody User user) throws IdInvalidException {
                return ResponseEntity.status(HttpStatus.CREATED).body(securityUtil.registerUser(user));
        }

}
