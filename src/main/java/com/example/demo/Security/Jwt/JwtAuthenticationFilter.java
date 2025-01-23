package com.example.demo.Security.Jwt;

import com.example.demo.Security.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationService authenticationService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationService = authenticationService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + header); // Log inicial do cabeçalho

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("Token extraído: " + token); // Log do token extraído

            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsername(token);
                System.out.println("Usuário autenticado: " + username); // Log do usuário do token

                UserDetails userDetails = authenticationService.loadUserByUsername(username);
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Token inválido ou expirado."); // Log de token inválido
            }
        } else {
            System.out.println("Cabeçalho Authorization ausente ou incorreto."); // Log de cabeçalho ausente
        }

        filterChain.doFilter(request, response);
    }
}
