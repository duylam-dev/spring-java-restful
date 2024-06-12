package vn.hoidanit.jobhunter.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${hoidanit.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;
    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public ResLoginDTO refreshToken(String refreshToken) throws IdInvalidException {
        if (refreshToken.equals("error"))
            throw new IdInvalidException("cookie haven't refresh token");
        // check valid
        var decodedToken = jwtDecoder.decode(refreshToken);
        // fetch email from refresh token
        String email = decodedToken.getSubject();
        User currentUser = userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null)
            throw new IdInvalidException("refresh token khong hop le");
        // issue new token and set refresh token as cookie
        var userLogin = setInfoUserResponse(email);
        // create new access token
        String accessToken = generateToken(email, userLogin, false);
        return new ResLoginDTO(accessToken, userLogin.getUser());
    }

    public ResLoginDTO authentication(LoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // save to spring security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var res = new ResLoginDTO();
        var userLogin = setInfoUserResponse(loginDTO.getUsername());
        res.setUser(userLogin.getUser());
        // create access token
        String accessToken = generateToken(authentication.getName(), userLogin, false);
        res.setAccessToken(accessToken);
        // create refresh token
        String refresh_token = generateToken(loginDTO.getUsername(), userLogin, true);
        // save to database
        userService.updateRefreshToken(loginDTO.getUsername(), refresh_token);
        return res;
    }

    public ResLoginDTO.UserGetAccount getAccount() {
        String email = getCurrentUserLogin().isPresent() ? getCurrentUserLogin().get() : "";
        return setInfoUserResponse(email);
    }

    public void logout() throws IdInvalidException {
        String email = getCurrentUserLogin().isPresent() ? getCurrentUserLogin().get() : "";
        if (email.equals(""))
            throw new IdInvalidException("access token khong hop le");
        userService.updateRefreshToken(email, null);

    }

    private String generateToken(String email, ResLoginDTO.UserGetAccount user, boolean refresh) {
        Instant now = Instant.now();
        Instant validity = refresh ? now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS)
                : now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(email)
            .claim("user", user)
            .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claims)).getTokenValue();
    
    }
    private ResLoginDTO.UserGetAccount setInfoUserResponse(String email){
        User currentUserDB = userService.handleFindUserByUserName(email);
        var userLogin = new ResLoginDTO.UserLogin();
        if (currentUserDB != null) {
            userLogin.setEmail(email);
            userLogin.setId(currentUserDB.getId());
            userLogin.setName(currentUserDB.getName());
        }
        return new ResLoginDTO.UserGetAccount(userLogin);
    }




    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }


    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    // public static boolean isAuthenticated() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    // }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     return (
    //         authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
    //     );
    // }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
    //     return !hasCurrentUserAnyOfAuthorities(authorities);
    // }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    // public static boolean hasCurrentUserThisAuthority(String authority) {
    //     return hasCurrentUserAnyOfAuthorities(authority);
    // }

    // private static Stream<String> getAuthorities(Authentication authentication) {
    //     return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    // }

}
