package com.strhzy.finalpr.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthRepository {

    private static final String BASE_PATH = "/auth";
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, String> login(String username, String password) {
        try {
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", username);
            loginRequest.put("password", password);

            return HttpService.post(BASE_PATH + "/login", loginRequest, Map.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при авторизации", e);
        }
    }

    public String register(String username, String password) {
        try {
            Map<String, String> registerRequest = new HashMap<>();
            registerRequest.put("username", username);
            registerRequest.put("password", password);

            return HttpService.post(BASE_PATH + "/register", registerRequest, String.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при регистрации", e);
        }
    }

    public void logout() {
        HttpService.clearToken();
    }
}