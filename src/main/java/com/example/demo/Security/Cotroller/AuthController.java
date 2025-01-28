package com.example.demo.Security.Cotroller;


import com.example.demo.Controller.LoginRequest;
import com.example.demo.Security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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


}
