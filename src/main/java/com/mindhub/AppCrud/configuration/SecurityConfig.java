package com.mindhub.AppCrud.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/login", "/api/students", "/api/teachers").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/**", "/api/schedules",
                                "/api/schedules/courses").authenticated()
                .requestMatchers(HttpMethod.GET, "/h2-console/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/persons", "/api/students", "/api/teachers",
                                "/api/students/first-name/containing/{letter}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/admin", "/api/schedules").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/teacher/current").hasAuthority("TEACHER")
                .requestMatchers(HttpMethod.PATCH, "/api/teachers/current/remove/courses").hasAuthority("TEACHER")
                .requestMatchers(HttpMethod.GET, "/api/students/current").hasAuthority("STUDENT")
                .requestMatchers(HttpMethod.PATCH, "/api/students/current/add/courses",
                                "/api/students/current/remove/courses").hasAuthority("STUDENT")
                .anyRequest().denyAll());

        http.csrf(csrf -> csrf.disable());

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> response.sendError(403)));

        http.formLogin(login -> login
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/api/login")
                .successHandler((request, response, authentication) -> clearAuthenticationAttributes(request))
                .failureHandler((request, response, exception) -> response.sendError(401)));

        // Este ajuste permite a los usuarios permanecer autenticados incluso después de cerrar el navegador
        http.rememberMe(Customizer.withDefaults());

        http.logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .deleteCookies("JSESSIONID"));

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
