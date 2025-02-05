package com.example.demo.Configuration;


import com.example.demo.Security.Jwt.JwtAuthenticationEntryPoint;
import com.example.demo.Security.Jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {



   private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/js/**","/api/auth/**", "register.html").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .anyRequest().authenticated() // Require authentication for other requests
                )//.exceptionHandling(AbstractHttpConfigurer::disable)
               // .exceptionHandling(ex -> ex
               //       .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // Custom authentication error handling
               // )
                .httpBasic(httpBasic -> {})
                .formLogin(login -> login
                        .loginPage("/login.html")
                        .permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before UsernamePasswordAuthenticationFilter
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
