package com.example.demo.Security.service;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Security.Jwt.JwtTokenProvider;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {



    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;



    public AuthenticationService(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = (User) userService.getUser(username);
        if (user !=  null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getName())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }



    public String authenticate(String username, String password) {
        User user = (User) userService.getUser(username);
        if (user !=  null) {
            if (passwordEncoder().matches(password, user.getPassword())) {
                // Gera o token e retorna
                return jwtTokenProvider.generateToken(user.getName(), user.getRoles());
            }
        }
        throw new UsernameNotFoundException("Usuário ou senha inválidos");
    }

    public String register(UserDTO userDTO) {
        try {
            userDTO.setPassword(passwordEncoder().encode(userDTO.getPassword()));
            User user = (User) userService.save(userDTO);
          return "User criado "+ user.getName()+ "com sucesso";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
