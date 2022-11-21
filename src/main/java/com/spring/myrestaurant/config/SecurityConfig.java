package com.spring.myrestaurant.config;

import com.spring.myrestaurant.jwt.JwtTokenVerifierFilter;
import com.spring.myrestaurant.jwt.JwtUsernamePasswordAuthenticationFilter;
import com.spring.myrestaurant.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtTokenVerifierFilter jwtTokenVerifierFilter = new JwtTokenVerifierFilter(tokenProvider);
        JwtUsernamePasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter =
                new JwtUsernamePasswordAuthenticationFilter(
                        authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                        tokenProvider
                );
        jwtUsernameAndPasswordAuthenticationFilter.setFilterProcessesUrl("/api/v1/users/login");

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtTokenVerifierFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtUsernameAndPasswordAuthenticationFilter)
                .authorizeHttpRequests((authz) -> {
                            try {
                                authz
                                        .antMatchers(HttpMethod.POST, "/api/v1/users/login/**", "/api/v1/users/signup/**").permitAll()
                                        .antMatchers(HttpMethod.POST, "/api/v1/receipts").hasAuthority("ROLE_USER")
                                        .antMatchers(HttpMethod.GET, "/api/v1/dishes/**", "/api/v1/categories/**",
                                                "/api/v1/receipts/user").hasAnyAuthority( "ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER")
                                        .antMatchers( "/api/v1/dishes/**", "/api/v1/categories/**", "/api/v1/statuses/**",
                                                "/api/v1/receipts/**").hasAnyAuthority( "ROLE_ADMIN", "ROLE_MANAGER")
                                        .antMatchers(HttpMethod.PATCH, "/api/v1/users/cart/add/**", "/api/v1/users/cart/remove/**").hasAuthority("ROLE_USER")
                                        .antMatchers(HttpMethod.PUT, "/api/v1/users").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER")
                                        .antMatchers("/api/v1/**").hasAuthority("ROLE_ADMIN")
                                        .anyRequest().authenticated();
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/h2-console/**", "/v2/api-docs", "/swagger-resources/configuration/ui",
                "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html",
                "/webjars/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/vendor/**",
                "/fonts/**");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
