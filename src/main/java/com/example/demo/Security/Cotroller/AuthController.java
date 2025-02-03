package com.example.demo.Security.Cotroller;


import com.auth0.jwt.JWT;
import com.example.demo.Controller.LoginRequest;
import com.example.demo.Security.Jwt.JwtTokenProvider;
import com.example.demo.Security.service.AuthenticationService;
import com.example.demo.Security.service.TokenBlackList;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthenticationService authenticationService;
    private final TokenBlackList tokenBlackList;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthController(AuthenticationService authenticationService, TokenBlackList tokenBlackList, JwtTokenProvider jwtTokenProvider) {
        this.authenticationService = authenticationService;
        this.tokenBlackList = tokenBlackList;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

            String token = authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            // Cria o cookie
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true) // Somente para HTTPS
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 dias
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("redirectUrl", "/homepage.html");

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){

        Cookie ck = Arrays.stream(request.getCookies()).filter(c -> "jwt".equals(c.getName())).findFirst().orElse(null);

        if(ck != null){
            String token = ck.getValue();
            long expirationTime = jwtTokenProvider.getExpirationTime(token).getTime() - System.currentTimeMillis();
            tokenBlackList.addToBlacklist(token,expirationTime);
        }

        ResponseCookie cookie =  ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true) // Somente para HTTPS
                .path("/")
                .maxAge(0) // 7 dias
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        SecurityContextHolder.clearContext();
        return  ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/login.html")
                .build();
    }


}
