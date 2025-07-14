package com.calyma.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.calyma.backend.repository.UserRepository;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.calyma.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, SimpleJwtFilter simpleJwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/status", "/test", "/setup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/productos/**", "/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/carrito/**").permitAll()
                        .requestMatchers("/pedidos/**", "/reseñas/**").hasAnyAuthority("USER", "ADMIN")
                        // SOLO ADMIN puede crear, actualizar o eliminar productos:
                        .requestMatchers(HttpMethod.POST, "/productos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/productos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(simpleJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("application/json");
                        Map<String, Object> body = new HashMap<>();
                        body.put("error", "No autenticado");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json");
                        Map<String, Object> body = new HashMap<>();
                        body.put("error", "Acceso denegado");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8082");
        configuration.addAllowedOrigin("http://127.0.0.1:8082");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SimpleJwtFilter simpleJwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new SimpleJwtFilter(jwtService, userDetailsService);
    }

}