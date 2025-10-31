package com.strhzy.api.controller;

import com.strhzy.api.service.CustomerService;
import com.strhzy.api.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

class LoginResponse {
    public String token;
    public Object user;

    public LoginResponse(String token, Object user) {
        this.token = token;
        this.user = user;
    }
}

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomerService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, CustomerService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication auth = authenticationManager.authenticate(authInputToken);

        String token = jwtService.generateToken(auth.getName());

        LoginResponse response = new LoginResponse(token, userService.findByUsername(username).orElse(null));

        return response;
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> payload) {
        userService.register(payload.get("username"),payload.get("password"));
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }
}
