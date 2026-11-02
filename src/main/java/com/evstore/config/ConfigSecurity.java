package com.evstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// tell Spring this class holds setup beans for our app
@Configuration
public class ConfigSecurity {

    // bean to hash passwords with BCrypt so we dont store plaintext
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //configure basic security rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // disable csrf for easy postman / form testing
        http.csrf(csrf -> csrf.disable())
            // disable frame options so the H2 console can actually render in the browser
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            // permit all incoming requests, well handle our own auth checks in the controller/session
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            
        return http.build();
    }
}

