package com.example.turnos_medicos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/turnos").permitAll()
                .anyRequest().authenticated()
            )
            //.formLogin(Customizer.withDefaults());   // autenticación/inicio sesion por el formularios
            .httpBasic(Customizer.withDefaults());     // autenticación/inicio sesión básica HTTP (cos postman)

        return http.build();
    }
}