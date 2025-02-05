package com.example.demo.Security.Jwt;

import com.example.demo.Security.service.AuthenticationService;
import com.example.demo.Security.service.TokenBlackList;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationService authenticationService;
    private final TokenBlackList tokenBlackList;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationService authenticationService, TokenBlackList tokenBlackList) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationService = authenticationService;
        this.tokenBlackList = tokenBlackList;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        if (token == null && request.getCookies() != null) {
            Cookie jwtCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            if(jwtCookie != null) {
                token = jwtCookie.getValue();
            }
        }



        if (token != null) {
            if (jwtTokenProvider.validateToken(token) && !tokenBlackList.isBlacklisted(token)) {
                String username = jwtTokenProvider.getUsername(token);
                System.out.println("Usuário autenticado: " + username);

                UserDetails userDetails = authenticationService.loadUserByUsername(username);
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Configura o contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                sendErrorResponse(response,HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
            }
        }

        // 4. Continue a cadeia de filtros
        filterChain.doFilter(request, response);
    }


    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) { // ⬅️ Garante que a resposta não foi enviada antes
            response.setContentType("application/json");
            response.setStatus(status);
            response.getWriter().write("{\"error\": \"" + message + "\"}");
            response.getWriter().flush();
        }
    }
}
