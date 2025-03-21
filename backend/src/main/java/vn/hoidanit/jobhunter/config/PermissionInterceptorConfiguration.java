package vn.hoidanit.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    public final String[] PUBLIC_ENDPOINT = {
            "/", "/api/v1/auth/login", "/api/v1/auth/refresh",
            "/storage/**", "/api/v1/auth/register", "/api/v1/email", " /api/v1/files","/api/v1/auth/logout","/api/v1/companies/**",
            "/api/v1/jobs/**","/api/v1/skills/**"

    };
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(PUBLIC_ENDPOINT);
    }
}
